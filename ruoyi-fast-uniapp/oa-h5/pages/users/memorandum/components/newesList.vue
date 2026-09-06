<template>
  <view class="report-contents">
    <uni-list :border="false" v-if="listData.length">
      <uni-list-item class="list-item-01" v-for="(item, index) in listData" :key="index">
        <!-- 自定义 header -->
        <template v-slot:header>
          <view class="time-con">{{item.month}}</view>
        </template>
        <!-- 自定义 body -->
        <template v-slot:body>
          <uni-list :border="false" class="uni-list-02">
            <uni-list-item v-for="items in item.data" :key="items.id"
              :to="'/pages/users/memorandum/details?tab=newes&pid=&id='+items.id">
              <!-- 自定义 body -->
              <template v-slot:body>
                <view class="item-list-right">
                  <view class="right-top line1">{{items.title}}</view>
                  <view class="right-info line2">{{items.content}}</view>
                  <view class="right-time">
                    编辑于 <uni-dateformat format="yyyy/MM/dd hh:mm" :date="items.updated_at"></uni-dateformat>
                  </view>
                </view>
              </template>
            </uni-list-item>
          </uni-list>
        </template>
      </uni-list-item>
    </uni-list>
    <empty v-else :index="6" :title="emptyTitle"></empty>
  </view>
</template>

<script setup>
  import { ref, reactive, toRefs } from "vue"
  import empty from '@/components/empty/index'
  import avatar from '@/components/avatar/index.vue'
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
  })
  const { listData, emptyTitle } = toRefs(props)
</script>

<style scoped lang="scss">
  .report-contents {
    ::v-deep .uni-list {
      background-color: $uni-default-bg;

      .uni-list-item {
        border-radius: 16rpx;
        background-color: $uni-default-bg;
      }
    }

    .list-item-01 {
      border-radius: 16rpx;

      ::v-deep .uni-list-item__container {
        display: block;
        padding: 0;
        padding-left: 0;
      }

      ::v-deep .uni-list--border {
        left: auto;
        top: auto;
      }

      .time-con {
        width: 100%;
        font-size: 24rpx;
        color: $nui-text-color-four;
        padding: 40rpx 0 20rpx 0;
        background-color: #f5f5f5;
        display: block;
      }

      .uni-list-02 {
        // border-radius: 16rpx;

        .uni-list-item {
          background-color: #fff;
          margin-bottom: 20rpx;
          // border-radius: 16rpx;

          &:last-of-type {
            margin-bottom: 0;
          }
        }

        ::v-deep .uni-list-item__container {
          display: flex;
          padding: 30rpx 24rpx;
          border-radius: 16rpx;
        }
      }
    }

    .item-list-right {
      width: 100%;

      .right-top {
        padding-bottom: 16rpx;
        font-size: $uni-font-size-default;
        color: $uni-text-color;

      }

      .right-time {
        font-size: 24rpx;
        color: $uni-text-color-five;
      }

      .right-info {
        font-size: 26rpx;
        color: $nui-text-color-two;
        margin-bottom: 16rpx;
        line-height: 1.5;
      }
    }
  }
</style>