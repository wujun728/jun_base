import axios from 'axios'
import { ElNotification } from 'element-plus'
import { saveAs } from 'file-saver'
import type { FileSaverOptions } from 'file-saver'
import { getToken } from '@/utils/auth'
import errorCode from '@/utils/errorCode'
import { blobValidate } from '@/utils/ruoyi'

const baseURL = import.meta.env.VITE_APP_BASE_API

export default {
  name(name: string, isDelete = true) {
    var url =
      baseURL +
      '/common/download?fileName=' +
      encodeURIComponent(name) +
      '&delete=' +
      isDelete
    axios({
      method: 'get',
      url: url,
      responseType: 'blob',
      headers: { Authorization: 'Bearer ' + getToken() },
    }).then(async (res) => {
      const isBlob = await blobValidate(res.data)
      if (isBlob) {
        const blob = new Blob([res.data])
        this.saveAs(blob, decodeURIComponent(res.headers['download-filename']))
      } else {
        this.printErrMsg(res.data)
      }
    })
  },
  resource(resource: string | number | boolean) {
    var url =
      baseURL +
      '/common/download/resource?resource=' +
      encodeURIComponent(resource)
    axios({
      method: 'get',
      url: url,
      responseType: 'blob',
      headers: { Authorization: 'Bearer ' + getToken() },
    }).then(async (res) => {
      const isBlob = await blobValidate(res.data)
      if (isBlob) {
        const blob = new Blob([res.data])
        this.saveAs(blob, decodeURIComponent(res.headers['download-filename']))
      } else {
        this.printErrMsg(res.data)
      }
    })
  },
  zip(url: string, name: string) {
    var url = baseURL + url
    axios({
      method: 'get',
      url: url,
      responseType: 'blob',
      headers: { Authorization: 'Bearer ' + getToken() },
    }).then(async (res) => {
      const isBlob = await blobValidate(res.data)
      if (isBlob) {
        const blob = new Blob([res.data], { type: 'application/zip' })
        this.saveAs(blob, name)
      } else {
        this.printErrMsg(res.data)
      }
    })
  },
  saveAs(text: Blob, name: string, opts?: FileSaverOptions) {
    saveAs(text, name, opts)
  },
  async printErrMsg(data: any) {
    const resText = await data.text()
    const rspObj = JSON.parse(resText)
    const errMsg = errorCode[rspObj.code] || rspObj.msg || errorCode['default']
    ElNotification({
      message: errMsg,
      type: 'error',
    })
  },
}
