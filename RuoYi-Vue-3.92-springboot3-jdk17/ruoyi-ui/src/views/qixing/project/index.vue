<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="项目编码" prop="projectCode">
        <el-input
          v-model="queryParams.projectCode"
          placeholder="请输入项目编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="项目名称" prop="projectName">
        <el-input
          v-model="queryParams.projectName"
          placeholder="请输入项目名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="项目类型细分" prop="dictProjectTypeSub">
        <el-input
          v-model="queryParams.dictProjectTypeSub"
          placeholder="请输入项目类型细分"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="瀹㈡埛缂栫爜" prop="refIdCuscode">
        <el-input
          v-model="queryParams.refIdCuscode"
          placeholder="请输入${comment}"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="客户(委托单位)" prop="refCusname">
        <el-input
          v-model="queryParams.refCusname"
          placeholder="请输入客户(委托单位)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="项目计划开始时间" prop="projectStarttime">
        <el-date-picker clearable
          v-model="queryParams.projectStarttime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择项目计划开始时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="项目计划结束时间" prop="projectEndtime">
        <el-date-picker clearable
          v-model="queryParams.projectEndtime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择项目计划结束时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="项目经理" prop="refProjectManager">
        <el-input
          v-model="queryParams.refProjectManager"
          placeholder="请输入项目经理"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="承接(合伙)人" prop="refUndertakePerson">
        <el-input
          v-model="queryParams.refUndertakePerson"
          placeholder="请输入承接(合伙)人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="承做(合伙)人" prop="refUndertakTpersonDo">
        <el-input
          v-model="queryParams.refUndertakTpersonDo"
          placeholder="请输入承做(合伙)人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="风险评估等级" prop="dictRiskAssessment">
        <el-input
          v-model="queryParams.dictRiskAssessment"
          placeholder="请输入风险评估等级"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="首次承接" prop="dictFirstUndertake">
        <el-input
          v-model="queryParams.dictFirstUndertake"
          placeholder="请输入首次承接"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="项目进度" prop="projectProgress">
        <el-input
          v-model="queryParams.projectProgress"
          placeholder="请输入项目进度"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="寤舵湡" prop="delay">
        <el-input
          v-model="queryParams.delay"
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
      <el-form-item label="鍒涘缓浜? prop="creator">
        <el-input
          v-model="queryParams.creator"
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
      <el-form-item label="删除标识" prop="deleted">
        <el-input
          v-model="queryParams.deleted"
          placeholder="请输入删除标识"
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
          v-hasPermi="['system:project:add']"
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
          v-hasPermi="['system:project:edit']"
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
          v-hasPermi="['system:project:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:project:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="projectList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="项目ID" align="center" prop="id" />
      <el-table-column label="项目编码" align="center" prop="projectCode" />
      <el-table-column label="项目名称" align="center" prop="projectName" />
      <el-table-column label="项目类型" align="center" prop="dictProjectType" />
      <el-table-column label="项目类型细分" align="center" prop="dictProjectTypeSub" />
      <el-table-column label="${comment}" align="center" prop="refIdCuscode" />
      <el-table-column label="客户(委托单位)" align="center" prop="refCusname" />
      <el-table-column label="${comment}" align="center" prop="projectDesc" />
      <el-table-column label="项目计划开始时间" align="center" prop="projectStarttime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.projectStarttime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="项目计划结束时间" align="center" prop="projectEndtime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.projectEndtime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="被评估单位" align="center" prop="cusnameTodo" />
      <el-table-column label="项目经理" align="center" prop="refProjectManager" />
      <el-table-column label="承接(合伙)人" align="center" prop="refUndertakePerson" />
      <el-table-column label="承做(合伙)人" align="center" prop="refUndertakTpersonDo" />
      <el-table-column label="风险评估等级" align="center" prop="dictRiskAssessment" />
      <el-table-column label="首次承接" align="center" prop="dictFirstUndertake" />
      <el-table-column label="客户诉求" align="center" prop="customerReq" />
      <el-table-column label="项目进度" align="center" prop="projectProgress" />
      <el-table-column label="流程状态" align="center" prop="dictWfState" />
      <el-table-column label="项目状态" align="center" prop="dictProjectStatus" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="${comment}" align="center" prop="delay" />
      <el-table-column label="${comment}" align="center" prop="createId" />
      <el-table-column label="${comment}" align="center" prop="creator" />
      <el-table-column label="${comment}" align="center" prop="updateId" />
      <el-table-column label="删除标识" align="center" prop="deleted" />
      <el-table-column label="${comment}" align="center" prop="orderId" />
      <el-table-column label="${comment}" align="center" prop="orderStatus" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:project:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:project:remove']"
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

    <!-- 添加或修改项目信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="项目编码" prop="projectCode">
          <el-input v-model="form.projectCode" placeholder="请输入项目编码" />
        </el-form-item>
        <el-form-item label="项目名称" prop="projectName">
          <el-input v-model="form.projectName" placeholder="请输入项目名称" />
        </el-form-item>
        <el-form-item label="项目类型细分" prop="dictProjectTypeSub">
          <el-input v-model="form.dictProjectTypeSub" placeholder="请输入项目类型细分" />
        </el-form-item>
        <el-form-item label="瀹㈡埛缂栫爜" prop="refIdCuscode">
          <el-input v-model="form.refIdCuscode" placeholder="请输入${comment}" />
        </el-form-item>
        <el-form-item label="客户(委托单位)" prop="refCusname">
          <el-input v-model="form.refCusname" placeholder="请输入客户(委托单位)" />
        </el-form-item>
        <el-form-item label="${comment}" prop="projectDesc">
          <el-input v-model="form.projectDesc" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="项目计划开始时间" prop="projectStarttime">
          <el-date-picker clearable
            v-model="form.projectStarttime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择项目计划开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="项目计划结束时间" prop="projectEndtime">
          <el-date-picker clearable
            v-model="form.projectEndtime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择项目计划结束时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="被评估单位" prop="cusnameTodo">
          <el-input v-model="form.cusnameTodo" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="项目经理" prop="refProjectManager">
          <el-input v-model="form.refProjectManager" placeholder="请输入项目经理" />
        </el-form-item>
        <el-form-item label="承接(合伙)人" prop="refUndertakePerson">
          <el-input v-model="form.refUndertakePerson" placeholder="请输入承接(合伙)人" />
        </el-form-item>
        <el-form-item label="承做(合伙)人" prop="refUndertakTpersonDo">
          <el-input v-model="form.refUndertakTpersonDo" placeholder="请输入承做(合伙)人" />
        </el-form-item>
        <el-form-item label="风险评估等级" prop="dictRiskAssessment">
          <el-input v-model="form.dictRiskAssessment" placeholder="请输入风险评估等级" />
        </el-form-item>
        <el-form-item label="首次承接" prop="dictFirstUndertake">
          <el-input v-model="form.dictFirstUndertake" placeholder="请输入首次承接" />
        </el-form-item>
        <el-form-item label="客户诉求" prop="customerReq">
          <el-input v-model="form.customerReq" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="项目进度" prop="projectProgress">
          <el-input v-model="form.projectProgress" placeholder="请输入项目进度" />
        </el-form-item>
        <el-form-item label="流程状态" prop="dictWfState">
          <el-input v-model="form.dictWfState" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="寤舵湡" prop="delay">
          <el-input v-model="form.delay" placeholder="请输入${comment}" />
        </el-form-item>
        <el-form-item label="鍒涘缓浜篒D" prop="createId">
          <el-input v-model="form.createId" placeholder="请输入${comment}" />
        </el-form-item>
        <el-form-item label="鍒涘缓浜? prop="creator">
          <el-input v-model="form.creator" placeholder="请输入${comment}" />
        </el-form-item>
        <el-form-item label="鏇存柊浜篒D" prop="updateId">
          <el-input v-model="form.updateId" placeholder="请输入${comment}" />
        </el-form-item>
        <el-form-item label="删除标识" prop="deleted">
          <el-input v-model="form.deleted" placeholder="请输入删除标识" />
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
import { listProject, getProject, delProject, addProject, updateProject } from "@/api/qixing/project";

