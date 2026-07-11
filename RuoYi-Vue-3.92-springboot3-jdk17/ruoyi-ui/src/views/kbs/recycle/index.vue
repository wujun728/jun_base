<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px" @submit.native.prevent>
      <el-form-item label="名称" prop="objectName">
        <el-input v-model="queryParams.objectName" placeholder="请输入名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="类型" prop="objectType">
        <el-select v-model="queryParams.objectType" placeholder="请选择类型" clearable>
          <el-option v-for="dict in dict.type.t_kbs_favorite_type" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-divider v-if="showSearch" />

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="multiple"
          @click="handleRecover"
          v-hasPermi="['recycle:recycle:recover']"
        >恢复</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['recycle:recycle:remove']"
        >彻底删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="recycleList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="名称" align="left" prop="objectName" show-overflow-tooltip />
      <el-table-column label="类型" align="center" prop="objectType" width="120">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.t_kbs_favorite_type" :value="scope.row.objectType" />
        </template>
      </el-table-column>
      <el-table-column label="创建人" align="center" prop="createBy" width="180" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="140">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleRecover(scope.row)" v-hasPermi="['recycle:recycle:recover']">恢复</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['recycle:recycle:remove']">彻底删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
  </div>
</template>

<script>
import { listRecycle, recoverRecycle, delRecycle } from "@/api/kbs/recycle/recycle";

export default {
  name: "KbsRecycle",
  dicts: ["t_kbs_favorite_type"],
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
      // 知识库文档回收站表格数据
      recycleList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        objectName: null,
        objectType: null,
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询知识库文档回收站列表 */
    getList() {
      this.loading = true;
      listRecycle(this.queryParams).then((res) => {
        this.recycleList = res.rows;
        this.total = res.total;
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
    /** 恢复按钮操作 */
    handleRecover(row) {
      const id = row.id || this.ids;
      recoverRecycle(id).then((res) => {
        if (res.code === 200) {
          this.getList();
          this.$modal.msgSuccess("恢复成功");
        }
      });
    },
    /** 彻底删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      const name = row.objectName;
      this.$modal
        .confirm("是否确认彻底删除[" + name + "]？删除后无法找回！")
        .then(function () {
          return delRecycle(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
  },
};
</script>
