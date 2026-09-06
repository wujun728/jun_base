<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    >
      <el-form-item label="数据集" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入数据集名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
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
      <el-table-column label="主键ID" align="center" prop="id" v-if="false" />
      <el-table-column label="数据集名称" align="center" prop="name" />
      <el-table-column
        label="表单定义"
        align="center"
        prop="defination"
        v-if="false"
      />
      <el-table-column
        label="关联流程"
        align="center"
        prop="refProcKey"
        v-if="false"
      />
      <el-table-column label="创建者" align="center" prop="createName" />
      <el-table-column label="创建时间" align="center" prop="createTime" />
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-search"
            @click="handleForm(scope.row)"
            >查看数据</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-set-up"
            @click="handleConfig(scope.row)"
            >编辑属性</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-share"
            @click="handleLink(scope.row)"
            >外链地址</el-button
          >
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
  </div>
</template>

<script>
import { listDef } from "@/api/form/form";

export default {
  name: "DataList",
  data() {
    return {
      jsonData: {},
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
    this.getList();
  },
  methods: {
    /** 查询单定义列表 */
    getList() {
      this.loading = true;
      listDef(this.queryParams).then((response) => {
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
      const formId = row.id;
      this.$router.push({ path: `/table/data/${formId}` });
    },
    handleConfig(row) {
      const formId = row.id;
      this.$router.push({ path: `/table/config/${formId}` });
    },
    handleLink(row) {
      const formId = row.id;
      this.$alert(
        "http://" + window.location.host + "/form/data/" + formId,
        "外链地址",
        {
          confirmButtonText: "确定",
          callback: (action) => {},
        }
      );
    },
  },
};
</script>