export default {
  name: "Project",
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
      // 项目信息表格数据
      projectList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        projectCode: null,
        projectName: null,
        dictProjectType: null,
        dictProjectTypeSub: null,
        refIdCuscode: null,
        refCusname: null,
        projectDesc: null,
        projectStarttime: null,
        projectEndtime: null,
        cusnameTodo: null,
        refProjectManager: null,
        refUndertakePerson: null,
        refUndertakTpersonDo: null,
        dictRiskAssessment: null,
        dictFirstUndertake: null,
        customerReq: null,
        projectProgress: null,
        dictWfState: null,
        dictProjectStatus: null,
        delay: null,
        createId: null,
        creator: null,
        updateId: null,
        deleted: null,
        orderId: null,
        orderStatus: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        projectCode: [
          { required: true, message: "项目编码不能为空", trigger: "blur" }
        ],
        projectName: [
          { required: true, message: "项目名称不能为空", trigger: "blur" }
        ],
        dictProjectType: [
          { required: true, message: "项目类型不能为空", trigger: "change" }
        ],
        dictProjectTypeSub: [
          { required: true, message: "项目类型细分不能为空", trigger: "blur" }
        ],
        refCusname: [
          { required: true, message: "客户(委托单位)不能为空", trigger: "blur" }
        ],
        cusnameTodo: [
          { required: true, message: "被评估单位不能为空", trigger: "blur" }
        ],
        refProjectManager: [
          { required: true, message: "项目经理不能为空", trigger: "blur" }
        ],
        dictRiskAssessment: [
          { required: true, message: "风险评估等级不能为空", trigger: "blur" }
        ],
        projectProgress: [
          { required: true, message: "项目进度不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询项目信息列表 */
    getList() {
      this.loading = true;
      listProject(this.queryParams).then(response => {
        this.projectList = response.rows;
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
        projectCode: null,
        projectName: null,
        dictProjectType: null,
        dictProjectTypeSub: null,
        refIdCuscode: null,
        refCusname: null,
        projectDesc: null,
        projectStarttime: null,
        projectEndtime: null,
        cusnameTodo: null,
        refProjectManager: null,
        refUndertakePerson: null,
        refUndertakTpersonDo: null,
        dictRiskAssessment: null,
        dictFirstUndertake: null,
        customerReq: null,
        projectProgress: null,
        dictWfState: null,
        dictProjectStatus: null,
        remark: null,
        delay: null,
        createTime: null,
        createId: null,
        creator: null,
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
      this.title = "添加项目信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getProject(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改项目信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateProject(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addProject(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除项目信息编号为"' + ids + '"的数据项？').then(function() {
        return delProject(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/project/export', {
        ...this.queryParams
      }, `project_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
