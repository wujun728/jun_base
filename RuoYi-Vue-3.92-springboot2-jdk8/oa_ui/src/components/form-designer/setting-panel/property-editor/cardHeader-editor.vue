<template>
  <el-form-item :label="i18nt('designer.setting.cardHeader')">
    <el-switch v-model="optionModel.cardHeader"></el-switch>
  </el-form-item>
</template>

<script>
  import i18n from "@/utils/i18n"

  export default {
    name: "cardHeader-editor",
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
