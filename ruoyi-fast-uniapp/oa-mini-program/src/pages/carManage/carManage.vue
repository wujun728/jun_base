<script lang="ts" setup>
import { selectVehicleList, vehicleStatistics } from '@/service/api/carManage'
import steps1 from '@/static/img/steps1.png'
import steps2 from '@/static/img/steps2.png'
import steps3 from '@/static/img/steps3.png'
import steps4 from '@/static/img/steps4.png'

onLoad(() => {
  uni.setNavigationBarColor({
    frontColor: '#000000',
    backgroundColor: '#8daffd',
  })
});
onShow(() => {
    getNum()
    getCarList()
});






const state = ref<any>('')
const returnCount = ref<any>()
const borrowCount = ref<any>()
const params = ref({
    pageNum: 1,
    pageSize: 10,
    pages: 0
})
const carList = ref<any>([])
// 查询车辆列表
async function getCarList() {
    const data = await selectVehicleList(params.value)
    carList.value = carList.value.concat(data.records)
    params.value.pages = data.pages

    if (data.pages > 1) {
        state.value = 'error'
    } else {
        state.value = 'finished'
    }
    console.log("🚀 ~ getCarList ~ carList.value:", carList.value)
}
// 加载更多
function loadmore() {
    setTimeout(() => {
        params.value.pageNum++
        getCarList()
        state.value = 'loading'
    }, 200)
}
onReachBottom(() => {
    console.log('到底部了！！！');
    if (params.value.pageNum < params.value.pages) {
        loadmore()
    } else if (params.value.pageNum === params.value.pages) {
        state.value = 'finished'
    }
})

// 待出车，待还车
async function getNum() {
    const data = await vehicleStatistics({})
    console.log(data);
    returnCount.value = data.returnCount
    borrowCount.value = data.borrowCount

}
// 用车记录
function CarRecordsFn(val) {
    params.value.pageNum = 1
    carList.value = []
    state.value = ''
    uni.navigateTo({
        url: '/pages/carRecords/carRecords',
        success(res) {
            res.eventChannel.emit('data', val)
        },
    })
}
// 预约用车
function bookingCarFn(val) {
    params.value.pageNum = 1
    carList.value = []
    state.value = ''
    uni.navigateTo({
        url: '/pages/carReservation/carReservation',
        success(res) {
            res.eventChannel.emit('data', val)
        },
    })
}
// 跳转我的用车
function goMineCar(val) {
    params.value.pageNum = 1
    carList.value = []
    state.value = ''
    if (val === 1) {
        uni.navigateTo({
            url: '/pages/myCarUse1/myCarUse1',
            success(res) {
                res.eventChannel.emit('data', {
                    type: val
                })
            },
        })
    } else if (val === 0) {
        uni.navigateTo({
            url: '/pages/myCarUse/myCarUse',
            success(res) {
                res.eventChannel.emit('data', {
                    type: val
                })
            },
        })
    } else {
        uni.navigateTo({
            url: '/pages/myCarUseAll/myCarUseAll',
            success(res) {
                res.eventChannel.emit('data', {
                    type: val
                })
            },
        })
    }


}

const show = ref(false)
function showTips() {
    show.value = true
}
const swiperList = ref([
    steps1,
    steps2,
    steps3,
    steps4,
])
function handleClick(e) {
    console.log(e)
}
function onChange(e) {
    console.log(e)
}
</script>

