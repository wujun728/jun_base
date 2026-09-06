/// <reference types="types" />
declare module 'virtual:svg-icons-register'
declare module 'jsencrypt/bin/jsencrypt.min'
declare type StrNum = string | number
// declare type Provide = import('@/plugins/index.ts').ProxyType
declare type BaseFormProps = import('@/BaseComponent/BaseForm/index.ts').Props
declare type BaseFormItem = import('@/BaseComponent/BaseForm/index.ts').FormItem
declare type BaseFormItems =
  import('@/BaseComponent/BaseForm/index.ts').FormItems
declare type BaseTableItem =
  import('@/BaseComponent/BaseTable/index.ts').TableItem
declare type BaseTableProps =
  import('@/BaseComponent/BaseTable/index.ts').BaseTableProps
declare type BaseTableColumnProps =
  import('@/BaseComponent/BaseTable/index.ts').TableColumnProps
type PermissionRouterRaw = import('vue-router').RouteRecordRaw
type RouteType = import('vue-router').RouteLocationNormalizedLoadedGeneric
type TagsType = Partial<RouteType & PermissionRouterRaw>
type AnyCss = Partial<CSSStyleDeclaration>
type ButtonsType =
  | 'refresh'
  | 'add'
  | 'edit'
  | 'delete'
  | 'columnDisplay'
  | 'comSearch'
interface Window {
  existLoading: boolean
  isSmallScreen: boolean
  unique: number
}

interface anyObj {
  [key: string]: any
}

interface downReturn {
  [key: string]: number
}

// api
interface ResBase {
  code: number
  msg: string
}

interface LoginApi {
  username: string
  password: string
  code: string
  uuid?: string
}
interface LoginRes extends ResBase {
  token: string
}
interface GetInfoRes extends ResBase {
  permissions: string[]
  roles: string[]
  user: {
    avatar: string | null
    userName: string
    nickName: string
  }
}

interface GetRoutersRes extends ResBase {
  data: RouterItem[]
}

interface RouterItem {
  name: string
  path: string
  hidden: boolean
  component: string
  meta: Meta
  redirect?: string
  alwaysShow?: boolean
  permissions?: string[]
  roles?: string[]
  children?: RouterItem[]
}

interface Meta {
  title: string
  icon: string
  noCache: boolean
  link: null | string
}

// pageContent
interface OrderType {
  orderByColumn: string | undefined
  isAsc: 'desc' | 'asc' | undefined
}
// ContextMenu
interface ContextMenuItem {
  disabled?: boolean
  icon: string
  label: string
  name: string
  menu?: TagsType
}
interface ContextMenuState {
  show: boolean
  axis: {
    x: number
    y: number
  }
  menu?: TagsType
  arrowAxis: number
}
// MenuTree
type MenuTreeOnlyOneChild = PermissionRouterRaw & {
  noShowingChildren?: boolean
  query?: anyObj
}
// GlobalSearch
interface searchPoolItem {
  path: string
  title: string[]
  icon: string
  query?: string
}
// store
interface UserState {
  token: string
  name: string
  avatar: string
  roles: string[]
  permissions: string[]
  nickName: string
  loginTime: string
}
// layoutStore
type LayoutMode = 'Default' | 'Classic' | 'Streamline' | 'Double'
interface LayoutState {
  showDrawer: boolean
  shrink: boolean
  layoutMode: LayoutMode
  mainAnimation: string
  isDark: boolean
  menuWidth: number
  menuDefaultIcon: string
  menuCollapse: boolean
  menuUniqueOpened: boolean
  menuShowTopBar: boolean
  menuBackground: string[]
  menuColor: string[]
  menuHoverBackground: string[]
  menuActiveBackground: string[]
  menuActiveColor: string[]
  menuTopBarBackground: string[]
  headerBarTabColor: string[]
  headerBarBackground: string[]
  headerBarHoverBackground: string[]
  headerBarTabActiveBackground: string[]
  headerBarTabActiveColor: string[]
}
// PermissionStore
interface PermiState {
  routes: PermissionRouterRaw[]
  addRoutes: PermissionRouterRaw[]
  defaultRoutes: PermissionRouterRaw[]
  topbarRouters: PermissionRouterRaw[]
  sidebarRouters: PermissionRouterRaw[]
}
// tagsViewStore
interface TagsViewState {
  visitedViews: TagsType[]
  cachedViews: string[]
  iframeViews: TagsType[]
}
// businessStore
interface BusinessState {
  listInfo: anyObj
  pageSearchControl: Record<string, boolean>
}

interface Payload {
  pageName: string
  requestUrl?: string
  requestBaseUrl?: string
  cacheKey?: string
  id?: StrNum
  delUrl?: string
  newData?: anyObj
  editInfo?: anyObj
  sendIdKey?: string
  queryInfo?: anyObj
}

