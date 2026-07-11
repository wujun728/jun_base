<template>
  <!-- 正文预览 -->
  <div>
    <div v-if="showDownLoad" style="padding: 5px; background: #f5f9ff; ">
      <el-button size="mini" plain icon="el-icon-download" @click="downloadFile()">下载正文</el-button>
    </div>
    <div class="preview-container">
      <!-- iframe预览区域 -->
      <iframe :src="fileInfo.mainTextUrl" :style="iframeStyle" frameborder="0"></iframe>
    </div>
  </div>
</template>

<script>
export default {
  name: "MainTextView",
  data() {
    return {
      screenHeight: 765,
    };
  },
  props: {
    showDownLoad: {
      type: Boolean,
      default: false,
    },
    fileInfo: {
      type: Object,
      default: null,
    },
  },
  computed: {
    iframeStyle() {
      return {
        width: "100%",
        height: `${this.screenHeight}px`,
      };
    },
  },
  mounted() {
    this.setHeight();
    window.addEventListener("resize", this.setHeight);
  },
  beforeUnmount() {
    // 移除监听
    window.removeEventListener("resize", this.setHeight);
  },
  methods: {
    setHeight() {
      this.screenHeight = window.innerHeight - 200;
    },
    // 下载正文
    downloadFile() {
      let param = { fileId: this.fileInfo.fileId };
      this.download(
        "file/operate/downloadfile",
        {
          ...param,
        },
        this.fileInfo.fileName
      );
    },
  },
};
</script>

<style scoped>
.preview-container {
  width: 100%;
  height: 100%;
}
</style>