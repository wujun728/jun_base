<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="${comment}" prop="refProjectCode">
        <el-input
          v-model="queryParams.refProjectCode"
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
      <el-form-item label="项目计划标题" prop="planName">
        <el-input
          v-model="queryParams.planName"
          placeholder="请输入项目计划标题"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="项目计划开始时间" prop="planTimeStart">
        <el-date-picker clearable
          v-model="queryParams.planTimeStart"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择项目计划开始时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="项目计划结束时间" prop="planTimeEnd">
        <el-date-picker clearable
          v-model="queryParams.planTimeEnd"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择项目计划结束时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="工期(人天)" prop="planDates">
        <el-input
          v-model="queryParams.planDates"
          placeholder="请输入工期(人天)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="项目计划交付日期" prop="planGivenTime">
        <el-date-picker clearable
          v-model="queryParams.planGivenTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择项目计划交付日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="项目计划完成天数" prop="planFinashDays">
        <el-input
          v-model="queryParams.planFinashDays"
          placeholder="请输入项目计划完成天数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="项目实际完成天数" prop="planFinashDays2">
        <el-input
          v-model="queryParams.planFinashDays2"
          placeholder="请输入项目实际完成天数"
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
          v-hasPermi="['system:plan:add']"
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
          v-hasPermi="['system:plan:edit']"
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
          v-hasPermi="['system:plan:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:plan:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="planList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="${comment}" align="center" prop="id" />
      <el-table-column label="${comment}" align="center" prop="refProjectCode" />
      <el-table-column label="项目名称" align="center" prop="refProjectName" />
      <el-table-column label="项目计划标题" align="center" prop="planName" />
      <el-table-column label="项目计划详细描述" align="center" prop="planDetail" />
      <el-table-column label="项目计划开始时间" align="center" prop="planTimeStart" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.planTimeStart, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="项目计划结束时间" align="center" prop="planTimeEnd" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.planTimeEnd, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="工期(人天)" align="center" prop="planDates" />
      <el-table-column label="项目计划交付日期" align="center" prop="planGivenTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.planGivenTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="项目计划完成天数" align="center" prop="planFinashDays" />
      <el-table-column label="项目实际完成天数" align="center" prop="planFinashDays2" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="${comment}" align="center" prop="creator" />
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
            v-hasPermi="['system:plan:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:plan:remove']"
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

    <!-- 添加或修改项目计划对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="${comment}" prop="refProjectCode">
          <el-input v-model="form.refProjectCode" placeholder="请输入${comment}" />
        </el-form-item>
        <el-form-item label="项目名称" prop="refProjectName">
          <el-input v-model="form.refProjectName" placeholder="请输入项目名称" />
        </el-form-item>
        <el-form-item label="项目计划标题" prop="planName">
          <el-input v-model="form.planName" placeholder="请输入项目计划标题" />
        </el-form-item>
        <el-form-item label="项目计划详细描述" prop="planDetail">
          <el-input v-model="form.planDetail" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="项目计划开始时间" prop="planTimeStart">
          <el-date-picker clearable
            v-model="form.planTimeStart"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择项目计划开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="项目计划结束时间" prop="planTimeEnd">
          <el-date-picker clearable
            v-model="form.planTimeEnd"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择项目计划结束时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="工期(人天)" prop="planDates">
          <el-input v-model="form.planDates" placeholder="请输入工期(人天)" />
        </el-form-item>
        <el-form-item label="项目计划交付日期" prop="planGivenTime">
          <el-date-picker clearable
            v-model="form.planGivenTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择项目计划交付日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="项目计划完成天数" prop="planFinashDays">
          <el-input v-model="form.planFinashDays" placeholder="请输入项目计划完成天数" />
        </el-form-item>
        <el-form-item label="项目实际完成天数" prop="planFinashDays2">
          <el-input v-model="form.planFinashDays2" placeholder="请输入项目实际完成天数" />
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
import { listPlan, getPlan, delPlan, addPlan, updatePlan } from "@/api/qixing/plan";

export default {
  name: "Plan",
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
      // 项目计划表格数据
      planList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        refProjectCode: null,
        refProjectName: null,
        planName: null,
        planDetail: null,
        planTimeStart: null,
        planTimeEnd: null,
        planDates: null,
        planGivenTime: null,
        planFinashDays: null,
        planFinashDays2: null,
        creator: null,
        createId: null,
        updateId: null,
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
        planName: [
          { required: true, message: "项目计划标题不能为空", trigger: "blur" }
        ],
        planDetail: [
          { required: true, message: "项目计划详细描述不能为空", trigger: "blur" }
        ],
        planTimeStart: [
          { required: true, message: "项目计划开始时间不能为空", trigger: "blur" }
        ],
        planTimeEnd: [
          { required: true, message: "项目计划结束时间不能为空", trigger: "blur" }
        ],
        planDates: [
          { required: true, message: "工期(人天)不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询项目计划列表 */
    getList() {
      this.loading = true;
      listPlan(this.queryParams).then(response => {
        this.planList = response.rows;
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
        refProjectCode: null,
        refProjectName: null,
        planName: null,
        planDetail: null,
        planTimeStart: null,
        planTimeEnd: null,
        planDates: null,
        planGivenTime: null,
        planFinashDays: null,
        planFinashDays2: null,
        remark: null,
        creator: null,
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
      this.title = "添加项目计划";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getPlan(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改项目计划";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updatePlan(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addPlan(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除项目计划编号为"' + ids + '"的数据项？').then(function() {
        return delPlan(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/plan/export', {
        ...this.queryParams
      }, `plan_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
