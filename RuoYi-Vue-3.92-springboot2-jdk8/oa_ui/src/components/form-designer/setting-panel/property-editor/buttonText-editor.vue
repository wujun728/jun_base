<template>
  <el-form-item :label="i18nt('designer.setting.buttonText')">
    <el-input type="text" v-model="optionModel.buttonText"></el-input>
  </el-form-item>
</template>

<script>
  import i18n from "@/utils/i18n"

  export default {
    name: "buttonText-editor",
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
