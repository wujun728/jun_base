<template>
  <div>
    <el-dialog
      :visible.sync="dialogFormVisible"
      :width="formData.width"
      :title="formData.title"
      :modal="true"
      :close-on-click-modal="false"
      custom-class="person"
      :show-close="true"
      :before-close="handleClose"
    >
      <div class="container">
        <el-form ref="form" :model="tableFrom" :rules="rules" label-width="80px" class="mt20">
          <el-form-item class="info">
            <el-alert class="cr-alert" title="" :closable="false" type="info" :show-icon="true">
              <template slot="title">
                <p>您账号的密码为初始值,为了账号安全,建议您修改密码</p>
              </template>
            </el-alert>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input
              v-model="tableFrom.password"
              size="small"
              type="password"
              auto-complete="on"
              :placeholder="$t('login.title2')"
            />
          </el-form-item>
          <el-form-item label="确认密码" prop="password_confirm">
            <el-input
              v-model="tableFrom.password_confirm"
              size="small"
              type="password"
              auto-complete="on"
              placeholder="请输入确认密码"
            />
          </el-form-item>
        </el-form>
        <div class="text-right">
          <el-button size="small" @click="handleClose()">{{ $t('public.cancel') }}</el-button>
          <el-button type="primary" size="small" @click="handleConfirm()">{{ $t('public.ok') }}</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { userEntSavePasswordApi } from '@/api/user'
export default {
  name: 'DialogForm',
  components: {},
  props: {
    formData: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  data() {
    return {
      dialogFormVisible: false,
      loading: false,
      tableFrom: {
        password: '',
        password_confirm: ''
      },
      rules: {
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        password_confirm: [{ required: true, message: '请输入确认密码', trigger: 'blur' }]
      }
    }
  },
  methods: {
    handleClose() {
      this.dialogFormVisible = false
      let userInfo = this.$store.state.user.userInfo
      userInfo.is_init = 0
      this.$store.commit('user/SET_USERINFO', userInfo)
    },
    handleOpen() {
      this.dialogFormVisible = true
    },
    // 提交
    handleConfirm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.tableFrom.phone = this.$store.state.user.userInfo.phone
          this.savePassword(this.tableFrom)
        }
      })
    },
    savePassword(data) {
      this.loading = true
      userEntSavePasswordApi(data)
        .then((res) => {
          if (res.status == 200) {
            this.loading = false
            this.handleClose()
          }
        })
        .catch((error) => {
          this.loading = false
        })
    }
  }
}
</script>

<style lang="scss" scoped>
.info {
  ::v-deep .el-form-item__content {
    margin-left: 12px !important;
  }
  ::v-deep .el-alert {
    padding: 6px 20px;
  }
}
::v-deep .el-dialog__wrapper {
  background-color: rgba(0, 0, 0, 0.6);
}
.dialog-footer {
  padding-top: 20px;
  border-top: 1px solid #e6ebf5;
  text-align: right;
}
</style>
