<template>
  <u-modal :show="show" title="修改密码" showCancelButton @confirm="confirm" @cancel="cancel">
    <view class="slot-content">
      <u--form :model="pwd" ref="uForm" labelWidth="160rpx" labelAlign="left">
        <u-form-item label="原密码" prop="origiPwd" class="u-border-bottom">
          <u--input v-model="pwd.origiPwd" type="password" placeholder="请输入原密码" border="none"></u--input>
        </u-form-item>
        <u-form-item label="新密码" prop="newPwd" class="u-border-bottom">
          <u--input v-model="pwd.newPwd" type="password" placeholder="请输入新密码" border="none"></u--input>
        </u-form-item>
        <u-form-item label="确认密码" prop="confirmPwd" class="u-border-bottom">
          <u--input v-model="pwd.confirmPwd" type="password" placeholder="请输入确认密码" border="none"></u--input>
        </u-form-item>
      </u--form>
    </view>
  </u-modal>
</template>

<script>
export default {
  props: {
    show: Boolean
  },
  data () {
    return {
      pwd: {
        origiPwd: '',
        newPwd: '',
        confirmPwd: ''
      }
    }
  },
  methods: {
    confirm () {
      this.$emit('close')
    },
    cancel () {
      this.$emit('close')
    }
  }
}
</script>

<style lang="scss" scoped>
</style>