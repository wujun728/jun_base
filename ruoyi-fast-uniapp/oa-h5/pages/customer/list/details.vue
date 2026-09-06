<template>
  <view class="content">
    <view class="cr-position-header">
      <view id="customer-header-body">
        <view class="status_bar"></view>

        <default-nav-bar :class="data.scrollTop>=100?'p60':'top'" jump-url="/pages/customer/list/index"
          :is-jump-bar="false" :is-right="true" :right-data="data.rightIcon" @handleNarItem="handleNarItem">
        </default-nav-bar>

        <view class="details-content plr10">
          <view class="company-name line1 mb12">{{data.clientInfo.customer_name }}</view>
          <view class="label-content mb12">
            <view class="label-text">
              <uni-tag class="lext" :text="item.name" type="warning" size="mini"
                v-for="(item,index) in data.clientInfo.customer_label" :key="index" />
            </view>
          </view>
          <view class="info-content display-align">
            <view class="info-user">业务员：{{data.clientInfo.salesman?data.clientInfo.salesman.name:'--'}}
            </view>
            <view class="info-time">跟进时间：
              <uni-dateformat v-if="data.clientInfo.last_follow_time" format="yyyy/MM/dd hh:mm"
                :date="data.clientInfo.last_follow_time">
              </uni-dateformat>
              <text v-else>--</text>
            </view>
          </view>
          <view class=" icon-content">
            <view class="icon-content-item" v-for="item in data.infoData" :key="item.id" @click="detailsItem(item)">
              <text class="iconfont" :class="item.icon"></text>
              <text class="name">{{item.name}}</text>
            </view>
          </view>

          <image v-if="data.clientInfo.customer_status == 0" class="item-list-status"
            src="/static/image/customer-notyet1.png" mode=""></image>
          <image v-if="data.clientInfo.customer_status == 1" class="item-list-status"
            src="/static/image/customer-closed.png" mode=""></image>
          <image v-if="data.clientInfo.customer_status == 2" class="item-list-status"
            src="/static/image/customer-lost.png" mode=""></image>
        </view>
      </view>

      <!-- 导航栏 -->
      <view class="customer-nav">
        <view class="customer-content">
          <view class="customer-tab">
            <uni-row class="plr10 display-align">
              <uni-col :span="21" class="display-align">
                <view class="customer-tab-item" v-for="(item,index) in data.tabData"
                  :class="index === data.currentIndex ? 'active' : ''" :key="item.id" @click="changeTab(item, index)">
                  {{item.name}}
                </view>
              </uni-col>

              <uni-col :span="3" class="customer-right" v-if="data.types!=3">
                <dean-popover v-if="data.currentId === 2" sssss ref="deanPopoverRef" model-direction="right">
                  <template #icon>
                    <text class="iconfont icon-add icon-a-gengduo2"></text>
                  </template>
                  <view class="modal-item" @click="addRecord(1)"><text class="iconfont icon-kehu-tianjiajilu"></text>加记录
                  </view>
                  <view class="modal-item" @click="addRecord(2)"><text
                      class="iconfont icon-kehu-tianjiatixing"></text>加提醒
                  </view>
                </dean-popover>
                <dean-popover v-if="data.currentId === 3" ref="deanPopoverRefLeft" model-direction="right">
                  <template #icon>
                    <text class="iconfont icon-add icon-a-gengduo2"></text>
                  </template>
                  <view class="modal-item" @click="addRecord(3)"><text
                      class="iconfont icon-kehu-tianjiahuikuan"></text>加回款</view>
                  <view class="modal-item" @click="addRecord(5)"><text
                      class="iconfont icon-kehu-tianjiaxufei"></text>加续费</view>
                  <view class="modal-item" @click="addRecord(4)"><text
                      class="iconfont icon-kehu-tianjiazhichu"></text>加支出
                  </view>
                </dean-popover>
              </uni-col>
            </uni-row>
          </view>
        </view>
      </view>
    </view>

    <view class="customer">
      <view class="customer-info plr10">
        <!-- 基本信息 -->
        <customer-info v-if="data.currentId === 1" :customerData="data.clientInfo.list"></customer-info>
        <!-- 跟进记录 -->
        <follow-record v-if="data.currentId===2" :followList="data.followList" @getfollowList='getfollowList'>
        </follow-record>
        <!-- 付款记录 -->
        <payment-record v-if="data.currentId === 3" :listData="data.paymentList"></payment-record>
      </view>
    </view>
    <customer-more ref="customerMoreRef" :types="data.types " :dataInfo="data.clientInfo" @change="change" />
    <globalIndex />
    <uni-popup ref="popupRef" background-color="#fff" type="bottom" v-if="data.currentPickerData.length">
      <view class="uni-picker-header">
        <view class="uni-picker-action uni-picker-action-cancel" @click="cancelPopup()">取消</view>
        <view class="uni-picker-action uni-picker-action-confirm" @click="confirmPopup(data.currentType)">完成
        </view>
      </view>
      <picker-view :indicator-style="data.indicatorStyle" :value="data.newValue" class="picker-view"
        @change="changePicker($event, data.currentType)">
        <picker-view-column>
          <view class="item-value" v-for="(item, index) in data.currentPickerData" :key="index">
            {{ item.name }}
          </view>
        </picker-view-column>
      </picker-view>
    </uni-popup>
    <!-- 备注弹窗 -->
    <textarea-popup ref="textareaPopupRef" :config-data="data.configData" @change="changePop" />
  </view>
