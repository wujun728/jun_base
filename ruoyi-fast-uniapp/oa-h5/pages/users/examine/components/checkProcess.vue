<template>
  <view class="uni-steps">
    <view class="uni-steps-title">审批流程</view>
    <view class="uni-steps__column">
      <view class="uni-steps__column-text-container">
        <view class="uni-steps-container" v-for="(item,index) in options" :key="index">
          <view class="uni-steps__column-container">
            <view class="uni-steps__column-line-item">
              <view class="uni-steps__column-line uni-steps__column-line--before"
                :style="{backgroundColor:index<=active&&index!==0?activeColor:index===0?'transparent':deactiveColor}">
              </view>
              <view class="uni-steps__column-check">
                <text :color="activeColor" class="iconfont"
                  :class="item.types === 1 ? 'icon-shenpixiangqing-shenpi' : 'icon-shenpixiangqing-chaosong'"></text>
              </view>
              <view class="uni-steps__column-line uni-steps__column-line--after"
                :style="{backgroundColor:index<active&&index!==options.length-1?activeColor:index===options.length-1?'transparent':deactiveColor}">
              </view>
            </view>
          </view>

          <view class="uni-steps__column-text">
            <view class="uni-steps__column-title">
              {{item.title}}
              <uni-tag v-if="item.types == 1 && item.settype != 5 && item.examine_mode > 0 && item.users.length > 1"
                class="uni-steps-tag" :inverted="true" :text="getExamineText(item.examine_mode)" type="primary" />
            </view>
            <view class="uni-steps__column-user">
              <view v-for="(items, indexs) in item.users" :key="'user'+indexs">
                <uni-row class="display-align">
                  <span class="iconfont icon-zhuanshenzhixiang" v-if="items.is_transfer > 1"></span>
                  <uni-col :span="10" class="uni-steps__column-user-left">

                    <avatar :src="items.card ? items.card.avatar : items.info.card.avatar" :radius="8"
                      :auto-size="false" :width="70" :height="70"></avatar>
                    <view class="name line1">{{items.card ? items.card.name : items.info.card.name}} </view>
                    <image v-if="items.status === 1 && item.types!==2 && items.is_sign != 1" class="iconfont-img"
                      src="/static/image/cloudfile/passed-status.png">
                    </image>
                    <!-- 加签 -->
                    <view v-if="items.is_sign == 1" class="acea-icon acea-sign">
                      <text class="iconfont icon-jiaqiantubiao"></text>
                    </view>
                    <!-- 转审 -->
                    <view v-if="items.is_transfer !== 2 && items.is_transfer" class="acea-icon acea-trans">
                      <text class="iconfont icon-zhuanshentubiao"></text>
                    </view>

                    <image v-if="items.status === 2 && item.types!==2" class="iconfont-img"
                      src="/static/image/cloudfile/refuse-status.png">
                    </image>
                  </uni-col>
                  <uni-col :span="14" class="uni-steps__column-user-right">
                    <view>
                      {{getDetailText(item.types, items.status,items)}}
                      ·
                      <uni-dateformat v-if="items.status > 0 || items.is_transfer === 3 || items.is_transfer === 1"
                        format="MM/dd hh:mm" :date="items.updated_at"></uni-dateformat>
                    </view>
                  </uni-col>
                </uni-row>

                <view class="sign-bag flex" v-if="items.content">
                  <text class="name">{{ items.is_transfer ? '转审' : '加签' }}意见：</text>{{ items.content }}
                </view>
              </view>
            </view>
          </view>
        </view>

      </view>
    </view>
  </view>
</template>

