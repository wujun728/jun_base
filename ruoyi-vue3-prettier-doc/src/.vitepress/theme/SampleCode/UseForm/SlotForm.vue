<template>
  <div class="demo-content">
    <BaseForm :data="formData" v-bind="formConfig">
      <template #usernameHeader="{ backData }">
        <el-tag>usernameHeader插槽返回值</el-tag>
        <div class="language-js language">
          <pre class="github-light github-dark" v-text="backData"></pre>
        </div>
      </template>
      <template #usernameBefore="{ backData }">
        <el-tag>usernameBefore插槽返回值与Header插槽相同</el-tag>
      </template>
      <template #usernameAfter="{ backData }">
        <el-tag>usernameAfter插槽返回值与Header插槽相同</el-tag>
      </template>
      <template #footer>
        <el-tag effect="plain">我是footer插槽</el-tag>
      </template>
      <template #deptCustomLabel="{ backData }">
        <el-button type="primary" size="small">
          <SvgIcon iconClass="plus" size="12"></SvgIcon>
          <span class="ml6">新增部门</span>
        </el-button>
      </template>
    </BaseForm>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
const formConfig = {
  formItems: [
    {
      field: 'username',
      label: '用户名',
      type: 'input',
    },
    {
      field: 'dept',
      label: '部门',
      type: 'input',
    },
  ],
  colLayout: {
    xl: 12,
    lg: 12,
    md: 12,
    sm: 24,
    xs: 24,
  },
  elFormConfig: {
    labelPosition: 'top',
  },
}
const formData = ref({
  username: '',
})
</script>

<style lang="scss" scoped>
.language {
  margin: 16px 0 !important;
}
</style>
