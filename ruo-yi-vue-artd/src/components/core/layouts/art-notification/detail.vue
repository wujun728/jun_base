<template>
  <el-drawer
    v-model="visible"
    title="公告详情"
    direction="rtl"
    size="50%"
    append-to-body
    :before-close="handleClose"
    class="notice-detail-drawer">
    <div v-loading="loading" class="notice-detail-drawer__body">
      <div v-if="!detail" class="notice-empty">
        <el-icon class="notice-empty__icon"><Document /></el-icon>
        <span>暂无数据</span>
      </div>
      <div v-else class="notice-page">
        <div class="notice-type-wrap">
          <span
            v-if="detail.noticeType === '1'"
            class="notice-type-tag notice-type--info">
            <ArtSvgIcon icon="ri:volume-down-line" />
            通知
          </span>
          <span
            v-else-if="detail.noticeType === '2'"
            class="notice-type-tag notice-type--primary">
            <ArtSvgIcon icon="ri:notification-3-line" />
            公告
          </span>
          <span v-else class="notice-type-tag notice-type--secondary">
            <el-icon><Document /></el-icon>
            消息
          </span>
        </div>

        <h1 class="notice-title">{{ detail.noticeTitle }}</h1>

        <div class="notice-meta">
          <span class="meta-item">
            <el-icon class="meta-item__icon"><User /></el-icon>
            <span>{{ detail.createBy || '—' }}</span>
          </span>
          <span class="notice-meta__sep"></span>
          <span class="meta-item">
            <el-icon class="meta-item__icon"><Clock /></el-icon>
            <span>{{ detail.createTime || '—' }}</span>
          </span>
          <span class="notice-meta__sep"></span>
          <span class="meta-item">
            <span
              :class="[
                'status-dot',
                isStatusNormal ? 'status-ok' : 'status-off'
              ]"></span>
            <span>{{ isStatusNormal ? '正常' : '已关闭' }}</span>
          </span>
        </div>

        <div class="notice-body">
          <div
            v-if="hasContent"
            class="notice-content"
            v-html="detail.noticeContent" />
          <div v-else class="notice-empty notice-empty--inner">
            <el-icon class="notice-empty__icon"><Document /></el-icon>
            暂无内容
          </div>
        </div>
      </div>
    </div>
  </el-drawer>
</template>

<script setup lang="ts">
import type { SysNotice } from '@/types/api/system/notice'

import { getNotice } from './api'

const visible = ref<boolean>(false)
const loading = ref<boolean>(false)
const detail = ref<SysNotice | null>({})

const isStatusNormal = computed<boolean>(() => {
  const status = detail.value && detail.value.status
  return status === '0'
})

const hasContent = computed<boolean>(() => {
  const content = detail.value && detail.value.noticeContent
  return content != null && String(content).trim() !== ''
})

async function open(payload: any) {
  let id = null
  let preset = null
  if (payload != null && typeof payload === 'object') {
    id = payload.noticeId
    if (payload.noticeContent != null) {
      preset = payload
    }
  } else {
    id = payload
  }
  visible.value = true
  if (preset) {
    detail.value = preset
    return
  }
  if (id == null || id === '') {
    detail.value = null
    return
  }
  loading.value = true
  detail.value = null
  const res = await getNotice(id)
  detail.value = res
  loading.value = false
}

function handleClose() {
  visible.value = false
  detail.value = null
  loading.value = false
}

defineExpose({
  open
})
</script>

<style lang="scss" scoped>
.notice-page {
  max-width: 760px;
  margin: 0 auto;
  padding: 8px 8px 20px;
  animation: notice-fade-up 0.32s ease both;
}

