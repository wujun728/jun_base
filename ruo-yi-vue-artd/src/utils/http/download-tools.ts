import axios from 'axios'
import { ElLoading, ElMessage } from 'element-plus'
import { saveAs } from 'file-saver'

import { useUserStore } from '@/store/modules/user'
import { blobValidate } from '@/utils/sys/ruoyi'

import { getErrorMessage } from './error'

const baseURL = import.meta.env.VITE_API_URL
let downloadLoadingInstance: ReturnType<typeof ElLoading.service>

export default {
  name(name: string, isDelete = true) {
    const url =
      baseURL +
      '/common/download?fileName=' +
      encodeURIComponent(name) +
      '&delete=' +
      isDelete
    axios({
      method: 'get',
      url: url,
      responseType: 'blob',
      headers: { Authorization: 'Bearer ' + useUserStore().accessToken }
    }).then((res: any) => {
      const isBlob = blobValidate(res.data)
      if (isBlob) {
        const blob = new Blob([res.data])
        this.saveAs(blob, decodeURIComponent(res.headers['download-filename']))
      } else {
        this.printErrMsg(res.data)
      }
    })
  },
  resource(resource: string) {
    const url =
      baseURL +
      '/common/download/resource?resource=' +
      encodeURIComponent(resource)
    axios({
      method: 'get',
      url: url,
      responseType: 'blob',
      headers: { Authorization: 'Bearer ' + useUserStore().accessToken }
    }).then((res: any) => {
      const isBlob = blobValidate(res.data)
      if (isBlob) {
        const blob = new Blob([res.data])
        this.saveAs(blob, decodeURIComponent(res.headers['download-filename']))
      } else {
        this.printErrMsg(res.data)
      }
    })
  },
  zip(url: string, name: string) {
    const downloadUrl = baseURL + url
    downloadLoadingInstance = ElLoading.service({
      text: '正在下载数据，请稍候',
      background: 'rgba(0, 0, 0, 0.7)'
    })
    axios({
      method: 'get',
      url: downloadUrl,
      responseType: 'blob',
      headers: { Authorization: 'Bearer ' + useUserStore().accessToken }
    })
      .then((res: any) => {
        const isBlob = blobValidate(res.data)
        if (isBlob) {
          const blob = new Blob([res.data], { type: 'application/zip' })
          this.saveAs(blob, name)
        } else {
          this.printErrMsg(res.data)
        }
        downloadLoadingInstance.close()
      })
      .catch((r: any) => {
        console.error(r)
        ElMessage.error('下载文件出现错误，请联系管理员！')
        downloadLoadingInstance.close()
      })
  },
  saveAs(text: Blob, name: string, opts?: any) {
    saveAs(text, name, opts)
  },
  async printErrMsg(data: any) {
    const resText = await data.text()
    const rspObj = JSON.parse(resText)
    const errMsg =
      getErrorMessage(rspObj.code) || rspObj.msg || getErrorMessage(500)
    ElMessage.error(errMsg)
  }
}
