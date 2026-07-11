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
      <el-form-item label="直属领导" prop="refUsername1">
        <el-input
          v-model="queryParams.refUsername1"
          placeholder="请输入直属领导"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="报道部门" prop="refJobDeptname">
        <el-input
          v-model="queryParams.refJobDeptname"
          placeholder="请输入报道部门"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="入职报告发起人" prop="username2">
        <el-input
          v-model="queryParams.username2"
          placeholder="请输入入职报告发起人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="导师" prop="refUsername2">
        <el-input
          v-model="queryParams.refUsername2"
          placeholder="请输入导师"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="试用期开始时间" prop="startTime1">
        <el-date-picker clearable
          v-model="queryParams.startTime1"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择试用期开始时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="试用期结束时间" prop="endTime1">
        <el-date-picker clearable
          v-model="queryParams.endTime1"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择试用期结束时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="合同开始时间" prop="beginTime2">
        <el-date-picker clearable
          v-model="queryParams.beginTime2"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择合同开始时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="合同结束时间" prop="endTime2">
        <el-date-picker clearable
          v-model="queryParams.endTime2"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择合同结束时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="工作地点" prop="workLocation">
        <el-input
          v-model="queryParams.workLocation"
          placeholder="请输入工作地点"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="考勤班次" prop="workmarkTimes">
        <el-input
          v-model="queryParams.workmarkTimes"
          placeholder="请输入考勤班次"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="资料是否齐全" prop="isFullEntryInfomation">
        <el-input
          v-model="queryParams.isFullEntryInfomation"
          placeholder="请输入资料是否齐全"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="入职手续是否办理完成" prop="isEntryJobFilish">
        <el-input
          v-model="queryParams.isEntryJobFilish"
          placeholder="请输入入职手续是否办理完成"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="合同是否签订" prop="isSignContract">
        <el-input
          v-model="queryParams.isSignContract"
          placeholder="请输入合同是否签订"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="附件(证件影印件+合同影印件)" prop="files">
        <el-input
          v-model="queryParams.files"
          placeholder="请输入附件(证件影印件+合同影印件)"
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
          v-hasPermi="['system:reported:add']"
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
          v-hasPermi="['system:reported:edit']"
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
          v-hasPermi="['system:reported:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:reported:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="reportedList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="入职人员名称" align="center" prop="refJobUsername" />
      <el-table-column label="直属领导" align="center" prop="refUsername1" />
      <el-table-column label="报道部门" align="center" prop="refJobDeptname" />
      <el-table-column label="入职报告发起人" align="center" prop="username2" />
      <el-table-column label="导师" align="center" prop="refUsername2" />
      <el-table-column label="试用期开始时间" align="center" prop="startTime1" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startTime1, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="试用期结束时间" align="center" prop="endTime1" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endTime1, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="合同开始时间" align="center" prop="beginTime2" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.beginTime2, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="合同结束时间" align="center" prop="endTime2" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endTime2, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="工作地点" align="center" prop="workLocation" />
      <el-table-column label="考勤班次" align="center" prop="workmarkTimes" />
      <el-table-column label="资料是否齐全" align="center" prop="isFullEntryInfomation" />
      <el-table-column label="入职手续是否办理完成" align="center" prop="isEntryJobFilish" />
      <el-table-column label="合同是否签订" align="center" prop="isSignContract" />
      <el-table-column label="附件(证件影印件+合同影印件)" align="center" prop="files" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="${comment}" align="center" prop="createId" />
      <el-table-column label="${comment}" align="center" prop="updateId" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:reported:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:reported:remove']"
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

    <!-- 添加或修改入职报道对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="入职人员名称" prop="refJobUsername">
          <el-input v-model="form.refJobUsername" placeholder="请输入入职人员名称" />
        </el-form-item>
        <el-form-item label="直属领导" prop="refUsername1">
          <el-input v-model="form.refUsername1" placeholder="请输入直属领导" />
        </el-form-item>
        <el-form-item label="报道部门" prop="refJobDeptname">
          <el-input v-model="form.refJobDeptname" placeholder="请输入报道部门" />
        </el-form-item>
        <el-form-item label="入职报告发起人" prop="username2">
          <el-input v-model="form.username2" placeholder="请输入入职报告发起人" />
        </el-form-item>
        <el-form-item label="导师" prop="refUsername2">
          <el-input v-model="form.refUsername2" placeholder="请输入导师" />
        </el-form-item>
        <el-form-item label="试用期开始时间" prop="startTime1">
          <el-date-picker clearable
            v-model="form.startTime1"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择试用期开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="试用期结束时间" prop="endTime1">
          <el-date-picker clearable
            v-model="form.endTime1"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择试用期结束时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="合同开始时间" prop="beginTime2">
          <el-date-picker clearable
            v-model="form.beginTime2"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择合同开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="合同结束时间" prop="endTime2">
          <el-date-picker clearable
            v-model="form.endTime2"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择合同结束时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="工作地点" prop="workLocation">
          <el-input v-model="form.workLocation" placeholder="请输入工作地点" />
        </el-form-item>
        <el-form-item label="考勤班次" prop="workmarkTimes">
          <el-input v-model="form.workmarkTimes" placeholder="请输入考勤班次" />
        </el-form-item>
        <el-form-item label="资料是否齐全" prop="isFullEntryInfomation">
          <el-input v-model="form.isFullEntryInfomation" placeholder="请输入资料是否齐全" />
        </el-form-item>
        <el-form-item label="入职手续是否办理完成" prop="isEntryJobFilish">
          <el-input v-model="form.isEntryJobFilish" placeholder="请输入入职手续是否办理完成" />
        </el-form-item>
        <el-form-item label="合同是否签订" prop="isSignContract">
          <el-input v-model="form.isSignContract" placeholder="请输入合同是否签订" />
        </el-form-item>
        <el-form-item label="附件(证件影印件+合同影印件)" prop="files">
          <el-input v-model="form.files" placeholder="请输入附件(证件影印件+合同影印件)" />
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
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listReported, getReported, delReported, addReported, updateReported } from "@/api/qixing/reported";

