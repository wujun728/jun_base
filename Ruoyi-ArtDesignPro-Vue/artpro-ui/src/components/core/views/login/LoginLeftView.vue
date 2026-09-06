<!-- 登录、注册、忘记密码左侧品牌区域 -->
<template>
  <div class="login-left-view">
    <div class="background-grid"></div>
    <div class="background-glow"></div>

    <div class="logo">
      <ArtLogo class="icon" size="46" />
      <h1 class="title">{{ getSiteConfig('site.name', AppConfig.systemInfo.name) }}</h1>
    </div>

    <div v-if="!hideContent" class="content-wrap">
      <p class="eyebrow">DESIGNED FOR MODERN BUSINESS</p>
      <h1>{{ getSiteConfig('site.login-left-title', $t('login.leftView.title')) }}</h1>
      <p class="description">{{
        getSiteConfig('site.login-left-sub-title', $t('login.leftView.subTitle'))
      }}</p>
      <div class="carousel-dots" aria-hidden="true">
        <span class="active"></span>
        <span></span>
        <span></span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
  import AppConfig from '@/config'
  import { getSiteConfig } from '@/utils/site-config'

  defineProps<{
    hideContent?: boolean
  }>()
</script>

<style lang="scss" scoped>
  .login-left-view {
    position: relative;
    box-sizing: border-box;
    width: 60vw;
    height: 100%;
    padding: 26px 36px;
    overflow: hidden;
    background-color: color-mix(in srgb, var(--default-box-color) 95%, var(--theme-color));
    border-right: 1px solid color-mix(in srgb, var(--art-gray-300) 72%, transparent);

    .background-grid {
      position: absolute;
      inset: 0;
      pointer-events: none;
      background-image:
        linear-gradient(
          color-mix(in srgb, var(--theme-color) 8%, transparent) 1px,
          transparent 1px
        ),
        linear-gradient(
          90deg,
          color-mix(in srgb, var(--theme-color) 8%, transparent) 1px,
          transparent 1px
        );
      background-size: 64px 64px;
      mask-image: linear-gradient(to right, #000 76%, transparent);
    }

    .background-glow {
      position: absolute;
      bottom: -220px;
      left: -180px;
      width: 620px;
      height: 620px;
      pointer-events: none;
      background: radial-gradient(
        circle,
        color-mix(in srgb, var(--theme-color) 42%, transparent) 0%,
        color-mix(in srgb, var(--theme-color) 16%, transparent) 38%,
        transparent 68%
      );
      filter: blur(18px);
    }

    .logo {
      position: relative;
      z-index: 2;
      display: flex;
      align-items: center;

      .title {
        margin-left: 12px;
        font-size: 20px;
        font-weight: 600;
        color: var(--art-gray-900);
      }
    }

    .content-wrap {
      position: absolute;
      top: 46%;
      left: 11.5%;
      z-index: 2;
      max-width: 760px;
      transform: translateY(-50%);
      animation: slideInLeft 0.6s cubic-bezier(0.25, 0.46, 0.45, 0.94) forwards;

      .eyebrow {
        margin-bottom: 20px;
        font-size: 11px;
        font-weight: 700;
        color: var(--theme-color);
        letter-spacing: 4px;
      }

      h1 {
        font-size: clamp(32px, 3vw, 48px);
        font-weight: 700;
        line-height: 1.24;
        color: var(--art-gray-900);
        letter-spacing: -1px;
      }

      .description {
        max-width: 720px;
        margin-top: 24px;
        font-size: 20px;
        line-height: 1.8;
        color: var(--art-gray-700);
      }

      .carousel-dots {
        display: flex;
        gap: 14px;
        margin-top: 34px;

        span {
          width: 22px;
          height: 5px;
          background-color: var(--art-gray-400);
          border-radius: 999px;

          &.active {
            width: 66px;
            background-color: var(--theme-color);
          }
        }
      }
    }
  }

  .dark .login-left-view {
    background-color: color-mix(in srgb, var(--default-box-color) 92%, var(--theme-color));

    .background-grid {
      opacity: 0.72;
    }

    .background-glow {
      opacity: 0.68;
    }
  }

  @keyframes slideInLeft {
    from {
      opacity: 0;
      transform: translate(-30px, -50%);
    }

    to {
      opacity: 1;
      transform: translate(0, -50%);
    }
  }

  @media only screen and (width <= 1600px) {
    .login-left-view {
      width: 58vw;

      .content-wrap {
        left: 10%;
        max-width: 680px;

        .description {
          max-width: 620px;
          font-size: 18px;
        }
      }
    }
  }

  @media only screen and (width <= 1180px) {
    .login-left-view {
      width: auto;
      height: auto;
      padding: 0;
      background: transparent;
      border: 0;

      .logo,
      .background-grid,
      .background-glow,
      .content-wrap {
        display: none;
      }
    }
  }
</style>
