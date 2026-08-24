
import SparkMD5 from 'spark-md5'
import request from '@/api/request'
export const uploadByPieces = (file, option, url) => {
  return new Promise(async (resolve, reject) => {
    // 读取文件的md5
    let fileMD5
    let fileRederInstance = new FileReader()
    fileRederInstance.readAsBinaryString(file)
    fileRederInstance.addEventListener('load', (e) => {
      let fileBolb = e.target.result
      fileMD5 = SparkMD5.hashBinary(fileBolb)
      readChunkMD5(file, fileMD5, option)
    })

    // 针对每个文件进行chunk处理
    const readChunkMD5 = async (file, md5, option) => {
      const chunkSize = 5 * 1024 * 1024 // 5MB一片
      const chunkCount = Math.ceil(file.size / chunkSize) // 总片数
      for (var i = 0; i < chunkCount; i++) {
        const { chunk } = getChunkInfo(file, i, chunkSize)
        await uploadChunk({ chunk, currentChunk: i, chunkCount, md5 }, option)
      }
    }
    const getChunkInfo = (file, currentChunk, chunkSize) => {
      let start = currentChunk * chunkSize
      let end = Math.min(file.size, start + chunkSize)
      let chunk = file.slice(start, end)
      chunk = blobToFile(chunk, file.name)
      return { start, end, chunk }
    }
    // Blob 转 File
    const blobToFile = (blob, fileName) => {
      const file = new File([blob], fileName, { type: blob.type })
      return file
    }
    const uploadChunk = (chunkInfo, option) => {
      // 创建formData对象，下面是结合不同项目给后端传入的对象。
      let formData = new FormData()
      if (Object.keys(option).length > 0) {
        for (let key in option) {
          formData.append(key, option[key])
        }
      }
      formData.append('file', chunkInfo.chunk)
      formData.append('md5', chunkInfo.md5)
      formData.append('chunk_index', chunkInfo.currentChunk)
      formData.append('chunk_total', chunkInfo.chunkCount)
      request
        .post(url, formData)
        .then((res) => {
          if (res.data.src) {
            resolve(res)
          } else if (res.status !== 200) {
            reject(res)
          }
        })
        .catch((e) => {
          reject(e)
        })
    }
  })
}
