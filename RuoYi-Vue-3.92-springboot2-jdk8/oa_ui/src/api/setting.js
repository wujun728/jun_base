import request from './request'

/**
 * @description 获取协议列表
 * @param data
 */
export function agreementListApi(data) {
  return request.get(`system/treaty`, data)
}

/**
 * @description 获取协议详情
 * @param id
 */
export function agreementInfoApi(id) {
  return request.get(`system/treaty/${id}/edit`)
}

/**
 * @description 保存协议详情
 * @param id
 * @param data
 */
export function agreementUpdateApi(id, data) {
  return request.put(`system/treaty/${id}`, data)
}

/**
 * @description 组织结构 -- 列表
 * @param data
 */
export function configFrameApi(data) {
  return request.get(`config/frame`, data)
}

/**
 * @description 添加组织结构信息 -- 列表
 * @param data
 */
export function configFrameCreateApi(data) {
  return request.get(`config/frame/create`, data)
}

/**
 * @description 保存组织结构信息
 * @param data
 */
export function configFrameUpdataApi(data) {
  return request.post(`config/frame`, data)
}

/**
 * @description 删除组织结构
 * @param id
 */
export function configFrameDeleteApi(id) {
  return request.delete(`config/frame/${id}`)
}

/**
 * @description 获取修改组织结构接口
 * @param id
 */
export function configFrameEditApi(id) {
  return request.get(`config/frame/${id}/edit`)
}

/**
 * @description 修改组织编辑保存接口
 * @param id
 * @param data
 */
export function frameUpdataApi(id, data) {
  return request.put(`config/frame/${id}`, data)
}

/**
 * 下级人员及下级部门主管列表列表
 * @returns {*}
 */
export function frameUserApi(data) {
  return request.get(`config/frame/suser`, data)
}

/**
 * 商业授权
 * @returns {*}
 */
export function auth() {
  return request.get(`common/auth`)
}

/**
 * 商业授权相关
 * @returns {*}
 */
export function version() {
  return request.get(`common/version`)
}

/**
 * 组织架构-选择部门主管
 * @returns {*}
 */
export function departmentHeadApi(id) {
  return request.get(`config/frame/users/${id}`)
}

/**
 * 工作台设置表单获取
 * @returns {*}
 */
export function getConfigWorkbenchApi() {
  return request.get(`config/work_bench`)
}

/**
 * 获取职级类别列表接口
 * @returns {*}
 */
export function rankCateListApi(data) {
  return request.get(`rank_cate`, data)
}

/**
 * 获取职级类别表单创建接口
 * @returns {*}
 */
export function rankCateCreateApi() {
  return request.get(`rank_cate/create`)
}


/**
 * 编辑职位内容保存接口
 * @returns {*}
 */
export function rankJobsPutApi(id, data) {
  return request.put(`/jobs/${id}`, data)
}

/**
 * 添加职位内容保存接口
 * @returns {*}
 */
export function rankJobsAddApi(data) {
  return request.post(`/jobs`, data)
}

/**
 * 根据职位获取职级内容
 * @returns {*}
 */
export function rankJobsApi(data) {
  return request.get(`/rank`, data)
}


/**
 * 获取职级类别表单编辑接口
 * @returns {*}
 */
export function rankCateEditApi(id) {
  return request.get(`rank_cate/${id}/edit`)
}

/**
 * 删除职级类别接口
 * @returns {*}
 */
export function rankCateDeleteApi(id) {
  return request.delete(`rank_cate/${id}`)
}

/**
 * 获取职级表单创建接口
 * @returns {*} rank
 */
export function rankCreateApi(data) {
  return request.get(`rank/create`, data)
}

/**
 * 获取职级列表接口
 * @returns {*}rank/{rank}/edit
 */
export function rankListApi(data) {
  return request.get(`rank`, data)
}

/**
 * 获取职级表单编辑接口
 * @returns {*}
 */
export function rankEditApi(id) {
  return request.get(`rank/${id}/edit`)
}

/**
 * 修改职级状态
 * @param id
 * @param data
 * @return {*}
 */
export function rankStatusApi(id, data) {
  return request.get(`rank/${id}`, data)
}

