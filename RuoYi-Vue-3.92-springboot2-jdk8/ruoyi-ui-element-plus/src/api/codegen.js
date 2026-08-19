import request from "@/utils/request";

const GENERATOR_BASE_URL = "/api/v1/codegen";

const GeneratorAPI = {
  /** 获取数据表分页列表 */
  getTablePage(params) {
    return request({
      url: `${GENERATOR_BASE_URL}/table`,
      method: "get",
      params,
    });
  },

  /** 获取代码生成配置 */
  getGenConfig(tableName) {
    return request({
      url: `${GENERATOR_BASE_URL}/${tableName}/config`,
      method: "get",
    });
  },

  /** 获取代码生成配置 */
  saveGenConfig(tableName, data) {
    return request({
      url: `${GENERATOR_BASE_URL}/${tableName}/config`,
      method: "post",
      data,
    });
  },

  /** 获取代码生成预览数据 */
  getPreviewData(tableName, pageType, type) {
    const params = {};
    if (pageType) {
      params.pageType = pageType;
    }
    if (type) {
      params.type = type;
    }
    return request({
      url: `${GENERATOR_BASE_URL}/${tableName}/preview`,
      method: "get",
      params: Object.keys(params).length ? params : undefined,
    });
  },

  /** 重置代码生成配置 */
  resetGenConfig(tableName) {
    return request({
      url: `${GENERATOR_BASE_URL}/${tableName}/config`,
      method: "delete",
    });
  },

  /**
   * 下载 ZIP 文件
   * @param url
   * @param fileName
   */
  download(tableName, pageType, type) {
    const params = {};
    if (pageType) {
      params.pageType = pageType;
    }
    if (type) {
      params.type = type;
    }
    return request({
      url: `${GENERATOR_BASE_URL}/${tableName}/download`,
      method: "get",
      params: Object.keys(params).length ? params : undefined,
      responseType: "blob",
    }).then((response) => {
      const contentDisposition = response?.headers?.["content-disposition"];
      let fileName = `${tableName}.zip`;
      if (contentDisposition) {
        // content-disposition: attachment; filename=xxx.zip
        const match = /filename\*?=(?:UTF-8''|")?([^;"]+)/i.exec(contentDisposition);
        if (match?.[1]) {
          try {
            fileName = decodeURIComponent(match[1]);
          } catch {
            fileName = match[1];
          }
        }
      }

      const blob = new Blob([response.data], { type: "application/zip" });
      const a = document.createElement("a");
      const url = window.URL.createObjectURL(blob);
      a.href = url;
      a.download = fileName;

      a.click();
      window.URL.revokeObjectURL(url);
    });
  },
};

export default GeneratorAPI;
