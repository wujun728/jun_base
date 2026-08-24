<template>
  <div class="divBox" @click="isSortShow = false">
    <!-- 云盘主页面 -->
    <div class="v-height-flag" style="height: 100%">
      <!-- 拖拽上传 -->

      <el-upload
        drag
        ref="uploadServer"
        :on-change="onChenge"
        :before-upload="handleUpload"
        action="#"
        :on-success="handleSuccess"
        :disabled="switchIndex == 1"
        :show-file-list="false"
        :http-request="uploadServerLog"
        multiple
      >
        <el-card
          body-style="padding:0;"
          class="card-box"
          v-loading="loading"
          element-loading-text=""
          element-loading-spinner=""
          element-loading-background=" rgba(255,255,255,0.6)"
        >
          <el-row :gutter="20" v-if="switchIndex != 7">
            <!-- 左侧企业空间 -->
            <el-col v-bind="gridl">
              <cloudfile-left ref="cloudfileLeft" @confirmData="confirmData" />
            </el-col>

            <el-col v-bind="gridr">
              <!-- 右侧顶部按钮 switchIndex默认值:1   -->
              <div id="cloudfile-right" class="cloudfile-right">
                <formBox
                  :breadcrumbArray="breadcrumbArray"
                  :pid="pid"
                  :spaceId="spaceId"
                  :switchIndex="switchIndex"
                  :total="total"
                  @formBoxClick="formBoxClick"
                  @handleCommand="handleCommand"
                  @handleSort="handleSort"
                  @uploadFile="handleUpload"
                />

                <el-row class="mt10">
                  <!-- 最近的文件 - 默认是空间文件 -->
                  <lately-file
                    v-if="switchIndex == 1"
                    ref="latelyFile"
                    :file-style="fileStyle"
                    :pageLimit="pageLimit"
                    :space-id="spaceId"
                    :spaceType="spaceType"
                    :wps_type="wps_type"
                    @handlerMyFile="handlerMyFile"
                    @shareItemFile="shareFile"
                    @openItemFile="openfile"
                    @totalFn="totalFn"
                  />
                  <!-- 空间的文件 -->
                  <space-file
                    v-if="switchIndex == 6"
                    ref="spaceFile"
                    :file-style="fileStyle"
                    :pageLimit="pageLimit"
                    :space-id="spaceId"
                    :spaceType="spaceType"
                    :switch="switchIndex"
                    :wps_type="wps_type"
                    @handlerMyFile="handlerMyFile"
                    @openItemFile="openfile"
                    @shareItemFile="shareFile"
                    @totalFn="totalFn"
                  />
                </el-row>
              </div>
            </el-col>
          </el-row>
          <!-- 回收站的文件 -->
          <recovery-file
            v-if="switchIndex == 7"
            ref="recoveryFile"
            :ent-button="entButton"
            :file-style="fileStyle"
            :space-id="spaceId"
            :wps_type="wps_type"
            @backFn="backFn"
          />
        </el-card>
      </el-upload>
    </div>

    <!-- 新建文件弹窗 -->
    <newFileDialog
      ref="newFileDialog"
      :config="configMyFile"
      @handleIsOK="handleIsOK"
      @handleCreateXmindFile="handleCreateXmindFile"
    />

    <!-- 移动文件弹窗 -->
    <move-dialog ref="moveDialog" :move-data="moveData" @handlerMove="handlerMove" />

    <!-- 打开文件 -->
    <fileDialog ref="viewFile" />

    <!-- 文件模板弹窗 -->
    <file-temp ref="fileTemp" :from-data="fileTemp" @handleTemplate="handleTemplate" />

    <!-- 文件上传进度 -->
    <uploadProgress
      ref="uploadProgress"
      :uploadFileList="uploadFileList"
      @updateFileStatus="updateFileStatus"
      @close="clearList"
    />
  </div>
</template>

<script>
// 导入辅助工具库
import helper from '@/libs/helper';
// 导入云存储上传工具
import { uploader } from '@/utils/uploadCloud';
// 导入获取令牌的工具
import { getToken } from '@/utils/auth';
// 导入设置信息
import SettingMer from '@/libs/settingMer';
// 导入分片上传工具
import { uploadByPieces } from '@/utils/uploadChunk';
// 导入云盘相关API
import { 
  folderAllDeleteApi,
  folderAllDestroyApi,
  folderAllDestroyFileApi,
  folderSpaceEntAllDeleteApi,
  folderSpaceEntAllDestroyApi,
  folderSpaceEntAllRecoverApi 
} from '@/api/cloud';
// 导入获取上传密钥的API
import { getUploadKeysApi } from '@/api/public';
// 导入生成XMind文件的工具
import { generateXmindFile } from '@/components/xmind-editor/utils';

