/**
 * 溜井数字化监控管理平台 - API 请求封装
 *
 * @description 封装所有 API 请求方法，包括 GET、POST、PUT 等
 * @author zhangzhen
 * @version 0.1.0
 */
import type {
  BarPayload,
  CameraCategory,
  CarsLogsParams,
  CarsLogsPayload,
  CheckHeightParams,
  DeviceStatus,
  LogsPayload,
  MaterialLevel,
  MineCarStats,
  OutLogsParams,
  OutLogsPayload,
  SelectOption,
  WarningItem,
  WarningSummary
} from './types'
import { mockCarsLogs, mockOutLogs, mockLogs, mockBar, mockDevices, mockLive, mockMaterials, mockMineCars, mockSelectOptions, mockWarnings, mockWarningSummary } from './mock'

const USE_MOCK = import.meta.env.DEV

// API 基础路径
const API_BASE = import.meta.env.VITE_DP_API_BASE_URL || import.meta.env.VITE_API_BASE_URL || ''
// 请求超时时间（毫秒）
const REQUEST_TIMEOUT = 8000
// Token Cookie 键名
const TOKEN_KEY = 'EL-ADMIN-TOEKN'

/**
 * 构建请求 URL
 * @param path API 路径
 * @param params 查询参数
 * @returns 完整的 URL
 */
function buildUrl(path: string, params?: Record<string, string | number | undefined>) {
  // 如果是完整 URL，直接添加参数
  if (/^https?:\/\//i.test(path)) {
    const url = new URL(path)
    Object.entries(params || {}).forEach(([key, value]) => {
      if (value !== undefined) url.searchParams.set(key, String(value))
    })
    return url.toString()
  }

  // 否则拼接基础路径
  const normalizedBase = API_BASE.replace(/\/$/, '')
  const normalizedPath = path.startsWith('/') ? path : `/${path}`
  const url = new URL(`${normalizedBase}${normalizedPath}`, window.location.origin)

  Object.entries(params || {}).forEach(([key, value]) => {
    if (value !== undefined) url.searchParams.set(key, String(value))
  })

  return url.toString()
}

/**
 * 发送 GET 请求
 * @param path API 路径
 * @param params 查询参数
 * @returns Promise<T>
 */
export async function request<T>(path: string, params?: Record<string, string | number | undefined>): Promise<T> {
  const controller = new AbortController()
  const timer = window.setTimeout(() => controller.abort(), REQUEST_TIMEOUT)
  const token = getCookie(TOKEN_KEY)
  const headers: HeadersInit = {
    'Content-Type': 'application/json'
  }

  // 如果有 Token，添加到请求头
  if (token) {
    headers.Authorization = token
  }

  try {
    const response = await fetch(buildUrl(path, params), {
      method: 'GET',
      headers,
      credentials: 'include',
      signal: controller.signal
    })

    if (!response.ok) {
      throw new Error(`HTTP ${response.status}`)
    }

    const text = await response.text()
    if (!text) return null as T

    try {
      return JSON.parse(text) as T
    } catch {
      return text as T
    }
  } finally {
    window.clearTimeout(timer)
  }
}

/**
 * 发送 JSON 请求（POST/PUT）
 * @param path API 路径
 * @param body 请求体
 * @param method 请求方法，默认 POST
 * @returns Promise<T | null>
 */
export async function sendJson<T>(path: string, body: unknown, method = 'POST'): Promise<T | null> {
  const controller = new AbortController()
  const timer = window.setTimeout(() => controller.abort(), REQUEST_TIMEOUT)
  const token = getCookie(TOKEN_KEY)
  const headers: HeadersInit = {
    'Content-Type': 'application/json'
  }

  if (token) {
    headers.Authorization = token
  }

  try {
    const response = await fetch(buildUrl(path), {
      method,
      headers,
      credentials: 'include',
      body: JSON.stringify(body),
      signal: controller.signal
    })

    if (!response.ok) {
      throw new Error(`HTTP ${response.status}`)
    }

    const text = await response.text()
    if (!text) return null

    try {
      return JSON.parse(text) as T
    } catch {
      return text as T
    }
  } finally {
    window.clearTimeout(timer)
  }
}

