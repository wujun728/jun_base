<template>
  <view class="comment-box">
    <view class="title">
      {{info.comment_title||'评论'}}
    </view>
    <view class="content">
      <uni-list :border="false">
        <uni-list-item v-for="(item,index) in list" :key="item.id" :border="false">
          <!-- 自定义 header -->
          <template v-slot:header>
            <view class="item-list-left">
              <avatar :src="item.user.avatar" :radius="8"></avatar>
            </view>
          </template>
          <!-- 自定义 body -->
          <template v-slot:body>
            <view class="item-list-right" :class="item.reply.length==0?'bottom-boder':'pb0'">
              <uni-row class="right-top">
                <uni-col :span="12"><text class="name">{{item.user.name}}</text>
                  <text class="time"> {{formatDate(item.created_at, true)}}</text></uni-col>
                <uni-col :span="12" class="text-right">
                  <view v-if="userInfo.id == item.user.id">
                    <text @click="deleteReply(item)">删除</text>
                  </view>
                  <view v-else>
                    <text @click="examineReply(item)">回复</text>
                  </view>
                </uni-col>
              </uni-row>
              <uni-row class="right-info">
                <uni-col :span="24">
                  <view v-html="item.comment"></view>
                </uni-col>
                <!-- 二级评论 -->
                <view v-for="(val,index) in item.reply" :key="val.id">
                  <view class="reply-item">
                    <image :src="val.user.avatar" class="img"></image>
                    <view class="reply-right">
                      <view class="flex-between">
                        <view>
                          <text class="name">{{val.user.name}}</text>
                          <text class="time"> {{formatDate(val.created_at, true)}}</text>
                        </view>
                        <view class="text-right" v-if="userInfo.id == val.user.id" @click="deleteReply(val)">删除</view>
                        <view class="text-right" v-else @click="examineReply(val)">回复</view>
                      </view>
                      <view class="conetent" v-html="val.comment"></view>
                    </view>
                  </view>
                </view>
              </uni-row>
            </view>
          </template>
        </uni-list-item>
      </uni-list>
    </view>

    <!-- 评论 -->
    <view class="replay">
      <uni-row class="display-align" v-if="data.examineReplyBtn">
        <uni-col :span="16" class="replay-left">
          <textarea maxlength="50" auto-height :focus="data.examineReplyBtn" @confirm="changeInput"
            v-model="data.content" placeholder="您可以发表评论哟～" />
        </uni-col>
        <uni-col :span="8" class="replay-right text-right">
          <text class="iconfont icon-liuyan-fasong" :style="{color: data.content ? '#1890FF' : '#E4E7ED'}"
            @click="clickReplay"></text>
        </uni-col>
      </uni-row>
      <view v-else class="examine-bottom-reply" @click="data.examineReplyBtn=true">
        <image class="reply-image" src="/static/image/cloudfile/leave-icon.png" mode=""></image>
        <view class="name"> {{info.comment_title||'评论'}} </view>
      </view>
    </view>
  </view>
</template>

<script setup>
  import { reactive, computed } from "vue"
  import { formatDate } from '@/utils/schedule'
  import avatar from '@/components/avatar/index.vue'
  const props = defineProps({
    list: {
      type: Array,
      default: [],
    },
    info: {
      type: Object,
      default: {},

    }
  })
  import { useStore } from 'vuex'
  const store = useStore();
  const userInfo = computed(() => store.state.app.userInfo)
  const { list } = toRefs(props)
  const data = reactive({
    itemData: {},
    content: '',
    examineReplyBtn: false
  })

  // 点击留言
  const examineReply = (item) => {
    data.itemData = item
    data.examineReplyBtn = true
  }
  let emit = defineEmits(['saveReplay', 'deleteReplyFn'])
  const clickReplay = () => {
    emit('saveReplay', data.itemData, data.content)
    changeInput()
  }
  const deleteReply = (item) => {
    data.itemData = item
    emit('deleteReplyFn', data.itemData)
  }

  const changeInput = () => {
    data.itemData = {}
    data.content = ''
    data.examineReplyBtn = false
  }
</script>
<style lang="scss">
  .comment-box {
    position: relative;
    margin: 10px 0;
    border-radius: 12rpx;
    padding: 30rpx 24rpx;
    // margin-bottom: 80px;
    font-family: PingFang SC, PingFang SC;
    background-color: #fff;
    font-family: PingFang SC, PingFang SC;


    .title {
      font-weight: 500;
      font-size: 30rpx;
      color: #2B2C32;
    }
  }

  ::v-deep.uni-list-item__container {
    padding: 0 !important;
    padding-top: 15px !important;
  }

  .conetent {
    font-family: PingFang SC, PingFang SC;
    font-weight: 400;
    font-size: 14px;
    color: #333333;
  }

  .examine-bottom-reply {
    padding-left: 10px;
    line-height: 1;
    text-align: center;

    .reply-image {
      width: 36rpx;
      height: 34rpx;
    }

    .name {
      font-size: 24rpx;
      font-weight: 400;
      color: $nui-text-color-four;
    }
  }

  .reply-item {
    padding-top: 30rpx;
    padding-bottom: 20rpx;
    width: 100%;
    display: flex;
    border-bottom: 1px solid #EBEEF5;

    .reply-right {
      width: 100%;
    }

    .flex-between {
      width: 100%;
      display: flex;
      justify-content: space-between;
      margin-bottom: 10rpx;
    }
  }

  .content {
    // margin-bottom: 60px;
  }

  .item-list-left {
    width: 60rpx;
    height: 60rpx;
  }

  .text-right {
    font-weight: 400;
    font-size: 11px;
    color: #909399;
  }

  .img {
    width: 40rpx;
    height: 40rpx;
    border-radius: 4rpx;
    margin-right: 10rpx;
  }

  .name {
    font-weight: 400;
    font-size: 24rpx;
    color: #606266;
  }

  .time {
    font-weight: 400;
    font-size: 11px;
    color: #909399;
    margin-left: 12rpx;
  }


  .item-list-right {
    width: calc(100% - 60rpx);
    padding-left: 14rpx;
    padding-bottom: 20rpx;

    .right-top {
      font-size: 24rpx;
      color: #606266;
    }

    .right-info {
      padding-top: 8px;
      font-size: 28rpx;
      color: #41485B;

      .right-info-text {
        font-size: 32rpx;
        color: #C0C4CC;
      }
    }
  }

  .bottom-boder {
    border-bottom: 1px solid #EBEEF5;
  }

  .replay {
    box-shadow: 0px 0px 6px 0px rgba(0, 0, 0, 0.06);
    width: 100%;
    position: fixed;
    left: 0;
    bottom: 0;
    display: flex;
    align-items: center;
    min-height: 108rpx;
    background-color: #fff;
    font-size: 28rpx;

    ::v-deep .uni-row {
      width: 100%;
      padding: 0 20rpx;

      .uni-input-placeholder {
        font-size: 28rpx;
        color: #C0C4CC;
      }
    }

    .replay-left {
      width: calc(100% - 60rpx);

      uni-textarea {
        width: 100%;
      }
    }

    .replay-right {
      width: 60rpx;

      .iconfont {
        color: #E4E7ED;
        font-size: 40rpx;
      }
    }
  }

  .pb0 {
    padding-bottom: 0;
  }
</style>