/**
 * 路由类型定义模块
 *
 * 提供路由相关的类型定义
 *
 * ## 主要功能
 *
 * - 路由元数据类型（标题、图标、权限等）
 * - 应用路由记录类型
 * - 路由配置扩展
 *
 * ## 使用场景
 *
 * - 路由配置类型约束
 * - 路由元数据定义
 * - 菜单生成
 * - 权限控制
 *
 * @module types/router/index
 * @author Art Design Pro Team
 */

import { RouteRecordRaw } from 'vue-router'

/**
 * 路由元数据接口
 * 定义路由的各种配置属性
 */
export interface RouteMeta extends Record<string | number | symbol, unknown> {
  /** 路由标题 */
  title: string
  /** 路由图标 */
  icon?: string
  /** 是否显示徽章 */
  showBadge?: boolean
  /** 文本徽章 */
  showTextBadge?: string
  /** 是否在菜单中隐藏 */
  isHide?: boolean
  /** 是否在标签页中隐藏 */
  isHideTab?: boolean
  /** 外部链接 */
  link?: string
  /** 是否为iframe */
  isIframe?: boolean
  /** 是否缓存 */
  keepAlive?: boolean
  /** 操作权限 */
  authList?: Array<{
    title: string
    authMark: string
  }>
  /** 是否为一级菜单 */
  isFirstLevel?: boolean
  /** 角色权限 */
  roles?: string[]
  /** 操作权限 */
  permissions?: string[]
  /** 是否固定标签页 */
  fixedTab?: boolean
  /** 激活菜单路径 */
  activePath?: string
  /** 是否为全屏页面 */
  isFullPage?: boolean
  /** 是否为权限按钮行 */
  isAuthButton?: boolean
  /** 权限标识 */
  authMark?: string
  /** 父级路径 */
  parentPath?: string
}

/**
 * 应用路由记录接口
 * 扩展 Vue Router 的路由记录类型
 */
export interface AppRouteRecord extends Omit<
  RouteRecordRaw,
  'meta' | 'children' | 'component'
> {
  id?: number
  meta: RouteMeta
  children?: AppRouteRecord[]
  component?: string | (() => Promise<any>)
}

/**
 * 若依路由类型定义
 * /**
* Note: 路由配置项
*
* hidden: true                     // 当设置 true 的时候该路由不会再侧边栏出现 如401，login等页面，或者如一些编辑页面/edit/1
* alwaysShow: true                 // 当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面
*                                  // 只有一个时，会将那个子路由当做根路由显示在侧边栏--如引导页面
*                                  // 若你想不管路由下面的 children 声明的个数都显示你的根路由
*                                  // 你可以设置 alwaysShow: true，这样它就会忽略之前定义的规则，一直显示根路由
* redirect: noRedirect             // 当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
* name:'router-name'               // 设定路由的名字，一定要填写不然使用<keep-alive>时会出现各种问题
* query: '{"id": 1, "name": "ry"}' // 访问路由的默认传递参数
* roles: ['admin', 'common']       // 访问路由的角色权限
* permissions: ['a:a:a', 'b:b:b']  // 访问路由的菜单权限
* meta : {
   noCache: true                   // 如果设置为true，则不会被 <keep-alive> 缓存(默认 false)
   title: 'title'                  // 设置该路由在侧边栏和面包屑中展示的名字
   icon: 'svg-name'                // 设置该路由的图标，对应路径src/assets/icons/svg
   breadcrumb: false               // 如果设置为false，则不会在breadcrumb面包屑中显示
   activeMenu: '/system/user'      // 当路由设置了该属性，则会高亮相对应的侧边栏。
 }
*/
export interface RuoYiAppRouteRecord extends Omit<
  RouteRecordRaw,
  'meta' | 'children' | 'component'
> {
  /** 是否在菜单中隐藏 */
  hidden?: boolean
  /** 是否在菜单中显示 */
  alwaysShow?: boolean
  /** 路由组件 */
  component?: string | (() => Promise<any>)
  /** 子路由 */
  children?: RuoYiAppRouteRecord[]
  /** 角色权限 */
  roles?: string[]

  /** 菜单权限 */
  permissions?: string[]

  meta: {
    noCache?: boolean // 如果设置为true，则不会被 <keep-alive> 缓存(默认 false)
    title?: string // 设置该路由在侧边栏和面包屑中展示的名字
    icon?: string // 设置该路由的图标，对应路径src/assets/icons/svg
    breadcrumb?: boolean // 如果设置为false，则不会在breadcrumb面包屑中显示
    activeMenu?: string // 当路由设置了该属性，则会高亮相对应的侧边栏。
    affix?: boolean // 是否固定标签页
  }
}