</template>

<script setup>
  import defaultNavBar from '@/components/defaultNavBar/index'
  import globalIndex from '@/components/globalIndex/index.vue'
  import deanPopover from '@/components/deanPopover/index.vue'
  import customerInfo from './components/customerInfo.vue'
  import customerMore from './components/customerMore.vue'
  import paymentRecord from './components/paymentRecord.vue'
  import followRecord from './components/followRecord.vue'
  import {
    reactive,
    ref,
    getCurrentInstance,
    onMounted,
    nextTick
  } from 'vue'
  import {
    onLoad,
    onShow,
    onPageScroll
  } from '@dcloudio/uni-app'
  import message from '@/utils/message'
  import {
    clientInfoApi,
    followListApi,
    billListApi,
    configApproveApi
  } from '@/api/customer'
  const data = reactive({
    rightIcon: [{
      type: 1,
      icon: 'icon-gengduo1',
      types: 'icon'
    }],
    configData: {
      title: '资料重命名',
      placeholder: '请填写资料名称',
    },
    id: 0, //客户id
    currentIndex: 0,
    scrollTop: 0,
    statusBarHeight: 20,
    currentId: 1,
    clientInfo: {}, // 客户信息
    followWhewe: {
      page: 1,
      limit: 10,
      eid: ''
    },
    types: 1,
    followList: [], // 跟进记录
    payWhere: {
      page: 1,
      limit: 10,
      types: '',
      eid: ''
    },
    paymentList: [], // 付款记录
    tabData: [{
        name: '基本信息',
        id: 1
      },
      {
        name: '跟进记录',
        id: 2
      },
      {
        name: '账目记录',
        id: 3
      }
    ],
    infoData: [{
        id: 1,
        name: '联系人',
        icon: 'icon-kehuxiangqing-lianxiren',
        url: '/pages/customer/list/liaison'
      },
      {
        id: 2,
        name: '合同',
        icon: 'icon-kehuxiangqing-hetong',
        url: '/pages/customer/list/contract'
      },
      {
        id: 3,
        name: '发票',
        icon: 'icon-kehuxiangqing-fapiao',
        url: '/pages/customer/list/invoice'
      },
      {
        id: 4,
        name: '资料',
        icon: 'icon-kehuxiangqing-fujian',
        url: '/pages/customer/list/file'
      },
    ],
    buildData: {},
    currentPickerData: [],
    currentType: 0,
    current: 0,
    current1: 0,
    current2: 0,
    parameterData: {
      contract_id: '',
      customer_id: '',
      invoice_id: '',
      bill_id: ''
    },
  })


  const textareaPopupRef = ref(null)
  const topHeight = ref(0)
  const popupRef = ref()

  onShow(() => {
    getbillList(true)
    let statusBarObj = getPhoneInfo()
    data.statusBarHeight = statusBarObj.statusBarHeight

    // #ifdef APP-PLUS
    topHeight.value = data.statusBarHeight + 'px'
    // #endif 
  })
  onPageScroll(e => {
    data.scrollTop = e.scrollTop

  })

  onLoad(e => {
    data.id = e.id
    getClientInfo(data.id)
    if (e.types) {
      data.types = e.types
    }
    if (e.type == 2) {
      data.currentId = Number(e.type)
      data.currentIndex = 1
    }
    if (e.type == 3) {
      data.currentId = Number(e.type)
      data.currentIndex = 2
    }

    tabLoadingList()
  })

  const change = () => {
    getClientInfo(data.id)
  }

  const cHeight = ref(0)
  const instance = getCurrentInstance()
  const getCustomerHeight = () => {
    let query = uni.createSelectorQuery().in(instance)
    query.select('#customer-header-body').fields({
      size: true,
      rect: true
    })
    query.exec(e => {
      cHeight.value = -Math.ceil(e[0].height) + data.statusBarHeight + 20 + 'px'
    })
  }

  const customerMoreRef = ref(null)
  const handleNarItem = e => {
    customerMoreRef.value.popupOpen(data.currentId)
  }

  const changeTab = (item, index) => {
    data.currentIndex = index
    data.currentId = item.id
    tabLoadingList()
    if (item.id == 3) {
      // 获取付款是否走审批
      getConfigApprove()
    }
  }
  const getConfigApprove = () => {
    configApproveApi().then(res => {
      data.buildData = res.data
    })
  }
  const cancelPopup = () => {
    popupRef.value.close()
  }


  const tabLoadingList = () => {
    if (data.currentId === 2) {
      // 跟进记录
      data.followWhewe.page = 1
      getfollowList(true)
    } else if (data.currentId === 3) {
      // 付款记录
      data.payWhere.page = 1
      getbillList(true)
    }
  }
  // 获取客户信息
  const getClientInfo = id => {
    clientInfoApi(id).then(res => {
      data.clientInfo = res.data
      nextTick(() => {
        getCustomerHeight()
      })
    }).catch(error => {
      message.error(error.message)
    })
  }

  const listLoading = ref(false) //是否正在加载
  // 获取跟进列表
  const getfollowList = (tab = false) => {
    data.followWhewe.eid = data.id
    followListApi(data.followWhewe).then(res => {
      if (tab) data.followList = []
      data.followList.push(...res.data.list)
      const allPage = Math.ceil(res.data.count / data.followWhewe.limit)
      listLoading.value = data.followList.length <= 0 || data.followWhewe.page >= allPage ? false : true
    }).catch(error => {
      message.error(error.message)
    })
  }

  // 获取付款记录列表
  const getbillList = (tab = false) => {
    data.payWhere.eid = data.id
    billListApi(data.payWhere).then(res => {
      if (tab) data.paymentList = []
      data.paymentList.push(...res.data.list)
      const allPage = Math.ceil(res.data.count / data.payWhere.limit)
      listLoading.value = data.paymentList.length <= 0 || data.payWhere.page >= allPage ? false : true
    }).catch(error => {
      message.error(error.message)
    })
  }

  import {
    clickNavigateTo,
    getPhoneInfo
  } from '@/utils/helper'
  // 查看详情
  const detailsItem = item => {
    let clientInfo = {
      name: data.clientInfo.name,
      eid: data.id
    }
    uni.setStorageSync('clientInfo', JSON.stringify(clientInfo))
    if (!item.url) return false
    if (item.id === 2 || item === 3) {
      clickNavigateTo(`${item.url}?eid=${data.id}&name=${data.clientInfo.customer_name}&types=${data.types}`)
    } else {
      clickNavigateTo(`${item.url}?eid=${data.id}&name=${data.clientInfo.customer_name}&types=${data.types}`)
    }
  }

  // 添加付款和支出记录
  const deanPopoverRef = ref(null)
  const addRecord = type => {
    if (type <= 2) {
      clickNavigateTo(`/pages/customer/list/addFollow?eid=${data.id}&type=${type}`)
    } else if (type == 4) {
      if (data.buildData.contract_disburse_switch) {
        clickNavigateTo(
          `/pages/users/examine/default?id=${data.buildData.contract_disburse_switch}&eid=${data.id}&types=customer`
        )
      } else {
        clickNavigateTo(`/pages/customer/list/addSpend?eid=${data.id}`)
      }

    } else if (type == 3) {
      // 添加回款 type：3
      clickNavigateTo(
        `/pages/users/examine/default?id=${data.buildData.contract_refund_switch}&eid=${data.id}&types=customer`
      )
      // 添加续费
    } else if (type == 5) {
      clickNavigateTo(
        `/pages/users/examine/default?id=${data.buildData.contract_renew_switch}&eid=${data.id}&types=customer`
      )
    }
    // deanPopoverRef.value.close()
  }


  import {
    onReachBottom
  } from '@dcloudio/uni-app'
  // 下拉加载
  onReachBottom(() => {
    if (data.currentId === 2) {
      if (listLoading.value) {
        data.followList.page++
        getfollowList()
      }
    } else if (data.currentId === 3) {
      if (listLoading.value) {
        data.payWhere.page++
        getbillList()
      }
    }
  })
