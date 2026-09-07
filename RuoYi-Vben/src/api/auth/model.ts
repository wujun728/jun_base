export interface RoleInfo {
  roleName: string;
  value: string;
}

/**
 * @description: 验证码登录需要用到的参数
 * @param code 验证码 可选(未开启验证码情况)
 * @param uuid 验证码ID 可选(未开启验证码情况)
 * @param username 用户名
 * @param password 密码
 */
export interface SimpleLoginParams {
  code?: string;
  uuid?: string;
  username: string;
  password: string;
}

/**
 * @description 用户信息 只保留能用到的
 * @param userId 用户id
 * @param userName 用户名
 * @param nickName 昵称
 * @param avatar 头像
 * @param createTime 创建时间
 */
export interface UserInfo {
  userId: number;
  userName: string;
  nickName: string;
  avatar?: string;
  createTime: string;
}
/**
 * @param roles 角色列表
 * @param user 用户信息
 * @param permissions 权限列表
 */
export interface UserInfoResult {
  roles: string[];
  user: UserInfo;
  permissions: string[];
}

/***
 * @description: 菜单meta
 * @param title 菜单名
 * @param icon 菜单图标
 * @param noCache 是否不缓存
 * @param link 外链链接
 */
export interface MenuMeta {
  title: string;
  icon: string;
  noCache: boolean;
  link?: string;
}

/**
 * @description: 菜单
 * @param name 菜单名
 * @param path 菜单路径
 * @param hidden 是否隐藏
 * @param component 组件名称 Laout
 * @param alwaysShow 总是显示
 * @param meta 路由信息
 * @param children 子路由信息
 */
export interface Menu {
  name: string;
  path: string;
  hidden: boolean;
  redirect?: string;
  component: string;
  alwaysShow?: boolean;
  meta: MenuMeta;
  children: Menu[];
}