// user
interface Department {
  deptId: number
  parentId?: number | null
  ancestors?: string | null
  deptName: string
  orderNum?: number | null
  leader?: string | null
  phone?: string | null
  email?: string | null
  status?: string | null
  delFlag?: string | null
  parentName?: string | null
  children: Department[]
  createBy?: string | null
  createTime?: string | null
  updateBy?: string | null
  updateTime?: string | null
  remark?: string | null
}

interface UserRow {
  createBy: string
  createTime: string
  updateBy?: string | null
  updateTime?: string | null
  remark: string
  userId: number
  deptId: number
  userName: string
  nickName: string
  email: string
  phonenumber: string
  sex: string
  avatar?: string
  password?: string | null
  status: '0' | '1'
  delFlag: string
  loginIp: string
  loginDate: string
  dept: Department
  roles?: Role[] // 如果有具体的角色信息，可以进一步定义Role接口
  roleIds?: number[] | null
  postIds?: number[] | null
  roleId?: number | null
  admin: boolean
  statusLoading?: boolean
}

interface UserRole {
  createBy?: string | null
  createTime?: string | null
  updateBy?: string | null
  updateTime?: string | null
  remark?: string | null
  roleId: number
  roleName: string
  roleKey: string
  roleSort: number
  dataScope: string
  menuCheckStrictly: boolean
  deptCheckStrictly: boolean
  status: string
  delFlag?: string | null
  flag: boolean
  menuIds?: number[] | null
  deptIds?: number[] | null
  permissions?: string[] | null
  admin: boolean
}

interface UserPost {
  createBy: string | null
  createTime: string
  updateBy?: string | null
  updateTime?: string | null
  remark: string
  postId: number
  postCode: string
  postName: string
  postSort: number
  status: string
  flag: boolean
}

interface UserGetInfo extends ResBase {
  data: UserRow
  postIds: number[]
  posts: UserPost[]
  roleIds: number[]
  roles: UserRole[]
}

// role
interface RoleInfo {
  createBy?: string | null
  createTime: string
  updateBy?: string | null
  updateTime?: string | null
  remark?: string
  roleId: number
  roleName: string
  roleKey: string
  roleSort: number
  dataScope: string
  menuCheckStrictly: boolean
  deptCheckStrictly: boolean
  status: string
  delFlag: string
  flag: boolean
  menuIds?: number[] | null
  deptIds?: number[] | null
  permissions?: string[] | null
  admin: boolean
}
// menu
interface MenuItem {
  createBy?: string | null
  createTime: string
  updateBy?: string | null
  updateTime?: string | null
  remark?: string | null
  menuId: number
  menuName: string
  parentName?: string | null
  parentId: number
  orderNum: number
  path: string
  component?: string | null
  query: string
  routeName: string
  isFrame: string
  isCache: string
  menuType: string
  visible: string
  status: string
  perms: string
  icon: string
  children: MenuItem[]
}

// dept
interface DeptItem {
  createBy?: string | null
  createTime: string
  updateBy?: string | null
  updateTime?: string | null
  remark?: string | null
  deptId: number
  parentId: number
  ancestors: string
  deptName: string
  orderNum: number
  leader: string
  phone: string
  email: string
  status: string
  delFlag: string
  parentName?: string | null
  children: DeptItem[]
}

// post
interface PostItem {
  createBy?: string | null
  createTime: string
  updateBy?: string | null
  updateTime?: string | null
  remark?: string
  postId: number
  postCode: string
  postName: string
  postSort: number
  status: string
  flag: boolean
}
// dict
interface DictItem {
  createBy?: string | null
  createTime: string
  updateBy?: string | null
  updateTime?: string | null
  remark?: string
  dictId: number
  dictName: string
  dictType: string
  status: string
}
interface DictEntry {
  createBy?: string | null
  createTime: string
  updateBy?: string | null
  updateTime?: string | null
  remark?: string
  dictCode: number
  dictSort: number
  dictLabel: string
  dictValue: string
  dictType: string
  cssClass?: string
  listClass?: string
  isDefault: string
  status: string
  default?: boolean // 这个字段在JSON中是 "default": true, 所以直接用boolean类型
}

// config
interface ConfigItem {
  createBy?: string | null
  createTime: string
  updateBy?: string | null
  updateTime?: string | null
  remark?: string
  configId: number
  configName: string
  configKey: string
  configValue: string
  configType: string
}

// notice
interface NoticeItem {
  createBy?: string | null
  createTime: string
  updateBy?: string | null
  updateTime?: string | null
  remark?: string
  noticeId: number
  noticeTitle: string
  noticeType: string
  noticeContent: string
  status: string
}

