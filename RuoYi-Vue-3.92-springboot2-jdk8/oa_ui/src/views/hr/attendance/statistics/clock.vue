<template>
  <div class="divBox">
    <el-card class="employees-card-bottom">
      <!-- 搜索条件 -->
      <div>
        <formBox @confirmData="confirmData" :total="total" :type="`clock`"></formBox>
      </div>
      <!-- 表格 -->
      <div class="table-box mt10">
        <el-table :data="tableData" v-loading="loading" style="width: 100%" :height="tableHeight">
          <el-table-column prop="card.name" label="姓名"> </el-table-column>
          <el-table-column prop="frame.name" label="部门"> </el-table-column>
          <el-table-column prop="group" label="考勤组">
            <template slot-scope="{ row }">
              {{ row.group || '--' }}
            </template>
          </el-table-column>
          <el-table-column prop="date" label="日期">
            <template slot-scope="{ row }">
              {{ $moment(row.created_at).format('YYYY-MM-DD') }}
            </template>
          </el-table-column>
          <el-table-column prop="date" label="星期">
            <template slot-scope="{ row }">
              {{ getWeek(row.created_at) }}
            </template>
          </el-table-column>
          <el-table-column prop="created_at" label="打卡时间"> </el-table-column>
          <el-table-column prop="date" label="操作">
            <template slot-scope="{ row }">
              <el-button type="text" @click="openDetails(row)">查看</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
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
    <!-- 查看详情 -->
    <details-drawer ref="detailsDrawer"></details-drawer>
    <!-- 导出 -->
    <export-excel :save-name="saveName" :export-data="exportData" ref="exportExcel" />
  </div>
</template>

<script>
import { clockRecordList } from '@/api/config'
import { toGetWeek } from '@/utils/format'
export default {
  name: 'CrmebOaEntDaily',
  components: {
    formBox: () => import('./components/formBox'),
    detailsDrawer: () => import('./components/detailsDrawer'),
    exportExcel: () => import('@/components/common/exportExcel')
  },

  data() {
    return {
      total: 0,
      loading: false,
      saveName: '人员打卡_记录_统计日期范围.xlsx',
      where: {
        page: 1,
        limit: 15,
        scope: '',
        frame_id: '',
        group_id: '',
        user_id: [],
        time: `${this.$moment().format('YYYY/MM/DD')}-${this.$moment().format('YYYY/MM/DD')}`
      },
      exportData: {
        data: [],
        cols: [{ wpx: 70 }, { wpx: 70 }, { wpx: 120 }, { wpx: 140 }, { wpx: 200 }, { wpx: 120 }, { wpx: 120 }]
      },
      tableData: []
    }
  },

  mounted() {
    this.getList()
  },

  methods: {
    openDetails(row) {
      this.$refs.detailsDrawer.openBox(row)
    },
    confirmData(data, type) {
      this.where.page = 1
      this.where.time = data.time
      this.where.scope = data.scope
      this.where.frame_id = data.frame_id
      this.where.user_id = data.user_id
      this.where.group_id = data.group_id
      if (type == '导出') {
        this.exportFn()
      } else {
        this.getList()
      }
    },
    async getList() {
      this.loading = true
      const result = await clockRecordList(this.where)
      this.loading = false
      this.total = result.data.count
      this.tableData = result.data.list
    },

    pageChange(page) {
      this.where.page = page
      this.getList()
    },
    async exportFn() {
      let aoaData = [['姓名', '部门', '考勤组', '日期', '星期', '打卡时间']]
      let obj = { ...this.where }
      obj.page = 0
      obj.limit = 0
      const result = await clockRecordList(obj)
      result.data.list.map((item) => {
        aoaData.push([
          item.card.name,
          item.frame.name,
          item.group,
          this.$moment(item.created_at).format('YYYY-MM-DD'),
          this.getWeek(item.created_at),
          item.created_at
        ])
      })
      this.exportData.data = aoaData
      this.$refs.exportExcel.exportExcel()
    },

    handleSizeChange(val) {
      this.where.limit = val
      this.getList()
    },
    getWeek(date) {
      // 参数时间戳
      return toGetWeek(date)
    }
  }
}
</script>

<style lang="scss" scoped>
.mb50 {
  margin-bottom: 50px;
}
::v-deep .el-table thead.is-group th {
  background-color: rgba(247, 251, 255, 1);
  border-color: #fff;
}
::v-deep .el-table {
  border: none;
}
.box {
  position: relative;
}
</style>
