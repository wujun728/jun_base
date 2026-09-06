<template>
  <a-drawer
    :open="visible"
    @update:open="$emit('update:visible', $event)"
    title="在线聊天"
    placement="right"
    width="400"
    class="chat-panel-drawer"
  >
    <div class="chat-container">
      <!-- 联系人列表 -->
      <div class="contact-list" v-if="!currentChat">
        <div class="contact-search">
          <a-input
            v-model:value="searchKeyword"
            placeholder="搜索联系人..."
            size="small"
          >
            <template #prefix>
              <SearchOutlined />
            </template>
          </a-input>
        </div>
        
        <div class="contact-items">
          <div
            v-for="contact in filteredContacts"
            :key="contact.id"
            class="contact-item"
            @click="startChat(contact)"
          >
            <div class="contact-avatar">
              <a-badge :dot="contact.online" color="green">
                <a-avatar :src="contact.avatar">
                  {{ contact.name.charAt(0) }}
                </a-avatar>
              </a-badge>
            </div>
            <div class="contact-info">
              <div class="contact-name">{{ contact.name }}</div>
              <div class="contact-status">{{ contact.online ? '在线' : '离线' }}</div>
            </div>
            <div class="contact-unread" v-if="contact.unreadCount > 0">
              <a-badge :count="contact.unreadCount" />
            </div>
          </div>
        </div>
      </div>

      <!-- 聊天界面 -->
      <div class="chat-interface" v-if="currentChat">
        <!-- 聊天头部 -->
        <div class="chat-header">
          <a-button type="text" @click="backToContacts">
            <ArrowLeftOutlined />
          </a-button>
          <div class="chat-user-info">
            <a-avatar :src="currentChat.avatar">
              {{ currentChat.name.charAt(0) }}
            </a-avatar>
            <div class="user-details">
              <div class="user-name">{{ currentChat.name }}</div>
              <div class="user-status">{{ currentChat.online ? '在线' : '离线' }}</div>
            </div>
          </div>
        </div>

        <!-- 消息列表 -->
        <div class="message-list" ref="messageListRef">
          <div
            v-for="message in currentMessages"
            :key="message.id"
            class="message-item"
            :class="{ 'own-message': message.isSelf }"
          >
            <div class="message-avatar" v-if="!message.isSelf">
              <a-avatar :src="currentChat.avatar" size="small">
                {{ currentChat.name.charAt(0) }}
              </a-avatar>
            </div>
            <div class="message-content">
              <div class="message-bubble">
                <div class="message-text">{{ message.content }}</div>
                <div class="message-time">{{ message.time }}</div>
              </div>
            </div>
            <div class="message-avatar" v-if="message.isSelf">
              <a-avatar src="/profile.jpg" size="small">
                我
              </a-avatar>
            </div>
          </div>
        </div>

        <!-- 输入框 -->
        <div class="message-input">
          <div class="input-toolbar">
            <a-button type="text" size="small">
              <SmileOutlined />
            </a-button>
            <a-button type="text" size="small">
              <PaperClipOutlined />
            </a-button>
            <a-button type="text" size="small">
              <PictureOutlined />
            </a-button>
          </div>
          <div class="input-area">
            <a-textarea
              v-model:value="messageInput"
              placeholder="输入消息..."
              :rows="2"
              @keydown.enter.prevent="sendMessage"
            />
            <a-button type="primary" @click="sendMessage" :disabled="!messageInput.trim()">
              发送
            </a-button>
          </div>
        </div>
      </div>
    </div>
  </a-drawer>
</template>

<script setup>
import { ref, computed, nextTick, watch } from 'vue';
import {
  SearchOutlined,
  ArrowLeftOutlined,
  SmileOutlined,
  PaperClipOutlined,
  PictureOutlined
} from '@ant-design/icons-vue';

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  }
});

const emit = defineEmits(['update:visible']);

// 搜索关键词
const searchKeyword = ref('');
const messageInput = ref('');
const currentChat = ref(null);
const messageListRef = ref(null);

// 联系人列表
const contacts = ref([
  {
    id: 1,
    name: '张三',
    avatar: '',
    online: true,
    unreadCount: 2
  },
  {
    id: 2,
    name: '李四',
    avatar: '',
    online: false,
    unreadCount: 0
  },
  {
    id: 3,
    name: '王五',
    avatar: '',
    online: true,
    unreadCount: 1
  },
  {
    id: 4,
    name: '赵六',
    avatar: '',
    online: true,
    unreadCount: 0
  }
]);

// 聊天消息
const chatMessages = ref({
  1: [
    {
      id: 1,
      content: '你好，有个问题想请教一下',
      time: '14:30',
      isSelf: false
    },
    {
      id: 2,
      content: '好的，什么问题？',
      time: '14:31',
      isSelf: true
    },
    {
      id: 3,
      content: '关于用户权限的配置',
      time: '14:32',
      isSelf: false
    }
  ],
  3: [
    {
      id: 1,
      content: '会议时间确定了吗？',
      time: '15:00',
      isSelf: false
    }
  ]
});

