<template>
  <div class="page-account">
    <div :class="[fullWidth > 768 ? 'containerSamll' : 'containerBig']" class="container">
      <div class="index_from page-account-container">
        <template v-if="loginType == 1">
          <div class="page-account-top page-code-top">
            <el-tabs v-if="!codeShowLogin" v-model="activeName" @tab-click="tabChange">
              <el-tab-pane :label="$t('login.passwordlogin')" name="passwordLogin" />
              <el-tab-pane :label="$t('login.smslogin')" name="cmsLogin" />
            </el-tabs>
            <el-tabs v-if="codeShowLogin">
              <el-tab-pane label="扫码登录" name="codeLogin" />
            </el-tabs>
          </div>
          <el-form
            v-if="!codeShowLogin"
            ref="loginForm"
            :model="loginForm"
            :rules="loginRules"
            autocomplete="on"
            class="login-form"
            label-position="left"
            @keyup.enter="handleLogin"
          >
            <template v-if="activeName == 'passwordLogin'">
              <el-form-item prop="account">
                <el-input
                  ref="account"
                  v-model="loginForm.account"
                  :placeholder="$t('login.phone')"
                  autocomplete="on"
                  name="username"
                  prefix-icon="el-icon-user"
                  tabindex="1"
                  type="text"
                />
              </el-form-item>
              <el-form-item prop="password">
                <el-input
                  :key="passwordType"
                  ref="password"
                  v-model="loginForm.password"
                  :placeholder="$t('login.password')"
                  :type="passwordType"
                  auto-complete="on"
                  name="password"
                  prefix-icon="el-icon-lock"
                  tabindex="2"
                />
                <span class="pwd" @click="handelTabTagger(3)">
                  <el-link :underline="false" type="primary">{{ $t('login.forgetpassword') }}</el-link>
                </span>
              </el-form-item>
              <el-form-item v-if="!captchaDisabled" class="captcha" prop="captcha">
                <div class="captcha">
                  <el-input
                    ref="username"
                    v-model="loginForm.captcha"
                    placeholder="验证码"
                    autocomplete="on"
                    name="username"
                    prefix-icon="el-icon-message"
                    style="width: 199px"
                    tabindex="3"
                    type="text"
                  />
                  <div class="imgs" @click="getCaptcha()">
                    <img :src="captchatImg" />
                  </div>
                </div>
              </el-form-item>
            </template>
            <template v-if="activeName == 'cmsLogin'">
              <el-form-item prop="phone">
                <el-input
                  ref="phone"
                  v-model="loginForm.phone"
                  :placeholder="$t('login.phone')"
                  autocomplete="on"
                  name="phone"
                  prefix-icon="el-icon-mobile-phone"
                  tabindex="1"
                  type="text"
                />
              </el-form-item>
              <el-form-item prop="verification_code">
                <div class="item-box">
                  <div>
                    <el-input
                      :key="passwordType"
                      ref="password"
                      v-model="loginForm.verification_code"
                      :placeholder="$t('login.codetitle')"
                      auto-complete="on"
                      name="password"
                      prefix-icon="el-icon-lock"
                      tabindex="2"
                      type="text"
                    />
                  </div>
                  <el-button :disabled="disabled" class="button" type="primary" @click="code">{{ text }}</el-button>
                </div>
              </el-form-item>
            </template>
            <el-button
              :loading="loading"
              style="width: 100%; margin-bottom: 30px"
              type="primary"
              @click.native.prevent="handleLogin"
            >
              {{ $t('login.logIn') }}
            </el-button>
          </el-form>
          <div v-if="codeShowLogin" class="code-content">
            <div v-if="!codeSuccess">
              <div v-loading="codeLoading" class="code-header">
                <div ref="QRCode_header" class="QRCode_header"></div>
                <img v-if="sitedata.site_logo" :src="sitedata.site_logo" alt="" class="image-code-logo" />
                <img v-else alt="" class="image-code-logo" src="../../assets/images/code-logo.png" />
                <div v-if="codeButton" class="code-model">
                  <div class="text">二维码失效</div>
                  <el-button round size="mini" type="primary" @click="getUserKey">刷新二维码</el-button>
                </div>
              </div>
              <div class="code-tip">请使用陀螺匠App扫码</div>
            </div>

            <div v-else class="success-code">
              <i class="el-icon-circle-check icon"></i>
              <div class="success-text">扫码成功</div>
              <div class="success-text1 code-tip">请在手机上根据提示确认登录</div>
            </div>
          </div>
          <div v-if="activeName === 'passwordLogin' && !codeShowLogin" class="text">未注册用户请联系管理员创建</div>
          <div v-if="activeName === 'cmsLogin' && !codeShowLogin" class="text">未注册用户请联系管理员创建</div>
          <el-button class="btn-login-code" size="small" @click="imageCode">{{
            codeShowLogin ? '账号登录' : '扫码登录'
          }}</el-button>
          <img
            :src="
              codeShowLogin ? require('@/assets/images/login-account.png') : require('@/assets/images/login-code.png')
            "
            alt=""
            class="image-login-code"
            @click="imageCode"
          />
        </template>
        <template v-if="loginType == 2">
          <div class="page-account-top">
            <div class="title-box">{{ $t('login.zhuce') }}</div>
            <el-form
              ref="loginForm"
              :model="loginForm"
              :rules="loginRules"
              autocomplete="on"
              class="login-form"
              label-position="left"
              @keyup.enter="handleLogin"
            >
              <template>
                <el-form-item prop="phone">
                  <el-input
                    ref="phone"
                    v-model="loginForm.phone"
                    :placeholder="$t('login.title1')"
                    autocomplete="on"
                    name="phone"
                    prefix-icon="el-icon-mobile-phone"
                    tabindex="1"
                    type="text"
                  />
                </el-form-item>
                <el-form-item prop="verification_code">
                  <div class="item-box">
                    <div>
                      <el-input
                        :key="passwordType"
                        ref="password"
                        v-model.number="loginForm.verification_code"
                        :placeholder="$t('login.codetitle')"
                        auto-complete="on"
                        name="password"
                        prefix-icon="el-icon-lock"
                        tabindex="2"
                        type="text"
                      />
                    </div>
                    <el-button :disabled="disabled" class="button" type="primary" @click="code">{{ text }}</el-button>
                  </div>
                </el-form-item>
                <el-form-item prop="password">
                  <el-input
                    :key="passwordType"
                    ref="password"
                    v-model="loginForm.password"
                    :placeholder="$t('login.title2')"
                    :type="passwordType"
                    auto-complete="on"
                    name="password"
                    prefix-icon="el-icon-lock"
                    tabindex="2"
                  />
                  <span class="show-pwd" @click="showPwd">
                    <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
                  </span>
                </el-form-item>
              </template>
              <el-button
                :loading="loading"
                style="width: 100%; margin-bottom: 30px"
                type="primary"
                @click.native.prevent="handleRegister"
              >
                {{ $t('login.register') }}
              </el-button>
            </el-form>
            <div class="tips-btn">
              {{ $t('login.title3') }}
              <el-link
                :underline="false"
                style="margin-top: -3px; margin-left: 5px"
                type="primary"
                @click="handelTabTagger(1)"
              >
                {{ $t('login.logIn') }}
              </el-link>
            </div>
          </div>
        </template>
        <template v-if="loginType == 3">
          <div class="page-account-top">
            <div class="title-box">{{ $t('login.title4') }}</div>
            <el-form
              ref="loginForm"
              :model="loginForm"
              :rules="loginRules"
              autocomplete="on"
              class="login-form"
              label-position="left"
              @keyup.enter="handleLogin"
            >
              <template>
                <el-form-item prop="phone">
                  <el-input
                    ref="phone"
                    v-model.number="loginForm.phone"
                    :placeholder="$t('login.title1')"
                    autocomplete="on"
                    name="phone"
                    prefix-icon="el-icon-mobile-phone"
                    tabindex="1"
                    type="text"
                  />
                </el-form-item>
                <el-form-item prop="verification_code">
                  <div class="item-box">
                    <div>
                      <el-input
                        :key="passwordType"
                        ref="password"
                        v-model.number="loginForm.verification_code"
                        :placeholder="$t('login.codetitle')"
                        auto-complete="on"
                        name="password"
                        prefix-icon="el-icon-lock"
                        tabindex="2"
                        type="text"
                      />
                    </div>
                    <el-button :disabled="disabled" class="button" type="primary" @click="code">{{ text }}</el-button>
                  </div>
                </el-form-item>
                <el-form-item prop="password">
                  <el-input
                    :key="passwordType"
                    ref="password"
                    v-model="loginForm.password"
                    :placeholder="$t('login.title2')"
                    :type="passwordType"
                    auto-complete="on"
                    name="password"
                    prefix-icon="el-icon-lock"
                    tabindex="2"
                  />
                  <span class="show-pwd" @click="showPwd">
                    <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
                  </span>
                </el-form-item>
              </template>
              <el-button
                :loading="loading"
                style="width: 100%; margin-bottom: 30px"
                type="primary"
                @click.native.prevent="handleResetPwd"
              >
                {{ $t('login.ok') }}
              </el-button>
            </el-form>
            <div class="tips-btn">
              {{ $t('login.title3') }}
              <el-link
                :underline="false"
                style="margin-top: -3px; margin-left: 5px"
                type="primary"
                @click="handelTabTagger(1)"
              >
                {{ $t('login.logIn') }}
              </el-link>
            </div>
          </div>
        </template>
      </div>
    </div>
    <div class="foot-bar">
      <p>
        {{ $t('login.contactnumber') }}：{{ sitedata.site_tel }} {{ $t('login.addresss') }}：{{ sitedata.site_address }}
      </p>
      <a href="https://beian.miit.gov.cn" target="_blank">{{ sitedata.site_record_number }}</a>
    </div>
  </div>
