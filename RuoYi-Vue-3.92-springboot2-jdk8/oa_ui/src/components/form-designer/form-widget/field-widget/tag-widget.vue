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
    <select-label
      v-show="!getReadMode() || field.isShow"
      :list="field.options.optionItems || []"
      :value="fieldModel"
      ref="selectLabel"
      style="width: 100%"
      :props="{ children: 'children', label: 'name' }"
      @handleLabelConf="handleLabelConf($event, val)"
      @submit="submit"
    >
      <template v-slot:custom>
        <el-select
          ref="fieldEditor"
          v-model="fieldModel"
          class="full-width-input"
          popper-class="popper-class"
          :size="field.options.size"
          :default-first-option="allowDefaultFirstOption"
          :multiple="true"
          :placeholder="field.options.placeholder || i18nt('render.hint.selectPlaceholder')"
          :remote="field.options.remote"
          :remote-method="remoteMethod"
          @focus="handleFocusCustomEvent"
        >
          <el-option v-for="item in labelData" :key="item.id" :label="item.name" :value="item.id"> </el-option>
        </el-select>
      </template>
    </select-label>

    <template v-if="getReadMode() && !field.isShow">
      <div class="readonly-mode-field" @click="isShowFn">
        <template v-if="contentForReadMode && contentForReadMode.length > 0">
          <el-tag class="mr10" v-for="item in contentForReadMode" :key="item.id">{{ item }}</el-tag>
        </template>
        <span v-else>{{ contentForReadMode || '--' }}</span>
      </div>
    </template>
  </form-item-wrapper>
</template>

<script>
import FormItemWrapper from './form-item-wrapper'
import emitter from '@/utils/emitter'
import i18n, { translate } from '@/utils/i18n'
import fieldMixin from '@/components/form-designer/form-widget/field-widget/fieldMixin'
import { clientConfigLabelApi } from '@/api/enterprise'
import { deepClone } from '@/utils/util'

export default {
  name: 'tag-widget',
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
    FormItemWrapper,
    selectLabel: () => import('@/components/form-common/select-label')
  },
  inject: ['refList', 'formConfig', 'globalOptionData', 'globalModel'],
  data() {
    return {
      oldFieldValue: null, //field组件change之前的值
      fieldModel: null,
      rules: [],
      tableData: [],
      labelData: [],
      labelList: [] // 选中的标签列表
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
            if (item.id === id) {
              name = item.name
              let obj = {
                id: item.id,
                name: name
              }
              // this.labelList.push(obj)
              names.push(name)
              return
            }

            if (item.child && item.child.length) {
              traverse(item.child, id)
            }
          }
        }

        return names
      }

      return getNamesByIds(this.labelData, this.fieldModel)
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
    this.getTableData()
  },

  mounted() {
    this.handleOnMounted()
  },

  beforeDestroy() {
    this.unregisterFromRefList()
  },

  methods: {
    handleFocusCustomEvent(event) {
      this.$refs.selectLabel.handlePopoverShow()
      // this.oldFieldValue = deepClone(this.fieldModel)
    },
    submit(data) {
      if (this.getReadMode() && this.isShow) {
        this.isShow = false
        this.putUpdateField()
      }
    },

    // 选中客户标签成功回调
    handleLabelConf(res) {
      this.fieldModel = res.ids
      this.labelList = res.list

      /* input的清除输入小按钮会同时触发handleChangeEvent、handleInputCustomEvent！！ */
      this.syncUpdateFormModel(this.fieldModel)
      this.emitFieldDataChange(this.fieldModel, this.oldFieldValue)

      //number组件一般不会触发focus事件，故此处需要手工赋值oldFieldValue！！
      // this.oldFieldValue = deepClone(this.fieldModel) /* oldFieldValue需要在initFieldModel()方法中赋初值!! */

      /* 主动触发表单的单个字段校验，用于清除字段可能存在的校验错误提示 */
      this.dispatch('VFormRender', 'fieldValidation', [this.getPropName()])
    },
    // 列表
    getTableData() {
      if (this.field.options.optionItems && this.field.options.optionItems.length == 0) {
        this.getLabelData()
      } else {
        this.tableData = this.field.options.optionItems
        if (this.tableData && this.tableData.length > 0) {
          this.tableData.map((value) => {
            if (value.children && value.children.length > 0) {
              value.children.map((val) => {
                this.labelData.push(val)
              })
            }
          })
        }
      }
    },

    getLabelData() {
      let data = {
        page: 0,
        limit: 0
      }
      clientConfigLabelApi(data).then((res) => {
        this.tableData = res.data.list === undefined ? [] : res.data.list
        if (this.tableData && this.tableData.length > 0) {
          this.tableData.map((value) => {
            if (value.children.length > 0) {
              value.children.map((val) => {
                this.labelData.push(val)
              })
            }
          })
        }
      })
    }
  }
}
</script>
<style>
.popper-class {
  display: none !important;
}
</style>
<style lang="scss" scoped>
// @import '../../../../styles/global.scss'; //* form-item-wrapper已引入，还需要重复引入吗？ *//

.full-width-input {
  width: 100% !important;
}
::v-deep .el-input__suffix {
  display: none;
}
</style>
@/utils/i18ns
