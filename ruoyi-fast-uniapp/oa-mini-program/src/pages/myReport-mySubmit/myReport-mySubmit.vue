<script lang="ts" setup>
import { useInit } from "@/hooks/useInit";
import { submitReportListOfMine, reportListOfAll } from '@/service/api/user'
// import type { MyReport } from "@/service/types/user";
import { dayjs } from "wot-design-uni";
onLoad(() => {
    getMyReport()
    reportList()
    const { pageName, pagePath, pageQuery } = useInit();
    console.log(pageName, pagePath, pageQuery, 'pageName,pagePath, pageQuery');
})
const list1 = ref([])
const value = ref<number[]>([])
// function open({ value }) {
//     console.log(value)
// }
const state = ref<any>('')

const model = reactive<any>({
    pageNum: 1,
    pageSize: 10,
    startTime: '',
    endTime: '',
    templateId: '1',
    pages: 0
})
let myReportList = ref<any>([])

function handleConfirm({ value }) {
    myReportList.value = []
    model.startTime = dayjs(value[0]).format('YYYY-MM-DD 00:00:00')
    model.endTime = dayjs(value[1]).format('YYYY-MM-DD 23:59:59')
    getMyReport()
}
const active = ref(0)
// function open() {
//     value.value = []
//     myReportList.value = []
//     state.value = ''
//     model.pageNum = 1
//     model.startTime = ''
//     model.endTime = ''
//     getMyReport()
// }
function click(val) {
    console.log(val);
    value.value = []
    myReportList.value = []
    state.value = ''
    active.value = val.index
    model.templateId = val.templateId
    model.pageNum = 1
    model.startTime = ''
    model.endTime = ''
    getMyReport()
}
async function getMyReport() {
    const data = await submitReportListOfMine(model)

    myReportList.value = myReportList.value.concat(data.records)
    model.pages = data.pages
    if (data.pages > 1) {
        state.value = 'error'
    } else {
        state.value = 'finished'
    }
    console.log("🚀 ~ myReportList:", myReportList.value)
}
async function reportList() {
    const data = await reportListOfAll({})
    list1.value = data.map(item => {
        return {
            name: item.templateName,
            templateId: item.templateId
        }
    })
}
function checkDetail(val) {
    uni.navigateTo({
        url: '/pages/dayReport/dayReport',
        success(res) {
            res.eventChannel.emit('data', val)
        },
    })
}
function loadmore() {
    setTimeout(() => {
        model.pageNum++
        getMyReport()
        state.value = 'loading'
    }, 200)
}
onReachBottom(() => {
    console.log('到底部了！！！');
    if (model.pageNum < model.pages) {
        loadmore()
    } else if (model.pageNum === model.pages) {
        state.value = 'finished'
    }
})

</script>
<template>
    <view class="content">
        <u-sticky bgColor="#fff">
            <u-tabs :list="list1" @click="click">
                <!-- <template #right>
                    <view
                        style="padding-left: 4px;display: flex;align-items: center;line-height: 1;flex: 0.2;justify-content: center;"
                        @tap="open">
                        <wd-button size="small">重置</wd-button>
                    </view>
                </template> -->
            </u-tabs>
        </u-sticky>
        <wd-calendar v-model="value" type="daterange" label="日期范围筛选" @confirm="handleConfirm" />
        <view style="padding: 20rpx;background: #F6F6F6;">
            <view v-for="(item, index) in myReportList" :key="index" class="main" @click="checkDetail(item)">
                <view style="display: flex;align-items: center;justify-content: center;position: relative;">
                    <img :src=item.reportTemplateVO.templateIcon alt="" style="width: 70rpx;height: 70rpx;border-radius: 12rpx;">
                </view>
                <view style="margin-left: 20rpx;">
                    <view
                        style="font-family: Source Han Sans CN;font-weight: 500;font-size: 30rpx;color: #333333;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;width: 400rpx;line-height: 1;">
                        {{ item.userNameAvatarVO.name }}提交的{{ item.reportTemplateVO.templateName }}
                    </view>
                    <view style="font-family: Source Han Sans CN;font-weight: 400;font-size: 24rpx;color: #666666;">
                        {{ item.reportTimeInfo }}
                    </view>
                </view>
                <view style="position: absolute;bottom: 0;left: 0;" class="btm">
                    <img src="../../static/icon/view.png" alt=""
                        style="width: 20rpx;height: 15rpx;margin-right: 10rpx;">
                    <view>{{ item.reportReadCount }}人已读</view>
                    <img src="../../static/icon/write.png" alt=""
                        style="width: 20rpx;height: 20rpx;margin-left: 40rpx;margin-right: 10rpx;">
                    <view>{{ item.reportMessageCount }}</view>
                </view>
            </view>
            <wd-loadmore :state="state" error-text="点击加载更多" :finished-text="myReportList && myReportList.length > 0?'已全部加载完毕':'暂无数据'" @reload="loadmore" />
        </view>
    </view>


</template>
<style scoped lang="scss">
.content {
    height: 100vh;
    background: #F6F6F6;

    // :deep(.u-tabs__wrapper__nav__item.data-v-0546c3e4) {
    //     // flex: 1;
    //     width: 180rpx;
    // }

    .main {
        position: relative;
        height: 120rpx;
        display: flex;
        align-items: center;
        background: #FFFFFF;
        border-radius: 8rpx;
        margin-bottom: 20rpx;
        padding: 0 20rpx 20rpx 20rpx;

        .btm {
            display: flex;
            align-items: center;
            line-height: 1;
            padding-left: 20rpx;
            font-family: Source Han Sans CN;
            font-weight: 400;
            font-size: 24rpx;
            color: #999999;
            line-height: 46rpx;
        }

        .yaun {
            width: 21rpx;
            height: 21rpx;
            border-radius: 50%;
            border: 1rpx solid #DEDEDE;
        }
    }
}
</style>