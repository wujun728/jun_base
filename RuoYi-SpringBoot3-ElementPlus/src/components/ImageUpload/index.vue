<template>
  <div class="component-upload-image">
    <el-upload
      :disabled="props.disabled"
      :accept="props.accept"
      drag
      multiple
      :action="uploadImgUrl"
      list-type="picture-card"
      :on-success="handleUploadSuccess"
      :before-upload="handleBeforeUpload"
      :data="props.data"
      :limit="limit"
      :on-error="handleUploadError"
      :on-exceed="handleExceed"
      ref="imageUpload"
      :before-remove="handleDelete"
      :show-file-list="true"
      :headers="headers"
      :file-list="fileList"
      :on-preview="handlePictureCardPreview"
      :class="{ hide: fileList.length >= limit }"
    >
      <el-icon class="avatar-uploader-icon"><plus /></el-icon>

      <template #file="{ file }">
        <div>
          <img
            loading="lazy"
            v-if="
              ['png', 'jpg', 'jpeg'].indexOf(
                file.url.slice(file.url.lastIndexOf('.') + 1)
              ) > -1
            "
            class="el-upload-list__item-thumbnail"
            :src="file.url"
            alt=""
          />
          <video
            v-else
            :src="file.url"
            class="el-upload-list__item-thumbnail"
          ></video>
          <span :id="file.name" curr="" class="el-upload-list__item-actions">
            <span
              class="el-upload-list__item-preview"
              @click="handlePictureCardPreview(file)"
            >
              <el-icon><zoom-in /></el-icon>
            </span>
            <span
              v-if="!disabled"
              class="el-upload-list__item-delete"
              @click="handleDelete(file)"
            >
              <el-icon><Delete /></el-icon>
            </span>
          </span>
        </div>
      </template>
    </el-upload>
    <!-- 上传提示 -->
    <div class="el-upload__tip" v-if="showTip">
      请上传
      <template v-if="fileSize">
        大小不超过 <b style="color: #f56c6c">{{ fileSize }}MB</b>
      </template>
      <template v-if="fileType">
        格式为 <b style="color: #f56c6c">{{ fileType.join("/") }}</b>
      </template>
      的文件
    </div>

    <el-dialog
      v-model="dialogVisible"
      title="预览"
      width="800px"
      append-to-body
      :lock-scroll="false"
      destroy-on-close
      :fullscreen="proxy.isPhone"
    >
      <img
        v-if="
          ['png', 'jpg', 'jpeg'].indexOf(
            dialogImageUrl.slice(dialogImageUrl.lastIndexOf('.') + 1)
          ) > -1
        "
        :src="dialogImageUrl"
        :style="{
          height: 'calc(' + (proxy.isPhone ? '100vh' : '70vh') + ' - 110px)',
        }"
        style="display: block; max-width: 100%; margin: 0 auto"
      />
      <video
        v-else
        autoplay
        controls
        :style="{
          height: 'calc(' + (proxy.isPhone ? '100vh' : '70vh') + ' - 110px)',
        }"
        style="display: block; max-width: 100%; margin: 0 auto"
      >
        <source :src="dialogImageUrl" />
      </video>
    </el-dialog>
    <el-image-viewer
      hide-on-click-modal
      teleported
      @close="
        () => {
          showViewer = false;
        }
      "
      v-if="showViewer"
      :url-list="previewList"
      :initial-index="previewIndex"
    />
  </div>
</template>

<script setup>
import clone from "xe-utils/clone";
import Sortable from "sortablejs";
import { getToken } from "@/utils/auth";
import { isExternal } from "@/utils/validate";

onMounted(() => {
  if (props.drag && !props.disabled) {
    nextTick(() => {
      const element = proxy.$refs.imageUpload?.$el?.querySelector('.el-upload-list')
      Sortable.create(element, {
        onEnd: (evt) => {
          const movedItem = fileList.value.splice(evt.oldIndex, 1)[0]
          fileList.value.splice(evt.newIndex, 0, movedItem)
          emit('update:modelValue', listToJsonString(fileList.value))
        }
      })
    })
  }
});

const props = defineProps({
  modelValue: [String, Object, Array],
  // 上传接口地址
  action: {
    type: String,
    default: "/common/upload",
  },
  // 上传携带的参数
  data: {
    type: Object,
  },
  disabled: {
    type: Boolean,
    default: false,
  },
  // https://developer.mozilla.org/en-US/docs/Web/HTML/Attributes/accept
  accept: {
    type: String,
    default: "*",
  },
  // 图片数量限制
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
    default: () => ["png", "jpg", "jpeg", "mp4", "webm", "ogg", "mp3"],
  },
  // 是否显示提示
  isShowTip: {
    type: Boolean,
    default: true,
  },
  // 拖动排序
  drag: {
    type: Boolean,
    default: true,
  },
});

