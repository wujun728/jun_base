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
      <el-form-item label="总结&评价人" prop="refUsername">
        <el-input
          v-model="queryParams.refUsername"
          placeholder="请输入总结&评价人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="在项目中的角色" prop="dictMemberRole">
        <el-input
          v-model="queryParams.dictMemberRole"
          placeholder="请输入在项目中的角色"
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
          v-hasPermi="['system:appraise:add']"
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
          v-hasPermi="['system:appraise:edit']"
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
          v-hasPermi="['system:appraise:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:appraise:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="appraiseList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="${comment}" align="center" prop="refProjectCode" />
      <el-table-column label="项目名称" align="center" prop="refProjectName" />
      <el-table-column label="总结&评价人" align="center" prop="refUsername" />
      <el-table-column label="在项目中的角色" align="center" prop="dictMemberRole" />
      <el-table-column label="总体评级" align="center" prop="detail1" />
      <el-table-column label="项目完成情况总结" align="center" prop="detail2" />
      <el-table-column label="项目经验教训总结" align="center" prop="detail3" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="创建人" align="center" prop="creator" />
      <el-table-column label="${comment}" align="center" prop="createId" />
      <el-table-column label="${comment}" align="center" prop="updateId" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:appraise:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:appraise:remove']"
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

    <!-- 添加或修改项目总结及评价对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="${comment}" prop="refProjectCode">
          <el-input v-model="form.refProjectCode" placeholder="请输入${comment}" />
        </el-form-item>
        <el-form-item label="项目名称" prop="refProjectName">
          <el-input v-model="form.refProjectName" placeholder="请输入项目名称" />
        </el-form-item>
        <el-form-item label="总结&评价人" prop="refUsername">
          <el-input v-model="form.refUsername" placeholder="请输入总结&评价人" />
        </el-form-item>
        <el-form-item label="在项目中的角色" prop="dictMemberRole">
          <el-input v-model="form.dictMemberRole" placeholder="请输入在项目中的角色" />
        </el-form-item>
        <el-form-item label="总体评级" prop="detail1">
          <el-input v-model="form.detail1" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="项目完成情况总结" prop="detail2">
          <el-input v-model="form.detail2" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="项目经验教训总结" prop="detail3">
          <el-input v-model="form.detail3" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="创建人" prop="creator">
          <el-input v-model="form.creator" placeholder="请输入创建人" />
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
import { listAppraise, getAppraise, delAppraise, addAppraise, updateAppraise } from "@/api/qixing/appraise";

export default {
  name: "Appraise",
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
      // 项目总结及评价表格数据
      appraiseList: [],
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
        refUsername: null,
        dictMemberRole: null,
        detail1: null,
        detail2: null,
        detail3: null,
        creator: null,
        createId: null,
        updateId: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        refProjectName: [
          { required: true, message: "项目名称不能为空", trigger: "blur" }
        ],
        refUsername: [
          { required: true, message: "总结&评价人不能为空", trigger: "blur" }
        ],
        dictMemberRole: [
          { required: true, message: "在项目中的角色不能为空", trigger: "blur" }
        ],
        detail1: [
          { required: true, message: "总体评级不能为空", trigger: "blur" }
        ],
        detail2: [
          { required: true, message: "项目完成情况总结不能为空", trigger: "blur" }
        ],
        detail3: [
          { required: true, message: "项目经验教训总结不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询项目总结及评价列表 */
    getList() {
      this.loading = true;
      listAppraise(this.queryParams).then(response => {
        this.appraiseList = response.rows;
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
        refUsername: null,
        dictMemberRole: null,
        detail1: null,
        detail2: null,
        detail3: null,
        remark: null,
        creator: null,
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
      this.title = "添加项目总结及评价";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getAppraise(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改项目总结及评价";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateAppraise(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addAppraise(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除项目总结及评价编号为"' + ids + '"的数据项？').then(function() {
        return delAppraise(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/appraise/export', {
        ...this.queryParams
      }, `appraise_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
