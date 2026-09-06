import type {LocationQueryRaw, Router} from 'vue-router';
import NProgress from 'nprogress'; // progress bar
import {useUserStore} from '@/store';
import {isLogin} from '@/utils/auth';
import usePermission from '@/hooks/permission';
import {appRoutes} from '../routes';
import {NOT_FOUND} from '../constants';

export default function setupUserLoginInfoGuard(router: Router) {
  router.beforeEach(async (to, from, next) => {
    NProgress.start();
    const userStore = useUserStore();
    const Permission = usePermission();
    if (isLogin()) {
      try {
        await userStore.info();
        if(to.name === 'login'){
            next(Permission.findFirstPermissionRoute(appRoutes, userStore.roles) || NOT_FOUND);
        }
        next();
      } catch (error) {
        console.log("error",error);
        await userStore.logout();
        next({
          name: 'login',
          query: {
            redirect: to.name,
            ...to.query,
          } as LocationQueryRaw,
        });
      }
    } else {
      if (to.name === 'login') {
        next();
        return;
      }
      next({
        name: 'login',
        query: {
          redirect: to.name,
          ...to.query,
        } as LocationQueryRaw,
      });
    }
  });
}
