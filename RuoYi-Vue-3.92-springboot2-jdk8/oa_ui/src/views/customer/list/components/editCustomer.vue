<!-- 查看客户信息侧滑页面 -->
<template>
  <div class="station">
    <el-drawer
      :append-to-body="true"
      :before-close="handleClose"
      :direction="direction"
      :show-close="true"
      :size="formData.width"
      :title="formData.title"
      :visible.sync="drawer"
    >
      <div slot="title" class="invoice-title">
        <el-row class="invoice-header">
          <el-col class="invoice-left">
            <div class="invoice-logo"><i class="icon iconfont iconhetong"></i></div>
          </el-col>
          <el-col v-if="drawer" class="invoice-right">
            <div class="txt1 over-text">
              {{ dataInfo.customer_name }}
              <i class="el-icon-message-solid default-color pointer" @click="addRecord"></i>
              <span class="default-color pointer txt3" @click="addRecord">添加提醒</span>
            </div>
            <div class="txt2">
              <span class="title">客户状态：</span>

              <span v-if="dataInfo.customer_status == 0" class="info3">未成交</span>
              <span v-else :class="dataInfo.customer_status == 1 ? 'info1' : 'info2'">{{
                dataInfo.customer_status == 1 ? '已成交' : '已流失'
              }}</span>
              <span class="title">{{ $t('customer.salesman') }}：</span
              ><span class="weight">{{ dataInfo.salesman ? dataInfo.salesman.name : '--' }}</span>
              <span class="title">客户编号：</span><span class="weight">{{ dataInfo.customer_no }}</span>
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
      <div class="contract-body table-box">
        <!--基本信息-->
        <div v-show="tabNumber == 1" class="contract-info">
          <el-form class="invoice-body" label-width="auto">
            <div v-for="(item, index) in dataInfo.list" :key="index">
              <div class="from-item-title mb15">
                <span>{{ item.title }}</span>
              </div>
              <div class="form-box">
                <div
                  v-for="(value, key) in item.data"
                  :key="key"
                  :class="
                    value.type !== 'file' && value.type !== 'image' && value.type !== 'oaWangeditor' ? '' : 'oneline'
                  "
                  class="form-item"
                >
                  <el-form-item v-if="value.key == 'customer_status'">
                    <span slot="label">{{ value.key_name }} ：</span>
                    <span>{{ value.value[0] || '-' }}</span>
                  </el-form-item>
                  <el-form-item v-else-if="value.type == 'file' || value.type == 'images'">
                    <span slot="label">{{ value.key_name }}：</span>
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

                    <div class="content" v-html="value.value" />
                  </el-form-item>
                  <el-form-item v-else>
                    <span slot="label">{{ value.key_name }}：</span>
                    <p v-html="getValue(value.value)"></p>
                  </el-form-item>
                </div>
              </div>
            </div>
          </el-form>
        </div>
        <!--跟进记录-->
        <div v-show="tabNumber === 2" class="contract-record">
          <record ref="record" :form-info="formData"></record>
        </div>
        <!--联系人-->
        <div v-show="tabNumber === 8" class="contract-list">
          <liaison ref="liaison" :customInfo="customInfo" :custom_type="custom_type" :form-info="formData"></liaison>
        </div>
        <!--合同-->
        <div v-show="tabNumber === 3" class="contract-list">
          <contract ref="contract" :form-info="formData"></contract>
        </div>
        <!--付款记录-->
        <div v-show="tabNumber === 4" class="contract-list">
          <contract-record-all ref="contractRecord" :form-info="formData"></contract-record-all>
        </div>
        <!--付款提醒-->
        <div v-show="tabNumber === 5" class="contract-remind">
          <contract-remind ref="contractRemind" :form-info="formData" :type="1"></contract-remind>
        </div>
        <!--发票-->
        <div v-show="tabNumber === 6" class="contract-list">
          <contract-invoice ref="contractInvoice" :form-info="formData"></contract-invoice>
        </div>
        <!--附件相关-->
        <div v-show="tabNumber === 7" class="contract-list">
          <file ref="file" :form-info="formData"></file>
        </div>
        <!--动态记录-->
        <div v-show="tabNumber === 9" class="contract-list">
          <dynamic-record ref="dynamicRecord" :form-info="formData"></dynamic-record>
        </div>
      </div>
    </el-drawer>
    <!-- 跟进提醒弹窗 -->
    <remind-dialog ref="remindDialog" :config="remindConfig" @change="change"></remind-dialog>
  </div>
