<template>
  <div :class="[hasTreeFieldInQueryForm ? 'page-container' : 'app-container']">
    <div v-if="hasTreeFieldInQueryForm" class="left-container">
      <AnjiTree
        ref="queryFormTree"
        v-model.trim="queryParams[queryFormTreeField.field]"
        :is-open="queryFormTreeField.anjiTreeOption.isOpen"
        :enable-filter="queryFormTreeField.anjiTreeOption.enableFilter"
        :label-name="queryFormTreeField.label"
        :url="queryFormTreeField.anjiTreeOption.url"
        @node-click="handleTreeNodeCheck"
      />
    </div>
    <div class="right-container">
      <!-- 查询表单开始 -->
      <el-form
        ref="formSearch"
        :model="queryParams"
        label-width="100px"
        v-permission="option.buttons.query.permission"
      >
        <el-row>
          <el-col
            v-for="(item, index) in queryFormFieldExcludeTree"
            :key="item.field"
            :span="queryFormFieldSpan(item)"
          >
            <el-form-item
              v-if="index <= 2 || (index > 2 && queryParams.showMoreSearch)"
              :label="item.label"
              :rules="item.rules"
              :prop="item.field"
            >
              <!-- 输入框 -->
              <el-input
                v-if="
                  item.inputType == 'input' || item.inputType == 'input-number'
                "
                v-model.trim="queryParams[item.field]"
                :placeholder="item.placeholder || '请输入'"
                :clearable="item.clearable !== false"
                :disabled="item.disabled"
                @change="value => queryFormChange(item.field, value)"
              />
              <!-- 开关 -->
              <el-switch
                v-else-if="item.inputType == 'switch'"
                v-model.trim="queryParams[item.field]"
                :disabled="item.disabled"
                :active-value="item.switchOption.disableValue"
                :inactive-value="item.switchOption.enableValue"
                active-color="#5887fb"
                inactive-color="#ccc"
                @change="value => queryFormChange(item.field, value)"
              />
              <!-- 下拉框 -->
              <anji-select
                v-else-if="item.inputType == 'anji-select'"
                v-model.trim="queryParams[item.field]"
                :multiple="item.anjiSelectOption.multiple"
                :dict-code="item.anjiSelectOption.dictCode"
                :url="item.anjiSelectOption.url"
                :method="item.anjiSelectOption.method"
                :query-param="item.anjiSelectOption.queryParam"
                :option="item.anjiSelectOption.option"
                :label="item.anjiSelectOption.label"
                :disabled-options="item.anjiSelectOption.disabledOptions"
                :disabled="item.disabled"
                :merge-label="item.anjiSelectOption.mergeLabel"
                @change="value => queryFormChange(item.field, value)"
              />
              <!-- 日期时间框  -->
              <el-date-picker
                v-else-if="item.inputType.indexOf('date') >= 0"
                v-model="queryParams[item.field]"
                style="width: 100%"
                :placeholder="item.placeholder || '请选择'"
                :type="item.inputType"
                :clearable="item.clearable !== false"
                @change="value => queryFormChange(item.field, value)"
              />
              <!-- 待扩展的表单类型，请自行扩展 -->
              <el-input
                v-else
                placeholder="组件不支持此类型表单请至组件内部自行扩展"
                disabled
              />
            </el-form-item>
          </el-col>

          <el-col :span="6" style="text-align: center">
            <el-button
              type="primary"
              @click="handleQueryForm('query')"
            >查询</el-button
            >
            <el-button type="danger" @click="handleResetForm()">重置</el-button>
            <a
              v-if="queryFormFieldExcludeTree.length > 3"
              style="margin-left: 8px"
              @click="handleToggleMoreSearch"
            >
              {{ queryParams.showMoreSearch == true ? "收起" : "展开" }}
              <i
                :class="
                  queryParams.showMoreSearch
                    ? 'el-icon-arrow-up'
                    : 'el-icon-arrow-down'
                "
              />
            </a>
          </el-col>
        </el-row>
      </el-form>
      <!-- 查询表单结束 -->

      <!-- 批量操作 -->
      <slot name="buttonLeftOnTable" />
      <el-button
        v-if="
          option.buttons.add.isShow == undefined
            ? true
            : option.buttons.add.isShow
        "
        v-permission="option.buttons.add.permission"
        type="primary"
        icon="el-icon-plus"
        @click="handleOpenEditView('add')"
      >新增</el-button
      >
      <el-button
        v-if="
          option.buttons.delete.isShow == undefined
            ? true
            : option.buttons.delete.isShow
        "
        v-permission="option.buttons.delete.permission"
        :disabled="disableBatchDelete"
        type="danger"
        icon="el-icon-delete"
        @click="handleDeleteBatch()"
      >删除</el-button
      >

      <!-- 表格开始 -->
      <el-table
        class="anji_curd_table"
        :data="records"
        border
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
      >
        <!--多选-->
        <el-table-column fixed type="selection" width="50" align="center" />
        <!--隐藏列-->
        <el-table-column v-if="tableExpandColumns.length > 0" type="expand">
          <template slot-scope="scope">
            <p
              v-for="item in tableExpandColumns"
              :key="item.field"
              class="table-expand-item"
            >
              <span class="titel"> {{ item.label }}: </span>
              <span>{{ scope.row[item.field] }}</span>
            </p>
          </template>
        </el-table-column>
        <!--序号-->
        <el-table-column label="序号" min-width="50" align="center">
          <template slot-scope="scope">
            {{
              queryParams.pageSize * (queryParams.pageNumber - 1) +
                scope.$index +
                1
            }}
          </template>
        </el-table-column>

        <template v-for="item in option.columns">
          <el-table-column
            v-if="item.tableHide != true && item.columnType != 'expand'"
            :key="item.field"
            :prop="item.field"
            :label="fieldLabel(item)"
            :min-width="item.minWidth || 110"
            :sortable="item.sortable"
            :show-overflow-tooltip="true"
            align="center"
          >
            <template slot-scope="scope">
              <div v-if="item.columnType == 'imgPreview'">
                <!-- 图片缩略图-->
                <el-image
                  style="width: 25%; height: 50%"
                  fit="contain"
                  :src="scope.row[item.field]"
                  :preview-src-list="[scope.row[item.field]]"
                />
              </div>
              <div v-else>
                <span v-if="item.inputType == 'switch' && !item.colorStyle">
                  <el-switch
                    v-model.trim="scope.row[item.field]"
                    :active-value="1"
                    :inactive-value="0"
                    active-color="#5887fb"
                    inactive-color="#ccc"
                    @change="switchChange(scope.row, item.switchOption)"
                  />
                </span>
                <!-- 带单位 -->
                <span v-else-if="item.inputType == 'anji-input'">{{
                  fieldValueByAnjiInput(scope.row[item.field], item)
                }}</span>
                <!--表格 a 合并 b上-->
                <span
                  v-else-if="item.mergeColumn"
                >{{ scope.row[item.field] }}({{
                  scope.row[item.mergeColumn]
                }})</span
                >
                <!-- 没有单位 -->
                <span
                  v-else-if="item.colorStyle"
                  :class="item.colorStyle[scope.row[item.editField]]"
                >{{ fieldValueByRowRenderer(scope.row, item) }}</span
                >
                <span v-else>{{
                  fieldValueByRowRenderer(scope.row, item)
                }}</span>
                <!-- 正常展示模式
                <div v-if="!item.custom">
                  是第一列数据 && 需要高亮字段不为false 高亮并且可以点击
                  <span v-if="!index && item.operate !== false" class="view" @click="handleOpenEditView('view', scope.row)">{{ scope.row[item.field] }}</span>
                  <span v-else>{{ scope.row[item.field] }}</span>
                </div>
                -->
                <!-- 自定义展示数据
                <div v-else v-html="item.renderer(scope.row)" />-->
              </div>
            </template>
          </el-table-column>
        </template>
        <!--操作栏-->
        <el-table-column
          align="center"
          fixed="right"
          label="操作"
          :width="
            option.buttons.customButton &&
              option.buttons.customButton.operationWidth
              ? option.buttons.customButton.operationWidth
              : 100
          "
        >
          <template slot-scope="scope">
            <slot name="edit" :msg="scope.row" />
            <el-button
              v-if="
                (option.buttons.query.isShow == undefined
                  ? true
                  : option.buttons.query.isShow) &&
                  hasPermission(option.buttons.edit.permission) == false
              "
              type="text"
              size="small"
              @click="handleOpenEditView('view', scope.row)"
              v-permission="option.buttons.query.permission"
            >查看</el-button
            >
            <el-button
              v-if="
                option.buttons.edit.isShow == undefined
                  ? true
                  : option.buttons.edit.isShow
              "
              type="text"
              size="small"
              @click="handleOpenEditView('edit', scope.row)"
              v-permission="option.buttons.edit.permission"
            >编辑</el-button
            >
            <el-button
              v-if="
                hasRowCustomButton == false &&
                  option.buttons.delete.isShow == undefined
                  ? true
                  : option.buttons.edit.isShow
              "
              type="text"
              size="small"
              @click="handleDeleteBatch(scope.row)"
              v-permission="option.buttons.delete.permission"
            >删除</el-button
            >
            <el-dropdown v-if="hasRowCustomButton" trigger="click">
              <span class="el-dropdown-link">
                更多<i class="el-icon-caret-bottom el-icon--right" />
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item class="clearfix">
                  <slot name="rowButton" :msg="scope.row" />
                  <el-button
                    v-if="
                      option.buttons.delete.isShow == undefined
                        ? true
                        : option.buttons.edit.isShow
                    "
                    type="text"
                    size="small"
                    @click="handleDeleteBatch(scope.row)"
                    v-permission="option.buttons.delete.permission"
                  >删除</el-button
                  >
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination
          v-show="total > 0"
          background
          :current-page.sync="queryParams.pageNumber"
          :page-sizes="$pageSizeAll"
          :page-size="queryParams.pageSize"
          layout="total, prev, pager, next, jumper, sizes"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
        <div>
          <slot name="tableSelectionBtn" :selection="checkRecords" />
        </div>
      </div>

      <!-- 表格结束 -->

      <EditDialog
        ref="edit"
        :option="option"
        :model-type="editDialogModelType"
        :visible="editDialogOpen"
        :row-data="editDialogRowData"
        @closeEvent="editDialogClosedEvent"
      >
        <template v-slot:customCard>
          <slot name="cardInEditPage" />
        </template>
        <template slot="editBtn" slot-scope="scope">
          <slot name="editBtnPage" :rowData="scope" />
        </template>
      </EditDialog>
    </div>
    <slot name="pageSection" />
  </div>
