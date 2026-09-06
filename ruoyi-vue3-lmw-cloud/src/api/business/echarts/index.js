import request from '@/utils/hsj/service'
import { businessBase } from '../../config/base'
// 获取时间信息
export function getDateView() {
  return request.get({
    url: businessBase + '/statistical/dateView',
  })
}
// 按月统计
export function getStatisticsbymonth(params) {
  return request.get({
    url:
      businessBase +
      `/statistical/statisticsbymonth/${params.year}/${params.month}`,
    params,
  })
}
// 按季度统计
export function getStatisticsbyquarter(params) {
  return request.get({
    url: businessBase + `/statistical/statisticsbyquarter/${params.year}`,
    params,
  })
}
// 按年统计
export function getStatisticsbyyear(params) {
  return request.get({
    url: businessBase + `/statistical/statisticsbyyear/${params.year}`,
    params,
  })
}