</template>
<script>
import { chargeDetailsApi } from '@/api/enterprise'
export default {
  name: 'EditCustomer',
  components: {
    uploadFile: () => import('@/components/form-common/oa-upload'),
    contractRecordAll: () => import('./contractRecordAll'),
    contractInvoice: () => import('@/views/customer/contract/components/contractInvoice'),
    liaison: () => import('@/views/customer/list/components/liaison'),
    record: () => import('@/views/customer/list/components/record'),
    file: () => import('@/views/customer/list/components/file'),
    contract: () => import('@/views/customer/list/components/contract'),
    contractRemind: () => import('@/views/customer/contract/components/contractRemind'),
    remindDialog: () => import('./remindDialog'),
    dynamicRecord: () => import('./dynamicRecord')
  },
  props: {
    formData: {
      type: Object,
      default: () => {
        return {}
      }
    },
    custom_type: {
      type: Number,
      default: 1
    }
  },
  data() {
    return {
      dataInfo: [],
      drawer: false,
      direction: 'rtl',
      tabPosition: 'top',
      tabIndex: '1',
      tabNumber: 1,
      customInfo: { id: 0, types: 0 },
      tabData: [
        { value: '1', label: this.$t('setting.info.essentialinformation') },
        { value: '2', label: this.$t('customer.followrecord') },
        { value: '8', label: this.$t('customer.contacts') },
        { value: '3', label: this.$t('customer.contract') },
        { value: '4', label: '账目记录' },
        { value: '5', label: this.$t('customer.paymentreminder') },
        { value: '6', label: this.$t('customer.invoice') },
        { value: '7', label: this.$t('customer.annexrelated') },
        { value: '9', label: '动态记录' }
      ],
      remindConfig: {}
    }
  },

  methods: {
    async getDetails(id) {
      const result = await chargeDetailsApi(id)
      this.dataInfo = result.data
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

    change() {
      this.$refs.record.getTableData()
    },
    handleClose() {
      this.drawer = false
    },

    openBox(id, type) {
      if (id) {
        this.getDetails(id)
        this.customInfo.id = id
      }
      if (type) {
        this.customInfo.types = type
      }
      this.drawer = true
    },

    // 点击tab切换
    handleClick(event) {
      this.tabNumber = Number(event.name);
      const actions = {
        2: () => this.$refs.record.getTableData(),
        3: () => this.$refs.contract.getTableData(),
        4: () => {
          this.$refs.contractRecord.getTableData();
          this.$refs.contractRecord.getConfigApprove();
        },
        5: () => this.$refs.contractRemind.getTableData(),
        6: () => {
          this.$refs.contractInvoice.getTableData();
          this.$refs.contractInvoice.getConfigApprove();
        },
        7: () => this.$refs.file.getTableData(),
        8: () => {
          this.$refs.liaison.salesmanCustom();
          this.$refs.liaison.getTableData();
        },
        9: () => this.$refs.dynamicRecord.getTableData(),
        1: () => this.getDetails(this.formData.data.eid)
      };
      const action = actions[this.tabNumber];
      if (action) {
        action();
      }
    },

    // 添加跟进提醒
    addRecord() {
      this.remindConfig = {
        eid: this.formData.data.eid,
        isEdit: false
      }
      this.$refs.remindDialog.handleOpen(false)
    }
  }
}
</script>

<style lang="scss" scoped>
.content {
  padding: 0 14px;
  width: 100%;
  height: 400px;
  border: 1px solid #d7dbe0;
}
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
::v-deep.el-date-editor.el-input {
  width: 100%;
}

.addBox ::v-deep .el-dialog__body {
  padding: 0;
}
.addBox ::v-deep .el-dialog {
  border-radius: 6px;
  height: 300px;
}
::v-deep .el-form--inline .el-form-item {
  display: flex;
}
::v-deep .el-drawer__body {
  padding-bottom: 50px;
}
::v-deep .el-drawer__header {
  height: 80px !important;
  border: none;
  padding: 14px 18px;
}
::v-deep .el-tabs__item {
  line-height: 40px !important;
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
      font-size: 16px;
      font-weight: bold;
      color: rgba(0, 0, 0, 0.85);
    }
    .txt3 {
      font-size: 14px;
    }
    .txt2 {
      margin-top: 10px;
      font-size: 13px;
      color: #000;
      .title {
        font-size: 14px;
        color: #999999;
        padding-left: 20px;
        font-weight: 400;
      }
      .title:first-of-type {
        padding-left: 0;
      }
      .info1 {
        color: #19be6b;
      }
      .info2 {
        color: rgba(245, 34, 45, 1);
      }
      .info3 {
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
  display: flex;
  height: 100%;
  justify-content: center;
  .contract-info {
    width: 100%;
    .form-box {
      display: flex;
      flex-wrap: wrap;
      justify-content: space-between;
      .form-item {
        width: 49%;
        ::v-deep .el-form-item__content {
          width: calc(100% - 110px);
        }
        ::v-deep .el-select--medium {
          width: 100%;
        }
        ::v-deep .el-form-item {
          margin-bottom: 20px;
        }
        ::v-deep .el-textarea__inner {
          resize: none;
        }
      }
    }
  }
  .contract-record {
    width: 100%;
  }
  .contract-remind {
    height: calc(100% - 120px);
  }
  .contract-list {
    width: 100%;
    height: calc(100% - 44px);
    ::v-deep .el-button--medium {
      font-size: 13px;
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
::v-deep .el-form-item__label {
  color: #606266;
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
::v-deep.btn-box {
  display: flex;
  justify-content: flex-end;
  .upload-box {
    text-align: right;
  }
}
</style>
