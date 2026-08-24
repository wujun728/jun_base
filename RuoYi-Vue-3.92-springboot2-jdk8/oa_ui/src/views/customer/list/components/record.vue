<!-- 客户-客户跟进记录页面组件 -->
<template>
  <div class="followUpRecord">
    <div class="btn-box1 mb10">
      <div class="title-16">跟进记录</div>
      <el-button
        type="primary"
        size="small"
        v-if="formInfo.types == 2 || (formInfo.types == 1 && userId == formInfo.data.salesman.id)"
        @click="addRecord"
        >添加跟进记录</el-button
      >
    </div>
    <record-upload v-if="addRecordShow" :form-info="formInfo" @change="uploadChange"></record-upload>
    <div class="recordContent">
      <el-timeline>
        <el-timeline-item
          v-for="(activity, index) in liaisonData"
          :key="index"
          :type="activity.type"
          color="#1890FF"
          :icon="activity.icon"
          :size="activity.size"
        >
          <div v-if="activity.types === 1">
            <div class="head">
              <img :src="activity.card.avatar" alt="" class="head-portrait" />
              <div class="head-right">
                <span class="head-name">{{ activity.card.name }}</span>
                <span class="head-time">{{ activity.created_at }}</span>
              </div>
              <el-dropdown class="more">
                <i class="el-icon-more" />
                <el-dropdown-menu style="width: 100px; text-align: center">
                  <el-dropdown-item @click.native="handleContract(activity)">编辑 </el-dropdown-item>
                  <el-dropdown-item @click.native="handleDelete(activity)"> 删除 </el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </div>
            <div class="record">
              {{ activity.content }}
            </div>
            <div class="reminderTime" v-if="activity.time">
              <img src="../../../../assets/images/zhong.png" alt="" class="zhong" /> 提醒时间：{{ activity.time }}
            </div>
          </div>
          <div v-else>
            <div v-if="editIndex !== index">
              <div class="head">
                <img :src="activity.card.avatar" alt="" class="head-portrait" />
                <div class="head-right">
                  <span class="head-name">{{ activity.card.name }}</span>
                  <span class="head-time">{{ activity.created_at }}</span>
                </div>
                <el-dropdown class="more">
                  <i class="el-icon-more" />
                  <el-dropdown-menu style="width: 100px; text-align: center">
                    <el-dropdown-item @click.native="handleEdit(activity, index)"> 编辑 </el-dropdown-item>
                    <el-dropdown-item @click.native="handleDelete(activity)"> 删除 </el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </div>
              <div class="record">
                {{ activity.content }}
              </div>
              <div v-for="(fileItem, g) in activity.attachs" :key="g" class="flex">
                <div class="fileItem" @click="filePreview(fileItem)">
                  <span class="file" v-if="toSrcIcon(fileItem.name) !== 'img'">
                    {{ getFileTypeFn(fileItem.name) }}
                  </span>
                  <img :src="fileItem.url" class="file" alt="" v-else />

                  {{ fileItem.real_name }}
                </div>
              </div>
            </div>

            <record-upload v-if="editIndex === index" :form-info="formInfo" @change="uploadChange"></record-upload>
          </div>
        </el-timeline-item>
      </el-timeline>
    </div>
    <div v-if="liaisonData.length == 0" class="default">
      <img src="../../../../assets/images/genjin.png" alt="" class="img" />
      <span class="text">{{ $t('public.message14') + '~' }}</span>
    </div>
    <!-- 查看图片 -->
    <!-- 打开文件 -->
    <fileDialog ref="viewFile"></fileDialog>

    <remind-dialog ref="remindDialog" :config="remindConfig" @change="remindChange"></remind-dialog>
  </div>
</template>

