import request from '@/utils/request'

export function findDocumentStorageByPg(text, token) {
  return new Promise((resolve, reject) => {
    const eventSource = new EventSource(
      `${process.env.VUE_APP_BASE_API}/langchain/findDocumentStorageByPg?text=${encodeURIComponent(text)}`,
      {
        headers: {
          'Authorization': `Bearer ${token}`, // 注入认证令牌
          'Content-Type': 'text/event-stream' // 显式声明内容类型
        }
      }
    )

    eventSource.onopen  = () => {
      resolve(eventSource)
    }

    eventSource.onerror  = (error) => {
      if (error.status  === 401) {
        reject(new Error('认证失败，请重新登录'))
      } else {
        reject(error)
      }
      eventSource.close()
    }
  })
}


export function streamChat(sessionId, text, token) {
  return new Promise((resolve, reject) => {
    const eventSource = new EventSource(
      `${process.env.VUE_APP_BASE_API}/langchain/streamChat?sessionId=${encodeURIComponent(sessionId)}&text=${encodeURIComponent(text)}`,
      {
        headers: {
          'Authorization': `Bearer ${token}`, // 注入认证令牌
          'Content-Type': 'text/event-stream' // 显式声明内容类型
        }
      }
    )

    eventSource.onopen  = () => {
      resolve(eventSource)
    }

    eventSource.onerror  = (error) => {
      if (error.status  === 401) {
        reject(new Error('认证失败，请重新登录'))
      } else {
        reject(error)
      }
      eventSource.close()
    }
  })
}

// 新增聊天会话
export function insertSession(data) {
  return request({
    url: '/langchain/insertSession',
    method: 'post',
    data: data
  })
}
