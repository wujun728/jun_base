import type { LockInfo } from '#/store';

import { defineStore } from 'pinia';

import { LOCK_INFO_KEY } from '@/enums/cacheEnum';
import { Persistent } from '@/utils/cache/persistent';

interface LockState {
  lockInfo: Nullable<LockInfo>;
}

export const useLockStore = defineStore({
  id: 'app-lock',
  state: (): LockState => ({
    lockInfo: Persistent.getLocal(LOCK_INFO_KEY),
  }),
  getters: {
    getLockInfo(state): Nullable<LockInfo> {
      return state.lockInfo;
    },
  },
  actions: {
    setLockInfo(info: LockInfo) {
      this.lockInfo = Object.assign({}, this.lockInfo, info);
      Persistent.setLocal(LOCK_INFO_KEY, this.lockInfo, true);
    },
    resetLockInfo() {
      Persistent.removeLocal(LOCK_INFO_KEY, true);
      this.lockInfo = null;
    },
    // Unlock
    async unLock(password?: string) {
      if (this.lockInfo?.pwd === password) {
        this.resetLockInfo();
        return true;
      }
      const tryLogin = async () => {
        try {
          // 自行处理逻辑
          // 这里没有限制
          this.resetLockInfo();
          return;
        } catch (error) {
          return false;
        }
      };
      return await tryLogin();
    },
  },
});
