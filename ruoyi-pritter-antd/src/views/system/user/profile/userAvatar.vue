<script setup>
import 'vue-cropper/dist/index.css'
import { VueCropper } from 'vue-cropper'
import { uploadAvatar } from '@/api/system/user'
import useUserStore from '@/store/modules/user'

const userStore = useUserStore()
const proxy = inject('proxy')

const open = ref(false)
const visible = ref(false)
const submitLoading = ref(false)
const cropperRef = useTemplateRef('cropperRef')
const previews = ref({})

const options = reactive({
  img: userStore.avatar,
  autoCrop: true,
  autoCropWidth: 200,
  autoCropHeight: 200,
  fixedBox: true,
  fixed: true,
  fixedNumber: [1, 1],
  outputType: 'png',
})

watch(
  () => userStore.avatar,
  (avatar) => {
    if (!open.value) {
      options.img = avatar
    }
  }
)

watch(open, async (isOpen) => {
  if (isOpen) {
    visible.value = true
  } else {
    visible.value = false
    submitLoading.value = false
    options.img = userStore.avatar
    previews.value = {}
  }
})

function editCropper() {
  open.value = true
}

const ALLOWED_IMAGE_TYPES = ['image/jpeg', 'image/jpg', 'image/png']
const ALLOWED_IMAGE_EXTENSIONS = ['.jpg', '.jpeg', '.png']
const ACCEPT_IMAGE = '.jpg,.jpeg,.png,image/jpeg,image/png'

function isAllowedImage(file) {
  const extension = file.name.slice(file.name.lastIndexOf('.')).toLowerCase()
  return (
    ALLOWED_IMAGE_TYPES.includes(file.type) ||
    ALLOWED_IMAGE_EXTENSIONS.includes(extension)
  )
}

function beforeUpload(file) {
  if (!isAllowedImage(file)) {
    proxy.$modal.msgError('文件格式错误，请上传 JPG、JPEG、PNG 格式的图片')
    return false
  }
  const reader = new FileReader()
  reader.readAsDataURL(file)
  reader.onload = () => {
    options.img = reader.result
  }
  return false
}

function rotateLeft() {
  cropperRef.value?.rotateLeft()
}

function rotateRight() {
  cropperRef.value?.rotateRight()
}

function changeScale(num) {
  cropperRef.value?.changeScale(num || 1)
}

function realTime(data) {
  previews.value = data
}

function uploadImg() {
  cropperRef.value?.getCropBlob((data) => {
    const formData = new FormData()
    formData.append('avatarfile', data)
    submitLoading.value = true
    uploadAvatar(formData)
      .then((response) => {
        const newAvatar = import.meta.env.VITE_APP_BASE_API + response.imgUrl
        options.img = newAvatar
        userStore.avatar = newAvatar
        open.value = false
        proxy.$modal.notifySuccess('修改成功')
      })
      .finally(() => {
        submitLoading.value = false
      })
  })
}
</script>

<template>
  <div class="user-info-head" @click="editCropper()">
    <img :src="options.img" title="点击上传头像" class="img-lg" />
    <a-modal
      v-model:open="open"
      :width="getWidth('800px')"
      title="修改头像"
      destroy-on-close
    >
      <a-row :gutter="16">
        <a-col :xs="24" :md="12">
          <div class="cropper-container">
            <VueCropper
              v-if="visible"
              ref="cropperRef"
              :img="options.img"
              :info="true"
              :auto-crop="options.autoCrop"
              :auto-crop-width="options.autoCropWidth"
              :auto-crop-height="options.autoCropHeight"
              :fixed-box="options.fixedBox"
              :fixed="options.fixed"
              :fixed-number="options.fixedNumber"
              :output-type="options.outputType"
              @real-time="realTime"
            />
          </div>
        </a-col>
        <a-col :xs="24" :md="12">
          <div class="avatar-upload-preview">
            <div :style="previews.div">
              <img :src="previews.url" :style="previews.img" />
            </div>
          </div>
        </a-col>
      </a-row>
      <template #footer>
        <a-row :gutter="8" align="middle">
          <a-col>
            <a-upload
              :show-upload-list="false"
              :accept="ACCEPT_IMAGE"
              :before-upload="beforeUpload"
            >
              <a-button type="primary" ghost>
                <SvgIcon iconClass="upload" />
                <span class="ml6">选择</span>
              </a-button>
            </a-upload>
          </a-col>
          <a-col>
            <a-button @click="changeScale(1)">
              <SvgIcon iconClass="plus" />
            </a-button>
          </a-col>
          <a-col>
            <a-button @click="changeScale(-1)">
              <SvgIcon iconClass="minus" />
            </a-button>
          </a-col>
          <a-col>
            <a-button @click="rotateLeft()">
              <SvgIcon iconClass="arrow-rotate-left" />
            </a-button>
          </a-col>
          <a-col>
            <a-button @click="rotateRight()">
              <SvgIcon iconClass="arrow-rotate-right" />
            </a-button>
          </a-col>
          <a-col flex="auto" class="text-right">
            <a-button type="primary" :loading="submitLoading" @click="uploadImg()">
              提 交
            </a-button>
          </a-col>
        </a-row>
      </template>
    </a-modal>
  </div>
</template>

<style lang="scss" scoped>
.user-info-head {
  position: relative;
  display: inline-block;
}

.img-lg {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  object-fit: cover;
}

.cropper-container {
  width: 100%;
  height: 350px;
}

.avatar-upload-preview {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 350px;
  overflow: hidden;
  border-radius: 50%;
  box-shadow: 0 0 4px #ccc;
}

.text-right {
  text-align: right;
}

.user-info-head:hover:after {
  content: '+';
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  color: #eee;
  background: rgba(0, 0, 0, 0.5);
  font-size: 24px;
  font-style: normal;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  cursor: pointer;
  line-height: 110px;
  border-radius: 50%;
}
</style>
