import {DEFAULT_LAYOUT} from '../base';
import {AppRouteRecordRaw} from '../types';

const tool: AppRouteRecordRaw = {
    path: '/tool',
    name: 'Tool',
    component: DEFAULT_LAYOUT,
    meta: {
        locale: 'menu.tool',
        title: '系统工具',
        requiresAuth: true,
        icon: 'IconTool',
        order: 3,
    },
    children: [
        {
            path: 'build',
            name: 'Build',
            component: () => import('@/views/tool/build/index.vue'),
            meta: {
                locale: 'menu.tool.build',
                title: '表单构建',
                requiresAuth: true,
                roles: ['*'],
                icon: 'IconCommon',
            },
        },
        {
            path: 'gen',
            name: 'Gen',
            component: () => import('@/views/tool/gen/index.vue'),
            meta: {
                locale: 'menu.tool.gen',
                title: '代码生成',
                requiresAuth: true,
                roles: ['*'],
                icon: 'IconCode',
            },
        },
        {
            path: 'swagger',
            name: 'Swagger',
            component: () => import('@/views/tool/swagger/index.vue'),
            meta: {
                locale: 'menu.tool.swagger',
                title: '系统接口',
                requiresAuth: true,
                roles: ['*'],
                icon: 'IconInteraction',
            },
        },
    ],
};

export default tool;
