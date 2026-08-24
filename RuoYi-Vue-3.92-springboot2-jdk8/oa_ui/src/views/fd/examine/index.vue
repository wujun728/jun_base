<!-- 财务-付款审核页面 -->
<template>
  <div class="divBox">
    <el-card class="mb14 normal-page">
      <div class="from-s">
        <div class="flex-row flex-col">
          <el-button class="mb20" size="small" @click="getExportData"> 导出 </el-button>
          <formBox ref="formBox" :activeName="activeName" @confirmData="confirmData" />
        </div>
      </div>

      <div class="splitLine"></div>
      <div class="mt10">
        <div class="inTotal">共 {{ total }} 条</div>

        <el-table
          :data="tableData"
          style="width: 100%"
          row-key="id"
          class="mt10"
          default-expand-all
          @sort-change="sortChange"
        >
          <el-table-column prop="date" min-width="150" sortable label="付款时间" />
          <el-table-column prop="bill_types" label="记录类型" min-width="90">
            <template slot-scope="scope">
              <el-tag :type="scope.row.types == 2 ? 'warning' : 'success'">{{
                scope.row.types == 2 ? '支出' : '收入'
              }}</el-tag>
            </template>
          </el-table-column>

          <el-table-column prop="num" min-width="90" label="付款金额(元)" />
          <el-table-column prop="pay_type" min-width="100" label="支付方式">
            <template slot-scope="scope">
              <span>{{ scope.row.pay_type !== '' ? scope.row.pay_type : '--' }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="treaty.contract_name" label="合同名称" min-width="120"> </el-table-column>
          <el-table-column prop="treaty.contract_no" label="合同编号" min-width="100"> </el-table-column>
          <el-table-column prop="created_at" label="申请时间" min-width="140"> </el-table-column>

          <el-table-column prop="status" min-width="120" label="付款审核状态">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.status === 0" type="warning" size="mini"> {{ $t('customer.audit') }}</el-tag>
              <el-tag v-if="scope.row.status === 1" type="info" size="mini"> {{ $t('customer.passed') }}</el-tag>
              <el-popover v-if="scope.row.status === 2" trigger="hover" placement="top">
                <p>{{ $t('customer.reason') }}:</p>
                <p>{{ scope.row.fail_msg }}</p>
                <div slot="reference">
                  <el-tag type="danger" size="mini"> {{ $t('customer.fail') }}</el-tag>
                </div>
              </el-popover>
            </template>
          </el-table-column>

          <el-table-column
            prop="address"
            fixed="right"
            v-if="activeName == '1'"
            :label="$t('public.operation')"
            min-width="160"
          >
            <template slot-scope="scope">
              <el-button type="text" @click="handleCheck(scope.row)" v-hasPermi="['fd:examine:check']">查看</el-button>

              <el-button type="text" @click="handleContract(scope.row, '')">通过</el-button>
              <el-button type="text" @click="refuse(scope.row)">拒绝</el-button>
            </template>
          </el-table-column>
          <el-table-column
            prop="address"
            fixed="right"
            v-if="activeName == '2'"
            :label="$t('public.operation')"
            min-width="180"
          >
            <template slot-scope="scope">
              <el-button type="text" @click="handleCheck(scope.row)" v-hasPermi="['fd:examine:check']">查看</el-button>
              <el-button
                type="text"
                v-if="scope.row.status !== 0"
                @click="handleContract(scope.row, 'edit')"
                v-hasPermi="['fd:examine:edit']"
                >编辑</el-button
              >
              <el-dropdown v-if="scope.row.status !== 0 && scope.row.invoice_id == 0">
                <span class="el-dropdown-link el-button--text el-button">
                  {{ $t('hr.more') }}
                </span>
                <el-dropdown-menu>
                  <el-dropdown-item @click.native="withdraw(scope.row)">撤回审核 </el-dropdown-item>
                  <el-dropdown-item @click.native="handleDelete(scope.row)">删除 </el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
              <template v-else>
                <el-button type="text" v-if="scope.row.status !== 0" @click="handleDelete(scope.row)">删除</el-button>
              </template>

              <el-button v-if="scope.row.status == 0" type="text" @click="handleContract(scope.row)">通过</el-button>
              <el-button type="text" v-if="scope.row.status == 0" @click="refuse(scope.row)">拒绝</el-button>
            </template>
          </el-table-column>
        </el-table>

        <div class="paginationClass">
          <el-pagination
            :page-size="where.limit"
            :current-page="where.page"
            :page-sizes="[10, 15, 20]"
            layout="total, prev, pager, next, jumper"
            :total="total"
            @size-change="handleSizeChange"
            @current-change="pageChange"
          />
        </div>
      </div>
    </el-card>

    <!-- 拒绝弹窗 -->
    <el-dialog
      class="dialog"
      :title="title"
      top="25vh"
      :append-to-body="true"
      :visible.sync="dialogVisible"
      width="540px"
    >
      <div class="line" />
      <el-form :model="form" ref="from" :rules="rules" class="from">
        <el-form-item :label="reason + '：'" label-width="90px" prop="remarks">
          <el-input type="textarea" v-model="form.remarks"></el-input>
        </el-form-item>
        <div class="footer">
          <el-button size="small" class="btn" @click="cancelFn">取消</el-button>
          <el-button size="small" type="primary" @click="submitFn" class="btn">确定</el-button>
        </div>
      </el-form>
    </el-dialog>
    <!-- 编辑付款记录弹窗 -->
    <examine-dialog ref="examineDialog" :config="config" @isOk="isOk"></examine-dialog>
    <!-- 编辑账目记录弹窗 -->
    <operationDialog
      ref="operationDialog"
      :statusOption="statusOptions"
      :config="operationDialog"
      @isOk="isOk"
    ></operationDialog>
    <!-- 查看图片 -->
    <el-image-viewer v-if="isImage" :on-close="closeImageViewer" :url-list="srcList" />
    <!-- 导出组件 -->
    <export-excel :template="false" :save-name="saveName" :export-data="exportData" ref="exportExcel" />
    <!-- 查看付款记录详情侧滑弹窗 -->
    <applyForPayment
      ref="applyForPayment"
      :form-data="fromData"
      @isOk="getTableData"
      :paymentType="paymentType"
    ></applyForPayment>
    <!-- 支出弹窗 -->
    <expend-dialog ref="expendDialog" :config="operationDialog" @isOk="isOk"></expend-dialog>
  </div>
</template>

<script>
import { clientBillListApi, clientBillStatusApi, getbillCate, billCateApi, billDelFinanceApi } from '@/api/enterprise'

export default {
  name: 'Index',
  components: {
    formBox: () => import('./components/formBox'),
    examineDialog: () => import('@/views/fd/examine/components/examineDialog'),
    operationDialog: () => import('@/views/fd/examine/components/operationDialog'),
    ElImageViewer: () => import('element-ui/packages/image/src/image-viewer'),
    exportExcel: () => import('@/components/common/exportExcel'),
    applyForPayment: () => import('@/views/customer/list/components/applyForPayment'),
    expendDialog: () => import('@/views/customer/list/components/expendDialog')
  },
  props: {
    activeName: {
      type: String,
      default: '1'
    },
    types: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      tableData: [],
      dialogVisible: false,
      reason: '拒绝原因',
      title: '审核未通过',
      paymentType: '',
      form: {
        remarks: ''
      },
      saveName: '',
      exportData: {
        data: [],
        cols: [
          { wpx: 130 },
          { wpx: 70 },
          { wpx: 120 },
          { wpx: 120 },
          { wpx: 130 },
          { wpx: 130 },
          { wpx: 110 },
          { wpx: 110 }
        ]
      },
      statusOptions: [],
      catePath: [],
      rules: {
        remarks: [{ required: true, message: '请填写拒绝原因', trigger: 'blur' }]
      },

      where: {
        types: '',
        page: 1,
        status: 0,
        time: '',
        name: '',
        time_field: 'time',
        limit: 15,
        no_withdraw: 1,
        sort: ''
      },
      paymentId: '',
      total: 0,
      config: {},
      isImage: false,
      srcList: [],
      operationDialog: {},
      fromData: {}
    }
  },
  created() {
    this.where.status = this.types
    this.getTableData()
    this.getOption()
  },
  methods: {
    pageChange(page) {
      this.where.page = page
      this.getTableData()
    },
    sortChange(column) {
      if (column.order === 'ascending') {
        this.where.sort = column.prop + ' asc'
      } else if (column.order === 'descending') {
        this.where.sort = column.prop + ' desc'
      } else {
        this.where.sort = ''
      }

      this.getTableData()
    },
    handleSizeChange(val) {
      this.where.limit = val
      this.getTableData()
    },
    tabClick() {
      if (this.activeName == '1') {
        this.where.status = '0'
        this.getTableData()
      } else {
        this.where.status = ''
        this.getTableData()
      }
    },

    // 查看
    async handleCheck(item) {
      if (item.status === 0) {
        this.paymentType = 'fd'
      } else {
        this.paymentType = ''
      }
      this.fromData = {
        title: this.$t('customer.viewcustomer'),
        width: '500px',
        data: item,
        isClient: false,
        edit: true
      }

      this.$refs.applyForPayment.openBox()
    },
    // 撤回审核
    async withdraw(row) {
      let data = {
        status: -1
      }
      await this.$modalSure('确认撤回审核状态吗')
      await clientBillStatusApi(row.id, data)
      this.getTableData()
    },

    // 拒绝
    refuse(row) {
      this.dialogVisible = true
      this.paymentId = row.id
    },
    // 导出
    getExportData() {
      this.saveName = '导出付款审核_' + this.$moment(new Date()).format('MM_DD_HH_mm_ss') + '.xlsx'
      this.where.limit = 0

      let where = {
        types: this.where.types,
        page: this.where.page,
        status: this.where.status,
        time: this.where.time,
        name: this.where.name,
        time_field: this.where.time_field,
        limit: 0,
        no_withdraw: this.where.no_withdraw
      }
      clientBillListApi(where).then((res) => {
        let data = res.data.list
        let aoaData = [
          ['付款时间', '付款金额', '支付方式', '备注', '业务类型', '客户名称', '合同名称', '合同编号', '业务员']
        ]
        if (data.length > 0) {
          data.forEach((value) => {
            if (value.types == 0) {
              value.types = '回款记录'
            } else {
              if (value.renew) {
                value.types = '续费记录' + '-' + value.renew
              } else {
                value.types = '续费记录'
              }
            }

            aoaData.push([
              value.date,
              value.num,
              value.pay_type,
              value.mark,
              value.types,
              value.client ? value.client.name : '',
              value.treaty ? value.treaty.title : '',
              value.treaty ? value.treaty.contract_no : '',
              value.card ? value.card.name : ''
            ])
          })

          this.exportData.data = aoaData
          this.$refs.exportExcel.exportExcel()
        }
      })

      this.where.page = 1
      this.where.limit = 10
      this.getTableData()
    },

    submitFn() {
      let data = {
        fail_msg: this.form.remarks,
        status: 2
      }
      this.$refs.from.validate((valid) => {
        if (valid) {
          clientBillStatusApi(this.paymentId, data).then((res) => {
            this.getTableData()
            this.dialogVisible = false
            this.form.remarks = ''
          })
        }
      })
    },
    async getbillCate(id) {
      getbillCate(id).then((res) => {
        this.catePath = res.data.bill_cate_path
      })
    },

    async getOption() {
      billCateApi().then((res) => {
        this.statusOptions = res.data
      })
    },

    // 通过
    handleContract(row, type) {
      this.catePath = []
      this.getbillCate(row.cid)
      let str = '审核通过'
      if (type === 'edit') {
        str = '编辑账目记录'
      }
      setTimeout(() => {
        this.operationDialog = {
          title: str,
          data: row,
          path: this.catePath,
          type,
          source: 'fd'
        }
      }, 200)
      if (row.types !== 2) {
        this.$refs.operationDialog.handleOpen()
      } else {
        this.$refs.expendDialog.handleOpen()
      }
    },

    cancelFn() {
      this.dialogVisible = false
      this.form.remarks = ''
    },

    handlePictureCardPreview(val) {
      this.srcList.push(val)
      this.isImage = true
    },

    closeImageViewer() {
      this.isImage = false
      this.srcList = []
    },
    getStatus(id) {
      var str = ''
      if (id === 0) {
        str = this.$t('customer.audit')
      } else if (id === 1) {
        str = this.$t('customer.passed')
      } else {
        str = this.$t('customer.fail')
      }
      return str
    },
    // 获取表格数据
    async getTableData() {
      const result = await clientBillListApi(this.where)
      this.tableData = result.data.list
      this.total = result.data.count
    },

    // 删除
    async handleDelete(item) {
      await this.$modalSure(this.$t('finance.message5'))
      await billDelFinanceApi(item.id)
      this.getTableData()
    },

    // 编辑
    async handleEdit(item) {
      this.config = {
        title: this.$t('customer.fundaudit'),
        width: '480px',
        type: 1,
        data: item
      }
      this.$refs.examineDialog.handleOpen()
    },
    confirmData(data) {
      this.where.status = this.activeName == 1 ? 0 : data.status
      this.where.types = data.type
      this.where.date = data.date
      this.where.field_key = data.select
      this.where.name = data.name
      this.where.time_field = data.time_field
      this.where.time = data.time
      this.where.page = 1
      this.getTableData()
    },
    isOk() {
      this.getTableData()
    }
  }
}
</script>

