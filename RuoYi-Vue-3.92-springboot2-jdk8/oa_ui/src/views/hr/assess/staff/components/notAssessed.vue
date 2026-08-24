<template>
  <div>
    <!-- 查看未考核人员 -->
    <el-drawer title="未创建考核人员" :visible.sync="drawer" size="700px" :before-close="handleClose">
      <div class="box">
        <!-- 筛选 -->
        <el-form :inline="true" class="from-s">
          <div class="flex">
            <el-form-item :label="$t('toptable.assessmentcycle')" class="select-bar">
              <el-select
                v-model="tableFrom.period"
                size="small"
                @change="changePeriod"
                :placeholder="$t('finance.pleaseselect')"
                style="width: 150px"
              >
                <el-option
                  v-for="(item, index) in periodOptions"
                  :key="index"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
            <template v-if="tableFrom.period">
              <el-form-item :label="$t('access.assessmenttime')" class="select-bar">
                <el-date-picker
                  class="time"
                  v-if="tableFrom.period === 1 || tableFrom.period === 2"
                  ref="getDateValue"
                  v-model="tableFrom.time"
                  size="small"
                  :type="dateArray[tableFrom.period - 1].type"
                  :format="dateArray[tableFrom.period - 1].format"
                  :placeholder="dateArray[tableFrom.period - 1].text"
                  @change="getDateValue"
                />
                <el-date-picker
                  v-else-if="tableFrom.period === 3"
                  ref="getDateValue"
                  v-model="tableFrom.time"
                  class="time"
                  size="small"
                  :type="dateArray[4].type"
                  :format="dateArray[4].format"
                  :placeholder="dateArray[4].text"
                  clearable
                  @change="getDateValue"
                />

                <dateQuarter
                  v-if="quarterBtn"
                  ref="dateQuarter"
                  :get-value="getQuarterDate"
                  :half-year-btn="halfYearBtn"
                />
              </el-form-item>
              <el-form-item>
                <el-tooltip effect="dark" content="重置搜索条件" placement="top">
                  <div class="reset" @click="reset"><i class="iconfont iconqingchu"></i></div>
                </el-tooltip>
              </el-form-item>
            </template>
          </div>
        </el-form>
        <!-- 表格 -->
        <el-table ref="multipleTable" :data="listData" tooltip-effect="dark" style="width: 100%">
          <el-table-column prop="name" label="人员姓名"> </el-table-column>
          <el-table-column prop="title" label="考核名称"> </el-table-column>
          <el-table-column prop="frame.name" label="部门"> </el-table-column>
          <el-table-column prop="job.name" label="职位"> </el-table-column>
          <el-table-column prop="super.name" label="直属上级"> </el-table-column>
        </el-table>
        <div class="block mt10 text-right">
          <el-pagination
            :page-size="tableFrom.limit"
            :current-page="tableFrom.page"
            layout="total, prev, pager, next, jumper"
            :total="total"
            @current-change="pageChange"
          />
        </div>
      </div>
    </el-drawer>
  </div>
</template>
<script>
import { userAssessAbnormalList } from '@/api/user'
export default {
  components: {
    dateQuarter: () => import('@/components/form-common/select-dateQuarter')
  },
  data() {
    return {
      drawer: false,
      quarterBtn: false,
      listData: [],
      dateArray: [
        { value: 1, type: 'week', text: '选择周', format: 'yyyy 第 WW 周' },
        { value: 2, type: 'month', text: '选择月份', format: 'yyyy-MM' },
        { value: 4, type: '' },
        { value: 5, type: '' },
        { value: 3, type: 'year', text: '选择年份', format: 'yyyy' }
      ],
      total: 0,
      timeVal: [],
      tableFrom: { limit: 15, page: 1, period: 2, time: '' },
      periodOptions: [
        { value: 2, label: '月考核' },
        { value: 1, label: '周考核' },
        { value: 5, label: '季度考核' },
        { value: 4, label: '半年考核' },
        { value: 3, label: '年考核' }
      ],
      halfYearBtn: false
    }
  },

  methods: {
    handleClose() {
      this.drawer = false
    },
    reset() {
      this.tableFrom.period = 2
      this.tableFrom.time = this.$moment().startOf('month').format('YYYY-MM-DD 00:00:00')
      this.quarterBtn = false
      this.getList()
    },
    async getList() {
      const result = await userAssessAbnormalList(this.tableFrom)
      this.listData = result.data.list
      this.total = result.data.count || 0
    },
    pageChange(page) {
      this.tableFrom.page = page
      this.getList()
    },
    getQuarterDate(data) {
      this.tableFrom.time = this.getQuarterTime(this.tableFrom.period, data)
      this.getList()
    },
    getQuarterTime(type, time) {
      if (!time) return false
      var str = ''
      const timeArr = time.split('-')
      const year = timeArr[0]
      const month = timeArr[1]
      if (type === 5) {
        if (month === '01') {
          str = year + '-01-01 00:00:00'
        } else if (month === '02') {
          str = year + '-04-01 00:00:00'
        } else if (month === '03') {
          str = year + '-07-01 00:00:00'
        } else if (month === '04') {
          str = year + '-10-01 00:00:00'
        }
      } else if (type === 4) {
        if (month === '01') {
          str = year + '-01-01 00:00:00'
        } else if (month === '02') {
          str = year + '-07-01 00:00:00'
        }
      }
      return str
    },

    openBox() {
      this.getList()
      this.tableFrom.time = this.$moment().startOf('month').format('YYYY-MM-DD 00:00:00')
      this.drawer = true
    },
    changePeriod(e) {
      // 切换季度半年考核
      if (e == 4) {
        this.quarterBtn = true
        this.halfYearBtn = true
        setTimeout(() => {
          this.$refs.dateQuarter.showValue = ''
        }, 200)
      } else if (e == 5) {
        this.quarterBtn = true
        this.halfYearBtn = false
        setTimeout(() => {
          this.$refs.dateQuarter.showValue = ''
        }, 200)
      } else {
        this.quarterBtn = false
      }
      this.timeVal = []
      this.tableFrom.time = ''
      this.tableFrom.page = 1
      this.getList()
    },
    getDateValue(e) {
      this.timeVal = []
      if (!e) {
        this.tableFrom.time = ''
      } else {
        if (this.tableFrom.period == 1) {
          this.tableFrom.time = this.$moment(this.tableFrom.time).format('YYYY-MM-DD 00:00:00')
        } else if (this.tableFrom.period == 2) {
          this.tableFrom.time = this.$moment(this.tableFrom.time).format('YYYY-MM-DD 00:00:00')
        } else if (this.tableFrom.period == 3) {
          this.tableFrom.time = this.$moment(this.tableFrom.time).format('YYYY-MM-DD 00:00:00')
        } else {
          this.quarterBtn = true
        }
      }
      this.tableFrom.page = 1
      this.getList()
    }
  }
}
</script>
<style scoped lang="scss">
.box {
  padding: 20px;
}
::v-deep.el-form-item--medium .el-form-item__label {
  line-height: 32px;
  margin-left: 10px;
}
</style>
