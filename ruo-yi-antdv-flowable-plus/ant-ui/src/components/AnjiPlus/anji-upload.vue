<template>
  <div class="uploadImage">
    <el-upload
      :headers="headers"
      :limit="limit"
      :action="requestUrl"
      list-type="picture-card"
      :file-list="fileList"
      :on-preview="handlePictureCardPreview"
      :on-remove="handleRemove"
      :on-success="handleSuccess"
      :show-file-list="true"
      :before-upload="handleBeforeUpload"
      :class="fileList && fileList.length >= limit ? 'hide_box' : ''"
    >
      <i class="el-icon-plus" />
    </el-upload>
    <el-dialog :visible.sync="dialogVisibleImageUpload" :modal="false">
      <img width="100%" :src="imageUploadUrl" alt="" />
    </el-dialog>
  </div>
</template>
<script>
import { getToken } from '@/utils/auth' // get token from cookie
export default {
  props: {
    upLoadUrl: {
      type: String,
      default: () => {
        return ''
      }
    },
    viewUrl: {
      type: String,
      default: () => {
        return process.env.BASE_API + '/file/download/'
      }
    },
    limit: {
      type: Number,
      default: () => {
        return 1
      }
    },
    value: {
      type: String,
      default: () => {
        return ''
      }
    }
  },
  data() {
    return {
      imageUploadUrl: '',
      dialogVisibleImageUpload: false,
      fileList: []
    }
  },
  computed: {
    requestUrl() {
      if (this.upLoadUrl != null && this.upLoadUrl.trim() != '') {
        return process.env.BASE_API + this.upLoadUrl
      } else {
        return process.env.BASE_API + '/file/upload'
      }
    },
    headers() {
      return {
        Authorization: getToken() // 直接从本地获取token就行
      }
    }
  },
  watch: {
    value: {
      handler(val) {
        this.echoUpload(this.value)
      },
      immediate: true
    }
  },
  mounted() {
    this.echoUpload(this.value)
  },
  methods: {
    handleRemove(file) {
      this.fileList = []
      this.change()
    },
    handlePictureCardPreview(file) {
      this.imageUploadUrl = file.url
      this.dialogVisibleImageUpload = true
    },
    // 上传成功的回调
    handleSuccess(response, file, fileList) {
      if (response.code != 200) {
        this.$message.error('上传失败')
        return
      }
      this.fileList.push({
        url: file.response.data.urlPath
      })
      this.change()
    },
    // 回传出去
    change() {
      const fileList = (this.fileList.length > 0 && this.fileList[0].url) || ''
      this.$emit('input', fileList)
      this.$emit('change', fileList)
    },
    // 上传检验
    handleBeforeUpload(file) {
      const extension = file.name
        .split('.')
        [file.name.split('.').length - 1].toLowerCase()
      // .png|.jpg|.gif|.icon|.pdf|.xlsx|.xls|.csv|.mp4|.avi
      const extensionList = ['png', 'jpg', 'gif', 'icon']
      if (extensionList.indexOf(extension) < 0) {
        this.$message.warning('请上传正确的格式文件')
        return false
      }
      return true
    },
    // 回显
    echoUpload(val) {
      if (!val) {
        this.fileList = []
      } else {
        const list = [{ url: val }]
        this.fileList = list
      }
    }
  }
}
</script>
<style lang="scss" scoped>
.uploadImage .el-upload--picture-card {
  width: 60px;
  height: 60px;
  line-height: 65px;
}
.uploadImage .el-upload-list__item {
  width: 60px;
  height: 60px;
}
.hide_box /deep/ .el-upload--picture-card {
  display: none;
}
.el-upload-list__item {
  line-height: 1.5;
}
.el-upload-list--picture-card .el-upload-list__item-actions {
  text-align: left;
  display: flex;
  flex-wrap: wrap;
}
.imgBox,
.iconFont {
  width: 100% !important;
  height: 100% !important;
}
</style>
