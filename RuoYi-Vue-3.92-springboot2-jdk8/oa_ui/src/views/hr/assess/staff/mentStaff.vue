<template>
  <div class="divBox">
    <div class="form-wrapper">
      <el-card class="employees-card-bottom" v-show="checkBtn">
        <div>
          <formBox
            ref="formBox"
            @confirmData="confirmData"
            @openAssessed="openAssessed"
            @getExportData="getExportData"
            :total="total"
          />
          <div v-loading="loading">
            <el-table
              :data="tableData"
              style="width: 100%"
              row-key="id"
              :height="tableHeight"
              default-expand-all
              :tree-props="{ children: 'children' }"
            >
              <el-table-column prop="test.name" :label="$t('access.examinee')" min-width="100" />
              <el-table-column prop="frame.name" :label="$t('user.work.department')" min-width="120" />
              <el-table-column prop="name" :label="$t('user.work.assessmentname')" min-width="100" />
              <el-table-column prop="period" :label="$t('toptable.assessmentcycle')" min-width="100">
                <template slot-scope="scope">
                  <span>{{ $refs.formBox.getPeriodText(scope.row.period) }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="score" :label="$t('user.work.qssessmentscore')" min-width="80" />
              <el-table-column prop="level" :label="$t('access.assessmentgrade')" min-width="80" />
              <el-table-column prop="check.name" :label="$t('user.work.assessor')" min-width="80" />
              <el-table-column prop="status" :label="$t('hr.assessmentstatus')" min-width="80">
                <template slot-scope="scope">
                  <span>{{ $refs.formBox.getStatusText(scope.row.status) }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="id" :label="$t('access.assessmenttime')" min-width="150">
                <template slot-scope="scope">
                  <p class="p-time">
                    <span>{{ $t('access.start') }}：</span>
                    {{ $moment(scope.row.start_time).format('yyyy-MM-DD') }}
                  </p>
                  <p class="p-time">
                    <span>{{ $t('access.end') }}：</span>
                    {{ $moment(scope.row.end_time).format('yyyy-MM-DD') }}
                  </p>
                </template>
              </el-table-column>
              <el-table-column prop="address" :label="$t('public.operation')" fixed="right" width="180">
                <template slot-scope="scope">
                  <el-button
                    type="text"
                    @click="changeStaff(scope.row)"
                    v-hasPermi="['hr:assessStaff:assessmentStaff:edit']"
                    >查看</el-button
                  >

                  <el-button
                    type="text"
                    @click="handleDelete(scope.row)"
                    v-hasPermi="['hr:assessStaff:assessmentStaff:delete']"
                    >{{ $t('public.delete') }}</el-button
                  >

                  <el-button
                    v-if="scope.row.status == 3 || scope.row.status == 4"
                    type="text"
                    @click="handleScore(scope.row)"
                    v-hasPermi="['hr:assessStaff:assessmentStaff:record']"
                    >评分记录</el-button
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
      </el-card>
      <export-excel :template="false" :save-name="saveName" :export-data="exportData" ref="exportExcel" />
      <score-dialog ref="scoreDialog" :config="config" />
      <!-- 未考核人员 -->
      <notAssessed ref="sssessed"></notAssessed>
    </div>
    <!-- 考核详情   -->
    <auditProcess
      v-if="!checkBtn"
      :id="id"
      ref="auditProcess"
      :strType="`check`"
      :process-index="status"
      @auditProcess="auditProcess"
    />
  </div>
</template>

<script>
import { userAssessDelete, userAssessList, userAssessRemindApi, userAssessAbnormal } from '@/api/user'

export default {
  name: 'AssessRecord',
  components: {
    formBox: () => import('./components/formBox'),
    scoreDialog: () => import('@/views/user/assessment/components/scoreDialog'),
    auditProcess: () => import('@/views/user/assessment/components/auditProcess'),
    exportExcel: () => import('@/components/common/exportExcel'),
    notAssessed: () => import('./components/notAssessed')
  },

  data() {
    return {
      where: {
        page: 1,
        limit: 15
      },
      checkBtn: true,
      status: '',
      total: 0,
      loading: false,
      tableData: [],
      search: {
        time: '',
        period: '',
        status: '',
        frame: [],
        test_uid: [],
        check_uid: [],
        date: ''
      },
      showTips: 0,
      config: {},
      saveName: '导出.xlsx',
      exportData: {
        data: [],
        cols: [
          { wpx: 70 },
          { wpx: 70 },
          { wpx: 100 },
          { wpx: 100 },
          { wpx: 100 },
          { wpx: 100 },
          { wpx: 100 },
          { wpx: 100 },
          { wpx: 160 },
          { wpx: 160 }
        ],
        rows: [{ hpt: 30 }]
      },
      rows: []
    }
  },
  created() {
    this.getTableData()
  },
  methods: {
    getTips() {
      const data = {
        period: this.search.period,
        time: this.search.date
      }
      userAssessAbnormal(data).then((res) => {
        this.showTips = res.data.count
      })
    },
    openAssessed() {
      this.$refs.sssessed.openBox()
    },
    changeStaff(data) {
      this.id = data.id
      this.status = data.status
      this.checkBtn = false
    },
    auditProcess() {
      this.checkBtn = true
    },
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
      const result = await userAssessList(this.where)
      this.tableData = result.data.list
      this.loading = false
      this.total = result.data.count
      this.getTips()
    },
    // 批量导出
    getExportData() {
      let obj = {}
      obj = { ...this.where }
      obj.page = 1
      obj.limit = 0
      this.saveName =
        this.$t('access.performanceexport') + '_' + this.$moment(new Date()).format('MM-DD-HH-mm-ss') + '.xlsx'
      userAssessList(obj).then((res) => {
        const data = res.data.list
        if (data.length <= 0) {
          this.$message.error(this.$t('access.placeholder24'))
        } else {
          const aoaData = [
            [
              this.$t('user.work.name'),
              this.$t('user.work.department'),
              this.$t('user.work.assessmentname'),
              this.$t('toptable.assessmentcycle'),
              this.$t('user.work.qssessmentscore'),
              this.$t('access.assessmentgrade'),
              this.$t('user.work.assessor'),
              this.$t('hr.assessmentstatus'),
              this.$t('access.starttime'),
              this.$t('access.endtime')
            ]
          ]
          data.forEach((value) => {
            aoaData.push([
              value.test ? value.test.name : '--',
              value.frame ? value.frame.name : '--',
              value.name,
              this.$refs.formBox.getPeriodText(value.period),
              value.score,
              value.level,
              value.check ? value.check.name : '--',
              this.$refs.formBox.getStatusText(value.status),
              value.start_time,
              value.end_time
            ])
            this.rows.push({
              hpt: 30
            })
          })
          this.exportData.data = aoaData
          this.exportData.rows = this.rows
          this.$refs.exportExcel.exportExcel()
        }
      })
    },

    // 删除
    handleDelete(row) {
      debugger
      this.$modalForm(userAssessDelete(row.id)).then(() => {
        this.getTableData()
      })
    },
    handleScore(row) {
      this.config = {
        title: this.$t('access.scoringrecord'),
        width: '720px',
        id: row.id
      }
      this.$refs.scoreDialog.handleOpen()
    },
    async getAssessRemind(item) {
      await userAssessRemindApi(item.id)
    },
    confirmData(data) {
      this.where.page = 1
      if (data == 'reset') {
        this.where = {
          page: 1,
          limit: 15
        }
      } else {
        this.where = { ...this.where, ...data }
      }

      this.getTableData()
    }
  }
}
</script>

<style lang="scss" scoped>
.alert {
  margin-top: 10px;
  width: 100%;
  padding-top: 0;
}
::v-deep.cr-alert .el-icon-info {
  font-size: 16px;
}
::v-deep .el-alert {
  padding-left: 30px;
  border: 1px solid #1890ff;
  color: #1890ff;
  font-size: 13px;
  background-color: #edf7ff;
  line-height: 1;
  margin-bottom: 14px;
}
::v-deep .el-alert--info .el-alert__description {
  color: #303133;
  font-size: 13px;
  font-weight: 500;
}

.info {
  color: #1890ff;
  cursor: pointer;
}
.p-time {
  margin: 0 0 10px 0;
  padding: 0;
}
.p-time:last-of-type {
  margin: 0;
}

::v-deep .el-table th {
  background-color: #f7fbff;
}
::v-deep .el-drawer__body {
  height: 100%;
  overflow-y: auto;
}
</style>
