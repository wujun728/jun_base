<template>
  <div class="user-info-head" @click="editCropper()">
    <a-avatar :size="avatarSize" :src="displayAvatar" class="avatar-img">
      <template #icon>
        <UserOutlined />
      </template>
    </a-avatar>
    <div class="upload-mask">
      <CameraOutlined style="font-size: 24px;" />
      <span class="upload-text">{{ maskText }}</span>
    </div>

    <a-modal
      :title="title"
      v-model:open="open"
      :width="800"
      :destroyOnClose="true"
      @cancel="closeDialog"
    >
      <a-row :gutter="16">
        <a-col :xs="24" :md="12">
          <div class="cropper-wrapper">
            <vue-cropper
              ref="cropperRef"
              :img="cropperImg"
              :info="true"
              :autoCrop="options.autoCrop"
              :autoCropWidth="options.autoCropWidth"
              :autoCropHeight="options.autoCropHeight"
              :fixedBox="options.fixedBox"
              :outputType="options.outputType"
              :fixed="options.fixed"
              :fixedNumber="options.fixedNumber"
              :canMove="options.canMove"
              :canMoveBox="options.canMoveBox"
              :centerBox="options.centerBox"
              @realTime="realTime"
              v-if="visible"
            />
          </div>
        </a-col>
        <a-col :xs="24" :md="12">
          <div class="preview-wrapper">
            <div class="preview-title">预览</div>
            <div class="avatar-upload-preview" :style="previewContainerStyle">
              <div v-if="previewData.url" :style="previewData.div">
                <img :src="previewData.url" :style="previewData.img" />
              </div>
              <a-avatar v-else :size="200" class="preview-placeholder">
                <template #icon>
                  <UserOutlined />
                </template>
              </a-avatar>
            </div>
          </div>
        </a-col>
      </a-row>

      <template #footer>
        <div class="modal-footer">
          <div class="action-buttons">
            <a-upload
              :beforeUpload="beforeUpload"
              :showUploadList="false"
              accept="image/*"
            >
              <a-button>
                <UploadOutlined />
                选择图片
              </a-button>
            </a-upload>
            <a-button-group class="zoom-buttons">
              <a-tooltip title="放大">
                <a-button @click="changeScale(1)">
                  <ZoomInOutlined />
                </a-button>
              </a-tooltip>
              <a-tooltip title="缩小">
                <a-button @click="changeScale(-1)">
                  <ZoomOutOutlined />
                </a-button>
              </a-tooltip>
            </a-button-group>
            <a-button-group class="rotate-buttons">
              <a-tooltip title="向左旋转">
                <a-button @click="rotateLeft()">
                  <RotateLeftOutlined />
                </a-button>
              </a-tooltip>
              <a-tooltip title="向右旋转">
                <a-button @click="rotateRight()">
                  <RotateRightOutlined />
                </a-button>
              </a-tooltip>
            </a-button-group>
          </div>
          <div class="submit-buttons">
            <a-button @click="closeDialog">取消</a-button>
            <a-button type="primary" :loading="uploading" @click="uploadImg">
              {{ uploading ? '上传中...' : '确认上传' }}
            </a-button>
          </div>
        </div>
      </template>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, reactive, computed, watch } from 'vue'
import { message } from 'ant-design-vue'
import {
  UserOutlined,
  CameraOutlined,
  UploadOutlined,
  ZoomInOutlined,
  ZoomOutOutlined,
  RotateLeftOutlined,
  RotateRightOutlined
} from '@ant-design/icons-vue'
import "vue-cropper/dist/index.css"
import { VueCropper } from "vue-cropper"
import { uploadAvatar } from "@/api/system/user"
import { useUserStore } from "@/stores/user"
import defaultAvatar from '@/assets/images/profile.jpg'

// Props 定义
const props = defineProps({
  // 当前头像URL
  avatar: {
    type: String,
    default: ''
  },
  // 头像大小
  avatarSize: {
    type: Number,
    default: 128
  },
  // 遮罩层文字
  maskText: {
    type: String,
    default: '更换头像'
  }
})

// Emits 定义
const emit = defineEmits(['update:avatar', 'success'])

const userStore = useUserStore()
const cropperRef = ref(null)

const open = ref(false)
const visible = ref(false)
const uploading = ref(false)
const title = ref("修改头像")
const cropperImg = ref('')
const previewData = reactive({
  url: '',
  img: {},
  div: {}
})

// 图片裁剪配置
const options = reactive({
  autoCrop: true,            // 是否默认生成截图框
  autoCropWidth: 200,        // 默认生成截图框宽度
  autoCropHeight: 200,       // 默认生成截图框高度
  fixedBox: true,            // 固定截图框大小 不允许改变
  outputType: "png",         // 默认生成截图为PNG格式
  fixed: true,               // 固定截图框比例
  fixedNumber: [1, 1],       // 截图框比例 1:1
  canMove: true,             // 上传图片是否可以移动
  canMoveBox: true,          // 截图框能否拖动
  centerBox: true,           // 截图框是否被限制在图片里面
  filename: 'avatar'         // 文件名称
})

// 预览容器样式
const previewContainerStyle = computed(() => ({
  width: '200px',
  height: '200px',
  borderRadius: '50%',
  overflow: 'hidden',
  border: '2px solid #e8e8e8',
  background: '#fff',
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center'
}))

