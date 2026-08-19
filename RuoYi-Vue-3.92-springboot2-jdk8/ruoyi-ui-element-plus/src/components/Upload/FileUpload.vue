<!-- 文件上传组件 -->
<template>
  <div>
    <el-upload
      v-model:file-list="fileList"
      :style="props.style"
      :before-upload="handleBeforeUpload"
      :http-request="handleUpload"
      :on-success="handleSuccess"
      :on-error="handleError"
      :on-exceed="handleExceed"
      :accept="props.accept"
      :limit="props.limit"
      multiple
    >
      <!-- 上传文件按钮 -->
      <el-button type="primary" :disabled="fileList.length >= props.limit">
        {{ props.uploadBtnText }}
      </el-button>

      <!-- 文件列表 -->
      <template #file="{ file }">
        <template v-if="file.status === 'success'">
          <div class="el-upload-list__item-info">
            <a class="el-upload-list__item-name" @click="handleDownload(file)">
              <el-icon>
                <Document />
              </el-icon>
              <span class="el-upload-list__item-file-name">{{ file.name }}</span>
              <span class="el-icon--close" @click.stop="handleRemove(file.url)">
                <el-icon>
                  <Close />
                </el-icon>
              </span>
            </a>
          </div>
        </template>
        <template v-else>
          <div class="el-upload-list__item-info">
            <el-progress style="display: inline-flex" :percentage="file.percentage" />
          </div>
        </template>
      </template>
    </el-upload>
  </div>
</template>

<script setup>
import FileAPI from "@/api/file";

const props = defineProps({
  /**
   * 请求携带的额外参数
   */
  data: {
    type: Object,
    default: () => {
      return {};
    },
  },
  /**
   * 上传文件的参数名
   */
  name: {
    type: String,
    default: "file",
  },
  /**
   * 文件上传数量限制
   */
  limit: {
    type: Number,
    default: 10,
  },
  /**
   * 单个文件上传大小限制(单位MB)
   */
  maxFileSize: {
    type: Number,
    default: 10,
  },
  /**
   * 上传文件类型
   */
  accept: {
    type: String,
    default: "*",
  },
  /**
   * 上传按钮文本
   */
  uploadBtnText: {
    type: String,
    default: "上传文件",
  },

  /**
   * 样式
   */
  style: {
    type: Object,
    default: () => {
      return {
        width: "300px",
      };
    },
  },
});

const modelValue = defineModel("modelValue", {
  type: Array,
  required: true,
  default: () => [],
});

const fileList = ref([]);

// 监听 modelValue 转换用于显示的 fileList
watch(
  modelValue,
  (value) => {
    fileList.value = value.map((item) => {
      const name = item.name ? item.name : item.url?.substring(item.url.lastIndexOf("/") + 1);
      return {
        name,
        url: item.url,
        status: "success",
        uid: getUid(),
      };
    });
  },
  {
    immediate: true,
  }
);

/**
 * 上传前校验
 */
function handleBeforeUpload(file) {
  // 限制文件大小
  if (file.size > props.maxFileSize * 1024 * 1024) {
    ElMessage.warning("上传文件不能大于" + props.maxFileSize + "M");
    return false;
  }
  return true;
}

/*
 * 上传文件
 */
function handleUpload(options) {
  return new Promise((resolve, reject) => {
    const file = options.file;

    const formData = new FormData();
    formData.append(props.name, file);

    // 处理附加参数
    Object.keys(props.data).forEach((key) => {
      formData.append(key, props.data[key]);
    });

    FileAPI.upload(formData, (percent) => {
      const uid = file.uid;
      const fileItem = fileList.value.find((file) => file.uid === uid);
      if (fileItem) {
        fileItem.percentage = percent;
      }
    })
      .then((res) => {
        resolve(res);
      })
      .catch((err) => {
        reject(err);
      });
  });
}

/**
 * 上传成功
 */
const handleSuccess = (response, uploadFile, files) => {
  ElMessage.success("上传成功");
  // 只有当状态为 success 或者 fail，代表文件上传全部完成了，失败也算完成
  if (
    files.every((file) => {
      return file.status === "success" || file.status === "fail";
    })
  ) {
    const fileInfos = [];
    files.map((file) => {
      if (file.status === "success") {
        // 只取携带 response 的才是刚上传的
        const res = file.response;
        if (res) {
          fileInfos.push({ name: res.name, url: res.url });
        }
      } else {
        //失败上传 从fileList删掉，不展示
        fileList.value.splice(
          fileList.value.findIndex((e) => e.uid === file.uid),
          1
        );
      }
    });
    if (fileInfos.length > 0) {
      modelValue.value = [...modelValue.value, ...fileInfos];
    }
  }
};

/**
 * 上传失败
 */
const handleError = (_error) => {
  console.error(_error);
  ElMessage.error("上传失败");
};

/**
 * 删除文件
 */
function handleRemove(fileUrl) {
  FileAPI.delete(fileUrl).then(() => {
    modelValue.value = modelValue.value.filter((file) => file.url !== fileUrl);
  });
}

/**
 * 下载文件
 */
function handleDownload(file) {
  const { url, name } = file;
  if (url) {
    FileAPI.download(url, name);
  }
}

/** 获取一个不重复的id */
function getUid() {
  // 时间戳左移13位（相当于乘以8192） + 13位随机数
  return (Date.now() << 13) | Math.floor(Math.random() * 8192);
}
</script>
<style lang="scss" scoped>
.el-upload-list__item .el-icon--close {
  position: absolute;
  top: 50%;
  right: 5px;
  color: var(--el-text-color-regular);
  cursor: pointer;
  opacity: 0.75;
  transform: translateY(-50%);
  transition: opacity var(--el-transition-duration);
}

:deep(.el-upload-list) {
  margin: 0;
}

:deep(.el-upload-list__item) {
  margin: 0;
}
</style>
