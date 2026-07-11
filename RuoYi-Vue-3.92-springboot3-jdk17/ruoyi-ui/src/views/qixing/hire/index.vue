<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="入职人员名称" prop="refJobUsername">
        <el-input
          v-model="queryParams.refJobUsername"
          placeholder="请输入入职人员名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="入职部门" prop="refJobDeptname">
        <el-input
          v-model="queryParams.refJobDeptname"
          placeholder="请输入入职部门"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="入职岗位" prop="dictJob">
        <el-input
          v-model="queryParams.dictJob"
          placeholder="请输入入职岗位"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="直属领导" prop="refEntryLeader">
        <el-input
          v-model="queryParams.refEntryLeader"
          placeholder="请输入直属领导"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="新员工导师" prop="refEntryTeach">
        <el-input
          v-model="queryParams.refEntryTeach"
          placeholder="请输入新员工导师"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="薪资" prop="money">
        <el-input
          v-model="queryParams.money"
          placeholder="请输入薪资"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="入职时间" prop="entryTime">
        <el-date-picker clearable
          v-model="queryParams.entryTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择入职时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="流程状态" prop="wfstate">
        <el-input
          v-model="queryParams.wfstate"
          placeholder="请输入流程状态"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="当前节点" prop="currNodename">
        <el-input
          v-model="queryParams.currNodename"
          placeholder="请输入当前节点"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="当前审批人" prop="currUsercode">
        <el-input
          v-model="queryParams.currUsercode"
          placeholder="请输入当前审批人"
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
          v-hasPermi="['system:hire:add']"
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
          v-hasPermi="['system:hire:edit']"
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
          v-hasPermi="['system:hire:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:hire:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="hireList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="入职人员名称" align="center" prop="refJobUsername" />
      <el-table-column label="入职部门" align="center" prop="refJobDeptname" />
      <el-table-column label="用工类型" align="center" prop="dictJobType" />
      <el-table-column label="入职岗位" align="center" prop="dictJob" />
      <el-table-column label="直属领导" align="center" prop="refEntryLeader" />
      <el-table-column label="新员工导师" align="center" prop="refEntryTeach" />
      <el-table-column label="薪资" align="center" prop="money" />
      <el-table-column label="入职时间" align="center" prop="entryTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.entryTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="流程状态" align="center" prop="wfstate" />
      <el-table-column label="当前节点" align="center" prop="currNodename" />
      <el-table-column label="当前审批人" align="center" prop="currUsercode" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="${comment}" align="center" prop="createId" />
      <el-table-column label="${comment}" align="center" prop="updateId" />
      <el-table-column label="${comment}" align="center" prop="orderId" />
      <el-table-column label="${comment}" align="center" prop="orderStatus" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:hire:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:hire:remove']"
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

    <!-- 添加或修改录用审批对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="入职人员名称" prop="refJobUsername">
          <el-input v-model="form.refJobUsername" placeholder="请输入入职人员名称" />
        </el-form-item>
        <el-form-item label="入职部门" prop="refJobDeptname">
          <el-input v-model="form.refJobDeptname" placeholder="请输入入职部门" />
        </el-form-item>
        <el-form-item label="入职岗位" prop="dictJob">
          <el-input v-model="form.dictJob" placeholder="请输入入职岗位" />
        </el-form-item>
        <el-form-item label="直属领导" prop="refEntryLeader">
          <el-input v-model="form.refEntryLeader" placeholder="请输入直属领导" />
        </el-form-item>
        <el-form-item label="新员工导师" prop="refEntryTeach">
          <el-input v-model="form.refEntryTeach" placeholder="请输入新员工导师" />
        </el-form-item>
        <el-form-item label="薪资" prop="money">
          <el-input v-model="form.money" placeholder="请输入薪资" />
        </el-form-item>
        <el-form-item label="入职时间" prop="entryTime">
          <el-date-picker clearable
            v-model="form.entryTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择入职时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="流程状态" prop="wfstate">
          <el-input v-model="form.wfstate" placeholder="请输入流程状态" />
        </el-form-item>
        <el-form-item label="当前节点" prop="currNodename">
          <el-input v-model="form.currNodename" placeholder="请输入当前节点" />
        </el-form-item>
        <el-form-item label="当前审批人" prop="currUsercode">
          <el-input v-model="form.currUsercode" placeholder="请输入当前审批人" />
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
import { listHire, getHire, delHire, addHire, updateHire } from "@/api/qixing/hire";

export default {
  name: "Hire",
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
      // 录用审批表格数据
      hireList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        refJobUsername: null,
        refJobDeptname: null,
        dictJobType: null,
        dictJob: null,
        refEntryLeader: null,
        refEntryTeach: null,
        money: null,
        entryTime: null,
        wfstate: null,
        currNodename: null,
        currUsercode: null,
        createId: null,
        updateId: null,
        orderId: null,
        orderStatus: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        refJobUsername: [
          { required: true, message: "入职人员名称不能为空", trigger: "blur" }
        ],
        refJobDeptname: [
          { required: true, message: "入职部门不能为空", trigger: "blur" }
        ],
        dictJobType: [
          { required: true, message: "用工类型不能为空", trigger: "change" }
        ],
        dictJob: [
          { required: true, message: "入职岗位不能为空", trigger: "blur" }
        ],
        refEntryLeader: [
          { required: true, message: "直属领导不能为空", trigger: "blur" }
        ],
        money: [
          { required: true, message: "薪资不能为空", trigger: "blur" }
        ],
        entryTime: [
          { required: true, message: "入职时间不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询录用审批列表 */
    getList() {
      this.loading = true;
      listHire(this.queryParams).then(response => {
        this.hireList = response.rows;
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
        refJobUsername: null,
        refJobDeptname: null,
        dictJobType: null,
        dictJob: null,
        refEntryLeader: null,
        refEntryTeach: null,
        money: null,
        entryTime: null,
        wfstate: null,
        currNodename: null,
        currUsercode: null,
        remark: null,
        createTime: null,
        createId: null,
        updateTime: null,
        updateId: null,
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
      this.title = "添加录用审批";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getHire(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改录用审批";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateHire(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addHire(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除录用审批编号为"' + ids + '"的数据项？').then(function() {
        return delHire(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/hire/export', {
        ...this.queryParams
      }, `hire_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
