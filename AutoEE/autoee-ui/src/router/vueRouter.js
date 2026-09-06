import { createRouter, createWebHistory, createWebHashHistory } from 'vue-router';
import HomePage from '/src/views/HomePage.vue';
import LoginPage from '/src/views/LoginPage.vue';
import NProgress from 'nprogress'; // progress bar
import 'nprogress/nprogress.css';
import { getToken } from '../utils/auth.js';
import vueStore from '../store/vueStore.js';
import { notification } from 'ant-design-vue';

// 定义路由规则：请求相应的路径时，展示对应的页面
const routes = [
  {
    path: '/',
    component: LoginPage,
  },
  {
    path: '/login',
    name: 'LoginPage',
    component: LoginPage,
  },
  {
    path: '/home',
    name: 'HomePage',
    component: HomePage,
    children: [
      {
        name: 'Workbench',
        path: 'workbench',
        // component: workbench,
        component: () => import('/src/views/workbench/WorkbenchPage.vue'),
        // components: { view: workbench },
        // components: {
        //   workbench: () => import("/src/views/WorkbenchPage.vue"),
        // },
        // components: {
        //   default: () => import("/src/views/WorkbenchPage.vue"),
        // },
      },
      {
        // 进入主页后，二级路由直接显示工作台页面
        path: '',
        component: () => import('/src/views/workbench/WorkbenchPage.vue'),
        // component: resolve=>(require(['/src/views/404.vue'])，resolve)
      },
      // {
      //   path: '',
      //   component: () => import('/src/views/404.vue'),
      //   // component: resolve=>(require(['/src/views/404.vue'])，resolve)
      // },
    ],
  },
  // {
  //   path: '',
  //   component: () => import('/src/views/404.vue'),
  // },
];

// 创建一个路由实例
const vueRouter = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});

// 白名单
const whiteList = ['/', '/login', '/auth-redirect', '/bind', '/register'];

// 通过import.meta.glob进行动态引入页面
const modules = import.meta.glob('/src/views/**/*.vue');

