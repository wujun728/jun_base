<script lang="ts" setup>
// import { useUserStore } from '@/store';
import { badgeListOfMine, wearBadge } from '@/service/api/user'
import { back } from '@/utils/router'
import { useUserInfoStore } from '@/store';
import { getUserInfo } from '@/service/api/user'


onShow(() => {
    getBadgeList()
})
const active = ref<any>()
const arr = ref<any>()
const badgeList = ref<any>([
    {badgeName:'不佩戴',badgeId:null}
])
const userStore = useUserInfoStore();

async function getUserInfoFn() {
    const res = await getUserInfo({})
    userStore.setUserInfo2({
        users: { ...res.user }
    });

}
function click(item, index) {
    arr.value = []
    arr.value.push(item.badgeId)
    active.value = index
    console.log(arr.value[0]);
}
async function getBadgeList() {
    badgeList.value = [
    {badgeName:'不佩戴',badgeId:null}
]
    const data = await badgeListOfMine({})
    badgeList.value =badgeList.value.concat(data)
}
async function wearBadgeFn() {
    const data = await wearBadge({ badgeId: arr.value[0]?arr.value[0]:'' })
    if (data) {
        getUserInfoFn()
        back(1)
    }
}
</script>

<template>
    <view class="content">
        <view class="grid">
            <view v-for="(item, index) in badgeList" :key="index" class="one"
                :style="{ 'border': (active === index ? '4px solid #7EA4FD' : 'none') }" @click="click(item, index)">
                <view v-if="!item.badgeId"><wd-status-tip image="content" /></view>
                <img v-else :src=item.badgeImage alt="" class="slot-img">
                <view>{{ item.badgeName }}</view>
            </view>
        </view>
        <!-- <wd-grid square :column="4" :gutter="10" clickable>
            <wd-grid-item v-for="(item, index) in badgeList" :key="index" use-icon-slot :text='item.badgeName'
                icon-size="36px" @itemclick="click(item, index)">
                <template #icon>
                    <image class="slot-img2" :src=item.badgeImage />
                </template>
</wd-grid-item>
</wd-grid> -->
        <view class="footer">
            <wd-button type="primary" block @click="wearBadgeFn">立即装扮</wd-button>
        </view>
    </view>
</template>

<style scoped lang="scss">
.content {
    height: 100vh;
    background: #F6F6F6;
    position: relative;

    .grid {
        display: flex;
        flex-wrap: wrap;
        align-items: center;
        justify-content: flex-start;
        padding: 0 20rpx;


        .one {
            margin: 20rpx 20rpx 0 0;
            width: calc((100% - 60rpx) / 4);
            min-width: calc((100% - 60rpx) / 4);
            max-width: calc((100% - 60rpx) / 4);
            height: 185rpx;
            background: #FFFFFF;
            border-radius: 14rpx;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            font-family: Source Han Sans CN;
            font-weight: 500;
            font-size: 26rpx;
            color: #333333;
            line-height: 33rpx;
            box-sizing: border-box;
        }

        .one:nth-child(4n + 4) {
            margin-right: 0;
        }

        .border {
            border: 1px solid blue;
        }
    }

    :deep(.wd-grid-item__content.is-square.data-v-0c130832) {
        border-radius: 14rpx;
    }
    :deep(.wd-status-tip__image.data-v-1a3f88eb) {
       width: 100rpx !important;
       height: 100rpx !important;
    }

    .slot-img {
        height: 50px;
        width: 50px;
        border-radius: 4px;
        margin-bottom: 20rpx;
    }

    .slot-img2 {
        height: 36px;
        width: 36px;
        border-radius: 4px;
    }

    .footer {
        position: fixed;
        bottom: calc(60px + env(safe-area-inset-bottom));
        width: 100%;

        :deep(.wd-button.is-medium.is-round) {
            width: 600rpx;
            height: 88rpx;
            background: #7EA4FD;
            border-radius: 14rpx;
            font-family: Source Han Sans CN;
            font-weight: 500;
            font-size: 30rpx;
            color: #FFFFFF;
        }
    }

}
</style>