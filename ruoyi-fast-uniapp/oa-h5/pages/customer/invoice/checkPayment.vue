<template>
  <view class="content">
    <!-- 选择付款单页面 -->
    <view class="cr-position-header">
      <view class="status_bar"></view>
      <default-nav-bar :is-right="true" :default-title="data.defaultTitle">
      </default-nav-bar>
    </view>
    <view class="examine-content m10">
      <view class="item" v-for="(item,index) in data.list" :key="index">
        <view class="iconfont icon-xuanzeanniu-weixuan" @click="check(item,index)" v-if="data.ids.indexOf(item.id)<0" />
        <view class="iconfont icon-denglu-tongyi" @click="check(item)" v-else />
        <view class="list-item">

          <view class="name"> 付款单号</view>
          <view class="list-text">{{item.bill_no }}</view>
        </view>
        <view class="list-item">
          <view class="name"> 合同名称 </view>
          <view class="list-text">{{item.treaty?item.treaty.contract_name:'--'}}</view>
        </view>
        <view class="list-item">
          <view class="name"> 业务类型 </view>
          <view class="list-text">{{item.types==0?'回款记录':'续费记录'+'-'+item.renew.title}}</view>
        </view>
        <view class="list-item">
          <view class="name"> 付款金额 </view>
          <view class="list-text">{{item.num?item.num:'--'}}</view>
        </view>
        <view class="list-item">
          <view class="name"> 发票状态 </view>
          <view class="list-text" v-if="item.status==-1">开票撤回</view>
          <view class="list-text" v-if="item.status==0">待开票</view>
          <view class="list-text" v-if="item.status==1">已开票</view>
          <view class="list-text" v-if="item.status==2">已拒绝</view>
          <view class="list-text" v-if="item.status==3">申请作废</view>
          <view class="list-text" v-if="item.status==4">同意作废</view>

          <view class="list-text" v-if="item.status==5">拒绝作废</view>
          <view class="list-text" v-if="item.status==6">作废撤回</view>
          <view class="list-text" v-if="!item.status">--</view>
        </view>
        <view class="list-item">
          <view class="name"> 创建人 </view>
          <view class="list-text">{{item.card?item.card.name:'--'}}</view>
        </view>
        <view class="list-item">
          <view class="name"> 创建时间 </view>
          <view class="list-text">{{item.created_at }}</view>
        </view>
      </view>
    </view>

    <!-- 底部 -->
    <view class="footer">
      <view class="flex">
        <view class="iconfont icon-xuanzeanniu-weixuan" v-if="data.checkShow" @click="checkAll(data.checkTitle)" />
        <view class="iconfont icon-denglu-tongyi" v-if="!data.checkShow" @click="checkAll(data.checkTitle)" />
        <view class="text">
          {{data.checkTitle}}（{{data.ids.length}}/{{data.list.length}}）
        </view>
      </view>
      <view class="flex">
        <view class="btn cancel" @click="cancel">
          取消
        </view>
        <view class="btn next" @click="goAddInvoice">
          下一步
        </view>
      </view>
    </view>
    <!-- <uni-popup ref="popupRef" background-color="#fff" type="bottom">
      <view class="uni-picker-header">
        <view class="uni-picker-action uni-picker-action-cancel" @click="cancelPopup">取消</view>
        <view class="uni-picker-action uni-picker-action-confirm" @click="confirmPopup">完成</view>
      </view>
      <picker-view :indicator-style="data.indicatorStyle" :value="data.newValue" class="picker-view"
        @change="changePicker">
        <picker-view-column>
          <view @click="changePicker(index)" class="item-value" v-for="(item,index) in data.buildData" :key="index">
            {{item.name}}
          </view>
        </picker-view-column>
      </picker-view>
    </uni-popup> -->
  </view>
</template>

