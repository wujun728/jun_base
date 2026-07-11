import eventType from "./eventType.js";

let connectWs = function ($wsApi, $store, $enums, $msgType, $eventBus, $alert, $message) {
  // ws初始化
  $wsApi.connect(process.env.VUE_APP_WS_URL, $store.state.user.token, 0);
  $wsApi.onConnect(() => {
    //处理连接后消息
  });
  $wsApi.onMessage((cmd, msgInfo) => {
    if (cmd == 2) {
      // 下线
    } else if (cmd == 5) {
      // 处理系统消息
      handleSystemMessage($wsApi, $enums, $alert, msgInfo);
    } else {
      // 处理业务消息
      if (eventType[msgInfo.type]) {
        // 事件消息，利用事件总线转发
        $eventBus.$emit(eventType[msgInfo.type], { text: msgInfo.content });
      }
    }
  });
  $wsApi.onClose((e) => {
    console.log(e);
    if (e.code != 3000) {
      // 断线重连
      console.log("连接断开，正在尝试重新连接...");
      $wsApi.reconnect(process.env.VUE_APP_WS_URL, $store.state.user.token);
    }
  });
}

let handleSystemMessage = function ($wsApi, $enums, $alert, msg) {
  // 用户被封禁
  if (msg.type == $enums.MESSAGE_TYPE.USER_BANNED) {
    $wsApi.close(3000)
    $alert('您的账号已被管理员封禁,原因:' + msg.content, '账号被封禁', {
      confirmButtonText: '确定',
      callback: (action) => {
      },
    })
    return
  }
} 

export {
  connectWs, 
  handleSystemMessage,
}

