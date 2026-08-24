import request from './request'
/**
 * 云文件--企业空间--空间列表
 * @returns {*}
 */
export function folderSpaceListApi() {
  return request.get(`cloud/space/list`)
}
/**
 * 云文件--回收站--删除(彻底删除)
 * @param id
 * @returns {*}
 */
export function folderDestroyApi(id) {
  return request.delete(`folder/destroy/${id}`)
}

/**
 * 云文件--回收站--批量删除(彻底删除)
 * @param data
 * @returns {*}
 */
export function folderAllDestroyApi(data) {
  return request.delete(`folder/destroy`, data)
}

/**
 * 云文件--回收站--恢复文件
 * @param id
 * @returns {*}
 */
export function folderDestroyFileApi(id) {
  return request.post(`folder/recover/${id}`)
}

/**
 * 云文件--回收站--批量恢复文件
 * @param data
 * @returns {*}
 */
export function folderAllDestroyFileApi(data) {
  return request.post(`folder/recover`, data)
}

/**
 * 云文件--回收站--列表
 * @param  data
 * @returns {*}
 */
export function folderRecycleListApi(data) {
  return request.get(`folder/recycle_lst`, data)
}

/**
 * 云文件--文件目录重命名
 * @param  id
 * @param data
 * @returns {*}
 */
export function folderRenameApi(id, data) {
  return request.post(`folder/rename/${id}`, data)
}

/**
 * 云文件--我的文件--设为常用文件夹
 * @param  id
 * @returns {*}
 */
export function folderShortcutApi(id) {
  return request.post(`folder/shortcut/${id}`)
}

/**
 * 云文件--我的文件--取消常用文件夹
 * @param  id
 * @returns {*}
 */
export function folderUnShortcutApi(id) {
  return request.post(`folder/unshortcut/${id}`)
}

/**
 * 云文件--我的文件--常用/共享文件数
 * @returns {*}
 */
export function folderTotalApi() {
  return request.get(`folder/total`)
}

/**
 * 云文件--我的文件--共享
 * @param  id
 * @param data
 * @returns {*}
 */
export function folderShareApi(id, data) {
  return request.post(`folder/share/${id}`, data)
}

/**
 * 云文件--我的文件--取消共享
 * @param  id
 * @returns {*}
 */
export function folderUnShareApi(id) {
  return request.delete(`folder/share/${id}`)
}

/**
 * 云文件--文件夹--列表
 * @returns {*}
 */
export function folderDirListApi() {
  return request.get(`folder/dir_lst`)
}

/**
 * 云文件--文件夹--移动文件/目录
 * @returns {*}
 */
export function folderMoveApi(id, data) {
  return request.post(`folder/move/${id}`, data)
}

/**
 * 云文件--文件夹--批量移动文件/目录
 * @returns {*}
 */
export function folderMoveAllApi(data) {
  return request.post(`folder/move`, data)
}

/**
 * 云文件--企业空间--创建空间
 * @returns {*}
 */
export function folderSpaceAddApi(data) {
  return request.post(`cloud/space/create`, data)
}

/**
 * 云文件--企业空间--编辑空间
 * @returns {*}
 */
export function folderSpaceRenameApi(id, data) {
  return request.post(`enterprise/folder_space/rename/${id}`, data)
}

/**
 * 云文件--企业空间--编辑权限获取
 * @returns {*}
 */
export function folderSpaceShareRuleApi(id, data) {
  return request.get(`cloud/space/rules/${id}`, data)
}

/**
 * 云文件--企业空间--子目录权限获取
 * @returns {*}
 */
export function folderSpaceSubRuleApi(fid, id) {
  return request.get(`cloud/file/${fid}/rules/${id}`)
}

/**
 * 云文件--企业空间--分享云文件
 * @returns {*}
 */
export function folderShare(fid, id, data) {
  return request.post(`enterprise/folder/${fid}/share/${id}`, data)
}

/**
 * 云文件--企业空间--编辑权限修改
 * @returns {*}
 */
export function folderSpaceShareEditApi(id, data) {
  return request.put(`cloud/space/update/${id}`, data)
}

