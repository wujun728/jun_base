<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="底稿名称" prop="draftName">
        <el-input
          v-model="queryParams.draftName"
          placeholder="请输入底稿名称"
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
      <el-form-item label="底稿输出时间" prop="draftTime">
        <el-date-picker clearable
          v-model="queryParams.draftTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择底稿输出时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="底稿输出责任人(承做)" prop="refDraftBy">
        <el-input
          v-model="queryParams.refDraftBy"
          placeholder="请输入底稿输出责任人(承做)"
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
          v-hasPermi="['system:draft:add']"
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
          v-hasPermi="['system:draft:edit']"
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
          v-hasPermi="['system:draft:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:draft:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="draftList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="底稿ID" align="center" prop="id" />
      <el-table-column label="底稿名称" align="center" prop="draftName" />
      <el-table-column label="底稿类型" align="center" prop="dictDraftType" />
      <el-table-column label="${comment}" align="center" prop="refProjectCode" />
      <el-table-column label="项目名称" align="center" prop="refProjectName" />
      <el-table-column label="底稿描述" align="center" prop="draftDesc" />
      <el-table-column label="底稿输出时间" align="center" prop="draftTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.draftTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="底稿输出责任人(承做)" align="center" prop="refDraftBy" />
      <el-table-column label="底稿整理进度" align="center" prop="dictDraftStatus" />
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
            v-hasPermi="['system:draft:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:draft:remove']"
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

    <!-- 添加或修改项目底稿对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="底稿名称" prop="draftName">
          <el-input v-model="form.draftName" placeholder="请输入底稿名称" />
        </el-form-item>
        <el-form-item label="${comment}" prop="refProjectCode">
          <el-input v-model="form.refProjectCode" placeholder="请输入${comment}" />
        </el-form-item>
        <el-form-item label="项目名称" prop="refProjectName">
          <el-input v-model="form.refProjectName" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="底稿描述" prop="draftDesc">
          <el-input v-model="form.draftDesc" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="底稿输出时间" prop="draftTime">
          <el-date-picker clearable
            v-model="form.draftTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择底稿输出时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="底稿输出责任人(承做)" prop="refDraftBy">
          <el-input v-model="form.refDraftBy" placeholder="请输入底稿输出责任人(承做)" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="鍒涘缓浜? prop="creator">
          <el-input v-model="form.creator" type="textarea" placeholder="请输入内容" />
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
import { listDraft, getDraft, delDraft, addDraft, updateDraft } from "@/api/qixing/draft";

export default {
  name: "Draft",
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
      // 项目底稿表格数据
      draftList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        draftName: null,
        dictDraftType: null,
        refProjectCode: null,
        refProjectName: null,
        draftDesc: null,
        draftTime: null,
        refDraftBy: null,
        dictDraftStatus: null,
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
        draftName: [
          { required: true, message: "底稿名称不能为空", trigger: "blur" }
        ],
        dictDraftType: [
          { required: true, message: "底稿类型不能为空", trigger: "change" }
        ],
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
    /** 查询项目底稿列表 */
    getList() {
      this.loading = true;
      listDraft(this.queryParams).then(response => {
        this.draftList = response.rows;
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
        draftName: null,
        dictDraftType: null,
        refProjectCode: null,
        refProjectName: null,
        draftDesc: null,
        draftTime: null,
        refDraftBy: null,
        dictDraftStatus: null,
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
      this.title = "添加项目底稿";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getDraft(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改项目底稿";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateDraft(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addDraft(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除项目底稿编号为"' + ids + '"的数据项？').then(function() {
        return delDraft(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/draft/export', {
        ...this.queryParams
      }, `draft_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
