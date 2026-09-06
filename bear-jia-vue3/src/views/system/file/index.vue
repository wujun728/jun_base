<template>
  <PageContainer>
    <div class="file-manager">
      <a-layout style="height: 100%;">
        <!-- 左侧文件夹树 -->
        <a-layout-sider
          width="280"
          theme="light"
          :collapsed="false"
          class="folder-sider"
        >
          <FolderTree
            ref="folderTreeRef"
            @folder-select="handleFolderSelect"
            @folder-change="handleFolderChange"
          />
        </a-layout-sider>

        <!-- 右侧文件展示区 -->
        <a-layout-content class="file-content">
          <FileList
            ref="fileListRef"
            :current-folder-id="currentFolderId"
            @folder-change="handleFolderChange"
            @preview="handlePreview"
            @rename="handleRename"
            @delete="handleDelete"
            @batch-move="handleBatchMove"
            @batch-delete="handleBatchDelete"
            @show-upload="() => fileUploadModalRef.open()"
            @show-create-folder="() => folderTreeRef.showCreateFolderModal()"
          />
        </a-layout-content>
      </a-layout>
    </div>

    <!-- 使用自定义组件 -->
    <FileUploadModal
      ref="fileUploadModalRef"
      :current-folder-id="currentFolderId"
      @refresh-father-page-table="handleFolderChange"
    />
    <FileDetailModal
      ref="fileDetailModalRef"
    />
    <FileStatsModal
      ref="fileStatsModalRef"
    />

    <!-- 上传文件弹窗 -->
    <a-modal
      v-model:open="uploadVisible"
      title="上传文件"
      width="600px"
      @ok="handleUploadOk"
      @cancel="uploadVisible = false"
    >
      <a-upload-dragger
        v-model:fileList="fileList"
        name="file"
        multiple
        :before-upload="beforeUpload"
        @remove="handleRemove"
      >
        <p class="ant-upload-drag-icon">
          <InboxOutlined />
        </p>
        <p class="ant-upload-text">点击或拖拽文件到此区域上传</p>
        <p class="ant-upload-hint">
          支持单个或批量上传。严禁上传公司数据或其他敏感信息。
        </p>
      </a-upload-dragger>

      <a-form :model="uploadForm" layout="vertical" style="margin-top: 16px;">
        <a-form-item label="上传到">
          <a-cascader
            v-model:value="uploadForm.parentPath"
            :options="folderOptions"
            placeholder="选择目标文件夹"
            :show-search="true"
            change-on-select
          />
        </a-form-item>
        <a-form-item label="业务类型">
          <a-select v-model:value="uploadForm.businessType" placeholder="请选择业务类型">
            <a-select-option value="avatar">用户头像</a-select-option>
            <a-select-option value="attachment">系统附件</a-select-option>
            <a-select-option value="common">通用文件</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="业务ID">
          <a-input-number v-model:value="uploadForm.businessId" placeholder="可选，关联的业务ID" style="width: 100%;" />
        </a-form-item>
        <a-form-item label="备注">
          <a-textarea v-model:value="uploadForm.remark" placeholder="可选，文件备注信息" />
        </a-form-item>
      </a-form>
    </a-modal>

    <!-- 文件预览弹窗 -->
    <a-modal
      v-model:open="previewVisible"
      title="文件预览"
      width="80%"
      :footer="null"
      centered
    >
      <div class="preview-container">
        <div v-if="previewFile.fileType === 'pdf'" class="pdf-preview">
          <iframe :src="previewFile.fileUrl" width="100%" height="600px"></iframe>
        </div>
        <div v-else-if="isImageFile(previewFile.fileType)" class="image-preview">
          <img :src="previewFile.fileUrl" alt="预览图片" style="max-width: 100%; max-height: 600px;" />
        </div>
        <div v-else class="unsupported-preview">
          <a-result
            status="info"
            title="暂不支持此文件类型的预览"
            sub-title="请下载文件后使用相应软件打开"
          >
            <template #extra>
              <a-button type="primary" @click="handleDownload(previewFile)">
                下载文件
              </a-button>
            </template>
          </a-result>
        </div>
      </div>
    </a-modal>

    <!-- 移动文件弹窗 -->
    <a-modal
      v-model:open="moveVisible"
      title="移动文件"
      @ok="handleMoveOk"
      @cancel="moveVisible = false"
    >
      <a-form layout="vertical">
        <a-form-item label="目标文件夹">
          <a-tree-select
            v-model:value="moveForm.targetFolderId"
            :tree-data="folderTreeData"
            placeholder="选择目标文件夹"
            tree-default-expand-all
            :tree-line="true"
          />
        </a-form-item>
      </a-form>
    </a-modal>
  </PageContainer>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { message } from 'ant-design-vue';
