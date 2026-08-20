<template>
  <view style="padding: 40rpx;">
    <view>
      <u-search placeholder="请输入查询内容" v-model="params.title" height="60rpx" @search="searchData" @custom="searchData"></u-search>
    </view>
    <view>
      <u-list v-if="logList.length > 0" @scrolltolower="scrolltolower">
        <u-list-item v-for="(item, index) in logList" :key="index" class="log-item">
          <view><text style="color: #606266;">操作内容：</text><text>{{item.title}}</text></view>
          <view><text style="color: #606266;">请求地址：</text><text>{{item.operUrl}}</text></view>
          <view><text style="color: #606266;">操作地址：</text><text>{{item.operLocation}}</text></view>
          <view><text style="color: #606266;">操作时间：</text><text>{{item.operTime}}</text></view>
        </u-list-item>
      </u-list>
      <u-empty v-else></u-empty>
    </view>
  </view>
</template>

<script>
import * as LogApi from '@/api/center/log'

export default {
  data () {
    return {
      params: {
        pageNum: 0,
        pageSize: 10,
        title: ''
      },
      logList: []
    }
  },
  created () {
    this.loadData();
  },
  methods: {
    // 加载日志列表数据
    loadData () {
      const app = this
      // 首先获取当前登录账号信息
      app.$store.dispatch('Info').then(res => {
        app.params.pageNum += 1
        if (res.user) {
          // 只查询当前用户的操作日志
          app.params.operName = res.user.userName
        }
        LogApi.operLog(app.params).then(res => {
          app.logList = app.logList.concat(res.rows);
        })
      })
    },
    // 查询按钮动作
    searchData () {
      this.params.pageNum = 0
      this.logList = []
      this.loadData();
    },
    // 滚动分页加载数据
    scrolltolower () {
      this.loadData();
    }
  }
}
</script>

<style lang="scss" scoped>
.log-item {
  padding: 20rpx 0;
  border-bottom: 0.5px solid #ccc;
}
</style>