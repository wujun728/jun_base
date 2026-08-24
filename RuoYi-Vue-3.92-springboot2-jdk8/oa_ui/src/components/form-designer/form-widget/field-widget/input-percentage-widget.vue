// 百分比组件
<template>
  <form-item-wrapper
    :designer="designer"
    :field="field"
    :rules="rules"
    :design-state="designState"
    :parent-widget="parentWidget"
    :parent-list="parentList"
    :index-of-parent-list="indexOfParentList"
    :sub-form-row-index="subFormRowIndex"
    :sub-form-col-index="subFormColIndex"
    :sub-form-row-id="subFormRowId"
  >
    <el-slider
      v-show="!getReadMode() || field.isShow"
      ref="fieldEditor"
      v-model="fieldModel"
      :disabled="field.options.disabled"
      :show-stops="field.options.showStops"
      :min="Number(field.options.min)"
      :max="Number(field.options.max)"
      :step="Number(field.options.step)"
      :range="false"
      :vertical="field.options.vertical"
      @change="handleChangeEvent($event, fieldModel)"
    >
    </el-slider>
    <template v-if="getReadMode() && !field.isShow">
      <div class="readonly-mode-field" @click="isShowFn">{{ contentForReadMode || '--' }} %</div>
    </template>
  </form-item-wrapper>
</template>

<script>
import FormItemWrapper from './form-item-wrapper'
import emitter from '@/utils/emitter'
import i18n, { translate } from '@/utils/i18n'
import fieldMixin from '@/components/form-designer/form-widget/field-widget/fieldMixin'

export default {
  name: 'input-percentage-widget',
  componentName: 'FieldWidget', //必须固定为FieldWidget，用于接收父级组件的broadcast事件
  mixins: [emitter, fieldMixin, i18n],
  props: {
    field: Object,
    parentWidget: Object,
    parentList: Array,
    indexOfParentList: Number,
    designer: Object,

    designState: {
      type: Boolean,
      default: false
    },

    subFormRowIndex: {
      /* 子表单组件行索引，从0开始计数 */ type: Number,
      default: -1
    },
    subFormColIndex: {
      /* 子表单组件列索引，从0开始计数 */ type: Number,
      default: -1
    },
    subFormRowId: {
      /* 子表单组件行Id，唯一id且不可变 */ type: String,
      default: ''
    }
  },
  components: {
    FormItemWrapper
  },
  inject: ['refList', 'formConfig', 'globalOptionData', 'globalModel'],
  data() {
    return {
      oldFieldValue: 0, //field组件change之前的值
      fieldModel: 0,

      rules: []
    }
  },
  watch: {
    field(val) {}
  },
  computed: {
    contentForReadMode() {
      return this.fieldModel ? this.fieldModel : '--'
    }
  },
  beforeCreate() {
    /* 这里不能访问方法和属性！！ */
  },

  created() {
    /* 注意：子组件mounted在父组件created之后、父组件mounted之前触发，故子组件mounted需要用到的prop
         需要在父组件created中初始化！！ */
    this.initFieldModel()
    this.registerToRefList()
    this.initEventHandler()
    this.buildFieldRules()
    this.handleOnCreated()
  },

  mounted() {
    this.handleOnMounted()
  },

  beforeDestroy() {
    this.unregisterFromRefList()
  },

  methods: {}
}
</script>

<style lang="scss" scoped>
@import '../../../../styles/global.scss'; //* form-item-wrapper已引入，还需要重复引入吗？ *//

.full-width-input {
  width: 100% !important;
}
::v-deep .el-slider__button-wrapper {
  z-index: 1 !important;
}
</style>
@/utils/i18ns