import { InboxOutlined } from '@ant-design/icons-vue';
import FolderTree from './components/FolderTree.vue';
import FileList from './components/FileList.vue';
import FileUploadModal from './components/FileUploadModal.vue';
import FileDetailModal from './components/FileDetailModal.vue';
import FileStatsModal from './components/FileStatsModal.vue';
import { uploadFile, getFolderTree } from '@/api/system/file';

// 组件引用
const folderTreeRef = ref();
const fileListRef = ref();
const fileUploadModalRef = ref();
const fileDetailModalRef = ref();
const fileStatsModalRef = ref();

// 当前文件夹
const currentFolderId = ref('0');

// 上传相关
const uploadVisible = ref(false);
const fileList = ref([]);
const uploadForm = reactive({
  parentPath: [],
  businessType: 'common',
  businessId: null,
  remark: ''
});
const folderOptions = ref([]);

// 预览相关
const previewVisible = ref(false);
const previewFile = reactive({});

// 移动相关
const moveVisible = ref(false);
const moveForm = reactive({
  targetFolderId: null
});
const folderTreeData = ref([]);

// 处理文件夹选择
const handleFolderSelect = (folderId) => {
  currentFolderId.value = folderId;
  // 刷新文件列表
  if (fileListRef.value) {
    fileListRef.value.refreshList();
  }
};

// 处理文件夹变化
const handleFolderChange = (folderId) => {
  if (folderId) {
    currentFolderId.value = folderId;
  }

  // 刷新文件夹树和文件列表
  if (folderTreeRef.value) {
    folderTreeRef.value.refreshTreeData();
  }
  if (fileListRef.value) {
    fileListRef.value.refreshList();
  }

  // 刷新文件夹选项
  loadFolderOptions();
};

// 上传前检查
const beforeUpload = (file) => {
  const isLt10M = file.size / 1024 / 1024 < 10;
  if (!isLt10M) {
    message.error('文件大小不能超过 10MB!');
    return false;
  }
  return false; // 阻止自动上传
};

// 移除文件
const handleRemove = (file) => {
  const index = fileList.value.indexOf(file);
  const newFileList = fileList.value.slice();
  newFileList.splice(index, 1);
  fileList.value = newFileList;
};

// 处理上传确认
const handleUploadOk = async () => {
  if (fileList.value.length === 0) {
    message.warning('请选择要上传的文件');
    return;
  }

  try {
    const formData = new FormData();
    fileList.value.forEach(file => {
      formData.append('files', file.originFileObj || file);
    });

    // 添加其他参数
    formData.append('parentId', uploadForm.parentPath[uploadForm.parentPath.length - 1] || '0');
    formData.append('businessType', uploadForm.businessType);
    if (uploadForm.businessId) {
      formData.append('businessId', uploadForm.businessId);
    }
    if (uploadForm.remark) {
      formData.append('remark', uploadForm.remark);
    }

    await uploadFile(formData);
    message.success('上传成功');
    uploadVisible.value = false;
    fileList.value = [];

    // 刷新列表
    handleFolderChange();
  } catch (error) {
    console.error('上传失败:', error);
    message.error('上传失败');
  }
};

// 处理移动确认
const handleMoveOk = async () => {
  try {
    // 调用移动API
    message.success('移动成功');
    moveVisible.value = false;
    handleFolderChange();
  } catch (error) {
    console.error('移动失败:', error);
    message.error('移动失败');
  }
};

// 处理下载
const handleDownload = (file) => {
  window.open(file.fileUrl, '_blank');
};

// 判断是否为图片文件
const isImageFile = (fileType) => {
  const imageTypes = ['jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp'];
  return imageTypes.includes(fileType?.toLowerCase());
};

// 处理文件预览
const handlePreview = (record) => {
  Object.assign(previewFile, record);
  previewVisible.value = true;
};

// 处理文件重命名
const handleRename = (record) => {
  // 重命名功能已在FileList组件中实现
  console.log('文件重命名:', record);
};

// 处理文件删除
const handleDelete = (record) => {
  // 删除功能已在FileList组件中实现
  console.log('文件删除:', record);
};

// 处理批量移动
const handleBatchMove = (fileIds) => {
  // 批量移动功能已在FileList组件中实现
  console.log('批量移动:', fileIds);
};

