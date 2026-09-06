<script setup lang="ts">
import profile from '@/assets/images/profile.jpg'
import { ref, nextTick } from "vue";
import socketclient from "@/plugins/socketclient";
import { parseTime } from '@/utils/ruoyi';
import { createAsyncMessage, createMessage } from '@/types/Message';

const message = ref("");
const url = ref("ws://127.0.0.1:8080/websocket/message");
const username = ref("Ricky");
const contacts = ref([
  { name: "梅洛迪·梅西", email: "melody@altbox.com", avatar: profile, online: true, lastMsg: "20小时前" },
  { name: "马克·史密斯", email: "max@kt.com", avatar: profile, online: true, lastMsg: "2小时前" },
  { name: "肖恩·宾", email: "sean@dellito.com", avatar: profile, online: false, lastMsg: "5小时前" },
  // ...可继续添加联系人
  { name: "Ricky", email: "ricky@domain.com", avatar: profile, online: true, lastMsg: "刚刚" }
]);
const currentContact = ref(contacts.value[0]);
const chatMessages = ref<Array<{ from: string, content: string, time: string }>>([
  { from: "Art Bot", content: "不客气，有任何问题随时联系我。", time: "10:11" },
  { from: "Ricky", content: "明白了，谢谢你的帮助!", time: "10:10" }
]);
const chatContainer = ref<HTMLElement>();

function join() {
  socketclient.connect({ url: url.value }).then(() => {
    socketclient.asyncSend(createAsyncMessage('admin', { content: '你好，我是Ricky，很高兴见到你！' })).then(() => {
      console.log("回调");
    });
  })
  socketclient.onMessage((msg) => {
    chatMessages.value.push({
      from: "Art Bot",
      content: msg.content,
      time: parseTime(new Date(), '{h}:{m}')
    });
    nextTick(() => {
      if (chatContainer.value) chatContainer.value.scrollTop = chatContainer.value.scrollHeight;
    });
  });
}

function send() {
  if (!message.value.trim()) return;
  chatMessages.value.push({
    from: username.value,
    content: message.value,
    time: parseTime(new Date(), '{h}:{m}')
  });
  socketclient.send(createMessage('admin', { content: message.value }));
  message.value = "";
  nextTick(() => {
    if (chatContainer.value) chatContainer.value.scrollTop = chatContainer.value.scrollHeight;
  });
}

function selectContact(contact: any) {
  currentContact.value = contact;
  // 可根据实际需求加载该联系人的聊天记录
}

function close() {
  socketclient.close();
}
</script>
<template>
  <div class="app-container">
    <div class="chat-app">
      <div class="sidebar">
        <div class="profile-section">
          <el-avatar :src="profile" size="large" />
          <div class="profile-info">
            <div class="profile-name">伊桑·李特</div>
            <div class="profile-email">ethan@domain.com</div>
          </div>
        </div>
        <div class="contact-list">
          <div v-for="contact in contacts" :key="contact.email"
            :class="['contact-item', { active: contact === currentContact }]" @click="selectContact(contact)">
            <el-avatar :src="contact.avatar" size="small" />
            <div class="contact-info">
              <div class="contact-name">{{ contact.name }}</div>
              <div class="contact-email">{{ contact.email }}</div>
            </div>
            <div class="contact-status">
              <span v-if="contact.online" class="online-dot"></span>
              <span class="last-msg">{{ contact.lastMsg }}</span>
            </div>
          </div>
        </div>
      </div>
      <div class="chat-main">
        <div class="chat-header">
          <div class="chat-title">
            <el-avatar :src="currentContact.avatar" size="small" />
            <span class="chat-name">{{ currentContact.name }}</span>
            <span class="chat-status" v-if="currentContact.online">在线</span>
          </div>
          <div class="chat-actions">
            <el-input class="mr20" placeholder="请输入内容..." v-model="url" />
            <el-button type="primary" @click="join">连接</el-button>
            <el-button type="danger" @click="close">断开连接</el-button>
          </div>
        </div>
        <div class="chat-content" ref="chatContainer">
          <div v-for="(msg, idx) in chatMessages" :key="idx"
            :class="['chat-bubble-row', msg.from === username ? 'self' : 'other']">
            <el-avatar class="bubble-avatar" size="small"
              :src="msg.from === username ? currentContact.avatar : profile" />
            <div class="chat-bubble">
              <div class="bubble-header">
                <span class="bubble-name">{{ msg.from }}</span>
                <span class="bubble-time">{{ msg.time }}</span>
              </div>
              <div class="bubble-content">{{ msg.content }}</div>
            </div>
          </div>
        </div>
        <div class="chat-input-area">
          <el-input v-model="message" type="textarea" :rows="2" placeholder="请输入内容..."
            @keydown.enter.native.prevent="send" />
          <el-button type="primary" @click="send" class="send-btn">发送</el-button>
        </div>
      </div>
    </div>
  </div>
</template>
<style lang="scss" scoped>
.app-container {
  display: flex;
  justify-content: center;
  align-items: center;
}

.chat-app {
  display: flex;
  height: 80vh;
  width: 80vw;
  background: #f5f6fa;
  border-radius: 10px;
  border: 1px solid #e6e6e6;
  overflow: hidden;
}

.sidebar {
  width: 300px;
  background: #fff;
  border-right: 1px solid #e6e6e6;
  display: flex;
  flex-direction: column;

  .profile-section {
    display: flex;
    align-items: center;
    padding: 20px 16px 10px 16px;
    border-bottom: 1px solid #f0f0f0;

    .profile-info {
      margin-left: 12px;


      .profile-name {
        font-weight: bold;
        font-size: 16px;
      }

      .profile-email {
        font-size: 12px;
        color: #888;
      }
    }
  }

  .contact-list {
    overflow-y: auto;
    padding: 12px 5px;


    .contact-item {
      display: flex;
      align-items: center;
      padding: 10px 16px;
      cursor: pointer;
      transition: background 0.2s;

      &.active,
      &:hover {
        background: #f0f4fa;
      }
    }

    .contact-info {
      flex: 1;
      margin-left: 10px;

      .contact-name {
        font-size: 14px;
        font-weight: 500;
      }

      .contact-email {
        font-size: 12px;
        color: #aaa;
      }
    }

    .contact-status {
      display: flex;
      flex-direction: column;
      align-items: flex-end;

      .online-dot {
        width: 8px;
        height: 8px;
        background: #67c23a;
        border-radius: 50%;
        margin-bottom: 4px;
        display: inline-block;
      }

      .last-msg {
        font-size: 11px;
        color: #bbb;
      }
    }
  }
}

.chat-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: #f9fafb;

  .chat-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 18px 24px;
    background: #fff;
    border-bottom: 1px solid #e6e6e6;

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

    .chat-actions {
      width: 80%;
      display: flex;
      align-items: center;
    }
  }

  .chat-content {
    flex: 1;
    overflow-y: auto;
    padding: 30px 40px 20px 40px;
    background: #f9fafb;

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

  .chat-input-area {
    display: flex;
    align-items: flex-end;
    padding: 18px 24px;
    background: #fff;
    border-top: 1px solid #e6e6e6;

    .send-btn {
      margin-left: 12px;
      height: 40px;
    }
  }
}
</style>