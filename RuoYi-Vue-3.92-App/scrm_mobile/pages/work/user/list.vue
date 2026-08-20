<template>
  <view class="mobile-item-container">
    <Navbar title="用户管理" bgColor="#fff" :h5Show="false"></Navbar>
    <view style="padding: 16px 0 10px;">
      <u-search :show-action="true" actionText="搜索" :animation="true" height="40px"></u-search>
    </view>
    <view v-if="list && list.length > 0">
      <u-cell v-for="(item, index) in list" :key="index" :isLink="true" :border="true" @click="navigateTo(item)">
        <u-avatar slot="icon" v-if="item.avatar" :src="item.avatar"></u-avatar>
        <u-avatar slot="icon" v-else :text="item.remark.substring(0, 1)" randomBgColor></u-avatar>
        <view slot="title">
          <view style="display: flex; padding: 8px 0;">
            <text style="font-size: 18px; font-weight: bold;">{{item.remark}}</text>
            <u-tag :text="item.delFlag == 0 ? '启用' : '停用'" :type="item.delFlag == 0 ? 'primary' : 'error'" shape="circle" size="mini" style="margin-left: 8px;"></u-tag>
          </view>
          <view style="display: flex; justify-content:space-between;">
            <text>部门：{{item.dept.deptName}}</text>
            <text>电话：{{item.phonenumber}}</text>
          </view>
          <view>
            <text>邮件：{{item.email}}</text>
          </view>
        </view>
        <view slot="label">
        </view>
      </u-cell>
      <!-- <u-loadmore :status="status" /> -->
    </view>
    <u-empty v-else></u-empty>
    <FloatButton type="primary" icon="plus" @click="navigateTo"></FloatButton>
  </view>
</template>

<script>
import * as UserManageApi from '@/api/work/userManage'
import Navbar from '@/components/navbar/Navbar'
import FloatButton from '@/components/button/FloatButton'

export default {
  components: {
    Navbar,
    FloatButton
  },
  data () {
    return {
      params: {
        pageNum: 0,
        pageSize: 10
      },
      list: []
    }
  },
  onLoad () {
    this.loadData();
  },
  methods: {
    // 加载用户列表数据
    loadData () {
      const app = this
      this.params.pageNum += 1;
      UserManageApi.userList(this.params).then(res => {
        app.list = res.rows;
      })
    },
    navigateTo (user) {
      if (user) {
        uni.navigateTo({ url: '/pages/work/user/edit?id=' + user.userId })
      } else {
        uni.navigateTo({ url: '/pages/work/user/edit' })
      }
    }
  }
}
</script>