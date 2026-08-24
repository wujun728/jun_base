<!-- 通过付款审核记录弹窗 -->
<template>
  <div>
    <el-dialog
      :append-to-body="true"
      :before-close="handleClose"
      :title="config.title"
      :visible.sync="dialogVisible"
      width="540px"
    >
      <el-form ref="form" :label-width="labelWidth + 'px'" :model="rules" :rules="rule" class="mt15">
        <div class="alert">
          <el-alert
            :description="
              this.config.type == 'edit'
                ? '保存后，收支记账与对应合同账目记录也会同步修改！'
                : '审核通过后，系统自动生成收支记账流水信息！'
            "
            class="cr-alert"
            show-icon
            type="info"
          >
          </el-alert>
        </div>
        <!-- 账目分类 -->
        <el-form-item v-if="config.type !== 'edit'" prop="bill_cate_id">
          <span slot="label">账目分类：</span>
          <el-cascader
            v-model="rules.bill_cate_id"
            :options="statusOption"
            :props="{ checkStrictly: true, label: 'name', value: 'id', emitPath: false }"
            @change="handleChange"
          ></el-cascader>
        </el-form-item>
        <!-- 续费类型 -->
        <el-form-item v-if="types !== 0" prop="cate_id">
          <span slot="label">续费类型： </span>
          <el-select v-model="rules.cate_id" :placeholder="$t('customer.placeholder31')" size="small">
            <el-option v-for="item in sourceOptions" :key="item.id" :label="item.value.title" :value="item.id" />
          </el-select>
        </el-form-item>

        <!-- 回款金额 -->
        <el-form-item prop="amount">
          <span slot="label">{{ title01 }}：</span>
          <el-input-number
            v-model="rules.amount"
            :controls="false"
            :max="999999.99"
            :placeholder="placeholder01"
            size="small"
          ></el-input-number>
        </el-form-item>

        <!-- 续费结束时间 -->
        <el-form-item v-if="types == 1">
          <span slot="label">续费结束日期：</span>
          <el-date-picker
            v-model="rules.end_date"
            :picker-options="config.type > 2 ? expireTimeOption : ''"
            :type="'date'"
            placeholder="请选择续费结束日期"
            size="small"
          >
          </el-date-picker>
        </el-form-item>
        <!-- 支付方式 -->
        <el-form-item v-if="config.type !== 3" prop="type_id">
          <span slot="label">支付方式：</span>
          <el-select v-model="rules.type_id" placeholder="请选择支付方式" size="small">
            <el-option v-for="item in paymentOptions" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>

        <!-- 付款时间 -->
        <el-form-item v-if="config.type !== 3" prop="dateTime">
          <span slot="label">付款时间：</span>
          <el-date-picker
            v-model="rules.dateTime"
            format="yyyy/MM/dd HH:mm:ss"
            picker-options="expireTimeOption"
            placeholder="请选择付款时间"
            size="small"
            type="datetime"
          >
          </el-date-picker>
        </el-form-item>

        <!-- 付款凭证 -->
        <el-form-item v-if="config.type !== 3">
          <span slot="label">付款凭证：</span>
          <div class="avatar">
            <el-upload
              :headers="myHeaders"
              :http-request="uploadServerLog"
              :show-file-list="false"
              action="##"
              class="upload-demo mr10"
            >
              <img v-if="imageUrl" :src="imageUrl" class="img" />
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>

            <span class="clew">
              支持jpg、jpeg、png<br />
              建议734*1034 <br />大小不超过2M
            </span>
          </div>
        </el-form-item>
        <el-form-item prop="remarks">
          <span slot="label"> 备注</span>
          <el-input v-model="rules.remarks" maxlength="50" show-word-limit type="textarea" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="handleClose">{{ $t('public.cancel') }}</el-button>
        <el-button size="small" type="primary" @click="handleConfirm">{{ $t('public.ok') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { enterprisePayTypeApi, clientConfigListApi, clientBillStatusApi, billFinanceApi } from '@/api/enterprise'
import { getToken } from '@/utils/auth'
import { uploader } from '@/utils/uploadCloud'
import SettingMer from '@/libs/settingMer'
export default {
  name: '',
  components: {},
  props: {
    statusOption: {
      type: Array
    },
    config: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  data() {
    const checkAmount = (rule, value, callback) => {
      if (this.types == 0 && !value) {
        return callback(new Error(this.$t('customer.placeholder33')))
      } else if (this.types == 1 && !value) {
        return callback(new Error(this.$t('customer.placeholder35')))
      } else {
        callback()
      }
    }
    const checkDateTime = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('请选择付款时间'))
      } else {
        callback()
      }
    }
    const checkType = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('请选择续费类型'))
      } else {
        callback()
      }
    }
    return {
      dialogVisible: false,
      labelWidth: 120,
      uploadSize: 2,
      title01: '回款金额(元)',
      title02: '续费结束日期',
      placeholder01: '请输入回款金额',
      paymentOptions: [],
      sourceOptions: [],
      uploadData: {},
      loading: false,
      imageUrl: '',
      myHeaders: {
        authorization: 'Bearer ' + getToken()
      },

      expireTimeOption: {
        disabledDate(date) {
          return date.getTime() <= Date.now() - 8.64e7
        }
      },
      types: 0,

      rules: {
        amount: undefined,
        dateTime: '',
        remarks: '',
        cate_id: '', // 续费类型
        attach_ids: '',
        bill_cate_id: [], // 账目分类
        end_date: '',
        type_id: ''
      },
      rule: {
        amount: [{ required: true, validator: checkAmount, trigger: 'blur' }],
        dateTime: [{ required: true, validator: checkDateTime, trigger: 'change' }],
        type: [{ required: true, validator: checkType, trigger: 'change' }],
        cate_id: [{ required: true, message: '请选择续费类型', trigger: 'change' }],
        bill_cate_id: [{ required: true, message: '请选择账目分类', trigger: 'blur' }],
        type_id: { required: true, message: '请选择支付方式', trigger: 'change' } // 支付方式
      }
    }
  },
  computed: {
    fileUrl() {
      return SettingMer.https + `/system/attach/upload`
    }
  },
  watch: {
    config: {
      handler(nVal) {
        this.rules.bill_cate_id = nVal.path ? nVal.path : []

        if (nVal.data.renew) {
          this.rules.cate_id = nVal.data.renew.id
        }
        this.types = nVal.data.types
        if (JSON.stringify(nVal.data.attachs) !== '[]') {
          this.imageUrl = nVal.data.attachs[0].src
          this.rules.attach_ids = nVal.data.attachs[0].id
        }
        this.rules.amount = nVal.data.num
        this.rules.type_id = nVal.data.type_id
        this.rules.remarks = nVal.data.mark

        this.rules.dateTime = this.$moment(nVal.data.date).format('YYYY/MM/DD HH:mm:ss')

        if (nVal.data.end_date == '0000-00-00') {
          this.rules.end_date = ''
        } else {
          this.rules.end_date = nVal.data.end_date
        }

        if (this.types == 0) {
          this.title01 = '回款金额(元)'
        } else {
          this.title01 = '续费金额(元)'
          this.placeholder01 = '请输入续费金额'
        }
      }
    }
  },
  created() {},
  mounted() {},
  methods: {
    handleOpen() {
      this.dialogVisible = true
      this.getPaymentMethod()
      this.getClientList()
    },
    // 选择账目分类
    handleChange(val) {},
    // 获取支付方式
    getPaymentMethod() {
      let data = {
        status: 1
      }
      enterprisePayTypeApi(data).then((res) => {
        this.paymentOptions = res.data.list
      })
    },

    // 列表
    async getClientList() {
      const key = 'renew'
      const result = await clientConfigListApi({ key })
      this.sourceOptions = result.data === undefined ? [] : result.data
    },

    handleConfirm() {
      let data = {}
      if (this.rules.bill_cate_id.length > 0) {
        this.rules.bill_cate_id = this.rules.bill_cate_id.pop()
      }
      this.$refs.form.validate((valid) => {
        if (valid) {
          data = {
            cid: this.config.data.cid,
            eid: this.config.data.eid,
            types: this.config.data.types,
            num: this.rules.amount,
            mark: this.rules.remarks,
            date: this.$moment(this.rules.dateTime).format('YYYY-MM-DD HH:mm:ss'),
            end_date: this.rules.end_date,
            type_id: this.rules.type_id,
            cate_id: this.rules.cate_id,
            bill_cate_id: this.rules.bill_cate_id,
            attach_ids: this.rules.attach_ids
          }

          if (this.config.type == 'edit') {
            this.clientContractEdit(this.config.data.id, data)
          } else {
            ;(data.status = 1), this.clientContractPass(this.config.data.id, data)
          }
        }
      })
    },

    // 通过付款与续费
    clientContractPass(id, data) {
      this.loading = true
      clientBillStatusApi(id, data)
        .then((res) => {
          this.loading = false
          this.handleClose()
          this.reset()
          this.$emit('isOk')
        })
        .catch((error) => {
          this.loading = false
        })
    },

    // 编辑付款与续费
    async clientContractEdit(id, data) {
      await billFinanceApi(id, data)
      this.handleClose()
      this.reset()
      this.$emit('isOk')
    },

    handleClose() {
      this.dialogVisible = false
      this.imageUrl = ''
      if (this.config.edit === 2) {
        this.reset()
      }
      this.reset()
    },

    reset() {
      this.rules.amount = undefined
      this.rules.dateTime = ''
      this.rules.end_date = ''
      this.rules.remarks = ''
      this.rules.type = ''
      this.rules.day = undefined
      this.rules.period = 2
      this.rules.bill_cate_id = []
      this.imageUrl = ''
    },
    // 上传文件方法
    uploadServerLog(params) {
      this.percentShow = true
      const file = params.file
      let options = {
        way: 2,
        relation_type: 'bill',
        relation_id: 0,
        eid: 0
      }
      uploader(file, 1, options)
        .then((res) => {
          // 获取上传文件渲染页面
          if (res.name) {
            this.imageUrl = res.url

            this.rules.attach_ids = res.attach_id
          }
        })
        .catch((err) => {
          this.percentShow = false
        })
    }
  }
}
</script>
<style lang="scss" scoped>
.img {
  width: 75px;
  height: 95px;
}
::v-deep .el-cascader {
  width: 100%;
}
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
}
</style>