<template>
    <page-meta :page-style="`overflow:${show ? 'hidden' : 'visible'};`"></page-meta>
    <view class="content">
        <view class="father">
            <view class="top">
                <view
                    style="display: flex;margin-top: 8rpx;justify-content: space-evenly;align-items: center;padding: 20rpx;">
                    <view style="flex: 1;height: 100%;">
                        <view style="display: flex;align-items: center" class="top2" @click="goMineCar(0)">
                            <img src="../../static/icon/yydcc.png" alt=""
                                style="width: 70rpx;height: 70rpx;margin-right: 20rpx;">
                            <view>
                                <view
                                    style="font-family: Source Han Sans CN;font-weight: bold;font-size: 26rpx;color: #333333;">
                                    预约待出车</view>
                                <view
                                    style="font-family: Source Han Sans CN;font-weight: 400;font-size: 22rpx;color: #999999;">
                                    预约待出车次数{{ borrowCount }}</view>
                            </view>
                        </view>
                        <view style="display: flex;align-items: center;margin-top: 20rpx;" class="top2"
                            @click="goMineCar(1)">
                            <img src="../../static/icon/ycdgh.png" alt=""
                                style="width: 70rpx;height: 70rpx;margin-right: 20rpx;">
                            <view>
                                <view
                                    style="font-family: Source Han Sans CN;font-weight: bold;font-size: 26rpx;color: #333333;">
                                    用车待归还</view>
                                <view
                                    style="font-family: Source Han Sans CN;font-weight: 400;font-size: 22rpx;color: #999999;">
                                    用车待归还数量{{ returnCount }}</view>
                            </view>
                        </view>
                    </view>
                    <img src="../../static/icon/wdyc.png" alt=""
                        style="width: 345rpx;height: 240rpx;margin-left: 20rpx;" @click="goMineCar">
                </view>
                <view class="top-tt">
                    车辆列表
                    <span style="position: absolute;right: 20rpx;top: -16rpx;">
                        <wd-button type="text" @click="showTips">操作说明</wd-button>
                    </span>
                </view>
                <wd-popup v-model="show" lockScroll>
                    <view>
                        <wd-swiper :list="swiperList" :autoplay="false" :indicator="{ showControls: true }" :current="0"
                            height="600px" :loop="false" @click="handleClick" @change="onChange"></wd-swiper>
                    </view>
                </wd-popup>
                <view class="top-tt2">温馨提示：开始用车前请提前预约，用车完成及时归还。</view>
            </view>
            <view v-for="( item, index ) in  carList " :key="index" class="main">
                <view class="box">
                    <view class="box-top">
                        <img src="../../static/icon/car.png" alt=""
                            style="width: 30rpx;height: 30rpx;margin-right: 20rpx;">
                        <view
                            style="font-family: Source Han Sans CN;font-weight: bold;font-size: 30rpx;color: #333333;">
                            {{ item.vehicleName }}
                        </view>
                        <view class="box-top-status">
                            <view v-if="item.vehicleUsageStatus === '0'" class="status0" />
                            <view v-if="item.vehicleUsageStatus === '1'" class="status1" />
                            <view v-if="item.vehicleUsageStatus === '0'">闲置中</view>
                            <view v-if="item.vehicleUsageStatus === '1'">使用中</view>
                        </view>
                    </view>
                    <view v-if="item.vehicleUsageStatus === '1'" class="box-mid">
                        <view>
                            <span>使用人：{{ item.userName }}</span>
                            <span style="margin-left: 50rpx;">地址：{{ item.destination }}</span>
                        </view>
                        <view style="margin: 20rpx 0;">预计时间：{{ item.startTime }}——{{ item.endTime }}</view>
                        <view>出车时间：{{ item.realStartTime }}</view>
                    </view>
                    <view v-else class="box-mid">
                        <view>车辆停放位置：{{ item.parkingPlace }}</view>
                    </view>
                    <view class="box-bottom">
                        <wd-button plain hairline type="info" style="margin-right: 20rpx;" size="small"
                            @click="CarRecordsFn(item)">用车记录</wd-button>
                        <wd-button v-if="item.vehicle.vehicleStatus === '0'" plain hairline size="small"
                            @click="bookingCarFn(item)">预约用车</wd-button>
                    </view>
                </view>
            </view>
            <wd-loadmore :state="state" error-text="点击加载更多"
                :finished-text="carList && carList.length > 0 ? '已全部加载完毕' : '暂无数据'" @reload="loadmore" />
        </view>

    </view>
</template>

<style scoped lang="scss">
.content {
    height: 100vh;
    background: #F6F6F6;

    .father {
        background: #F6F6F6;

    }

    .top {
        :deep(.wd-center-enter-active.data-v-82a7a455, .wd-center-leave-active.data-v-82a7a455) {
            // height: 80%;
            width: 85%;
            border-radius: 20rpx;
        }


        overflow: hidden;
        // width: 100%;
        // height: 465rpx;
        // padding: 0 20rpx;
        background: linear-gradient(0deg, rgba(214, 229, 255, 0) 0%, rgba(126, 164, 253, 0.88) 88%);

        .top-tt {
            position: relative;
            font-family: Source Han Sans CN;
            font-weight: bold;
            font-size: 30rpx;
            color: #333333;
            margin: 0 20rpx;
        }

        .top-tt2 {
            font-family: Source Han Sans CN;
            font-weight: 400;
            font-size: 24rpx;
            color: #F8941F;
            margin: 10rpx 20rpx;
        }

        .top2 {
            padding: 20rpx;
            background: #FFFFFF;
            border-radius: 8rpx;
        }
    }

    .main {
        background: #F6F6F6;
        padding: 10rpx 20rpx;

        .box {
            // height: 323rpx;
            background: #FFFFFF;
            border-radius: 8rpx;
            padding: 20rpx;

            .box-top {
                position: relative;
                padding: 0 20rpx 20rpx 0;
                display: flex;
                align-items: center;

                .box-top-status {
                    font-family: Source Han Sans CN;
                    font-weight: 400;
                    font-size: 26rpx;
                    color: #666666;
                    position: absolute;
                    display: flex;
                    align-items: center;
                    right: 20rpx;
                    top: 0;

                    .status0 {
                        width: 16rpx;
                        height: 16rpx;
                        background: #7EA4FD;
                        border-radius: 50%;
                        margin-right: 20rpx;
                    }

                    .status1 {
                        width: 16rpx;
                        height: 16rpx;
                        background: #FF3F3F;
                        border-radius: 50%;
                        margin-right: 20rpx;
                    }
                }
            }

            .box-mid {
                background: #F7F9FF;
                border-radius: 8rpx;
                font-family: Source Han Sans CN;
                font-weight: 400;
                font-size: 26rpx;
                color: #666666;
                padding: 20rpx;
            }

            .box-bottom {
                margin-top: 20rpx;
                text-align: right;

                :deep(.wd-button.is-plain.is-info.data-v-aa3a6253) {
                    margin-right: 20rpx;
                }
            }


        }
    }
}
</style>