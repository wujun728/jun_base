<template>
  <div class="app-container chat-container">
    <!-- 左侧边栏 -->
    <div  class="chat-sidebar">
      <div class="sidebar-header">
        <div class="logo-area">
          <el-icon class="el-icon-chat-dot-square logo-icon"/>
          <span class="logo-text">Ai Chat</span>
        </div>
        <el-button
          type="primary"
          class="new-chat-btn"
          @click="startNewChat"
        >
          <el-icon class="el-icon-edit-outline"/>
          <span>新建对话</span>
        </el-button>
      </div>

      <div class="chat-history-container">
        <div class="history-header">
          <span class="history-title">历史对话</span>
          <el-dropdown trigger="click" size="small">
            <el-button type="text">
              <el-icon class="el-icon-more"/>
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>
                  <el-button type="text" @click="cleanChatHistory">
                    清空历史
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button type="text" @click="exportChatHistory">
                    导出记录
                  </el-button>
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
        <div class="chat-history">
          <div
            v-for="(chat, index) in chatHistory"
            :key="index"
            class="chat-history-item"
            :class="{ 'active': currentChatIndex === index }"
            @click="switchChat(index)"
          >
            <el-icon class="el-icon-chat-round chat-icon"/>
            <div class="chat-info">
              <div class="title">{{ chat.title }}</div>
              <div class="time">{{ chat.createTime }}</div>
            </div>
            <el-dropdown trigger="hover" class="chat-actions">
              <el-button type="text" class="action-btn">
                <el-icon class="el-icon-more"/>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item>
                    <el-button type="text" @click="renameChat(index)">
                      重命名
                    </el-button>
                  </el-dropdown-item>
                  <el-dropdown-item>
                    <el-button type="text" @click="deleteChat(index)">
                      删除
                    </el-button>
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </div>
    </div>

    <!-- 主聊天区域 -->
    <div class="chat-main">
      <div class="chat-container-wrapper">
        <!-- 消息列表 -->
        <div class="chat-messages" ref="scrollArea">
          <div
            v-for="(message, index) in messages"
            :key="index"
            :class="['message', message.role === 'user' ? 'message-user' : 'message-ai']"
          >
            <div class="message-avatar">
              <img
                :src="message.role === 'user' ? require('@/assets/images/profile.jpg') :
                    selectedModel ==='qwq-32b'? require('@/assets/images/ai/qwq-ai.png') : require('@/assets/images/ai/deepseek-ai.png')"
                :alt="message.role === 'user' ? '用户' : 'AI'"
              />
            </div>
            <div class="message-content">
              <div class="message-text">{{ message.content }}</div>
              <div class="message-time">{{ parseTime(message.createTime) }}</div>
            </div>
          </div>
        </div>

        <!-- 输入区域 -->
        <div class="chat-input-wrapper">
          <div class="chat-input">
            <div class="input-wrapper">
              <el-input
                v-model="inputMessage"
                type="textarea"
                :rows="3"
                placeholder="请输入您的问题..."
                resize="none"
                @keyup.enter.native="sendMessage"
              />
              <el-button
                type="primary"
                @click="sendMessage"
                :loading="isLoading"
              >
                发送
                <el-icon class="el-icon-s-promotion"/>
              </el-button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 右侧设置面板 -->
    <div class="chat-settings">
      <el-card>
        <template #header>
          <div>对话设置</div>
        </template>

        <el-form>
          <el-form-item label="AI模型">
            <el-select v-model="selectedModel" disabled>
              <el-option value="deepseek-1-8b" label="DeepSeek-R1:8B"/>
              <el-option value="qwq-32b" label="QwQ:32B"/>
            </el-select>
          </el-form-item>

          <el-form-item label="对话模式">
            <el-radio-group v-model="chatMode">
              <el-radio label="base">基础</el-radio>
              <el-radio label="rag">知识库</el-radio>
              <el-radio label="internet">联网</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-form>
      </el-card>
    </div>

    <el-dialog :title="'重命名'" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules">
        <el-form-item prop="title">
          <el-input v-model="form.title"
                    placeholder="请输入名称"
                    clearable />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="confirmRenameChat">确 定</el-button>
        <el-button @click="open = false">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>


