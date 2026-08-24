<!-- @FileDescription: 上传文件进度条弹窗 -->
<template>
  <div class="box" v-if="progressShow">
    <div class="title flex-between">
      <!-- 上传中 -->
      <div v-if="!isAllSuccess && errorFileList === 0">
        <img src="../../../../assets/images/cloud-icon1.png" alt="" class="img" /> 上传中 {{ successNum }}/{{
          fileList.length
        }}
      </div>

      <!-- 上传成功 -->
      <div v-else-if="isAllSuccess" class="flex-center">
        <img src="../../../../assets/images/cloud-icon2.png" alt="" class="img mr6" />
        全部上传成功
      </div>

      <!-- 部分文件上传失败 -->
      <div v-else>
        <img src="../../../../assets/images/cloud-icon3.png" alt="" class="img" />
        部分文件上传失败
      </div>

      <div class="pointer">
        <i :class="takeShow ? 'el-icon-arrow-down' : 'el-icon-arrow-up'" @click="takeBack"></i>
        <i class="el-icon-close" @click="close"></i>
      </div>
    </div>

    <div class="tips-box" v-show="takeShow">
      <template v-if="!isAllSuccess">
        <span>已上传{{ successNum }}/{{ uploadFileList.length }}个任务</span></template
      >

      <template v-else-if="successNum == fileList.length">
        <span>已上传{{ successNum }}个任务,共 {{ toSizeFile(totalSize) }}</span>
      </template>

      <template v-else>
        <span>已上传{{ successNum }}/{{ fileList.length }}个任务</span>
      </template>
    </div>

    <div class="content" v-show="takeShow">
      <div class="item flex-between" v-for="(item, index) in fileList" :key="index">
        <div class="flex-center">
          <div class="file">W</div>
          <div class="file-item">
            <span class="file-size" v-if="item.status === 'pending'">正在上传中</span>
            <span class="file-name over-text">{{ item.name }}</span>
            <span class="file-size" v-if="item.status === 'error'">{{ item.errorMsg }}</span>
            <span class="file-size" v-else-if="item.status === 'success'"> {{ toSizeFile(item.size) }}</span>
          </div>
        </div>

        <div>
          <div class="flex-center">
            <!-- 上传中 -->
            <template v-if="item.status === 'pending'">
              <span class="percent">{{ item.progressValue }}%</span>
              <oa-progress
                :uploadStatus="item.status"
                :fileSize="item.size"
                @update="(value) => (item.progressValue = value)"
              />
            </template>

            <!-- 上传成功 -->
            <i class="el-icon-check" v-else-if="item.status === 'success'"></i>

            <!-- 上传失败 -->
            <i class="el-icon-warning" v-if="item.status === 'error'"></i>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { formatBytes } from '@/libs/public'
export default {
  name: 'OA-uploadProgress',

  props: {
    uploadFileList: {
      type: Array,
      default: () => []
    }
  },

  data() {
    return {
      // 进度条显示
      progressShow: false,
      // 上传文件列表
      fileList: [],
      // 收放切换
      takeShow: true
    }
  },

  computed: {
    // 已上传文件数量
    successNum() {
      const arr = this.fileList.filter((item) => item.status === 'success')
      return arr.length || 0
    },

    // 是否全部上传成功
    isAllSuccess() {
      return this.successNum === this.fileList.length
    },

    // 以上传文件大小
    totalSize() {
      const arr = this.fileList.filter((item) => item.status === 'success')
      return arr.reduce((prev, curr) => {
        return prev + curr.size
      }, 0)
    },

    // 失败文件列表
    errorFileList() {
      const arr = this.fileList.filter((item) => item.status === 'error')
      return arr.length || 0
    }
  },

  methods: {
    // 上传文件
    uploadFile(fileList) {
      fileList.forEach((uploadItem) => {
        const { index, uploadFn, status, progressValue } = uploadItem

        if (status === 'ready' && progressValue === 1) {
          // 开启进度条
          this.$emit('updateFileStatus', { status: 'pending', index })

          uploadFn()
            .then(() => {
              this.$emit('updateFileStatus', { status: 'success', index })
            })

            .catch(({ message }) => {
              this.$emit('updateFileStatus', { status: 'error', index, message })
            })
        }
      })
    },

    takeBack() {
      this.takeShow = !this.takeShow
    },

    toSizeFile(size) {
      return formatBytes(size)
    },

    // 关闭上传进度弹窗
    close() {
      this.$emit('close')
    }
  },

  watch: {
    uploadFileList: {
      handler(list) {
        if (list && list.length > 0) {
          this.progressShow = true

          this.fileList = list.map((item) => {
            return {
              name: item.file.name,
              size: item.file.size,
              type: item.file.size,
              status: item.status,
              errorMsg: item.errorMsg || '',
              progressValue: item.progressValue
            }
          })

          this.uploadFile(list)
        } else {
          this.progressShow = false

          this.fileList = []
        }
      },
      deep: true
    }
  },

  components: { oaProgress: () => import('@/components/form-common/oa-progress') }
}
</script>
<style scoped lang="scss">
.box {
  max-height: 500px;
  overflow-y: auto;
  width: 407px;
  position: fixed;
  right: 37px;
  bottom: 54px;
  background: #fff;
  border: 1px solid #dcdfe6;
  border-radius: 6px;
  font-family: PingFang SC, PingFang SC;
  ::v-deep .el-collapse {
    border: none;

    border-radius: 6px;
  }
  .mr6 {
    margin-right: 6px;
  }
  .title {
    margin: 20px;
    font-weight: 500;
    font-size: 15px;
    color: #303133;
    .el-icon-arrow-down {
      margin-right: 10px;
    }
    .el-icon-arrow-up {
      margin-right: 10px;
    }
    .img {
      width: 19px;
      height: 19px;
    }
  }
  .tips-box {
    width: 100%;
    height: 32px;
    background: #f7f7f7;
    padding: 0 19px;
    line-height: 32px;
    font-weight: 400;
    font-size: 13px;
    color: #303133;
  }
  .item {
    margin: 20px;
  }
}
.percent {
  font-family: PingFang SC, PingFang SC;
  font-weight: 400;
  font-size: 11px;
  color: #909399;
  margin-right: 5px;
}
.file {
  width: 19px;
  display: flex;
  width: 19px;
  height: 23px;
  background: url('../../../../assets/images/cloud/file-box.png') no-repeat;
  background-size: 19px 23px;
  color: #fff !important;
  justify-content: center;
  line-height: 23px;
  font-size: 12px;
  margin-right: 10px;
}
.file-item {
  display: flex;
  flex-direction: column;
  // justify-content: center;
  font-family: PingFang SC, PingFang SC;
  font-weight: 400;
  .file-name {
    width: 230px;
    font-size: 13px;
    color: #303133;
    line-height: 18px;
  }
  .file-size {
    font-size: 12px;
    color: #909399;
    line-height: 17px;
  }
}
.el-icon-check {
  font-size: 18px;
  color: #1890ff;
  font-weight: 500;
}
.el-icon-warning {
  color: #ed4014;
  font-size: 16px;
}
</style>
