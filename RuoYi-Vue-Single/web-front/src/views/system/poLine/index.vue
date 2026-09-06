<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="订单ID" prop="poId">
        <el-input
          v-model="queryParams.poId"
          placeholder="请输入订单ID"
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
      <el-form-item label="产品编码(冗余)" prop="productCode">
        <el-input
          v-model="queryParams.productCode"
          placeholder="请输入产品编码(冗余)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="产品名称(冗余)" prop="productName">
        <el-input
          v-model="queryParams.productName"
          placeholder="请输入产品名称(冗余)"
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
      <el-form-item label="规格" prop="spec">
        <el-input
          v-model="queryParams.spec"
          placeholder="请输入规格"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="计量单位" prop="unit">
        <el-input
          v-model="queryParams.unit"
          placeholder="请输入计量单位"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="数量" prop="qtyOrdered">
        <el-input
          v-model="queryParams.qtyOrdered"
          placeholder="请输入数量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="单价" prop="price">
        <el-input
          v-model="queryParams.price"
          placeholder="请输入单价"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="厂家" prop="manufacturer">
        <el-input
          v-model="queryParams.manufacturer"
          placeholder="请输入厂家"
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
      <el-form-item label="已采购数量(已收货)" prop="qtyPurchased">
        <el-input
          v-model="queryParams.qtyPurchased"
          placeholder="请输入已采购数量(已收货)"
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
          v-hasPermi="['system:poLine:add']"
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
          v-hasPermi="['system:poLine:edit']"
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
          v-hasPermi="['system:poLine:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:poLine:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="poLineList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键" align="center" prop="id" />
      <el-table-column label="订单ID" align="center" prop="poId" />
      <el-table-column label="行号" align="center" prop="lineNo" />
      <el-table-column label="产品ID" align="center" prop="productId" />
      <el-table-column label="产品编码(冗余)" align="center" prop="productCode" />
      <el-table-column label="产品名称(冗余)" align="center" prop="productName" />
      <el-table-column label="简拼(冗余)" align="center" prop="shortName" />
      <el-table-column label="规格" align="center" prop="spec" />
      <el-table-column label="计量单位" align="center" prop="unit" />
      <el-table-column label="数量" align="center" prop="qtyOrdered" />
      <el-table-column label="单价" align="center" prop="price" />
      <el-table-column label="厂家" align="center" prop="manufacturer" />
      <el-table-column label="仓库ID" align="center" prop="warehouseId" />
      <el-table-column label="已采购数量(已收货)" align="center" prop="qtyPurchased" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:poLine:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:poLine:remove']"
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

    <!-- 添加或修改采购订单-明细对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="订单ID" prop="poId">
          <el-input v-model="form.poId" placeholder="请输入订单ID" />
        </el-form-item>
        <el-form-item label="行号" prop="lineNo">
          <el-input v-model="form.lineNo" placeholder="请输入行号" />
        </el-form-item>
        <el-form-item label="产品ID" prop="productId">
          <el-input v-model="form.productId" placeholder="请输入产品ID" />
        </el-form-item>
        <el-form-item label="产品编码(冗余)" prop="productCode">
          <el-input v-model="form.productCode" placeholder="请输入产品编码(冗余)" />
        </el-form-item>
        <el-form-item label="产品名称(冗余)" prop="productName">
          <el-input v-model="form.productName" placeholder="请输入产品名称(冗余)" />
        </el-form-item>
        <el-form-item label="简拼(冗余)" prop="shortName">
          <el-input v-model="form.shortName" placeholder="请输入简拼(冗余)" />
        </el-form-item>
        <el-form-item label="规格" prop="spec">
          <el-input v-model="form.spec" placeholder="请输入规格" />
        </el-form-item>
        <el-form-item label="计量单位" prop="unit">
          <el-input v-model="form.unit" placeholder="请输入计量单位" />
        </el-form-item>
        <el-form-item label="数量" prop="qtyOrdered">
          <el-input v-model="form.qtyOrdered" placeholder="请输入数量" />
        </el-form-item>
        <el-form-item label="单价" prop="price">
          <el-input v-model="form.price" placeholder="请输入单价" />
        </el-form-item>
        <el-form-item label="厂家" prop="manufacturer">
          <el-input v-model="form.manufacturer" placeholder="请输入厂家" />
        </el-form-item>
        <el-form-item label="仓库ID" prop="warehouseId">
          <el-input v-model="form.warehouseId" placeholder="请输入仓库ID" />
        </el-form-item>
        <el-form-item label="已采购数量(已收货)" prop="qtyPurchased">
          <el-input v-model="form.qtyPurchased" placeholder="请输入已采购数量(已收货)" />
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
import { listPoLine, getPoLine, delPoLine, addPoLine, updatePoLine } from "@/api/system/poLine";

export default {
  name: "PoLine",
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
      // 采购订单-明细表格数据
      poLineList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        poId: null,
        lineNo: null,
        productId: null,
        productCode: null,
        productName: null,
        shortName: null,
        spec: null,
        unit: null,
        qtyOrdered: null,
        price: null,
        manufacturer: null,
        warehouseId: null,
        qtyPurchased: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        poId: [
          { required: true, message: "订单ID不能为空", trigger: "blur" }
        ],
        lineNo: [
          { required: true, message: "行号不能为空", trigger: "blur" }
        ],
        productId: [
          { required: true, message: "产品ID不能为空", trigger: "blur" }
        ],
        qtyOrdered: [
          { required: true, message: "数量不能为空", trigger: "blur" }
        ],
        price: [
          { required: true, message: "单价不能为空", trigger: "blur" }
        ],
        qtyPurchased: [
          { required: true, message: "已采购数量(已收货)不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询采购订单-明细列表 */
    getList() {
      this.loading = true;
      listPoLine(this.queryParams).then(response => {
        this.poLineList = response.rows;
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
        poId: null,
        lineNo: null,
        productId: null,
        productCode: null,
        productName: null,
        shortName: null,
        spec: null,
        unit: null,
        qtyOrdered: null,
        price: null,
        manufacturer: null,
        warehouseId: null,
        qtyPurchased: null,
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
      this.title = "添加采购订单-明细";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getPoLine(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改采购订单-明细";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updatePoLine(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addPoLine(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除采购订单-明细编号为"' + ids + '"的数据项？').then(function() {
        return delPoLine(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/poLine/export', {
        ...this.queryParams
      }, `poLine_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
