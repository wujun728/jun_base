import request from './request';

/**
 * @description 验证码
 */
export function configApi(key) {
  return request.get(`config/${key}`);
}

/**
 * @description 上传配置(表单)
 */
export function uploadApi() {
  return request.get(`upload/config`);
}

