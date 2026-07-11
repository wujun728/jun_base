<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px" @submit.native.prevent>
      <el-form-item label="表单名称" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入表单名称" clearable size="small" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-divider />

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['template.dynamic:form:add']">新增表单</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="formList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="表单名称" align="left" prop="name" show-overflow-tooltip />
      <el-table-column label="备注" align="left" prop="remark" width="300" show-overflow-tooltip />
      <el-table-column label="创建人" align="center" prop="createBy" width="120" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="160" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="160">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleDetail(scope.row)">预览</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['template.dynamic:form:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['template.dynamic:form:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改流程表单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-divider />
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="表单名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入表单名称" />
        </el-form-item>
        <el-form-item label="表单内容">
          <editor v-model="form.content" :min-height="192" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!--表单配置详情-->
    <el-dialog :title="formTitle" :visible.sync="formConfOpen" append-to-body width="1000px">
      <el-divider />
      <div class="test-form">
        <parser :key="new Date().getTime()" :form-conf="formConf" />
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listDynamicForm, delDynamicForm, addDynamicForm, updateDynamicForm, exportDynamicForm } from "@/api/workflow/dynamicForm";
import Editor from "@/components/Editor";
import Parser from "@/components/parser/Parser";
export default {
  name: "DynamicForm",
  components: {
    Editor,
    Parser,
  },
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
      // 流程表单表格数据
      formList: [],
      // 弹出层标题
      title: "",
      formConf: {}, // 默认表单数据
      formConfOpen: false,
      formTitle: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        content: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询流程表单列表 */
    getList() {
      this.loading = true;
      listDynamicForm(this.queryParams).then((response) => {
        this.formList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        name: null,
        content: null,
        createTime: null,
        updateTime: null,
        createBy: null,
        updateBy: null,
        remark: null,
      };
      this.resetForm("form");
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
    /** 表单配置信息 */
    handleDetail(row) {
      this.formConfOpen = true;
      this.formTitle = "页面预览";
      this.formConf = JSON.parse(row.content);
      // 隐藏按钮
      this.formConf.formBtns = false;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.$router.push({ path: "/tool/build/index", query: { formId: null } });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.$router.push({ path: "/tool/build/index", query: { formId: row.id } });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateDynamicForm(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addDynamicForm(this.form).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const formIds = row.id || this.ids;
      this.$confirm("是否确认删除？删除后，原引用此表单的流程不受影响。", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return delDynamicForm(formIds);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        });
    },
  },
};
</script>

<style lang="scss" scoped>
.test-form {
  margin: 10px auto;
  width: 900px;
  padding: 10px;
}
</style>
