<script setup>
import { InboxOutlined } from '@ant-design/icons-vue'
import modal from '@/plugins/modal'

const props = defineProps({
  upload: {
    type: Object,
    required: true,
  },
})

const emit = defineEmits([
  'progress',
  'success',
  'downloadTemplate',
  'update:upload',
])

const fileList = ref([])

const handleValueChange = (value, field) => {
  emit('update:upload', { ...props.upload, [field]: value })
}

const beforeUpload = (file) => {
  fileList.value = [
    {
      uid: file.uid,
      name: file.name,
      status: 'done',
      originFileObj: file,
    },
  ]
  return false
}

const handleFileUploadProgress = (event, file) => {
  emit('progress', {
    event,
    file,
    fileList: fileList.value,
  })
}

const handleFileSuccess = (response, file) => {
  fileList.value = []
  emit('success', {
    response,
    file,
    fileList: fileList.value,
  })
}

const submitFileForm = () => {
  const file = fileList.value[0]?.originFileObj
  if (!file) {
    modal.msgWarning('请选择要导入的文件')
    return
  }

  const formData = new FormData()
  formData.append('file', file)
  const url = `${props.upload.url}?updateSupport=${props.upload.updateSupport}`
  const xhr = new XMLHttpRequest()

  xhr.upload.addEventListener('progress', (event) => {
    handleFileUploadProgress(event, file)
  })

  xhr.addEventListener('load', () => {
    let response
    try {
      response = JSON.parse(xhr.responseText)
    } catch {
      response = { msg: '导入失败' }
    }
    handleFileSuccess(response, file)
  })

  xhr.addEventListener('error', () => {
    handleFileSuccess({ msg: '上传失败' }, file)
  })

  xhr.open('POST', url)
  const headers = props.upload.headers || {}
  Object.keys(headers).forEach((key) => {
    xhr.setRequestHeader(key, headers[key])
  })
  xhr.send(formData)
}

const closeDialog = () => {
  handleValueChange(false, 'open')
}

const downloadTemplate = () => {
  emit('downloadTemplate')
}

const handleUpdateSupportChange = (e) => {
  handleValueChange(e.target.checked ? 1 : 0, 'updateSupport')
}

watch(
  () => props.upload.open,
  (open) => {
    if (!open) {
      fileList.value = []
    }
  }
)
</script>

<template>
  <a-modal
    :open="upload.open"
    :title="upload.title"
    :width="getWidth('400px')"
    :mask-closable="false"
    :confirm-loading="upload.isUploading"
    @update:open="handleValueChange($event, 'open')"
  >
    <a-upload-dragger
      v-model:file-list="fileList"
      :max-count="1"
      accept=".xlsx,.xls"
      :disabled="upload.isUploading"
      :before-upload="beforeUpload"
    >
      <p class="ant-upload-drag-icon">
        <InboxOutlined />
      </p>
      <p class="ant-upload-text">
        将文件拖到此处，或<em>点击上传</em>
      </p>
    </a-upload-dragger>

    <div class="upload-tip">
      <div class="upload-tip-checkbox">
        <a-checkbox
          :checked="!!upload.updateSupport"
          @change="handleUpdateSupportChange"
        >
          是否更新已经存在的用户数据
        </a-checkbox>
      </div>
      <span>仅允许导入xls、xlsx格式文件。</span>
      <a class="download-link" @click="downloadTemplate">下载模板</a>
    </div>

    <template #footer>
      <div class="dialog-footer">
        <a-button
          type="primary"
          :loading="upload.isUploading"
          @click="submitFileForm"
        >
          确 定
        </a-button>
        <a-button @click="closeDialog">取 消</a-button>
      </div>
    </template>
  </a-modal>
</template>

<style scoped lang="scss">
.upload-tip {
  margin-top: 12px;
  text-align: center;
  font-size: 12px;
  color: rgba(0, 0, 0, 0.45);
}

.upload-tip-checkbox {
  margin-bottom: 8px;
}

.download-link {
  margin-left: 4px;
  font-size: 12px;
  color: var(--ant-color-primary, #1677ff);
  cursor: pointer;

  &:hover {
    color: var(--ant-color-primary-hover, #4096ff);
  }
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}

.ant-upload-text em {
  font-style: normal;
  color: var(--ant-color-primary, #1677ff);
}
</style>
