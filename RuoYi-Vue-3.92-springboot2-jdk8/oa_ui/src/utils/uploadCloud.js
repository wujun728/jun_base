import * as qiniu from 'qiniu-js'
import Cos from 'cos-js-sdk-v5'
import request from '@/api/request'
import axios from 'axios'
import SettingMer from '@/libs/settingMer'
import { ossUpload, getUploadKeysApi, attachSaveApi, localUpload } from '@/api/public'
import { uploadByPieces } from './uploadChunk'
import helper from '@/libs/helper'
import Tips from '@/utils/tips'
import { getFileExtension } from '@/libs/public'

const sign = (method, publicKey, privateKey, md5, contentType, date, bucketName, fileName) => {
  const CryptoJS = require('crypto-js') // 这里使用了crypto-js加密算法库，安装方法会在后面说明
  const CanonicalizedResource = `/${bucketName}/${fileName}`
  const StringToSign = method + '\n' + md5 + '\n' + contentType + '\n' + date + '\n' + CanonicalizedResource // 此处的md5以及date是可选的，contentType对于PUT请求是可选的，对于POST请求则是必须的
  let Signature = CryptoJS.HmacSHA1(StringToSign, privateKey)
  Signature = CryptoJS.enc.Base64.stringify(Signature)
  return 'UCloud' + ' ' + publicKey + ':' + Signature
}

/**
 * @description 上传文件
 * @param {Object} file  文件
 * @param {string} uploadType 上传文件类型 1：只能上传图片格式，0：上传任意格式文件
 * @param {Object} option 上传文件合不同项目给后端传入的对象option.url:这个是上传是自定义接口
 */

