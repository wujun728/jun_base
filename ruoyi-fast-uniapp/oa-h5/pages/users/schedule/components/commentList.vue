<template>
  <view>
    <view class="p_box">
      <view v-for="(item, index) in list" :key="index">
        <view class="flex">
          <view class="p_left">
            <image :src="item.from_user.avatar" alt="" class="img" />
          </view>
          <view class="p_right">
            <view class="p_name">
              <text>{{ item.from_user.name }}</text>
              <view>
                <text class="p_time">{{ item.created_at }} </text>
                <text class="iconfont icon-shanchu1" v-if="item.from_user.id == data.userId"
                  @click="commentDel(item)"></text>
              </view>

            </view>
            <view class="p_content">
              <text> {{ item.content }}</text>
              <text class="hui" @click="replyFn(item)">回复</text>
            </view>

            <!-- 二级回复 -->
            <template v-if="item.children.length !== 0">
              <view class="flex mt14" v-for="(per, v) in item.children" :key="v">
                <view class="p_left">
                  <image :src="per.from_user.avatar" alt="" class="img" />
                </view>
                <view class="p_right">
                  <view class="p_name">
                    <text>{{ per.from_user.name }}</text>
                    <view>
                      <text class="p_time">{{ item.created_at }} </text>
                      <text class="iconfont icon-shanchu1" v-if="per.from_user.id == data.userId"
                        @click="commentDel(per)"></text>
                    </view>
                  </view>
                  <view class="p_content ">
                    <view class="huiname"> <text class="p_time huiname"> 回复{{ per.to_user.name }} : </text>
                      {{ per.content }}
                    </view>
                    <view class="hui" @click="replyFn(item,per)">回复</view>
                  </view>
                </view>
              </view>
            </template>
          </view>
        </view>
        <div class="splitLine" v-if="index+1!=list.length"></div>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
  import { ref, reactive, toRefs, watch } from 'vue'
  const props = defineProps({
    list: {
      type: Array,
      default: () => {
        return []
      }
    },
  })
  const data = reactive({
    userId: JSON.parse(uni.getStorageSync('storageUserData')).userInfo.id
  })
  const { list } = toRefs(props)
  // 回复
  const replyFn = (data, row) : void => {
    emit('replyFn', data, row)
  }
  // 删除
  const commentDel = (row) : void => {
    emit('commentDel', row)
  }
  const emit = defineEmits(['replyFn', 'commentDel']);
</script>

<style lang="scss" scoped>
  .reply {
    cursor: pointer;
  }

  .mt14 {
    margin-top: 40rpx;
  }

  .splitLine {
    width: 100%;
    height: 2rpx;
    margin: 28rpx 0;
    margin-bottom: 20rpx;
    border-bottom: 2rpx solid #f7f7f7;
  }

  .flex {
    display: flex;
  }

  .p_box {
    margin-top: 20rpx;
    width: 100%;
    border-radius: 4rpx;

    .p_left {
      margin-right: 14rpx;

      .img {
        display: block;
        width: 60rpx;
        height: 60rpx;
        border-radius: 8rpx;
      }
    }

    .p_right {
      width: 100%;
      font-family: PingFang SC-Medium, PingFang SC;
      font-size: 24rpx;

      .p_name {
        width: 100%;
        display: flex;
        justify-content: space-between;
        font-size: 24rpx;
        font-family: PingFang SC-Regular, PingFang SC;
        font-weight: 400;
        color: #687383;
        margin-bottom: 8rpx;
      }

      .p_content {
        font-size: 28rpx;
        font-family: PingFang SC-Regular, PingFang SC;
        font-weight: 400;
        color: #41485B;
        display: flex;
        justify-content: space-between;

        .hui {
          display: block;
          font-size: 22rpx;
          font-family: PingFang SC-Regular, PingFang SC;
          font-weight: 400;
          color: #1890FF;
          flex-shrink: 0;
        }
      }
    }
  }

  .icon-shanchu1 {
    font-size: 24rpx;
    color: rgba(192, 196, 204, 1);
  }

  .p_time {
    font-size: 22rpx;
    font-family: PingFang SC-Regular, PingFang SC;
    font-weight: 400;
    color: #909399;
    margin-bottom: 10rpx;
    margin-right: 10rpx;
  }

  .huiname {

    font-size: 26rpx !important;
    font-family: PingFang SC-Regular, PingFang SC;
    font-weight: 400;
    // color: #909399;
  }
</style>