/**
 * 云文件--企业空间--编辑子文件权限
 * @returns {*}
 */
export function folderSubRuleApi(fid, id, data) {
  return request.put(`cloud/file/${fid}/rules/${id}`, data)
}

/**
 * 云文件--企业空间--空间刪除
 * @returns {*}
 */
export function folderSpaceDeleteApi(id) {
  return request.delete(`cloud/space/delete/${id}`)
}

/**
   * 云文件--企业空间--空间详情列表
   * @returns {*}

   */
export function folderSpaceEntListApi(fid, data) {
  return request.get(`cloud/file/${fid}/list`, data)
}
/**
   * 云文件--企业空间--最近预览
   * @returns {*}

   */
export function folderRecentlyListApi(data) {
  return request.get(`cloud/space/lately`, data)
}

/**
   * 云文件--企业空间--使用模板文件(付费文档)
   * @returns {*}

   */
export function folderSpaceEntTemplateUseApi(data) {
  return request.post(`enterprise/folder/template_use`, data)
}

/**
   * 云文件--企业空间--文件夹创建
   * @returns {*}

   */
export function folderSpaceEntMakeApi(fid, data) {
  return request.post(`cloud/file/${fid}/folder`, data)
}

/**
   * 云文件--企业空间--文件创建
   * @returns {*}

   */
export function folderSpaceEntCreateApi(id, data) {
  return request.post(`cloud/file/${id}/create`, data)
}

/**
 * 云文件--企业空间--文件夹/文件 重命名
 * @returns {*}
 */
export function folderSpaceEntRenameApi(fid, id, data) {
  return request.post(`cloud/file/${fid}/rename/${id}`, data)
}

/**
 * 云文件--企业空间--回收站--列表
 * @returns {*}

 */
export function folderSpaceEntRecycleApi(data) {
  return request.get(`cloud/space/recycle`, data)
}
/**
 * 云文件--企业空间--回收站--彻底删除
 * @returns {*}

 */
export function folderForceDeleteApi(id) {
  return request.delete(`cloud/space/force_delete/${id}`)
}
/**
 * 云文件--企业空间--回收站--批量彻底删除
 * @returns {*}

 */
export function folderForceDeletesApi(data) {
  return request.delete(`cloud/space/force_deletes`, data)
}

/**
   * 云文件--企业空间--文件夹/文件 删除
   * @returns {*}

   */
export function folderSpaceEntDeleteApi(fid, id) {
  return request.delete(`cloud/file/${fid}/delete/${id}`)
}

/**
   * 云文件--企业空间-- 批量 文件夹/文件 删除
   * @returns {*}

   */
export function folderSpaceEntAllDeleteApi(fid, data) {
  return request.delete(`cloud/file/${fid}/batch_delete`, data)
}

/**
   * 云文件--企业空间--文件夹/文件恢复（从回收站）
   * @returns {*}

   */
export function folderSpaceEntRecoverApi(id) {
  return request.put(`cloud/space/recovery/${id}`)
}

/**
   * 云文件--企业空间--批量文件夹/文件恢复（从回收站）
   * @returns {*}

   */
export function folderSpaceEntAllRecoverApi(data) {
  return request.put(`cloud/space/batch_recovery`, data)
}

/**
   * 云文件--企业空间--文件夹/文件彻底删除（从回收站）
   * @returns {*}

   */
export function folderSpaceEntDestroyApi(fid, id) {
  return request.delete(`enterprise/folder/${fid}/destroy/${id}`)
}

/**
 * 云文件--企业空间--批量文件夹/文件彻底删除（从回收站）
 * @returns {*}
 */
export function folderSpaceEntAllDestroyApi(fid, data) {
  return request.delete(`enterprise/folder/${fid}/destroy`, data)
}

/**
   * 云文件--企业空间--空间资源详情
   * @returns {*} enterprise/folder/{fid}/dir_lst

   */
export function folderSpaceEntDetailApi(fid, id) {
  return request.get(`cloud/file/${fid}/info/${id}`)
}

/**
   * 云文件--企业空间--空间目录列表
   * @returns {*} enterprise/folder/{fid}/move/{id}

   */
