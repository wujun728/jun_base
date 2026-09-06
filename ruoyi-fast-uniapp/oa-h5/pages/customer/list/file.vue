<template>
  <view>
    <view class="cr-position-header">
      <view class="status_bar"></view>
      <default-nav-bar :is-right="true" :jump-url="data.jumpUrl" :is-jump-bar="false" :right-data="rightIcon"
        @handleNarItem="handleNarItem"></default-nav-bar>
    </view>
    <view class="content m10">
      <file-card-list :list-data="data.listData" :empty-title="data.emptyTitle"></file-card-list>
    </view>
  </view>
</template>

<script setup>
  import empty from '@/components/empty/index'
  import defaultNavBar from '@/components/defaultNavBar/index.vue'
  import fileCardList from './components/fileCardList.vue'
  import { ref, reactive } from 'vue'
  import message from '@/utils/message'
  import { clientFileListApi } from '@/api/customer'

  const data = reactive({
    listData: [],
    where: {
      page: 1,
      limit: 10,
      eid: ''
    },
    emptyTitle: '暂无上传资料～',
    jumpUrl: ''
  })

  const rightIcon = reactive([
    { type: 1, icon: 'icon-a-gengduo2' },
  ])

  import { onLoad } from '@dcloudio/uni-app'
  onLoad(e => {
    data.where.eid = e.eid
    data.jumpUrl = `/pages/customer/list/details?id=${e.eid}`
    if (e.types == 3) {
      rightIcon = rightIcon.splice(0, 1)
    }
    getFlieList()
  })

  import { uploadFlie } from '@/utils/file'
  import { fileSizeOne } from '@/utils/helper'
  const handleNarItem = (e) => {
    const datas = {
      relation_id: data.where.eid,
      relation_type: 'client',
    }
    uploadFlie('attach/imgs', datas, fileSizeOne).then(res => {
      if (res.status === 200) {
        data.where.page = 1
        getFlieList(true)
      }
    }).catch(error => {
      message.error(error)
    })
  }

  const listLoading = ref(false)
  // 获取附件列表
  const getFlieList = (tab = false) => {
    clientFileListApi(data.where).then(res => {
      if (tab) data.listData = []
      data.listData.push(...res.data.list)
      const allPage = Math.ceil(res.data.count / data.where.limit)
      listLoading.value = data.listData.length <= 0 || data.where.page >= allPage ? false : true
      uni.stopPullDownRefresh() //停止刷新
    }).catch(error => {
      message.error(error.message)
    })
  }

  import { onReachBottom, onPullDownRefresh } from '@dcloudio/uni-app'
  // 下拉加载
  onReachBottom(() => {
    if (listLoading.value) {
      data.where.page++
      getFlieList()
    }
  })
  //上拉加载
  onPullDownRefresh(() => {
    data.where.page = 1
    data.value = false
    data.listData = []
    getFlieList()
  })
</script>
<style scoped lang="scss">
  .cr-position-header {
    position: sticky;
  }

  .content {}
</style>