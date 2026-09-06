/**
 * 溜井数字化监控管理平台 - TypeScript 类型定义
 *
 * @description 定义所有 API 数据结构和业务数据类型
 * @author zhangzhen
 * @version 0.1.0
 */

/**
 * 报警统计摘要
 */
export interface WarningSummary {
  total: number
  wcl: number
  cl: number
  ljwcl: number
}

/**
 * 报警详情项
 */
export interface WarningItem {
  id?: string
  type: string
  name: string
  instruction?: string
  ip?: string
  remark?: string
  section?: string
  status?: number | string
  video?: string
  warningTime?: string
  warning_time: string
  handle?: string
  handleTime?: string
  state: number | string
  roter?: string
  router?: string
  route?: string
  img?: string
  imgUrl?: string
  img_url?: string
  image?: string
  imageUrl?: string
  image_url?: string
  pic?: string
  pic_url?: string
  picture?: string
  picture_url?: string
  warning_img?: string
  warningImg?: string
  snapshot?: string
}

/**
 * 设备状态
 */
export interface DeviceStatus {
  name: string
  total: number
  online: number
  offline: number
}

/**
 * 矿车统计
 */
export interface MineCarStats {
  online: number
  total: number
  offline: number
}

/**
 * 柱状图数据
 */
export interface BarPayload {
  total: number
  xA: string[]
  yA: number[]
}

/**
 * 出入料统计汇总
 */
export interface LogsTotal {
  rTotal: number
  rCars: number
  cTotal: number
  cCars: number
}

/**
 * 溜井出入料记录
 */
export interface WellLog {
  name: string
  rTotal: number
  cTotal: number
}

/**
 * 出入料记录数据
 */
export interface LogsPayload {
  total: LogsTotal
  detail: WellLog[]
}

/**
 * 部门选择选项
 */
export interface SelectOption {
  lable: string
  value: string
  dept_sort?: number
}

/**
 * 料位校验高度请求参数
 */
export interface CheckHeightParams {
  id: string | number
  nowQuantity: number | string
}

/**
 * 溜井料位数据
 */
export interface MaterialLevel {
  id?: string | number
  lname: string
  ton: number
  shitype: string
  shitypename?: string
  status: string | number
  all_quantity: number
  now_quantity: number
  dept?: string
}

/**
 * 摄像头项
 */
export interface CameraItem {
  label: string
  url: string
}

/**
 * 摄像头分组
 */
export interface CameraCategory {
  label: string
  children: CameraItem[]
}

/**
 * 矿车日志项
 */
export interface CarsLogItem {
  id: number | string
  fulls?: string | number
  persons?: string
  lname?: string
  company?: string
  createTime?: string
  car?: string
  cars?: string
  shitype?: string | number
  imgpath?: string
  videopath?: string
  rate?: string | number
  timecha?: string | number
  yiwu?: string
  bigs?: string
  name?: string
}

/**
 * 矿车日志数据
 */
export interface CarsLogsPayload {
  content: CarsLogItem[]
  totalElements: number
}

/**
 * 矿车日志查询参数
 */
export interface CarsLogsParams {
  page: number
  size: number
  sort?: string
  fulls?: string
  lname?: string
  car?: string
  createTime?: string
}

/**
 * 出料记录项
 */
export interface OutLogItem {
  plateNumber: string
  taskTime: string
  unloadingTime: string
  team: string
  car_type: string
  wellingPoint: string
  instruction: string
  mineralType: string
  welling_point_id: string
  miners: string
  totalTonnage: string
  create_time: string
  weighingTime: string
  vehicles: string
  leader: string
  schedule: string
  releaseDate: string
  releaseConfirmation: string
  ydNo: string
  discharge: string
}

/**
 * 出料记录数据
 */
export interface OutLogsPayload {
  content: OutLogItem[]
  totalElements: number
}

/**
 * 出料记录查询参数
 */
export interface OutLogsParams {
  page: number
  size: number
  sort?: string
}
