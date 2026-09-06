import { request } from '@/utils/service/index'

// 获取路由
export const getRouters = () => {
  return request({
    url: '/getRouters',
    method: 'get',
  })
}
