<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="数据权限名称" prop="scopeName">
        <el-input
          v-model="queryParams.scopeName"
          placeholder="请输入数据权限名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:rule:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:rule:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:rule:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          :loading="exportLoading"
          @click="handleExport"
          v-hasPermi="['system:rule:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="ruleList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="资源编号" align="center" prop="resourceCode" />
      <el-table-column label="数据权限名称" align="center" prop="scopeName" />
      <el-table-column label="数据权限类型" align="center" prop="scopeType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.data_rule_type" :value="scope.row.scopeType"/>
        </template>
      </el-table-column>
      <el-table-column label="数据库表" align="center" prop="tableName" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:rule:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:rule:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改数据权限对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="50%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="资源编号" prop="resourceCode">
          <el-input v-model="form.resourceCode" placeholder="请输入资源编号" />
        </el-form-item>
        <el-form-item label="数据权限名称" prop="scopeName">
          <el-input v-model="form.scopeName" placeholder="请输入数据权限名称" />
        </el-form-item>
        <el-form-item label="数据权限字段" prop="scopeField">
          <el-input v-model="form.scopeField" placeholder="请输入数据权限字段" />
        </el-form-item>
        <el-form-item label="数据权限类名" prop="scopeClass">
          <el-input v-model="form.scopeClass" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="数据权限字段" prop="scopeColumn">
          <el-input v-model="form.scopeColumn" placeholder="请输入数据权限字段" />
        </el-form-item>
        <el-form-item label="数据权限类型" prop="scopeType">
          <el-select v-model="form.scopeType" placeholder="请选择数据权限类型">
            <el-option
              v-for="dict in dict.type.data_rule_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="数据权限值域" prop="scopeValue">
          <el-input v-model="form.scopeValue" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="数据权限备注" prop="remarks">
          <el-input v-model="form.remarks" placeholder="请输入数据权限备注" />
        </el-form-item>
        <el-form-item label="数据库表" prop="tableName">
          <el-input v-model="form.tableName" placeholder="请输入数据库表" />
        </el-form-item>
        <el-form-item label="用户表对应字段" prop="userColumn">
          <el-input v-model="form.userColumn" placeholder="请输入用户表对应字段" />
        </el-form-item>
        <el-form-item label="user实体类对应字段" prop="userEntityField">
          <el-input v-model="form.userEntityField" placeholder="请输入user实体类对应字段" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listDataRule, getDataRule, delDataRule, addDataRule, updateDataRule, exportDataRule } from "@/api/system/dataRule";

export default {
  name: "RoleDataRule",
  dicts: ['data_rule_type'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 导出遮罩层
      exportLoading: false,
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
      // 数据权限表格数据
      ruleList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        scopeName: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        resourceCode: [
          { required: true, message: "资源编号不能为空", trigger: "blur" }
        ],
        scopeName: [
          { required: true, message: "数据权限名称不能为空", trigger: "blur" }
        ],
        scopeField: [
          { required: true, message: "数据权限字段不能为空", trigger: "blur" }
        ],
        scopeClass: [
          { required: true, message: "数据权限类名不能为空", trigger: "blur" }
        ],
        scopeColumn: [
          { required: true, message: "数据权限字段不能为空", trigger: "blur" }
        ],
        scopeType: [
          { required: true, message: "数据权限类型不能为空", trigger: "change" }
        ],
        tenantId: [
          { required: true, message: "租户ID不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询数据权限列表 */
    getList() {
      this.loading = true;
      listDataRule(this.queryParams).then(response => {
        this.ruleList = response.rows;
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
        resourceCode: null,
        scopeName: null,
        scopeField: null,
        scopeClass: null,
        scopeColumn: null,
        scopeType: null,
        scopeValue: null,
        remarks: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        delFlag: null,
        tableName: null,
        userColumn: null,
        tenantId: null,
        userEntityField: null
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
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加数据权限";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getDataRule(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改数据权限";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateDataRule(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addDataRule(this.form).then(response => {
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
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除数据权限编号为"' + ids + '"的数据项？').then(function() {
        return delDataRule(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$modal.confirm('是否确认导出所有数据权限数据项？').then(() => {
        this.exportLoading = true;
        return exportDataRule(queryParams);
      }).then(response => {
        this.$download.name(response.msg);
        this.exportLoading = false;
      }).catch(() => {});
    }
  }
};
</script>
