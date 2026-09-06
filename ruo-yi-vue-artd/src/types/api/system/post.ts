/** 岗位分页查询参数 */
export interface PostQueryParams {
  /** 页码 */
  pageNum?: number
  /** 每页条数 */
  pageSize?: number
  /** 岗位编码 */
  postCode?: string
  /** 岗位名称 */
  postName?: string
  /** 状态 */
  status?: string
}

/** 岗位信息 */
export interface SysPost {
  /** 岗位编号 */
  postId?: number
  /** 岗位编码 */
  postCode?: string
  /** 岗位名称 */
  postName?: string
  /** 岗位排序 */
  postSort?: number
  /** 状态（0正常 1停用） */
  status?: '0' | '1'
  /** 创建时间 */
  createTime?: string
  /** 备注 */
  remark?: string
}
