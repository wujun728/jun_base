<!--
 * @Author: lide1202@hotmail.com
 * @Date: 2021-5-4 11:04:24
 * @Last Modified by:   lide1202@hotmail.com
 * @Last Modified time: 2021-5-6 11:04:24
 !-->
<template>
  <div v-if="option['hide'] == null || option['hide'] == false" class="anji-card">
    <div class="card-head">
      {{ option.title }}
      <div class="main-card-header-button">
        <el-button type="text" @click="handleSetRowColNum(4)">||||</el-button>
        <el-button type="text" @click="handleSetRowColNum(3)">|||</el-button>
        <el-button type="text" @click="handleSetRowColNum(2)">||</el-button>
      </div>
    </div>
    <div class="card-body">
      <el-form ref="editForm" :model="editForm" :label-width="option.labelWidth || '100px'">
        <!--:disabled="modelType == 'view'"-->
        <template v-for="group in columnGroups">
          <el-divider v-if="groupModel" :key="group" content-position="left">{{ group }}</el-divider>
          <el-row :key="group" class="form_table">
            <template v-for="item in groupFormFields[group]">
              <el-col v-if="fieldIsHide(item.editHide) == false" :key="item.editField" :span="item.rowColSpan == null ? cardRowColSpan : item.rowColSpan">
                <el-form-item :label="item.label" :rules="item.rules" :prop="item.editField" :disabled="item.disabled">
                  <!-- 输入框 -->
                  <span v-if="item.tips != '' && item.tips != null" :style="{ 'margin-left': '-13px' }" class="input_tips">
                    <el-tooltip class="item" effect="dark" :content="item.tips" placement="top-start">
                      <svg-icon icon-class="tishi-yiwen" />
                    </el-tooltip>
                  </span>
                  <el-input
                    v-if="item.inputType == 'input'"
                    v-model.trim="editForm[item.editField]"
                    :placeholder="item.placeholder || '请输入'"
                    :clearable="item.clearable !== false"
                    :disabled="fieldIsDisable(item.disabled)"
                    @change="(value) => formChange(item.editField, value, null)" />
                  <!-- 开关 -->
                  <el-switch
                    v-else-if="item.inputType == 'switch'"
                    v-model.trim="editForm[item.editField]"
                    :disabled="fieldIsDisable(item.disabled)"
                    :active-value="1"
                    :inactive-value="0"
                    inactive-color="#ccc"
                    active-color="#5887fb"
                    @change="(value) => formChange(item.editField, value, null)" />
                  <el-input-number
                    v-else-if="item.inputType == 'input-number'"
                    v-model.trim="editForm[item.editField]"
                    :min="item.inputNumberOption.min"
                    :max="item.inputNumberOption.max"
                    :placeholder="item.placeholder || '请输入'"
                    :clearable="item.clearable !== false"
                    :disabled="fieldIsDisable(item.disabled)"
                    @change="(value) => formChange(item.editField, value, null)" />
                  <!-- 自定义input -->
                  <anji-input
                    v-else-if="item.inputType == 'anji-input'"
                    v-model.trim="editForm[item.editField]"
                    :unit="item.anjiInput.unit"
                    :conversion="item.anjiInput.conversion"
                    :keep-point="item.anjiInput.keepPoint"
                    :rounding="item.anjiInput.rounding"
                    :placeholder="item.placeholder || '请输入'"
                    :clearable="item.clearable !== false"
                    :disabled="fieldIsDisable(item.disabled)"
                    @change="(value) => formChange(item.editField, value, null)" />
                  <!-- 下拉框 -->
                  <anji-select
                    v-else-if="item.inputType == 'anji-select'"
                    v-model.trim="editForm[item.editField]"
                    :multiple="item.anjiSelectOption.multiple"
                    :disabled="fieldIsDisable(item.disabled)"
                    :dict-code="item.anjiSelectOption.dictCode"
                    :placeholder="item.placeholder"
                    :url="item.anjiSelectOption.url"
                    :method="item.anjiSelectOption.method"
                    :query-param="item.anjiSelectOption.queryParam"
                    :merge-label="item.anjiSelectOption.mergeLabel"
                    :option="item.anjiSelectOption.option"
                    :label="item.anjiSelectOption.label"
                    :remote-filter="item.anjiSelectOption.remoteFilter"
                    :disabled-options="item.anjiSelectOption.disabledOptions"
                    @change="(value, option) => formChange(item.editField, value, option)"
                  />
                  <!-- 日期时间框  -->
                  <el-date-picker
                    v-else-if="item.inputType.indexOf('date') >= 0"
                    v-model="editForm[item.editField]"
                    style="width: 100%"
                    :placeholder="item.placeholder || '请选择'"
                    :type="item.inputType"
                    :format="item.format"
                    :value-format="item.valueFormat"
                    :disabled="fieldIsDisable(item.disabled)"
                    :clearable="item.clearable !== false"
                    @change="(value) => formChange(item.editField, value, null)" />
                  <!-- checkbox -->
                  <anji-checkbox
                    v-else-if="item.inputType == 'checkbox'"
                    v-model.trim="editForm[item.editField]"
                    :dict-code="item.anjiCheckbox.dictCode"
                    :label="item.anjiCheckbox.label"
                    :disabled="fieldIsDisable(item.disabled)"
                    @change="(value, options) => formChange(item.editField, value, options)" />
                  <!-- 城市三级联动 -->
                  <anji-cascader v-else-if="item.inputType == 'anji-cascader'" v-model.trim="editForm[item.editField]" :disabled="fieldIsDisable(item.disabled)" :url="item.anjiCascader.url" @change="(value) => formChange(item.editField, value, null)" />
                  <!-- 上传组件 -->
                  <anji-upload
                    v-else-if="item.inputType == 'anji-upload'"
                    v-model.trim="editForm[item.editField]"
                    :up-load-url="item.anjiUpload.upLoadUrl"
                    :view-url="item.anjiUpload.viewUrl"
                    :upload-type="item.anjiUpload.uploadType"
                    :limit="item.anjiUpload.limit"
                    @change="(value) => formChange(item.editField, value, null)" />
                  <!-- input自带输入建议 -->
                  <anji-autocomplete
                    v-else-if="item.inputType == 'anji-autocomplete'"
                    v-model.trim="editForm[item.editField]"
                    :disabled="fieldIsDisable(item.disabled)"
                    :label="item.anjiAutocomplete.label"
                    :option="item.anjiAutocomplete.option"
                    :appoint-value="item.anjiAutocomplete.appointValue"
                    :url="item.anjiAutocomplete.url"
                    @change="(value, option) => formChange(item.editField, value, option)" />
                  <!-- textarea -->
                  <el-input
                    v-else-if="item.inputType == 'textarea'"
                    v-model.trim="editForm[item.editField]"
                    :placeholder="item.placeholder || '请输入'"
                    :clearable="item.clearable !== false"
                    :disabled="fieldIsDisable(item.disabled)"
                    type="textarea"
                    :rows="2"
                    @change="(value) => formChange(item.editField, value, null)" />
                  <el-input v-else placeholder="组件不支持此类型表单请至组件内部自行扩展" disabled />
                </el-form-item>
              </el-col>
            </template>
          </el-row>
        </template>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  components: {},
  props: {
    modelType: String, // add view edit
    showDialog: Boolean,
    option: {
      // 界面渲染相关配置json
      type: [Object],
      default: () => {
        return {
          title: '', // 页面标题
          labelWidth: '', // 表单输入框label宽度
          queryFormFields: [], // 查询表单条件
          buttons: {
            // 按钮
            query: {},
            edit: {},
            delete: {},
            add: {},
          },
          columns: [], // 表格列
          formChange: (formData, fieldName, fieldVal, fieldExtend) => {}, // 弹出框表单修改回调
        }
      },
    },
    // 当relateData不为空时，该组件是渲染与relateData一对一关联的子表信息
    relateData: {
      type: [Object],
      default: () => {
        return {}
      },
    },
    value: {
      type: [Object],
      default: () => {
        return {}
      },
    },
  },
  data() {
    return {
      cardRowColNum: 2, // 主信息一行显示几列
      editForm: {}, // 提交表单的数据
    }
  },
  computed: {
    // 主键的列名
    primaryKeyFieldName() {
      var primaryKey = this.option.columns.find((item) => item['primaryKey'] == true)
      if (primaryKey != null) {
        return primaryKey['field']
      } else {
        return null
        console.warn('在columns中查找primaryKey=true失败，会导致查询详情和删除失败')
      }
    },
    // 指定当前实体关联主表的
    joinColumn() {
      var columnName = this.option.joinColumn
      if (this.isBlank(columnName)) {
        console.warn('在joinEntity中查找joinColumn属性失败，会导致查询详情和删除失败')
        columnName = ''
      }
      return columnName
    },
    // 提交表单中所有的列，因部分翻译字段，表格中和表单中的key不一样，field editField不全，这里补全下，
    formFields() {
      if (this.option.columns == null) {
        return []
      }
      var fields = this.deepClone(this.option.columns)
      fields = fields.map((item) => {
        if (this.isBlank(item['editField'])) {
          item['editField'] = item['field']
        }
        // 没有设定分组的，全部补全成 其它信息
        if (this.isBlank(item['group'])) {
          item['group'] = '其它'
        }
        return item
      })
      return fields
    },
    // 卡片设定中一行显示几列，每列的col数
    cardRowColSpan() {
      return 24 / this.cardRowColNum
    },
    // 如果表单内容较多，启用了分组，这里先算出所有的分组
    columnGroups() {
      if (this.isBlank(this.formFields)) {
        return []
      } else {
        // 找出所有hide != true的关联表
        var groups = this.formFields
          .map((item) => {
            return item['group']
          })
          .filter((currentValue, index, arr) => {
            return arr.indexOf(currentValue) == index
          })
        return groups
      }
    },
    /* {
      '分组1': [{column1.1}],
      '分组2': [{column2.1}],
    }*/
    groupFormFields() {
      if (this.showDialog) {
        // 将每个分组初始化
        var groupFormFields = {}
        this.columnGroups.forEach((value, index, array) => {
          groupFormFields[value] = []
        })
        // 将所有编辑列，按分组存放
        this.formFields.forEach((item, index, array) => {
          groupFormFields[item['group']].push(item)
        })
        return groupFormFields
      }
      return []
    },
    groupModel() {
      return this.columnGroups.length > 1
    },
  },
  created() {
    // 如果表单，是做为主表的编辑页面，如果是通过v-model传递进来的值，通过监听value，更新this.editForm
    this.$watch(
      function () {
        return this.value
      },
      function (newVal, oldVal) {
        this.editForm = newVal
        // 通过v-model传递值进来时，说明当前form是主表信息
        this.formChange()
      }
    )
    // 如果表单，是做为子表的编辑页面，当relateData中的关联字段发生更新时，触发查询，比如在goods详情中，goodsCode
    this.$watch(
      function () {
        return this.relateData[this.joinColumn]
      },
      function (newVal, oldVal) {
        // 如果是父组件(弹出框)关闭时，设置this.relateData = {}时触发，清空本组件的数据
        if (this.isBlank(this.relateData)) {
          this.cardRowColNum = 2
          this.editForm = {}
          return
        }
        // 如果是关联字段发生更新，触发查询
        if (this.isNotBlank(newVal)) {
          this.queryDetail()
        }
      }
    )
  },
  mounted() {
    // 如果表单，是做为主表的编辑页面，如果是通过v-model传递进来的值
    if (this.isNotBlank(this.value)) {
      this.editForm = this.value
      this.formChange()
    }
    // 如果表单，是做为子表的编辑页面，首次打开时，根据关联属性，加载详情数据
    if (this.isNotBlank(this.relateData) && this.isNotBlank(this.relateData[this.joinColumn])) {
      this.queryDetail()
    }
  },
  methods: {
    // 该行是否显示 true/false/ 'hideOnAdd hideOnView hideOnEdit'
    fieldIsHide(editHide) {
      if (typeof editHide == 'boolean') {
        return editHide
      }
      if (typeof editHide == 'string') {
        if (this.modelType == 'add') {
          return editHide.indexOf('hideOnAdd') >= 0
        }
        if (this.modelType == 'view') {
          return editHide.indexOf('hideOnView') >= 0
        }
        if (this.modelType == 'edit') {
          return editHide.indexOf('hideOnEdit') >= 0
        }
      }
      return false
    },
    // 该行是否禁用 true/false/ 'disableOnAdd disableOnView disableOnEdit'
    fieldIsDisable(disable) {
      if (typeof disable == 'boolean') {
        return disable
      }
      if (typeof disable == 'string') {
        if (this.modelType == 'add') {
          return disable.indexOf('disableOnAdd') >= 0
        }
        if (this.modelType == 'view') {
          return disable.indexOf('disableOnView') >= 0
        }
        if (this.modelType == 'edit') {
          return disable.indexOf('disableOnEdit') >= 0
        }
      }
      return false
    },
    // 设置一行显示几列
    handleSetRowColNum(num) {
      this.cardRowColNum = num
      this.$emit('changeRowColNum', num)
    },

    async queryDetail() {
      var queryParams = this.relateData
      const { data, code } = await this.option.buttons.queryByPrimarykey.api(queryParams)
      if (code != '200') return
      this.editForm = data
      this.formChange()
    },
    // 校验表单
    validate(callback) {
      this.$refs.editForm.validate(async (valid, obj) => {
        if (callback != null) {
          callback(valid)
        }
      })
    },
    handleSave(callback) {
      this.$refs.editForm.validate(async (valid, obj) => {
        if (valid) {
          if (this.modelType == 'add') {
            // 当edit-from是作为关联子表的界面，补全关联属性
            if (typeof this.option.beforeInsert == 'function') {
              this.option.beforeInsert(this.relateData, this.editForm)
            }
            const { code, message } = await this.option.buttons.add.api(this.editForm)
            if (code == '200') {
              if (callback != null) {
                callback()
              }
            } else {
              console.log(`提交表单调用新增接口失败：${message}`)
            }
          } else {
            // 当edit-from是作为关联子表的界面，补全关联属性
            if (typeof this.option.beforeUpdate == 'function') {
              this.option.beforeUpdate(this.relateData, this.editForm)
            }
            const { code, message } = await this.option.buttons.edit.api(this.editForm)
            if (code == '200') {
              if (callback != null) {
                callback()
              }
            } else {
              console.log(`提交表单调用更新接口失败：${message}`)
            }
          }
        } else {
          console.log('表单校验失败')
        }
      })
    },
    // 表单任何一个变动时，通知外部v-model
    formChange(fieldName, fieldVal, fieldExtend) {
      this.$emit('input', this.editForm)
      // 表单变动后，回调option中的formChange事件
      if (typeof this.option.formChange == 'function') {
        this.option.formChange(this.editForm, fieldName, fieldVal, fieldExtend)
      }
    },
  },
}
</script>

<style scoped lang="scss">
.input_tips {
  position: absolute;
  margin-top: -8px;
  .svg-icon {
    font-size: 20px;
    color: rgb(71, 8, 8);
  }
}
</style>
