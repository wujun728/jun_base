<template>
    <n-form label-placement="left" label-width="auto">
        <n-form-item-row label="用户名">
            <n-input placeholder="用户名" v-model:value="registerForm.username"
                     @keyup.enter.native="handleRegister" clearable>
                <template #prefix>
                    <n-icon :component="Person"/>
                </template>
            </n-input>
        </n-form-item-row>
        <n-form-item-row label="密码">
            <n-input v-model:value="registerForm.password"
                     type="password" @keyup.enter.native="handleRegister"
                     show-password-on="mousedown"
                     placeholder="密码" clearable>
                <template #prefix>
                    <n-icon :component="BagOutline"/>
                </template>
            </n-input>
        </n-form-item-row>
        <n-form-item-row label="重复密码">
            <n-input v-model:value="registerForm.password2"
                     type="password" @keyup.enter.native="handleRegister"
                     show-password-on="mousedown"
                     placeholder="重复密码" clearable>
                <template #prefix>
                    <n-icon :component="BagOutline"/>
                </template>
            </n-input>
        </n-form-item-row>
        <n-form-item-row label="验证码">
            <n-input-group>
                <n-input v-model:value="registerForm.code"
                         placeholder="验证码" @keyup.enter.native="handleRegister"
                         clearable>
                    <template #prefix>
                        <n-icon :component="QrCode"/>
                    </template>
                </n-input>
                <img :src="`data:image/png;base64,${captchaCode ?? null}`"
                     @click="() => getCaptcha()"
                     style="width: 120px; height: 34px; cursor: pointer"
                     alt="重新获取">
            </n-input-group>
        </n-form-item-row>
    </n-form>
    <n-button type="primary" block @click="handleRegister">注册</n-button>
</template>

<script lang="ts">
import {defineComponent, onMounted, reactive, ref} from "vue";
import {IRegisterFormType} from "@/interface/modules/system";
import {BagOutline, Moon, Person, QrCode, SunnySharp} from '@vicons/ionicons5';
import {userController} from "@/api";

export default defineComponent({
    name: "RegisterForm",
    emits: ["doRegister"],
    setup(props, {emit}) {
        const registerForm = reactive<IRegisterFormType>({
            username: undefined,
            password: undefined,
            password2: undefined,
            code: undefined
        })
        // 验证码
        const captchaCode = ref<string | undefined>(undefined)

        // 获取验证码
        const getCaptcha = () => {
            userController.captcha().then(res => {
                captchaCode.value = res.data.img
            })
        }

        onMounted(() => {
            getCaptcha()
        })

        // 注册
        const handleRegister = () => {
            emit("doRegister", registerForm)
        }
        return {
            registerForm,
            handleRegister,
            captchaCode,
            getCaptcha,
            BagOutline, Moon, Person, QrCode, SunnySharp
        }
    }
})
</script>

<style scoped>

</style>