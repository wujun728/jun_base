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
    <el-cascader
      ref="fieldEditor"
      v-show="!getReadMode() || field.isShow"
      :options="cityList"
      v-model="fieldModel"
      class="full-width-input"
      :disabled="field.options.disabled"
      :size="field.options.size"
      :clearable="field.options.clearable"
      :filterable="field.options.filterable"
      :show-all-levels="true"
      :props="{
        checkStrictly: field.options.checkStrictly,
        multiple: field.options.multiple,
        expandTrigger: 'hover',
        value: 'id',
        label: 'name',
        children: 'children'
      }"
      @visible-change="hideDropDownOnClick"
      @expand-change="hideDropDownOnClick"
      :placeholder="field.options.placeholder || i18nt('render.hint.selectPlaceholder')"
      @focus="handleFocusCustomEvent"
      @blur="handleBlurCustomEvent"
      @change="handleChangeEvent"
    >
    </el-cascader>
    <template v-if="getReadMode() && !field.isShow">
      <div class="readonly-mode-field" @click="isShowFn()">{{ contentForReadMode || '--' }}</div>
    </template>
  </form-item-wrapper>
</template>

<script>
import FormItemWrapper from './form-item-wrapper'
import emitter from '@/utils/emitter'
import i18n, { translate } from '@/utils/i18n'
import fieldMixin from '@/components/form-designer/form-widget/field-widget/fieldMixin'
import { getDictTreeListApi } from '@/api/form'

export default {
  name: 'cascader-address-widget',
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
      rules: [],
      cityList: [],
      selectedCity: null
    }
  },
  computed: {
    showFullPath() {
      return this.field.options.showAllLevels === undefined || !!this.field.options.showAllLevels
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
            if (item.id === id) {
              name = item.name
              names.push(name)
              return
            }

            if (item.children && item.children.length) {
              traverse(item.children, id)
            }
          }
        }
        return names.toString().replaceAll(',', '-')
      }
      return getNamesByIds(this.cityList, this.fieldModel)
    }
  },
  beforeCreate() {
    /* 这里不能访问方法和属性！！ */
  },
  watch: {},

  created() {
    /* 注意：子组件mounted在父组件created之后、父组件mounted之前触发，故子组件mounted需要用到的prop
         需要在父组件created中初始化！！ */

    this.initOptionItems()
    this.getCityList()
    this.initFieldModel()
    if (this.fieldModel && this.fieldModel.length > 0) {
      this.fieldModel = this.fieldModel.map(Number)
    }

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

  methods: {
    /* 开启任意级节点可选后，点击radio隐藏下拉框 */
    hideDropDownOnClick() {
      setTimeout(() => {
        document.querySelectorAll('.el-cascader-panel .el-radio').forEach((el) => {
          el.onclick = () => {
            this.$refs.fieldEditor.dropDownVisible = false //单选框部分点击隐藏下拉框
          }
        })
      }, 100)
    },

    getCityList() {
      let data = {
        type_id: 2,
        isCityShow: this.field.options.isCityShow || ''
      }
      getDictTreeListApi(data)
        .then((res) => {
          const { data, status } = res
          this.cityList = data
        })
        .catch((error) => {
          reject(error)
        })
    }
  }
}
</script>

<style lang="scss" scoped>
@import '../../../../styles/global.scss'; //* form-item-wrapper已引入，还需要重复引入吗？ *//

.full-width-input {
  width: 100% !important;
}
</style>
@/utils/i18ns
