<template>
  <view class="examine-content-list">
    <uni-list :border="false" v-if="listData.length > 0">
      <uni-list-item v-for="(item,index) in listData" :key="'list'+item.id">
        <template v-slot:body>
          <view class="item-list">
            <view @click="examineList(item)">
              <uni-row class="item-list-content">
                <uni-col :span="6" class="left">
                  <text class="iconfont icon-kehuxiangqing-hetong"></text>
                </uni-col>
                <uni-col :span="18" class="right">
                  <view class="name line1-1">{{item.contract_name }}</view>
                  <view class="time">
                    起止时间：
                    <uni-dateformat format="yyyy/MM/dd" :date="item.start_date"></uni-dateformat>
                    -
                    <template v-if="item.end_date !== '0000-00-00'">
                      <uni-dateformat format="yyyy/MM/dd" :date="item.end_date"></uni-dateformat>
                    </template>
                    <template v-else>永久</template>
                  </view>
                </uni-col>
              </uni-row>
              <view class="item-list-button">
                <view class="item-bottom">
                  <uni-row class="item-bottom-top">
                    <uni-col :span="11">
                      <text class="name">总金额</text>
                      <text class="number">{{item.
contract_price}}</text>
                    </uni-col>
                    <uni-col :span="11" :offset="2">
                      <text class="name">未回款</text>
                      <text class="number1">{{item.surplus}}</text>
                    </uni-col>
                  </uni-row>
                  <uni-row class="item-bottom-item" v-for="(items,indexs) in item.bills" :key="items.id">
                    <uni-col :span="2" class="item-bottom-item-left">
                      <text class="iconfont icon-tixing-fukuantixing"></text>
                    </uni-col>
                    <uni-col :span="22" class="item-bottom-item-right">
                      <uni-row>

                        <uni-col class="item-right-type line1-1" :span="6">{{items.renew ? items.renew.title : '--'}}
                        </uni-col>
                        <uni-col class="item-right-type line1-1" :span="6">{{items.num}}</uni-col>

                        <uni-col :span="12" class="text-right">
                          <uni-dateformat class="time" format="yyyy/MM/dd" :date="items.date"></uni-dateformat>
                        </uni-col>
                      </uni-row>
                    </uni-col>
                  </uni-row>
                </view>
              </view>
            </view>

            <view class="item-list-bottom" v-if="type === 1">
              <uni-row class="item-bottom display-align">
                <uni-col :span="16" class="display-align">
                  <avatar :src="item.salesman.avatar" :auto-size="false" :width="60" :height="60" :radius="8"></avatar>
                  <view class="item-bottom-text">{{item.salesman.name}} 负责</view>
                </uni-col>
                <uni-col :span="8" class="text-right item-bottom-right" v-if="follow < 1"
                  @click="clickFollow(index,item)">
                  <text class="iconfont" :class="item.contract_followed
 == 0 ? 'icon-shequ-shoucang' : 'icon-shequ-shoucang-yishoucang selected-color'"></text>
                  {{item.contract_followed
 == 0 ? '关注' : '已关注'}}
                </uni-col>
              </uni-row>
            </view>

            <!-- <image v-if="item.contract_status == 1 " class="item-list-status" src="/static/image/normal01.png" mode="" /> -->
            <image class="item-list-status" :src="getAbnormalText(item)" mode="">
            </image>

          </view>
        </template>
      </uni-list-item>
    </uni-list>
    <empty v-else :index="9" :title="emptyTitle"></empty>
  </view>
</template>

