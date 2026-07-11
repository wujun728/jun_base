<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="92px">
      <el-form-item label="模板名称" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入模板名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="启用状态" prop="enableFlag">
        <el-select v-model="queryParams.enableFlag" placeholder="请选择发布状态" clearable>
          <el-option v-for="dict in dict.type.sys_yes_or_no" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-divider />

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['workflow:template:add']">新增模板</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="templateList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="模板名称" align="left" prop="name" show-overflow-tooltip />
      <el-table-column label="模板类型" align="center" prop="typeName" width="180" show-overflow-tooltip />
      <el-table-column label="表单类型" align="center" prop="formType" width="180" show-overflow-tooltip>
        <template slot-scope="scope">
          <dict-tag :options="dict.type.workflow_template_form_type" :value="scope.row.formType" />
        </template>
      </el-table-column>
      <el-table-column label="启用状态" align="center" prop="enableFlag" width="100">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.enableFlag" active-value="1" inactive-value="0" @change="handleStatus(scope.row)"></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="排序" align="center" prop="sort" width="100" />
      <el-table-column label="创建人" align="center" prop="createBy" width="100" show-overflow-tooltip />
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, "{y}-{m}-{d} {h}:{i}:{s}") }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="120">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['workflow:template:edit']">修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            v-if="scope.row.enableFlag === '0'"
            @click="handleDelete(scope.row)"
            v-hasPermi="['workflow:template:remove']"
          >刪除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
  </div>
</template>

<script>
import { listTemplate, getTemplate, delTemplate, changeEnable } from "@/api/workflow/template";

export default {
  name: "WorkflowTemplate",
  dicts: ["sys_yes_or_no", "workflow_template_form_type"],
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
      // 模板配置表格数据
      templateList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        type: null,
        enableFlag: null,
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询模板配置列表 */
    getList() {
      this.loading = true;
      listTemplate(this.queryParams).then((response) => {
        this.templateList = response.rows;
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
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd(row) {
      this.$router.push({ path: "/workflow/template/add" });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.$router.push({
        path: "/workflow/template/add",
        query: { id: row.id },
      });
    },
    /** 发布/下架操作 */
    handleStatus(row) {
      let text = row.enableFlag === "0" ? "停用" : "启用";
      this.$modal
        .confirm('确认要"' + text + '"当前配置吗？')
        .then(function () {
          let data = {
            id: row.id,
            enableFlag: row.enableFlag,
          };
          return changeEnable(data);
        })
        .then(() => {
          this.$modal.msgSuccess(text + "成功");
          this.getList();
        })
        .catch(function () {
          row.enableFlag = row.enableFlag === "0" ? "1" : "0";
        });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal
        .confirm("是否确认删除此模板？")
        .then(function () {
          return delTemplate(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
  },
};
</script>
