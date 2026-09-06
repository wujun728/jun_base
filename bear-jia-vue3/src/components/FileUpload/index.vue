<template>
  <div class="file-upload-container">
    <a-upload-dragger
        v-if="!disabled && (fileList.length < limit || limit === 0)"
        :file-list="fileList"
        :action="uploadFileUrl"
        :headers="headers"
        :before-upload="handleBeforeUpload"
        :remove="handleDelete"
        :multiple="true"
        :data="data"
        :disabled="disabled"
        :show-upload-list="false"
        @change="handleChange"
        class="upload-dragger-card"
    >
      <p class="ant-upload-drag-icon">
        <inbox-outlined :style="{ fontSize: '48px', color: '#1890ff' }" />
      </p>
      <p class="ant-upload-text">点击或拖拽文件到此区域上传</p>
      <p class="ant-upload-hint" v-if="showTip">
        <template v-if="fileSize">大小不超过 <b style="color: #ff4d4f">{{ fileSize }}MB</b></template>
        <template v-if="fileType && fileSize"> · </template>
        <template v-if="fileType">格式为 <b style="color: #ff4d4f">{{ fileType.join("/") }}</b></template>
      </p>
    </a-upload-dragger>

    <!-- 文件列表 -->
    <div class="file-list" v-if="fileList.length > 0">
      <div
        v-for="file in fileList"
        :key="file.uid"
        class="file-item"
      >
        <div class="file-item-content">
          <file-outlined :style="{ fontSize: '18px', color: '#1890ff' }" class="file-icon" />
          <div class="file-info">
            <div class="file-name-row">
              <a-tooltip :title="getFileName(file.name || file.response?.fileName)" placement="topLeft">
                <a
                  v-if="file.status === 'done' && (file.url || file.response?.fileName)"
                  :href="`${baseUrl}${file.url || file.response?.fileName}`"
                  target="_blank"
                  class="file-name"
                >
                  {{ getFileName(file.name || file.response?.fileName) }}
                </a>
                <span
                  v-else
                  class="file-name"
                >
                  {{ getFileName(file.name || file.response?.fileName) }}
                </span>
              </a-tooltip>
              <a-tag
                v-if="file.status === 'uploading'"
                color="processing"
                class="file-status-tag"
              >
                上传中
              </a-tag>
              <a-tag
                v-else-if="file.status === 'done'"
                color="success"
                class="file-status-tag"
              >
                已完成
              </a-tag>
              <a-tag
                v-else-if="file.status === 'error'"
                color="error"
                class="file-status-tag"
              >
                失败
              </a-tag>
            </div>
            <div class="file-progress" v-if="file.status === 'uploading'">
              <a-progress :percent="Math.round(file.percent || 0)" size="small" :show-info="false" />
            </div>
          </div>
          <a-tooltip title="删除" placement="top">
            <a-button
              v-if="!disabled"
              type="text"
              danger
              size="small"
              @click="handleDelete(file)"
              class="delete-btn"
            >
              <delete-outlined />
            </a-button>
          </a-tooltip>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { defineComponent, ref, watch, computed, nextTick } from 'vue';
import { message } from 'ant-design-vue';
import { InboxOutlined, FileOutlined, DeleteOutlined } from '@ant-design/icons-vue';
import { getToken } from "@/utils/auth";
import Sortable from 'sortablejs';

