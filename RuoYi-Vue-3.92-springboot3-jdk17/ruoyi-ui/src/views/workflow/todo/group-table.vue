<template>
  <div class="table-body">
    <el-row :gutter="10" class="mb8" style="float: right" v-if="todoType == 1">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-finished" plain size="mini" @click="handleDelete">终止办理</el-button>
      </el-col>
    </el-row>

    <el-table
      v-loading="loading"
      ref="todoTable"
      :data="todoList"
      :row-class-name="tableRowClassName"
      stripe
      class="pointer"
      @row-click="handleRowClick"
      @select-all="handleSelectAll"
      @select="handleSelectionChange"
      :element-loading-text="loadingText"
      element-loading-spinner="el-icon-loading"
    >
      <el-table-column type="selection" width="55" align="center" />
      <!-- <el-table-column label="序号" type="index" align="center" width="55">
        <template slot-scope="scope">
          <span>{{(queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1}}</span>
        </template>
      </el-table-column>-->
      <el-table-column label="标题" align="left" prop="title" show-overflow-tooltip>
        <template slot-scope="scope">
          <span v-if="scope.row.urgeFlag === '1'" class="urge">【催】</span>
          <span v-if="scope.row.urgencyStatus && scope.row.urgencyStatus !== '0'" class="urgeStatus">{{ urgency(scope.row.urgencyStatus) }}</span>
          <span v-if="scope.row.handleType === '0'" class="draft">【草稿】</span>
          <span v-if="scope.row.handleType === '2'" class="hanleType">【驳回】</span>
          <span v-if="scope.row.handleType === '3'" class="hanleType">【退回】</span>
          <span v-if="['4','5'].includes(scope.row.handleType)" class="hanleType">【取回】</span>
          <span v-if="scope.row.handleType === '6'" class="hanleType">【抄送】</span>
          <span v-if="scope.row.handleType === '7'" class="hanleType">【委派】</span>
          <span v-if="scope.row.handleType === '8'" class="hanleType">【转办】</span>
          <span v-if="scope.row.handleType === '9'" class="hanleType">【加签】</span>
          <span v-if="scope.row.handleType === '10'" class="hanleType">【委托】</span>
          <span v-if="scope.row.handleType === '11'" class="hanleType">【秘书办理】</span>
          <span>{{ scope.row.title }}</span>
        </template>
      </el-table-column>
      <el-table-column label="类型" align="center" prop="templateName" width="150" show-overflow-tooltip />
      <el-table-column label="当前环节" align="center" prop="curNode" width="150" show-overflow-tooltip />
      <el-table-column label="发送人" align="center" prop="senderName" width="120" show-overflow-tooltip />
      <el-table-column label="发送时间" align="center" prop="sendTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.sendTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
  </div>
</template>

<script>
import { listTodoTable, readTodo, noRead, readCopyTodo } from "@/api/workflow/todo";
import { finishProcess } from "@/api/workflow/task";

export default {
  name: "TodoTable",
  data() {
    return {
      // 遮罩层
      loading: true,
      // loading文本内容
      loadingText: "正在加载中...",
      // 显示搜索条件
      showSearch: true,
      // 当前选中行
      selectedRow: null,
      // 全部数按钮样式控制
      allType: "primary",
      // 未读数按钮样式控制
      noReadType: "",
      // 全部待办总数
      allTotal: 0,
      // 未读总数
      totalNoRead: 0,
      // 总条数
      total: 0,
      // 待办表格数据
      todoList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {},
      groupLable: 0, // 展示模式
    };
  },
  props: {
    todoType: {
      type: Number,
      default: 1,
    },
  },
  computed: {
    // 接收人
    urgency() {
      return (val) => {
        if (!val || val === "0") return "";
        let desc = "";
        switch (val) {
          case "1":
            desc = "【加急】";
            break;
          case "2":
            desc = "【紧急】";
            break;
          case "3":
            desc = "【特急】";
            break;
        }
        return desc;
      };
    },
  },
  methods: {
    /** 行样式控制 */
    tableRowClassName({ row, rowIndex }) {
      if (row.readFlag === "0") {
        return "no-read";
      }
      return "";
    },
    handleGroupModelChange(label) {
      this.groupLable = label;
    },
    /** 行点击 */
    handleRowClick(row, column, event) {
      let pageType = "1";
      if (row.handleType === "0") {
        pageType = "0"; // 草稿
      }
      if (row.handleType === "6") {
        pageType = "2";
        readCopyTodo(row.id).then((res) => {
          if (res.code === 200) {
            this.totalNoRead -= 1;
            this.updateNoReadNum(this.totalNoRead);
          }
        });
      } else if (row.readFlag === "0") {
        // 设置已读
        row.readFlag = "1";
        readTodo(row.id).then((res) => {
          if (res.code === 200) {
            this.totalNoRead -= 1;
            this.updateNoReadNum(this.totalNoRead);
          }
        });
      }
      this.$router.push({
        path: "/workflow/flowForm/" + new Date().getTime(),
        query: {
          title: row.title,
          pageType: pageType,
          todoId: row.id,
          businessId: row.businessId,
          procInsId: row.procInstId,
          templateId: row.templateId,
          taskId: row.taskId,
          userId: row.curHandler,
          draft: row.handleType === "0" ? "1" : "0",
          type: row.type,
          handleType: row.handleType,
        },
      });
    },
    /** 更新未读数 */
    updateNoReadNum(num) {
      num = num <= 0 ? 0 : num;
      this.$emit("resetNoReadTotal", num);
    },
    /** 初始化数据 */
    getAllData(params) {
      this.queryParams = params;
      this.getList();
    },
    /** 获取未读数 */
    getNoRead() {
      noRead().then((res) => {
        this.totalNoRead = res.data;
        this.updateNoReadNum(this.totalNoRead);
      });
    },
    /** 获取未读数据 */
    getNoReadData(params) {
      this.loading = true;
      this.queryParams = params;
      listTodoTable({ ...this.queryParams, readFlag: "0" }).then((response) => {
        this.todoList = response.rows;
        this.total = response.total;
        this.totalNoRead = this.total;
        this.updateNoReadNum(this.totalNoRead);
        this.loading = false;
      });
    },
    /** 查询待办列表 */
    getList() {
      this.resetLoadingText();
      this.loading = true;
      listTodoTable(this.queryParams).then((response) => {
        this.todoList = response.rows;
        this.total = response.total;
        this.allTotal = this.total;
        this.loading = false;
        this.$emit("resetAllToal", this.total);
      });
    },
    /** 重置文案内容 */
    resetLoadingText() {
      this.loadingText = "正在加载中...";
    },
    /** 单选事件 */
    handleSelectionChange(selection, row) {
      if (selection.includes(row)) {
        this.$refs.todoTable.clearSelection();
        this.$nextTick(() => {
          this.$refs.todoTable.toggleRowSelection(row, true);
          this.selectedRow = row;
        });
      } else {
        this.selectedRow = null;
      }
    },
    /** 全选控制 */
    handleSelectAll(selection) {
      this.$refs.todoTable.clearSelection();
    },
    /** 终止办理 */
    handleDelete() {
      if (!this.selectedRow) {
        this.$modal.msgError("请先勾选需要终止办理的行数据！");
        return;
      }
      this.$confirm("确认终止办理：" + this.selectedRow.title + "吗？", "提示", { confirmButtonText: "确 认", cancelButtonText: "取 消" })
        .then(() => {
          this.loading = true;
          this.loadingText = "正在处理中，请稍后...";
          const param = { todoId: this.selectedRow.id };
          finishProcess(param)
            .then((res) => {
              if (res.code === 200) {
                this.$modal.msgSuccess("操作成功");
                this.getList();
              }
              this.loading = false;
            })
            .catch((this.loading = true));
        })
        .catch(() => console.info("操作取消"));
    },
  },
};
</script>

<style lang="scss" scoped>
.table-body {
  min-height: calc(100vh - 270px);
}
::v-deep .el-table .no-read {
  font-weight: 600;
}
::v-deep .el-table th {
  background-color: #f3f9ff !important;
  color: #5e7fa2;
  font-size: 14px;
}

::v-deep .el-table__row .cell {
  color: #303133;
  font-size: 14px;
}
.draft {
  font-size: 14px;
  font-weight: 500;
  margin-left: -6px;
}

.urge {
  font-size: 14px;
  font-weight: 600;
  color: #f19f00;
  margin-left: -6px;
}

.urgeStatus {
  font-size: 14px;
  font-weight: 600;
  color: #fd0505;
  margin-left: -6px;
}

.hanleType {
  font-size: 14px;
  font-weight: 600;
  color: #fd0505;
  margin-left: -6px;
}
</style>
