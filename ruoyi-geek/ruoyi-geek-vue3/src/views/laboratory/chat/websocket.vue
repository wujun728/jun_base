<script setup lang="ts">
import profile from '@/assets/images/profile.jpg'
import { ref, nextTick } from "vue";
import socketclient from "@/plugins/socketclient";
import { parseTime } from '@/utils/ruoyi';
import { createAsyncMessage, createMessage } from '@/types/Message';
import { ChatLayout, ContactList, ContactItem, ChatApp } from '@/components/Chat';
import { ChatDotSquare, ChatRound } from '@element-plus/icons-vue'
const _contacts = [
  { id: 1, name: "梅洛迪·梅西", email: "melody@altbox.com", avatar: profile, online: true, lastMsg: "20小时前" },
  { id: 2, name: "马克·史密斯", email: "max@kt.com", avatar: profile, online: true, lastMsg: "2小时前" },
  { id: 3, name: "肖恩·宾", email: "sean@dellito.com", avatar: profile, online: false, lastMsg: "5小时前" },
  { id: 4, name: "Ricky", email: "ricky@domain.com", avatar: profile, online: true, lastMsg: "刚刚" }
]
const contacts = _contacts.concat(...Array(10).fill(_contacts)).map((c, id) => ({ ...c, name: c.name + id, id: c.id + id * 100 }))
const currentContact = ref(_contacts[0])
const url = ref("ws://127.0.0.1:8080/websocket/message");

const chatMessages = ref<Array<{ from: string, content: string, time: string }>>([
  { from: "Art Bot", content: "不客气，有任何问题随时联系我。", time: "10:11" },
  { from: "Ricky", content: "明白了，谢谢你的帮助!", time: "10:10" }
]);
function join() {
  socketclient.connect({ url: url.value }).then(() => {
    socketclient.asyncSend(createAsyncMessage('admin', {
      content: '你好，我是Ricky，很高兴见到你！'
    })).then(() => {
      console.log("回调");
    });
  })
  socketclient.onMessage((msg) => {
    chatMessages.value.push({
      from: "Art Bot",
      content: msg.content,
      time: parseTime(new Date(), '{h}:{m}')
    });
  });
}

function close() {
  socketclient.close();
}

const wsDialogVisible = ref(false);
</script>
<template>
  <div class="app-container">
    <chat-layout>
      <template #window-header>
        <div style="display: flex;justify-content: space-between;">
          <div class="profile-section">
            <el-avatar :src="profile" size="small" />
            <div class="profile-info">
              <div class="profile-name">伊桑·李特</div>
              <div class="profile-email">ethan@domain.com</div>
            </div>
          </div>
        </div>
      </template>
      <template #window-sidebar>
        <div><el-button :icon="ChatDotSquare" @click="wsDialogVisible = true" /></div>
        <div><el-button :icon="ChatRound" /></div>
      </template>
      <template #app-sidebar>
        <div class="search-section">
          <el-input placeholder="搜索联系人..."></el-input>
        </div>
        <ContactList :list="contacts" v-model="currentContact">
          <template #default="{ item }">
            <ContactItem :contact="item" />
          </template>
        </ContactList>
      </template>
      <ChatApp :currentContact="currentContact" :chatMessages="chatMessages" />
    </chat-layout>
    <el-dialog v-model="wsDialogVisible" title="设置ws" width="800">
      <el-input class="mr20" placeholder="请输入内容..." v-model="url" />
      <template #footer>
        <el-button type="warning" @click="join">连接</el-button>
        <el-button type="danger" @click="close">断开连接</el-button>
      </template>
    </el-dialog>
  </div>
</template>
<style lang="scss" scoped>
.app-container {
  display: flex;
  justify-content: center;
  align-items: center;
}

.profile-section {
  display: flex;
  align-items: center;
  padding: 10px 16px 10px 16px;
  border-bottom: 1px solid #f0f0f0;

  .profile-info {
    display: flex;
    align-items: end;
    gap: 10px;
    margin-left: 12px;
    color: white;

    .profile-name {
      font-weight: bold;
      font-size: 16px;
    }

    .profile-email {
      font-size: 12px;
    }
  }
}

.search-section {
  height: 53px;
  display: flex;
  align-items: center;
  border-bottom: 1px solid #f0f0f0;
  padding: 0 16px;
}
</style>