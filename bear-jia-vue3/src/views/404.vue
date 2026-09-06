<template>
  <div class="not-found">
    <div class="not-found-content">
      <h1 class="error-code">404</h1>
      <h2 class="error-title">页面未找到</h2>
      <p class="error-desc">抱歉，您访问的页面不存在</p>
      <a-button type="primary" @click="goHome">返回首页</a-button>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const goHome = () => {
  router.push('/home')
}
</script>

<style lang="less" scoped>
.not-found {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 500px;
  padding: 32px;
  background: var(--bg-color);
  
  .not-found-content {
    text-align: center;
    
    .error-code {
      font-size: 120px;
      font-weight: bold;
      color: var(--primary-color);
      margin: 0;
      line-height: 1.2;
    }
    
    .error-title {
      font-size: 24px;
      color: var(--text-primary);
      margin: 16px 0;
    }
    
    .error-desc {
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
