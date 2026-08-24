<template>
  <div class="room-content">
    <el-form ref="ruleForm" :model="ruleForm" class="form-box" label-width="140px">
      <el-form-item :label="$t('setting.headportrait') + '：'" prop="business_license">
        <el-row class="upload-content">
          <el-col :span="8" class="left">
            <el-upload
              v-if="!ruleForm.avatar"
              :auto-upload="true"
              :headers="myHeaders"
              :http-request="uploadServerLog"
              :show-file-list="false"
              action="##"
              list-type="picture-card"
            >
              <i class="el-icon-plus" />
            </el-upload>

            <div v-else class="el-upload-list el-upload-list--picture-card">
              <img :src="ruleForm.avatar" alt="" class="el-upload-list__item-thumbnail" />
              <span class="el-upload-list__item-actions">
                <span class="el-upload-list__item-delete">
                  <i class="el-icon-delete" @click="handleRemove()"></i>
                </span>
              </span>
            </div>
          </el-col>
          <el-col :span="8" class="right">
            <p>建议图片分辨率80*80且大小不能超过2M,</p>
            <p>支持上传jpg、jpeg、gif、png格式的图片</p></el-col
          >
        </el-row>
      </el-form-item>
      <el-form-item :label="$t('setting.name') + '：'" prop="enterprise_name">
        <el-input v-model="ruleForm.name" :placeholder="$t('setting.title')" clearable size="small" />
      </el-form-item>
      <el-form-item :label="$t('setting.userid') + '：'" prop="enterprise_name">
        <div class="flex-item">
          <el-input v-model="ruleForm.uid" :placeholder="$t('setting.useridtitle')" disabled size="small" />
          <el-button :data-clipboard-text="ruleForm.uid" class="btns copy-data" type="text" @click="copy">
            {{ $t('setting.copy') }}
          </el-button>
        </div>
      </el-form-item>
      <el-form-item :label="$t('setting.phone') + '：'" prop="enterprise_name">
        <div v-show="isEditPhone" class="flex-item">
          <el-input v-model="ruleForm.phone" :placeholder="$t('setting.phonetitle')" disabled size="small" />
          <el-link :underline="false" class="btns" type="primary" @click="isEditPhone = false">
            {{ $t('setting.changephone') }}
          </el-link>
        </div>
        <div v-show="isEditPhone === false" class="flex-item">
          <el-input v-model="newPhone" :placeholder="$t('setting.phonetitle')" size="small" />
          <el-button :disabled="disabled" class="button ml10" size="small" type="primary" @click="getCode">{{
            text
          }}</el-button>
        </div>
      </el-form-item>
      <el-form-item v-show="isEditPhone === false" :label="$t('login.code') + '：'" prop="enterprise_name">
        <el-input v-model="verification_code" :placeholder="$t('login.codetitle')" size="small" type="text" />

        <el-link :underline="false" class="btns" type="primary" @click="isEditPhone = true">
          {{ $t('public.cancel') }}
        </el-link>
      </el-form-item>
      <el-form-item label="密码：" prop="password">
        <div v-show="isEditPassword" class="flex-item">
          <el-input
            v-model="ruleForm.password"
            :placeholder="$t('login.title2')"
            auto-complete="on"
            disabled
            size="small"
            type="password"
          />
          <el-link :underline="false" class="btns" type="primary" @click="replacePwd">
            {{ $t('login.changepassword') }}
          </el-link>
        </div>
        <div v-show="isEditPassword === false" class="flex-item" prop="newPassword">
          <el-input
            v-model="newPassword"
            :placeholder="$t('login.title2')"
            auto-complete="on"
            size="small"
            show-password
            type="password"
            @blur="regularFn"
          />
        </div>
      </el-form-item>
      <el-form-item v-show="isEditPassword === false" label="确认密码：" prop="enterprise_name">
        <el-input v-model="password_confirm" placeholder="请输入密码" size="small" type="password" show-password />
        <!-- <el-link type="primary" :underline="false" class="btns" @click="confirmPwd">{{ $t('public.ok') }}</el-link> -->
        <el-link :underline="false" class="btns" type="primary" @click="cancelPwd">
          {{ $t('public.cancel') }}
        </el-link>
      </el-form-item>
      <el-form-item :label="$t('setting.mailbox') + '：'" prop="enterprise_name">
        <el-input v-model="ruleForm.email" :placeholder="$t('setting.mailboxtitle')" clearable size="small" />
      </el-form-item>
    </el-form>
    <div class="room-footer btn-shadow">
      <el-button size="small" @click="cancel">{{ $t('public.cancel') }}</el-button>
      <el-button size="small" type="primary" @click="preserve">
        {{ $t('public.save') }}
      </el-button>
    </div>
  </div>
