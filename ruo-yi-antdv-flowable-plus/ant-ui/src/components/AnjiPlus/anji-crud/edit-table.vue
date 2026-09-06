<!--
 * @Author: lide1202@hotmail.com
 * @Date: 2021-5-4 11:04:24
 * @Last Modified by:   lide1202@hotmail.com
 * @Last Modified time: 2021-5-6 11:04:24
 !-->
<template>
  <div
    v-if="option['hide'] == null || option['hide'] == false"
    class="anji-card">
    <div class="card-head">{{ option.title }}</div>
    <div class="card-body">
      <el-form
        ref="form"
        :model="form">
        <!-- 表格开始 -->
        <el-table
          :data="formRecordsUndelete"
          border
          :row-class-name="tableRowClassAdapter"
          @selection-change="handleSelectionChange"
          @row-click="handleTableRowClick">
          <!-- <el-table-column fixed type="selection" width="50" align="center" /> -->

          <el-table-column
            label="序号"
            min-width="50"
            align="center">
            <template slot-scope="scope">
              {{ scope.$index + 1 }}
            </template>
          </el-table-column>

          <template v-for="item in option.columns">
            <el-table-column
              v-if="fieldIsHide(item.tableHide) != true && item.columnType != 'expand'"
              :key="item.field"
              :label="item.label"
              :min-width="item.minWidth || 110"
              align="center">
              <template slot-scope="scope">
                <el-form-item
                  :prop="'records.' + scope.$index + '.' + item.field"
                  :rules="item.rules">
                  <!-- 输入框 -->
                  <el-input
                    v-if="item.inputType == 'input'"
                    v-model="scope.row[item.field]"
                    size="small"
                    :placeholder="item.placeholder || '请输入'"
                    :clearable="item.clearable !== false"
                    :disabled="saveButtonStatus[scope.$index] == 'inShow' || item.disabled"
                    @change="(value) => tableRowChange(scope.$index, item.field, value)" />
                  <!-- 开关 -->
                  <el-switch
                    v-else-if="item.inputType == 'switch'"
                    v-model="scope.row[item.field]"
                    :disabled="saveButtonStatus[scope.$index] == 'inShow' || item.disabled"
                    :active-value="item.switchOption.disableValue"
                    :inactive-value="item.switchOption.enableValue"
                    @change="(value) => tableRowChange(scope.$index, item.field, value)"
                    active-color="#5887fb"
                    inactive-color="#ccc">
                  </el-switch>
                  <el-input
                    v-else-if="item.inputType == 'input-number'"
                    v-model="scope.row[item.field]"
                    size="small"
                    :min="item.inputNumberOption.min"
                    :max="item.inputNumberOption.max"
                    :placeholder="item.placeholder || '请输入'"
                    :clearable="item.clearable !== false"
                    :disabled="saveButtonStatus[scope.$index] == 'inShow' || item.disabled"
                    @change="(value) => tableRowChange(scope.$index, item.field, value)" />

                  <!-- 自定义input -->
                  <anji-input
                    v-else-if="item.inputType == 'anji-input'"
                    v-model.trim="scope.row[item.field]"
                    :default-value="item.defaultValue"
                    :unit="item.anjiInput.unit"
                    :conversion="item.anjiInput.conversion"
                    :placeholder="item.placeholder || '请输入'"
                    :clearable="item.clearable !== false"
                    :disabled="saveButtonStatus[scope.$index] == 'inShow' || item.disabled"
                    @change="(value) => tableRowChange(scope.$index, item.field, value)" />

                  <!-- 下拉框 -->
                  <anji-select
                    v-else-if="item.inputType == 'anji-select'"
                    v-model.trim="scope.row[item.field]"
                    :multiple="item.anjiSelectOption.multiple"
                    :default-value="item.defaultValue"
                    :dict-code="item.anjiSelectOption.dictCode"
                    :url="item.anjiSelectOption.url"
                    :method="item.anjiSelectOption.method"
                    :query-param="item.anjiSelectOption.queryParam"
                    :option="item.anjiSelectOption.option"
                    :label="item.anjiSelectOption.label"
                    :disabled-options="item.anjiSelectOption.disabledOptions"
                    :disabled="saveButtonStatus[scope.$index] == 'inShow' || item.disabled"
                    @change="(value, option) => tableRowChange(scope.$index, item.field, value, option)" />
                  <!-- 日期时间框  -->
                  <el-date-picker
                    v-else-if="item.inputType.indexOf('date') >= 0"
                    v-model="scope.row[item.field]"
                    style="width: 100%"
                    :placeholder="item.placeholder || '请选择'"
                    :type="item.inputType"
                    :clearable="item.clearable !== false"
                    :disabled="saveButtonStatus[scope.$index] == 'inShow' || item.disabled"
                    @change="(value) => tableRowChange(scope.$index, item.field, value)" />
                  <!-- 待扩展的表单类型，请自行扩展 -->
                  <el-input
                    v-else
                    placeholder="组件不支持此类型表单请至组件内部自行扩展"
                    disabled />
                </el-form-item>
              </template>
            </el-table-column>
          </template>
          <el-table-column
            fixed="right"
            label="操作"
            width="100">
            <template slot-scope="scope">
              <el-button
                type="text"
                size="small"
                @click="handleAddOrUpdate(scope.row, scope.$index)">{{ getRowEditButton(scope.$index) }}</el-button>
              <el-button
                type="text"
                size="small"
                @click="handleDelete(scope.row, scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <!-- 表格结束 -->
      </el-form>
      <button
        v-if="modelType != 'view'"
        class="table-add-row-button"
        @click="handleAdd">
        <i class="el-icon-plus" />
        <span>新增</span>
      </button>
    </div>
  </div>
