import { useDictSync } from "./useDictSync";
import { useOnlineUsers } from "./useOnlineUsers";
import { useSse, cleanupSse } from "./useSse";

/**
 * 初始化所有 SSE 服务
 */
export function setupSse() {
  const sse = useSse();
  sse.connect();

  const dictSync = useDictSync();
  dictSync.initialize();

  const onlineUsers = useOnlineUsers();
  onlineUsers.initialize();
}

/**
 * 清理所有 SSE 连接
 */
export function cleanupSseServices() {
  const dictSync = useDictSync();
  dictSync.cleanup();

  const onlineUsers = useOnlineUsers();
  onlineUsers.cleanup();

  cleanupSse();
}

export { useDictSync } from "./useDictSync";
export { useOnlineUsers } from "./useOnlineUsers";
export { useSse, cleanupSse, SseConnectionState } from "./useSse";
export { SseTopics } from "./sseTopics";
