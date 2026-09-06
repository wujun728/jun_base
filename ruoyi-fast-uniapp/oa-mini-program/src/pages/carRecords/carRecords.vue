<script lang="ts" setup>
import { selectAllUser } from "@/service/api/user";
import { selectVehicleRecordList } from '@/service/api/carManage'
import { dayjs } from "wot-design-uni";

onLoad(() => {
    getUserList()
    getParams()
});


const multiple = ref(false)
const selectParent = ref(false)
const checkStrictly = ref(false)
const nextTreeRefCell = ref()
const treeData = ref<any>([])
const userId = ref<any>([])
const userName = ref<any>()
const dropMenu = ref()
const option = ref<any>([
    { label: '待出车', value: 0 },
    { label: '待还车', value: 1 },
    { label: '已还车', value: 2 },
])
const promotion = ref<any>()


// 列表-------------------------
const state = ref<any>('')
const params = ref<any>({
    pageNum: 1,
    pageSize: 10,
    pages: 0,
    id: '',
    recordStatus: '',
    date: '',
    userId: ''
})
function getParams() {
    const instance:any = getCurrentInstance()?.proxy
    const eventChannel = instance!.getOpenerEventChannel();
    eventChannel.on('data', (res) => {
        params.value.id = res.vehicle.id
        // uni.setNavigationBarTitle({
        //     title: res.reportTemplateVO?.templateName
        // })
        getCarList()
    })
}
const carList = ref<any>([])
// 查询车辆列表
async function getCarList() {

    const data = await selectVehicleRecordList({
        ...params.value,
        date: params.value.date ? dayjs(params.value.date).format('YYYY-MM-DD') : '',
        userId: userId.value && userId.value.length > 0 ? userId.value.join() : ''
    })
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
// 列表----------------------------------------

function reset() {
    carList.value = []
    params.value = {
        pageNum: 1,
        pageSize: 10,
        pages: 0,
        vehicleId: '',
        recordStatus: '',
        date: ''
    }
    userName.value = ''
    userId.value = ''
    getCarList()
}
// 确定
function confirm() {
    carList.value = []
    params.value.pageNum = 1
    getCarList()
    dropMenu.value.close()
}
// 重置
function clear() {
    promotion.value = null
    reset()
    // dropMenu.value.close()
}
// 筛选车辆状态
function handleConfirm({ value }) {
    params.value.recordStatus = value
}

function goDetail(val) {
    uni.navigateTo({
        url: '/pages/carRecordsDetail/carRecordsDetail',
        success(res) {
            res.eventChannel.emit('data', val)
        },
    })
}

// 查询所有人员
async function getUserList() {
    treeData.value = await new Promise((resolve) => {
        selectAllUser({}).then((res) => {
            const fn = (list) => {
                if (list && list.length > 0) {
                    list.forEach((item) => {
                        item.label = item.name
                        item.id = item.userId
                        if (item.children && item.children.length > 0) {
                            fn(item.children)
                        }
                    })
                }
            }
            fn(res)
            resolve(res)
        })
    })
}
// 选择人员
function itemCellclick(_multiple, _selectParent, _checkStrictly = false) {
    echoDefaultCell()
    multiple.value = _multiple
    selectParent.value = _selectParent
    checkStrictly.value = _checkStrictly
    unref(nextTreeRefCell).showTree = true
}
function echoDefaultCell() {
    const selectIds = userId.value
    checkedTreeData(treeData.value, selectIds)
    multiple.value = false
    nextTreeRefCell.value.showTree = true
}
function onconfirmCell(list) {
    if (list && list.length > 0) {
        userName.value = list[0].label
        userId.value = []
        userId.value.push(list[0].id)
    } else {
        userName.value = ''
        userId.value = []
    }
}
function checkedTreeData(treeData, selectIds) {
    // eslint-disable-next-line array-callback-return
    treeData.map(item => {
        if (selectIds.indexOf(item.id) !== -1) {
            item.checked = true
        } else {
            item.checked = false
        }
        if (item.children && item.children.length) {
            checkedTreeData(item.children, selectIds)
        }
    })
}
function oncancel() {
    // 清除treeData的选中状态
    checkedTreeData(unref(treeData), [])
}
function getColor(val) {
    if (val === '0') {
        return 'status0'
    } else if (val === '1') {
        return 'status1'
    } else {
        return 'status2'
    }
}
</script>

<template>
    <view class="content">
        <view style="background: #F6F6F6;">
            <u-sticky bgColor="#fff">
                <wd-drop-menu>
                    <wd-drop-menu-item ref="dropMenu" title="筛选">
                        <view>
                            <wd-picker v-model="promotion" label="用车状态" placeholder="请选择用车状态" label-width="100px"
                                :columns="option" align-right @confirm="handleConfirm" />
                            <wd-datetime-picker v-model="params.date" type="date" label="预约日期" align-right />
                            <wd-cell title="使用人员" is-link :value="userName"
                                @click="itemCellclick(false, true)"></wd-cell>
                            <next-tree ref="nextTreeRefCell" :checkStrictly="checkStrictly" :selectParent="selectParent"
                                :multiple="multiple" :treeData="treeData" ifSearch searchModel="depHighlight"
                                @cancel="oncancel" @confirm="onconfirmCell"></next-tree>
                            <view class="footer">
                                <wd-button block type="info" @click="clear">重置</wd-button>
                                <wd-button block @click="confirm">确定</wd-button>
                            </view>
                        </view>
                    </wd-drop-menu-item>
                </wd-drop-menu>
            </u-sticky>

            <view v-for="(item, index) in carList" :key="index" class="main" @click="goDetail(item)">
                <view class="box">
                    <view class="box-top">
                        <view
                            style="font-family: Source Han Sans CN;font-weight: bold;font-size: 30rpx;color: #333333;">
                            {{ item.userName }}的用车记录
                        </view>
                        <view class="box-top-status">
                            <view :class="getColor(item.recordStatus)"></view>
                            <view>{{ item.recordStatus === '0' ? '待出车' : item.recordStatus === '1' ? '待还车' : '已还车' }}
                            </view>
                        </view>
                    </view>
                    <view class="box-mid">
                        <view>预计时间：{{ item.startTime }}——{{ item.endTime }}</view>
                        <view style="margin-top: 20rpx;">实际时间：{{ item.realStartTime }}——{{ item.realEndTime }}</view>
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
    :deep(.u-sticky.data-v-84417a11){
        margin-bottom: 10rpx;
    }
    .footer {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 20rpx;

        :deep(.wd-button.is-medium.is-primary.data-v-aa3a6253) {
            background: #7EA4FD;
            border-radius: 8rpx;
            font-family: Source Han Sans CN;
            font-weight: 500;
            font-size: 30rpx;
            color: #FFFFFF;
        }

        :deep(.wd-button.is-medium.is-info.data-v-aa3a6253) {
            background: #FFFFFF;
            border: 1px solid #DEDEDE;
            border-radius: 8rpx;
            font-family: Source Han Sans CN;
            font-weight: 500;
            font-size: 30rpx;
            color: #999999;
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

                    .status2 {
                        width: 16rpx;
                        height: 16rpx;
                        background: #32C399;
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


        }
    }

}
</style>