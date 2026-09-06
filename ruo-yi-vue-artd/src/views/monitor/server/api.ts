import response from '@/utils/http'

// 获取服务信息
export function getServer() {
  return response<any>({
    url: '/monitor/server',
    method: 'get'
  })
}
