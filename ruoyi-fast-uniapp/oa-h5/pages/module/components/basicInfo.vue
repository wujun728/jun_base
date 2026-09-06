<template>
  <view class="box">
    <view class="title">基本信息</view>
    <uni-row class="list-info" v-for="(val, valIndex) in list" :key="valIndex">
      <uni-col :span="6" class="text-right info-item-left">
        {{ val.title }}
      </uni-col>
      <uni-col :span="18" class="info-item-right">
        <view v-if="val.form_value === 'image'" class="imgFlex">
          <template v-if="val.value && val.value.length > 0">
            <img class="img" :src="imgItem.url" alt="" v-for="(imgItem, index) in val.value" :key="index"
              @click="preview(imgItem)" />
          </template>
          <view v-else>--</view>
        </view>
        <view v-else-if="val.form_value === 'file'">
          <view class="file" v-if="val.value && val.value.length > 0">
            <view class="item" v-for="(fileItem, index) in val.value" :key="fileItem.id" @click="preview(fileItem)">
              <div class="left-view">
                <image v-if="!isTypeImage(fileItem.name)" class="img"
                  :src="`/static/image/cloudfile/${isFileTypeIcon(fileItem.name)}`" mode="widthFix">
                </image>
                <image v-else class="img" :src="fileItem.url"> </image>
              </div>
              <div class="right-view over-text name">
                {{ fileItem.name }}
                <view class="size"> {{formatBytes(fileItem.size) ||'--'}} </view>
              </div>
            </view>
          </view>
          <!-- <view v-else>--</view> -->
        </view>
        <view v-else-if="val.form_value === 'tag'" class="imgFlex">
          <template v-if="val.value.length > 0">
            <view v-for="(item, index) in val.value" class="tag">{{ item }}</view>
          </template>
          <view v-else>--</view>
        </view>
        <view v-else-if="val.form_value === 'switch'">
          <view>{{ val.value == 1 ? '是' : '否' }}</view>
        </view>
        <view v-else-if="val.form_value === 'rich_text'">
          <view v-html="val.value" v-if="val.value!=='<p><br></p>'" class="rich">
          </view>
          <view v-else>--</view>

        </view>
        <text v-else>
          {{ getValue(val) }}
        </text>
      </uni-col>
    </uni-row>
  </view>
</template>
<script setup>
  import { formatBytes } from '@/utils/file'
  import { isFileTypeIcon, lookPreview, isTypeImage } from '@/utils/helper'
  const props = defineProps({
    list: {
      type: Array,
      default: [],
    },
  })

  // 图片与文档预览
  const preview = (item) => {
    lookPreview(item.url, item.name, [item.url])
  }
  const getValue = (val) => {

    let str = ''
    if (val.value == '') {
      str = '--'
    } else if (val.form_value === 'cascader_address' || val.form_value == 'cascader_radio') {
      str = val.value.join('/')
    } else if (val.form_value === 'radio') {
      str = val.value ? val.value.name : '--'
    } else if (val.form_value === 'checkbox') {
      let arr = []
      if (val.value.length > 0) {
        val.value.map(el => {
          arr.push(el.name)
        })
      }
      str = arr.join(',')
    } else if (val.form_value == 'input_select') {
      str = val.value.name.name || val.value.name
    } else if (val.form_value == 'cascader') {
      str = val.value[0]
    } else if (Array.isArray(val)) {
      str = val.value.toString()
    } else {
      str = val.value
    }
    return str || '--'
  }
  const { list } = toRefs(props)
</script>
<style lang="scss" scoped>
  .box {
    width: 100%;
    height: 100%;
    background-color: #fff;

    .title {
      font-family: PingFang SC, PingFang SC;
      font-weight: 500;
      font-size: 32rpx;
      color: #303133;
      margin-top: 10px;
      padding: 30rpx 30rpx;
    }
  }

  .imgFlex {
    display: flex;
    flex-wrap: wrap;
  }

  .tag {
    padding: 0 12rpx;
    height: 48rpx;
    line-height: 50rpx;
    border: 1rpx solid #1890ff;
    font-family: PingFang SC, PingFang SC;
    font-weight: 400;
    font-size: 26rpx;
    color: #1890ff;
    border-radius: 4rpx;
    margin-right: 16rpx;
  }

  .img {
    display: block;
    width: 92rpx;
    height: 92rpx;
    border-radius: 8rpx;
    margin-right: 10rpx;
    margin-bottom: 10rpx;
  }

  .info-item-left {
    font-family: PingFang SC, PingFang SC;
    font-weight: 400;
    font-size: 28rpx;
    color: #909399;
  }

  .info-item-right {
    padding-left: 40rpx !important;
    font-family: PingFang SC, PingFang SC;
    font-weight: 400;
    font-size: 28rpx;
    color: #303133;
  }

  .list-info {
    padding: 0 20rpx 28rpx 20rpx;
  }

  .file {
    .item {
      width: 100%;
      height: 42px;
      padding: 7px;
      background: #f6f7f9;
      border-radius: 4px 4px 4px 4px;
      display: flex;

      .left-view {
        width: 26px;
        min-width: 26px;
        height: 100%;

        .img {
          display: block;
          margin: 0;
          width: 26px;
          height: 26px;
          border-radius: 2px 2px 2px 2px;
        }
      }

      .right-view {
        width: 100%;
        height: 100%;
        margin-left: 5px;
        font-size: 12px;

        .name {
          width: 100%;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
          font-size: 12px;
          color: #303133;
        }

        .size {
          font-family: PingFang SC, PingFang SC;
          font-weight: 400;
          font-size: 10px;
          color: #909399;
        }
      }
    }
  }

  ::v-deep .rich {
    p img {
      width: 50%;
    }
  }
</style>