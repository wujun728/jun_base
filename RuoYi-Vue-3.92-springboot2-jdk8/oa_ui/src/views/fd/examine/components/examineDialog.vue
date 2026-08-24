<!-- 编辑付款记录弹窗 -->
<template>
  <el-dialog
    :title="config.title"
    :visible.sync="examineVisible"
    :width="config.width"
    :append-to-body="true"
    :before-close="handleClose"
  >
    <el-form class="mt15" label-width="100px">
      <el-form-item>
        <span slot="label"><span class="color-tab">*</span>{{ config.title }}：</span>
        <el-radio-group v-model="radio">
          <el-radio :label="1">{{ config.type === 3 ? $t('customer.invoiced') : $t('customer.passed') }}</el-radio>
          <el-radio :label="2">{{ config.type === 3 ? $t('customer.notinvoiced') : $t('customer.fail') }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <template v-if="config.type === 3 && radio === 1">
        <el-form-item>
          <span slot="label"><span class="color-tab">*</span>发票编号：</span>
          <el-input type="text" v-model="num" size="small" placeholder="请输入发票编号" />
        </el-form-item>
        <el-form-item>
          <span slot="label"><span class="color-tab">*</span>发送方式：</span>
          <el-select
            v-model="invoice_type"
            size="small"
            @change="handleMethod"
            :placeholder="$t('customer.placeholder54')"
          >
            <el-option v-for="(item, index) in methodOptions" :key="index" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <span slot="label"
            ><span class="color-tab">*</span>{{ invoice_type === 'express' ? '收货地址' : '接收邮箱' }}：</span
          >
          <el-input
            type="text"
            v-model="invoice_address"
            size="small"
            :placeholder="invoice_type === 'express' ? '请输入收件地址' : '请输入接受邮箱'"
          />
        </el-form-item>
      </template>

      <el-form-item>
        <span slot="label"><span v-if="radio === 2" class="color-tab">*</span>{{ $t('public.remarks') }}：</span>
        <el-input
          type="textarea"
          :rows="3"
          v-model="remarks"
          :maxlength="200"
          show-word-limit
          :placeholder="
            config.type === 3 && radio === 1 ? '用于快递单号/邮箱地址等相关开票信息' : $t('customer.placeholder18')
          "
        />
      </el-form-item>
      <el-form-item v-if="config.type === 3 && radio === 1">
        <span slot="label">{{ $t('customer.billingdocuments') }}:</span>
        <div class="examine-card">
          <ul class="el-upload-list el-upload-list--picture-card">
            <li class="el-upload-list__item" v-for="(item, index) in uploadList" :key="index">
              <i v-if="fileIsImage(item.type)" :class="getFileType(2, item.name)" class="icon iconfont upload-icon"></i>
              <el-image v-else :src="item.path"></el-image>
              <span class="el-upload-list__item-actions">
                <span
                  v-if="!fileIsImage(item.type)"
                  class="el-upload-list__item-preview"
                  @click="handlePictureCardPreview(item)"
                  ><i class="el-icon-zoom-in"></i
                ></span>
                <span class="el-upload-list__item-delete"
                  ><i @click="handleFileDelete(item, index)" class="el-icon-delete"></i
                ></span>
              </span>
            </li>
          </ul>

          <el-upload
            :action="fileUrl"
            list-type="picture-card"
            :on-success="handleSuccess"
            :headers="myHeaders"
            :show-file-list="false"
            :before-upload="handleUpload"
            :data="uploadData"
            multiple
          >
            <i slot="default" class="el-icon-plus"></i>
          </el-upload>
          <el-image-viewer v-if="isImage" :on-close="closeImageViewer" :url-list="srcList" />
        </div>
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
  clientBillStatusApi,
  clientFileDeleteApi,
  clientInvoiceStatusApi,
  putInvalid,
  clientInvoiceStatus
} from '@/api/enterprise'
import SettingMer from '@/libs/settingMer'
import ElImageViewer from 'element-ui/packages/image/src/image-viewer'
import { getToken } from '@/utils/auth'
import file from '@/utils/file'
import Vue from 'vue'
Vue.use(file)

