import { RouteItem } from '@/types/route'
const Layout = () => import('@/layout/index.vue')

// 公共路由,配置详情请参见RouteItem定义
export const constantRoutes: RouteItem[] = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/views/redirect/index.vue')
      }
    ]
  },
  {
    path: '/auth',
    component: () => import('@/views/auth/index.vue'),
    hidden: true,
    children: [
      {
        path: 'login',
        name: 'Login',
        component: () => import('@/views/auth/login.vue'),
        hidden: true
      },
      {
        path: 'register',
        name: 'Register',
        component: () => import('@/views/auth/register.vue'),
        hidden: true
      }
    ]
  },
  {
    path: "/:pathMatch(.*)*",
    component: () => import('@/views/error/404.vue'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/error/401.vue'),
    hidden: true
  },
  {
    path: '',
    component: Layout,
    redirect: '/index',
    children: [
      {
        path: '/index',
        component: () => import('@/views/index.vue'),
        name: 'Index',
        meta: { title: '首页', icon: 'dashboard', affix: true }
      }
    ]
  },
  {
    path: '/laboratory',
    component: Layout,
    meta: { title: '实验室', icon: 'dashboard' },
    children: [
      {
        path: 'threeTest',
        component: () => import('@/views/three/gltfmode.vue'),
        name: 'threeTest',
        meta: { title: 'three实验室', icon: 'dashboard' }
      },
      {
        path: "websocket",
        component: () => import('@/views/websocket.vue'),
        name: 'websocket',
        meta: { title: 'websocket实验室', icon: 'dashboard' }
      },
    ]
  },
  {
    path: '/user',
    component: Layout,
    hidden: true,
    redirect: 'noredirect',
    children: [
      {
        path: 'profile',
        component: () => import('@/views/system/user/profile/index.vue'),
        name: 'Profile',
        meta: { title: '个人中心', icon: 'user' }
      }
    ]
  },

]

