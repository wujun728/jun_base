import request from '@/utils/hsj/service'
import { businessBase } from '@/api/config/base'
export const getProvinceList = () => {
  return request.get({
    url: `${businessBase}/area/list`,
  })
}
export const getCityList = (code, level) => {
  return request.get({
    url: `${businessBase}/area/${code}/${level}`,
  })
}
export const getProvinceCountList = (id) => {
  let url = businessBase + `/specialist/statisticsSpecialist`
  if (id) {
    url += `/${id}`
  }
  return request.get({
    url,
  })
}
