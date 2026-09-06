<template>
  <div>
    <a-modal v-model:visible="pageData.visible" width="60%" title="用户详细信息">
      <template #footer>
        <a-button @click="pageData.visible = false">关闭</a-button>
      </template>
      <a-form class="autoee-detail-page" ref="userDetailFormRef" name="userDetailForm" :model="userDetailForm.data" :labelCol="{ span: 8 }" :wrapperCol="{ span: 14 }">
        <a-row :gutter="24">
          <a-col span="12">
            <a-form-item name="userName" label="用户账号">
              <span>{{ userDetailForm.data.userName }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="nickName" label="用户名称">
              <span>{{ userDetailForm.data.nickName }}</span>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item name="sex" label="性别">
              <span>{{ AutoeeUtil.getDictLabelByKey(userDetailForm.data.sex, pageData.sexDict) }}</span>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item name="deptId" label="所属部门">
              <span>{{ AutoeeUtil.getDeptNameByDeptId(userDetailForm.data.deptId, pageData.deptTreeData) }}</span>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item name="postIds" label="岗位">
              <span>{{ AutoeeUtil.getPostNamesByPostIds(userDetailForm.data.postIds, pageData.postDict) }}</span>
            </a-form-item>
          </a-col>

          <a-col :span="12">
            <a-form-item name="roleIds" label="角色">
              <span>{{ AutoeeUtil.getRoleNamesByRoleIds(userDetailForm.data.roleIds, pageData.roleDict) }}</span>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item name="phonenumber" label="手机号码">
              <span>{{ userDetailForm.data.phonenumber }}</span>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item name="email" label="邮箱">
              <span>{{ userDetailForm.data.email }}</span>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item name="status" label="状态">
              <span>{{ AutoeeUtil.getDictLabelByKey(userDetailForm.data.status, pageData.statusDict) }}</span>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue';
import AutoeeUtil from '@/utils/AutoeeUtil.js';
import { getUser } from '@/api/system/user';

// 当前页面使用的数据
const pageData = reactive({
  visible: false,
  sexDict: [],
  deptTreeData: [],
  statusDict: [],
  postDict: [],
  roleDict: [],
});
AutoeeUtil.getDictsByType('sys_user_sex').then((res) => {
  pageData.sexDict = res;
});
AutoeeUtil.getDeptTreeData().then((res) => {
  pageData.deptTreeData = res.data;
});
AutoeeUtil.getDictsByType('sys_normal_disable').then((res) => {
  pageData.statusDict = res;
});

//详细Form
const userDetailFormRef = ref();
    const userDetailForm = reactive({ data: { postIds: [], roleIds:[]} });
// 打开详细窗口
const openModal = (record) => {
  pageData.visible = true;
  getUser(record.userId).then((response) => {
    userDetailForm.data = response.data;
    pageData.postDict = AutoeeUtil.getPostDict(response.posts);
    pageData.roleDict = AutoeeUtil.getRoleDict(response.roles);
    userDetailForm.data.postIds = response.postIds;
    userDetailForm.data.roleIds = response.roleIds;
  });
};

// 对外暴露出去
defineExpose({
  openModal,
});
</script>

<style lang="less"></style>
