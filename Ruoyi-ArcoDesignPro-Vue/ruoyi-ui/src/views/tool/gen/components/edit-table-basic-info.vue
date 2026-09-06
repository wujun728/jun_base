<template>
  <a-form
      ref="basicInfoForm"
      :model="form"
      :rules="state.rules"
      :label-col-props="{ span: 6 }"
      :wrapper-col-props="{ span: 18 }"
      label-align="left"
      auto-label-width
  >
    <a-row :gutter="16">
      <a-col :span="8" :offset="2">
        <a-form-item field="tableName" :label="'表名称'">
          <a-input v-model="form.tableName" placeholder="请输入表名称"/>
        </a-form-item>
      </a-col>
      <a-col :span="8" :offset="2">
        <a-form-item field="tableComment" :label="'表描述'">
          <a-input v-model="form.tableComment" placeholder="请输入表描述"/>
        </a-form-item>
      </a-col>
      <a-col :span="8" :offset="2">
        <a-form-item field="className" :label="'实体类名称'">
          <a-input v-model="form.className" placeholder="请输入实体类名称"/>
        </a-form-item>
      </a-col>
      <a-col :span="8" :offset="2">
        <a-form-item field="functionAuthor" :label="'作者'">
          <a-input v-model="form.functionAuthor" placeholder="请输入作者"/>
        </a-form-item>
      </a-col>
      <a-col :span="18" :offset="2">
        <a-form-item label="备注" field="remark">
          <a-textarea v-model="form.remark" type="textarea" placeholder="请输入内容"></a-textarea>
        </a-form-item>
      </a-col>
    </a-row>
  </a-form>
</template>

<script setup lang="ts">

import {computed, reactive, ref, watch} from "vue";
import {GenTableDetailInfo} from "@/api/tool/gen";

const props = defineProps({
  modelValue: {
    type: Object,
    default: () => {},
  }
})

const emits = defineEmits(['update:modelValue']);

const form = computed<GenTableDetailInfo>({
  get() { return props.modelValue as GenTableDetailInfo },
  set(val) { emits('update:modelValue', val) },
})

const state = reactive({
  // 表单校验
  rules: {
    tableName: [
      { required: true, message: "请输入表名称", trigger: "blur" }
    ],
    tableComment: [
      { required: true, message: "请输入表描述", trigger: "blur" }
    ],
    className: [
      { required: true, message: "请输入实体类名称", trigger: "blur" }
    ],
    functionAuthor: [
      { required: true, message: "请输入作者", trigger: "blur" }
    ]
  }
})

const basicInfoForm = ref();
// 验证表单
const validateForm = () => {
  return basicInfoForm.value.validate()
}

defineExpose({
  validateForm,
})

</script>

<style scoped lang="less">

</style>