<style lang="scss" scoped>
.btn-type {
  padding: 4px 10px;
  font-size: 13px;
}
.invoice-info {
  color: #909399;
}
.invoice-info > div > span {
  color: #606266;
}
.line {
  width: 100%;
  height: 4px;
  border-bottom: 1px solid #f2f6fc;
  margin-bottom: 30px;
}
.img {
  width: 40px;
  height: 40px;
}
.head-box {
  display: flex;
  align-items: center;
  .input {
    width: 240px;
    margin: 0 20px 0 10px;
  }
}
.detail-box {
  padding: 20px;
  color: #333;
  .item-box {
    display: flex;
    margin-bottom: 20px;
    font-size: 14px;
    span {
      /*width: 80px;*/
    }
    div {
      margin-left: 20px;
    }
  }
  .content-box {
    span {
      font-size: 14px;
    }
  }
}
::v-deep .el-drawer__body {
  height: 100%;
  overflow-y: auto;
}
.footer {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
  margin-bottom: 20px;
}
.from {
  margin: 0 24px;
}

.dialog {
  ::v-deep .el-dialog {
    border-radius: 6px;
    height: 249px;
  }
  ::v-deep .el-dialog__body {
    padding: 0;
  }
  ::v-deep .el-textarea__inner {
    width: 400px;
    height: 90px;
    font-size: 13px;
  }
}
</style>

<style lang="scss">
.content-box {
  font-size: 13px;
}

.table-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 60px;
  height: 22px;
  border-radius: 3px;
  font-size: 13px;
  cursor: pointer;

  &.blue {
    background: rgba(24, 144, 255, 0.05);
    border: 1px solid #1890ff;
    color: #1890ff;
  }

  &.yellow {
    background: rgba(255, 153, 0, 0.05);
    border: 1px solid #ff9900;
    color: #ff9900;
  }
  &.red {
    background: rgba(255, 153, 0, 0.05);
    border: 1px solid #ed4014;
    color: #ed4014;
  }

  &.green {
    background: rgba(0, 192, 80, 0.05);
    border: 1px solid #00c050;
    color: #00c050;
  }

  &.gray {
    background: rgba(153, 153, 153, 0.05);
    border: 1px solid #999999;
    color: #999999;
  }
}
</style>
