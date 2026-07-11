<template>
  <!-- 已办 -->
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px" @submit.native.prevent>
      <el-form-item label prop="title">
        <el-input
          v-model="queryParams.title"
          prefix-icon="el-icon-search"
          placeholder="请输入标题关键字"
          clearable
          @clear="resetQuery"
          @keyup.enter.native="handleQuery"
          style="width: 300px;"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-divider />
    <el-row :gutter="10" class="mb8" style="float: right">
      <el-col :span="1.5">
        <el-button type="primary" plain size="mini" @click="handleRevoke">
          <svg-icon icon-class="quhui" class="mr5"></svg-icon>取回
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="primary" plain size="mini" @click="handleUrge">
          <svg-icon icon-class="cui" class="mr5"></svg-icon>催办
        </el-button>
      </el-col>
    </el-row>

    <el-table
      v-loading="loading"
      ref="doneTable"
      :data="doneList"
      stripe
      class="pointer"
      @row-click="handleRowClick"
      @select-all="handleSelectAll"
      @select="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <!-- <el-table-column label="序号" type="index" align="center" width="55">
        <template slot-scope="scope">
          <span>{{(queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1}}</span>
        </template>
      </el-table-column>-->
      <el-table-column label="标题" align="left" prop="title" show-overflow-tooltip />
      <el-table-column label="类型" align="center" prop="templateName" width="160" show-overflow-tooltip />
      <el-table-column label="办理环节" align="center" prop="handleNode" width="160" show-overflow-tooltip />
      <el-table-column label="办理时间" align="center" prop="handleTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.handleTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
  </div>
</template>

<script>
import { listDone, urgeAll } from "@/api/workflow/done";
import { revokeProcess } from "@/api/workflow/task";

export default {
  name: "Done",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 当前选中行
      selectedRow: null,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 已办表格数据
      doneList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: null,
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询已办列表 */
    getList() {
      this.loading = true;
      listDone(this.queryParams).then((response) => {
        this.doneList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 行点击
    handleRowClick(row, column, event) {
      this.$router.push({
        path: "/workflow/flowForm/" + new Date().getTime(),
        query: {
          title: row.title,
          pageType: "2",
          todoId: row.todoId,
          businessId: row.businessId,
          procInsId: row.procInstId,
          templateId: row.templateId,
          taskId: row.taskId,
          userId: row.handler,
        },
      });
    },
    /** 单选事件 */
    handleSelectionChange(selection, row) {
      if (selection.includes(row)) {
        this.$refs.doneTable.clearSelection();
        this.$nextTick(() => {
          this.$refs.doneTable.toggleRowSelection(row, true);
          this.selectedRow = row;
        });
      } else {
        this.selectedRow = null;
      }
    },
    /** 全选控制 */
    handleSelectAll(selection) {
      this.$refs.doneTable.clearSelection();
    },
    /** 取回 */
    handleRevoke() {
      if (!this.selectedRow) {
        this.$modal.msgError("请先勾选需要取回的行数据！");
        return;
      }
      this.$confirm("确认取回：" + this.selectedRow.title + "吗？", "提示", { confirmButtonText: "确 认", cancelButtonText: "取 消" })
        .then(() => {
          this.loading = true;
          const param = { doneId: this.selectedRow.id };
          revokeProcess(param)
            .then((res) => {
              if (res.code === 200) {
                this.$modal.msgSuccess("操作成功");
                this.handleQuery();
                this.selectedRow = null;
                this.loading = false;
              }
            })
            .catch((this.loading = false));
        })
        .catch(() => console.info("操作取消"));
    },
    /** 催办 */
    handleUrge() {
      if (!this.selectedRow) {
        this.$modal.msgError("请先勾选需要催办的行数据！");
        return;
      }
      this.$confirm("确认催办：" + this.selectedRow.title + "吗？", "提示", { confirmButtonText: "确 认", cancelButtonText: "取 消" })
        .then(() => {
          this.loading = true;
          const param = { procInstId: this.selectedRow.procInstId };
          urgeAll(param)
            .then((res) => {
              if (res.code === 200) {
                this.$modal.msgSuccess("操作成功");
                this.handleQuery();
                this.selectedRow = null;
                this.loading = false;
              }
            })
            .catch((this.loading = false));
        })
        .catch(() => console.info("操作取消"));
    },
  },
};
</script>
<style lang="scss" scoped>
::v-deep .el-table th {
  background-color: #f3f9ff !important;
  color: #5e7fa2;
  font-size: 14px;
}

::v-deep .el-table__row .cell {
  color: #303133;
  font-size: 14px;
}

/* 内容单元格样式 */
::v-deep .custom-column .cell {
  padding-left: 20px !important;
  transition: padding 0.3s; /* 添加过渡动画 */
}
</style>