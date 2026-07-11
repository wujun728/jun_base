<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="92px">
      <el-form-item label="bean名称" prop="beanName">
        <el-input v-model="queryParams.beanName" placeholder="请输入bean名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="交换机key" prop="exchangeKey">
        <el-input v-model="queryParams.exchangeKey" placeholder="请输入交换机key" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="路由key" prop="routingKey">
        <el-input v-model="queryParams.routingKey" placeholder="请输入路由key" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择" clearable>
          <el-option v-for="dict in dict.type.mq_async_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-divider />

    <el-table v-loading="loading" :data="asyncList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="bean名称" align="left" prop="beanName" width="150" show-overflow-tooltip />
      <el-table-column label="交换机key" align="left" prop="exchangeKey" width="150" show-overflow-tooltip />
      <el-table-column label="路由key" align="left" prop="routingKey" width="200" show-overflow-tooltip />
      <el-table-column label="消息内容" align="left" prop="messageContent" show-overflow-tooltip />
      <el-table-column label="失败原因" align="left" prop="failReason" width="200" show-overflow-tooltip />
      <el-table-column label="状态" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.mq_async_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="重试次数" align="center" prop="retryTime" width="100" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="100">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            v-if="['0','2'].includes(scope.row.status)"
            :disabled="scope.row.isDisabled"
            @click="handleRetry(scope.row)"
            v-hasPermi="['mq:async:retry']"
          >重试</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['mq:async:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
  </div>
</template>

<script>
import { listAsync, delAsync, retry } from "@/api/mq/async";

export default {
  name: "Async",
  dicts: ["mq_async_status"],
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
      // 异步任务日志记录表格数据
      asyncList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        beanName: null,
        exchangeKey: null,
        routingKey: null,
      },
      // 表单参数
      form: {},
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询异步任务日志记录列表 */
    getList() {
      this.loading = true;
      listAsync(this.queryParams).then((res) => {
        if (res.rows) {
          this.asyncList = res.rows;
          this.asyncList.forEach((item, index) => {
            this.$set(item, "isDisabled", false);
          });
          this.total = res.total;
          this.loading = false;
        }
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
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 重试按钮操作 */
    handleRetry(row) {
      const id = row.id;
      row.isDisabled = true;
      retry(id).then((response) => {
        if (response.code == 200) {
          row.isDisabled = false;
          this.$message.success("重试成功");
          this.getList();
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal
        .confirm('是否确认删除异步任务日志记录编号为"' + ids + '"的数据项？')
        .then(function () {
          return delAsync(ids);
        })
        .then(() => {
          this.$message.success("删除成功");
          this.getList();
        })
        .catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "mq/async/export",
        {
          ...this.queryParams,
        },
        `async_${new Date().getTime()}.xlsx`
      );
    },
  },
};
</script>
