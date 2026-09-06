import store from "../store";
const { getters } = store;
export default class WebSocketClass {
  private url: any;
  private data: any;
  private isCreate: boolean;
  private isInitiative: boolean;
  private timeoutNumber: any;
  private heartbeatTimer: any;
  private reconnectTimer: any;
  private socketExamples: any;
  private againTime: number;

  constructor(url: any, time: any) {
    this.url = url;
    this.data = null;
    this.isCreate = false; // WebSocket 是否创建成功
    this.isInitiative = false; // 是否主动断开
    this.timeoutNumber = time; // 心跳检测间隔
    this.heartbeatTimer = null; // 心跳检测定时器
    this.reconnectTimer = null; // 断线重连定时器
    this.socketExamples = null; // websocket实例
    this.againTime = 3; // 重连等待时间(单位秒)
  }

  // 初始化websocket连接
  initSocket() {
    this.socketExamples = uni.connectSocket({
      url: this.url,
      header: {
        "content-type": "application/json"
      },
      success: () => {
        this.isCreate = true;
      },
      fail: (rej: any) => {
        console.error(rej);
        this.isCreate = false;
      }
    });
    this.createSocket();
  }

  // 创建websocket连接
  createSocket() {
    if (this.isCreate && getters.isLogin) {
      console.log("WebSocket 开始初始化");
      // 监听 WebSocket 连接打开事件
      try {
        this.socketExamples.onOpen(() => {
          console.log("WebSocket 连接成功");
          clearInterval(this.heartbeatTimer);
          clearTimeout(this.reconnectTimer);
          // 打开心跳检测
          this.heartbeatCheck();
        });
        // 监听 WebSocket 接受到服务器的消息事件
        this.socketExamples.onMessage((res: any) => {
          const data = JSON.parse(res.data);
          if (data.type === "notice") {
            uni.$emit("message", data);
          }
        });
        // 监听 WebSocket 连接关闭事件
        this.socketExamples.onClose(() => {
          console.log("WebSocket 关闭了");
          this.reconnect();
        });
        // 监听 WebSocket 错误事件
        this.socketExamples.onError(() => {
          console.log("WebSocket 出错了");
          this.isInitiative = false;
        });
      } catch (error) {
        console.warn(error);
      }
    }
  }

  // 发送消息
  sendMsg(value: any) {
    const param = JSON.stringify(value);
    return new Promise((resolve, reject) => {
      this.socketExamples.send({
        data: param,
        success() {
          resolve(true);
        },
        fail(error: any) {
          reject(error);
        }
      });
    });
  }

  // 开启心跳检测
  heartbeatCheck() {
    this.data = { type: "ping" };
    this.heartbeatTimer = setInterval(() => {
      this.sendMsg(this.data);
    }, this.timeoutNumber * 1000);
  }

  // 重新连接
  reconnect() {
    // 停止发送心跳
    clearTimeout(this.reconnectTimer);
    clearInterval(this.heartbeatTimer);
    // 如果不是人为关闭的话，进行重连
    if (!this.isInitiative) {
      this.reconnectTimer = setTimeout(() => {
        this.initSocket();
      }, this.againTime * 1000);
    }
  }

  // 关闭 WebSocket 连接
  closeSocket(reason = "关闭") {
    this.socketExamples.close({
      reason,
      success() {
        this.data = null;
        this.isCreate = false;
        this.isInitiative = true;
        this.socketExamples = null;
        clearInterval(this.heartbeatTimer);
        clearTimeout(this.reconnectTimer);
        console.log("关闭 WebSocket 成功");
      },
      fail() {
        console.log("关闭 WebSocket 失败");
      }
    });
  }
}
