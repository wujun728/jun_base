<template>
  <view
    id="tabbar"
    class="uni-tabbar display-center"
    :class="{ borderShow: isBorader }"
  >
    <template v-if="isShow && config.tabbar.length">
      <template v-for="(item, index) in config.tabbar" :key="index">
        <view class="uni-tabbar_item" v-if="item.isShow" @tap="changeTab(item)">
          <view class="uni-tabbar_icon">
            <image
              v-if="item.pagePath === pagePath"
              mode="aspectFit"
              :src="item.selectedIconPath"
            ></image>
            <image v-else mode="aspectFit" :src="item.iconPath"></image>
          </view>
          <view
            class="uni-tabbar_label"
            :class="{ active: item.pagePath == pagePath }"
          >
            {{ item.name }}
          </view>
        </view>
      </template>
    </template>
    <view v-else-if="!tabbar.length" class="empty-img">暂无数据，请设置</view>
  </view>
</template>

<script setup lang="ts">
import { ref, reactive, toRefs, watch, computed } from 'vue'
import { useStore } from 'vuex'
let store = useStore()

const props = withDefaults(
  defineProps<{
    pagePath: string
    type?: number
  }>(),
  {
    pagePath: '',
    type: 0,
  },
)
const { pagePath, type } = toRefs(props)
const isBorader: Ref<boolean> = ref(false)
const isShow: Ref<boolean> = ref(true)
let enterpriseAuth = computed(() => store.state.app.enterpriseAuth)
let page: Ref<string> = ref('/pages/index/index')

interface Tab {
  iconPath: string
  name: string
  pagePath: string
  selectedIconPath: string
  isShow?: number
}

// 默认底部导航
const tabbar = reactive([
  {
    iconPath: '/static/image/bar-logo01.png',
    name: '消息',
    pagePath: '/pages/index/index',
    selectedIconPath: '/static/image/bar-logo01-active.png',
    isShow: 1,
  },
  {
    iconPath: '/static/image/bar-logo02.png',
    name: '社区',
    pagePath: '/pages/forum/index',
    selectedIconPath: '/static/image/bar-logo02-active.png',
    isShow: 1,
  },
  {
    iconPath: '/static/image/bar-logo03.png',
    name: '工作台',
    pagePath: '/pages/workbench/index',
    selectedIconPath: '/static/image/bar-logo03-active.png',
    isShow: 1,
  },
  {
    iconPath: '/static/image/bar-logo05.png',
    name: '通讯录',
    pagePath: '/pages/user/index',
    selectedIconPath: '/static/image/bar-logo05-active.png',
    isShow: 1,
  },
])
// 默认工作汇报底部导航栏请勿删除
const reportBar = reactive([
  {
    iconPath: '/static/image/bar-logo07.png',
    name: '写汇报',
    pagePath: '/pages/users/report/index',
    selectedIconPath: '/static/image/bar-logo07-active.png',
    isShow: 1,
  },
  {
    iconPath: '/static/image/bar-logo06.png',
    name: '看汇报',
    pagePath: '/pages/users/report/check',
    selectedIconPath: '/static/image/bar-logo06-active.png',
    isShow: 1,
  },
  {
    iconPath: '/static/image/bar-logo12.png',
    name: '统计',
    pagePath: '/pages/users/report/census',
    selectedIconPath: '/static/image/bar-logo12-active.png',
    isShow: 0,
  },
])
// 默认申请审批底部导航栏请勿删除
const examineBar = reactive([
  {
    iconPath: '/static/image/bar-logo09.png',
    name: '新申请',
    pagePath: '/pages/users/examine/index',
    selectedIconPath: '/static/image/bar-logo09-active.png',
    isShow: 1,
  },
  {
    iconPath: '/static/image/bar-logo10.png',
    name: '我审批的',
    pagePath: '/pages/users/examine/approve',
    selectedIconPath: '/static/image/bar-logo10-active.png',
    isShow: 1,
  },
  {
    iconPath: '/static/image/bar-logo11.png',
    name: '已发起',
    pagePath: '/pages/users/examine/center',
    selectedIconPath: '/static/image/bar-logo11-active.png',
    isShow: 1,
  },
])
// 默认记事本底部导航栏请勿删除
const notebookBar = reactive([
  {
    iconPath: '/static/image/bar-logo13.png',
    name: '最新',
    pagePath: '/pages/users/memorandum/index',
    selectedIconPath: '/static/image/bar-logo13-active.png',
    isShow: 1,
  },
  {
    iconPath: '/static/image/bar-logo14.png',
    name: '文件夹',
    pagePath: '/pages/users/memorandum/folder',
    selectedIconPath: '/static/image/bar-logo14-active.png',
    isShow: 1,
  },
])
// 打卡tab
const attendanceBar = reactive([
  {
    iconPath: '/static/image/attendance/tab-0-0.png',
    name: '打卡',
    pagePath: '/pages/attendance/index',
    selectedIconPath: '/static/image/attendance/tab-0-1.png',
    isShow: 1,
  },
  {
    iconPath: '/static/image/attendance/tab-1-0.png',
    name: '申请',
    pagePath: '/pages/attendance/apply',
    selectedIconPath: '/static/image/attendance/tab-1-1.png',
    isShow: 1,
  },
  {
    iconPath: '/static/image/attendance/tab-2-0.png',
    name: '统计',
    pagePath: '/pages/attendance/statistics',
    selectedIconPath: '/static/image/attendance/tab-2-1.png',
    isShow: 1,
  },
])
const config = reactive({
  tabbar: <any>[],
})

