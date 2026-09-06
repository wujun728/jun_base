<template>
  <view class="customer-tab-info">
    <view class="info-item" v-for="(item,index) in customerData" :key="index">
      <uni-row v-for="(val,index1) in item.data" :key="index1">
        <uni-col :span="5" class="text-right info-item-left">{{val.key_name}}</uni-col>

        <uni-col :span="19" v-if="val.type == 'oaWangeditor'" class="info-item-right"><text
            v-html="val.value"></text></uni-col>
        <uni-col :span="19" class="info-item-right" v-else-if="val.type=='file'">
          <view class="file" v-if="val.files.length>0">
            <view class="item" v-for="(fileItem,index) in val.files" :key="index" @click="preview(fileItem)">
              <view class="left">
                <image class="slot-image" :src="`/static/image/cloudfile/${isFileTypeIcon(fileItem.real_name)}`"
                  mode="widthFix">
                </image>
                <view>
                  <view class="name">
                    {{fileItem.real_name }}
                  </view>
                  <view class="size">
                    {{formatBytes(fileItem.att_size) ||'--'}}
                  </view>
                </view>
              </view>
            </view>
          </view>
          <view v-else>--</view>
        </uni-col>

        <uni-col :span="19" v-else-if="val.type=='images'" class="info-item-right">
          <view class="images" v-if="val.files.length>0">
            <img class="img" :src="imgItem.att_dir" alt="" v-for="(imgItem,index) in val.files" :key="index"
              @click="preview(imgItem)">
          </view>
          <view v-else>--</view>
        </uni-col>
        <uni-col :span="19" v-else class="info-item-right">{{cityFn(val.value)}}</uni-col>
      </uni-row>
    </view>
  </view>
</template>

<script setup>
  import { formatBytes } from '@/utils/file'
  import { isFileTypeIcon, lookPreview, isTypeImage } from '@/utils/helper'
  import { reactive, toRefs } from 'vue'
  const props = defineProps({
    customerData: {
      type: Array,
      default: () => {
        return []
      }
    }
  })
  // 图片与文档预览
  const preview = item => {
    lookPreview(item.att_dir, item.real_name, [item.att_dir])
  }
  const { customerData } = toRefs(props)
  const cityFn = val => {
    let str = ''
    if (val == '') {
      str = '--'
    } else if (Array.isArray(val)) {
      str = val.toString()
    } else {
      str = val
    }
    return str
  }
</script>

<style scoped lang="scss">
  .label-text {
    width: 100%;
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
  }

  .customer-tab-info {
    padding-top: 50rpx;

    .info-item {
      font-size: $uni-font-size-default;
      color: $uni-text-color;
      margin-bottom: 20rpx;

      &:last-of-type {
        margin-right: 0;
      }

      .info-item-left {
        color: $nui-text-color-four;
      }

      .info-item-right {
        padding-left: 40rpx !important;
        line-height: 1.5;
      }
    }
  }

  .file {
    .item {
      display: flex;
      padding: 14rpx;
      width: 100%;
      height: 78rpx;
      background: #F6F7F9;
      border-radius: 8rpx;
      margin-bottom: 12rpx;

      .slot-image {
        width: 52rpx;
        height: 52rpx;
        margin-right: 10rpx;
      }

      .left {
        display: flex;
        font-family: PingFang SC, PingFang SC;
        font-weight: 400;

        .name {
          width: 400rpx;
          overflow: hidden;
          white-space: nowrap;
          text-overflow: ellipsis;
          font-size: 24rpx;
          color: #303133;
        }

        .size {

          font-size: 20rpx;
          color: #909399;
          margin-top: 2rpx;
        }
      }
    }
  }

  .images {
    .img {
      width: 82rpx;
      height: 82rpx;
      border-radius: 8rpx;
      margin-right: 16rpx;
    }
  }
</style>