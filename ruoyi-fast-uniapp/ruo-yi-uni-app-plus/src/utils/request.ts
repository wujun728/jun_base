import store from '@/store'
import config from '@/config'
import { getToken } from '@/utils/auth'
import errorCode from '@/utils/errorCode'
import { toast, showConfirm, tansParams } from '@/utils/common'
import { RequestConfig, ResponseData } from '@/types/request'
import { encryptBase64, encryptWithAes, generateAesKey, decryptWithAes, decryptBase64 } from '@/utils/crypto';
import { encrypt, decrypt } from '@/utils/jsencrypt';

const encryptHeader = 'encrypt-key';
let timeout = 10000
const baseUrl = config.baseUrl
const clientId = config.clientID;
const appEncrypt = config.appEncrypt;

const request = <T>(config: RequestConfig):Promise<ResponseData<T>> => {
  // 是否需要设置 token
  const isToken = (config.headers || {}).isToken === false
  config.header = config.header || {}
  if (getToken() && !isToken) {
    config.header['Authorization'] = 'Bearer ' + getToken(),
	config.header['clientid'] = clientId
  }
  // get请求映射params参数
  if (config.params) {
    let url = config.url + '?' + tansParams(config.params)
    url = url.slice(0, -1)
    config.url = url
  }

  // 是否需要加密
  const isEncrypt = (config.headers || {}).isEncrypt === true;

  if (appEncrypt) {
    // 当开启参数加密
    if (isEncrypt && (config.method === 'POST' || config.method === 'PUT')) {
      // 生成一个 AES 密钥
      const aesKey = generateAesKey();
      config.header[encryptHeader] = encrypt(encryptBase64(aesKey));
      config.data = typeof config.data === 'object' ? encryptWithAes(JSON.stringify(config.data), aesKey) : encryptWithAes(config.data, aesKey);
    }
  }
  return new Promise((resolve, reject) => {
    uni.request({
      method: config.method || 'GET',
      timeout: config.timeout || timeout,
      url: config.baseUrl || baseUrl + config.url,
      data: config.data,
      header: config.header,
      dataType: 'json'
    }).then(response => {
      if (appEncrypt) {
        // 加密后的 AES 秘钥
        const keyStr = response.header[encryptHeader];
        // 加密
        if (keyStr != null && keyStr != '') {
          const data = response.data;
          // 请求体 AES 解密
          const base64Str = decrypt(keyStr);
          // base64 解码 得到请求头的 AES 秘钥
          const aesKey = decryptBase64(base64Str.toString());
          // aesKey 解码 data
          const decryptData = decryptWithAes(data as string, aesKey);
          // 将结果 (得到的是 JSON 字符串) 转为 JSON
          response.data = JSON.parse(decryptData);
        }
      }

      /* let [error, res] = response
       if (error) {
         toast('后端接口连接异常')
         reject('后端接口连接异常')
         return
       } */


      const res = response
      const data: ResponseData<T> = res.data as ResponseData<T>
      const code = data.code || 200
      // @ts-ignore
      const msg:string = errorCode[code] || data.msg || errorCode['default']
      if (code === 401) {
        showConfirm('登录状态已过期，您可以继续留在该页面，或者重新登录?').then(res => {
          if (res.confirm) {
            store.dispatch('LogOut').then(res => {
              uni.reLaunch({ url: '/pages/login' })
            })
          }
        })
        reject('无效的会话，或者会话已过期，请重新登录。')
      } else if (code === 500) {
        toast(msg)
        reject('500')
      } else if (code !== 200) {
        toast(msg)
        reject(code)
      }
      resolve(data)
    })
      .catch(error => {
        let { message } = error
        if (message === 'Network Error') {
          message = '后端接口连接异常'
        } else if (message.includes('timeout')) {
          message = '系统接口请求超时'
        } else if (message.includes('Request failed with status code')) {
          message = '系统接口' + message.substr(message.length - 3) + '异常'
        }
        toast(message)
        reject(error)
      })
  })
}

export default request
