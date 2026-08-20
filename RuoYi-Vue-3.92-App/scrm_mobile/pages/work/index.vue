<template>
	<view>
    <Navbar title="工作台" bgColor="#fff" :hideBtn="true" :h5Show="false"></Navbar>
    <view class="mobile-item-container">
      <view style="font-size: 40rpx; font-weight: bold; padding: 0 0 40rpx 0;">待处理</view>
      <u-grid col="3">
        <u-grid-item @click="navigateTo('/pages/work/notice/list')">
          <view style="position: relative;">
            <u-icon name="chat" color="#2979ff" size="60rpx"></u-icon>
            <u-badge style="position: absolute; top: -5rpx; right: -10rpx;" type="error" numberType="overflow" max="99" v-model="todo.notice"></u-badge>
          </view>
          <text class="btn-text">通知公告</text>
        </u-grid-item>
        <u-grid-item>
          <view style="position: relative;">
            <u-icon name="list-dot" color="#2979ff" size="60rpx"></u-icon>
            <u-badge style="position: absolute; top: -5rpx; right: -10rpx;" type="error" numberType="overflow" max="99" v-model="todo.task"></u-badge>
          </view>
          <text class="btn-text">待办任务</text>
        </u-grid-item>
        <u-grid-item>
          <view style="position: relative;">
            <u-icon name="warning" color="#2979ff" size="60rpx"></u-icon>
            <u-badge style="position: absolute; top: -5rpx; right: -10rpx;" type="error" numberType="overflow" max="99" v-model="todo.error"></u-badge>
          </view>
          <text class="btn-text">异常信息</text>
        </u-grid-item>
      </u-grid>
    </view>
    <Gap />
    <view class="mobile-item-container">
      <view style="font-size: 40rpx; font-weight: bold; padding: 0 0 40rpx 0;">统计报表</view>
      <u-grid col="3">
        <u-grid-item>
          <u-icon name="star" color="#2979ff" size="60rpx"></u-icon>
          <text class="btn-text">访问量统计</text>
        </u-grid-item>
        <u-grid-item>
          <u-icon name="share-square" color="#2979ff" size="60rpx"></u-icon>
          <text class="btn-text">分享统计</text>
        </u-grid-item>
      </u-grid>
    </view>
    <Gap />
    <view style="padding: 40rpx; background-color: #fff; margin-top: 40rpx; min-height: 600rpx;">
      <u-tabs
        :activeStyle="{
          color: '#303133',
          fontSize: '40rpx',
          fontWeight: 'bold',
          transform: 'scale(1.05)'
        }"
        :inactiveStyle="{
          color: '#606266',
          fontSize: '36rpx',
          transform: 'scale(1)'
        }"
        :list="tabs"
        @change="tabChange">
      </u-tabs>
      <view v-if="activeKey == 'work'" style="padding: 40rpx 0;">
        <u-grid col="3">
          <u-grid-item @click="navigateTo('/pages/work/notice/manage')">
            <u-icon name="edit-pen" color="#2979ff" size="60rpx"></u-icon>
            <text class="btn-text">公告管理</text>
          </u-grid-item>
          <u-grid-item @click="navigateTo('/pages/work/user/list')">
            <u-icon name="plus-people-fill" color="#2979ff" size="60rpx"></u-icon>
            <text class="btn-text">用户管理</text>
          </u-grid-item>
          <u-grid-item>
            <u-icon name="tags" color="#2979ff" size="60rpx"></u-icon>
            <text class="btn-text">部门管理</text>
          </u-grid-item>
        </u-grid>
        <u-grid col="3" style="margin-top: 40rpx;">
          <u-grid-item>
            <u-icon name="bookmark" color="#2979ff" size="60rpx"></u-icon>
            <text class="btn-text">岗位管理</text>
          </u-grid-item>
        </u-grid>
      </view>
      <view v-if="activeKey == 'plugin'" style="padding: 40rpx 0;">
        <u-grid col="3">
          <u-grid-item>
            <u-icon name="order" color="#2979ff" size="60rpx"></u-icon>
            <text class="btn-text">表单样例</text>
          </u-grid-item>
        </u-grid>
      </view>
    </view>
	</view>
</template>

<script>
import Navbar from '@/components/navbar/Navbar'
import Gap from '@/components/gap/Gap'

export default {
  components: {
    Gap,
    Navbar,
  },
  data () {
    return {
      activeKey: 'work',
      tabs: [{
        name: '日常工作',
        key: 'work'
      }, {
        name: '插件',
        key: 'plugin'
      }],
      todo: {
        notice: 2,
        task: 5,
        error: 1
      }
    }
  },
  methods: {
    tabChange (item) {
      this.activeKey = item.key;
    },
    navigateTo (url) {
      uni.navigateTo({ url: url })
    }
  }
}
</script>

<style lang="scss">
</style>