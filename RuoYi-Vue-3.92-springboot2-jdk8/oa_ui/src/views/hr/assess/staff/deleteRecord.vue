<template>
  <div class="divBox">
    <div class="form-wrapper">
      <el-card class="employees-card-bottom">
        <div class="title-16 mb20">删除记录</div>
        <div class="total-16 mb10">共 {{ total }} 条</div>
        <div v-loading="loading">
          <el-table :data="tableData" :height="tableHeight1" style="width: 100%" row-key="id" default-expand-all>
            <el-table-column prop="id" label="ID" min-width="55" />
            <el-table-column prop="mark" :label="$t('access.deletereason')" min-width="150" />
            <el-table-column prop="info.name" :label="$t('user.work.assessmentname')" min-width="140" />
            <el-table-column prop="card.name" :label="$t('access.deleteperson')" min-width="160" />
            <el-table-column prop="check.name" :label="$t('user.work.assessor')" min-width="160" />
            <el-table-column prop="test.name" :label="$t('access.examinee')" min-width="160" />
            <el-table-column prop="address" :label="$t('public.operation')" fixed="right" width="100">
              <template slot-scope="scope">
                <el-button
                  type="text"
                  @click="handleScore(scope.row)"
                  v-hasPermi="['hr:assessStaff:deleteRecord:details']"
                  >{{ $t('access.assessmentdetails') }}</el-button
                >
              </template>
            </el-table-column>
          </el-table>
          <div class="page-fixed">
            <el-pagination
              :page-size="where.limit"
              :current-page="where.page"
              :page-sizes="[15, 20, 30]"
              layout="total,sizes, prev, pager, next, jumper"
              :total="total"
              @size-change="handleSizeChange"
              @current-change="pageChange"
            />
          </div>
        </div>
      </el-card>
      <details-dialog ref="detailsDialog" :config="config" />
    </div>
  </div>
</template>

<script>
import { userAssessDeletesList } from '@/api/user'
export default {
  name: 'DeleteInfo',
  components: {
    detailsDialog: () => import('./components/detailsDialog')
  },

  data() {
    return {
      where: {
        page: 1,
        limit: 15
      },
      loading: false,
      tableHeight1: window.innerHeight - 244 + 'px',
      total: 0,
      tableData: [],
      config: {}
    }
  },
  created() {
    this.getTableData()
  },
  methods: {
    pageChange(page) {
      this.where.page = page
      this.getTableData()
    },
    handleSizeChange(val) {
      this.where.limit = val
      this.getTableData()
    },
    async getTableData() {
      this.loading = true
      var data = {
        page: this.where.page,
        limit: this.where.limit
      }
      const result = await userAssessDeletesList(data)
      this.tableData = result.data.list
      this.loading = false
      this.total = result.data.count
    },
    handleScore(row) {
      this.config = {
        title: this.$t('access.assessmentdetails'),
        width: '1100px',
        data: row.info
      }
      this.$refs.detailsDialog.handleOpen()
    }
  }
}
</script>

<style lang="scss" scoped>
::v-deep .el-drawer__body {
  height: 100%;
  overflow-y: auto;
}
</style>