import {streamChat} from "@/api/ai/chatBox";
import {getToken} from "@/utils/auth";
import {listDetail} from "@/api/ai/chatSessionDetail";
import {addSession, delAllSession, delSession, listSession, updateSession} from "@/api/ai/chatSession";

export default {
  name: 'AiChat',
  data() {
    return {
      chatHistory: null,
      messages: null,
      inputMessage: '',
      selectedModel: 'deepseek-1-8b',
      chatMode: 'base',
      currentChatIndex: 0,
      isLoading: false,
      eventSource: null,
      open: false,
      form: {},
      rules: {
        title: [
          {required: true, message: '名称不能为空', trigger: 'blur'}
        ],
      },
    };
  },
  async created() {
    // 加载会话历史
    await this.listChatHistory();
    // 初始化会话
    this.initialSession()
  },
  methods: {
    // 初始化会话
    initialSession() {
      if (this.chatHistory === null || this.chatHistory.length === 0) {
        this.messages = [];
        return;
      }
      this.switchChat(0)
    },
    // 加载会话历史
    async listChatHistory() {
      const response = await listSession();
      this.chatHistory = response.rows;
    },
    // 开启新会话
    startNewChat() {
      this.messages = [];
    },
    // 加载会话
    switchChat(index) {
      this.currentChatIndex = index
      let session = this.chatHistory[this.currentChatIndex];
      listDetail({sessionId: session.id}).then(response => {
        this.messages = response.rows;
      });
    },
    // 发送消息
    async sendMessage() {
      if (!this.inputMessage.trim()  || this.isLoading)  return;
      this.isLoading  = true;
      // 新会话
      if (this.messages.length === 0) {
        // 新增会话
        await addSession({title: this.inputMessage});
        // 加载所有会话
        await this.listChatHistory();
        // 加载最新会话消息
        this.currentChatIndex = 0;
      }
      // 新增会话详细
      let userMsg = {sessionId: this.chatHistory[this.currentChatIndex].id, role: 'user', content: this.inputMessage, createTime: Date.now()};
      this.messages.push(userMsg);
      // 清空输入框
      this.inputMessage = '';
      try {
        // 创建 SSE 连接（携带 Token）
        this.eventSource  = await streamChat(
          this.chatHistory[this.currentChatIndex].id,
          this.messages[this.messages.length - 1].content,
          getToken() // 注入认证令牌
        );
        // 初始化助手消息
        let assistantMsg = {sessionId: this.chatHistory[this.currentChatIndex].id, role: 'assistant', content: '', createTime: Date.now()};
        this.messages.push(assistantMsg);
        // 流式数据处理器
        this.eventSource.onmessage = async (event) => {
          if (event.data)  {
            assistantMsg.content  += event.data;
            this.$nextTick(() => this.scrollToBottom());
          }
        };
        // 错误处理
        this.eventSource.onerror = async (e) => {
          this.isLoading  = false;
          this.eventSource.close();
        };
      } catch (error) {
        console.error('API  Error:', error);
        this.isLoading  = false;
      }
    },
    // 清除历史
    cleanChatHistory(){
      this.$modal.confirm('确定要清空历史吗？').then(function() {
        return delAllSession();
      }).then(() => {
        this.listChatHistory();
        this.$modal.msgSuccess("清空历史成功");
      }).catch(() => {});

    },

    exportChatHistory(){

    },
    renameChat(index){
      this.form = {
        id : this.chatHistory[index].id,
        title : this.chatHistory[index].title,
      };
      this.open = true;
    },

    confirmRenameChat() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          updateSession(this.form).then(response => {
            this.$modal.msgSuccess("修改成功");
            this.open = false;
            this.listChatHistory();
          });
        }
      });
    },

    deleteChat(index){
      const chat = this.chatHistory[index];
      this.$modal.confirm('确定要删除会话"' + chat.title + '"吗，删除后聊天记录不可恢复？').then(function () {
        return delSession(chat.id);
      }).then(() => {
        this.listChatHistory();
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {
      })
    },

    scrollToBottom() {
      const container = this.$refs.scrollArea;
      container.scrollTop = container.scrollHeight;
    }
  },
  beforeUnmount() {
    if (this.eventSource) this.eventSource.close();
  }
};
</script>

