<!-- @FileDescription: 审批详情侧滑页面 -->
<template>
  <div>
    <el-drawer
      size="628px"
      :visible.sync="drawer"
      :direction="direction"
      :append-to-body="true"
      :before-close="handleClose"
    >
      <div slot="title">
        <div v-if="examineData.card" class="headerBox acea-row row-middle row-between">
          <div class="acea-row row-middle">
            <div class="portrait mr10">
              <img v-if="judge(examineData)" :src="examineData.card.avatar" alt="" />
              <img v-else src="../../../../assets/images/portrait.png" alt="" />
            </div>
            <div class="nameBox">
              <span class="st1"
                >{{ examineData.card.name }}的{{ examineData.approve ? examineData.approve.name : '请假' }}</span
              >
              <span class="st2" :class="getColor(examineData.status)">
                {{ $func.getExamineStatus(examineData.status, examineData) }}
              </span>
            </div>
          </div>

          <div class="flex-center">
            <!-- <template v-if="examineData.status == 0 && examineData.verify_status === 0"> -->
            <el-button v-if="examineData.verify_status === 0" type="danger" size="small" @click="onAgree(0)"
              >拒绝</el-button
            >
            <el-button v-if="examineData.verify_status === 0" type="primary" size="small" @click="onAgree(1)"
              >同意</el-button
            >

            <el-button
              v-if="isRevokeFn(examineData) && userId == examineData.card.id"
              size="small"
              class="ml10"
              @click="handleRefuse()"
              >撤销</el-button
            >

            <el-dropdown v-if="examineData.verify_status === 0 && examineData.approve.types !== 11">
              <span class="iconfont icongengduo2 pointer ml10"></span>
              <el-dropdown-menu style="text-align: left">
                <template v-if="examineData.status == 0">
                  <el-dropdown-item v-if="examineData.rules.is_sign == 1" @click.native="dropdownSearch(0)"
                    >加签
                  </el-dropdown-item>
                  <el-dropdown-item v-if="examineData.verify_status == 0" @click.native="dropdownSearch(1)"
                    >转审
                  </el-dropdown-item>
                </template>
              </el-dropdown-menu>
            </el-dropdown>
            <!-- </template> -->

            <el-button
              type="primary"
              v-if="examineData.status == 0 && examineData.verify_status !== 0"
              size="small"
              @click="urgentProcessing()"
              >催办</el-button
            >
          </div>

          <!-- <div class="flex-center">
            <el-button
              v-if="isRevokeFn(examineData) && userId == examineData.card.id"
              size="small"
              @click="handleRefuse()"
              >撤销</el-button
            >
          </div> -->
        </div>
      </div>
      <div class="ex-content">
        <el-scrollbar style="height: 100%">
          <div class="ex-content-con" :class="isShow ? 'pb-120' : ''">
            <div class="acea-row mb20">
              <div class="shu mr10"></div>
              <div class="title">提交审批</div>
            </div>
            <!-- -----------------------------------审批内容-------------------------------- -->
            <el-form label-width="auto">
              <el-form-item v-for="(item, index) in form.rule" :key="index">
                <div class="label">
                  <span class="rule-label">{{ item.label }}:</span>
                  <div v-if="Array.isArray(item.value)" style="width: 90%">
                    <upload-list :file-list="item.value"></upload-list>
                  </div>
                  <div v-else-if="item.type === 'rich_text'" style="width: 90%">
                    <div class="rich-box" v-html="item.value"></div>
                  </div>
                  <span v-else class="rule-value">{{ item.value || '--' }}</span>
                </div>
              </el-form-item>
              <el-form-item v-if="examineData.apply_id">
                <div class="revoke" @click="revokeFn(examineData.apply_id)">
                  查看需要撤销的申请单 <span class="el-icon-arrow-right"></span>
                </div>
              </el-form-item>
            </el-form>
            <!-- -----------------------------------审批流程-------------------------------- -->
            <detail-procecss v-if="examineData.examine != 0" :examine-data="examineData"></detail-procecss>
            <message-from
              class="flex-bottom"
              v-if="examineData.examine != 0"
              :examine-data="examineData"
              @upDate="upDate"
              ref="leaveAMessage"
            ></message-from>
            <div class="from-foot-btn fix" v-if="examineData.examine != 0">
              <div class="flex" v-if="isShow">
                <img class="avatar" :src="avatar" alt="" />
                <div class="replyCon" :class="isShow ? 'border' : ''">
                  <el-input
                    ref="replyInput"
                    v-model="textarea"
                    placeholder="添加留言"
                    resize="none"
                    type="textarea"
                    class="replyText"
                  />
                  <div class="bnt">
                    <el-button size="small" @click="cancel">{{ $t('public.cancel') }}</el-button>
                    <el-button size="small" type="primary" @click="submitReply">提交</el-button>
                  </div>
                </div>
              </div>
              <template v-else>
                <div class="flex" @click="evaluate">
                  <img class="avatar" :src="avatar" alt="" />
                  <div class="replyCon-no">添加评论</div>
                </div>
              </template>
            </div>
          </div>
        </el-scrollbar>
      </div>
    </el-drawer>
    <!-- 加签转申 -->
    <addSignature ref="addSignature" @submit="submit" />
    <!-- 撤销 -->
    <oa-dialog
      ref="oaDialog"
      :fromData="fromData"
      :formConfig="formConfig"
      :formRules="formRules"
      :formDataInit="formDataInit"
      @submit="getApplyRevoke"
    ></oa-dialog>
  </div>
