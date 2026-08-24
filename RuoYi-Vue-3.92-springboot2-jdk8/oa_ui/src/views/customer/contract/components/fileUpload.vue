<!-- 合同-添加合同记录附件弹窗组件 -->
<template>
  <div class="followUpRecord">
    <div class="addContent">
      <div class="left">
        <img :src="formInfo.data.card ? formInfo.data.card.avatar : avatar" alt="" class="head-portrait" />
      </div>
      <div class="right">
        <div class="box" @click="focusFn">
          <el-input
            placeholder="填写备注信息吧"
            ref="inoutFocus"
            autosize
            type="textarea"
            resize="none"
            v-model="form.content"
          >
          </el-input>
          <div class="uploadBox">
            <el-tag type="info" v-for="(item, i) in uploadList" :key="i">
              <div class="info">
                <i class="el-icon-error" @click="deleteTag(item)"></i>
                <img v-if="toSrc(item.name) === 1" class="img" src="../../../../assets/images/doc.png" alt="" />
                <img v-if="toSrc(item.name) === 2" class="img" src="../../../../assets/images/ppt.png" alt="" />
                <img v-if="toSrc(item.name) === 3" class="img" src="../../../../assets/images/xls.png" alt="" />
                <img v-if="toSrc(item.name) === 4" class="img" src="../../../../assets/images/record2.png" alt="" />
                <img v-if="toSrc(item.name) === 5" class="img" src="../../../../assets/images/pdf.png" alt="" />
                <span class="text-info line1">{{ item.name }}</span>
              </div>
            </el-tag>
          </div>
        </div>
        <el-row class="footer">
          <el-col :span="12" class="flex">
            <el-upload
              class="mr10 upload-real"
              action="##"
              :show-file-list="false"
              :headers="myHeaders"
              :http-request="uploadServerLog"
            >
              <div class="addText" v-if="!percentShow"><span class="iconfont iconfujian"></span> 添加文件</div>
              <div class="addText" v-else>
                <img src="../../../../assets/images/loading.gif" alt="" class="l_gif" />
              </div>
            </el-upload>
          </el-col>
          <el-col :span="12" class="text-right">
            <el-button size="small" v-if="formInfo.type === 'edit'" @click="clientCancel">取消</el-button>
            <el-button type="primary" size="small" @click="clientFollowSave">确定</el-button>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>

<script>
import { contracFileEditApi, contracFileSaveApi } from '@/api/enterprise'
import ElImageViewer from 'element-ui/packages/image/src/image-viewer'
import { uploader } from '@/utils/uploadCloud'
import file from '@/utils/file'
import { toSrcFn } from '@/utils/format'
import Vue from 'vue'

