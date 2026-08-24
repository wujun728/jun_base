<template>
  <div>
    <el-form-item :label="i18nt('designer.setting.bottomMargin')">
      <el-input-number
        v-model.number="optionModel.bottomMargin"
        :min="0"
        class="hide-spin-button"
      ></el-input-number>
    </el-form-item>
  </div>
</template>

<script>
import i18n from '@/utils/i18n'
import propertyMixin from '@/components/form-designer/setting-panel/property-editor/propertyMixin'

export default {
  name: 'bottomMargin-editor',
  mixins: [i18n, propertyMixin],
  props: {
    designer: Object,
    selectedWidget: Object,
    optionModel: Object
  }
}
</script>

<style scoped>
</style>
@/utils/i18ns
