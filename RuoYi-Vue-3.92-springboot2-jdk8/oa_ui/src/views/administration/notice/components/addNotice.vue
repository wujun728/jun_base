<!-- 行政-新建公告页面 -->
<template>
  <div>
    <el-card class="card-box notice" :body-style="{ padding: '0' }">
      <div class="header">
        <i class="el-icon-arrow-left onHand" @click="handleEmit"></i>
        <span>{{ formData.isEdit ? '修改通知公告' : '添加通知公告' }}</span>
      </div>
      <div ref="notice" class="pl20 mt20">
        <el-form ref="form" :model="form" :rules="rules" label-width="100px">
          <el-form-item label="公告标题" class="input" prop="title">
            <el-input
              v-model="form.title"
              size="small"
              maxlength="50"
              clearable
              show-word-limit
              placeholder="请输入公告标题"
            ></el-input>
          </el-form-item>
          <el-form-item label="公告分类" prop="categoryId">
            <el-select v-model="form.categoryId" size="small" clearable placeholder="请选择公告分类">
              <el-option
                v-for="item in formData.optionData"
                :key="item.id"
                :label="item.cate_name"
                :value="item.id"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="公告简介" class="input">
            <el-input
              v-model="form.info"
              type="textarea"
              :rows="4"
              maxlength="100"
              resize="none"
              show-word-limit
              clearable
              placeholder="请输入公告简介"
            ></el-input>
          </el-form-item>
          <el-form-item label="公告封面">
            <div v-if="form.imageUrl" class="avatar">
              <img class="img" :src="form.imageUrl" />
              <div class="avatar-upload">
                <div class="avatar-upload-content">
                  <i @click="handlePictureCardPreview" class="el-icon-zoom-in"></i>
                  <i @click="handleAvatar" class="el-icon-delete"></i>
                </div>
              </div>
            </div>
            <i @click="beforeUpload" v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-form-item>
          <el-form-item> 建议尺寸: 162X108</el-form-item>
          <el-form-item label="公告正文" prop="contents">
            <div style="width: 80%">
              <ueditorFrom :border="true" type="notepad" :height="`500px`" :content="form.content" ref="ueditorFrom" />
            </div>
          </el-form-item>
          <el-form-item label="是否置顶">
            <el-switch v-model="form.delivery"></el-switch>
          </el-form-item>
          <el-row class="resource-content">
            <el-col class="resource-left">
              <el-form-item label="发布时间">
                <el-radio-group v-model="form.resource" size="small">
                  <el-radio :label="0" class="resource">立即发布</el-radio>
                  <el-radio :label="1">定时发布</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col class="resource-right">
              <!-- <el-form-item> -->
              <el-date-picker
                class="date"
                v-model="form.date"
                size="small"
                prefix-icon="el-icon-date"
                type="datetime"
                :picker-options="expireTimeOption"
                clearable
                placeholder="选择日期时间"
              ></el-date-picker>
              <!-- </el-form-item> -->
            </el-col>
          </el-row>
        </el-form>
      </div>
    </el-card>
    <div class="cr-bottom-button">
      <el-button type="primary" size="small" :loading="loading" @click="handleConfirm()">
        {{ $t('public.save') }}
      </el-button>
    </div>
    <el-dialog title="选择图片" :visible.sync="dialogVisible" width="850px" :before-close="handleClose">
      <upload-picture ref="uploadPicture" :check-button="true" @getImage="getImage"></upload-picture>
    </el-dialog>
    <el-image-viewer v-if="isImage" :on-close="closeImageViewer" :url-list="srcList" />
  </div>
</template>

