<!-- 查看发票详情弹窗 -->
<template>
  <div>
    <el-dialog
      :title="config.title"
      :visible.sync="dialogVisible"
      width="540px"
      :close-on-click-modal="false"
      :append-to-body="true"
      :before-close="handleClose"
    >
      <el-form ref="form" :model="rules" :rules="rule" :label-width="labelWidth + 'px'" class="mt15">
        <el-form-item prop="remark" v-if="rules.status == 2">
          <span slot="label">拒绝理由：</span>
          <el-input v-model="rules.remark" type="textarea"></el-input>
        </el-form-item>

        <div v-if="rules.status == 1">
          <el-form-item prop="invoice_type">
            <span slot="label">发送方式：</span>
            <el-select v-model="rules.invoice_type" placeholder="请选择发送方式">
              <el-option label="邮箱" value="mail"></el-option>
              <el-option label="快递" value="express"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item prop="collect_name" v-if="rules.invoice_type !== 'mail'">
            <span slot="label">联系人：</span>
            <el-input v-model="rules.collect_name" placeholder="请输入联系人"></el-input>
          </el-form-item>
          <el-form-item prop="collect_tel" v-if="rules.invoice_type !== 'mail'">
            <span slot="label">联系电话：</span>
            <el-input v-model="rules.collect_tel" placeholder="请输入联系电话"></el-input>
          </el-form-item>
          <el-form-item :prop="propType">
            <span slot="label">{{ rules.invoice_type == 'mail' ? '邮箱地址' : '邮寄地址' }}：</span>
            <el-input
              v-if="rules.invoice_type == 'mail'"
              v-model="rules.invoice_mail"
              placeholder="请输入邮箱地址"
            ></el-input>
            <el-input v-else v-model="rules.invoice_address" placeholder="请输入邮寄地址"></el-input>
          </el-form-item>
          <el-form-item>
            <span slot="label">备注：</span>
            <el-input v-model="rules.remark" type="textarea"></el-input>
          </el-form-item>
          <el-form-item>
            <span slot="label">开票凭证：</span>
            <div class="avatar">
              <el-upload
                class="upload-demo mr10 mb15"
                action="##"
                :show-file-list="false"
                :headers="myHeaders"
                :http-request="uploadServerLog"
              >
                <img class="img" :src="imageUrl" v-if="imageUrl" />
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>

              <span class="clew"> 支持jpg、jpeg、png <br />建议734*1034 <br />大小不超过2M </span>
            </div>
          </el-form-item>
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="handleClose">{{ $t('public.cancel') }}</el-button>
        <el-button size="small" type="primary" :loading="loading" @click="handleConfirm">{{
          $t('public.ok')
        }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { clientInvoiceStatusApi } from '@/api/enterprise'
import { getToken } from '@/utils/auth'
import { uploader } from '@/utils/uploadCloud'
export default {
  name: '',

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
      labelWidth: 100,
      imageUrl: '',
      uploadData: {},
      uploadSize: 5,
      myHeaders: {
        authorization: 'Bearer ' + getToken()
      },
      rules: {
        status: 1,
        invoice_type: 'mail',
        invoice_address: '',
        invoice_mail: '',
        collect_name: '',
        remark: '',
        collect_tel: '',
        file: ''
      },
      rule: {
        status: [{ required: true, message: '请选择开票结果', trigger: 'change' }],
        invoice_type: [{ required: true, message: '请选择发送方式', trigger: 'change' }],
        invoice_address: [{ required: true, message: '请输入邮寄地址', trigger: 'blur' }],
        invoice_mail: [{ required: true, message: '请输入邮箱地址', trigger: 'blur' }],
        collect_name: [{ required: true, message: '请输入联系人', trigger: 'blur' }],
        collect_tel: [{ required: true, message: '请输入联系电话', trigger: 'blur' }],
        remark: [{ required: true, message: '请输入拒绝理由', trigger: 'blur' }]
      },
      loading: false
    }
  },
  computed: {
    propType() {
      if (this.rules.invoice_type == 'mail') {
        return 'invoice_mail'
      } else {
        return 'invoice_address'
      }
    }
  },
  watch: {
    config: {
      handler(nVal) {
        this.rules.invoice_type = nVal.data.collect_type
        if (this.rules.invoice_type == 'mail') {
          this.rules.invoice_mail = nVal.data.collect_email
        } else {
          this.rules.invoice_address = nVal.data.mail_address
        }

        this.rules.collect_tel = nVal.data.collect_tel
        this.rules.collect_name = nVal.data.collect_name
      }
    }
  },
  created() {},
  mounted() {},
  methods: {
    openBox() {
      this.dialogVisible = true
    },
    handleConfirm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          if (this.rules.invoice_type == 'mail') {
            this.rules.invoice_address = this.rules.invoice_mail
          }
          this.loading = true
          clientInvoiceStatusApi(this.config.data.id, this.rules)
            .then((res) => {
              this.loading = false
              this.$emit('isOk')
              this.handleClose()
            })
            .then((err) => {
              this.loading = false
            })
        }
      })
    },
    // 上传文件方法
    uploadServerLog(params) {
      const file = params.file
      let options = {
        way: 2,
        relation_type: 'invoice',
        relation_id: this.config.data.id,
        eid: 0
      }
      uploader(file, 1, options)
        .then((res) => {
          // 获取上传文件渲染页面
          if (res.data.name) {
            this.imageUrl = res.data.url
            this.rules.file = res.data.attach_id
            this.rules.attach_ids = [res.data.attach_id]
          }
        })
        .catch((err) => {})
    },

    handleClose() {
      this.dialogVisible = false
      this.rules = {
        status: 1,
        invoice_type: 'mail',
        collect_email: '',
        remark: '',
        collect_tel: '',
        collect_name: '',
        invoice_address: '',
        invoice_mail: '',
        file: ''
      }
      this.imageUrl = ''
    }
  }
}
</script>
<style scoped lang="scss">
.line {
  width: 100%;
  height: 4px;
  border-bottom: 1px solid #e6ebf5;
  margin-bottom: 20px;
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

.img {
  width: 75px;
  height: 95px;
}
.avatar {
  display: flex;
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
