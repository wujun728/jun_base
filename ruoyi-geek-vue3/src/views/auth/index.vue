<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import { Document, ChatDotRound, User } from '@element-plus/icons-vue';
import { getConfigKey } from '@/api/system/config';
import { useRoute, useRouter } from 'vue-router';
import Oauth from "./oauth.vue";
import { getToken } from '@/utils/auth';
import { RoutesAlias } from '@/router/routesAlias';
// 页面加载状态
const pageLoaded = ref(false);
const successCount = ref(0);
const targetSuccessCount = 100;

// 数字增长动画
function startCountAnimation() {
  const duration = 3000; // 动画持续时间（毫秒）
  const step = Math.ceil(targetSuccessCount / (duration / 50)); // 每50毫秒增加的数值
  const interval = setInterval(() => {
    successCount.value += step;
    if (successCount.value >= targetSuccessCount) {
      successCount.value = targetSuccessCount;
      clearInterval(interval);
    }
  }, 50);
}
// 验证码开关
const captchaEnabled = ref(false);
// 注册开关
const register = ref(false);

const route = useRoute();
console.log(route);



onMounted(async () => {
  if (getToken()) {
    useRouter().push(RoutesAlias.Home)
    return;
  }

  captchaEnabled.value = await getConfigKey("sys.account.captchaEnabled").then(res => res.msg === 'true')
  register.value = await getConfigKey("sys.account.registerUser").then(res => res.msg === 'true')
  // 设置页面加载状态为true，触发动画
  pageLoaded.value = true;
  startCountAnimation();
});

const features = [
  { icon: Document, text: '模块解耦' },
  { icon: ChatDotRound, text: '前沿技术' },
  { icon: User, text: '多端支持' }
];

const method = ref("password");
const methods = computed(() => {
  const text = route.name === 'Login' ? '登录' : '注册';
  return [
    { label: `密码${text}`, value: "password" },
    { label: `手机${text}`, value: "phone" },
    { label: `邮箱${text}`, value: "email" }
  ]
});

const title = computed(() => import.meta.env.VITE_APP_TITLE || '后台管理系统');

</script>
<template>
  <div class="auth">
    <div class="background-animation">
      <div v-for="i in 10" :key="i" class="floating-shape"></div>
    </div>
    <div class="container" :class="{ 'appear-animation': pageLoaded }">
      <div class="container-left">
        <h1>欢迎使用GEEK生态</h1>
        <p>打造高效、现代、可扩展的企业级开发平台</p>
        <div class="features">
          <div class="feature-item" v-for="(item, index) in features" :key="index">
            <el-icon>
              <component :is="item.icon" />
            </el-icon>
            <span>{{ item.text }}</span>
          </div>
        </div>
        <div class="success-stories">
          <div class="story-counter">
            <span class="counter">{{ successCount }}+</span>
            <span class="counter-label">成功案例</span>
          </div>
        </div>
      </div>
      <div class="container-right" v-if="pageLoaded">
        <h3 class="title">{{ title }}</h3>
        <el-segmented v-model="method" :options="methods" block />
        <div class="container-form">
          <router-view v-slot="{ Component }">
            <component :is="Component" :register="register" :captchaEnabled="captchaEnabled" :method="method" />
          </router-view>
        </div>
        <Oauth />
      </div>
    </div>
    <!--  底部  -->
    <div class="el-auth-footer">
      <span>Copyright © 2018-2024 若依Geek后台管理系统 All Rights Reserved.</span>
    </div>
  </div>
</template>
<style lang='scss' scoped>
@use 'sass:list';
@use 'sass:map';

.auth {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  width: 100%;
  background: linear-gradient(135deg, var(--el-bg-color) 0%, var(--el-fill-color) 100%);
}

