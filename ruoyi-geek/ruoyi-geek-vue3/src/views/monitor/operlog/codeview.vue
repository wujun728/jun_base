<script setup lang="ts">
import { ref, watchEffect } from 'vue'
import hljs from 'highlight.js'
import { ElMessage } from 'element-plus'

const props = defineProps<{
  code: string
  /** 代码语言，如 'json' | 'sql' | 'xml' | 'javascript' 等；不传则自动识别 */
  language?: string
}>()

// 高亮后的 HTML
const highlightedHtml = ref('')

const escapeHtml = (str: string) =>
  (str || '')
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;')
    .replace(/'/g, '&#39;')

const doHighlight = () => {
  try {
    const content = props.code ?? ''
    if (props.language) {
      const res = hljs.highlight(content, { language: props.language })
      highlightedHtml.value = res.value
    } else {
      const res = hljs.highlightAuto(content)
      highlightedHtml.value = res.value
    }
  } catch (e) {
    // 兜底：转义为纯文本
    highlightedHtml.value = escapeHtml(props.code ?? '')
  }
}

watchEffect(() => doHighlight())

const copying = ref(false)
const copy = async () => {
  if (copying.value) return
  copying.value = true
  try {
    await navigator.clipboard.writeText(props.code || '')
    ElMessage?.success?.('已复制到剪贴板')
  } catch (e) {
    ElMessage?.error?.('复制失败')
  } finally {
    copying.value = false
  }
}

// 是否自动换行
const wrap = ref(true)
</script>

<template>
  <div class="codeview-wrapper">
    <div class="toolbar">
      <span class="lang" v-if="props.language">{{ props.language }}</span>
      <button type="button" class="wrap-btn" @click="wrap = !wrap" :title="wrap ? '当前：自动换行' : '当前：不换行'">
        {{ wrap ? '不换行' : '换行' }}
      </button>
      <button type="button" class="copy-btn" @click="copy" :disabled="copying">
        {{ copying ? '复制中…' : '复制' }}
      </button>
    </div>
    <pre class="codeview" :class="{ 'no-wrap': !wrap }"><code class="hljs" v-html="highlightedHtml"></code></pre>
  </div>

</template>

<!-- 引入高亮主题样式（可按需更换，如 atom-one-dark.css 等） -->
<style src="highlight.js/styles/github.css"></style>

<style scoped lang="scss">
.codeview-wrapper {
  position: relative;
  /* 让组件在表单等 flex 容器中可收缩，避免被长行撑开 */
  width: 100%;
  min-width: 0;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 6px;
  font-size: 12px;
  color: #666;

  .lang {
    padding: 2px 6px;
    background: #f2f3f5;
    border-radius: 3px;
  }

  .copy-btn {
    padding: 4px 8px;
    border: 1px solid #dcdfe6;
    background: #fff;
    border-radius: 4px;
    color: #606266;
    cursor: pointer;
    transition: all 0.15s ease;

    &:hover {
      color: #409eff;
      border-color: #a0cfff;
    }

    &:disabled {
      cursor: not-allowed;
      opacity: 0.6;
    }
  }

  .wrap-btn {
    padding: 4px 8px;
    border: 1px solid #dcdfe6;
    background: #fff;
    border-radius: 4px;
    color: #606266;
    cursor: pointer;
    margin-right: 8px;
    transition: all 0.15s ease;

    &:hover {
      color: #409eff;
      border-color: #a0cfff;
    }
  }
}

.codeview {
  max-height: 300px;
  overflow: auto;
  background-color: #f5f5f5;
  border-radius: 4px;
  font-family: 'Courier New', Courier, monospace;
  white-space: pre-wrap;
  word-break: break-word;
  margin: 0;
  width: 100%;
  box-sizing: border-box;
  border: 1px solid #e1e1e1;
}

.codeview.no-wrap {
  /* 不换行，允许横向滚动 */
  white-space: pre;
  word-break: normal;
  overflow-wrap: normal;
  overflow-x: auto;
}

/* 让 code 遵循父级的换行策略 */
.codeview code.hljs {
  white-space: inherit;
  display: block;
  max-width: 100%;
}

/* 不换行时，让内容宽度按最长行计算，从而触发横向滚动条 */
/* 适应容器宽度：不强制按最长行扩展宽度，保持父容器布局稳定 */
</style>