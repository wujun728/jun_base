import { getToken } from "@/utils/auth";

const uploadImage = {
    fieldName: "file",
    //上传图片接口地址
    server: process.env.VUE_APP_BASE_API + "/file/operate/uploadfile?chunkFlag=false&chunkNumber=1&totalChunks=1",
    // 单个文件的最大体积限制，默认为 2M
    maxFileSize: 3 * 1024 * 1024, // 3M
    // 最多可上传几个文件，默认为 100
    maxNumberOfFiles: 10,
    // 选择文件时的类型限制，默认为 ['image/*'] 。如不想限制，则设置为 []
    allowedFileTypes: ["image/*"],
    meta: {
        // token: 'xxx',
        // otherKey: 'yyy'
    },
    // 将 meta 拼接到 url 参数中，默认 false
    metaWithUrl: false,
    headers: {
        Authorization: "Bearer " + getToken(),
    },
    withCredentials: false, // 跨域是否传递 cookie ，默认为 false
    timeout: 30 * 1000, // 超时时间，默认为 30 秒
    customInsert(res, insertFn) {
        if (res.code === 200) {
            console.log(`${res.data.name} 上传成功`);
        } else {
            console.log(`${res.data.name} 上传失败，请重新尝试`);
        }
        if (Array.isArray(res.data)) {
            res.data.forEach((item) => {
                let url = item?.fileId;
                if (url) {
                    url = process.env.VUE_APP_BASE_API + "/file/operate/downloadfile/v1?fileId=" + url;
                }
                if (url) insertFn(url, item.fileName, url);
            });
        }
    },
    // 单个文件上传成功之后
    onSuccess(file, res) {
        console.log(`${file.name} 上传成功`, res);
    },
    // 单个文件上传失败
    onFailed(file, res) {
        console.log(`${file.name} 上传失败`, res);
    },
    // 上传进度的回调函数
    onProgress(progress) {
        console.log("progress", progress);
        // progress 是 0-100 的数字
    },
    // 上传错误，或者触发 timeout 超时
    onError(file, err, res) {
        console.log(`${file.name} 上传出错`, err, res);
    },
}

const uploadVideo = {
    //上传视频接口地址
    fieldName: "file",
    server: process.env.VUE_APP_BASE_API + "/file/operate/uploadfile?chunkFlag=false&chunkNumber=1&totalChunks=1",
    // 单个文件的最大体积限制
    maxFileSize: 50 * 1024 * 1024, // 50M
    // 最多可上传几个文件，默认为 100
    maxNumberOfFiles: 10,
    // 选择文件时的类型限制，默认为 ['image/*'] 。如不想限制，则设置为 []
    allowedFileTypes: ["video/*"],
    // 自定义上传参数，例如传递验证的 token 等。参数会被添加到 formData 中，一起上传到服务端。
    meta: {
        // token: 'xxx',
        // otherKey: 'yyy'
    },
    // 将 meta 拼接到 url 参数中，默认 false
    metaWithUrl: false,
    headers: {
        Authorization: "Bearer " + getToken(),
    },
    // 跨域是否传递 cookie ，默认为 false
    withCredentials: false,
    // 超时时间，默认为 30 秒
    timeout: 60 * 1000, // 60 秒,
    customInsert(res, insertFn) {
        if (res.code === 200) {
            console.log(`${res.data.name} 上传成功`);
        } else {
            console.log(`${res.data.name} 上传失败，请重新尝试`);
        }
        if (Array.isArray(res.data)) {
            res.data.forEach((item) => {
                let url = item?.fileId;
                if (url) {
                    url = process.env.VUE_APP_BASE_API + "/file/operate/downloadfile/v1?fileId=" + url;
                }
                if (url) insertFn(url, item.fileName, url);
            });
        }
    },
    // 单个文件上传成功之后
    onSuccess(file, res) {
        console.log(`${file.name} 上传成功`, res);
    },
    // 单个文件上传失败
    onFailed(file, res) {
        console.log(`${file.name} 上传失败`, res);
    },
    // 上传进度的回调函数
    onProgress(progress) {
        console.log("progress", progress);
        // progress 是 0-100 的数字
    },
    // 上传错误，或者触发 timeout 超时
    onError(file, err, res) {
        console.log(`${file.name} 上传出错`, err, res);
    },
}

const menuConf = {
    //上传图片配置
    uploadImage: uploadImage,
    //上传视频配置
    uploadVideo: uploadVideo
}

export default menuConf