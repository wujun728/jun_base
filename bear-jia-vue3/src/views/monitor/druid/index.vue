<template>
  <a-spin :spinning="loading" tip="加载中...">
    <div :style="'height:'+ height">
      <iframe 
        v-if="!showError"
        :src="src" 
        frameborder="no" 
        style="width: 100%;height: 100%" 
        scrolling="auto"
        @load="onIframeLoad"
        @error="onIframeError"
      />
      <div v-else class="error-container">
        <a-result
          status="error"
          title="Druid监控页面加载失败"
          sub-title="请检查以下配置："
        >
          <template #extra>
            <a-space direction="vertical" style="width: 100%">
              <div class="error-details">
                <p>1. 后端服务是否启动（端口8080）</p>
                <p>2. Druid监控是否启用</p>
                <p>3. 网络连接是否正常</p>
                <p>4. 当前请求地址：{{ src }}</p>
              </div>
              <a-button type="primary" @click="retryLoad">重新加载</a-button>
              <a-button @click="openInNewTab">在新窗口中打开</a-button>
            </a-space>
          </template>
        </a-result>
      </div>
    </div>
  </a-spin>
</template>

<script setup name="Druid">
import { ref, onMounted } from 'vue'

const src = ref(import.meta.env.VITE_APP_BASE_API + '/druid/index.html')
const height = ref(document.documentElement.clientHeight - 94.5 + 'px;')
const loading = ref(true)
const showError = ref(false)

onMounted(() => {
  console.log('Druid页面加载，iframe src:', src.value)
  console.log('环境变量 VITE_APP_BASE_API:', import.meta.env.VITE_APP_BASE_API)
  
  setTimeout(() => {
    loading.value = false
  }, 230)

  window.onresize = () => {
    height.value = document.documentElement.clientHeight - 94.5 + 'px;'
  }
})

// iframe加载成功
const onIframeLoad = () => {
  console.log('Druid iframe加载成功')
  loading.value = false
  showError.value = false
}

// iframe加载失败
const onIframeError = () => {
  console.error('Druid iframe加载失败，请检查后端Druid监控是否启用')
  loading.value = false
  showError.value = true
}

// 重新加载
const retryLoad = () => {
  showError.value = false
  loading.value = true
  // 强制刷新iframe
  const iframe = document.querySelector('iframe')
  if (iframe) {
    iframe.src = iframe.src
  }
}

// 在新窗口中打开
const openInNewTab = () => {
  window.open(src.value, '_blank')
}
</script>

<style scoped>
.error-container {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  padding: 20px;
}

.error-details {
  text-align: left;
  margin: 16px 0;
}

.error-details p {
  margin: 8px 0;
  color: #666;
}
</style>
