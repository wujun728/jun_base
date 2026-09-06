import { generateUUID } from "@/utils/geek";
import * as mqtt from "mqtt/dist/mqtt";
const enableJSONDecoding = true // 开启JSON解析消息，需要开启JSON解析消息才能开启uuid和event
const enableJSONEncoding = true // 开启JSON消息编码
const enableUUID = true // 需要接收信息中包含uuid字段，uuid优先级高于event

type MqttConnectOptions = {
    clean: boolean,
    connectTimeout: number,
    clientId: string, // 认证信息
    keepalive: number,
    protocolId?: string, // 默认 'MQTT'
    protocolVersion?: number, // 默认4
    username?: string,
    password?: string,
}
type MqttConnectConfig = {
    url?: string,
    options: MqttConnectOptions
}
interface MqttClient {
    connected:boolean,
    on: {
        (event: "connect", fun: () => void): void;
        (event: "reconnect", fun: () => void): void;
        (event: "disconnect", fun: () => void): void;
        (event: "offline", fun: () => void): void;
        (event: "message", fun: (topic: string, message: any, packet?: any) => void): void;
        (event: "packetsend", fun: (packet: any) => void): void;
        (event: "packetreceive", fun: (packet: any) => void): void;
        (event: "error", fun: (err: any) => void): void;
    };
    publish: {
        (topic: string, message: any, options?: any, callback?: (err: any) => void): void;
    }
    subscribe: {
        (topic: string, options?: any, callback?: (err: any, granted: { topic: string, qos: any }) => void): void
    }
    unsubscribe: {
        (topic: string, callback?: (err: any) => void): void
    }
    end(force?:boolean, options?:any, callback?: (err: any) => void): void
}

let _client: MqttClient;
let _callback: { [key: string]: (data: any) => void } = {}
let onmassage = (topic:string,message:string)=>{}
export default {
    /**
     * 连接websocket
     * 最简单的用法就是传入{url:"ws://demo"}
     * 当连接成功后触发回调函数
     */
    connect(config: MqttConnectConfig) {
        return new Promise<void>((resolve, reject) => {
            if(!_client || !_client.connected){
                _client = mqtt.connect(config.url, config.options);
                _client.on('connect', resolve);
                _client.on('error', reject);
                _client.on('message', (topic,message) => {
                    if (enableJSONDecoding) {
                        let data = JSON.parse(message)
                        if (enableUUID && (data || {}).uuid !== undefined) {
                            _callback[data.uuid](data)
                            delete _callback[data.uuid]
                        }
                    }
                    onmassage(topic,message)
                })
            }
        })
    },
    /**
     * 发送信息
     * @param msg 消息，会被处理成json字符串
     * @param uuid 唯一标识,可以传入uuid，也可以传入true自动生成uuid，flase表示该消息不需要单独处理
     * @returns
     */
    send(topic: string, msg: any, uuid: string | boolean = false, options: any = undefined) {
        return new Promise((resolve, reject) => {
            const useUUID = enableUUID && uuid != undefined && uuid != "" && uuid != false
            if (useUUID) {
                if (uuid === true) {
                    msg.uuid = generateUUID()
                    _callback[msg.uuid] = resolve
                } else {
                    _callback[uuid] = resolve
                }
            }
            if(enableJSONEncoding){
                msg = JSON.stringify(msg)
            }
            _client.publish(topic, msg, options, err => {
                if (err) {
                    reject(err)
                } else if (uuid === false) {
                    resolve(err)
                }
            })
        })
    },
    /**
     * 关闭连接
     * @returns 关闭连接的Promise，回调函数只会运行一次
     */
    close(force?: boolean, options?: any, callback?: (err: any) => void) {
        return new Promise<void>((resolve, reject) => {
            _client.end(force,options,()=>{
                resolve()
            })
        })
    },
    /**
     * 订阅事件
     * @param event 要监听的事件
     * @returns 在回调函数中处理事件
     */
    subscribe(topic: string, options: string | undefined = undefined) {
        return new Promise<void>((resolve, reject) => {
            _client.subscribe(topic, options, err => {
                if (err) {
                    reject(err)
                } else {
                    resolve()
                }
            })
        })
    },
    /**
     * 取消订阅事件
     * @param event 要取消监听的事件
     */
    unsubscribe(topic: string) {
        return new Promise<void>((resolve, reject) => {
            _client.unsubscribe(topic, err => {
                if (err) {
                    reject(err)
                } else {
                    resolve()
                }
            })
        })
    },
    /**
     * 定义默认监听事件
     * @param callback 默认监听事件的处理函数
     */
    onMessage(callback: (topic:string,message:string) => void) {
        onmassage = callback
    },
    /**
     * 定义异常事件
     * @param callback 默认异常事件的处理函数
     */
    onError(callback: (data: any) => void) {
        _client.on('error', callback)
    },
    /**
     * 定义关闭事件
     * @param callback 默认关闭事件的处理函数
     */
    onClose(callback: () => void) {
        _client.on('disconnect', callback)
    }
}