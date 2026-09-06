<template>
  <div class="container">
    <!--面包屑-->
    <Breadcrumb :items="['系统管理', '用户管理', '分配角色']"/>
    <a-card
        class="general-card r-nav-card"
        :body-style="{ padding: '15px' }"
        :header-style="{ padding: '15px' }"
    >
      <a-row>
        <a-col>
          <h4 class="form-header">基本信息</h4>
          <a-form
              :model="state.form"
              :label-col-props="{ span: 6 }"
              :wrapper-col-props="{ span: 18 }"
              label-align="left"
              auto-label-width
          >
            <a-row :gutter="16">
              <a-col :span="8" :offset="2">
                <a-form-item field="nickName" :label="'用户昵称'">
                  <a-input
                      v-model="state.form.nickName"
                      disabled
                  />
                </a-form-item>
              </a-col>
              <a-col :span="8" :offset="2">
                <a-form-item field="userName" :label="'登录账号'">
                  <a-input
                      v-model="state.form.userName"
                      disabled
                  />
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </a-col>
        <a-col>
          <h4 class="form-header">角色信息</h4>
          <a-table
              v-model:selectedKeys="state.selectedKeys" row-key="roleId" :columns="state.columns" :data="state.roles"
              :row-selection="state.rowSelection" :pagination="state.pagination"
          />
        </a-col>
        <a-col class="submit-nav-button">
          <a-space :size="16">
            <a-button type="primary" @click="submitForm()">提交</a-button>
            <a-button @click="close()">返回</a-button>
          </a-space>
        </a-col>
      </a-row>
    </a-card>
  </div>
</template>

<script lang="ts" setup>
import {reactive} from 'vue';
import {useRouter} from 'vue-router';
import {useTabBarStore} from '@/store';
import {getAuthRole, updateAuthRole, UserRecord} from '@/api/system/user';
import {RoleRecord} from "@/api/system/role";
import {Message} from "@arco-design/web-vue";
import {TagProps} from "@/store/modules/tab-bar/types";

const router = useRouter();
const tabBarStore = useTabBarStore();
const state = reactive({
  // 用户信息
  form: {} as UserRecord,
  // 角色信息
  roles: [],
  selectedKeys: [] as number[],
  rowSelection: {
    type: 'checkbox',
    showCheckedAll: true,
    onlyCurrent: false,
  } as unknown,
  pagination: {
    current: 1,
    pageSize: 10,
    total: 0,
  },
  columns: [
    {
      title: '角色编号',
      dataIndex: 'roleId',
      slotName: 'roleId',
      width: 120,
    },
    {
      title: '角色名称',
      dataIndex: 'roleName',
      width: 150,
    },
    {
      title: '权限字符',
      dataIndex: 'roleKey',
      width: 150,
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      slotName: 'createTime',
      width: 180,
    },
  ]
});

const getUserForm = async () => {
  const userId = router.currentRoute.value.params && router.currentRoute.value.params.userId;
  if (userId) {
    const {user, roles} = await getAuthRole(userId as string);
    state.form = user;
    state.roles = roles;
    state.pagination.total = roles.length;
    roles.forEach((item: RoleRecord) => {
      if (item.flag) {
        state.selectedKeys.push(item.roleId)
      }
    })
  }
};
getUserForm();

// 关闭按钮
const close = () => {
  const tabList = tabBarStore.getTabList;
  const tag = tabList.find((x) => x.name === router.currentRoute.value.name) as TagProps
  const idx = tabList.findIndex((x) => x.name === tag.name)
  tabBarStore.deleteTag(idx, tag);
  const latest = tabList[idx - 1]; // 获取队列的前一个tab
  router.push({name: latest.name});

}

// 提交按钮
const submitForm = async () => {
  const {userId} = state.form;
  const roleIds = state.selectedKeys.join(",");
  await updateAuthRole({userId, roleIds});
  Message.success("授权成功");
  close();
}

</script>

<style scoped lang="less">

.form-header {
  font-size: 15px;
  color: #6379bb;
  border-bottom: 1px solid #ddd;
  margin: 8px 10px 25px 10px;
  padding-bottom: 5px
}

.container {
  padding: 0 20px 20px 20px;
}

.submit-nav-button {
  text-align: center;
}
</style>
