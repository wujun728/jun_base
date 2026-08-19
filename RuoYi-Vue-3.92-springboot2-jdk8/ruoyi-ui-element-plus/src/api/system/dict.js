import request from "@/utils/request";

const DICT_BASE_URL = "/api/v1/dicts";

const DictAPI = {
  //---------------------------------------------------
  // 字典相关接口
  //---------------------------------------------------

  /**
   * 字典分页列表
   * @param {Object} queryParams 查询参数
   * @returns {Promise} 字典分页结果
   */
  getPage(queryParams) {
    return request({
      url: `${DICT_BASE_URL}`,
      method: "get",
      params: queryParams,
    });
  },

  /**
   * 字典列表
   * @returns {Promise} 字典列表
   */
  getList() {
    return request({
      url: `${DICT_BASE_URL}/options`,
      method: "get",
    });
  },

  /**
   * 字典表单数据
   * @param {string} id 字典ID
   * @returns {Promise} 字典表单数据
   */
  getFormData(id) {
    return request({
      url: `${DICT_BASE_URL}/${id}/form`,
      method: "get",
    });
  },

  /**
   * 新增字典
   * @param {Object} data 字典表单数据
   * @returns {Promise} 添加结果
   */
  create(data) {
    return request({
      url: `${DICT_BASE_URL}`,
      method: "post",
      data,
    });
  },

  /**
   * 修改字典
   * @param {string} id 字典ID
   * @param {Object} data 字典表单数据
   * @returns {Promise} 修改结果
   */
  update(id, data) {
    return request({
      url: `${DICT_BASE_URL}/${id}`,
      method: "put",
      data,
    });
  },

  /**
   * 删除字典
   * @param {string} ids 字典ID，多个以英文逗号(,)分隔
   * @returns {Promise} 删除结果
   */
  deleteByIds(ids) {
    return request({
      url: `${DICT_BASE_URL}/${ids}`,
      method: "delete",
    });
  },

  //---------------------------------------------------
  // 字典项相关接口
  //---------------------------------------------------
  /**
   * 获取字典分页列表
   * @param {string} dictCode 字典编码
   * @param {Object} queryParams 查询参数
   * @returns {Promise} 字典分页结果
   */
  getDictItemPage(dictCode, queryParams) {
    return request({
      url: `${DICT_BASE_URL}/${dictCode}/items`,
      method: "get",
      params: queryParams,
    });
  },

  /**
   * 获取字典项列表
   * @param {string} dictCode 字典编码
   * @returns {Promise} 字典项列表
   */
  getDictItems(dictCode) {
    return request({
      url: `${DICT_BASE_URL}/${dictCode}/items/options`,
      method: "get",
    });
  },

  /**
   * 新增字典项
   * @param {string} dictCode 字典编码
   * @param {Object} data 字典项表单数据
   * @returns {Promise} 添加结果
   */
  createDictItem(dictCode, data) {
    return request({
      url: `${DICT_BASE_URL}/${dictCode}/items`,
      method: "post",
      data,
    });
  },

  /**
   * 获取字典项表单数据
   * @param {string} dictCode 字典编码
   * @param {string} id 字典项ID
   * @returns {Promise} 字典项表单数据
   */
  getDictItemFormData(dictCode, id) {
    return request({
      url: `${DICT_BASE_URL}/${dictCode}/items/${id}/form`,
      method: "get",
    });
  },

  /**
   * 修改字典项
   * @param {string} dictCode 字典编码
   * @param {string} id 字典项ID
   * @param {Object} data 字典项表单数据
   * @returns {Promise} 修改结果
   */
  updateDictItem(dictCode, id, data) {
    return request({
      url: `${DICT_BASE_URL}/${dictCode}/items/${id}`,
      method: "put",
      data,
    });
  },

  /**
   * 删除字典项
   * @param {string} dictCode 字典编码
   * @param {string} ids 字典项ID，多个以英文逗号(,)分隔
   * @returns {Promise} 删除结果
   */
  deleteDictItems(dictCode, ids) {
    return request({
      url: `${DICT_BASE_URL}/${dictCode}/items/${ids}`,
      method: "delete",
    });
  },
};

export default DictAPI;
