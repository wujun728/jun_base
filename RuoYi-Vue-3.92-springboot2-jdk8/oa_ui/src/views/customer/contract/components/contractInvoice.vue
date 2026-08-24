<!-- 客户列表/合同管理：查看-发票页面  contractInvoice：来区分合同管理还是客户管理 -->
<template>
  <div class="station-invoice">
    <div class="btn-box1">
      <div class="title-16">发票列表</div>
      <el-button
        v-if="!formInfo.types || formInfo.types == 2 || (formInfo.types == 1 && userId == formInfo.data.salesman.id)"
        type="primary"
        size="small"
        @click="handleBuild(0, buildData.invoicing_switch, 'invoicing_switch')"
      >
        申请开票
      </el-button>
    </div>
    <div class="amount mt10">
      <div>
        <span class="amount-label">累计开票金额(元)：</span>
        <span class="amount-val">{{ cumulative_invoiced_price }}</span>
      </div>
      <div>
        <span class="amount-label ml36">审核中开票金额(元)：</span>
        <span class="amount-val">{{ audit_invoiced_price }}</span>
      </div>
      <div>
        <span class="amount-label ml36">累计收款金额(元)：</span>
        <span class="amount-val"> {{ cumulative_payment_price }}</span>
      </div>
    </div>
    <el-table :data="tableData" style="width: 100%; height: 100%">
      <el-table-column prop="created_at" label="申请时间" sortable min-width="150"> </el-table-column>
      <el-table-column prop="title" label="发票抬头" min-width="150"> </el-table-column>
      <el-table-column prop="amount" :label="$t('customer.invoicingpay')" min-width="100">
        <template slot-scope="scope">
          <span :class="scope.row.price == scope.row.amount ? '' : 'active'">{{ scope.row.amount }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="price" label="付款金额(元)" min-width="100">
        <template slot-scope="scope">
          <span :class="scope.row.price == scope.row.amount ? '' : 'active'">{{ scope.row.price }}</span>
        </template>
      </el-table-column>

      <el-table-column prop="status" label="发票审核状态" min-width="110">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status == -1 ? 'danger' : scope.row.status == -1 ? 'warning' : ''">
            {{ getInvoiceStatus(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>

      <!-- 操作 -->
      <el-table-column prop="address" min-width="190" :label="$t('public.operation')">
        <template slot-scope="scope">
          <el-button @click="handleCheck(scope.row)" type="text">{{ $t('public.check') }}</el-button>

          <el-button @click="setRemarks(scope.row)" type="text">备注</el-button>

          <el-button v-if="scope.row.status === 0" @click="invoiceWithdrawal(scope.row)" type="text">
            发票撤回
          </el-button>
          <el-button
            v-if="scope.row.status === 5"
            @click="handleBuild(scope.row, buildData.void_invoice_switch, 'void_invoice_switch')"
            type="text"
          >
            申请作废
          </el-button>
          <el-button v-if="scope.row.status === 4" @click="invoiceWithdrawal(scope.row, 1)" type="text"
            >作废撤回</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <el-row>
      <el-col :span="2"> </el-col>
      <el-col :span="22">
        <div class="block mt10 text-right">
          <el-pagination
            :page-size="where.limit"
            :current-page="where.page"
            layout="total, prev, pager, next, jumper"
            :total="total"
            @current-change="pageChange"
          />
        </div>
      </el-col>
    </el-row>

    <!-- 发票撤回/申请弹窗 -->
    <el-dialog
      :title="title"
      top="25vh"
      class="addBox"
      :append-to-body="true"
      :visible.sync="dialogVisible"
      width="540px"
      :close-on-click-modal="false"
    >
      <div class="line" />
      <el-form :model="form" :rules="rules" class="from">
        <el-form-item :label="reason + '：'" label-width="90px" prop="remarks">
          <el-input type="textarea" v-model="form.remarks" placeholder="请填写原因"></el-input>
        </el-form-item>
        <div class="footer">
          <el-button size="small" class="btn" @click="cancelFn">取消</el-button>
          <el-button size="small" type="primary" @click="submitFn" class="btn">确定</el-button>
        </div>
      </el-form>
    </el-dialog>
    <!-- 直接申请发票  -->
    <invoice-apply
      ref="invoiceApply"
      :contractInvoice="contractInvoice"
      :form-data="formBoxConfig"
      @isOk="isOk"
    ></invoice-apply>
    <!-- 备注 -->
    <mark-dialog ref="markDialog" :config="configMark" @isMark="getTableData()"></mark-dialog>
    <!-- 查看发票 -->
    <invoice-details :form-data="invoiceData" ref="invoiceDetailsView"></invoice-details>
    <invoice-view :form-data="invoiceData" ref="invoiceView"></invoice-view>
    <!-- 选择付款单后开票 -->
    <mergeInvoice
      ref="mergeInvoice"
      :isApproval="is_approval"
      :contractInvoice="contractInvoice"
      :form-data="formBoxConfig"
      :parameterData="parameterData"
      @isOk="isOk"
      @getTableData="getTableData"
    ></mergeInvoice>
    <!--审批开票 -->

    <edit-examine ref="editExamine" :parameterData="parameterData" @isOk="getTableData()"></edit-examine>
  </div>
</template>

<script>
import { getInvoiceText, getInvoiceClassName } from '@/libs/customer'
import { approveApplyRevokeApi } from '@/api/business'
import {
  clientInvoiceDeleteApi,
  clientInvoiceListApi,
  accumulatedAmountApi,
  recallStatus,
  invalidApply,
  uninvoicedListApi
} from '@/api/enterprise'
import { configRuleApproveApi } from '@/api/config'
export default {
  name: 'ContractInvoice',
  props: {
    formInfo: {
      type: Object,
      default: () => {
        return {}
      }
    },
    contractInvoice: {
      type: String,
      default: ''
    }
  },
  components: {
    invoiceApply: () => import('@/components/invoice/invoiceApply'),
    markDialog: () => import('@/views/customer/contract/components/markDialog'),
    invoiceDetails: () => import('@/components/invoice/invoiceDetails'),
    invoiceView: () => import('@/views/customer/invoice/components/invoiceView'),
    mergeInvoice: () => import('@/components/invoice/mergeInvoice'),
    editExamine: () => import('@/views/user/examine/components/editExamine')
  },
  data() {
    return {
      parameterData: {
        contract_id: '',
        customer_id: '',
        invoice_id: '',
        bill_id: ''
      },
      userId: JSON.parse(localStorage.getItem('userInfo')).id,
      buildData: [], // 申请开票审批类型
      voidData: [], // 申请作废类型
      drawer: false,
      is_approval: false, // 判断是否开启审批流程
      mergeOpen: false, // 判断是否有付款单
      dialogVisible: false,
      cumulative_invoiced_price: '',
      audit_invoiced_price: '',
      cumulative_payment_price: '',
      direction: 'rtl',
      title: '',
      reason: '',
      form: {
        remarks: ''
      },
      rules: {
        remarks: [{ required: true, message: '请填写撤回理由', trigger: 'blur' }]
      },
      tableData: [],
      where: {
        page: 1,
        limit: 15,
        types: ''
      },
      withdrawId: '',
      total: 0,
      formBoxConfig: {},
      configMark: {},
      isShow: true,
      invoiceData: {},
      commandId: 0 // 当前审批内容
    }
  },
  watch: {
    formInfo: {
      handler(nVal) {
        this.isShow = nVal.isClient === false
        this.getConfigApprove()
        // this.getVoidData()
      },
      immediate: true,
      deep: true
    }
  },
  created() {
    this.getConfigApprove()
  },
  methods: {
    // V1.4获取开票审批类型
    async getConfigApprove() {
      const result = await configRuleApproveApi(0)
      this.buildData = result.data
      this.is_approval = this.buildData.invoicing_switch != 0 ? true : false
    },

    getTableData() {
      this.accumulatedAmount()
      this.where.cid = this.formInfo.data.cid
      this.parameterData.contract_id = this.formInfo.data.cid
      this.where.eid = this.formInfo.data.eid
      this.parameterData.customer_id = this.formInfo.data.eid

      clientInvoiceListApi(this.where).then((res) => {
        this.tableData = res.data.list
        this.total = res.data.count
      })
    },

    getAbnormal(row) {
      const isStatus = {
        0: row.mark || '暂无',
        2: row.mark || '暂无',
        3: row.card_remark || '暂无',
        4: row.card_remark || '暂无',
        5: row.finance_remark || '暂无',
        6: row.remark || '暂无'
      }
      return isStatus[row.status] || row.remark
    },
    accumulatedAmount() {
      let data = {
        eid: this.formInfo.data.eid,
        cid: this.formInfo.data.cid
      }

      accumulatedAmountApi(data).then((res) => {
        this.cumulative_invoiced_price = res.data.cumulative_invoiced_price
        this.audit_invoiced_price = res.data.audit_invoiced_price
        this.cumulative_payment_price = res.data.cumulative_payment_price
      })
    },
    pageChange(val) {
      this.where.page = val
      this.getTableData()
    },
    getInvoiceStatus(status) {
      return getInvoiceText(status)
    },
    getInvoiceColor(status) {
      return getInvoiceClassName(status)
    },
    isOk() {
      setTimeout(() => {
        this.getTableData()
      }, 300)
      this.$emit('handleInvoice')
    },
    // 发票撤回
    async invoiceWithdrawal(val, type) {
      let id = type == 1 ? val.revoke_id : val.link_id
      await this.$modalSure('确认撤回该发票吗')
      await approveApplyRevokeApi(id)
      this.getTableData()
    },
    async getApplyRevoke(id) {
      await approveApplyRevokeApi(id)
      this.getTableData()
    },
    // 作废撤回
    withdraw(val) {
      this.$modalSure(this.$t('customer.message08')).then(() => {
        let data = {
          invalid: -1
        }
        invalidApply(val.id, data)
          .then((res) => {
            this.getTableData()
          })
          .catch((error) => {})
      })
    },
    // 申请作废
    apply(val) {
      this.title = '发票申请作废'
      this.reason = '作废原因'
      this.dialogVisible = true
      this.withdrawId = val.id
    },

    submitFn() {
      let data = {
        remark: this.form.remarks
      }
      if (this.title == '发票申请作废') {
        data.invalid = 1
        invalidApply(this.withdrawId, data).then((res) => {
          this.cancelFn()
          this.getTableData()
        })
      } else {
        recallStatus(this.withdrawId, data).then((res) => {
          this.cancelFn()
          this.getTableData()
        })
      }
    },

    cancelFn() {
      this.dialogVisible = false
      this.form.remarks = ''
    },
    mergeOpenFn(val, type) {
      let data = null
      return new Promise((resolve, reject) => {
        if (type == '') {
          data = {
            eid: this.contractInvoice == 'is_contract' ? this.formInfo.data.eid : this.formInfo.data.id
          }
        } else {
          data = {
            eid: val.eid,
            invoice_id: val.id,
            withdraw: ''
          }
        }

        uninvoicedListApi(data).then((res) => {
          if (JSON.stringify(res.data) == '[]') {
            this.mergeOpen = false
          } else {
            this.mergeOpen = true
          }
          resolve(this.mergeOpen)
        })
      })
    },
    // V1.4获取申请开票表单
    handleBuild(command, val, type) {
      if (type === 'void_invoice_switch') {
        this.parameterData.invoice_id = command.id
        this.$refs.editExamine.openBox(val, command, type)
      } else {
        this.opneApply(1, '', val)
      }
    },

    // 申请开票
    async opneApply(val, type, id) {
      if (type == '') {
        this.mergeOpenFn(val, type).then(() => {
          this.formBoxConfig = {
            title: '申请开票',
            width: '1000px',
            edit: false,
            data: this.formInfo.data,
            type
          }
          if (this.mergeOpen) {
            this.$refs.mergeInvoice.openBox(id)
          } else {
            if (id > 0) {
              this.parameterData.contract_id = this.formInfo.data.cid
              this.parameterData.customer_id = this.formInfo.data.eid
              this.parameterData.invoice_id = ''
              this.$refs.editExamine.openBox(id)
            } else {
              this.$refs.invoiceApply.openBox()
            }
          }
        })
      } else {
        this.mergeOpenFn(val, type).then(() => {
          if (this.mergeOpen) {
            this.formBoxConfig = {
              title: '编辑发票申请',
              width: '1000px',
              edit: true,
              rowData: val,
              data: this.formInfo.data,
              type
            }
            this.$refs.mergeInvoice.openBox()
          } else {
            this.formBoxConfig = {
              title: '编辑发票申请',
              width: '1000px',
              edit: true,
              data: this.formInfo.data,
              rowData: val
            }
            if (this.buildData.length > 0) {
              this.$refs.editExamine.openBox(this.commandId)
            } else {
              this.$refs.invoiceApply.openBox(val.id)
            }
          }
        })
      }
    },
    handleEdit(row) {
      this.formBoxConfig = {
        title: this.$t('customer.invoiceedit'),
        width: '820px',
        edit: true,
        data: row
      }
      this.$refs.invoiceApply.openBox()
    },
    handleCheck(row) {
      this.invoiceData = {
        title: '发票查看',
        width: '1000px',
        data: row
      }
      if (row.link_id == 0) {
        this.$refs.invoiceView.openBox(row.link_id)
      } else {
        this.$refs.invoiceDetailsView.openBox(row.link_id)
      }
    },
    setRemarks(row) {
      this.configMark = {
        title: this.$t('customer.remarkinformation'),
        width: '480px',
        id: row.id,
        type: 2,
        mark: row.mark
      }
      this.$refs.markDialog.handleOpen()
    },
    // 删除
    handleDelete(item) {
      this.$modalSure(this.$t('customer.placeholder23')).then(() => {
        clientInvoiceDeleteApi(item.id).then((res) => {
          if (this.where.page > 1 && this.tableData.length <= 1) {
            this.where.page--
          }
          this.isOk()
        })
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.line {
  width: 100%;
  height: 4px;
  border-bottom: 1px solid #f2f5fc;
  margin-bottom: 30px;
}
.from {
  margin: 0 24px;
}
.addBox ::v-deep .el-dialog__body {
  padding: 0;
}
.ml36 {
  margin-left: 36px;
}
::v-deep .el-dialog__header .el-dialog__title {
  font-size: 14px;
  color: #303133;
}
.footer {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
  margin-bottom: 20px;
}
.invoice-info {
  color: #909399;
}
.invoice-info > div > span {
  color: #606266;
}

.active {
  color: red;
}
.amount {
  display: flex;
  margin-bottom: 14px;
  font-size: 13px;
  .amount-label {
    color: #909399;
  }
  .amount-val {
    color: #303133;
  }
}
.addBox ::v-deep .el-dialog {
  border-radius: 6px;
  height: 249px;
}
::v-deep .el-table {
  height: 100% !important;
}
::v-deep .el-input__inner {
  text-align: left;
}
.addBox ::v-deep .el-textarea__inner {
  width: 400px;
  height: 90px;
  font-size: 13px;
}

.from-item-title {
  border-left: 5px solid #1890ff;
  span {
    padding-left: 10px;
    font-weight: bold;
    font-size: 14px;
  }
}
.btn-box1 {
  height: 32px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.table-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 70px;
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
