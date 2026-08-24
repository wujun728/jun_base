<template>
  <el-form-item :label="i18nt('designer.setting.required')">
    <span slot="label"
      >{{ i18nt('designer.setting.required') }}
      <el-tooltip effect="light" content="字段设计中开启必填，此字段务必必填">
        <i class="el-icon-info"></i
      ></el-tooltip>
    </span>
    <el-switch v-model="optionModel.required"></el-switch>
  </el-form-item>
</template>

<script>
import i18n from '@/utils/i18n'

export default {
  name: 'required-editor',
  mixins: [i18n],
  props: {
    designer: Object,
    selectedWidget: Object,
    optionModel: Object
  }
}
</script>

<style scoped></style>
@/utils/i18ns
