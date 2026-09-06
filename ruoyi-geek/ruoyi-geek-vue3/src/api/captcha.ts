import { CaptchaVO } from '@/types/user'
import request from '@/utils/request'

export function getCaptcha(data: CaptchaVO) {
  return request<CaptchaVO>({
    url: '/captcha/get',
    method: 'post',
    headers: {
      repeatSubmit: false
    },
    data
  })
}

export function checkCaptcha(data: CaptchaVO) {
  return request<CaptchaVO>({
    url: '/captcha/check',
    method: 'post',
    headers: {
      repeatSubmit: false
    },
    data
  })
}