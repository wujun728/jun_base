<!-- 查看合同信息页面 -->
<template>
  <div class="contract">
    <el-drawer
      :append-to-body="true"
      :before-close="handleClose"
      :direction="direction"
      :modal="true"
      :modal-append-to-body="false"
      :show-close="true"
      :size="formData.width"
      :title="formData.title"
      :visible.sync="drawer"
      :wrapper-closable="true"
    >
      <div slot="title" class="invoice-title">
        <el-row class="invoice-header">
          <el-col class="invoice-left">
            <div class="invoice-logo"><i class="icon iconfont iconhetong"></i></div>
          </el-col>

          <el-col v-if="drawer" class="invoice-right">
            <div class="txt1 over-text">{{ formData.data.contract_name }}</div>
            <div class="txt2">
              <span class="title">合同状态：</span
              ><span :style="{ color: activeColor }">{{ getStatus(dataInfo.contract_status) }}</span>
              <span class="title">合同金额(元)：</span><span class="info1">¥{{ dataInfo.contract_price }}</span>
              <span class="title">{{ $t('customer.customer') }}：</span
              ><span class="weight">{{
                dataInfo.contract_customer ? dataInfo.contract_customer.customer_name : '--'
              }}</span>
              <span class="title"> 业务员：</span
              ><span class="weight">{{ dataInfo.salesman ? dataInfo.salesman.name : '--' }}</span>
            </div>
          </el-col>
        </el-row>
      </div>
      <el-tabs
        v-if="tabData.length"
        v-model="tabIndex"
        :tab-position="tabPosition"
        type="border-card"
        @tab-click="handleClick"
      >
        <el-tab-pane v-for="item in tabData" :key="item.value" :label="item.label" :name="item.value"></el-tab-pane>
      </el-tabs>
      <div class="contract-body table-box mt14">
        <!--基本信息-->
        <div v-show="tabNumber === 1" class="contract-info">
          <el-form class="invoice-body" label-width="auto">
            <div v-for="(item, index) in dataInfo.list" :key="index">
              <div class="from-item-title mb15">
                <span>{{ item.title }}</span>
              </div>
              <div class="form-box">
                <div
                  v-for="(value, key) in item.data"
                  :key="key"
                  :class="value.type !== 'file' && value.type !== 'image' ? '' : 'oneline'"
                  class="form-item"
                >
                  <el-form-item v-if="value.type == 'file' || value.type == 'images'">
                    <span slot="label">{{ value.key_name }}:</span>

                    <upload-file
                      v-if="value.files && value.files.length > 0"
                      v-model="value.files"
                      :only-image="false"
                      :onlyRead="true"
                      :value="value.files"
                    ></upload-file>
                    <span v-else>--</span>
                  </el-form-item>
                  <el-form-item v-else-if="value.type == 'oaWangeditor'">
                    <span slot="label">{{ value.key_name }}：</span>
                    <ueditor-from ref="ueditorFrom" :border="true" :content="value.value" :height="`400px`">
                    </ueditor-from>
                  </el-form-item>
                  <el-form-item v-else>
                    <span slot="label">{{ value.key_name }}：</span>
                    <p v-html="getValue(value.value)"></p>
                  </el-form-item>
                </div>
              </div>
              <div class="line" />
            </div>
          </el-form>
        </div>
        <!--合同付款-->
        <div v-show="tabNumber === 2" class="contract-list">
          <contract-payment ref="contractPayment" :form-info="formData"></contract-payment>
        </div>
        <!--合同续费-->
        <div v-show="tabNumber === 6" class="contract-list">
          <contract-renew ref="contractRenew" :form-info="formData"></contract-renew>
        </div>
        <!--付款提醒-->
        <div v-show="tabNumber === 3" class="contract-list">
          <contract-remind ref="contractRemind" :form-info="formData"></contract-remind>
        </div>
        <!--发票-->
        <div v-show="tabNumber === 4" class="contract-list">
          <contract-invoice
            ref="contractInvoice"
            :contractInvoice="contractInvoice"
            :form-info="formData"
            @handleInvoice="handleInvoice"
          ></contract-invoice>
        </div>
        <!--附件相关-->
        <div v-if="tabNumber === 5" class="contract-list">
          <file ref="file" :form-info="formData"></file>
        </div>
      </div>
    </el-drawer>
    <edit-customer ref="editCustomer" :form-data="newForm" @isOkEdit="getTableData(true)"></edit-customer>
  </div>
</template>

