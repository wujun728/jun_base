<template>
  <div>
    <el-dialog
      :title="repeatData.title"
      :visible.sync="dialogVisible"
      :width="repeatData.width"
      :before-close="handleClose"
    >
      <div class="mt20">
        <el-form ref="form" :model="rules" :rules="rule" label-width="100px">
          <el-form-item prop="title">
            <span v-if="repeatData.title == '添加类目' || repeatData.title == '编辑类目'" slot="label">类目名称：</span>
            <span v-else slot="label"
              >{{ repeatData.label === 1 ? $t('customer.typename') : $t('customer.labelename') }}：</span
            >
            <el-input
              v-if="repeatData.title == '添加分类'"
              v-model="rules.title"
              maxlength="16"
              show-word-limit
              placeholder="请输入分类名称"
            />
            <el-input
              v-else
              v-model="rules.title"
              maxlength="16"
              show-word-limit
              :placeholder="placeholderFn(repeatData.label)"
            />
          </el-form-item>
          <el-form-item>
            <span slot="label">{{ $t('toptable.sort') }}：</span>
            <el-input-number
              :controls="false"
              :min="0"
              :max="999999"
              v-model="rules.sort"
              :placeholder="$t('customer.placeholder04')"
            />
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="handleClose">{{ $t('public.cancel') }}</el-button>
        <el-button size="small" :loading="loading" type="primary" @click="handleConfirm">{{
          $t('public.ok')
        }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  clientConfigLabelEditApi,
  clientConfigLabelSaveApi,
  clientConfigSaveApi,
  invoiceCategory,
  putInvoiceCategory
} from '@/api/enterprise'

export default {
  name: 'AddDialog',
  components: {},
  props: {
    repeatData: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  data() {
    return {
      dialogVisible: false,
      rules: {
        title: '',
        sort: 0
      },
      rule: {
        title: [{ required: true, message: this.$t('customer.message02'), trigger: 'blur' }]
      },
      key: 'cate',
      loading: false
    }
  },
  watch: {
    repeatData: {
      handler(nVal) {
        if (nVal.label === 1) {
          if (nVal.type === 2) {
            this.rules.title = nVal.data.value.title
            this.rules.sort = nVal.data.sort
          }
        } else {
          if (nVal.type === 2) {
            this.rules.title = nVal.data.name
            this.rules.sort = nVal.data.sort
          }
        }
      },
      deep: true
    }
  },
  methods: {
    handleClose() {
      this.rules.title = ''
      this.rules.sort = ''
      this.dialogVisible = false
    },
    confirmData() {
      this.$emit('handleRepeatData', this.rules)
    },
    placeholderFn(type) {
      if (type == 1) {
        return this.$t('customer.placeholder03')
      } else if (type == 3) {
        return '请输入类目名称'
      } else {
        return this.$t('customer.placeholder05')
      }
    },

    handleConfirm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          // 添加分类
          if (this.repeatData.label === 1) {
            this.rules.sort = this.rules.sort === '' ? 1 : this.rules.sort
            var id = this.repeatData.type === 1 ? 0 : this.repeatData.data.id
            var data = [{ title: this.rules.title, sort: this.rules.sort, id: id }]
            this.clientConfigSave({ data, key: this.key })
          } else if (this.repeatData.label === 3) {
            this.rules.sort = this.rules.sort === '' ? 1 : this.rules.sort
            var id = this.repeatData.type === 1 ? 0 : this.repeatData.data.id
            var data = { name: this.rules.title, sort: this.rules.sort }
            if (this.repeatData.type === 1) {
              this.invoiceCategory(data)
            } else {
              this.putInvoiceCategory(this.repeatData.data.id, data)
            }
          } else {
            // 客户标签
            this.rules.sort = this.rules.sort === '' ? 1 : this.rules.sort
            if (this.repeatData.type === 1) {
              this.labelSave({ name: this.rules.title, pid: 0, sort: this.rules.sort })
            } else {
              this.labelEdit(this.repeatData.data.id, { name: this.rules.title, pid: 0, sort: this.rules.sort })
            }
          }
        }
      })
    },
    // 保存企业设置--客户分类
    clientConfigSave(data) {
      this.loading = true
      clientConfigSaveApi(data)
        .then((res) => {
          this.handleClose()
          this.$emit('isOk')
          this.loading = false
        })
        .catch((error) => {
          this.loading = false
        })
    },

    // 保存发票类目
    invoiceCategory(data) {
      this.loading = true
      invoiceCategory(data)
        .then((res) => {
          this.$emit('isOk')
          this.loading = false
          this.handleClose()
        })
        .catch((err) => {
          this.loading = false
          this.handleClose()
        })
    },

    // 编辑发票类目
    putInvoiceCategory(id, data) {
      this.loading = true
      putInvoiceCategory(id, data)
        .then((res) => {
          this.$emit('isOk')
          this.loading = false
          this.handleClose()
        })
        .catch((err) => {
          this.loading = false
        })
    },

    // 客户标签保存
    labelSave(data) {
      this.loading = true
      clientConfigLabelSaveApi(data)
        .then((res) => {
          this.handleClose()
          this.$emit('isOk')
          this.loading = false
        })
        .catch((error) => {
          this.loading = false
        })
    },
    // 客户标签修改
    labelEdit(id, data) {
      this.loading = true
      clientConfigLabelEditApi(id, data)
        .then((res) => {
          this.handleClose()
          this.$emit('isOk')
          this.loading = false
        })
        .catch((error) => {
          this.loading = false
        })
    }
  }
}
</script>

<style scoped lang="scss">
::v-deep .el-dialog__footer {
  // border-top: 1px solid #d8d8d8;
}
.body {
  p {
    margin: 0 0 6px 0;
  }
}
::v-deep .el-input-number--medium {
  width: 100%;
  .el-input__inner {
    text-align: left;
  }
}
.dialog-footer {
  padding-top: 20px;
}
</style>
