<!-- 添加付款记录/付款提醒弹窗页面 以config.type来区分 type=1:回款记录和续费记录/ type=3 回款提醒和续费提醒 -->
<template>
  <el-dialog
    :append-to-body="true"
    :before-close="handleClose"
    :close-on-click-modal="false"
    :title="config.title"
    :visible.sync="dialogVisible"
    width="540px"
  >
    <el-form ref="form" :label-width="labelWidth + 'px'" :model="rules" :rules="rule" class="mt15">
      <el-form-item v-if="config.type == 1 && config.formType" prop="cid">
        <span slot="label">选择合同： </span>
        <el-select v-model="rules.cid" placeholder="请选择合同" size="small">
          <el-option v-for="item in contractData" :key="item.id" :label="item.title" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item prop="radio">
        <span slot="label">{{ config.type !== 3 ? '业务类型' : '提醒类型' }}： </span>
        <el-radio-group v-model="rules.radio" @change="radioChange">
          <el-radio :label="1"> {{ config.type !== 3 ? '回款' : '回款提醒' }}</el-radio>
          <el-radio :label="2"> {{ config.type !== 3 ? '续费' : '续费提醒' }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item prop="amount">
        <span slot="label">{{ title01 }}：</span>
        <el-input-number
          v-model="rules.amount"
          :controls="false"
          :min="0"
          :placeholder="placeholder01"
          :precision="2"
          size="small"
        ></el-input-number>
      </el-form-item>
      <!-- 续费结束时间 -->
      <el-form-item v-if="rules.radio == 2 || config.type == 3" :prop="mandatory">
        <span slot="label">{{ title02 }}：</span>
        <el-date-picker
          v-model="rules.endDate"
          :default-time="config.type == 3 ? '09:00:00' : ''"
          :placeholder="placeholder02"
          :type="config.type == 3 ? 'datetime' : 'date'"
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
          :picker-options="pickerOptions"
          :placeholder="placeholder02"
          picker-options="expireTimeOption"
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
            class="mr10 upload-demo"
          >
            <img v-if="imageUrl" :src="imageUrl" class="img" />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
          <p class="clew">支持jpg、jpeg、png <br />建议734*1034 <br />大小不超过2M</p>
        </div>
      </el-form-item>

      <el-form-item :prop="config.type == 3 ? 'remarks' : ''">
        <span slot="label">{{ config.type !== 3 ? '备注' : '提醒内容' }}：</span>
        <el-input v-model="rules.remarks" maxlength="255" show-word-limit type="textarea" />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button size="small" @click="handleClose">{{ $t('public.cancel') }}</el-button>
      <el-button :loading="loading" size="small" type="primary" @click="handleConfirm">{{ $t('public.ok') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import {
  clientBillEditApi,
  clientBillSaveApi,
  clientConfigListApi,
  selectContractListApi,
  clientFollowEditApi,
  clientFollowSaveApi,
  clientRemindEditApi,
  clientRemindSaveApi,
  enterprisePayTypeApi
} from '@/api/enterprise'
import { getDictDataListApi } from '@/api/form'
import { getToken } from '@/utils/auth'
import SettingMer from '@/libs/settingMer'
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
    const checkAmount = (rule, value, callback) => {
      if (this.rules.radio == 1 && !value) {
        return callback(new Error(this.$t('customer.placeholder33')))
      } else if (this.rules.radio == 2 && !value) {
        return callback(new Error(this.$t('customer.placeholder35')))
      } else if (this.config.type === 3 && !value) {
        return callback(new Error(this.$t('customer.placeholder37')))
      } else if (this.config.type === 4 && !value) {
        return callback(new Error(this.$t('customer.placeholder35')))
      } else {
        callback()
      }
    }
    const checkDateTime = (rule, value, callback) => {
      if (this.rules.radio == 1 && !value) {
        if (this.config.type == 3) {
          return callback(new Error(this.$t('customer.placeholder38')))
        } else {
          return callback(new Error(this.$t('customer.placeholder34')))
        }
      } else if (this.rules.radio == 2 && !value) {
        return callback(new Error(this.$t('customer.placeholder36')))
      } else if (this.config.type === 4 && !value) {
        return callback(new Error(this.$t('customer.placeholder39')))
      } else if (this.config.type === 5 && !value) {
        return callback(new Error(this.$t('customer.placeholder41')))
      } else {
        callback()
      }
    }
    const checkType = (rule, value, callback) => {
      if ((this.rules.radio == 2 || this.config.type === 4) && !value) {
        return callback(new Error(this.$t('customer.placeholder31')))
      } else {
        callback()
      }
    }

    const checkRemark = (rule, value, callback) => {
      if (!value) {
        return callback(new Error(this.$t('customer.placeholder40')))
      } else {
        callback()
      }
    }

    const checkContent = (rule, value, callback) => {
      if (this.config.type === 5 && !value) {
        return callback(new Error(this.$t('customer.placeholder40')))
      } else {
        callback()
      }
    }
    return {
      dialogVisible: false,
      remind_id: '',
      rules: {
        cid: '',
        amount: undefined,
        dateTime: '',
        remarks: '',
        type: '',
        rate: undefined,
        content: '',
        attach_ids: '',
        endDate: '',
        type_id: '',
        radio: 1
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
        amount: [{ required: true, validator: checkAmount, trigger: 'blur' }],
        dateTime: [{ required: true, validator: checkDateTime, trigger: 'change' }],
        type: [{ required: true, validator: checkType, trigger: 'change' }],
        type_id: { required: true, message: '请选择支付方式', trigger: 'change' }, // 支付方式
        remarks: [{ required: true, validator: checkRemark, trigger: 'blur' }],
        content: [{ required: true, validator: checkContent, trigger: 'blur' }],
        radio: [{ required: true, message: '请选择业务类型', trigger: 'change' }],
        endDate: [{ required: true, message: '请选择提醒时间', trigger: 'change' }]
      },
      title01: '回款金额(元)',
      title02: '',
      placeholder01: '请输入回款金额',
      placeholder02: this.$t('customer.placeholder33'),
      labelWidth: 120,
      payText: '',
      sourceOptions: [],
      paymentOptions: [],
      typeOptions: [
        { value: 0, label: this.$t('access.day') },
        { value: 1, label: this.$t('user.work.week') },
        { value: 2, label: this.$t('user.work.month') },
        { value: 3, label: this.$t('calendar.year') }
      ],
      loading: false,
      pickerOptions: {
        shortcuts: [
          {
            text: '今天',
            onClick(picker) {
              picker.$emit('pick', new Date())
            }
          },
          {
            text: '昨天',
            onClick(picker) {
              const date = new Date()
              date.setTime(date.getTime() - 3600 * 1000 * 24)
              picker.$emit('pick', date)
            }
          },
          {
            text: '前天',
            onClick(picker) {
              const date = new Date()
              date.setTime(date.getTime() - 3600 * 1000 * 24 * 2)
              picker.$emit('pick', date)
            }
          }
        ]
      },
      contractData: []
    }
  },
  watch: {
    config: {
      handler(nVal) {
        if (nVal.cid) {
          this.rules.cid = nVal.cid
        }

        if (nVal.type == 1) {
          if (nVal.data.types == 0) {
            this.rules.radio = 1
          } else if (nVal.data.types == 1) {
            this.rules.radio = 2
            this.title01 = '续费金额(元)'
            this.title02 = '续费结束日期'
            this.placeholder02 = this.$t('customer.placeholder36')
          } else {
            this.rules.radio = 1
            this.title01 = '回款金额(元)'
          }
          if (nVal.data.renew) {
            this.rules.type = nVal.data.renew.id
          } else {
            this.rules.type = nVal.data.cate_id
          }
          this.rules.amount = nVal.data.num || undefined
          this.remind_id = nVal.data.id || ''
        }

        if (nVal.type == 2) {
          if (nVal.data.types == 0) {
            this.rules.radio = 1
          } else {
            this.rules.radio = 2
          }
          if (nVal.data.renew) {
            this.rules.type = nVal.data.renew.id
          }
          if (JSON.stringify(nVal.data.attachs) !== '[]') {
            this.imageUrl = nVal.data.attachs[0].src || ''
          }

          this.rules.amount = nVal.data.num
          this.rules.type_id = nVal.data.type_id
          this.rules.remarks = nVal.data.mark
          this.rules.dateTime = nVal.data.date

          if (nVal.data.end_date == '0000-00-00') {
            this.rules.endDate = ''
          } else {
            this.rules.endDate = nVal.data.end_date
          }
          this.title01 = '续费金额(元)'
          this.title02 = '续费结束日期'
          this.placeholder01 = this.$t('customer.placeholder35')
          this.placeholder02 = this.$t('customer.placeholder36')
        }
        if (nVal.type == 3) {
          this.rules.amount = nVal.data.num
          if (nVal.data.renew) {
            this.rules.type = nVal.data.renew.id
          }
          if (nVal.data.types == 1) {
            this.rules.radio = 2
            this.title02 = '续费提醒时间' // 回款提醒日期
            this.placeholder02 = '请选择续费提醒时间'
            this.rules.endDate = nVal.data.time
            this.title01 = '续费金额(元)'
            this.placeholder01 = '请输入续费金额'
          } else {
            this.rules.radio = 1
            this.title02 = '回款提醒日期' // 回款提醒日期
            this.placeholder02 = this.$t('customer.placeholder38')
            this.rules.endDate = nVal.data.time
            this.title01 = this.$t('customer.collectionamount01') // 预计回款金额
            this.placeholder01 = this.$t('customer.placeholder37') //
          }
          this.rules.remarks = nVal.data.mark
        }
      },
      deep: true
    },
    lang() {
      this.setOptions()
    }
  },
  computed: {
    lang: function () {
      return this.$store.getters.lang
    },

    fileUrl() {
      return SettingMer.https + `/system/attach/upload`
    },
    mandatory() {
      if (this.config.type == 3) {
        return 'endDate'
      } else {
        return ''
      }
    },

    paymentJudgment: function () {
      let arrPay = []
      this.paymentOptions.map((item) => {
        arrPay.push(item.id)
      })
      return arrPay.indexOf(this.config.data.type_id)
    }
  },

  methods: {
    setOptions() {
      this.typeOptions = [
        { value: 0, label: this.$t('access.day') },
        { value: 1, label: this.$t('user.work.week') },
        { value: 2, label: this.$t('user.work.month') },
        { value: 3, label: this.$t('calendar.year') }
      ]
    },

    getTableData() {
      selectContractListApi({ data: [this.config.eid] }).then((res) => {
        this.contractData = res.data
        if (this.contractData.length > 0) {
          if (this.config.type == 1 && this.config.formType) {
            this.rules.cid = this.contractData[0].id
          }
        }
      })
    },

    radioChange(e) {
      this.rules.radio = e
      if (e == 1) {
        if (this.config.type == 3) {
          this.title01 = '预计回款金额'
          this.title02 = '回款提醒日期'
        } else {
          this.title01 = '回款金额(元)'
          this.title02 = '回款提醒日期'
        }
        this.placeholder02 = this.$t('customer.placeholder34')
        this.placeholder01 = this.$t('customer.placeholder33')
      } else if (e == 2) {
        if (this.config.type == 3) {
          this.title01 = this.$t('customer.renewalamount')
          this.placeholder01 = this.$t('customer.placeholder35')
          this.title02 = this.$t('customer.renewaldate01')
          this.placeholder02 = this.$t('customer.placeholder39')
        } else {
          this.title01 = '续费金额(元)'
          this.title02 = '续费结束日期'
          this.placeholder01 = this.$t('customer.placeholder35')
          this.placeholder02 = this.$t('customer.placeholder36')
        }
      }
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
        .catch((err) => {})
    },

    // 获取当前时间
    getTime() {
      this.rules.dateTime = this.$moment().format('YYYY-MM-DD HH:mm:ss')
    },

    // 列表
    async getClientList() {
      let obj = {
        types: 'client_renew',
        level: 1
      }
      const result = await getDictDataListApi(obj)
      this.sourceOptions = result.data.list === undefined ? [] : result.data.list
    },
    handleOpen() {
      this.dialogVisible = true
      this.getTime()
      this.getPaymentMethod()
      // this.getClientList()
      if (this.config.type == 1 && this.config.formType) {
        this.getTableData()
      }
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

    // 提交回款/续费/付款提醒
    handleConfirm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          // 添加回款
          var data = {}
          if (this.rules.radio == 1 && this.config.type !== 3) {
            data = {
              cid: this.config.cid,
              eid: this.config.eid,
              types: 0,
              cate_id: 0,
              num: this.rules.amount,
              mark: this.rules.remarks,
              date: this.$moment(this.rules.dateTime).format('YYYY-MM-DD HH:mm:ss'),
              end_date: this.rules.endDate ? this.$moment(this.rules.endDate).format('YYYY-MM-DD ') : '',
              type_id: this.rules.type_id,
              attach_ids: this.rules.attach_ids,
              bill_types: 1,
              remind_id: this.remind_id ? this.remind_id : this.config.cid
            }
            if (this.config.formType && this.config.formType === 'list') {
              data.cid = this.rules.cid
            }
            if (this.config.title == '编辑付款') {
              this.clientContractEdit(this.config.data.id, data)
            } else {
              this.clientContractSave(data)
            }
          } else if (this.rules.radio == 2 && this.config.type !== 3) {
            // 添加续费
            data = {
              cid: this.config.cid,
              eid: this.config.eid,
              types: 1,
              num: this.rules.amount,
              cate_id: this.rules.type,
              mark: this.rules.remarks,
              date: this.$moment(this.rules.dateTime).format('YYYY-MM-DD HH:mm:ss'),
              end_date: this.rules.endDate ? this.$moment(this.rules.endDate).format('YYYY-MM-DD ') : '',
              type_id: this.rules.type_id,
              attach_ids: this.rules.attach_ids,
              remind_id: this.remind_id
            }
            if (this.config.formType && this.config.formType === 'list') {
              data.cid = this.rules.cid
            }
            if (this.config.title == '编辑付款') {
              this.clientContractEdit(this.config.data.id, data)
            } else {
              this.clientContractSave(data)
            }
          } else if (this.config.type === 5) {
            data = {
              eid: this.config.eid,
              types: 1,
              time: this.$moment(this.rules.dateTime).format('YYYY-MM-DD HH:mm:ss'),
              content: this.rules.content
            }
            this.config.edit === 1 ? this.clientFollowSave(data) : this.clientFollowEdit(this.config.data.id, data)
          } else if (this.config.type == 3 && this.rules.radio == 1) {
            // 付款提醒
            data = {
              cid: this.config.cid,
              eid: this.config.eid,
              num: this.rules.amount,
              types: 0,
              mark: this.rules.remarks,
              time: this.$moment(this.rules.endDate).format('YYYY-MM-DD HH:mm:ss')
            }
            if (this.config.title == '编辑付款提醒') {
              this.clientRemindEdit(this.config.data.id, data)
            } else {
              this.clientRemindSave(data)
            }
          } else if (this.config.type == 3 && this.rules.radio == 2) {
            // 续费提醒
            data = {
              cid: this.config.cid,
              eid: this.config.eid,
              num: this.rules.amount,
              types: 1,
              mark: this.rules.remarks,
              time: this.$moment(this.rules.endDate).format('YYYY-MM-DD HH:mm:ss'),
              cate_id: this.rules.type
            }
            if (this.config.title == '编辑付款提醒') {
              this.clientRemindEdit(this.config.data.id, data)
            } else {
              this.clientRemindSave(data)
            }
          }
        }
      })
    },

    reset() {
      this.rules.amount = undefined
      this.rules.dateTime = ''
      this.rules.endDate = ''
      this.rules.remarks = ''
      this.rules.type = ''
      this.rules.radio = 1
      this.rules.type_id = ''
      this.rules.day = undefined
      // this.rules.period = 2;
      this.$refs.form.resetFields()
    },

    // 保存付款与续费
    clientContractSave(data) {
      this.loading = true
      clientBillSaveApi(data)
        .then((res) => {
          this.loading = false
          this.$emit('isOk')

          setTimeout(() => {
            this.handleClose()
            this.reset()
          }, 200)
        })
        .catch((error) => {
          this.loading = false
        })
    },
    // 修改付款与续费
    clientContractEdit(id, data) {
      this.loading = true
      clientBillEditApi(id, data)
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
    // 跟进记录--客户提醒与跟进详情
    clientFollowSave(data) {
      this.loading = true
      clientFollowSaveApi(data)
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
    // 跟进记录--修改客户提醒与跟进详情
    clientFollowEdit(id, data) {
      this.loading = true
      clientFollowEditApi(id, data)
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

    // 获取支付方式
    async getPaymentMethod() {
      let data = {
        status: 1
      }
      const result = await enterprisePayTypeApi(data)
      this.paymentOptions = result.data.list
    },
    // 保存付款提醒
    clientRemindSave(data) {
      this.loading = true
      clientRemindSaveApi(data)
        .then((res) => {
          this.loading = false
          this.$emit('isOk')

          setTimeout(() => {
            this.handleClose()
            this.reset()
          }, 200)
        })
        .catch((error) => {
          this.loading = false
        })
    },
    // 编辑付款提醒
    clientRemindEdit(id, data) {
      this.loading = true
      clientRemindEditApi(id, data)
        .then((res) => {
          this.loading = false
          this.handleClose()
          this.reset()
          this.$emit('isOk')
        })
        .catch((error) => {
          this.loading = false
        })
    }
  }
}
</script>

<style lang="scss" scoped>
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
::v-deep .el-dialog__footer {
  padding: 20px 0;
}
.dialog-footer {
  padding-top: 20px;
  padding-right: 20px;

  border-top: 1px solid #e6ebf5;
}
</style>
