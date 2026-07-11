<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="日报标题" prop="planName">
        <el-input
          v-model="queryParams.planName"
          placeholder="请输入日报标题"
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
      <el-form-item label="日期" prop="dailyDate">
        <el-date-picker clearable
          v-model="queryParams.dailyDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="投入工时" prop="costTime">
        <el-input
          v-model="queryParams.costTime"
          placeholder="请输入投入工时"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="风险级别" prop="dictRisk">
        <el-input
          v-model="queryParams.dictRisk"
          placeholder="请输入风险级别"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否求助" prop="dictAskHelp">
        <el-input
          v-model="queryParams.dictAskHelp"
          placeholder="请输入是否求助"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否延迟" prop="dictIsDelay">
        <el-input
          v-model="queryParams.dictIsDelay"
          placeholder="请输入是否延迟"
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
          v-hasPermi="['system:daily:add']"
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
          v-hasPermi="['system:daily:edit']"
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
          v-hasPermi="['system:daily:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:daily:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="dailyList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="日报标题" align="center" prop="planName" />
      <el-table-column label="${comment}" align="center" prop="refProjectCode" />
      <el-table-column label="项目名称" align="center" prop="refProjectName" />
      <el-table-column label="日报周报" align="center" prop="dictDailyType" />
      <el-table-column label="日期" align="center" prop="dailyDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.dailyDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="工作内容描述" align="center" prop="dailyDetail" />
      <el-table-column label="投入工时" align="center" prop="costTime" />
      <el-table-column label="风险级别" align="center" prop="dictRisk" />
      <el-table-column label="是否求助" align="center" prop="dictAskHelp" />
      <el-table-column label="是否延迟" align="center" prop="dictIsDelay" />
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
            v-hasPermi="['system:daily:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:daily:remove']"
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

    <!-- 添加或修改项目日报周报对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="日报标题" prop="planName">
          <el-input v-model="form.planName" placeholder="请输入日报标题" />
        </el-form-item>
        <el-form-item label="${comment}" prop="refProjectCode">
          <el-input v-model="form.refProjectCode" placeholder="请输入${comment}" />
        </el-form-item>
        <el-form-item label="项目名称" prop="refProjectName">
          <el-input v-model="form.refProjectName" placeholder="请输入项目名称" />
        </el-form-item>
        <el-form-item label="日期" prop="dailyDate">
          <el-date-picker clearable
            v-model="form.dailyDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="工作内容描述" prop="dailyDetail">
          <el-input v-model="form.dailyDetail" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="投入工时" prop="costTime">
          <el-input v-model="form.costTime" placeholder="请输入投入工时" />
        </el-form-item>
        <el-form-item label="风险级别" prop="dictRisk">
          <el-input v-model="form.dictRisk" placeholder="请输入风险级别" />
        </el-form-item>
        <el-form-item label="是否求助" prop="dictAskHelp">
          <el-input v-model="form.dictAskHelp" placeholder="请输入是否求助" />
        </el-form-item>
        <el-form-item label="是否延迟" prop="dictIsDelay">
          <el-input v-model="form.dictIsDelay" placeholder="请输入是否延迟" />
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
import { listDaily, getDaily, delDaily, addDaily, updateDaily } from "@/api/qixing/daily";

export default {
  name: "Daily",
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
      // 项目日报周报表格数据
      dailyList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        planName: null,
        refProjectCode: null,
        refProjectName: null,
        dictDailyType: null,
        dailyDate: null,
        dailyDetail: null,
        costTime: null,
        dictRisk: null,
        dictAskHelp: null,
        dictIsDelay: null,
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
        planName: [
          { required: true, message: "日报标题不能为空", trigger: "blur" }
        ],
        refProjectName: [
          { required: true, message: "项目名称不能为空", trigger: "blur" }
        ],
        dailyDate: [
          { required: true, message: "日期不能为空", trigger: "blur" }
        ],
        dailyDetail: [
          { required: true, message: "工作内容描述不能为空", trigger: "blur" }
        ],
        costTime: [
          { required: true, message: "投入工时不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询项目日报周报列表 */
    getList() {
      this.loading = true;
      listDaily(this.queryParams).then(response => {
        this.dailyList = response.rows;
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
        planName: null,
        refProjectCode: null,
        refProjectName: null,
        dictDailyType: null,
        dailyDate: null,
        dailyDetail: null,
        costTime: null,
        dictRisk: null,
        dictAskHelp: null,
        dictIsDelay: null,
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
      this.title = "添加项目日报周报";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getDaily(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改项目日报周报";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateDaily(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addDaily(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除项目日报周报编号为"' + ids + '"的数据项？').then(function() {
        return delDaily(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/daily/export', {
        ...this.queryParams
      }, `daily_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
