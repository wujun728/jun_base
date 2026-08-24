<template>
  <el-dialog :title="config.title" :visible.sync="dialogVisible" :width="config.width" :before-close="handleClose">
    <div class="mt15">
      <el-table :data="tableData" style="width: 100%">
        <el-table-column prop="card" :label="$t('hr.administrators')" width="240">
          <template slot-scope="scope">
            <div class="user-name">
              <img :src="scope.row.card.avatar" alt="" />
              <span>{{ scope.row.card.name }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="score" :label="$t('access.totalscore1')" />
        <el-table-column prop="total" :label="$t('access.totalscore')" />
        <el-table-column prop="created_at" :label="$t('toptable.operationtime')" width="180" />
      </el-table>
      <div class="text-right">
        <el-pagination
          :page-size="where.limit"
          :current-page="where.page"
          layout="prev, pager, next"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="pageChange"
        />
      </div>
    </div>
  </el-dialog>
</template>

<script>
import { userAssessRecord } from '@/api/user';

export default {
  name: 'ScoreDialog',
  props: {
    config: {
      type: Object,
      default: () => {
        return {};
      },
    },
  },
  data() {
    return {
      dialogVisible: false,
      where: {
        page: 1,
        limit: 15,
      },
      tableData: [],
      total: 0,
    };
  },
  watch: {
    config: {
      handler(nVal) {
        this.getList();
      },
      deep: true,
    },
  },
  methods: {
    handleOpen() {
      this.dialogVisible = true;
    },
    handleClose() {
      this.dialogVisible = false;
      this.tableData = [];
    },
    pageChange(page) {
      this.where.page = page;
      this.getList();
    },
    handleSizeChange(val) {
      this.where.limit = val;
      this.getList();
    },
    async getList() {
      const data = { page: this.where.page, limit: this.where.limit };
      const result = await userAssessRecord(this.config.id, data)
      this.tableData = result.data.list;
      this.total = result.data.count;
    },
  },
};
</script>

<style scoped lang="scss">

.user-name {
  display: flex;
  align-items: center;
  img {
    width: 24px;
    height: 24px;
    border-radius: 50%;
  }
  span {
    padding-left: 10px;
  }
}
</style>
