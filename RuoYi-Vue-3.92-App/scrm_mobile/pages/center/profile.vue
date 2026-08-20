<template>
  <view>
    <Navbar :hideBtn="false" bgColor="#f3f4f6"></Navbar>
    <view style="background-color: #2b85e4; padding: 40rpx;">
      <view style="width: 140rpx; height: 140rpx; border: 1px solid #fff; border-radius: 50%; margin: 0 auto;">
        <u-avatar src="/static/img/avatar.png" size="120rpx" style="margin: 10rpx;"></u-avatar>
      </view>
    </view>
    <view style="padding: 40rpx;">
      <u--form :model="userInfo" ref="uForm" labelWidth="160rpx" labelAlign="left">
        <u-form-item label="姓名" prop="nickName" class="u-border-bottom">
          <u--input
            placeholder="请输入内容"
            border="none"
            v-model="userInfo.nickName"
          ></u--input>
        </u-form-item>
        <u-form-item label="性别" prop="sex" class="u-border-bottom">
          <u-radio-group v-model="userInfo.sex" size="36rpx">
            <u-radio shape="circle" label="男" name="1" labelSize="32rpx"></u-radio>
            <u-radio shape="circle" label="女" name="2" labelSize="32rpx" style="margin-left: 36rpx;"></u-radio>
          </u-radio-group>
        </u-form-item>
        <u-form-item label="手机号码" prop="phonenumber" class="u-border-bottom">
          <u--input
            placeholder="请输入内容"
            border="none"
            v-model="userInfo.phonenumber"
          ></u--input>
        </u-form-item>
        <u-form-item label="邮箱" prop="email" class="u-border-bottom">
          <u--input
            placeholder="请输入内容"
            border="none"
            v-model="userInfo.email"
          ></u--input>
        </u-form-item>
      </u--form>
    </view>
    <view style="padding: 40rpx;">
      <u-row gutter="32">
        <u-col span="6">
          <u-button icon="arrow-left" text="返回" plain @click="goBack()"></u-button>
        </u-col>
        <u-col span="6">
		      <u-button icon="checkmark-circle" text="保存" type="primary"></u-button>
        </u-col>
      </u-row>
    </view>
  </view>
</template>

<script>
import Navbar from '@/components/navbar/Navbar'

export default {
  components: {
    Navbar,
  },
  data () {
    return {
      userInfo: {
        nickName: '若依',
        sex: '1',
        email: 'fastbuild@163.com',
        phonenumber: '18888888888'
      }
    }
  },
  methods: {
    goBack () {
      uni.navigateBack({ delta: 1});
    }
  }
}
</script>

<style lang="sass" scoped>

</style>