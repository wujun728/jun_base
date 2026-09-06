import { getToken } from '@/utils/auth'
import { generateUUID } from '@/utils/geek';
let _client: UniApp.SocketTask;
let _callback: { [key: string]: (data: any) => void } = {}
const enableJSON = true // 开启JSON解析消息，需要开启JSON解析消息才能开启uuid和event
const enableJSONEncoding = true // 开启JSON消息编码
const enableUUID = true // 需要接收信息中包含uuid字段，uuid优先级高于event
const enableEvent = true // 需要接收信息中包含event字段


export interface SocketConnectConfig extends UniApp.ConnectSocketOption {
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
    connect(config: SocketConnectConfig) {
        return new Promise((resolve, reject) => {
            const isToken = (config.headers || {}).isToken === false
            config.header = config.header || { 'content-type': 'application/json' }
            if (getToken() && !isToken) {
                config.header['Authorization'] = 'Bearer ' + getToken()
            }
            if(_client !== undefined){
                _client.close({})
            }
            _client = uni.connectSocket({
                url: config.url,
                header: config.header,
                method: config.method || 'GET',
                fail: reject
            });
            _client.onError(reject)
            _client.onOpen(resolve)
            _client.onMessage(res => {
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
            if(enableJSONEncoding){
                msg = JSON.stringify(msg)
            }
            if (enableUUID && uuid != undefined && uuid != "" && uuid != false) {
                if (uuid === true) {
                    msg.uuid = generateUUID()
                    _callback[msg.uuid] = resolve
                } else {
                    _callback[uuid] = resolve
                }
            }
            _client.send({
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
            let onclose = _client.onClose
            _client.onClose(res => {
                resolve(res)
                _client.onClose = onclose
            })
            _client.close({
                fail: reject
            })

        })
    },
    /**
     * 监听事件
     * @param event 要监听的事件
     * @returns 在回调函数中处理事件
     */
    subscribe(event: string) {
        return new Promise((resolve) => {
            _callback[event] = resolve
        })
    },
    /**
     * 取消监听事件
     * @param event 要取消监听的事件
     */
    unsubscribe(event: string) {
        delete _callback[event]
    },
    /**
     * 定义默认监听事件
     * @param callback 默认监听事件的处理函数
     */
    onMessage(callback: (data: any) => void) {
        _client.onMessage(res => {
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
        _client.onError(callback)
    },
    /**
     * 定义关闭事件
     * @param callback 默认关闭事件的处理函数
     */
    onClose(callback: () => void) {
        _client.onError(callback)
    }
};