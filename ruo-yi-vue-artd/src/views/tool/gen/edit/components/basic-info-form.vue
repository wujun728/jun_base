<template>
  <ArtForm
    ref="basicInfoForm"
    v-model="infoModel"
    :items="formItems"
    :rules="rules"
    :show-submit="false"
    :show-reset="false"
    label-width="150px" />
</template>

<script setup lang="ts">
import type { GenTable } from '@/types/api/tool/gen'

import ArtForm from '@/components/core/forms/art-form/index.vue'

const formRef = useTemplateRef<InstanceType<typeof ArtForm>>('basicInfoForm')

const infoModel = defineModel<GenTable>('info', {
  default: () => ({ view: false }) as GenTable
})

const formItems = [
  {
    key: 'tableName',
    label: '表名称',
    type: 'input',
    span: 12,
    props: { placeholder: '请输入仓库名称' }
  },
  {
    key: 'tableComment',
    label: '表描述',
    type: 'input',
    span: 12,
    props: { placeholder: '请输入' }
  },
  {
    key: 'className',
    label: '实体类名称',
    type: 'input',
    span: 12,
    props: { placeholder: '请输入' }
  },
  {
    key: 'functionAuthor',
    label: '作者',
    type: 'input',
    span: 12,
    props: { placeholder: '请输入' }
  },
  {
    key: 'remark',
    label: '备注',
    type: 'input',
    span: 24,
    props: { type: 'textarea', rows: 3 }
  }
]

const rules = ref({
  tableName: [{ required: true, message: '请输入表名称', trigger: 'blur' }],
  tableComment: [{ required: true, message: '请输入表描述', trigger: 'blur' }],
  className: [{ required: true, message: '请输入实体类名称', trigger: 'blur' }],
  functionAuthor: [{ required: true, message: '请输入作者', trigger: 'blur' }]
})

async function validateForm() {
  if (!formRef.value) return
  return await formRef.value.validate()
}

defineExpose({ validateForm })
</script>
