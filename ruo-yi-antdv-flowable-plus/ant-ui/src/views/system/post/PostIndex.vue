<template>
  <div>
    <a-card :bordered="false" style="margin-bottom: 10px;">
      <div class="table-page-search-wrapper">
        <a-form :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-row :gutter="48">
            <a-col :md="6" :sm="24">
              <a-form-item label="岗位名称">
                <a-input v-model="queryParam.postName" placeholder="请输入岗位名称" allow-clear @keyup.enter.native="handleQuery"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item label="岗位编码">
                <a-input v-model="queryParam.postCode" placeholder="请输入岗位编码" allow-clear @keyup.enter.native="handleQuery"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item label="状态">
                <a-select placeholder="请选择" v-model="queryParam.status" style="width: 100%" allow-clear>
                  <a-select-option v-for="(d, index) in statusOptions" :key="index" :value="d.dictValue">{{ d.dictLabel }}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col>
              <span class="table-page-search-submitButtons" style="float: right;">
                <a-button type="primary" @click="handleQuery"><a-icon type="search" />查询</a-button>
                <a-button style="margin-left: 8px" @click="resetQuery"><a-icon type="redo" />重置</a-button>
              </span>
            </a-col>
          </a-row>
        </a-form>
      </div>
    </a-card>
    <a-card :bordered="false" class="table-card">
      <!-- 增加 -->
      <post-add-form
        v-if="showAddModal"
        ref="postAddForm"
        :statusOptions="statusOptions"
        @ok="getList"
        @close="showAddModal = false"
      />
      <!-- 修改 -->
      <post-edit-form
        v-if="showEditModal"
        ref="postEditForm"
        :statusOptions="statusOptions"
        @ok="getList"
        @close="showEditModal = false"
      />
      <advance-table
        :columns="columns"
        :data-source="list"
        title="岗位管理"
        :loading="loading"
        rowKey="configId"
        @refresh="getList"
        size="middle"
        tableKey="system-post-PostIndex-table"
        :format-conditions="true"
        :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        :pagination="{
          current: queryParam.pageNum,
          pageSize: queryParam.pageSize,
          total: total,
          showSizeChanger: true,
          showLessItems: true,
          showQuickJumper: true,
          showTotal: (total, range) => `第 ${range[0]}-${range[1]} 条，总计 ${total} 条`,
          onChange: changeSize,
          customRow: onClickRow,
          onShowSizeChange: onSizeChange,
        }"
      >
        <div class="table-operations" slot="button">
          <a-button type="primary" size="small" @click="handleAdd()" v-hasPermi="['system:config:add']">
            <a-icon type="plus" />新增
          </a-button>
          <a-button type="danger" :disabled="multiple" @click="handleDelete" v-hasPermi="['system:config:remove']">
            <a-icon type="delete" />删除
          </a-button>
          <a-button v-if="!multiple" @click="handleExport" v-hasPermi="['system:config:export']">
            <a-icon type="download" />导出
          </a-button>
        </div>
        <span slot="status" slot-scope="{text, record}">
          <a-badge :status="record.status == '0' ? 'processing' : 'error'" :text=" statusFormat(record) " />
        </span>
        <span slot="createTime" slot-scope="{text, record}">
          {{ parseTime(record.createTime) }}
        </span>
        <span slot="operation" slot-scope="{text, record}">
          <a @click="handleUpdate(record, undefined)" v-hasPermi="['system:post:edit']">
            修改
          </a>
          <a-divider type="vertical" v-hasPermi="['system:post:remove']" />
          <a @click="handleDelete(record)" v-hasPermi="['system:post:remove']">
            删除
          </a>
        </span>
      </advance-table>
    </a-card>
  </div>