export default {
  name: 'CloudFile',
  components: {
    cloudfileLeft: () => import('./components/layout/cloudfileLeft'), // 左侧导航
    formBox: () => import('./components/layout/formBox'), // 顶部操作
    latelyFile: () => import('./components/layout/latelyFile'), // 最近的文件
    recoveryFile: () => import('./components/layout/recoveryFile'), // 回收站的文件
    spaceFile: () => import('./components/layout/spaceFile'), // 企业空间文件
    newFileDialog: () => import('./components/newFileDialog'), // 新建文件弹窗
    moveDialog: () => import('./components/moveDialog'), // 移动文件弹窗
    fileTemp: () => import('./components/fileTemp'), // 文件模板弹窗
    uploadProgress: () => import('./components/uploadProgress'), // 文件上传进度
    fileDialog: () => import('@/components/openFile/previewDialog ') // 图片、MP3，MP4弹窗
  },
  data() {
    return {
      myHeaders: {
        authorization: 'Bearer ' + getToken()
      },
      loading: false,
      uploadData: {},
      fileStyle: {
        style: 1,
        sort: 3,
        type: 1,
        keyword: '',
        sortBy: 0
      },
      total: 0,
      gridl: {
        xl: 3,
        lg: 4,
        md: 5,
        sm: 6,
        xs: 24
      },
      gridr: {
        xl: 21,
        lg: 20,
        md: 19,
        sm: 18,
        xs: 24
      },
      type: '1',
      fileUrl: '',
      spaceType: '', // 判断是否是最近打开
      breadcrumbArray: [], // 面包屑文字
      switchIndex: 1,
      configMyFile: {},
      pid: 0,
      spaceId: 0,
      entButton: false,
      ids: [],
      uploadSize: JSON.parse(localStorage.getItem('sitedata')).global_attach_size || 30,
      moveData: {
        id: [],
        type: 2
      },
      elemetnNode: '',
      fileData: {},
      fileTemp: {
        type: 1,
        title: this.$t('file.selecttemplate'),
        width: '820px'
      },
      isSortShow: false,
      pageLimit: 0,
      wps_type: JSON.parse(localStorage.getItem('webConfig')).wps_type || '0',
      uploadFileList: [],
      // 上传参数
      uploadRes: {}
    }
  },

  mounted() {
    this.$refs.uploadServer.$el.querySelector('.el-upload__input').disabled = true
    this.$nextTick(() => {
      let height = document.documentElement.clientHeight - 290
      this.pageLimit = Math.floor(height / 58) // 假设每行高度为 50px
    })

    document.addEventListener('dragenter', this.handleDragEnter)
    document.addEventListener('dragleave', this.handleDrop)

    // 获取上传参数
    this.getUploadParams()
  },
  beforeDestroy() {
    document.removeEventListener('dragenter', this.handleDragEnter)
    document.removeEventListener('dragleave', this.handleDrop)
  },

  methods: {
    async handleCreateXmindFile(config) {
      const { data, success, error } = config
      try {
        const xmindFile = await generateXmindFile(data.name + '.xmind')
        const res = await this.handleUpload(xmindFile, true)
        if (res.status === 200) {
          this.$message.success('创建成功')
          success()
        } else {
          error()
        }
      } catch (err) {
        error()
      }
    },
    // 上传前
    handleUpload(file, onlyFunc = false) {
      this.loading = false
      const types = helper.uploadTypes
      const fileTypeName = file.name.substr(file.name.lastIndexOf('.') + 1)
      const isImage = types.includes(fileTypeName.toLowerCase())
      const isLtSize = file.size / 1024 / 1024 < this.uploadSize
      if (!isImage) {
        this.$message.error('仅支持 ' + types.join(',') + ' 格式')
        return false
      }
      if (!isLtSize) {
        this.$message.error('上传文件、图片大小不能超过 ' + this.uploadSize + ' MB!')
        return false
      }

      // 生成上传函数
      const uploadFn = () => {
        return new Promise((resolve, reject) => {
          if (this.uploadRes.type === 'local') {
            // 本地上传
            uploadByPieces(file, { pid: this.pid == 0 ? this.spaceId : this.pid }, this.fileUrl)
              .then((res) => {
                resolve(res)
              })

              .catch((err) => {
                reject(err)
              })
          } else {
            // 云储存上传
            uploader(file, 0, {
              uplaodRes: this.uploadRes,
              fid: this.pid == 0 ? this.spaceId : this.pid,
              url: SettingMer.https + `/cloud/file/${this.pid == 0 ? this.spaceId : this.pid}/save`,
              upload_type: this.uploadRes.type
            }).then((res) => {
              resolve(res)
            })
          }
        })
      }

      if (onlyFunc) {
        return uploadFn()
      }

      // 加入上传队列实现上传进度条
      this.uploadFileList.push({
        // 索引
        index: this.uploadFileList.length,
        // 文件
        file,
        // 上传函数
        uploadFn,
        // 文件状态
        status: 'ready',
        // 进度条值
        progressValue: 1
      })
    },

    // 上传文件变化
    onChenge(file, fileList) {},

    // 清空上传列表
    clearList() {
      this.$refs.uploadServer.clearFiles()
      this.uploadFileList = []
    },

    // 监听上传文件拖拽进入事件
    handleDragEnter(e) {
      if (this.switchIndex == 6) {
        this.elemetnNode = e.target
        this.loading = true
      } else {
        return false
      }
    },

    // 监听上传文件放下事件
    handleDrop(e) {
      if (this.elemetnNode === e.target) {
        this.loading = false
      }
      e.preventDefault()
    },

    getUploadParams() {
      getUploadKeysApi().then((res) => {
        this.uploadRes = res.data || null
      })
    },

    // 上传文件方法
    async uploadServerLog(params) {},

    // 上传成功
    handleSuccess(response, file, fileList) {},

    handleClick(e) {
      // this.$refs.shareFile.shareIndex = e.name
      // this.$refs.shareFile.getTreeData()
    },

    // 点击item项
    openfile(item) {
      this.filePreview(item, this.spaceId, 'cloud')
    },

    // 分享文件
    shareFile(item) {
      let origin = window.location.origin
      const fileType = item.file_ext
      const types = ['jpeg', 'gif', 'bmp', 'png', 'jpg', 'mp3', 'mp4']
      const isImage = types.includes(fileType)
      // 打开word类型文件
      let url = ''
      if (!isImage) {
        url = `${origin}/admin/openFile?id=${item.id}&&fid=${item.pid || this.spaceId}`
      } else {
        // 打开图片,音频，视频类型文件
        url = item.file_url
      }

      let realName = JSON.parse(localStorage.getItem('userInfo'))
      const oInput = document.createElement('input')
      let value = '【' + realName.name + '】分享给您【' + item.name + '】' + '请点击链接打开!' + url

      oInput.value = value
      document.body.appendChild(oInput)
      oInput.select()
      document.execCommand('Copy')
      oInput.style.display = 'none'
      document.body.removeChild(oInput)
      this.$message.success('复制分享链接成功，请前去粘贴使用')
    },

    formBoxClick(type, val, index) {
      switch (type) {
        // 移动至
        case 'allMove': {
          this.allMove()
          break
        }

        // 删除
        case 'allDelete': {
          this.allDelete(val)
          break
        }

        // 共享相关操作
        case 'handleClick': {
          this.handleClick(val)
          break
        }

        // 我的文件相关操作
        case 'handleScreen': {
          this.handleScreen(val)
          break
        }

        // 点击面包屑
        case 'getBreadcrumb': {
          this.getBreadcrumb(val, index)
          break
        }

        // 文件样式变化
        case 'styleChage': {
          this.styleChage(val, index)
          break
        }

        // 文件类型筛选
        case 'handleType': {
          this.handleType(val)
          break
        }

        // 上传文件成功
        case 'handleSuccess': {
          this.handleSuccess(val)
          break
        }

        // 打开回收站
        case 'recoveryBtn': {
          this.recoveryBtn()
          break
        }
      }
    },

    // 更新文件上传状态
    // ready 准备上传  uploading 上传中  success 上传成功 error 上传失败
    updateFileStatus({ status, index, message }) {
      let file = this.uploadFileList.find((item) => item.index === index)

      if (file) {
        file.status = status
      }

      switch (status) {
        case 'error': {
          file.errorMsg = message
          break
        }

        case 'success': {
          this.switchIndex === 6 ? this.$refs.spaceFile.getTreeData() : this.$refs.latelyFile.getTreeData()
          break
        }
      }
    },

    styleChage(val, type) {
      this.fileStyle.keyword = val.keyword
      this.fileStyle.style = val.style
      if (!type) {
        this.getFileList(val.keyword)
      }
    },

    totalFn(val) {
      this.total = val
    },

    // 列表搜索
    getFileList(val) {
      this.ids = []
      switch (this.switchIndex) {
        // 最近访问
        case 1: {
          this.$refs.latelyFile.getTreeData(1, val)
          break
        }

        //  企业空间文件
        case 6: {
          this.$refs.spaceFile.getTreeData(1)
          this.$refs.spaceFile.menuIds = []
          break
        }

        //  回收站
        case 7: {
          this.$refs.recoveryFile.getTreeData()
          this.$refs.recoveryFile.menuIds = []
          break
        }
      }
    },

    backFn() {
      this.switchIndex = 1
    },

    confirmData(data, item) {
      this.fileStyle.keyword = ''
      if (item === 'recently') {
        // 最近访问
        this.switchIndex = 1
        this.spaceType = item
        this.breadcrumbArray = []
        setTimeout(() => {
          this.$refs.latelyFile.getTreeData(item)
        }, 300)
        return false
      } else if (item == 'recovery') {
        // 回收站
        this.switchIndex = 7
        this.spaceType = item
        this.breadcrumbArray = []
        setTimeout(() => {
          this.$refs.recoveryFile.getTreeData(item)
        }, 300)
        return false
      } else {
        // 空间文件
        this.switchIndex = data.selectIndex
        this.spaceType = ''
        this.breadcrumbArray = []
        this.breadcrumbArray.push(item)
      }
      if (data.type === 2) {
        // this.getFileList()
      } else {
        this.type = '1'
        this.spaceId = data.id
        if (data.selectIndex == 6) {
          this.pid = 0
          this.entButton = true
          setTimeout(() => {
            this.$refs.spaceFile.where.pid = ''
            this.$refs.spaceFile.breadcrumbArray = [{ name: this.$t('file.allfiles'), pid: '' }]
            this.$refs.spaceFile.checked = false
            this.$refs.spaceFile.menuIds = []
            this.$refs.spaceFile.where.page = 1
            if (this.spaceId) {
              this.$refs.spaceFile.getTreeData(item)
            } else {
              this.$refs.spaceFile.fileData = []
            }
          }, 200)
        } else {
          this.entButton = false
        }
      }
    },

    // 点击面包屑
    getBreadcrumb(id, index) {
      setTimeout(() => {
        this.$refs.spaceFile.getBreadcrumb(id, index)
      }, 200)
      this.$nextTick(() => {
        this.breadcrumbArray.splice(index + 1, this.breadcrumbArray.length - index)
        this.$refs.spaceFile.breadcrumbArray = JSON.parse(JSON.stringify(this.breadcrumbArray))
      })
    },

    // 排序
    handleCommand(command) {
      this.fileStyle.sort = command
      this.getFileList()
    },

    // 排序
    handleSort(index, id) {
      this.fileStyle.sortBy = id
      this.getFileList()
    },

    // 类型
    handleType(command) {
      this.fileStyle.type = command
      this.getFileList()
    },

    handleScreen(command) {
      if (command == 5) {
        this.configMyFile = {
          title: this.$t('file.newfolder'),
          pid: this.pid,
          command: command,
          switch: this.switchIndex,
          spaceId: this.spaceId,
          type: ''
        }
        this.$refs.newFileDialog.handleOpen()
      } else if (command == 7) {
        this.handleTemp()
      } else {
        var title = ''
        var type = ''
        if (command == 1) {
          title = this.$t('file.newdocument')
          type = 'word'
        } else if (command == 2) {
          title = this.$t('file.newtable')
          type = 'excel'
        } else if (command == 9) {
          title = this.$t('file.newmindmap')
          type = 'mindmap'
        } else {
          title = this.$t('file.newslide')
          type = 'ppt'
        }
        this.configMyFile = {
          title: title,
          pid: this.pid,
          command: command,
          switch: this.switchIndex,
          spaceId: this.spaceId,
          type: type
        }
        this.$refs.newFileDialog.handleOpen()
      }
    },

    // 新建企业空间
    addEntSpace() {
      this.$refs.cloudfileLeft.addName()
    },

    handleTemp() {
      if (this.entButton) {
        // 企业空间
        this.fileTemp.type = 2

        this.fileTemp.fid = this.spaceId
      } else {
        this.fileTemp.type = 1
      }
      this.fileTemp.id = this.pid
      this.$refs.fileTemp.handleOpen()
    },

    handlerMyFile(data, item) {
      if (item) {
        this.breadcrumbArray.push(item)
      }
      if (data.type === 2) {
        this.$refs.cloudfileLeft.getFolderTotal()
      } else {
        this.pid = data.id
        this.ids = data.ids
      }
    },

    handleIsOK() {
      if (this.switchIndex === 6) {
        this.$refs.spaceFile.getTreeData(1)
      } else {
        this.$refs.myFile.getTreeData(1)
      }
    },

    recoveryBtn() {
      this.switchIndex = 7
    },

    handleTemplate() {
      this.getFileList()
    },

    allDelete(type) {
      if (this.ids.length <= 0) {
        this.$message.error(this.$t('file.placeholder05'))
      } else {
        const uids = this.ids
        if (uids.length > 0 && uids[0] === 0) {
          uids.splice(0, 1)
        }
        if (type === 1) {
          this.getFolderAllDelete({ id: uids })
        } else if (type === 2) {
          if (this.entButton) {
            // 企业空间
            this.$modalSure('彻底删除后,内容无法恢复,你确定要删除该内容吗').then(() => {
              this.getFolderEntAllDestroy(this.spaceId, { id: uids })
            })
          } else {
            this.$modalSure('彻底删除后,内容无法恢复,你确定要删除该内容吗').then(() => {
              this.getFolderAllDestroy({ id: uids })
            })
          }
        } else if (type === 3) {
          this.$modalSure('您确定要加入回收站').then(() => {
            this.getFolderEntAllDelete(this.spaceId, { id: uids })
          })
        }
      }
    },

    allDestroy() {
      if (this.ids.length <= 0) {
        this.$message.error(this.$t('file.placeholder06'))
      } else {
        const uids = this.ids
        if (uids.length > 0 && uids[0] === 0) {
          uids.splice(0, 1)
        }
        if (this.entButton) {
          // 企业空间
          this.getFolderEntAll(this.spaceId, { id: uids })
        } else {
          this.getFolderAllFile({ id: uids })
        }
      }
    },

    allMove() {
      if (this.ids.length <= 0) {
        this.$message.error(this.$t('file.placeholder07'))
      } else {
        const uids = this.ids
        if (uids.length > 0 && uids[0] === 0) {
          uids.splice(0, 1)
        }
        if (this.switchIndex == 6) {
          this.moveData.id = uids
          this.moveData.type = 4
          this.moveData.fid = this.spaceId
          this.$refs.moveDialog.handleOpen()
        } else {
          this.moveData.id = uids
          this.moveData.type = 2
          this.$refs.moveDialog.handleOpen()
        }
      }
    },

    // 移动回调
    handlerMove(data) {
      if (data.type === 2 || data.type === 4) {
        this.getFileList()
      }
    },
    // 批量删除文件（我的文件）
    getFolderAllDelete(data) {
      folderAllDeleteApi(data).then((res) => {
        this.getFileList()
      })
    },

    // 批量彻底删除文件（我的文件）
    async getFolderAllDestroy(data) {
      await folderAllDestroyApi(data)
      await this.getFileList()
    },

    // 批量恢复文件（我的文件）
    async getFolderAllFile(data) {
      await folderAllDestroyFileApi(data)
      await this.getFileList()
    },

    // 批量删除文件（企业空间）
    async getFolderEntAllDelete(id, data) {
      await folderSpaceEntAllDeleteApi(id, data)
      await this.getFileList()
    },

    // 批量彻底删除文件（企业空间）
    async getFolderEntAllDestroy(id, data) {
      await folderSpaceEntAllDestroyApi(id, data)
      await this.getFileList()
    },

    // 批量恢复删除文件（企业空间）
    async getFolderEntAll(id, data) {
      await folderSpaceEntAllRecoverApi(id, data)
      await this.getFileList()
    }
  },

  watch: {
    lang() {
      this.setOptions()
    },
    switchIndex: {
      handler(nVal, oVal) {
        if (nVal == 6 || nVal == 1) {
          this.fileUrl = SettingMer.https + `/cloud/file/${this.spaceId}/upload`
        } else {
          this.fileUrl = SettingMer.https + `/folder/upload`
        }
      },
      immediate: true,
      deep: true
    },
    'fileStyle.style'(val) {
      const windowHeight = document.documentElement.clientHeight - 284
      //大图模式，根据页面高度计算每页数据条数，这里可以根据实际需求进行适当的调整
      if (val == 2) {
        const winWidth = document.getElementById('cloudfile-right').offsetWidth
        const col = Math.floor(winWidth / 130)
        const row = Math.floor(windowHeight / 154)
        this.pageLimit = col * row
      } else {
        let height = document.documentElement.clientHeight - 290
        this.pageLimit = Math.floor(height / 58)
      }
      setTimeout(() => {
        this.getFileList()
      }, 200)
    }
  }
}
</script>