<script>
import { clientDataInfoApi, contractDetailApi } from '@/api/enterprise'
export default {
  name: 'EditContract',
  props: {
    formData: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  components: {
    contractInfo: () => import('./contractInfo'),
    contractRenew: () => import('./contractRenew'),
    contractInvoice: () => import('./contractInvoice'),
    contractRemind: () => import('./contractRemind'),
    contractPayment: () => import('./contractPayment'),
    file: () => import('./file'),
    uploadFile: () => import('@/components/form-common/oa-upload'),
    ueditorFrom: () => import('@/components/form-common/oa-wangeditor'),
    editCustomer: () => import('@/views/customer/list/components/editCustomer')
  },
  data() {
    return {
      drawer: false,
      direction: 'rtl',
      record: {
        info: ''
      },
      cid: 0,
      activeColor: '',
      newForm: {},
      contractInvoice: 'is_contract',
      autosize: {
        minRows: 5,
        maxRows: 10
      },
      remindAutosize: {
        minRows: 2,
        maxRows: 4
      },
      dataInfo: [],
      tabPosition: 'top',
      tabIndex: '1',
      tabNumber: 1,
      tabData: [
        { value: '1', label: this.$t('setting.info.essentialinformation') },
        { value: '2', label: '账目记录' },
        // { value: '6', label: '合同续费' },
        { value: '3', label: this.$t('customer.paymentreminder') },
        { value: '4', label: this.$t('customer.invoice') },
        { value: '5', label: '记录' }
      ],
      configContract: {},
      formBoxConfig: {},
      loading: false
    }
  },
  computed: {
    lang: function () {
      return this.$store.getters.lang
    }
  },
  watch: {
    lang() {
      this.setOptions()
    }
  },
  methods: {
    setOptions() {
      this.tabData = [
        { value: '1', label: this.$t('setting.info.essentialinformation') },
        { value: '2', label: this.$t('customer.paymentrecord') },
        { value: '3', label: this.$t('customer.paymentreminder') },
        { value: '4', label: this.$t('customer.invoice') },
        { value: '5', label: this.$t('customer.annexrelated') }
      ]
    },
    handleClose() {
      this.drawer = false
    },
    // 获取合同详情
    getDetails(id) {
      contractDetailApi(id).then((res) => {
        this.dataInfo = res.data
      })
    },
    // 合同状态
    getStatus(val) {
      let statusData = {
        0: '未开始',
        1: '进行中',
        2: '已结束',
        3: '异常合同'
      }
      let colorData = {
        0: '#00c050',
        1: '#1890ff',
        2: '#999999',
        3: '#ed4014'
      }
      this.activeColor = colorData[val]

      return statusData[val]
    },
    // 数组转成字符串
    getValue(val) {
      let str = ''
      if (val == '') {
        str = '--'
      } else if (Array.isArray(val)) {
        str = val.toString()
      } else {
        str = val
      }
      return str || '--'
    },
    openBox(row) {
      this.cid = row.cid
      this.drawer = true
      if (this.tabNumber == 2) {
        setTimeout(() => {
          this.$refs.contractPayment.getTableData(row.id)
          this.$refs.contractPayment.getConfigApprove(6)
          this.$refs.contractPayment.getConfigApprove(7)
          this.$refs.contractPayment.getConfigApprove(8)
        }, 300)
      }
      if (row.cid) {
        this.getDetails(row.cid)
      }
    },
    handleClick(event) {
      this.tabNumber = Number(event.name)
      if (this.tabNumber === 2) {
        this.$refs.contractPayment.getTableData(this.cid)
        this.$refs.contractPayment.getConfigApprove(6)
        this.$refs.contractPayment.getConfigApprove(7)
        this.$refs.contractPayment.getConfigApprove(8)
      } else if (this.tabNumber === 3) {
        this.$refs.contractRemind.getTableData()
      } else if (this.tabNumber === 4) {
        this.$refs.contractInvoice.getTableData()
      } else if (this.tabNumber === 5) {
        setTimeout(() => {
          this.$refs.file.getTableData()
        }, 500)
      } else if (this.tabNumber === 6) {
        this.$refs.contractRenew.getTableData()
      }
    },

    // 打开客户
    handleClient() {
      clientDataInfoApi(this.formData.data.contract_customer.id).then((res) => {
        res.data.source = res.data.source.id
        let newAddress = []
        if (res.data.address) {
          res.data.address.map((item) => {
            newAddress.push(item.label)
          })
          res.data.address = newAddress
        }

        this.newForm = {
          title: this.$t('customer.editcustomer'),
          width: '1000px',
          data: res.data,
          isClient: true,
          edit: true
        }

        this.$refs.editCustomer.tabIndex = '1'
        this.$refs.editCustomer.tabNumber = 1
        this.$refs.editCustomer.openBox()
      })
    },
    handleContractInfo(data) {
      this.loading = data.loading
      if (data.success === 1) {
        this.formData.editIndex = data.editIndex
        this.formData.data.title = data.title
        this.formData.data.price = data.price
        this.$emit('isOk')
      }
    },
    handleInvoice() {
      this.$emit('isOk')
    },
    handleConfirm() {
      this.$refs.contractInfo.handleConfirm()
    }
  }
}
</script>

<style lang="scss" scoped>
::v-deep .el-tabs__item.is-active {
  border-right-color: transparent !important;
  border-left-color: transparent !important;
  &::after {
    content: '';
    height: 2px;
    width: 100%;
    background-color: #1890ff;
    position: absolute;
    left: 0;
    top: 0;
  }
}

.add-color {
  cursor: pointer !important;
  color: #1890ff !important;
}
.contract {
}
::v-deep .el-drawer__header {
  height: 80px !important;
  padding: 14px 18px;
}
::v-deep .el-tabs__content {
  padding: 0;
}
::v-deep .el-form--inline .el-form-item {
  display: flex;
}
::v-deep .el-tabs__header {
  background-color: #f7fbff;
  border-bottom: none;
}
::v-deep .el-tabs__nav-wrap::after {
  height: 0;
}
::v-deep .el-tabs__active-bar {
  top: 0;
}
::v-deep .el-input__inner {
  text-align: left;
}
.el-tabs--border-card {
  height: 39px;
  position: fixed;
  top: 80px;
  width: 100%;
  z-index: 4;
  background-color: transparent;
  border: none;
  box-shadow: none;
}
.invoice-title {
  .invoice-header {
    display: flex;
    align-items: center;
    .invoice-left {
      width: 48px;
      margin-right: 10px;
      .invoice-logo {
        width: 48px;
        height: 48px;
        display: flex;
        align-items: center;
        justify-content: center;
        background-color: #1890ff;
        border-radius: 4px;
        i {
          color: #ffffff;
          font-size: 30px;
          // margin-top: 12px;
        }
      }
    }
    .invoice-right {
      width: calc(100% - 55px);
    }
    .txt1 {
      font-size: 14px;
      font-weight: bold;
      color: rgba(0, 0, 0, 0.85);
    }
    .txt2 {
      margin-top: 10px;
      font-size: 13px;
      color: #000;
      .title {
        color: #999999;
        padding-left: 20px;
      }
      .title:first-of-type {
        padding-left: 0;
      }
      .info1 {
        color: rgba(245, 34, 45, 1);
      }
      .info2 {
        color: #1890ff;
      }
    }
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
.contract-body {
  margin-top: 39px;
  padding: 20px;
  height: 100%;
  display: flex;
  justify-content: center;
  .contract-info {
    width: 100%;
  }
  .contract-list {
    width: 100%;
    height: 100%;
    ::v-deep .el-button--medium {
      font-size: 13px;
    }
    .icon-cover {
      font-size: 28px;
    }
  }
}
.dialog-footer {
  padding-top: 20px;
  border-top: 1px solid #e6ebf5;
  text-align: right;
}
.from-foot-btn {
  button {
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
    ::v-deep .el-form-item__label {
      color: #606266;
    }
    ::v-deep .el-form-item__content {
      width: calc(100% - 110px);
    }
    ::v-deep .el-select--medium {
      width: 100%;
    }
    ::v-deep .el-form-item {
      margin-bottom: 0;
    }
    ::v-deep .el-textarea__inner {
      resize: none;
    }

    p {
      margin: 0;
      padding: 0;

      font-weight: 400 !important;
      color: #303133;
      font-size: 13px !important;
      margin-top: 10px;
      line-height: 18px;
    }
  }
}
.oneline {
  width: 100% !important;
}
.line {
  width: 100%;
  height: 4px;
  border-bottom: 1px dashed #f2f6fc;
  margin-bottom: 20px;
  margin-top: 10px;
}
::v-deep.btn-box {
  display: flex;
  justify-content: flex-end;
}
::v-deep.el-tabs__header .el-tabs__item {
  line-height: 40px;
}
</style>
