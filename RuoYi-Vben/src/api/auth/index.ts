import { defHttp } from '@/utils/http/axios';
import { UserInfoResult, Menu, SimpleLoginParams } from './model';
import { ErrorMessageMode } from '#/axios';

enum Api {
  Login = '/login',
  Logout = '/logout',
  GetUserInfo = '/getInfo',
  GetRoutes = '/getRouters',
}

/**
 * @description 登录接口
 * @returns token
 */
export function loginApi(data: SimpleLoginParams, mode: ErrorMessageMode = 'modal') {
  return defHttp.post<{ token: string }>(
    {
      url: Api.Login,
      data,
    },
    {
      errorMessageMode: mode,
    },
  );
}

/**
 * @description: 获取用户信息
 */
export function getUserInfo() {
  return defHttp.get<UserInfoResult>({ url: Api.GetUserInfo }, { errorMessageMode: 'none' });
}

/**
 * 用户登出
 * @returns
 */
export function doLogout() {
  return defHttp.post<void>({ url: Api.Logout });
}

/**
 * 获取对应用户的菜单
 * @returns
 */
export function getRoutes() {
  return defHttp.get<Menu[]>({ url: Api.GetRoutes });
}
