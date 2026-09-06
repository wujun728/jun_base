<script setup lang="ts">
import { forward } from '@/utils/router';
import { handelLogin } from '@/service/api/user'
import type { Login } from '@/service/types/user'
import { useUserStore } from '@/store';


const model = reactive<Login>({
    username: '',
    password: '',
})
const form = ref()
const userStore = useUserStore();


onLoad(async () => {
    const username = uni.getStorageSync('username')
    const password = uni.getStorageSync('password')
    const loginTime = uni.getStorageSync('loginTime')
    if (username && password && loginTime) {
        const currentTime = new Date().getTime();
        const days = (currentTime - loginTime) / (1000 * 60 * 60 * 24);
        if (days <= 30) {
            const res = await handelLogin({
                username: username,
                password: password,
            })
            userStore.setUserInfo({
                token: res.token
            });
            forward('index', {
                a: 1
            });
            console.log('免密登录成功！');
            uni.setStorageSync('token', res.token);

        }
    }
})



function handleBlur(prop: string) {
    form.value.validate(prop)
}
function loginFn() {
    form.value
        .validate()
        .then(async ({ valid }) => {
            if (valid) {
                const res = await handelLogin(model)
                userStore.setUserInfo({
                    token: res.token
                });
                // 登录成功将账号密码存到本地，免密登录
                uni.setStorageSync('token', res.token);
                uni.setStorageSync('username', model.username);
                uni.setStorageSync('password', model.password);
                uni.setStorageSync('loginTime', new Date().getTime());
                forward('index', {
                    a: 1
                });
            }
        })
        .catch((error) => {
            console.log(error, 'error')
        })
}
</script>

<template>
    <view class="loginPic">
        <img src="../../static/img/loginPic.png" alt="" style="width: 100%;height: 451rpx;">
        <view class="loginInput">
            <view style="width: 608rpx;">
                <wd-form ref="form" :model="model">
                    <wd-cell-group border>
                        <view class="label" style="margin-top: 87rpx;">账号</view>
                        <wd-input v-model="model.username" prop="username" clearable placeholder="请输入账号"
                            :rules="[{ required: true, message: '请输入账号' }]" @blur="handleBlur('username')" />
                        <view class="label" style="margin-top: 66rpx;">密码</view>
                        <wd-input v-model="model.password" prop="password" show-password clearable placeholder="请输入密码"
                            :rules="[{ required: true, message: '请输入密码' }]" @blur="handleBlur('password')" />
                    </wd-cell-group>
                    <view class="tip">忘记密码请联系企业管理员修改</view>
                    <view class="footer">
                        <wd-button type="primary" block @click="loginFn">登录</wd-button>
                    </view>
                </wd-form>
            </view>
            <view class="fixed">
                <view style="font-family: DingTalk JinBuTi;font-weight: 400;font-size: 22rpx;color: #999999;">做 中 国 安 全
                    应 急 科 技 创 新 引 擎 ！</view>
                <view style="font-family: DingTalk JinBuTi;font-weight: 400;font-size: 22rpx;color: #999999;">China's
                    Emergency Technology Innovation Engine!</view>
            </view>

        </view>
    </view>

</template>

<style scoped lang="scss">
.loginPic {
    position: relative;
    height: 100vh;
}

.fixed {
    position: fixed;
    bottom: calc(env(safe-area-inset-bottom));
    display: flex;
    align-items: center;
    justify-content: center;
    flex-direction: column;
    width: 100%;
}

.loginInput {
    position: absolute;
    top: 287rpx;
    width: 100%;
    height: 1032rpx;
    background: #ffffff;
    border-radius: 60rpx 60rpx 0rpx 0rpx;
    display: flex;
    justify-content: center;
    padding-top: 72rpx;
}

.label {
    font-family: Source Han Sans CN;
    font-weight: 500;
    font-size: 30rpx;
    color: #333333;
}

.tip {
    font-family: Source Han Sans CN;
    font-weight: 400;
    font-size: 26rpx;
    color: #999999;
    margin-top: 33rpx;
}

:deep(.wd-button.is-medium.is-round.data-v-aa3a6253) {
    width: 608rpx;
    height: 88rpx;
    background: #7EA4FD;
    border-radius: 14rpx;
    font-family: Source Han Sans CN;
    font-weight: 500;
    font-size: 30rpx;
    color: #FFFFFF;
}

.footer {
    margin-top: 40rpx;
}
</style>