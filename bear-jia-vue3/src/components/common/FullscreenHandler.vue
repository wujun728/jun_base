<template>
  <div class="fullscreen-handler">
    <!-- 全屏时的退出提示 -->
    <transition name="fade">
      <div v-if="isFullscreen" class="fullscreen-tip">
        <div class="tip-content">
          <FullscreenExitOutlined />
          <span>按 ESC 键或点击右上角按钮退出全屏</span>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import { FullscreenExitOutlined } from '@ant-design/icons-vue';

const isFullscreen = ref(false);

// 检查全屏状态
const checkFullscreenStatus = () => {
  isFullscreen.value = !!(
    document.fullscreenElement ||
    document.webkitFullscreenElement ||
    document.mozFullScreenElement ||
    document.msFullscreenElement
  );
};

// 全屏状态变化监听
const handleFullscreenChange = () => {
  checkFullscreenStatus();
};

// ESC 键退出全屏
const handleKeydown = (event) => {
  if (event.key === 'Escape' && isFullscreen.value) {
    exitFullscreen();
  }
};

// 进入全屏
const enterFullscreen = () => {
  const element = document.documentElement;
  if (element.requestFullscreen) {
    element.requestFullscreen();
  } else if (element.webkitRequestFullscreen) {
    element.webkitRequestFullscreen();
  } else if (element.mozRequestFullScreen) {
    element.mozRequestFullScreen();
  } else if (element.msRequestFullscreen) {
    element.msRequestFullscreen();
  }
};

// 退出全屏
const exitFullscreen = () => {
  if (document.exitFullscreen) {
    document.exitFullscreen();
  } else if (document.webkitExitFullscreen) {
    document.webkitExitFullscreen();
  } else if (document.mozCancelFullScreen) {
    document.mozCancelFullScreen();
  } else if (document.msExitFullscreen) {
    document.msExitFullscreen();
  }
};

// 切换全屏状态
const toggleFullscreen = () => {
  if (isFullscreen.value) {
    exitFullscreen();
  } else {
    enterFullscreen();
  }
};

// 暴露方法给父组件
defineExpose({
  isFullscreen,
  toggleFullscreen,
  enterFullscreen,
  exitFullscreen
});

onMounted(() => {
  // 监听全屏状态变化
  document.addEventListener('fullscreenchange', handleFullscreenChange);
  document.addEventListener('webkitfullscreenchange', handleFullscreenChange);
  document.addEventListener('mozfullscreenchange', handleFullscreenChange);
  document.addEventListener('MSFullscreenChange', handleFullscreenChange);
  
  // 监听键盘事件
  document.addEventListener('keydown', handleKeydown);
  
  // 初始检查
  checkFullscreenStatus();
});

onUnmounted(() => {
  // 清理事件监听
  document.removeEventListener('fullscreenchange', handleFullscreenChange);
  document.removeEventListener('webkitfullscreenchange', handleFullscreenChange);
  document.removeEventListener('mozfullscreenchange', handleFullscreenChange);
  document.removeEventListener('MSFullscreenChange', handleFullscreenChange);
  document.removeEventListener('keydown', handleKeydown);
});
</script>

<style scoped>
.fullscreen-handler {
  .fullscreen-tip {
    position: fixed;
    top: 20px;
    left: 50%;
    transform: translateX(-50%);
    z-index: 9999;
    background: rgba(0, 0, 0, 0.8);
    color: white;
    padding: 12px 20px;
    border-radius: 6px;
    font-size: 14px;
    
    .tip-content {
      display: flex;
      align-items: center;
      gap: 8px;
      
      .anticon {
        font-size: 16px;
      }
    }
  }
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
