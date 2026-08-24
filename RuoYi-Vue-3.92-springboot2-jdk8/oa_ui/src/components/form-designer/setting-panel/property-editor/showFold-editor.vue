<template>
  <el-form-item :label="i18nt('designer.setting.showFold')">
    <el-switch v-model="optionModel.showFold"></el-switch>
  </el-form-item>
</template>

<script>
  import i18n from "@/utils/i18n"

  export default {
    name: "showFold-editor",
    mixins: [i18n],
    props: {
      designer: Object,
      selectedWidget: Object,
      optionModel: Object,
    },
  }
</script>

<style scoped>

</style>
@/utils/i18ns
