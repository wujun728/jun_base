<script setup lang="ts">
import tab from "@/plugins/tab";
import list from "./template.config.js";

interface ListItem {
  groupName: string;
  list: FieldItem[];
}

interface FieldItem {
  title: string;
  icon: string;
  path: string;
}

const listData = list as ListItem[];
const getIcon = (path: string) => `../static/icon/${path}.png`;
const openPage = (path: string) => tab.navigateTo(path)
const getGroupTitle = (item: ListItem) => item.groupName;
const getFieldTitle = (item: FieldItem) => item.title;
</script>
<template>
  <view class="wrap">
    <view class="list-wrap">
      <u-cell-group title-bg-color="rgb(243, 244, 246)" :title="getGroupTitle(item)" v-for="(item, index) in listData"
        :key="index">
        <u-cell :titleStyle="{ fontWeight: 500 }" @click="openPage(item1.path)" :title="getFieldTitle(item1)"
          v-for="(item1, index1) in item.list" :key="index1">
          <template v-slot:icon>
            <image class="u-cell-icon" :src="getIcon(item1.icon)" mode="widthFix"></image>
          </template>
        </u-cell>
      </u-cell-group>
    </view>
    <u-gap height="70"></u-gap>
  </view>
</template>
<style lang="scss" scoped>
page {
  background-color: rgb(240, 242, 244);
}

.u-cell-icon {
  width: 36rpx;
  height: 36rpx;
  margin-right: 8rpx;
}
</style>