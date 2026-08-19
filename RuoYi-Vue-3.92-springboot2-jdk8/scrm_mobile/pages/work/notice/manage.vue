<template>
  <view class="mobile-item-container">
    <Navbar title="公告管理" bgColor="#fff" :h5Show="false"></Navbar>
    <Tabs :tabs="tabs" @change="tabChange" style="margin-bottom: 16px;"></Tabs>
    <Record v-if="activeKey == 'draft'" :list="list" @click="navigateTo"></Record>
    <Record v-if="activeKey == 'published'" :list="list" @click="navigateTo"></Record>
    <FloatButton type="primary" icon="plus" @click="navigateTo"></FloatButton>
  </view>
</template>

<script>
import * as NoticeApi from '@/api/work/notice'
import Navbar from '@/components/navbar/Navbar'
import FloatButton from '@/components/button/FloatButton'
import Tabs from '@/components/tabs/Tabs'
import Record from './record'

export default {
  components: {
    Navbar,
    FloatButton,
    Tabs,
    Record,
  },
  data () {
    return {
      activeKey: 'draft',
      tabs: [{
        name: '草稿',
        key: 'draft',
      }, {
        name: '已发布',
        key: 'published'
      }],
      params: {
        pageNum: 0,
        pageSize: 10
      },
      list: []
    }
  },
  created () {
    this.loadData()
  },
  methods: {
    // 加载通知公告数据
    loadData () {
      const app = this
      this.params.pageNum += 1;
      NoticeApi.noticeList(this.params).then(res => {
        app.list = res.rows;
      })
    },
    // 滚动分页加载数据
    scrolltolower () {
      this.loadData();
    },
    tabChange (tab) {
      this.activeKey = tab.key;
      this.params.pageNum = 0;
      this.loadData();
    },
    navigateTo (notice) {
      if (notice) {
        uni.navigateTo({ url: '/pages/work/notice/edit?id=' + notice.noticeId })
      } else {
        uni.navigateTo({ url: '/pages/work/notice/edit' })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.list-item {
  padding: 20rpx 0;
  border-bottom: 0.5px solid #ccc;
}
</style>