<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="归还单ID" prop="loanReturnId">
        <el-input
          v-model="queryParams.loanReturnId"
          placeholder="请输入归还单ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="行号" prop="lineNo">
        <el-input
          v-model="queryParams.lineNo"
          placeholder="请输入行号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="借出单据号" prop="loanCode">
        <el-input
          v-model="queryParams.loanCode"
          placeholder="请输入借出单据号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="产品ID" prop="productId">
        <el-input
          v-model="queryParams.productId"
          placeholder="请输入产品ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="产品编码(冗余)" prop="productCode">
        <el-input
          v-model="queryParams.productCode"
          placeholder="请输入产品编码(冗余)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="归还数量" prop="qty">
        <el-input
          v-model="queryParams.qty"
          placeholder="请输入归还数量"
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
          v-hasPermi="['system:loanReturnLine:add']"
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
          v-hasPermi="['system:loanReturnLine:edit']"
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
          v-hasPermi="['system:loanReturnLine:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:loanReturnLine:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="loanReturnLineList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键" align="center" prop="id" />
      <el-table-column label="归还单ID" align="center" prop="loanReturnId" />
      <el-table-column label="行号" align="center" prop="lineNo" />
      <el-table-column label="借出单据号" align="center" prop="loanCode" />
      <el-table-column label="产品ID" align="center" prop="productId" />
      <el-table-column label="产品编码(冗余)" align="center" prop="productCode" />
      <el-table-column label="归还数量" align="center" prop="qty" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:loanReturnLine:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:loanReturnLine:remove']"
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

    <!-- 添加或修改归还单-明细对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="归还单ID" prop="loanReturnId">
          <el-input v-model="form.loanReturnId" placeholder="请输入归还单ID" />
        </el-form-item>
        <el-form-item label="行号" prop="lineNo">
          <el-input v-model="form.lineNo" placeholder="请输入行号" />
        </el-form-item>
        <el-form-item label="借出单据号" prop="loanCode">
          <el-input v-model="form.loanCode" placeholder="请输入借出单据号" />
        </el-form-item>
        <el-form-item label="产品ID" prop="productId">
          <el-input v-model="form.productId" placeholder="请输入产品ID" />
        </el-form-item>
        <el-form-item label="产品编码(冗余)" prop="productCode">
          <el-input v-model="form.productCode" placeholder="请输入产品编码(冗余)" />
        </el-form-item>
        <el-form-item label="归还数量" prop="qty">
          <el-input v-model="form.qty" placeholder="请输入归还数量" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
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
import { listLoanReturnLine, getLoanReturnLine, delLoanReturnLine, addLoanReturnLine, updateLoanReturnLine } from "@/api/system/loanReturnLine";

export default {
  name: "LoanReturnLine",
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
      // 归还单-明细表格数据
      loanReturnLineList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        loanReturnId: null,
        lineNo: null,
        loanCode: null,
        productId: null,
        productCode: null,
        qty: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        loanReturnId: [
          { required: true, message: "归还单ID不能为空", trigger: "blur" }
        ],
        lineNo: [
          { required: true, message: "行号不能为空", trigger: "blur" }
        ],
        productId: [
          { required: true, message: "产品ID不能为空", trigger: "blur" }
        ],
        qty: [
          { required: true, message: "归还数量不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询归还单-明细列表 */
    getList() {
      this.loading = true;
      listLoanReturnLine(this.queryParams).then(response => {
        this.loanReturnLineList = response.rows;
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
        loanReturnId: null,
        lineNo: null,
        loanCode: null,
        productId: null,
        productCode: null,
        qty: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
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
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加归还单-明细";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getLoanReturnLine(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改归还单-明细";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateLoanReturnLine(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addLoanReturnLine(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除归还单-明细编号为"' + ids + '"的数据项？').then(function() {
        return delLoanReturnLine(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/loanReturnLine/export', {
        ...this.queryParams
      }, `loanReturnLine_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
