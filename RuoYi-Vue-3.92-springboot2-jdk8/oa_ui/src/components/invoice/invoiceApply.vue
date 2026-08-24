<!-- 申请发票信息侧滑弹窗 -->
<template>
  <div class="station">
    <el-drawer
      :append-to-body="true"
      :before-close="handleClose"
      :direction="direction"
      :modal="true"
      :size="formData.width"
      :title="formData.title ? formData.title : title"
      :visible.sync="drawer"
      :wrapper-closable="true"
      :wrapperClosable="false"
    >
      <div class="invoice">
        <el-form ref="form" :model="rules" :rules="rule" label-width="110px">
          <div class="from-item-title mb15">
            <span>{{ $t('setting.info.essentialinformation') }}</span>
          </div>

          <!-- 基本信息 -->
          <div class="form-box">
            <div class="form-item">
              <el-form-item>
                <span slot="label"><span class="color-tab">* </span>客户名称:</span>

                <span class="font-color">{{ rules.source }}</span>
              </el-form-item>
            </div>
            <div class="form-item">
              <el-form-item>
                <span slot="label"><span class="color-tab">* </span>{{ $t('customer.contractname') }}:</span>

                <span class="font-color">{{ rules.name }}</span>
              </el-form-item>
            </div>

            <div class="form-item">
              <el-form-item prop="category_id">
                <span slot="label">发票类目:</span>
                <el-select
                  v-model="rules.category_id"
                  :placeholder="$t('customer.placeholder45')"
                  clearable
                  size="small"
                >
                  <el-option v-for="item in categoryList" :key="item.id" :label="item.name" :value="item.id" />
                </el-select>
              </el-form-item>
            </div>
            <div class="form-item">
              <el-form-item prop="bill_date">
                <span slot="label">{{ $t('customer.invoicingdate') }}:</span>
                <el-date-picker
                  v-model="rules.bill_date"
                  :placeholder="$t('customer.placeholder65')"
                  clearable
                  size="small"
                  type="date"
                >
                </el-date-picker>
              </el-form-item>
            </div>
          </div>
          <div class="line" />
          <!-- 邮寄信息 -->
          <div class="from-item-title mb15">
            <span>{{ $t('customer.mailinginformation') }}</span>
          </div>
          <div class="form-box">
            <div class="form-item" style="width: 100%">
              <el-form-item>
                <span slot="label">开票要求:</span>
                <el-radio-group v-model="rules.collect_type" @change="collectTypeFn">
                  <el-radio :label="'mail'">电子</el-radio>
                  <el-radio :label="'express'">纸质</el-radio>
                </el-radio-group>
              </el-form-item>
            </div>

            <div v-if="rules.collect_type == 'express'" class="form-item">
              <el-form-item prop="collect_name">
                <span slot="label">联系人:</span>
                <el-input
                  v-model.trim="rules.collect_name"
                  :placeholder="$t('customer.placeholder52')"
                  clearable
                  size="small"
                />
              </el-form-item>
            </div>
            <div v-if="rules.collect_type == 'express'" class="form-item">
              <el-form-item prop="collect_tel">
                <span slot="label">{{ $t('customer.contactnumber') }}:</span>
                <el-input
                  v-model.trim="rules.collect_tel"
                  :placeholder="$t('customer.placeholder53')"
                  clearable
                  size="small"
                />
              </el-form-item>
            </div>

            <div v-if="rules.collect_type == 'mail'" class="form-item">
              <el-form-item prop="collect_email">
                <span slot="label">邮箱地址:</span>
                <el-input
                  v-model="rules.collect_email"
                  :placeholder="$t('customer.placeholder55')"
                  clearable
                  size="small"
                />
              </el-form-item>
            </div>
            <div v-else class="form-item">
              <el-form-item prop="mail_address">
                <span slot="label">邮寄地址:</span>
                <el-input
                  v-model.trim="rules.mail_address"
                  :placeholder="$t('customer.placeholder56')"
                  clearable
                  size="small"
                />
              </el-form-item>
            </div>
          </div>
          <div class="line" />

          <!-- 发票信息 -->
          <div class="from-item-title mb15">
            <span>{{ $t('customer.invoiceinformation') }}</span>
          </div>
          <div class="form-box">
            <div class="form-item">
              <el-form-item>
                <span slot="label"><span class="color-tab">* </span>{{ $t('customer.headerinformation') }}:</span>
                <el-select v-model="rules.types" :placeholder="$t('customer.placeholder45')" clearable size="small">
                  <el-option v-for="item in invoiceOptions" :key="item.value" :label="item.label" :value="item.value" />
                </el-select>
              </el-form-item>
            </div>
            <div class="form-item">
              <el-form-item prop="amount">
                <span slot="label">{{ $t('customer.invoicingpay') }}:</span>
                <el-input-number
                  v-model="rules.amount"
                  :controls="false"
                  :min="0"
                  :placeholder="$t('customer.placeholder44')"
                  :precision="2"
                  clearable
                  size="small"
                ></el-input-number>
              </el-form-item>
            </div>

            <div class="form-item">
              <el-form-item prop="title">
                <span slot="label">{{ $t('customer.invoiceheader') }}:</span>
                <el-input
                  v-model.trim="rules.title"
                  :placeholder="$t('customer.placeholder46')"
                  clearable
                  size="small"
                />
              </el-form-item>
            </div>
            <div v-if="rules.types > 1" class="form-item">
              <el-form-item prop="ident">
                <span slot="label">{{ $t('customer.paytaxes') }}:</span>
                <el-input
                  v-model.trim="rules.ident"
                  :placeholder="$t('customer.placeholder47')"
                  clearable
                  size="small"
                />
              </el-form-item>
            </div>
            <div v-if="rules.types > 2" class="form-item">
              <el-form-item>
                <!-- prop="bank" -->
                <span slot="label">{{ $t('customer.bankdeposit') }}:</span>
                <el-input
                  v-model.trim="rules.bank"
                  :placeholder="$t('customer.placeholder48')"
                  clearable
                  size="small"
                />
              </el-form-item>
            </div>
            <div v-if="rules.types > 2" class="form-item">
              <el-form-item>
                <!-- prop="account" -->
                <span slot="label">{{ $t('customer.accountnumber') }}:</span>
                <el-input
                  v-model.trim="rules.account"
                  :placeholder="$t('customer.placeholder49')"
                  clearable
                  size="small"
                />
              </el-form-item>
            </div>
            <div v-if="rules.types > 2" class="form-item">
              <el-form-item>
                <!-- prop="address" -->
                <span slot="label">{{ $t('customer.billingaddress') }}:</span>
                <el-input
                  v-model="rules.address"
                  :placeholder="$t('customer.placeholder50')"
                  clearable
                  size="small"
                  @input="onInput()"
                />
              </el-form-item>
            </div>
            <div v-if="rules.types > 2" class="form-item">
              <el-form-item>
                <!-- prop="tel" -->
                <span slot="label">{{ $t('customer.tel') }}:</span>
                <el-input v-model.trim="rules.tel" :placeholder="$t('customer.placeholder51')" clearable size="small" />
              </el-form-item>
            </div>
          </div>
          <div class="line" />

          <div class="form-box">
            <div class="form-item" style="width: 100%">
              <el-form-item>
                <span slot="label">{{ $t('public.remarks') }}:</span>
                <el-input
                  v-model="rules.mark"
                  :placeholder="$t('customer.placeholder18')"
                  :rows="4"
                  maxlength="255"
                  show-word-limit
                  type="textarea"
                />
              </el-form-item>
            </div>
          </div>
        </el-form>
        <div class="button from-foot-btn fix btn-shadow">
          <el-button size="small" @click="handleClose">{{ $t('public.cancel') }}</el-button>
          <el-button :loading="loading" size="small" type="primary" @click="handleConfirm('ruleForm')">{{
            $t('public.ok')
          }}</el-button>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import { clientInvoiceEditApi, clientInvoiceSaveApi, clientInvoiceDetailApi } from '@/api/enterprise'
