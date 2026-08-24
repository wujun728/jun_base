import { roterPre } from '@/settings'
import Layout from '@/layout'
import { name } from 'file-loader'

const defaultRoutes = [
  {
    path: roterPre,
    component: Layout,
    redirect: `${roterPre}/user/work`,
    children: [
      {
        path: `${roterPre}/user/work`,
        component: () => import('@/views/user/workbench/index'),
        name: 'dashboard-admin',
        meta: { title: '工作台', icon: 'dashboard', affix: true }
      }
    ]
  },
  {
    path: '/',
    component: Layout,
    redirect: `${roterPre}/user/work`,
    children: [
      {
        path: `${roterPre}/user/work`,
        component: () => import('@/views/user/workbench/index'),
        name: 'dashboard',
        meta: { title: '工作台', icon: 'dashboard', affix: true }
      },
      {
        path: `${roterPre}/search`,
        name: 'search',
        component: () => import('@/views/search/index'),
        meta: { title: '搜索' }
      },
      {
        path: `${roterPre}/user/forum/index`,
        name: 'forum',
        component: () => import('@/views/user/forum/index'),
        meta: { title: '知识社区' }
      },

      {
        path: `${roterPre}/user/notice/index`,
        name: 'notice',
        component: () => import('@/views/user/notice/index'),
        meta: { title: '企业动态' }
      }
    ]
  },
  {
    path: `${roterPre}/login`,
    name: 'login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: `${roterPre}/share/:id`,
    name: 'share',
    component: () => import('@/views/share'),
    hidden: true
  },
  {
    path: roterPre + '/401',
    component: () => import('@/views/error-page/401'),
    hidden: true
  },
  {
    path: roterPre + '/setting/icons',
    component: () => import('@/components/form-common/select-icon.vue'),
    name: 'icons'
  },
  {
    path: roterPre + '/setting/auth',
    component: () => import('@/components/systemAuth/index'),
    name: 'systemAuth'
  },

  {
    path: roterPre + '/setting/uploadPicture',
    component: () => import('@/components/uploadPicture/index.vue'),
    name: 'uploadPicture'
  },

  {
    path: roterPre + '/openFile',
    component: () => import('@/components/openFile/index.vue'),
    name: 'previewPage',
    meta: { title: '文件预览' }
  },
  // {
  //   path: roterPre + '/forum/index',
  //   component: () => import('@/views/user/forum/index'),
  //   name: 'forum',
  //   meta: { title: '知识社区' }
  // },

  {
    path: roterPre + '/process',
    component: () => import('@/views/develop/crud/process.vue'),
    name: 'process'
  },
  {
    path: roterPre + '/dashboard-design',
    component: () => import('@/views/system/dashboard-design/charts/index.vue'),
    name: 'dashboardDesign',
    meta: { title: '图表设计' }
  },
  {
    path: roterPre + '/event',
    component: () => import('@/views/develop/crud/event.vue'),
    name: 'event'
  },
  {
    path: roterPre + '/404',
    name: '404',
    component: () => import('@/views/error-page/404'),
    hidden: true
  }
]
export default defaultRoutes
