<template>
  <div>
    <el-dialog
      class="message-details"
      :visible.sync="dialogVisible"
      :append-to-body="true"
      :width="messageData.width"
      :before-close="beforeClose"
    >
      <template v-if="messageData.data">
        <div class="details" :class="messageData.data.button_template ? '' : 'mb14'">
          <el-row>
            <el-col class="left">
              <img v-if="messageData.data.image" class="image" :src="messageData.data.image" alt="" />
              <div v-else class="left-icon">
                <i class="iconfont iconxiaoxi"></i>
              </div>
            </el-col>
            <el-col class="right">
              <div class="top">
                <el-tag size="small" effect="dark">{{ messageData.data.cate_name }}</el-tag>
              </div>
              <div
                class="pointer"
                v-if="messageData.data.buttons.length > 0"
                @click="handleConfirm(messageData.data.buttons[0])"
              >
                <div class="mt10 content over-text">{{ messageData.data.title }}</div>
                <div class="mt10 bottom">{{ messageData.data.message }}</div>
              </div>
              <div class="pointer" v-else>
                <div class="mt10 content over-text">{{ messageData.data.title }}</div>
                <div class="mt10 bottom">{{ messageData.data.message }}</div>
              </div>
            </el-col>
          </el-row>
        </div>
        <div slot="footer" class="dialog-footer" v-if="messageData.data.buttons.length > 0">
          <template v-if="messageData.data.cate_name !== '考勤'">
            <el-button
              type="primary"
              size="small"
              v-for="(item, index) in messageData.data.buttons"
              :disabled="selectedType.includes(item.action)"
              @click="handleConfirm(item)"
              >{{ item.title }}</el-button
            >
          </template>
        </div>
      </template>
    </el-dialog>
    <message-handle-popup ref="messageHandlePopup" :detail="messageData.data"></message-handle-popup>
  </div>
</template>

<script>
import noticeHandle from '@/libs/noticeHandle'
import { noticeMessageReadApi } from '@/api/user'
import { messageListApi } from '@/api/public'
import { roterPre } from '@/settings'
import messageHandlePopup from '@/components/common/messageHandlePopup'
export default {
  name: 'MessageDetails',
  components: {
    messageHandlePopup
  },
  props: {
    messageData: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  data() {
    return {
      dialogVisible: false,
      icon: 2,
      roterPre: roterPre,
      selectedType: ['delete', 'recall']
    }
  },
  watch: {
    messageData: {
      handler(nVal) {
        if (nVal.data.is_read === 0) {
          this.batchMessageRead(1, { ids: [nVal.data.id] })
        }
      },
      deep: true
    }
  },
  methods: {
    handleClose() {
      noticeHandle(this.messageData.data, 0)
      this.handleEmit()
      this.dialogVisible = false
    },
    beforeClose() {
      this.dialogVisible = false
    },
    handleOpen() {
      this.dialogVisible = true
    },
    handleConfirm(item) {
      this.dialogVisible = false
      if (this.selectedType.includes(item.action)) return false
      this.$refs.messageHandlePopup.openMessage(item, this.messageData.data)
    },
    noticeLink() {
      if (this.messageData.data.is_read === 0) {
        this.batchMessageRead(1, { ids: [this.messageData.data.id] })
      }
    },
    // 批量标记未已读
    batchMessageRead(status, data) {
      noticeMessageReadApi(status, data)
        .then((res) => {
          this.$emit('isOk')
          this.getMessage()
          // this.$message.success(res.message);
        })
        .catch((error) => {
          // this.$message.error(error.message);
        })
    },
    handleEmit() {
      this.$emit('isOk')
    },
    // 消息数量
    getMessage() {
      messageListApi({ page: 1, limit: 5 }).then((res) => {
        const num = res.data.messageNum ? res.data.messageNum : 0
        this.$store.commit('user/SET_MESSAGE', num)
      })
    }
  }
}
</script>

<style scoped lang="scss">
.message-details {
  ::v-deep .el-dialog__header {
    padding: 20px;
  }
  ::v-deep .el-dialog__headerbtn {
    position: absolute;
    top: 14px;
  }
}
.details {
  .left {
    width: 72px;
    .image {
      width: 100%;
      height: 72px;
      display: inline-block;
    }
    .left-icon {
      width: 100%;
      height: 72px;
      text-align: center;
      line-height: 72px;
      border-radius: 50%;
      background-color: #e7f3ff;
      i {
        font-size: 36px;
        color: #1890ff;
      }
    }
  }
  .right {
    width: calc(100% - 72px);
    padding-left: 14px;
    ::v-deep .el-tag {
      border-radius: 2px;
    }
    .content {
      font-size: 14px;
      font-weight: 600;
      color: #303133;
    }
    .bottom {
      font-size: 14px;
      color: #606266;
      line-height: 1.5;
    }
  }
}
</style>
