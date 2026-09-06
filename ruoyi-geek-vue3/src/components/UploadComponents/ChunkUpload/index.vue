<script setup lang="ts">
import { ref, computed } from 'vue';
import { ElMessage } from 'element-plus';
import { useChunkUpload } from '.';

const emits = defineEmits(['update:modelValue']);
const props = withDefaults(defineProps<{
  chunkSize: number;
  maxConcurrency: number;
}>(), {
  chunkSize: 10,
  maxConcurrency: 1
});
const isUploading = ref(false);
const partETags = ref<{ partNumber: number; etag: string }[]>([]);
const chunkBytes = computed(() => props.chunkSize * 1024 * 1024); // 分片大小（字节）
const selectedFile = ref<File | null>(null);
const fileInfo = computed(() => ({
  fileName: selectedFile.value?.name ?? '',
  fileSize: (selectedFile.value?.size ?? 0) / (1024 * 1024),
}));

// 处理文件选择
function handleBeforeUpload(file: File) {
  selectedFile.value = file;
  return false; // 阻止默认上传
}
const chunkUpload = ref(useChunkUpload())
// 开始上传
async function startUpload() {
  try {
    isUploading.value = true;
    const file = selectedFile.value;
    if (!file) throw new Error('未选择文件');
    chunkUpload.value = useChunkUpload(file, {
      chunkSize: chunkBytes.value,
      concurrency: props.maxConcurrency
    })
    await chunkUpload.value.startUpload();
    ElMessage.success('文件上传成功');
    emits('update:modelValue');
  } catch (error: any) {
    ElMessage.error(`文件上传失败: ${error.message}`);
    console.error('分片上传失败', error);
  } finally {
    isUploading.value = false;
  }
}

// 重置上传状态
function resetUpload() {
  selectedFile.value = null;
  partETags.value = [];
  chunkUpload.value = useChunkUpload();
}

</script>
<template>
  <div>
    <el-card class="custom-file-info-card">
      <template #header>
        <div class="file-details">
          <div class="detail-item">
            <span class="detail-label">文件大小:</span>
            <span class="detail-value">{{ fileInfo.fileSize }} MB</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">总分片数:</span>
            <span class="detail-value">{{ chunkUpload.totalChunks }}</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">分片大小:</span>
            <span class="detail-value">{{ chunkSize }} MB</span>
          </div>
          <div class="detail-item">
            <span class="detail-label">并发数量:</span>
            <span class="detail-value">{{ maxConcurrency }}</span>
          </div>
        </div>
      </template>
      <el-upload class="custom-upload-area" drag :show-file-list="false" :before-upload="handleBeforeUpload">
        <div class="upload-content">
          <i class="el-icon-upload el-icon--primary upload-icon"></i>
          <div class="upload-text">将文件拖到此处，或<em class="upload-link">点击上传</em></div>
          <div class="upload-tip">支持拖拽上传，最大文件500MB</div>
          <i class="el-icon-document file-icon"></i>
          <span class="file-name">{{ fileInfo.fileName || '未选择文件' }}</span>
        </div>
      </el-upload>
      <template #footer>
        <div>
          <el-progress :percentage="chunkUpload.uploadPercentage" :status="chunkUpload.uploadStatus">
            <span style="margin-right: 10px;">{{ chunkUpload.uploadMessage || '等待上传...' }}</span>
            <span>分片: {{ chunkUpload.uploadedChunks }} / {{ chunkUpload.totalChunks }}</span>
          </el-progress>
          <div class="dialog-footer">
            <el-button @click="resetUpload" class="btn-cancel" icon="Refresh">重置</el-button>
            <el-button type="primary" :disabled="!selectedFile || isUploading" @click="startUpload" class="btn-upload"
              icon="Upload">
              <i class="el-icon-upload2" v-if="!isUploading"></i>
              <i class="el-icon-loading" v-else></i>
              {{ isUploading ? '上传中...' : '开始上传' }}
            </el-button>
          </div>
        </div>
      </template>
    </el-card>
  </div>
</template>
<style scoped lang="scss">
/* 自定义文件信息卡片 */
.custom-file-info-card {
  width: 100%;
  border-radius: 8px;
  border: 1px solid #ebeef5;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

/* 自定义上传区域样式 */
.custom-upload-area {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  border: 2px dashed #dcdfe6;
  border-radius: 8px;
  background-color: #fafbfc;
  transition: all 0.3s ease;
  cursor: pointer;

  :deep(.el-upload) {
    width: 100%;

    .el-upload-dragger {
      border: none;
    }
  }

  &:hover {
    border-color: #409eff;
    background-color: #f5f7fa;
  }

  .upload-content {
    text-align: center;

    .upload-icon {
      font-size: 48px;
      color: #409eff;
      margin-bottom: 12px;
      transition: transform 0.3s ease;
    }

    .upload-link {
      color: #409eff;
      font-style: normal;
      cursor: pointer;
      text-decoration: underline;
      transition: color 0.3s ease;

      &:hover {
        color: #3a8ee6;
      }
    }

    .upload-tip {
      font-size: 12px;
      color: #909399;
      margin-top: 4px;
    }

    .file-icon {
      font-size: 20px;
      margin-right: 8px;
      color: #409eff;
    }

    .file-name {
      max-width: 400px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }

  }

}

.file-details {
  padding: 16px;
  display: flex;
  justify-content: space-between;

  .detail-item {
    padding: 6px 0;

    .detail-label {
      flex-shrink: 0;
      width: 80px;
      color: #606266;
      font-size: 14px;
    }

    .detail-value {
      color: #303133;
      font-size: 14px;
    }
  }

}

/* 自定义按钮 */
.dialog-footer {
  display: flex;
  justify-content: space-around;
  padding: 16px 30px;

  .btn-cancel {
    margin-right: 12px;
  }

  .btn-upload {
    min-width: 120px;
  }
}
</style>