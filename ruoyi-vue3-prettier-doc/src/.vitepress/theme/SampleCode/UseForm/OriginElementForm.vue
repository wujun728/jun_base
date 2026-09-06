<template>
  <div class="demo-content">
    <el-switch
      v-model="formConfig.elFormConfig.disabled"
      active-text="禁用"
      inactive-text="启用"
    />
    <BaseForm :data="formData" v-bind="formConfig"></BaseForm>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
const formData = ref({
  username: '',
  hobby: '',
})
const formConfig = reactive({
  elFormConfig: {
    labelPosition: 'top',
    disabled: false,
  },
  formItems: [
    {
      field: 'username',
      label: '姓名',
      type: 'input',
      formItemConfig: {
        size: 'large',
      },
    },
    {
      label: '爱好',
      field: 'hobby',
      type: 'select',
      config: {
        placeholder: '爱好可多选',
        multiple: true,
        collapseTags: true,
        maxCollapseTags: 2,
      },
      setLabel: 'name',
      setValue: 'id',
      options: [
        {
          name: '唱',
          id: '1',
        },
        {
          name: '跳',
          id: '2',
        },
        {
          name: 'rap',
          id: '3',
        },
      ],
    },
  ],
  colLayout: {
    xl: 12,
    lg: 12,
    md: 12,
    sm: 24,
    xs: 24,
  },
})
</script>
