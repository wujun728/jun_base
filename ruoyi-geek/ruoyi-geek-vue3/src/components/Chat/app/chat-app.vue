<script lang="ts" setup>
import { nextTick, ref, watch } from 'vue';
import ChatInput from '../components/chat-input.vue';
import { createMessage } from '@/types/Message';
import socketclient from "@/plugins/socketclient";
import { parseTime } from '@/utils/ruoyi';
const message = ref("");
const username = ref("Ricky");
import profile from '@/assets/images/profile.jpg'
const chatContainer = ref<HTMLElement>();

const props = withDefaults(defineProps<{
  currentContact: { name: string; avatar: string; online?: boolean };
  chatMessages: Array<{ from: string; content: string; time: string }>;
}>(), {
  currentContact: () => ({ name: "Ricky", avatar: profile }),
  chatMessages: () => []
})

function send(msg: string) {
  props.chatMessages.push({
    from: username.value,
    content: msg,
    time: parseTime(new Date(), '{h}:{m}')
  });
  socketclient.send(createMessage('admin', { content: msg }));
  message.value = "";
}
watch(() => props.chatMessages.length, () => {
  nextTick(() => {
    if (chatContainer.value) chatContainer.value.scrollTop = chatContainer.value.scrollHeight;
  })
})
</script>
<template>
  <div class="chat-main">
    <div class="chat-header">
      <div class="chat-title">
        <el-avatar :src="currentContact.avatar" size="small" />
        <span class="chat-name">{{ currentContact.name }}</span>
        <span class="chat-status" v-if="currentContact.online">在线</span>
      </div>
    </div>
    <div class="chat-content" ref="chatContainer">
      <div v-for="(msg, idx) in chatMessages" :key="idx"
        :class="['chat-bubble-row', msg.from === username ? 'self' : 'other']">
        <el-avatar class="bubble-avatar" size="small" :src="msg.from === username ? currentContact.avatar : profile" />
        <div class="chat-bubble">
          <div class="bubble-header">
            <span class="bubble-name">{{ msg.from }}</span>
            <span class="bubble-time">{{ msg.time }}</span>
          </div>
          <div class="bubble-content">{{ msg.content }}</div>
        </div>
      </div>
    </div>
    <ChatInput @send="send" />
  </div>
</template>
<style lang="scss" scoped>
.chat-main {
  display: flex;
  flex-direction: column;
  height: 100%;

  .chat-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 24px;
    background: #fff;
    border-bottom: 1px solid #e6e6e6;
    height: 53px;

    .chat-title {
      display: flex;
      align-items: center;

      .chat-name {
        font-weight: 600;
        margin-left: 10px;
      }

      .chat-status {
        color: #67c23a;
        font-size: 12px;
        margin-left: 8px;
      }
    }
  }

  .chat-content {
    overflow-y: auto;
    padding: 30px 20px 20px 20px;
    flex: 1;

    .chat-bubble-row {
      display: flex;
      align-items: flex-end;
      margin-bottom: 18px;

      &.self {
        flex-direction: row-reverse;

        & .chat-bubble {
          background: #e6f7ff;
        }
      }

      .bubble-avatar {
        margin: 0 10px;
      }

      .chat-bubble {
        max-width: 420px;
        background: #fff;
        border-radius: 8px;
        padding: 12px 18px;
        box-shadow: 0 1px 2px rgba(0, 0, 0, 0.03);
        position: relative;


        .bubble-header {
          display: flex;
          justify-content: space-between;
          margin-bottom: 4px;

          .bubble-name {
            font-size: 13px;
            font-weight: 500;
            color: #409eff;
          }

          .bubble-time {
            font-size: 11px;
            color: #bbb;
          }
        }

        .bubble-content {
          font-size: 15px;
          color: #333;
          word-break: break-all;
        }
      }
    }
  }
}
</style>