<script setup>
  import defaultNavBar from '@/components/defaultNavBar/index'
  import {
    ref,
    reactive,
    toRefs,
    onMounted,
    computed
  } from 'vue'
  import {
    unInvoicedListApi,
    configApproveApi
  } from '@/api/customer'
  const data = reactive({
    defaultTitle: '选择付款单',
    isShowTitle: true,
    checkTitle: '全选',
    checkShow: true,
    ids: [],
    id: 0, //审批id
    eid: '', // 客户id
    cid: '',
    name: '', // 客户名称
    list: [],
    newtol: [],
    num: '',
    buildData: [],
    current: 0
  })
  import {
    onLoad
  } from '@dcloudio/uni-app'
  const popupRef = ref()
  onLoad(e => {
    if (e.cid) {
      data.cid = e.cid
    }
    if (e.id) {
      data.id = e.id
    }
    data.eid = e.eid
    data.name = e.name
    getList(e.eid)
    data.list.forEach(item => {
      item.show = false
    })
    getConfigApprove()
  })

  import {
    clickNavigateTo,
    backToTop
  } from '@/utils/helper'

  const goAddInvoice = () => {
    clickNavigateTo(
      `/pages/users/examine/default?id=${data.id}&bill_id=${data.ids}&eid=${data.eid}`)

  }
  // const cancelPopup = () => {
  //   popupRef.value.close()
  // }
  const getConfigApprove = () => {
    configApproveApi().then(res => {
      data.buildData = res.data
    })
  }
  const changePicker = (e) => {
    data.current = e.detail.value
  }

  // const confirmPopup = () => {
  //   const row = data.buildData[data.current]
  //   clickNavigateTo(`/pages/users/examine/default?id=${row.id}&name=${row.name}&bill_id=${data.eid}`)
  // }

  const cancel = () => {
    uni.navigateBack()
  }

  const getList = id => {
    let dataInfo = {
      eid: id
    }
    unInvoicedListApi(dataInfo).then(res => {
      data.list = res.data
    })
  }


  const check = (val, index) => {
    let labelIdList = data.ids.indexOf(val.id)
    if (labelIdList < 0) {
      data.ids.push(val.id) //添加
      data.newtol.push(Number(val.num))
    } else {
      data.newtol = data.newtol.filter(item => {
        return item !== Number(val.num)
      })
      data.ids = data.ids.filter(item => {
        return item !== val.id
      })

    }

    data.num = data.newtol.reduce((x, y) => x + y).toFixed(2)
  }
  const checkAll = val => {
    data.ids = []
    if (val === '全选') {
      data.checkShow = false
      data.checkTitle = '取消全选'
      data.list.map(item => {
        data.ids.push(item.id)
      })
    } else if (val === '取消全选') {
      data.checkShow = true
      data.checkTitle = '全选'
      data.ids = []
    }
  }
  const onCheck = val => {
    data.ids.splice(val.id, 1)
  }
</script>

<style lang="scss">
  .content {
    .flex {
      display: flex;
    }

    .examine-content {
      padding-top: calc($uni-default-bar-height + var(--status-bar-height));
      padding-bottom: 126rpx;

      .item {
        margin-bottom: 20rpx;
        width: 100%;
        position: relative;
        background-color: #fff;
        border-radius: 16rpx;
        padding: 40rpx 30rpx;
        padding-left: 96rpx;

        .iconfont {
          position: absolute;
          top: 40rpx;
          left: 30rpx;
          font-size: 36rpx;
          color: #C0C4CC;
        }

        .icon-denglu-tongyi {
          color: #1890FF;
        }

        .list-item {
          display: flex;
          margin-bottom: 24rpx;

          .list-text {
            height: 28rpx;
            width: 378rpx;
            margin-left: 40rpx;
            color: #303133;
            font-size: 28rpx;
            line-height: 42rpx;
          }

          .name {
            color: #909399;
            font-family: PingFang SC-常规体, PingFang SC;
            font-size: 28rpx;
            width: 112rpx;
            text-align: right;
          }
        }
      }
    }

    .footer {
      position: fixed;
      bottom: 0;
      width: 100%;
      height: 116rpx;
      padding: 0 30rpx;
      background-color: #fff;
      display: flex;
      justify-content: space-between;
      align-items: center;


      .btn {
        width: 168rpx;
        height: 74rpx;
        font-size: 30rpx;
        font-family: PingFang SC-常规体, PingFang SC;
        font-weight: 400;
        text-align: center;
        line-height: 74rpx;
        border-radius: 8rpx;
      }

      .cancel {
        background-color: #F0F1F5;
        color: #303133;
        margin-right: 24rpx;
      }

      .next {
        color: #fff;
        background-color: #1890FF;
      }

      .text {
        font-size: 30rpx;
        font-family: PingFang SC-常规体, PingFang SC;
        font-weight: 400;
        color: #303133;
      }

      .icon-denglu-tongyi {
        margin-top: 4rpx;
        color: #1890FF;
        margin-right: 26rpx;
      }

      .icon-xuanzeanniu-weixuan {
        margin-top: 4rpx;
        color: #C0C4CC;
        margin-right: 26rpx;
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