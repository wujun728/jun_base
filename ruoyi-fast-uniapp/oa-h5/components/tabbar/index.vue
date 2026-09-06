<template>
  <view class="tab">
    <view v-for="(item, index) in list" :key="index" class="tab-item" @click="switchTab(item, index)">
      <image class="tab_img" :src="currentIndex == index ? item.selectedIconPath : item.iconPath"></image>
      <view class="tab_text" :style="{ color: currentIndex == index ? selectedColor : color }">{{ item.text }}</view>
    </view>
  </view>
</template>

<script setup>
  import { reactive, ref } from 'vue'
  import { clicKReLaunch } from '@/utils/helper'
  import { useStore } from 'vuex'
  const store = useStore()
  const props = defineProps({
    currentIndex: {
      type: Number,
      default: 0,
    },
  })
  const { currentIndex } = toRefs(props)

  const selectedColor = ref('#308BF8')
  const color = ref('#687383')
  const list = ref([{
      id: 0,
      text: '客户',
      pagePath: '/pages/customer/list/index',
      iconPath: '/static/image/customer.png',
      selectedIconPath: '/static/image/customer-s.png',
    },
    {
      id: 1,
      text: '合同',
      pagePath: '/pages/customer/contract/index',
      iconPath: '/static/image/contract.png',
      selectedIconPath: '/static/image/contract-s.png',
    },
    {
      id: 2,
      text: '发票',
      pagePath: '/pages/customer/invoice/index',
      iconPath: '/static/image/invoice.png',
      selectedIconPath: '/static/image/invoice-s.png',
    },
    {
      id: 3,
      text: '统计',
      pagePath: '/pages/customer/list/statistics',
      iconPath: '/static/image/statistics.png',
      selectedIconPath: '/static/image/statistics-s.png',
    },
  ])
  const switchTab = (item, index) => {
    store.commit('currentIndexFn', index)
    // store.state.app.userInfo
    // data.currentIndex = index;
    // console.log(data.currentIndex)
    let url = item.pagePath
    clicKReLaunch(url)
  }
</script>

<style lang="scss">
  .tab {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    height: 103rpx;
    background: white;
    display: flex;
    justify-content: center;
    align-items: center;
    padding-bottom: env(safe-area-inset-bottom); // 适配iphoneX的底部

    .tab-item {
      flex: 1;
      text-align: center;
      display: flex;
      justify-content: center;
      align-items: center;
      flex-direction: column;

      .tab_img {
        width: 44rpx;
        height: 44rpx;
      }

      .tab_text {
        font-size: 22rpx;
        font-family: PingFang SC, PingFang SC;
        font-weight: 400;
        color: #687383;
      }
    }
  }
</style>