const Layout = () => import('@/layout/index.vue')
export default [
  {
    path: '/flowable',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'definition/model/',
        component: () => import('@/modules/flowable/view/definition/model.vue'),
        name: 'Model',
        hidden: true,
        meta: { title: '流程设计', icon: '' }
      },
      {
        path: 'task/finished/detail/index',
        component: () => import('@/modules/flowable/view/task/finished/detail/index.vue'),
        name: 'FinishedRecord',
        hidden: true,
        meta: { title: '流程详情', icon: '' }
      },
      {
        path: 'task/myProcess/detail/index',
        component: () => import('@/modules/flowable/view/task/myProcess/detail/index.vue'),
        name: 'MyProcessRecord',
        hidden: true,
        meta: { title: '流程详情', icon: '' }
      },
      {
        path: 'task/myProcess/send/index',
        component: () => import('@/modules/flowable/view/task/myProcess/send/index.vue'),
        name: 'SendRecord',
        hidden: true,
        meta: { title: '流程发起', icon: '' }
      },
      {
        path: 'task/todo/detail/index',
        component: () => import('@/modules/flowable/view/task/todo/detail/index.vue'),
        name: 'TodoRecord',
        hidden: true,
        meta: { title: '流程处理', icon: '' }
      }
    ]
  },
]