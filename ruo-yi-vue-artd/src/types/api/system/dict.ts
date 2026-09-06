/** 字典分页查询参数 */
export interface DictTypeQueryParams {
  /** 页码 */
  pageNum?: number
  /** 每页条数 */
  pageSize?: number
  /** 字典名称 */
  dictName?: string
  /** 字典类型 */
  dictType?: string
  /** 状态 */
  status?: string
  /** 创建时间 */
  createTime?: string[] | string
  /** 创建时间范围 */
  params?: {
    beginTime?: string
    endTime?: string
  }
}

/** 字典数据查询参数 */
export interface DictDataQueryParams {
  /** 页码 */
  pageNum?: number
  /** 每页条数 */
  pageSize?: number
  /** 字典名称 */
  dictName?: string
  /** 字典标签 */
  dictLabel?: string
  /** 字典类型 */
  dictType?: string
  /** 状态 */
  status?: string
}

/** 字典类型信息 */
export interface SysDictType {
  /** 字典编号 */
  dictId?: number
  /** 字典名称 */
  dictName?: string
  /** 字典类型 */
  dictType?: string
  /** 状态（0正常 1停用） */
  status?: '0' | '1'
  /** 备注 */
  remark?: string
  /** 创建时间 */
  createTime?: string
}

/** 字典数据信息 */
export interface SysDictData {
  /** 字典编码 */
  dictCode?: number
  /** 字典标签 */
  dictLabel?: string
  /** 字典键值 */
  dictValue?: string
  /** 字典类型 */
  dictType?: string
  /** 样式属性 */
  cssClass?: string
  /** 表格字典样式 */
  listClass?: string
  /** 字典排序 */
  dictSort?: number
  /** 是否默认（Y是 N否） */
  isDefault?: 'Y' | 'N'
  /** 状态（0正常 1停用） */
  status?: '0' | '1'
  /** 备注 */
  remark?: string
  /** 创建时间 */
  createTime?: string
}
