<template>
  <view v-if="list && list.length > 0">
    <u-cell v-for="(item, index) in list" :key="index" :isLink="true" :border="true" @click="$emit('click', item)">
      <u-avatar slot="icon" :bg-color="item.noticeType == 2 ? '#2979ff' : '#19be6b'" :text="item.noticeType == 2 ? '公' : '通'" shape="square"></u-avatar>
      <view slot="title" class="notice-record-title">{{item.noticeTitle}}</view>
      <view slot="label" class="notice-record-desc">
        <view style="display: flex;">
          <u-icon name="clock" size="12"></u-icon>
          <text>{{item.createTime}}</text>
        </view>
        <view style="display: flex; margin-left: 16px;">
          <u-icon name="account" size="12"></u-icon>
          <text>{{item.remark}}</text>
        </view>
      </view>
    </u-cell>
    <!-- <u-loadmore :status="status" /> -->
  </view>
  <u-empty v-else></u-empty>
</template>

<script>
export default {
  props: {
    list: {
      type: Array,
      default: []
    },
    status: String
  }
}
</script>

<style lang="scss" scoped>
/deep/.u-cell__body {
  padding: 8px 0 !important;
}
.notice-record {
  display: flex;
  padding: 16px 0;

  &-content {
    margin-left: 8px;
  }

  &-title {
    font-size: 18px;
    font-weight: bold;
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
  }

  &-title .read {
    color: #ccc;
  }

  &-desc {
    padding: 4px 0;
    font-size: 12px;
    color: #909399;
    display: flex;
  }
}
</style>