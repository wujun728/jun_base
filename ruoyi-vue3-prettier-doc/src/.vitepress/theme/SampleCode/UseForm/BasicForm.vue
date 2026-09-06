<template>
  <div class="demo-content">
    <BaseForm :data="formData" v-bind="formConfig"> </BaseForm>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const formData = ref({
  username: '',
  select: '',
  birth: '',
  sex: '',
  hobby: [],
})
const formConfig = {
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
      label: '用户名',
      type: 'input',
      config: {
        placeholder: '请输入用户名',
      },
    },
    {
      label: '类型',
      field: 'select',
      type: 'select',
      options: [
        { label: '选项1', value: '1' },
        { label: '选项2', value: '2' },
      ],
    },
    {
      label: '出生年月',
      field: 'birth',
      type: 'datePicker',
      config: {
        type: 'date',
      },
    },
    {
      label: '树形下拉',
      field: 'TreeSelect',
      type: 'TreeSelect',
      options: [
        {
          value: '2',
          label: 'Level one 2',
          children: [
            {
              value: '2-1',
              label: 'Level two 2-1',
              children: [
                {
                  value: '2-1-1',
                  label: 'Level three 2-1-1',
                },
              ],
            },
            {
              value: '2-2',
              label: 'Level two 2-2',
              children: [
                {
                  value: '2-2-1',
                  label: 'Level three 2-2-1',
                },
              ],
            },
          ],
        },
      ],
    },
    {
      label: '级联下拉',
      field: 'Cascader',
      type: 'Cascader',
      options: [
        {
          value: 'guide',
          label: 'Guide',
          children: [
            {
              value: 'disciplines',
              label: 'Disciplines',
              children: [
                {
                  value: 'consistency',
                  label: 'Consistency',
                },
                {
                  value: 'feedback',
                  label: 'Feedback',
                },
                {
                  value: 'efficiency',
                  label: 'Efficiency',
                },
                {
                  value: 'controllability',
                  label: 'Controllability',
                },
              ],
            },
          ],
        },
      ],
    },
    {
      label: '爱好',
      field: 'hobby',
      type: 'checkBox',
      isGroup: true,
      optionConfig: {
        border: true,
      },
      options: [
        {
          label: '唱',
          value: '1',
        },
        {
          label: '跳',
          value: '2',
        },
      ],
    },
    {
      label: '性别',
      field: 'sex',
      type: 'radio',
      isGroup: true,
      optionConfig: {
        border: true,
      },
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
      label: '数字',
      field: 'InputNumber',
      type: 'InputNumber',
    },
    {
      label: '开关',
      field: 'Switch',
      type: 'Switch',
    },
  ],
  elFormConfig: {
    labelWidth: 80,
  },
}
</script>
