<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="费用编号" prop="costCode">
        <el-input
          v-model="queryParams.costCode"
          placeholder="请输入费用编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="工号" prop="usercode">
        <el-input
          v-model="queryParams.usercode"
          placeholder="请输入工号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="报销人(受款人)" prop="refUsername">
        <el-input
          v-model="queryParams.refUsername"
          placeholder="请输入报销人(受款人)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="报销人部门(走部门)" prop="deptname">
        <el-input
          v-model="queryParams.deptname"
          placeholder="请输入报销人部门(走部门)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="费用金额" prop="money">
        <el-input
          v-model="queryParams.money"
          placeholder="请输入费用金额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="报销人岗位类型" prop="userPost">
        <el-input
          v-model="queryParams.userPost"
          placeholder="请输入报销人岗位类型"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="${comment}" prop="refTodoPersonCode">
        <el-input
          v-model="queryParams.refTodoPersonCode"
          placeholder="请输入${comment}"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="办理人(可代办)" prop="refTodoPreson">
        <el-input
          v-model="queryParams.refTodoPreson"
          placeholder="请输入办理人(可代办)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="办理人部门" prop="refTodoDeptname">
        <el-input
          v-model="queryParams.refTodoDeptname"
          placeholder="请输入办理人部门"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否列入预算" prop="dictYeNo">
        <el-input
          v-model="queryParams.dictYeNo"
          placeholder="请输入是否列入预算"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否项目费用" prop="dictBelongProject">
        <el-input
          v-model="queryParams.dictBelongProject"
          placeholder="请输入是否项目费用"
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
      <el-form-item label="关联项目名称(项目费用)" prop="refProjectName">
        <el-input
          v-model="queryParams.refProjectName"
          placeholder="请输入关联项目名称(项目费用)"
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
          v-hasPermi="['system:expense:add']"
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
          v-hasPermi="['system:expense:edit']"
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
          v-hasPermi="['system:expense:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:expense:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="expenseList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="${comment}" align="center" prop="id" />
      <el-table-column label="费用编号" align="center" prop="costCode" />
      <el-table-column label="工号" align="center" prop="usercode" />
      <el-table-column label="报销人(受款人)" align="center" prop="refUsername" />
      <el-table-column label="报销人部门(走部门)" align="center" prop="deptname" />
      <el-table-column label="费用金额" align="center" prop="money" />
      <el-table-column label="货币币种" align="center" prop="dictMoneyType" />
      <el-table-column label="报销人岗位类型" align="center" prop="userPost" />
      <el-table-column label="${comment}" align="center" prop="refTodoPersonCode" />
      <el-table-column label="办理人(可代办)" align="center" prop="refTodoPreson" />
      <el-table-column label="办理人部门" align="center" prop="refTodoDeptname" />
      <el-table-column label="是否列入预算" align="center" prop="dictYeNo" />
      <el-table-column label="是否项目费用" align="center" prop="dictBelongProject" />
      <el-table-column label="${comment}" align="center" prop="refProjectCode" />
      <el-table-column label="关联项目名称(项目费用)" align="center" prop="refProjectName" />
      <el-table-column label="费用类型" align="center" prop="dictCostType" />
      <el-table-column label="费用明细" align="center" prop="costDetail" />
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
            v-hasPermi="['system:expense:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:expense:remove']"
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

    <!-- 添加或修改费用报销对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="费用编号" prop="costCode">
          <el-input v-model="form.costCode" placeholder="请输入费用编号" />
        </el-form-item>
        <el-form-item label="工号" prop="usercode">
          <el-input v-model="form.usercode" placeholder="请输入工号" />
        </el-form-item>
        <el-form-item label="报销人(受款人)" prop="refUsername">
          <el-input v-model="form.refUsername" placeholder="请输入报销人(受款人)" />
        </el-form-item>
        <el-form-item label="报销人部门(走部门)" prop="deptname">
          <el-input v-model="form.deptname" placeholder="请输入报销人部门(走部门)" />
        </el-form-item>
        <el-form-item label="费用金额" prop="money">
          <el-input v-model="form.money" placeholder="请输入费用金额" />
        </el-form-item>
        <el-form-item label="报销人岗位类型" prop="userPost">
          <el-input v-model="form.userPost" placeholder="请输入报销人岗位类型" />
        </el-form-item>
        <el-form-item label="${comment}" prop="refTodoPersonCode">
          <el-input v-model="form.refTodoPersonCode" placeholder="请输入${comment}" />
        </el-form-item>
        <el-form-item label="办理人(可代办)" prop="refTodoPreson">
          <el-input v-model="form.refTodoPreson" placeholder="请输入办理人(可代办)" />
        </el-form-item>
        <el-form-item label="办理人部门" prop="refTodoDeptname">
          <el-input v-model="form.refTodoDeptname" placeholder="请输入办理人部门" />
        </el-form-item>
        <el-form-item label="是否列入预算" prop="dictYeNo">
          <el-input v-model="form.dictYeNo" placeholder="请输入是否列入预算" />
        </el-form-item>
        <el-form-item label="是否项目费用" prop="dictBelongProject">
          <el-input v-model="form.dictBelongProject" placeholder="请输入是否项目费用" />
        </el-form-item>
        <el-form-item label="${comment}" prop="refProjectCode">
          <el-input v-model="form.refProjectCode" placeholder="请输入${comment}" />
        </el-form-item>
        <el-form-item label="关联项目名称(项目费用)" prop="refProjectName">
          <el-input v-model="form.refProjectName" placeholder="请输入关联项目名称(项目费用)" />
        </el-form-item>
        <el-form-item label="费用明细" prop="costDetail">
          <el-input v-model="form.costDetail" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
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
import { listExpense, getExpense, delExpense, addExpense, updateExpense } from "@/api/qixing/expense";