// 处理批量删除
const handleBatchDelete = (fileIds) => {
  // 批量删除功能已在FileList组件中实现
  console.log('批量删除:', fileIds);
};

// 加载文件夹选项
const loadFolderOptions = async () => {
  try {
    const response = await getFolderTree();
    if (response.code === 200) {
      folderOptions.value = buildFolderOptions(response.data);
      folderTreeData.value = buildTreeData(response.data);
    }
  } catch (error) {
    console.error('加载文件夹选项失败:', error);
  }
};

// 构建文件夹选项
const buildFolderOptions = (folders) => {
  return folders.map(folder => ({
    value: folder.id,
    label: folder.fileName,
    children: folder.children ? buildFolderOptions(folder.children) : []
  }));
};

// 构建树形数据
const buildTreeData = (folders) => {
  return folders.map(folder => ({
    title: folder.fileName,
    value: folder.id,
    key: folder.id,
    children: folder.children ? buildTreeData(folder.children) : []
  }));
};

onMounted(() => {
  loadFolderOptions();
});

// 暴露方法给子组件调用
defineExpose({
  showUploadModal: () => {
    uploadVisible.value = true;
  },
  showPreviewModal: (file) => {
    Object.assign(previewFile, file);
    previewVisible.value = true;
  },
  showMoveModal: () => {
    moveVisible.value = true;
  }
});
</script>

<style scoped>
.file-manager {
  height: calc(100vh - 156px);
  min-height: 420px;
  overflow: hidden;
  background: #ffffff;
  border-radius: 8px;
}

.folder-sider {
  border-right: 1px solid #f0f0f0;
  background: #ffffff;
  box-shadow: none;
}

.file-content {
  background: #ffffff;
  overflow: hidden;
  margin-left: 0;
  border-radius: 0 8px 8px 0;
  box-shadow: none;
}

.preview-container {
  text-align: center;
}

.pdf-preview iframe {
  border: none;
  border-radius: 4px;
}

.image-preview img {
  border-radius: 4px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

:deep(.ant-layout) {
  background: transparent;
}

:deep(.ant-layout-sider) {
  background: #ffffff;
  border-radius: 8px 0 0 8px;
}

:deep(.ant-layout-sider-children) {
  display: flex;
  flex-direction: column;
}

:deep(.ant-layout-content) {
  background: #ffffff;
}

:deep(.ant-upload-drag) {
  border: 2px dashed #d9d9d9;
  border-radius: 6px;
  background: #fafafa;
  transition: border-color 0.3s;
}

:deep(.ant-upload-drag:hover) {
  border-color: #1890ff;
}

:deep(.ant-upload-drag-icon) {
  font-size: 48px;
  color: #1890ff;
  margin-bottom: 16px;
}

:deep(.ant-upload-text) {
  font-size: 16px;
  color: #666;
  margin-bottom: 8px;
}

:deep(.ant-upload-hint) {
  font-size: 14px;
  color: #999;
}
</style>

<style>
/* ========== 暗黑模式 - 高级现代化设计 ========== */
.dark-theme .file-manager {
  /* 使用 Level 0 背景 */
  background: var(--bg-color) !important;
}

.dark-theme .folder-sider {
  /* 步骤2: 细边框 + Level 1 背景 */
  border-right: 1px solid var(--border-color) !important;
  background: var(--component-background) !important;
  box-shadow: none;
}

.dark-theme .file-content {
  /* Level 1 背景 + 细边框 */
  background: var(--component-background) !important;
  border: none;
  border-radius: 0 12px 12px 0;
  box-shadow: none;
}

.dark-theme :deep(.ant-layout-sider) {
  background: var(--component-background) !important;
  border-radius: 12px 0 0 12px;
}

.dark-theme :deep(.ant-layout-content) {
  background: var(--component-background) !important;
}

.dark-theme :deep(.ant-upload-drag) {
  border: 2px dashed var(--border-color);
  background: var(--hover-bg);
  transition: all 0.3s ease;
}

.dark-theme :deep(.ant-upload-drag:hover) {
  border-color: var(--primary-color);
  background: var(--primary-alpha-10);
  box-shadow: 0 4px 12px var(--glow-primary);
}

.dark-theme :deep(.ant-upload-drag-icon) {
  color: var(--primary-color);
}

.dark-theme :deep(.ant-upload-text) {
  /* 步骤3: 二级文字 */
  color: var(--text-secondary);
  font-weight: 500;
}

.dark-theme :deep(.ant-upload-hint) {
  /* 步骤3: 三级文字 */
  color: var(--text-tertiary);
}
</style>
