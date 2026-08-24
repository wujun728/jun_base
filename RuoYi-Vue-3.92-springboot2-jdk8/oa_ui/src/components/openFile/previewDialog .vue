<!-- @FileDescription: 文件预览弹窗-mp3/mp4 -->
<template>
  <div>
    <!-- mp3 -->
    <el-dialog
      class="mp3-dialog"
      :visible.sync="is_mp3Show"
      width="592px"
      :append-to-body="true"
      :close-on-click-modal="false"
    >
      <div slot="title">{{ file.name }}.{{ file.file_ext }}</div>
      <div class="mp3-box">
        <mp3 :url="url"></mp3>
      </div>
    </el-dialog>
    <!-- mp4 -->
    <el-dialog
      class="mp4-dialog"
      :visible.sync="is_mp4Show"
      top="10vh"
      width="1200px"
      :append-to-body="true"
      :close-on-click-modal="false"
      :show-close="false"
    >
      <div slot="title" class="flex-between">
        <span>{{ file.name }}.{{ file.file_ext }}</span>
        <div @click.stop="closeFn" class="el-icon-close"></div>
      </div>
      <div class="mp4-box">
        <mp4 :url="url"></mp4>
      </div>
    </el-dialog>
    <!-- 图片 -->
    <previewImage ref="imageViewer" :src-list="srcList"></previewImage>
  </div>
</template>
<script>
import { getFileInfoApi, getAttachInfoApi } from '@/api/cloud'
export default {
  name: '',
  components: {
    mp3: () => import('./previewMp3'),
    mp4: () => import('./previewMp4'),
    previewImage: () => import('./previewImage')
  },
  props: {},
  data() {
    return {
      is_mp3Show: false,
      is_mp4Show: false,
      url: '',
      file: {},
      srcList: []
    }
  },
  computed: {},

  methods: {
    closeFn() {
      this.is_mp4Show = false
    },
    openFile(fid, id) {
      if (fid) {
        getFileInfoApi(fid, id).then((res) => {
          if (res.data.file_ext == 'mp3') {
            this.is_mp3Show = true
          } else if (res.data.file_ext == 'mp4') {
            this.is_mp4Show = true
          } else {
            this.srcList.unshift(res.data.file_url)
            this.$refs.imageViewer.openImageViewer(res.data.file_url)
          }
          this.file = res.data
          this.url = res.data.file_url
        })
      } else {
        getAttachInfoApi(id).then((res) => {
          if (res.data.file_ext == 'mp3') {
            this.is_mp3Show = true
          } else if (res.data.file_ext == 'mp4') {
            this.is_mp4Show = true
          } else {
            this.srcList.unshift(res.data.file_url)
            this.$refs.imageViewer.openImageViewer(res.data.file_url)
          }
          this.file = res.data
          this.url = res.data.file_url
        })
      }
    }
  }
}
</script>
<style scoped lang="scss">
.mp3-dialog {
  ::v-deep .el-dialog {
    .mp3-box {
      width: 544px;
      background: #fff;
      border-radius: 6px;
      margin-bottom: 20px;
    }
  }
}
.mp4-dialog {
  ::v-deep .el-dialog__body {
    padding: 0;
  }
  .el-icon-close {
    z-index: 99;
    cursor: pointer;
  }

  ::v-deep.el-dialog__header {
    width: 100%;
    position: absolute;
    top: 0;
    // display: none;
    background: linear-gradient(180deg, #000000 0%, rgba(0, 0, 0, 0) 100%);
    font-family: PingFang SC, PingFang SC;
    font-weight: 500;
    font-size: 16px;
    color: #fff;
    // padding: 20px 20px 0 20px;
    border: none;
  }
  ::v-deep.el-dialog__headerbtn .el-dialog__close {
    color: #fff;
  }
}
.mp4-box {
  z-index: 120;
}
</style>
