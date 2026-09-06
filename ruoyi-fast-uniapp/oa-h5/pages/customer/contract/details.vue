<template>
  <view class="content">
    <view class="cr-position-header">
      <view id="customer-header-body">
        <view class="status_bar"></view>

        <default-nav-bar :class="data.scrollTop>100?'p60':'top'" :jump-url="data.jumpUrl" :is-jump-bar="false"
          :is-right="true" :right-data="rightIcon" @handleNarItem="handleNarItem">
        </default-nav-bar>

        <view class="details-content plr10">
          <view class="company-name line1 mb12">{{data.detail.contract_name}}</view>
          <view class="info-content display-align mb12">
            客户名称：{{data.detail.contract_customer ? data.detail.contract_customer.customer_name : '--' }}
          </view>
          <view class="info-content display-align">
            <view>
              起止时间：
              <uni-dateformat format="yyyy/MM/dd" :date="data.detail.start_date"></uni-dateformat>
              ～
              <template v-if="data.detail.end_date !== '0000-00-00'">
                <uni-dateformat format="yyyy/MM/dd" :date="data.detail.end_date"></uni-dateformat>
              </template>
              <template v-else>永久</template>
            </view>
          </view>

          <view class="icon-content">
            <view class="icon-content-item" v-for="(item, index) in infoData" :key="item.id"
              @click="detailsItem(index,item)">
              <text class="iconfont" :class="item.icon"></text>
              <text class="name">{{item.name}}</text>
            </view>
            <view class="icon-content-item" @click="detailsItem(3)">
              <text class="iconfont icon-hetongxiangqing-jianfapiao"></text>
              <text class="name">建发票 </text>
            </view>
            <view class="icon-content-item" @click="detailsItem(4)">
              <text class="iconfont icon-hetongxiangqing-tianfujian"></text>
              <text class="name">填记录</text>
            </view>
          </view>
          <!--   <image v-if="data.detail.is_abnormal == 1 " class="item-list-status" src="/static/image/normal01.png"
            mode="" /> -->
          <image class=" item-list-status" :src="getAbnormalText(data.detail)" mode="">
          </image>
        </view>
      </view>

      <!-- 导航栏 -->
      <view class="customer-nav">
        <view class="customer-content">
          <view class="customer-tab">
            <uni-row class="plr10 display-align">
              <uni-col :span="21" class="display-align">
                <view class="customer-tab-item" v-for="(item,index) in tabData"
                  :class="index === data.currentIndex ? 'active' : ''" :key="item.id" @click="changeTab(index)">
                  {{item.name}}
                </view>
                <!-- <text class="iconfont icon-xuanfuanniu-jia" v-if="data.scrollTop>=300" @click="scrollTo()" /> -->
              </uni-col>
              <uni-col :span="3" class="customer-right">
                <dean-popover sssss ref="deanPopoverRef" model-direction="right">
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
      <view class="plr10">
        <!-- 基本信息 -->

        <contract-info v-if="data.currentId === 1" :contract-data="data.detail.list"></contract-info>
        <!-- 付款提醒 -->
        <pay-remind v-if="data.currentId === 2" :list-data="data.remindList" :buildData="data.buildData"></pay-remind>
        <!-- 付款记录 -->
        <view v-if="data.currentId === 3">
          <view class="record-header">
            <uni-row>
              <uni-col :span="8" class="left">
                <view class="record-name">已付金额(元)</view>
                <view class="record-number">{{data.paymentPrice}}</view>
              </uni-col>
              <uni-col :span="8" class="right">
                <view class="record-name">未付金额(元)</view>
                <view class="record-number" style="color: #E93323;">{{data.auditPrice}}</view>
              </uni-col>
              <uni-col :span="8" class="right">
                <view class="record-name">支出金额(元)</view>
                <view class="record-number">{{data.expensePrice}}</view>
              </uni-col>
            </uni-row>
          </view>
          <payment-record v-if="data.currentId === 3" :list-data="data.paymentList" :cid="data.id"></payment-record>
        </view>
        <!-- 发票 -->
        <invoice-list v-if="data.currentId === 4" :list-data="data.invoiceList" :type="1" :eid="data.detail.eid"
          :cid="data.id" :name="data.detail.client?data.detail.client.name:''" empty-title="暂无申请发票，快去申请吧～">
        </invoice-list>
        <!-- 附件 -->
        <file-record v-if="data.currentId === 5" direction="column" :active="0" :list-data="data.fileList"
          empty-title="暂无上传资料～"></file-record>
      </view>
    </view>
    <drop-down ref="dropDownRef" :list-data="forumMeus" @btn-click="dropDownItem"></drop-down>
    <!-- <upload-file-image ref="uploadFileImageRef" title="上传附件"></upload-file-image> -->
    <globalIndex />
  </view>