/* 背景动画元素 */
.background-animation {
  position: absolute;
  inset: 0;
  z-index: 0;
  pointer-events: none;

  .floating-shape {
    position: absolute;
    width: 80px;
    height: 80px;
    border-radius: 50%;
    background: var(--el-color-primary-light-8);
    animation: float 15s infinite linear;

    @keyframes float {
      0% {
        transform: translateY(0) scale(1);
        opacity: 0.6;
      }

      50% {
        transform: translateY(-40px) scale(1.2);
        opacity: 0.3;
      }

      100% {
        transform: translateY(0) scale(1);
        opacity: 0.6;
      }
    }

    // 使用Sass循环简化浮动形状配置
    $shapes: (
      (top: 10%, left: 10%, w: 60px, h: 60px, delay: 0s, duration: 20s),
      (top: 20%, right: 20%, w: 100px, h: 100px, delay: 2s, duration: 22s),
      (bottom: 15%, left: 15%, w: 70px, h: 70px, delay: 4s, duration: 18s),
      (bottom: 25%, right: 10%, w: 50px, h: 50px, delay: 6s, duration: 25s),
      (top: 50%, left: 5%, w: 40px, h: 40px, delay: 8s, duration: 30s),
      (top: 40%, right: 5%, w: 90px, h: 90px, delay: 10s, duration: 28s),
      (top: 70%, left: 30%, w: 55px, h: 55px, delay: 12s, duration: 24s),
      (bottom: 40%, right: 25%, w: 65px, h: 65px, delay: 14s, duration: 26s),
      (top: 30%, left: 40%, w: 75px, h: 75px, delay: 16s, duration: 29s),
      (bottom: 10%, right: 40%, w: 45px, h: 45px, delay: 18s, duration: 27s)
    );

  @for $i from 1 through list.length($shapes) {
    $shape: list.nth($shapes, $i);

    &:nth-child(#{$i}) {
      @each $key, $value in $shape {
        @if $key ==top or $key ==bottom or $key ==left or $key ==right {
          #{$key}: #{$value};
        }
      }

      width: map.get($shape, w);
      height: map.get($shape, h);
      animation-delay: map.get($shape, delay);
      animation-duration: map.get($shape, duration);
    }
  }
}
}

.container {
  display: flex;
  width: 85%;

  @media screen and (max-width: 768px) {
    width: 95%;
  }

  max-width: 1200px;
  min-height: 600px;
  height: 70vh;
  background: var(--el-bg-color);
  border-radius: var(--el-border-radius-large);
  box-shadow: var(--el-box-shadow);
  overflow: hidden;
  position: relative;
  z-index: 1;
  opacity: 0;
  transform: translateY(30px);
  transition: all 1s ease;

  &.appear-animation {
    opacity: 1;
    transform: translateY(0);
  }
}

.container-left {
  width: 50%;
  background: linear-gradient(135deg, var(--el-color-primary-light-3) 0%, var(--el-color-primary-dark-3) 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  color: var(--el-color-white);
  padding: 40px;
  text-align: center;

  @media screen and (max-width: 768px) {
    display: none;
  }

  h1 {
    font-size: 2.5rem;
    margin-bottom: 20px;
    font-weight: 600;
    text-shadow: 0 2px 4px var(--el-color-primary-dark-2);
  }

  p {
    font-size: 1.1rem;
    margin-bottom: 40px;
    opacity: 0.9;
    line-height: 1.6;
  }

  .features {
    display: flex;
    justify-content: space-around;
    margin-top: 50px;
    width: 80%;

    .feature-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      transition: all 0.3s ease;

      &:hover {
        transform: translateY(-8px) scale(1.05);

        .el-icon {
          box-shadow: 0 8px 16px var(--el-color-primary-dark-2);
          transform: rotate(5deg);
        }
      }

      .el-icon {
        font-size: 32px;
        margin-bottom: 15px;
        padding: 8px;
        background: var(--el-color-primary-light-8);
        border-radius: 50%;
        box-shadow: 0 4px 12px var(--el-color-primary-light-5);
        transition: all 0.3s ease;
      }

      span {
        font-size: 1rem;
        font-weight: 500;
      }
    }
  }

  .success-stories {
    margin-top: 40px;

    .story-counter {
      display: flex;
      flex-direction: column;
      align-items: center;

      .counter {
        font-size: 3rem;
        font-weight: 700;
        color: var(--el-color-white);
        text-shadow: 0 2px 8px var(--el-color-primary-dark-2);
      }

      .counter-label {
        font-size: 1.2rem;
        margin-top: 5px;
        font-weight: 500;
      }
    }
  }
}

.container-right {
  width: 50%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background-color: #fff;
  position: relative;

  @media screen and (max-width: 768px) {
    width: 100%;
  }

  &::after {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: radial-gradient(circle at center, rgba(255, 255, 255, 0) 70%, rgba(235, 235, 235, 0.5) 100%);
    pointer-events: none;
  }

  .title {
    margin: 30px auto 20px auto;
    text-align: center;
    color: #333;
    font-size: 28px;
    font-weight: 600;
    background: linear-gradient(45deg, var(--el-color-primary-light-3), var(--el-color-primary-dark-3));
    -webkit-background-clip: text;
    background-clip: text;
    -webkit-text-fill-color: transparent;
  }

  .el-segmented {
    background-color: var(--el-color-primary);
    padding: 0;
    box-shadow: var(--el-box-shadow-light);

    :deep(.el-segmented__item) {
      border-radius: 0;
      background-color: var(--el-color-white);
      border-color: var(--el-color-white);
    }
  }
}

.container-form {
  width: 80%;
  max-width: 400px;
  padding: 20px;
}

.el-auth-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #fff;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
}
</style>