export default {
  name: 'ExamineDialog',
  props: {
    config: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  components: {
    ElImageViewer
  },
  data() {
    return {
      remarks: '',
      radio: 1,
      isSend: 1,
      num: '',
      invoice_type: '',
      invoice_address: '',
      examineVisible: false,
      loading: false,
      title1: '',
      myHeaders: {
        authorization: 'Bearer ' + getToken()
      },
      methodOptions: [
        { value: 'express', label: this.$t('customer.express') },
        { value: 'mail', label: this.$t('customer.mail') }
      ],
      uploadData: {},
      uploadList: [],
      uploadSize: 5,
      disabled: false,
      isImage: false,
      srcList: []
    }
  },
  computed: {
    fileUrl() {
      return SettingMer.https + `/client/file/upload`
    }
  },
  watch: {
    config: {
      handler(nVal) {
        if (nVal.type === 1) {
          this.title1 = this.$t('customer.fundaudit')
        } else if (nVal.type === 2) {
          this.title1 = this.$t('customer.invoicereview')
        } else {
          if (nVal.data.num) {
            this.radio = 1
          } else {
            this.radio = 2
          }
          this.title1 = this.$t('customer.invoicingapproval')
        }
      },
      deep: true
    }
  },
  methods: {
    handleOpen() {
      this.examineVisible = true
    },
    handleClose() {
      this.examineVisible = false
      this.reset()
    },
    handleConfirm() {
      var file = []
      if (this.radio === 2 && !this.remarks) {
        this.$message.error(this.$t('customer.placeholder18'))
      } else if (this.config.type === 3 && this.radio === 1 && !this.num) {
        this.$message.error('请输入发票编号')
      } else if (this.config.type === 3 && this.radio === 1 && !this.invoice_address) {
        if (this.invoice_type === 'express') {
          this.$message.error('请输入邮寄地址')
        } else {
          this.$message.error('请输入邮箱地址')
        }
      } else {
        let data = {
          status: this.radio
        }

        if (this.config.type === 1) {
          let mark = ''
          data.fail_msg = this.remarks
          if (this.config.data.types === 1) {
            if (this.config.data.treaty) {
              mark = this.config.data.treaty.title + '-' + this.config.data.renew.title
            }
          } else {
            if (this.config.data.treaty) {
              mark = this.config.data.treaty.title + '-' + this.$t('customer.Contractpayment')
            }
          }
          data.mark = mark
          this.getBillStatus(this.config.data.id, data)
        } else if (this.config.type === 2) {
          if (this.config.title === '作废审核') {
            let status = ''
            if (this.radio == 1) {
              status = 2
            } else {
              status = 3
            }
            let data = {
              remark: this.remarks,
              invalid: status
            }

            this.putInvalid(this.config.data.id, data)
          } else {
            data.remark = this.remarks

            this.invoiceStatus(this.config.data.id, data)
          }
        } else if (this.config.type === 3) {
          data.status = 3
          data.remark = this.remarks
          data.invoice_address = this.invoice_address
          data.invoice_type = this.invoice_type
          data.num = this.num
          data.is_send = this.config.data.collect_type === 'mail' ? this.isSend : 0
          if (this.uploadList.length > 0) {
            this.uploadList.map((value) => {
              file.push(value.id)
            })
          }
          data.file = file
          this.invoiceStatus(this.config.data.id, data)
        }
      }
    },
    closeImageViewer() {
      this.isImage = false
      this.srcList = []
    },

    // 作废审核
    async putInvalid(id, data) {
      await clientInvoiceStatus(id, data)
      this.$emit('isOk')
      this.handleClose()
      this.loading = false
    },

    // 删除附件
    async handleFileDelete(row, index) {
      await clientFileDeleteApi(row.id)
      this.uploadList.splice(index, 1)
    },
    // 上传前
    handleUpload(file) {
      const types = [
        'jpeg',
        'gif',
        'bmp',
        'png',
        'jpg',
        'doc',
        'docx',
        'xls',
        'xlsx',
        'xlsm',
        'ppt',
        'pptx',
        'txt',
        'pdf'
      ]
      this.uploadData.cid = this.config.data.cid
      this.uploadData.eid = this.config.data.eid
      const fileTypeName = file.name.substr(file.name.lastIndexOf('.') + 1)
      const isImage = types.includes(fileTypeName)
      const isLtSize = file.size / 1024 / 1024 < this.uploadSize
      if (!isImage) {
        this.$message.error('不支持该' + fileTypeName + '格式')
        return false
      }
      if (!isLtSize) {
        this.$message.error('上传图片大小不能超过 ' + this.uploadSize + ' MB!')
        return false
      }
      return true
    },
    // 上传成功
    handleSuccess(response) {
      if (response.status === 200) {
        this.uploadList.push({
          path: response.data.path,
          id: response.data.id,
          type: response.data.type,
          name: response.data.name
        })
        this.$message.success(response.message)
      } else {
        this.$message.error(response.message)
      }
    },
    handlePictureCardPreview(row) {
      const fileType = row.name.substr(row.name.lastIndexOf('.') + 1)
      const types = ['jpeg', 'gif', 'bmp', 'png', 'jpg']
      const isImage = types.includes(fileType)
      if (isImage) {
        this.isImage = true
        this.srcList.push(row.path)
      }
    },
    // 资金审核
    getBillStatus(id, data) {
      this.loading = true
      clientBillStatusApi(id, data)
        .then((res) => {
          this.$emit('isOk')
          this.handleClose()
          this.loading = false
        })
        .catch((error) => {
          this.loading = false
        })
    },
    // 发票审核
    invoiceStatus(id, data) {
      this.loading = true
      clientInvoiceStatusApi(id, data)
        .then((res) => {
          this.$emit('isOk')
          this.handleClose()
          this.loading = false
        })
        .catch((error) => {
          this.loading = false
        })
    },
    reset() {
      this.remarks = ''
      this.radio = 1
      this.isSend = 1
      this.num = ''
      this.invoice_type = ''
      this.invoice_address = ''
      this.uploadList = []
    },
    handleMethod(e) {
      let nameTel = this.config.data.collect_name + '-' + this.config.data.collect_tel
      if (e === this.config.data.collect_type) {
        if (e === 'express') {
          this.invoice_address = nameTel + '-' + this.config.data.collect_email
        } else {
          this.invoice_address = this.config.data.collect_email
        }
      } else {
        if (e === 'express') {
          this.invoice_address = nameTel + '-'
        } else {
          this.invoice_address = ''
        }
      }
    }
  }
}
</script>

<style scoped lang="scss">

::v-deep .el-textarea__inner {
  resize: none;
}
::v-deep .el-select,
::v-deep .el-input-number--medium,
::v-deep .el-date-editor {
  width: 100%;
}
::v-deep .el-form-item:last-of-type {
  margin-bottom: 0;
}
.examine-card {
  div {
    display: inline-block;
  }
  ::v-deep .el-upload--picture-card {
    width: 98px;
    height: 98px;
    line-height: 102px;
  }
  .upload-icon {
    font-size: 98px;
  }
  ::v-deep .el-upload-list--picture-card .el-upload-list__item {
    width: 98px;
    height: 98px;
    line-height: 1;
    .el-image {
      width: 100%;
      height: 100%;
    }
  }
}
.el-upload-list__item-actions {
  i {
    color: #ffffff;
    font-size: 24px;
  }
}
.dialog-footer {
  padding-top: 20px;
}
</style>
