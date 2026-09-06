import {createRouter, createWebHashHistory, RouteRecordRaw} from "vue-router";
import {useUserStore} from "@/store/modules/useUserStore";
import {storeToRefs} from "pinia";
import {storage} from "@/hooks/useStorage";
import {store} from "@/store";
import {setting} from "@/config/setting";
import {useRouterStore} from "@/store/modules/useRouterStore";


/**
 * 系统路由
 */
const routes: Array<RouteRecordRaw> = [
    {
        path: "/",
        name: "Layout",
        redirect: "/home",
        component: () => import("@/layout/index.vue"),
        children: [
            {
                path: "home",
                name: "home",
                meta: {title: "首页"},
                component: () => import("@/views/home/index.vue")
            }
        ]
    },
    {
        path: "/login",
        name: "login",
        meta: {title: "登录"},
        component: () => import("@/views/login/index.vue")
    }
]

const router = createRouter({
    history: createWebHashHistory(),
    routes
})

router.beforeEach((to, from, next) => {
    window.$loading && window.$loading.start();
    document.title = `${setting.title} - ${to.meta.title as string}`
    const {authorization, tokenKey} = storeToRefs(useUserStore(store));
    const token = authorization?.value ?? storage.get(tokenKey?.value as string);

    if (to.name === "login") {
        if (token) {
            window.$dialog.warning({
                title: "温馨提醒",
                content: "当前状态【已登录】，是否退出登录回到登录页面？",
                positiveText: "确定",
                negativeText: "取消",
                onPositiveClick: () => {
                    useUserStore(store).logout().then(() => {
                        useRouterStore(store).$reset()
                        next({name: to.name as string})
                        window.$message.success("退出成功")
                    })
                },
                onNegativeClick: () => {
                    next({name: from.name as string})
                },
                onMaskClick: () => {
                    next({name: from.name as string})
                },
                onClose: () => {
                    next({name: from.name as string})
                },
                onEsc: () => {
                    next({name: from.name as string})
                }
            })
        } else {
            next()
        }
    } else {
        if (token) {
            // 注入路由
            useRouterStore(store).addRoutes()
            if (!router.hasRoute(to.name as string)) {
                next({...to, replace: true});
            } else {
                next();
            }
        } else {
            next({name: "login"})
        }
    }

})

router.afterEach(() => {
    window.$loading && window.$loading.finish();
})

/**
 * 封装路由跳转
 * @param name 路由name
 * @param query 路由参数
 */
export const useCommonRouter = (name: string, query?: any) => {
    router.push({
        name: name,
        query: query
    })
}

export default router