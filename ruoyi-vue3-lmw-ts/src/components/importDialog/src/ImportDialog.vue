<script setup lang="ts">
import { UploadProgressEvent, UploadFile, UploadFiles } from 'element-plus'
type Upload = {
  open: boolean
  // 弹出层标题（用户导入）
  title: string
  // 是否禁用上传
  isUploading: boolean
  // 是否更新已经存在的用户数据
  updateSupport: number
  // 设置上传的请求头部
  headers: anyObj
  // 上传的地址
  url: string
}
type Props = {
  upload: Upload
}
type Emits = {
  progress: [
    {
      event: UploadProgressEvent
      uploadFile: UploadFile
      uploadFiles: UploadFiles
    },
  ]
  success: [{ response: any; uploadFile: UploadFile; uploadFiles: UploadFiles }]
  downloadTemplate: []
  'update:upload': [value: any]
}
const { upload } = defineProps<Props>()
const emits = defineEmits<Emits>()
const uploadRef = useTemplateRef('uploadRef')
const handleFileUploadProgress = (
  event: UploadProgressEvent,
  uploadFile: UploadFile,
  uploadFiles: UploadFiles
) => {
  emits('progress', {
    event,
    uploadFile,
    uploadFiles,
  })
}

const handleFileSuccess = (
  response: any,
  uploadFile: UploadFile,
  uploadFiles: UploadFiles
) => {
  uploadRef.value?.handleRemove(uploadFile)
  emits('success', {
    response,
    uploadFile,
    uploadFiles,
  })
}

const submitFileForm = () => {
  uploadRef.value?.submit()
}

const handleValueChange = (value: any, field: keyof Upload) => {
  emits('update:upload', { ...upload, [field]: value })
}

const closeDialog = () => {
  emits('update:upload', { ...upload, open: false })
}

const downloadTemplate = () => {
  emits('downloadTemplate')
}
</script>
<template>
  <el-dialog
    :title="upload.title"
    :model-value="upload.open"
    @update:modelValue="handleValueChange($event, 'open')"
    :width="getWidth('400px')"
    draggable
  >
    <el-upload
      ref="uploadRef"
      :limit="1"
      accept=".xlsx, .xls"
      :headers="upload.headers"
      :action="upload.url + '?updateSupport=' + upload.updateSupport"
      :disabled="upload.isUploading"
      :on-progress="handleFileUploadProgress"
      :on-success="handleFileSuccess"
      :auto-upload="false"
      drag
    >
      <el-icon class="el-icon--upload"><upload-filled /></el-icon>
      <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
      <template #tip>
        <div class="el-upload__tip text-center">
          <div class="el-upload__tip">
            <el-checkbox
              :model-value="upload.updateSupport"
              @update:modelValue="handleValueChange($event, 'updateSupport')"
            >
              是否更新已经存在的用户数据
            </el-checkbox>
          </div>
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link
            type="primary"
            :underline="false"
            style="font-size: 12px; vertical-align: baseline"
            @click="downloadTemplate"
          >
            下载模板
          </el-link>
        </div>
      </template>
    </el-upload>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="closeDialog">取 消</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<style scoped lang="scss"></style>
