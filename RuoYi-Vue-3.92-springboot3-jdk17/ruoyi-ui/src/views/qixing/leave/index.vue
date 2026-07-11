<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="员工工号" prop="refUsercoce">
        <el-input
          v-model="queryParams.refUsercoce"
          placeholder="请输入员工工号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="员工姓名" prop="refUsername">
        <el-input
          v-model="queryParams.refUsername"
          placeholder="请输入员工姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="员工部门" prop="refUserdept">
        <el-input
          v-model="queryParams.refUserdept"
          placeholder="请输入员工部门"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="请假日期" prop="leaveDate">
        <el-input
          v-model="queryParams.leaveDate"
          placeholder="请输入请假日期"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="请假结束日期" prop="leaveDateEnd">
        <el-date-picker clearable
          v-model="queryParams.leaveDateEnd"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择请假结束日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="请假小时数" prop="leaveHours">
        <el-input
          v-model="queryParams.leaveHours"
          placeholder="请输入请假小时数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="废弃" prop="leaveHours2">
        <el-input
          v-model="queryParams.leaveHours2"
          placeholder="请输入废弃"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="废弃" prop="refCurrTodoPerson">
        <el-input
          v-model="queryParams.refCurrTodoPerson"
          placeholder="请输入废弃"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="填报人" prop="creator">
        <el-input
          v-model="queryParams.creator"
          placeholder="请输入填报人"
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
      <el-form-item label="娴佺▼ID" prop="orderId">
        <el-input
          v-model="queryParams.orderId"
          placeholder="请输入${comment}"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="${comment}" prop="orderState">
        <el-input
          v-model="queryParams.orderState"
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
          v-hasPermi="['system:leave:add']"
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
          v-hasPermi="['system:leave:edit']"
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
          v-hasPermi="['system:leave:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:leave:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="leaveList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="员工工号" align="center" prop="refUsercoce" />
      <el-table-column label="员工姓名" align="center" prop="refUsername" />
      <el-table-column label="员工部门" align="center" prop="refUserdept" />
      <el-table-column label="请假日期" align="center" prop="leaveDate" />
      <el-table-column label="请假结束日期" align="center" prop="leaveDateEnd" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.leaveDateEnd, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="请假类型" align="center" prop="dictLeanveType" />
      <el-table-column label="请假原因" align="center" prop="leaveDesc" />
      <el-table-column label="请假小时数" align="center" prop="leaveHours" />
      <el-table-column label="废弃" align="center" prop="leaveHours2" />
      <el-table-column label="废弃" align="center" prop="dictApproveStatus" />
      <el-table-column label="废弃" align="center" prop="refCurrTodoPerson" />
      <el-table-column label="填报人" align="center" prop="creator" />
      <el-table-column label="${comment}" align="center" prop="createId" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="${comment}" align="center" prop="updateId" />
      <el-table-column label="${comment}" align="center" prop="orderId" />
      <el-table-column label="${comment}" align="center" prop="orderState" />
      <el-table-column label="${comment}" align="center" prop="orderStatus" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:leave:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:leave:remove']"
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

    <!-- 添加或修改员工请假对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="员工工号" prop="refUsercoce">
          <el-input v-model="form.refUsercoce" placeholder="请输入员工工号" />
        </el-form-item>
        <el-form-item label="员工姓名" prop="refUsername">
          <el-input v-model="form.refUsername" placeholder="请输入员工姓名" />
        </el-form-item>
        <el-form-item label="员工部门" prop="refUserdept">
          <el-input v-model="form.refUserdept" placeholder="请输入员工部门" />
        </el-form-item>
        <el-form-item label="请假日期" prop="leaveDate">
          <el-input v-model="form.leaveDate" placeholder="请输入请假日期" />
        </el-form-item>
        <el-form-item label="请假结束日期" prop="leaveDateEnd">
          <el-date-picker clearable
            v-model="form.leaveDateEnd"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择请假结束日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="请假原因" prop="leaveDesc">
          <el-input v-model="form.leaveDesc" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="请假小时数" prop="leaveHours">
          <el-input v-model="form.leaveHours" placeholder="请输入请假小时数" />
        </el-form-item>
        <el-form-item label="废弃" prop="leaveHours2">
          <el-input v-model="form.leaveHours2" placeholder="请输入废弃" />
        </el-form-item>
        <el-form-item label="废弃" prop="refCurrTodoPerson">
          <el-input v-model="form.refCurrTodoPerson" placeholder="请输入废弃" />
        </el-form-item>
        <el-form-item label="填报人" prop="creator">
          <el-input v-model="form.creator" placeholder="请输入填报人" />
        </el-form-item>
        <el-form-item label="鍒涘缓浜篒D" prop="createId">
          <el-input v-model="form.createId" placeholder="请输入${comment}" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="鏇存柊浜篒D" prop="updateId">
          <el-input v-model="form.updateId" placeholder="请输入${comment}" />
        </el-form-item>
        <el-form-item label="娴佺▼ID" prop="orderId">
          <el-input v-model="form.orderId" placeholder="请输入${comment}" />
        </el-form-item>
        <el-form-item label="${comment}" prop="orderState">
          <el-input v-model="form.orderState" placeholder="请输入${comment}" />
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
import { listLeave, getLeave, delLeave, addLeave, updateLeave } from "@/api/qixing/leave";

export default {
  name: "Leave",
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
      // 员工请假表格数据
      leaveList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        refUsercoce: null,
        refUsername: null,
        refUserdept: null,
        leaveDate: null,
        leaveDateEnd: null,
        dictLeanveType: null,
        leaveDesc: null,
        leaveHours: null,
        leaveHours2: null,
        dictApproveStatus: null,
        refCurrTodoPerson: null,
        creator: null,
        createId: null,
        updateId: null,
        orderId: null,
        orderState: null,
        orderStatus: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        refUsercoce: [
          { required: true, message: "员工工号不能为空", trigger: "blur" }
        ],
        refUsername: [
          { required: true, message: "员工姓名不能为空", trigger: "blur" }
        ],
        refUserdept: [
          { required: true, message: "员工部门不能为空", trigger: "blur" }
        ],
        leaveDate: [
          { required: true, message: "请假日期不能为空", trigger: "blur" }
        ],
        dictLeanveType: [
          { required: true, message: "请假类型不能为空", trigger: "change" }
        ],
        leaveDesc: [
          { required: true, message: "请假原因不能为空", trigger: "blur" }
        ],
        leaveHours: [
          { required: true, message: "请假小时数不能为空", trigger: "blur" }
        ],
        creator: [
          { required: true, message: "填报人不能为空", trigger: "blur" }
        ],
        createId: [
          { required: true, message: "$comment不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询员工请假列表 */
    getList() {
      this.loading = true;
      listLeave(this.queryParams).then(response => {
        this.leaveList = response.rows;
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
        refUsercoce: null,
        refUsername: null,
        refUserdept: null,
        leaveDate: null,
        leaveDateEnd: null,
        dictLeanveType: null,
        leaveDesc: null,
        leaveHours: null,
        leaveHours2: null,
        dictApproveStatus: null,
        refCurrTodoPerson: null,
        creator: null,
        createId: null,
        remark: null,
        createTime: null,
        updateTime: null,
        updateId: null,
        orderId: null,
        orderState: null,
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
      this.title = "添加员工请假";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getLeave(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改员工请假";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateLeave(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addLeave(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除员工请假编号为"' + ids + '"的数据项？').then(function() {
        return delLeave(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/leave/export', {
        ...this.queryParams
      }, `leave_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