export default defineComponent({
  name: "FileUpload",
  components: {
    InboxOutlined,
    FileOutlined,
    DeleteOutlined
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
    // 数量限制
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
      default: () => ["doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt", "pdf"]
    },
    // 是否显示提示
    isShowTip: {
      type: Boolean,
      default: true
    },
    // 禁用组件（仅查看文件）
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
    const uploadListRef = ref(null);

    const baseUrl = import.meta.env.VITE_APP_BASE_API || '';
    const uploadFileUrl = computed(() => baseUrl + props.action);

    const headers = {
      Authorization: "Bearer " + getToken(),
    };

    // 是否显示提示
    const showTip = computed(() => {
      return props.isShowTip && (props.fileType.length > 0 || props.fileSize > 0);
    });

    // 监听值变化
    watch(() => props.modelValue, (val) => {
      if (val) {
        let list = [];
        // 首先将值转为数组
        const values = Array.isArray(val) ? val : val.split(',');
        // 然后将数组转为对象数组
        list = values.map((item, index) => {
          if (typeof item === "string") {
            return {
              uid: `-${index}`,
              name: getFileName(item),
              status: 'done',
              url: item
            };
          }
          return item;
        });
        fileList.value = list;
      } else if (val === '' && fileList.value.length === 0) {
        // 只有当值为空字符串且文件列表也为空时才清空
        // 这样可以避免在上传过程中被清空
        fileList.value = [];
      }
    }, { deep: true, immediate: true });

    // 初始化拖拽排序
    const initSortable = () => {
      if (props.drag && !props.disabled) {
        nextTick(() => {
          // 使用延时确保DOM已经渲染完成
          setTimeout(() => {
            const el = document.querySelector('.ant-upload-list');
            if (el) {
              Sortable.create(el, {
                ghostClass: 'file-upload-ghost',
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
      // 校检文件类型
      if (props.fileType && props.fileType.length > 0) {
        const fileName = file.name.split('.');
        const fileExt = fileName[fileName.length - 1].toLowerCase();
        const isTypeOk = props.fileType.map(type => type.toLowerCase()).indexOf(fileExt) >= 0;
        if (!isTypeOk) {
          message.error(`文件格式不正确，请上传${props.fileType.join("/")}格式文件!`);
          return false;
        }
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
          message.error(`上传文件大小不能超过 ${props.fileSize} MB!`);
          return false;
        }
      }

      uploading.value = true;
      return true;
    };

    // 处理上传状态变化
    const handleChange = (info) => {
      // 更新文件列表
      fileList.value = [...info.fileList];

      // 向父组件emit change事件
      emit('change', info);

      if (info.file.status === 'uploading') {
        uploading.value = true;
      } else if (info.file.status === 'done') {
        uploading.value = false;
        if (info.file.response && info.file.response.code === 200) {
          message.success(`${info.file.name} 上传成功`);
          updateModelValue();
        } else {
          message.error(info.file.response?.msg || `${info.file.name} 上传失败`);
          // 上传失败时从列表中移除
          const index = fileList.value.findIndex(item => item.uid === info.file.uid);
          if (index !== -1) {
            fileList.value.splice(index, 1);
          }
        }
      } else if (info.file.status === 'error') {
        uploading.value = false;
        message.error(`${info.file.name} 上传失败`);
        // 上传失败时从列表中移除
        const index = fileList.value.findIndex(item => item.uid === info.file.uid);
        if (index !== -1) {
          fileList.value.splice(index, 1);
        }
      } else if (info.file.status === 'removed') {
        updateModelValue();
      }
    };

    // 删除文件
    const handleDelete = (file) => {
      const index = fileList.value.findIndex(item => item.uid === file.uid);
      if (index !== -1) {
        fileList.value.splice(index, 1);
        updateModelValue();
      }
      return true;
    };

    // 更新表单值
    const updateModelValue = () => {
      const urls = fileList.value.map(file => {
        if (file.response && file.response.fileName) {
          return file.response.fileName;
        }
        return file.url || '';
      }).filter(url => url).join(',');

      emit('update:modelValue', urls);
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

    // 组件挂载后初始化拖拽
    nextTick(() => {
      initSortable();
    });

    return {
      fileList,
      uploading,
      uploadListRef,
      baseUrl,
      uploadFileUrl,
      headers,
      showTip,
      handleBeforeUpload,
      handleChange,
      handleDelete,
      getFileName
    };
  }
});
</script>

<style scoped lang="less">
.file-upload-container {
  .upload-dragger-card {
    :deep(.ant-upload-drag) {
      border: 2px dashed #d9d9d9;
      border-radius: 8px;
      background: #fafafa;
      transition: all 0.3s;
      padding: 20px;

      &:hover {
        border-color: #1890ff;
        background: #f0f8ff;
      }
    }

    .ant-upload-drag-icon {
      margin-bottom: 16px;
    }

    .ant-upload-text {
      font-size: 16px;
      color: #333;
      margin-bottom: 8px;
      font-weight: 500;
    }

    .ant-upload-hint {
      font-size: 13px;
      color: #666;
      line-height: 1.6;
    }
  }

  .file-list {
    margin-top: 16px;
    max-height: 400px;
    overflow-y: auto;

    .file-item {
      padding: 12px;
      margin-bottom: 8px;
      border: 1px solid #e8e8e8;
      border-radius: 6px;
      background: #fafafa;
      transition: all 0.3s;

      &:hover {
        background: #f0f0f0;
        border-color: #d9d9d9;
      }

      .file-item-content {
        display: flex;
        align-items: center;
        gap: 12px;

        .file-icon {
          flex-shrink: 0;
        }

        .file-info {
          flex: 1;
          min-width: 0;
          display: flex;
          flex-direction: column;
          gap: 4px;

          .file-name-row {
            display: flex;
            align-items: center;
            gap: 8px;
            width: 100%;
            min-height: 22px;

            :deep(.ant-tooltip) {
              flex: 1;
              min-width: 0;
              display: flex;
            }

            .file-name {
              flex: 1;
              font-size: 14px;
              color: #333;
              text-decoration: none;
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
              min-width: 0;
              display: block;

              &:hover {
                color: #1890ff;
              }
            }

            .file-status-tag {
              flex-shrink: 0;
              margin: 0;
            }
          }

          .file-progress {
            width: 100%;
          }
        }

        .delete-btn {
          flex-shrink: 0;
          padding: 4px 8px;
          height: 32px;
        }
      }
    }
  }

  :deep(.file-upload-ghost) {
    opacity: 0.5;
    background: #e6f7ff;
    border: 1px dashed #1890ff;
  }
}
</style>
