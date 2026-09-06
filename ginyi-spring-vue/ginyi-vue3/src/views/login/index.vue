<template>
    <n-card class="login-box" :style="{height: clientHeight + 'px'}">
        <div style="display: flex; justify-content: flex-end" class="dark-switch">
            <n-switch v-model:value="darkTheme">
                <template #checked>
                    <n-icon size="14" color="#ffd93b" background="#212126">
                        <SunnySharp/>
                    </n-icon>
                </template>
                <template #unchecked>
                    <n-icon size="14" color="#ffd93b" background="#212126">
                        <Moon/>
                    </n-icon>
                </template>
            </n-switch>
        </div>
        <div
            style="width: 400px; position: absolute; top: 40%; left: 50%; transform: translate(-50%, -50%);
            border: 1px solid #ccc; border-radius: 8px; padding: 30px;">
            <n-tabs
                class="card-tabs"
                default-value="signin"
                size="large"
                animated
                style="margin: 0 -4px"
                pane-style="padding-left: 4px; padding-right: 4px; box-sizing: border-box;">
                <n-tab-pane name="signin" tab="登录">
                    <LoginForm @doLogin="doLogin" :is-success="isLoginSuccess"/>
                </n-tab-pane>
                <n-tab-pane name="signup" tab="注册">
                    <RegisterForm @doRegister="doRegister"/>
                </n-tab-pane>
            </n-tabs>
        </div>
    </n-card>
</template>

<script lang="ts">
import {useSystemStore} from "@/store/modules/useSystemStore";
import {ILoginFormType, IRegisterFormType} from "@/interface/modules/system";
import {useUserStore} from "@/store/modules/useUserStore";
import {useCommonRouter} from "@/router";
import {menuController, userController} from "@/api";
import {defineComponent, ref} from "vue";
import {storeToRefs} from "pinia";
import {useRouterStore} from "@/store/modules/useRouterStore";
import LoginForm from "@/views/login/loginForm.vue";
import {Moon, SunnySharp} from "@vicons/ionicons5";
import RegisterForm from "@/views/login/registerForm.vue";

export default defineComponent({
    components: {
        LoginForm, RegisterForm, SunnySharp, Moon
    },
    setup() {
        // 系统深色主题
        const {darkTheme, clientHeight} = storeToRefs(useSystemStore())
        // 登录状态
        const isLoginSuccess = ref<boolean | string>(false)

        // 登录
        const doLogin = (data: ILoginFormType) => {
            userController.login(data).then(res => {
                return useUserStore().login(data.username, res.data)
            }).then(() => {
                isLoginSuccess.value = true
                return menuController.getRouterList()
            }).then(res => {
                useSystemStore().setMenuList(res.data.list)
                useSystemStore().resetBreadMenuList()
                useRouterStore().setRoutesList(res.data.list)
                window.$notification.success({
                    title: "登录成功",
                    content: "工作顺利，快乐摸鱼！",
                    duration: 2500,
                })
                useCommonRouter("home")
            }).catch(() => {
                // 用于更新验证码
                isLoginSuccess.value = `${false}_${new Date().valueOf()}`
            })
        }
        // 注册
        const doRegister = (data: IRegisterFormType) => {
            console.log('注册data', data)
        }
        return {
            isLoginSuccess,
            darkTheme, clientHeight,
            doLogin,
            doRegister,
            SunnySharp, Moon
        }
    }
})


</script>

<style scoped lang="less">
.login-box {
    border-radius: 0;
    background-image: url("../../../src/assets/bg-light.svg");
    background-repeat: no-repeat;
    background-size: 100% 100%;
}

.dark-switch .n-switch {
    ::v-deep(.n-switch__rail) {
        background-color: #000e1c;
    }
}
</style>