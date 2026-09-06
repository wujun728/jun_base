<template>
  <div class="page">
    <el-row :gutter="16">
      <el-col :xl="6" :gl="7" :md="8" :sm="12" :xs="24">
        <el-card>
          <template #header>
            <div>
              <span>个人信息</span>
            </div>
          </template>
          <div class="pl10 pr10">
            <div class="text-center">
              <userAvatar :user="state.user" />
            </div>
            <ul class="list-group list-group-striped">
              <li class="list-group-item">
                <div>
                  <svg-icon icon-class="user" />
                  <span class="ml3">用户名称</span>
                </div>
                <div>{{ state.user.userName }}</div>
              </li>
              <li class="list-group-item">
                <div>
                  <svg-icon icon-class="phone" />
                  <span class="ml3">手机号码</span>
                </div>
                <div>{{ state.user.phonenumber }}</div>
              </li>
              <li class="list-group-item">
                <div>
                  <svg-icon icon-class="email" />
                  <span class="ml3">用户邮箱</span>
                </div>
                <div>{{ state.user.email }}</div>
              </li>
              <li class="list-group-item">
                <div>
                  <svg-icon icon-class="tree" />
                  <span class="ml3"> 所属部门 </span>
                </div>
                <div v-if="state.user.dept">
                  {{ state.user.dept.deptName }} / {{ state.postGroup }}
                </div>
              </li>
              <li class="list-group-item">
                <div>
                  <svg-icon icon-class="peoples" />
                  <span class="ml3"> 所属角色 </span>
                </div>
                <div>{{ state.roleGroup }}</div>
              </li>
              <li class="list-group-item">
                <div>
                  <svg-icon icon-class="date" />
                  <span class="ml3">创建日期</span>
                </div>
                <div>{{ state.user.createTime }}</div>
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :xl="18" :gl="17" :md="16" :sm="12" :xs="24">
        <el-card>
          <template #header>
            <div>
              <span>基本资料</span>
            </div>
          </template>
          <el-tabs v-model="activeTab">
            <el-tab-pane label="基本资料" name="userinfo">
              <userInfo :user="state.user" />
            </el-tab-pane>
            <el-tab-pane label="修改密码" name="resetPwd">
              <resetPwd />
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup name="Profile">
import userAvatar from './userAvatar'
import userInfo from './userInfo'
import resetPwd from './resetPwd'
import { getUserProfile } from '@/api/system/user'

const activeTab = ref('userinfo')
const state = reactive({
  user: {},
  roleGroup: {},
  postGroup: {},
})

function getUser() {
  getUserProfile().then((response) => {
    state.user = response.data
    state.roleGroup = response.roleGroup
    state.postGroup = response.postGroup
  })
}

getUser()
</script>
<style lang="scss" scoped>
.page {
  margin: var(--ba-main-space) var(--ba-main-space) 0 var(--ba-main-space);
}

.text-center {
  text-align: center;
}
.list-group-striped > .list-group-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.list-group {
  padding-left: 0px;
  list-style: none;
}

.list-group-item {
  padding: 11px 0px;
  font-size: 14px;
}

.list-group-striped > .list-group-item {
  border-left: 0;
  border-right: 0;
  border-radius: 0;
  padding-left: 0;
  padding-right: 0;
}
</style>
