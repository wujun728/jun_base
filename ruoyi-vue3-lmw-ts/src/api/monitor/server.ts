import { request } from '@/utils/hsj/service/index'

// 获取服务信息
export function getServer() {
  return request<GetServerApi>({
    url: '/monitor/server',
    method: 'get',
  })
}
