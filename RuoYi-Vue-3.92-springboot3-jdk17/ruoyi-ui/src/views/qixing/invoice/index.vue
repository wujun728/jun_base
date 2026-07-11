<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="${comment}" prop="refCustomerCode">
        <el-input
          v-model="queryParams.refCustomerCode"
          placeholder="请输入${comment}"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="开票客户" prop="refCustomerName">
        <el-input
          v-model="queryParams.refCustomerName"
          placeholder="请输入开票客户"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="${comment}" prop="refProjectCode">
        <el-input
          v-model="queryParams.refProjectCode"
          placeholder="请输入${comment}"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="开票项目" prop="refProjectName">
        <el-input
          v-model="queryParams.refProjectName"
          placeholder="请输入开票项目"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="纳税人识别号" prop="invoiceTaxNo">
        <el-input
          v-model="queryParams.invoiceTaxNo"
          placeholder="请输入纳税人识别号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="电话" prop="telephone">
        <el-input
          v-model="queryParams.telephone"
          placeholder="请输入电话"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="开户行账号" prop="bankCardNo">
        <el-input
          v-model="queryParams.bankCardNo"
          placeholder="请输入开户行账号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="开票金额" prop="invoiceMoney">
        <el-input
          v-model="queryParams.invoiceMoney"
          placeholder="请输入开票金额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="开票审批状态" prop="dictInvoiceState">
        <el-input
          v-model="queryParams.dictInvoiceState"
          placeholder="请输入开票审批状态"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="流程状态" prop="dictWfState">
        <el-input
          v-model="queryParams.dictWfState"
          placeholder="请输入流程状态"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="开票人" prop="refInvoiceMan">
        <el-input
          v-model="queryParams.refInvoiceMan"
          placeholder="请输入开票人"
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
      <el-form-item label="${comment}" prop="deleted">
        <el-input
          v-model="queryParams.deleted"
          placeholder="请输入${comment}"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="娴佺▼ID" prop="orderId">
        <el-input
          v-model="queryParams.orderId"
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
          v-hasPermi="['system:invoice:add']"
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
          v-hasPermi="['system:invoice:edit']"
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
          v-hasPermi="['system:invoice:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:invoice:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="invoiceList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="${comment}" align="center" prop="refCustomerCode" />
      <el-table-column label="开票客户" align="center" prop="refCustomerName" />
      <el-table-column label="${comment}" align="center" prop="refProjectCode" />
      <el-table-column label="开票项目" align="center" prop="refProjectName" />
      <el-table-column label="开票类型" align="center" prop="dictType" />
      <el-table-column label="开票单位名称" align="center" prop="invoiceCompanyName" />
      <el-table-column label="纳税人识别号" align="center" prop="invoiceTaxNo" />
      <el-table-column label="地址" align="center" prop="address" />
      <el-table-column label="电话" align="center" prop="telephone" />
      <el-table-column label="开户行" align="center" prop="dictBank" />
      <el-table-column label="开户行账号" align="center" prop="bankCardNo" />
      <el-table-column label="开票金额" align="center" prop="invoiceMoney" />
      <el-table-column label="开票审批状态" align="center" prop="dictInvoiceState" />
      <el-table-column label="流程状态" align="center" prop="dictWfState" />
      <el-table-column label="开票人" align="center" prop="refInvoiceMan" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="${comment}" align="center" prop="createId" />
      <el-table-column label="${comment}" align="center" prop="updateId" />
      <el-table-column label="${comment}" align="center" prop="deleted" />
      <el-table-column label="${comment}" align="center" prop="orderId" />
      <el-table-column label="${comment}" align="center" prop="orderStatus" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:invoice:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:invoice:remove']"
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

    <!-- 添加或修改项目开票对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="${comment}" prop="refCustomerCode">
          <el-input v-model="form.refCustomerCode" placeholder="请输入${comment}" />
        </el-form-item>
        <el-form-item label="开票客户" prop="refCustomerName">
          <el-input v-model="form.refCustomerName" placeholder="请输入开票客户" />
        </el-form-item>
        <el-form-item label="${comment}" prop="refProjectCode">
          <el-input v-model="form.refProjectCode" placeholder="请输入${comment}" />
        </el-form-item>
        <el-form-item label="开票项目" prop="refProjectName">
          <el-input v-model="form.refProjectName" placeholder="请输入开票项目" />
        </el-form-item>
        <el-form-item label="开票单位名称" prop="invoiceCompanyName">
          <el-input v-model="form.invoiceCompanyName" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="纳税人识别号" prop="invoiceTaxNo">
          <el-input v-model="form.invoiceTaxNo" placeholder="请输入纳税人识别号" />
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="form.address" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="电话" prop="telephone">
          <el-input v-model="form.telephone" placeholder="请输入电话" />
        </el-form-item>
        <el-form-item label="开户行" prop="dictBank">
          <el-input v-model="form.dictBank" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="开户行账号" prop="bankCardNo">
          <el-input v-model="form.bankCardNo" placeholder="请输入开户行账号" />
        </el-form-item>
        <el-form-item label="开票金额" prop="invoiceMoney">
          <el-input v-model="form.invoiceMoney" placeholder="请输入开票金额" />
        </el-form-item>
        <el-form-item label="开票审批状态" prop="dictInvoiceState">
          <el-input v-model="form.dictInvoiceState" placeholder="请输入开票审批状态" />
        </el-form-item>
        <el-form-item label="流程状态" prop="dictWfState">
          <el-input v-model="form.dictWfState" placeholder="请输入流程状态" />
        </el-form-item>
        <el-form-item label="开票人" prop="refInvoiceMan">
          <el-input v-model="form.refInvoiceMan" placeholder="请输入开票人" />
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
        <el-form-item label="${comment}" prop="deleted">
          <el-input v-model="form.deleted" placeholder="请输入${comment}" />
        </el-form-item>
        <el-form-item label="娴佺▼ID" prop="orderId">
          <el-input v-model="form.orderId" placeholder="请输入${comment}" />
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
import { listInvoice, getInvoice, delInvoice, addInvoice, updateInvoice } from "@/api/qixing/invoice";

