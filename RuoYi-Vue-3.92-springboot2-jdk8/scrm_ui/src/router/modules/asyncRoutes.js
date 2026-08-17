import Layout from '@/layout'

export const asyncRoutes = [
  {
    path: '/system',
    component: Layout,
    children: [
      {
        path: 'user',
        component: () => import('@/views/system/user/index'),
        name: 'SystemUser',
        meta: { title: '用户管理', icon: 'user' },
      },
      {
        path: 'role',
        component: () => import('@/views/system/role/index'),
        name: 'SystemRole',
        meta: { title: '角色管理', icon: 'peoples' },
      },
      {
        path: 'menu',
        component: () => import('@/views/system/menu/index'),
        name: 'SystemMenu',
        meta: { title: '菜单管理', icon: 'tree-table' },
      },
      {
        path: 'dept',
        component: () => import('@/views/system/dept/index'),
        name: 'SystemDept',
        meta: { title: '部门管理', icon: 'tree' },
      },
      {
        path: 'post',
        component: () => import('@/views/system/post/index'),
        name: 'SystemPost',
        meta: { title: '岗位管理', icon: 'post' },
      },
      {
        path: 'dict',
        component: () => import('@/views/system/dict/index'),
        name: 'SystemDict',
        meta: { title: '字典管理', icon: 'dict' },
      },
      {
        path: 'config',
        component: () => import('@/views/system/config/index'),
        name: 'SystemConfig',
        meta: { title: '参数设置', icon: 'edit' },
      },
      {
        path: 'notice',
        component: () => import('@/views/system/notice/index'),
        name: 'SystemNotice',
        meta: { title: '通知公告', icon: 'message' },
      },
      {
        path: 'log',
        component: () => import('@/components/ParentView'),
        name: 'SystemLog',
        meta: { title: '日志管理', icon: 'log' },
        children: [
          {
            path: 'operlog',
            component: () => import('@/views/monitor/operlog/index'),
            name: 'MonitorOperlog',
            meta: { title: '操作日志', icon: 'form' },
          },
          {
            path: 'logininfor',
            component: () => import('@/views/monitor/logininfor/index'),
            name: 'MonitorLogininfor',
            meta: { title: '登录日志', icon: 'logininfor' },
          },
        ],
      },
      {
        path: 'monitor',
        component: () => import('@/components/ParentView'),
        name: 'SystemMonitor',
        meta: { title: '系统监控', icon: 'monitor' },
        children: [
          {
            path: 'online',
            component: () => import('@/views/monitor/online/index'),
            name: 'MonitorOnline',
            meta: { title: '在线用户', icon: 'online' },
          },
          {
            path: 'job',
            component: () => import('@/views/monitor/job/index'),
            name: 'MonitorJob',
            meta: { title: '定时任务', icon: 'job' },
          },
          {
            path: 'druid',
            component: () => import('@/views/monitor/druid/index'),
            name: 'MonitorDruid',
            meta: { title: '数据监控', icon: 'druid' },
          },
          {
            path: 'server',
            component: () => import('@/views/monitor/server/index'),
            name: 'MonitorServer',
            meta: { title: '服务监控', icon: 'server' },
          },
          {
            path: 'cache',
            component: () => import('@/views/monitor/cache/index'),
            name: 'MonitorCache',
            meta: { title: '缓存监控', icon: 'redis' },
          },
          {
            path: 'cacheList',
            component: () => import('@/views/monitor/cache/list'),
            name: 'MonitorCacheList',
            meta: { title: '缓存列表', icon: 'redis-list' },
          },
        ],
      },
    ],
  },
  {
    path: '/tool',
    component: Layout,
    children: [
      {
        path: 'build',
        component: () => import('@/views/tool/build/index'),
        name: 'ToolBuild',
        meta: { title: '表单构建', icon: 'build' },
      },
      {
        path: 'gen',
        component: () => import('@/views/tool/gen/index'),
        name: 'ToolGen',
        meta: { title: '代码生成', icon: 'code' },
      },
      {
        path: 'swagger',
        component: () => import('@/views/tool/swagger/index'),
        name: 'ToolSwagger',
        meta: { title: '系统接口', icon: 'swagger' },
      },
    ],
  },
]