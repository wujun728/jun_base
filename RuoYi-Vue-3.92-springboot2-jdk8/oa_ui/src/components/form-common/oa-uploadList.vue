<!-- @FileDescription: 上传文件后渲染组件  -->
<template>
  <div class="width100">
    <div class="width100">
      <div class="fileItem" v-for="(fileItem, index) in fileList" :key="index" @click="filePreview(fileItem)">
        <div class="file-item-left">
          <div class="file" v-if="toSrcIcon(fileItem.name) !== 'img'">{{ getFileTypeFn(fileItem.name) }}</div>

          <img v-else :src="fileItem.file_url || fileItem.url || fileItem.src" alt="" class="img" />
        </div>
        <div class="file-item-right">
          <div class="file-name over-text">{{ fileItem.name }}</div>
          <div class="file-size">{{ toSizeFile(fileItem.size) }}</div>
        </div>
        <i v-if="showClose" class="file-close el-icon-error" @click.stop="fileDelete(index)"></i>
      </div>
      <div v-if="fileList.length == 0">--</div>
    </div>

    <!-- 打开文件 -->
    <fileDialog ref="viewFile"></fileDialog>
  </div>
</template>

<script>
import { formatBytes, getFileType, isTypeImage, getFileExtension } from '@/libs/public'
export default {
  name: 'UploadList',
  props: {
    fileList: {
      type: Array,
      default: () => []
    },
    showClose: {
      type: Boolean,
      default: false
    }
  },
  components: {
    fileDialog: () => import('@/components/openFile/previewDialog ') // 图片、MP3，MP4弹窗
  },
  data() {
    return {
      srcList: []
    }
  },
  watch: {
    fileList: {
      handler(nVal) {
        this.srcList = []
        if (nVal.length > 0) {
          nVal.forEach((value) => {
            if (isTypeImage(value.name)) {
              this.srcList.push(value.file_url)
            }
          })
        }
      },
      immediate: true
    }
  },
  methods: {
    toSrcIcon(name) {
      return getFileType(name)
    },
    toSizeFile(size) {
      return formatBytes(size)
    },
    getFileTypeFn(name) {
      return getFileExtension(name)
    },
    fileDelete(index) {
      this.fileList.splice(index, 1)
    }
  }
}
</script>

<style scoped lang="scss">
.fileItem {
  cursor: pointer;
  width: 100%;
  background-color: #f6f7f9;
  display: flex;
  height: auto;
  line-height: 1;
  align-items: center;
  padding: 6px;
  position: relative;
  margin-bottom: 8px;
  &:last-of-type {
    margin-bottom: 0;
  }
  .file-close {
    font-size: 18px;
    position: absolute;
    // top: 0px;
    right: 10px;
    color: #c0c4cc;
    cursor: pointer;
  }
  .file-item-left {
    width: 36px;
    cursor: pointer;
    .iconfont {
      font-size: 36px;
    }
    .img {
      display: inline-block;
      width: 36px;
      height: 36px;
    }
  }
  .file-item-right {
    width: 210px;
    height: 36px;
    display: flex;
    justify-content: space-between;
    flex-direction: column;
    padding: 0 20px 0 6px;
    .file-name {
      font-size: 13px;
      line-height: 1.5;
    }
    .file-size {
      font-size: 12px;
      line-height: 1.5;
    }
  }
}
.file {
  display: flex;
  width: 33px;
  height: 41px;
  background: url('../../assets/images/cloud/file-box.png') no-repeat;
  background-size: 33px 41px;
  color: #fff !important;
  justify-content: center;
  line-height: 41px;
  font-size: 13px;
  margin-right: 10px;
}
</style>