<script>
import { noticeEditApi, noticeEditCreateApi, noticeSaveApi } from '@/api/administration'
export default {
  name: 'AddNotice',
  props: {
    formData: {
      type: Object,
      default() {
        return {}
      }
    },
    cateId: {
      type: Number,
      default: 0
    }
  },
  components: {
    ueditorFrom: () => import('@/components/form-common/oa-wangeditor'),
    uploadPicture: () => import('@/components/uploadPicture/index'),
    ElImageViewer: () => import('element-ui/packages/image/src/image-viewer')
  },
  data() {
    return {
      loading: false,
      dialogVisible: false,
      form: {
        title: '',
        categoryId: '',
        info: '',
        imageUrl: '',
        content: '',
        delivery: false,
        resource: 0,
        date: '',
        contents: ''
      },
      expireTimeOption: {
        disabledDate(date) {
          // disabledDate 文档上：设置禁用状态，参数为当前日期，要求返回 Boolean
          return date.getTime() < Date.now() - 24 * 60 * 60 * 1000
        }
      },
      rules: {
        title: [{ required: true, message: '请输入公告标题', trigger: 'blur' }],
        categoryId: [{ required: true, message: '请选择公告分类', trigger: 'change' }],
        contents: [{ required: true, message: '请输入公告正文', trigger: 'change' }]
      },
      isImage: false,
      srcList: []
    }
  },
  mounted() {
    this.$erd.listenTo(this.$refs.notice, (element) => {
      let h = document.documentElement.clientHeight
      h = h - 28 - 54 - 58
      element.style.height = h + 'px'
    })
  },
  watch: {},
  methods: {
    beforeUpload() {
      this.dialogVisible = true
    },
    handleClose() {
      this.dialogVisible = false
      this.$refs.uploadPicture.getFileList('')
      this.$refs.uploadPicture.selectItem = []
      this.$refs.uploadPicture.checkPicList = []
    },
    getImage(data) {
      this.form.imageUrl = data.att_dir
      this.handleClose()
    },
    handleAvatar() {
      this.form.imageUrl = ''
    },
    reset() {
      this.form = {
        title: '',
        categoryId: '',
        info: '',
        imageUrl: '',
        content: '',
        delivery: false,
        resource: 0,
        date: '',
        contents: ''
      }
      this.$refs.ueditorFrom.clear()
      this.$refs.form.resetFields()
    },
    // 监听编辑器
    ueditorEdit(e) {
      this.form.contents = e
      if (e) {
        this.$refs.form.validateField('contents')
      }
    },
    handleEmit() {
      this.$emit('isNotice', this.form.categoryId)
      this.reset()
      this.form.contents = ''
    
    },
    handleCancel() {
      this.handleEmit()
      this.$refs.ueditorFrom.clear()
    },
    handleConfirm() {
      if (this.form.resource == 1 && !this.form.date) {
        this.$message.error('请选择发布时间')

        return false
      }
      this.form.contents = this.$refs.ueditorFrom.getValue()
      if (this.form.contents == '<p><br></p>') {
        this.$message.error('请输入公告正文')
        return false
      }
      this.$refs.form.validate((valid) => {
        if (valid) {
          var data = {
            cate_id: this.form.categoryId,
            title: this.form.title,
            info: this.form.info,
            cover: this.form.imageUrl,
            content: this.form.contents,
            is_top: this.form.delivery ? 1 : 0,
            push_type: this.form.resource,
            push_time: this.$moment(this.form.date).format('YYYY-MM-DD HH:mm:ss')
          }
          if (this.formData.isEdit) {
            this.getNoticeEdit(this.formData.id, data)
          } else {
            this.getNoticeSave(data)
          }
        }
      })
    },
    // 保存
    getNoticeSave(data) {
      this.loading = true
      noticeSaveApi(data)
        .then((res) => {
          this.loading = false

          this.handleEmit()
        })
        .catch((error) => {
          this.loading = false
        })
    },
    // 编辑
    getNoticeEdit(id, data) {
      this.loading = true
      noticeEditApi(id, data)
        .then((res) => {
          this.loading = false

          this.handleEmit()
        })
        .catch((error) => {
          this.loading = false
        })
    },
    getNoticeCreate(id) {
      noticeEditCreateApi(id).then((res) => {
        const data = res.data
        this.form.categoryId = data.cate_id
        this.form.title = data.title
        this.form.info = data.info
        this.form.imageUrl = data.cover
        this.$refs.ueditorFrom.tabButton = true
        this.form.content = data.content
        this.form.contents = data.content
        this.form.delivery = data.is_top === 1
        this.form.resource = data.push_type
        if (data.push_type === 0) {
          this.form.date = ''
        } else {
          this.form.date = data.push_time
        }
      })
    },
    handlePictureCardPreview() {
      this.srcList.push(this.form.imageUrl)
      this.isImage = true
    },
    closeImageViewer() {
      this.isImage = false
      this.srcList = []
    }
  }
}
</script>

<style lang="scss" scoped>
::v-deep .el-icon-back {
  display: none;
}
::v-deep .w-e-text-container p {
  margin: 24px 0;
}
::v-deep .el-page-header__content {
  font-size: 15px;
  font-weight: 600;
}
::v-deep .el-scrollbar__wrap {
  overflow-x: hidden;
}
.card-box {
  height: calc(100vh - 120px) !important;
  overflow-y: scroll;
  font-size: 13px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #303133;
  // margin-bottom: 30px;
  padding-bottom: 30px;
}
.header {
  padding-left: 20px;
  height: 70px;
  line-height: 70px;
  font-family: PingFang SC, PingFang SC;
  font-weight: 500;
  font-size: 17px;
  color: #303133;
  border-bottom: 1px solid #dcdfe6;
}

.notice {
  .input {
    ::v-deep .el-input,
    ::v-deep .el-textarea {
      width: 50%;
    }
  }
  ::v-deep .el-select {
    width: 50%;
    ::v-deep .el-input--medium {
      width: 100%;
      .el-input__inner {
        width: 100%;
      }
    }
  }
  .resource-content {
    display: flex;
    align-items: flex-end;
    ::v-deep .el-form-item {
      margin-bottom: 0px;
    }
    .resource-left {
      width: 130px;
      ::v-deep .resource {
        display: block;
        margin-bottom: 5px;
      }
    }
    .resource-right {
      width: calc(100% - 130px);
      .date {
        margin-left: 90px;
        margin-bottom: 6px;
      }
    }
  }
  .avatar {
    width: 81px;
    height: 54px;
    position: relative;
    img {
      width: 100%;
      height: 100%;
    }
    &:hover {
      .avatar-upload {
        display: block;
      }
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
        width: 100%;
        height: 100%;
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
  .avatar-uploader-icon {
    border: 1px dashed #d9d9d9;
    font-size: 28px;
    color: #8c939d;
    width: 81px;
    height: 54px;
    line-height: 54px;
    text-align: center;
    cursor: pointer;
  }
}
.cr-bottom-button {
  position: fixed;
  left: -20px;
  right: 0;
  bottom: 0;
  width: calc(100% + 220px);
}
</style>
