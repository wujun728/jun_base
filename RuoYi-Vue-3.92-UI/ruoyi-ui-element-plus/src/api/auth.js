import request from "@/utils/request";

const AuthAPI = {
  /** 登录 */
  login(data) {
    return request({
      url: "/login",
      method: "post",
      data,
      headers: { Authorization: "no-auth" },
    });
  },

  /** 获取验证码（返回 { img, uuid, captchaEnabled }） */
  getCaptcha() {
    return request({
      url: "/captchaImage",
      method: "get",
      headers: { Authorization: "no-auth" },
      timeout: 20000,
    });
  },

  /** 获取当前登录用户信息（含角色、权限） */
  getInfo() {
    return request({
      url: "/getInfo",
      method: "get",
    });
  },

  /** 退出登录 */
  logout() {
    return request({
      url: "/logout",
      method: "post",
    });
  },
};

export default AuthAPI;