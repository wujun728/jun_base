import { Message } from '@/types/Message';
import { getToken } from '@/utils/auth'
import { StrUtil } from '@/utils/StrUtil';
let _socket: WebSocket;
let _callback: { [key: string]: (data: any) => void } = {}
type ConnectSocketOption = {
    url: string | URL, headers?: {
        isToken?: boolean
    }
}


export default {
    /**
     * 连接websocket
     * 最简单的用法就是传入{url:"ws://demo"}
     * 当连接成功后触发回调函数
     */
    connect(options: ConnectSocketOption) {
        return new Promise((
            resolve: (client: WebSocket, en: Event) => void,
        ) => {
            const isToken = (options.headers || {}).isToken === false
            let authorization = ""
            if (getToken() && !isToken) {
                authorization = 'Bearer ' + getToken()
            }
            if (_socket !== undefined) {
                _socket.close()
            }
            _socket = new WebSocket(options.url + `?Authorization=${encodeURIComponent(authorization)}`);
            _socket.onopen = (event: Event) => resolve(_socket, event);
            this.onMessage(() => { });
        })
    },
    /**
     * 关闭连接
     * @returns 关闭连接的Promise，回调函数只会运行一次
     */
    close() {
        return new Promise((resolve) => {
            let onclose = _socket.onclose
            _socket.onclose = res => {
                resolve(res)
                _socket.onclose = onclose
            }
            _socket.close()
        })
    },
    /**
     * 发送信息
     * @param msg 消息，会被处理成json字符串
     * @returns
     */
    send(msg: Message) {
        _socket.send(JSON.stringify(msg))
    },
    /**
     * 发送信息,可以异步回调
     * @param msg 消息，会被处理成json字符串
     * @returns
     */
    asyncSend(msg: Message) {
        return new Promise((resolve, reject) => {
            _callback[msg.messageId] = resolve
            _socket.send(JSON.stringify(msg))
        })
    },
    /**
     * 监听事件
     * @param event 要监听的事件
     * @returns 在回调函数中处理事件
     */
    on(event: string, callback: (data: any) => void) {
        _callback[event] = callback
    },
    /**
     * 取消监听事件
     * @param event 要取消监听的事件
     */
    off(event: string) {
        delete _callback[event]
    },
    /**
     * 定义默认监听事件
     * @param callback 默认监听事件的处理函数
     */
    onMessage(callback: (data: Message) => void) {
        _socket.onmessage = res => {
            let uuid: string | undefined;
            try {
                let data: Message = JSON.parse(res.data)
                uuid = data.messageId
                const event = data.subject
                if (StrUtil.isNotEmpty(uuid) && !!_callback[uuid]) {
                    _callback[uuid](data)
                } else if (StrUtil.isNotEmpty(event) && !!_callback[event]) {
                    _callback[event](data)
                } else {
                    callback(data);
                }
            } catch (error) {
                console.error("WebSocket JSON parse error:", error);
                console.error("Received data:", res.data);
            } finally {
                if (uuid && _callback[uuid]) {
                    delete _callback[uuid]
                }
            }
        }
    },
    /**
     * 定义异常事件
     * @param callback 默认异常事件的处理函数
     */
    onError(callback: ((client: WebSocket, en: Event) => void)) {
        _socket.onerror = (event) => callback(_socket, event);
    },
    /**
     * 定义关闭事件
     * @param callback 默认关闭事件的处理函数
     */
    onClose(callback: (client: WebSocket, en: Event) => void) {
        _socket.onclose = (event) => callback(_socket, event);
    }
};