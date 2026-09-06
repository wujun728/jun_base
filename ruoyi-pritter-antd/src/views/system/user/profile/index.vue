<script setup name="Profile">
import userAvatar from './userAvatar.vue'
import userInfo from './userInfo.vue'
import resetPwd from './resetPwd.vue'
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

<template>
  <div class="default-main page">
    <a-row :gutter="16">
      <a-col :xl="6" :lg="7" :md="8" :sm="12" :xs="24">
        <a-card title="个人信息">
          <div class="pl10 pr10">
            <div class="text-center">
              <userAvatar :user="state.user" />
            </div>
            <ul class="list-group list-group-striped">
              <li class="list-group-item">
                <div>
                  <SvgIcon iconClass="user" />
                  <span class="ml3">用户名称</span>
                </div>
                <div>{{ state.user.userName }}</div>
              </li>
              <li class="list-group-item">
                <div>
                  <SvgIcon iconClass="phone" />
                  <span class="ml3">手机号码</span>
                </div>
                <div>{{ state.user.phonenumber }}</div>
              </li>
              <li class="list-group-item">
                <div>
                  <SvgIcon iconClass="email" />
                  <span class="ml3">用户邮箱</span>
                </div>
                <div>{{ state.user.email }}</div>
              </li>
              <li class="list-group-item">
                <div>
                  <SvgIcon iconClass="tree" />
                  <span class="ml3">所属部门</span>
                </div>
                <div v-if="state.user.dept">
                  {{ state.user.dept.deptName }} / {{ state.postGroup }}
                </div>
              </li>
              <li class="list-group-item">
                <div>
                  <SvgIcon iconClass="peoples" />
                  <span class="ml3">所属角色</span>
                </div>
                <div>{{ state.roleGroup }}</div>
              </li>
              <li class="list-group-item">
                <div>
                  <SvgIcon iconClass="date" />
                  <span class="ml3">创建日期</span>
                </div>
                <div>{{ state.user.createTime }}</div>
              </li>
            </ul>
          </div>
        </a-card>
      </a-col>
      <a-col :xl="18" :lg="17" :md="16" :sm="12" :xs="24">
        <a-card title="基本资料">
          <a-tabs v-model:activeKey="activeTab">
            <a-tab-pane key="userinfo" tab="基本资料">
              <userInfo :user="state.user" />
            </a-tab-pane>
            <a-tab-pane key="resetPwd" tab="修改密码">
              <resetPwd />
            </a-tab-pane>
          </a-tabs>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<style lang="scss" scoped>
.text-center {
  text-align: center;
}

.list-group-striped > .list-group-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.list-group {
  padding-left: 0;
  list-style: none;
}

.list-group-item {
  padding: 11px 0;
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
