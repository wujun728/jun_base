<template>
  <div>
    <a-card :bordered="false" style="margin-bottom: 10px;">
      <!-- 条件搜索 -->
      <div class="table-page-search-wrapper">
        <a-form :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-row :gutter="48">
            <a-col :md="6" :sm="24">
              <a-form-item label="系统模块">
                <a-input v-model="queryParam.title" placeholder="请输入系统模块" allow-clear @keyup.enter.native="handleQuery"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item label="操作人员">
                <a-input v-model="queryParam.operName" placeholder="请输入操作人员" allow-clear @keyup.enter.native="handleQuery"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item label="类型">
                <a-select placeholder="操作类型" v-model="queryParam.businessType" style="width: 100%" allow-clear>
                  <a-select-option v-for="(d, index) in typeOptions" :key="index" :value="d.dictValue">{{ d.dictLabel }}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24" v-if="advanced">
              <a-form-item label="状态">
                <a-select placeholder="操作状态" v-model="queryParam.status" style="width: 100%" allow-clear>
                  <a-select-option v-for="(d, index) in statusOptions" :key="index" :value="d.dictValue">{{ d.dictLabel }}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24" v-if="advanced">
              <a-form-item label="操作时间">
                <a-range-picker style="width: 100%" v-model="dateRange" valueFormat="YYYY-MM-DD" format="YYYY-MM-DD" allow-clear/>
              </a-form-item>
            </a-col>
            <a-col>
              <span class="table-page-search-submitButtons" style="float: right;">
                <a-button type="primary" @click="handleQuery"><a-icon type="search" />查询</a-button>
                <a-button style="margin-left: 8px" @click="resetQuery"><a-icon type="redo" />重置</a-button>
                <a @click="toggleAdvanced" style="margin-left: 8px">
                  {{ advanced ? '收起' : '展开' }}
                  <a-icon :type="advanced ? 'up' : 'down'"/>
                </a>
              </span>
            </a-col>
          </a-row>
        </a-form>
      </div>
    </a-card>
    <a-card :bordered="false" class="table-card">
      <view-form ref="viewForm" />
      <advance-table
        :columns="columns"
        :data-source="list"
        title="系统参数"
        :loading="loading"
        rowKey="id"
        tableKey="monitor-operlog-Operlog-table"
        :isTableConfig="false"
        :isShowSetBtn="false"
        @refresh="getList"
        size="middle"
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
          onShowSizeChange: onSizeChange
        }"
      >
        <div class="table-operations" slot="button">
          <a-button type="danger" v-if="!multiple" :disabled="multiple" @click="handleDelete" v-hasPermi="['monitor:operlog:remove']">
            <a-icon type="delete" />删除
          </a-button>
          <a-button type="danger" @click="handleClean" v-hasPermi="['monitor:operlog:remove']">
            <a-icon type="delete" />清空
          </a-button>
          <a-button @click="handleExport" v-hasPermi="['system:config:export']">
            <a-icon type="download" />导出
          </a-button>
        </div>
        <span slot="businessType" slot-scope="{text, record}">
          <a-tag :color="text | operTypeFilter">
            {{ typeFormat(record) }}
          </a-tag>
        </span>
        <span slot="status" slot-scope="{text, record}">
          <a-badge :status="record.status == '0' ? 'processing' : 'error'" :text=" statusFormat(record) " />
        </span>
        <span slot="operation" slot-scope="{text, record}">
          <a @click="$refs.viewForm.handleView(record)" v-hasPermi="['monitor:operlog:query']">
            <a-icon type="eye" theme="twoTone"/>详细
          </a>
        </span>
      </advance-table>
    </a-card>
  </div>
