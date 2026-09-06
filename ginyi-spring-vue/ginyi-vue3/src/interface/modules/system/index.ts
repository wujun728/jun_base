/**
 * 项目配置
 */
import {Component} from "vue";

export interface IProject {
    // 开发环境请求地址
    devBaseURL: string | undefined;
    // 生产环境请求地址
    prodBaseURL: string | undefined;
    // 项目logo
    logo?: string | undefined;
    // 项目名称 = title + name
    title?: string | undefined;
    // 项目名称 = title + name
    name?: string | undefined;
    // 作者
    author?: string | undefined;
}

/**
 * pinia - 系统状态
 */
export interface ISystemState {
    // 系统深色主题
    darkTheme?: boolean | undefined;
    // 系统主题色
    themeColor?: string | undefined;
    // 系统主题色列表
    themeColorList?: Array<string>;
    // 客户端宽度
    clientWidth?: number | undefined;
    // 客户端高度
    clientHeight?: number | undefined;
    // Layout - header 的高度
    layoutHeaderHeight?: number | undefined;
    // Layout - footer 的高度
    layoutFooterHeight?: number | undefined;
    // 侧边栏折叠
    collapsed?: boolean | undefined;
    // logo处的高度
    logoHeight?: number | undefined;
    // 多标签的高度
    tabsHeight?: number | undefined;
    // 多标签页 选中的的索引
    tabIndex?: number
    // 多标签页 列表
    tabsList?: Array<ITabType>;
    // 左侧菜单栏列表
    menuList?: Array<any>;
    // 面包屑导航列表
    breadMenuList?: Array<any>;
}

export interface ITabType {
    id: number,
    tabKey: string,
    tabName: string,
    icon?: string | Component | Function
}

/**
 * pinia - 系统路由
 */
export interface IRouterType {
    // 路由列表
    routesList?: Array<any>;
    // 需要被缓存的路由列表
    routesKeepAliveList?: Array<any>
}

/**
 * 登录表单
 */
export interface ILoginFormType {
    // 用户名
    username: string | undefined;
    // 密码
    password: string | undefined;
    // 验证码
    code: string | undefined;
}

/**
 * 注册表单
 */
export interface IRegisterFormType {
    // 用户名
    username: string | undefined;
    // 密码
    password: string | number | undefined;
    // 重复密码
    password2: string | number | undefined;
    // 验证码
    code: string | undefined;
}

/**
 * 用户
 */
export interface IUser {
    // 用户名
    username?: string | undefined;
    // Token key
    tokenKey?: string | undefined;
    // Token令牌
    authorization?: string | undefined;
}

/**
 * 表格列
 */
export interface IColumnType {
    key: string;
    title: string;
    width?: number
}

/**
 * 分页
 */
export interface IPage {
    page: number | string;
    pageSize: number | string
}

/**
 * CommonTable按钮配置
 */
export interface IButtonConfig {
    // 按钮类型，如 add 新增
    type?: string,
    // 按钮文本，如 新增
    title?: string,
    // 按钮事件类型
    actionType?: any,
    // 是否显示，必传
    show: boolean,
    // 按钮颜色类型
    colorType?: "default" | "tertiary" | "primary" | "success" | "warning" | "error" | "info",
}
export interface IButtonType {
    addButton: IButtonConfig,
    editButton: IButtonConfig,
    deleteButton: IButtonConfig,
    batchDeleteButton:IButtonConfig
}
