<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="${comment}" prop="refidProjectCodeHide">
        <el-input
          v-model="queryParams.refidProjectCodeHide"
          placeholder="请输入${comment}"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="项目名称" prop="refProjectName">
        <el-input
          v-model="queryParams.refProjectName"
          placeholder="请输入项目名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="合同状态" prop="dictContractState">
        <el-input
          v-model="queryParams.dictContractState"
          placeholder="请输入合同状态"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="合同号" prop="contractNo">
        <el-input
          v-model="queryParams.contractNo"
          placeholder="请输入合同号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="合同标题" prop="contractName">
        <el-input
          v-model="queryParams.contractName"
          placeholder="请输入合同标题"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="签约日期" prop="signTime">
        <el-date-picker clearable
          v-model="queryParams.signTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择签约日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="报告数量" prop="reportCount">
        <el-input
          v-model="queryParams.reportCount"
          placeholder="请输入报告数量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="约定书金额" prop="contractMoney">
        <el-input
          v-model="queryParams.contractMoney"
          placeholder="请输入约定书金额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="签约/承诺人" prop="signBy">
        <el-input
          v-model="queryParams.signBy"
          placeholder="请输入签约/承诺人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="执业天数" prop="contractDays">
        <el-input
          v-model="queryParams.contractDays"
          placeholder="请输入执业天数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="鍒涘缓浜? prop="creator">
        <el-input
          v-model="queryParams.creator"
          placeholder="请输入${comment}"
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
          v-hasPermi="['system:contract:add']"
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
          v-hasPermi="['system:contract:edit']"
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
          v-hasPermi="['system:contract:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:contract:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="contractList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="合同ID" align="center" prop="id" />
      <el-table-column label="${comment}" align="center" prop="refidProjectCodeHide" />
      <el-table-column label="项目名称" align="center" prop="refProjectName" />
      <el-table-column label="合同状态" align="center" prop="dictContractState" />
      <el-table-column label="合同类型" align="center" prop="dictContractType" />
      <el-table-column label="合同号" align="center" prop="contractNo" />
      <el-table-column label="合同标题" align="center" prop="contractName" />
      <el-table-column label="签约日期" align="center" prop="signTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.signTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="报告数量" align="center" prop="reportCount" />
      <el-table-column label="报告详细描述" align="center" prop="reportDetail" />
      <el-table-column label="约定书金额" align="center" prop="contractMoney" />
      <el-table-column label="签约/承诺人" align="center" prop="signBy" />
      <el-table-column label="执业天数" align="center" prop="contractDays" />
      <el-table-column label="约定书详细描述" align="center" prop="contractDetails" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="${comment}" align="center" prop="creator" />
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
            v-hasPermi="['system:contract:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:contract:remove']"
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

    <!-- 添加或修改业务约定书对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="${comment}" prop="refidProjectCodeHide">
          <el-input v-model="form.refidProjectCodeHide" placeholder="请输入${comment}" />
        </el-form-item>
        <el-form-item label="项目名称" prop="refProjectName">
          <el-input v-model="form.refProjectName" placeholder="请输入项目名称" />
        </el-form-item>
        <el-form-item label="合同状态" prop="dictContractState">
          <el-input v-model="form.dictContractState" placeholder="请输入合同状态" />
        </el-form-item>
        <el-form-item label="合同号" prop="contractNo">
          <el-input v-model="form.contractNo" placeholder="请输入合同号" />
        </el-form-item>
        <el-form-item label="合同标题" prop="contractName">
          <el-input v-model="form.contractName" placeholder="请输入合同标题" />
        </el-form-item>
        <el-form-item label="签约日期" prop="signTime">
          <el-date-picker clearable
            v-model="form.signTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择签约日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="报告数量" prop="reportCount">
          <el-input v-model="form.reportCount" placeholder="请输入报告数量" />
        </el-form-item>
        <el-form-item label="报告详细描述" prop="reportDetail">
          <el-input v-model="form.reportDetail" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="约定书金额" prop="contractMoney">
          <el-input v-model="form.contractMoney" placeholder="请输入约定书金额" />
        </el-form-item>
        <el-form-item label="签约/承诺人" prop="signBy">
          <el-input v-model="form.signBy" placeholder="请输入签约/承诺人" />
        </el-form-item>
        <el-form-item label="执业天数" prop="contractDays">
          <el-input v-model="form.contractDays" placeholder="请输入执业天数" />
        </el-form-item>
        <el-form-item label="约定书详细描述" prop="contractDetails">
          <el-input v-model="form.contractDetails" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="鍒涘缓浜? prop="creator">
          <el-input v-model="form.creator" placeholder="请输入${comment}" />
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
import { listContract, getContract, delContract, addContract, updateContract } from "@/api/qixing/contract";

export default {
  name: "Contract",
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
      // 业务约定书表格数据
      contractList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        refidProjectCodeHide: null,
        refProjectName: null,
        dictContractState: null,
        dictContractType: null,
        contractNo: null,
        contractName: null,
        signTime: null,
        reportCount: null,
        reportDetail: null,
        contractMoney: null,
        signBy: null,
        contractDays: null,
        contractDetails: null,
        creator: null,
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
        refProjectName: [
          { required: true, message: "项目名称不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询业务约定书列表 */
    getList() {
      this.loading = true;
      listContract(this.queryParams).then(response => {
        this.contractList = response.rows;
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
        refidProjectCodeHide: null,
        refProjectName: null,
        dictContractState: null,
        dictContractType: null,
        contractNo: null,
        contractName: null,
        signTime: null,
        reportCount: null,
        reportDetail: null,
        contractMoney: null,
        signBy: null,
        contractDays: null,
        contractDetails: null,
        remark: null,
        creator: null,
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
      this.title = "添加业务约定书";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getContract(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改业务约定书";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateContract(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addContract(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除业务约定书编号为"' + ids + '"的数据项？').then(function() {
        return delContract(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/contract/export', {
        ...this.queryParams
      }, `contract_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