// 计算显示的头像
const displayAvatar = computed(() => {
  if (props.avatar) {
    // 如果是完整URL，直接返回
    if (props.avatar.startsWith('http://') || props.avatar.startsWith('https://')) {
      return props.avatar
    }
    // 如果是相对路径，拼接API基础路径
    const baseApi = import.meta.env.VITE_APP_BASE_API || ''
    return baseApi + props.avatar
  }
  // 使用默认头像
  return defaultAvatar
})

// 监听 props.avatar 变化
watch(() => props.avatar, (newVal) => {
  if (newVal) {
    cropperImg.value = displayAvatar.value
  }
}, { immediate: true })

/** 编辑头像 */
function editCropper() {
  open.value = true
  // 延迟显示裁剪器，等待modal完全打开
  setTimeout(() => {
    visible.value = true
    cropperImg.value = displayAvatar.value
  }, 100)
}

/** 图片缩放 */
function changeScale(num) {
  num = num || 1
  if (cropperRef.value) {
    cropperRef.value.changeScale(num)
  }
}

/** 上传预处理 */
function beforeUpload(file) {
  // 检查文件类型
  const isImage = file.type.startsWith('image/')
  if (!isImage) {
    message.error('请选择图片文件（JPG、PNG、GIF等）')
    return false
  }

  // 检查文件大小 (限制为 5MB)
  const isLt5M = file.size / 1024 / 1024 < 5
  if (!isLt5M) {
    message.error('图片大小不能超过 5MB')
    return false
  }

  // 读取文件并显示在裁剪器中
  const reader = new FileReader()
  reader.readAsDataURL(file)
  reader.onload = () => {
    cropperImg.value = reader.result
    options.filename = file.name
  }

  // 阻止默认上传行为
  return false
}

/** 上传图片 */
function uploadImg() {
  if (!cropperRef.value) {
    message.error('裁剪器未初始化')
    return
  }

  uploading.value = true

  cropperRef.value.getCropBlob(async (data) => {
    try {
      const formData = new FormData()
      formData.append("avatarfile", data, options.filename || 'avatar.png')

      const response = await uploadAvatar(formData)

      // 获取新头像URL
      const newAvatarUrl = response.imgUrl || response.data?.url || response.url || ''

      // 更新 store 中的头像
      const fullAvatarUrl = newAvatarUrl.startsWith('http')
        ? newAvatarUrl
        : (import.meta.env.VITE_APP_BASE_API || '') + newAvatarUrl
      userStore.avatar = fullAvatarUrl

      // 触发事件通知父组件
      emit('update:avatar', newAvatarUrl)
      emit('success', { url: newAvatarUrl, fullUrl: fullAvatarUrl })

      message.success(response.msg || '头像修改成功')

      // 关闭弹窗
      open.value = false
      visible.value = false
    } catch (error) {
      console.error('头像上传失败:', error)
      message.error('头像上传失败，请重试')
    } finally {
      uploading.value = false
    }
  })
}

/** 实时预览 */
function realTime(data) {
  if (data.url) {
    previewData.url = data.url
    // 调整预览图片样式以适应圆形预览框
    previewData.img = data.img || {}
    previewData.div = data.div || {}
  }
}

/** 关闭窗口 */
function closeDialog() {
  open.value = false
  visible.value = false
  // 重置预览
  previewData.url = ''
  previewData.img = {}
  previewData.div = {}
}

// 暴露方法给父组件
defineExpose({
  editCropper
})
</script>

<style lang="less" scoped>
.user-info-head {
  position: relative;
  display: inline-block;
  cursor: pointer;
  border-radius: 50%;
  overflow: hidden;

  .avatar-img {
    display: block;
    border: 4px solid #fff;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  }

  .upload-mask {
    position: absolute;
    inset: 0;
    background: rgba(0, 0, 0, 0.5);
    color: #fff;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    opacity: 0;
    transition: opacity 0.3s ease;
    border-radius: 50%;

    .upload-text {
      margin-top: 8px;
      font-size: 14px;
    }
  }

  &:hover .upload-mask {
    opacity: 1;
  }
}

.cropper-wrapper {
  height: 350px;
  background: #f5f5f5;
  border-radius: 8px;
  overflow: hidden;
}

.preview-wrapper {
  height: 350px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: #fafafa;
  border-radius: 8px;

  .preview-title {
    margin-bottom: 16px;
    font-size: 14px;
    color: #666;
  }

  .avatar-upload-preview {
    .preview-placeholder {
      width: 100%;
      height: 100%;
    }
  }
}

.modal-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;

  .action-buttons {
    display: flex;
    gap: 8px;

    .zoom-buttons {
      margin-left: 8px;
    }
  }

  .submit-buttons {
    display: flex;
    gap: 8px;
  }
}

// 响应式适配
@media (max-width: 768px) {
  .cropper-wrapper,
  .preview-wrapper {
    height: 250px;
  }

  .modal-footer {
    flex-direction: column;
    gap: 16px;

    .action-buttons {
      flex-wrap: wrap;
      justify-content: center;
    }

    .submit-buttons {
      width: 100%;
      justify-content: flex-end;
    }
  }
}
</style>
