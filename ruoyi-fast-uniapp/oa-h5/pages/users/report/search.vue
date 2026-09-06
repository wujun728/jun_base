<template>
  <view class="content">
    <view class="cr-position-header">
      <view class="status_bar"></view>
      <view class="default-search">
        <uni-search-bar @confirm="search" @focus="search" :focus="true" bgColor="#F0F1F5" v-model="config.where.name"
          @clear="clearSearch" @cancel="cancelSearch">
        </uni-search-bar>
      </view>
      <view class="bar-search">
        <form-box :type="config.selectType" @change="changeFormBox"></form-box>
      </view>
    </view>

    <view class="report-content m10">
      <report-list :list-data="config.listData" :empty-title="emptyTitle"></report-list>
    </view>
  </view>
</template>

<script setup>
  import reportList from "./components/reportList.vue"
  import formBox from "./components/formBox.vue"
  import { ref, reactive, onMounted, getCurrentInstance } from "vue"
  import message from '@/utils/message'
  const emptyTitle = ref('暂无搜索的内容～')

  const rightIcon = reactive([
    { type: 1, icon: 'icon-sousuo', types: 'icon' }
  ])

  const config = reactive({
    selectType: 'mine',
    where: {
      finish: '',
      type: 0,
      types: '',
      name: '',
      time: '',
      user_id: '',
      page: 1,
      limit: 10
    },
    listData: []
  })
  const listLoading = ref(false) //是否正在加载

  onMounted(() => {
    getClientInfo()
  })

  import { onLoad } from '@dcloudio/uni-app'
  onLoad((options) => {
    if (options.type) {
      config.selectType = options.type
    }
  })

  const clearSearch = () => {
    config.where.name = ''
    config.listData = []
  }

  const cancelSearch = () => {
    uni.navigateBack()
  }

  const search = () => {
    if (config.where.name) {
      getEnterpriseDaily(true)
    } else {
      clearSearch()
    }
  }

  const instance = getCurrentInstance() // 获取组件实例
  const height = ref(0)
  const getClientInfo = () => {
    let query = uni.createSelectorQuery().in(instance);
    query.select('.cr-position-header').fields({ size: true })
    query.exec((data) => {
      height.value = data[0].height + 'px'
    })
  }

  import { enterpriseDailyApi } from '@/api/user'
  // 获取
  const getEnterpriseDaily = (tab = false) => {

    config.where.type = config.selectType === 'mine' ? 0 : 1
    console.log(config.where)
    enterpriseDailyApi(config.where).then(res => {
      // 切换时数据清空
      if (tab) config.listData = []
      config.listData.push(...res.data.list)
      const allPage = Math.ceil(res.data.count / config.where.limit)
      if (config.listData.length <= 0 || config.where.page >= allPage) {
        listLoading.value = false
      } else {
        listLoading.value = true
      }
      uni.stopPullDownRefresh() //停止刷新
    }).catch((error) => {
      uni.hideLoading()
      message.error(error.message)
    })
  }

  const changeFormBox = (e) => {
    config.where.types = e.status

    config.where.time = e.time
    config.where.user_id = e.approveId
    config.where.page = 1
    getEnterpriseDaily(true)
  }

  import { onReachBottom } from '@dcloudio/uni-app'
  // 下拉加载
  onReachBottom(() => {
    if (listLoading.value) {
      config.where.page++
      getEnterpriseDaily()
    }
  })
</script>

<style scoped lang="scss">
  ::v-deep .selected-list {
    display: none;
  }

  .cr-position-header {
    background-color: #fff;
  }

  .default-search {
    padding-bottom: 34rpx;
  }

  .content {
    width: 100%;
    position: relative;

    .bar-search {
      border-top: 1px solid #EBEEF5;
      width: 100%;
      background-color: #fff;


      ::v-deep .uni-calendar--fixed {
        z-index: 101;
      }
    }

    .report-content {
      margin-top: 0;
      padding-top: v-bind(height);
    }
  }
</style>