export default {
  name: "Expense",
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
      // 费用报销表格数据
      expenseList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        costCode: null,
        usercode: null,
        refUsername: null,
        deptname: null,
        money: null,
        dictMoneyType: null,
        userPost: null,
        refTodoPersonCode: null,
        refTodoPreson: null,
        refTodoDeptname: null,
        dictYeNo: null,
        dictBelongProject: null,
        refProjectCode: null,
        refProjectName: null,
        dictCostType: null,
        costDetail: null,
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
        costCode: [
          { required: true, message: "费用编号不能为空", trigger: "blur" }
        ],
        refUsername: [
          { required: true, message: "报销人(受款人)不能为空", trigger: "blur" }
        ],
        deptname: [
          { required: true, message: "报销人部门(走部门)不能为空", trigger: "blur" }
        ],
        money: [
          { required: true, message: "费用金额不能为空", trigger: "blur" }
        ],
        dictMoneyType: [
          { required: true, message: "货币币种不能为空", trigger: "change" }
        ],
        dictYeNo: [
          { required: true, message: "是否列入预算不能为空", trigger: "blur" }
        ],
        dictBelongProject: [
          { required: true, message: "是否项目费用不能为空", trigger: "blur" }
        ],
        dictCostType: [
          { required: true, message: "费用类型不能为空", trigger: "change" }
        ],
        costDetail: [
          { required: true, message: "费用明细不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询费用报销列表 */
    getList() {
      this.loading = true;
      listExpense(this.queryParams).then(response => {
        this.expenseList = response.rows;
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
        costCode: null,
        usercode: null,
        refUsername: null,
        deptname: null,
        money: null,
        dictMoneyType: null,
        userPost: null,
        refTodoPersonCode: null,
        refTodoPreson: null,
        refTodoDeptname: null,
        dictYeNo: null,
        dictBelongProject: null,
        refProjectCode: null,
        refProjectName: null,
        dictCostType: null,
        costDetail: null,
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
      this.title = "添加费用报销";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getExpense(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改费用报销";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateExpense(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addExpense(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除费用报销编号为"' + ids + '"的数据项？').then(function() {
        return delExpense(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/expense/export', {
        ...this.queryParams
      }, `expense_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
