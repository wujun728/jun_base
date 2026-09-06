import {AppRouteRecordRaw} from "@/router/routes/types";
import {DEFAULT_LAYOUT} from "@/router/routes/base";

const AUTH_ROLE: AppRouteRecordRaw = {
    path: '/system/user-auth',
    component: DEFAULT_LAYOUT,
    meta: {
        requiresAuth: true,
        hideInMenu: true,
    },
    children: [
        {
            path: 'role/:userId(\\d+)',
            component: () => import('@/views/system/user/components/edit-auth-role.vue'),
            name: 'AuthRole',
            meta: {
                locale: 'menu.system.auth-role',
                requiresAuth: true,
                title: '分配角色',
            }
        }
    ],
}

export default AUTH_ROLE;
