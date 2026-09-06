import request from '@/utils/request'
import config from "@/config";
import { getToken } from "@/utils/auth";


/**初始化上传 */
export function initChunkUpload(fileName, fileSize) {
    return request({
        url: '/file/initUpload',
        method: 'post',
        params: {
            fileName,
            fileSize
        }
    })
}


/**上传分片视频 */
export function uploadChunk(uploadId, filePath, chunkIndex, formattedPath) {
    return new Promise((resolve, reject) => {
        uni.uploadFile({
            url: `${config.baseUrl}/file/uploadChunk`,
            filePath: formattedPath,
            name: "chunk",
            timeout: 60000, // 增加超时时间到60秒
            header: {
                Authorization: `Bearer ${getToken()}`,
            },
            formData: {
                uploadId: uploadId,
                filePath: filePath,
                partNumber: chunkIndex,
            },
            success: (res) => {
                try {
                    const resultData = JSON.parse(res.data);
                    resolve(resultData);
                } catch (error) {
                    console.error("解析上传结果失败:", error);
                    reject(error);
                }
            },
            fail: (err) => {
                console.error(`分片${chunkIndex}上传请求失败:`, err);
                reject(err);
            },
        });
    });
}


/**完成分片上传 */
export function completeChunkUpload(uploadId, filePath, fileSize, fileName, partETags) {
    return request({
        url: '/file/completeUpload',
        method: 'post',
        params: {
            uploadId,
            filePath,
            fileSize,
            fileName,
        },
        data: partETags
    })
}



















