<template>
  <a-modal
    title="文件详情"
    :open="visible"
    width="800px"
    :footer="null"
    @cancel="handleClose"
  >
    <div v-if="fileData" class="file-detail">
      <!-- 基本信息 -->
      <a-card title="基本信息" class="detail-card">
        <a-row :gutter="16">
          <a-col :span="12">
            <div class="detail-item">
              <span class="label">文件名称：</span>
              <span class="value">{{ fileData.fileName }}</span>
            </div>
          </a-col>
          <a-col :span="12">
            <div class="detail-item">
              <span class="label">原始名称：</span>
              <span class="value">{{ fileData.originalName }}</span>
            </div>
          </a-col>
          <a-col :span="12">
            <div class="detail-item">
              <span class="label">文件类型：</span>
              <span class="value">{{ fileData.fileType }}</span>
            </div>
          </a-col>
          <a-col :span="12">
            <div class="detail-item">
              <span class="label">文件大小：</span>
              <span class="value">{{ formatFileSize(fileData.fileSize) }}</span>
            </div>
          </a-col>
          <a-col :span="12">
            <div class="detail-item">
              <span class="label">MIME类型：</span>
              <span class="value">{{ fileData.mimeType }}</span>
            </div>
          </a-col>
          <a-col :span="12">
            <div class="detail-item">
              <span class="label">内容哈希：</span>
              <span class="value">{{ fileData.contentHash }}</span>
            </div>
          </a-col>
        </a-row>
      </a-card>

      <!-- 业务信息 -->
      <a-card title="业务信息" class="detail-card">
        <a-row :gutter="16">
          <a-col :span="12">
            <div class="detail-item">
              <span class="label">业务类型：</span>
              <a-tag :color="getBusinessTypeColor(fileData.businessType)">
                {{ getBusinessTypeName(fileData.businessType) }}
              </a-tag>
            </div>
          </a-col>
          <a-col :span="12">
            <div class="detail-item">
              <span class="label">业务关联ID：</span>
              <span class="value">{{ fileData.businessId || '-' }}</span>
            </div>
          </a-col>
          <a-col :span="12">
            <div class="detail-item">
              <span class="label">文件状态：</span>
              <a-badge :status="getStatusBadge(fileData.status)" :text="getStatusName(fileData.status)"/>
            </div>
          </a-col>
          <a-col :span="12">
            <div class="detail-item">
              <span class="label">处理进度：</span>
              <a-progress
                v-if="fileData.status === 2"
                :percent="fileData.processProgress"
                size="small"
              />
              <span v-else>{{ fileData.processProgress }}%</span>
            </div>
          </a-col>
          <a-col :span="24" v-if="fileData.errorMessage">
            <div class="detail-item">
              <span class="label">错误信息：</span>
              <span class="value error-text">{{ fileData.errorMessage }}</span>
            </div>
          </a-col>
        </a-row>
      </a-card>

      <!-- 统计信息 -->
      <a-card title="统计信息" class="detail-card">
        <a-row :gutter="16">
          <a-col :span="12">
            <div class="detail-item">
              <span class="label">下载次数：</span>
              <span class="value">{{ fileData.downloadCount }}</span>
            </div>
          </a-col>
          <a-col :span="12">
            <div class="detail-item">
              <span class="label">创建时间：</span>
              <span class="value">{{ fileData.createTime }}</span>
            </div>
          </a-col>
          <a-col :span="12">
            <div class="detail-item">
              <span class="label">创建者：</span>
              <span class="value">{{ fileData.createBy }}</span>
            </div>
          </a-col>
          <a-col :span="12">
            <div class="detail-item">
              <span class="label">更新时间：</span>
              <span class="value">{{ fileData.updateTime }}</span>
            </div>
          </a-col>
        </a-row>
      </a-card>

      <!-- 文件预览 -->
      <a-card title="文件预览" class="detail-card" v-if="canPreview">
        <div class="file-preview">
          <img 
            v-if="isImage(fileData.fileType)" 
            :src="fileData.fileUrl" 
            :alt="fileData.fileName"
            class="preview-image"
          />
          <div v-else class="preview-placeholder">
            <BearJiaIcon :icon="getFileIcon(fileData.fileType)" class="preview-icon"/>
            <p>该文件类型暂不支持预览</p>
          </div>
        </div>
      </a-card>

      <!-- 元数据 -->
      <a-card title="元数据" class="detail-card" v-if="fileData.metadata">
        <pre class="metadata-content">{{ formatMetadata(fileData.metadata) }}</pre>
      </a-card>

      <!-- 备注 -->
      <a-card title="备注" class="detail-card" v-if="fileData.remark">
        <p class="remark-content">{{ fileData.remark }}</p>
      </a-card>
    </div>

    <template #footer>
      <a-space>
        <a-button @click="handleClose">关闭</a-button>
        <a-button type="primary" @click="handleDownload">
          <BearJiaIcon icon="download-outlined"/>
          下载文件
        </a-button>
        <a-button v-if="canPreview" @click="handlePreview">
          <BearJiaIcon icon="eye-outlined"/>
          预览文件
        </a-button>
      </a-space>
    </template>
  </a-modal>
