import type { UserInfo } from '#/store';
import type { ErrorMessageMode } from '#/axios';
import { defineStore } from 'pinia';
import { store } from '@/store';
import { RoleEnum } from '@/enums/roleEnum';
import { PageEnum } from '@/enums/pageEnum';
import { ROLES_KEY, TOKEN_KEY, USER_INFO_KEY } from '@/enums/cacheEnum';
import { getAuthCache, setAuthCache } from '@/utils/auth';
import { loginApi, getUserInfo, doLogout } from '@/api/auth';
import { SimpleLoginParams, UserInfoResult } from '@/api/auth/model';
import { useI18n } from '@/hooks/web/useI18n';
import { useMessage } from '@/hooks/web/useMessage';
import { router } from '@/router';
import { usePermissionStore } from '@/store/modules/permission';
import { RouteRecordRaw } from 'vue-router';
import { PAGE_NOT_FOUND_ROUTE } from '@/router/routes/basic';
import { isArray } from '@/utils/is';
import { h } from 'vue';
import { useGlobSetting } from '@/hooks/setting';

/**
 * 没有头像时默认的头像
 */
// https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png
const defaultAvatar = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png';
const { apiUrl } = useGlobSetting();

interface UserState {
  userInfo: Nullable<UserInfo>;
  token?: string;
  roleList: RoleEnum[];
  sessionTimeout?: boolean;
  lastUpdateTime: number;
  // ruoyi
  permissionList: string[];
}