<script setup>
  import empty from '@/components/empty/index.vue'
  import avatar from '@/components/avatar/index.vue'
  import { ref, toRefs } from 'vue'
  import message from '@/utils/message'
  import { useStore } from 'vuex'
  const store = useStore()
  const props = defineProps({
    listData: {
      type: Array,
      default () {
        return []
      }
    },
    emptyTitle: {
      type: String,
      default: ''
    },
    type: {
      type: Number,
      default: 0
    },
    follow: {
      type: Number,
      default: 0
    },
    formType: {
      type: Object,
      default: {}
    }
  })
  const { listData, emptyTitle, type, follow, formType } = toRefs(props)
  import moment from 'moment'
  import { clickNavigateTo } from '@/utils/helper'

  import { clientContractStatusApi } from '@/api/customer'
  const indexItem = ref(-1)
  const clickFollow = (index, item) => {
    indexItem.value = index
    getStatuss(item.id, item.contract_followed ==
      0 ? 1 : 0, {
        status: item.contract_followed ==
          0 ? 1 : 0,
      })
  }
  // 取消/关注
  const getStatuss = (id, status, datas) => {
    clientContractStatusApi(id, status, datas).then(res => {
      message.error(res.message)
      listData.value[indexItem.value].contract_followed = listData.value[indexItem.value].contract_followed ===
        0 ? 1 : 0
    }).catch(error => {
      message.error(error.message)
    })
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


  const examineList = item => {
    store.commit('setCustomerFormType', formType.value)
    clickNavigateTo(`/pages/customer/contract/details?id=${item.id}`)
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

      .flex-box {
        display: flex;
        justify-content: space-between;
      }

      .uni-list-item {
        margin-bottom: 20rpx;
        border-radius: 8rpx;

        .uni-list-item__container {
          padding: 36rpx 24rpx;
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
          background-color: $uni-color-primary;
          border-radius: 8rpx;
          display: flex;
          align-items: center;
          justify-content: center;

          .iconfont {
            font-size: 52rpx;
            color: #fff;
          }
        }

        .right {
          line-height: 1.2;
          width: calc(100% - 84rpx);
          padding-left: 20rpx !important;
          height: 84rpx;
          display: flex;
          justify-content: space-between;
          flex-direction: column;

          .name {
            color: $uni-text-color;
            font-size: $uni-font-size-default;
            font-weight: $uni-default-font-weight;
          }

          .time {

            color: $nui-text-color-four;
            font-size: 24rpx;

          }
        }

      }

      .item-list-button {
        margin-top: 36rpx;
        display: flex;
        justify-content: space-between;
        padding: 32rpx 40rpx 32rpx 40rpx;
        background-color: #F2F6FC;
        border-radius: 8rpx;
        width: 100%;

        .item-bottom {
          width: 100%;

          .item-bottom-top {
            width: 100%;
            display: flex;
            justify-content: space-around;
            align-items: center;

            .name {
              display: block;
              font-size: 24rpx;
              color: $nui-text-color-two;
              margin-bottom: 20rpx;
            }

            .number {
              display: block;
              font-size: 36rpx;
              font-weight: 600;
              color: $uni-color-primary;
            }

            .number1 {
              display: block;
              font-size: 32rpx;
              font-weight: 600;
              color: #E93323;
            }
          }

          .item-bottom-item {
            margin-top: 30rpx;
            display: flex;
            align-items: center;
            font-size: 26rpx;
            color: $uni-text-color;
            margin-bottom: 30rpx;

            &:last-of-type {
              margin-bottom: 0;
            }

            .item-bottom-item-left {
              width: 34rpx;

              .iconfont {
                font-size: 34rpx;
                color: $uni-color-two;
              }
            }

            .item-bottom-item-right {

              width: calc(100% - 34rpx);
              line-height: 1.2;


              .item-right-type {

                padding-left: 20rpx !important;

              }

              .time {
                color: $nui-text-color-four;
                font-size: 24rpx;
              }
            }
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

      .item-list-bottom {
        display: flex;
        justify-content: space-between;
        padding-top: 36rpx;
        width: 100%;

        .item-bottom {
          width: 100%;

          .item-bottom-text {
            padding-left: 16rpx;
            color: $uni-text-color;
            font-size: 28rpx;
          }

          .item-bottom-right {
            font-size: 28rpx;
            color: $nui-text-color-four;

            .iconfont {
              font-size: 32rpx;
              margin-right: 4rpx;

              &.selected-color {
                color: $uni-color-two;
              }
            }
          }
        }
      }
    }
  }
</style>