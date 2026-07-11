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
      <el-form-item label="${comment}" prop="refUserCode">
        <el-input
          v-model="queryParams.refUserCode"
          placeholder="请输入${comment}"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户名称" prop="refUserName">
        <el-input
          v-model="queryParams.refUserName"
          placeholder="请输入用户名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="借阅结束日期" prop="endTime">
        <el-date-picker clearable
          v-model="queryParams.endTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择借阅结束日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="流程状态" prop="dictBorrowState">
        <el-input
          v-model="queryParams.dictBorrowState"
          placeholder="请输入流程状态"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="提交人" prop="creator">
        <el-input
          v-model="queryParams.creator"
          placeholder="请输入提交人"
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
          v-hasPermi="['system:borrow:add']"
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
          v-hasPermi="['system:borrow:edit']"
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
          v-hasPermi="['system:borrow:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:borrow:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="borrowList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="${comment}" align="center" prop="refProjectCode" />
      <el-table-column label="项目名称" align="center" prop="refProjectName" />
      <el-table-column label="${comment}" align="center" prop="refUserCode" />
      <el-table-column label="用户名称" align="center" prop="refUserName" />
      <el-table-column label="借阅原因" align="center" prop="borrowDesc" />
      <el-table-column label="借阅结束日期" align="center" prop="endTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="流程状态" align="center" prop="dictBorrowState" />
      <el-table-column label="提交人" align="center" prop="creator" />
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
            v-hasPermi="['system:borrow:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:borrow:remove']"
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

    <!-- 添加或修改项目借阅对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="${comment}" prop="refProjectCode">
          <el-input v-model="form.refProjectCode" placeholder="请输入${comment}" />
        </el-form-item>
        <el-form-item label="项目名称" prop="refProjectName">
          <el-input v-model="form.refProjectName" placeholder="请输入项目名称" />
        </el-form-item>
        <el-form-item label="${comment}" prop="refUserCode">
          <el-input v-model="form.refUserCode" placeholder="请输入${comment}" />
        </el-form-item>
        <el-form-item label="用户名称" prop="refUserName">
          <el-input v-model="form.refUserName" placeholder="请输入用户名称" />
        </el-form-item>
        <el-form-item label="借阅原因" prop="borrowDesc">
          <el-input v-model="form.borrowDesc" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="借阅结束日期" prop="endTime">
          <el-date-picker clearable
            v-model="form.endTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择借阅结束日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="流程状态" prop="dictBorrowState">
          <el-input v-model="form.dictBorrowState" placeholder="请输入流程状态" />
        </el-form-item>
        <el-form-item label="提交人" prop="creator">
          <el-input v-model="form.creator" placeholder="请输入提交人" />
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
import { listBorrow, getBorrow, delBorrow, addBorrow, updateBorrow } from "@/api/qixing/borrow";

export default {
  name: "Borrow",
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
      // 项目借阅表格数据
      borrowList: [],
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
        refUserCode: null,
        refUserName: null,
        borrowDesc: null,
        endTime: null,
        dictBorrowState: null,
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
        refProjectCode: [
          { required: true, message: "$comment不能为空", trigger: "blur" }
        ],
        refProjectName: [
          { required: true, message: "项目名称不能为空", trigger: "blur" }
        ],
        refUserCode: [
          { required: true, message: "$comment不能为空", trigger: "blur" }
        ],
        refUserName: [
          { required: true, message: "用户名称不能为空", trigger: "blur" }
        ],
        endTime: [
          { required: true, message: "借阅结束日期不能为空", trigger: "blur" }
        ],
        dictBorrowState: [
          { required: true, message: "流程状态不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询项目借阅列表 */
    getList() {
      this.loading = true;
      listBorrow(this.queryParams).then(response => {
        this.borrowList = response.rows;
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
        refUserCode: null,
        refUserName: null,
        borrowDesc: null,
        endTime: null,
        dictBorrowState: null,
        creator: null,
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
      this.title = "添加项目借阅";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getBorrow(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改项目借阅";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateBorrow(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addBorrow(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除项目借阅编号为"' + ids + '"的数据项？').then(function() {
        return delBorrow(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/borrow/export', {
        ...this.queryParams
      }, `borrow_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
