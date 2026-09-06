import request from '@/utils/hsj/service'
import { businessBase } from '../../config/base'
export function checkVerifyCase(data) {
  return request.post({
    url: businessBase + '/case/verifyCase',
    data,
  })
}
