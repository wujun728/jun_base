<template>
  <view class="">
    <!-- #ifdef APP-PLUS -->
    <view class="cr-position-header">
      <view class="status_bar"></view>
      <default-nav-bar :is-left="false"></default-nav-bar>
    </view>
    <!-- #endif -->
    <view class="content">
      <view class="nav-content plr12 m10" v-for="(item, index) in data.menus" :key="'list' + index">
        <view class="nav-title">{{ item.menu_name }}</view>
        <template v-if="item.children && item.children.length > 0">
          <view class="nav-content-list">
            <view class="nav-list-item" v-for="(items, indexs) in item.children" :key="'items' + indexs" @click="navItemClicks(items)">
              <view class="nav-list-item-box">
                <image class="image" :src="items.uni_img"></image>
              </view>
              <view class="nav-list-item-title">{{ items.menu_name }}</view>
            </view>
          </view>
        </template>
      </view>
      <!-- 菜单列表  -->
    </view>
    <global-index></global-index>
  </view>
</template>
<script setup lang="ts">
import defaultNavBar from '@/components/defaultNavBar/index.vue'
import globalIndex from '@/components/globalIndex/index.vue'
import { loginMenus } from '@/api/user'
import { clickNavigateTo } from '@/utils/helper'
import { useStore } from 'vuex'
import { toLogin } from '@/libs/login'

const store = useStore()
const isLogin = computed(() => store.state.app.isLogin)

onShow(() => {
  if (!isLogin.value) {
    toLogin()
  } else {
    getMenus()
  }
})

let data = reactive({
  noticeData: [],
  menus: [],
})

// 页面跳转
const navItemClicks = (item: any) => {
  if (item.menu_name === '客户列表') {
    uni.setStorageSync('types', '1')
  } else if (item.menu_name === '合同管理') {
    uni.setStorageSync('types', '2')
  } else if (item.menu_name === '发票管理') {
    uni.setStorageSync('types', '3')
  }
  if (!item.uni_path) return
  clickNavigateTo(item.uni_path)
}
const getMenus = async () => {
  const res = await loginMenus()
  data.menus = res.data.menu
}
</script>
<style scoped lang="scss">
.content {
  // #ifdef APP-PLUS
  padding-top: calc($uni-default-bar-height + var(--status-bar-height));
  // #endif
}

.nav-content {
  padding-top: 28rpx;
  background-color: #fff;
  border-radius: 16rpx;

  .nav-title {
    font-size: 32rpx;
    padding-bottom: 34rpx;
    font-weight: 600;
    color: $uni-text-color;

    .nav-title-right {
      font-size: 24rpx;
      color: $nui-text-color-four;
      font-weight: normal;

      .iconfont {
        display: inline-block;
        font-size: 24rpx;
        padding-left: 10rpx;
        transform: rotate(180deg);
      }
    }
  }

  .nav-content-list {
    border-radius: 16rpx;
    display: flex;
    justify-content: flex-start;
    flex-wrap: wrap;
    width: calc(100% + 58rpx);
    margin-left: -29rpx;

    .nav-list-item {
      width: 25%;
      text-align: center;
      margin-bottom: 36rpx;

      .nav-list-item-box {
        width: 90rpx;
        height: 90rpx;
        display: inline-block;

        .image {
          width: 100%;
          height: 100%;
        }
      }

      .nav-list-item-title {
        padding-top: 8rpx;
        font-size: 24rpx;
        color: $uni-text-color;
      }
    }
  }
}
</style>
