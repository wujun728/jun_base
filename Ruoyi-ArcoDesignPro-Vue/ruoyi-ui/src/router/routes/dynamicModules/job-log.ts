import {AppRouteRecordRaw} from "@/router/routes/types";
import {DEFAULT_LAYOUT} from "@/router/routes/base";

const JOB_LOG: AppRouteRecordRaw = {
    path: '/monitor/job',
    component: DEFAULT_LAYOUT,
    meta: {
        requiresAuth: true,
        hideInMenu: true,
    },
    children: [
        {
            path: 'log/:jobId(\\d+)',
            component: () => import('@/views/monitor/job/components/list-job-log.vue'),
            name: 'JobLog',
            meta: {
                locale: 'menu.monitor.job-log',
                requiresAuth: true,
                title: '调度日志'
            }
        }
    ],
}

export default JOB_LOG;