</template>
<script>
import AnjiTree from '@/components/AnjiPlus/anji-tree.vue'
import EditDialog from './edit'
import request from '@/utils/request'
export default {
  components: {
    EditDialog,
    AnjiTree
  },
  props: {
    option: {
      require: true,
      type: Object,
      default: () => {
        return {
          // 查询表单条件
          queryFormFields: [],
          // 按钮
          buttons: {
            query: {},
            edit: {},
            delete: {},
            add: {}
          },
          // 表格列
          columns: [],
          queryFormChange: (fileName, val) => {}
        }
      }
    }
  },
  data() {
    return {
      // 查询表单提交的值
      queryParams: {
        showMoreSearch: false, // 是否展开更多搜索条件
        pageNumber: 1,
        pageSize: 10,
        order: '',
        sort: ''
      },

      checkRecords: [], // 表格中当前选中的记录
      records: [], // 接口返回的记录列表
      total: 0, // 接口返回的总条数

      // 编辑详情弹框
      editDialogOpen: false, // 新建时主动打开编辑弹框
      editDialogRowData: {}, // 编辑时的主键
      editDialogModelType: 'view', // 编辑 查看

      hasRowCustomButton: false // 除了编辑删除外，还有自定义的行按钮
    }
  },
  computed: {
    // 左侧树形查询条件
    queryFormTreeField() {
      var treeField = this.option.queryFormFields.find(
        item => item['inputType'] == 'anji-tree'
      )
      return treeField
    },
    // 查询条件里是否有树形控件
    hasTreeFieldInQueryForm() {
      return this.isNotBlank(this.queryFormTreeField)
    },
    // 不包含树形控件的查询条件
    queryFormFieldExcludeTree() {
      var treeFields = this.option.queryFormFields.filter(
        item => item['inputType'] != 'anji-tree'
      )
      return treeFields
    },
    // 主键的列名
    primaryKeyFieldName() {
      var primaryKey = this.option.columns.find(
        item => item['primaryKey'] == true
      )
      if (primaryKey != null) {
        return primaryKey['field']
      } else {
        return null
        console.warn(
          '在columns中查找primaryKey=true失败，会导致查询详情和删除失败'
        )
      }
    },

    // 表格中可展开的列
    tableExpandColumns() {
      var expandColumns = this.option.columns.filter(
        item => item['columnType'] == 'expand'
      )
      return expandColumns
    },

    // 是否可以批量删除
    disableBatchDelete() {
      return this.checkRecords.length <= 0
    }
  },
  created() {
    // 为查询框中所有input加上默认值
    this.option.queryFormFields.forEach(item => {
      // 动态添加属性
      this.$set(this.queryParams, item.field, item.defaultValue || null)
    })
    // 查询列表
    this.handleQueryForm('query')
    this.queryFormChange()
  },
  mounted() {
    if (this.$scopedSlots['rowButton'] != null) {
      this.hasRowCustomButton = true
    } else {
      this.hasRowCustomButton = false
    }
  },
  methods: {
    queryFormFieldSpan(item) {
      // console.log(item)

      if (item.span != null) {
        return item.span
      } else {
        return 6
      }
      // let rowLength = this.option.queryFormFields.length;
      // console.log(rowLength, "ss")
      // console.log(rowLength % 3)
      // if (rowLength <= 3) {
      //   return 6
      // }
      // else if (rowLength % 3 == 0) {
      //   return 8
      // } else if (rowLength > 6) {
      //   return 8
      // }
    },
    // 切换更多搜索条件
    handleToggleMoreSearch() {
      this.queryParams.showMoreSearch = !this.queryParams.showMoreSearch
    },
    // 列上排序切换
    handleSortChange(column) {
      // {column: {…}, prop: "orgCode", order: "ascending"}
      if (column == null || column.prop == null) {
        console.warn('排序字段名prop为空，无法排序')
        return
      }
      var sort = column.prop // 列表查询默认排序列
      var order = column.order == 'ascending' ? 'ASC' : 'DESC'
      this.queryParams['sort'] = sort
      this.queryParams['order'] = order
      this.handleQueryForm('query')
    },
    // 查询按钮
    handleQueryForm(from) {
      // 如果是点查询按钮，把树的查询属性去掉
      if (from == 'query') {
        if (this.hasTreeFieldInQueryForm) {
          delete this.queryParams[this.queryFormTreeField.field]
        }
      }
      // 如果是点树查询，把查询区里的属性去掉
      if (from == 'tree') {
        if (this.hasTreeFieldInQueryForm) {
          var treeVal = this.queryParams[this.queryFormTreeField.field]
          this.queryParams = {
            pageNumber: 1,
            pageSize: 10
          }
          this.queryParams[this.queryFormTreeField.field] = treeVal
        }
      }
      // 默认的排序
      if (
        this.isBlank(this.queryParams['order']) &&
        this.isNotBlank(this.option.buttons.query.order)
      ) {
        this.queryParams['sort'] = this.option.buttons.query.sort
        this.queryParams['order'] = this.option.buttons.query.order
      }
      this.queryParams.pageNumber = 1
      this.handleQueryPageList()
    },
    // 列表查询
    async handleQueryPageList() {
      var params = this.queryParams
      // 将特殊参数值urlcode处理 var params = this.urlEncodeObject(this.queryParams, 'order,sort')
      const { data, code } = await this.option.buttons.query.api(params)
      if (code != '200') return
      this.records = data.records
      this.total = data.total
    },
    // 重置
    handleResetForm() {
      this.queryParams = {
        pageNumber: 1,
        pageSize: 10
      }
      // this.$refs['queryForm'].resetFields()
      // this.records = []
      // this.total = 0
    },
    // 树形查询条件点击回调
    handleTreeNodeCheck() {
      this.handleQueryForm('tree')
      // 为新建页面的对应属性值，绑定上对应的默认值
      var treeFieldName = this.queryFormTreeField['field']
      for (var i = 0; i < this.option.columns.length; i++) {
        var item = this.option.columns[i]
        if (
          item['editField'] == treeFieldName ||
          item['field'] == treeFieldName
        ) {
          this.$set(
            this.option.columns[i],
            'defaultValue',
            this.queryParams[treeFieldName]
          )
          break
        }
      }
    },
    // 编辑和查看操作
    handleOpenEditView(modelType, row) {
      if (modelType == 'view' || modelType == 'edit') {
        this.editDialogRowData = row
      }
      this.editDialogModelType = modelType
      if (modelType == 'add') {
        // 新增模式，不需要查询数据详情，直接打开
        this.editDialogOpen = true
      }
      const obj = {
        type: modelType,
        value: row
      }
      this.$emit('handleCustomValue', obj)
    },
    // 弹框被关闭时的回调事件
    editDialogClosedEvent(value) {
      // 把列表页中弹框打开标记改成已关闭
      this.editDialogOpen = false
      // 关闭弹出框时，如果有树，刷新下
      if (
        this.hasTreeFieldInQueryForm &&
        this.$refs.queryFormTree != null &&
        !value
      ) {
        this.$refs.queryFormTree.queryData()
      }
      this.handleQueryPageList()
      // 关闭时 清空表单的验证规则
      this.$refs.edit.$refs.mainForm.$refs.editForm.resetFields()
    },
    // 批量删除
    handleDeleteBatch(row) {
      var ids = []
      if (row != null) {
        ids.push(row[this.primaryKeyFieldName]) // 删除指定的行
      } else {
        // 批量删除选中的行
        ids = this.checkRecords.map(item => item[this.primaryKeyFieldName])
      }
      this.$confirm('删除确认', '确认要删除吗?', {
        type: 'warning',
        confirmButtonClass: 'delete_sure',
        cancelButtonClass: 'el-button--danger is-plain'
      })
        .then(() => {
          this.option.buttons.delete.api(ids).then(res => {
            // {code: "200", message: "操作成功", data: true}
            this.checkRecords = []
            // 关闭弹出框时，如果有树，刷新下
            if (
              this.hasTreeFieldInQueryForm &&
              this.$refs.queryFormTree != null
            ) {
              this.$refs.queryFormTree.queryData()
            }
            this.handleQueryPageList()
          })
        })
        .catch(e => {
          e
        })
    },

    // 选择项改变时
    handleSelectionChange(val) {
      this.checkRecords = val
    },
    // 页码改变
    handleCurrentChange(pageNumber) {
      this.queryParams.pageNumber = pageNumber
      this.handleQueryPageList()
    },
    // 每页size改变时
    handleSizeChange(val) {
      this.queryParams.pageNumber = 1
      this.queryParams.pageSize = val
      this.handleQueryPageList()
    },
    // table列文件缩略图
    thumbnailUrl(row, field) {
      // return 'http://10.108.3.123:9090/tms/file/download/79ee7e8b-2a9a-4142-b06d-706ac8089205'
      // if (row.filePath) {
      //   if (row.filePath.endsWith('xlsx') || row.filePath.endsWith('xls')) {
      //     return fileExcel;
      //   } else if (row.filePath.endsWith('pdf')) {
      //     return filePdf;
      //   }
      //   return process.env.VUE_APP_BASE_API + '/tms/file/download/' + row.fileId;
      // } else {
      //   return logo;
      // }
    },
    // 带单位的列，需要转换
    fieldLabel(columnConfig) {
      if (columnConfig == null) {
        return ''
      }
      if (
        columnConfig.inputType == 'anji-input' &&
        columnConfig.anjiInput != null
      ) {
        return `${columnConfig.label}(${columnConfig.anjiInput.unit})`
      } else {
        return columnConfig.label
      }
    },
    // 带单位的输入框
    fieldValueByAnjiInput(value, columnConfig) {
      if (columnConfig == null) {
        return value
      }
      if (
        columnConfig.inputType == 'anji-input' &&
        columnConfig.anjiInput != null
      ) {
        return value / columnConfig.anjiInput.conversion
      } else {
        return value
      }
    },
    // 带表格列格式化的值
    fieldValueByRowRenderer(row, columnConfig) {
      if (
        columnConfig == null ||
        typeof columnConfig.fieldTableRowRenderer != 'function'
      ) {
        return row[columnConfig.field]
      } else {
        return columnConfig.fieldTableRowRenderer(row)
      }
    },
    // 暴露给外部crud页面，回传saveForm的值
    getMainEntity() {
      return this.$refs.edit.getSaveForm()
    },
    setMainEntity(object) {
      this.$refs.edit.setSaveForm(object)
    },
    async switchChange(val, api) {
      request({
        url: api.url,
        method: 'put',
        headers: { noPrompt: false },
        data: [val.id]
      }).then(response => {
        this.handleQueryPageList()
      })
    },
    queryFormChange(fileName, fieldVal) {
      if (typeof this.option.queryFormChange == 'function') {
        this.option.queryFormChange(this.queryParams, fileName, fieldVal)
      }
    }
  }
}
</script>