</script>

<style>
  page {
    background-color: #fff;
  }
</style>

<style scoped lang="scss">
  .content {
    width: 100%;

    .label-text {
      width: 100%;
      overflow: hidden;
      text-overflow: ellipsis;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
    }

    .cr-position-header {
      position: sticky;
      /* #ifdef APP-PLUS */
      top: calc(v-bind(cHeight) - v-bind(topHeight));
      /* #endif */
      /* #ifndef APP-PLUS */
      top: calc(v-bind(cHeight) - 20rpx);
      /* #endif */

      /* #ifdef APP-PLUS */
      .p60 {
        padding-top: calc(v-bind(topHeight) + 60rpx);
        width: 100%;
        background-color: #fff;
        position: sticky;
        top: 0;
        z-index: 999;
      }

      /* #endif */
      /* #ifndef APP-PLUS */
      .p60 {
        padding-top: 60rpx !important;
        width: 100%;
        background-color: #fff;
        position: sticky;
        top: 0;
        z-index: 999;
      }

      /* #endif */
      .top {
        width: 100%;
        background-color: #fff;
        position: sticky;
        top: 0;
        z-index: 999;
      }
    }

    .details-content {
      position: relative;
      padding-top: 56rpx;

      .item-list-status {
        position: absolute;
        top: 20rpx;
        right: 0;
        width: 160rpx;
        height: 188rpx;
      }

      .mb12 {
        margin-bottom: 24rpx;
      }

      .company-name {
        font-size: 32rpx;
        color: $uni-text-color;
        font-weight: $uni-default-font-weight;
      }

      .label-content {
        ::v-deep .uni-tag {
          display: inline-block;
          background-color: rgba(255, 153, 0, 0.1);
          border: rgba(255, 153, 0, 0.1);
          color: $uni-color-two;
          padding: 4rpx 8rpx;
          font-size: 24rpx;
          margin-top: 16rpx;
          margin-right: 16rpx;


          &:last-of-type {
            margin-right: 0;
          }
        }
      }

      .info-content {
        font-size: 26rpx;
        color: $nui-text-color-four;

        .info-user {
          padding-right: 30rpx;
          position: relative;

          &::after {
            position: absolute;
            right: 0;
            top: 0;
            content: '';
            width: 1px;
            height: 100%;
            background-color: $uni-line-style-color-three;

          }
        }

        .info-time {
          padding-left: 30rpx;
        }
      }

      .icon-content {
        padding: 48rpx 0;
        width: 100%;
        display: flex;
        align-items: center;

        .icon-content-item {
          width: 25%;
          display: flex;
          align-items: center;
          justify-content: center;
          flex-wrap: wrap;

          .image {
            width: 64rpx;
            height: 64rpx;
          }

          .iconfont {
            font-size: 64rpx;
            color: $uni-color-primary;
          }

          .name {
            margin-top: 10rpx;
            width: 100%;
            text-align: center;
            font-size: 26rpx;
            color: $uni-text-color;
          }
        }
      }

    }

    .customer-nav {
      width: 100%;
      padding-top: 20rpx;
      background-color: $uni-default-bg;

      .customer-content {
        background-color: #fff;
        width: 100%;

        .customer-tab {
          height: 40px;
          border-bottom: 1px solid $uni-line-style-color-three;

          ::v-deep .uni-row {
            height: 100%;

            .uni-col {
              height: 100%;
            }
          }

          .customer-tab-item {
            font-size: 28rpx;
            color: $nui-text-color-four;
            margin-right: 48rpx;
            cursor: pointer;
            height: 100%;
            line-height: 40px;

            &.active {
              color: $uni-text-color;
              border-bottom: 2px solid $uni-color-primary;
            }

            &:last-of-type {
              margin-right: 0;
            }
          }

          .customer-right {
            display: flex;
            align-items: center;
            justify-content: flex-end;

            .icon-add {
              font-size: 42rpx;
              color: $uni-text-color;
            }
          }
        }
      }

    }

    .customer {
      width: 100%;

      .customer-info {}
    }
  }

  .popovers-container {
    display: flex;
    justify-content: space-around;
    /* Or 'flex-start' to have them at the beginning */
  }

  .modal-items {
    z-index: 99;
    color: #333333;
    text-align: left;
    font-size: 28rpx;
    display: flex;
    align-items: center;
    font-weight: normal;
    margin-bottom: 44rpx !important;

    &:last-of-type {
      margin-bottom: 0;
    }

    .iconfont {
      font-size: 30rpx;
      color: $nui-text-color-four;
      margin-right: 12rpx;
    }
  }

  .no-bottom-margin {
    margin-bottom: 0 !important;
  }

  .uni-picker-header {
    border-bottom: 1px solid #e5e5e5;
    width: 100%;
    height: 90rpx;
    background-color: #fff;
    display: flex;
    align-items: center;
    justify-content: space-between;

    .uni-picker-action {
      max-width: 50%;
      top: 0;
      height: 100%;
      box-sizing: border-box;
      padding: 0 14px;
      font-size: 30rpx;
      line-height: 90rpx;
      cursor: pointer;

      &.uni-picker-action-cancel {
        color: #888;
      }

      &.uni-picker-action-confirm {
        color: #007aff;
      }
    }
  }


  .picker-view {
    width: 750rpx;
    height: 480rpx;
    background-color: #fff;
  }

  .item-value {
    display: flex;
    justify-content: center;
    align-items: center;
  }
</style>