</template>
<script>
import { list, delOperlog, cleanOperlog, exportOperlog } from '@/api/monitor/operlog'
import ViewForm from './modules/ViewForm'
import AdvanceTable from '@/components/pt/table/AdvanceTable'
export default {
  name: 'Operlog',
  components: {
    ViewForm,
    AdvanceTable
  },
  data () {
    return {
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
      loading: false,
      sunloading: false,
      total: 0,
      // 状态数据字典
      statusOptions: [],
      typeOptions: [],
      labelCol: { span: 6 },
      wrapperCol: { span: 18 },
      // 日期范围
      dateRange: [],
      queryParam: {
        pageNum: 1,
        pageSize: 10,
        title: undefined,
        operName: undefined,
        businessType: undefined,
        status: undefined
      },
      addModalRefName: 'addModal', // 添加弹窗ref名称
      columns: [
        {
          title: '系统模块',
          dataIndex: 'title',
          align: 'center'
        },
        {
          title: '操作类型',
          dataIndex: 'businessType',
          scopedSlots: { customRender: 'businessType' },
          align: 'center'
        },
        {
          title: '请求方式',
          dataIndex: 'requestMethod',
          align: 'center'
        },
        {
          title: '操作人员',
          dataIndex: 'operName',
          align: 'center'
        },
        {
          title: '主机',
          dataIndex: 'operIp',
          align: 'center'
        },
        {
          title: '操作地点',
          dataIndex: 'operLocation',
          align: 'center'
        },
        {
          title: '状态',
          dataIndex: 'status',
          scopedSlots: { customRender: 'status' },
          align: 'center'
        },
        {
          title: '耗时',
          dataIndex: 'takeUpTime',
          align: 'center'
        },
        {
          title: '操作日期',
          dataIndex: 'operTime',
          align: 'center'
        },
        {
          title: '操作',
          dataIndex: 'operation',
          scopedSlots: { customRender: 'operation' },
          align: 'center'
        }
      ]
    }
  },
  filters: {
    operTypeFilter (type) {
      let value = 'blue'
      if (type === 3 || type === 7 || type === 9) {
        value = 'orange'
      }
      return value
    }
  },
  created () {
    this.getList()
    this.getDicts('sys_common_status').then(response => {
      this.statusOptions = response.data
    })
    this.getDicts('sys_oper_type').then(response => {
      this.typeOptions = response.data
    })
  },
  computed: {
  },
  watch: {
  },
  methods: {
    onSizeChange (current, size) {
      this.queryParam.pageNum = 1
      this.queryParam.pageSize = size
      this.getList()
    },
    /** 查询定时任务列表 */
    getList () {
      this.loading = true
      list(this.addDateRange(this.queryParam, this.dateRange)).then(response => {
          this.list = response.data.list
          this.total = response.data.total
          this.loading = false
        }
      )
    },
    // 操作日志状态字典翻译
    statusFormat (row, column) {
      return this.selectDictLabel(this.statusOptions, row.status)
    },
    // 操作日志类型字典翻译
    typeFormat (row, column) {
      return this.selectDictLabel(this.typeOptions, row.businessType)
    },
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParam.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.dateRange = []
      this.queryParam = {
        pageNum: 1,
        pageSize: 10,
        title: undefined,
        operName: undefined,
        businessType: undefined,
        status: undefined
      }
      this.handleQuery()
    },
    onShowSizeChange (current, pageSize) {
      this.queryParam.pageSize = pageSize
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
      this.multiple = !selectedRowKeys.length
    },
    toggleAdvanced () {
      this.advanced = !this.advanced
    },
    /** 删除按钮操作 */
    handleDelete (row) {
      var that = this
      const operIds = row.id || this.ids
      this.$confirm({
        title: '确认删除所选中数据?',
        content: '当前选中编号为' + operIds + '的数据',
        onOk () {
          return delOperlog(operIds)
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
    /** 清空按钮操作 */
    handleClean () {
      var that = this
      this.$confirm({
        title: '是否确认清空?',
        content: '此操作将会清空所有操作日志数据项',
        onOk () {
          return cleanOperlog()
            .then(() => {
              that.onSelectChange([], [])
              that.getList()
              that.$message.success(
                '清空成功',
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
          return exportOperlog(that.queryParam)
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
