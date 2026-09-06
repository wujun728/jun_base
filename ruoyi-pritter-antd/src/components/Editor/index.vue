<script setup>
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css'
import { getToken } from '@/utils/auth'

const proxy = inject('proxy')

const props = defineProps({
  modelValue: {
    type: String,
    default: '',
  },
  height: {
    type: Number,
    default: null,
  },
  minHeight: {
    type: Number,
    default: null,
  },
  readOnly: {
    type: Boolean,
    default: false,
  },
  fileSize: {
    type: Number,
    default: 5,
  },
  type: {
    type: String,
    default: 'url',
  },
})

const emit = defineEmits(['update:modelValue'])

const quillEditorRef = useTemplateRef('quillEditorRef')
const uploadRef = useTemplateRef('uploadRef')
const uploadUrl = import.meta.env.VITE_APP_BASE_API + '/common/upload'
const headers = {
  Authorization: 'Bearer ' + getToken(),
}

const options = ref({
  theme: 'snow',
  bounds: document.body,
  debug: 'warn',
  modules: {
    toolbar: [
      ['bold', 'italic', 'underline', 'strike'],
      ['blockquote', 'code-block'],
      [{ list: 'ordered' }, { list: 'bullet' }],
      [{ indent: '-1' }, { indent: '+1' }],
      [{ size: ['small', false, 'large', 'huge'] }],
      [{ header: [1, 2, 3, 4, 5, 6, false] }],
      [{ color: [] }, { background: [] }],
      [{ align: [] }],
      ['clean'],
      ['link', 'image', 'video'],
    ],
  },
  placeholder: '请输入内容',
  readOnly: props.readOnly,
})

const styles = computed(() => {
  const style = {}
  if (props.minHeight) {
    style.minHeight = `${props.minHeight}px`
  }
  if (props.height) {
    style.height = `${props.height}px`
  }
  return style
})

const content = ref('')

watch(
  () => props.modelValue,
  (value) => {
    const normalized = value == null || value === '' ? '<p></p>' : value
    if (normalized !== content.value) {
      content.value = normalized
    }
  },
  { immediate: true }
)

const handleTextChange = () => {
  emit('update:modelValue', content.value)
}

const handleEditorReady = () => {
  if (props.type !== 'url') {
    return
  }
  const quill = quillEditorRef.value?.getQuill()
  if (!quill) {
    return
  }
  const toolbar = quill.getModule('toolbar')
  toolbar.addHandler('image', (value) => {
    if (value) {
      uploadRef.value?.click()
    } else {
      quill.format('image', false)
    }
  })
}

const handleBeforeUpload = (file) => {
  const type = ['image/jpeg', 'image/jpg', 'image/png', 'image/svg']
  if (!type.includes(file.type)) {
    proxy.$modal.msgError('图片格式错误!')
    return false
  }
  if (props.fileSize) {
    const isLt = file.size / 1024 / 1024 < props.fileSize
    if (!isLt) {
      proxy.$modal.msgError(`上传文件大小不能超过 ${props.fileSize} MB!`)
      return false
    }
  }
  return true
}

const handleUploadSuccess = (res) => {
  if (res.code == 200) {
    const quill = toRaw(quillEditorRef.value).getQuill()
    const length = quill.selection.savedRange.index
    quill.insertEmbed(
      length,
      'image',
      import.meta.env.VITE_APP_BASE_API + res.fileName
    )
    quill.setSelection(length + 1)
  } else {
    proxy.$modal.msgError('图片插入失败')
  }
}

const handleUploadChange = (info) => {
  if (info.file.status === 'done') {
    handleUploadSuccess(info.file.response)
  } else if (info.file.status === 'error') {
    handleUploadError()
  }
}

const handleUploadError = () => {
  proxy.$modal.msgError('图片插入失败')
}
</script>

<template>
  <div>
    <a-upload
      v-if="type === 'url'"
      :action="uploadUrl"
      :before-upload="handleBeforeUpload"
      :headers="headers"
      name="file"
      :show-upload-list="false"
      class="editor-img-uploader"
      @change="handleUploadChange"
    >
      <span ref="uploadRef" class="editor-img-uploader-trigger" />
    </a-upload>
    <div class="editor">
      <QuillEditor
        ref="quillEditorRef"
        v-model:content="content"
        content-type="html"
        :options="options"
        :style="styles"
        @ready="handleEditorReady"
        @textChange="handleTextChange"
      />
    </div>
  </div>
</template>

<style>
.editor-img-uploader,
.editor-img-uploader-trigger {
  display: none;
}

.editor,
.ql-toolbar {
  white-space: pre-wrap !important;
  line-height: normal !important;
}

.quill-img {
  display: none;
}

.ql-snow .ql-tooltip[data-mode='link']::before {
  content: '请输入链接地址:';
}

.ql-snow .ql-tooltip.ql-editing a.ql-action::after {
  border-right: 0;
  content: '保存';
  padding-right: 0;
}

.ql-snow .ql-tooltip[data-mode='video']::before {
  content: '请输入视频地址:';
}

.ql-snow .ql-picker.ql-size .ql-picker-label::before,
.ql-snow .ql-picker.ql-size .ql-picker-item::before {
  content: '14px';
}

.ql-snow .ql-picker.ql-size .ql-picker-label[data-value='small']::before,
.ql-snow .ql-picker.ql-size .ql-picker-item[data-value='small']::before {
  content: '10px';
}

.ql-snow .ql-picker.ql-size .ql-picker-label[data-value='large']::before,
.ql-snow .ql-picker.ql-size .ql-picker-item[data-value='large']::before {
  content: '18px';
}

.ql-snow .ql-picker.ql-size .ql-picker-label[data-value='huge']::before,
.ql-snow .ql-picker.ql-size .ql-picker-item[data-value='huge']::before {
  content: '32px';
}

.ql-snow .ql-picker.ql-header .ql-picker-label::before,
.ql-snow .ql-picker.ql-header .ql-picker-item::before {
  content: '文本';
}

.ql-snow .ql-picker.ql-header .ql-picker-label[data-value='1']::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value='1']::before {
  content: '标题1';
}

.ql-snow .ql-picker.ql-header .ql-picker-label[data-value='2']::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value='2']::before {
  content: '标题2';
}

.ql-snow .ql-picker.ql-header .ql-picker-label[data-value='3']::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value='3']::before {
  content: '标题3';
}

.ql-snow .ql-picker.ql-header .ql-picker-label[data-value='4']::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value='4']::before {
  content: '标题4';
}

.ql-snow .ql-picker.ql-header .ql-picker-label[data-value='5']::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value='5']::before {
  content: '标题5';
}

.ql-snow .ql-picker.ql-header .ql-picker-label[data-value='6']::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value='6']::before {
  content: '标题6';
}

.ql-snow .ql-picker.ql-font .ql-picker-label::before,
.ql-snow .ql-picker.ql-font .ql-picker-item::before {
  content: '标准字体';
}

.ql-snow .ql-picker.ql-font .ql-picker-label[data-value='serif']::before,
.ql-snow .ql-picker.ql-font .ql-picker-item[data-value='serif']::before {
  content: '衬线字体';
}

.ql-snow .ql-picker.ql-font .ql-picker-label[data-value='monospace']::before,
.ql-snow .ql-picker.ql-font .ql-picker-item[data-value='monospace']::before {
  content: '等宽字体';
}
</style>