</template>

<script>
import func from '@/utils/preload'
import {
  approveApplyEditApi,
  approveVerifyStatusApi,
  approveApplyUrgeApi,
  approveReplyApi,
  approveSignApi,
  approveTransferApi,
  approveApplyRevokeApi
} from '@/api/business'

export default {
  name: 'DetailExamine',
  props: {
    // type=1 我审批的
    // type=0 我申请的
    type: {
      type: Number,
      default: 0
    }
  },
  components: {
    detailProcecss: () => import('./detailProcecss'),
    messageFrom: () => import('./messageFrom'),
    addSignature: () => import('./addSignature'),
    uploadList: () => import('@/components/form-common/oa-uploadList'),
    oaDialog: () => import('@/components/form-common/dialog-form'),
    city: () => import('@/components/hr/city')
  },
  data() {
    return {
      drawer: false,
      direction: 'rtl',
      fapi: null,
      formDataInit: {
        info: ''
      },
      formConfig: [
        {
          type: 'textarea',
          label: '撤销理由：',
          placeholder: '请输入撤销理由',
          key: 'info'
        }
      ],
      formRules: {
        info: [{ required: true, message: '请输入撤销理由', trigger: 'blur' }]
      },
      fromData: {
        width: '600px',
        title: '撤销',
        btnText: '确定',
        labelWidth: 'auto',
        type: ''
      },
      apply_id: 0, //查看撤销关联id
      oldApprovalId: 0, // 返回上一个审批
      rules: [],
      avatar: this.$store.state.user.userInfo.avatar,
      userId: this.$store.state.user.userInfo.id,
      form: {
        rule: [],
        formData: {},
        loaded: false,
        options: {
          submitBtn: false,
          form: {
            labelWidth: '130px'
          },
          preview: true
        }
      },
      is_revoke: true,
      id: '', // 审批id
      typeData: {},
      examineData: {},
      textarea: '',
      isShow: false
    }
  },
  beforeCreate() {
    this.$vue.prototype.$func = func
  },

  methods: {
    async submitReply() {
      if (this.textarea == '') {
        return this.$message.error('请输入留言')
      }
      await approveReplyApi({
        apply_id: this.examineData.id,
        content: this.textarea
      })
      await this.upDate(this.examineData.id)
      this.textarea = ''
      this.isShow = false
    },

    // 查看撤销订单
    revokeFn(apply_id) {
      this.apply_id = apply_id
      this.oldApprovalId = this.examineData.id
      this.approveApply(this.apply_id, this.typeData)
    },

    // 撤销按钮判断
    isRevokeFn(val) {
      if (((val.status === 1 && val.rules && val.rules.recall == 1) || val.status === 0) && !val.recall) {
        return true
      } else {
        return false
      }
    },
    // 撤销
    handleRefuse() {
      if (this.examineData.status === 0) {
        this.$modalSure(this.$t('你确定要撤销申请吗')).then(() => {
          this.getApplyRevoke()
          this.close()
        })
      } else {
        this.$refs.oaDialog.openBox()
      }
    },

    async getApplyRevoke(data) {
      await approveApplyRevokeApi(this.examineData.id, data)
      if (data) {
        this.close()
        this.$refs.oaDialog.handleClose()
      }
      this.$emit('getList')
    },

    // 加签转申
    dropdownSearch(status) {
      this.$refs.addSignature.openBox(status)
    },

    submit(data, status) {
      if (status == 0) {
        approveSignApi(this.examineData.id, data).then((res) => {
          if (res.status == 200) {
            this.approveApply(this.id, this.typeData)
            this.$refs.addSignature.handleClose()
          }
        })
      } else {
        let obj = {
          user: data.user,
          info: data.info
        }
        if (JSON.parse(localStorage.getItem('userInfo')).id == obj.user[0]) {
          this.$message.error('转审人不能选自己')

          return false
        }
        approveTransferApi(this.examineData.id, obj).then((res) => {
          if (res.status == 200) {
            this.approveApply(this.id, this.typeData)
            this.$refs.addSignature.handleClose()
          }
        })
      }
    },

    getColor(status) {
      let className = ''
      switch (status) {
        case 1:
          className = 'gray'
          break
        case -1:
          className = 'gray'
          break
        case 2:
          className = 'red'
          break
        case 0:
          className = 'yellow'
          break
        default:
          className = 'gray'
      }
      return className
    },

    // 添加评论
    evaluate() {
      this.textarea = ''
      this.isShow = true
      setTimeout(() => {
        this.$refs.replyInput.focus()
      }, 300)
    },

    cancel() {
      this.isShow = false
    },

    handleClose() {
      if (this.oldApprovalId > 0) {
        this.approveApply(this.oldApprovalId, this.typeData)
        this.oldApprovalId = 0
      } else {
        this.close()
      }
    },

    close() {
      this.drawer = false
      this.isShow = false
      this.oldApprovalId = 0
      this.apply_id = 0
    },

    judge(row) {
      return row.card.avatar.includes('https')
    },

    // 催办
    async urgentProcessing() {
      await approveApplyUrgeApi(this.examineData.id)
    },

    // 拒绝/同意
    async onAgree(n) {
      await this.$modalSure(`你确定要 ${n === 0 ? '拒绝' : '同意'} 申请人的申请吗`)
      await approveVerifyStatusApi(this.examineData.id, n)
      this.drawer = false
      this.$emit('getList')
    },

    // 打开撤销弹窗
    openBox(command, is_revoke) {
      if (is_revoke) {
        this.is_revoke = false
      }
      this.typeData = { types: 1 }
      this.form.loaded = false
      this.id = command.id
      this.approveApply(this.id, this.typeData)
    },

    upDate(id) {
      const data = { types: 1 }
      this.form.loaded = false
      this.approveApply(id, data)
    },

    // 获取表单配置
    approveApply(id, data) {
      approveApplyEditApi(id, data).then((res) => {
        this.drawer = true
        this.examineData = res.data
        let rule = []
        const formData = {}

        rule = res.data.content
        this.form.rule = rule
        this.form.formData = formData
        this.form.loaded = true
      })
    }
  }
}
</script>

