<template>
  <div class="demo-content">
    <div class="flex mb9">
      <div class="pr12 customLabel screenWidth">当前屏幕宽度</div>
      <el-tag type="info">{{ innerWidth }}px</el-tag>
    </div>
    <BaseForm :data="formConfig.colLayout" v-bind="layoutFormConfig"></BaseForm>
    <BaseForm :data="formData" v-bind="formConfig"></BaseForm>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
const innerWidth = window.innerWidth
const formData = ref({
  username: '',
  address: '',
  hobby: '',
  more: '',
})
const formConfig = reactive({
  colLayout: {
    xl: 12,
    lg: 12,
    md: 12,
    sm: 24,
    xs: 24,
  },
  formItems: [
    {
      field: 'username',
      label: 'username',
      type: 'input',
    },
    {
      field: 'hobby',
      label: 'hobby',
      type: 'input',
    },
    {
      field: 'address',
      label: 'address',
      type: 'input',
      layout: {
        xl: 24,
        lg: 24,
        md: 24,
        sm: 24,
        xs: 24,
      },
    },
  ],
  elFormConfig: {
    labelWidth: 80,
    labelPosition: 'right',
  },
})

const layoutFormConfig = {
  colLayout: {
    xl: 12,
    lg: 12,
    md: 12,
    sm: 24,
    xs: 24,
  },
  formItems: [
    {
      field: 'xs',
      label: '宽度<768px',
      type: 'inputNumber',
      config: {
        min: 1,
        max: 24,
      },
    },
    {
      field: 'sm',
      label: '宽度≥768px',
      type: 'inputNumber',
      config: {
        min: 1,
        max: 24,
      },
    },
    {
      field: 'md',
      label: '宽度≥992px',
      type: 'inputNumber',
      config: {
        min: 1,
        max: 24,
      },
    },
    {
      field: 'lg',
      label: '宽度≥1200px',
      type: 'inputNumber',
      config: {
        min: 1,
        max: 24,
      },
    },
    {
      field: 'xl',
      label: '宽度≥1920px',
      type: 'inputNumber',
      config: {
        min: 1,
        max: 24,
      },
    },
  ],
  elFormConfig: {
    labelWidth: 100,
    labelPosition: 'right',
  },
}
</script>
<style lang="scss" scoped>
.screenWidth {
  width: 100px;
  text-align: right;
}
</style>
