<template>
  <div class="v-height-flag">
    <!-- 绩效考核页面 -->
    <el-card v-if="isHeader" class="mb14">
      <div class="flex-box" v-if="isHeader">
        <div class="header">
          <div class="left">
            <i class="el-icon-arrow-left" @click="clickReturn"></i>
            <div class="invoice-logo"><i class="icon iconfont iconchuanjainkaohe"></i></div>
          </div>
          <div class="right">
            <span class="title">{{
              rowData && rowData.test && rowData.test.name ? rowData.test.name + '的考核' : '绩效考核'
            }}</span>
            <div class="process-left-list">
              <div v-for="(item, index) in tableData" :key="index" class="process-list">
                <span class="text" :class="index == processIndex ? 'active' : ''" round>{{ item.name }}</span>
                <i v-if="index != tableData.length - 1" class="el-icon-arrow-right" />
              </div>
            </div>
          </div>
        </div>
        <div class="process-right">
          <el-button v-if="isShow && strType !== 'check'" size="small" @click="onlySaveSubmit">仅保存</el-button>
          <el-button v-if="isShow && strType !== 'check'" type="primary" size="small" @click="addSaveSubmit">{{
            isShowButton
          }}</el-button>
        </div>
      </div>
    </el-card>
    <el-card :body-style="{ padding: '0 20px 20px 20px' }">
      <div v-if="isTips">
        <div
          v-if="processIndex == 3"
          v-show="appeal.user_id != undefined && assessInfo.verify && assessInfo.verify.id == userId"
          class="tips-box mb15 tActive"
        >
          <i class="el-icon-close" @click="closeTips" />
          <p>绩效申诉: {{ appeal.content }}</p>
        </div>
        <div v-else class="mb14">
          <el-alert v-if="tipsBoxContent" class="cr-alert" title="" :closable="true" type="info" :show-icon="false">
            <template slot="title">
              <p>{{ tipsBoxContent }}</p>
            </template>
          </el-alert>
        </div>
      </div>

      <goalSetting
        v-if="processIndex == 0 || processIndex == 5"
        :id="id"
        ref="goalSetting"
        :rowData="rowData"
        :strType="strType"
        :add-button="addButton"
        @handleGoal="handleGoal"
      />

      <!-- 上级评价 -->
      <execution
        v-if="[1, 2, 3, 4].includes(processIndex)"
        :id="id"
        ref="execution"
        :strType="strType"
        :reply="reply"
        :isShow="isShowProps"
        :processIndex="processIndex"
        :appeal="appeal"
        :marks="mark"
        @saveOk="saveOk"
        @handleExecution="handleExecution"
      />
    </el-card>
  </div>
</template>