<style scoped lang="scss">
::v-deep .el-drawer__header {
  height: 80px;
}
.headerBox {
  .portrait {
    width: 48px;
    height: 48px;
    border-radius: 5px;
    display: flex;
    justify-content: center;
    align-items: center;
    overflow: hidden;
    img {
      width: 100%;
      height: 100%;
    }
  }
  .nameBox {
    span {
      display: block;
    }
    .st1 {
      font-size: 15px;
      font-weight: 600;
      color: rgba(0, 0, 0, 0.85);
    }
    .st2 {
      font-size: 13px;
      margin-top: 6px;
      &.blue {
        color: #1890ff;
      }

      &.yellow {
        color: #ff9900;
      }
      &.red {
        color: #ed4014;
      }

      &.green {
        color: #00c050;
      }

      &.gray {
        color: #999999;
      }
    }
    .st-color {
      color: rgb(25, 190, 107);
    }
  }
}
.revoke {
  cursor: pointer;
  font-family: PingFang SC, PingFang SC;
  font-weight: 400;
  font-size: 13px;
  color: #1890ff;
  .el-icon-arrow-right {
    color: #c0c4cc !important;
  }
}

.icongengduo2 {
  font-size: 32px !important;
}

::v-deep .el-drawer__body {
  padding-bottom: 0;
}
.rich-box {
  padding: 6px;
  background: #f5f6f9;
  ::v-deep p {
    img {
      width: 80%;
    }
  }
}

