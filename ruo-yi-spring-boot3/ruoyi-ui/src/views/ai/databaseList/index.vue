<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="数据库别名" prop="aliasName">
        <el-input
          v-model="queryParams.aliasName"
          placeholder="请输入数据库别名"
          clearable
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
          v-hasPermi="['ai:databaseList:add']"
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
          v-hasPermi="['ai:databaseList:edit']"
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
          v-hasPermi="['ai:databaseList:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['ai:databaseList:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="databaseListList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="数据库别名" align="center" prop="aliasName" />
      <el-table-column label="数据库地址" align="center" prop="host" />
      <el-table-column label="数据库端口" align="center" prop="port" />
      <el-table-column label="数据库名" align="center" prop="database" />
      <el-table-column label="数据库表名称" align="center" prop="table" />
      <el-table-column label="数据库用户名" align="center" prop="user" />
      <el-table-column label="数据库密码" align="center" prop="password" />
      <el-table-column label="向量纬度" align="center" prop="dimension" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['ai:databaseList:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['ai:databaseList:remove']"
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

    <!-- 添加或修改向量库列表对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1000px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="150px">
        <el-form-item label="数据库别名" prop="aliasName">
          <el-input v-model="form.aliasName" placeholder="请输入数据库别名" />
        </el-form-item>
        <el-form-item label="数据库地址" prop="host">
          <el-input v-model="form.host" placeholder="请输入数据库地址" />
        </el-form-item>
        <el-form-item label="数据库端口" prop="port">
          <el-input v-model="form.port" placeholder="请输入数据库端口" />
        </el-form-item>
        <el-form-item label="数据库名" prop="database">
          <el-input v-model="form.database" placeholder="请输入数据库名" />
        </el-form-item>
        <el-form-item label="数据库表名称" prop="table">
          <el-input v-model="form.table" placeholder="请输入数据库表名称" />
        </el-form-item>
        <el-form-item label="数据库用户名" prop="user">
          <el-input v-model="form.user" placeholder="请输入数据库用户名" />
        </el-form-item>
        <el-form-item label="数据库密码" prop="password">
          <el-input v-model="form.password" placeholder="请输入数据库密码" />
        </el-form-item>
        <el-form-item label="向量纬度" prop="dimension">
          <el-input v-model="form.dimension" placeholder="请输入向量纬度" />
        </el-form-item>
        <el-form-item label="是否选择" prop="isChose">
          <el-switch
  v-model="form.isChose">
</el-switch>
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
import { listDatabaseList, getDatabaseList, delDatabaseList, addDatabaseList, updateDatabaseList } from "@/api/ai/databaseList";

export default {
  name: "DatabaseList",
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
      // 向量库列表表格数据
      databaseListList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        aliasName: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询向量库列表列表 */
    getList() {
      this.loading = true;
      listDatabaseList(this.queryParams).then(response => {
        this.databaseListList = response.rows;
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
        aliasName: null,
        host: null,
        port: null,
        database: null,
        table: null,
        user: null,
        password: null,
        dimension: null,
        isChose: null
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
      this.title = "添加向量库列表";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getDatabaseList(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改向量库列表";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateDatabaseList(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addDatabaseList(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除向量库列表编号为"' + ids + '"的数据项？').then(function() {
        return delDatabaseList(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('ai/databaseList/export', {
        ...this.queryParams
      }, `databaseList_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
