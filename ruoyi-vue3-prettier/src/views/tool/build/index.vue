<script setup>
const formConfig = {
  rules: {
    Cascader: [
      { required: true, message: 'Cascader不能为空', trigger: 'change' },
    ],
  },
  formItems: [
    {
      label: 'Input',
      field: 'Input',
      type: 'input',
    },
    {
      label: 'InputNumber',
      field: 'InputNumber',
      type: 'InputNumber',
      config: {
        placeholder: '',
      },
    },
    {
      label: 'Textarea',
      field: 'Textarea',
      type: 'Textarea',
      config: {
        clearable: false,
      },
    },
    {
      label: 'Cascader',
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
      label: 'Select',
      field: 'Select',
      type: 'Select',
      slotNames: ['label'],
      options: [
        {
          value: 'Option1',
          label: 'Option1',
        },
        {
          value: 'Option2',
          label: 'Option2',
        },
        {
          value: 'Option3',
          label: 'Option3',
        },
        {
          value: 'Option4',
          label: 'Option4',
        },
        {
          value: 'Option5',
          label: 'Option5',
        },
      ],
    },
    {
      label: 'Tree',
      field: 'Tree',
      type: 'Tree',
      options: [
        {
          label: 'Level one 1',
          children: [
            {
              label: 'Level two 1-1',
              children: [
                {
                  label: 'Level three 1-1-1',
                },
              ],
            },
          ],
        },
      ],
    },
    {
      label: 'TreeSelect',
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
      label: 'Datepicker',
      field: 'Datepicker',
      type: 'datepicker',
    },
    {
      label: 'CheckBox',
      field: 'CheckBox',
      type: 'checkBox',
      isGroup: true,
      options: [
        {
          label: 'Option 1',
          value: '1',
        },
        {
          label: 'Option 2',
          value: '2',
        },
      ],
    },
  ],
}
const formData = ref({})
</script>

<template>
  <div class="default-main page">
    formData: {{ formData }}
    <BaseForm v-bind="formConfig" :data="formData">
      <template #SelectLabel="{ backData }">
        <span>{{ backData.slotData.label }}: </span>
        <span style="font-weight: bold">{{ backData.slotData.value }}</span>
      </template>
    </BaseForm>
  </div>
</template>
<style lang="scss" scoped></style>
