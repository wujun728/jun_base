<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="候选人" prop="refPeopleName">
        <el-input
          v-model="queryParams.refPeopleName"
          placeholder="请输入候选人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="面试时间" prop="ivTime">
        <el-date-picker clearable
          v-model="queryParams.ivTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择面试时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="面试官名称" prop="refIvUsername">
        <el-input
          v-model="queryParams.refIvUsername"
          placeholder="请输入面试官名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="面试官电话" prop="ivPhone">
        <el-input
          v-model="queryParams.ivPhone"
          placeholder="请输入面试官电话"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="面试结果" prop="dictResult">
        <el-input
          v-model="queryParams.dictResult"
          placeholder="请输入面试结果"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="工作岗位" prop="jobName">
        <el-input
          v-model="queryParams.jobName"
          placeholder="请输入工作岗位"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="工作年限" prop="workYear">
        <el-input
          v-model="queryParams.workYear"
          placeholder="请输入工作年限"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="工作地点" prop="workLocation">
        <el-input
          v-model="queryParams.workLocation"
          placeholder="请输入工作地点"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="期望薪资" prop="jobMoney">
        <el-input
          v-model="queryParams.jobMoney"
          placeholder="请输入期望薪资"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="到岗时间" prop="getInCompayTime">
        <el-date-picker clearable
          v-model="queryParams.getInCompayTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择到岗时间">
        </el-date-picker>
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
          v-hasPermi="['system:interview:add']"
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
          v-hasPermi="['system:interview:edit']"
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
          v-hasPermi="['system:interview:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:interview:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="interviewList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="候选人" align="center" prop="refPeopleName" />
      <el-table-column label="面试时间" align="center" prop="ivTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.ivTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="面试类型" align="center" prop="dictIvType" />
      <el-table-column label="面试官名称" align="center" prop="refIvUsername" />
      <el-table-column label="面试官电话" align="center" prop="ivPhone" />
      <el-table-column label="面试结果" align="center" prop="dictResult" />
      <el-table-column label="面试评价" align="center" prop="ivEvaluate" />
      <el-table-column label="工作岗位" align="center" prop="jobName" />
      <el-table-column label="工作描述" align="center" prop="jobDesc" />
      <el-table-column label="工作年限" align="center" prop="workYear" />
      <el-table-column label="工作地点" align="center" prop="workLocation" />
      <el-table-column label="工作内容" align="center" prop="workContent" />
      <el-table-column label="期望薪资" align="center" prop="jobMoney" />
      <el-table-column label="到岗时间" align="center" prop="getInCompayTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.getInCompayTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="离职原因" align="center" prop="outJobDesc" />
      <el-table-column label="职业技能特长" align="center" prop="jobSkill" />
      <el-table-column label="候选人优缺点" align="center" prop="jobInterview" />
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
            v-hasPermi="['system:interview:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:interview:remove']"
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

    <!-- 添加或修改面试汇总对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="候选人" prop="refPeopleName">
          <el-input v-model="form.refPeopleName" placeholder="请输入候选人" />
        </el-form-item>
        <el-form-item label="面试时间" prop="ivTime">
          <el-date-picker clearable
            v-model="form.ivTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择面试时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="面试官名称" prop="refIvUsername">
          <el-input v-model="form.refIvUsername" placeholder="请输入面试官名称" />
        </el-form-item>
        <el-form-item label="面试官电话" prop="ivPhone">
          <el-input v-model="form.ivPhone" placeholder="请输入面试官电话" />
        </el-form-item>
        <el-form-item label="面试结果" prop="dictResult">
          <el-input v-model="form.dictResult" placeholder="请输入面试结果" />
        </el-form-item>
        <el-form-item label="面试评价" prop="ivEvaluate">
          <el-input v-model="form.ivEvaluate" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="工作岗位" prop="jobName">
          <el-input v-model="form.jobName" placeholder="请输入工作岗位" />
        </el-form-item>
        <el-form-item label="工作描述" prop="jobDesc">
          <el-input v-model="form.jobDesc" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="工作年限" prop="workYear">
          <el-input v-model="form.workYear" placeholder="请输入工作年限" />
        </el-form-item>
        <el-form-item label="工作地点" prop="workLocation">
          <el-input v-model="form.workLocation" placeholder="请输入工作地点" />
        </el-form-item>
        <el-form-item label="工作内容">
          <editor v-model="form.workContent" :min-height="192"/>
        </el-form-item>
        <el-form-item label="期望薪资" prop="jobMoney">
          <el-input v-model="form.jobMoney" placeholder="请输入期望薪资" />
        </el-form-item>
        <el-form-item label="到岗时间" prop="getInCompayTime">
          <el-date-picker clearable
            v-model="form.getInCompayTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择到岗时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="离职原因" prop="outJobDesc">
          <el-input v-model="form.outJobDesc" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="职业技能特长" prop="jobSkill">
          <el-input v-model="form.jobSkill" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="候选人优缺点" prop="jobInterview">
          <el-input v-model="form.jobInterview" type="textarea" placeholder="请输入内容" />
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
import { listInterview, getInterview, delInterview, addInterview, updateInterview } from "@/api/qixing/interview";

export default {
  name: "Interview",
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
      // 面试汇总表格数据
      interviewList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        refPeopleName: null,
        ivTime: null,
        dictIvType: null,
        refIvUsername: null,
        ivPhone: null,
        dictResult: null,
        ivEvaluate: null,
        jobName: null,
        jobDesc: null,
        workYear: null,
        workLocation: null,
        workContent: null,
        jobMoney: null,
        getInCompayTime: null,
        outJobDesc: null,
        jobSkill: null,
        jobInterview: null,
        createId: null,
        updateId: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        refPeopleName: [
          { required: true, message: "候选人不能为空", trigger: "blur" }
        ],
        ivTime: [
          { required: true, message: "面试时间不能为空", trigger: "blur" }
        ],
        dictIvType: [
          { required: true, message: "面试类型不能为空", trigger: "change" }
        ],
        refIvUsername: [
          { required: true, message: "面试官名称不能为空", trigger: "blur" }
        ],
        ivPhone: [
          { required: true, message: "面试官电话不能为空", trigger: "blur" }
        ],
        ivEvaluate: [
          { required: true, message: "面试评价不能为空", trigger: "blur" }
        ],
        jobInterview: [
          { required: true, message: "候选人优缺点不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询面试汇总列表 */
    getList() {
      this.loading = true;
      listInterview(this.queryParams).then(response => {
        this.interviewList = response.rows;
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
        refPeopleName: null,
        ivTime: null,
        dictIvType: null,
        refIvUsername: null,
        ivPhone: null,
        dictResult: null,
        ivEvaluate: null,
        jobName: null,
        jobDesc: null,
        workYear: null,
        workLocation: null,
        workContent: null,
        jobMoney: null,
        getInCompayTime: null,
        outJobDesc: null,
        jobSkill: null,
        jobInterview: null,
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
      this.title = "添加面试汇总";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getInterview(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改面试汇总";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateInterview(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addInterview(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除面试汇总编号为"' + ids + '"的数据项？').then(function() {
        return delInterview(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/interview/export', {
        ...this.queryParams
      }, `interview_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
