<!-- 合同-关联付款单的申请发票页面 -->
<template>
  <div>
    <div class="station">
      <el-drawer
        :append-to-body="true"
        :before-close="handleClose"
        :direction="direction"
        :modal="true"
        :size="formData.width"
        :title="formData.title"
        :visible.sync="drawer"
        :wrapper-closable="true"
      >
        <!-- 步骤条 -->
        <div class="step">
          <span :class="activeIndex == 1 ? 'active' : ''" class="public">1</span>
          <span :class="activeIndex == 1 ? 'activeText' : ''" class="step-text">选择付款订单</span>
          <span class="line-title" />
          <span :class="activeIndex == 2 ? 'active' : ''" class="public">2</span>
          <span :class="activeIndex == 2 ? 'activeText' : ''" class="step-text">填写发票信息</span>
        </div>

        <!-- 未选择付款订单提示 -->
        <div v-if="activeIndex == 2 && rules.bill_id.length == 0" class="alert">
          <el-alert class="cr-alert" description="请注意,您没有选择该发票关联的付款订单！" show-icon type="info">
          </el-alert>
        </div>
        <!-- 表格 -->
        <div v-if="activeIndex == 1" class="mt20">
          <el-button v-if="formData.type == 'edit'" class="mb14" size="small" type="primary" @click="addPaymentFn"
            >添加其他付款单</el-button
          >
          <paymentTable
            ref="paymentTable"
            :edit="edit"
            :tableData="tableData1"
            @handleSelectionFn="handleSelectionChange"
            @totalFn="totalFn"
          />
        </div>

        <!-- 基本信息 -->
        <div v-if="activeIndex == 2" class="mt20">
          <div class="mb50">
            <approval-edit ref="approval" :commandId="commandId" :parameterData="parameterData"></approval-edit>
          </div>
        </div>
        <div class="button from-foot-btn fix btn-shadow">
          <el-button size="small" @click="handleClose">{{ activeIndex == 1 ? '取消' : '上一步' }}</el-button>
          <el-button size="small" type="primary" @click="handleConfirm()">
            {{ activeIndex == 1 ? '下一步' : '确定' }}
          </el-button>
        </div>
      </el-drawer>

      <el-dialog
        :append-to-body="true"
        :visible.sync="addPayment"
        title="选择申请开票付款单"
        width="1100px"
        @close="cancelFn"
      >
        <div class="line" />
        <paymentTable :edit="edit" :ids="rules.bill_id" :tableData="tableData2" @handleSelectionFn="pushTableFn" />
        <div class="footer">
          <el-button class="btn" size="small" @click="cancelFn">取消</el-button>
          <el-button class="btn" size="small" type="primary" @click="okFn">确定</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import paymentTable from './paymentTable'

