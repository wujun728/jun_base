import { APP_ID, APP_VERSION } from './app';
import { useUserStore } from '@/store';

const commonParams = {
  isLoading: true,
  appid: APP_ID,
  v: APP_VERSION // 系统版本，用于获取最新版数据
};

export function getCommonParams() {
  const userStore = useUserStore();

  return {
    ...{
      token: userStore.user.token,
      uuid: userStore.user.id,
      timestamp: Date.now()
    },
    ...commonParams
  };
}
export function setCommonParams(params: Object) {
  Object.assign(commonParams, params);
}