/**
 * 删除职级接口
 * @returns {*}
 */
export function rankDeleteApi(id) {
  return request.delete(`rank/${id}`)
}

/**
 * 新建职位等级接口
 * @returns {*}
 */
export function rankLevelSaveApi(data) {
  return request.post(`rank_level`, data)
}

/**
 * 获取职位等级列表接口
 * @returns {*}
 */
export function rankLevelListApi(data) {
  return request.get(`rank_level`)
}

/**
 * 职位等级修改接口接口
 * @returns {*}
 */
export function rankLevelEditApi(id, data) {
  return request.put(`rank_level/${id}`, data)
}

/**
 * 职位等级修改接口接口
 * @returns {*}
 */
export function rankLevelDeleteApi(id) {
  return request.delete(`rank_level/${id}`)
}

/**
 * 批量修改职位区间
 * @returns {*}
 */
export function rankLevelBatchApi(batch) {
  return request.put(`rank_level/batch/${batch}`)
}

/**
 * 批量修改职位区间
 * @returns {*}
 */
export function rankLevelRelationApi(id, data) {
  return request.put(`rank_level/relation/${id}`, data)
}

/**
 * 移除关联职级
 * @returns {*}
 */
export function rankLevelRelationDeleteApi(id) {
  return request.delete(`rank_level/relation/${id}`)
}

/**
 * 获取未使用的直接
 * @returns {*}
 * @param {Number} id
 */
export function rankLevelRankApi(id) {
  return request.get(`rank_level/rank/${id}`)
}

/**
 * 获取基础设置分类
 * @returns {*}
 */
export function configCateApi(data) {
  return request.get(`config/cate`, data)
}

/**
 * 获取基础设置分类
 * @returns {*}
 */
export function configUpdateDataApi(data) {
  return request.get(`config/data/updateConfig`, data)
}

/**
 * 在线升级列表
 * @returns {*}
 */
export function getUpgradeList(data) {
  return request.get(`system/upgrade/list`, data)
}

/**
 * 在线升级数据
 * @returns {*}
 */
export function getUpgradeKey(data) {
  return request.get(`system/upgrade/key`, data)
}

/**
 * 在线升级日志
 * @returns {*}
 */
export function getUpgradeLog(data) {
  return request.get(`system/upgrade/log_list`, data)
}

/**
 * 在线升级协议
 * @returns {*}
 */
export function getUpgradeAgreement(data) {
  return request.get(`system/upgrade/agreement`, data)
}

/**
 * 开始升级
 * @returns {*}
 */
export function getUpgradeStart(package_key) {
  return request.post(`system/upgrade/start/${package_key}`)
}

/**
 * 升级进度
 * @returns {*}
 */
export function getUpgradeProgress() {
  return request.get(`system/upgrade/progress`)
}

/**
 * 获取消息列表
 * @returns {*}
 * @param {Object} data
 */
export function messageListApi(data) {
  return request.get(`system/message/list`, data)
}

/**
 * 获取消息分类
 * @returns {*}
 */
export function messageCateApi() {
  return request.get(`system/message/cate`)
}

/**
 * 同步消息
 * @returns {*}
 */
export function messageSyncApi() {
  return request.put(`system/message/sync`)
}

/**
 * 修改状态
 * @param {Object} data
 * @param {Number} id
 * @param {Number} type
 * @returns {*}
 */
export function messageStatusApi(id, type, data) {
  return request.put(`system/message/status/${id}/${type}`, data)
}

/**
 * 修改消息-修改时间
 * @param {Object} data
 * @param {Number} id
 * @returns {*}
 */
export function messageUpdateApi(id, data) {
  return request.put(`system/message/update/${id}`, data)
}

/**
 * 修改可订阅状态
 * @param {Object} data
 * @param {Number} id
 * @returns {*}
 */
export function messageSubscribeApi(id, data) {
  return request.put(`system/message/subscribe/${id}`, data)
}

/**
 * 获取分类列表接口
 * @param {Object} data
 * @returns {*}
 */
export function configQuickCateApi(data) {
  return request.get(`config/quickCate`, data)
}

