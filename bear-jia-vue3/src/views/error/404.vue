<template>
  <div class="error-page">
    <div class="error-content">
      <h1>404</h1>
      <h2>页面未找到</h2>
      <p>抱歉，您访问的页面不存在或已被移除</p>
      <a-button type="primary" @click="goHome">返回首页</a-button>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';

const router = useRouter();

const goHome = () => {
  router.push('/');
};
</script>

<style lang="less" scoped>
.error-page {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: var(--bg-color);

  .error-content {
    text-align: center;

    h1 {
      font-size: 120px;
      color: var(--primary-color);
      margin-bottom: 16px;
    }

    h2 {
      font-size: 24px;
      color: var(--text-primary);
      margin-bottom: 16px;
    }

    p {
      font-size: 16px;
      color: var(--text-secondary);
      margin-bottom: 24px;
    }
    
    .ant-btn {
      &.ant-btn-primary {
        background: var(--primary-color);
        border-color: var(--primary-color);
        
        &:hover {
          background: var(--primary-color-hover);
          border-color: var(--primary-color-hover);
        }
      }
    }
  }
}
</style> 