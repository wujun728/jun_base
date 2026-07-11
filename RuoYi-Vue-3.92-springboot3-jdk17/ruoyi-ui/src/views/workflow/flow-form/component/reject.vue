<template>
  <el-dialog :title="rejectTitle" :visible.sync="rejectOpen" width="40%" append-to-body>
    <el-divider />
    <div>
      <span>流程将回退到上一个环节，是否确定？</span>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="rejectOpen = false">取 消</el-button>
      <el-button type="primary" @click="taskReject">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { commonSubmit, checkRejectCondition } from "@/api/workflow/process";
import { StrUtil } from "@/utils/StrUtil";

export default {
  name: "reject",
  props: {
    taskForm: {
      type: Object,
      default: () => {},
    },
    rejectTitle: {
      //驳回弹框标题
      type: String,
      default: "",
    },
  },
  data() {
    return {
      rejectOpen: false, //是否打开驳回弹框
    };
  },
  methods: {
    /** 驳回任务 */
    taskReject() {
      if (StrUtil.isBlank(this.taskForm.comment)) {
        this.$message.error("请填写审批意见");
        return;
      }
      let rejectParams = {
        operateType: "201", //操作类型，提交、驳回、退回等
        flowTask: this.taskForm,
      };
      checkRejectCondition(this.taskForm).then((res) => {
        if (res.data) {
          commonSubmit(rejectParams).then((res) => {
            this.$modal.msgSuccess(res.msg);
            this.goBack();
          });
        }
      });
    },
    /** 返回页面 */
    goBack() {
      // 关闭当前标签页并返回上个页面
      const obj = { path: "/my/todo", query: { t: Date.now() } };
      this.$tab.closeOpenPage(obj);
    },
  },
};
</script>
