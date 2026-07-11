<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="流程实例" prop="procInstId">
        <el-input v-model="queryParams.procInstId" placeholder="请输入流程实例" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="流程名称" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入流程名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="业务ID" prop="businessKey">
        <el-input v-model="queryParams.businessKey" placeholder="请输入业务ID" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-divider />
    <el-table v-loading="loading" :data="historyList" @selection-change="handleSelectionChange">
      <el-table-column label="序号" type="index" align="center" width="55">
        <template slot-scope="scope">
          <span>{{(queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column label="业务标题" align="left" prop="title" />
      <el-table-column label="业务ID" align="left" prop="businessKey" width="280" show-overflow-tooltip />
      <el-table-column label="流程实例" align="left" prop="processInstanceId" width="100" show-overflow-tooltip />
      <el-table-column label="流程名称" align="left" prop="name" width="200" show-overflow-tooltip />
      <el-table-column label="当前节点" align="left" prop="currentTask" width="140" show-overflow-tooltip />
      <el-table-column label="当前处理人" align="left" prop="assignee" width="140" show-overflow-tooltip />
      <el-table-column label="开始时间" align="center" prop="startTime" width="160" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="90">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-info" @click="showHistory(scope.row)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog title="执行记录" :visible.sync="showHistoryDialog" width="1000px" class="custom-dialog">
      <el-divider />
      <el-table :data="historyRecordList" height="500" v-loading="loadDetail">
        <el-table-column prop="taskName" label="活动名称" align="left" width="160" show-overflow-tooltip />
        <el-table-column prop="assignee" label="办理人" align="left" width="120" show-overflow-tooltip />
        <el-table-column prop="startTime" label="开始时间" align="left" width="160" />
        <el-table-column prop="endTime" label="结束时间" align="left" width="160" />
        <el-table-column prop="comment" label="审批意见" align="left" />
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button @click="showHistoryDialog = false">关 闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getListHistoryProcess, getListByTypeAndId } from "@/api/workflow/flowable/monitor";

export default {
  name: "FlowExp",
  dicts: ["sys_common_status"],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 流程历史
      historyList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        procInstId: null,
        name: null,
        businessKey: null,
      },
      showHistoryDialog: false,
      historyList: [],
      historyRecordList: [],
      loadDetail: true,
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询流程历史列表 */
    getList() {
      this.loading = true;
      getListHistoryProcess(this.queryParams)
        .then((response) => {
          this.historyList = response.rows;
          this.total = response.total;
          this.loading = false;
        })
        .catch((e) => {
          this.loading = false;
        });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
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
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    // 查看历史
    showHistory(row) {
      this.loadDetail = true;
      const { processInstanceId } = row;
      this.showHistoryDialog = true;
      this.historyRecordList = [];
      getListByTypeAndId("history", processInstanceId).then((res) => {
        this.historyRecordList = res.rows;
        this.loadDetail = false;
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.custom-dialog .el-dialog__body {
  padding: 0 20px; /* 调整内边距 */
  max-height: 600px; /* 如果需要也可控制整个 body 区域高度 */
  overflow: hidden;
}
</style>