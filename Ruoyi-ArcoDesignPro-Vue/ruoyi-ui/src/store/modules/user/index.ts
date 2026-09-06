import {defineStore} from 'pinia';
import {getUserInfo, login as userLogin, LoginData, LoginRes, logout as userLogout,} from '@/api/user';
import {clearToken, setToken} from '@/utils/auth';
import {removeRouteListener} from '@/utils/route-listener';
import {UserAction} from './types';
import useAppStore from '../app';

const env = import.meta.env.VITE_APP_BASE_API;

const useUserStore = defineStore('user', {
    state: (): UserAction => ({
        permissions: [],
        roles: [],
        user: {
            avatar: '',
            dept: {
                deptName: '',
            },
        },
    }),

    getters: {
        userInfo(state: UserAction): UserAction {
            return {...state};
        },
    },

    actions: {
        // Set user's information
        setInfo(partial: Partial<UserAction>) {
            if (partial.user?.avatar) {
                partial.user.avatar = env + partial.user.avatar;
            } else if (partial.user?.avatar === '') {
                partial.user.avatar = '//lf1-xgcdn-tos.pstatp.com/obj/vcloud/vadmin/start.8e0e4855ee346a46ccff8ff3e24db27b.png';
            }
            this.$patch(partial);
        },

        // Reset user's information
        resetInfo() {
            this.$reset();
        },

        // Get user's information
        async info() {
            const res = await getUserInfo();
            // @ts-ignore
            this.setInfo(res);
        },

    // Login
    async login(loginForm: LoginData) {
      try {
        const res = await userLogin(loginForm);
        const data = res as unknown as LoginRes;
        setToken(data.token);
      } catch (err) {
        clearToken();
        throw err;
      }
    },
    logoutCallBack() {
      const appStore = useAppStore();
      this.resetInfo();
      clearToken();
      removeRouteListener();
      appStore.clearServerMenu();
    },
    // Logout
    async logout() {
      try {
        await userLogout();
      } finally {
        this.logoutCallBack();
      }
    },
  },
});

export default useUserStore;