export default {
  name: "Invoice",
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
      // 项目开票表格数据
      invoiceList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        refCustomerCode: null,
        refCustomerName: null,
        refProjectCode: null,
        refProjectName: null,
        dictType: null,
        invoiceCompanyName: null,
        invoiceTaxNo: null,
        address: null,
        telephone: null,
        dictBank: null,
        bankCardNo: null,
        invoiceMoney: null,
        dictInvoiceState: null,
        dictWfState: null,
        refInvoiceMan: null,
        createId: null,
        updateId: null,
        deleted: null,
        orderId: null,
        orderStatus: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        refCustomerName: [
          { required: true, message: "开票客户不能为空", trigger: "blur" }
        ],
        refProjectName: [
          { required: true, message: "开票项目不能为空", trigger: "blur" }
        ],
        dictType: [
          { required: true, message: "开票类型不能为空", trigger: "change" }
        ],
        invoiceCompanyName: [
          { required: true, message: "开票单位名称不能为空", trigger: "blur" }
        ],
        invoiceTaxNo: [
          { required: true, message: "纳税人识别号不能为空", trigger: "blur" }
        ],
        dictBank: [
          { required: true, message: "开户行不能为空", trigger: "blur" }
        ],
        bankCardNo: [
          { required: true, message: "开户行账号不能为空", trigger: "blur" }
        ],
        invoiceMoney: [
          { required: true, message: "开票金额不能为空", trigger: "blur" }
        ],
        dictInvoiceState: [
          { required: true, message: "开票审批状态不能为空", trigger: "blur" }
        ],
        refInvoiceMan: [
          { required: true, message: "开票人不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询项目开票列表 */
    getList() {
      this.loading = true;
      listInvoice(this.queryParams).then(response => {
        this.invoiceList = response.rows;
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
        refCustomerCode: null,
        refCustomerName: null,
        refProjectCode: null,
        refProjectName: null,
        dictType: null,
        invoiceCompanyName: null,
        invoiceTaxNo: null,
        address: null,
        telephone: null,
        dictBank: null,
        bankCardNo: null,
        invoiceMoney: null,
        dictInvoiceState: null,
        dictWfState: null,
        refInvoiceMan: null,
        remark: null,
        createTime: null,
        createId: null,
        updateTime: null,
        updateId: null,
        deleted: null,
        orderId: null,
        orderStatus: null
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
      this.title = "添加项目开票";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getInvoice(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改项目开票";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateInvoice(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addInvoice(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除项目开票编号为"' + ids + '"的数据项？').then(function() {
        return delInvoice(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/invoice/export', {
        ...this.queryParams
      }, `invoice_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
