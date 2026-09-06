import {RouteLocationNormalized, RouteRecordRaw} from 'vue-router';
import {useUserStore} from '@/store';

export default function usePermission() {
    const userStore = useUserStore();
    return {
        accessRouter(route: RouteLocationNormalized | RouteRecordRaw) {
            return (
                !route.meta?.requiresAuth ||
                !route.meta?.roles ||
                route.meta?.roles?.includes('*') ||
                route.meta?.roles?.find(item => userStore.roles.includes(item))
            );
        },
        findFirstPermissionRoute(_routers: any, roles = ['admin']) {
            const cloneRouters = Array.isArray(_routers) ? [..._routers] : [];
            while (cloneRouters.length) {
                const firstElement = cloneRouters.shift();
                if (
                    firstElement?.meta?.roles?.find((el: string[]) => {
                        return el.includes('*') || roles.filter((item) => {
                            return el.includes(item)
                        }).length > 0;
                    })
                )
                    return {name: firstElement.name};
                if (firstElement?.children) {
                    cloneRouters.push(...firstElement.children);
                }
            }
            return null;
        },
        // You can add any rules you want
    };
}