</template>

<script>
import {
  captchaApi,
  getCmsKeyApi,
  getCmsApi,
  registerUserApi,
  savePasswordApi,
  site,
  getPerfectList,
  userScanKeyApi,
  loginInfo
} from '@/api/user'
import notice from '@/libs/notice'
import sendVerifyCode from '@/mixins/SendVerifyCode'
import helper from '@/libs/helper'
import QRCode from 'qrcodejs2'
import { roterPre } from '@/settings'
import { getMenus } from '@/utils/auth'
import { AiEmbeddedManage } from "@/libs/ai-embedded"

export default {
  name: 'Login',
  mixins: [sendVerifyCode],
  data() {
    const validateUsername = (rule, value, callback) => {
      if (!value) {
        callback(new Error(this.$t('login.phone')))
      } else {
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
      if (!value) {
        callback(new Error(this.$t('login.title2')))
      } else {
        callback()
      }
    }
    const checkPhone = (rule, value, callback) => {
      if (!value) {
        return callback(new Error(this.$t('customer.placeholder09')))
      }
      setTimeout(() => {
        if (helper.phoneReg.test(value)) {
          callback()
        } else {
          callback(new Error(this.$t('customer.placeholder71')))
        }
      }, 150)
    }
    return {
      sitedata: '',
      fullWidth: document.body.clientWidth,
      swiperOption: {
        pagination: {
          el: '.pagination'
        },
        autoplay: {
          enabled: true,
          disableOnInteraction: false,
          delay: 3000
        }
      },
      loginLogo: '',
      swiperList: [],
      captchatImg: '',
      loginForm: {
        account: '',
        password: '',
        key: '',
        captcha: '',
        verification_code: '',
        phone: ''
      },
      loginRules: {
        account: [{ required: true, trigger: 'blur', validator: validateUsername }],
        password: [{ required: true, trigger: 'blur', validator: validatePassword }],
        captcha: [{ required: true, message: this.$t('login.title5'), trigger: 'blur' }],
        verification_code: [{ required: true, message: this.$t('login.title6'), trigger: 'blur' }],
        phone: [{ required: true, validator: checkPhone, trigger: 'change' }]
      },
      passwordType: 'password',
      capsTooltip: false,
      loading: false,
      showDialog: false,
      redirect: undefined,
      otherQuery: {},
      activeName: 'passwordLogin',
      loginType: 1, // 1登录 2注册 3忘记密码
      cmsKey: '', // 短信验证码的key,
      topKeys: {}, // 顶部导航菜单
      qrcode: null,
      codeLoading: false,
      codeTimer: null,
      codeKey: '',
      codeButton: false,
      codeSuccess: false,
      codeShowLogin: false,
      captchaDisabled: false
    }
  },
  watch: {
    captchaDisabled(val) {
      const rule = this.loginRules.captcha && this.loginRules.captcha[0]
      if (rule) rule.required = !val
    },
    fullWidth(val) {
      // 为了避免频繁触发resize函数导致页面卡顿，使用定时器
      if (!this.timer) {
        // 一旦监听到的screenWidth值改变，就将其重新赋给data里的screenWidth
        this.screenWidth = val
        this.timer = true
        const that = this
        setTimeout(function () {
          // 打印screenWidth变化的值
          that.timer = false
        }, 400)
      }
    },
    $route: {
      handler: function (route) {
        const query = route.query
        if (query) {
          this.redirect = query.redirect
          this.otherQuery = this.getOtherQuery(query)
        }
      },
      immediate: true
    }
  },
  created() {
    if (this.$route.query.loginType) {
      this.loginType = this.$route.query.loginType
    }

    const _this = this
    document.onkeydown = function (e) {
      if (_this.$route.path.indexOf('login') !== -1) {
        const key = window.event.keyCode
        if (key === 13) {
          _this.handleLogin()
        }
      }
    }
    window.addEventListener('resize', this.handleResize)
    // this.getCmsKey()
  },
  mounted() {
    this.site()
    this.getCaptcha()
  },
  beforeCreate() {},
  beforeDestroy() {
    clearInterval(this.codeTimer)
    this.codeTimer = null
  },
  destroyed() {},
  methods: {
    // 站点配置
    site() {
      site().then((res) => {
        this.sitedata = res.data
        localStorage.setItem('sitedata', JSON.stringify(this.sitedata))
      })
    },
    resetForm() {
      this.loginForm = {
        account: '',
        password: '',
        key: '',
        captcha: '',
        verification_code: '',
        phone: ''
      }
    },
    tabChange() {
      this.$refs.loginForm.resetFields()
      if (this.activeName == 'passwordLogin') {
        this.loginForm.account = this.loginForm.phone
      } else if (this.activeName == 'cmsLogin') {
        this.loginForm.phone = this.loginForm.account
      }
    },
    // 获取短信验证码的key
    getCmsKey() {
      getCmsKeyApi().then(async (res) => {
        this.cmsKey = res.data
        const exp = helper.phoneReg
        if (!this.loginForm.phone) return this.$message.error(this.$t('login.title1'))
        if (!exp.test(this.loginForm.phone)) return this.$message.error(this.$t('login.title9'))
        await getCmsApi({
          phone: this.loginForm.phone,
          key: this.cmsKey.key,
          types: 0,
          from: 1
        }).then((res) => {
          if (res.status == 200) {
            this.sendCode()
          }
        })
      })
    },
    // 切换
    handelTabTagger(type) {
      this.loginType = type
      this.resetForm()
    },
    // 发送手机验证码
    async code() {
      await this.getCmsKey()
    },
    // 忘记密码
    handleResetPwd() {
      this.$refs['loginForm'].validate((valid) => {
        if (valid) {
          this.loading = true
          this.loginForm.password_confirm = this.loginForm.password
          savePasswordApi(this.loginForm)
            .then((res) => {
              this.loading = false
              this.loginType = 1
              this.resetForm()
            })
            .catch((error) => {
              this.loading = false
            })
        }
      })
    },
    imageCode() {
      if (this.codeShowLogin) {
        this.codeShowLogin = false
        if (this.codeTimer) {
          clearInterval(this.codeTimer)
        }
      } else {
        this.codeShowLogin = true
        this.getUserKey()
      }
    },
    getCaptcha() {
      captchaApi().then((res) => {
        // 若依 /captchaImage：开启时返回 data:{img,uuid,captchaEnabled}；关闭时仅 {code,msg} 无 data
        const info = res.data || {}
        if (info.captchaEnabled === false || !info.img) {
          this.captchaDisabled = true
          this.captchatImg = ''
        } else {
          this.captchaDisabled = false
          this.captchatImg = 'data:image/gif;base64,' + info.img
        }
        this.loginForm.key = info.uuid || ''
      })
    },
    checkCapslock(e) {
      const { key } = e
      this.capsTooltip = key && key.length === 1 && key >= 'A' && key <= 'Z'
    },
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    // 用户注册
    handleRegister() {
      this.$refs['loginForm'].validate((valid) => {
        if (valid) {
          this.loading = true
          this.loginForm.password_confirm = this.loginForm.password
          registerUserApi(this.loginForm)
            .then((res) => {
              this.loading = false
              this.loginType = 1
              this.resetForm()
            })
            .catch((error) => {
              this.loading = false
            })
        }
      })
    },
    handleLogin() {
      this.$refs['loginForm'].validate(async (valid) => {
        if (!valid) {
          return false
        }
        // 验证码关闭时清除 code/uuid
        if (this.captchaDisabled) {
          this.loginForm.captcha = ''
          this.loginForm.key = ''
        }
        this.loading = true
        const payload = { userInfo: this.loginForm, activeName: this.activeName }
        await this.$store.dispatch('user/login', payload).catch(() => {
          this.loading = false
          this.loginForm.captcha = ''

          this.getCaptcha()
        })
        this.getCaptcha()
        this.loading = false
        await this.loginAfter()
      })
    },

    getOtherQuery(query) {
      return Object.keys(query).reduce((acc, cur) => {
        if (cur !== 'redirect') {
          acc[cur] = query[cur]
        }
        return acc
      }, {})
    },
    handleResize(event) {
      this.fullWidth = document.body.clientWidth
    },
    // 获取生成二维码
    getUserKey() {
      this.codeLoading = true
      this.codeSuccess = false
      userScanKeyApi()
        .then(async (res) => {
          await this.$nextTick()
          this.$refs.QRCode_header.innerHTML = ''
          this.codeKey = res.data.key
          this.qrcode = new QRCode(this.$refs.QRCode_header, {
            text: this.codeKey,
            width: 190,
            height: 190,
            colorDark: '#000000',
            colorLight: '#ffffff',
            correctLevel: QRCode.CorrectLevel.M
          })
          this.qrcode._el.title = ''
          this.codeButton = false
          this.codeTimer = setInterval(() => {
            this.getUserStatus()
          }, 2000)
          this.codeLoading = false
        })
        .catch((error) => {
          this.codeLoading = false
        })
    },
    // 获取登录返回状态
    async getUserStatus() {
      await this.$store
        .dispatch('user/scanLogin', { key: this.codeKey })
        .then((data) => {
          if (data.status === -1) {
            this.codeButton = true
            this.codeSuccess = false
            if (this.codeTimer) {
              clearInterval(this.codeTimer)
            }
          } else if (data.status === 1) {
            this.codeSuccess = true
          }
        })
        .catch((err) => {
          if (this.codeTimer) {
            clearInterval(this.codeTimer)
          }
          this.$message.error(err.message)
        })
      await this.loginAfter()
    },
    async getInfo() {
      const res = await loginInfo()
      // 若依 /getInfo 返回 { user, roles, permissions }
      const user = res.user || (res.data && res.data.user) || {}
      this.$store.commit('user/SET_USERINFO', user)
      this.$store.commit('user/SET_ENTINFO', user.enterprise || {})
      if (res.permissions || (res.data && res.data.permissions)) {
        this.$store.commit('user/SET_PERMISSIONS', res.permissions || res.data.permissions)
      }
      if (res.roles || (res.data && res.data.roles)) {
        this.$store.commit('user/SET_ROLES', res.roles || res.data.roles)
      }
    },
    async loginAfter() {
      if (this.$store.getters.isLogin) {
        let path = this.$route.query.redirect || '/'
        this.$store.commit('app/SET_PARENTCUR', 0)
        await getMenus()
        await this.getInfo()

        this.$router.push({
          path: path,
          query: this.otherQuery
        })

        this.loading = false
        this.$root.closeNotice()
        try {
          this.$root.notice = notice(this.$store.getters.token)
        } catch (e) {
          // ws 不可用不阻断
        }
      }
    }
  }
}
</script>

<style lang="scss" scoped>
$screen-md: 768px;
$font-size-base: 14px;
$animation-time: 0.3s;
$animation-time-quick: 0.15s;
$transition-time: 0.2s;
$ease-in-out: ease-in-out;
$subsidiary-color: #808695;
@media (min-width: 1920px) {
  .containerSamll {
    margin-top: 150px;
  }
}
@media (max-width: 1920px) and (min-width: 1200px) {
  .containerSamll {
    margin-top: 6%;
  }
  .index_from {
    height: auto !important;
  }
}
.code-content {
  width: 100%;
  height: auto;
  .code-header {
    width: 220px;
    height: 220px;
    background: linear-gradient(#cbcbcb, #cbcbcb) left top, linear-gradient(#cbcbcb, #cbcbcb) left top,
      linear-gradient(#cbcbcb, #cbcbcb) right top, linear-gradient(#cbcbcb, #cbcbcb) right top,
      linear-gradient(#cbcbcb, #cbcbcb) right bottom, linear-gradient(#cbcbcb, #cbcbcb) right bottom,
      linear-gradient(#cbcbcb, #cbcbcb) left bottom, linear-gradient(#cbcbcb, #cbcbcb) left bottom;
    background-repeat: no-repeat;
    background-size: 2px 20px, 20px 2px;
    margin: 0 auto 20px auto;
    display: flex;
    align-items: center;
    justify-content: center;
    position: relative;
    .QRCode_header {
      width: 190px;
      height: 190px;
    }
    .image-code-logo {
      width: 50px;
      height: 50px;
      position: absolute;
      border-radius: 8px;
      border: 3px solid #fff;
    }
    .code-model {
      position: absolute;
      left: 0;
      top: 0;
      bottom: 0;
      right: 0;
      background-color: rgba(0, 0, 0, 0.7);
      display: flex;
      align-items: center;
      justify-content: center;
      flex-direction: column;
      .text {
        color: #fff;
        font-size: 15px;
      }
    }
  }

  .code-tip {
    margin-bottom: 14px;
  }
  .success-code {
    margin-top: 40px;
    .icon {
      font-size: 40px;
      color: #1890ff;
    }
    > div {
      padding-top: 15px;
      font-size: 15px;
    }
    .success-text {
      color: #909399;
    }
    .success-text1 {
      color: #303133;
      font-weight: 800;
    }
  }
}
.text {
  font-size: 13px;
  color: #999;
  margin-bottom: 10px;
}
.page-account {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100vh;
  overflow: auto;

  &-container {
    flex: 1;
    padding: 32px 0;
    text-align: center;
    width: 384px;
    margin: 0 auto;

    &-result {
      width: 100%;
    }
  }

  &-tabs {
    .ivu-tabs-bar {
      border-bottom: none;
    }
    .ivu-tabs-nav-scroll {
      text-align: center;
    }
    .ivu-tabs-nav {
      display: inline-block;
      float: none;
    }
  }
  &-top {
    padding: 32px 0;
    &-logo {
      img {
        max-height: 75px;
      }
    }
    &-desc {
      font-size: $font-size-base;
      color: $subsidiary-color;
    }
  }

  &-auto-login {
    margin-bottom: 24px;
    text-align: left;
    a {
      float: right;
    }
  }

  &-other {
    margin: 24px 0;
    text-align: left;
    span {
      font-size: $font-size-base;
    }
    img {
      width: 24px;
      margin-left: 16px;
      cursor: pointer;
      vertical-align: middle;
      opacity: 0.7;
      transition: all $transition-time $ease-in-out;
      &:hover {
        opacity: 1;
      }
    }
  }

  .ivu-poptip,
  .ivu-poptip-rel {
    display: block;
  }

  &-register {
    float: right;
    &-tip {
      text-align: left;
      &-title {
        font-size: $font-size-base;
      }
      &-desc {
        white-space: initial;
        font-size: $font-size-base;
        margin-top: 6px;
      }
    }
  }

  &-to-login {
    text-align: center;
    margin-top: 16px;
  }

  &-header {
    text-align: right;
    position: fixed;
    top: 16px;
    right: 24px;
  }
}
.labelPic {
  position: absolute;
  right: 0;
}
@media (min-width: $screen-md) {
  .page-account {
    background-image: url('../../assets/images/bg.png');
    background-repeat: no-repeat;
    background-position: top center;
    background-size: 100% auto;
  }
  .page-account-container {
    padding: 32px 0 24px 0;
    position: relative;

    .image-login-code {
      width: 69px;
      height: 69px;
      position: absolute;
      right: 0;
      top: 0;
      cursor: pointer;
    }
    .btn-login-code {
      position: absolute;
      right: 75px;
      top: 12px;
      background: rgba(231, 244, 255, 1);
      border: 1px solid rgba(231, 244, 255, 1);
      color: #1890ff;
    }
  }
}
.page-account {
  display: flex;
}

.page-account .code {
  display: flex;
  align-items: center;
  justify-content: center;
}

.page-account .code .pictrue {
  height: 40px;
}

.swiperPross {
  border-radius: 6px 0px 0px 6px;
  overflow: hidden;
}

.swiperPross,
.swiperPic,
.swiperPic img {
  width: 510px;
  height: 100%;
}

.swiperPic img {
  width: 100%;
  height: 100%;
}

.container {
  padding: 0 !important;
  /*overflow: hidden;*/
  border-radius: 12px;
  z-index: 1;
  display: flex;
}

.containerSamll {
  width: 424px;

  background: #fff !important;
  box-shadow: 0px 2px 32px 0px rgba(0, 18, 107, 0.08);
}

.containerBig {
  width: auto !important;
  background: #f7f7f7 !important;
}

.index_from {
  padding: 0 40px 32px 40px;
  height: 460px;
  box-sizing: border-box;
}

.page-account-top {
  padding: 20px 0 !important;
  box-sizing: border-box !important;
}
.page-code-top {
  margin-top: 30px;
}

.page-account-container {
  border-radius: 0px 6px 6px 0px;
}

.btn {
  background: linear-gradient(90deg, rgba(25, 180, 241, 1) 0%, rgba(14, 115, 232, 1) 100%) !important;
}
</style>

<style lang="scss" scoped>
.captcha {
  display: flex;
  align-items: flex-start;
}
$bg: #2d3a4b;
$dark_gray: #889aa4;
$light_gray: #eee;
.imgs {
  margin-left: 26px;
  img {
    height: 36px;
  }
}
.login-form {
  position: relative;
  max-width: 100%;
  margin: 0 auto;
  overflow: hidden;
}
.tips {
  font-size: 14px;
  color: #fff;
  margin-bottom: 10px;

  span {
    &:first-of-type {
      margin-right: 16px;
    }
  }
}
.svg-container {
  padding: 6px 5px 6px 15px;
  color: $dark_gray;
  vertical-align: middle;
  width: 30px;
  display: inline-block;
}
.show-pwd {
  position: absolute;
  right: 10px;
  top: 7px;
  font-size: 16px;
  color: $dark_gray;
  cursor: pointer;
  user-select: none;
  ::v-deep.svg-icon {
    vertical-align: 0.3em;
  }
}
.thirdparty-button {
  position: absolute;
  right: 0;
  bottom: 6px;
}
.foot-bar {
  position: fixed;
  left: 0;
  bottom: 17px;
  width: 100%;
  text-align: center;
  font-size: 13px;
  color: #333;

  a {
    color: #333 !important;
    text-decoration: none;
  }
}
::v-deep .el-tabs__nav-wrap::after {
  display: none;
}
.el-divider {
  background-color: #1890ff;
}
::v-deep.el-tabs__item {
  font-size: 18px;
  font-weight: bold;
}
.item-box {
  display: flex;
  div {
    position: relative;
    flex: 1;
  }
  .button {
    margin-left: 8px;
  }
}
.link-btn {
  position: absolute;
  left: 0;
  bottom: 34px;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 13px;
  color: #999999;
}
.logos {
  position: unset;
  margin-top: 20px;
}
.page-account-top {
  .title-box {
    position: relative;
    text-align: left;
    color: #1890ff;
    font-weight: bold;
    height: 40px;
    line-height: 40px;
    margin-bottom: 30px;
    font-size: 18px;
    &:after {
      content: ' ';
      position: absolute;
      left: 0%;
      bottom: 0px;
      width: 60px;
      height: 2px;
      background: #1890ff;
    }
  }
}
.tips-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  color: #999;
  font-size: 13px;
  .el-link {
    font-size: 13px;
  }
}
.pwd {
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
}
</style>
