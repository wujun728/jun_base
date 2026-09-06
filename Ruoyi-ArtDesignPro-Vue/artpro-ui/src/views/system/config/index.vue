<template>
  <div class="art-full-height">
    <ElCard class="site-config-card">
      <ElTabs v-model="siteTab">
        <ElTabPane
          v-for="group in groups"
          :key="group.name"
          :label="group.label"
          :name="group.name"
        >
          <ArtForm
            v-model="siteForm"
            :items="group.items"
            :show-submit="false"
            :show-reset="false"
            label-width="130px"
          />
        </ElTabPane>
      </ElTabs>
      <ElButton
        v-if="hasAuth('system:config:edit')"
        type="primary"
        :loading="saving"
        @click="saveSite"
      >
        保存站点配置
      </ElButton>
    </ElCard>
  </div>
</template>

<script setup lang="ts">
  import { ElMessage } from 'element-plus'
  import { fetchSiteConfig, updateSiteConfig } from '@/api/system/config'
  import { useAuth } from '@/hooks/core/useAuth'
  import { loadSiteConfig } from '@/utils/site-config'

  defineOptions({ name: 'SystemConfig' })
  const { hasAuth } = useAuth()
  const siteTab = ref('identity')
  const saving = ref(false)
  const siteForm = reactive<Record<string, any>>({})
  const keyOf = (key: string) => key.replaceAll('.', '__').replaceAll('-', '_')
  const option = (label: string, value: string) => ({ label, value })
  const field = (key: string, label: string, type = 'input', props: Record<string, any> = {}) => ({
    key: keyOf(key),
    label,
    type,
    span: 12,
    props: { clearable: true, ...props }
  })
  const yesNo = [option('是', 'true'), option('否', 'false')]
  const skinOptions = [
    option('蓝色', 'skin-blue'),
    option('绿色', 'skin-green'),
    option('紫色', 'skin-purple'),
    option('红色', 'skin-red'),
    option('黄色', 'skin-yellow')
  ]
  const sideThemeOptions = [option('深色主题', 'theme-dark'), option('浅色主题', 'theme-light')]
  const behaviorCaptchaOptions = [
    option('随机', 'RANDOM'),
    option('滑块验证', 'SLIDER'),
    option('旋转验证', 'ROTATE'),
    option('滑动还原', 'CONCAT'),
    option('文字点选', 'WORD_IMAGE_CLICK')
  ]
  const initPasswordModifyOptions = [option('关闭提醒', '0'), option('提醒修改', '1')]
  const passwordCharTypeOptions = [
    option('任意字符', '0'),
    option('仅数字', '1'),
    option('仅英文字母', '2'),
    option('字母和数字', '3'),
    option('字母数字和特殊字符', '4')
  ]
  const showBehaviorCaptcha = computed(
    () => siteForm[keyOf('sys.account.captchaType')] === 'slider'
  )
  const groups = computed(() => [
    {
      name: 'identity',
      label: '系统标识',
      items: [
        field('site.name', '系统名称'),
        field('site.description', '站点简介'),
        field('site.login.title', '登录欢迎标题'),
        field('site.login.description', '登录欢迎描述'),
        field('site.login-left-title', '登录左侧标题'),
        field('site.login-left-sub-title', '登录左侧描述')
      ]
    },
    {
      name: 'appearance',
      label: '界面与水印',
      items: [
        field('sys.index.skinName', '默认皮肤样式', 'select', { options: skinOptions }),
        field('sys.index.sideTheme', '侧边栏主题', 'select', { options: sideThemeOptions }),
        field('site.watermark.mode', '水印内容', 'select', {
          options: [
            option('当前用户名', 'username'),
            option('站点名称', 'site'),
            option('自定义文本', 'custom')
          ]
        }),
        field('site.watermark.content', '自定义水印文本'),
        field('site.watermark.show-time', '叠加当前时间', 'select', { options: yesNo })
      ]
    },
    {
      name: 'security',
      label: '登录与安全',
      items: [
        field('sys.account.captchaEnabled', '验证码开关', 'select', { options: yesNo }),
        field('sys.account.captchaType', '验证码类型', 'select', {
          options: [option('滑块验证码', 'slider'), option('图片验证码', 'image')]
        }),
        {
          ...field('sys.account.behaviorCaptchaType', '行为验证码类型', 'select', {
            options: behaviorCaptchaOptions
          }),
          hidden: !showBehaviorCaptcha.value
        },
        field('sys.account.registerUser', '开放用户注册', 'select', { options: yesNo }),
        field('sys.login.blackIPList', '登录黑名单', 'input', {
          type: 'textarea',
          rows: 3,
          placeholder: '多个匹配项以 ; 分隔，支持 * 通配和网段'
        }),
        field('security.access-token-hours', '令牌有效时长（小时）', 'number', {
          min: 0,
          placeholder: '0 表示永久有效'
        }),
        field('security.max-failed-login-count', '失败锁定阈值', 'number'),
        field('security.account-lock-minutes', '账号锁定时长（分钟）', 'number'),
        field('security.password-min-length', '密码最小长度', 'number'),
        field('security.password-max-length', '密码最大长度', 'number'),
        field('security.password-require-uppercase', '要求大写字母', 'select', { options: yesNo }),
        field('security.password-require-lowercase', '要求小写字母', 'select', { options: yesNo }),
        field('security.password-require-number', '要求数字', 'select', { options: yesNo }),
        field('security.password-require-special', '要求特殊字符', 'select', { options: yesNo })
      ]
    },
    {
      name: 'account',
      label: '用户账号',
      items: [
        field('sys.user.initPassword', '用户初始密码', 'input', {
          type: 'password',
          showPassword: true,
          autocomplete: 'new-password'
        }),
        field('sys.account.initPasswordModify', '初始密码修改策略', 'select', {
          options: initPasswordModifyOptions
        }),
        field('sys.account.passwordValidateDays', '密码更新周期（天）', 'number'),
        field('sys.account.chrtype', '密码字符范围', 'select', {
          options: passwordCharTypeOptions
        })
      ]
    }
  ])
  const defaults: Record<string, string> = {
    'site.name': 'Nexa Admin',
    'site.description': '商业化中后台管理系统',
    'site.login.title': '欢迎回来',
    'site.login.description': '输入您的账号和密码登录',
    'site.login-left-title': '统一管理入口',
    'site.login-left-sub-title': '高效、清晰、可扩展的企业管理控制台',
    'sys.index.skinName': 'skin-blue',
    'sys.index.sideTheme': 'theme-dark',
    'site.watermark.content': '',
    'site.watermark.mode': 'username',
    'site.watermark.show-time': 'false',
    'sys.account.captchaEnabled': 'true',
    'sys.account.captchaType': 'slider',
    'sys.account.behaviorCaptchaType': 'SLIDER',
    'sys.account.registerUser': 'false',
    'sys.login.blackIPList': '',
    'security.access-token-hours': '2',
    'security.max-failed-login-count': '5',
    'security.account-lock-minutes': '10',
    'security.password-min-length': '5',
    'security.password-max-length': '20',
    'security.password-require-uppercase': 'false',
    'security.password-require-lowercase': 'false',
    'security.password-require-number': 'false',
    'security.password-require-special': 'false',
    'sys.user.initPassword': '123456',
    'sys.account.initPasswordModify': '1',
    'sys.account.passwordValidateDays': '0',
    'sys.account.chrtype': '0'
  }
  const originalKeys = computed(() =>
    groups.value
      .flatMap((group) => group.items)
      .map((item) => {
        const match = Object.keys(defaults).find((key) => keyOf(key) === item.key)
        return match!
      })
  )
  async function loadSite() {
    const received = await fetchSiteConfig()
    const values = Object.fromEntries(
      Object.entries(defaults).map(([key, defaultValue]) => [key, received[key] || defaultValue])
    )
    originalKeys.value.forEach((key) => {
      const value = values[key]
      siteForm[keyOf(key)] =
        (key.startsWith('security.') && !key.includes('require-')) ||
        key === 'sys.account.passwordValidateDays'
          ? Number(value)
          : value
    })
  }
  async function saveSite() {
    saving.value = true
    try {
      const values = Object.fromEntries(
        originalKeys.value.map((key) => [key, String(siteForm[keyOf(key)] ?? '')])
      )
      await updateSiteConfig(values)
      await loadSiteConfig()
      ElMessage.success('站点配置已保存')
    } finally {
      saving.value = false
    }
  }
  onMounted(loadSite)
</script>

<style scoped>
  .site-config-card {
    min-height: 100%;
  }
</style>
