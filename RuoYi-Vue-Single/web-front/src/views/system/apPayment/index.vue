<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="付款单据号" prop="code">
        <el-input
          v-model="queryParams.code"
          placeholder="请输入付款单据号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="业务员" prop="salesman">
        <el-input
          v-model="queryParams.salesman"
          placeholder="请输入业务员"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="付款日期" prop="payDate">
        <el-date-picker clearable
          v-model="queryParams.payDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择付款日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="审核人" prop="approver">
        <el-input
          v-model="queryParams.approver"
          placeholder="请输入审核人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="收款人" prop="receiver">
        <el-input
          v-model="queryParams.receiver"
          placeholder="请输入收款人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="票据号码" prop="billNo">
        <el-input
          v-model="queryParams.billNo"
          placeholder="请输入票据号码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="票据金额" prop="billAmount">
        <el-input
          v-model="queryParams.billAmount"
          placeholder="请输入票据金额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="发票号码" prop="invoiceNo">
        <el-input
          v-model="queryParams.invoiceNo"
          placeholder="请输入发票号码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="发票金额" prop="invoiceAmount">
        <el-input
          v-model="queryParams.invoiceAmount"
          placeholder="请输入发票金额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="操作员" prop="operator">
        <el-input
          v-model="queryParams.operator"
          placeholder="请输入操作员"
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
          v-hasPermi="['system:apPayment:add']"
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
          v-hasPermi="['system:apPayment:edit']"
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
          v-hasPermi="['system:apPayment:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:apPayment:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="apPaymentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键" align="center" prop="id" />
      <el-table-column label="付款单据号" align="center" prop="code" />
      <el-table-column label="供货商ID" align="center" prop="supplierId" />
      <el-table-column label="业务员" align="center" prop="salesman" />
      <el-table-column label="付款日期" align="center" prop="payDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.payDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="审核人" align="center" prop="approver" />
      <el-table-column label="收款人" align="center" prop="receiver" />
      <el-table-column label="票据号码" align="center" prop="billNo" />
      <el-table-column label="票据金额" align="center" prop="billAmount" />
      <el-table-column label="发票号码" align="center" prop="invoiceNo" />
      <el-table-column label="发票金额" align="center" prop="invoiceAmount" />
      <el-table-column label="操作员" align="center" prop="operator" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:apPayment:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:apPayment:remove']"
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

    <!-- 添加或修改采购付款登记对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="付款单据号" prop="code">
          <el-input v-model="form.code" placeholder="请输入付款单据号" />
        </el-form-item>
        <el-form-item label="供货商ID" prop="supplierId">
          <el-input v-model="form.supplierId" placeholder="请输入供货商ID" />
        </el-form-item>
        <el-form-item label="业务员" prop="salesman">
          <el-input v-model="form.salesman" placeholder="请输入业务员" />
        </el-form-item>
        <el-form-item label="付款日期" prop="payDate">
          <el-date-picker clearable
            v-model="form.payDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择付款日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="审核人" prop="approver">
          <el-input v-model="form.approver" placeholder="请输入审核人" />
        </el-form-item>
        <el-form-item label="收款人" prop="receiver">
          <el-input v-model="form.receiver" placeholder="请输入收款人" />
        </el-form-item>
        <el-form-item label="票据号码" prop="billNo">
          <el-input v-model="form.billNo" placeholder="请输入票据号码" />
        </el-form-item>
        <el-form-item label="票据金额" prop="billAmount">
          <el-input v-model="form.billAmount" placeholder="请输入票据金额" />
        </el-form-item>
        <el-form-item label="发票号码" prop="invoiceNo">
          <el-input v-model="form.invoiceNo" placeholder="请输入发票号码" />
        </el-form-item>
        <el-form-item label="发票金额" prop="invoiceAmount">
          <el-input v-model="form.invoiceAmount" placeholder="请输入发票金额" />
        </el-form-item>
        <el-form-item label="操作员" prop="operator">
          <el-input v-model="form.operator" placeholder="请输入操作员" />
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
import { listApPayment, getApPayment, delApPayment, addApPayment, updateApPayment } from "@/api/system/apPayment";

export default {
  name: "ApPayment",
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
      // 采购付款登记表格数据
      apPaymentList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        code: null,
        salesman: null,
        payDate: null,
        approver: null,
        receiver: null,
        billNo: null,
        billAmount: null,
        invoiceNo: null,
        invoiceAmount: null,
        operator: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        code: [
          { required: true, message: "付款单据号不能为空", trigger: "blur" }
        ],
        supplierId: [
          { required: true, message: "供货商ID不能为空", trigger: "blur" }
        ],
        payDate: [
          { required: true, message: "付款日期不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询采购付款登记列表 */
    getList() {
      this.loading = true;
      listApPayment(this.queryParams).then(response => {
        this.apPaymentList = response.rows;
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
        code: null,
        supplierId: null,
        salesman: null,
        payDate: null,
        approver: null,
        receiver: null,
        billNo: null,
        billAmount: null,
        invoiceNo: null,
        invoiceAmount: null,
        operator: null,
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
      this.title = "添加采购付款登记";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getApPayment(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改采购付款登记";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateApPayment(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addApPayment(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除采购付款登记编号为"' + ids + '"的数据项？').then(function() {
        return delApPayment(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/apPayment/export', {
        ...this.queryParams
      }, `apPayment_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
