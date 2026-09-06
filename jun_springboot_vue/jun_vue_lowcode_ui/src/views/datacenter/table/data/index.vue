<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    >
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="dateRange"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button
          type="cyan"
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

    <el-table
      v-loading="loading"
      :data="defList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column
        v-for="cln in attrs"
        :key="cln.id"
        v-bind:label="cln.attrName"
        align="center"
        v-bind:prop="cln.attrCode"
      />
      <el-table-column
        label="详情"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-search"
            @click="handleForm(scope.row)"
          ></el-button>
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

    <!-- 详情对话框 -->
    <el-dialog
      :title="详情"
      :visible.sync="open"
      v-if="open"
      width="800px"
      append-to-body
    >
      <fm-generate-form :data="jsonData" :value="editData" ref="generateForm">
      </fm-generate-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="close">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getDef } from "@/api/form/form";
import { formDataList } from "@/api/flow/flow";
import { listShowAttrByFormId } from "@/api/form/attr";

export default {
  name: "MyTable",
  data() {
    return {
      attrs: [],
      formId: "",
      jsonData: {},
      editData: {},
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
      // 单定义表格数据
      defList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        defination: null,
        refProcKey: null,
        createName: null,
      },
      // 表单参数
      form: { procKey: null, formId: null, formName: null, data: null },
      // 表单校验
      rules: {},
    };
  },
  created() {
    this.formId = this.$route.params.formId;
    listShowAttrByFormId(this.formId).then((response) => {
      this.attrs = response.data;
      this.getList();
    });
  },
  methods: {
    /** 查询单定义列表 */
    getList() {
      this.loading = true;
      formDataList(
        this.formId,
        this.addDateRange(this.queryParams, this.dateRange)
      ).then((response) => {
        this.defList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    handleForm(row) {
      getDef(row._formId).then((response) => {
        this.jsonData = JSON.parse(response.data.defination);
      });
      this.editData = row;
      this.open = true;
    },
    close() {
      this.open = false;
      this.jsonData = {};
      this.editData = {};
    },
  },
};
</script>
