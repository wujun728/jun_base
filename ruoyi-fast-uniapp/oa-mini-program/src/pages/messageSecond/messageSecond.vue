<script lang="ts" setup>
import tips from '../../static/icon/tips.png'
import internalRemind from '../../static/icon/internalRemind.png'
import reportRemind from '../../static/icon/reportRemind.png'
import scheduleRemind from '../../static/icon/scheduleRemind.png'
import projectAllocation from '../../static/icon/projectAllocation.png'
import projectFllow from '../../static/icon/projectFllow.png'
import { announcement, updateAnnouncementSetReadByIdList, reportNotice, updateReportNoticeSetReadByIdList } from '@/service/api/homePage'
// import type { HomePage } from '@/service/types/homePage'
import { useInit } from '@/hooks/useInit';
import { readReport } from '@/service/api/user'
// import { forward } from '@/utils/router'

// onLoad(() => {
//     noticeList()
// })
onShow(() => {
    noticeList()
})
const tabb = ref<number>(0)
const state = ref<any>('')
const state2 = ref<any>('')
const imgUrl = ref('')
const sixPoint = ref()

const params = reactive<any>({
    pageNum: 1,
    pageSize: 8,
    total: 0,
    type: '1',
    status: '0'
})
let noticeData = ref<any>([])

async function click(item) {
    if (sixPoint.value === '1' || sixPoint.value === '2') {
        updateAnnouncement(item)
        uni.navigateTo({
            url: '/pages/messageDetail/messageDetail',
            success(res) {
                res.eventChannel.emit('data', item)
            },
        })
    } else if (sixPoint.value === '3') {
        updateReport(item)
        if (!item.reportRecordId) {
            uni.navigateTo({
                url: '/pages/myReport/myReport',
                success(res) {
                    res.eventChannel.emit('data', item)
                },
            })
        } else {
            // eslint-disable-next-line @typescript-eslint/no-unused-vars
            const res = await readReport({
                recordId: item.reportRecordId,
                templateId: item.reportTemplateId
            })
            uni.navigateTo({
                url: '/pages/dayReport-message/dayReport-message',
                success(res) {
                    res.eventChannel.emit('data', item)
                },
            })
        }


    }


}

function img(pageQuery) {
    sixPoint.value = pageQuery.a
    if (pageQuery.a === '1') {
        uni.setNavigationBarTitle({
            title: '温馨提示'
        })
        imgUrl.value = tips
    } else if (pageQuery.a === '2') {
        uni.setNavigationBarTitle({
            title: '内部通知'
        })
        imgUrl.value = internalRemind
    } else if (pageQuery.a === '3') {
        imgUrl.value = reportRemind
        uni.setNavigationBarTitle({
            title: '汇报提醒'
        })
    } else if (pageQuery.a === '4') {
        imgUrl.value = scheduleRemind
    } else if (pageQuery.a === '5') {
        imgUrl.value = projectAllocation
    } else if (pageQuery.a === '6') {
        imgUrl.value = projectFllow
    }
}

async function updateAnnouncement(item) {
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    const data = await updateAnnouncementSetReadByIdList({ ids: item.id })
    noticeData.value = []
    params.pageNum = 1
}
async function updateReport(item) {
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    const data = await updateReportNoticeSetReadByIdList({ ids: item.reportNoticeId })
    noticeData.value = []
    params.pageNum = 1
}


async function noticeList() {

    const { pageQuery }:any = useInit();
    console.log(pageQuery, ' pageQuery');
    img(pageQuery)
    if (pageQuery.a === '1' || pageQuery.a === '2') {
        params.type = pageQuery.a
        const res: any = await announcement(params)
        noticeData.value = noticeData.value.concat(res.records)
        params.total = res.totalPage
        if (res.totalPage > 1) {
            state.value = 'error'
            state2.value = 'error'
        } else {
            state.value = 'finished'
            state2.value = 'finished'
        }
    } else if (pageQuery.a === '3') {
        params.type = '1'
        const res: any = await reportNotice(params)
        noticeData.value = noticeData.value.concat(res.records)
        params.total = res.pages
        if (res.pages > 1) {
            state.value = 'error'
            state2.value = 'error'
        } else {
            state.value = 'finished'
            state2.value = 'finished'
        }
    }


}

function handleClick(val) {
    console.log(val);
    params.status = val.index
    params.pageNum = 1
    noticeData.value = []
    noticeList()
}

function loadmore() {
    setTimeout(() => {
        params.pageNum++
        noticeList()
        state.value = 'loading'
    }, 200)

}
onReachBottom(() => {
    console.log('到底部了！！！');
    if (params.pageNum < params.total) {
        loadmore()
    } else if (params.pageNum === params.total) {
        state.value = 'finished'
    }
})

