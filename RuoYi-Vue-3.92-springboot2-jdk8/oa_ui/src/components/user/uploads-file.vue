<!-- @FileDescription: 办公-日报-填写汇报上传文件组件-->
<template>
  <div class="combined-upload-wrapper">
    <div class="upload-list" :style="{ '--width': fileWidth }">
      <div class="width100">
        <div class="upload-con">
          <div v-if="showClose && fileList.length < maxLength" class="upload-box">
            <el-upload action="#" :show-file-list="false" :http-request="uploadServerLog" :multiple="multiple">
              <slot>
                <div class="btn">＋上传附件（最多{{ maxLength }}个）</div>
              </slot>
            </el-upload>
          </div>
          <div class="addText" v-if="percentShow">
            <img src="../../assets/images/loading.gif" alt="" class="l_gif" />
          </div>

          <div class="fileItem" v-for="(fileItem, index) in fileList" :key="index" @click="lookViewer(fileItem)">
            <div class="file-item-left">
              <i
                class="iconfont"
                v-if="toSrcIcon(fileItem.name) !== 'img'"
                :class="toSrcIcon(fileItem.name ? fileItem.name : fileItem.real_name)"
              ></i>
              <img v-else :src="fileItem.file_url || fileItem.url || fileItem.src" alt="" class="img" />
              <div class="file-item-right">
                <div class="file-name over-text">
                  {{ fileItem.name || fileItem.real_name }}
                </div>
                <div class="file-size">{{ toSizeFile(fileItem.size ? fileItem.size : fileItem.att_size) }}</div>
              </div>
            </div>
            <i v-if="showClose" class="file-close el-icon-error" @click.stop="fileDelete(index)"></i>
          </div>
        </div>
        <image-viewer ref="imageViewer" :src-list="srcList"></image-viewer>
      </div>
    </div>
  </div>
</template>

<script>
import { uploader } from '@/utils/uploadCloud'
import { fileLinkDownLoad, formatBytes, getFileType, isTypeImage } from '@/libs/public'
import imageViewer from '@/components/common/imageViewer'
import helper from '@/libs/helper'

export default {
  name: 'UploadFileList',
  components: {
    imageViewer
  },
  props: {
    multiple: { type: Boolean, default: false },
    fileSize: { type: Number, default: helper.fileSize },
    onlyImage: { type: Boolean, default: false },
    method: { type: String, default: 'post' },
    url: { type: String, default: '', required: true },
    params: { type: Object, default: () => {} },
    showProgress: { type: Boolean, default: true },
    fileList: { type: Array, default: () => [] },
    showClose: { type: Boolean, default: false },
    maxLength: { type: Number, default: 9 }, // 最多上传文件数量
    fileWidth: { type: String, default: '426px' }
  },
  data() {
    return {
      percentShow: false,
      percent: 0,
      fileName: '',
      srcList: []
    }
  },
  watch: {
    fileList: {
      handler(nVal) {
        this.srcList = []
        if (nVal.length > 0) {
          nVal.forEach((value) => {
            if (isTypeImage(value.name || value.real_name)) {
              this.srcList.push(value.file_url || value.att_dir)
            }
          })
        }
      },
      immediate: true
    }
  },
  methods: {
    uploadServerLog(params) {
      this.percentShow = true
      const file = params.file
      let options = {
        way: this.params.way ? this.params.way : 2,
        relation_type: this.params.relation_type,
        relation_id: 0,
        eid: this.params.eid
      }
      let onlyImg = this.onlyImage ? 1 : 0
      uploader(file, onlyImg, options)
        .then((res) => {
          if (res.name) {
            this.$emit('on-success', res, this.onlyImage)
            this.percentShow = false
          }
        })
        .catch((err) => {
          this.percentShow = false
        })
    },
    toSrcIcon(name) {
      return getFileType(name)
    },
    toSizeFile(size) {
      return formatBytes(size)
    },
    fileDelete(index) {
      this.fileList.splice(index, 1)
    },
    lookViewer(item) {
      let url = item.file_url || item.url || item.src || item.att_dir
      const name = item.name ? item.name : item.real_name
      if (isTypeImage(url)) {
        this.$refs.imageViewer.openImageViewer(url)
      } else {
        fileLinkDownLoad(url, name)
      }
    }
  }
}
</script>

<style scoped lang="scss">
.upload-box {
  width: var(--width);
  height: 48px;
  display: inline-block;
}
.upload-con {
  display: flex;
  flex-direction: column;
  flex-wrap: wrap;

  .fileItem:nth-child(even) {
    margin-right: 20px;
    margin-top: 8px;
  }
}
.percent {
  width: 50%;
  margin-top: 20px;
}
.img {
  display: inline-block;
  width: 36px;
  height: 36px;
  margin-top: 10px;
  margin-right: 10px;
  margin-left: 10px;
}
.btn {
  width: var(--width);
  height: 48px;
  line-height: 48px;
  background: #f3f8fe;
  color: #1890ff;
  display: inline-block;
}
.addText {
  display: inline-block;
}
.fileItem {
  margin: 0;
  padding: 0;
  padding: 10px 0;
  cursor: pointer;
  width: var(--width);
  background-color: #f7f7f7;
  display: flex;
  height: 48px;
  // line-height: 1;
  align-items: center;
  position: relative;
  .file-close {
    font-size: 18px;
    position: absolute;
    top: 14px;
    right: 10px;
    color: #c0c4cc;
    cursor: pointer;
  }
  .file-item-left {
    cursor: pointer;
    display: flex;

    .iconfont {
      font-size: 36px;
      line-height: 48px;
      padding: 6px;
    }
    .file-name {
      width: calc(var(--width) - 48px);
      font-size: 13px;
      line-height: 1.5;
    }
    .file-size {
      font-size: 12px;
      line-height: 1.5;
    }
  }
  .file-item-right {
    padding: 10px 0;
    display: inline-block;
  }
}
</style>
