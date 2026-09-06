<template>
  <div class="demo-content">
    <div><span class="font">表单值:</span> {{ formData }}</div>
    <BaseForm v-bind="formConfig" :data="formData">
      <template #colorCustom="{ backData }">
        <el-color-picker v-model="backData.formData.color" />
      </template>
    </BaseForm>
  </div>
</template>

<script setup>
import { ref } from 'vue'
const formData = ref({
  username: '',
  color: '#626aef',
})
const formConfig = {
  formItems: [
    {
      field: 'username',
      label: '用户名',
      type: 'input',
    },
    {
      field: 'color',
      label: '颜色',
      type: 'custom',
    },
  ],
  elFormConfig: {
    labelWidth: 80,
    labelPosition: 'right',
  },
  colLayout: {
    xl: 12,
    lg: 12,
    md: 12,
    sm: 24,
    xs: 24,
  },
}
</script>
