<template>
  <div>
    <a-modal 
      v-model:open="pageData.visible" 
      width="95%" 
      :style="{ top: '10px', height: '95vh' }" 
      title="代码预览" 
      :destroyOnClose="true"
      :bodyStyle="{ height: 'calc(95vh - 120px)', overflow: 'hidden' }"
    >
      <template #footer>
        <a-space>
          <a-button @click="copyCurrentCode" type="primary">
            <template #icon><CopyOutlined /></template>
            复制代码
          </a-button>
          <a-button @click="pageData.visible = false">关闭</a-button>
        </a-space>
      </template>
      <a-tabs v-model:activeKey="pageData.activePanelKey" size="default" hide-add :style="{ height: '100%', display: 'flex', flexDirection: 'column' }">
        <a-tab-pane v-for="(value, key) in pageData.preview.data" :tab="getTabName(key)" :key="key">
          <div v-if="pageData.activePanelKey === key" class="code-preview-container">
            <!-- 悬浮工具栏 -->
            <div class="floating-toolbar">
              <a-tooltip title="复制代码">
                <a-button 
                  type="text" 
                  size="small" 
                  @click="copyCurrentCode"
                  class="toolbar-btn"
                >
                  <template #icon><CopyOutlined /></template>
                </a-button>
              </a-tooltip>
              <a-tooltip title="下载代码">
                <a-button 
                  type="text" 
                  size="small" 
                  @click="downloadCurrentCode"
                  class="toolbar-btn"
                >
                  <template #icon><DownloadOutlined /></template>
                </a-button>
              </a-tooltip>
            </div>
            <div class="code-scroll-container">
              <pre class="code-content" v-highlightjs><code>{{ highlightedCode(value) }}</code></pre>
            </div>
          </div>
        </a-tab-pane>
      </a-tabs>
    </a-modal>
  </div>
</template>

<script setup>
import { reactive } from 'vue';
import { CopyOutlined, DownloadOutlined } from '@ant-design/icons-vue';
import hljs from 'highlight.js';
import 'highlight.js/styles/github.css';
import BearJiaUtil from '@/utils/BearJiaUtil.js';
import { previewTable } from '@/api/tool/gen';
// 当前页面使用的数据
const pageData = reactive({
  visible: false,
  preview: [],
  activePanelKey: '',
});

pageData.activePanelKey = 'vm/java/domain.java.vm';

const genBasicInfoForm = reactive({ visible: false, data: {} });
// 重置genBasicInfoForm
const resetgenBasicInfoForm = () => {
  BearJiaUtil.resetFormFieldsToEmpty(genBasicInfoForm.data);
  // 值为数组时，特殊处理下
  // genBasicInfoForm.data.postIds = [];
  // genBasicInfoForm.data.roleIds = [];
};

// 打开修改窗口
const openPreviewModal = (record) => {
  pageData.visible = true;
  previewTable(record.tableId).then((response) => {
    // console.log('response.data=' + JSON.stringify(response.data));
    pageData.preview.data = response.data;
  });
};

const applyHighlight = (element) => {
  const codeElement = element?.querySelector?.('code');
  if (!codeElement) {
    return;
  }
  const source = codeElement.textContent || '';
  const { value, language } = hljs.highlightAuto(source);
  codeElement.innerHTML = value;
  codeElement.className = language ? `hljs language-${language}` : 'hljs';
};

const vHighlightjs = {
  mounted(element) {
    applyHighlight(element);
  },
  updated(element) {
    applyHighlight(element);
  }
};

// 高亮显示
const highlightedCode = (code) => {
  return code;
};

// 获取标签名称
const getTabName = (key) => {
  return key.substring(key.lastIndexOf('/') + 1, key.indexOf('.vm'));
};

// 复制当前代码
const copyCurrentCode = async () => {
  const currentCode = pageData.preview.data[pageData.activePanelKey];
  if (!currentCode) {
    BearJiaUtil.messageWarning('没有可复制的代码');
    return;
  }

  try {
    await navigator.clipboard.writeText(currentCode);
    BearJiaUtil.messageSuccess('代码已复制到剪贴板');
  } catch (error) {
    console.error('复制失败:', error);
    // 降级方案：使用传统方法
    const textArea = document.createElement('textarea');
    textArea.value = currentCode;
    document.body.appendChild(textArea);
    textArea.select();
    try {
      document.execCommand('copy');
      BearJiaUtil.messageSuccess('代码已复制到剪贴板');
    } catch (fallbackError) {
      console.error('降级复制也失败:', fallbackError);
      BearJiaUtil.messageError('复制失败，请手动复制');
    }
    document.body.removeChild(textArea);
  }
};

