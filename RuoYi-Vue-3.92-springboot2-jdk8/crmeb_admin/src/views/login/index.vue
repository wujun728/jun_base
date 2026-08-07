<template>
  <div
    class="page-account"
    :style="
      backgroundImages
        ? { backgroundImage: 'url(' + backgroundImages + ')' }
        : { backgroundImage: 'url(' + backgroundImageMo + ')' }
    "
  >
    <div class="container" :class="[fullWidth > 768 ? 'containerSamll' : 'containerBig']">
      <template v-if="fullWidth > 768">
        <swiper :options="swiperOption" class="swiperPross">
          <swiper-slide v-for="(item, index) in swiperList" :key="index" class="swiperPic">
            <img :src="item.pic" />
          </swiper-slide>
          <div slot="pagination" class="swiper-pagination" />
        </swiper>
      </template>
      <div class="index_from page-account-container">
        <div class="page-account-top">
          <div class="page-account-top-logo">
            <img v-if="loginLogo" :src="loginLogo" alt="logo" />
            <h2 v-else style="color:#1890ff;font-size:22px;margin:0">若依管理系统</h2>
          </div>
        </div>
        <el-form
          ref="loginForm"
          :model="loginForm"
          :rules="loginRules"
          class="login-form"
          autocomplete="on"
          label-position="left"
          @keyup.enter="handleLogin"
        >
          <el-form-item prop="username">
            <el-input
              ref="username"
              v-model="loginForm.username"
              prefix-icon="el-icon-user"
              placeholder="用户名"
              name="username"
              type="text"
              tabindex="1"
              autocomplete="on"
            />
          </el-form-item>

          <el-form-item prop="password">
            <el-input
              :key="passwordType"
              ref="password"
              v-model="loginForm.password"
              prefix-icon="el-icon-lock"
              :type="passwordType"
              placeholder="密码"
              name="password"
              tabindex="2"
              auto-complete="on"
            />
            <span class="show-pwd" @click="showPwd">
              <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
            </span>
          </el-form-item>

          <el-form-item v-if="captchaEnabled" prop="code">
            <el-input
              v-model="loginForm.code"
              prefix-icon="el-icon-picture-outline"
              placeholder="验证码"
              style="width:63%"
            />
            <div class="login-code" style="width:33%;float:right;height:40px">
              <img :src="codeUrl" @click="getCode" class="login-code-img" />
            </div>
          </el-form-item>

          <div class="acea-row">
            <el-button
              :loading="loading"
              type="primary"
              class="btn"
              style="width: 100%; margin-bottom: 30px"
              @click.native.prevent="handleLogin"
              :disabled="disabled"
              >登 录
            </el-button>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import { getLoginPicApi, getCodeImg } from '@/api/user';