// 过滤联系人
const filteredContacts = computed(() => {
  if (!searchKeyword.value) {
    return contacts.value;
  }
  return contacts.value.filter(contact =>
    contact.name.toLowerCase().includes(searchKeyword.value.toLowerCase())
  );
});

// 当前聊天消息
const currentMessages = computed(() => {
  if (!currentChat.value) return [];
  return chatMessages.value[currentChat.value.id] || [];
});

// 开始聊天
const startChat = (contact) => {
  currentChat.value = contact;
  contact.unreadCount = 0;
  nextTick(() => {
    scrollToBottom();
  });
};

// 返回联系人列表
const backToContacts = () => {
  currentChat.value = null;
};

// 发送消息
const sendMessage = () => {
  if (!messageInput.value.trim() || !currentChat.value) return;

  const newMessage = {
    id: Date.now(),
    content: messageInput.value.trim(),
    time: new Date().toLocaleTimeString('zh-CN', { 
      hour: '2-digit', 
      minute: '2-digit' 
    }),
    isSelf: true
  };

  if (!chatMessages.value[currentChat.value.id]) {
    chatMessages.value[currentChat.value.id] = [];
  }
  
  chatMessages.value[currentChat.value.id].push(newMessage);
  messageInput.value = '';

  nextTick(() => {
    scrollToBottom();
  });

  // 模拟对方回复
  setTimeout(() => {
    const replyMessage = {
      id: Date.now() + 1,
      content: '收到，我来处理一下',
      time: new Date().toLocaleTimeString('zh-CN', { 
        hour: '2-digit', 
        minute: '2-digit' 
      }),
      isSelf: false
    };
    chatMessages.value[currentChat.value.id].push(replyMessage);
    nextTick(() => {
      scrollToBottom();
    });
  }, 1000);
};

// 滚动到底部
const scrollToBottom = () => {
  if (messageListRef.value) {
    messageListRef.value.scrollTop = messageListRef.value.scrollHeight;
  }
};

// 监听抽屉打开，重置状态
watch(() => props.visible, (newVal) => {
  if (newVal) {
    currentChat.value = null;
    searchKeyword.value = '';
    messageInput.value = '';
  }
});
</script>

<style scoped>
.chat-panel-drawer {
  .chat-container {
    height: 100%;
    display: flex;
    flex-direction: column;

    .contact-list {
      height: 100%;
      display: flex;
      flex-direction: column;

      .contact-search {
        margin-bottom: 16px;
      }

      .contact-items {
        flex: 1;
        overflow-y: auto;

        .contact-item {
          display: flex;
          align-items: center;
          padding: 12px 0;
          border-bottom: 1px solid #f0f0f0;
          cursor: pointer;
          transition: background 0.3s;

          &:hover {
            background: #fafafa;
          }

          .contact-avatar {
            margin-right: 12px;
          }

          .contact-info {
            flex: 1;

            .contact-name {
              font-weight: 500;
              margin-bottom: 4px;
            }

            .contact-status {
              font-size: 12px;
              color: #999;
            }
          }

          .contact-unread {
            margin-left: 8px;
          }
        }
      }
    }

    .chat-interface {
      height: 100%;
      display: flex;
      flex-direction: column;

      .chat-header {
        display: flex;
        align-items: center;
        padding: 12px 0;
        border-bottom: 1px solid #f0f0f0;
        margin-bottom: 16px;

        .chat-user-info {
          display: flex;
          align-items: center;
          margin-left: 12px;

          .user-details {
            margin-left: 12px;

            .user-name {
              font-weight: 500;
            }

            .user-status {
              font-size: 12px;
              color: #999;
            }
          }
        }
      }

      .message-list {
        flex: 1;
        overflow-y: auto;
        padding: 0 8px;

        .message-item {
          display: flex;
          margin-bottom: 16px;

          &.own-message {
            justify-content: flex-end;

            .message-content {
              .message-bubble {
                background: var(--primary-color);
                color: white;
              }
            }
          }

          .message-avatar {
            margin: 0 8px;
          }

          .message-content {
            max-width: 70%;

            .message-bubble {
              background: #f5f5f5;
              border-radius: 12px;
              padding: 8px 12px;

              .message-text {
                word-break: break-word;
                line-height: 1.4;
              }

              .message-time {
                font-size: 11px;
                color: rgba(0, 0, 0, 0.45);
                margin-top: 4px;
              }
            }
          }

          &.own-message .message-content .message-bubble .message-time {
            color: rgba(255, 255, 255, 0.7);
          }
        }
      }

      .message-input {
        border-top: 1px solid #f0f0f0;
        padding-top: 12px;

        .input-toolbar {
          margin-bottom: 8px;

          .ant-btn {
            margin-right: 8px;
          }
        }

        .input-area {
          display: flex;
          gap: 8px;

          .ant-input {
            flex: 1;
          }
        }
      }
    }
  }
}
</style>
