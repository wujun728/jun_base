<template>
  <!-- 我起草的 -->
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px" @submit.native.prevent>
      <el-form-item label prop="bizTitle">
        <el-input
          v-model="queryParams.bizTitle"
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

    <el-table
      v-loading="loading"
      ref="todoTable"
      :data="tableList"
      stripe
      class="pointer"
      @row-click="handleRowClick"
      @select-all="handleSelectAll"
      @select="handleSelectionChange"
      :element-loading-text="loadingText"
      element-loading-spinner="el-icon-loading"
    >
      <!-- <el-table-column type="selection" width="55" align="center" /> -->
      <el-table-column label="序号" type="index" align="center" width="55">
        <template slot-scope="scope">
          <span>{{(queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column label="标题" align="left" prop="bizTitle" show-overflow-tooltip />
      <el-table-column label="类型" align="center" prop="templateName" width="150" show-overflow-tooltip />
      <el-table-column label="状态" align="center" prop="status" width="150" show-overflow-tooltip>
        <template slot-scope="scope">
          <dict-tag :options="dict.type.workflow_biz_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
  </div>
</template>

<script>
import { listDraft } from "@/api/workflow/draft";

export default {
  name: "MyDraftWorkflow",
  dicts: ["workflow_biz_status"],
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
      // 总条数
      total: 0,
      // 待办表格数据
      tableList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        bizTitle: null,
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 行点击 */
    handleRowClick(row, column, event) {
      this.$router.push({
        path: "/workflow/flowForm/" + new Date().getTime(),
        query: {
          title: row.bizTitle,
          pageType: "2",
          businessId: row.bizId,
          procInsId: row.procInstId,
          templateId: row.templateId,
          taskId: row.taskId,
          userId: row.createId,
        },
      });
    },
    /** 查询待办列表 */
    getList() {
      this.loading = true;
      listDraft(this.queryParams).then((response) => {
        this.tableList = response.rows;
        this.total = response.total;
        this.allTotal = this.total;
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
      this.daterangeCreateTime = [];
      this.resetForm("queryForm");
      this.handleQuery();
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
  },
};
</script>

<style lang="scss" scoped>
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
</style>
