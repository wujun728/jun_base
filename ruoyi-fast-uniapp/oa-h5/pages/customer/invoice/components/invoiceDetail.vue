<template>
  <!-- 内容 -->
  <view class="examine-content" v-if="detail.id">
    <view class="header">
      <view class="left">
        <text class="status">{{getInvoiceStatus(detail.status)}}</text>
        <view class="flex">
          <uni-dateformat v-if="detail.real_date" class="time" format="yyyy/MM/dd" :date="detail.real_date">
          </uni-dateformat>
          <text class="real-time" v-if="detail.real_date"> （实际开票时间）</text>
          <text class="real-time ml20" v-else> 暂无实际开票时间</text>

        </view>
      </view>
      <view class="right">
        <image src="@/static/image/invoice-02.png" mode="" class="img"></image>
      </view>
    </view>
    <!-- 基础信息 -->
    <view class="content-box" v-if="detail.types">
      <view class="box">
        <view class="left"></view>
        <view class="center">
          <view class="center-header">
            <view class="title">{{getInvoiceTypes(Number(detail.types))}}</view>
            <view class="amount ">开票金额</view>
            <view class="num">
              <text class="text">￥</text>{{detail.amount}}
            </view>
          </view>
          <view class="list">
            <view class="list-item">
              <view class="name"> 发票抬头 </view>
              <view class="list-text">{{detail.title}}</view>
            </view>
            <view class="list-item" v-if="detail.types > 1">
              <view class="name"> 税号 </view>
              <view class="list-text">{{detail.ident||'--'}}</view>
            </view>

          </view>
        </view>
        <view class="right"></view>
      </view>
      <!-- 合同信息 -->
      <view class="box mt20" v-if="detail.client">
        <view class="left"></view>
        <view class="center">
          <view class="list">
            <view class="list-item" v-if="detail.treaty">
              <view class="name"> 合同名称 </view>
              <view class="list-text">{{detail.treaty ? detail.treaty.title : '--'}}</view>
            </view>
            <view class="list-item">
              <view class="name"> 客户名称 </view>
              <view class="list-text">{{detail.client.name}}</view>
            </view>
            <view class="list-item">
              <view class="name"> 开票要求 </view>
              <view class="list-text">{{detail.collect_type == 'mail' ? '电子' : '纸质'}}</view>
            </view>
            <view class="list-item" v-if="detail.status!==0 &&detail.status!==-1">
              <view class="name"> 发送方式 </view>
              <view class="list-text">{{detail.invoice_type == 'mail' ? '邮箱' : '快递'}}</view>
            </view>
            <template v-if="detail.status!==0 &&detail.status!==-1">
              <view class="list-item" v-if="detail.invoice_type == 'mail'">
                <view class="name"> 邮箱地址 </view>
                <view class="list-text"> {{ detail.collect_email }}</view>
              </view>
              <template v-else>
                <view class="list-item">
                  <view class="name"> 联系人 </view>
                  <view class="list-text"> {{ detail.collect_name||'--' }}</view>
                </view>
                <view class="list-item">
                  <view class="name"> 联系电话 </view>
                  <view class="list-text"> {{detail.collect_tel||'--'}}</view>
                </view>
                <view class="list-item">
                  <view class="name"> 邮寄地址 </view>
                  <view class="list-text"> {{ detail.invoice_address||'--' }}</view>
                </view>
              </template>
            </template>
            <template v-else>
              <view class="list-item" v-if="detail.collect_type == 'mail'">
                <view class="name"> 邮箱地址 </view>
                <view class="list-text"> {{ detail.collect_email ||'--'}}</view>
              </view>
              <template v-else>
                <view class="list-item">
                  <view class="name"> 联系人 </view>
                  <view class="list-text"> {{ detail.collect_name ||'--'}}</view>
                </view>
                <view class="list-item">
                  <view class="name"> 联系电话 </view>
                  <view class="list-text"> {{detail.collect_tel||'--'}}</view>
                </view>
                <view class="list-item">
                  <view class="name"> 邮寄地址 </view>
                  <view class="list-text"> {{ detail.mail_address ||'--'}}</view>
                </view>
              </template>
            </template>

            <view class="list-item">
              <view class="name"> 付款金额 </view>
              <view class="list-text">{{detail.price || '--'}}</view>
            </view>
            <view class="list-item">
              <view class="name">开票日期 </view>
              <view class="list-text">
                <uni-dateformat format="yyyy/MM/dd" :date="detail.bill_date"></uni-dateformat>
                <text class="time">(期望)</text>
              </view>
            </view>
            <view class="list-item">
              <view class="name"> 备注 </view>
              <view class="list-text">{{detail.mark ? detail.mark : '--'}}</view>
            </view>
          </view>
        </view>
        <view class="right"></view>
      </view>
      <!-- 开票信息 -->
      <view class="box mt20">
        <view class="left"></view>
        <view class="center">
          <view class="list">
            <view class="list-item">
              <view class="name"> 开票结果 </view>
              <view class="list-text">{{getInvoiceStatus(detail.status)}}</view>
            </view>
            <view class="list-item" v-if="detail.status!==0 &&detail.status!==-1">
              <view class="name"> 审核备注 </view>
              <view class="list-text" v-if="detail.status==-1">--</view>
              <view class="list-text" v-else>{{detail.remark||'--' }}</view>
            </view>
            <view class="list-item" v-if="detail.attachs.length>0&&!detail.serial_number">
              <view class="name"> 开票凭证 </view>
              <image :src="detail.attachs[0].url" mode="" class="list-text" style="width: 80rpx; height: 80rpx;">
              </image>
            </view>

            <view class="list-item" v-if="detail.serial_number">
              <view class="name"> 开票凭证 </view>
              <text class="iconfont icon-wenzhang" @click="preview(detail.attach)"></text>

            </view>
            <view class="list-item" v-if="detail.status !==0">
              <view class="name"> 操作时间 </view>
              <view class="list-text">{{detail.updated_at }}</view>
            </view>
            <view class="list-item">
              <view class="name"> 申请人 </view>
              <view class="list-text">{{detail.card ? detail.card.name : '--' }}</view>
            </view>
            <view class="list-item">
              <view class="name"> 申请时间 </view>
              <view class="list-text">{{detail.created_at }}</view>
            </view>
          </view>
        </view>
        <view class="right"></view>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
  import { toRefs } from 'vue'
  import type { Detail } from '@/utils/typeHelper'

  const props = withDefaults(
    defineProps<{
      detail : Detail,
    }>(), {

  })
  const { detail } = toRefs(props)

  // 发票状态
  enum InvoiceStatus {
    '发票作废' = -1,
    '待审核',
    '待开票',
    '已拒绝',
    '撤回开票',
    '申请作废',
    '已开票',
  }
  import { lookPreview } from '@/utils/helper'
  const getInvoiceStatus = (status : number = 0) : string => {
    return InvoiceStatus[status]
  }
  // 图片与文档预览
  const preview = item => {
    lookPreview(item, '发票', [item])
  }
  // 发票类型
  enum InvoiceTypes {
    '个人普通发票' = 1,
    '企业普通发票',
    '企业专用发票'
  }
  const getInvoiceTypes = (status : number = 1) : string => {
    return InvoiceTypes[status]
  }