</script>

<template>
    <view class="content">
        <wd-tabs v-model="tabb" sticky @click="handleClick">
            <block :key="1">
                <wd-tab title="未读">
                    <view class="hasRead">
                        <view style="display: flex;flex-direction: column;">
                            <view v-for="(item, index) in noticeData" :key="index" class="remind list-item"
                                @click="click(item)">
                                <view style="display: flex;align-items: center;justify-content: center;">
                                    <img :src=imgUrl alt="" style="width: 70rpx;height: 70rpx;">
                                </view>
                                <view style="margin-left: 20rpx;">
                                    <view v-if="sixPoint === '1' || sixPoint === '2'" class="title">
                                        {{ item.title }}
                                    </view>
                                    <view v-if="sixPoint === '3'" class="title">
                                        {{ item.message }}
                                    </view>
                                    <view
                                        style="font-family: Source Han Sans CN;font-weight: 400;font-size: 22rpx;color: #666666;display: flex;align-items: center;">
                                        <view v-if="sixPoint === '1' || sixPoint === '2'" style="width: 224rpx;">
                                            发布人：{{ item.creatorName }}</view>
                                        <view v-if="sixPoint === '3'" style="width: 224rpx;">
                                            {{ item.bottomInfo }}
                                        </view>
                                        <view style="margin-left: 33rpx;">时间：{{ item.updateTime }}</view>
                                    </view>
                                </view>
                                <img src="../../static/icon/noRead.png" alt=""
                                    style="width: 70rpx;height: 30rpx;position: absolute;top: 32rpx;right: 20rpx;">
                            </view>
                            <wd-loadmore :state="state" error-text="点击加载更多" @reload="loadmore" />
                        </view>

                    </view>
                </wd-tab>
            </block>
            <block :key="2">
                <wd-tab title="已读">
                    <view class="hasRead">
                        <view style="display: flex;flex-direction: column;">
                            <view v-for="(item, index) in noticeData" :key="index" class="remind" @click="click(item)">
                                <view style="display: flex;align-items: center;justify-content: center;">
                                    <img :src=imgUrl alt="" style="width: 70rpx;height: 70rpx;">
                                </view>
                                <view style="margin-left: 20rpx;">
                                    <view v-if="sixPoint === '1' || sixPoint === '2'" class="title">
                                        {{ item.title }}
                                    </view>
                                    <view v-if="sixPoint === '3'" class="title">
                                        {{ item.message }}
                                    </view>
                                    <view
                                        style="font-family: Source Han Sans CN;font-weight: 400;font-size: 22rpx;color: #666666;display: flex;align-items: center;">
                                        <view v-if="sixPoint === '1' || sixPoint === '2'" style="width: 224rpx;">
                                            发布人：{{ item.creatorName }}</view>
                                        <view v-if="sixPoint === '3'" style="width: 224rpx;">{{ item.bottomInfo }}
                                        </view>
                                        <view style="margin-left: 33rpx;">时间：{{ item.updateTime }}</view>
                                    </view>
                                </view>
                                <img src="../../static/icon/hasRead.png" alt=""
                                    style="width: 70rpx;height: 30rpx;position: absolute;top: 32rpx;right: 20rpx;">
                            </view>
                            <wd-loadmore :state="state2" error-text="点击加载更多" @reload="loadmore" />
                        </view>
                    </view>
                </wd-tab>
            </block>
        </wd-tabs>
    </view>
</template>

<style scoped lang="scss">
.content {
    height: 100vh;
    background: #F6F6F6;

    .hasRead {
        padding: 0 20rpx;
        background: #F6F6F6;

        .remind {
            height: 120rpx;
            background: #FFFFFF;
            border-radius: 4rpx;
            display: flex;
            align-items: center;
            margin-top: 20rpx;
            padding: 0 20rpx;
            position: relative;

            .title {
                font-family: Source Han Sans CN;
                font-weight: 500;
                font-size: 30rpx;
                color: #333333;
                max-width: 400rpx;
                white-space: nowrap;
                text-overflow: ellipsis;
                overflow: hidden;
            }
        }

        .list-item {
            position: relative;
            display: flex;
            background: #fff;
            color: #464646;
        }

        .list-item:after {
            position: absolute;
            display: block;
            content: '';
            height: 1px;
            left: 0;
            width: 100%;
            bottom: 0;
            background: #eee;
            transform: scaleY(0.5);
        }
    }


}
</style>