<template>
  <!--  在线模版选择弹窗-->
  <div>
    <el-dialog
      :append-to-body="true"
      :before-close="handleClose"
      :fullscreen="true"
      :title="fromData.title"
      :visible.sync="dialogVisible"
      :width="fromData.width"
      top="0"
    >
      <iframe :src="fileUrl" class="iframe"></iframe>
    </el-dialog>
  </div>
</template>

<script>
import { tempDownloadApi } from '@/api/enterprise'
import file from '@/utils/file'
import Vue from 'vue'
import { getAccount, toSrcFn } from '@/utils/format'
Vue.use(file)
export default {
  name: 'FileTemp',
  components: {},
  props: {
    fromData: {
      type: Object,
      default: () => {
        return {}
      }
    },
    wps_type: {
      type: String | Number,
      default: '0'
    }
  },
  data() {
    return {
      dialogVisible: false,
      name: '',
      fileUrl: ''
    }
  },
  watch: {},
  mounted() {
    let uniquedId = JSON.parse(localStorage.getItem('enterprise')).uniqued
    let accountNum = getAccount(JSON.parse(localStorage.getItem('userInfo')).phone, uniquedId)
    let type = 'iframe'
    let host = window.location.host
    this.fileUrl = `https://www.tuoluojiang.com/files?account=${accountNum}&uniqued=${uniquedId}&type=${type}&host=${host}`
    this.getLoad()
  },
  methods: {
    getLoad() {
      // 在父页面添加事件监听器来接收消息
      window.addEventListener('message', async (event) => {
        // 处理接收到的数据
        if (event.data && typeof event.data === 'number' && this.fromData.fid) {
          let data = {
            id: this.fromData.id ? this.fromData.id : this.fromData.fid,
            temp_id: event.data
          }
          await tempDownloadApi(this.fromData.fid, data)
          this.handleClose()
          this.handleEmit()
        }
      })
    },

    handleClose() {
      this.dialogVisible = false
    },
    handleOpen() {
      this.dialogVisible = true
    },

    src(e) {
      return toSrcFn(e)
    },

    handleEmit() {
      this.$emit('handleTemplate')
    }
  }
}
</script>

<style lang="scss" scoped>
.iframe {
  width: 100%;
  height: 100%;
  border: none;
}
::v-deep .el-dialog__body {
  height: calc(100% - 55px);
  padding: 0 !important;
}
::v-deep .el-input--small {
  width: 50%;
}
</style>
