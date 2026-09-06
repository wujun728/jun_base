<script setup lang="ts">
import profile from '@/assets/images/profile.jpg'
import { getAction } from '@/utils/request';
import { ref } from 'vue';
import QRCodeView from '@/components/QRCodeView/index.vue';
import JsBarcodeView from '@/components/JsBarcodeView/index.vue';

const props = defineProps({
    orderNumber: {
        type: String,
        default: '20240610163141A001'
    }
});

const payType = ref('wechat');
const payUrl = ref('');
const payLoading = ref(false);

function handlePay() {
    payLoading.value = true;
    payUrl.value = '';
    // 兼容 orderNumber 可能为对象
    const orderNo = props.orderNumber;
    getAction<string>(`/pay/${payType.value}/url/${orderNo}`).then(res => {
        const url: string = res.data;
        if (payType.value === 'alipay' && url && url.startsWith('<form')) {
            // 支付宝返回form，新的窗口打开并自动提交
            const newWindow = window.open('', '_blank');
            if (newWindow) {
                newWindow.document.write(url);
                newWindow.document.close();
                newWindow.document.querySelector('form')?.submit();
            } else {
                // 如果被浏览器拦截，回退到原有方式
                const div = document.createElement('div');
                div.innerHTML = url;
                document.body.appendChild(div);
                div.querySelector('form')?.submit();
            }
        } else if (res.msg) {
            payUrl.value = url;
        }
    }).finally(() => {
        payLoading.value = false;
    });
}
</script>
<template>
    <div>
        <el-form>
            <el-form-item label="订单号">
                <el-input v-model="props.orderNumber"></el-input>
            </el-form-item>
            <el-form-item label="支付链接">
                <el-input v-model="payUrl" disabled></el-input>
            </el-form-item>
            <el-form-item label="支付方式">
                <el-select v-model="payType" style="width: 120px">
                    <el-option label="微信" value="wechat" />
                    <el-option label="支付宝" value="alipay" />
                    <el-option label="收钱吧" value="sqb" />
                </el-select>
                <el-button type="primary" :loading="payLoading" @click="handlePay">支付测试</el-button>
            </el-form-item>
            <el-form-item>
                <div class="codeview" v-if="payUrl">
                    <QRCodeView :data="payUrl" :logo="profile" :options="{ width: 200, height: 200 }" />
                    <JsBarcodeView :data="payUrl" />
                </div>
            </el-form-item>
        </el-form>
    </div>
</template>
<style lang="scss" scoped>
.codeview {
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
}
</style>