export const uploader = async (file, uploadType, option) => {
  // 判断是否有自定义上传地址，没有的话就使用默认地址
  if (!option.url) {
    option.url = SettingMer.https + `/system/attach/save`
  }
  let fileSize = localStorage.getItem('sitedata')?JSON.parse(localStorage.getItem('sitedata')).global_attach_size : 30
  return new Promise(async (resolve, reject) => {
    const size = file.size
    if (size > fileSize * 1024 * 1024) {
      Tips.msgError(`文件大小不能大于${fileSize}MB`)
      return reject(false)
    }
    const type = getFileExtension(file.name).toLowerCase()

    
    // 只能上传图片格式
    if (parseInt(uploadType) === 1 && parseInt(helper.uploadTypes.indexOf(type)) === -1) {
      Tips.msgError(`只能上传${helper.checkImageType.join(',')}格式的文件`)
      return reject(false)
    }

    let apiRes = null
   // 获取api上传参数:云盘上传不需要调用
    if (option && !option.uploadRes) {
      apiRes = await getUploadKeysApi()
    } else {
      apiRes = option.uploadRes
    }

    // 失败回调
    if (!apiRes) {
      reject(false)
      return
    }

    // 成功回调
    baseUpload
      .videoUpload({
        type: apiRes.data.type,
        evfile: file,
        res: apiRes,
        option: option,
        uploading(status, progress) {}
      })
      .then((res) => {
        switch (apiRes.data.type) {
          case 'local': {
            resolve(res)
            break
          }

          default: {
            const fileData = {
              name: file.name,
              size: Math.round(file.size / 1024),
              type: file.type,
              url: res.url
            }
            let fileFormData = {}
            if(option.uplaodRes){
              fileFormData={
                pid: option.pid,
                ...fileData,
                upload_type: option.upload_type,
              
              }
            } else {
              fileFormData = {
                file: fileData,
                  way: option.way,
                  relation_type: option.relation_type,
                  relation_id: option.relation_id,
                  eid: option.eid
              }
            }
           
            request
              .post(option.url,fileFormData)
              .then((per) => {
                if (parseInt(per.status) === 200) {
                  Tips.msgSuccess('上传成功')
                } else {
                  Tips.msgError('上传失败')
                }
                resolve(per)
              })
              .catch((e) => {
                reject(e)
              })

         
          }
        }
      })
  })
}
export const baseUpload = {
  videoUpload(config) {
    const map = {
      COS: () => this.cosUpload(config.evfile, config.res.data, config.uploading, config.option),
      OSS: () => this.ossHttp(config.evfile, config.res, config.uploading, config.option),
      OBS: () => this.obsHttp(config.evfile, config.res, config.uploading, config.option),
      US3: () => this.us3Http(config.evfile, config.res, config.uploading, config.option),
      JDOSS: () => this.jdHttp(config.evfile, config.res, config.uploading, config.option),
      CTOSS: () => this.obsHttp(config.evfile, config.res, config.uploading, config.option),
      QINIU: () => this.qiniuHttp(config.evfile, config.res, config.uploading, config.option),
      local: () => this.uploadMp4ToLocal(config.evfile, config.res, config.uploading, config.option)
    }

    return new Promise((resolve, reject) => {
      map[config.type]()
        .then((res) => {
          resolve(res)
        })

        .catch((err) => {
          reject(err)
        })
    })
  },
  cosUpload(file, config, uploading) {
    let cos = new Cos({
      getAuthorization(options, callback) {
        callback({
          TmpSecretId: config.credentials.tmpSecretId, // 临时密钥的 tmpSecretId
          TmpSecretKey: config.credentials.tmpSecretKey, // 临时密钥的 tmpSecretKey
          XCosSecurityToken: config.credentials.sessionToken, // 临时密钥的 sessionToken
          ExpiredTime: config.expiredTime // 临时密钥失效时间戳，是申请临时密钥时，时间戳加 durationSeconds
        })
      }
    })
    let fileObject = file
    let Key = fileObject.name
    let pos = Key.lastIndexOf('.')
    let suffix = ''
    if (pos !== -1) {
      suffix = Key.substring(pos)
    }
    let filename = new Date().getTime() + suffix
    return new Promise((resolve, reject) => {
      cos.sliceUploadFile(
        {
          Bucket: config.bucket /* 必须 */,
          Region: config.region /* 必须 */,
          Key: filename /* 必须 */,
          Body: fileObject, // 上传文件对象
          onProgress: function (progressData) {
            uploading(progressData)
          }
        },
        function (err, data) {
          if (err) {
            reject({ msg: err })
          } else {
            resolve({ url: 'http://' + data.Location, ETag: data.ETag })
          }
        }
      )
    })
  },
  cosHttp(evfile, res, videoIng) {
    // 腾讯云
    // 对更多字符编码的 url encode 格式
    let camSafeUrlEncode = function (str) {
      return encodeURIComponent(str)
        .replace(/!/g, '%21')
        .replace(/'/g, '%27')
        .replace(/\(/g, '%28')
        .replace(/\)/g, '%29')
        .replace(/\*/g, '%2A')
    }
    let fileObject = evfile
    let Key = fileObject.name
    let pos = Key.lastIndexOf('.')
    let suffix = ''
    if (pos !== -1) {
      suffix = Key.substring(pos)
    }
    let filename = new Date().getTime() + suffix
    let data = res.data
    let XCosSecurityToken = data.credentials.sessionToken
    let url = data.url + camSafeUrlEncode(filename).replace(/%2F/g, '/')
    let xhr = new XMLHttpRequest()
    xhr.open('PUT', url, true)
    XCosSecurityToken && xhr.setRequestHeader('x-cos-security-token', XCosSecurityToken)
    xhr.upload.onprogress = function (e) {
      let progress = Math.round((e.loaded / e.total) * 10000) / 100
      videoIng(true, progress)
    }
    return new Promise((resolve, reject) => {
      xhr.onload = function () {
        if (/^2\d\d$/.test('' + xhr.status)) {
          var ETag = xhr.getResponseHeader('etag')
          videoIng(false, 0)
          resolve({ url: url, ETag: ETag })
        } else {
          reject({ msg: '文件 ' + filename + ' 上传失败，状态码：' + xhr.statu })
        }
      }
      xhr.onerror = function () {
        reject({ msg: '文件 ' + filename + '上传失败，请检查是否没配置 CORS 跨域规' })
      }
      xhr.send(fileObject)
      xhr.onreadystatechange = function () {}
    })
  },
  ossHttp(evfile, res, videoIng) {
    let that = this
    let fileObject = evfile
    let file = fileObject.name
    let pos = file.lastIndexOf('.')
    let suffix = ''
    if (pos !== -1) {
      suffix = file.substring(pos)
    }
    let filename = new Date().getTime() + suffix
    let formData = new FormData()
    let data = res.data
    // 注意formData里append添加的键的大小写
    formData.append('key', filename) // 存储在oss的文件路径
    formData.append('OSSAccessKeyId', data.accessid) // accessKeyId
    formData.append('policy', data.policy) // policy
    formData.append('Signature', data.signature) // 签名
    // 如果是base64文件，那么直接把base64字符串转成blob对象进行上传就可以了
    formData.append('file', fileObject)
    formData.append('success_action_status', 200) // 成功后返回的操作码
    let url = data.host
    let fileUrl = url + '/' + filename
    videoIng(true, 100)
    return new Promise((resolve, reject) => {
      axios.defaults.withCredentials = false
      axios
        .post(url, formData)
        .then(() => {
          // that.progress = 0;
          videoIng(false, 0)
          resolve({ url: fileUrl })
        })
        .catch((res) => {
          reject({ msg: res })
        })
    })
  },
  obsHttp(file, res, videoIng) {
    const fileObject = file
    const Key = fileObject.name
    const pos = Key.lastIndexOf('.')
    let suffix = ''
    if (pos !== -1) {
      suffix = Key.substring(pos)
    }
    const filename = new Date().getTime() + suffix
    const formData = new FormData()
    const data = res.data
    // 注意formData里append添加的键的大小写
    formData.append('key', filename)
    formData.append('AccessKeyId', data.accessid)
    formData.append('policy', data.policy)
    formData.append('signature', data.signature)
    formData.append('file', fileObject)
    formData.append('success_action_status', 200)
    const url = data.host
    const fileUrl = url + '/' + filename
    videoIng(true, 100)
    return new Promise((resolve, reject) => {
      axios.defaults.withCredentials = false
      axios
        .post(url, formData)
        .then(() => {
          videoIng(false, 0)
          resolve({ url: data.cdn ? data.cdn + '/' + filename : fileUrl })
        })
        .catch((res) => {
          reject({ msg: res })
        })
    })
  },
  us3Http(file, res, videoIng) {
    const fileObject = file
    const Key = fileObject.name
    const pos = Key.lastIndexOf('.')
    let suffix = ''
    if (pos !== -1) {
      suffix = Key.substring(pos)
    }
    const filename = new Date().getTime() + suffix
    const data = res.data

    const auth = sign('PUT', data.accessid, data.secretKey, '', fileObject.type, '', data.storageName, filename)
    return new Promise((resolve, reject) => {
      axios.defaults.withCredentials = false
      const url = `https://${data.storageName}.cn-bj.ufileos.com/${filename}`
      axios
        .put(url, fileObject, {
          headers: {
            Authorization: auth,
            'content-type': fileObject.type
          }
        })
        .then((res) => {
          videoIng(false, 0)
          resolve({ url: data.cdn ? data.cdn + '/' + filename : url })
        })
        .catch((res) => {
          reject({ msg: res })
        })
    })
  },
  qiniuHttp(evfile, res, videoIng, option) {
    const uptoken = res.data.token
    const file = evfile // Blob 对象，上传的文件
    const Key = file.name // 上传后文件资源名以设置的 key 为主，如果 key 为 null 或者 undefined，则文件资源名会以 hash 值作为资源名。
    const pos = Key.lastIndexOf('.')
    let suffix = ''
    if (pos !== -1) {
      suffix = Key.substring(pos)
    }
    const filename = new Date().getTime() + suffix
    const fileUrl = res.data.domain + '/' + filename
    const config = {
      useCdnDomain: true
    }
    const putExtra = {
      fname: '', // 文件原文件名
      params: {}, // 用来放置自定义变量
      mimeType: null // 用来限制上传文件类型，为 null 时表示不对文件类型限制；限制类型放到数组里： ["image/png", "image/jpeg", "image/gif"]
    }
    const observable = qiniu.upload(file, filename, uptoken, putExtra, config)
    return new Promise((resolve, reject) => {
      observable.subscribe({
        next: (result) => {
          const progress = Math.round(result.total.loaded / result.total.size)
          videoIng(true, progress)
          // 主要用来展示进度
        },
        error: (errResult) => {
          // 失败报错信息
          reject({ msg: errResult })
        },
        complete: (result) => {
          // 接收成功后返回的信息
          videoIng(false, 0)
          resolve({ url: res.data.cdn ? res.data.cdn + '/' + filename : fileUrl })
        }
      })
    })
  },
  // 京东云上传
  jdHttp(evfile, r, videoIng) {
    const fileObject = evfile // 获取的文件对象
    const formData = new FormData()
    formData.append('file', fileObject)
    return new Promise((resolve, reject) => {
      ossUpload(r.data.upload_url, formData)
        .then((res) => {
        
        })
        .catch((err) => {
          videoIng(true, 100)
          resolve(r.data)
        })
    })
  },

  // 本地上传
  uploadMp4ToLocal(evfile, res, videoIng, option) {
    return uploadByPieces(evfile, option, 'system/attach/upload')
  }
}
