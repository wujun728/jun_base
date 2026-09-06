<script setup lang="ts">
import { getAction } from '@/utils/request';

function oauthLogin(provider: string) {
  console.log(`开始 ${provider} 登录流程`);
  // 打开链接 /system/auth
  getAction<string>(`/system/auth/login/${provider}`)
    .then((response) => {
      if (response && response.msg) {
        window.open(response.msg, '_blank');
      } else {
        console.error('登录链接获取失败', response);
      }
    })
    .catch((error) => {
      console.error(`登录 ${provider} 失败`, error);
    });
}
</script>
<template>
  <!-- 第三方登录区域 -->
  <div class="other-login">
    <div class="divider">
      <span class="divider-text">第三方账号登录</span>
    </div>
    <div class="oauth-buttons">
      <div class="oauth-btn" @click="oauthLogin('gitee')" data-tooltip="Gitee登录">
        <svg-icon icon-class="gitee" class="oauth-icon gitee" />
        <span class="oauth-name">Gitee</span>
      </div>
      <div class="oauth-btn" @click="oauthLogin('github')" data-tooltip="GitHub登录">
        <svg-icon icon-class="github" class="oauth-icon github" />
        <span class="oauth-name">GitHub</span>
      </div>
      <div class="oauth-btn" @click="oauthLogin('qq')" data-tooltip="QQ登录">
        <svg-icon icon-class="qq" class="oauth-icon qq" />
        <span class="oauth-name">QQ</span>
      </div>
      <div class="oauth-btn" @click="oauthLogin('wechat')" data-tooltip="微信登录">
        <svg-icon icon-class="wechat" class="oauth-icon wechat" />
        <span class="oauth-name">微信</span>
      </div>
    </div>
  </div>
</template>
<style lang="scss" scoped>
.other-login {
  max-width: 360px;
  padding: 0 20px;
  width: 100%;

  .divider {
    position: relative;
    text-align: center;
    margin: 20px 0;

    &::before {
      content: '';
      position: absolute;
      top: 50%;
      left: 0;
      width: 100%;
      height: 1px;
      background: linear-gradient(90deg, transparent, #eaeaea, transparent);
      z-index: 1;
    }

    .divider-text {
      position: relative;
      display: inline-block;
      padding: 0 12px;
      background-color: #fff;
      color: #909399;
      font-size: 13px;
      z-index: 2;
    }
  }

  .oauth-buttons {
    display: flex;
    justify-content: space-between;
    margin-top: 15px;

    .oauth-btn {
      display: flex;
      flex-direction: column;
      align-items: center;
      cursor: pointer;
      transition: all 0.5s;
      padding: 8px 16px;
      border-radius: 4px;
      position: relative;

      &:hover {
        background-color: #f5f5f5;
        transform: translateY(-5px);

        &::after {
          content: attr(data-tooltip);
          position: absolute;
          bottom: -25px;
          left: 50%;
          transform: translateX(-50%);
          background-color: rgba(0, 0, 0, 0.7);
          color: white;
          padding: 3px 8px;
          border-radius: 4px;
          font-size: 12px;
          white-space: nowrap;
          opacity: 0;
          animation: fadeIn 0.3s forwards;
        }

        @keyframes fadeIn {
          to {
            opacity: 1;
          }
        }
      }

      .oauth-icon {
        font-size: 28px;
        margin-bottom: 5px;
        transition: transform 0.3s ease;

        &.gitee {
          color: #c71d23;
        }

        &.github {
          color: #24292e;
        }

        &.qq {
          color: #12b7f5;
        }

        &.wechat {
          color: #07c160;
        }
      }

      .oauth-name {
        font-size: 12px;
        color: #606266;
        position: relative;
        transition: all 0.3s ease;

        &::after {
          content: '';
          position: absolute;
          bottom: -2px;
          left: 0;
          width: 0;
          height: 1px;
          background: currentColor;
          transition: width 0.3s ease;
        }

        .oauth-btn:hover & {
          font-weight: 500;

          &::after {
            width: 100%;
          }
        }
      }
    }
  }
}
</style>