// operlog
interface OperlogItem {
  createBy?: string | null
  createTime?: string | null
  updateBy?: string | null
  updateTime?: string | null
  remark?: string | null
  operId: number
  title: string
  businessType: number
  businessTypes?: number[] | null // 假设这里可以是多个业务类型的数组
  method: string
  requestMethod: string
  operatorType: number
  operName: string
  deptName: string
  operUrl: string
  operIp: string
  operLocation: string
  operParam: string // 如果需要更复杂的参数解析，可以考虑进一步定义为对象类型
  jsonResult: string // 或者定义为更具体的响应对象类型
  status: number
  errorMsg?: string | null
  operTime: string
  costTime: number
}

// Logininfor
interface LogininforItem {
  createBy?: string | null
  createTime?: string | null
  updateBy?: string | null
  updateTime?: string | null
  remark?: string | null
  infoId: number
  userName: string
  status: string
  ipaddr: string
  loginLocation: string
  browser: string
  os: string
  msg: string
  loginTime: string
}
// online
interface OnlineItem {
  tokenId: string
  deptName?: string | null // 部门名称可能是可选的
  userName: string
  ipaddr: string
  loginLocation: string
  browser: string
  os: string
  loginTime: number // 时间戳，使用number类型
}

// job
interface JobItem {
  createBy?: string | null
  createTime: string
  updateBy?: string | null
  updateTime?: string | null
  remark?: string
  jobId: number
  jobName: string
  jobGroup: string
  invokeTarget: string
  cronExpression: string
  misfirePolicy: string
  concurrent: string
  status: string
  nextValidTime?: string // 下一次有效时间可能是可选的
}

// server
interface CPUInfo {
  cpuNum: number // CPU数量
  total: number // 总时间 (单位需要依据原始数据确定)
  sys: number // 系统占用百分比
  used: number // 已使用百分比
  wait: number // 等待百分比
  free: number // 空闲百分比
}

interface MemInfo {
  total: number // 总内存 (单位GB)
  used: number // 已使用内存 (单位GB)
  free: number // 空闲内存 (单位GB)
  usage: number // 使用率百分比
}

interface JVMInfo {
  total: number // 已分配的总内存 (单位MB)
  max: number // 最大可分配内存 (单位MB)
  free: number // 空闲内存 (单位MB)
  version: string // Java版本
  home: string // Java安装路径
  runTime: string // 运行时长
  startTime: string // 启动时间
  inputArgs: string[] // JVM启动参数
  used: number // 已使用的内存 (单位MB)
  usage: number // 内存使用率百分比
  name: string // JVM名称
}

interface SysInfo {
  computerName: string // 计算机名
  computerIp: string // IP地址
  userDir: string // 用户目录
  osName: string // 操作系统名称
  osArch: string // 操作系统架构
}

interface SysFileInfo {
  dirName: string // 目录名
  sysTypeName: string // 文件系统类型名
  typeName: string // 类型名
  total: string // 总空间 (单位GB)
  free: string // 空闲空间 (单位GB)
  used: string // 已使用空间 (单位GB)
  usage: number // 使用率百分比
}

interface SystemStatus {
  cpu: CPUInfo
  mem: MemInfo
  jvm: JVMInfo
  sys: SysInfo
  sysFiles: SysFileInfo[]
}
interface GetServerApi extends ResBase {
  data: SystemStatus
}
// cache
interface CacheInfo {
  info: {
    redis_version: string
    redis_mode: string
    tcp_port: string
    connected_clients: string
    uptime_in_days: string
    used_memory_human: string
    used_cpu_user_children: string
    maxmemory_human: string
    aof_enabled: string
    rdb_last_bgsave_status: string
    instantaneous_input_kbps: string
    instantaneous_output_kbps: string
  }
  dbSize: string
  commandStats: { value: string; name: string }[]
}
interface GetCacheApi extends ResBase {
  data: CacheInfo
}

// cacheList
interface CacheEntry {
  cacheName: string
  cacheKey: string
  cacheValue: string
  remark: string
}
interface CacheNameApi extends ResBase {
  data: CacheEntry[]
}

// gen
interface GenInfo {
  tableName: string
  tableComment: string
  className: string
  createTime: string
  updateTime: string
  tableId: number
  genType: string
  genPath: string
}
interface GenPreviewInfo {
  'vm/java/controller.java.vm'?: string
  'vm/java/domain.java.vm'?: string
  'vm/java/mapper.java.vm'?: string
  'vm/java/service.java.vm'?: string
  'vm/java/serviceImpl.java.vm'?: string
  'vm/js/api.js.vm'?: string
  'vm/sql/sql.vm'?: string
  'vm/vue/v3/index.vue.vm'?: string
  'vm/xml/mapper.xml.vm'?: string
}
interface GenPreviewApi extends ResBase {
  data: GenPreviewInfo
}

interface ImportTableEntry {
  tableName: string
  tableComment: string
  createTime: string
  updateTime: string
}

// prpfile
interface UserProfileApi extends ResBase {
  data: UserRow
  postGroup: string
  roleGroup: string
}
