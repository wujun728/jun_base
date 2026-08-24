import SettingMer from '@/libs/settingMer'
import noticeHandle from '@/libs/noticeHandle'
import ElementUI from 'element-ui'
import { roterPre } from '@/settings'
import { messageListApi } from '@/api/public'
import store from '@/store'
import { noticeMessageReadApi } from '@/api/user'
import { EventBus } from '@/libs/bus'
let limitConnect = 40 // 断线重连次数
let timeToken = ''
const audioUrl = require('../assets/audio/tip.mp3')
const audioTip = new Audio(audioUrl)
const notifications = {}
// 提取消息内容生成函数
function generateMessageContent(message) {
  const imgShow = message.image
  let content = `
    <a onclick="onNotice()" href="${roterPre}${message.url}">
      <div class='el-row display-align'>
        <div class='el-col el-col-24 left' ${imgShow == '' ? 'style="display:none"' : 'style="display:block"'}>
           <img src='${imgShow}' alt='' style="width:55px;height:55px" >
        </div>
        <div ${imgShow == '' ? 'class="el-col el-col-24 right width100"' : 'class="el-col el-col-24 right"'}>
          <p class='title over-text'>${message.title}</p>
          <p class='caption over-text2'>${message.message}</p>
        </div>
      </div>
    </a>`
  
  if (message.buttons.length > 0) {
    content += message.buttons.map((value, i) => `
      <div class='text-right'>
        <button type="button" class="el-button el-button--text el-button--small" onclick="onConfirm(${i})">
          <span>${value.title}</span>
        </button>
      </div>`
    ).join('')
  }
  
  return content
}

// 提取通知显示函数
function showNotification(message) {
  const notify = ElementUI.Notification({
    title: '消息',
    dangerouslyUseHTMLString: true,
    message: generateMessageContent(message),
    duration: 10000,
    offset: 60,
    iconClass: 'iconfont iconxiaoxi',
    customClass: 'message-socket'
  })
  
  notifications[message.uniqid] = notify
  getMessage()
  
  // 设置全局回调函数
  window.onConfirm = (index) => {
    const item = message.buttons[index]
    EventBus.$emit('messageSuccess', { item, detail: message })
    closeNotification(message)
  }
  
  window.onCancel = () => {
    noticeHandle(message, 0)
    closeNotification(message)
  }
  
  window.onNotice = () => closeNotification(message)
}

// 提取关闭通知函数
function closeNotification(message) {
  if (notifications[message.uniqid]) {
    notifications[message.uniqid].close()
    delete notifications[message.uniqid]
    batchMessageRead(1, { ids: [message.id] })
  }
}

// 主函数优化
function notice(token) {
  const ws = new WebSocket(`${SettingMer.wsSocketUrl}/ws?type=ent&token=${token}`)
  timeToken = token
  let pingInterval
  
  const send = (type, data) => ws.send(JSON.stringify({ type, data }))
  
  ws.onopen = () => {
    console.log('webSocket open')
    limitConnect = 40
    pingInterval = setInterval(() => send('ping'), 10000)
  }
  
  ws.onmessage = (res) => {
    const data = JSON.parse(res.data)
    if (data.type === 'notice') {
      audioTip.play()
      showNotification(data.data)
    }
  }
  
  ws.onclose = (e) => {
    EventBus.$emit('close', e)
    reconnect()
    clearInterval(pingInterval)
  }
  
  ws.onerror = () => reconnect()
  
  return () => ws.close()
}

// 重连
function reconnect() {
  // lockReconnect加锁，防止onclose、onerror两次重连
  if (limitConnect > 0) {
    if (!localStorage.getItem('lockReconnect')) {
      localStorage.setItem('lockReconnect', 1)
      limitConnect--
      console.log('第' + (40 - limitConnect + 1) + '次重连')
      // 进行重连
      setTimeout(function () {
        notice(timeToken)
        localStorage.removeItem('lockReconnect')
      }, 10000)
    }
  } else {
    console.log('webSocket连接已超时')
  }
}
// 批量标记未已读
function batchMessageRead(status, data) {
  noticeMessageReadApi(status, data)
    .then((res) => {
      getMessage()
    })
    .catch((error) => {
      // console.log(error.message);
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

export default notice
