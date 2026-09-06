// eslint-disable-next-line
import { UserLayout,BasicLayout,ElementLayout } from '@/layouts'

/**
 * 工作台
 */
export const indexRouterMap = [
  {
    path: '/index',
    name: 'index',
    component: 'DashBoard',
    meta: { title: '工作台', keepAlive: true, icon: 'home', noCache: false }
  },
  // {
  //   name: 'index',
  //   path: '/',
  //   component: 'Layout',
  //   meta: { title: '首页', icon: 'home', hideHeader: true },
  //   redirect: '/index',
  //   children: [
  //     {
  //       path: '/index',
  //       name: 'index',
  //       component: 'DashBoard',
  //       meta: { title: '首页', keepAlive: true, icon: 'home', noCache: false }
  //     }
  //   ]
  // },
  {
    path: '/account/center',
    name: 'center',
    component: 'AccountCenter',
    meta: { title: '个人中心', keepAlive: true, noCache: false },
    hidden: true
  },
  // {
  //   path: '/dashboard/console',
  //   name: 'center',
  //   component: 'Console',
  //   meta: { title: '控制台', keepAlive: true, noCache: false },
  //   hidden: true
  // },
  {
    path: '/account/settings',
    name: 'settings',
    component: 'AccountSettings',
    meta: { title: '个人设置', hideHeader: true },
    redirect: '/account/settings/base',
    hidden: true,
    children: [
      {
        path: '/account/settings/base',
        name: 'BaseSettings',
        component: 'BaseSettings',
        hidden: true,
        meta: { title: '基本设置', hidden: true, keepAlive: true, noCache: false }
      },
      {
        path: '/account/settings/security',
        name: 'SecuritySettings',
        component: 'SecuritySettings',
        meta: { title: '安全设置', hidden: true, keepAlive: true, noCache: false }
      }
    ]
  },
  {
    path: '/monitor/job/log',
    name: 'JobLog',
    component: 'JobLog',
    meta: { title: '调度日志', keepAlive: true, noCache: false },
    hidden: true
  },
  {
    path: '/system/notice/NoticeReadIndex',
    name: 'NoticeReadIndex',
    component: 'NoticeReadIndex',
    meta: { title: '通知公告阅读', keepAlive: true, noCache: false },
    hidden: true
  },
  {
    path: '/system/notice/form',
    name: 'NoticeForm',
    component: 'NoticeForm',
    meta: { title: '公告编辑', keepAlive: true, noCache: false },
    hidden: true
  },
  {
    path: '/gen/edit',
    name: 'GenEdit',
    component: 'GenEdit',
    meta: { title: '修改生成配置', keepAlive: true, noCache: false },
    hidden: true
  }
]
/**
 * 基础路由
 * @type { *[] }
 */
export const constantRouterMap = [
  {
    path: '/user',
    component: UserLayout,
    redirect: '/user/login',
    hidden: true,
    children: [
      {
        path: 'login',
        name: 'login',
        component: () => import(/* webpackChunkName: "user" */ '@/views/user/Login')
      }
    ]
  },

  {
    path: '/404',
    name: '404',
    component: () => import(/* webpackChunkName: "fail" */ '@/views/exception/404')
  },
  {
    path: '/applyLicense',
    name: 'applyLicense',
    component: () => import(/* webpackChunkName: "fail" */ '@/views/applyLicense/ApplyLicense')
  },

  {
    path: '/tool',
    component: BasicLayout,
    redirect: '/tool/index',
    hidden: true,
    children: [
      {
        path: 'build/index',
        name: 'FormBuild',
        component: () => import('@/views/tool/build/index'),
        meta: { title: '表单配置', icon: '' }
      }
    ]
  },
  { path: '/bigscreen/viewer', component: () => import('@/views/report/bigscreen/viewer'), hidden: true, meta: { requireAuth: true }},
  { path: '/bigscreen/designer', component: () => import('@/views/report/bigscreen/designer'), hidden: true, meta: { requireAuth: true }},
  { path: '/excelreport/viewer', component: () => import('@/views/report/excelreport/viewer'), hidden: true, meta: { requireAuth: true }},
  { path: '/excelreport/designer', component: () => import('@/views/report/excelreport/designer'), hidden: true, meta: { requireAuth: true }},
  {
    path: '/report', name: 'report', component: ElementLayout, meta: { title: '报表设计', icon: 'iconnavicon-ywcs', requireAuth: true, permission: 'datasourceManage|resultsetManage|reportManage|bigScreenManage' },
    children: [
      { path: 'datasource', name: 'datasource', component: () => import('@/views/report/datasource/index'), meta: { title: '数据源', icon: 'icondatabase', keepAlive: true, requireAuth: true, permission: 'datasourceManage'} },
      { path: 'resultset', name: 'resultset', component: () => import('@/views/report/resultset/index'), meta: { title: '数据集', icon: 'iconAPIwangguan', keepAlive: true, requireAuth: true, permission: 'resultsetManage'} },
      { path: 'report', name: 'reportIndex', component: () => import('@/views/report/report/index'), meta: { title: '报表管理', icon: 'iconnavicon-ywcs', keepAlive: true, requireAuth: true, permission: 'reportManage'} },
      { path: 'bigscreen', name: 'bigscreen', component: () => import('@/views/report/bigscreen/index'), meta: { title: '大屏报表', icon: 'iconchufaqipeizhi-hui', keepAlive: true, requireAuth: true, permission: 'bigScreenManage'},       },
      { path: 'excelreport', name: 'excelreport', component: () => import('@/views/report/excelreport/index'), meta: { title: '表格报表', icon: 'iconliebiao', keepAlive: true, requireAuth: true, permission: 'excelManage'} },
    ]
  },
  {
    path: '/flowable',
    component: ElementLayout,
    redirect: '/flowable/definition/model',
    hidden: true,
    children: [
      {
        path: 'definition/model/',
        name: 'Model',
        component: () => import('@/views/flowable/definition/model'),
        meta: { title: '流程设计', icon: '' }
      }
    ]
  },
  {
    path: '/flowable',
    component: ElementLayout,
    redirect: '/flowable/task/record/index',
    hidden: true,
    children: [
      {
        path: 'task/record/index',
        name: 'Record',
        component: () => import('@/views/flowable/task/record/index'),
        meta: { title: '流程处理', icon: '' }
      }
    ]
  }

]