<style scoped lang="scss">
.style-btn {
  pointer-events: none;
}
.page-container {
  height: 100%;
  position: relative;
  .left-container {
    width: 20%;
    position: absolute;
    top: 0;
    left: 0;
    background: #fff;
    border-radius: 4px 0px 0px 4px;
    padding: 20px 20px 0;
    overflow: hidden;
    overflow-y: auto;
    height: 100%;
  }
  .right-container {
    width: calc(80% - 5px);
    position: absolute;
    top: 0;
    right: 0;
    background: #fff;
    border-radius: 0px 4px 4px 0px;
    padding: 20px 20px 0;
    height: 100%;
  }
}
.el-table .cell,
.el-table td div {
  overflow: hidden;
  text-overflow: ellipsis;
}
.el-dropdown {
  font-size: 12px;
  display: inline;
  color: #5887fb;
  cursor: pointer;
}
.el-dropdown-menu--mini .el-dropdown-menu__item {
  min-width: 80px;
  max-width: 110px;
  float: right;
  .el-button--text {
    float: right;
  }
  &:hover {
    background: none !important;
  }
  .el-button--mini {
    float: right;
  }
  .el-button + .el-button {
    margin-left: 0 !important;
    float: right;
  }
}
.pagination {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  margin-top: 20px;
}
.anji_curd_table {
  margin-top: 20px;
}
</style>