Vue.use(file)
export default {
  name: 'Record',
  components: {
    ElImageViewer
  },
  props: {
    formInfo: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  data() {
    return {
      addRecordShow: false,
      isTitle: '添加跟进记录',
      textarea: '',
      isEdit: null,
      fileName: '',
      iframeIsShow: false,
      percentShow: false,
      avatar: JSON.parse(localStorage.getItem('userInfo')).avatar,
      id: '',
      form: {
        content: '',
        attach_ids: [],
        cid: ''
      },

      rules: {
        content: [{ required: true, message: '请填写记录描述', trigger: 'blur' }]
      },
      uploadData: {},

      uploadList: [],
      myHeaders: {
        authorization: 'Bearer ' + localStorage.getItem('token')
      },
      successData: {
        type: 'add'
      }
    }
  },

  watch: {
    formInfo: {
      handler(nVal) {
        if (nVal.type === 'edit') {
          this.form.content = nVal.editData.content
          this.uploadList = nVal.editData.attachs
        }
      },
      immediate: true,
      deep: true
    }
  },
  methods: {
    focusFn() {
      this.$refs.inoutFocus.focus()
    },
    // 上传文件方法
    uploadServerLog(params) {
      this.percentShow = true
      const file = params.file
      let options = {
        way: 2,
        relation_type: 'contract',
        relation_id: this.formInfo.type == 'edit' ? this.formInfo.editData.id : 0,
        eid: 0
      }
      uploader(file, 0, options)
        .then((res) => {
          // 获取上传文件渲染页面
          if (res.data) {
            this.uploadList.push(res.data)
            this.percentShow = false
          }
        })
        .catch((err) => {
          this.percentShow = false
        })
    },

    // 删除附件
    deleteTag(row) {
      this.uploadList = this.uploadList.filter((item) => {
        return item.id != row.id
      })
    },
    successChange() {
      this.$emit('change', this.successData)
    },
    clientCancel() {
      this.successData.type = 'edit'
      this.successChange()
    },
    // 判断上传的文件格式文件是否有无图片，无图则为默认
    toSrc(e) {
      return toSrcFn(e)
    },
    // 合同资料
    clientFollowSave() {
      this.form.attach_ids = []
      if (!this.form.content) {
        return this.$message.error('记录描述不能为空！')
      }
      if (this.uploadList.length > 0) {
        this.uploadList.map((value) => {
          this.form.attach_ids.push(value.id)
        })
      }

      this.form.cid = this.formInfo.data.cid
      if (this.formInfo.type == 'add') {
        this.contractFileSave(this.form)
      } else {
        this.contractFileEdit(this.formInfo.editData.id, this.form)
      }
    },
    // 合同资料--添加
    contractFileSave(data) {
      this.loading = true
      contracFileSaveApi(data)
        .then((res) => {
          this.loading = false
          this.$emit('change', 'add')
          this.successData.type = 'add'
          this.form.content = ''
          this.form.attach_ids = []
          this.uploadList = []
        })
        .catch((error) => {
          this.loading = false
        })
    },
    // 合同资料--修改
    contractFileEdit(id, data) {
      this.loading = true
      contracFileEditApi(id, data)
        .then((res) => {
          this.loading = false
          this.clientCancel()
          this.form.content = ''
          this.form.attach_ids = []
          this.uploadList = []
        })
        .catch((error) => {
          this.loading = false
        })
    }
  }
}
</script>

<style lang="scss" scoped>
.followUpRecord {
  font-family: PingFangSC-Regular, PingFang SC;
}
.percent {
  margin-top: 20px;
}

.mt30 {
  margin-top: 30px;
}
.addText {
  font-size: 12px;
  font-family: PingFang SC-Regular, PingFang SC;
  font-weight: 400;
  color: #303133;
  .iconfont {
    color: #303133;
    font-size: 12px;
  }
}
.addContent {
  margin-top: 20px;
  display: flex;

  .left {
    img {
      display: block;
      width: 35px;
      height: 35px;
      border-radius: 50%;
      margin-right: 10px;
    }
  }
  .right {
    width: 100%;

    .box {
      width: 100%;
      min-height: 134px;
      border: 1px solid #dcdfe6;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      .uploadBox {
        margin: 14px;
      }
    }
    .footer {
      margin-top: 30px;
      margin-bottom: 10px;
      height: 32px;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
  }
}
.topBtn {
  width: 102px;
  height: 32px;
  font-size: 13px;
}
.iconzhong {
  margin-left: 9px;
  color: #79a8d7 !important;
  font-size: 15px;
}
.reminderTime {
  margin-left: 45px;
  width: 246px;
  height: 28px;
  background: #f1f9ff;
  border-radius: 4px;
  line-height: 28px;
  font-size: 13px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #1890ff;
  margin-top: 10px;
}
.iframe {
  width: 100%;
  height: 252px;
  border: none;
}
.el-tag {
  padding: 0px;
  margin-right: 16px;
}
.el-button--medium {
  padding: 10px 15px !important;
}
.recordContent {
  padding-top: 38px;
}
.el-timeline {
  padding-left: 0px;
}
.head {
  display: flex;
  align-items: center;
  .more {
    position: absolute;
    right: 0;
  }

  .el-icon-more {
    color: #909399;
  }
  .head-portrait {
    width: 35px;
    height: 35px;
    border-radius: 50%;
  }
  .head-right {
    margin-left: 10px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    .head-name {
      font-size: 14px;

      font-weight: 400;
      color: #303133;
    }
    .head-time {
      font-size: 13px;
      line-height: 17px;
      font-weight: 400;
      color: #c0c4cc;
    }
  }
}
.record {
  padding-left: 45px;
  font-size: 14px;
  line-height: 21px;
  font-weight: 400;
  color: #303133;
}
.line {
  width: 100%;
  height: 4px;
  border-bottom: 1px solid #dcdfe6;
  margin-bottom: 20px;
}
.info {
  height: 28px;
  display: flex;
  align-items: center;
  padding: 0 8px;
  position: relative;
  margin-bottom: 10px;
  .el-icon-error {
    color: #ccc;
    font-size: 13px;
    position: absolute;
    top: -5px;
    right: -5px;
  }
  .text-info {
    display: inline-block;
    max-width: 180px;
  }
}
.img {
  width: 20px;
  height: 20px;
  margin-right: 4px;
  vertical-align: middle;
}
.flex {
  display: flex;
}
.mt10 {
  margin-top: 20px;
}
::v-deep .el-timeline-item__tail {
  position: absolute;
  left: 4px;
  height: 100%;

  border-left: 1px solid #dfe4ed;
}

::v-deep .el-textarea__inner {
  min-height: 96px;
  // word-break: break-all;
  border: none;
  font-size: 12px;
}
</style>