export default {
  name: 'Login',
  data() {
    return {
      swiperList: [],
      loginLogo: '',
      backgroundImages: '',
      backgroundImageMo: require('@/assets/imgs/bg.jpg'),
      fullWidth: document.body.clientWidth,
      swiperOption: {
        pagination: { el: '.pagination' },
        autoplay: { enabled: true, disableOnInteraction: false, delay: 3000 },
      },
      loginForm: {
        username: 'admin',
        password: 'admin123',
        code: '',
        uuid: '',
      },
      loginRules: {
        username: [{ required: true, trigger: 'blur', message: '请输入用户名' }],
        password: [{ required: true, trigger: 'blur', message: '请输入密码' }],
        code: [{ required: false, trigger: 'change', message: '请输入验证码' }],
      },
      passwordType: 'password',
      loading: false,
      redirect: undefined,
      otherQuery: {},
      disabled: false,
      captchaEnabled: true,
      codeUrl: '',
    };
  },
  watch: {
    $route: {
      handler: function (route) {
        const query = route.query;
        if (query) {
          this.redirect = query.redirect;
          this.otherQuery = this.getOtherQuery(query);
        }
      },
      immediate: true,
    },
  },
  created() {
    const _this = this;
    document.onkeydown = function (e) {
      if (_this.$route.path.indexOf('login') !== -1) {
        if (window.event.keyCode === 13) _this.handleLogin();
      }
    };
    window.addEventListener('resize', this.handleResize);
  },
  mounted() {
    this.getInfo();
    this.getCode();
    if (this.loginForm.username === '') {
      this.$refs.username.focus();
    } else if (this.loginForm.password === '') {
      this.$refs.password.focus();
    }
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize);
  },
  methods: {
    handleResize() {
      this.fullWidth = document.body.clientWidth;
    },
    getInfo() {
      getLoginPicApi().then((res) => {
        this.swiperList = res.banner || [];
        this.loginLogo = res.loginLogo || '';
        this.backgroundImages = res.backgroundImage || '';
      });
    },
    getCode() {
      getCodeImg().then((res) => {
        this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled;
        if (this.captchaEnabled) {
          this.codeUrl = 'data:image/gif;base64,' + res.img;
          this.loginForm.uuid = res.uuid;
        }
      });
    },
    showPwd() {
      this.passwordType = this.passwordType === 'password' ? '' : 'password';
      this.$nextTick(() => { this.$refs.password.focus(); });
    },
    handleLogin() {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          this.loading = true;
          this.$store.dispatch('user/login', this.loginForm).then(() => {
            this.$router.push({ path: this.redirect || '/', query: this.otherQuery });
            this.loading = false;
            this.disabled = true;
            this.$store.dispatch('user/getMenus').then(() => {
              this.$router.push({ path: this.redirect || '/dashboard', query: this.otherQuery });
            });
          }).catch((err) => {
            this.loading = false;
            this.disabled = false;
            this.getCode();
          });
        }
      });
    },
    getOtherQuery(query) {
      return Object.keys(query).reduce((acc, cur) => {
        if (cur !== 'redirect') { acc[cur] = query[cur]; }
        return acc;
      }, {});
    },
  },
};
</script>

<style lang="scss" scoped>
$screen-md: 768px;
$font-size-base: 14px;
$animation-time: 0.3s;
$transition-time: 0.2s;
$ease-in-out: ease-in-out;
$subsidiary-color: #808695;

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
  }

  &-top {
    padding: 32px 0;
    &-logo {
      img { max-height: 75px; }
    }
  }
}

@media (min-width: $screen-md) {
  .page-account {
    background-repeat: no-repeat;
    background-position: center;
    background-size: cover;
  }
  .page-account-container {
    padding: 32px 0 24px 0;
    position: relative;
  }
}

.swiperPross {
  border-radius: 6px 0px 0px 6px;
  overflow: hidden;
}
.swiperPross, .swiperPic, .swiperPic img {
  width: 286px;
  height: 100%;
}
.swiperPic img {
  width: 100%;
  height: 100%;
}

.container {
  height: 400px !important;
  padding: 0 !important;
  border-radius: 6px;
  z-index: 1;
  display: flex;
}
.containerSamll {
  width: 670px;
  background: #fff !important;
}
.containerBig {
  width: auto !important;
  background: #f7f7f7 !important;
}

.index_from {
  width: 384px;
  padding: 0 40px 32px 40px;
  height: 400px;
  box-sizing: border-box;
}

.page-account-top {
  padding: 80px 0 20px 0 !important;
  box-sizing: border-box !important;
  display: flex;
  justify-content: center;
}
.page-account-container {
  border-radius: 0px 6px 6px 0px;
}

.login-form {
  position: relative;
  max-width: 100%;
  margin: 0 auto;
  overflow: hidden;
}

.show-pwd {
  position: absolute;
  right: 10px;
  top: 7px;
  font-size: 16px;
  color: #889aa4;
  cursor: pointer;
  user-select: none;
}

.login-code { cursor: pointer; }
.login-code-img { height: 40px; width: 100%; }
</style>