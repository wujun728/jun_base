export const SIDE_BAR_MINI_WIDTH = 48;
export const SIDE_BAR_SHOW_TIT_MINI_WIDTH = 80;

export enum ContentEnum {
  // auto width
  FULL = 'full',
  // fixed width
  FIXED = 'fixed',
}

// menu theme enum
export enum ThemeEnum {
  DARK = 'dark',
  LIGHT = 'light',
}

export enum SettingButtonPositionEnum {
  AUTO = 'auto',
  HEADER = 'header',
  FIXED = 'fixed',
}

export enum SessionTimeoutProcessingEnum {
  ROUTE_JUMP,
  PAGE_COVERAGE,
}

/**
 * 权限模式
 */
export enum PermissionModeEnum {
  // role
  // 角色权限
  ROLE = 'ROLE',
  // back
  // 后端
  BACK = 'BACK',
  // route mapping
  // 路由映射
  ROUTE_MAPPING = 'ROUTE_MAPPING',
}

// Route switching animation
// 路由切换动画
export enum RouterTransitionEnum {
  ZOOM_FADE = 'zoom-fade',
  ZOOM_OUT = 'zoom-out',
  FADE_SIDE = 'fade-slide',
  FADE = 'fade',
  FADE_BOTTOM = 'fade-bottom',
  FADE_SCALE = 'fade-scale',
}

export enum IconEnum {
  EDIT = 'uil:edit',
  SYNC = 'ic:baseline-sync',
  DELETE = 'ic:outline-delete-outline',
  IMPORT = 'bx:import',
  EXPORT = 'bx:export',
  RESET_PWD = 'fluent:key-reset-20-filled',
  ADD = 'zondicons:add-outline',
  EXPAND = 'ion:expand-outline',
  COLLAPSE = 'octicon:sidebar-collapse-16',
  REFRESH = 'charm:refresh',
  DETAIL = 'icon-park-outline:view-grid-detail',
  LOCK = 'icon-park-twotone:lock',
  UNLOCK = 'icon-park-twotone:unlock',
  DOWNLOAD = 'material-symbols:download',
  // eslint-disable-next-line @typescript-eslint/no-duplicate-enum-values
  UPLOAD = 'zondicons:add-outline',
  PREVIEW = 'codicon:preview',
  PREV = 'iconoir:skip-prev',
  NEXT = 'iconoir:skip-next',
  CANCEL = 'mdi:cancel',
  CLOSE = 'zondicons:close-outline',
  PHONE = 'solar:phone-linear',
  EMAIL = 'carbon:email',
}
