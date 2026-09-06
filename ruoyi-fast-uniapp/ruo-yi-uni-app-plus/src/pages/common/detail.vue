<template>
  <view>
    <uni-card class="view-title" :title="title">
      <text class="uni-body view-content">{{ content }}</text>
    </uni-card>
  </view>
</template>

<script setup>

import {ref} from "vue";
import {onLoad} from "@dcloudio/uni-app";
import {equipInfo} from "@/api/xcj/equip";

const title = ref('');
const content = ref('');
const id= ref('');
onLoad(async (options) => {
  id.value = options.id
  const res = await equipInfo(id.value);
  title.value = res.data.eqName;
  uni.setNavigationBarTitle({
    title: title.value
  })
});
</script>

<style scoped>
page {
  background-color: #ffffff;
}

.view-title {
  font-weight: bold;
}

.view-content {
  font-size: 26rpx;
  padding: 12px 5px 0;
  color: #333;
  line-height: 24px;
  font-weight: normal;
}
</style>