<script setup>
  import { reactive, toRefs } from "vue"
  import avatar from '@/components/avatar/index.vue'

  const props = defineProps({
    activeColor: {
      // 激活状态颜色
      type: String,
      default: '#2979FF'
    },
    deactiveColor: {
      // 未激活状态颜色
      type: String,
      default: 'rgba(48, 139, 248, 0.3)'
    },
    active: {
      // 当前步骤
      type: Number,
      default: 0
    },
    activeIcon: {
      // 当前步骤
      type: String,
      default: 'checkbox-filled'
    },
    options: {
      type: Array,
      default () {
        return []
      }
    }
  })
  const { activeColor, deactiveColor, active, activeIcon, options } = toRefs(props)

  const getExamineText = (id) => {
    let str = ''
    if (id == 1) {
      str = '或签'
    } else if (id == 2) {
      str = '会签'
    } else if (id == 3) {
      str = '依次审批'
    }
    return str
  }

  const getDetailText = (type, status, item) => {
    let str = '';
    if (type == 1 && status == 1 && item.is_sign != 1) {
      str = '已同意 ';
    } else if (type == 1 && status == 2) {
      str = '已拒绝 ';
    } else if (type == 2 && status > 0) {
      str = '已抄送 ';
    } else if (item.is_sign == 1) {
      str = '已加签 '
    } else if (item.is_transfer === 1 || item.is_transfer === 3) {
      str = '已转审 '
    }
    return str;
  }
</script>

