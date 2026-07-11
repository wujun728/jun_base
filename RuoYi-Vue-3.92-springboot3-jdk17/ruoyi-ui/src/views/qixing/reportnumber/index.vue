<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="${comment}" prop="refReportnumberCode">
        <el-input
          v-model="queryParams.refReportnumberCode"
          placeholder="请输入${comment}"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="项目报告" prop="refReportnumberTitle">
        <el-input
          v-model="queryParams.refReportnumberTitle"
          placeholder="请输入项目报告"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="报告文号(生成)" prop="reportnumberCode">
        <el-input
          v-model="queryParams.reportnumberCode"
          placeholder="请输入报告文号(生成)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="报告出具人" prop="refReportnumberMan">
        <el-input
          v-model="queryParams.refReportnumberMan"
          placeholder="请输入报告出具人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="报告审核人" prop="refReportnumberCheckMan">
        <el-input
          v-model="queryParams.refReportnumberCheckMan"
          placeholder="请输入报告审核人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="签字注册会计师" prop="refSignatureAccountant">
        <el-input
          v-model="queryParams.refSignatureAccountant"
          placeholder="请输入签字注册会计师"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="申请人" prop="creator">
        <el-input
          v-model="queryParams.creator"
          placeholder="请输入申请人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="鍒涘缓浜篒D" prop="createId">
        <el-input
          v-model="queryParams.createId"
          placeholder="请输入${comment}"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="鏇存柊浜篒D" prop="updateId">
        <el-input
          v-model="queryParams.updateId"
          placeholder="请输入${comment}"
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
          v-hasPermi="['system:reportnumber:add']"
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
          v-hasPermi="['system:reportnumber:edit']"
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
          v-hasPermi="['system:reportnumber:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:reportnumber:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="reportnumberList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="${comment}" align="center" prop="refReportnumberCode" />
      <el-table-column label="项目报告" align="center" prop="refReportnumberTitle" />
      <el-table-column label="报告文号(生成)" align="center" prop="reportnumberCode" />
      <el-table-column label="报告号状态" align="center" prop="dictRpStatus" />
      <el-table-column label="报告出具人" align="center" prop="refReportnumberMan" />
      <el-table-column label="报告审核人" align="center" prop="refReportnumberCheckMan" />
      <el-table-column label="签字注册会计师" align="center" prop="refSignatureAccountant" />
      <el-table-column label="申请人" align="center" prop="creator" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="${comment}" align="center" prop="createId" />
      <el-table-column label="${comment}" align="center" prop="updateId" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:reportnumber:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:reportnumber:remove']"
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

    <!-- 添加或修改项目报告文号对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="${comment}" prop="refReportnumberCode">
          <el-input v-model="form.refReportnumberCode" placeholder="请输入${comment}" />
        </el-form-item>
        <el-form-item label="项目报告" prop="refReportnumberTitle">
          <el-input v-model="form.refReportnumberTitle" placeholder="请输入项目报告" />
        </el-form-item>
        <el-form-item label="报告文号(生成)" prop="reportnumberCode">
          <el-input v-model="form.reportnumberCode" placeholder="请输入报告文号(生成)" />
        </el-form-item>
        <el-form-item label="报告出具人" prop="refReportnumberMan">
          <el-input v-model="form.refReportnumberMan" placeholder="请输入报告出具人" />
        </el-form-item>
        <el-form-item label="报告审核人" prop="refReportnumberCheckMan">
          <el-input v-model="form.refReportnumberCheckMan" placeholder="请输入报告审核人" />
        </el-form-item>
        <el-form-item label="签字注册会计师" prop="refSignatureAccountant">
          <el-input v-model="form.refSignatureAccountant" placeholder="请输入签字注册会计师" />
        </el-form-item>
        <el-form-item label="申请人" prop="creator">
          <el-input v-model="form.creator" placeholder="请输入申请人" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
        <el-form-item label="鍒涘缓浜篒D" prop="createId">
          <el-input v-model="form.createId" placeholder="请输入${comment}" />
        </el-form-item>
        <el-form-item label="鏇存柊浜篒D" prop="updateId">
          <el-input v-model="form.updateId" placeholder="请输入${comment}" />
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
import { listReportnumber, getReportnumber, delReportnumber, addReportnumber, updateReportnumber } from "@/api/qixing/reportnumber";

export default {
  name: "Reportnumber",
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
      // 项目报告文号表格数据
      reportnumberList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        refReportnumberCode: null,
        refReportnumberTitle: null,
        reportnumberCode: null,
        dictRpStatus: null,
        refReportnumberMan: null,
        refReportnumberCheckMan: null,
        refSignatureAccountant: null,
        creator: null,
        createId: null,
        updateId: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        refReportnumberTitle: [
          { required: true, message: "项目报告不能为空", trigger: "blur" }
        ],
        reportnumberCode: [
          { required: true, message: "报告文号(生成)不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询项目报告文号列表 */
    getList() {
      this.loading = true;
      listReportnumber(this.queryParams).then(response => {
        this.reportnumberList = response.rows;
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
        refReportnumberCode: null,
        refReportnumberTitle: null,
        reportnumberCode: null,
        dictRpStatus: null,
        refReportnumberMan: null,
        refReportnumberCheckMan: null,
        refSignatureAccountant: null,
        creator: null,
        remark: null,
        createTime: null,
        createId: null,
        updateTime: null,
        updateId: null
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
      this.title = "添加项目报告文号";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getReportnumber(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改项目报告文号";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateReportnumber(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addReportnumber(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除项目报告文号编号为"' + ids + '"的数据项？').then(function() {
        return delReportnumber(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/reportnumber/export', {
        ...this.queryParams
      }, `reportnumber_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
