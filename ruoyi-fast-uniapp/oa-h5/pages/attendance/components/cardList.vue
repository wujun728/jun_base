<template>
  <view class="nav">
    <view class="card">
      <view class="header">
        <view class="title">{{ title }}</view>
        <view class="jump" v-if="isJump">
          查看明细
          <text class="iconfont icon-jinru-copy"></text>
        </view>
      </view>
      <view class="schedule" v-if="list.length">
        <view class="item" v-for="(item, index) in list" :key="index">
          <view class="user">
            <image :src="item.card.avatar" mode=""></image>
            <view class="user-msg">
              <view class="name">{{ item.card.name }}</view>
              <view class="title">
                <text>{{ item.card.frame.name }}</text>
                <text v-if="item.card.job"> ({{ item.card.job.name }})</text>
              </view>
            </view>
          </view>
          <view class="msg">
            <text
              :class="{ err: ex.location_status != 1 }"
              v-for="(ex, i) in item.external"
              :key="i"
              >{{ ex.type == 1 ? "上班" : "下班"
              }}{{ ex.location_status == 1 ? "正常" : "异常"
              }}{{ i / 2 == 0 ? "，" : "" }}</text
            >
          </view>
        </view>
      </view>
	  <view v-else class="default">
	  	<image src="../../../static/image/empty.png" mode=""></image>
	  	<view class="text">暂无外勤卡统计数据～</view>
	  </view>
    </view>
  </view>
</template>

<script setup>
import { ref, reactive, toRefs, onMounted } from "vue";
const props = defineProps({
  list: {
    type: Array,
    default: () => {
      [];
    },
  },
  title: {
    type: String,
    default: "",
  },
  isJump: {
    type: Boolean,
    default: false,
  },
});
const { list, title } = toRefs(props);
onMounted(() => {});
function typeText(type) {
  let name;
  switch (type) {
    case 0:
      name = "上班外勤，下班外勤";
      break;
    case 1:
      name = "下班外勤";
      break;
  }
  return name;
}
</script>

<style lang="scss" scoped>
.nav {
  padding-bottom: 120rpx;
}

.card {
  margin: 20rpx 20rpx 0rpx;
  background-color: #fff;
  border-radius: 12rpx;

  .header {
    display: flex;
    justify-content: space-between;
    padding: 30rpx 24rpx;
    border-bottom: 1rpx solid #ebeef5;

    .title {
      font-size: 30rpx;
      font-weight: 500;
      color: #303133;
      line-height: 30rpx;
    }

    .jump {
      display: flex;
      align-items: center;
      font-size: 24rpx;
      font-family: PingFang SC-Regular, PingFang SC;
      font-weight: 400;
      color: #308bf8;
      line-height: 24rpx;

      .icon-jinru-copy {
        font-size: 20rpx;
        color: #c0c4cc;
        margin-left: 12rpx;
      }
    }
  }

  .schedule {
    .item:last-child {
      border-bottom: none;
    }
    .item {
      margin-left: 24rpx;
      display: flex;
      justify-content: space-between;
      align-items: center;
      border-bottom: 1rpx solid #f0f1f5;
      padding: 30rpx 28rpx 30rpx 0;
      .user {
        display: flex;

        image {
          width: 80rpx;
          height: 80rpx;
          border-radius: 8rpx;
          margin-right: 20rpx;
        }

        .user-msg {
          display: flex;
          flex-direction: column;
          justify-content: space-between;

          .name {
            font-size: 30rpx;
            font-weight: 400;
            color: #303133;
            line-height: 30rpx;
          }

          .title {
            font-size: 24rpx;
            font-weight: 400;
            color: #909399;
            line-height: 24rpx;
          }
        }
      }

      .msg {
        font-size: 28rpx;
        font-weight: 400;
        color: #606266;
        line-height: 36rpx;
        width: 9em;
        .err{
          color: #ED4014;
        }
      }
    }
  }
}
	.default {
		width: 100%;
		padding: 80rpx 0;
		
		image {
			width: 400rpx;
			height: 300rpx;
			margin: auto;
			display: block;
		}
		.text {
			font-size: 26rpx;
			font-family: PingFang SC-Regular, PingFang SC;
			font-weight: 400;
			color: #909399;
			width: 100%;
			text-align: center;
			margin-top: 28rpx;
		}
	}
</style>
