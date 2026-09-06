<!-- 图片卡片 -->
<template>
  <div class="c-p w-full" @click="handleClick">
    <div class="art-card overflow-hidden">
      <div class="relative aspect-[16/10] w-full overflow-hidden">
        <ElImage
          :src="props.imageUrl"
          fit="cover"
          loading="lazy"
          class="h-full w-full transition-transform duration-300 ease-in-out hover:scale-105">
          <template #placeholder>
            <div class="flex-cc h-full w-full bg-[#f5f7fa]">
              <ElIcon><Picture /></ElIcon>
            </div>
          </template>
        </ElImage>
        <div
          class="absolute right-3.5 bottom-3.5 rounded bg-g-200 px-2 py-1 text-xs"
          v-if="props.readTime">
          {{ props.readTime }} 阅读
        </div>
      </div>

      <div class="p-4">
        <div
          class="mb-2 inline-block rounded bg-g-300/70 px-2 py-0.5 text-xs"
          v-if="props.category">
          {{ props.category }}
        </div>
        <p class="m-0 mb-3 text-base font-medium">{{ props.title }}</p>
        <div class="flex-c gap-4 text-xs text-g-600">
          <span class="flex-c gap-1" v-if="props.views">
            <ElIcon class="text-base"><View /></ElIcon>
            {{ props.views }}
          </span>
          <span class="flex-c gap-1" v-if="props.comments">
            <ElIcon class="text-base"><ChatLineRound /></ElIcon>
            {{ props.comments }}
          </span>
          <span>{{ props.date }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Picture, View, ChatLineRound } from '@element-plus/icons-vue'

defineOptions({ name: 'ArtImageCard' })

interface Props {
  /** 图片地址 */
  imageUrl: string
  /** 标题 */
  title: string
  /** 分类 */
  category?: string
  /** 阅读时间 */
  readTime?: string
  /** 浏览量 */
  views?: number
  /** 评论数 */
  comments?: number
  /** 日期 */
  date?: string
}

const props = withDefaults(defineProps<Props>(), {
  imageUrl: '',
  title: '',
  category: '',
  readTime: '',
  views: 0,
  comments: 0,
  date: ''
})

const emit = defineEmits<{
  (e: 'click', card: Props): void
}>()

const handleClick = () => {
  emit('click', props)
}
</script>
