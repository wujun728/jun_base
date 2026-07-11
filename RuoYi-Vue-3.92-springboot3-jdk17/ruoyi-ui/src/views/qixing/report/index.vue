<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="报告名称" prop="reportName">
        <el-input
          v-model="queryParams.reportName"
          placeholder="请输入报告名称"
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
      <el-form-item label="项目名称" prop="refProjectName">
        <el-input
          v-model="queryParams.refProjectName"
          placeholder="请输入项目名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="报告输出时间" prop="reportTime">
        <el-date-picker clearable
          v-model="queryParams.reportTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择报告输出时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="报告输出责任人" prop="refReportBy">
        <el-input
          v-model="queryParams.refReportBy"
          placeholder="请输入报告输出责任人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="报告完成天数" prop="reportFinashDays">
        <el-input
          v-model="queryParams.reportFinashDays"
          placeholder="请输入报告完成天数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建人" prop="creator">
        <el-input
          v-model="queryParams.creator"
          placeholder="请输入创建人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="报告文号" prop="reportnumCode">
        <el-input
          v-model="queryParams.reportnumCode"
          placeholder="请输入报告文号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建人" prop="createId">
        <el-input
          v-model="queryParams.createId"
          placeholder="请输入创建人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="更新人" prop="updateId">
        <el-input
          v-model="queryParams.updateId"
          placeholder="请输入更新人"
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
          v-hasPermi="['system:report:add']"
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
          v-hasPermi="['system:report:edit']"
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
          v-hasPermi="['system:report:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:report:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="reportList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="报告ID" align="center" prop="id" />
      <el-table-column label="报告名称" align="center" prop="reportName" />
      <el-table-column label="${comment}" align="center" prop="refProjectCode" />
      <el-table-column label="项目名称" align="center" prop="refProjectName" />
      <el-table-column label="报告类型" align="center" prop="dictReportType" />
      <el-table-column label="报告详细描述" align="center" prop="reportDetail" />
      <el-table-column label="客户原始述求" align="center" prop="customerReq" />
      <el-table-column label="报告输出时间" align="center" prop="reportTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.reportTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="报告输出责任人" align="center" prop="refReportBy" />
      <el-table-column label="报告完成天数" align="center" prop="reportFinashDays" />
      <el-table-column label="报告状态" align="center" prop="dictReportStatus" />
      <el-table-column label="创建人" align="center" prop="creator" />
      <el-table-column label="报告文号" align="center" prop="reportnumCode" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="创建人" align="center" prop="createId" />
      <el-table-column label="更新人" align="center" prop="updateId" />
      <el-table-column label="${comment}" align="center" prop="orderId" />
      <el-table-column label="${comment}" align="center" prop="orderStatus" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:report:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:report:remove']"
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

    <!-- 添加或修改项目报告对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="报告名称" prop="reportName">
          <el-input v-model="form.reportName" placeholder="请输入报告名称" />
        </el-form-item>
        <el-form-item label="${comment}" prop="refProjectCode">
          <el-input v-model="form.refProjectCode" placeholder="请输入${comment}" />
        </el-form-item>
        <el-form-item label="项目名称" prop="refProjectName">
          <el-input v-model="form.refProjectName" placeholder="请输入项目名称" />
        </el-form-item>
        <el-form-item label="报告详细描述" prop="reportDetail">
          <el-input v-model="form.reportDetail" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="客户原始述求" prop="customerReq">
          <el-input v-model="form.customerReq" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="报告输出时间" prop="reportTime">
          <el-date-picker clearable
            v-model="form.reportTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择报告输出时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="报告输出责任人" prop="refReportBy">
          <el-input v-model="form.refReportBy" placeholder="请输入报告输出责任人" />
        </el-form-item>
        <el-form-item label="报告完成天数" prop="reportFinashDays">
          <el-input v-model="form.reportFinashDays" placeholder="请输入报告完成天数" />
        </el-form-item>
        <el-form-item label="创建人" prop="creator">
          <el-input v-model="form.creator" placeholder="请输入创建人" />
        </el-form-item>
        <el-form-item label="报告文号" prop="reportnumCode">
          <el-input v-model="form.reportnumCode" placeholder="请输入报告文号" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="创建人" prop="createId">
          <el-input v-model="form.createId" placeholder="请输入创建人" />
        </el-form-item>
        <el-form-item label="更新人" prop="updateId">
          <el-input v-model="form.updateId" placeholder="请输入更新人" />
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
import { listReport, getReport, delReport, addReport, updateReport } from "@/api/qixing/report";

export default {
  name: "Report",
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
      // 项目报告表格数据
      reportList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        reportName: null,
        refProjectCode: null,
        refProjectName: null,
        dictReportType: null,
        reportDetail: null,
        customerReq: null,
        reportTime: null,
        refReportBy: null,
        reportFinashDays: null,
        dictReportStatus: null,
        creator: null,
        reportnumCode: null,
        createId: null,
        updateId: null,
        orderId: null,
        orderStatus: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        reportName: [
          { required: true, message: "报告名称不能为空", trigger: "blur" }
        ],
        refProjectName: [
          { required: true, message: "项目名称不能为空", trigger: "blur" }
        ],
        dictReportType: [
          { required: true, message: "报告类型不能为空", trigger: "change" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询项目报告列表 */
    getList() {
      this.loading = true;
      listReport(this.queryParams).then(response => {
        this.reportList = response.rows;
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
        reportName: null,
        refProjectCode: null,
        refProjectName: null,
        dictReportType: null,
        reportDetail: null,
        customerReq: null,
        reportTime: null,
        refReportBy: null,
        reportFinashDays: null,
        dictReportStatus: null,
        creator: null,
        reportnumCode: null,
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
      this.title = "添加项目报告";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getReport(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改项目报告";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateReport(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addReport(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除项目报告编号为"' + ids + '"的数据项？').then(function() {
        return delReport(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/report/export', {
        ...this.queryParams
      }, `report_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
