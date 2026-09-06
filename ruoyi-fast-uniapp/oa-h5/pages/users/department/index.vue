<template>
  <view class="forget">
    <view class="forget-content">
      <!-- 非h5页面默认工具栏高度获取 -->
      <view class="target">
        <view class="status_bar"></view>
        <default-nav-bar :index="0" :is-border="false" :default-title="data.title"></default-nav-bar>
        <view class="plr10">
          <view class="display-align" @click="clickOrganizationRef">
            <view class="box1">
              <text class="iconfont icon-zuzhijiagou default-color"></text>
            </view>
            <view class="title p-l-30 default-text-color-one">组织架构</view>
          </view>
        </view>
        <view class="personnel m-t-30">企业成员（{{data.count}}人）</view>
      </view>
      <view class="personnel-content">
        <personnel-list :options="data.list" :show-select="true" :mode="data.mode"
          :is-checked="data.isChecked"></personnel-list>
      </view>
      <select-bottom-bar :is-checked="data.isChecked" @handleOk="handleOk"></select-bottom-bar>
    </view>

  </view>
</template>

<script setup>
  import defaultNavBar from '@/components/defaultNavBar/index'
  import personnelList from './components/personnelList/uni-indexed-list.vue'
  import selectBottomBar from './components/selectBottomBar.vue'
  import { ref, onMounted, reactive, getCurrentInstance, nextTick, watch, computed } from 'vue'
  import message from '@/utils/message'
  import { useStore } from 'vuex'
  import { onLoad } from '@dcloudio/uni-app'
  import { decryptQuery } from '@/utils/cryptoCode'
  import { enterpriseUsersApi } from '@/api/user'
  import { navigateToDepartment } from '@/utils/autoload'
  import { getIdsFromArray, clickNavigateTo } from '@/utils/helper'

  /**
   * mode = multiSelector 多选  
   * mode = selector 单选 默认单选
   * isChecked = 0 是否禁止已有的成员
   */
  const popupRef = ref(null)
  const chenkPersonnelRef = ref(null)
  const store = useStore()
  const data = reactive({
    list: [],
    count: 0,
    title: '选择成员',
    mode: 'selector',
    isChecked: 0
  })

  const instance = getCurrentInstance() // 获取组件实例

  let clientTop = ref(0)

  const search = ref('')

  const selectPersonnel = reactive({
    arr: [],
    ids: []
  })

  //返回的回掉函数
  const goBackChange = () => {
    if (!data.optionsId) {
      if (data.isSelect === 0) {
        resetSelectDepartment()
      }
    }
  }
  // 页面加载完成
  onLoad(e => {
    if (e.item) {
      // 参数解密
      let options = decryptQuery(e.item)

      // 单选/多选
      if (options.mode && options.mode == 'multiSelector') {
        data.mode = options.mode
      }
      // 是否禁止已有的成员
      if (options.isChecked && options.isChecked == 1) {
        data.isChecked = Number(options.isChecked)
      }
    }
  })

  const getIsShow = computed(() => {
    //返回的是ref对象
    return store.state.app.isOrgShow
  })

  // 数据监听
  watch(getIsShow, (newvalue, oldvalue) => {

  }, { immediate: true, deep: true });


  //确认后回调
  const handleOk = (e) => {
    uni.navigateBack({ delta: 1 })
  }

  // 查看组织架构
  const clickOrganizationRef = () => {
    const query = `isShow=true&isFirst=1&isSelect=1&mode=${data.mode}&backNumber=2&isChecked=${data.isChecked}`
    navigateToDepartment(query)
  }

  onMounted(() => {
    getClientTop()
    getEntUsers()
  })

  const getEntUsers = () => {
    enterpriseUsersApi().then((res) => {
      data.list = res.data.list
      data.count = res.data.count
    }).catch((error) => {
      message.error(error.message)
    })
  }

  // 获取默认页面高度
  const getClientTop = async () => {
    await nextTick()
    let query = uni.createSelectorQuery().in(instance);
    query.select('.target').boundingClientRect(data => {
      clientTop.value = Math.floor(data.height) + 'px'
    }).exec()
  }
</script>
<style>
  page {
    background-color: #fff;
  }
</style>
<style lang="scss" scoped>
  .forget {

    .forget-content {
      width: 100vw;

      .search-content {
        padding-top: 16rpx;

        ::v-deep .uni-easyinput__content {
          border: none;
          background-color: #F0F1F5 !important;
        }
      }

      .box1 {
        width: 70rpx;
        height: 70rpx;
        background-color: #F2F6FC;
        text-align: center;
        line-height: 70rpx;
        border-radius: 8rpx;

        uni-text {
          font-size: 40rpx;
        }
      }

      .title {
        font-weight: 600;
      }

      .personnel {
        background-color: #F0F1F5;
        padding: 24rpx 30rpx;
        font-size: 24rpx;
        color: #909399;
      }

      .personnel-content {
        ::v-deep .uni-indexed-list {
          //
          height: calc(100vh - v-bind(clientTop) - 124rpx);
          top: v-bind(clientTop);
          bottom: 124rpx;
        }
      }
    }
  }
</style>