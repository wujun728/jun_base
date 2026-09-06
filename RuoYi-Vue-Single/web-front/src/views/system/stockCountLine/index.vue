<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="盘点单ID" prop="countId">
        <el-input
          v-model="queryParams.countId"
          placeholder="请输入盘点单ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="行号" prop="lineNo">
        <el-input
          v-model="queryParams.lineNo"
          placeholder="请输入行号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="产品ID" prop="productId">
        <el-input
          v-model="queryParams.productId"
          placeholder="请输入产品ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="产品编号(冗余)" prop="productCode">
        <el-input
          v-model="queryParams.productCode"
          placeholder="请输入产品编号(冗余)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="名称(冗余)" prop="productName">
        <el-input
          v-model="queryParams.productName"
          placeholder="请输入名称(冗余)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="简拼(冗余)" prop="shortName">
        <el-input
          v-model="queryParams.shortName"
          placeholder="请输入简拼(冗余)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="仓库ID" prop="warehouseId">
        <el-input
          v-model="queryParams.warehouseId"
          placeholder="请输入仓库ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="当前库存数量" prop="qtyOnHand">
        <el-input
          v-model="queryParams.qtyOnHand"
          placeholder="请输入当前库存数量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="实际库存数量" prop="qtyActual">
        <el-input
          v-model="queryParams.qtyActual"
          placeholder="请输入实际库存数量"
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
          v-hasPermi="['system:stockCountLine:add']"
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
          v-hasPermi="['system:stockCountLine:edit']"
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
          v-hasPermi="['system:stockCountLine:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:stockCountLine:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="stockCountLineList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键" align="center" prop="id" />
      <el-table-column label="盘点单ID" align="center" prop="countId" />
      <el-table-column label="行号" align="center" prop="lineNo" />
      <el-table-column label="产品ID" align="center" prop="productId" />
      <el-table-column label="产品编号(冗余)" align="center" prop="productCode" />
      <el-table-column label="名称(冗余)" align="center" prop="productName" />
      <el-table-column label="简拼(冗余)" align="center" prop="shortName" />
      <el-table-column label="仓库ID" align="center" prop="warehouseId" />
      <el-table-column label="当前库存数量" align="center" prop="qtyOnHand" />
      <el-table-column label="实际库存数量" align="center" prop="qtyActual" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:stockCountLine:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:stockCountLine:remove']"
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

    <!-- 添加或修改盘点单-明细对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="盘点单ID" prop="countId">
          <el-input v-model="form.countId" placeholder="请输入盘点单ID" />
        </el-form-item>
        <el-form-item label="行号" prop="lineNo">
          <el-input v-model="form.lineNo" placeholder="请输入行号" />
        </el-form-item>
        <el-form-item label="产品ID" prop="productId">
          <el-input v-model="form.productId" placeholder="请输入产品ID" />
        </el-form-item>
        <el-form-item label="产品编号(冗余)" prop="productCode">
          <el-input v-model="form.productCode" placeholder="请输入产品编号(冗余)" />
        </el-form-item>
        <el-form-item label="名称(冗余)" prop="productName">
          <el-input v-model="form.productName" placeholder="请输入名称(冗余)" />
        </el-form-item>
        <el-form-item label="简拼(冗余)" prop="shortName">
          <el-input v-model="form.shortName" placeholder="请输入简拼(冗余)" />
        </el-form-item>
        <el-form-item label="仓库ID" prop="warehouseId">
          <el-input v-model="form.warehouseId" placeholder="请输入仓库ID" />
        </el-form-item>
        <el-form-item label="当前库存数量" prop="qtyOnHand">
          <el-input v-model="form.qtyOnHand" placeholder="请输入当前库存数量" />
        </el-form-item>
        <el-form-item label="实际库存数量" prop="qtyActual">
          <el-input v-model="form.qtyActual" placeholder="请输入实际库存数量" />
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
import { listStockCountLine, getStockCountLine, delStockCountLine, addStockCountLine, updateStockCountLine } from "@/api/system/stockCountLine";

export default {
  name: "StockCountLine",
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
      // 盘点单-明细表格数据
      stockCountLineList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        countId: null,
        lineNo: null,
        productId: null,
        productCode: null,
        productName: null,
        shortName: null,
        warehouseId: null,
        qtyOnHand: null,
        qtyActual: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        countId: [
          { required: true, message: "盘点单ID不能为空", trigger: "blur" }
        ],
        lineNo: [
          { required: true, message: "行号不能为空", trigger: "blur" }
        ],
        productId: [
          { required: true, message: "产品ID不能为空", trigger: "blur" }
        ],
        warehouseId: [
          { required: true, message: "仓库ID不能为空", trigger: "blur" }
        ],
        qtyOnHand: [
          { required: true, message: "当前库存数量不能为空", trigger: "blur" }
        ],
        qtyActual: [
          { required: true, message: "实际库存数量不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询盘点单-明细列表 */
    getList() {
      this.loading = true;
      listStockCountLine(this.queryParams).then(response => {
        this.stockCountLineList = response.rows;
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
        countId: null,
        lineNo: null,
        productId: null,
        productCode: null,
        productName: null,
        shortName: null,
        warehouseId: null,
        qtyOnHand: null,
        qtyActual: null,
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
      this.title = "添加盘点单-明细";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getStockCountLine(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改盘点单-明细";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateStockCountLine(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addStockCountLine(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除盘点单-明细编号为"' + ids + '"的数据项？').then(function() {
        return delStockCountLine(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/stockCountLine/export', {
        ...this.queryParams
      }, `stockCountLine_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