</template>
<script>
import { listPost, delPost, exportPost } from '@/api/system/post'
import PostEditForm from './modules/PostEditForm'
import PostAddForm from './modules/PostAddForm'
import AdvanceTable from '@/components/pt/table/AdvanceTable'
export default {
  name: 'Post',
  components: {
    PostEditForm,
    PostAddForm,
    AdvanceTable
  },
  data () {
    return {
      showAddModal: false,
      showEditModal: false,
      list: [],
      selectedRowKeys: [],
      selectedRows: [],
      // 高级搜索 展开/关闭
      advanced: false,
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      ids: [],
      postNames: [],
      loading: false,
      sunloading: false,
      total: 0,
      // 类型数据字典
      statusOptions: [],
      // 日期范围
      dateRange: [],
      labelCol: { span: 6 },
      wrapperCol: { span: 18 },
      queryParam: {
        pageNum: 1,
        pageSize: 10,
        postCode: undefined,
        postName: undefined,
        status: undefined
      },
      addModalRefName: 'addModal', // 添加弹窗ref名称
      columns: [
        {
          title: '岗位编码',
          dataIndex: 'postCode',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '岗位名称',
          dataIndex: 'postName',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '排序',
          dataIndex: 'sort',
          align: 'center'
        },
        {
          title: '状态',
          dataIndex: 'status',
          scopedSlots: { customRender: 'status' },
          align: 'center'
        },
        {
          title: '创建时间',
          dataIndex: 'createTime',
          ellipsis: true,
          scopedSlots: { customRender: 'createTime' },
          align: 'center'
        },
        {
          title: '操作',
          dataIndex: 'operation',
          width: '15%',
          scopedSlots: { customRender: 'operation' },
          align: 'center'
        }
      ]
    }
  },
  filters: {
  },
  created () {
    this.getList()
    this.getDicts('sys_normal_disable').then(response => {
      this.statusOptions = response.data
    })
  },
  computed: {
  },
  watch: {
  },
  methods: {
    /** 查询定时任务列表 */
    getList () {
      this.showAddModal = false
      this.showEditModal = false
      this.loading = true
      listPost(this.addDateRange(this.queryParam, this.dateRange)).then(response => {
          this.list = response.data.list
          this.list.map((item) => { item.operation = item.remark })
          this.total = response.data.total
          this.loading = false
        }
      )
    },
    // 岗位状态字典翻译
    statusFormat (row) {
      return this.selectDictLabel(this.statusOptions, row.status)
    },
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParam.pageNum = 1
      this.getList()
    },
    handleAdd (record) {
      this.showAddModal = true
      this.$nextTick(() => (
        this.$refs.postAddForm.handleAdd(record)
      ))
    },
    handleUpdate (record, ids) {
      this.showEditModal = true
      this.$nextTick(() => (
        this.$refs.postEditForm.handleUpdate(record, ids)
      ))
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.dateRange = []
      this.queryParam = {
        pageNum: 1,
        pageSize: 10,
        postCode: undefined,
        postName: undefined,
        status: undefined
      }
      this.handleQuery()
    },
    onShowSizeChange (current, pageSize) {
      this.queryParam.pageSize = pageSize
      this.getList()
    },
    onSizeChange (current, size) {
      this.queryParam.pageNum = 1
      this.queryParam.pageSize = size
      this.getList()
    },
    changeSize (current, pageSize) {
      this.queryParam.pageNum = current
      this.queryParam.pageSize = pageSize
      this.getList()
    },
    onSelectChange (selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectedRows
      this.ids = this.selectedRows.map(item => item.id)
      this.postNames = this.selectedRows.map(item => item.postName)
      this.single = selectedRowKeys.length !== 1
      this.multiple = !selectedRowKeys.length
    },
    onClickRow (record) {
      return {
        on: {
          click: () => {
            const keys = []
            keys.push(record.id)
            this.selectedRowKeys = keys
          }
        }
      }
    },
    toggleAdvanced () {
      this.advanced = !this.advanced
    },
    /** 删除按钮操作 */
    handleDelete (row) {
      var that = this
      const postIds = row.id || this.ids
      const postNames = row.postName || this.postNames
      this.$confirm({
        title: '确认删除所选中数据?',
        content: '当前选中岗位名称为"' + postNames + '"的数据',
        onOk () {
          return delPost(postIds)
            .then(() => {
              that.onSelectChange([], [])
              that.getList()
              that.$message.success(
                '删除成功',
                3
              )
            })
        },
        onCancel () {}
      })
    },
    /** 导出按钮操作 */
    handleExport () {
      var that = this
      this.$confirm({
        title: '是否确认导出?',
        content: '此操作将导出当前条件下所有数据而非选中数据',
        onOk () {
          return exportPost(that.queryParam)
            .then(response => {
              that.download(response.msg)
              that.$message.success(
                '导出成功',
                3
              )
            })
        },
        onCancel () {}
      })
    }
  }
}
</script>