<script>
import { userAssessExplain, userAssessInfo } from '@/api/user'
export default {
  name: 'AuditProcess',
  components: {
    goalSetting: () => import('./goalSetting'),
    execution: () => import('./execution')
  },
  props: {
    processIndex: {
      type: Number | String,
      default: 0
    },
    id: {
      type: Number | String,
      default: 0
    },
    strType: {
      type: String,
      default: ''
    },
    isShowProps: {
      type: Number | String | Boolean,
      default: false
    },
    isTips: {
      type: Boolean,
      default: true
    },
    isHeader: {
      type: Boolean,
      default: true
    },
    addButton: {
      type: Boolean,
      default: false
    },
    rowData: {
      type: [Object, Array],
      default: () => {}
    }
  },
  data() {
    return {
      tableData: [
        { id: 1, name: this.$t('access.goalsetting') },
        { id: 2, name: this.$t('access.executionphase') },
        { id: 3, name: this.$t('access.higherevaluation') },
        { id: 4, name: this.$t('access.performancereview') },
        { id: 5, name: this.$t('access.end') }
      ],
      from: {
        return: 0
      },
      resultData: {},
      userId: '',
      tipsBoxContent: '',
      reply: [],
      appeal: {},
      mark: {},
      assessInfo: [],
      testAccess: false,
      isShow: false,
      isShowButton: ''
    }
  },
  computed: {
    lang: function () {
      return this.$store.getters.lang
    }
  },
  watch: {
    lang() {
      this.setOptions()
    },
    id() {
      this.getAssessExplain()
      this.getTableData()
    }
  },
  mounted() {
    let info = JSON.parse(localStorage.getItem('userInfo'))
    this.userId = info.userId
    if (this.addButton) this.showSaveSubmit()
    if (this.id > 0) {
      this.getAssessExplain()
      this.getTableData()
    }
  },
  methods: {
    // 返回
    goBack() {
      this.$router.go(-1)
    },
    setOptions() {
      this.tableData = [
        { id: 1, name: this.$t('access.goalsetting') },
        { id: 2, name: this.$t('access.executionphase') },
        { id: 3, name: this.$t('access.higherevaluation') },
        { id: 4, name: this.$t('access.performancereview') },
        { id: 5, name: this.$t('access.end') }
      ]
    },
    closeTips() {
      this.isTips = false
    },
    clickReturn() {
      this.from.return = 1
      this.$emit('auditProcess', this.from)
    },
    handleGoal() {
      this.clickReturn()
    },
    saveOk() {
      this.$emit('saveOk')
    },
    handleExecution() {
      // 自评提交成功后
      this.clickReturn()
      // this.processIndex = 2
      this.getAssessExplain()
    },
    async getTableData() {
      const result = await userAssessInfo(this.id)
      this.resultData = result
      this.assessInfo = result.data.assessInfo
      this.showSaveSubmit()
      this.$store.state.user.userInfo.uid === this.assessInfo.check.uid
        ? (this.testAccess = false)
        : (this.testAccess = true)
    },
    async getAssessExplain() {
      const result = await userAssessExplain(this.id)
      this.tipsBoxContent = result.data.explain
      this.reply = result.data.reply
      this.appeal = result.data.appeal.user_id ? result.data.appeal : {}
      this.mark = result.data.mark.content ? result.data.mark : {}
    },
    handleSuperior() {
      this.clickReturn()
      this.getAssessExplain()
    },
    onlySaveSubmit() {
      if (this.processIndex === 0) {
        // 目标制定
        if (this.addButton) {
          this.$refs.goalSetting.addPreserve(1, 'only')
        } else {
          this.$refs.goalSetting.editPreserve(1, 'only')
        }
      } else {
        this.$refs.execution.addPreserve('only')
      }
    },
    addSaveSubmit() {
      if (this.processIndex === 0) {
        // 目标制定
        if (this.addButton) {
          this.$refs.goalSetting.addPreserve(1)
        } else {
          this.$refs.goalSetting.editPreserve(1)
        }
      } else {
        this.$refs.execution.addPreserve()
      }
    },
    showSaveSubmit() {
      this.isShowButton = '保存提交'
      if (this.processIndex === 0) {
        if (this.addButton) {
          this.isShowButton = this.$t('public.enable')
          this.isShow = false
        } else {
          if (this.assessInfo.is_show !== 1) {
            this.isShowButton = this.$t('public.enable')
            this.isShow = false
          } else {
            this.isShow = false
          }
        }
      } else if (this.processIndex === 1) {
        this.isShow = true
      } else if (this.processIndex === 2) {
        this.isShow = true
      } else if (this.processIndex === 3) {
        this.isShow = true
      } else if (this.processIndex === 4) {
        this.isShow = true
      } else {
        this.isShow = false
        this.isShowButton = this.$t('public.enable')
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.bgc-f7 {
  background-color: #f7f7f7;
  border-color: transparent;
  color: #000000;
}
.flex-box {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-right: 20px;
}
.el-icon-arrow-left {
  cursor: pointer;
  font-size: 19px;
  color: #606266;
  margin-right: 8px;
}
.header {
  width: 100%;
  height: 70px;
  display: flex;
  .left {
    padding: 11px 0 11px 20px;
    display: flex;
    align-items: center;

    .invoice-logo {
      width: 48px;
      height: 48px;
      background: #1890ff;
      border-radius: 4px;
      line-height: 48px;
      text-align: center;
      .iconchuanjainkaohe {
        font-size: 24px;
        color: #fff;
      }
    }
  }
  .right {
    padding: 11px 0 11px 0px;
    margin-left: 13px;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    .title {
      font-family: PingFang SC, PingFang SC;
      font-weight: 500;
      font-size: 17px;
      color: #303133;
      text-align: left;
      font-style: normal;
      text-transform: none;
    }
    .tab {
      ::v-deep .el-tabs__nav-wrap::after {
        height: 0;
      }
      ::v-deep .el-tabs__item {
        line-height: 37px;
        font-family: PingFang SC, PingFang SC;
        font-weight: 400;
        font-size: 13px;
        color: #606266;
      }
      ::v-deep .el-tabs__item.is-active {
        color: #1890ff;
      }
    }
  }
}
::v-deep .el-tabs__header {
  margin-bottom: 0;
}
::v-deep .el-tabs__nav-wrap::after {
  display: none;
}
.right-box {
  position: absolute;
  right: 0;
  top: 50%;
  width: 300px;
  transform: translateY(-50%);
}
.p8 {
  padding: 8px 16px;
  font-size: 13px;
}
.tips-box {
  padding: 0 20px;
  background: #fffbe6;
  border-radius: 2px;
  border: 1px solid #ffe58f;
  font-size: 13px;
  color: rgba(0, 0, 0, 0.65);
  padding-top: 10px;
  position: relative;
  i {
    position: absolute;
    font-size: 14px;
    color: #000000;
    top: 12px;
    right: 6px;
    cursor: pointer;
  }
}
.tips-box.tActive {
  background-color: #fffafa;
  border-color: #f5c0c0;
}
.process-con {
  display: flex;
  align-items: center;
}
.process-left-list {
  display: flex;
  width: 80%;
  align-items: center;
  .process-list {
    display: flex;
    align-items: center;
    flex-wrap: nowrap;
    ::v-deep .el-button {
      cursor: default;
    }
  }
  .text {
    white-space: nowrap;
    color: #606266;
  }
  .active {
    color: #1890ff;
  }
  .el-button--medium {
    padding: 8px 0;
    width: 100px;
    font-size: 13px;
    cursor: auto;
  }
  i {
    font-size: 12px;
    color: #999999;
    margin: 0 10px;
    background: linear-gradient(-180deg, #dedede, #999999);
    background: -webkit-linear-gradient(-180deg, #dedede, #999999);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
  }
}
.process-right {
  width: 20%;
  text-align: right;
}
.v-height-flag .mb14 ::v-deep .el-card__body {
  padding: 0px;
}
</style>
