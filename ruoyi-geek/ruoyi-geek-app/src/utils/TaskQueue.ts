import { nextTick } from "vue";

export type TaskFn<T = unknown> = (signal?: AbortSignal) => Promise<T>;
export type ErrorMode = 'continue' | 'abort';

export interface TaskQueueOptions {
  concurrency?: number; // 并发数，默认 4
  errorMode?: ErrorMode; // 出错策略：继续 or 中止
  autoStart?: boolean; // add 时是否自动启动，默认 true
}

export interface AddTaskOptions {
  priority?: number; // 优先级，数值越大越先执行，默认 0
  signal?: AbortSignal; // 任务级取消信号
  timeout?: number; // 单任务超时（ms）
  id?: string; // 任务标识，便于调试
}

class AbortError extends Error {
  name = 'AbortError';
  constructor(message = 'Aborted') {
    super(message);
  }
}

interface QueueItem<T = unknown> {
  id?: string;
  priority: number;
  fn: TaskFn<T>;
  resolve: (v: T) => void;
  reject: (e: unknown) => void;
  signal?: AbortSignal;
  timeout?: number;
  addedAt: number;
}

export class TaskQueue {
  /** 最大并发数 */
  private concurrency!: number; // 在构造函数中设置

  /** 当前正在执行的任务数 */
  private runningCount = 0;

  /** 待执行任务队列（使用 unknown 以避免泛型入队时的类型不兼容） */
  private taskQueue: QueueItem<unknown>[] = [];

  /** 出错策略 */
  private errorMode: ErrorMode = 'abort';

  /** 自动启动 */
  private autoStart: boolean = true;

  /** 是否暂停调度 */
  private paused = false;

  /** 是否已中止（如因错误或手动 abort） */
  private aborted = false;
  private abortReason?: string;

  /** 错误收集（errorMode=continue 时有用） */
  private errors: unknown[] = [];

  /** empty/idle 等待者 */
  private emptyWaiters: Array<() => void> = [];
  private idleWaiters: Array<() => void> = [];

  constructor(options: number | TaskQueueOptions = 4) {
    if (typeof options === 'number') {
      this.concurrency = Math.max(1, options);
      this.errorMode = 'abort';
      this.autoStart = true;
    } else {
      this.concurrency = Math.max(1, options.concurrency ?? 4);
      this.errorMode = options.errorMode ?? 'abort';
      this.autoStart = options.autoStart ?? true;
    }
  }

  // 状态只读属性
  get size() { return this.taskQueue.length; }
  get pending() { return this.runningCount; }
  get isPaused() { return this.paused; }
  get isAborted() { return this.aborted; }
  get collectedErrors() { return this.errors.slice(); }

  /**
   * 添加任务（必须是函数），返回该任务自身的 Promise。
   */
  public add<T>(fn: TaskFn<T>, opts: AddTaskOptions = {}): Promise<T> {
    return new Promise<T>((resolve, reject) => {
      if (this.aborted) {
        reject(new AbortError(this.abortReason || 'Queue aborted'));
        return;
      }

      const item: QueueItem<T> = {
        id: opts.id,
        priority: opts.priority ?? 0,
        fn,
        resolve,
        reject,
        signal: opts.signal,
        timeout: opts.timeout,
        addedAt: Date.now(),
      };

      this.enqueue(item as unknown as QueueItem<unknown>);
      if (this.autoStart && !this.paused) this.runNext();
    });
  }

  /** 批量添加 */
  public addAll<T>(fns: Array<TaskFn<T>>, opts?: AddTaskOptions): Promise<T>[] {
    return fns.map((fn) => this.add(fn, opts));
  }

  /** 等待队列空（无排队任务） */
  public onEmpty(): Promise<void> {
    if (this.size === 0) return Promise.resolve();
    return new Promise((resolve) => this.emptyWaiters.push(resolve));
  }

