<template>
  <view class="examine-content-list">
    <uni-list :border="false" v-if="listData.length > 0">
      <uni-list-item v-for="(item, index) in listData" :key="'list' + item.id">
        <template v-slot:body>
          <view class="item-list">
            <view @click="examineList(item)" class="p24">
              <view class="item-list-top">{{ item.customer_name }}</view>
              <uni-row class="item-list-content">
                <uni-col :span="6" class="left">联系电话</uni-col>
                <uni-col :span="18">{{ item.b37a3f16 || '--' }}</uni-col>
              </uni-row>
              <uni-row class="item-list-content">
                <uni-col :span="6" class="left">客户标签</uni-col>
                <uni-col :span="18">
                  <!-- {{item.customer_label.length>0}} -->
                  <view
                    class="label-text"
                    v-if="item.customer_label && item.customer_label.length > 0"
                  >
                    <text v-for="(val, id) in item.customer_label" :key="id">
                      {{ val.name }}
                      {{
                        item.customer_label.length > 1 &&
                        index < item.customer_label.length - 1
                          ? '、'
                          : ''
                      }}
                    </text>
                  </view>
                  <view class="label-text" v-else>
                    --
                  </view>
                </uni-col>
              </uni-row>
              <uni-row class="item-list-content">
                <uni-col :span="6" class="left">跟进时间</uni-col>
                <uni-col :span="18">
                  <uni-dateformat
                    v-if="item.last_follow_time"
                    format="yyyy/MM/dd hh:mm"
                    :date="item.last_follow_time"
                  ></uni-dateformat>
                  <text v-else>--</text>
                </uni-col>
              </uni-row>
            </view>
            <view class="item-list-button">
              <uni-row class="item-bottom display-align p24">
                <uni-col :span="16" class="display-align">
                  <avatar
                    :auto-size="false"
                    :width="60"
                    :height="60"
                    :src="item.salesman.avatar"
                    :radius="8"
                  ></avatar>
                  <view class="item-bottom-text">
                    {{ item.salesman.name }} 负责
                  </view>
                </uni-col>

                <uni-col
                  :span="8"
                  class="text-right item-bottom-right"
                  @click="clickFollow(index, item)"
                >
                  <text
                    class="iconfont icon-shequ-shoucang"
                    v-if="item.customer_followed != 1"
                  ></text>
                  <text
                    class="iconfont icon-shequ-shoucang-yishoucang"
                    v-else
                  ></text>
                  {{ item.customer_followed == 1 ? '已关注' : '关注' }}
                </uni-col>
              </uni-row>
            </view>
            <!-- {{customer-notyet}} -->
            <image
              v-if="item.customer_status == 0"
              class="item-list-status"
              src="/static/image/customer-notyet1.png"
              mode=""
            ></image>
            <image
              v-if="item.customer_status == 1"
              class="item-list-status"
              src="/static/image/customer-closed.png"
              mode=""
            ></image>
            <image
              v-if="item.customer_status == 2"
              class="item-list-status"
              src="/static/image/customer-lost.png"
              mode=""
            ></image>
          </view>
        </template>
      </uni-list-item>
    </uni-list>
    <empty v-else :index="7" :title="emptyTitle"></empty>
  </view>
</template>

<script setup>
import empty from '@/components/empty/index.vue'
import avatar from '@/components/avatar/index.vue'

import message from '@/utils/message'
const props = defineProps({
  listData: {
    type: Array,
    default() {
      return []
    },
  },
  typeIndex: {
    type: Number,
    default: 0,
  },
  types: {
    type: Number,
    default: 0,
  },
  emptyTitle: {
    type: String,
    default: '',
  },
})
const { listData, typeIndex, emptyTitle, types } = toRefs(props)
import { clickNavigateTo } from '@/utils/helper'
const examineList = (item) => {
  clickNavigateTo(
    `/pages/customer/list/details?id=${item.id}&&types=${types.value}`,
  )
}

import { clientStatusApi } from '@/api/customer'
const clickFollow = (index, item) => {
  clientStatusApi(item.id, item.customer_followed == 0 ? 1 : 0)
    .then((res) => {
      message.error(res.message)
      item.customer_followed = item.customer_followed == 0 ? 1 : 0
    })
    .catch((error) => {
      message.error(error.message)
    })
}
</script>

<style scoped lang="scss">
.icon-shequ-shoucang-yishoucang {
  color: #f90;
}

.p24 {
  padding-left: 24rpx;
  padding-right: 24rpx;
}

.label-text {
  width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.examine-content-list {
  ::v-deep .uni-list {
    background-color: $uni-default-bg;

    .uni-list--border {
      top: auto;
      left: auto;
    }

    .uni-list-item {
      margin-bottom: 20rpx;
      border-radius: 8rpx;

      .uni-list-item__container {
        padding: 36rpx 0rpx 0 0rpx;
      }
    }
  }

  .item-list {
    width: 100%;
    position: relative;

    .item-list-top {
      width: 100%;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
      font-size: 32rpx;
      padding-bottom: 36rpx;
      color: $uni-text-color;
      font-family: PingFang SC-中黑体, PingFang SC;
      font-weight: 500;
    }

    .item-list-content {
      font-size: 28rpx;
      color: $uni-text-color;
      font-weight: 400;
      margin-bottom: 24rpx;
      display: flex;
      align-items: flex-end;

      &:last-of-type {
        margin-bottom: 0;
      }

      .left {
        color: $nui-text-color-four;
      }
    }

    .item-list-button {
      margin-top: 36rpx;
      display: flex;
      justify-content: space-between;
      border-top: 1px solid $uni-line-style-color-three;
      padding: 26rpx 0;
      width: 100%;

      .item-bottom {
        width: 100%;

        .item-bottom-text {
          margin-bottom: 15rpx;
          padding-left: 16rpx;
          color: $uni-text-color;
          font-size: 28rpx;
        }

        .item-bottom-right {
          font-size: 28rpx;
          color: $nui-text-color-four;
          margin-bottom: 15rpx;

          .iconfont {
            font-size: 32rpx;
            margin-right: 4rpx;
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
  }
}
</style>
