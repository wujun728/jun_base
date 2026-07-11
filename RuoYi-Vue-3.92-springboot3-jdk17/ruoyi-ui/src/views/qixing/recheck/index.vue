<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="${comment}" prop="refPcode">
        <el-input
          v-model="queryParams.refPcode"
          placeholder="请输入${comment}"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="项目名称" prop="refPname">
        <el-input
          v-model="queryParams.refPname"
          placeholder="请输入项目名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="项目经理名称" prop="refPmanager">
        <el-input
          v-model="queryParams.refPmanager"
          placeholder="请输入项目经理名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="项目报告" prop="refPreport">
        <el-input
          v-model="queryParams.refPreport"
          placeholder="请输入项目报告"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="项目报告责任人" prop="refPmanager2">
        <el-input
          v-model="queryParams.refPmanager2"
          placeholder="请输入项目报告责任人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="项目复核责任人" prop="recheckMan">
        <el-input
          v-model="queryParams.recheckMan"
          placeholder="请输入项目复核责任人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="项目复核意见" prop="recheckAdvice">
        <el-input
          v-model="queryParams.recheckAdvice"
          placeholder="请输入项目复核意见"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="项目复核状态" prop="recheckState">
        <el-input
          v-model="queryParams.recheckState"
          placeholder="请输入项目复核状态"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="当前处理人" prop="currMan">
        <el-input
          v-model="queryParams.currMan"
          placeholder="请输入当前处理人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="流程节点" prop="dictWfState">
        <el-input
          v-model="queryParams.dictWfState"
          placeholder="请输入流程节点"
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
          v-hasPermi="['system:recheck:add']"
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
          v-hasPermi="['system:recheck:edit']"
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
          v-hasPermi="['system:recheck:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:recheck:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="recheckList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="成员ID" align="center" prop="id" />
      <el-table-column label="${comment}" align="center" prop="refPcode" />
      <el-table-column label="项目名称" align="center" prop="refPname" />
      <el-table-column label="项目经理名称" align="center" prop="refPmanager" />
      <el-table-column label="项目报告" align="center" prop="refPreport" />
      <el-table-column label="项目报告责任人" align="center" prop="refPmanager2" />
      <el-table-column label="项目复核责任人" align="center" prop="recheckMan" />
      <el-table-column label="项目复核意见" align="center" prop="recheckAdvice" />
      <el-table-column label="项目复核状态" align="center" prop="recheckState" />
      <el-table-column label="当前处理人" align="center" prop="currMan" />
      <el-table-column label="流程节点" align="center" prop="dictWfState" />
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
            v-hasPermi="['system:recheck:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:recheck:remove']"
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

    <!-- 添加或修改项目复核对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="${comment}" prop="refPcode">
          <el-input v-model="form.refPcode" placeholder="请输入${comment}" />
        </el-form-item>
        <el-form-item label="项目名称" prop="refPname">
          <el-input v-model="form.refPname" placeholder="请输入项目名称" />
        </el-form-item>
        <el-form-item label="项目经理名称" prop="refPmanager">
          <el-input v-model="form.refPmanager" placeholder="请输入项目经理名称" />
        </el-form-item>
        <el-form-item label="项目报告" prop="refPreport">
          <el-input v-model="form.refPreport" placeholder="请输入项目报告" />
        </el-form-item>
        <el-form-item label="项目报告责任人" prop="refPmanager2">
          <el-input v-model="form.refPmanager2" placeholder="请输入项目报告责任人" />
        </el-form-item>
        <el-form-item label="项目复核责任人" prop="recheckMan">
          <el-input v-model="form.recheckMan" placeholder="请输入项目复核责任人" />
        </el-form-item>
        <el-form-item label="项目复核意见" prop="recheckAdvice">
          <el-input v-model="form.recheckAdvice" placeholder="请输入项目复核意见" />
        </el-form-item>
        <el-form-item label="项目复核状态" prop="recheckState">
          <el-input v-model="form.recheckState" placeholder="请输入项目复核状态" />
        </el-form-item>
        <el-form-item label="当前处理人" prop="currMan">
          <el-input v-model="form.currMan" placeholder="请输入当前处理人" />
        </el-form-item>
        <el-form-item label="流程节点" prop="dictWfState">
          <el-input v-model="form.dictWfState" placeholder="请输入流程节点" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
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
import { listRecheck, getRecheck, delRecheck, addRecheck, updateRecheck } from "@/api/qixing/recheck";

export default {
  name: "Recheck",
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
      // 项目复核表格数据
      recheckList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        refPcode: null,
        refPname: null,
        refPmanager: null,
        refPreport: null,
        refPmanager2: null,
        recheckMan: null,
        recheckAdvice: null,
        recheckState: null,
        currMan: null,
        dictWfState: null,
        createId: null,
        updateId: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        refPname: [
          { required: true, message: "项目名称不能为空", trigger: "blur" }
        ],
        refPmanager: [
          { required: true, message: "项目经理名称不能为空", trigger: "blur" }
        ],
        refPreport: [
          { required: true, message: "项目报告不能为空", trigger: "blur" }
        ],
        refPmanager2: [
          { required: true, message: "项目报告责任人不能为空", trigger: "blur" }
        ],
        recheckMan: [
          { required: true, message: "项目复核责任人不能为空", trigger: "blur" }
        ],
        recheckAdvice: [
          { required: true, message: "项目复核意见不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询项目复核列表 */
    getList() {
      this.loading = true;
      listRecheck(this.queryParams).then(response => {
        this.recheckList = response.rows;
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
        refPcode: null,
        refPname: null,
        refPmanager: null,
        refPreport: null,
        refPmanager2: null,
        recheckMan: null,
        recheckAdvice: null,
        recheckState: null,
        currMan: null,
        dictWfState: null,
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
      this.title = "添加项目复核";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getRecheck(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改项目复核";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateRecheck(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addRecheck(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除项目复核编号为"' + ids + '"的数据项？').then(function() {
        return delRecheck(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/recheck/export', {
        ...this.queryParams
      }, `recheck_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
