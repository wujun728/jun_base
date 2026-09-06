<template>
  <div v-if="visible" class="global-loading-container">
    <div class="global-loading-content">
      <a-spin :size="size" :tip="tip">
        <template #indicator>
          <LoadingOutlined style="font-size: 24px" spin />
        </template>
      </a-spin>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { LoadingOutlined } from '@ant-design/icons-vue';

const visible = ref(false);
const tip = ref('加载中...');
const size = ref('large');

// 显示加载
const show = (options = {}) => {
  if (options.tip) tip.value = options.tip;
  if (options.size) size.value = options.size;
  visible.value = true;
};

// 隐藏加载
const hide = () => {
  visible.value = false;
};

// 暴露方法给父组件
defineExpose({
  show,
  hide
});
</script>

<style lang="less" scoped>
.global-loading-container {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.45);
  z-index: 9999;
  display: flex;
  align-items: center;
  justify-content: center;

  .global-loading-content {
    padding: 20px;
    border-radius: 8px;
    background-color: white;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);

    // 暗黑模式适配
    .dark-theme & {
      background-color: #1f1f1f;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.35);
    }
  }
}
</style>
