import {AppRouteRecordRaw} from "@/router/routes/types";
import {DEFAULT_LAYOUT} from "@/router/routes/base";

const GEN_EDIT: AppRouteRecordRaw = {
    path: '/tool/gen-edit',
    component: DEFAULT_LAYOUT,
    meta: {
        requiresAuth: true,
        hideInMenu: true,
    },
    children: [
        {
            path: 'index/:tableId(\\d+)',
            component: () => import('@/views/tool/gen/components/edit-table.vue'),
            name: 'GenEdit',
            meta: {
                locale: 'tool.gen.gen-edit',
                requiresAuth: true,
                title: '修改生成配置',
            },
        }
    ],
}

export default GEN_EDIT;