  /** 等待完全空闲（无排队、无运行中） => 等待所有任务完成 */
  public waitAll(): Promise<void> {
    if (this.size === 0 && this.runningCount === 0) return Promise.resolve();
    return new Promise((resolve) => this.idleWaiters.push(resolve));
  }

  /** 暂停调度 */
  public pause() { this.paused = true; }

  /** 恢复调度 */
  public resume() {
    if (!this.paused) return;
    this.paused = false;
    this.runNext();
  }

  /** 手动中止：清空剩余队列并拒绝它们 */
  public abort(reason = 'Aborted by user') {
    if (this.aborted) return;
    this.aborted = true;
    this.abortReason = reason;
    this.clear(new AbortError(reason));
  }

  /** 清空待执行任务（不影响已在运行中的任务） */
  public clear(err: unknown = new AbortError('Cleared')) {
    const pending = this.taskQueue.splice(0, this.taskQueue.length);
    for (const item of pending) item.reject(err);
    this.notifyEmptyIfNeeded();
    this.notifyIdleIfNeeded();
  }

  /** 动态调整并发度 */
  public setConcurrency(n: number) {
    this.concurrency = Math.max(1, n | 0);
    this.runNext();
  }

  /** 修改错误策略 */
  public setErrorMode(mode: ErrorMode) { this.errorMode = mode; }

  /** 入队（按优先级降序，稳定插入） */
  private enqueue(item: QueueItem<unknown>) {
    const idx = this.taskQueue.findIndex((q) => q.priority < item.priority);
    if (idx === -1) this.taskQueue.push(item);
    else this.taskQueue.splice(idx, 0, item);
  }

  /** 调度下一批任务（在下一帧启动） */
  private runNext() {
    if (this.paused || this.aborted) return;

    while (this.runningCount < this.concurrency && this.taskQueue.length > 0) {
      const item = this.taskQueue.shift()!;
      if (this.taskQueue.length === 0) this.notifyEmptyIfNeeded();

      this.runningCount++;
      nextTick(() => {
        this.execute(item)
          .catch(() => { /* 错误在 execute 中处理 */ })
          .finally(() => {
            this.runningCount--;
            if (!this.aborted) this.runNext();
            this.notifyIdleIfNeeded();
          });
      });
    }
  }

  /** 实际执行（处理 signal、timeout、错误策略） */
  private async execute(item: QueueItem<unknown>): Promise<void> {
    if (item.signal?.aborted) {
      item.reject(new AbortError('Task aborted before start'));
      return;
    }

    let timer: number | undefined;
    const onAbort = () => {
      if (timer) clearTimeout(timer);
      item.reject(new AbortError('Task aborted'));
    };
    if (item.signal) item.signal.addEventListener('abort', onAbort, { once: true });

    if (item.timeout && item.timeout > 0) {
      timer = window.setTimeout(() => {
        item.reject(new Error(`Task timeout after ${item.timeout}ms`));
      }, item.timeout);
    }

    try {
      const result = await item.fn(item.signal);
      if (timer) clearTimeout(timer);
      (item.resolve as (v: unknown) => void)(result);
    } catch (err) {
      if (timer) clearTimeout(timer);
      this.errors.push(err);
      item.reject(err);

      if (this.errorMode === 'abort' && !this.aborted) {
        this.aborted = true;
        this.abortReason = 'Aborted due to previous error';
        this.clear(err instanceof Error ? err : new Error(String(err)));
      }
    } finally {
      if (item.signal) item.signal.removeEventListener('abort', onAbort);
    }
  }

  private notifyEmptyIfNeeded() {
    if (this.taskQueue.length === 0 && this.emptyWaiters.length) {
      const callbacks = this.emptyWaiters.splice(0, this.emptyWaiters.length);
      for (const cb of callbacks) cb();
    }
  }

  private notifyIdleIfNeeded() {
    if (this.taskQueue.length === 0 && this.runningCount === 0 && this.idleWaiters.length) {
      const callbacks = this.idleWaiters.splice(0, this.idleWaiters.length);
      for (const cb of callbacks) cb();
    }
  }
}

export default TaskQueue;