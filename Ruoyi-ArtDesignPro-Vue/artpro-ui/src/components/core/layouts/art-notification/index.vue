<!-- 通知组件 -->
<template>
  <div
    v-show="visible"
    class="art-notification-panel art-card-sm !shadow-xl"
    :style="{
      transform: show ? 'scaleY(1)' : 'scaleY(0.9)',
      opacity: show ? 1 : 0
    }"
    @click.stop
  >
    <div class="flex-cb px-3.5 mt-3.5">
      <span class="text-base font-medium text-g-800">{{ $t('notice.title') }}</span>
      <span
        class="text-xs text-g-800 px-1.5 py-1 c-p select-none rounded hover:bg-g-200"
        @click="handleReadAll"
      >
        {{ $t('notice.btnRead') }}
      </span>
    </div>

    <ul class="box-border flex items-end w-full h-12.5 px-3.5 border-b-d">
      <li
        v-for="(item, index) in barList"
        :key="item.type"
        class="h-12 leading-12 mr-5 overflow-hidden text-[13px] text-g-700 c-p select-none"
        :class="{ 'bar-active': barActiveIndex === index }"
        @click="barActiveIndex = index"
      >
        {{ item.name }} ({{ item.unread }})
      </li>
    </ul>

    <div class="w-full h-[calc(100%-95px)]">
      <div v-loading="loading" class="h-[calc(100%-60px)] overflow-y-scroll scrollbar-thin">
        <ul>
          <li
            v-for="item in currentList"
            :key="item.noticeId"
            class="box-border flex-c px-3.5 py-3.5 c-p last:border-b-0 hover:bg-g-200/60"
            @click="handleNoticeClick(item)"
          >
            <div
              class="size-9 leading-9 text-center rounded-lg flex-cc"
              :class="[getNoticeStyle(item.noticeType).iconClass]"
            >
              <ArtSvgIcon
                class="text-lg !bg-transparent"
                :icon="getNoticeStyle(item.noticeType).icon"
              />
            </div>
            <div class="w-[calc(100%-45px)] ml-3.5">
              <div class="flex-cb gap-2">
                <h4
                  class="text-sm leading-5.5 text-g-900 truncate"
                  :class="item.isRead ? 'font-normal' : 'font-semibold'"
                >
                  {{ item.noticeTitle }}
                </h4>
                <span v-if="!item.isRead" class="shrink-0 size-1.5 rounded-full bg-danger"></span>
              </div>
              <p class="mt-1.5 text-xs text-g-500">{{ item.createTime || '-' }}</p>
            </div>
          </li>
        </ul>

        <div
          v-if="!loading && currentList.length === 0"
          class="relative top-25 h-full text-g-500 text-center !bg-transparent"
        >
          <ArtSvgIcon icon="ri:inbox-line" class="text-5xl" />
          <p class="mt-3.5 text-xs !bg-transparent">
            {{ $t('notice.text[0]') }}{{ barList[barActiveIndex].name }}
          </p>
        </div>
      </div>

      <div v-if="canViewAll" class="relative box-border w-full px-3.5">
        <ElButton class="w-full mt-3" @click="handleViewAll" v-ripple>
          {{ $t('notice.viewAll') }}
        </ElButton>
      </div>
    </div>

    <div class="h-25"></div>
  </div>

  <ElDrawer v-model="detailVisible" :title="detail.noticeTitle || $t('notice.title')" size="560px">
    <div class="notice-detail">
      <div class="flex-c gap-2 text-xs text-g-500">
        <ElTag size="small" effect="plain">{{ getTypeLabel(detail.noticeType) }}</ElTag>
        <span>{{ detail.createTime || '-' }}</span>
      </div>
      <div class="notice-content mt-5 text-sm leading-7 text-g-800" v-html="detail.noticeContent || '-'"></div>
    </div>
  </ElDrawer>
</template>

<script setup lang="ts">
  import { computed, ref, watch } from 'vue'
  import { storeToRefs } from 'pinia'
  import { useI18n } from 'vue-i18n'
  import { useRouter } from 'vue-router'
  import { fetchNotice, type NoticeType, type TopNotice } from '@/api/system/notice'
  import { useAuth } from '@/hooks'
  import { useNoticeStore } from '@/store/modules/notice'

  defineOptions({ name: 'ArtNotification' })

  interface NoticeStyle {
    icon: string
    iconClass: string
  }

  const props = defineProps<{ value: boolean }>()
  const emit = defineEmits<{ 'update:value': [value: boolean] }>()
  const { t } = useI18n()
  const router = useRouter()
  const { hasAuth } = useAuth()
  const noticeStore = useNoticeStore()
  const { lists, unreadCounts, loading } = storeToRefs(noticeStore)
  const show = ref(false)
  const visible = ref(false)
  const detailVisible = ref(false)
  const detail = ref<Record<string, any>>({})
  const barActiveIndex = ref(0)
  const noticeTypes: NoticeType[] = ['1', '2', '3']
  const canViewAll = computed(() => hasAuth('system:notice:list'))

  const barList = computed(() =>
    noticeTypes.map((type, index) => ({
      type,
      name: t(`notice.bar[${index}]`),
      unread: unreadCounts.value[type]
    }))
  )

  const currentList = computed(() => lists.value[noticeTypes[barActiveIndex.value]])

  const noticeStyleMap: Record<NoticeType, NoticeStyle> = {
    '1': { icon: 'ri:notification-3-line', iconClass: 'bg-theme/12 text-theme' },
    '2': { icon: 'ri:volume-up-line', iconClass: 'bg-success/12 text-success' },
    '3': { icon: 'ri:todo-line', iconClass: 'bg-warning/12 text-warning' }
  }

  const getNoticeStyle = (type: NoticeType): NoticeStyle => noticeStyleMap[type]
  const getTypeLabel = (type?: NoticeType) =>
    type ? barList.value[noticeTypes.indexOf(type)]?.name || '-' : '-'

  const handleReadAll = async () => {
    await noticeStore.readAll()
  }

  const handleNoticeClick = async (item: TopNotice) => {
    await noticeStore.read(item.noticeId)
    emit('update:value', false)
    if (canViewAll.value) {
      await router.push('/system/notice')
      return
    }
    detail.value = await fetchNotice(item.noticeId)
    detailVisible.value = true
  }

  const handleViewAll = async () => {
    emit('update:value', false)
    await router.push('/system/notice')
  }

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

  watch(() => props.value, showNotice)
</script>

<style scoped>
  @reference '@styles/core/tailwind.css';

  .art-notification-panel {
    @apply absolute
    top-14.5
    right-5
    w-90
    h-125
    overflow-hidden
    transition-all
    duration-300
    origin-top
    will-change-[top,left]
    max-[640px]:top-[65px]
    max-[640px]:right-0
    max-[640px]:w-full
    max-[640px]:h-[80vh];
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

  .notice-content :deep(img) {
    max-width: 100%;
  }
</style>
