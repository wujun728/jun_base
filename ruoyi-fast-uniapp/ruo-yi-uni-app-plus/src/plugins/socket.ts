import { getToken } from '@/utils/auth'
import { generateUUID } from '@/utils/geek';
let _socket: UniApp.SocketTask;
let _callback: { [key: string]: (data: any) => void } = {}
const enableJSON = true // 开启JSON解析消息，需要开启JSON解析消息才能开启uuid和event
const enableUUID = true // 需要接收信息中包含uuid字段，uuid优先级高于event
const enableEvent = true // 需要接收信息中包含event字段


interface ConnectSocketOption extends UniApp.ConnectSocketOption {
    headers: {
        isToken: boolean
    }
}

export default {
    /**
     * 连接websocket
     * 最简单的用法就是传入{url:"ws://demo"}
     * 当连接成功后触发回调函数
     */
    connect(options: ConnectSocketOption) {
        return new Promise((resolve, reject) => {
            const isToken = (options.headers || {}).isToken === false
            options.header = options.header || { 'content-type': 'application/json' }
            if (getToken() && !isToken) {
                options.header['Authorization'] = 'Bearer ' + getToken()
            }
            if(_socket !== undefined){
                _socket.close({})
            }
            _socket = uni.connectSocket({
                url: options.url,
                header: options.header,
                method: options.method || 'GET',
                fail: reject
            });
            _socket.onError(reject)
            _socket.onOpen(resolve)
            _socket.onMessage(res => {
                if(enableJSON){
                    let data = JSON.parse((res || {}).data)
                    if (enableUUID && (data || {}).uuid !== undefined) {
                        _callback[data.uuid](data)
                    } else if (enableEvent && (data || {}).event !== undefined) {
                        _callback[data.event](data)
                    }
                }
            })
        })
    },
    /**
     * 发送信息
     * @param msg 消息，会被处理成json字符串
     * @param uuid 唯一标识,可以传入uuid，也可以传入true自动生成uuid，flase表示该消息不需要单独处理
     * @returns
     */
    send(msg: any, uuid: string | boolean = false) {
        return new Promise((resolve, reject) => {
            if (enableUUID && uuid != undefined && uuid != "" && uuid != false) {
                if (uuid == true) {
                    msg.uuid = generateUUID()
                    _callback[msg.uuid] = resolve
                } else {
                    _callback[uuid] = resolve
                }
            }
            _socket.send({
                data: JSON.stringify(msg),
                fail: reject
            })
        })
    },
    /**
     * 关闭连接
     * @returns 关闭连接的Promise，回调函数只会运行一次
     */
    close() {
        return new Promise((resolve, reject) => {
            let onclose = _socket.onClose
            _socket.onClose(res => {
                resolve(res)
                _socket.onClose = onclose
            })
            _socket.close({
                fail: reject
            })

        })
    },
    /**
     * 监听事件
     * @param event 要监听的事件
     * @returns 在回调函数中处理事件
     */
    on(event: string) {
        return new Promise((resolve) => {
            _callback[event] = resolve
        })
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
    onMessage(callback: (data: any) => void) {
        _socket.onMessage(res => {
            if(enableJSON){
                let data = JSON.parse((res || {}).data)
                if (enableUUID && (data || {}).uuid !== undefined) {
                    _callback[data.uuid](res)
                    delete _callback[data.uuid]
                } else if (enableEvent && (data || {}).event !== undefined) {
                    _callback[data.event](res)
                } else {
                    callback(data)
                }
            }else{
                callback(res.data)
            }
        })
    },
    /**
     * 定义异常事件
     * @param callback 默认异常事件的处理函数
     */
    onError(callback: (data: any) => void) {
        _socket.onError(callback)
    },
    /**
     * 定义关闭事件
     * @param callback 默认关闭事件的处理函数
     */
    onClose(callback: (data: any) => void) {
        _socket.onError(callback)
    }
};