<!-- @FileDescription: 文件预览页面-文档类型 -->
<template>
  <div class="file-box">
    <div class="header flex-between">
      <div>{{ file.name }}.{{ file.file_ext }}</div>
      <el-button v-show="editBtn" :loading="loading" size="small" type="primary" @click="save">保存</el-button>
    </div>

    <div class="main">
      <component
        :is="type"
        ref="fileEdit"
        :fid="fid"
        :file="file"
        :url="file.file_url"
        :editBtn="editBtn"
        @startLoading="startLoading"
        @closeLoading="closeLoading"
      />
    </div>
  </div>
</template>
<script>
import { getFileInfoApi, getAttachInfoApi } from '@/api/cloud'
export default {
  name: '',
  components: {
    docx: () => import('./previewDocx'),
    md: () => import('./previewMd'),
    mp3: () => import('./previewMp3'),
    mp4: () => import('./previewMp4'),
    pdf: () => import('./previewPdf'),
    txt: () => import('./previewTxt'),
    office: () => import('./previewOffice'),
    docxEdit: () => import('./editDocx'),
    xlsxEdit: () => import('./editXlsx'),
    xmindEdit: () => import('./editXmind'),
    xmindPreview: () => import('./previewXmind')
  },
  data() {
    return {
      type: '',
      file: {},
      id: 0,
      fid: 0,
      loading: false,
      editBtn: false
    }
  },
  created() {
    this.id = this.$route.query.id
    if (this.$route.query.fid) {
      this.fid = this.$route.query.fid
      this.getFileInfo()
    } else {
      this.getAttachInfo()
    }
  },
  methods: {
    // 获取文件详情和权限
    getFileInfo() {
      getFileInfoApi(this.fid, this.id).then((res) => {
        this.handleRes(res)
      })
    },

    getAttachInfo() {
      getAttachInfoApi(this.id).then((res) => {
        this.handleRes(res)
      })
    },

    // 判断文件是预览还是编辑
    handleRes(res) {
      this.file = res.data
      this.type = res.data.file_ext
      if (res.data.name) {
        document.title = res.data.name
      } else {
        res.data.name = res.data.real_name.split('.')[0]
        document.title = res.data.name
      }

      // 编辑
      if (
        res.data.auth &&
        res.data.auth.update == 1 &&
        ['docx', 'xlsx', 'txt', 'md', 'html', 'xmind'].includes(res.data.file_ext)
      ) {
        if (res.data.file_ext == 'html') {
          this.type = 'md'
        }
        this.editBtn = true
        if (res.data.file_ext == 'docx') {
          this.type = 'docxEdit'
        } else if (res.data.file_ext == 'xlsx') {
          this.type = 'xlsxEdit'
        } else if (res.data.file_ext == 'xmind') {
          this.type = 'xmindEdit'
        }

        // 仅预览 office 类文件
      } else if (['doc', 'pptx', 'ppt', 'xlsx', 'docx', 'xls'].includes(res.data.file_ext)) {
        this.type = 'office'
        // 仅预览 xmind 类文件
      } else if (['xmind'].includes(res.data.file_ext)) {
        this.type = 'xmindPreview'
      }
    },
    startLoading() {
      this.loading = true
    },
    closeLoading() {
      this.loading = false
    },
    // 保存
    save() {
      this.loading = true
      if (this.type == 'docxEdit') {
        this.$refs.fileEdit.wordOption()
      } else if (this.type == 'xlsxEdit') {
        this.$refs.fileEdit.exportData(1)
      } else if (this.type == 'txt') {
        this.$refs.fileEdit.save()
      } else if (this.type == 'md') {
        this.$refs.fileEdit.save()
      } else if (this.type == 'xmindEdit') {
        this.$refs.fileEdit.save()
      }
    }
  }
}
</script>
<style lang="scss" scoped>
.file-box {
  height: 100vh;
  overflow-y: auto;
  scrollbar-width: none; /* firefox */
  -ms-overflow-style: none; /* IE 10+ */
  .header {
    z-index: 99;
    position: fixed;
    top: 0;
    width: 100%;
    height: 60px;
    display: flex;
    align-items: center;
    background: #fff;
    padding: 0 20px;
    font-family: PingFang SC, PingFang SC;
    font-weight: 500;
    font-size: 18px;
    color: #303133;
    border-bottom: 1px solid #dcdfe6ff;
  }
  .main {
    width: 100%;
    margin-top: 60px;
  }
}
</style>
