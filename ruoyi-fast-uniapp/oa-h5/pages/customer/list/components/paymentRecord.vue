<template>
  <view class="examine-content-list">
    <uni-list :border="false" v-if="listData.length > 0"
      :style="{backgroundColor: typeIndex === 0 ? '#fff': '#f5f5f5'}">
      <uni-list-item v-for="(item,index) in listData"
        :class="typeIndex === 0 ? 'content-item-list' : 'content-item-list-card'" :key="'list'+item.id">
        <template v-slot:body>
          <view class="item-list" @click="changeItem(item)">
            <uni-row class="item-list-content">
              <uni-col :span="6" class="left">
                <text class="iconfont icon-hetongxufei" v-if="!cid"></text>
                <image v-else :src="item.card.avatar" mode="" class="img"></image>
              </uni-col>
              <uni-col :span="18" class="right">
                <view class="right-top">
                  <uni-row class="display-align">
                    <uni-col :span="18" class="right-top-left" v-if="cid">{{item.card?item.card.name:'--'}}</uni-col>
                    <uni-col :span="18" class="right-top-left" v-else>{{item.treaty?item.treaty.title:'--'}}</uni-col>
                    <uni-col :span="7" class="text-right right-top-right ">
                      <uni-tag v-if="item.status==0" :inverted="true" text="待审核" type="warning" size="mini" />
                      <uni-tag v-else-if="item.status==2" :inverted="true" text="未通过" type="error" size="mini" />
                      <uni-tag v-if="item.status==-1" :inverted="true" text="已撤回" size="mini" />
                      <uni-tag v-if="item.status==1" :inverted="true" text="已通过" type="success" size="mini" />
                    </uni-col>
                  </uni-row>
                </view>
                <view class="right-bottom">
                  <uni-row class="right-bottom-row display-align">
                    <uni-col :span="17" class="display-align">
                      <view class="right-bottom-name" v-if="cid==0">{{item.card.name}}</view>
                      <view class="right-bottom-name">
                        <uni-dateformat format="yyyy/MM/dd hh:mm" :date="item.created_at" />
                      </view>
                    </uni-col>
                    <uni-col :span="7" class="text-right line1-1">{{item.pay_type}}</uni-col>
                  </uni-row>
                </view>
              </uni-col>
            </uni-row>

            <view class="item-list-button">
              <uni-row class="item-bottom display-align">
                <uni-col :span="12">
                  <view class="item-bottom-text">{{getPayRecordTypes(item.types)}}金额</view>
                  <view class="item-bottom-number">{{item.num }}元</view>
                </uni-col>
                <uni-col :span="12">
                  <view class="item-bottom-text">付款日期</view>
                  <view class="item-bottom-number">
                    <uni-dateformat format="yyyy-MM-dd " :date="item.date" />
                  </view>
                </uni-col>
              </uni-row>
            </view>

            <view class="item-button-content" v-if="typeIndex === 1 && item.status === 0">
              <button class="button default-error" @click.stop="btnChange(item, 0)">拒绝</button>
              <button class="button default-color" @click.stop="btnChange(item, 1)">同意</button>
            </view>
          </view>
        </template>
      </uni-list-item>
    </uni-list>
    <empty v-else :index="9" :title="emptyTitle"></empty>
  </view>
</template>

<script setup lang="ts">
  import empty from '@/components/empty/index.vue'
  import { toRefs } from 'vue'
  import { clickNavigateTo } from '@/utils/helper'
  import type { Detail } from '@/utils/typeHelper'
  import { getPayRecordTypes } from '@/utils/assessment'

  const props = withDefaults(
    defineProps<{
      listData : Array<any>
      typeIndex ?: number
      cid ?: string | number
      emptyTitle ?: string,
      tab ?: number
    }>(), {
    listData: <any>[],
    typeIndex: 0,
    cid: 0,
    tab: 0,
    emptyTitle: '暂无付款记录，快去添加吧！'
  })
  // toRef {

  const { listData, typeIndex, emptyTitle, cid, tab } = toRefs(props)

  let emit = defineEmits(['change'])
  const changeItem = (item : Detail) : void => {
    let str = ''
    if (typeIndex.value === 0) {
      str = `/pages/customer/contract/collectionDetails?id=${item.id}&eid=${item.eid}&types=${cid.value ? cid.value : '0'}`
    } else {
      str = `/pages/finance/payment/details?id=${item.id}&tab=${tab.value}`
    }
    clickNavigateTo(str)
  }
  // 内容点击
  const btnChange = (item : Detail, type : number) : void => {
    emit('change', { row: item, type: type })
  }
</script>

<style scoped lang="scss">
  .examine-content-list {
    ::v-deep .uni-list {
      .uni-list--border {
        top: auto;
        left: auto;
      }

      .content-item-list {
        border-bottom: 1px solid rgba(240, 241, 245, 0.55);

        .uni-list-item__container {
          padding: 50rpx 0 42rpx 0;
        }
      }

      .content-item-list-card {
        margin-bottom: 20rpx;

        .uni-list-item__container {
          padding: 36rpx 30rpx;
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
        width: 100%;

        .left {
          width: 80rpx;
          height: 84rpx;

          .iconfont {
            font-size: 84rpx;
            color: $uni-color-primary;
          }

          .img {
            width: 80rpx;
            height: 80rpx;
          }
        }

        .right {
          padding-left: 20rpx !important;
          width: calc(100% - 80rpx);
          height: 80rpx;
          display: flex;
          flex-direction: column;
          justify-content: space-between;


          .right-top {
            font-size: $uni-font-size-default;
            font-weight: $uni-default-font-weight;
            color: $uni-text-color;
            margin-bottom: 12rpx;

            .right-top-left {
              max-width: 80%;
              width: auto;
            }

            .right-top-right {
              max-width: 20%;
              margin-left: 12rpx;
              width: auto;
              line-height: 1;

              ::v-deep .uni-tag {
                font-size: 12px;
                padding: 2px 5px 0 5px;
                transform: scale(0.91, 0.91);
              }
            }
          }

          .right-bottom {
            font-size: 24rpx;
            color: $nui-text-color-two;

            .right-bottom-row {
              .right-bottom-name {
                padding: 0 20rpx;
                font-size: 24rpx;
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
      }

      .item-list-button {
        margin-top: 36rpx;
        display: flex;
        justify-content: space-between;
        background-color: #F2F6FC;
        padding: 28rpx 0;
        border-radius: 8rpx;
        width: 100%;

        .item-bottom {
          width: 100%;

          ::v-deep .uni-col {
            padding-left: 40rpx !important;
            border-right: 1px solid $uni-line-style-color-three;

            &:last-of-type {
              border-right: none;
            }
          }

          .item-bottom-text {
            margin-bottom: 20rpx;
            font-size: 24rpx;
            color: $nui-text-color-two;
          }

          .item-bottom-number {
            font-size: 32rpx;
            color: $uni-text-color;
            font-family: PingFang SC-中黑体, PingFang SC;
            font-weight: 500;

          }
        }
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