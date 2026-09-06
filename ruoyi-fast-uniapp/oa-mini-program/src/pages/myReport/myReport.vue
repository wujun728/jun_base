<script lang="ts" setup>
import { reportListToSubmit } from "@/service/api/user";
import { forward } from "@/utils/router";
onLoad(() => {
    getMySubmitList()
    uni.setNavigationBarColor({
    frontColor: '#000000',
    backgroundColor: '#9bb9fc',
  })
})
let toSubmitData = ref<any>([])
function goTo(val) {
    forward(val)
}
async function getMySubmitList() {
    const data = await reportListToSubmit({})
    toSubmitData.value = data
    console.log(data);
}
function click(val) {
    console.log('ewsdasd');

    uni.navigateTo({
        url: '/pages/editDayReport/editDayReport',
        success(res) {
            res.eventChannel.emit('data', {
                templateId: val.templateId,
                templateName: val.templateName
            })
        },
    })
    // forward('editDayReport', {
    //     templateId: val.templateId,
    //     templateName: val.templateName
    // })
}
</script>
<template>
    <view class="content">
        <view class="banner">
            <img src="../../static/img/reportban.png" alt="" style="width: 100%;height: 100%;">
            <view
                style="display: flex;align-items: center;justify-content: space-evenly;position: absolute;z-index: 99;bottom: 50rpx;width: 100%;">
                <img src="../../static/img/mySubmit.png" alt="" style="width: 345rpx;height: 145rpx;"
                    @click="goTo('myReport-mySubmit')">
                <img src="../../static/img/myResive.png" alt="" style="width: 345rpx;height: 145rpx;"
                    @click="goTo('myReport-myResive')">
            </view>
        </view>
        <view class="title">发起汇报</view>
        <view style="padding: 20rpx;background: #F6F6F6;">
            <view v-for="(item, index) in toSubmitData" :key="index" class="main" @click="click(item)">
                <img :src=item.templateIcon alt="" style="width: 66rpx;height: 66rpx;border-radius: 10rpx;">
                <view style="margin-left: 26rpx;">
                    <view class="tt">{{ item.templateName }}</view>
                    <view class="mm">{{ item.templateDescription }}</view>
                </view>
                <img src="../../static/icon/arrow_right.png" alt=""
                    style="width: 39rpx;height: 39rpx;position: absolute;right: 40rpx;">
            </view>
        </view>
        <view class="fixed">
            <view style="font-family: DingTalk JinBuTi;font-weight: 400;font-size: 22rpx;color: #999999;">
                做 中 国 安 全 应 急 科 技 创 新 引 擎 ！
            </view>
            <view style="font-family: DingTalk JinBuTi;font-weight: 400;font-size: 22rpx;color: #999999;">
                China's Emergency Technology Innovation Engine!
            </view>
        </view>
    </view>

</template>
<style scoped lang="scss">
.content {
    height: 100vh;
    background: #F6F6F6;
    position: relative;

    .fixed {
        position: fixed;
        bottom: calc(env(safe-area-inset-bottom));
        display: flex;
        align-items: center;
        justify-content: center;
        flex-direction: column;
        width: 100%;
    }

    .banner {
        width: 100%;
        height: 380rpx;
        background-size: 100%;
        overflow: hidden;
        position: relative;
    }

    .title {
        font-family: Source Han Sans CN;
        font-weight: bold;
        font-size: 28rpx;
        color: #333333;
        margin-left: 20rpx;
        margin-top: -30rpx;
    }

    .main {
        position: relative;
        background: #FFFFFF;
        border-radius: 20rpx;
        display: flex;
        align-items: center;
        padding: 30rpx;
        margin-bottom: 20rpx;

        .tt {
            font-family: Source Han Sans CN;
            font-weight: 500;
            font-size: 32rpx;
            color: #000000;
        }

        .mm {
            font-family: Source Han Sans CN;
            font-weight: 400;
            font-size: 26rpx;
            color: #666666;
            margin-top: 10rpx;
            width: 400rpx;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;

        }
    }
}
</style>