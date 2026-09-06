import type { LoginFormConfigOptions } from '../types/index.ts'

export default (options: LoginFormConfigOptions): BaseFormProps => {
  return {
    elFormConfig: {
      labelPosition: 'top',
      requireAsteriskPosition: 'right',
      hideRequiredAsterisk: true,
    },
    rules: {
      username: [
        {
          required: true,
          trigger: 'blur',
          message: '请输入账号',
        },
      ],
      password: [
        {
          required: true,
          trigger: 'blur',
          message: '请输入密码',
        },
      ],
      code: [
        {
          required: true,
          trigger: 'blur',
          message: '请输入验证码',
        },
      ],
    },
    itemStyle: {},
    formItems: [
      {
        field: 'username',
        label: '用户名',
        type: 'input',
        config: {
          placeholder: '请输入用户名',
          size: 'large',
          autocomplete: 'off',
        },
        eventFunction: options.listener,
        hideLabel: false,
      },
      {
        field: 'password',
        label: '密码',
        type: 'input',
        config: {
          placeholder: '请输入密码',
          size: 'large',
          clearable: false,
          showPassword: true,
          autocomplete: 'new-password',
        },
        eventFunction: options.listener,
        hideLabel: false,
      },
      {
        field: 'code',
        label: '验证码',
        type: 'custom',
        isHidden: options.codeHide,
        layout: {
          xs: 24,
          sm: 24,
          md: 24,
          lg: 24,
          xl: 24,
        },
        hideLabel: false,
      },
      {
        field: 'rememberMe',
        type: 'custom',
        hideLabel: true,
        layout: {
          xs: 24,
          sm: 24,
          md: 24,
          lg: 24,
          xl: 24,
        },
      },
    ],
    colLayout: {
      xl: 24,
      lg: 24,
      md: 24,
      sm: 24,
      xs: 24,
    },
    footerLayout: {
      xl: 24,
      lg: 24,
      md: 24,
      sm: 24,
      xs: 24,
    },
  }
}
