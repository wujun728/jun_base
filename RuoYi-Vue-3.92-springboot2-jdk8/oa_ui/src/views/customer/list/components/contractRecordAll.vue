<!-- 客户-账目记录页面组件 -->
<template>
  <div class="station">
    <div class="btn-box1">
      <div class="title-16">账目记录列表</div>

      <div v-if="showAddButton">
        <el-button
          v-if="buildData.contract_renew_switch != 0"
          size="small"
          @click="handleBuild(0, buildData.contract_renew_switch, 'contract_renew_switch')"
        >
          添加续费
        </el-button>

        <el-button
          v-if="buildData.contract_disburse_switch != 0"
          size="small"
          @click="handleBuild(0, buildData.contract_disburse_switch, 'contract_disburse_switch')"
        >
          添加支出
        </el-button>

        <el-button
          v-if="buildData.contract_refund_switch != 0"
          size="small"
          type="primary"
          @click="handleBuild(0, buildData.contract_refund_switch, 'contract_refund_switch')"
        >
          添加回款
        </el-button>
      </div>
    </div>

    <el-table class="mt10" :data="debtData" :class="debtData.length > 0 ? '' : 'mb15'" style="width: 100%">
      <el-table-column prop="date" label="付款时间" min-width="150"> </el-table-column>
      <el-table-column prop="bill_types" label="记录类型" min-width="150">
        <template slot-scope="scope">
          <el-tag :type="scope.row.types == 2 ? 'warning' : 'success'">{{
            scope.row.types == 2 ? '支出' : '收入'
          }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="num" label="付款金额(元)" min-width="100"> </el-table-column>
      <el-table-column prop="pay_type" label="支付方式" min-width="90">
        <template slot-scope="scope">
          <span>{{ scope.row.pay_type !== '' ? scope.row.pay_type : '--' }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="bill_no" label="付款单号" min-width="110"> </el-table-column>
      <el-table-column prop="status" label="付款审核状态" min-width="110">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 0" type="warning" size="mini"> {{ $t('customer.audit') }}</el-tag>
          <el-tag v-if="scope.row.status === 1 && !scope.row.recall" type="info" size="mini">
            {{ $t('customer.passed') }}</el-tag
          >
          <el-tag v-if="scope.row.status === -1" type="info" size="mini">已撤销</el-tag>
          <el-tag v-if="scope.row.status === 1 && scope.row.recall" type="info" size="mini">撤销中</el-tag>
          <el-popover v-if="scope.row.status === 2" trigger="hover" placement="top">
            <p>{{ $t('customer.reason') }}:</p>
            <p>{{ scope.row.fail_msg }}</p>
            <div slot="reference">
              <el-tag type="danger" size="mini"> {{ $t('customer.fail') }}</el-tag>
            </div>
          </el-popover>
        </template>
      </el-table-column>

      <el-table-column prop="address" min-width="100" :label="$t('public.operation')">
        <template slot-scope="scope">
          <div class="flex">
            <el-button type="text" @click="handleCheck(scope.row)">查看</el-button>
            <template v-if="userId == scope.row.card.id">
              <el-button
                type="text"
                @click="withdraw(scope.row)"
                v-if="
                  (scope.row.status === 1 &&
                    scope.row.approve_rule &&
                    scope.row.approve_rule.recall == 1 &&
                    !scope.row.recall) ||
                  scope.row.status === 0
                "
                >撤销</el-button
              >
              <el-button type="text" v-if="scope.row.status == -1" @click="handleDelete(scope.row)">删除</el-button>
            </template>
          </div>
        </template>
      </el-table-column>
    </el-table>
    <div class="mt10 flex-between">
      <div class="amount mt10">
        <div v-if="paymentPrice.payment_price != '0.00'" class="mr36">
          <span class="amount-label">累计收款金额(元)：</span
          ><span class="amount-val incomeColor">{{ paymentPrice.payment_price }}</span>
        </div>
        <div v-if="paymentPrice.expense_price != '0.00'" class="mr36">
          <span class="amount-label">累计支出金额(元)：</span
          ><span class="expendColor">{{ paymentPrice.expense_price }}</span>
        </div>
        <div v-if="paymentPrice.audit_income_price != '0.00' || paymentPrice.audit_expense_price != '0.00'">
          <span class="amount-label">审核中金额(元)：</span>
          <span class="amount-val">
            <span class="incomeColor"> {{ paymentPrice.audit_income_price }}(收入) </span>

            <span class="expendColor ml10"> {{ paymentPrice.audit_expense_price }}(支出)</span>
          </span>
        </div>
      </div>
      <div>
        <el-pagination
          :page-size="where.limit"
          :current-page="where.page"
          layout="total, prev, pager, next, jumper"
          :total="total"
          @current-change="renewChange"
        />
      </div>
    </div>

    <applyForPayment ref="applyForPayment" :form-data="fromData"></applyForPayment>
    <edit-examine ref="editExamine" :parameterData="parameterData" @isOk="getTableData()"></edit-examine>
    <detail-examine ref="detailExamine" @getList="getTableData" />
    <!-- 撤销 -->
    <oa-dialog
      ref="oaDialog"
      :fromData="oaFromData"
      :formConfig="formConfig"
      :formRules="formRules"
      :formDataInit="formDataInit"
      @submit="getApplyRevoke"
    ></oa-dialog>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { clientBillAllListApi, clientBillDeleteApi, getCustomerStatisticsApi } from '@/api/enterprise'
import { approveApplyRevokeApi } from '@/api/business'
import { configRuleApproveApi } from '@/api/config'
export default {
  name: 'ContractRecord',
  props: {
    formInfo: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  components: {
    oaDialog: () => import('@/components/form-common/dialog-form'),
    applyForPayment: () => import('./applyForPayment'),
    editExamine: () => import('@/views/user/examine/components/editExamine'),
    detailExamine: () => import('@/views/user/examine/components/detailExamine')
  },
  computed: {
    ...mapGetters(['userInfo'])
  },
  data() {
    return {
      debtData: [],
      parameterData: {
        contract_id: '',
        customer_id: '',
        invoice_id: '',
        bill_id: ''
      },
      formDataInit: {
        info: ''
      },
      userId: JSON.parse(localStorage.getItem('userInfo')).id,
      formConfig: [
        {
          type: 'textarea',
          label: '撤销理由：',
          placeholder: '请输入撤销理由',
          key: 'info'
        }
      ],
      formRules: {
        info: [{ required: true, message: '请输入撤销理由', trigger: 'blur' }]
      },
      oaFromData: {
        width: '600px',
        title: '撤销',
        btnText: '确定',
        labelWidth: 'auto',
        type: ''
      },
      rowData: {},
      where: {
        page: 1,
        limit: 15,
        types: '',
        eid: ''
      },
      userId: this.$store.state.user.userInfo.id,
      fromData: {},
      total: 0,
      type: 0,
      paymentPrice: {},
      buildData: []
    }
  },
  computed: {
    showAddButton() {
      return this.formInfo.types == 2 || (this.formInfo.types == 1 && this.userId == this.formInfo.data.salesman.id)
    } 
  },
  mounted() {
    this.getConfigApprove()
  },
  methods: {
    async getTableData() {
      this.getCumulative()
      this.where.eid = this.formInfo.data.eid
      const result = await clientBillAllListApi(this.where)
      this.debtData = result.data.list
      this.total = result.data.count
    },
    async getConfigApprove() {
      const result = await configRuleApproveApi(0)
      this.buildData = result.data
    },
    handleBuild(command, val, type) {
      const item = this.debtData[command]
      this.parameterData.customer_id = this.formInfo.data.eid
      this.$refs.editExamine.openBox(val, this.formInfo.data.eid, type)
    },
    async getCumulative() {
      const result = await getCustomerStatisticsApi(this.formInfo.data.eid)
      this.paymentPrice = result.data
    },
    renewChange(val) {
      this.where.page = val
      this.getTableData()
    },

    // 查看
    async handleCheck(item) {
      this.fromData = {
        title: this.$t('customer.viewcustomer'),
        width: '500px',
        data: item,
        isClient: false,
        name: this.formInfo.data.name,
        id: item.eid,
        edit: true,
        type: 1
      }
      if (item.apply_id) {
        item.id = item.apply_id
        this.$refs.detailExamine.openBox(item)
      } else {
        this.$refs.applyForPayment.openBox()
      }
    },
    // 撤回
    withdraw(row) {
      this.rowData = row
      if (row.status === 0) {
        this.$modalSure(this.$t('你确定要撤销申请吗')).then(() => {
          this.getApplyRevoke()
        })
      } else {
        this.$refs.oaDialog.openBox()
      }
    },
    async getApplyRevoke(data) {
      await approveApplyRevokeApi(this.rowData.apply_id, data)
      if (data) {
        this.$refs.oaDialog.handleClose()
      }
      this.getTableData()
    },

    // 删除
    handleDelete(item, type) {
      this.$modalSure('确定要删除此账目记录').then(() => {
        clientBillDeleteApi(item.id).then((res) => {
          if (type === 1) {
            if (this.where.page > 1 && this.debtData.length <= 1) {
              this.where.page--
              this.where.types = 0
            }
          } else {
            if (this.where.page > 1 && this.renewData.length <= 1) {
              this.where.page--
              this.where.types = 1
            }
          }

          this.getTableData()
        })
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.invoice-info {
  color: #909399;
}

.incomeColor {
  color: #19be6b;
}
.expendColor {
  color: #ff9900;
}

.invoice-info > div > span {
  color: #606266;
}
.mr36 {
  margin-right: 36px;
}
.amount {
  display: flex;
  margin-bottom: 10px;
  font-size: 13px;
  .amount-label {
    color: #909399;
  }
}
.hand {
  cursor: pointer;
}
.flex {
  display: flex;
  align-items: center;
}
.img {
  width: 40px;
  height: 40px;
}

.renewal-content {
  width: 100%;
  margin-bottom: 10px;
  p {
    margin: 12px 20px 0 0;
    padding: 0;
    font-size: 13px;
    display: inline-block;
    &:last-of-type {
      margin-right: 0;
    }
  }
}
.from-item-title {
  margin-top: 8px;
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
  width: 60px;
  height: 22px;
  border-radius: 3px;
  font-size: 13px;
  cursor: pointer;

  &.blue {
    background: rgba(24, 144, 255, 0.05);
    color: #1890ff;
  }

  &.yellow {
    background: rgba(255, 153, 0, 0.05);
    color: #ff9900;
  }
  &.red {
    background: rgba(255, 153, 0, 0.05);
    color: #ed4014;
  }

  &.green {
    background: rgba(0, 192, 80, 0.05);
    color: #00c050;
  }

  &.gray {
    background: rgba(153, 153, 153, 0.05);
    color: #999999;
  }
}
.build-dropdown {
  max-height: 300px;
  overflow: auto;
  overflow-x: hidden;
}
.build-dropdown::-webkit-scrollbar {
  /*width: 0;宽度为0隐藏*/
  width: 8px;
  height: 4px;
}
.build-dropdown::-webkit-scrollbar-thumb {
  border-radius: 5px;
  height: 8px;
  background: rgba(0, 0, 0, 0.2); //滚动条颜色
}
.build-dropdown::-webkit-scrollbar-track {
  box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
  border-radius: 5px;
  background: #eee; //滚动条背景色
}
</style>
