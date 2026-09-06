import { defineStore } from 'pinia';
import userSetup from './store-setup/user';
import userInfoSetup from './store-setup/userInfo';
import isIphoneXSetup from './store-setup/isPhoneX';
import piniaPluginPersist from 'pinia-plugin-persist-uni';

const register = function <Id extends string, S>(id: Id, setup: () => S) {
  return defineStore(id, setup, { persist: { enabled: true } });
};

export const useUserStore = register('user', userSetup);
export const useUserInfoStore = register('users', userInfoSetup);
export const usePhoneStore = register('isIphoneX', isIphoneXSetup);

const store = createPinia();
store.use(piniaPluginPersist);

export default store;
