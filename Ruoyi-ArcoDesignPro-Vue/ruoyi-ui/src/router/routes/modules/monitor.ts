import {DEFAULT_LAYOUT} from '../base';
import {AppRouteRecordRaw} from '../types';

const monitor: AppRouteRecordRaw = {
    path: '/monitor',
    name: 'Monitor',
    component: DEFAULT_LAYOUT,
    meta: {
        locale: 'menu.monitor',
        title: '系统监控',
        requiresAuth: true,
        icon: 'IconComputer',
        order: 2,
    },
    children: [
        {
            path: 'online',
            name: 'Online',
            component: () => import('@/views/monitor/online/index.vue'),
            meta: {
                locale: 'menu.monitor.online',
                title: '在线用户',
                requiresAuth: true,
                roles: ['*'],
                icon: 'IconWifi',
            },
        },
        {
            path: 'job',
            name: 'Job',
            component: () => import('@/views/monitor/job/index.vue'),
            meta: {
                locale: 'menu.monitor.job',
                title: '定时任务',
                requiresAuth: true,
                roles: ['*'],
                icon: 'IconRightCircle',
            },
        },
        {
            path: 'druid',
            name: 'Druid',
            component: () => import('@/views/monitor/druid/index.vue'),
            meta: {
                locale: 'menu.monitor.druid',
                title: '数据监控',
                requiresAuth: true,
                roles: ['*'],
                icon: 'IconComputer',
            },
        },
        {
            path: 'server',
            name: 'Server',
            component: () => import('@/views/monitor/server/index.vue'),
            meta: {
                locale: 'menu.monitor.server',
                title: '服务监控',
                requiresAuth: true,
                roles: ['*'],
                icon: 'IconDesktop',
            },
        },
        {
            path: 'cache-info',
            name: 'Cache',
            component: () => import('@/views/monitor/cache/info/index.vue'),
            meta: {
                locale: 'menu.monitor.cache-info',
                title: '缓存监控',
                requiresAuth: true,
                roles: ['*'],
                icon: 'IconDice',
            },
        },
        {
            path: 'cache-data',
            name: 'CacheData',
            component: () => import('@/views/monitor/cache/data/index.vue'),
            meta: {
                locale: 'menu.monitor.cache-data',
                title: '缓存列表',
                requiresAuth: true,
                roles: ['*'],
                icon: 'IconList',
            },
        },
    ],
};

export default monitor;
