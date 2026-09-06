<script setup>
defineProps({
  info: {
    type: Object,
    default: () => ({}),
  },
})

const basicInfoFormRef = useTemplateRef('basicInfoFormRef')

const rules = {
  tableName: [{ required: true, message: '请输入表名称', trigger: 'blur' }],
  tableComment: [{ required: true, message: '请输入表描述', trigger: 'blur' }],
  className: [{ required: true, message: '请输入实体类名称', trigger: 'blur' }],
  functionAuthor: [{ required: true, message: '请输入作者', trigger: 'blur' }],
}

defineExpose({
  basicInfoFormRef,
})
</script>

<template>
  <a-form
    ref="basicInfoFormRef"
    :model="info"
    :rules="rules"
    :label-col="{ span: 6 }"
    :wrapper-col="{ span: 16 }"
  >
    <a-row :gutter="16">
      <a-col :span="12">
        <a-form-item label="表名称" name="tableName">
          <a-input v-model:value="info.tableName" placeholder="请输入表名称" />
        </a-form-item>
      </a-col>
      <a-col :span="12">
        <a-form-item label="表描述" name="tableComment">
          <a-input v-model:value="info.tableComment" placeholder="请输入表描述" />
        </a-form-item>
      </a-col>
      <a-col :span="12">
        <a-form-item label="实体类名称" name="className">
          <a-input v-model:value="info.className" placeholder="请输入实体类名称" />
        </a-form-item>
      </a-col>
      <a-col :span="12">
        <a-form-item label="作者" name="functionAuthor">
          <a-input v-model:value="info.functionAuthor" placeholder="请输入作者" />
        </a-form-item>
      </a-col>
      <a-col :span="24">
        <a-form-item
          label="备注"
          name="remark"
          :label-col="{ span: 3 }"
          :wrapper-col="{ span: 20 }"
        >
          <a-textarea v-model:value="info.remark" :rows="3" />
        </a-form-item>
      </a-col>
    </a-row>
  </a-form>
</template>
