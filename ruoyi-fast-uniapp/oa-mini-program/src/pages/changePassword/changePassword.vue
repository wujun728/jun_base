<script setup lang="ts">
import { changePassword } from '@/service/api/user'
import type { Password } from '@/service/types/user'
import { useUserStore } from '@/store';

const model = reactive<Password>({
    password: '',
})

const form = ref()
const userStore = useUserStore();
function loginFn() {
    form.value
        .validate()
        .then(async ({ valid }) => {
            if (valid) {
                const res = await changePassword(model)
                console.log(res, 'rerwerwerwerwerw');
                if (res) {
                    userStore.setUserInfo({
                        token: ''
                    });
                    uni.reLaunch({
                        url: '/pages/login/login'
                    })
                }
            }
        })
        .catch((error) => {
            console.log(error, 'error')
        })
}
</script>

<template>
    <view class="loginInput">
        <view style="width: 608rpx;">
            <wd-form ref="form" :model="model">
                <wd-cell-group border>
                    <view class="label" style="margin-top: 66rpx;">新密码</view>
                    <wd-input v-model="model.password" prop="password" show-password clearable placeholder="请输入新密码"
                        :rules="[{ required: true, message: '请填写新密码' }]" />
                </wd-cell-group>
                <view class="footer">
                    <wd-button type="primary" block @click="loginFn">提交</wd-button>
                </view>
            </wd-form>
        </view>
    </view>

</template>

<style scoped lang="scss">
.loginInput {
    width: 100%;
    height: 100vh;
    background: #ffffff;
    display: flex;
    justify-content: center;
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