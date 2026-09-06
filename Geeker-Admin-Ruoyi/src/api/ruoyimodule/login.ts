import http from "@/api";
import { AuthState, UserState } from "@/stores/interface";

export function getCodeImg() {
  return http.get(
    `/captchaImage`,
    {},
    {
      cancel: false,
      isToken: false,
      loading: false
    }
  );
}
export function loginApi(data) {
  return http.post<any>(`/login`, data, {
    cancel: false,
    isToken: false
  });
}
export function getInfo() {
  return http.get<{
    user: UserState["userInfo"];
  }>(`/getInfo`);
}
export function getRouters() {
  return http.get<AuthState["authMenuList"]>(`/getRouters`);
}
export function loginOut(data) {
  return http.post(`/logout`, data, {
    cancel: false,
    isToken: false
  });
}
