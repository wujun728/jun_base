import request from "@/utils/request";

const FileAPI = {
  /** 上传文件 （传入 FormData，上传进度回调） */
  upload(formData, onProgress) {
    return request({
      url: "/api/v1/files",
      method: "post",
      data: formData,
      headers: { "Content-Type": "multipart/form-data" },
      onUploadProgress: (progressEvent) => {
        if (progressEvent.total) {
          const percent = Math.round((progressEvent.loaded * 100) / progressEvent.total);
          onProgress?.(percent);
        }
      },
    });
  },

  /**
   * 上传文件
   */
  uploadFile(file) {
    const formData = new FormData();
    formData.append("file", file);
    return request({
      url: "/api/v1/files",
      method: "post",
      data: formData,
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  },

  /**
   * 删除文件
   *
   * @param filePath 文件完整路径
   */
  delete(filePath) {
    return request({
      url: "/api/v1/files",
      method: "delete",
      params: { filePath },
    });
  },

  /**
   * 下载文件
   * @param url
   * @param fileName
   */
  download(url, fileName) {
    return request({
      url,
      method: "get",
      responseType: "blob",
    }).then((res) => {
      const blob = new Blob([res.data]);
      const a = document.createElement("a");
      const url = window.URL.createObjectURL(blob);
      a.href = url;
      a.download = fileName || "下载文件";
      a.click();
      window.URL.revokeObjectURL(url);
    });
  },
};

export default FileAPI;
