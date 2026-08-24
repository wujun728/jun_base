<template>
  <div>
    <div v-if="user !== '个人简历'">
      <div v-if="imageUrl" class="avatar">
        <img :src="imageUrl" class="img" />
        <div class="avatar-upload">
          <div class="avatar-upload-content">
            <i class="el-icon-zoom-in" @click="handlePictureCardPreview"></i>
            <i class="el-icon-delete" @click="handleAvatar"></i>
          </div>
        </div>
      </div>
      <i v-else class="el-icon-plus avatar-uploader-icon" @click="beforeUpload"></i>
    </div>
    <div v-if="user == '个人简历'" class="avatar">
      <el-upload
        :headers="myHeaders"
        :http-request="uploadServerLog"
        :show-file-list="false"
        action="##"
        class="upload-demo mr10 mb15"
        multiple
      >
        <img v-if="imageUrl" :src="imageUrl" class="img" />
        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
      </el-upload>
    </div>

    <el-dialog :before-close="handleClose" :visible.sync="dialogVisible" title="选择图片" v-bind="$attrs" width="850px">
      <upload-picture ref="uploadPicture" :check-button="true" @getImage="getImage"></upload-picture>
    </el-dialog>
    <el-image-viewer v-if="isImage" :on-close="closeImageViewer" :url-list="srcList" />
  </div>
</template>

<script>
import uploadPicture from '@/components/uploadPicture/index'
import ElImageViewer from 'element-ui/packages/image/src/image-viewer'
import { getToken } from '@/utils/auth'
import { uploader } from '@/utils/uploadCloud'
export default {
  name: 'name',
  props: {
    imageUrl: {
      type: [String, Boolean],
      require: false
    },
    user: {
      type: [String],
      require: ''
    },

    isUploadStatus: {
      type: Boolean,
      default: () => false
    }
  },
  components: {
    uploadPicture,
    ElImageViewer
  },
  data() {
    return {
      src: null,
      dialogVisible: false,
      isImage: false,
      srcList: [],
      myHeaders: {
        authorization: 'Bearer ' + getToken()
      }
    }
  },

  methods: {
    //  开启弹窗
    beforeUpload() {
      this.dialogVisible = true
    },

    // 获取弹窗
    getImage(data) {
      this.$emit('update:imageUrl', data.att_dir)
      this.$emit('getImage', data.att_dir)
      this.handleClose()
    },

    // 关闭弹窗
    handleClose() {
      this.dialogVisible = false
      this.$refs.uploadPicture.getFileList('')
      this.$refs.uploadPicture.selectItem = []
      this.$refs.uploadPicture.checkPicList = []
    },
    // 查看图片
    handlePictureCardPreview() {
      this.srcList.push(this.imageUrl)
      this.isImage = true
    },
    // 删除图片
    handleAvatar() {
      this.$emit('update:imageUrl', '')
    },

    // 上传文件方法
    uploadServerLog(params) {
      this.percentShow = true
      const file = params.file
      let options = {
        way: 2,
        relation_type: '',
        relation_id: '',
        eid: ''
      }
      uploader(file, 0, options)
        .then((res) => {
          // 获取上传文件渲染页面
          if (res.data.name) {
            this.$emit('update:imageUrl', res.data.url)
            this.$emit('getImage', res.data.url)
          }
        })
        .catch((err) => {})
    },

    closeImageViewer() {
      this.isImage = false
      this.srcList = []
    }
  }
}
</script>
<style lang="scss" scoped>
.avatar-uploader-icon {
  border: 1px dashed #d9d9d9;
  font-size: 14px;
  color: #8c939d;
  width: 160px;
  height: 103px;
  line-height: 103px;
  text-align: center;
  cursor: pointer;
}
.avatar {
  width: 160px;
  height: 103px;
  position: relative;
  img {
    width: 160px;
    height: 103px;
  }
  &:hover {
    .avatar-upload {
      display: block;
    }
  }
  ::v-deep .el-upload {
    width: 95px;
    height: 65px;
  }
  .avatar-upload {
    position: absolute;
    left: 0;
    top: 0;
    bottom: 0;
    right: 0;
    display: none;
    background-color: rgba(0, 0, 0, 0.6);
    .avatar-upload-content {
      width: 160px;
      height: 103px;
      display: flex;
      align-items: center;
      justify-content: center;
      i {
        color: #fff;
        font-size: 18px;
        cursor: pointer;
        margin-right: 10px;
        &:last-of-type {
          margin-right: 0;
        }
      }
    }
  }
}
</style>
