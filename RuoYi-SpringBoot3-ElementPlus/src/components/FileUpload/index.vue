<template>
  <div class="upload-file">
    <el-upload
      multiple
      :accept="props.accept"
      :action="uploadFileUrl"
      :before-upload="handleBeforeUpload"
      :file-list="fileList"
      :data="props.data"
      :limit="limit"
      :on-error="handleUploadError"
      :on-exceed="handleExceed"
      :on-success="handleUploadSuccess"
      :show-file-list="false"
      :headers="headers"
      class="upload-file-uploader"
      ref="uploadFileList"
    >
      <!-- 上传按钮 -->
      <el-button :link="props.isLink" type="primary">{{ uploadBtnText }}</el-button>
    </el-upload>
    <!-- 上传提示 -->
    <div class="el-upload__tip" v-if="showTip && !disabled">
      请上传
      <template v-if="fileSize">
        大小不超过 <b style="color: #f56c6c">{{ fileSize }}MB</b>
      </template>
      <template v-if="fileType">
        格式为 <b style="color: #f56c6c">{{ fileType.join("/") }}</b>
      </template>
      的文件
    </div>
    <!-- 文件列表 -->
    <transition-group ref="uploadFileList"
      v-if="isShowFileList"
      class="upload-file-list el-upload-list el-upload-list--text"
      name="el-fade-in-linear"
      tag="ul"
    >
      <li
        :key="file.uid"
        class="el-upload-list__item ele-upload-list__item-content"
        v-for="(file, index) in fileList"
      >
        <el-link
          :href="`${baseUrl}${file.url}`"
          underline="never"
          target="_blank"
        >
          <span class="el-icon-document"> {{ getFileName(file.name) }} </span>
        </el-link>
        <div class="ele-upload-list__item-content-action">
          <el-link underline="never" @click="handleDelete(index)" type="danger" v-if="!disabled"
            >删除</el-link
          >
        </div>
      </li>
    </transition-group>
  </div>
</template>

<script setup>
import { getToken } from "@/utils/auth";
import Sortable from 'sortablejs'

const props = defineProps({
  modelValue: [String, Object, Array],
  accept: {
    type: String,
    default: "*",
  },
  uploadBtnText: {
    type: String,
    default: "选取文件",
  },
  // 上传接口地址
  action: {
    type: String,
    default: "/common/upload"
  },
  // 上传携带的参数
  data: {
    type: Object
  },
  // 数量限制
  limit: {
    type: Number,
    default: 5,
  },
  // 大小限制(MB)
  fileSize: {
    type: Number,
    default: 50,
  },
  // 文件类型, 例如['png', 'jpg', 'jpeg']
  fileType: {
    type: Array,
    default: () => ["doc","docx", "xls","xlsx", "ppt", "txt", "pdf"],
  },
  // 是否显示提示
  isShowTip: {
    type: Boolean,
    default: true,
  },
  // 禁用组件（仅查看文件）
  disabled: {
    type: Boolean,
    default: false
  },
  isShowFileList: {
    type: Boolean,
    default: true,
  },
  isImport: {
    type: Boolean,
    default: false,
  },
  isLink: {
    type: Boolean,
    default: false,
  },
  updateSupport: {
    type: Boolean,
    default: false,
  },
  // 拖动排序
  drag: {
    type: Boolean,
    default: true
  }
});

const { proxy } = getCurrentInstance();
const emit = defineEmits();
const number = ref(0);
const uploadList = ref([]);
const baseUrl = import.meta.env.VITE_APP_BASE_API;
const uploadFileUrl = ref(
  import.meta.env.VITE_APP_BASE_API + props.action
); // 上传文件服务器地址
const headers = ref({ Authorization: "Bearer " + getToken() });
const fileList = ref([]);
const showTip = computed(
  () => props.isShowTip && (props.fileType || props.fileSize)
);

watch(
  () => props.action,
  (val) => {
    if (val) {
      uploadFileUrl.value =
        import.meta.env.VITE_APP_BASE_API +
        val +
        (val.indexOf("?") == -1 ? "?" : "&") +
        "updateSupport=" +
        props.updateSupport;
    }
  },
  { deep: true, immediate: true }
);

watch(
  () => props.modelValue,
  (val) => {
    if (val && !props.isImport) {
      let temp = 1;
      // 首先将值转为数组
      const list = Array.isArray(val) ? val : JSON.parse(val);
      // 然后将数组转为对象数组
      fileList.value = list.map((item) => {
        if (typeof item === "string") {
          item = { name: item, url: item };
        }
        item.uid = item.uid || new Date().getTime() + temp++;
        return item;
      });
    } else {
      fileList.value = [];
      return [];
    }
  },
  { deep: true, immediate: true }
);

