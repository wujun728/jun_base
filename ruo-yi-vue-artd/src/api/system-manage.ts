import { RuoYiAppRouteRecord } from '@/types/router'
import request from '@/utils/http'

// 获取菜单列表
export function fetchGetMenuList() {
  return request<RuoYiAppRouteRecord[]>({
    url: '/getRouters',
    method: 'get'
  })
}