export const useUserStore = defineStore({
  id: 'app-user',
  state: (): UserState => ({
    // user info
    userInfo: null,
    // token
    token: undefined,
    // roleList
    roleList: [],
    // Whether the login expired
    sessionTimeout: false,
    // Last fetch time
    lastUpdateTime: 0,
    permissionList: [],
  }),
  getters: {
    getUserInfo(state): UserInfo {
      return state.userInfo || getAuthCache<UserInfo>(USER_INFO_KEY) || {};
    },
    getToken(state): string {
      return state.token || getAuthCache<string>(TOKEN_KEY);
    },
    getRoleList(state): RoleEnum[] {
      return state.roleList.length > 0 ? state.roleList : getAuthCache<RoleEnum[]>(ROLES_KEY);
    },
    getSessionTimeout(state): boolean {
      return !!state.sessionTimeout;
    },
    getLastUpdateTime(state): number {
      return state.lastUpdateTime;
    },
    getPermissionList(state): string[] {
      return state.permissionList;
    },
  },
  actions: {
    setToken(info: string | undefined) {
      this.token = info ? info : ''; // for null or undefined value
      setAuthCache(TOKEN_KEY, info);
    },
    setRoleList(roleList: RoleEnum[]) {
      this.roleList = roleList;
      setAuthCache(ROLES_KEY, roleList);
    },
    setUserInfo(info: UserInfo | null) {
      this.userInfo = info;
      this.lastUpdateTime = new Date().getTime();
      setAuthCache(USER_INFO_KEY, info);
    },
    setSessionTimeout(flag: boolean) {
      this.sessionTimeout = flag;
    },
    setPermissionList(list: string[]) {
      this.permissionList.push(...list);
    },
    resetState() {
      this.userInfo = null;
      this.token = '';
      this.roleList = [];
      this.sessionTimeout = false;
      this.permissionList = [];
    },
    /**
     * @description: login
     */
    async login(
      params: SimpleLoginParams & {
        goHome?: boolean;
        mode?: ErrorMessageMode;
      },
    ): Promise<UserInfo | null> {
      try {
        const { goHome = true, mode, ...loginParams } = params;
        const { token } = await loginApi(loginParams, mode);
        // save token
        this.setToken(token);
        return this.afterLoginAction(goHome);
      } catch (error) {
        return Promise.reject(error);
      }
    },
    async afterLoginAction(goHome?: boolean): Promise<UserInfo | null> {
      if (!this.getToken) return null;
      // get user info
      const userInfo = await this.getUserInfoAction();

      const sessionTimeout = this.sessionTimeout;
      if (sessionTimeout) {
        this.setSessionTimeout(false);
      } else {
        const permissionStore = usePermissionStore();

        // 这里使用动态加载
        if (!permissionStore.isDynamicAddedRoute) {
          const routes = await permissionStore.buildRoutesAction();
          [...routes, PAGE_NOT_FOUND_ROUTE].forEach((route) => {
            /**
             * 不需要添加外链路由 外链路由显示由menu控制
             * Route paths should start with a "/": "http://xxx" should be "/http://xxx".
             */
            if (route.path.startsWith('http')) {
              return;
            }
            router.addRoute(route as unknown as RouteRecordRaw);
          });
          // 记录动态路由加载完成
          permissionStore.setDynamicAddedRoute(true);
        }

        goHome && (await router.replace(PageEnum.BASE_HOME));
      }
      return userInfo;
    },
    async getUserInfoAction(): Promise<UserInfo | null> {
      if (!this.getToken) return null;
      const userInfo = await getUserInfo();
      const { roles = [], permissions = [] } = userInfo;
      // 添加角色
      if (isArray(roles)) {
        const roleList = roles.map((item) => item) as RoleEnum[];
        this.setRoleList(roleList);
      } else {
        userInfo.roles = [];
        this.setRoleList([]);
      }
      // 添加permissions
      if (isArray(roles)) {
        this.setPermissionList(permissions);
      } else {
        userInfo.permissions = [];
        this.setPermissionList([]);
      }

      /**
       * 不知道是bug还是故意设置成这样
       * 在绑定用户角色后 如果该用户没有该部门权限 user为空
       */
      if (!userInfo.user) {
        const content = '登录失败, 权限不足, 请联系管理员';
        throw new Error(content);
      }

      // 头像设置
      const { avatar } = userInfo.user;
      // 不存在时的默认头像
      if (!avatar) {
        userInfo.user.avatar = defaultAvatar;
      } else {
        userInfo.user.avatar = `${apiUrl}${avatar}`;
      }

      const transUserInfo = transformRespUser(userInfo);
      this.setUserInfo(transUserInfo);
      return transUserInfo;
    },
    /**
     * @param withoutRedirect true不需要携带重定向地址
     * @description: logout
     */
    async logout(withoutRedirect = false) {
      if (this.getToken) {
        try {
          await doLogout();
        } catch {
          console.log('注销Token失败');
        }
      }
      this.setToken(undefined);
      this.setSessionTimeout(false);
      this.setUserInfo(null);
      if (withoutRedirect) {
        // 直接回登陆页
        router.replace(PageEnum.BASE_LOGIN);
      } else {
        // 回登陆页带上当前路由地址
        router.replace({
          path: PageEnum.BASE_LOGIN,
          query: {
            redirect: encodeURIComponent(router.currentRoute.value.fullPath),
          },
        });
      }
    },

    /**
     * @description: Confirm before logging out
     */
    confirmLoginOut() {
      const { createConfirm } = useMessage();
      const { t } = useI18n();
      createConfirm({
        iconType: 'warning',
        title: () => h('span', t('sys.app.logoutTip')),
        content: () => h('span', t('sys.app.logoutMessage')),
        onOk: async () => {
          // 主动登出，不带redirect地址
          await this.logout(true);
        },
      });
    },
  },
});

// Need to be used outside the setup
export function useUserStoreWithOut() {
  return useUserStore(store);
}

/**
 * 转换为适配vben框架的UserInfo 暂时不打算直接替换
 * @param resp api返回
 * @returns
 */
function transformRespUser(resp: UserInfoResult): UserInfo {
  // 展开后得到UserInfo
  const { userId, userName, nickName, avatar = '' } = resp.user;
  return {
    permissions: resp.permissions,
    roles: resp.roles,
    userId,
    userName,
    nickName,
    avatar,
  };
}
