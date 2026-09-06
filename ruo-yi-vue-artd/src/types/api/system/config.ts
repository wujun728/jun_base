/** 参数配置分页查询参数 */
export interface ConfigQueryParams {
  /** 页码 */
  pageNum?: number
  /** 每页条数 */
  pageSize?: number
  /** 参数名称 */
  configName?: string
  /** 参数键名 */
  configKey?: string
  /** 系统内置 */
  configType?: string
  /** 创建时间 */
  createTime?: string[] | string
  /** 创建时间范围 */
  params?: {
    beginTime?: string
    endTime?: string
  }
}

/** 参数配置信息 */
export interface SysConfig {
  /** 参数编号 */
  configId?: number
  /** 参数名称 */
  configName?: string
  /** 参数键名 */
  configKey?: string
  /** 参数键值 */
  configValue?: string
  /** 系统内置（Y是 N否） */
  configType?: 'Y' | 'N'
  /** 备注 */
  remark?: string
  /** 创建时间 */
  createTime?: string
}
