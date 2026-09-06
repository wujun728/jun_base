<template>
  <div class="demo-content">
    <BaseForm ref="baseFormRef" :data="formData" v-bind="formConfig">
      <template #footer>
        <el-button @click="submit" type="primary">提交</el-button>
        <el-button @click="reset">重置</el-button>
      </template>
    </BaseForm>
  </div>
</template>

<script setup>
import { ref, useTemplateRef } from 'vue'
import { ElMessage } from 'element-plus'
const baseFormRef = useTemplateRef('baseFormRef')
const formData = ref({
  username: '',
  sex: '',
  address: '',
})
const formConfig = {
  rules: {
    username: [{ required: true, message: '用户不能为空', trigger: 'blur' }],
    address: [{ required: true, message: '地址不能为空', trigger: 'blur' }],
    sex: [{ required: true, message: '性别不能为空', trigger: 'blur' }],
  },
  formItems: [
    {
      field: 'username',
      label: '姓名',
      type: 'input',
    },
    {
      label: '性别',
      field: 'sex',
      type: 'select',
      options: [
        {
          label: '男',
          value: '1',
        },
        {
          label: '女',
          value: '2',
        },
      ],
    },
    {
      label: '地址',
      field: 'address',
      type: 'textarea',
    },
  ],
  elFormConfig: {
    labelPosition: 'left',
    scrollToError: false,
  },
  colLayout: {
    xl: 12,
    lg: 12,
    md: 12,
    sm: 24,
    xs: 24,
  },
  footerLayout: {
    xl: 12,
    lg: 12,
    md: 12,
    sm: 24,
    xs: 24,
  },
}
const submit = async () => {
  const flag = await baseFormRef.value?.getFormValidate()
  ElMessage({
    type: flag ? 'success' : 'error',
    message: '表单校验结果为' + flag,
    plain: true,
  })
}
const reset = () => {
  baseFormRef.value?.elFormRef.resetFields()
}
</script>
