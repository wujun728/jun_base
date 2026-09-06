<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="调拨单据号" prop="code">
        <el-input
          v-model="queryParams.code"
          placeholder="请输入调拨单据号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="目标仓库" prop="dstWarehouseId">
        <el-input
          v-model="queryParams.dstWarehouseId"
          placeholder="请输入目标仓库"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="源仓库" prop="srcWarehouseId">
        <el-input
          v-model="queryParams.srcWarehouseId"
          placeholder="请输入源仓库"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="调拨日期" prop="transferDate">
        <el-date-picker clearable
          v-model="queryParams.transferDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择调拨日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="调拨人" prop="transferBy">
        <el-input
          v-model="queryParams.transferBy"
          placeholder="请输入调拨人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="操作员" prop="operator">
        <el-input
          v-model="queryParams.operator"
          placeholder="请输入操作员"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="审核人" prop="approver">
        <el-input
          v-model="queryParams.approver"
          placeholder="请输入审核人"
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
          v-hasPermi="['system:transferHeader:add']"
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
          v-hasPermi="['system:transferHeader:edit']"
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
          v-hasPermi="['system:transferHeader:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:transferHeader:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="transferHeaderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键" align="center" prop="id" />
      <el-table-column label="调拨单据号" align="center" prop="code" />
      <el-table-column label="目标仓库" align="center" prop="dstWarehouseId" />
      <el-table-column label="源仓库" align="center" prop="srcWarehouseId" />
      <el-table-column label="调拨日期" align="center" prop="transferDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.transferDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="调拨人" align="center" prop="transferBy" />
      <el-table-column label="操作员" align="center" prop="operator" />
      <el-table-column label="审核人" align="center" prop="approver" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:transferHeader:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:transferHeader:remove']"
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

    <!-- 添加或修改调拨单-主对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="调拨单据号" prop="code">
          <el-input v-model="form.code" placeholder="请输入调拨单据号" />
        </el-form-item>
        <el-form-item label="目标仓库" prop="dstWarehouseId">
          <el-input v-model="form.dstWarehouseId" placeholder="请输入目标仓库" />
        </el-form-item>
        <el-form-item label="源仓库" prop="srcWarehouseId">
          <el-input v-model="form.srcWarehouseId" placeholder="请输入源仓库" />
        </el-form-item>
        <el-form-item label="调拨日期" prop="transferDate">
          <el-date-picker clearable
            v-model="form.transferDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择调拨日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="调拨人" prop="transferBy">
          <el-input v-model="form.transferBy" placeholder="请输入调拨人" />
        </el-form-item>
        <el-form-item label="操作员" prop="operator">
          <el-input v-model="form.operator" placeholder="请输入操作员" />
        </el-form-item>
        <el-form-item label="审核人" prop="approver">
          <el-input v-model="form.approver" placeholder="请输入审核人" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
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
import { listTransferHeader, getTransferHeader, delTransferHeader, addTransferHeader, updateTransferHeader } from "@/api/system/transferHeader";

export default {
  name: "TransferHeader",
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
      // 调拨单-主表格数据
      transferHeaderList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        code: null,
        dstWarehouseId: null,
        srcWarehouseId: null,
        transferDate: null,
        transferBy: null,
        operator: null,
        approver: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        code: [
          { required: true, message: "调拨单据号不能为空", trigger: "blur" }
        ],
        dstWarehouseId: [
          { required: true, message: "目标仓库不能为空", trigger: "blur" }
        ],
        srcWarehouseId: [
          { required: true, message: "源仓库不能为空", trigger: "blur" }
        ],
        transferDate: [
          { required: true, message: "调拨日期不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询调拨单-主列表 */
    getList() {
      this.loading = true;
      listTransferHeader(this.queryParams).then(response => {
        this.transferHeaderList = response.rows;
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
        code: null,
        dstWarehouseId: null,
        srcWarehouseId: null,
        transferDate: null,
        transferBy: null,
        operator: null,
        approver: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
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
      this.title = "添加调拨单-主";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getTransferHeader(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改调拨单-主";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateTransferHeader(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addTransferHeader(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除调拨单-主编号为"' + ids + '"的数据项？').then(function() {
        return delTransferHeader(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/transferHeader/export', {
        ...this.queryParams
      }, `transferHeader_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
