<template>
  <view class="content-box">
    <view class="price">
      <view class="iconfont icon-hetong-hetonghuikuan" v-if="info.types==0" />
      <view class="iconfont icon-tixing-fukuantixing" v-else />
      <view class="num">{{info.num}} <text>元</text></view>
      <view class="text" v-if="info.types==0">
        合同回款
      </view>
      <view class="text" v-if="info.types==1">
        合同续费-{{info.renew?info.renew.title:''}}
      </view>
      <image src="@/static/image/passed.png" mode="" class="passed" v-if="info.status==1"></image>
      <image src="@/static/image/failed.png" mode="" class="passed" v-if="info.status==2"></image>
      <image src="@/static/image/examine.png" mode="" class="passed" v-if="info.status==0"></image>
      <image src="@/static/image/withdrawn.png" mode="" class="passed" v-if="info.status==-1"></image>
    </view>
    <view class="details">
      <view class="list">
        <text class="title"> 支付方式 </text><text class="list-text">{{info.pay_type||'--' }}</text>
      </view>
      <view class="list">
        付款时间 <text class="list-text">{{info.date||'--'}}</text>
      </view>
      <view class="list">
        付款单号 <text class="list-text">{{info.bill_no||'--' }}</text>
      </view>
      <view class="list flex_start">
        付款凭证
        <view class="file" v-if="info.attachs.length>0">
          <view class="item" v-for="(fileItem,index) in info.attachs" :key="index" @click="preview(fileItem)">
            <view class="left">
              <image class="slot-image" :src="`/static/image/cloudfile/${isFileTypeIcon(fileItem.real_name)}`"
                mode="widthFix">
              </image>
              <view>
                <view class="name">
                  {{fileItem.real_name }}
                </view>

              </view>
            </view>
          </view>
        </view>
        <view v-else class="list-text">--</view>

      </view>
      <view class="list">
        客户名称 <text class="list-text">{{info.client?info.client.customer_name:'--'}}</text>
      </view>
      <view class="list">
        合同名称 <text class="list-text">{{info.treaty?info.treaty.contract_name:'--'}}</text>
      </view>
      <view class="list" v-if="info.types==1">
        续费到期 <text class="list-text">{{info.end_date.indexOf('0000') > -1 ? '--' : info.end_date }}</text>
      </view>

      <view class="list">
        <text class="title"> 业务员 </text> <text class="list-text">{{info.card?info.card.name:'--'}}</text>
      </view>
      <view class="list">
        <text class="title"> 备注 </text> <text class="list-text">{{info.mark||'--'}}</text>
      </view>
      <view class="list">
        审核结果
        <text class="list-text" v-if="info.status === 1 && !info.recall">已通过</text>
        <text class="list-text" v-if="info.status==0">待审核</text>
        <text class="list-text" v-if="info.status === 1 && info.recall">撤销中</text>
        <text class="list-text" v-if="info.status==2">未通过，<text v-if="info.fail_msg">{{info.fail_msg}}</text></text>
        <text class="list-text" v-if="info.status==-1">已撤销</text>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
  import { isFileTypeIcon, lookPreview, isTypeImage } from '@/utils/helper'
  import { toRefs } from 'vue'
  const props = defineProps({
    info: {
      type: Object,
      default() {
        return {}
      }
    }
  })
  // 图片与文档预览
  const preview = item => {
    lookPreview(item.src, item.real_name, [item.src])
  }
  const { info } = toRefs(props)
</script>
<style lang="scss" scoped>
  ::v-deep .drop-down-list {
    min-width: 200rpx !important;
  }

  .content {
    .content-box {
      width: 100%;
      background-color: #fff;
      border-radius: 16rpx;
      padding-bottom: 50rpx;

      .price {
        position: relative;
        width: 100%;
        height: 352rpx;
        padding-top: 60rpx;
        display: flex;
        flex-direction: column;
        align-items: center;


        .passed {
          display: block;
          width: 165rpx;
          height: 185rpx;
          top: 0;
          right: 0;
          position: absolute;
        }

        .icon-hetong-hetonghuikuan {
          font-size: 108rpx;
          color: #19BE6B;
          margin-bottom: 20rpx;
        }

        .icon-tixing-fukuantixing {
          font-size: 108rpx;
          color: #FF9900;
          margin-bottom: 20rpx;
        }

        .num {
          font-size: 48rpx;
          font-family: PingFang SC-中粗体, PingFang SC;
          font-weight: 600;
          color: #303133;
        }

        .text {
          font-size: 28rpx;
          font-family: PingFang SC-常规体, PingFang SC;
          font-weight: 400;
          color: #909399;
        }



        .num>text {
          font-size: 36rpx !important;
        }
      }
    }

    .details {
      margin: 0 30rpx;
      padding-top: 58rpx;
      border-top: 1px solid #F0F1F5;



      .list {
        display: flex;
        margin-left: 10rpx;
        color: #909399;
        font-family: PingFang SC-常规体, PingFang SC;
        font-size: 28rpx;
        margin-bottom: 36rpx;
        align-items: flex-end;

        .img {
          display: inline-block;
          width: 92rpx;
          height: 92rpx;
          margin-left: 30rpx;
        }

        .title {
          display: inline-block;
          width: 112rpx;
          text-align: right;
        }

        .list-text {
          display: block;
          width: 478rpx;
          margin-left: 30rpx;
          color: #303133;
        }
      }
    }
  }

  .flex_start {
    display: flex;
    align-items: flex-start !important;
  }

  .file {
    margin-left: 14rpx;

    .item {
      padding: 14rpx;
      width: 500rpx;
      height: 78rpx;
      background: #F6F7F9;
      border-radius: 8rpx;
      margin-bottom: 12rpx;

      .slot-image {
        display: inline-block;
        width: 52rpx;
        height: 52rpx;
        margin-right: 10rpx;
      }

      .left {
        display: flex;
        font-family: PingFang SC, PingFang SC;
        font-weight: 400;

        .name {
          font-size: 24rpx;
          color: #303133;
        }

        .size {
          font-size: 20rpx;
          color: #909399;
          margin-top: 2rpx;
        }
      }
    }
  }
</style>