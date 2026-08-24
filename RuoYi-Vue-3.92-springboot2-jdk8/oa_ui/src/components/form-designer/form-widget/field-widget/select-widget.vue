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
    <el-select
      v-show="!getReadMode() || field.isShow"
      ref="fieldEditor"
      v-model="fieldModel"
      class="full-width-input"
      :disabled="field.options.disabled"
      :size="field.options.size"
      :clearable="field.options.clearable"
      :filterable="field.options.filterable"
      :allow-create="field.options.allowCreate"
      :default-first-option="allowDefaultFirstOption"
      :automatic-dropdown="field.options.automaticDropdown"
      :multiple="field.options.multiple"
      :multiple-limit="field.options.multipleLimit"
      :placeholder="field.options.placeholder || i18nt('render.hint.selectPlaceholder')"
      :remote="field.options.remote"
      :remote-method="remoteMethod"
      @focus="handleFocusCustomEvent"
      @blur="handleBlurCustomEvent"
      @change="handleChangeEvent($event, fieldModel)"
    >
      <el-option
        v-for="item in field.options.optionItems"
        :key="item.value"
        :label="item.label"
        :value="item.value"
        :disabled="item.disabled"
      >
      </el-option>
    </el-select>
    <template v-if="getReadMode() && !field.isShow">
      <div class="readonly-mode-field" @click="isShowFn">{{ contentForReadMode || '--' }}</div>
    </template>
  </form-item-wrapper>
</template>

<script>
import FormItemWrapper from './form-item-wrapper'
import emitter from '@/utils/emitter'
import i18n, { translate } from '@/utils/i18n'
import fieldMixin from '@/components/form-designer/form-widget/field-widget/fieldMixin'

export default {
  name: 'select-widget',
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
      oldFieldValue: null, //field组件change之前的值
      fieldModel: null,
      rules: []
    }
  },
  computed: {
    allowDefaultFirstOption() {
      return !!this.field.options.filterable && !!this.field.options.allowCreate
    },

    remoteMethod() {
      if (!!this.field.options.remote && !!this.field.options.onRemoteQuery) {
        return this.remoteQuery
      } else {
        return undefined
      }
    },
    contentForReadMode() {
      function getNamesByIds(data, ids) {
        if (!ids.length) {
          return ''
        }
        let names = []

        for (let id of ids) {
          traverse(data, id)
        }

        function traverse(items, id) {
          let name
          for (let item of items) {
            if (item.value == id) {
              name = item.label
              names.push(name)
              return
            }
          }
        }

        return names.toString().replaceAll(',', '-')
      }
      return getNamesByIds(this.field.options.optionItems, this.fieldModel)
    }
  },
  beforeCreate() {
    /* 这里不能访问方法和属性！！ */
  },

  created() {
    /* 注意：子组件mounted在父组件created之后、父组件mounted之前触发，故子组件mounted需要用到的prop
         需要在父组件created中初始化！！ */
    this.initOptionItems()
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
</style>
@/utils/i18ns
