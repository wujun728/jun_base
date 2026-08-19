import { AuthStorage } from "@/utils/auth";

/** SSE 连接状态 */
export const SseConnectionState = {
  DISCONNECTED: "DISCONNECTED",
  CONNECTING: "CONNECTING",
  CONNECTED: "CONNECTED",
};

let globalInstance = null;

function createSseConnection(options = {}) {
  const baseUrl = import.meta.env.VITE_APP_BASE_API;
  const defaultUrl = `${baseUrl}/api/v1/sse/connect`;

  const config = {
    /** SSE 连接地址，默认走 VITE_APP_BASE_API 代理 */
    url: options.url ?? defaultUrl,
    /** 是否在控制台打印调试日志 */
    debug: options.debug ?? false,
    /** 连接超时时间（ms），默认 10000 */
    connectionTimeout: options.connectionTimeout ?? 10000,
    /** 重连间隔基数（ms），实际间隔 = min(基数 × 2^n, maxReconnectInterval) */
    reconnectInterval: options.reconnectInterval ?? 5000,
    /** 重连间隔上限（ms），默认 120000 */
    maxReconnectInterval: options.maxReconnectInterval ?? 120000,
    /** 最大重试次数，超过后停止重连，默认 10 */
    maxReconnectAttempts: options.maxReconnectAttempts ?? 10,
  };

  const connectionState = ref(SseConnectionState.DISCONNECTED);
  const isConnected = computed(() => connectionState.value === SseConnectionState.CONNECTED);

  let abortController = null;
  let connectionTimeoutTimer = null;
  let reader = null;
  /** 主动断开则不重连 */
  let isManualDisconnect = false;
  let reconnectTimer = null;
  let reconnectAttempts = 0;
  let currentReconnectInterval = config.reconnectInterval;

  const eventHandlers = new Map();

  const log = (...args) => {
    if (config.debug) {
      console.debug("[SSE]", ...args);
    }
  };
  const logError = (...args) => console.error("[SSE]", ...args);

  /** 清除定时器并返回 null */
  const clearTimer = (timer) => {
    if (timer) {
      clearTimeout(timer);
    }
    return null;
  };

  /** 重置重连状态：次数归零、间隔恢复基数 */
  const resetReconnectState = () => {
    reconnectAttempts = 0;
    currentReconnectInterval = config.reconnectInterval;
  };

  /** 指数退避：当前间隔翻倍，不超过上限 */
  const advanceReconnectState = () => {
    currentReconnectInterval = Math.min(currentReconnectInterval * 2, config.maxReconnectInterval);
  };

  /** 分发 SSE 事件：先尝试 JSON.parse，失败则传原始字符串 */
  const flushSseEvent = (eventName, data) => {
    if (!data) return;
    const handlers = eventHandlers.get(eventName);
    if (handlers) {
      try {
        const parsed = JSON.parse(data);
        handlers.forEach((handler) => handler(parsed));
      } catch {
        handlers.forEach((handler) => handler(data));
      }
    }
    log(`收到事件[${eventName}]:`, data);
  };

  /** 解析单行 SSE 数据：区分 event/data/注释/空行（触发分发） */
  const handleSseLine = (line, state) => {
    if (line.startsWith(":")) return;
    if (line.startsWith("event:")) {
      state.currentEvent = line.slice(6).trim() || "message";
      return;
    }
    if (line.startsWith("data:")) {
      const dataLine = line.slice(5).trim();
      state.currentData = state.currentData ? `${state.currentData}\n${dataLine}` : dataLine;
      return;
    }
    if (line === "") {
      flushSseEvent(state.currentEvent, state.currentData);
      state.currentEvent = "message";
      state.currentData = "";
    }
  };

  /** 持续读取流数据并按行解析，异常时触发重连 */
  const consumeSseStream = async (streamReader) => {
    const decoder = new TextDecoder();
    const state = { currentEvent: "message", currentData: "", buffer: "" };

    try {
      while (true) {
        const { done, value } = await streamReader.read();
        if (done) {
          reader = null;
          connectionState.value = SseConnectionState.DISCONNECTED;
          log("SSE 连接已关闭");
          return;
        }

        state.buffer += decoder.decode(value, { stream: true });
        const lines = state.buffer.split("\n");
        state.buffer = lines.pop() || "";

        for (const line of lines) {
          handleSseLine(line, state);
        }
      }
    } catch (err) {
      reader = null;
      connectionState.value = SseConnectionState.DISCONNECTED;
      if (err instanceof Error && err.name === "AbortError") {
        log("SSE 流读取已主动断开");
      } else {
        logError("SSE 流读取错误:", err);
        scheduleReconnect();
      }
    }
  };

  /** 调度重连：指数退避，达到上限或主动断开时停止 */
  const scheduleReconnect = () => {
    if (isManualDisconnect) return;
    if (config.maxReconnectAttempts > 0 && reconnectAttempts >= config.maxReconnectAttempts) {
      log(`已达到最大重试次数 ${config.maxReconnectAttempts}，停止重连`);
      return;
    }

    reconnectAttempts++;
    log(`将在 ${currentReconnectInterval}ms 后重试（${reconnectAttempts}）`);

    reconnectTimer = setTimeout(() => {
      advanceReconnectState();
      connect();
    }, currentReconnectInterval);
  };

  /** 建立连接：校验 token → fetch → 超时检测 → 消费流；401/403 不重连 */
  const connect = () => {
    isManualDisconnect = false;

    if (connectionState.value !== SseConnectionState.DISCONNECTED) {
      log(
        connectionState.value === SseConnectionState.CONNECTED
          ? "SSE 已连接，跳过重复连接"
          : "SSE 正在连接中，跳过重复连接"
      );
      return;
    }

    const token = AuthStorage.getAccessToken();
    if (!token) {
      log("未检测到有效令牌，稍后重试");
      reconnectTimer = setTimeout(() => connect(), config.reconnectInterval);
      return;
    }

    connectionState.value = SseConnectionState.CONNECTING;
    abortController = new AbortController();

    connectionTimeoutTimer = setTimeout(() => {
      if (connectionState.value === SseConnectionState.CONNECTING) {
        log("SSE 连接超时");
        disconnect();
      }
    }, config.connectionTimeout);

    log("正在建立 SSE 连接...");

    fetch(config.url, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
        Accept: "text/event-stream",
      },
      signal: abortController.signal,
    })
      .then((response) => {
        if (!response.ok) {
          if (response.status === 401 || response.status === 403) {
            isManualDisconnect = true;
            connectionState.value = SseConnectionState.DISCONNECTED;
            log(`SSE 连接被拒绝（HTTP ${response.status}），不再重连`);
            return null;
          }
          throw new Error(`HTTP ${response.status}`);
        }
        connectionTimeoutTimer = clearTimer(connectionTimeoutTimer);
        connectionState.value = SseConnectionState.CONNECTED;
        resetReconnectState();
        log("SSE 连接已建立");
        return response.body?.getReader();
      })
      .then((streamReader) => {
        if (!streamReader) return;
        reader = streamReader;
        return consumeSseStream(streamReader);
      })
      .catch((err) => {
        if (err instanceof Error && err.name === "AbortError") {
          log("SSE 连接已主动断开");
        } else {
          logError("SSE 连接错误:", err);
          connectionState.value = SseConnectionState.DISCONNECTED;
          scheduleReconnect();
        }
      });
  };

  /** 订阅指定事件，返回取消订阅函数 */
  const on = (eventName, handler) => {
    if (!eventHandlers.has(eventName)) {
      eventHandlers.set(eventName, new Set());
    }
    eventHandlers.get(eventName).add(handler);
    log(`已订阅事件: ${eventName}`);

    return () => {
      const handlers = eventHandlers.get(eventName);
      if (handlers) {
        handlers.delete(handler);
        if (handlers.size === 0) {
          eventHandlers.delete(eventName);
        }
      }
    };
  };

  /** 主动断开：清除定时器、取消流读取、中止请求，不触发重连 */
  const disconnect = () => {
    isManualDisconnect = true;
    connectionTimeoutTimer = clearTimer(connectionTimeoutTimer);
    reconnectTimer = clearTimer(reconnectTimer);
    reader?.cancel();
    reader = null;
    abortController?.abort();
    abortController = null;
    connectionState.value = SseConnectionState.DISCONNECTED;
    log("SSE 连接已断开");
  };

  /** 断开连接并清空所有事件订阅 */
  const cleanup = () => {
    disconnect();
    eventHandlers.clear();
    log("SSE 资源已清理");
  };

  return {
    connectionState: readonly(connectionState),
    isConnected,
    connect,
    disconnect,
    cleanup,
    on,
  };
}

/**
 * SSE 连接组合式函数（单例模式）
 *
 * 基于 fetch + ReadableStream 实现，支持指数退避重连、
 * 事件订阅/取消订阅、主动断开与资源清理。
 *
 * @param {object} options - 连接配置选项
 * @returns SSE 连接实例
 */
export function useSse(options = {}) {
  if (!globalInstance) {
    globalInstance = createSseConnection(options);
  }
  return globalInstance;
}

/** 清理 SSE 单例：断开连接、清空订阅、释放全局引用 */
export function cleanupSse() {
  if (globalInstance) {
    globalInstance.cleanup();
    globalInstance = null;
  }
}