// 上传前校检格式和大小
function handleBeforeUpload(file) {
  // 校检文件类型
  if (props.fileType.length) {
    const fileName = file.name.split(".");
    const fileExt = fileName[fileName.length - 1];
    const isTypeOk = props.fileType.indexOf(fileExt) >= 0;
    if (!isTypeOk) {
      proxy.$modal.msgError(
        `文件格式不正确，请上传${props.fileType.join("/")}格式文件!`
      );
      return false;
    }
  }
  // 校检文件名是否包含特殊字符
  if (file.name.includes(",")) {
    proxy.$modal.msgError("文件名不正确，不能包含英文逗号!");
    return false;
  }
  // 校检文件大小
  if (props.fileSize) {
    const isLt = file.size / 1024 / 1024 < props.fileSize;
    if (!isLt) {
      proxy.$modal.msgError(`上传文件大小不能超过 ${props.fileSize} MB!`);
      return false;
    }
  }
  proxy.$modal.loading("正在上传文件，请稍候...");
  number.value++;
  return true;
}

// 文件个数超出
function handleExceed() {
  proxy.$modal.msgError(`上传文件数量不能超过 ${props.limit} 个!`);
}

// 上传失败
function handleUploadError(err) {
  proxy.$modal.msgError("上传文件失败");
  proxy.$modal.closeLoading();
}

// 上传成功回调
function handleUploadSuccess(res, file) {
  if (props.isImport) {
    proxy.$modal.closeLoading();
    if (res.code === 200) {
      proxy.$modal.msgSuccess("导入成功");
    } else {
      proxy.$modal.msgError({
        showClose: true,
        dangerouslyUseHTMLString: true,
        message: res.msg,
      });
    }
    emit("update:modelValue", res);
  } else {
    if (res.code === 200) {
      uploadList.value.push({ name: res.fileName, url: res.fileName });
      uploadedSuccessfully();
    } else {
      number.value--;
      proxy.$modal.closeLoading();
      proxy.$modal.msgError({
        showClose: true,
        dangerouslyUseHTMLString: true,
        message: res.msg,
      });
      proxy.$refs.uploadFileList.handleRemove(file);
      uploadedSuccessfully();
    }
  }
}

// 删除文件
function handleDelete(index) {
  fileList.value.splice(index, 1);
  emit("update:modelValue", listToJsonString(fileList.value));
}

// 上传结束处理
function uploadedSuccessfully() {
  if (number.value > 0 && uploadList.value.length === number.value) {
    fileList.value = fileList.value
      .filter((f) => f.url !== undefined)
      .concat(uploadList.value);
    uploadList.value = [];
    number.value = 0;
    emit("update:modelValue", listToJsonString(fileList.value));
    proxy.$modal.closeLoading();
  }
}

// 获取文件名称
function getFileName(name) {
  // 如果是url那么取最后的名字 如果不是直接返回
  if (name.lastIndexOf("/") > -1) {
    return name.slice(name.lastIndexOf("/") + 1);
  } else {
    return name;
  }
}

// 对象转成指定字符串分隔
function listToString(list, separator) {
  let strs = "";
  separator = separator || ",";
  for (let i in list) {
    if (list[i].url) {
      strs += list[i].url + separator;
    }
  }
  return strs != "" ? strs.substr(0, strs.length - 1) : "";
}
function listToJsonString(list){
  let arr = [];
  for (let i in list) {
    if (undefined !== list[i].url && list[i].url.indexOf("blob:") !== 0) {
      arr.push(list[i].url.replace(baseUrl, ""));
    }
  }
  return JSON.stringify(arr);
}

// 初始化拖拽排序
onMounted(() => {
  if (props.drag && !props.disabled) {
    nextTick(() => {
      const element = proxy.$refs.uploadFileList?.$el || proxy.$refs.uploadFileList
      Sortable.create(element, {
        ghostClass: 'file-upload-darg',
        onEnd: (evt) => {
          const movedItem = fileList.value.splice(evt.oldIndex, 1)[0]
          fileList.value.splice(evt.newIndex, 0, movedItem)
          emit('update:modelValue', listToJsonString(fileList.value))
        }
      })
    })
  }
})
</script>

<style scoped lang="scss">
.file-upload-darg {
  opacity: 0.5;
  background: #c8ebfb;
}

.upload-file-uploader {
  margin-bottom: 5px;
}
.upload-file-list .el-upload-list__item {
  border: 1px solid #e4e7ed;
  line-height: 2;
  margin-bottom: 10px;
  position: relative;
  transition: none !important;
}
.upload-file-list .ele-upload-list__item-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: inherit;
}
.ele-upload-list__item-content-action .el-link {
  margin-right: 10px;
}
</style>
