<template>
  <view class="examine-from-con">
    <view class="examine-from-list">
      <template v-for="(item,index) in uploadFromData" :key="'formlist'+index">
        <uni-row v-if="isClear" class="examine-from-list-item" @click="previewImage(item)">
          <uni-col :span="6" class="examine-from-left">
            <image class="image" v-if="isTypeImage(item.name)" :src="item.url" mode=""></image>
            <image class="image" v-else :src="'/static/image/cloudfile/'+isFileTypeIcon(item.name)" mode=""></image>
          </uni-col>
          <uni-col :span="18" class="examine-from-right">
            <uni-row>
              <uni-col :span="22" class="line1">
                <view class="line1">{{item.name}}</view>
                <view class="right-size" v-if="item.size">
                  <text>{{formatBytes(item.size, 2)}}</text>
                </view>
              </uni-col>

              <uni-col :span="2" class="text-right" @click.stop="changeClear(index)">
                <uni-icons class="icon-clear" type="clear"></uni-icons>
              </uni-col>
            </uni-row>
          </uni-col>
        </uni-row>
        <uni-row v-else class="examine-from-list-item" @click="previewImage(item)">
          <uni-col :span="6" class="examine-from-left">
            <image v-if="isTypeImage(item.name)" :src="item.url" mode="" class="image"></image>
            <image class="image" v-else :src="'/static/image/cloudfile/'+isFileTypeIcon(item.name)" mode=""></image>
          </uni-col>
          <uni-col :span="18" class="examine-from-right line1">
            <view class="">{{item.name}}</view>
            <view class="right-size" v-if="item.size">
              <text>{{formatBytes(item.size, 2)}}</text>
            </view>
          </uni-col>
        </uni-row>
      </template>
    </view>
  </view>
</template>

<script setup>
  import { reactive, toRefs, watch } from "vue"
  import { isTypeImage, isFileTypeIcon, fileLinkDownLoad, lookPreview } from '@/utils/helper'
  import { formatBytes } from '@/utils/file'
  import message from '@/utils/message'

  const props = defineProps({
    uploadFromData: {
      type: Array,
      default () {
        return []
      }
    },
    isClear: {
      type: Boolean,
      default: false
    }
  })
  const { uploadFromData, isClear } = toRefs(props)

  let imageUrls = reactive([])

  // 数据监听
  watch(() => uploadFromData, (newvalue, oldvalue) => {
    if (newvalue.value.length > 0) {
      newvalue.value.forEach(value => {
        if (isTypeImage(value.name)) {
          imageUrls.push(value.url)
        }
      })
    }
  }, { immediate: true, deep: true });

  // 预览图片下载文件
  const previewImage = (item) => {
    lookPreview(item.url, item.name, imageUrls)
  }

  let emit = defineEmits(['handleClear'])
  const changeClear = (index) => {
    emit('handleClear', index)
  }
</script>

<style scoped lang="scss">
  .examine-from-con {
    width: 100%;

    .examine-from-list {
      .examine-from-list-item {
        font-size: 28rpx;
        height: 80rpx;
        display: flex;
        align-items: center;
        background-color: #F6F7F9;
        margin-bottom: 12rpx;
        padding: 0 14rpx;
        border-radius: 8rpx;
        position: relative;

        .icon-clear {
          color: #C0C4CC !important;
          font-size: 24rpx;
        }

        &:last-of-type {
          margin-bottom: 0;
        }

        .examine-from-left {
          color: $nui-text-color-four;
          width: 50rpx;
          height: 50rpx;

          .image {
            width: 50rpx;
            height: 50rpx;
            border-radius: 2rpx;
          }
        }

        .examine-from-right {
          width: calc(100% - 50rpx);
          color: $uni-text-color;
          font-size: 24rpx;
          line-height: 24rpx;
          padding-left: 16rpx !important;

          .right-size {
            color: $nui-text-color-four;
            padding-top: 10rpx;

            uni-text {
              font-size: 20rpx;
              transform: scale(0.63);
            }
          }
        }
      }
    }
  }
</style>