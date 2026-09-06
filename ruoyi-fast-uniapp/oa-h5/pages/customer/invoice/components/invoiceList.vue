<template>
  <view class="examine-content-list">
    <uni-list :border="false" v-if="listData.length > 0">
      <uni-list-item v-for="(item,index) in listData"
        :class="type ===0 ? 'content-item-list' : 'content-item-list-card'" :key="'list'+item.id">
        <template v-slot:body>
          <view class="item-list" @click="itemList(item)">
            <uni-row class="item-list-content">
              <uni-col :span="6" class="left">
                <text v-if="item.types == 1" class="iconfont icon-fapiao-gerenputongfapiao addColor"></text>
                <text v-if="item.types == 2" class="iconfont icon-fapiao-qiyeputongfapiao1"></text>
                <text v-if="item.types == 3" class="iconfont icon-fapiao-qiyezhuanyongfapiao"></text>
              </uni-col>
              <uni-col :span="18" class="right">
                <view class="name line1-1">
                  <text v-if="item.types==1">个人普通发票</text>
                  <text v-if="item.types==2">企业普通发票</text>
                  <text v-if="item.types==3">企业专用发票</text>
                  · {{item.collect_type == 'mail' ? '电子' : '纸质'}}
                </view>
                <view class="time display-align">
                  <view class="text">{{item.card.name||'--'}}</view>
                  <view class="text">
                    <uni-dateformat format="yyyy/MM/dd" :date="item.created_at"></uni-dateformat>
                  </view>
                </view>
              </uni-col>
            </uni-row>
            <view class="item-list-button">
              <view class="item-bottom-top">
                <text class="name">发票抬头</text>
                <text class="number">{{item.title||'--'}}</text>
              </view>
              <uni-row class="item-bottom-item">
                <uni-col :span="11">
                  <view class="name">开票金额</view>
                  <view class="number">{{item.amount||'--'}}元</view>
                </uni-col>
                <uni-col :span="11" :offset="2">
                  <view class="name">实际开票日期</view>
                  <view class="number">
                    <uni-dateformat v-if="item.real_date" format="yyyy/MM/dd" :date="item.real_date"></uni-dateformat>
                    <view v-else>
                      --
                    </view>
                  </view>
                </uni-col>
              </uni-row>
            </view>
            <image class="item-list-status" :src="statusTypeFn(item.status)" mode=""></image>
            <view class="item-button-content" v-if="isFinance && !data.showArray.includes(item.status)">
              <button class="button default-error" v-if="item.status === 5 "
                @click.stop="toExamine(item, 2)">作废</button>
              <button v-if="item.status == 1" class="button default-color"
                @click.stop="toExamine(item, 1)">录入发票</button>
            </view>
          </view>
        </template>
      </uni-list-item>
    </uni-list>
    <empty v-else :index="10" :title="emptyTitle"></empty>
  </view>
</template>

<script setup lang="ts">
  import empty from '@/components/empty/index.vue'
  import { reactive, toRefs } from 'vue'
  import type { Detail } from '@/utils/typeHelper'
  import { clickNavigateTo } from '@/utils/helper'

  const props = withDefaults(
    defineProps<{
      btnShow ?: boolean,
      type ?: number
      eid ?: string | number
      cid ?: string | number
      name ?: string
      isFinance ?: boolean
      emptyTitle ?: string
      tab ?: number
      listData : Array<any>
    }>(), {
    btnShow: false,
    type: 0,
    eid: 0,
    cid: 0,
    name: '',
    isFinance: false,
    emptyTitle: '',
    tab: 0,
    listData: <any>[]
  })
  // 导出 {
  const { listData, type, emptyTitle, eid, cid, name, isFinance, tab } = toRefs(props)

  const data = reactive({
    showArray: [2, 4],
    addBtn: '添加发票',
    addUrl: `/pages/customer/invoice/addInvoice?eid=${eid.value}&name=${name.value}&cid=${cid.value}`
  })
  let emit = defineEmits(['change'])

  // 图标判断
  enum StatusType {
    '/static/image/invoice4.png' = -1,
    '/static/image/examine.png',
    '/static/image/invoice0.png',
    '/static/image/invoice2.png',
    '/static/image/invoice01.png',
    '/static/image/invoice3.png',
    '/static/image/invoice1.png',
  }
  const statusTypeFn = (val : number) : string => {
    return StatusType[val]
  }

  const itemList = (item : Detail) : void => {
    if (isFinance.value) {
      clickNavigateTo(`/pages/finance/invoice/details?id=${item.id}&tab=${tab.value}`)
    } else {
      clickNavigateTo(`/pages/customer/invoice/details?id=${item.id}`)
    }
  }
  const toExamine = (row : object, type : number) : void => {
    emit('change', {
      row,
      type
    })
  }