// 页面路由跳转{
const changeTab = (item: Tab): boolean | undefined => {
  page.value = item.pagePath
  // 这里使用reLaunch关闭所有的页面，打开新的栏目页面
  if (type.value === 0) {
    uni.reLaunch({
      url: page.value,
    })
  } else {
    const routes = getCurrentPages() // 获取当前打开过的页面路由数组
    const curRoute = routes[routes.length - 1].route // 获取当前页面路由，也就是最后一个打开的页面路由
    if (item.pagePath.indexOf(curRoute) > -1) {
      return false
    }
    uni.navigateTo({
      url: page.value,
      animationType: 'fade-in',
    })
  }
}

watch(
  () => [type, enterpriseAuth],
  (newvalue, oldvalue): void => {
    const types = newvalue[0].value
    if (types === 0) {
      config.tabbar = tabbar
    } else if (types === 1) {
      reportBar[2].isShow = newvalue[1].value.manage_auth
      config.tabbar = reportBar
    } else if (types === 2) {
      config.tabbar = examineBar
    } else if (types === 3) {
      config.tabbar = notebookBar
    } else if (types === 4) {
      config.tabbar = attendanceBar
    }
  },
  {
    deep: true,
    immediate: true,
  },
)
</script>

<style lang="scss" scoped>
.uni-tabbar {
  position: fixed;
  bottom: 0;
  z-index: 98;
  width: 100%;
  height: 54px;
  height: calc(54px + constant(safe-area-inset-bottom)); ///兼容 IOS<11.2/
  height: calc(54px + env(safe-area-inset-bottom)); ///兼容 IOS>11.2/
  box-sizing: border-box;
  border-top: solid 1px #ebeef5;
  background-color: #fff;
  padding-bottom: constant(safe-area-inset-bottom); ///兼容 IOS<11.2/
  padding-bottom: env(safe-area-inset-bottom); ///兼容 IOS>11.2/
  justify-content: space-around;

  .uni-tabbar_item {
    font-size: 10px;
    text-align: center;
  }

  .uni-tabbar_icon {
    height: 21px;
    width: 21px;
    text-align: center;
    margin: 0 auto;

    image {
      width: 100%;
      height: 100%;
    }
  }

  .uni-tabbar_label {
    font-size: 10px;
    color: rgb(40, 40, 40);
    margin-top: 1px;

    &.active {
      color: #308bf8;
    }
  }
}
</style>
