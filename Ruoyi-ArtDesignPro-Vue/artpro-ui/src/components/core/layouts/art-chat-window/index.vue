<!-- Art Bot AI assistant -->
<template>
  <ElDrawer v-model="isDrawerVisible" :size="isMobile ? '100%' : '760px'" :with-header="false">
    <div class="mb-4 flex-cb">
      <div>
        <span class="text-base font-medium">Art Bot</span>
        <div class="mt-1 flex-c gap-1">
          <div
            class="h-2 w-2 rounded-full"
            :class="isOnline ? 'bg-success/100' : 'bg-danger/100'"
          />
          <span class="text-xs text-g-600">{{ isOnline ? '在线' : '暂无可用模型' }}</span>
        </div>
      </div>
      <ElIcon class="c-p" :size="20" @click="closeChat"><Close /></ElIcon>
    </div>

    <div class="flex h-[calc(100%-60px)] min-h-0 gap-3">
      <aside class="flex w-48 shrink-0 flex-col border-r-d pr-3 max-sm:w-32">
        <ElButton
          type="primary"
          :icon="Plus"
          :disabled="!models.length || generating"
          @click="newConversation"
        >
          新建会话
        </ElButton>
        <ElSelect
          v-model="selectedModelId"
          class="mt-3"
          placeholder="选择模型"
          :disabled="generating"
        >
          <ElOption
            v-for="model in models"
            :key="model.modelId"
            :label="model.modelName"
            :value="model.modelId"
          />
        </ElSelect>
        <div class="mt-3 flex-1 overflow-y-auto">
          <div
            v-for="item in conversations"
            :key="item.conversationId"
            class="group mb-1 flex items-center rounded px-2 py-2 text-sm c-p"
            :class="
              item.conversationId === activeConversationId
                ? 'bg-theme/15 text-theme'
                : 'hover:bg-g-200/70'
            "
            @click="selectConversation(item)"
          >
            <span class="min-w-0 flex-1 truncate">{{ item.title }}</span>
            <ElIcon
              class="ml-1 hidden shrink-0 group-hover:block"
              @click.stop="deleteConversation(item)"
            >
              <Delete />
            </ElIcon>
          </div>
        </div>
      </aside>

      <section class="flex min-w-0 flex-1 flex-col">
        <div
          ref="messageContainer"
          class="flex-1 overflow-y-auto border-t-d px-3 py-5 [&::-webkit-scrollbar]:!w-1"
        >
          <div
            v-if="!messages.length"
            class="flex h-full flex-col items-center justify-center text-center text-g-600"
          >
            <ArtSvgIcon icon="ri:robot-2-line" class="mb-3 text-4xl text-theme" />
            <p class="text-sm">选择模型并发送消息，开始新的对话。</p>
          </div>
          <div
            v-for="message in messages"
            :key="message.messageId"
            :class="[
              'mb-5 flex w-full items-start gap-2',
              message.role === 'user' ? 'flex-row-reverse' : 'flex-row'
            ]"
          >
            <ElAvatar
              :size="32"
              :src="message.role === 'user' ? meAvatar : aiAvatar"
              class="shrink-0"
            />
            <div
              :class="[
                'flex max-w-[82%] flex-col',
                message.role === 'user' ? 'items-end' : 'items-start'
              ]"
            >
              <div class="mb-1 text-xs text-g-600">{{
                message.role === 'user' ? userName : 'Art Bot'
              }}</div>
              <div
                v-if="message.role === 'assistant'"
                class="artbot-markdown rounded-md bg-g-300/50 px-3.5 py-2.5 text-sm leading-6 text-g-900"
                v-html="renderMarkdown(message.content)"
              />
              <div
                v-if="message.role !== 'assistant'"
                class="whitespace-pre-wrap rounded-md bg-theme/15 px-3.5 py-2.5 text-sm leading-6 text-g-900"
              >
                {{ message.content }}
              </div>
            </div>
          </div>
        </div>

        <div class="pt-3">
          <ElInput
            v-model="messageText"
            type="textarea"
            :rows="3"
            placeholder="输入消息，Enter 发送，Shift + Enter 换行"
            resize="none"
            :disabled="!models.length"
            @keydown.enter.exact.prevent="sendMessage"
          />
          <div class="mt-3 flex justify-end">
            <ElButton v-if="generating" :icon="VideoPause" @click="stopGeneration"
              >停止生成</ElButton
            >
            <ElButton
              v-else
              type="primary"
              :disabled="!messageText.trim() || !models.length"
              @click="sendMessage"
              v-ripple
            >
              发送
            </ElButton>
          </div>
        </div>
      </section>
    </div>
  </ElDrawer>
</template>

