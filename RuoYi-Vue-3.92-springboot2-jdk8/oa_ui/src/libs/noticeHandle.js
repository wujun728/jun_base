import SettingMer from '@/libs/settingMer'
import Vue from 'vue'
import ElementUI from 'element-ui'
import router from '../router'
import store from '../store'
import { roterPre } from '@/settings'
import { approveVerifyStatusApi } from '@/api/business'
import { userAssessSetShow } from '@/api/user'
import { messageListApi } from '@/api/public'
let templateType = ['business_approval']

function handleNotice(data = {}, type = 1) {
  let types = data.template_type
  if (type === 0 && !templateType.includes(types)) return
  if (Object.keys(data).length <= 0) {
    return false
  } else {
    if (types === 'enterprise_verify') {
      // 已通过平台审核，重新登录系统
      router.dispatch('user/logout')
      router.push(`${roterPre}/login?redirect=${router.fullPath}`)
    } else if (types === 'assess_start') {
      // 开启考核提醒
      userAssess(data.other.id)
    } else {
      router.push({ path: `${roterPre}${data.url}` })
    }
  }
}

// 业务审批提醒 同意与拒绝
function approveVerify(id, status) {
  approveVerifyStatusApi(id, status)
    .then((res) => {
      ElementUI.Message({
        message: res.message,
        type: 'success'
      })
    })
    .catch((error) => {
      ElementUI.Message({
        message: error.message,
        type: 'error'
      })
    })
}

// 开启考核提醒
function userAssess(id) {
  userAssessSetShow(id)
    .then((res) => {
      ElementUI.Message({
        message: res.message,
        type: 'success'
      })
    })
    .catch((error) => {
      ElementUI.Message({
        message: error.message,
        type: 'error'
      })
    })
}

// 消息数量
function getMessage() {
  messageListApi({ page: 1, limit: 5 })
    .then((res) => {
      const num = res.data.messageNum ? res.data.messageNum : 0
      store.commit('user/SET_MESSAGE', num)
    })
    .catch((error) => {
      ElementUI.Message({
        message: error.message,
        type: 'error'
      })
    })
}
export default handleNotice
