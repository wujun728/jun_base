<template>
  <el-drawer :direction="'rtl'" title="权限配置" :visible.sync="dialogFormVisible" size="50%">
    <div class="app-container">
      <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="数据权限名称" prop="scopeName">
          <el-input
            v-model="queryParams.scopeName"
            placeholder="请输入数据权限名称"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table ref="table" v-loading="loading" :data="ruleList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="资源编号" align="center" prop="resourceCode" />
        <el-table-column label="数据权限名称" align="center" prop="scopeName" />
        <el-table-column label="数据权限类型" align="center" prop="scopeType">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.data_rule_type" :value="scope.row.scopeType"/>
          </template>
        </el-table-column>
        <el-table-column label="数据库表" align="center" prop="tableName" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['system:rule:edit']"
            >修改</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['system:rule:remove']"
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

    </div>
    <div class="drawer-footer">
      <el-button @click="dialogFormVisible = false">
        取 消
      </el-button>
      <el-button type="primary" :loading="loading" @click="updateData">
        确 定
      </el-button>
    </div>
  </el-drawer>
</template>

<script>
import { listDataRule, updateRoleDataRules, fetchRoleDataRuleList, addDataRule, updateDataRule, exportDataRule } from "@/api/system/dataRule";

export default {
  name: 'RoleDataRuleList',
  dicts: ['data_rule_type'],
  data() {
    return {
      dialogFormVisible: false,
      // 遮罩层
      loading: true,
      // 导出遮罩层
      exportLoading: false,
      // 选中数组
      ids: [],
      temp: {
        roleId: undefined,
        ids: []
      },
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 数据权限表格数据
      ruleList: [],
      multipleSelection: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        scopeName: null,
        roleId: null
      }
    };
  },
  methods: {
    handleRoleDataRuleUpdate(id) {
      this.loading = false
      this.dialogFormVisible = true
      this.queryParams.roleId = id
      this.temp.roleId = id
      console.log('id',id)
      this.handleQuery()
    },
    /** 查询数据权限列表 */
    getList() {
      this.loading = true
      fetchRoleDataRuleList(this.queryParams).then(response => {
        this.ruleList = response.rows;
        this.total = response.total;
        this.loading = false;
        this.$nextTick(() => {
          this.ruleList.forEach(item => {
            if (item.select === true) {
              this.$refs.table.toggleRowSelection(item, true)
            }
          })
        })
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },

    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    updateData() {
      const ids = []
      this.multipleSelection.forEach(item => {
        ids.push(item.id)
      })
      this.temp.ids = ids.join(',')
      updateRoleDataRules(this.temp).then(response => {
        this.dialogFormVisible = false
        this.$modal.msgSuccess("修改成功");
      })
    }

  }
}
</script>
<style>
.drawer-footer {
  float: right;
  margin-right: 30px
}
</style>
