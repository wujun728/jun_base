<template>
  <div class="demo-content">
    <div class="disabled-switch mb19">
      <div class="pr12">disabled</div>
      <el-switch
        class="switch"
        v-model="allDisabled"
        active-text="禁用"
        inactive-text="启用"
      />
    </div>
    <div class="pr20 hide-item">
      <div class="pr12">隐藏项</div>
      <el-input-tag
        class="input-tag"
        v-model="hideItems"
        placeholder="请输入需要隐藏的表单项,field属性"
      />
    </div>
    <BaseForm
      :data="formData"
      v-bind="formConfig"
      :hideItems="hideItems"
      :allDisabled="allDisabled"
    ></BaseForm>
  </div>
</template>

<script setup>
import { ref } from 'vue'
const formData = ref({
  username: '',
  address: '',
  hobby: '',
  more: '',
})
const allDisabled = ref(false)
const hideItems = ref(['more'])
const formConfig = {
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
    },
    {
      field: 'more',
      label: 'more',
      type: 'input',
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

<style lang="scss" scoped>
.hide-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 10px;
  .input-tag {
    flex: 1;
  }
  div {
    text-align: right;
    width: 80px;
    font-weight: 500;
    font-size: var(--el-font-size-base);
    color: var(--el-text-color-primary);
  }
}
.disabled-switch {
  display: flex;
  align-items: center;
  div {
    text-align: right;
    width: 80px;
    font-weight: 500;
    font-size: var(--el-font-size-base);
    color: var(--el-text-color-primary);
  }
  .switch {
    flex: 1;
  }
}
</style>
