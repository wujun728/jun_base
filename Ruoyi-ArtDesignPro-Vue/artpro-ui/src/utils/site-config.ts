import { reactive } from 'vue'
import AppConfig from '@/config'
import { fetchSiteConfig } from '@/api/system/config'

export const siteConfig = reactive<Record<string, string>>({
  'site.name': AppConfig.systemInfo.name,
  'site.description': '商业化中后台管理系统',
  'site.login.title': '欢迎回来',
  'site.login.description': '输入您的账号和密码登录',
  'site.login-left-title': '统一管理入口',
  'site.login-left-sub-title': '高效、清晰、可扩展的企业管理控制台',
  'site.watermark.content': '',
  'site.watermark.mode': 'username',
  'site.watermark.show-time': 'false'
})

export const getSiteConfig = (key: string, fallback = '') => siteConfig[key] || fallback

export async function loadSiteConfig() {
  Object.assign(siteConfig, await fetchSiteConfig())
  AppConfig.systemInfo.name = getSiteConfig('site.name', AppConfig.systemInfo.name)
}