</script>

<style scoped lang="scss">
  .examine-content-list {
    ::v-deep .uni-list {
      background-color: $uni-default-bg;

      .uni-list--border {
        top: auto;
        left: auto;
      }

      .content-item-list {
        margin-bottom: 20rpx;
        border-radius: 8rpx;

        .uni-list-item__container {
          padding: 36rpx 24rpx;
        }
      }

      .content-item-list-card {
        border-bottom: 1px solid $uni-line-style-color-three;

        &:last-of-type {
          border-bottom: none;
        }

        .uni-list-item__container {
          padding: 36rpx 0;
        }

        .item-list-status {
          top: 0;
          right: 0;
        }
      }

    }

    .item-list {
      width: 100%;
      position: relative;

      .item-list-content {
        font-size: 28rpx;
        color: $uni-text-color;
        font-weight: 400;

        .left {
          width: 84rpx;
          height: 84rpx;


          .iconfont {
            font-size: 84rpx;
            color: $uni-color-primary;
          }

          .addColor {
            color: #FF9900;
          }
        }

        .right {
          width: calc(100% - 84rpx);
          padding-left: 20rpx !important;
          height: 80rpx;
          display: flex;
          justify-content: space-between;
          flex-direction: column;

          .name {
            font-size: 30rpx;
            font-family: PingFang SC-中黑体, PingFang SC;
            font-weight: 500;
          }

          .time {
            display: flex;
            align-items: center;
            color: $nui-text-color-four;
            font-size: 24rpx;

            .text {
              font-size: 24rpx;
              padding: 0 20rpx;
              border-right: 1px solid $uni-line-style-color-three;
              line-height: 24rpx;

              &:first-of-type {
                padding-left: 0;
              }

              &:last-of-type {
                padding-right: 0;
                border-right: none;
              }
            }
          }
        }

      }

      .item-list-button {
        margin-top: 36rpx;
        padding: 32rpx 0 32rpx 40rpx;
        background-color: #F2F6FC;
        border-radius: 8rpx;

        .name {
          display: block;
          font-size: 24rpx;
          color: $nui-text-color-two;
          margin-bottom: 20rpx;
        }

        .number {
          display: block;
          font-size: 32rpx;
          font-weight: 400;
          color: $uni-text-color;
        }

        .item-bottom-top {
          width: 100%;
        }

        .item-bottom-item {
          margin-top: 50rpx;

          .number {
            font-weight: normal;
          }
        }
      }

      .item-list-status {
        position: absolute;
        top: -36rpx;
        right: -24rpx;
        width: 160rpx;
        height: 188rpx;
      }


      .item-button-content {
        padding-top: 30rpx;
        display: flex;
        justify-content: space-between;

        .button {
          width: 100%;
          height: 74rpx;
          line-height: 74rpx;
          font-size: 30rpx;
          background-color: #F0F1F5;
          margin-right: 32rpx;

          &:last-of-type {
            margin-right: 0;
          }

          &::after {
            border-radius: 8rpx;
            border: none;
          }
        }
      }
    }
  }
</style>