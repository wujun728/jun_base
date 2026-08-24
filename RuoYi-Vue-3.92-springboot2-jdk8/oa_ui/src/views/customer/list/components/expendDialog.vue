<!-- 客户-账目记录-添加/编辑弹窗支出页面 -->
<template>
  <el-dialog
    :title="config.title"
    :visible.sync="dialogVisible"
    width="540px"
    :append-to-body="true"
    :before-close="handleClose"
    :close-on-click-modal="false"
  >
    <div class="alert mt20" v-if="this.config.source == 'fd'">
      <el-alert
        class="cr-alert"
        type="info"
        :description="
          this.config.type == 'edit'
            ? '保存后，收支记账与对应合同账目记录也会同步修改！'
            : '审核通过后，系统自动生成收支记账流水信息！'
        "
        show-icon
      >
      </el-alert>
    </div>
    <el-form ref="form" :model="rules" :rules="rule" :label-width="labelWidth + 'px'" class="mt15">
      <el-form-item prop="cid" v-if="config.formType == 'list'">
        <span slot="label">选择合同： </span>
        <el-select size="small" v-model="rules.cid" placeholder="请选择合同">
          <el-option v-for="item in contractData" :key="item.id" :label="item.title" :value="item.id" />
        </el-select>
      </el-form-item>
      <!-- 支付方式 -->
      <el-form-item prop="bill_cate_id">
        <span slot="label">支出类型：</span>
        <el-cascader
          v-model="rules.bill_cate_id"
          :options="billList"
          style="width: 100%"
          :props="{ checkStrictly: true, value: 'id', label: 'name', emitPath: false }"
        ></el-cascader>
      </el-form-item>

      <el-form-item prop="num">
        <span slot="label">支出金额(元)：</span>
        <el-input-number
          size="small"
          v-model="rules.num"
          :controls="false"
          :min="0"
          :precision="2"
          placeholder="请填写支出金额"
        ></el-input-number>
      </el-form-item>

      <!-- 支付方式 -->
      <el-form-item prop="type_id">
        <span slot="label">支付方式：</span>
        <el-select size="small" v-model="rules.type_id" placeholder="请选择支付方式">
          <el-option v-for="item in paymentOptions" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>

      <!-- 支出时间 -->
      <el-form-item prop="date">
        <span slot="label">支出时间：</span>
        <el-date-picker v-model="rules.date" size="small" picker-options="expireTimeOption" type="datetime">
        </el-date-picker>
      </el-form-item>

      <!-- 付款凭证 -->
      <el-form-item>
        <span slot="label">支出凭证：</span>
        <div class="avatar">
          <el-upload
            class="upload-demo mr10"
            action="##"
            :show-file-list="false"
            :headers="myHeaders"
            :http-request="uploadServerLog"
          >
            <img class="img" :src="imageUrl" v-if="imageUrl" />

            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
          <p class="clew">支持jpg、jpeg、png <br />建议734*1034 <br />大小不超过2M</p>
        </div>
      </el-form-item>

      <el-form-item>
        <span slot="label">备注：</span>
        <el-input
          type="textarea"
          v-model="rules.mark"
          maxlength="255"
          placeholder="请输入备注信息，最多可输入255字"
          show-word-limit
        />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button size="small" @click="handleClose">{{ $t('public.cancel') }}</el-button>
      <el-button size="small" :loading="loading" type="primary" @click="handleConfirm">{{ $t('public.ok') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import {
  selectContractListApi,
  enterprisePayTypeApi,
  clientBillSaveApi,
  billCateApi,
  clientBillEditApi,
  billFinanceApi,
  clientBillStatusApi
} from '@/api/enterprise'
import { getToken } from '@/utils/auth'
import { uploader } from '@/utils/uploadCloud'

export default {
  name: 'ContractDialog',
  props: {
    config: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  data() {
    return {
      dialogVisible: false,
      id: '',
      rules: {
        cid: '',
        num: undefined,
        date: '',
        mark: '',
        bill_cate_id: '',
        attach_ids: '',
        type_id: '',
        types: 2
      },
      val: '',
      imageUrl: '',
      uploadSize: 2,
      uploadData: {},
      myHeaders: {
        authorization: 'Bearer ' + getToken()
      },
      expireTimeOption: {
        disabledDate(date) {
          return date.getTime() <= Date.now() - 8.64e7
        }
      },
      rule: {
        cid: [{ required: true, message: '请选择合同', trigger: 'change' }],
        num: [{ required: true, message: '请输入支出金额', trigger: 'blur' }],
        date: [{ required: true, message: '请选择时间', trigger: 'change' }],
        bill_cate_id: [{ required: true, message: '请选择支出类型', trigger: 'change' }],
        type_id: { required: true, message: '请选择支付方式', trigger: 'change' } // 支付方式
      },

      labelWidth: 120,
      paymentOptions: [],
      billList: [],
      loading: false,
      contractData: []
    }
  },
  watch: {
    config: {
      handler(nVal) {
        if (nVal.data) {
          const { id, cid, eid, num, date, type_id, mark, bill_cate_id, attachs } = nVal.data;
          this.id = id;
          Object.assign(this.rules, { cid, eid, num, date, type_id, mark, bill_cate_id });
          if (attachs && attachs.length > 0) {
            const { id: attachId, src } = attachs[0];
            this.rules.attach_ids = attachId;
            this.imageUrl = src;
          }
        }
      }
    }
  },

  methods: {
    // 上传文件方法
    uploadServerLog(params) {
      const file = params.file
      let options = {
        way: 2,
        relation_type: 'bill',
        relation_id: '',
        eid: ''
      }
      uploader(file, 1, options)
        .then((res) => {
          // 获取上传文件渲染页面
          if (res.name) {
            this.imageUrl = res.url
            this.rules.attach_ids = res.attach_id
          }
        })
        .catch((err) => {})
    },

    // 获取当前时间
    getTime() {
      this.rules.date = this.$moment().format('YYYY-MM-DD HH:mm:ss')
    },

    // 获取支出分类
    getBillCate() {
      let data = {
        types: 0
      }
      billCateApi(data).then((res) => {
        this.billList = res.data
      })
    },

    getTableData() {
      selectContractListApi({ data: [this.config.eid] }).then((res) => {
        this.contractData = res.data
        if (this.contractData.length > 0) {
          this.rules.cid = this.contractData[0].id
        }
      })
    },
    handleConfirm() {
      if (!this.config.data) {
        if (this.config.formType !== 'list') {
          this.rules.cid = this.config.cid
        }
        this.rules.eid = this.config.eid
      }

      this.$refs.form.validate((valid) => {
        if (valid) {
          if (this.config.source == 'fd') {
            if (this.config.type == 'edit') {
              this.billFinance()
            } else {
              this.passFn()
            }
          } else {
            if (this.config.edit) {
              this.editFn()
            } else {
              this.saveFn()
            }
          }
        }
      })
    },
    passFn() {
      this.loading = true
      this.rules.status = 1
      clientBillStatusApi(this.id, this.rules)
        .then((res) => {
          this.loading = false
          this.$emit('isOk')
          this.dialogVisible = false
          this.reset()
        })
        .catch((err) => {
          this.loading = false
        })
    },

    editFn() {
      this.loading = true
      clientBillEditApi(this.id, this.rules)
        .then((res) => {
          this.loading = false
          this.dialogVisible = false
          this.$emit('isOk')
          this.reset()
        })
        .catch((err) => {
          this.loading = false
        })
    },

    // 财务编辑
    billFinance() {
      this.loading = true
      billFinanceApi(this.id, this.rules)
        .then((res) => {
          this.loading = false
          this.$emit('isOk')
          this.dialogVisible = false
          this.reset()
        })
        .catch((err) => {
          this.loading = false
        })
    },

    saveFn() {
      this.loading = true
      clientBillSaveApi(this.rules)
        .then((res) => {
          this.loading = false
          this.dialogVisible = false
          this.$emit('isOk')
          this.reset()
        })
        .catch((err) => {
          this.loading = false
        })
    },
    handleOpen() {
      this.dialogVisible = true

      this.getTime()
      this.getPaymentMethod()
      if (this.config.source !== 'fd') {
        setTimeout(() => {
          this.getTableData()
        }, 200)
      }

      this.getBillCate()
    },

    handleClose() {
      this.dialogVisible = false
      this.imageUrl = ''
      this.rules.endDate = ''
      ;(this.rules.type = ''), this.$refs.form.resetFields()
      if (this.config.edit === 2) {
        this.reset()
      }
      this.reset()
    },

    reset() {
      this.imageUrl = ''
      this.rules = {
        cid: '',
        num: undefined,
        date: '',
        mark: '',
        bill_cate_id: '',
        attach_ids: '',
        type_id: '',
        bill_types: 0,
        types: 2
      }
    },

    // 获取支付方式
    async getPaymentMethod() {
      let data = {
        status: 1
      }
      const result = await enterprisePayTypeApi(data)
      this.paymentOptions = result.data.list
    }
  }
}
</script>

<style scoped lang="scss">
::v-deep .el-alert {
  padding-left: 30px;
  border: 1px solid #1890ff;
  color: #1890ff;
  font-size: 13px;
  background-color: #edf7ff;
  line-height: 1;
  margin-bottom: 20px;
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
.img {
  display: block;

  width: 75px;
  height: 95px;
}
.avatar {
  box-sizing: border-box;
  display: flex;
  height: 97px;
}
.clew {
  display: inline-block;
  margin-top: 10px;
  width: 120px;
  font-size: 13px;
  color: #c0c4cc;
  line-height: 28px;
}
.avatar-uploader-icon {
  border: 1px solid #d9d9d9;
  font-size: 14px;
  color: #8c939d;
  width: 75px;
  height: 95px;
  line-height: 95px;
  text-align: center;
  cursor: pointer;
}
::v-deep .el-date-editor {
  width: 100%;
}
::v-deep .el-textarea__inner {
  resize: none;
}
::v-deep .el-input-number {
  width: 100%;
}
::v-deep .el-select {
  width: 100%;
}
::v-deep .el-form-item:last-of-type {
  margin-bottom: 0;
}
.dialog-footer {
  padding-top: 20px;
  border-top: 1px solid #e6ebf5;
}
</style>
