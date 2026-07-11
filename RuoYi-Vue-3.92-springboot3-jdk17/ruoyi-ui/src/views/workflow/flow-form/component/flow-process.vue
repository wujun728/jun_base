<template>
  <!-- 办理过程 -->
  <div class="mt5">
    <el-tabs v-model="activeName" type="border-card" class="custom-tabs" @tab-click="handleClick">
      <el-tab-pane label="办理意见" name="cmt">
        <flow-comment ref="flowComment" :procInsId="procInsId" @change-flag="changeCmtFlag" />
      </el-tab-pane>
      <el-tab-pane label="审批记录" name="approve">
        <flow-approve ref="flowApprove" :procInsId="procInsId" :userId="userId" @change-flag="changeRecordFlag" />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import FlowComment from "./flow-comment.vue";
import FlowApprove from "./flow-approve.vue";

export default {
  name: "FlowProcess",
  components: {
    FlowApprove,
    FlowComment,
  },
  data() {
    return {
      activeName: "cmt",
      // 加载意见状态，切换时避免重复加载
      loadCmtFlag: false,
      // 加载流程记录状态，切换时避免重复加载
      loadRecordFlag: false,
    };
  },
  props: {
    procInsId: {
      type: String,
      default: null,
    },
    userId: String,
  },
  created() {},
  methods: {
    // 初始化意见数据
    initCmtData() {
      if (!this.loadCmtFlag) {
        this.$refs.flowComment.getList();
      }
    },
    // 改变加载意见状态
    changeCmtFlag() {
      this.loadCmtFlag = true;
    },
    // 改变流程记录状态
    changeRecordFlag() {
      this.loadRecordFlag = true;
    },
    handleClick(tab, event) {
      if (tab.name === "approve" && this.loadRecordFlag == false) {
        this.$refs.flowApprove.getList();
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.custom-tabs ::v-deep .el-tabs__content {
  min-height: 550px;
  padding-top: 10px;
}
::v-deep .el-tabs--border-card > .el-tabs__header .el-tabs__item {
  font-size: 14px;
}
::v-deep .el-tabs__header {
  background-color: #fff;
  font-size: 14px;
}
::v-deep .el-tabs--border-card > .el-tabs__header .el-tabs__item.is-active {
  color: #409eff;
  font-size: 15px;
}
::v-deep .el-form-item {
  margin-bottom: 12px;
}
.el-table__body {
  transition: none !important; /* 禁用动画过渡 */
}
</style>