<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="办公用品名称" prop="offiecProductName">
        <el-input
          v-model="queryParams.offiecProductName"
          placeholder="请输入办公用品名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="需求数量" prop="reqNum">
        <el-input
          v-model="queryParams.reqNum"
          placeholder="请输入需求数量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="申请人" prop="creator">
        <el-input
          v-model="queryParams.creator"
          placeholder="请输入申请人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="审批状态" prop="dictApprove">
        <el-input
          v-model="queryParams.dictApprove"
          placeholder="请输入审批状态"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="审批意见" prop="descApprove">
        <el-input
          v-model="queryParams.descApprove"
          placeholder="请输入审批意见"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="审批人" prop="approvetor">
        <el-input
          v-model="queryParams.approvetor"
          placeholder="请输入审批人"
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
      <el-form-item label="${comment}" prop="orderState">
        <el-input
          v-model="queryParams.orderState"
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
          v-hasPermi="['system:count:add']"
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
          v-hasPermi="['system:count:edit']"
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
          v-hasPermi="['system:count:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:count:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="countList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="办公用品名称" align="center" prop="offiecProductName" />
      <el-table-column label="办公用品类型" align="center" prop="dictProductType" />
      <el-table-column label="办公用品用途" align="center" prop="officeTodo" />
      <el-table-column label="需求数量" align="center" prop="reqNum" />
      <el-table-column label="申请原因" align="center" prop="whyDesc" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="申请人" align="center" prop="creator" />
      <el-table-column label="审批状态" align="center" prop="dictApprove" />
      <el-table-column label="审批意见" align="center" prop="descApprove" />
      <el-table-column label="审批人" align="center" prop="approvetor" />
      <el-table-column label="${comment}" align="center" prop="createId" />
      <el-table-column label="${comment}" align="center" prop="updateId" />
      <el-table-column label="${comment}" align="center" prop="orderId" />
      <el-table-column label="${comment}" align="center" prop="orderState" />
      <el-table-column label="${comment}" align="center" prop="orderStatus" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:count:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:count:remove']"
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

    <!-- 添加或修改办公用品申领申购对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="办公用品名称" prop="offiecProductName">
          <el-input v-model="form.offiecProductName" placeholder="请输入办公用品名称" />
        </el-form-item>
        <el-form-item label="办公用品用途" prop="officeTodo">
          <el-input v-model="form.officeTodo" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="需求数量" prop="reqNum">
          <el-input v-model="form.reqNum" placeholder="请输入需求数量" />
        </el-form-item>
        <el-form-item label="申请原因" prop="whyDesc">
          <el-input v-model="form.whyDesc" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="申请人" prop="creator">
          <el-input v-model="form.creator" placeholder="请输入申请人" />
        </el-form-item>
        <el-form-item label="审批状态" prop="dictApprove">
          <el-input v-model="form.dictApprove" placeholder="请输入审批状态" />
        </el-form-item>
        <el-form-item label="审批意见" prop="descApprove">
          <el-input v-model="form.descApprove" placeholder="请输入审批意见" />
        </el-form-item>
        <el-form-item label="审批人" prop="approvetor">
          <el-input v-model="form.approvetor" placeholder="请输入审批人" />
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
        <el-form-item label="${comment}" prop="orderState">
          <el-input v-model="form.orderState" placeholder="请输入${comment}" />
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
import { listCount, getCount, delCount, addCount, updateCount } from "@/api/qixing/count";

export default {
  name: "Count",
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
      // 办公用品申领申购表格数据
      countList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        offiecProductName: null,
        dictProductType: null,
        officeTodo: null,
        reqNum: null,
        whyDesc: null,
        creator: null,
        dictApprove: null,
        descApprove: null,
        approvetor: null,
        createId: null,
        updateId: null,
        orderId: null,
        orderState: null,
        orderStatus: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        offiecProductName: [
          { required: true, message: "办公用品名称不能为空", trigger: "blur" }
        ],
        dictProductType: [
          { required: true, message: "办公用品类型不能为空", trigger: "change" }
        ],
        officeTodo: [
          { required: true, message: "办公用品用途不能为空", trigger: "blur" }
        ],
        reqNum: [
          { required: true, message: "需求数量不能为空", trigger: "blur" }
        ],
        whyDesc: [
          { required: true, message: "申请原因不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询办公用品申领申购列表 */
    getList() {
      this.loading = true;
      listCount(this.queryParams).then(response => {
        this.countList = response.rows;
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
        offiecProductName: null,
        dictProductType: null,
        officeTodo: null,
        reqNum: null,
        whyDesc: null,
        remark: null,
        creator: null,
        dictApprove: null,
        descApprove: null,
        approvetor: null,
        createTime: null,
        createId: null,
        updateTime: null,
        updateId: null,
        orderId: null,
        orderState: null,
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
      this.title = "添加办公用品申领申购";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getCount(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改办公用品申领申购";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateCount(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCount(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除办公用品申领申购编号为"' + ids + '"的数据项？').then(function() {
        return delCount(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/count/export', {
        ...this.queryParams
      }, `count_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