<script setup lang="ts">
  import MarkdownIt from 'markdown-it'
  import DOMPurify from 'dompurify'
  import { Close, Delete, Plus, VideoPause } from '@element-plus/icons-vue'
  import { ElMessage, ElMessageBox } from 'element-plus'
  import { mittBus } from '@/utils/sys'
  import { useUserStore } from '@/store/modules/user'
  import meAvatar from '@/assets/images/avatar/avatar5.webp'
  import aiAvatar from '@/assets/images/avatar/avatar10.webp'
  import {
    createArtBotConversation,
    fetchArtBotConversations,
    fetchArtBotMessages,
    fetchAvailableArtBotModels,
    removeArtBotConversation,
    streamArtBotMessage,
    type ArtBotConversation,
    type ArtBotMessage,
    type ArtBotModel
  } from '@/api/system/artbot'

  defineOptions({ name: 'ArtChatWindow' })
  const { width } = useWindowSize()
  const userStore = useUserStore()
  const markdown = new MarkdownIt({ breaks: true, linkify: true })
  const isMobile = computed(() => width.value < 640)
  const isDrawerVisible = ref(false)
  const models = ref<ArtBotModel[]>([])
  const conversations = ref<ArtBotConversation[]>([])
  const messages = ref<ArtBotMessage[]>([])
  const selectedModelId = ref<number>()
  const activeConversationId = ref<number>()
  const messageText = ref('')
  const generating = ref(false)
  const messageContainer = ref<HTMLElement>()
  let abortController: AbortController | undefined
  let localMessageId = 0

  const isOnline = computed(() => models.value.length > 0)
  const userName = computed(() => userStore.info.userName || '我')
  const renderMarkdown = (content: string) => DOMPurify.sanitize(markdown.render(content || ''))
  const scrollToBottom = () =>
    nextTick(() => {
      if (messageContainer.value)
        messageContainer.value.scrollTop = messageContainer.value.scrollHeight
    })

  async function loadConversations() {
    conversations.value = await fetchArtBotConversations()
  }
  async function selectConversation(conversation: ArtBotConversation) {
    if (generating.value) return
    activeConversationId.value = conversation.conversationId
    selectedModelId.value = conversation.modelId
    messages.value = await fetchArtBotMessages(conversation.conversationId)
    scrollToBottom()
  }
  async function newConversation() {
    if (!selectedModelId.value) return
    const conversation = await createArtBotConversation(selectedModelId.value)
    await loadConversations()
    await selectConversation(conversation)
  }
  async function deleteConversation(conversation: ArtBotConversation) {
    await ElMessageBox.confirm(`确定删除会话“${conversation.title}”吗？`, '提示', {
      type: 'warning'
    })
    await removeArtBotConversation(conversation.conversationId)
    if (activeConversationId.value === conversation.conversationId) {
      activeConversationId.value = undefined
      messages.value = []
    }
    await loadConversations()
  }
  async function sendMessage() {
    const content = messageText.value.trim()
    if (!content || generating.value || !selectedModelId.value) return
    if (!activeConversationId.value) await newConversation()
    if (!activeConversationId.value) return
    const conversationId = activeConversationId.value
    messages.value.push({
      messageId: `local-user-${++localMessageId}`,
      conversationId,
      role: 'user',
      content
    })
    const answer: ArtBotMessage = {
      messageId: `local-ai-${++localMessageId}`,
      conversationId,
      role: 'assistant',
      content: ''
    }
    messages.value.push(answer)
    messageText.value = ''
    generating.value = true
    abortController = new AbortController()
    scrollToBottom()
    try {
      await streamArtBotMessage(
        conversationId,
        content,
        userStore.accessToken,
        (chunk) => {
          answer.content += chunk
          scrollToBottom()
        },
        abortController.signal
      )
      await loadConversations()
    } catch (error) {
      if ((error as Error).name !== 'AbortError') {
        ElMessage.error((error as Error).message || '生成失败')
        if (!answer.content) answer.content = '生成失败，请稍后重试。'
      }
    } finally {
      generating.value = false
      abortController = undefined
    }
  }
  function stopGeneration() {
    abortController?.abort()
    generating.value = false
  }
  async function openChat() {
    isDrawerVisible.value = true
    try {
      models.value = await fetchAvailableArtBotModels()
      selectedModelId.value =
        models.value.find((item) => item.isDefault === '1')?.modelId || models.value[0]?.modelId
      await loadConversations()
      if (conversations.value[0]) await selectConversation(conversations.value[0])
    } catch {
      models.value = []
    }
  }
  function closeChat() {
    stopGeneration()
    isDrawerVisible.value = false
  }
  onMounted(() => mittBus.on('openChat', openChat))
  onUnmounted(() => {
    stopGeneration()
    mittBus.off('openChat', openChat)
  })
</script>

<style scoped>
  .artbot-markdown :deep(p:last-child) {
    margin-bottom: 0;
  }
  .artbot-markdown :deep(pre) {
    overflow-x: auto;
    border-radius: 6px;
    background: var(--art-gray-200);
    padding: 10px;
  }
  .artbot-markdown :deep(code) {
    font-family: monospace;
  }
  .artbot-markdown :deep(ul),
  .artbot-markdown :deep(ol) {
    padding-left: 18px;
  }
</style>
