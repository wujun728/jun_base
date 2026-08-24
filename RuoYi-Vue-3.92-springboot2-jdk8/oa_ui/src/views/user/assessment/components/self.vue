<template>
  <div>
    <div>
      <oaFromBox :isAddBtn="false" :search="search" :total="total" :isViewSearch="false" @confirmData="confirmData">
      </oaFromBox>
      <div class="mt-10" v-if="tableData.length > 0">
        <div>
          <el-table
            :data="tableData"
            style="width: 100%"
            row-key="id"
            :loading="loading"
            default-expand-all
            :height="tableHeight"
            :tree-props="{ children: 'children' }"
          >
            <el-table-column prop="name" :label="$t('user.work.assessmentname')" min-width="130" />
            <el-table-column prop="period" :label="$t('toptable.assessmentcycle')" min-width="100">
              <template slot-scope="scope">
                <span>{{ getPeriodText(scope.row.period) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="score" :label="$t('user.work.qssessmentscore')" min-width="100" />
            <el-table-column prop="level" :label="$t('access.assessmentgrade')" min-width="100" />
            <el-table-column prop="check.name" :label="$t('user.work.assessor')" min-width="100" />
            <el-table-column prop="status" :label="$t('hr.assessmentstatus')" min-width="120">
              <template slot-scope="scope">
                <el-tag :type="getStatusTag(scope.row.status).type">{{ getStatusTag(scope.row.status).text }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="start_time" :label="$t('access.starttime')" min-width="100">
              <template slot-scope="scope">
                {{ $moment(scope.row.start_time).format('yyyy-MM-DD') }}
              </template>
            </el-table-column>
            <el-table-column prop="end_time" :label="$t('access.endtime')" min-width="100">
              <template slot-scope="scope">
                {{ $moment(scope.row.end_time).format('yyyy-MM-DD') }}
              </template>
            </el-table-column>
            <el-table-column prop="address" :label="$t('public.operation')" fixed="right" width="240">
              <template slot-scope="scope">
                <el-button v-if="scope.row.status !== 1" type="text" @click="handleCheck(scope.row, 'check')">
                  查看
                </el-button>

                <el-button
                  v-if="scope.row.status > 0 && scope.row.status < 5"
                  type="text"
                  @click="handleCheck(scope.row, 'selfEdit')"
                >
                  {{ scope.row.status === 1 ? '自评' : '修改自评' }}
                </el-button>

                <el-button
                  type="text"
                  v-if="scope.row.status !== 0 && scope.row.status !== 1"
                  @click="handleScore(scope.row)"
                  >{{ $t('access.scoringrecord') }}</el-button
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
      </div>
      <default-page v-else :index="0" :min-height="420" />
    </div>
    <score-dialog ref="scoreDialog" :config="config" />
  </div>
</template>

<script>
import { userAssessSubord } from '@/api/user'
import Common from '@/components/user/accessCommon'

export default {
  name: 'Self',
  components: {
    defaultPage: () => import('@/components/common/defaultPage'),
    scoreDialog: () => import('./scoreDialog'),
    oaFromBox: () => import('@/components/common/oaFromBox')
  },
  data() {
    return {
      tableData: [],
      where: {
        page: 1,
        limit: 15,
        period: ''
      },
      loading: false,
      periodOptions: Common.periodOption,
      period: '',
      total: 0,
      from: {
        status: '',
        id: '',
        tabIndex: '1'
      },
      type: 0,
      config: {},
      search: [
        {
          field_name: '考核周期',
          field_name_en: 'period',
          form_value: 'select',
          data_dict: Common.periodOption
        }
      ]
    }
  },
  mounted() {},
  methods: {
    confirmData(data) {
      if (data === 'reset') {
        this.where = {
          page: 1,
          limit: 15,
          period: ''
        }
      } else {
        this.where = { ...this.where, ...data }
      }
      this.getTableData()
    },
    async getTableData() {
      this.loading = true
      var data = {
        page: this.where.page,
        limit: this.where.limit,
        type: 0,
        period: this.where.period
      }
      const result = await userAssessSubord(data)
      this.loading = false
      this.tableData = result.data.list
      this.total = result.data.count
    },
    selectPeriod() {
      this.where.page = 1
      this.getTableData()
    },
    pageChange(page) {
      this.where.page = page
      this.getTableData()
    },
    handleSizeChange(val) {
      this.where.limit = val
      this.getTableData()
    },
    handleCheck(row, str) {
      this.from.status = row.status
      this.from.id = row.id
      this.$emit('selfHandleCheck', this.from, str, row)
    },
    getPeriodText(id) {
      const txt = Common.getPeriodText(id)
      return txt
    },
    getStatusText(id) {
      const txt = Common.getStatusText(id)
      return txt
    },
    getStatusTag(status) {
      return Common.getStatusTag(status)
    },
    handleScore(row) {
      this.config = {
        title: this.$t('access.scoringrecord'),
        width: '720px',
        id: row.id
      }
      this.$refs.scoreDialog.handleOpen()
    }
  }
}
</script>

<style lang="scss" scoped>
::v-deep .el-form-item {
  margin-bottom: 14px;
}
::v-deep .el-table th {
  background-color: #f7fbff;
}
::v-deep .from-s .flex-row .el-form-item {
  margin-right: 10px;
  margin-left: 0;
}
</style>
