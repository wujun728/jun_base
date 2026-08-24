<!-- 转申加签 -->
<template>
  <div>
    <el-dialog
      :title="status == 0 ? '加签' : '转审'"
      :visible.sync="visible"
      width="600px"
      :before-close="handleClose"
      :append-to-body="true"
    >
      <div class="mt10">
        <el-form :model="formData" ref="form" label-width="auto">
          <el-form-item :label="status == 0 ? '加签审核人：' : '转审人：'" prop="user_id">
            <select-member
              ref="selectMember"
              :value="userList"
              style="width: 100%"
              @getSelectList="getSelectList"
              :only-one="status == 0 ? false : true"
            >
              <template v-slot:custom>
                <div class="flex-user">
                  <div
                    class="btn"
                    @click="handlesuperiorOpen"
                    v-if="status == 0 || (status == 1 && userList.length == 0)"
                  >
                    <span class="el-icon-plus"></span>
                    添加
                  </div>

                  <div v-for="(item, index) in userList" :key="index" class="user">
                    <img :src="item ? item.avatar : item.avatar" alt="" class="img" />
                    <span> {{ item.name || '--' }}</span>
                    <span class="el-icon-close" @click="userDelete(item)"></span>
                  </div>
                </div>
              </template>
            </select-member>
          </el-form-item>
          <el-form-item label="加签方式：" prop="type" v-if="status == 0">
            <div>
              <el-radio-group v-model="formData.types">
                <el-radio :label="1">在我之前</el-radio>
                <el-radio :label="0">在我之后</el-radio>
              </el-radio-group>
              <div class="tips" v-if="formData.types == 1">加签后，流程先经过加签审批人，再由我审批。</div>
              <div class="tips" v-if="formData.types == 0">在我之后加签，即表示同意该申请并增加审核人员。</div>
            </div>
          </el-form-item>
          <el-form-item label="多人审核方式：" prop="examineType" v-if="status == 0 && userList.length > 1">
            <el-radio-group v-model="formData.examine_mode">
              <el-radio :label="1">或签</el-radio>
              <el-radio :label="2">会签</el-radio>
              <el-radio :label="3">依次审批</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item :label="status == 0 ? '加签意见：' : '转审意见：'" prop="opinion">
            <el-input
              type="textarea"
              v-model="formData.info"
              :rows="3"
              size="small"
              :placeholder="status == 0 ? '请输入加签意见' : '请输入转审意见'"
            />
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <div class="footer-tips">
          {{ status == 0 && !formData.types ? '点击按钮，即表示同意该申请并增加审批人员' : '' }}
        </div>
        <el-button size="small" type="primary" @click="submit"> 确认{{ status == 0 ? '加签' : '转审' }} </el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { extractArrayIds } from '@/libs/public'
export default {
  name: '',
  components: {
    selectMember: () => import('@/components/form-common/select-member')
  },
  props: {},
  data() {
    return {
      visible: false,
      formData: {
        user: [],
        types: 1,
        examine_mode: 1,
        info: ''
      },
      status: 0,
      userList: []
    }
  },

  methods: {
    // 添加参与人
    handlesuperiorOpen() {
      this.$refs.selectMember.handlePopoverShow()
    },
    // 获取人员回调
    getSelectList(data) {
      this.userList = data
      this.formData.user = extractArrayIds(data, 'value')
    },
    openBox(val) {
      this.status = val
      this.visible = true
    },
    submit() {
      let str = ''
      if (this.status == 0) {
        str = '请选择加签审核人'
      } else {
        str = '请选择转审审核人'
      }
      if (this.userList.length == 0) return this.$message.error(str)
      this.$emit('submit', this.formData, this.status)
    },
    handleClose() {
      this.visible = false
      this.userList = []
      this.formData = {
        user: [],
        types: 1,
        examine_mode: 1,
        info: ''
      }
    },
    // 删除参与人
    userDelete(val) {
      this.userList = this.userList.filter((item) => {
        return item.value != val.value
      })
      this.formData.user = extractArrayIds(this.userList, 'value')
    }
  }
}
</script>
<style scoped lang="scss">
.dialog-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.footer-tips {
  font-family: PingFang SC, PingFang SC;
  font-weight: 400;
  font-size: 13px;
  color: #606266;
}

.flex-user {
  display: flex;
  flex-wrap: wrap;
  // margin-bottom: 20px;
  .user {
    padding: 4px 12px;
    height: 32px;
    text-align: center;
    line-height: 22px;
    font-family: PingFang SC, PingFang SC;
    font-weight: 400;
    font-size: 13px;
    color: #303133;
    background: #f7f7f7;
    margin-left: 10px;
    border-radius: 4px;
    margin-bottom: 10px;
    .img {
      width: 24px;
      height: 24px;
      border-radius: 50%;
    }
  }
}
.tips {
  font-family: PingFang SC, PingFang SC;
  font-weight: 400;
  font-size: 13px;
  color: #909399;
}

::v-deep.textarea .el-textarea__inner {
  font-size: 12px;
}
.btn {
  font-family: PingFang SC, PingFang SC;
  font-weight: 400;
  font-size: 13px;
  width: 73px;
  height: 32px;
  line-height: 22px;
  background: rgba(24, 144, 255, 0.07);
  border-radius: 4px;
  color: #1890ff;
}
</style>