<style scoped lang="scss">
.chat-container {
  display: flex;
  height: 90vh;
  background-color: #f5f7fa;
}

.chat-sidebar {
  width: 280px;
  display: flex;
  border-radius: 12px;
  flex-direction: column;
  background: #fff;
  border-right: 1px solid #e6e6e6;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);

  .sidebar-header {
    padding: 16px;
    border-bottom: 1px solid #eee;

    .logo-area {
      display: flex;
      align-items: center;
      margin-bottom: 16px;

      .logo-icon {
        color: #1890ff;
        margin-left: 40px;
        margin-right: 20px;
        font-size: 25px;
      }

      .logo-text {
        font-size: 20px;
        font-weight: 600;
        color: #333;
      }
    }

    .new-chat-btn {
      width: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 8px;
      height: 40px;
      border-radius: 8px;
      transition: all 0.3s;

      &:hover {
        transform: translateY(-1px);
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
      }
    }
  }

  .chat-history-container {
    flex: 1;
    overflow: hidden;
    padding: 16px 8px;

    .history-header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 0 8px;
      margin-bottom: 12px;

      .history-title {
        font-size: 13px;
        color: #666;
        font-weight: 500;
      }
    }
  }
  .chat-history {
    height: 95%;
    overflow-y: auto;
    &-item {
      display: flex;
      align-items: center;
      padding: 10px 12px;
      margin: 2px 0;
      border-radius: 8px;
      cursor: pointer;
      transition: all 0.2s;
      position: relative;

      &:hover {
        background: #f5f7fa;

        .chat-actions {
          opacity: 1;
        }
      }

      &.active {
        background: #e6f4ff;

        .title {
          color: #1890ff;
        }
      }

      .chat-icon {
        font-size: 18px;
        color: #666;
        margin-right: 15px;
      }

      .chat-info {
        flex: 1;
        min-width: 0;

        .title {
          text-align: left;
          font-size: 14px;
          color: #333;
          margin-bottom: 5px;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
        }

        .time {
          font-size: 12px;
          color: #999;
        }
      }

      .chat-actions {
        opacity: 0;
        transition: opacity 0.2s;

        .action-btn {
          padding: 2px;
        }
      }
    }
  }
}

.chat-main {
  flex: 1;
  margin-left: 15px;
  margin-right: 15px;
  border-radius: 12px;
  background: #fff;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  overflow: hidden;

  .chat-container-wrapper {
    display: flex;
    flex-direction: column;
    height: 100%;
  }
}

.chat-messages {
  flex: 1;
  padding: 20px;
  overflow-y: auto;

  .message {
    display: flex;
    margin-bottom: 20px;

    &-user {
      flex-direction: row-reverse;

      .message-content {
        margin-right: 12px;

        .message-text {
          background: #e3f2fd;
          border-radius: 12px 2px 12px 12px;
        }
      }
    }

    &-ai .message-content {
      margin-left: 12px;

      .message-text {
        background: #f5f5f5;
        border-radius: 2px 12px 12px 12px;
      }
    }

    &-avatar {
      width: 40px;
      height: 40px;
      border-radius: 50%;
      overflow: hidden;

      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }
    }

    &-content {
      max-width: 70%;
    }

    &-text {
      padding: 12px 16px;
      font-size: 14px;
      line-height: 1.5;
    }

    &-time {
      margin-top: 4px;
      font-size: 12px;
      color: #999;
    }
  }
}

.chat-input-wrapper {
  flex-shrink: 0;
  background: #fff;
  border-top: 1px solid #eee;

  .chat-input {
    padding: 20px;

    .input-wrapper {
      display: flex;
      gap: 10px;

      .el-input {
        flex: 1;
      }

      .el-button {
        align-self: flex-end;
      }
    }
  }
}

.chat-settings {
  width: 300px;
  .el-card {
    height: 100%;
  }
}
</style>
