<script lang="ts" setup>
import { ref } from 'vue';
const message = ref("");
const emit = defineEmits(['send']);
function send(e?: KeyboardEvent) {
  if (e?.isComposing) return;
  if (!message.value.trim()) return;
  emit('send', message.value);
  message.value = "";
}
</script>
<template>
  <div class="chat-input-area">
    <el-input v-model="message" type="textarea" :rows="5" placeholder="请输入内容..." @keydown.enter.exact.prevent="send"
      @keydown.enter.shift.stop />
    <el-button type="primary" @click="send" style="height:30px">发送</el-button>
  </div>
</template>
<style scoped lang="scss">
.chat-input-area {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  padding: 18px 24px;
  background: #fff;
  border-top: 1px solid #e6e6e6;
  gap: 10px;
  height: 192px;

  :deep(.el-textarea__inner) {
    box-shadow: none;
    resize: none;
  }
}
</style>