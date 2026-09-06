// @ts-check

import { defineStore } from 'pinia';
import { login as apiLogin, logout as apiLogout, getInfo as apiGetInfo } from '@/api/login';
import { getToken, setToken, removeToken } from '@/utils/auth';

/** @typedef {import('@/types/api').LoginRequest} LoginRequest */
/** @typedef {import('@/types/api').LoginResponse} LoginResponse */
/** @typedef {import('@/types/api').GetInfoResponse} GetInfoResponse */

/**
 * @typedef {object} UserState
 * @property {string} token
 * @property {boolean} userLoaded
 * @property {string} name
 * @property {string} avatar
 * @property {string[]} roles
 * @property {string[]} permissions
 * @property {string} nickName
 * @property {string} loginDate
 * @property {string} loginIp
 */

export const useUserStore = defineStore('user', {
  /** @returns {UserState} */
  state: () => ({
    token: getToken() || '',
    userLoaded: false,
    name: '',
    avatar: '',
    roles: [],
    permissions: [],
    nickName: '',
    loginDate: '',
    loginIp: '',
  }),

  getters: {
    // 用户信息对象，用于向后兼容
    userInfo: (state) => ({
      userName: state.name,
      nickName: state.nickName,
      avatar: state.avatar,
      roles: state.roles,
      permissions: state.permissions,
      loginDate: state.loginDate,
      loginIp: state.loginIp,
    }),
  },

  actions: {
    // 登录
    /**
     * @param {LoginRequest} loginInfo
     * @returns {Promise<void>}
     */
    async login(loginInfo) {
      /** @type {LoginResponse} */
      const res = await apiLogin({
        username: loginInfo.username,
        password: loginInfo.password,
        code: loginInfo.code,
        uuid: loginInfo.uuid,
      });
      setToken(res.token);
      this.token = res.token;
      this.userLoaded = false;
    },

    // 获取用户信息
    /**
     * @returns {Promise<GetInfoResponse>}
     */
    async getInfo() {
      /** @type {GetInfoResponse} */
      const res = await apiGetInfo();
      this.roles = Array.isArray(res.roles) ? res.roles : [];
      this.permissions = Array.isArray(res.permissions) ? res.permissions : [];
      this.name = res.user?.userName || '';
      this.avatar = res.user?.avatar || '';
      this.nickName = res.user?.nickName || '';
      this.loginDate = res.user?.loginDate || '';
      this.loginIp = res.user?.loginIp || '';
      this.userLoaded = true;
      return res;
    },

    resetUserState() {
      this.token = '';
      this.userLoaded = false;
      this.name = '';
      this.avatar = '';
      this.roles = [];
      this.permissions = [];
      this.nickName = '';
      this.loginDate = '';
      this.loginIp = '';
    },

    // 退出系统
    /**
     * @returns {Promise<void>}
     */
    async logout() {
      try {
        await apiLogout();
      } catch (error) {
        console.error('后端登出接口调用失败，执行本地登出兜底:', error);
      } finally {
        this.resetUserState();
        removeToken();
      }
    },

    // 前端 登出
    fedLogout() {
      this.resetUserState();
      removeToken();
    },
  },
});