</template>

<script setup>
import { ref, computed, getCurrentInstance } from 'vue';

// 获取当前组件实例
const { proxy } = getCurrentInstance();

// 响应式数据
const visible = ref(false);
const fileData = ref(null);

// 计算属性
const canPreview = computed(() => {
  if (!fileData.value) return false;
  return isImage(fileData.value.fileType) || 
         ['pdf', 'txt'].includes(fileData.value.fileType?.toLowerCase());
});

// 方法
const open = (data) => {
  fileData.value = data;
  visible.value = true;
};

const handleClose = () => {
  visible.value = false;
  fileData.value = null;
};

const handleDownload = () => {
  if (fileData.value?.fileUrl) {
    const link = document.createElement('a');
    link.href = fileData.value.fileUrl;
    link.download = fileData.value.originalName || fileData.value.fileName;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }
};

const handlePreview = () => {
  if (fileData.value?.fileUrl) {
    window.open(fileData.value.fileUrl, '_blank');
  }
};

// 工具方法
const formatFileSize = (size) => {
  if (!size) return '0 B';
  const units = ['B', 'KB', 'MB', 'GB', 'TB'];
  let index = 0;
  while (size >= 1024 && index < units.length - 1) {
    size /= 1024;
    index++;
  }
  return `${size.toFixed(2)} ${units[index]}`;
};

const getBusinessTypeColor = (businessType) => {
  const colorMap = {
    rag: 'blue',
    avatar: 'green',
    attachment: 'orange',
    common: 'default'
  };
  return colorMap[businessType] || 'default';
};

const getBusinessTypeName = (businessType) => {
  const nameMap = {
    rag: 'RAG文档',
    avatar: '用户头像',
    attachment: '系统附件',
    common: '通用文件'
  };
  return nameMap[businessType] || '未知';
};

const getStatusBadge = (status) => {
  const badgeMap = {
    0: 'default',
    1: 'success',
    2: 'processing',
    3: 'error'
  };
  return badgeMap[status] || 'default';
};

const getStatusName = (status) => {
  const nameMap = {
    0: '已删除',
    1: '正常',
    2: '处理中',
    3: '处理失败'
  };
  return nameMap[status] || '未知';
};

const getFileIcon = (fileType) => {
  const iconMap = {
    pdf: 'file-pdf-outlined',
    doc: 'file-word-outlined',
    docx: 'file-word-outlined',
    xls: 'file-excel-outlined',
    xlsx: 'file-excel-outlined',
    ppt: 'file-ppt-outlined',
    pptx: 'file-ppt-outlined',
    txt: 'file-text-outlined',
    jpg: 'file-image-outlined',
    jpeg: 'file-image-outlined',
    png: 'file-image-outlined',
    gif: 'file-image-outlined',
    mp4: 'video-camera-outlined',
    mp3: 'sound-outlined',
    zip: 'file-zip-outlined',
    rar: 'file-zip-outlined'
  };
  return iconMap[fileType?.toLowerCase()] || 'file-outlined';
};

const isImage = (fileType) => {
  const imageTypes = ['jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp'];
  return imageTypes.includes(fileType?.toLowerCase());
};

const formatMetadata = (metadata) => {
  try {
    return JSON.stringify(JSON.parse(metadata), null, 2);
  } catch {
    return metadata;
  }
};

// 暴露方法
defineExpose({
  open
});
</script>

<style scoped>
.file-detail {
  max-height: 600px;
  overflow-y: auto;
}

.detail-card {
  margin-bottom: 16px;
}

.detail-card:last-child {
  margin-bottom: 0;
}

.detail-item {
  margin-bottom: 12px;
  display: flex;
  align-items: center;
}

.detail-item:last-child {
  margin-bottom: 0;
}

.label {
  font-weight: 500;
  color: #666;
  min-width: 100px;
  margin-right: 8px;
}

.value {
  color: #333;
  word-break: break-all;
}

.error-text {
  color: #ff4d4f;
}

.file-preview {
  text-align: center;
  padding: 20px;
}

.preview-image {
  max-width: 100%;
  max-height: 300px;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.preview-placeholder {
  padding: 40px;
  color: #999;
}

.preview-icon {
  font-size: 48px;
  margin-bottom: 16px;
  color: #d9d9d9;
}

.metadata-content {
  background: #f5f5f5;
  padding: 12px;
  border-radius: 4px;
  font-size: 12px;
  max-height: 200px;
  overflow-y: auto;
}

.remark-content {
  color: #666;
  line-height: 1.6;
  margin: 0;
}
</style>
