<template>
  <div>
    <el-dialog
      title="历史记录"
      :visible.sync="dialogVisible"
      width="700px"
      :append-to-body="true"
      :before-close="handleClose"
    >
      <el-table :data="tableData" style="width: 100%;">
        <el-table-column prop="position.name" label="职位" width="150"> </el-table-column>
        <el-table-column prop="col15" label="岗位分数" width="140"> </el-table-column>
        <el-table-column prop="card.name" label="创建人"> </el-table-column>
        <el-table-column prop="updated_at" label="更新时间" min-width="150"> </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" @click="apply(scope.row)">使用</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="paginationClass">
        <el-pagination
          :page-size="where.limit"
          :current-page="where.page"
          layout="total, prev, pager, next"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="pageChange"
        />
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { putHayHistoryApi } from '@/api/config.js';
export default {
  name: 'CrmebOaEntHistoryList',

  data() {
    return {
      dialogVisible: false,
      total: 0,
      where: {
        page: 1,
        limit: 15,
      },
      id: 0,
      tableData: [],
    };
  },

  mounted() {},

  methods: {
    openBox(data) {
      this.index = data.$index;
      this.dialogVisible = true;
      this.id = data.row.col1;
      this.getList(this.id);
    },
    handleClose() {
      this.dialogVisible = false;
    },
    async getList(id) {
      const result = await putHayHistoryApi(id, this.where)
      this.total = result.data.count;
      this.tableData = result.data.list;
    },
    pageChange(page) {
      this.where.page = page;
      this.getList(this.id);
    },
    handleSizeChange(val) {
      this.where.limit = val;
      this.getList(this.id);
    },
    // 使用
    apply(data) {
      this.handleClose();
      this.$emit('applyFn', data, this.index);
    },
  },
};
</script>

<style lang="scss" scoped>
::v-deep .el-dialog__body {
  padding-top: 15px;
  padding-right: 0;
  border-top: 1px solid #dcdfe6;
}
.paginationClass {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