</template>

<script>
const ROW_DELETE_FLAG = 'deletedFlag'
export default {
  components: {},
  props: {
    modelType: String, // add view edit
    option: {
      // 界面渲染相关配置json
      type: [Object],
      default: () => {
        return {
          title: '', // 页面标题
          labelWidth: '',
          queryFormFields: [], // 查询表单条件
          buttons: {
            // 按钮
            query: {},
            edit: {},
            delete: {},
            add: {},
          },
          columns: [], // 表格列
        }
      },
    },
    relateData: {
      // 关联的主记录
      type: [Object],
      default: () => {
        return {}
      },
    },
    value: {
      type: [Array],
      default: () => {
        return []
      },
    },
    valueNew: {
      type: [Array],
      default: () => {
        return []
      },
    }
  },
  data () {
    return {
      checkRecords: [], // 表格中当前选中的记录

      form: {
        records: [], // 接口返回的记录列表
        total: 0, // 接口返回的总条数
      },

      saveButtonStatus: [], // 维护表格中每行编辑按钮的状态 inShow inEditing inAdding

      rowIdList: []
    }
  },
  computed: {
    // 主键的列名
    primaryKeyFieldName () {
      var primaryKey = this.option.columns.find((item) => item['primaryKey'] == true)
      if (primaryKey != null) {
        return primaryKey['field']
      } else {
        return null
        console.warn('在columns中查找primaryKey=true失败，会导致查询详情和删除失败')
      }
    },
    // 指定当前实体关联主表的关联字段，孙子关联表，没有该属性
    joinColumn () {
      var columnName = this.option.joinColumn
      if (this.isBlank(columnName)) {
        console.warn('在columns中查找关联字段失败，会导致查询详情和删除失败，孙子关联表忽略该错误')
        columnName = ''
      }
      return columnName
    },
    // 未删除的记录
    formRecordsUndelete () {
      if (this.form.records == null) {
        return []
      }
      return this.form.records.filter((item) => item[ROW_DELETE_FLAG] == null || item[ROW_DELETE_FLAG] == false)
    },
  },
  watch: {},
  created () {
    // 主表 relateData 的关联字段 joinColumn 变动时，触发查询子表的查询，孙子关联儿子的无效
    if (this.isNotBlank(this.joinColumn)) {
      this.$watch(
        function () {
          return this.relateData[this.joinColumn]
        },
        function (newVal, oldVal) {
          // 如果是关联字段发生更新，触发查询
          if (this.isNotBlank(newVal)) {
            this.handleQueryPageList(newVal)
          } else {
            // 如果关联字段为空，清空本表格的数据，如果是父组件(弹出框)关闭时，设置this.relateData = {默认值}时触发
            this.checkRecords = []
            this.form.records = []
            this.form.total = 0
            this.saveButtonStatus = []
          }
        }
      )
    }
  },
  mounted () {
    // 首次打开时，根据主表关联字段查询子表，加载表格数据
    if (this.isNotBlank(this.relateData) && this.isNotBlank(this.relateData[this.joinColumn])) {
      this.handleQueryPageList()
    }
  },
  methods: {
    // 该行是否显示 true/false/ 'hideOnAdd hideOnView hideOnEdit'
    fieldIsHide (tableHide) {
      if (typeof tableHide == 'boolean') {
        return tableHide
      }
      if (typeof tableHide == 'string') {
        if (this.modelType == 'add') {
          return tableHide.indexOf('hideOnAdd') >= 0
        }
        if (this.modelType == 'view') {
          return tableHide.indexOf('hideOnView') >= 0
        }
        if (this.modelType == 'edit') {
          return tableHide.indexOf('hideOnEdit') >= 0
        }
      }
      return false
    },
    // 获取行的提交按钮文字
    getRowEditButton (index) {
      if (this.saveButtonStatus[index] == 'inEditing') {
        return 'btn_savetemp'
      } else if (this.saveButtonStatus[index] == 'inAdding') {
        return 'btn_savetemp'
      } else if (this.saveButtonStatus[index] == 'inShow') {
        return 'btn_edit'
      } else {
        return 'not_permission'
      }
    },
    // 表格行渲染前前置处理
    tableRowClassAdapter ({ row, rowIndex }) {
      row.index = rowIndex
    },
    // 查询
    async handleQueryPageList (joinColumnValue) {
      if (this.isBlank(joinColumnValue)) {
        joinColumnValue = this.relateData[this.joinColumn]
      }
      var params = {}
      params[this.joinColumn] = joinColumnValue
      this.queryPageList(params)
    },
    // 暴露给外部直接调用带参数
    async queryPageList (params) {
      // 默认的排序
      if (this.isNotBlank(this.option.buttons.query.order)) {
        params['sort'] = this.option.buttons.query.sort
        params['order'] = this.option.buttons.query.order
      }
      const { data, code } = await this.option.buttons.query.api(params)
      if (code != '200') return
      this.form.records = data.records
      this.form.total = data.total
      this.$emit('input', this.form.records)
      for (var i = 0; i < this.form.total; i++) {
        this.saveButtonStatus.push('inShow')
      }
    },
    // 选择项改变时
    handleSelectionChange (val) {
      this.checkRecords = val
    },
    // 表格选中某一行时
    handleTableRowClick (row, column, event) {
      // console.log(row)
      // console.log(column)
      // 行点击后，回调option中的tableRowClick事件
      if (typeof this.option.tableRowClick == 'function') {
        this.option.tableRowClick(this.form.records, row, row.index, this.relateData)
      }
    },
    // 行数据更新时回调
    tableRowChange (rowIndex, fieldName, fieldVal, fieldExtend) {
      // 通知外面的组件
      this.$emit('input', this.form.records)

      // 表单变动后，回调option中的tableRowChange事件
      if (typeof this.option.tableChange == 'function') {
        this.option.tableChange(this.form.records, rowIndex, fieldName, fieldVal, fieldExtend, this.relateData)
      }
    },
    // 新增
    handleAdd () {
      this.saveButtonStatus.push('inAdding')
      this.form.records.push({})
    },
    // 父节点Change,子节点表格更新
    handleUpdata () {
      this.saveButtonStatus = []
      this.form.records = []
    },
    // 提交和修改
    handleAddOrUpdate (row, index) {
      // 编辑状态下点击保存提交
      if (this.saveButtonStatus[index] == 'inEditing' || this.saveButtonStatus[index] == 'inAdding') {
        this.handleSaveTemp(row, index)
      } else {
        this.$set(this.saveButtonStatus, index, 'inEditing')
      }
    },
    // 校验表单
    validate (callback) {
      this.$refs['form'].validate(async (valid, obj) => {
        if (callback != null) {
          callback(valid)
        }
      })
    },
    // 暂存
    async handleSaveTemp (row, index) {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          if (this.isBlank(row[this.primaryKeyFieldName])) {
            // 补全关联属性
            if (typeof this.option.beforeInsert == 'function') {
              this.option.beforeInsert(this.relateData, row)
            }
          } else {
            // 补全关联属性
            if (typeof this.option.beforeUpdate == 'function') {
              this.option.beforeUpdate(this.relateData, row)
            }
          }
          // 将行按钮的文字改成编辑
          this.$set(this.saveButtonStatus, index, 'inShow')
          // 通知外面的组件
          this.$emit('input', this.form.records)
        }
      })
      /*
      this.$refs['form'].validate((valid) => {
        if (valid) {
          // 验证通过
          if (this.isBlank(row[this.primaryKeyFieldName])) {
            // 补全关联属性
            if (typeof this.option.beforeInsert == 'function') {
              this.option.beforeInsert(this.relateData, row)
            }
            // 主键为空，新增
            this.option.buttons.add.api(row).then((response) => {
              if (response.code == 200) {
                this.$set(this.saveButtonStatus, index, 'inShow')
                // 保存完成后刷新列表
                this.handleQueryPageList()
              }
            })
          } else {
            // 补全关联属性
            if (typeof this.option.beforeUpdate == 'function') {
              this.option.beforeUpdate(this.relateData, row)
            }
            // 修改
            this.option.buttons.edit.api(row).then((response) => {
              if (response.code == 200) {
                this.$set(this.saveButtonStatus, index, 'inShow')
                // 保存完成后刷新列表
                this.handleQueryPageList()
              }
            })
          }
        } else {
          console.log('valid fail')
          return false
        }
      })
      */
    },
    // 删除
    handleDelete (row, index) {
      this.saveButtonStatus.splice(index, 1) // 清空状态
      // 界面上临时新增出来的一行，还没有提交到数据库，可以直接删除
      if (this.saveButtonStatus[index] == 'inAdding') {
        this.form.records.splice(index, 1)
        this.saveButtonStatus.splice(index, 1)
        this.$emit('input', this.form.records)
        return
      }
      // if (this.isBlank(row) || this.isBlank(row[this.primaryKeyFieldName])) {
      //   return
      // }
      // 将对应的行标识成删除
      // 找出该行在原始记录中的index
      // var realIndex = this.form.records.findIndex((item) => item[this.primaryKeyFieldName] == row[this.primaryKeyFieldName])
      // row[ROW_DELETE_FLAG] = true
      // this.$set(this.form.records, realIndex, row)
      // this.$emit('input', this.form.records)
      this.form.records.splice(index, 1)
      this.rowIdList.push(row.id)
      this.$emit('input', this.form.records)
      this.$emit('update:valueNew', this.rowIdList)
      /*
      之前是直接调用接口删除，后面统一改成在主表接口中增加、更新、删除
      // 已经保存在数据库的的行，要调用删除按钮
      var primaryKey = row[this.primaryKeyFieldName]
      this.$confirm(this.$lang('promptMessage_deleteTip'), this.$lang('promptMessage_deleteTipTitle'), {
        type: 'warning',
        confirmButtonClass: 'delete_sure',
        cancelButtonClass: 'el-button--danger is-plain',
      })
        .then(() => {
          this.option.buttons.delete.api(primaryKey).then((res) => {
            // {code: "200", message: "操作成功", data: true}
            this.checkRecords = []
            this.handleQueryPageList()
          })
        })
        .catch((e) => {
          e
        })
        */
    },
  },
}
</script>

<style scoped lang="scss">
.table-add-row-button {
  width: 100%;
  margin-top: 0px;
  margin-bottom: 0px;
  border-color: #d9d9d9;
  border-style: dashed;
  line-height: 1.499;
  position: relative;
  display: inline-block;
  font-weight: 400;
  white-space: nowrap;
  text-align: center;
  background-image: none;
  border: 1px solid transparent;
  box-shadow: 0 2px 0 rgba(0, 0, 0, 0.015);
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.645, 0.045, 0.355, 1);
  -webkit-user-select: none;
  -ms-user-select: none;
  user-select: none;
  -ms-touch-action: manipulation;
  touch-action: manipulation;
  height: 32px;
  padding: 0 15px;
  font-size: 14px;
  border-radius: 4px;
  color: rgba(0, 0, 0, 0.65);
  background-color: #fff;
  border-color: #d9d9d9;
}
</style>