@keyframes notice-fade-up {
  from {
    opacity: 0;
    transform: translateY(16px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* ---- type tag ---- */
.notice-type-tag {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 4px 14px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
  letter-spacing: 0.3px;
  margin-bottom: 18px;
}

.notice-type--info {
  background: color-mix(in srgb, var(--art-info) 12%, transparent);
  color: var(--art-info);
}

.notice-type--primary {
  background: color-mix(in srgb, var(--theme-color) 12%, transparent);
  color: var(--theme-color);
}

.notice-type--secondary {
  background: color-mix(in srgb, var(--art-secondary) 12%, transparent);
  color: var(--art-secondary);
}

/* ---- title ---- */
.notice-title {
  font-size: 24px;
  font-weight: 700;
  color: var(--art-gray-900);
  line-height: 1.4;
  margin: 0 0 20px;
  letter-spacing: -0.3px;
}

/* ---- meta ---- */
.notice-meta {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 0;
  padding: 14px 18px;
  border: 1px solid var(--default-border);
  border-radius: 10px;
  background: var(--default-box-color);
  margin-bottom: 28px;
}

.notice-meta__sep {
  width: 1px;
  height: 18px;
  background: var(--default-border);
  margin: 0 16px;
  flex-shrink: 0;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: var(--art-gray-600);
}

.meta-item__icon {
  font-size: 14px;
  color: var(--art-gray-400);
  flex-shrink: 0;
}

.status-dot {
  display: inline-block;
  width: 8px;
  height: 8px;
  border-radius: 50%;
  flex-shrink: 0;
}

.status-ok {
  background: var(--art-success);
  box-shadow: 0 0 0 2px color-mix(in srgb, var(--art-success) 20%, transparent);
}

.status-off {
  background: var(--art-error);
  box-shadow: 0 0 0 2px color-mix(in srgb, var(--art-error) 20%, transparent);
}

/* ---- body card ---- */
.notice-body {
  background: var(--default-box-color);
  border: 1px solid var(--default-border);
  border-radius: 10px;
  padding: 32px 36px;
  min-height: 140px;
}

.notice-content {
  font-size: 14px;
  line-height: 1.85;
  color: var(--art-gray-800);
  word-break: break-word;
}

/* ---- content deep styles ---- */
.notice-content :deep(p) {
  margin: 0 0 1em;
}

.notice-content :deep(h1),
.notice-content :deep(h2),
.notice-content :deep(h3),
.notice-content :deep(h4) {
  font-weight: 700;
  line-height: 1.4;
  color: var(--art-gray-900);
  margin: 1.5em 0 0.6em;
}

.notice-content :deep(h1) {
  font-size: 20px;
}
.notice-content :deep(h2) {
  font-size: 17px;
}
.notice-content :deep(h3) {
  font-size: 15px;
}
.notice-content :deep(h4) {
  font-size: 14px;
}

.notice-content :deep(a) {
  color: var(--theme-color);
}

.notice-content :deep(a:hover) {
  opacity: 0.8;
}

.notice-content :deep(img) {
  max-width: 100%;
  border-radius: 6px;
  margin: 10px 0;
}

.notice-content :deep(ul),
.notice-content :deep(ol) {
  padding-left: 22px;
  margin: 0 0 1em;
}

.notice-content :deep(li) {
  margin-bottom: 4px;
}

.notice-content :deep(li::marker) {
  color: var(--art-gray-400);
}

.notice-content :deep(blockquote) {
  border-left: 3px solid var(--default-border);
  margin: 1em 0;
  padding: 8px 18px;
  color: var(--art-gray-600);
  background: var(--default-bg-color);
  border-radius: 0 6px 6px 0;
}

.notice-content :deep(code) {
  font-family: 'SF Mono', 'Fira Code', 'JetBrains Mono', monospace;
  font-size: 0.9em;
  padding: 2px 6px;
  border-radius: 4px;
  background: var(--default-bg-color);
  color: var(--art-error);
}

.notice-content :deep(pre) {
  background: var(--default-bg-color);
  border: 1px solid var(--default-border);
  border-radius: 8px;
  padding: 16px 20px;
  margin: 1em 0;
  overflow-x: auto;
  font-size: 13px;
  line-height: 1.6;
}

.notice-content :deep(pre code) {
  padding: 0;
  background: none;
  color: var(--art-gray-800);
}

.notice-content :deep(table) {
  border-collapse: collapse;
  width: 100%;
  margin: 1em 0;
  font-size: 13px;
  border-radius: 6px;
  overflow: hidden;
}

.notice-content :deep(table th),
.notice-content :deep(table td) {
  border: 1px solid var(--default-border);
  padding: 8px 14px;
}

.notice-content :deep(table th) {
  background: var(--default-bg-color);
  font-weight: 600;
  color: var(--art-gray-700);
}

.notice-content :deep(hr) {
  border: none;
  border-top: 1px solid var(--default-border);
  margin: 1.5em 0;
}

/* ---- empty state ---- */
.notice-empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 0;
  color: var(--art-gray-400);
  font-size: 14px;
  gap: 12px;
}

.notice-empty__icon {
  font-size: 36px;
  color: var(--art-gray-300);
}

.notice-empty--inner {
  padding: 48px 0;
  color: var(--art-gray-500);
}

.notice-empty--inner .notice-empty__icon {
  color: var(--art-gray-300);
}

.notice-detail-drawer__body {
  height: 100%;
  overflow: auto;
  padding: 0 16px 22px;

  &::-webkit-scrollbar {
    width: 5px;
  }

  &::-webkit-scrollbar-track {
    background: transparent;
  }

  &::-webkit-scrollbar-thumb {
    background: var(--art-gray-300);
    border-radius: 4px;
  }
}

/* ---- responsive ---- */
@media (max-width: 640px) {
  .notice-body {
    padding: 20px 16px;
    border-radius: 8px;
  }

  .notice-meta {
    padding: 12px 14px;
    border-radius: 8px;
  }

  .notice-meta__sep {
    margin: 0 10px;
  }

  .notice-title {
    font-size: 20px;
    margin-bottom: 14px;
  }

  .notice-detail-drawer__body {
    padding: 0 8px 16px;
  }

  .notice-page {
    padding: 4px 4px 16px;
  }
}
</style>

<style lang="scss">
.notice-detail-drawer {
  .el-drawer__header {
    margin-bottom: 0;
    padding: 18px 24px;
    border-bottom: 1px solid var(--default-border);
    font-size: 16px;
    font-weight: 600;
    color: var(--art-gray-900);
  }

  .el-drawer__body {
    background: var(--default-bg-color);
    padding: 0;
  }

  .el-drawer__close-btn {
    color: var(--art-gray-400);

    &:hover {
      color: var(--art-gray-600);
      background: var(--art-hover-color);
      border-radius: 6px;
    }
  }
}
</style>