<style scoped lang="scss">
  $uni-primary: #2979ff !default;
  $uni-border-color: #EDEDED;

  .icon-zhuanshenzhixiang {
    font-size: 28rpx;
    color: #C0C4CC;
    margin-right: 20rpx;


  }

  .uni-steps {
    /* #ifndef APP-NVUE */
    display: flex;
    width: 100%;
    /* #endif */
    /* #ifdef APP-NVUE */
    flex: 1;
    /* #endif */
    flex-direction: column;
    padding: 36rpx 30rpx;
    border-radius: 16rpx;
  }



  .uni-steps-title {
    font-size: $uni-font-size-default;
    font-weight: $uni-default-font-weight;
    padding-bottom: 30rpx;
  }

  .uni-steps__row {
    /* #ifndef APP-NVUE */
    display: flex;
    /* #endif */
    flex-direction: column;
  }

  .uni-steps__column {
    /* #ifndef APP-NVUE */
    display: flex;
    /* #endif */
    flex-direction: row-reverse;
  }

  .uni-steps__row-text-container {
    /* #ifndef APP-NVUE */
    display: flex;
    /* #endif */
    flex-direction: row;
    align-items: flex-end;
    margin-bottom: 8px;
  }

  .uni-steps__column-text-container {
    /* #ifndef APP-NVUE */
    display: flex;
    /* #endif */
    flex-direction: column;
    flex: 1;
  }

  .uni-steps__row-text {
    /* #ifndef APP-NVUE */
    display: inline-flex;
    /* #endif */
    flex: 1;
    flex-direction: column;
  }

  .uni-steps-container {
    display: flex;
  }

  .uni-steps__column-text {
    padding-top: 6px;
    padding-bottom: 30rpx;
    width: calc(100% - 30px);
    display: flex;
    flex-direction: column;
  }

  .uni-steps__row-title {
    font-size: 14px;
    line-height: 16px;
    text-align: center;
  }

  .uni-steps__column-title {
    font-size: 28rpx;
    font-weight: 400;
    color: $nui-text-color-two;
    padding-left: 20rpx;
    padding-top: 10rpx;
    text-align: left;
    line-height: 28rpx;

    .uni-steps-tag {
      padding: 0 4rpx;
      font-size: 24rpx;
      font-weight: 400;
      margin-left: 16rpx;
    }
  }

  .uni-steps__column-user {
    padding-top: 40rpx;
    padding-left: 20rpx;

    ::v-deep .uni-row {
      margin-bottom: 32rpx;

      &:last-of-type {
        // margin-bottom: 0;
      }
    }

    .sign-bag {
      margin-top: 10px;
      margin-bottom: 32rpx;
      padding: 24rpx;
      background: #f5f5f5;
      color: #606266;
      font-family: Source Han Sans, Source Han Sans;
      font-weight: 400;
      font-size: 13px;
      border-radius: 8rpx;

      .name {
        color: #909399;
      }
    }

    .uni-steps__column-user-left {
      height: 70rpx;
      position: relative;
      display: flex;
      align-items: center;
      font-size: 26rpx;
      font-weight: 400;
      color: #41485B;

      .image {
        width: 70rpx;
        height: 70rpx;
        border-radius: 8rpx;
      }

      .name {
        padding-left: 24rpx;
        width: calc(100% - 70rpx);
      }

      .iconfont-img {
        width: 28rpx;
        height: 28rpx;
        color: #308BF8;
        position: absolute;
        top: -10rpx;
        left: 55rpx;
        border-radius: 50%;
        background-color: #fff;
      }
    }

    .uni-steps__column-user-right {
      text-align: right;
      font-size: 24rpx;
      font-weight: 400;
      color: $nui-text-color-four;
    }
  }

  .uni-steps__row-desc {
    font-size: 12px;
    line-height: 14px;
    text-align: center;
  }

  .uni-steps__column-desc {
    font-size: 12px;
    text-align: left;
    line-height: 18px;
  }

  .uni-steps__row-container {
    /* #ifndef APP-NVUE */
    display: flex;
    /* #endif */
    flex-direction: row;
  }

  .uni-steps__column-container {
    /* #ifndef APP-NVUE */
    display: inline-flex;
    /* #endif */
    width: 30px;
    flex-direction: column;
  }

  .uni-steps__row-line-item {
    /* #ifndef APP-NVUE */
    display: inline-flex;
    /* #endif */
    flex-direction: row;
    flex: 1;
    height: 14px;
    line-height: 14px;
    align-items: center;
    justify-content: center;
  }

  .uni-steps__column-line-item {
    /* #ifndef APP-NVUE */
    display: flex;
    /* #endif */
    flex-direction: column;
    flex: 1;
    align-items: center;
    justify-content: center;
  }

  .uni-steps__row-line {
    flex: 1;
    height: 1px;
    background-color: #B7BDC6;
  }

  .uni-steps__column-line {
    width: 2px;
    background-color: #B7BDC6;
  }

  .uni-steps__row-line--after {
    transform: translateX(1px);
  }

  .uni-steps__column-line--after {
    flex: 1;
    transform: translate(0px, 1px);
  }

  .uni-steps__row-line--before {
    transform: translateX(-1px);
  }

  .uni-steps__column-line--before {
    height: 6px;
  }

  .uni-steps__row-circle {
    width: 5px;
    height: 5px;
    border-radius: 50%;
    background-color: #B7BDC6;
    margin: 0px 3px;
  }

  .uni-steps__column-circle {
    width: 5px;
    height: 5px;
    border-radius: 50%;
    background-color: #B7BDC6;
    margin: 4px 0px 5px 0px;
  }

  .uni-steps__row-check {
    margin: 0px 6px;
  }

  .uni-steps__column-check {
    height: 48rpx;
    width: 48rpx;
    line-height: 48rpx;
    background-color: rgba(48, 139, 248, 0.1);
    text-align: center;
    border-radius: 50%;

    .iconfont {
      font-size: 30rpx;
      color: $uni-color-primary;
    }
  }

  .acea-icon {
    width: 28rpx;
    height: 28rpx;
    border-radius: 50%;
    position: absolute;
    top: -10rpx;
    left: 55rpx;
    display: flex;
    justify-content: center;
    align-items: center;

    .iconfont {
      font-size: 11px;
      color: #fff
    }
  }

  .acea-sign {
    background: #f6bb19;


  }

  .acea-trans {
    background: #cccccc;

    .iconzhuanshentubiao {
      font-size: 10px;
      margin-left: 2px;
    }
  }

  .acea-success {
    background: #1890ff;

    .el-icon-check {
      font-size: 10px;
      margin-left: 2px;
    }
  }
</style>