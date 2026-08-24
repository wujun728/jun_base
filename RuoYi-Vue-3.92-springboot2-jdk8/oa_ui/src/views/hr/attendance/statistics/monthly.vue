<template>
  <div class="divBox">
    <el-card class="normal-page">
      <!-- 搜索条件 -->
      <formBox :type="`month`" @confirmData="confirmData" :total="total"></formBox>
      <!-- 表格 -->
      <el-table class="mt10" v-loading="loading" :data="tableData" style="width: 100%" :height="tableHeight">
        <el-table-column prop="card.name" label="姓名"> </el-table-column>
        <el-table-column prop="frame.name" label="部门"> </el-table-column>
        <el-table-column label="出勤统计">
          <el-table-column prop="group" label="考勤组名称" width="120">
            <template slot-scope="scope">
              {{ scope.row.group || '--' }}
            </template>
          </el-table-column>
          <el-table-column prop="required_days" label="应出勤天数" width="120"> </el-table-column>
          <el-table-column prop="actual_days" label="实际出勤天数" width="120"> </el-table-column>
        </el-table-column>
        <el-table-column label="异常统计">
          <el-table-column prop="late" label="迟到次数" width="120"> </el-table-column>
          <el-table-column prop="leave_early" label="早退次数" width="120"> </el-table-column>
          <el-table-column prop="late_card" label="上班缺卡" width="120"> </el-table-column>
          <el-table-column prop="early_card" label="下班缺卡" width="120"> </el-table-column>
          <el-table-column prop="absenteeism" label="旷工天数" width="120"> </el-table-column>
        </el-table-column>
        <el-table-column prop="overtime_hours" label="加班（小时）" width="130"> </el-table-column>
        <el-table-column prop="trip_hours" label="出差（小时）" width="130"> </el-table-column>
        <el-table-column prop="out_hours" label="外出（小时）" width="130"> </el-table-column>
        <el-table-column label="请假">
          <div v-for="(item, index) in holiday_type" :key="index">
            <el-table-column
              prop="holiday_data"
              :label="item.name + (item.duration_type === 0 ? ' (天)' : ' (小时)')"
              width="120"
            >
              <template slot-scope="scope">
                {{ scope.row.holiday_data[index].duration }}
              </template>
            </el-table-column>
          </div>
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
    </el-card>
    <!-- 导出 -->
    <export-excel :save-name="saveName" :export-data="exportData" ref="exportExcel" />
  </div>
</template>

<script>
import { monthlyStatisticsApi } from '@/api/config'

export default {
  name: 'CrmebOaEntDaily',
  components: {
    formBox: () => import('./components/formBox'),
    exportExcel: () => import('@/components/common/exportExcel')
  },

  data() {
    return {
      total: 0,
      saveName: '上下班打卡_月报_统计日期范围.xlsx',
      exportData: {
        data: [],
        cols: []
      },
      loading: false,
      where: {
        page: 1,
        limit: 15,
        scope: '',
        status: '',
        frame_id: '',
        user_id: [],
        group_id: '',
        time: ''
      },
      tableData: [],
      holiday_type: []
    }
  },

  mounted() {
    this.getList()
  },

  methods: {
    async getList() {
      this.loading = true
      const result = await monthlyStatisticsApi(this.where)
      this.loading = false
      this.total = result.data.count
      this.tableData = result.data.list
      this.holiday_type = result.data.holiday_type
    },
    getType(val, list) {
      let str = ''
      for (let i = 0; i < list.length; i++) {
        if (list[i].name == val) {
          str = list[i].duration
          break
        }
      }
      return str
    },

    confirmData(data, type) {
      this.where.page = 1
      this.where.scope = data.scope
      this.where.status = data.status
      this.where.frame_id = data.frame_id
      this.where.user_id = data.user_id
      this.where.group_id = data.group_id
      this.where.time = data.time
      if (type == '导出') {
        this.exportFn()
      } else {
        this.getList()
      }
    },
    async exportFn() {
      let aoaData = [
        [`统计时间：${this.where.time} 制表时间：${this.$moment(new Date()).format('YYYY/MM/DD')}`],
        [
          '姓名',
          '部门',
          '考勤组名称',
          '应出勤天数',
          '实际出勤天数',
          '迟到次数',
          '早退次数',
          '上班缺卡',
          '下班缺卡',
          '旷工天数',
          '出差（小时)',
          '外出（小时)'
        ]
      ]
      let nameArr = []
      this.holiday_type.map((item) => {
        nameArr.push(item.name)
      })
      aoaData[1] = aoaData[1].concat(nameArr)

      let obj = { ...this.where }
      obj.page = 0
      obj.limit = 0

      const result = await monthlyStatisticsApi(obj)
      result.data.list.map((item) => {
        aoaData.push([
          item.card.name,
          item.frame.name,
          item.group,
          item.required_days,
          item.actual_days,
          item.late,
          item.leave_early,
          item.late_card,
          item.early_card,
          item.absenteeism,
          item.trip_hours,
          item.out_hours,
          this.getType(nameArr[0], item.holiday_data),
          this.getType(nameArr[1], item.holiday_data),
          this.getType(nameArr[2], item.holiday_data),
          this.getType(nameArr[3], item.holiday_data),
          this.getType(nameArr[4], item.holiday_data),
          this.getType(nameArr[5], item.holiday_data),
          this.getType(nameArr[6], item.holiday_data)
        ])
      })

      this.exportData.data = aoaData
      this.$refs.exportExcel.exportExcel()
    },

    pageChange(page) {
      this.where.page = page
      this.getList()
    },
    handleSizeChange(val) {
      this.where.limit = val
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
::v-deep .el-table thead.is-group th {
  background-color: rgba(247, 251, 255, 1);
  border-color: #fff;
}
::v-deep .el-table {
  border: none;
}
::v-deep .el-table td {
  border: none;
}
</style>