export default {
  name: "Reported",
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
      // 入职报道表格数据
      reportedList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        refJobUsername: null,
        refUsername1: null,
        refJobDeptname: null,
        username2: null,
        refUsername2: null,
        startTime1: null,
        endTime1: null,
        beginTime2: null,
        endTime2: null,
        workLocation: null,
        workmarkTimes: null,
        isFullEntryInfomation: null,
        isEntryJobFilish: null,
        isSignContract: null,
        files: null,
        createId: null,
        updateId: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        refJobUsername: [
          { required: true, message: "入职人员名称不能为空", trigger: "blur" }
        ],
        refUsername1: [
          { required: true, message: "直属领导不能为空", trigger: "blur" }
        ],
        refJobDeptname: [
          { required: true, message: "报道部门不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询入职报道列表 */
    getList() {
      this.loading = true;
      listReported(this.queryParams).then(response => {
        this.reportedList = response.rows;
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
        refUsername1: null,
        refJobDeptname: null,
        username2: null,
        refUsername2: null,
        startTime1: null,
        endTime1: null,
        beginTime2: null,
        endTime2: null,
        workLocation: null,
        workmarkTimes: null,
        isFullEntryInfomation: null,
        isEntryJobFilish: null,
        isSignContract: null,
        files: null,
        remark: null,
        createTime: null,
        createId: null,
        updateTime: null,
        updateId: null
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
      this.title = "添加入职报道";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getReported(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改入职报道";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateReported(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addReported(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除入职报道编号为"' + ids + '"的数据项？').then(function() {
        return delReported(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/reported/export', {
        ...this.queryParams
      }, `reported_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
