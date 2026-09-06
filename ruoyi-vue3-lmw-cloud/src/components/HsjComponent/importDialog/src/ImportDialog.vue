<script setup>
const props = defineProps({
  upload: {
    type: Object,
    required: true,
  },
})
const emits = defineEmits([
  'progress',
  'success',
  'downloadTemplate',
  'update:upload',
])
const uploadRef = ref(null)
const handleFileUploadProgress = (event, file, fileList) => {
  emits('progress', {
    event,
    file,
    fileList,
  })
}

const handleFileSuccess = (response, file, fileList) => {
  uploadRef.value.handleRemove(file)
  emits('success', {
    response,
    file,
    fileList,
  })
}

const submitFileForm = () => {
  uploadRef.value?.submit()
}

const handleValueChange = (value, field) => {
  emits('update:upload', { ...props.upload, [field]: value })
}

const closeDialog = () => {
  emits('update:upload', { ...props.upload, open: false })
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
