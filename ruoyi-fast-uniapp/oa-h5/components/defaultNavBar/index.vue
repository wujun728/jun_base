<template>
  <uni-nav-bar :border="isBorder" :background-color="backgroundColor" :color="color">
    <view v-if="defaultType !== 1 && isShowTitle" class="nar-bar-title line1-1">{{ title }}</view>
    <view v-if="defaultType === 1" class="nar-bar-content">
      <view class="nar-bar-list">
        <view class="nav-bar-list-item" v-for="(item, indexs) in tabData" :key="indexs"
          :class="indexs === typeIndex ? 'active' : ''" @click="narItemClick(item, indexs)">
          <view class="">{{ item.name }}</view>
        </view>
      </view>
    </view>
    <template v-if="isLeft" v-slot:left>
      <view class="iconfont icon-fanhui bar-return" :class="color === '#2B2C32' ? 'active-color' : ''" @click="cancel">
      </view>
      <!-- <view v-if="index>0" class="iconfont icon-guanbi bar-return" @click="toIndexLink"></view> -->
    </template>

    <template v-if="isRight" v-slot:right>
      <template v-if="rightData.length > 0">

        <view class="iconfont bar-return" :class="item.icon" v-for="item in rightData" :key="item.type"
          @click="clickRightItem(item)"></view>
      </template>
      <template v-else>
        <view @click="handleClickRight" class="right-text">{{ rightText }}</view>
      </template>
    </template>
  </uni-nav-bar>
</template>

<script setup lang="ts">
  import { ref, type Ref, toRefs, watch } from 'vue'
  import { clicKReLaunch, clickSwitchTab } from '@/utils/helper'
  import { useStore } from 'vuex'

  const store = useStore()

  let title : Ref<string> = ref('')
  const typeIndex : Ref<number> = ref(0)
  let emit = defineEmits(['handleNarItem', 'handleClickRight', 'goBackChange'])
  // 获取导航栏名称
  const prePage = () => {
    let pages = getCurrentPages()
    return pages[pages.length - 1]
  }

  const cancel = () : void => {
    let pages = getCurrentPages()
    if (jumpUrl.value) {
      // 判断消息是否从消息跳转
      if (store.state.app.isNoticeJumpPage) {
        uni.navigateBack({ delta: 1 })
        setTimeout(() => {
          // 设置消息跳转按钮
          store.commit('setiINoticeJumpPage', false)
        }, 200)
      } else {
        let url = jumpUrl.value
        if (isJumpBar.value) {
          // console.log(pages[0].$page.path, 'pages.length ')
          if (pages.length > 1) {
            url = pages[0].$page.path
          }
          // console.log(565566, isJumpBar.value)
          if (url == '/pages/users/examine/approve' || url == '/pages/users/examine/center') {
            clicKReLaunch(url)
          } else {
            clickSwitchTab(url)
          }
        } else {
          clicKReLaunch(url)
        }
      }
    } else {
      if (isSider.value) {
        // 侧边栏
        emit('goBackChange')
      } else {
        emit('goBackChange')
        if (pages.length > 1) {
          uni.navigateBack({ delta: 1 })
        } else {
          uni.switchTab({ url: '/pages/index/index' })
        }
      }
    }
  }
  // 导航菜单其切换
  const lineWidth : Ref<number> = ref(0)
  const lineLeft : Ref<number> = ref(0)
  const narItemClick = (item : object, index : number) : void => {
    typeIndex.value = index
    lineLeft.value = index * lineWidth.value
    emit('handleNarItem', item)
  }
  const clickRightItem = (item : object) : void => {
    emit('handleNarItem', item)
  }
  const handleClickRight = () : void => {
    emit('handleClickRight')
  }

  const props = withDefaults(
    defineProps<{
      index ?: number
      backgroundColor ?: string
      color ?: string
      defaultTitle ?: string
      defaultType ?: number
      tabData ?: Array<any>
      tabIndex ?: number
      isRight ?: boolean
      isLeft ?: boolean
      isShowTitle ?: boolean
      jumpUrl ?: string
      rightText ?: string
      isJumpBar ?: boolean
      isBorder ?: boolean
      isSider ?: boolean
      rightData ?: Array<any>
    }>(),
    {
      index: 0,
      backgroundColor: '#ffffff',
      color: '#2B2C32',
      defaultTitle: '', // 导航栏名称，默认获取page.json中navigationBarTitleText名字
      defaultType: 0, // 导航栏类型
      tabIndex: 0, // 导航栏选中
      isRight: false,
      rightText: '',
      isLeft: true,
      isShowTitle: true, // 是否显示导航栏文字
      jumpUrl: '', // 返回跳转的url，不填写返回uni.navigateBack()
      isJumpBar: true, // 是否有底部固定导航栏
      isBorder: false,
      isSider: false,
      tabData: <any>[], // 自定义导航栏列表与defaultType为1时，同时使用
      rightData: <any>[], // 自定义右侧图标
    }
  )
  // 导出 {
  const {
    index,
    backgroundColor,
    color,
    defaultTitle,
    defaultType,
    tabData,
    isRight,
    isLeft,
    rightData,
    isShowTitle,
    jumpUrl,
    isBorder,
    tabIndex,
    rightText,
    isJumpBar,
    isSider,
  } = toRefs(props)

  watch(
    [() => defaultTitle, () => tabIndex],
    (newvalue, oldvalue) => {
      const tTitle = newvalue[0].value
      const tIndex = newvalue[1].value
      if (tTitle) {
        title.value = defaultTitle.value
      } else if (tTitle === null) {
        title.value = ''
      } else {
        const preInfo = prePage()
        title.value = preInfo.$page.meta.navigationBar.titleText
      }

      typeIndex.value = tIndex
    },
    { immediate: true, deep: true }
  )
</script>

<style scoped lang="scss">
  .nar-bar-title {
    font-size: 16px;
    font-weight: 500;
  }

  .right-text {
    font-size: $uni-font-size-default;
    color: $uni-color-primary;
  }

  ::v-deep .uni-navbar__header-btns-left {
    justify-content: space-between;
  }

  ::v-deep .uni-navbar--border {
    border-bottom-color: #ebeef5;
  }

  ::v-deep .uni-navbar__header-btns-right {
    justify-content: v-bind('rightData.length > 1 ? "space-between" : "flex-end"');
  }

  ::v-deep .uni-navbar__header-container {
    justify-content: center;
    align-items: center;
  }

  .nar-bar-content {
    height: 100%;
  }

  .nar-bar-list {
    height: 100%;
    display: flex;
    align-items: center;
    color: $nui-text-color-two;
    font-size: 32rpx;
    position: relative;

    .nav-bar-list-item {
      display: flex;
      align-items: center;
      height: 100%;
      margin-right: 44rpx;
      border-bottom: 2px solid rgba(0, 0, 0, 0);

      &:last-of-type {
        margin-right: 0;
      }

      &.active {
        color: #2b2c32;
        font-weight: $uni-default-font-weight;
        border-bottom: 2px solid $uni-color-primary;
      }
    }
  }

  .bar-return {
    font-size: 34rpx;
    font-weight: 400;

    .active-color {
      color: $nui-text-color-two;
    }
  }
</style>