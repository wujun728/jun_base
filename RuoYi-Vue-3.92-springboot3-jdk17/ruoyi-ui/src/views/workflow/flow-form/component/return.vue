<template>
  <el-dialog :title="returnTitle" :visible.sync="returnOpen" width="40%" append-to-body>
    <el-divider />
    <el-form ref="taskForm" :model="taskForm" label-width="80px">
      <el-form-item label="退回节点" prop="targetKey">
        <el-radio-group v-model="taskForm.targetKey">
          <el-radio v-for="item in returnTaskList" :key="item.id" :label="item.id" size="mini">{{ item.name }}</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="returnOpen = false">取 消</el-button>
      <el-button type="primary" @click="taskReturn">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { returnList } from "@/api/workflow/task";
import { commonSubmit, checkReturnCondition } from "@/api/workflow/process";

export default {
  name: "return",
  props: {
    taskForm: {
      type: Object,
      default: () => {},
    },
    returnTitle: {
      //退回弹框标题
      type: String,
      default: "",
    },
  },
  data() {
    return {
      returnTaskList: [], // 回退列表数据
      returnOpen: false, //是否打开退回弹框
      targetKey: "", //退回节点key
    };
  },
  methods: {
    /** 提交退回任务 */
    taskReturn() {
      if (!this.taskForm.targetKey) {
        this.$modal.msgError("请选择退回节点！");
        return;
      }
      this.$refs["taskForm"].validate((valid) => {
        if (valid) {
          let returnParams = {
            operateType: "202", //操作类型，提交、驳回、退回等
            flowTask: this.taskForm,
          };
          checkReturnCondition(this.taskForm).then((res) => {
            if (res.data) {
              commonSubmit(returnParams).then((res) => {
                this.$modal.msgSuccess(res.msg);
                this.goBack();
              });
            }
          });
        }
      });
    },
    /** 可退回任务列表 */
    handleReturn() {
      this.returnOpen = true;
      returnList(this.taskForm).then((res) => {
        this.returnTaskList = res.data;
        this.taskForm.variables = null;
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


