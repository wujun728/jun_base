import {AppRouteRecordRaw} from "@/router/routes/types";
import {DEFAULT_LAYOUT} from "@/router/routes/base";

const ROLE_AUTH: AppRouteRecordRaw = {
    path: '/system/role-auth',
    component: DEFAULT_LAYOUT,
    meta: {
        requiresAuth: true,
        hideInMenu: true,
    },
    children: [
        {
            path: 'user/:roleId(\\d+)',
            component: () => import('@/views/system/role/components/edit-auth-user.vue'),
            name: 'AuthUser',
            meta: {
                locale: 'menu.system.auth-user',
                requiresAuth: true,
                title: '分配用户',
            }
        }
    ],
}

export default ROLE_AUTH;