const { proxy } = getCurrentInstance();
const emit = defineEmits();
const number = ref(0);
const uploadList = ref([]);
const dialogImageUrl = ref("");
const dialogVisible = ref(false);
const baseUrl = import.meta.env.VITE_APP_BASE_API;
const uploadImgUrl = ref(import.meta.env.VITE_APP_BASE_API + props.action); // 上传的图片服务器地址
const headers = ref({ Authorization: "Bearer " + getToken() });
const fileList = ref([]);
const showTip = computed(
  () => props.isShowTip && (props.fileType || props.fileSize)
);
const showViewer = ref(false);
const previewList = ref([]);
const previewIndex = ref(0);

watch(
  () => props.action,
  (val) => {
    if (val) {
      uploadImgUrl.value = import.meta.env.VITE_APP_BASE_API + val;
    }
  },
  { deep: true, immediate: true }
);

watch(
  () => props.modelValue,
  (val) => {
    if (val) {
      // 首先将值转为数组
      const list = Array.isArray(val) ? val : JSON.parse(val);
      // 然后将数组转为对象数组
      fileList.value = list.map((item) => {
        if (typeof item === "string") {
          if (item.indexOf(baseUrl) === -1 && !isExternal(item)) {
            item = { name: baseUrl + item, url: baseUrl + item };
          } else {
            item = { name: item, url: item };
          }
        }
        return item;
      });
      if ("image/*" == props.accept)
        previewList.value = fileList.value.map((item) => item.url);
    } else {
      fileList.value = [];
      previewList.value = [];
      return [];
    }
  },
  { deep: true, immediate: true }
);

// 上传前loading加载
function handleBeforeUpload(file) {
  let isImg = false;
  if (props.fileType.length) {
    let fileExtension = "";
    if (file.name.lastIndexOf(".") > -1) {
      fileExtension = file.name.slice(file.name.lastIndexOf(".") + 1);
    }
    isImg = props.fileType.some((type) => {
      if (file.type.indexOf(type) > -1) return true;
      if (fileExtension && fileExtension.indexOf(type) > -1) return true;
      return false;
    });
  } else {
    isImg = file.type.indexOf("image") > -1;
  }
  if (!isImg) {
    proxy.$modal.msgError(
      `文件格式不正确, 请上传${props.fileType.join("/")}格式文件!`
    );
    return false;
  }
  if (file.name.includes(",")) {
    proxy.$modal.msgError("文件名不正确，不能包含英文逗号!");
    return false;
  }
  if (props.fileSize) {
    const isLt = file.size / 1024 / 1024 < props.fileSize;
    if (!isLt) {
      proxy.$modal.msgError(`上传大小不能超过 ${props.fileSize} MB!`);
      return false;
    }
  }
  proxy.$modal.loading("正在上传，请稍候...");
  number.value++;
}

// 文件个数超出
function handleExceed() {
  proxy.$modal.msgError(`上传数量不能超过 ${props.limit} 个!`);
}

// 上传成功回调
function handleUploadSuccess(res, file) {
  if (res.code === 200) {
    uploadList.value.push({ name: res.fileName, url: res.fileName });
    uploadedSuccessfully();
  } else {
    number.value--;
    proxy.$modal.closeLoading();
    proxy.$modal.msgError(res.msg);
    proxy.$refs.imageUpload.handleRemove(file);
    uploadedSuccessfully();
  }
}

// 删除图片
function handleDelete(file) {
  const isCurr = getCurr(file.name);
  setCurr(file.name);
  if (!isCurr) return;
  const findex = fileList.value.map((f) => f.name).indexOf(file.name);
  if (findex > -1 && uploadList.value.length === number.value) {
    fileList.value.splice(findex, 1);
    emit("update:modelValue", listToJsonString(fileList.value));
    return false;
  }
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

// 上传失败
function handleUploadError() {
  proxy.$modal.msgError("上传失败");
  proxy.$modal.closeLoading();
}

function setCurr(id) {
  for (let i = 0; i < fileList.value.length; i++) {
    const file = fileList.value[i];
    document
      .getElementById(file.name)
      .setAttribute("curr", file.name == id ? "curr" : "");
  }
}

function getCurr(id) {
  return document.getElementById(id).getAttribute("curr") == "curr";
}

// 预览
function handlePictureCardPreview(file) {
  setCurr(file.name);
  if ("image/*" == props.accept) {
    showViewer.value = true;
    previewIndex.value = previewList.value.indexOf(file.url);
  } else {
    dialogImageUrl.value = file.url;
    dialogVisible.value = true;
  }
}

// 对象转成指定字符串分隔
function listToString(list, separator) {
  let strs = "";
  separator = separator || ",";
  for (let i in list) {
    if (undefined !== list[i].url && list[i].url.indexOf("blob:") !== 0) {
      strs += list[i].url.replace(baseUrl, "") + separator;
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
</script>

<style scoped lang="scss">
// .el-upload--picture-card 控制加号部分
:deep(.hide .el-upload--picture-card) {
  display: none;
}

:deep(.el-upload.el-upload--picture-card.is-disabled) {
  display: none !important;
} 
</style>