export function folderSpaceEntDirApi(fid) {
  return request.get(`cloud/space/dir`)
}

/**
 * 云文件--企业空间--移动文件或文件夹
 * @returns {*}
 */
export function folderSpaceEntMoveApi(fid, id, data) {
  return request.post(`cloud/file/${fid}/move/${id}`, data)
}

/**
 * 云文件--企业空间--复制文件
 * @returns {*}
 */
export function folderSpaceEntCopyApi(fid, id, data) {
  return request.post(`cloud/file/${fid}/copy/${id}`, data)
}

/**
 * 云文件--企业空间--转让空间
 * @returns {*}
 */
export function folderSpaceEntTransferApi(id, data) {
  return request.post(`cloud/space/transfer/${id}`, data)
}

/**
 * 云文件--企业空间--批量移动文件或文件夹
 * @returns {*}
 */
export function folderSpaceEntAllMoveApi(fid, data) {
  return request.post(`cloud/file/${fid}/batch_move`, data)
}

/**
   * 云文件--企业空间--浏览文件
   * @returns {*}

   */
export function folderSpaceEntViewApi(fid, id) {
  return request.post(`enterprise/folder/${fid}/view/${id}`)
}

/**
 * 云文件-我的文件-列表
 * @param data
 * @returns {*}
 */
export function folderListApi(data) {
  return request.get(`folder/lst`, data)
}

/**
 * 云文件-共享给我-列表
 * @param data
 * @returns {*}
 */
export function folderShareListApi(data) {
  return request.get(`folder/share_lst`, data)
}

/**
 * 云文件-共享给我-列表
 * @param id
 * @returns {*}
 */
export function folderShareUserApi(id, data) {
  return request.get(`folder/share/user/${id}`, data)
}

/**
 * 云文件-我的文件-创建文件夹
 * @param data folder/create
 * @returns {*}
 */
export function folderMakeApi(data) {
  return request.post(`folder/make`, data)
}

/**
 * 云文件-我的文件-创建文件
 * @param data
 * @returns {*}
 */
export function folderCreateApi(data) {
  return request.post(`folder/create`, data)
}

/**
 * 云文件-我的文件-收藏文件
 * @param id
 * @returns {*}
 */
export function folderCollectApi(id) {
  return request.post(`folder/collect/${id}`)
}

/**
 * 云文件-我的文件-浏览文件
 * @param id
 * @returns {*} folder/copy/{id}
 */
export function folderViewApi(id) {
  return request.post(`folder/view/${id}`)
}

/**
 * 云文件-我的文件-复制文件
 * @param id
 *  @param data
 * @returns {*}
 */
export function folderCopyApi(id, data) {
  return request.post(`folder/copy/${id}`, data)
}

/**
 * 云文件-我的文件-文件详情
 * @param id
 * @returns {*}
 */
export function folderDetailApi(id) {
  return request.get(`folder/detail/${id}`)
}
/**
 * 云文件-文件详情V1.7
 * @param id
 * @returns {*}
 */
export function getFileInfoApi(fid, id) {
  return request.get(`cloud/file/${fid}/info/${id}`)
}

/**
 * 云文件-我的文件-取消收藏文件
 * @param id
 * @returns {*}
 */
export function folderUnCollectApi(id) {
  return request.post(`folder/uncollect/${id}`)
}

/**
 * 云文件-模糊搜索-列表
 * @param data
 * @returns {*}
 */
export function folderMatchtListApi(fid, data) {
  return request.get(`enterprise/folder/${fid}/match`, data)
}

/**
 * 云文件--列表--删除(从列表删除)
 * @param id
 * @returns {*}
 */
export function folderDeleteApi(id) {
  return request.delete(`folder/delete/${id}`)
}

/**
 * 云文件--列表--批量删除(从列表删除)
 * @param data
 * @returns {*}
 */
export function folderAllDeleteApi(data) {
  return request.delete(`folder/delete`, data)
}


/**
 * @description 获取附件详情接口
 */
export function getAttachInfoApi(id) {
  return request.get(`system/attach/info/${id}`)
}