import { clientInvoiceSaveApi, uninvoicedListApi, clientInvoicePutApi } from '@/api/enterprise'
import helper from '@/libs/helper'
export default {
  name: 'MergeInvoice',
  props: {
    contractInvoice: {
      type: String,
      default: ''
    },
    formData: {
      type: Object,
      default: () => {
        return {}
      }
    },
    parameterData: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  components: {
    paymentTable,
    approvalEdit: () => import('./approvalEdit')
  },
  data() {
    const checkIdent = (rule, value, callback) => {
      if (!value && this.rules.invoiceType > 1) {
        return callback(new Error(this.$t('customer.placeholder47')))
      } else if (!helper.identReg.test(value)) {
        return callback(new Error('税号输入不合法'))
      } else {
        return callback()
      }
    }
    return {
      radio: 1,
      edit: '',
      linkId: 0,
      commandId: 0,
      drawer: false,
      addPayment: false,
      obtainEid: '',
      obtainCid: '',
      direction: 'rtl',
      tableData1: [],
      tableData2: [],
      pushTable: [],
      categoryList: [], // 发票类目
      invoiceOptions: [
        // 发票类型
        { value: 1, label: this.$t('customer.personalinvoice') },
        { value: 2, label: this.$t('customer.enterpriseinvoice') },
        { value: 3, label: this.$t('customer.specialinvoice') }
      ],
      methodOptions: [
        { value: 'express', label: this.$t('customer.express') },
        { value: 'mail', label: this.$t('customer.mail') }
      ],
      activeIndex: 1,
      withdraw: '',
      rules: {
        name: '',
        source: '',
        price: '0',
        amount: undefined,
        billDate: '',
        invoiceType: 2,
        title: '',
        ident: '',
        collect_type: 'mail',
        bank: '',
        address: '',
        account: '',
        tel: '',
        mark: '',
        collect_name: '',
        collect_tel: '',
        mail_address: '',
        collect_email: '',
        category_id: '',
        bill_id: []
      },
      newFormData: {},
      rule: {
        name: [{ required: true, message: this.$t('customer.placeholder42'), trigger: 'blur' }],
        amount: [{ required: true, message: this.$t('customer.placeholder44'), trigger: 'blur' }],
        billDate: [{ required: true, message: this.$t('customer.placeholder65'), trigger: 'blur' }],
        title: [{ required: true, message: this.$t('customer.placeholder46'), trigger: 'blur' }],
        ident: [{ required: true, validator: checkIdent, trigger: 'blur' }],
        category_id: [{ required: true, message: '请选择发票类目', trigger: 'change' }],
        collect_name: [{ required: true, message: '电话号码不能为空', trigger: 'blur' }],
        collect_tel: [{ required: true, message: '电话号码不能为空', trigger: 'blur' }],
        collect_email: [{ required: true, message: '邮箱地址不能为空', trigger: 'blur' }],
        mail_address: [{ required: true, message: '邮寄地址不能为空', trigger: 'blur' }],
        address: [{ required: true, message: this.$t('customer.placeholder56'), trigger: 'blur' }],
        bank: [{ required: true, message: this.$t('customer.placeholder48'), trigger: 'blur' }],
        account: [{ required: true, message: this.$t('customer.placeholder49'), trigger: 'blur' }],
        tel: [{ required: true, message: '电话号码不能为空', trigger: 'blur' }]
      }
    }
  },

  watch: {
    formData: {
      handler(nVal) {
        this.newFormData = nVal
        this.edit = nVal.type

        if (nVal.type == 'edit') {
          for (let key in this.rules) {
            this.rules[key] = nVal.rowData[key]
          }
          this.rules.source = nVal.rowData.client ? nVal.rowData.client.name : ''

          if (nVal.rowData.account == '') {
            this.rules.account = this.rules.price
          } else {
            this.rules.account = nVal.rowData.account
          }
          this.rules.invoiceType = Number(nVal.rowData.types)
        } else {
          if (nVal.data) {
            this.rules.source = nVal.data.name ? nVal.data.name : '--'
          }
          if (nVal.oneData) {
            for (let key in this.rules) {
              this.rules[key] = nVal.oneData[key]
            }
            this.rules.invoiceType = Number(nVal.oneData.types)
          }
        }
      },
      immediate: true,
      deep: true
    }
  },

  methods: {
    openBox(commandId) {
      this.commandId = commandId
      setTimeout(() => {
        if (this.edit == 'edit') {
          this.editPaymentRecord()
          this.$nextTick(() => {
            this.$refs.paymentTable.edit = 'edit'
          })
        } else {
          this.getPaymentRecord()
        }
        this.rules.billDate = this.$moment(new Date()).format('YYYY-MM-DD')
      }, 300)
      this.drawer = true
    },
    cancelFn() {
      this.edit = 'edit'
      this.addPayment = false
    },
    okFn() {
      this.rules.bill_id = []
      let totalAmount = []
      let list = JSON.parse(localStorage.getItem('paymentOrderList'))
      this.pushTable.forEach((item) => {
        list.push(item)
        this.addPayment = false
        this.tableData1 = this.unique(list)
        this.edit = 'edit'
      })
      localStorage.setItem('paymentOrderList', JSON.stringify(this.tableData1))
      this.tableData1.map((item) => {
        this.rules.bill_id.push(item.id)
        totalAmount.push(item.num)
      })
      let newtol = totalAmount.map(Number)
      this.rules.price = newtol.reduce((x, y) => x + y).toFixed(2)
      this.rules.amount = this.rules.price
    },

    // 去重方法
    unique(arr) {
      const res = new Map()
      return arr.filter((arr) => !res.has(arr.id) && res.set(arr.id, 1))
    },
    // 多选push表格
    pushTableFn(val) {
      this.pushTable = val
    },
    // 移除后付款金额总和重新计算
    totalFn(val, table) {
      localStorage.setItem('paymentOrderList', JSON.stringify(table))
      let totalAmount = []
      this.rules.bill_id = []
      if (table.length == 0) {
        this.rules.bill_id.length = 0
      } else {
        table.map((item) => {
          this.rules.bill_id.push(item.id)
          totalAmount.push(item.num)
        })
        let newtol = totalAmount.map(Number)
        this.rules.price = newtol.reduce((x, y) => x + y).toFixed(2)
        this.rules.amount = this.rules.price
      }
    },

    // 获取付款列表
    getPaymentRecord() {
      let data = {
        eid: '',
        cid: '',
        withdraw: this.withdraw
      }

      data.eid = this.newFormData.data.eid
      data.cid = this.newFormData.data.cid

      uninvoicedListApi(data).then((res) => {
        this.tableData1 = res.data
        localStorage.setItem('paymentOrderList', JSON.stringify(this.tableData1))
      })
    },

    // 获取重新提交付款列表
    editPaymentRecord() {
      let data = {
        eid: this.newFormData.rowData.eid,
        invoice_id: this.newFormData.rowData.id,
        withdraw: this.withdraw
      }
      uninvoicedListApi(data).then((res) => {
        this.tableData1 = res.data
        localStorage.setItem('paymentOrderList', JSON.stringify(this.tableData1))
        this.tableData1.map((item) => {
          this.rules.bill_id.push(item.id)
        })
      })
    },
    getPayment() {
      if (this.newFormData.rowData) {
        let data = {
          eid: this.newFormData.rowData.eid,
          withdraw: this.withdraw
        }
        uninvoicedListApi(data).then((res) => {
          this.tableData2 = res.data
        })
      }
    },

    // 添加付款订单
    addPaymentFn() {
      this.edit = ''
      this.addPayment = true
      this.withdraw = 1
      this.getPayment()
    },

    // 多选
    handleSelectionChange(val) {
      if (JSON.stringify(val) == '[]') {
        this.rules.bill_id.length = 0
      }
      this.rules.bill_id = []
      let totalAmount = []
      val.map((item) => {
        this.rules.bill_id.push(item.id)
        totalAmount.push(item.num)
      })
      this.parameterData.bill_id = this.rules.bill_id
      let newtol = totalAmount.map(Number)
      this.rules.price = newtol.reduce((x, y) => x + y).toFixed(2)
      this.rules.amount = this.rules.price
    },

    // 提交
    handleConfirm() {
      let newEid = 0
      let newCid = 0
      if (this.contractInvoice == 'is_contract') {
        newEid = this.formData.data.eid
        newCid = this.formData.data.id
      } else {
        newEid = this.formData.data.id
        newCid = this.formData.data.cid
      }
      if (this.activeIndex == 1) {
        this.activeIndex = 2
        if (JSON.stringify(this.rules.bill_id) !== '[]') {
          this.rules.bill_id = [...new Set(this.rules.bill_id)]
        }
        this.parameterData.bill_id = this.rules.bill_id
      } else {
        if (this.commandId) {
          this.$refs.approval.handleConfirm()
          setTimeout(() => {
            this.drawer = false
            this.$emit('getTableData')
          }, 2000)
        } else {
          this.$refs.tableform.validate((valid) => {
            if (valid) {
              let data = this.rules
              data.cid = this.formData.rowData ? this.formData.rowData.cid : newCid
              data.eid = this.formData.rowData ? this.formData.rowData.eid : newEid
              let objId = {
                eid: data.eid,
                category_id: this.rules.category_id
              }

              let categoryList = JSON.parse(localStorage.getItem('categoryList'))
              if (categoryList && categoryList.length !== 0) {
                var flag = categoryList.find((cur) => cur.eid == data.eid)
                if (flag) {
                  categoryList.map((item) => {
                    if (item.eid == objId.eid) {
                      item.category_id = objId.category_id
                    }
                  })
                } else {
                  categoryList.push(objId)
                }
              }

              localStorage.setItem('categoryList', JSON.stringify(categoryList))
              if (this.edit == 'edit') {
                this.invoicePut(this.newFormData.rowData.id, data)
              } else {
                this.invoiceSave(data)
              }
            }
          })
        }
      }
    },

    // 发票申请
    invoiceSave(data) {
      clientInvoiceSaveApi(data).then((res) => {
        if (res.status == 200) {
          this.drawer = false
          this.activeIndex = 1
          this.$emit('isOk')
          localStorage.removeItem('paymentOrderList')
        }
      })
    },

    // 修改发票申请
    invoicePut(id, data) {
      clientInvoicePutApi(id, data).then((res) => {
        if (res.status == 200) {
          this.drawer = false
          this.activeIndex = 1
          this.$emit('isOk')
          this.rest()
          localStorage.removeItem('paymentOrderList')
        }
      })
    },

    // 取消/上一步
    handleClose() {
      if (this.activeIndex == 1) {
        this.drawer = false
        this.rest()
      } else {
        this.activeIndex = 1
        let list = JSON.parse(localStorage.getItem('paymentOrderList'))
        this.$nextTick(() => {
          this.$set(this, 'tableData1', list)
        })

        this.getPayment()
      }
    },
    rest() {
      this.activeIndex = 1
      for (key in this.rules) {
        this.rules[key] = ''
      }
      this.rules.price = '0'
      this.rules.amount = undefined
      this.rules.invoiceType = 2
      this.rules.collect_type = 'mail'
      this.rules.bill_id = []
    }
  }
}
</script>

<style lang="scss" scoped>
.public {
  display: inline-block;
  width: 28px;
  height: 28px;
  border-radius: 50%;
  text-align: center;
  line-height: 28px;
  font-size: 16px;
  font-family: PingFangSC-Semibold, PingFang SC;
  font-weight: 600;
  border-radius: 50%;
  border: 1px solid #dcdfe6;
  color: #c0c4cc;
  margin-left: 8px;
  cursor: pointer;
}
.footer {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
.line {
  width: 100%;
  height: 4px;
  border-bottom: 1px solid #f2f6fc;
  margin-bottom: 30px;
}
.active {
  background: #0091ff;
  border: none;
  color: #ffffff;
}
.line {
  width: 100%;
  height: 4px;
  border-bottom: 1px dashed #dcdfe6;
  margin-bottom: 30px;
  margin-top: 10px;
}
.img {
  width: 40px;
  height: 40px;
}

.step-text {
  margin-left: 8px;
  margin-right: 8px;
  font-size: 16px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #909399;
}
.step {
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 15px 0;
}

.line-title {
  display: inline-block;
  width: 180px;
  height: 4px;
  border-bottom: 1px solid #e9e9e9;
}
.activeText {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
}
::v-deep .el-drawer__body {
  margin-right: 20px;
  margin-left: 20px;
}
.invoice {
  margin: 20px 20px 20px 20px;
  .from-foot-btn button {
    width: auto;
    height: auto;
  }
}
::v-deep .el-input-number {
  width: 100%;
  .el-input__inner {
    text-align: left;
  }
}
.from-item-title {
  border-left: 5px solid #1890ff;
  span {
    padding-left: 10px;
    font-weight: bold;
    font-size: 14px;
  }
}
.form-box {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  .form-item {
    width: 49%;
    ::v-deep .el-form-item__content {
      width: calc(100% - 110px);
    }
    ::v-deep .el-select {
      width: 100%;
    }
    ::v-deep .el-textarea__inner {
      resize: none;
    }
  }
}
.alert {
  width: 100%;
  padding: 20px 20px;
  padding-bottom: 0;
}
::v-deep .el-alert {
  padding-left: 30px;
  border: 1px solid #1890ff;
  color: #1890ff;
  font-size: 13px;
  background-color: #edf7ff;
  line-height: 1;
  margin-bottom: 10px;
}
::v-deep .el-alert--info .el-alert__description {
  color: #303133;
  font-size: 13px;
  font-weight: 500;
}
::v-deep .el-alert__icon.is-big {
  font-size: 16px;
  width: 15px;
}
.mb50 {
  margin-bottom: 50px;
}
</style>