</template>

<script setup>
  import deanPopover from '@/components/deanPopover/index.vue'
  import defaultNavBar from '@/components/defaultNavBar/index'
  import globalIndex from '@/components/globalIndex/index.vue'
  import contractInfo from './components/contractInfo.vue'
  import payRemind from './components/payRemind.vue'
  import paymentRecord from '@/pages/customer/list/components/paymentRecord.vue'
  import fileRecord from './components/fileRecord.vue'
  import invoiceList from '@/pages/customer/invoice/components/invoiceList.vue'
  import dropDown from '@/pages/forum/components/dropDown.vue'
  import { reactive, ref, getCurrentInstance, onMounted, nextTick } from 'vue'
  import message from '@/utils/message'
  import { useStore } from 'vuex'
  import moment from 'moment'
  import { configApproveApi } from '@/api/customer'
  const store = useStore()
  const data = reactive({
    id: 0, //合同id
    eid: 0, // 客户id
    currentIndex: 0,
    statusBarHeight: 20,
    currentId: 1,
    detail: {},
    billWhere: {
      page: 1,
      limit: 10,
      types: '',
      cid: ''
    },
    scrollTop: 0,
    paymentPrice: 0,
    auditPrice: 0,
    expensePrice: 0,
    paymentList: [],
    remindWhere: {
      page: 1,
      limit: 10,
      types: '',
      cid: ''
    },
    remindList: [],
    invoiceWhere: {
      page: 1,
      limit: 10,
      types: '',
      cid: ''
    },
    invoiceList: [],
    fileWhere: {
      page: 1,
      limit: 10,
      cid: ''
    },
    fileList: [],
    jumpUrl: '',

    buildData: {},
    currentType: 0,
    current: 0,
    current1: 0,
    current2: 0
  })

  const rightIcon = reactive([
    { type: 2, icon: 'icon-dingbu-jiantixing', types: 'icon' },
    { type: 1, icon: 'icon-gengduo1', types: 'icon' },
  ])

  const forumMeus = reactive([
    { name: '编辑', id: 1, icon: 'icon-gongzuohuibao-bianji' },
    { name: '转移', id: 2, icon: 'icon-danchuang-zhuanyi' },
    { name: '删除', id: 3, icon: 'icon-shanchu1' },
  ])

  import { onLoad, onShow, onPageScroll } from '@dcloudio/uni-app'
  const topHeight = ref(0)
  const popupRef = ref()
  onShow(() => {
    getDetails(data.id)
    getInvoiceList(true)
    getPrice()
    getBillList(true)
    data.jumpUrl = toJumpUrl()
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
    getDetails(data.id)
    getPrice()
    getConfigApprove()

    if (e.tab) {
      data.currentIndex = Number(e.tab)
      data.currentId = tabData[data.currentIndex].id
      tabLoadingList()
    }
    data.jumpUrl = toJumpUrl()
  })
  const getConfigApprove = () => {
    configApproveApi().then(res => {
      data.buildData = res.data
    })
  }


  const toJumpUrl = () => {
    // 判断跳转地址
    const formType = store.state.app.customerFormType
    let url = ''
    if (formType.type && formType.type === 'list') {
      url = `/pages/customer/list/contract?eid=${formType.eid}`
    } else {
      url = '/pages/customer/contract/index'
    }
    return url
  }

  const getAbnormalText = row => {
    let str = ''
    if (row.contract_status == 0) {
      str = '/static/image/normal03.png'
    } else if (row.contract_status == 1) {
      str = '/static/image/normal00.png'

    } else if (row.contract_status == 2) {
      str = '/static/image/normal02.png'

    } else if (row.contract_status == 3) {
      str = '/static/image/normal01.png'
    }
    return str
  }


  const cHeight = ref(0)
  const instance = getCurrentInstance()
  const getCustomerHeight = () => {
    nextTick(() => {
      let query = uni.createSelectorQuery().in(instance)
      query.select('#customer-header-body').fields({ size: true, rect: true })
      query.exec(e => {
        cHeight.value = -Math.ceil(e[0].height) + data.statusBarHeight + 22 + 'px'
      })
    })
  }

  const dropDownRef = ref(null)
  const handleNarItem = e => {
    if (e.type == 1) {
      dropDownRef.value.openDropdown()
    } else {
      clickNavigateTo(
        `/pages/customer/contract/addRemind?cid=${data.id}&eid=${data.eid}&name=${data.detail.contract_customer.customer_name}`
      )
    }
  }

  const dropDownItem = e => {
    if (e.id === 1) {
      clickNavigateTo(
        `/pages/customer/contract/addContract?cid=${data.id}&eid=${data.eid}&name=${data.detail.contract_customer.customer_name}`
      )
    }
    if (e.id === 2) {
      clickNavigateTo(`/pages/customer/list/shift?type=2&cid=${data.id}`)
    }
    if (e.id === 3) {
      let cid = data.id
      showModal('您确定要删除该合同吗').then(res => {
        contractDeleteApi(cid).then(res => {
          message.success(res.message)
          const url = toJumpUrl()
          delayedReLaunch(url)
        }).catch(error => {
          message.error(error.message)
        })
      }).catch(error => {
        console.log('取消了')
      })
    }
  }
  // tab切换
  const tabData = reactive([
    { name: '基本信息', id: 1 },
    { name: '账目记录', id: 3 },
    { name: '付款提醒', id: 2 },
    { name: '发票', id: 4 },
    { name: '记录', id: 5 },
  ])
  const changeTab = index => {
    if (index === 1) {
      getPrice()
    }
    data.currentIndex = index
    data.currentId = tabData[index].id
    tabLoadingList()
  }

  const tabLoadingList = () => {
    if (data.currentId === 2) {
      // 付款提醒
      data.remindWhere.page = 1
      getRemindList(true)
    } else if (data.currentId === 3) {
      // 付款记录
      data.billWhere.page = 1
      getBillList(true)
    } else if (data.currentId === 4) {
      // 发票
      data.invoiceWhere.page = 1
      getInvoiceList(true)
    } else if (data.currentId === 5) {
      // 发票
      data.fileWhere.page = 1
      getFlieList(true)
    }
  }

  // 菜单导航
  import { clickNavigateTo, delayedReLaunch, showModal, getPhoneInfo } from '@/utils/helper'
  import { uploadFlie } from '@/utils/file'
  const infoData = reactive([{
      id: 1,
      name: '建回款',
      icon: 'icon-hetongxiangqing-jianhuikuan',
      url: '/pages/customer/contract/addPayment'
    },
    { id: 2, name: '建续费', icon: 'icon-jianzhichu1', url: '/pages/customer/list/addSpend' },
  ])
  const uploadFileImageRef = ref(null)
  const getPrice = () => {
    clientcontractStatisticsApi(data.id).then(res => {
      data.auditPrice = res.data.unpaid_price
      data.paymentPrice = res.data.payment_price
      data.expensePrice = res.data.expense_price
    })
  }
  const scrollTo = () => {
    if (data.currentIndex == 1) {
      clickNavigateTo(`/pages/customer/contract/addPayment?cid=${data.id}&eid=${data.eid}`)
    }
    if (data.currentIndex == 2) {
      clickNavigateTo(`/pages/customer/contract/addRemind?cid=${data.id}&eid=${data.eid}`)
    }
    if (data.currentIndex == 3) {
      let dataInfo = { eid: data.detail.eid }
      unInvoicedListApi(dataInfo).then(res => {
        if (res.data.length > 0) {
          clickNavigateTo(
            `/pages/customer/invoice/checkPayment?eid=${data.eid}&name=${data.detail.client.name}&cid=${data.detail.id}`
          )
        } else {
          clickNavigateTo(
            `/pages/customer/invoice/addInvoice?eid=${data.eid}&name=${data.detail.client.name}&cid=${data.detail.id}`
          )
        }
      })
    }
    if (data.currentIndex == 4) {
      const datas = { cid: data.detail.id, eid: data.eid }
      uploadFlie('client/file/upload', datas).then(res => {
        if (res.status) {
          changeTab(4)
        }
      }).catch(error => {
        message.error(error.message)
      })
    }
  }
  // 查看详情
  const detailsItem = (index, row) => {
    if (index == 0) {
      addRecord(3)
    } else if (index == 1) {
      addRecord(5)
    } else if (index == 3) {
      // 建发票
      let dataInfo = { eid: data.eid }
      unInvoicedListApi(dataInfo).then(res => {
        if (res.data.length > 0) {
          clickNavigateTo(
            `/pages/customer/invoice/checkPayment?id=${data.buildData.invoicing_switch}&eid=${data.eid}&cid=${data.detail.id}`
          )
        } else {
          clickNavigateTo(
            `/pages/users/examine/default?id=${data.buildData.invoicing_switch}&eid=${data.eid}&types=contract`
          )
        }
      })
    } else if (index == 4) {
      // 填附件
      clickNavigateTo(`/pages/customer/contract/addFile?cid=${data.id}`)

    }
  }

  import {
    clientContractDetailApi,
    billListApi,
    contractRemindListApi,
    clientInvoiceApi,
    clientContractResourceListApi,
    contractDeleteApi,
    unInvoicedListApi,
    clientcontractStatisticsApi
  } from '@/api/customer'
  // 获取客户详情
  const getDetails = id => {
    clientContractDetailApi(id).then(res => {
      data.detail = res.data
      data.eid = res.data.contract_customer ? res.data.contract_customer.id : 0
      getCustomerHeight()
    }).catch(error => {
      message.error(error.message)
    })
  }
  const listLoading = ref(false) //是否正在加载
  // 获取付款记录列表
  const getBillList = (tab = false) => {
    data.billWhere.cid = data.id
    billListApi(data.billWhere).then(res => {
      if (tab) data.paymentList = []
      data.paymentList.push(...res.data.list)
      const allPage = Math.ceil(res.data.count / data.billWhere.limit)
      listLoading.value = data.paymentList.length <= 0 || data.billWhere.page >= allPage ? false : true
    }).catch(error => {
      message.error(error.message)
    })
  }

  // 获取付款提醒列表
  const getRemindList = (tab = false) => {
    data.remindWhere.cid = data.id
    contractRemindListApi(data.remindWhere).then(res => {
      if (tab) data.remindList = []
      data.remindList.push(...res.data.list)

      const allPage = Math.ceil(res.data.count / data.remindWhere.limit)
      listLoading.value = data.remindList.length <= 0 || data.remindWhere.page >= allPage ? false : true
    }).catch(error => {
      message.error(error.message)
    })
  }

  // 添加账目
  const addRecord = type => {
    let eid = data.detail.contract_customer ? data.detail.contract_customer.id : 0
    if (type == 4) {
      // 添加支出
      clickNavigateTo(
        `/pages/users/examine/default?id=${data.buildData.contract_disburse_switch}&cid=${data.detail.id}&types=contract`
      )
    } else if (type == 3) {
      // 添加回款 type：3
      clickNavigateTo(
        `/pages/users/examine/default?id=${data.buildData.contract_refund_switch}&cid=${data.detail.id}&types=contract`
      )
      // 添加续费
    } else if (type == 5) {
      clickNavigateTo(
        `/pages/users/examine/default?id=${data.buildData.contract_renew_switch}&cid=${data.detail.id}&types=contract`
      )
    }
    deanPopoverRef.value.close()
  }

  // 获取发票列表
  const getInvoiceList = (tab = false) => {
    data.invoiceWhere.cid = data.id
    clientInvoiceApi(data.invoiceWhere).then(res => {
      if (tab) data.invoiceList = []
      data.invoiceList.push(...res.data.list)
      const allPage = Math.ceil(res.data.count / data.invoiceWhere.limit)
      listLoading.value = data.invoiceList.length <= 0 || data.invoiceWhere.page >= allPage ? false : true
    }).catch(error => {
      message.error(error.message)
    })
  }

  // 获取附件列表
  const getFlieList = (tab = false) => {
    data.fileWhere.cid = data.id
    clientContractResourceListApi(data.fileWhere).then(res => {
      if (tab) data.fileList = []
      data.fileList.push(...res.data.list)
      const allPage = Math.ceil(res.data.count / data.fileWhere.limit)
      listLoading.value = data.fileList.length <= 0 || data.fileWhere.page >= allPage ? false : true
    }).catch(error => {
      message.error(error.message)
    })
  }

  import { onReachBottom } from '@dcloudio/uni-app'
  // 下拉加载
  onReachBottom(() => {
    if (data.currentId === 2) {
      if (listLoading.value) {
        data.remindWhere.page++
        getRemindList()
      }
    } else if (data.currentId === 3) {
      if (listLoading.value) {
        data.billWhere.page++
        getBillList()
      }
    } else if (data.currentId === 4) {
      if (listLoading.value) {
        data.invoiceWhere.page++
        getInvoiceList()
      }
    } else if (data.currentId === 5) {
      if (listLoading.value) {
        data.fileWhere.page++
        getFlieList()
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
  .customer-right {
    display: flex;
    align-items: center;
    justify-content: flex-end;

    .icon-add {
      font-size: 42rpx;
      color: $uni-text-color;
    }
  }

  .content {
    width: 100%;
    background-color: #fff;

    .icon-xuanfuanniu-jia {
      color: red;
      margin-left: 20rpx;
      margin-bottom: 2rpx;
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
        padding-top: 60rpx;
        width: 100%;
        background-color: #fff;
        position: sticky;
        top: 0;
        z-index: 9999;
      }

      /* #endif */

      .top {
        width: 100%;
        background-color: #fff;
        position: sticky;
        top: 0;
        z-index: 99;
      }


    }

    .details-content {
      position: relative;
      padding-top: 86rpx;

      .item-list-status {
        position: absolute;
        top: 30rpx;
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

      .info-content {
        font-size: 26rpx;
        color: $nui-text-color-four;
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
            font-size: 26rpx;
            color: $nui-text-color-four;
            margin-right: 38rpx;
            cursor: pointer;
            height: 100%;
            line-height: 40px;
            text-wrap: nowrap;


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

      .record-header {
        padding: 50rpx 0 42rpx 0;
        margin: 0 -30rpx;
        border-bottom: 1px solid $uni-line-style-color-three;
        line-height: 1.2;

        .left {
          padding-left: 44rpx !important;
          border-right: 1px solid $uni-line-style-color-three;
        }

        .record-name {
          font-size: 24rpx;
          color: $nui-text-color-two;
          margin-bottom: 20rpx;
        }

        .record-number {
          font-size: 32rpx;
          color: $uni-text-color;
          font-weight: $uni-default-font-weight;
        }

        .right {
          padding-left: 54rpx !important;
          border-right: 1px solid $uni-line-style-color-three;

          &:last-of-type {
            border-left: none;
          }
        }
      }
    }
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