</template>

<script>
import ClipboardJS from 'clipboard'
import { mapGetters } from 'vuex'
import { loginRegex } from '@/utils/format'
import sendVerifyCode from '@/mixins/SendVerifyCode'
import { uploader } from '@/utils/uploadCloud'
import { getToken } from '@/utils/auth'
import { userInfo, getCmsKeyApi, getCmsApi, editUserInfo, checkPasswordApi, site } from '@/api/user'
import helper from '@/libs/helper'
export default {
  name: 'Index',
  mixins: [sendVerifyCode],
  data() {
    return {
      ruleForm: {
        name: '',
        uid: '',
        phone: '',
        avatar: '',
        email: '',
        password: ''
      },
      uPattern: '',
      textVal: '',
      password_confirm: '',
      verification_code: '',
      newPhone: '',
      newPassword: '',
      isEditPhone: true,
      isEditPassword: true,
      myHeaders: {
        authorization: 'Bearer ' + getToken()
      },
      sitedata: {},
      dsad: false,
      uploadSize: 2
    }
  },
  computed: {
    ...mapGetters(['userInfo'])
  },
  mounted() {
    this.sitedata = JSON.parse(localStorage.getItem('sitedata'))
    var { val, text } = loginRegex(this.sitedata.password_type, Number(this.sitedata.password_length))
    this.textVal = text
    this.uPattern = new RegExp(val)
    this.userInfoList()
  },
  methods: {
    regularFn() {
      if (!this.uPattern.test(this.newPassword)) return this.$message.error(this.textVal)
    },

    replacePwd() {
      this.newPassword = ''
      this.password_confirm = ''
      this.isEditPassword = false
    },
    copy() {
      this.$nextTick(() => {
        const clipboard = new ClipboardJS('.copy-data')
        clipboard.on('success', () => {
          this.$message.success(this.$t('setting.copytitle'))
          clipboard.destroy()
        })
        this.$store.commit('app/SET_CLICK_TAB', false)
      })
    },
    cancelPwd() {
      this.ruleForm.password = '......'
      this.password_confirm = ''
      this.isEditPassword = true
    },
    defaultPwd(data) {
      if (!data) {
        this.$set(this.ruleForm, 'password', '......')
      }
    },
    preserve() {
      if (!this.isEditPassword) {
        this.confirmPwd()
      }
      if (!this.isEditPhone) {
        this.confirmBnt()
      }
      if (!this.password_confirm) {
        this.$delete(this.ruleForm, 'password')
      }
      this.ruleForm.password_confirm = this.password_confirm
      this.ruleForm.password = this.newPassword
      this.ruleForm.verification_code = this.verification_code
      editUserInfo(this.ruleForm).then((res) => {
        if (res.status != '200') {
          return false
        }
        let userInfo = JSON.parse(JSON.stringify(this.$store.state.user.userInfo))
        userInfo.name = res.data.name
        userInfo.avatar = res.data.avatar
        userInfo.email = res.data.email
        if (res.data.phone) {
          userInfo.phone = res.data.phone
        }
        this.$store.commit('user/SET_USERINFO', userInfo)
        this.defaultPwd(this.ruleForm.password)
        if (res.status == '200') {
          this.$emit('isOk')
        }
      })
    },
    cancel() {
      this.isEditPhone = true
      this.isEditPassword = true
      this.userInfoList()
      this.$emit('isOk')
    },
    confirmPwd() {
      if (!this.uPattern.test(this.newPassword)) return this.$message.error(this.textVal)

      if (!this.newPassword) return this.$message.error(this.$t('login.title2'))
      const data = { password: this.newPassword, password_confirm: this.password_confirm }
      checkPasswordApi(data).then((res) => {
        this.ruleForm.password = this.newPassword
        this.isEditPassword = true
      })
    },
    getCode() {
      this.getCmsKey()
    },
    getCmsKey() {
      getCmsKeyApi().then((res) => {
        const cmsKey = res.data.key
        const exp = helper.phoneReg
        if (!this.newPhone) return this.$message.error(this.$t('login.title1'))
        if (!exp.test(this.newPhone)) {
          return this.$message.error(this.$t('login.title9'))
        }
        getCmsApi({
          phone: this.newPhone,
          key: cmsKey,
          types: 2
        })
          .then((res) => {
            this.sendCode()
          })
          .catch(() => {
            this.getCmsKey()
          })
      })
    },
    confirmBnt() {
      const exp = helper.phoneReg
      if (!this.verification_code || !this.newPhone) {
        return this.$message.error(this.$t('login.rules'))
      }
      if (!exp.test(this.newPhone)) {
        return this.$message.error(this.$t('login.title9'))
      }
      if (!/^\d{6}$/.test(this.verification_code)) {
        return this.$message.error(this.$t('login.title5'))
      }
      this.ruleForm.phone = this.newPhone
      this.isEditPhone = true
    },
    userInfoList() {
      site().then((res) => {
        this.sitedata = res.data
        localStorage.setItem('sitedata', JSON.stringify(this.sitedata))
      })
      userInfo().then((res) => {
        this.ruleForm = res.data

        this.defaultPwd(res.data.password)
      })
    },
    // 上传文件方法
    uploadServerLog(params) {
      const file = params.file
      let options = {
        way: 1,
        relation_type: '',
        relation_id: 0,
        eid: 0
      }
      uploader(file, 1, options).then((res) => {
        // 获取上传文件渲染页面
        if (res.data.name) {
          this.ruleForm.avatar = res.data.url
          this.userInfo.card.avatar = res.data.url
        }
      })
    },

    handleRemove() {
      this.ruleForm.avatar = ''
    }
  }
}
</script>