import helper from '@/libs/helper'

export default {
  name: 'InvoiceApply',
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
    title: {
      type: String,
      default: ''
    }
  },
  data() {
    const checkEmail = (rule, value, callback) => {
      const mailReg = helper.mailboxReg
      if (!value) {
        return callback(new Error(this.$t('customer.placeholder68')))
      } else {
        return callback()
      }
    }
    const checkIdent = (rule, value, callback) => {
      if (!value && this.rules.types > 1) {
        return callback(new Error(this.$t('customer.placeholder47')))
      } else if (!helper.identReg.test(value)) {
        callback(new Error('税号输入不合法'))
      } else {
        callback()
      }
    }
    return {
      drawer: false,
      direction: 'rtl',
      categoryList: [],
      eid: 0,
      cid: 0,
      rules: {
        name: '',
        source: '',
        price: '',
        collect_type: 'mail',
        amount: undefined,
        bill_date: '',
        types: 2,
        title: '',
        ident: '',
        mail_address: '',
        bank: '',
        account: '',
        tel: '',
        mark: '',
        collect_name: '',
        collect_tel: '',
        method: 'mail',
        addressBox: '',
        collect_email: '',
        address: '',
        category_id: ''
      },
      methodOptions: [
        { value: 'express', label: this.$t('customer.express') },
        { value: 'mail', label: this.$t('customer.mail') }
      ],
      loading: false,
      invoiceOptions: [
        { value: 1, label: this.$t('customer.personalinvoice') },
        { value: 2, label: this.$t('customer.enterpriseinvoice') },
        { value: 3, label: this.$t('customer.specialinvoice') }
      ],
      rule: {
        name: [{ required: true, message: this.$t('customer.placeholder42'), trigger: 'blur' }],
        amount: [{ required: true, message: this.$t('customer.placeholder44'), trigger: 'blur' }],
        bill_date: [{ required: true, message: this.$t('customer.placeholder65'), trigger: 'blur' }],
        title: [{ required: true, message: this.$t('customer.placeholder46'), trigger: 'blur' }],
        ident: [{ required: true, validator: checkIdent, trigger: 'blur' }],
        category_id: [{ required: true, message: '请选择发票类目', trigger: 'change' }],
        collect_name: [{ required: true, message: '电话号码不能为空', trigger: 'blur' }],
        collect_tel: [{ required: true, message: '电话号码不能为空', trigger: 'blur' }],
        collect_email: [{ required: true, validator: checkEmail, trigger: 'blur' }],
        mail_address: [{ required: true, message: '请填写邮寄地址', trigger: 'blur' }],
        address: [{ required: true, message: '请填写开票地址', trigger: 'blur' }],
        bank: [{ required: true, message: this.$t('customer.placeholder48'), trigger: 'blur' }],
        account: [{ required: true, message: this.$t('customer.placeholder49'), trigger: 'blur' }],
        tel: [{ required: true, message: '电话号码不能为空', trigger: 'blur' }]
      }
    }
  },
  computed: {
    lang: function () {
      return this.$store.getters.lang
    }
  },
  watch: {
    formData: {
      handler(nVal) {
        // 添加
        if (!nVal.edit) {
          if (nVal.data.cid == 0) {
            this.eid = nVal.data.id
            this.cid = 0
          }
          if (nVal.data.eid) {
            this.eid = nVal.data.eid
            this.cid = nVal.data.id
          }
          this.rules.name = nVal.data.title || '--'
          if (nVal.data.name) {
            this.rules.source = nVal.data.name
          } else {
            this.rules.source = nVal.data.client ? nVal.data.client.name : '--'
          }

          if (nVal.oneData) {
            this.rules = nVal.oneData
            this.rules.source = nVal.data.name ? nVal.data.name : nVal.data.client.name
            this.rules.types = Number(nVal.oneData.types)
            setTimeout(() => {
              this.rules.category_id = nVal.oneData.category_id
            }, 200)
          } else {
            this.rules.address = ''
          }

          this.rules.bill_date = this.$moment(new Date()).format('YYYY-MM-DD')
          this.rules.price = nVal.data.price
        }
      },
      deep: true
    },
    lang() {
      this.setOptions()
    }
  },
  methods: {
    setOptions() {
      this.methodOptions = [
        { value: 'express', label: this.$t('customer.express') },
        { value: 'mail', label: this.$t('customer.mail') }
      ]
      this.invoiceOptions = [
        { value: 1, label: this.$t('customer.personalinvoice') },
        { value: 2, label: this.$t('customer.enterpriseinvoice') },
        { value: 3, label: this.$t('customer.specialinvoice') }
      ]
    },
    change(e) {
      this.$forceUpdate()
    },
    handleClose() {
      this.drawer = false
      this.reset()
    },
    openBox(id) {
      if (id) {
        clientInvoiceDetailApi(id).then((res) => {
          this.rules = res.data
          this.rules.source = res.data.client ? res.data.client.name : '--'
          this.eid = res.data.client ? res.data.client.id : res.data.eid
          this.rules.name = res.data.treaty ? res.data.treaty.title : '--'
          this.cid = res.data.treaty ? res.data.treaty.id : res.data.cid
          this.rules.types = Number(res.data.types)
        })
      }
      this.drawer = true
    },
    collectTypeFn(e) {
      this.rules.collect_type = e
    },
    onInput() {
      this.$forceUpdate()
    },
    reset() {
      for (let key in this.rules) {
        this.rules[key] = ''
      }
      this.rules.amount = undefined
      this.rules.types = 2
      this.rules.collect_type = 'mail'
    },
    handleMethod() {
      this.$refs.form.clearValidate('address')
      this.$refs.form.clearValidate('mailbox')
    },
    // 提交
    handleConfirm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          var data = this.rules
          data.bill_date = this.$moment(this.rules.bill_date).format('yyyy-MM-DD')
          data.cid = this.cid
          data.eid = this.eid

          let objId = {
            eid: this.formData.data.eid,
            category_id: this.rules.category_id
          }

          let categoryList = JSON.parse(localStorage.getItem('categoryList'))
          if (categoryList) {
            var flag = categoryList.find((cur) => cur.eid == this.formData.data.eid)

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
          if (this.formData.edit) {
            this.invoiceEdit(this.formData.rowData.id, data)
          } else {
            this.invoiceSave(data)
          }
        }
      })
    },
    // 发票申请
    invoiceSave(data) {
      this.loading = true
      clientInvoiceSaveApi(data)
        .then((res) => {
          this.handleClose()
          this.$emit('isOk')
          this.reset()
          this.loading = false
        })
        .catch((error) => {
          this.loading = false
        })
    },
    // 发票修改
    invoiceEdit(id, data) {
      this.loading = true
      clientInvoiceEditApi(id, data)
        .then((res) => {
          this.handleClose()
          this.$emit('isOk')
          this.reset()
          this.loading = false
        })
        .catch((error) => {
          this.loading = false
        })
    }
  }
}
</script>

<style lang="scss" scoped>
.station ::v-deep.el-drawer__body {
  padding: 20px 20px 50px 20px;
}
::v-deep .el-form--inline .el-form-item {
  display: flex;
}
::v-deep .el-input-number {
  width: 100%;
  .el-input__inner {
    text-align: left;
  }
}
.font-color {
  color: #606266;
}
.line {
  width: 100%;
  height: 4px;
  border-bottom: 1px dashed #dcdfe6;
  margin-bottom: 30px;
  margin-top: 10px;
}
::v-deep .el-date-editor {
  width: 100%;
}
.invoice {
  margin: 20px 20px 20px 20px;
  .from-foot-btn button {
    width: auto;
    height: auto;
  }
}
.from-item-title {
  border-left: 3px solid #1890ff;
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
.dialog-footer {
  padding-top: 20px;
  border-top: 1px solid #e6ebf5;
  text-align: right;
}
::v-deep.el-drawer {
  width: 50% !important;
}
</style>
