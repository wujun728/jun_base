<template>
  <!--减签任务列表 -->
  <el-dialog :title="deleteMultiTaskTitle" :visible.sync="deleteMultiTaskOpen" width="53%" append-to-body>
    <el-table
      v-loading="loading"
      ref="deleteMultiTable"
      element-loading-text="正在加载中..."
      element-loading-spinner="el-icon-loading"
      :data="deleteMultiTaskList"
      row-key="taskId"
      key="deleteMultiTable"
      @row-click="clickRow"
      @selection-change="handleSelectDeleteMultiTaskChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="流程环节" align="left" prop="taskName" width="300" show-overflow-tooltip />
      <el-table-column label="部门" align="left" prop="assigneeDeptName" width="200" show-overflow-tooltip />
      <el-table-column label="办理人" align="left" prop="assigneeName" width="140" />
    </el-table>
    <span slot="footer" class="dialog-footer">
      <el-button @click="deleteMultiTaskOpen = false">取 消</el-button>
      <el-button type="primary" @click="deleteMultiTask">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { deleteMultiTask, getDeleteMultiTasks } from "@/api/workflow/process";

export default {
  name: "deleteMulti",
  props: {
    taskForm: {
      type: Object,
      default: () => {},
    },
  },
  data() {
    return {
      loading: true, // 遮罩层
      deleteMultiTaskOpen: false, //是否打开弹框
      deleteMultiTaskList: [], //减签任务列表
      deleteMultiTaskTitle: "减签任务列表",
      deleteMultiExecuionIds: [], // 子执行流ID列表
      deleteMultiAssigneeIds: [], // 子执行流处理人ID列表
      deleteMultiTaskSelection: [], //选择减签任务列表
    };
  },
  methods: {
    // 初始化
    init() {
      this.deleteMultiTaskOpen = true;
      let params = {
        procInsId: this.taskForm.procInsId,
        taskId: this.taskForm.taskId,
      };
      getDeleteMultiTasks(params)
        .then((res) => {
          this.loading = false;
          if (res.code == 200 && res.data) {
            this.deleteMultiTaskList = res.data;
          }
        })
        .catch((ex) => {
          this.loading = false;
        });
    },
    toggleSelection(rows) {
      if (rows) {
        rows.forEach((row) => {
          this.$refs.deleteMultiTable.toggleRowSelection(row);
        });
      } else {
        this.$refs.deleteMultiTable.clearSelection();
      }
    },
    // 选择减签任务
    handleSelectDeleteMultiTaskChange(selection) {
      this.deleteMultiTaskSelection = selection;
      // 单选
      if (selection.length > 1) {
        this.$refs.deleteMultiTable.clearSelection();
        this.$refs.deleteMultiTable.toggleRowSelection(selection);
        selection.forEach((selectRow) => {
          this.deleteMultiExecuionIds.push(selectRow.executionId);
          this.deleteMultiAssigneeIds.push(selectRow.assignee);
        });
      } else if (selection.length === 1) {
        let selectRow = selection.pop();
        this.deleteMultiExecuionIds.push(selectRow.executionId);
        this.deleteMultiAssigneeIds.push(selectRow.assignee);
      }
    },
    // 点击一行时选中
    clickRow(row) {
      // 单选选中行
      if (this.deleteMultiTaskSelection[0] == row) {
        // 取消
        this.deleteMultiTaskSelection = [];
        this.$refs.deleteMultiTable.clearSelection();
      } else {
        // 选择
        this.deleteMultiTaskSelection = [row];
        this.$refs.deleteMultiTable.clearSelection();
        this.$refs.deleteMultiTable.toggleRowSelection(row, true);
      }
    },
    // 减签
    deleteMultiTask() {
      if (!this.deleteMultiExecuionIds || this.deleteMultiExecuionIds.length === 0) {
        this.$message.error("请选择减签的任务！");
        return;
      }
      this.$confirm("确认减签任务吗？", "提示", {
        confirmButtonText: "确 认",
        cancelButtonText: "取 消",
      }).then(() => {
        let data = {
          taskId: this.taskForm.taskId,
          procInsId: this.taskForm.procInsId,
          currentChildExecutionIds: this.deleteMultiExecuionIds.join(","),
          assignee: this.deleteMultiAssigneeIds.join(","),
        };
        deleteMultiTask(data).then((res) => {
          if (res.code === 200) {
            this.deleteMultiTaskOpen = false;
            this.deleteMultiExecuionIds = [];
            this.deleteMultiAssigneeIds = [];
            this.deleteMultiTaskSelection = [];
            this.$message.success("减签成功！");
          }
        });
      });
    },
  },
};
</script>
