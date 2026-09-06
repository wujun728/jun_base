<template>
  <div class="image-upload-container">
    <a-upload
        v-model:file-list="fileList"
        :action="uploadImgUrl"
        :headers="headers"
        :before-upload="handleBeforeUpload"
        :remove="handleDelete"
        :multiple="true"
        :data="data"
        :disabled="disabled"
        list-type="picture-card"
        @change="handleChange"
        @preview="handlePreview"
    >
      <div v-if="!disabled && (fileList.length < limit || limit === 0)">
        <plus-outlined />
        <div style="margin-top: 8px">上传</div>
      </div>
    </a-upload>

    <!-- 上传提示 -->
    <div class="upload-tip" v-if="showTip">
      请上传
      <template v-if="fileSize"> 大小不超过 <b style="color: #ff4d4f">{{ fileSize }}MB</b> </template>
      <template v-if="fileType"> 格式为 <b style="color: #ff4d4f">{{ fileType.join("/") }}</b> </template>
      的图片
    </div>

    <!-- 预览弹窗 -->
    <a-modal
        v-model:open="previewVisible"
        :title="previewTitle"
        :footer="null"
        @cancel="previewVisible = false"
    >
      <img alt="预览图片" style="width: 100%" :src="previewImage" />
    </a-modal>
  </div>
</template>

<script>
import { defineComponent, ref, watch, computed, nextTick, onMounted } from 'vue';
import { message } from 'ant-design-vue';
import { PlusOutlined } from '@ant-design/icons-vue';
import { getToken } from "@/utils/auth";
import { isExternal } from "@/utils/bearjia";
import Sortable from 'sortablejs';

