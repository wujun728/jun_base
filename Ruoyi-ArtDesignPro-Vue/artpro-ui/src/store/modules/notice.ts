import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import {
  fetchTopNotices,
  markAllNoticesRead,
  markNoticeRead,
  type NoticeType,
  type TopNotice
} from '@/api/system/notice'
import { useUserStore } from './user'

const NOTICE_TYPES: NoticeType[] = ['1', '2', '3']
const WS_RECONNECT_INTERVAL = 5_000
const WS_MAX_RECONNECT_ATTEMPTS = 12
const WS_REFRESH_DELAY = 300

interface NoticeWebSocketMessage {
  type?: string
}

export const useNoticeStore = defineStore('noticeStore', () => {
  const lists = ref<Record<NoticeType, TopNotice[]>>({ '1': [], '2': [], '3': [] })
  const unreadCounts = ref<Record<NoticeType, number>>({ '1': 0, '2': 0, '3': 0 })
  const loading = ref(false)
  let socket: WebSocket | undefined
  let reconnectTimer: ReturnType<typeof setTimeout> | undefined
  let refreshTimer: ReturnType<typeof setTimeout> | undefined
  let reconnectAttempts = 0
  let manuallyClosed = false

  const totalUnread = computed(() =>
    NOTICE_TYPES.reduce((total, type) => total + unreadCounts.value[type], 0)
  )

  const refresh = async (silent = false) => {
    if (loading.value) return
    loading.value = true
    try {
      const results = await Promise.all(NOTICE_TYPES.map((type) => fetchTopNotices(type, silent)))
      NOTICE_TYPES.forEach((type, index) => {
        lists.value[type] = results[index].rows
        unreadCounts.value[type] = results[index].unreadCount
      })
    } catch (error) {
      if (!silent) throw error
    } finally {
      loading.value = false
    }
  }

  const read = async (noticeId: number) => {
    await markNoticeRead(noticeId)
    await refresh(true)
  }

  const readAll = async () => {
    await markAllNoticesRead()
    await refresh(true)
  }

  const startRealtime = () => {
    stopRealtime()
    void refresh(true)
    connectWebSocket()
  }

  const stopRealtime = () => {
    disconnectWebSocket()
  }

  const connectWebSocket = () => {
    const { accessToken } = useUserStore()
    if (
      !accessToken ||
      socket?.readyState === WebSocket.OPEN ||
      socket?.readyState === WebSocket.CONNECTING
    ) {
      return
    }

    manuallyClosed = false
    socket = new WebSocket(buildWebSocketUrl(accessToken))
    socket.onopen = () => {
      reconnectAttempts = 0
    }
    socket.onmessage = (event) => handleWebSocketMessage(event)
    socket.onclose = () => {
      socket = undefined
      scheduleReconnect()
    }
    socket.onerror = () => {
      socket?.close()
    }
  }

  const disconnectWebSocket = () => {
    manuallyClosed = true
    if (reconnectTimer) {
      clearTimeout(reconnectTimer)
      reconnectTimer = undefined
    }
    if (refreshTimer) {
      clearTimeout(refreshTimer)
      refreshTimer = undefined
    }
    socket?.close()
    socket = undefined
    reconnectAttempts = 0
  }

  const scheduleReconnect = () => {
    if (manuallyClosed || reconnectTimer || reconnectAttempts >= WS_MAX_RECONNECT_ATTEMPTS) {
      return
    }
    reconnectAttempts += 1
    reconnectTimer = setTimeout(() => {
      reconnectTimer = undefined
      connectWebSocket()
    }, WS_RECONNECT_INTERVAL)
  }

  const handleWebSocketMessage = (event: MessageEvent) => {
    if (event.data === 'pong') return

    try {
      const message = JSON.parse(event.data) as NoticeWebSocketMessage
      if (message.type?.startsWith('notice:')) {
        scheduleRefresh()
      }
    } catch {
      // 非通知业务消息忽略。
    }
  }

  const scheduleRefresh = () => {
    if (refreshTimer) clearTimeout(refreshTimer)
    refreshTimer = setTimeout(() => {
      refreshTimer = undefined
      void refresh(true)
    }, WS_REFRESH_DELAY)
  }

  const buildWebSocketUrl = (token: string) => {
    const apiUrl = import.meta.env.VITE_API_URL || window.location.origin
    const url = new URL(apiUrl, window.location.origin)
    const basePath = url.pathname.replace(/\/$/, '')
    url.protocol = url.protocol === 'https:' ? 'wss:' : 'ws:'
    url.pathname = `${basePath}/ws/notice`
    url.search = ''
    url.searchParams.set('token', token)
    return url.toString()
  }

  return {
    lists,
    unreadCounts,
    totalUnread,
    loading,
    refresh,
    read,
    readAll,
    startRealtime,
    stopRealtime
  }
})