// 每次访问路由前的处理：控制前端页面访问权限
vueRouter.beforeEach(async (to, from) => {
  // to：要去的路由
  // from：当前路由
  // vuex4，废弃next()，直接返回true或false来决定是否放行当前路由

  // console.log('from.path=' + from.path);
  // console.log('to.path=' + to.path);
  // 开启进度条
  NProgress.start();

  // 已经获取到token
  if (getToken()) {
    // 全局设置信息
    to.meta.title && vueStore.dispatch('settings/setTitle', to.meta.title);

    // 已经获取到token，跳过登陆直接进入主页
    if (to.path === '/' || to.path === '/login') {
      console.log('已经获取到token，跳过登陆直接进入主页');
      vueRouter.push({ path: '/home' });
      NProgress.done();
    } else {
      // console.log('vueStore.getters.roles.length=' + vueStore.getters.roles.length);
      // 判断当前用户是否已拉取完user_info信息，如果尚未拉取用户信息，请求后台进行获取
      if (vueStore.getters.roles.length === 0) {
        console.log('[开始]-获取用户信息');
        // await关键字只能放到async函数里面，并且需要成对使用
        await vueStore
          .dispatch('GetUserInfo_Action')
          .then(async () => {
            console.log('[完成]-获取用户信息');
            // await关键字只能放到async函数里面，并且需要成对使用
            console.log('[开始]-获取路由信息');
            await vueStore.dispatch('GenerateRoutes_Action').then((accessRoutes) => {
              console.log('[完成]-获取路由信息');
              // console.log('accessRoutes=' + JSON.stringify(accessRoutes));
              // console.log('现有路由表=' + JSON.stringify(vueRouter.getRoutes()));
              console.log('[开始]-将用户可以访问的菜单添加到路由表');
              for (let index = 0; index < accessRoutes.length; index++) {
                // 获取父路由
                const fatherRoute = accessRoutes[index];
                // console.log("fatherRoute=" + JSON.stringify(fatherRoute));

                // 如果父路由没有name或者path，暂时先不处理
                if (!(fatherRoute.name && fatherRoute.path)) {
                  // vueRouter.addRoute(route);
                } else {
                  // 如果父路由是超链接，暂时先不处理
                  if (fatherRoute.meta && fatherRoute.meta.link && fatherRoute.meta.link != 'null') {
                    // console.log("[跳过]-父路由是超链接暂时先不处理");
                    // TODO
                  } else {
                    // 添加父路由-一级菜单
                    vueRouter.addRoute({
                      name: fatherRoute.name,
                      path: fatherRoute.path,
                      component: HomePage, // 一级菜单必须的有component，而且必须为之前的一级菜单HomePage
                    });
                    // console.log('[成功]-添加父路由，name=' + fatherRoute.name + '，path=' + fatherRoute.path);
                    // 循环父路由中的子路由
                    if (fatherRoute.children && fatherRoute.children.length) {
                      const childRouteArr = fatherRoute.children;
                      // console.log('childRouteArr=' + JSON.stringify(childRouteArr));
                      for (let i = 0; i < childRouteArr.length; i++) {
                        const childRoute = childRouteArr[i];
                        // console.log('childRoute=' + JSON.stringify(childRoute));
                        // console.log('childRoute.component=' + JSON.stringify(childRoute.component));
                        // 向当前的父路由中添加子路由-二级菜单
                        if (childRoute.component && childRoute.component != 'ParentView') {
                          vueRouter.addRoute(fatherRoute.name, {
                            name: childRoute.name,
                            path: childRoute.path,
                            component: modules['/src/views/' + childRoute.component + '.vue'],
                            // component: () => import('/src/views/' + childRoute.component + '.vue'),
                          });
                          // console.log('[成功]-添加子路由，name:' + childRoute.name + '，path:' + childRoute.path + '，父路由:' + fatherRoute.name);
                          // 循环当前路由中的子路由-三级菜单，应该够用了

                          // console.log('childRoute=' + JSON.stringify(childRoute));
                          // console.log('childRoute.children=' + JSON.stringify(childRoute.children));
                          if (childRoute.children && childRoute.children.length) {
                            const threeLevelRouteArr = childRoute.children;
                            for (let j = 0; j < threeLevelRouteArr.length; j++) {
                              const threeLevelRoute = threeLevelRouteArr[j];
                              // console.log('threeLevelRoute=' + JSON.stringify(threeLevelRoute));
                              // console.log('threeLevelRoute.component=' + JSON.stringify(threeLevelRoute.component));
                              // 向当前的父路由中添加子路由-三级菜单
                              if (threeLevelRoute.component && threeLevelRoute.component != 'ParentView') {
                                vueRouter.addRoute(childRoute.name, {
                                  name: threeLevelRoute.name,
                                  path: childRoute.path + '/' + threeLevelRoute.path,
                                  component: modules['/src/views/' + threeLevelRoute.component + '.vue'],
                                  // component: () => import('/src/views/' + threeLevelRoute.component + '.vue'),
                                });
                                // console.log('[成功]-添加三级子路由，name:' + threeLevelRoute.name + '，path:' + threeLevelRoute.path + '，父路由:' + childRoute.name);
                              }
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
              console.log('[完成]-将用户可以访问的菜单添加到路由表');
              // console.log('vueRouter.getRoutes=' + JSON.stringify(vueRouter.getRoutes()));

              // 不能使用forEach，异步执行导致出错
              // accessRoutes.forEach(route => {
              //   console.log(route);
              //   vueRouter.addRoute(route); // 动态添加可访问路由表
              // };);
              vueRouter.push({ path: to.path });
            });
          })
          .catch((err) => {
            vueStore.dispatch('LogOut_Action').then(() => {
              notification.error({
                message: '错误提示信息',
                description: err,
              });
              vueRouter.push({ path: '/login' });
            });
          });
        // 二级路由刷新页面时，出现白屏不能显示对应页面，重新获取用户信息后，再次请求对应路由
      }
    }
  } else {
    console.log('尚未获取到token');
    // 没有token
    if (whiteList.indexOf(to.path) !== -1) {
      // console.log('当前path=', to.path, '在白名、单中，可以直接访问');
      // 在免登录白名单，直接进入
    } else {
      vueRouter.push({ path: '/login' }); // 否则全部重定向到登录页
      // next(`/login?redirect=${to.fullPath}`);
      NProgress.done();
    }
  }
});

// 每次访问路由后的处理
vueRouter.afterEach(() => {
  NProgress.done();
  // NextLoading.done();
});

export default vueRouter;
