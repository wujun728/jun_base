<script setup>
import { useMediaQuery } from '@vueuse/core'

defineOptions({ name: 'Index' })

const appTitle = import.meta.env.VITE_APP_TITLE || '若依管理系统'
const isMobile = useMediaQuery('(max-width: 767px)')
const descriptionsColumn = computed(() => (isMobile.value ? 1 : 2))

const techStacks = [
  { name: 'Vue 3', url: 'https://cn.vuejs.org/' },
  { name: 'Vite', url: 'https://cn.vitejs.dev/' },
  { name: 'Ant Design Vue', url: 'https://antdv.com/' },
  { name: 'Pinia', url: 'https://pinia.vuejs.org/zh/' },
  { name: 'Vue Router', url: 'https://router.vuejs.org/zh/' },
]

const dependencies = [
  { name: 'vue', description: '渐进式 JavaScript 框架' },
  { name: 'ant-design-vue', description: 'Ant Design 的 Vue 实现' },
  { name: 'vue-router', description: 'Vue.js 官方路由' },
  { name: 'pinia', description: 'Vue 状态管理库' },
  { name: 'axios', description: 'HTTP 请求库' },
]
</script>

<template>
  <div class="home">
    <a-card title="关于" class="home-card">
      <p>
        欢迎使用 {{ appTitle }}。本项目基于若依前后端分离版本，使用 Ant Design
        Vue 重构前端界面。
      </p>
    </a-card>

    <a-card title="技术栈" class="home-card">
      <a-space wrap>
        <a
          v-for="item in techStacks"
          :key="item.name"
          :href="item.url"
          target="_blank"
          rel="noopener noreferrer"
        >
          <a-tag color="blue">{{ item.name }}</a-tag>
        </a>
      </a-space>
    </a-card>

    <a-card title="npm 依赖" class="home-card">
      <a-descriptions
        bordered
        :column="descriptionsColumn"
        size="small"
        class="home-descriptions"
      >
        <a-descriptions-item
          v-for="item in dependencies"
          :key="item.name"
          :label="item.name"
        >
          <a-tag>{{ item.description }}</a-tag>
        </a-descriptions-item>
      </a-descriptions>
    </a-card>
  </div>
</template>

<style scoped lang="scss">
.home {
  min-width: 0;

  .home-card {
    margin-bottom: 16px;

    &:last-child {
      margin-bottom: 0;
    }
  }

  p {
    margin: 0;
    line-height: 1.6;
    word-break: break-word;
  }

  :deep(.ant-card-head) {
    @media (max-width: 767px) {
      min-height: 40px;
      padding: 0 12px;
    }
  }

  :deep(.ant-card-body) {
    @media (max-width: 767px) {
      padding: 12px;
    }
  }

  .home-descriptions {
    :deep(.ant-descriptions-item-label),
    :deep(.ant-descriptions-item-content) {
      word-break: break-word;
    }

    :deep(.ant-descriptions-item-content .ant-tag) {
      @media (max-width: 767px) {
        white-space: normal;
        height: auto;
        line-height: 1.4;
      }
    }
  }

  @media (max-width: 767px) {
    .home-card {
      margin-bottom: 12px;
    }
  }
}
</style>
