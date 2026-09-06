/**
 * 溜井数字化监控管理平台 - 设备注册表
 *
 * @description 管理设备信息和在线状态检测
 * @author zhangzhen
 * @version 0.1.0
 */
import type { DeviceStatus } from './types'

/**
 * 设备类型
 */
type DeviceKind = '摄像头' | 'RFID阅读器' | '声光报警器' | '硬盘录像机'

/**
 * 网络设备信息
 */
interface NetworkDevice {
  kind: DeviceKind
  name: string
  area: string
  location: string
  ip: string
}

/**
 * 设备检测超时时间（毫秒）
 */
const DEVICE_TIMEOUT = Number(import.meta.env.VITE_DEVICE_CHECK_TIMEOUT || 1200)
/**
 * 设备状态缓存时间（毫秒）
 */
const DEVICE_CACHE_TTL = Number(import.meta.env.VITE_DEVICE_CHECK_CACHE_TTL || 120000)
/**
 * 设备检测并发数
 */
const DEVICE_CHECK_CONCURRENCY = Number(import.meta.env.VITE_DEVICE_CHECK_CONCURRENCY || 6)

/**
 * 支持的设备类型列表
 */
const deviceKinds: DeviceKind[] = ['摄像头', 'RFID阅读器', '声光报警器', '硬盘录像机']
/**
 * 缓存的设备状态
 */
let cachedStatuses: DeviceStatus[] | null = null
/**
 * 缓存时间戳
 */
let cachedAt = 0

/**
 * 网络设备注册表
 */
export const networkDevices: NetworkDevice[] = [
  { kind: '声光报警器', name: '声光报警器 (1920挂车点)', area: '1920斜井巷道', location: '1920斜井巷道', ip: '192.168.18.124' },
  { kind: '声光报警器', name: '61#溜井', area: '61#溜井巷道', location: '61#溜井巷道', ip: '192.168.18.118' },
  { kind: '声光报警器', name: '54#溜井', area: '54#溜井巷道', location: '54#溜井巷道', ip: '192.168.18.117' },
  { kind: '声光报警器', name: '53#溜井', area: '53#溜井巷道', location: '53#溜井巷道', ip: '192.168.18.116' },
  { kind: '声光报警器', name: '4#溜井', area: '4#溜井巷道', location: '4#溜井巷道', ip: '192.168.18.115' },
  { kind: '声光报警器', name: '5#溜井', area: '5#溜井巷道', location: '5#溜井巷道', ip: '192.168.18.114' },
  { kind: '摄像头', name: '5#溜井入料口摄像头', area: '三坑', location: '5#溜井口上方', ip: '192.168.18.103' },
  { kind: '摄像头', name: '4#溜井入料口摄像头', area: '三坑', location: '4#溜井口上方', ip: '192.168.18.104' },
  { kind: '摄像头', name: '53#溜井入料口摄像头', area: '三坑', location: '53#溜井口上方', ip: '192.168.18.105' },
  { kind: '摄像头', name: '54#溜井入料口摄像头', area: '三坑', location: '54#溜井口上方', ip: '192.168.18.106' },
  { kind: '摄像头', name: '61#溜井入料口摄像头', area: '一坑', location: '34#溜井口上方（1540）', ip: '192.168.18.107' },
  { kind: '摄像头', name: '1920挂车点', area: '二坑', location: '', ip: '192.168.18.122' },
  { kind: '摄像头', name: '1920挂车点1', area: '二坑', location: '1920斜井巷道', ip: '192.168.18.123' },
  { kind: '硬盘录像机', name: '硬盘录像机（机房）', area: '1360数据机房', location: '数据机房', ip: '192.168.18.100' },
  { kind: '硬盘录像机', name: '硬盘录像机（4#、5#、53#、54#）', area: '4#/5#溜井设备箱', location: '4#/5#溜井设备箱', ip: '192.168.18.101' },
  { kind: '硬盘录像机', name: '硬盘录像机（34#）', area: '34#溜井设备箱', location: '34#溜井设备箱', ip: '192.168.18.102' },
  { kind: 'RFID阅读器', name: '61#溜井', area: '61#溜井巷道', location: '61#溜井巷道', ip: '192.168.18.113' },
  { kind: 'RFID阅读器', name: '54#溜井', area: '54#溜井巷道', location: '54#溜井巷道', ip: '192.168.18.112' },
  { kind: 'RFID阅读器', name: '53#溜井', area: '53#溜井巷道', location: '53#溜井巷道', ip: '192.168.18.111' },
  { kind: 'RFID阅读器', name: '4#溜井', area: '4#溜井巷道', location: '4#溜井巷道', ip: '192.168.18.110' },
  { kind: 'RFID阅读器', name: '5#溜井', area: '5#溜井巷道', location: '5#溜井巷道', ip: '192.168.18.109' }
]

/**
 * 检测设备 IP 是否在线
 * @param ip 设备 IP 地址
 * @returns 是否在线
 */
async function probeIp(ip: string): Promise<boolean> {
  const controller = new AbortController()
  const timer = window.setTimeout(() => controller.abort(), DEVICE_TIMEOUT)

  try {
    await fetch(`http://${ip}/`, {
      method: 'GET',
      mode: 'no-cors',
      cache: 'no-store',
      signal: controller.signal
    })
    return true
  } catch {
    return false
  } finally {
    window.clearTimeout(timer)
  }
}

/**
 * 获取所有设备的在线状态
 * @returns 设备状态列表
 */
export async function checkDeviceStatuses(): Promise<DeviceStatus[]> {
  const now = Date.now()
  // 如果缓存未过期，直接返回缓存
  if (cachedStatuses && now - cachedAt < DEVICE_CACHE_TTL) {
    return cachedStatuses
  }

  // 获取所有唯一的 IP 地址
  const uniqueIps = Array.from(new Set(networkDevices.map(item => item.ip)))
  const pairs: Array<readonly [string, boolean]> = []

  // 并发检测设备状态
  for (let index = 0; index < uniqueIps.length; index += DEVICE_CHECK_CONCURRENCY) {
    const group = uniqueIps.slice(index, index + DEVICE_CHECK_CONCURRENCY)
    pairs.push(...await Promise.all(group.map(async ip => [ip, await probeIp(ip)] as const)))
  }

  // 创建 IP 到在线状态的映射
  const reachable = new Map(pairs)

  // 按设备类型统计状态
  cachedStatuses = deviceKinds.map(kind => {
    const devices = networkDevices.filter(item => item.kind === kind)
    const online = devices.filter(item => reachable.get(item.ip)).length

    return {
      name: kind,
      total: devices.length,
      online,
      offline: devices.length - online
    }
  })

  cachedAt = now
  return cachedStatuses
}