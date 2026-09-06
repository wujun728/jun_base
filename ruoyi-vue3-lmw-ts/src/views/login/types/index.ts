/**
 * 登录页表单 model（含仅前端使用的「记住密码」）
 */
export interface LoginPageFormData {
  username: string
  password: string
  code: string
  uuid: string
  rememberMe: boolean
}

/**
 * 与若依 `/captchaImage` 约定字段一致
 */
export interface LoginCaptchaPayload {
  img?: string
  uuid?: string
  captchaEnabled?: boolean
}

/**
 * `config/formConfig.ts` 入参
 */
export interface LoginFormConfigOptions {
  codeHide: boolean
  listener: {
    keyup: (event: KeyboardEvent) => void
  }
}
