import axios from 'axios'
import {ElLoading, ElMessage} from 'element-plus'
import {saveAs} from 'file-saver'
import {getToken} from '@/utils/auth'
import errorCode from '@/utils/errorCode'
import {blobValidate} from '@/utils/ruoyi'
import JSZip from 'jszip'

const baseURL = import.meta.env.VITE_APP_BASE_API
let downloadLoadingInstance

export default {
    name(name, isDelete = true) {
        var url = baseURL + "/common/download?fileName=" + encodeURIComponent(name) + "&delete=" + isDelete
        axios({
            method: 'get',
            url: url,
            responseType: 'blob',
            headers: {'Authorization': 'Bearer ' + getToken()}
        }).then((res) => {
            const isBlob = blobValidate(res.data)
            if (isBlob) {
                const blob = new Blob([res.data])
                this.saveAs(blob, decodeURIComponent(res.headers['download-filename']))
            } else {
                this.printErrMsg(res.data)
            }
        })
    },
    resource(resource) {
        var url = baseURL + "/common/download/resource?resource=" + encodeURIComponent(resource)
        axios({
            method: 'get',
            url: url,
            responseType: 'blob',
            headers: {'Authorization': 'Bearer ' + getToken()}
        }).then((res) => {
            const isBlob = blobValidate(res.data)
            if (isBlob) {
                const blob = new Blob([res.data])
                this.saveAs(blob, decodeURIComponent(res.headers['download-filename']))
            } else {
                this.printErrMsg(res.data)
            }
        })
    },
    zip(url, name) {
        var url = baseURL + url
        downloadLoadingInstance = ElLoading.service({text: "正在下载数据，请稍候", background: "rgba(0, 0, 0, 0.7)",})
        axios({
            method: 'get',
            url: url,
            responseType: 'blob',
            headers: {'Authorization': 'Bearer ' + getToken()}
        }).then((res) => {
            const isBlob = blobValidate(res.data)
            if (isBlob) {
                const blob = new Blob([res.data], {type: 'application/zip'})
                this.saveAs(blob, name)
            } else {
                this.printErrMsg(res.data)
            }
            downloadLoadingInstance.close()
        }).catch((r) => {
            console.error(r)
            ElMessage.error('下载文件出现错误，请联系管理员！')
            downloadLoadingInstance.close()
        })
    },
    fileListZip(fileUrlList, fileName) {
        downloadLoadingInstance = ElLoading.service({text: "正在下载数据，请稍候", background: "rgba(0, 0, 0, 0.7)",})
        // 初始化
        let fileList = []
        fileUrlList.forEach(fileUrl => {
            fileList.push(fileUrl)
        })
        const zip = new JSZip();
        const promises = fileList.map((fileUrl, index) => {
            return new Promise((resolve, reject) => {
                axios({
                    method: 'get',
                    url: baseURL + "/common/download/resource?resource=" + encodeURIComponent(fileUrl),
                    responseType: 'blob',  // 修改为blob类型以便处理
                    headers: {'Authorization': 'Bearer ' + getToken()}  // 添加认证头
                }).then(res => {
                    // 从响应头获取文件名，而不是从URL解析
                    const fileName = decodeURIComponent(res.headers['download-filename']) || 'file_' + index;

                    // 将blob转换为arraybuffer用于JSZip
                    const reader = new FileReader();
                    reader.onload = function () {
                        zip.file(fileName, reader.result, {binary: true});
                        resolve();
                    };
                    reader.readAsArrayBuffer(res.data);
                }).catch(err => {
                    reject('加载失败：' + err)
                    downloadLoadingInstance.close();
                })
            })
        })

        return Promise.all(promises).then(res => {
            fileName = fileName == null || fileName === '' ? fileName = '文件' + new Date().getTime() : fileName
            zip.generateAsync({type: 'blob'}).then(content => {
                this.saveAs(content, fileName + '_' + new Date().getTime() + '.zip')
            })
            downloadLoadingInstance.close();
        })
    },
    saveAs(text, name, opts) {
        saveAs(text, name, opts)
    },
    async printErrMsg(data) {
        const resText = await data.text()
        const rspObj = JSON.parse(resText)
        const errMsg = errorCode[rspObj.code] || rspObj.msg || errorCode['default']
        ElMessage.error(errMsg)
    }
}