</script>

<style lang="scss" scoped>
  .m30 {
    margin: 0 30rpx;
  }

  .icon-wenzhang {
    font-size: 60rpx;
    margin-left: 20rpx;
    color: #388AEF;
  }


  .mt20 {
    margin-top: 20rpx;
  }

  .examine-content {
    /* #ifdef APP-PLUS */
    padding-top: calc($uni-default-bar-height);
    /* #endif */
    /* #ifndef APP-PLUS */
    padding-top: 20rpx;
    /* #endif */
    position: relative;

    .content-box {
      padding: 0 30rpx 20rpx 30rpx;
      width: 100%;
      position: absolute;
      top: 82%;
    }

    .header {
      height: 344rpx;
      background-color: #388AEF;
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 0 30rpx;

      .left {

        display: flex;
        flex-direction: column;


        .status {
          display: flex;
          margin-left: 30rpx;
          font-size: 36rpx;
          font-family: PingFang SC-Medium, PingFang SC;
          font-weight: 500;
          color: #FFFFFF;
        }

        .flex {
          margin-top: 10rpx;

          .time {
            margin-left: 30rpx;
            margin-top: 24rpx;
            font-size: 28rpx;
            font-family: PingFang SC-Regular, PingFang SC;
            font-weight: 400;
            color: #FFFFFF;
          }

          .real-time {
            display: inline-block;
            margin-left: 10rpx;
            color: #fff;
            font-size: 28rpx;
            font-family: PingFang SC-Regular, PingFang SC;
            font-weight: 400;
          }
        }

      }

      .right {
        .img {
          width: 204rpx;
          height: 204rpx;
        }
      }
    }

    .box {
      background-color: #fff;
      display: flex;
      justify-content: space-between;
      border-radius: 16rpx;

      .left {
        display: flex;
        width: 40rpx;
        padding-top: 12rpx;
        flex-direction: column;
        align-items: center;
        border-right: 2px dashed #EBEEF4;
        background: url('@/static/image/invoice-bg.png') repeat-y 50% 0;
      }

      .right {
        width: 40rpx;
        display: flex;
        padding-top: 12rpx;
        flex-direction: column;
        align-items: center;
        border-left: 2px dashed #EBEEF4;
        background: url('@/static/image/invoice-bg.png') repeat-y 50% 0;
      }

      .center {
        .center-header {
          margin-top: 40rpx;
          display: flex;
          flex-direction: column;
          align-items: center;
        }

        .num {
          margin-top: 10rpx;
          text-align: center;
          font-size: 36rpx;
          font-family: PingFang SC-Medium, PingFang SC;
          font-weight: 500;
          color: #ED4014;

          .text {
            font-size: 28rpx;
          }
        }

        .title {
          width: 426rpx;
          height: 80rpx;
          text-align: center;
          font-size: 30rpx;
          line-height: 80rpx;
          font-family: PingFang SC-Medium, PingFang SC;
          font-weight: 500;
          color: #A3442B;
          background: url(@/static/image/invoice-03.png) no-repeat;
          background-size: 100% 100%;
          word-spacing: 1;
        }

        .list {
          margin-top: 50rpx;

          .list-item {
            display: flex;
            color: #909399;
            // align-items: flex-end;
            font-family: PingFang SC-常规体, PingFang SC;
            font-size: 28rpx;
            margin-bottom: 36rpx;

            .list-text {
              width: 378rpx;
              margin-left: 30rpx;
              color: #303133;

              .time {
                margin-left: 8rpx;
                color: #909399;
              }
            }

            .name {
              min-width: 112rpx;
              text-align: right;
            }
          }
        }

        .amount {
          margin-top: 40rpx;
          text-align: center;
          font-size: 26rpx;
          font-family: PingFang SC-Regular, PingFang SC;
          font-weight: 400;
          color: #909399;
        }
      }
    }
  }

  .ml20 {
    display: inline-block;
    margin-left: 30rpx !important;
  }
</style>