<script>
import { clientFollowDeleteApi, clientFollowListApi } from '@/api/enterprise'
import { getFileType, getFileExtension } from '@/libs/public'
import { toSrcFn } from '@/utils/format'
import { roterPre } from '@/settings'
import file from '@/utils/file'
import Vue from 'vue'
Vue.use(file)
export default {
  name: 'Record',
  components: {
    recordUpload: () => import('./recordUpload'),
    fileDialog: () => import('@/components/openFile/previewDialog '), // 图片、MP3，MP4弹窗
    remindDialog: () => import('./remindDialog')
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
      liaisonData: [],
      liaison: {},
      addRecordShow: false,
      isTitle: '添加跟进记录',
      userId: JSON.parse(localStorage.getItem('userInfo')).id,
      id: '',
      form: {
        content: '',
        files: [],
        types: 0,
        time: ''
      },
      editIndex: -1,
      remindConfig: {}
    }
  },
  methods: {
    getFileTypeFn(name) {
      return getFileExtension(name)
    },
    toSrcIcon(name) {
      return getFileType(name)
    },

    getTableData() {
      this.liaisonData = []
      this.liaison.eid = this.formInfo.data.eid
      clientFollowListApi(this.liaison).then((res) => {
        this.liaisonTotal = res.data.count
        res.data.list.map((item, index) => {
          if (index == 0) {
            item.icon = 'iconfont icondangqian'
          }
          this.liaisonData.push(item)
        })
      })
    },

    uploadChange(e) {
      if (e.type === 'add') {
        this.addRecordShow = false
      } else {
        this.editIndex = -1
      }
      this.getTableData()
    },

    // 下载文件
    downloadFile(row, name) {
      this.fileLinkDownLoad(row, name)
    },

    // 添加
    addRecord() {
      this.formInfo.type = 'add'
      this.addRecordShow = !this.addRecordShow
      this.isTitle = '添加跟进记录'
    },

    //提醒编辑
    handleContract(row = {}) {
      this.remindConfig = {
        eid: this.formInfo.data.eid,
        isEdit: true,
        data: row
      }
      this.$refs.remindDialog.handleOpen(true)
    },

    remindChange() {
      this.getTableData()
    },

    // 编辑
    handleEdit(row, index) {
      this.formInfo.type = 'edit'
      this.formInfo.editData = row
      this.editIndex = index
    },

    // 删除
    async handleDelete(row, index) {
      await this.$modalSure(this.$t('customer.placeholder63'))
      await clientFollowDeleteApi(row.id)
      if (this.liaison.page > 1 && this.liaisonData.length <= 1) {
        this.liaison.page--
      }
      this.getTableData()
    },

    // 判断上传的文件格式文件是否有无图片，无图则为默认
    toSrc(e) {
      return toSrcFn(e)
    }
  }
}
</script>

<style lang="scss" scoped>
.file {
  display: flex;
  width: 30px;
  height: 38px;
  background: url('../../../../assets/images/cloud/file-box.png') no-repeat;
  background-size: 30px 38px;
  color: #fff !important;
  justify-content: center;
  line-height: 38px;
  font-size: 12px;
  margin-right: 10px;
}
.default {
  .img {
    width: 200px;
    height: 150px;
  }
  .text {
    font-size: 12px;
    font-family: PingFangSC, PingFang SC;
    font-weight: 400;
    color: #c0c4cc;
  }
  height: 369px;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.followUpRecord {
  font-family: PingFangSC-Regular, PingFang SC;
}
.btn-box1 {
  height: 32px;
  display: flex;
  justify-content: space-between;
  align-items: center;
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
.topBtn {
  width: 102px;
  height: 32px;
  font-size: 13px;
}
.zhong {
  display: inline-block;
  margin-left: 9px;
  margin-right: 4px;
  width: 17px;
  height: 19px;
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
  display: flex;
  align-items: center;
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

.fileItem {
  cursor: pointer;
  display: flex;
  align-items: center;
  margin: 10px;
  padding: 4px 20px 4px 8px;
  background-color: #f5f5f5;
  width: fit-content;
  margin-left: 38px;
  font-size: 13px;
  font-weight: 400;
  color: #333333;
  border-radius: 4px;
}
.iconfont {
  color: #1890ff;
}
::v-deep .el-timeline-item__tail {
  position: absolute;
  left: 4px;
  height: 100%;

  border-left: 1px solid #dfe4ed;
}
::v-deep.el-timeline-item__node--normal {
  left: 0px;
  width: 10px;
  height: 10px;
}
::v-deep .el-timeline-item__wrapper {
  position: relative;
  top: -12px;
}
::v-deep .el-textarea__inner {
  min-height: 96px;
  border: none;
  font-size: 12px;
}
::v-deep .icondangqian {
  font-size: 16px;
  color: #1890ff;
  background-color: none;
}
</style>
