<template>
  <view class="content">
    <view class="cr-position-header">
      <view class="status_bar"></view>
      <default-nav-bar jump-url="/pages/workbench/index" :default-title="data.defaultTitle" :is-right="data.isRight"
        :right-data="rightIcon" @handleNarItem="handleNarItem">
      </default-nav-bar>
      <form-box @change="formBoxChange"></form-box>
    </view>

    <view class="examine-content m10">
      <examine-list-default :list-data="data.listData" :type-index="data.typeIndex"
        :empty-title="data.emptyTitle"></examine-list-default>
    </view>

    <global-index></global-index>
    <view class="uni-p-b-98"></view>
    <bottom-navigation :type="2" page-path="/pages/users/examine/center"></bottom-navigation>
  </view>
</template>

<script setup>
  import defaultNavBar from '@/components/defaultNavBar/index.vue'
  import bottomNavigation from "@/components/bottomNavigation/index.vue"
  import examineListDefault from "./components/examineListDefault.vue"
  import examineTab from "./components/examineTab.vue"
  import globalIndex from '@/components/globalIndex/index.vue'
  import formBox from "./components/formBox.vue"
  import {
    ref,
    reactive,
    onMounted,
    getCurrentInstance
  } from "vue"
  import message from "@/utils/message"
  import {
    examineTabData
  } from '@/utils/assessment'

  const data = reactive({
    typeIndex: 0,
    tabIndex: 0,
    isRight: true,
    defaultTitle: '待处理',
    emptyTitle: '当前暂无审批内容～',
    customStyle: {
      border: 'none',
      lineHeight: '20px',
      background: '#ED4014'
    },
    examineTabData: examineTabData,
    listData: [],
    where: {
      limit: 10,
      page: 1,
      types: 0,
      status: '',
      time: '',
      approve_id: ''
    },
  })


  import {
    useBarHeight
  } from '@/utils/useVerifyCode'
  const {
    height,
    getBarHeight
  } = useBarHeight()
  const instance = getCurrentInstance()

  onMounted(() => {
    getBarHeight('.cr-position-header', instance)
  })


  import {
    approveApplyApi
  } from '@/api/business'
  import {
    onLoad
  } from '@dcloudio/uni-app'
  onLoad((options) => {
    const len = data.examineTabData.length - 1
    if (options.id) {
      if (options.id > len) {
        data.tabIndex = len
      } else {
        data.tabIndex = Number(options.id)
      }
      getTabList()
    } else {
      getTabList()
    }
  })

  const rightIcon = reactive([{
    type: 1,
    icon: 'icon-sousuo'
  }])

  import {
    clickNavigateTo,
    backToTop
  } from '@/utils/helper'
  const handleNarItem = (e) => {
    clickNavigateTo(`/pages/users/examine/search?index=${data.tabIndex}&type=2`)
  }

  const changeTab = (e) => {
    if (data.where.page > 1) {
      backToTop()
    }
    data.tabIndex = e.index
    data.where.page = 1
    getTabList(true)
  }

  const formBoxChange = (e) => {
    data.where.page = 1
    data.where.status = e.status
    data.where.time = e.time
    data.where.approve_id = e.approveId
    getTabList(true)
  }


  // 条件判断
  const getTabList = (tab = false) => {
    data.defaultTitle = data.examineTabData[data.tabIndex].name
    const id = data.examineTabData[data.tabIndex].id
    data.typeIndex = Number(id)
    if (id < 0) {

      data.where.types = 0
      data.isRight = false

    } else {
      data.where.types = 1
      data.where.verify_status = id
      data.isRight = true
    }
    getConfigList(tab)
  }

  const listLoading = ref(false)
  // 列表加载
  const getConfigList = (tab = false) => {
    approveApplyApi(data.where).then((res) => {
      // 切换时数据清空
      if (tab) data.listData = []
      data.listData.push(...res.data.list)
      const allPage = Math.ceil(res.data.count / data.where.limit)
      if (data.listData.length <= 0 || data.where.page >= allPage) {
        listLoading.value = false
      } else {
        listLoading.value = true
      }
      uni.stopPullDownRefresh() //停止刷新
    }).catch((error) => {
      message.error(error.message)
    });
  }

  import {
    onReachBottom,
    onPullDownRefresh
  } from '@dcloudio/uni-app'
  // 下拉加载
  onReachBottom(() => {
    if (listLoading.value) {
      data.where.page++
      getConfigList()
    }
  })
  //上拉加载
  onPullDownRefresh(() => {
    data.where.page = 1
    data.value = false
    data.listData = []
    getConfigList()
  })
</script>

<style scoped lang="scss">
  .content {
    width: 100%;
    position: relative;

    .cr-position-header {
      background-color: #fff;

      .examine-search {
        height: 80rpx;
        line-height: 80rpx;
        border-bottom: 1px solid #F0F1F5;

        .examine-search-list {
          display: flex;
          justify-content: space-between;
          align-items: center;

          .examine-search-list-item {
            position: relative;
            text-align: center;
            color: $nui-text-color-four;
            border-bottom: 2px solid rgba(0, 0, 0, 0);

            &.active {
              color: $uni-text-color;
              font-size: 600;
              font-weight: $uni-default-font-weight;
              border-bottom: 2px solid $uni-color-primary;
            }

            .name {
              font-size: 28rpx;
            }

            .examine-search-badge {
              position: absolute;
              top: -10px;
              right: 0;
            }
          }
        }
      }
    }

    .examine-content {
      padding-top: v-bind(height);
    }
  }
</style>