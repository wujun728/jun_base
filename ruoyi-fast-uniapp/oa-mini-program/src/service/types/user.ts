export interface User {
  id?: number;
  token?: string;
}
export interface Users {
  users?:{ [key: string]: any };
}
export interface IsPhoneX {
  isIphoneX?: boolean;
  navHeight?: Number;
  menuRight?: Number;
  menuTop?: Number;
  menuHeight?: Number;
}
export interface UserInfo {
  user?:object
}
/**
 * SysUserDto对象，用户管理表
 */
export interface UpdateUserInfo {
  /**
   * 头像
   */
  avatar?: string;
  /**
   * 部门id
   */
  deptId?: number;
  deptIds?: number[];
  deptNames?: string;
  /**
   * 性别（1：男，2：女）
   */
  gender?: string;
  /**
   * 是否设置签名（0：否，1：是）
   */
  haveSignature?: string;
  /**
   * 个人简介
   */
  introduction?: string;
  /**
   * 是否为主管（0：否，1：是）
   */
  isManager?: string;
  /**
   * 姓名
   */
  name?: string;
  pageNum?: number;
  pageSize?: number;
  /**
   * 账号密码
   */
  password?: string;
  /**
   * 个人照片（逗号分割）
   */
  photo?: string;
  fileList?: Record<string, string>[];
  photoFiles?: Record<string, string>[];
  /**
   * 岗位id
   */
  postId?: number;
  postIdStr?: string;
  /**
   * 角色id
   */
  roleId?: number;
  /**
   * 个人签名
   */
  signature?: string;
  /**
   * 状态（0：启用，1：禁用）
   */
  status?: string;
  /**
   * 手机号
   */
  telephone?: string;
  /**
   * 用户id
   */
  userId?: number;
  /**
   * 批量删除id
   */
  userIds?: number[];
  /**
   * 用户名（默认手机号）
   */
  username?: string;
}

/**
 * SysUser对象，用户管理表
 */
export interface Login {
  code?: number;
  data?: any;
  message?: string;
  /**
   * 头像
   */
  avatar?: string;
  deptIds?: number[];
  deptIdStr?: string;
  /**
   * 性别（1：男，2：女）
   */
  gender?: string;
  /**
   * 是否设置签名（0：否，1：是）
   */
  haveSignature?: string;
  /**
   * 个人简介
   */
  introduction?: string;
  /**
   * 是否为主管（0：否，1：是）
   */
  isManager?: string;
  /**
   * 姓名
   */
  name?: string;
  /**
   * 账号密码
   */
  password?: string;
  /**
   * 个人照片（逗号分割）
   */
  photo?: string;
  postIdStr?: string;
  /**
   * 个人签名
   */
  signature?: string;
  /**
   * 状态（0：启用，1：禁用）
   */
  status?: string;
  /**
   * 手机号
   */
  telephone?: string;
  /**
   * 用户id
   */
  userId?: number;
  /**
   * 用户名（默认手机号）
   */
  username?: string;
  /**
   * token
   */
  token?: string;
}
/**
 * ReportSubmitListDTO对象，我提交的前端传参
 */
export interface MyReport {
  /**
   * 发布日期止
   */
  endTime?: string;
  /**
   * 页数
   */
  pageNum: number;
  /**
   * 每页数量
   */
  pageSize: number;
  /**
   * 发布日期始
   */
  startTime?: string;
  /**
   * 汇报模版id
   */
  templateId?: number | string;
  pages?:number
  records?: []
  onlyUnread?:string
}
export interface Password {
  /**
   * password
   */
  password: string;
}