<style lang="scss" scoped>
.card-box {
  height: calc(100vh - 80px);
}
.upload {
  display: inline-block;
}

::v-deep.el-loading-spinner {
  /* 图片替换为你自定义的即可 */
  background: url('../../../assets/images/loading-img.png') no-repeat;

  background-size: 150px 79px;
  width: 100%;
  height: 100%;
  position: relative;
  top: 50%;
  left: 50%;
}
::v-deep .el-loading-text {
  position: relative;
  top: 50%;
  left: 50%;
  font-family: PingFang SC, PingFang SC;
  font-weight: 400;
  font-size: 16px;
  color: #303133;
}
::v-deep.el-loading-spinner .circular {
  display: none;
}

.mt22 {
  margin-top: 22px;
}
.iconshaixuan {
  margin-left: 4px;
  font-size: 13px;
  color: #999999;
}
.iconpaixu {
  margin-left: 4px;
  font-size: 13px;
  color: #999999;
}
.cloudfile-right {
  padding: 0 20px;

  .header-16 {
    display: flex;
    justify-content: space-between;
    align-items: center;
    .breadcrumb-item {
      cursor: pointer;
      font-family: PingFang SC, PingFang SC;
      font-weight: 400;
      font-size: 15px;
      color: #909399;
      margin-top: 26px;
    }
  }
  .el-dropdown-link {
    height: 32px;
    padding: 0 10px;
    display: flex;
    align-items: center;
  }
  .el-dropdown-link:hover {
    background-color: rgba(247, 247, 247, 1);
  }
  .text {
    width: 25px;
    color: #909399;
    height: 32px;
    display: flex;
    justify-content: center;
    font-family: PingFang SC, PingFang SC;
    font-weight: 400;
    font-size: 13px;
    border-radius: 4px;
  }

  .header {
    .header-right {
      display: flex;
      align-items: center;
      justify-content: flex-end;
      // text-align: right;
      flex-wrap: wrap;

      ::v-deep .el-dropdown-selfdefine {
        display: flex;
        align-items: center;
        i {
          padding-left: 6px;
        }
      }
      .icon-name {
        font-size: 13px;
        span {
          padding-right: 3px;
        }
        i {
          font-size: 13px;
        }
      }
    }
  }
  .sort-dropdown {
    .sort-content {
      position: absolute;
      left: -20px;
      top: 30px;
      z-index: 222;
      ul {
        width: 120px;
        list-style: none;
        margin-bottom: 0;
      }
      ::v-deep .el-dropdown-menu {
        position: static;
        margin: 0;
      }
      .sort-ul {
        margin-top: -3px !important;
        border-radius: 0 0 4px 4px !important;
        li {
          padding-right: 40px !important;
        }
      }
    }
  }
}
::v-deep .el-upload {
  width: 100%;
  height: 100%;
  border: none;
  text-align: left;
  // pointer-events: none;
}
::v-deep .el-upload .el-upload-dragger {
  width: 100%;
  height: 100%;
  border: none;
  text-align: left;
  // pointer-events: none;
}
::v-deep.el-pagination {
  padding-bottom: 0;
}
</style>
