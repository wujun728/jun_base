<template>
  <el-form-item :label="i18nt('designer.setting.textContent')">
    <el-input v-model="optionModel.textContent" type="textarea" :rows="3"></el-input>
  </el-form-item>
</template>

<script>
  import i18n from "@/utils/i18n"

  export default {
    name: "textContent-editor",
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
