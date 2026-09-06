import { request } from '@/utils/hsj/service/index'

// 获取路由
export const getRouters = () => {
  return request({
    url: '/getRouters',
    method: 'get',
  })
}
