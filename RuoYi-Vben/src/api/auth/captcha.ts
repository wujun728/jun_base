import { defHttp } from '@/utils/http/axios';

/**
 * 验证码相关
 */

enum Api {
  captchaImage = '/captchaImage',
}

/**
 * @param img 图片验证码 需要和base64拼接
 * @param captchaEnabled 是否开启
 * @param uuid 验证码ID
 */
export interface CaptchaResponse {
  img: string;
  captchaEnabled: boolean;
  uuid: string;
}

/**
 * 图片验证码
 * @returns
 */
export function captchaImage() {
  return defHttp.get<CaptchaResponse>({ url: Api.captchaImage }, { ignoreCancelToken: true });
}
