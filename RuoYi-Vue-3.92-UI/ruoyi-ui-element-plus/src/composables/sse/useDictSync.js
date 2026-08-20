import { useDictStoreHook } from "@/stores/dict";
import { useSse } from "./useSse";

let singletonInstance = null;

function createDictSyncComposable() {
  const dictStore = useDictStoreHook();
  const sse = useSse();

  const messageCallbacks = ref([]);

  let unsubscribe = null;

  /** 处理字典变更消息：清除指定字典缓存，并通知所有已注册回调 */
  const handleDictChangeMessage = (data) => {
    const { dictCode } = data;

    if (!dictCode) {
      console.warn("[DictSync] 收到无效的字典变更消息：缺少 dictCode");
      return;
    }

    dictStore.removeDictItem(dictCode);

    messageCallbacks.value.forEach((callback) => {
      try {
        callback(data);
      } catch (error) {
        console.error("[DictSync] 回调函数执行失败:", error);
      }
    });
  };

  /** 订阅 SSE 字典变更事件 */
  const initialize = () => {
    unsubscribe = sse.on("dict", handleDictChangeMessage);
  };

  /** 取消 SSE 订阅并清空所有回调 */
  const cleanup = () => {
    if (unsubscribe) {
      unsubscribe();
      unsubscribe = null;
    }
    messageCallbacks.value = [];
  };

  /** 注册字典变更回调，返回取消注册函数 */
  const onDictChange = (callback) => {
    messageCallbacks.value.push(callback);

    return () => {
      const index = messageCallbacks.value.indexOf(callback);
      if (index !== -1) {
        messageCallbacks.value.splice(index, 1);
      }
    };
  };

  return {
    isConnected: sse.isConnected,
    connectionState: sse.connectionState,
    initialize,
    cleanup,
    onDictChange,
  };
}

/**
 * 字典同步组合式函数（单例模式）
 *
 * 监听 SSE 字典变更事件，收到变更时自动清除对应字典缓存，
 * 并通知所有已注册的回调函数。
 */
export function useDictSync() {
  if (!singletonInstance) {
    singletonInstance = createDictSyncComposable();
  }
  return singletonInstance;
}
