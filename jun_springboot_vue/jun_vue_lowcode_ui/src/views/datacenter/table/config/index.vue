<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      size="small"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    >
      <!-- <el-form-item label="表单id" prop="formId">
        <el-input
          v-model="queryParams.formId"
          placeholder="请输入表单id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item label="属性名称" prop="attrName">
        <el-input
          v-model="queryParams.attrName"
          placeholder="请输入属性名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!-- <el-form-item label="属性code" prop="attrCode">
        <el-input
          v-model="queryParams.attrCode"
          placeholder="请输入属性code"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否显示：1 是、0 否" prop="isShow">
        <el-input
          v-model="queryParams.isShow"
          placeholder="请输入是否显示：1 是、0 否"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="排序" prop="showOrder">
        <el-input
          v-model="queryParams.showOrder"
          placeholder="请输入排序"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建者名称" prop="createName">
        <el-input
          v-model="queryParams.createName"
          placeholder="请输入创建者名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item> -->
      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >搜索</el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
          >重置</el-button
        >
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <!-- <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['nocode:attr:add']"
          >新增</el-button
        >
      </el-col> -->
      <!-- <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['nocode:attr:edit']"
          >修改</el-button
        >
      </el-col> -->
      <!-- <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['nocode:attr:remove']"
          >删除</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['nocode:attr:export']"
          >导出</el-button
        >
      </el-col> -->
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="attrList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键ID" align="center" prop="id" v-if="false" />
      <el-table-column
        label="表单id"
        align="center"
        prop="formId"
        v-if="false"
      />
      <el-table-column label="属性名称" align="center" prop="attrName" />
      <el-table-column label="属性编码" align="center" prop="attrCode" />
      <el-table-column label="是否显示" align="center" prop="isShow">
        <template slot-scope="scope">
          <span>{{ scope.row.isShow == 1 ? "显示" : "隐藏" }}</span>
        </template>
      </el-table-column>
      <el-table-column label="排序" align="center" prop="showOrder" />
      <el-table-column label="创建者" align="center" prop="createName" />
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            >修改</el-button
          >
          <!-- <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['nocode:attr:remove']"
            >删除</el-button
          > -->
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改表单属性对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="表单id" prop="formId">
          <el-input
            v-model="form.formId"
            placeholder="请输入表单id"
            :disabled="true"
          />
        </el-form-item>
        <el-form-item label="属性名称" prop="attrName">
          <el-input v-model="form.attrName" placeholder="请输入属性名称" />
        </el-form-item>
        <el-form-item label="属性编码" prop="attrCode">
          <el-input
            v-model="form.attrCode"
            placeholder="请输入属性code"
            :disabled="true"
          />
        </el-form-item>

        <el-form-item label="是否显示" prop="isShow">
          <el-select v-model="form.isShow" placeholder="是否显示">
            <el-option label="显示" :value="1"> </el-option>
            <el-option label="隐藏" :value="0"> </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="排序" prop="showOrder">
          <el-input v-model="form.showOrder" placeholder="请输入排序" />
        </el-form-item>
        <el-form-item label="创建者" prop="createName">
          <el-input
            v-model="form.createName"
            placeholder="请输入创建者名称"
            :disabled="true"
          />
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
import {
  listAttr,
  getAttr,
  delAttr,
  addAttr,
  updateAttr,
} from "@/api/form/attr";

export default {
  name: "Attr",
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
      // 表单属性表格数据
      attrList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        formId: null,
        attrName: null,
        attrCode: null,
        isShow: null,
        showOrder: null,
        createName: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
    };
  },
  created() {
    this.queryParams.formId = this.$route.params.formId;
    this.getList();
  },
  methods: {
    /** 查询表单属性列表 */
    getList() {
      this.loading = true;
      listAttr(this.queryParams).then((response) => {
        this.attrList = response.rows;
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
        formId: null,
        attrName: null,
        attrCode: null,
        isShow: null,
        showOrder: null,
        createName: null,
        createBy: null,
        createTime: null,
        updateTime: null,
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
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加表单属性";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getAttr(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改表单属性";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateAttr(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addAttr(this.form).then((response) => {
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
      this.$modal
        .confirm('是否确认删除表单属性编号为"' + ids + '"的数据项？')
        .then(function () {
          return delAttr(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "nocode/attr/export",
        {
          ...this.queryParams,
        },
        `attr_${new Date().getTime()}.xlsx`
      );
    },
  },
};
</script>