<style lang="scss" scoped>
::v-deep.el-form-item__label {
  /*width: 100px !important;*/
}
::v-deep.el-form-item__content {
  margin-left: 100px !important;
}
::v-deep .el-form-item__error {
  margin-left: 40px;
}
.codeItem {
  width: 300px;
  ::v-deep.el-input {
    width: auto;
  }
}
::v-deep.el-upload--picture-card {
  width: 70px;
  height: 70px;
  line-height: 80px;
}
.el-upload-list__item-thumbnail {
  border-radius: 4px;
}
.el-upload-list--picture-card {
  position: relative;
  width: 70px;
  height: 70px;
  display: inline-block;
}
.flex-item {
  display: flex;
  align-items: center;
}
.el-input {
  width: 300px;
}
.upload-content {
  .left {
    width: 80px;
  }
  .right {
    width: calc(100% - 130px);
    p {
      margin: 0;
      padding: 0;
      line-height: 1.5;
    }
  }
}
.copy-data {
  font-size: 14px;
  font-weight: 500;
}
.btns {
  margin-left: 11px;
}
.btn-box {
  padding-left: 140px;
  button {
    width: 100px;
    height: 32px;
  }
}
.room-content {
  margin-top: 40px;
  height: calc(100% - 40px);
  padding-bottom: 60px;
  position: relative;
  .room-footer {
    padding: 14px 0;
    text-align: center;
    position: absolute;
    bottom: 0;
    left: 0;
    width: 100%;
  }
}
</style>
