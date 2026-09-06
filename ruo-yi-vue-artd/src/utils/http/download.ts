import { saveAs } from 'file-saver'

import { tansParams, blobValidate } from '@/utils/sys/ruoyi'
import { loadingService } from '@/utils/ui'

import { getErrorMessage } from './error'
import { axiosInstance } from './index'

// 通用下载方法
export default function download(
  url: string,
  params: any,
  filename: string,
  config?: any
) {
  loadingService.showLoading()
  return axiosInstance
    .post(url, params, {
      transformRequest: [
        (params: any) => {
          return tansParams(params)
        }
      ],
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      responseType: 'blob',
      ...config
    })
    .then(async (response: any) => {
      const data = response.data
      const isBlob = blobValidate(data)
      console.log(data, 'isBlob')
      if (isBlob) {
        const blob = new Blob([data])
        saveAs(blob, filename)
      } else {
        const resText = await data.text()
        const rspObj = JSON.parse(resText)
        const errMsg = getErrorMessage(rspObj.code) || rspObj.msg || ''
        ElMessage.error(errMsg)
      }
      loadingService.hideLoading()
    })
    .catch((r: any) => {
      console.error(r)
      ElMessage.error('下载文件出现错误，请联系管理员！')
      loadingService.hideLoading()
    })
}
