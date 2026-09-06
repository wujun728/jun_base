<template>
  <view class="mobile-item-container">
    <Navbar title="通知公告" bgColor="#fff" :h5Show="false"></Navbar>
    <u-tabs :activeStyle="activeStyle" :list="tabs" @change="tabChange"></u-tabs>
    <!-- 未读 -->
    <view v-if="activeKey == 'unread'">
      <view style="padding: 16px 0 10px;">
        <view class="notice-item-tips">
          <text>通知</text>
          <u-tag text="1" size="mini" type="primary" style="margin: 0 8px;"></u-tag>
          <text>条，公告</text>
          <u-tag text="1" size="mini" type="success" style="margin: 0 8px;"></u-tag>
          <text>条，共 2 条。</text>
        </view>
      </view>
      <Record :list="list" @click="toDetail"></Record>
    </view>
    <!-- 已读 -->
    <view v-if="activeKey == 'read'">
      <view style="padding: 16px 0 10px;">
        <u-search :show-action="true" actionText="搜索" :animation="true" shape="square" height="40px"></u-search>
      </view>
      <Record :list="[]" @click="toDetail"></Record>
    </view>
    <!-- 全部 -->
    <view v-if="activeKey == 'all'">
      <view style="padding: 16px 0 10px;">
        <u-search :show-action="true" actionText="搜索" :animation="true" shape="square" height="40px"></u-search>
      </view>
      <Record :list="list" @click="toDetail"></Record>
    </view>
  </view>
</template>

<script>
import * as NoticeApi from '@/api/work/notice'
import Navbar from '@/components/navbar/Navbar'
import Record from './record'

export default {
  components: {
    Navbar,
    Record,
  },
  data () {
    return {
      activeKey: 'unread',
      tabs: [{
        name: '未读',
        key: 'unread',
        badge: {
          value: 2,
        }
      }, {
        name: '已读',
        key: 'read'
      }, {
        name: '全部',
        key: 'all'
      }],
      params: {
        pageNum: 0,
        pageSize: 10
      },
      list: [],
      activeStyle: {
        color: '#303133',
        fontSize: '20px',
        fontWeight: 'bold',
        transform: 'scale(1.05)'
      }
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
    tabChange (item) {
      this.activeKey = item.key;
      this.params.pageNum = 0;
      this.loadData();
    },
    // 滚动分页加载数据
    scrolltolower () {
      this.loadData();
    },
    toDetail (notice) {
      uni.navigateTo({ url: '/pages/work/notice/detail?id=' + notice.noticeId })
    }
  }
}
</script>

<style lang="scss" scoped>
.notice-item-tips {
  border-radius: 8px;
  background-color: #f4f4f5;
  padding: 9px 16px;
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  align-items: center;
}
</style>