export default defineComponent({
  name: "ImageUpload",
  components: {
    PlusOutlined
  },
  props: {
    // 值
    modelValue: {
      type: [String, Object, Array],
      default: ''
    },
    // 上传接口地址
    action: {
      type: String,
      default: "/common/upload"
    },
    // 上传携带的参数
    data: {
      type: Object,
      default: () => ({})
    },
    // 图片数量限制
    limit: {
      type: Number,
      default: 5
    },
    // 大小限制(MB)
    fileSize: {
      type: Number,
      default: 5
    },
    // 文件类型, 例如['png', 'jpg', 'jpeg']
    fileType: {
      type: Array,
      default: () => ["png", "jpg", "jpeg", "gif"]
    },
    // 是否显示提示
    isShowTip: {
      type: Boolean,
      default: true
    },
    // 禁用组件（仅查看图片）
    disabled: {
      type: Boolean,
      default: false
    },
    // 拖动排序
    drag: {
      type: Boolean,
      default: true
    }
  },
  emits: ['update:modelValue', 'change'],
  setup(props, { emit }) {
    const fileList = ref([]);
    const uploading = ref(false);

    // 预览相关
    const previewVisible = ref(false);
    const previewImage = ref('');
    const previewTitle = ref('');

    const baseUrl = import.meta.env.VITE_APP_BASE_API || '';
    const uploadImgUrl = computed(() => baseUrl + props.action);

    const headers = {
      Authorization: "Bearer " + getToken(),
    };

    // 是否显示提示
    const showTip = computed(() => {
      return props.isShowTip && (props.fileType.length > 0 || props.fileSize > 0);
    });

    // 将字符串值转换为文件列表
    const parseModelValue = (val) => {
      if (!val) return [];

      // 首先将值转为数组
      const values = Array.isArray(val) ? val : val.split(',');

      // 然后将数组转为对象数组
      return values
          .filter(item => item) // 过滤空值
          .map((item, index) => {
            if (typeof item === "string") {
              let url = item;
              if (url.indexOf(baseUrl) === -1 && !isExternal(url)) {
                url = baseUrl + item;
              }
              return {
                uid: `-${index}`,
                name: getFileName(item),
                status: 'done',
                url: url,
                thumbUrl: url
              };
            }
            return item;
          });
    };

    // 监听值变化
    watch(() => props.modelValue, (val) => {
      fileList.value = parseModelValue(val);
    }, { deep: true, immediate: true });

    // 初始化拖拽排序
    const initSortable = () => {
      if (props.drag && !props.disabled) {
        nextTick(() => {
          // 使用延时确保DOM已经渲染完成
          setTimeout(() => {
            const el = document.querySelector('.ant-upload-list-picture-card');
            if (el) {
              Sortable.create(el, {
                ghostClass: 'image-upload-ghost',
                onEnd: (evt) => {
                  const targetIndex = evt.newIndex;
                  const currentIndex = evt.oldIndex;
                  if (targetIndex !== currentIndex) {
                    const currentItem = fileList.value.splice(currentIndex, 1)[0];
                    fileList.value.splice(targetIndex, 0, currentItem);
                    updateModelValue();
                  }
                }
              });
            }
          }, 200);
        });
      }
    };

    // 上传前校检格式和大小
    const handleBeforeUpload = (file) => {
      let isImg = false;
      if (props.fileType && props.fileType.length > 0) {
        let fileExtension = "";
        if (file.name.lastIndexOf(".") > -1) {
          fileExtension = file.name.slice(file.name.lastIndexOf(".") + 1).toLowerCase();
        }
        isImg = props.fileType.some(type => {
          if (file.type.indexOf(type) > -1) return true;
          if (fileExtension && fileExtension.indexOf(type.toLowerCase()) > -1) return true;
          return false;
        });
      } else {
        isImg = file.type.indexOf("image") > -1;
      }

      if (!isImg) {
        message.error(`文件格式不正确，请上传${props.fileType.join("/")}图片格式文件!`);
        return false;
      }

      // 校检文件名是否包含特殊字符
      if (file.name.includes(',')) {
        message.error('文件名不正确，不能包含英文逗号!');
        return false;
      }

      // 校检文件大小
      if (props.fileSize) {
        const isLt = file.size / 1024 / 1024 < props.fileSize;
        if (!isLt) {
          message.error(`上传图片大小不能超过 ${props.fileSize} MB!`);
          return false;
        }
      }

      // 检查数量限制
      if (props.limit > 0 && fileList.value.length >= props.limit) {
        message.error(`最多只能上传 ${props.limit} 张图片!`);
        return false;
      }

      uploading.value = true;
      return true;
    };

    // 处理上传状态变化
    const handleChange = (info) => {
      // 更新文件列表
      let newFileList = [...info.fileList];

      // 处理上传状态
      if (info.file.status === 'uploading') {
        uploading.value = true;
      } else if (info.file.status === 'done') {
        uploading.value = false;
        if (info.file.response && info.file.response.code === 200) {
          // 设置文件URL
          const uploadedFile = newFileList.find(item => item.uid === info.file.uid);
          if (uploadedFile) {
            uploadedFile.url = baseUrl + info.file.response.fileName;
            uploadedFile.thumbUrl = baseUrl + info.file.response.fileName;
            uploadedFile.response = info.file.response;
          }
          message.success(`${info.file.name} 上传成功`);
          fileList.value = newFileList;
          updateModelValue();
        } else {
          message.error(info.file.response?.msg || `${info.file.name} 上传失败`);
          // 移除上传失败的文件
          fileList.value = newFileList.filter(item => item.uid !== info.file.uid);
        }
      } else if (info.file.status === 'error') {
        uploading.value = false;
        message.error(`${info.file.name} 上传失败`);
        // 移除上传失败的文件
        fileList.value = newFileList.filter(item => item.uid !== info.file.uid);
      } else if (info.file.status === 'removed') {
        fileList.value = newFileList;
        updateModelValue();
      }

      // 触发change事件
      emit('change', info);
    };

    // 删除图片
    const handleDelete = (file) => {
      const index = fileList.value.findIndex(item => item.uid === file.uid);
      if (index !== -1) {
        fileList.value.splice(index, 1);
        updateModelValue();
      }
      return true;
    };

    // 预览图片
    const handlePreview = (file) => {
      previewImage.value = file.url || file.thumbUrl;
      previewVisible.value = true;
      previewTitle.value = file.name || getFileName(file.url);
    };

    // 更新表单值
    const updateModelValue = () => {
      // 过滤出已上传成功的文件
      const successFiles = fileList.value.filter(file =>
          file.status === 'done' && (file.response?.fileName || (file.url && !file.uid.startsWith('rc-upload-')))
      );

      const urls = successFiles.map(file => {
        if (file.response && file.response.fileName) {
          return file.response.fileName;
        }
        let url = file.url || '';
        // 移除baseUrl前缀
        if (url.startsWith(baseUrl)) {
          url = url.replace(baseUrl, '');
        }
        return url;
      }).filter(url => url).join(',');

      emit('update:modelValue', urls);

      // 调试输出
      console.log('更新图片值:', urls);
      console.log('当前文件列表:', fileList.value);
    };

    // 获取文件名称
    const getFileName = (name) => {
      if (!name) return '';
      // 如果是url那么取最后的名字 如果不是直接返回
      if (name.lastIndexOf("/") > -1) {
        return name.slice(name.lastIndexOf("/") + 1);
      } else {
        return name;
      }
    };

    // 组件挂载后初始化
    onMounted(() => {
      // 初始化拖拽
      initSortable();

      // 如果有初始值，确保文件列表正确初始化
      if (props.modelValue) {
        fileList.value = parseModelValue(props.modelValue);
      }
    });

    return {
      fileList,
      uploading,
      previewVisible,
      previewImage,
      previewTitle,
      baseUrl,
      uploadImgUrl,
      headers,
      showTip,
      handleBeforeUpload,
      handleChange,
      handleDelete,
      handlePreview,
      getFileName
    };
  }
});
</script>

<style scoped lang="less">
.image-upload-container {
  .upload-tip {
    margin-top: 8px;
    font-size: 12px;
    color: #606266;
  }

  :deep(.ant-upload-list-item) {
    transition: all 0.3s;
  }

  :deep(.image-upload-ghost) {
    opacity: 0.5;
    background: #e6f7ff;
    border: 1px dashed #1890ff;
  }

  :deep(.ant-upload-list-picture-card-container) {
    width: 104px;
    height: 104px;
    margin-right: 8px;
    margin-bottom: 8px;
  }

  :deep(.ant-upload.ant-upload-select-picture-card) {
    width: 104px;
    height: 104px;
    margin-right: 8px;
    margin-bottom: 8px;
  }
}
</style>