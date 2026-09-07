export enum PageEnum {
  // basic login path
  BASE_LOGIN = '/login',
  // 默认/dashboard 在登录时会带上redirect 然后死循环  原因未知  暂时写死地址
  BASE_HOME = '/dashboard',
  // error page path
  ERROR_PAGE = '/exception',
  // error log page path
  ERROR_LOG_PAGE = '/error-log/list',
}
export const PageWrapperFixedHeightKey = 'PageWrapperFixedHeight';
