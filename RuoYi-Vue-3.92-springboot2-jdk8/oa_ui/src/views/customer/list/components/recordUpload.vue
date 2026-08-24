<!-- 客户跟进记录填写弹窗组件 -->
<template>
  <div class="followUpRecord">
    <div class="addContent">
      <div class="left">
        <img :src="avatar" alt="" class="head-portrait" />
      </div>
      <div class="right">
        <div class="box" @click="focusFn">
          <el-input
            ref="inputFocus"
            v-model="form.content"
            autosize
            placeholder="填写跟进记录吧"
            resize="none"
            type="textarea"
          >
          </el-input>
          <div class="uploadBox">
            <el-tag v-for="(item, i) in uploadList" :key="i" class="mt10" type="info">
              <div class="info">
                <i class="el-icon-error" @click="deleteTag(item)"></i>
                <img v-if="toSrc(item.real_name) === 1" alt="" class="img" src="@/assets/images/doc.png" />
                <img v-else-if="toSrc(item.real_name) === 2" alt="" class="img" src="@/assets/images/ppt.png" />
                <img v-else-if="toSrc(item.real_name) === 3" alt="" class="img" src="@/assets/images/xls.png" />
                <img v-else-if="toSrc(item.real_name) === 4" alt="" class="img" src="@/assets/images/record2.png" />
                <img v-else-if="toSrc(item.real_name) === 5" alt="" class="img" src="@/assets/images/pdf.png" />
                <span class="text-info line1">{{ item.real_name }}</span>
              </div>
            </el-tag>
          </div>
        </div>
        <el-row class="footer">
          <el-col :span="12" class="flex">
            <el-upload
              :headers="myHeaders"
              :http-request="uploadServerLog"
              :show-file-list="false"
              action="##"
              class="mr10 upload-real"
            >
              <div v-if="!percentShow" class="addText"><span class="iconfont iconfujian"></span> 添加文件</div>
              <div v-else class="addText">
                <img alt="" class="l_gif" src="@/assets/images/loading.gif" />
              </div>
            </el-upload>
          </el-col>
          <el-col :span="12" class="text-right">
            <el-button v-if="formInfo.type === 'edit'" size="small" @click="clientCancel">取消</el-button>
            <el-button :loading="loading" size="small" type="primary" @click="clientFollowSave">确定</el-button>
          </el-col>
        </el-row>
        <!-- <div v-if="formInfo.show !== 1" class="mt30"></div> -->
      </div>
    </div>
  </div>
</template>

<script>
import { clientFollowEditApi, clientFollowSaveApi } from '@/api/enterprise'
import ElImageViewer from 'element-ui/packages/image/src/image-viewer'
import { uploader } from '@/utils/uploadCloud'
import file from '@/utils/file'
import { toSrcFn } from '@/utils/format'
import Vue from 'vue'
Vue.use(file)
export default {
  name: 'recordUpload',
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
      loading: false,
      percentShow: false,
      id: '',
      form: {
        content: '',
        files: [],
        types: 0,
        time: ''
      },
      follow_id: 0,
      rules: {
        content: [{ required: true, message: '请输入跟进信息', trigger: 'blur' }]
      },
      avatar: '',
      uploadData: {},
      uploadSize: 15,
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
        this.follow_id = nVal.follow_id ? nVal.follow_id : 0
        if (nVal.type === 'edit') {
          this.form.content = nVal.editData.content
          this.uploadList = nVal.editData.attachs
        }
      },
      immediate: true,
      deep: true
    }
  },
  mounted() {
    let userInfo = JSON.parse(localStorage.getItem('userInfo'))

    this.avatar = userInfo.avatar
  },
  methods: {
    focusFn() {
      this.$refs.inputFocus.focus()
    },
    // 判断上传的文件格式文件是否有无图片，无图则为默认
    toSrc(e) {
      return toSrcFn(e)
    },

    // 上传文件方法
    uploadServerLog(params) {
      this.percentShow = true
      const file = params.file
      let options = {
        way: 2,
        relation_type: 'follow',
        relation_id: this.formInfo.type !== 'add' ? this.formInfo.editData.id : 0,
        eid: this.formInfo.data.eid
      }
      uploader(file, 0, options)
        .then((res) => {
          // 获取上传文件渲染页面
          if (res.data) {
            this.uploadList.push({
              id: res.data.attach_id,
              real_name: res.data.name
            })
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
        return item.id !== row.id
      })
    },

    successChange() {
      this.$emit('change', this.successData)
    },

    clientCancel() {
      this.successData.type = 'edit'
      this.successChange()
    },

    // 跟进记录--跟进详情
    clientFollowSave() {
      var attach_ids = []
      if (this.uploadList.length > 0) {
        this.uploadList.map((value) => {
          attach_ids.push(value.id)
        })
      } else {
        attach_ids = []
      }
      const data = {
        content: this.form.content,
        types: 0,
        attach_ids,
        eid: this.formInfo.data.eid,
        time: this.form.time,
        follow_id: this.follow_id
      }
      this.loading = true
      if (this.formInfo.type === 'add') {
        // 防抖input
        this.clientFollowAdd(data)
      } else {
        this.clientFollowEdit(this.formInfo.editData.id, data)
      }
    },

    // 跟进记录--添加
    async clientFollowAdd(data) {
      const res = await clientFollowSaveApi(data)
      this.loading = false
      if (res.status === 200) {
        this.addRecordShow = false
        this.loading = false
        this.successData.type = 'add'
        this.successChange()
        this.form.content = ''
        this.form.time = ''
        this.uploadList = []
      }
    },

    // 跟进记录--修改跟进详情
    async clientFollowEdit(id, data) {
      const res = await clientFollowEditApi(id, data)
      this.loading = false
      if (res.status === 200) {
        this.clientCancel()
        this.form.content = ''
        this.form.time = ''
        this.uploadList = []
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.followUpRecord {
  font-family: PingFangSC-Regular, PingFang SC;
}
.mt30 {
  margin-top: 30px;
}
.percent {
  margin-top: 20px;
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
  border: none;
  font-size: 12px;
}
</style>
