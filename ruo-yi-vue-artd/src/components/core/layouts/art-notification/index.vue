<!-- 通知组件 -->
<template>
  <div
    class="art-notification-panel art-card-sm !shadow-xl"
    :style="{
      transform: show ? 'scaleY(1)' : 'scaleY(0.9)',
      opacity: show ? 1 : 0
    }"
    v-show="visible"
    @click.stop>
    <div class="flex-cb mt-3.5 px-3.5">
      <span class="text-base font-medium text-g-800">
        {{ $t('notice.title') }}
      </span>
      <span
        class="c-p rounded px-1.5 py-1 text-xs text-g-800 select-none hover:bg-g-200"
        @click="readNoticeAll">
        {{ $t('notice.btnRead') }}
      </span>
    </div>

    <div class="h-[370px] w-full">
      <div class="scrollbar-thin h-[calc(100%-60px)] overflow-y-scroll">
        <!-- 通知 -->
        <ul>
          <li
            v-for="(item, index) in noticeList"
            :key="index"
            class="flex-c c-p box-border px-3.5 py-3.5 last:border-b-0 hover:bg-g-200/60"
            @click="viewNotice(item)">
            <div
              class="flex-cc size-9 rounded-lg text-center leading-9"
              :class="[getNoticeStyle[item.noticeType!].iconClass]">
              <ArtSvgIcon
                class="!bg-transparent text-lg"
                :icon="getNoticeStyle[item.noticeType!].icon" />
            </div>
            <div class="ml-3.5 w-[calc(100%-45px)]">
              <h4
                class="text-sm leading-5.5 font-normal text-g-900"
                :class="{ 'text-g-600!': item.isRead }">
                {{ item.noticeTitle }}
              </h4>
              <p class="mt-1.5 text-xs text-g-500">{{ item.createTime }}</p>
            </div>
          </li>
        </ul>

        <!-- 空状态 -->
        <div
          v-show="noticeList.length === 0"
          class="relative top-25 h-full !bg-transparent text-center text-g-500">
          <ArtSvgIcon icon="system-uicons:inbox" class="text-5xl" />
          <p class="mt-3.5 !bg-transparent text-xs">
            {{ $t('notice.text[0]') }}
          </p>
        </div>
      </div>
    </div>
    <ArtNotificationDetail ref="noticeViewRef" />
  </div>
</template>

<script setup lang="ts">
import type { SysNotice } from '@/types/api/system/notice'

import { ref, watch } from 'vue'

import { markNoticeReadAll } from './api'
import ArtNotificationDetail from './detail.vue'

defineOptions({ name: 'ArtNotification' })

const props = defineProps<{
  value: boolean
  noticeList: SysNotice[]
  unreadCount: number
}>()

const noticeViewRef =
  useTemplateRef<InstanceType<typeof ArtNotificationDetail>>('noticeViewRef')
const show = ref(false)
const visible = ref(false)

// 查看通知详情
function viewNotice(item: SysNotice) {
  if (!noticeViewRef.value) return
  noticeViewRef.value.open(item)
}

// 标记所有已读
function readNoticeAll() {
  const ids = props.noticeList.map((n: SysNotice) => n.noticeId).join(',')
  if (!ids) return
  markNoticeReadAll(ids).catch(() => {})
  emit('allReadNotice')
}

const getNoticeStyle = {
  '1': {
    icon: 'ri:volume-down-line',
    iconClass: 'bg-info/12 text-info'
  },
  '2': {
    icon: 'ri:notification-3-line',
    iconClass: 'bg-theme/12 text-theme'
  }
}

// 动画管理
const useNotificationAnimation = () => {
  const showNotice = (open: boolean) => {
    if (open) {
      visible.value = true
      setTimeout(() => {
        show.value = true
      }, 5)
    } else {
      show.value = false
      setTimeout(() => {
        visible.value = false
      }, 350)
    }
  }

  return {
    showNotice
  }
}

const { showNotice } = useNotificationAnimation()
// 监听属性变化
watch(
  () => props.value,
  (newValue) => {
    showNotice(newValue)
  }
)

const emit = defineEmits<{
  allReadNotice: []
  'update:value': [value: boolean]
}>()
</script>

<style scoped>
@reference '@styles/core/tailwind.css';

.art-notification-panel {
  @apply absolute top-14.5 right-5 h-90 w-90 origin-top overflow-hidden transition-all duration-300 will-change-[top,left] max-[640px]:top-[65px] max-[640px]:right-0 max-[640px]:h-[80vh] max-[640px]:w-full;
}

.bar-active {
  color: var(--theme-color) !important;
  border-bottom: 2px solid var(--theme-color);
}

.scrollbar-thin::-webkit-scrollbar {
  width: 5px !important;
}

.dark .scrollbar-thin::-webkit-scrollbar-track {
  background-color: var(--default-box-color);
}

.dark .scrollbar-thin::-webkit-scrollbar-thumb {
  background-color: #222 !important;
}
</style>