/**
 * 发送 POST 请求
 * @param path API 路径
 * @param body 请求体
 * @returns Promise<T | null>
 */
export function postJson<T>(path: string, body: unknown): Promise<T | null> {
  return sendJson<T>(path, body, 'POST')
}

/**
 * 发送 PUT 请求
 * @param path API 路径
 * @param body 请求体
 * @returns Promise<T | null>
 */
export function putJson<T>(path: string, body: unknown): Promise<T | null> {
  return sendJson<T>(path, body, 'PUT')
}

/**
 * 从 Cookie 中获取值
 * @param name Cookie 键名
 * @returns Cookie 值
 */
function getCookie(name: string) {
  const match = document.cookie.match(new RegExp(`(?:^|; )${name.replace(/([.$?*|{}()[\]\\/+^])/g, '\\$1')}=([^;]*)`))
  return match ? decodeURIComponent(match[1]) : ''
}

/**
 * 溜井监控平台 API 集合
 */
export const dpApi = {
  // 获取报警统计
  warningSummary: () => USE_MOCK ? Promise.resolve(mockWarningSummary) : request<WarningSummary>('/api/dp/warning'),
  // 获取报警列表
  warningList: (type: string) => USE_MOCK ? Promise.resolve(mockWarnings) : request<WarningItem[]>('/api/dp/warning/list', { type }),
  // 获取设备状态
  facility: () => USE_MOCK ? Promise.resolve(mockDevices) : request<DeviceStatus[]>('/api/dp/facility'),
  // 获取矿车统计
  cars: () => USE_MOCK ? Promise.resolve(mockMineCars) : request<MineCarStats>('/api/dp/cars'),
  // 获取柱状图数据
  bar: () => USE_MOCK ? Promise.resolve(mockBar) : request<BarPayload>('/api/dp/bar'),
  // 获取出入料记录
  logs: () => USE_MOCK ? Promise.resolve(mockLogs) : request<LogsPayload>('/api/dp/logs'),
  // 获取部门选择选项
  select: () => USE_MOCK ? Promise.resolve(mockSelectOptions) : request<SelectOption[]>('/api/dp/select'),
  // 获取料位数据
  aiLogs: (type: string, dept: string) => USE_MOCK ? Promise.resolve(mockMaterials) : request<MaterialLevel[]>('/api/dp/aiLogs', { type, dept }),
  // 获取直播摄像头列表
  live: () => USE_MOCK ? Promise.resolve(mockLive) : request<CameraCategory[]>('/api/dp/live'),
  // 获取矿车日志（入料记录）
  carsLogs: (params: CarsLogsParams) => {
    if (USE_MOCK) {
      const { page = 0, size = 8 } = params
      const start = page * size
      const end = start + size
      const content = mockCarsLogs.content.slice(start, end)
      return Promise.resolve({
        ...mockCarsLogs,
        content
      })
    }
    return request<CarsLogsPayload>('/api/carsLogs', { ...params })
  },
  // 获取出料记录
  outLogs: (params: OutLogsParams) => {
    if (USE_MOCK) {
      const { page = 0, size = 8 } = params
      const start = page * size
      const end = start + size
      const content = mockOutLogs.content.slice(start, end)
      return Promise.resolve({
        ...mockOutLogs,
        content
      })
    }
    return request<OutLogsPayload>('/api/ynLogsOut', { ...params })
  },
  // 校验高度
  checkHeight: (params: CheckHeightParams) => postJson<unknown>('/api/camera/editheight', params),
  // 发送声光报警消息
  sirenMessage: (params: { ip: string; text: string; num: number }) => request<unknown>('/api/ynSiren/send/message', params)
}
