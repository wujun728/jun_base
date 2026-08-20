<template>
  <view class="mobile-item-container">
    <Navbar title="公告详情" bgColor="#fff" :h5Show="false"></Navbar>
    <view style="font-size: 40rpx; font-weight: bold;">{{notice.noticeTitle}}</view>
    <view style="display: flex; font-size: 12px; color: #909399;">
      <u-icon name="clock" size="12"></u-icon>
      <text>{{notice.createTime}}</text>
      <u-icon name="account" size="12"></u-icon>
      <text>{{notice.remark}}</text>
    </view>
    <view style="margin-top: 40rpx;">
      <u-parse :content="notice.noticeContent"></u-parse>
    </view>
  </view>
</template>

<script>
import * as NoticeApi from '@/api/work/notice'
import Navbar from '@/components/navbar/Navbar'

export default {
  components: {
    Navbar,
  },
  data () {
    return {
      noticeId: undefined,
      notice: {}
    }
  },
  onLoad (params) {
    this.noticeId = params.id
    this.loadData()
  },
  methods: {
    loadData () {
      const app = this
      NoticeApi.noticeById(this.noticeId).then(res => {
        app.notice = res.data
      })
    }
  }
}
</script>

<style lang="scss" scoped>
</style>