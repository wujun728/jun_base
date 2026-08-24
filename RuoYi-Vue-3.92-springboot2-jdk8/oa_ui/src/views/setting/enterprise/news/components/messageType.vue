<template>
  <div>
    <el-dialog title="新增消息类型" :visible.sync="show" width="600px" :before-close="handleClose">
      <div>
        <el-form :model="formData" label-width="80px">
          <el-form-item label="类型名称：">
            <el-input v-model="formData.name" size="small" placeholder="请输入消息类型名称"></el-input>
          </el-form-item>
          <el-form-item label="类型图标：">
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
          </el-form-item>
          <el-form-item label="排序：">
            <el-input-number v-model="formData.sort" :min="0" :max="999999"></el-input-number>
          </el-form-item>
        </el-form>
      </div>

      <span slot="footer" class="dialog-footer">
        <el-button @click="handleClose" size="small">取 消</el-button>
        <el-button type="primary" size="small">确 定</el-button>
      </span>
    </el-dialog>

    <el-dialog
      :before-close="handleImgClose"
      :visible.sync="dialogVisible"
      title="选择图片"
      v-bind="$attrs"
      width="850px"
    >
      <upload-picture ref="uploadPicture" :check-button="true" @getImage="getImage"></upload-picture>
    </el-dialog>
  </div>
</template>
<script>
import uploadPicture from '@/components/uploadPicture/index'
export default {
  name: '',
  components: {
    uploadPicture
  },
  data() {
    return {
      show: false,
      dialogVisible: false,
      imageUrl: '',
      formData: {
        name: '',
        icon: '',
        sort: ''
      }
    }
  },

  methods: {
    openBox(val) {
      this.show = true
    },
    //  开启弹窗
    beforeUpload() {
      this.dialogVisible = true
    },
    handleClose() {
      this.show = false
    },
    // 删除图片
    handleAvatar() {
      this.imageUrl = ''
      this.formData.icon = ''
    },
    // 获取弹窗
    getImage(data) {
      this.imageUrl = data.att_dir
      this.handleImgClose()
    },
    handleImgClose() {
      this.dialogVisible = false
    }
  }
}
</script>
<style scoped lang="scss">
.avatar-uploader-icon {
  border: 1px dashed #d9d9d9;
  font-size: 14px;
  color: #8c939d;
  width: 103px;
  height: 103px;
  line-height: 103px;
  text-align: center;
  cursor: pointer;
}
.avatar {
  width: 103px;
  height: 103px;
  position: relative;
  img {
    width: 103px;
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
      width: 103px;
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
