<template>
  <view class="report-contents">
    <uni-list :border="false" v-if="listData.length">
      <template v-for="(item, index) in listData">
        <uni-list-item class="list-item-01" v-for="items in item.data" :key="'list' + items.daily_id">
          <!-- 自定义 header -->
          <template v-slot:header>
            <view class="time-con">{{ formatDate(items.created_at, true) }}</view>
          </template>
          <!-- 自定义 body -->
          <template v-slot:body>
            <uni-list :border="false" class="uni-list-02">
              <uni-list-item :to="'/pages/users/report/mine?type=' + items.types + '&id=' + items.daily_id">
                <!-- 自定义 header -->
                <template v-slot:header>
                  <view class="flex">
                    <view class="item-list-left">
                      <avatar :src="items.avatar" :radius="8"></avatar>
                    </view>
                    <view class="right-title">{{ items ? items.name : '--' }} -
                      {{ getReportType(items.types) }}
                      <view class="right-time">
                        <uni-dateformat format="hh:mm" :date="items.created_at"></uni-dateformat>
                      </view>
                    </view>
                  </view>
                </template>
                <!-- 自定义 body -->
                <template v-slot:body>
                  <view class="item-list-right">
                    <view class="right-info">
                      <uni-row>
                        <uni-col :span="5">
                          <text class="lable-text" v-if="items.types !== 3">
                            {{ getReportText(items.finish.types)[0] }}：</text>
                          <text class="lable-text" v-else>汇报总结：</text>
                        </uni-col>
                        <uni-col :span="19">
                          <view class="over-text">{{ items.finish ? items.finish.join(',') : '--' }}</view>
                          <!-- <view class="mb4" v-for="(item, index) in items.finish">{{ item }}</view> -->
                        </uni-col>
                      </uni-row>

                      <!-- <text>{{ items.finish ? items.finish.join(',') : '--' }}</text> -->
                    </view>

                    <view class="right-info mt6" v-if="items.types != 3">
                      <uni-row>
                        <uni-col :span="5" class="lable-text"> {{ getReportText(items.finish.types)[1] }}： </uni-col>
                        <uni-col :span="19">
                          <view class="over-text">{{ items.plan ? items.plan.join(',') : '--' }}</view>
                          <!-- <view class="mb4" v-for="(val, indexj) in items.plan" :key="indexj">{{ val }}</view> -->
                        </uni-col>
                      </uni-row>
                    </view>
                  </view>
                </template>
              </uni-list-item>
            </uni-list>
          </template>
        </uni-list-item>
      </template>
    </uni-list>
    <empty v-else :index="2" :title="emptyTitle"></empty>
  </view>
</template>

<script setup>
  import { ref, reactive, toRefs } from 'vue'
  import empty from '@/components/empty/index'
  import { formatDate } from '@/utils/schedule'
  import avatar from '@/components/avatar/index.vue'
  const props = defineProps({
    listData: {
      type: Array,
      default () {
        return []
      },
    },
    emptyTitle: {
      type: String,
      default: '',
    },
  })
  const { listData, emptyTitle } = toRefs(props)
  const getReportType = (type) => {
    let str = ''
    if (type === 1) {
      str = '周报'
    } else if (type === 2) {
      str = '月报'
    } else if (type == 3) {
      str = '汇报'
    } else {
      str = '日报'
    }
    return str
  }

  const getReportText = (type) => {
    let str = []
    if (type === 1) {
      str = ['本周总结', '下周计划']
    } else if (type === 2) {
      str = ['本月总结', '下月计划']
    } else {
      str = ['今日总结', '明日计划']
    }
    return str
  }
</script>

<style scoped lang="scss">
  .report-contents {
    .flex {
      display: flex;
    }

    .right-title {
      font-size: 30rpx;
      font-weight: 500;
      color: $uni-text-color;
    }

    .right-time {
      font-family: PingFang SC, PingFang SC;
      font-weight: 400;
      font-size: 24rpx;
      color: #c0c4cc;
      margin-top: 8rpx;
    }

    .item-list-left {
      width: 70rpx;
      height: 70rpx;
      margin-right: 20rpx;
      margin-bottom: 20rpx;
    }

    .lable-text {
      font-family: PingFang SC, PingFang SC;
      font-weight: 400;
      font-size: 26rpx;
      color: #606266;
    }

    .mt6 {
      margin-top: 12rpx;
    }

    .list-item-01 {
      ::v-deep .uni-list-item__container {
        display: block;
        padding: 0;
        padding-left: 0;
        // border-radius: 16rpx;
      }

      ::v-deep .uni-list--border {
        left: auto;
        top: auto;
      }

      .time-con {
        width: 100%;
        font-size: 24rpx;
        color: $nui-text-color-four;
        padding: 20rpx 0;
        display: block;
        background-color: $uni-default-bg;
      }

      .uni-list-02 {
        background-color: #f5f5f5;
        // border-radius: 16rpx;

        .uni-list-item {
          border-bottom: 1px solid #f0f1f5;
          border-radius: 16rpx;
          margin-bottom: 20rpx;

          &:first-of-type {
            // border-radius: 16rpx 16rpx 0 0;
          }

          &:last-of-type {
            border-bottom: none;
          }
        }

        ::v-deep .uni-list-item__container {
          padding: 32rpx 24rpx;
          // border-radius: 16rpx;
        }
      }
    }

    .item-list-right {
      .right-info {
        font-size: 26rpx;
        color: $nui-text-color-four;
        // padding-bottom: 8rpx;

        &:last-of-type {
          padding-bottom: 0;
        }
      }
    }
  }
</style>