.ex-content {
  padding: 20px 0 0 20px;
  height: 100%;
  .ex-content-con.pb-120 {
    padding-bottom: 120px;
  }
  .ex-content-con {
    padding-right: 30px;
    padding-bottom: 50px;
  }
  ::v-deep.select-item {
    margin-top: 0 !important;
  }

  ::v-deep .el-divider--horizontal {
    margin-top: 0;
    margin-bottom: 30px;
  }
  ::v-deep.el-form-item__label {
    font-size: 13px;
    color: #999999;
    font-weight: normal;
  }
  ::v-deep.el-form-item {
    margin-bottom: 8px;
  }
  ::v-deep.el-form-item__content {
    font-size: 13px;
    color: #000000;
  }
  ::v-deep .el-scrollbar__wrap {
    overflow-x: hidden;
  }
  .shu {
    width: 3px;
    height: 16px;
    background: #1890ff;
    display: inline-block;
  }
  .title {
    font-size: 14px;
    font-weight: 600;
    color: rgba(0, 0, 0, 0.85);
  }
}
.label {
  display: flex;
  align-items: center;

  .rule-label {
    display: inline-block;

    text-align: right;
    color: #606266;
    white-space: nowrap;
    padding: 0 12px 0 0;
  }
  .rule-value {
    line-height: 24px;
    width: 90%;
  }
}

.flex {
  display: flex;
  width: 100%;
  margin: 4px 20px 0px 0;

  .avatar {
    width: 38px;
    height: 38px;
    border-radius: 50%;
    margin-right: 10px;
    object-fit: cover;
  }
}

.replyCon-no {
  cursor: pointer;
  flex: 1;
  width: 100%;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  padding: 12px 10px;
  height: 40px;
  font-size: 13px;
  color: rgba(0, 0, 0, 0.25);
}
// 动画高度 从40px 到 120px
@keyframes show {
  0% {
    height: 40px;
  }
  100% {
    height: 120px;
  }
}
// 动画高度 从120px 到 40px
@keyframes hide {
  0% {
    height: 120px;
  }
  100% {
    height: 40px;
  }
}

.replyCon {
  flex: 1;
  width: 100%;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  padding: 10px 0;
  height: 120px;
  font-size: 13px;

  animation: show 0.3s ease-in-out forwards;
  .bnt {
    text-align: right;
    margin: 10px 10px 0 0;
  }
  .replyText {
    ::v-deep.el-textarea__inner {
      border: 0;
      padding: 0 10px;
    }
  }
  ::v-deep.el-textarea__inner {
    height: 55px;
  }
}
.border {
  border: 1px solid #1890ff !important;
}
.from-foot-btn {
  height: max-content;
}
</style>
