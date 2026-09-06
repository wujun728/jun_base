<template>
  <div class="app-container">
    <el-card shadow="never" body-class="search-card">
      <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="queryParams.name" placeholder="请输入表达式名称" clearable @keyup.enter.native="handleQuery" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
            <el-option v-for="dict in sys_common_status" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="search" @click="handleQuery">搜索</el-button>
          <el-button icon="refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="mt10">
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button type="primary" plain icon="plus" @click="handleAdd"
            v-hasPermi="['system:expression:add']">新增</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="success" plain icon="edit" :disabled="single" @click="handleUpdate"
            v-hasPermi="['system:expression:edit']">修改</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="danger" plain icon="delete" :disabled="multiple" @click="handleDelete"
            v-hasPermi="['system:expression:remove']">删除</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="warning" plain icon="download" @click="handleExport"
            v-hasPermi="['system:expression:export']">导出</el-button>
        </el-col>
        <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-alert title="流程设计说明" type="warning">
        <div>1、用于指定流程任务配置中候选角色的表达式，可以使用SpEL表达式和流程中的变量。</div>
        <div>2、更改表达式<strong style="color: red;">不会影响</strong>已部署的流程实例。</div>
      </el-alert>
      <el-table v-loading="loading" :data="expressionList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="主键" align="center" prop="id" />
        <el-table-column label="名称" align="center" prop="name" />
        <el-table-column label="表达式内容" align="center" prop="expression" />
        <el-table-column label="指定类型" align="center" prop="dataType">
          <template v-slot="scope">
            <dict-tag :options="exp_data_type" :value="scope.row.dataType" />
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template v-slot="scope">
            <el-button type="primary" icon="edit" link @click="handleUpdate(scope.row)"
              v-hasPermi="['system:expression:edit']">修改</el-button>
            <el-button type="primary" icon="delete" link @click="handleDelete(scope.row)"
              v-hasPermi="['system:expression:remove']">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>

    <!-- 添加或修改流程达式对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入表达式名称" />
        </el-form-item>
        <el-form-item label="内容" prop="expression">
          <el-input v-model="form.expression" placeholder="请输入表达式内容" />
        </el-form-item>
        <el-form-item label="指定类型" prop="dataType">
          <el-radio-group v-model="form.dataType">
            <el-radio v-for="dict in exp_data_type" :key="dict.value" :value="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio v-for="dict in sys_common_status" :key="dict.value" :value="parseInt(dict.value)">{{ dict.label
              }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { listExpression, getExpression, delExpression, addExpression, updateExpression } from "@modules/flowable/api/expression";

export default {
  name: "FlowExp",
  setup() {
    const { proxy } = getCurrentInstance();
    const { sys_common_status, exp_data_type } = proxy.useDict("sys_common_status", "exp_data_type");
    return {
      sys_common_status,
      exp_data_type
    }
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
      // 流程达式表格数据
      expressionList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        expression: null,
        status: null,
      },
      // 表单参数
      form: {
        dataType: 'fixed'
      },
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询流程达式列表 */
    getList() {
      this.loading = true;
      listExpression(this.queryParams).then(response => {
        this.expressionList = response.rows;
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
        expression: null,
        createTime: null,
        updateTime: null,
        createBy: null,
        updateBy: null,
        status: null,
        remark: null
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
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加流程表达式";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getExpression(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改流程达式";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateExpression(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addExpression(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除流程达式编号为"' + ids + '"的数据项？').then(function () {
        return delExpression(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/expression/export', {
        ...this.queryParams
      }, `expression_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
