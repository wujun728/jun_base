// 公共路由
export const constantRoutes = [
  {
    path: '/login',
    name: 'LoginPage',
    component: () => import(/* webpackChunkName: "login" */ '@/views/LoginPage.vue'),
    hidden: true,
  },
  {
    path: '/login2',
    name: 'LoginPage2',
    component: () => import(/* webpackChunkName: "login" */ '@/views/LoginPage2.vue'),
    hidden: true,
  },
  {
    path: '/login3',
    name: 'LoginPage3',
    component: () => import(/* webpackChunkName: "login" */ '@/views/LoginPage3.vue'),
    hidden: true,
  },
  {
    path: '/register',
    name: 'RegisterPage',
    component: () => import(/* webpackChunkName: "login" */ '@/views/RegisterPage.vue'),
    hidden: true,
  },
  {
    path: '/404',
    component: () => import(/* webpackChunkName: "error" */ '@/views/404.vue'),
    hidden: true,
  },
  //   {
  //     path: '/401',
  //     component: () => import(/* webpackChunkName: "error" */ '@/views/error/401.vue'),
  //     hidden: true
  //   },
  {
    path: '/',
    redirect: '/home/workbench',
  },
  {
    path: '/home',
    component: () => import(/* webpackChunkName: "layout" */ '@/layout/BaseLayout.vue'),
    redirect: '/home/workbench',
    children: [
      {
        name: 'Workbench',
        path: 'workbench',
        component: () =>
          import(/* webpackChunkName: "workbench" */ '@/views/workbench/WorkbenchPage.vue'),
        meta: {
          title: '工作台',
          icon: 'dashboard',
          affix: true,
        },
      },
    ],
  },
  {
    path: '/system/user/profile',
    name: 'profile',
    component: () => import(/* webpackChunkName: "layout" */ '@/layout/BaseLayout.vue'),
    redirect: '/system/user/profile',
    meta: {
      title: '个人信息',
      icon: 'user',
    },
    children: [
      {
        path: '',
        name: 'UserProfile',
        component: () =>
          import(/* webpackChunkName: "profile" */ '@/views/system/user/profile/index.vue'),
        meta: {
          title: '个人信息',
          icon: 'user',
        },
      },
    ],
  },
  {
    path: '/system/role/module',
    component: () => import(/* webpackChunkName: "layout" */ '@/layout/BaseLayout.vue'),
    redirect: '/system/role/module/authUser',
    children: [
      {
        name: 'RoleAuthUser',
        path: 'authUser',
        component: () =>
          import(/* webpackChunkName: "role" */ '@/views/system/role/module/authUser.vue'),
        meta: {
          title: '角色授权用户',
          icon: 'user',
        },
      },
    ],
  },
];

export const notFoundRoute = {
  path: '/:pathMatch(.*)*',
  name: 'NotFound',
  redirect: '/404',
  hidden: true,
};

export default constantRoutes;
