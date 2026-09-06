<template>
  <div>
    <a-modal v-model:open="pageData.visible" title="用户详细信息" width="60%">
      <template #footer>
        <a-button @click="pageData.visible = false">关闭</a-button>
      </template>
      <a-form ref="userDetailFormRef" :labelCol="{ span: 8 }" :model="userDetailForm.data" :wrapperCol="{ span: 14 }"
              class="bearjia-detail-page" name="userDetailForm">
        <a-row :gutter="24">
          <a-col span="12">
            <a-form-item label="用户账号" name="userName">
              <span>{{ userDetailForm.data.userName }}</span>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item label="用户名称" name="nickName">
              <span>{{ userDetailForm.data.nickName }}</span>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="性别" name="sex">
              <span>{{ BearJiaUtil.getDictLabelByKey(userDetailForm.data.sex, pageData.sexDict) }}</span>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="所属部门" name="deptId">
              <span>{{ BearJiaUtil.getDeptNameByDeptId(userDetailForm.data.deptId, pageData.deptTreeData) }}</span>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="岗位" name="postIds">
              <span>{{ BearJiaUtil.getPostNamesByPostIds(userDetailForm.data.postIds, pageData.postDict) }}</span>
            </a-form-item>
          </a-col>

          <a-col :span="12">
            <a-form-item label="角色" name="roleIds">
              <span>{{ BearJiaUtil.getRoleNamesByRoleIds(userDetailForm.data.roleIds, pageData.roleDict) }}</span>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="手机号码" name="phonenumber">
              <span>{{ userDetailForm.data.phonenumber }}</span>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="邮箱" name="email">
              <span>{{ userDetailForm.data.email }}</span>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="状态" name="status">
              <span>{{ BearJiaUtil.getDictLabelByKey(userDetailForm.data.status, pageData.statusDict) }}</span>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import {reactive, ref} from 'vue';
import BearJiaUtil from '@/utils/BearJiaUtil.js';
import {getUser} from '@/api/system/user';

// 当前页面使用的数据
const pageData = reactive({
  visible: false,
  sexDict: [],
  deptTreeData: [],
  statusDict: [],
  postDict: [],
  roleDict: [],
});
BearJiaUtil.getDictsByType('sys_user_sex').then((res) => {
  pageData.sexDict = res;
});
BearJiaUtil.getDeptTreeData().then((res) => {
  pageData.deptTreeData = res.data;
});
BearJiaUtil.getDictsByType('sys_normal_disable').then((res) => {
  pageData.statusDict = res;
});

//详细Form
const userDetailFormRef = ref();
const userDetailForm = reactive({data: {postIds: [], roleIds: []}});
// 打开详细窗口
const openModal = (record) => {
  pageData.visible = true;
  getUser(record.userId).then((response) => {
    userDetailForm.data = response.data;
    pageData.postDict = BearJiaUtil.getPostDict(response.posts);
    pageData.roleDict = BearJiaUtil.getRoleDict(response.roles);
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