// 下载当前代码
const downloadCurrentCode = () => {
  const currentCode = pageData.preview.data[pageData.activePanelKey];
  if (!currentCode) {
    BearJiaUtil.messageWarning('没有可下载的代码');
    return;
  }

  const fileName = getTabName(pageData.activePanelKey);
  const blob = new Blob([currentCode], { type: 'text/plain' });
  const url = window.URL.createObjectURL(blob);
  const link = document.createElement('a');
  link.href = url;
  link.download = fileName;
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
  window.URL.revokeObjectURL(url);
  BearJiaUtil.messageSuccess('代码文件已下载');
};

// 对外暴露出去
defineExpose({
  openPreviewModal,
});
</script>

<style lang="less">
.code-preview-container {
  position: relative;
  height: calc(100vh - 200px);
  max-height: calc(100vh - 200px);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.floating-toolbar {
  position: absolute;
  top: 12px;
  right: 12px;
  z-index: 100;
  display: flex;
  gap: 4px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(8px);
  border: 1px solid rgba(0, 0, 0, 0.06);
  border-radius: 6px;
  padding: 4px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;

  &:hover {
    background: rgba(255, 255, 255, 1);
    box-shadow: 0 6px 16px rgba(0, 0, 0, 0.2);
  }
}

.toolbar-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  border-radius: 4px;
  transition: all 0.2s ease;
  background: rgba(24, 144, 255, 0.1);
  color: #1890ff;
  border: 1px solid rgba(24, 144, 255, 0.2);

  &:hover {
    background: rgba(24, 144, 255, 0.2);
    color: #1890ff;
    border-color: rgba(24, 144, 255, 0.4);
    transform: scale(1.05);
  }
}

.code-scroll-container {
  flex: 1;
  overflow-y: auto;
  overflow-x: auto;
  background: #f8f9fa;
  border: 1px solid #e9ecef;
  border-radius: 6px;
  margin: 0;
  min-height: 0;
  position: relative;
}

.code-content {
  margin: 0;
  padding: 20px;
  background: transparent;
  font-family: 'JetBrains Mono', 'Fira Code', 'Courier New', monospace;
  font-size: 13px;
  line-height: 1.6;
  white-space: pre-wrap;
  word-wrap: break-word;
  color: #24292e;
  min-height: auto;
  height: auto;
}

/* 暗黑模式适配 */
:global(.dark-theme) .floating-toolbar {
  background: rgba(30, 30, 30, 0.95);
  border-color: rgba(255, 255, 255, 0.1);

  &:hover {
    background: rgba(30, 30, 30, 1);
  }
}

:global(.dark-theme) .toolbar-btn {
  background: rgba(24, 144, 255, 0.15);
  color: #1890ff;
  border-color: rgba(24, 144, 255, 0.3);

  &:hover {
    background: rgba(24, 144, 255, 0.25);
    color: #1890ff;
    border-color: rgba(24, 144, 255, 0.5);
    transform: scale(1.05);
  }
}

:global(.dark-theme) .code-scroll-container {
  background: #1e1e1e;
  border-color: #3c3c3c;
}

:global(.dark-theme) .code-content {
  color: #d4d4d4;
}

/* 滚动条样式 */
.code-scroll-container::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

.code-scroll-container::-webkit-scrollbar-thumb {
  border: 2px solid transparent;
  border-radius: 999px;
  background: rgba(148, 163, 184, 0.45);
  background-clip: content-box;
  transition: background 0.3s ease;
}

.code-scroll-container::-webkit-scrollbar-thumb:hover {
  background: rgba(100, 116, 139, 0.58);
  background-clip: content-box;
}

.code-scroll-container::-webkit-scrollbar-track {
  border-radius: 999px;
  background: transparent;
}

:global(.dark-theme) .code-scroll-container::-webkit-scrollbar-thumb {
  border: 2px solid transparent;
  border-radius: 999px;
  background: rgba(148, 163, 184, 0.45);
  background-clip: content-box;
}

:global(.dark-theme) .code-scroll-container::-webkit-scrollbar-thumb:hover {
  background: rgba(100, 116, 139, 0.58);
  background-clip: content-box;
}
</style>