/**
 * 获取添加分类接口
 * @returns {*}
 */
export function configQuickCateCreateApi() {
  return request.get(`config/quickCate/create`)
}

/**
 * 删除分类接口
 * @param {Number} id
 * @returns {*}
 */
export function configQuickCateDeleteApi(id) {
  return request.delete(`config/quickCate/${id}`)
}

/**
 * 获取修改分类接口
 * @param {Number} id
 * @returns {*}
 */
export function configQuickCateEditApi(id) {
  return request.get(`config/quickCate/${id}/edit`)
}

/**
 * 获取快捷入口列表接口
 * @param {Object} data
 * @returns {*}
 */
export function configQuickListApi(data) {
  return request.get(`config/quick`, data)
}

/**
 * 获取添加快捷入口接口
 * @param {Object} data
 * @return {*}
 */
export function configQuickCreateApi(data) {
  return request.get(`config/quick/create`, data)
}

/**
 * 删除快捷入口接口
 * @param {Number} id
 * @return {*}
 */
export function configQuickDeleteApi(id) {
  return request.delete(`config/quick/${id}`)
}

/**
 * 获取修改快捷入口接口
 * @param {Number} id
 * @return {*}
 */
export function configQuickEditApi(id) {
  return request.get(`/config/quick/${id}/edit`)
}

/**
 * 快捷入口隐藏/展示接口
 * @param {Number} id
 * @param data
 * @return {*}
 */
export function configQuickShowApi(id, data) {
  return request.get(`config/quick/${id}`, data)
}

/**
 *储存配置-获取云储存配置头
 */
export function storageConfigApi() {
  return request.get(`config/storage/config`)
}

/**
 *储存配置-获取云储存配置头
 */
export function storageSwitchApi(data) {
  return request.post(`config/storage/config`, data)
}

/**
 * @description 储存配置-获取云储存配置表单
 */
export function addConfigApi(type) {
  return request.get(`config/storage/form/${type}`)
}

/**
 * @description 储存配置-获取云存储创建表单
 */
export function addStorageApi(type) {
  return request.get(`config/storage/create/${type}`)
}

/**
 * @description 储存配置-获取云存储列表
 */
export function storageListApi(data) {
  return request.get(`config/storage/index`, data)
}

/**
 * @description 储存配置-删除云存储列表
 */
export function storageDelApi(id) {
  return request.delete(`config/storage/${id}`)
}

/**
 * @description 储存配置-同步空间
 */
export function storageSynchApi(type) {
  return request.get(`config/storage/sync/${type}`)
}

/**
 * @description 储存配置-修改状态
 */
export function storageStatusApi(id) {
  return request.put(`config/storage/status/${id}`)
}

/**
 * @description 储存配置-修改空间域名
 */
export function editStorageApi(id) {
  return request.get(`config/storage/domain/${id}`)
}

/**
 * @description 储存配置-获取缩略图
 */
export function positionInfoApi() {
  return request.get(`config/storage/method`)
}

/**
 * @description 储存配置-保存缩略图
 */
export function positionPostApi(data) {
  return request.put(`config/storage/save_basic`, data)
}

/**
 * @description 储存配置切换
 */
export function saveType(type) {
  return request.put(`config/storage/save_type/${type}`)
}

/**
 * @description 获取防火墙配置
 */
export function getFirewallConfigApi() {
  return request.get(`config/data/firewall`)
}

/**
 * @description 防火墙配置保存
 */
export function saveFirewallConfigApi(data) {
  return request.put(`config/data/firewall`, data)
}

/**
 * @description 获取推送消息
 */
export function getMessageDetailsApi(id) {
  return request.get(`system/message/find/${id}`)
}

/**
 * @description 修改推送消息
 */
export function upDateMessageApi(id, data) {
  return request.put(`system/message/update/${id}`, data)
}

/**
 * @description 修改推送消息状态
 * @param id
 * @param type
 * @returns {*}
 */
export function putStatusMessageApi(id, type, data) {
  return request.put(`system/message/status/${id}/${type}`, data)
}

/**
 * @description 推送记录列表
 */
export function getCompanyMessageApi(data) {
  return request.get(`company/message/list`, data)
}
