import request from "@/utils/request";

const APP_BASE_URL = "/api/v1/apps";

const AppAPI = {
  /**
   * 应用分页列表
   * @param {Object} queryParams 查询参数
   * @returns {Promise} 应用分页结果
   */
  getPage(queryParams) {
    return request({
      url: `${APP_BASE_URL}`,
      method: "get",
      params: queryParams,
    });
  },

  /**
   * 应用表单数据
   * @param {string} id 应用ID
   * @returns {Promise} 应用表单数据
   */
  getFormData(id) {
    return request({
      url: `${APP_BASE_URL}/${id}/form`,
      method: "get",
    });
  },

  /**
   * 新增应用
   * @param {Object} data 应用表单数据
   * @returns {Promise} 新增结果
   */
  create(data) {
    return request({
      url: `${APP_BASE_URL}`,
      method: "post",
      data,
    });
  },

  /**
   * 修改应用
   * @param {string} id 应用ID
   * @param {Object} data 应用表单数据
   * @returns {Promise} 修改结果
   */
  update(id, data) {
    return request({
      url: `${APP_BASE_URL}/${id}`,
      method: "put",
      data,
    });
  },

  /**
   * 删除应用（多个 ID 以英文逗号分隔）
   * @param {string} ids 应用ID（逗号分隔）
   * @returns {Promise} 删除结果
   */
  deleteByIds(ids) {
    return request({
      url: `${APP_BASE_URL}/${ids}`,
      method: "delete",
    });
  },

  /**
   * 修改应用状态
   * @param {string} id 应用ID
   * @param {number} status 状态（1-正常 0-禁用）
   * @returns {Promise} 修改结果
   */
  updateStatus(id, status) {
    return request({
      url: `${APP_BASE_URL}/${id}/status`,
      method: "put",
      data: { status },
    });
  },
};

export default AppAPI;
