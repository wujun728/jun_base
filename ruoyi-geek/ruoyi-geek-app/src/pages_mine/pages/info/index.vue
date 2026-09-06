<template>
  <view class="container">
    <uni-list>
      <uni-list-item showExtraIcon="true" :extraIcon="{ type: 'person-filled' }" title="昵称" :rightText="user.nickName" />
      <uni-list-item showExtraIcon="true" :extraIcon="{ type: 'phone-filled' }" title="手机号码"
        :rightText="user.phonenumber" />
      <uni-list-item showExtraIcon="true" :extraIcon="{ type: 'email-filled' }" title="邮箱" :rightText="user.email" />
      <uni-list-item showExtraIcon="true" :extraIcon="{ type: 'auth-filled' }" title="岗位" :rightText="postGroup" />
      <uni-list-item showExtraIcon="true" :extraIcon="{ type: 'staff-filled' }" title="角色" :rightText="roleGroup" />
      <uni-list-item showExtraIcon="true" :extraIcon="{ type: 'calendar-filled' }" title="创建日期"
        :rightText="user.createTime" />
    </uni-list>

    <u-button @click="register()">绑定微信</u-button>
  </view>
</template>

<script setup>
import { getUserProfile } from "@/api/system/user"
import { ref } from "vue";
import modal from "@/plugins/modal"

const user = ref({})
const roleGroup = ref("")
const postGroup = ref("")
function getUser() {
  getUserProfile().then(response => {
    user.value = response.data
    roleGroup.value = response.roleGroup
    postGroup.value = response.postGroup
  })
}
getUser()

import { wxRegister } from "@/api/oauth"
import { getWxCode } from "@/utils/geek"
function register(){
  modal.loading('绑定微信中...')
  getWxCode().then(res=>{
    wxRegister('miniapp',res).then(res=>{
      modal.closeLoading()
    })
  })
  
}

</script>

<style lang="scss">
page {
  background-color: #ffffff;
}
</style>
