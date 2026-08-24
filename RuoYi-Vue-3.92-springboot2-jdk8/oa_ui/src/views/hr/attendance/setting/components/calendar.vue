<template>
  <div>
    <el-dialog top="5%" :visible.sync="show" width="600" :close-on-click-modal="false" :show-close="false">
      <div slot="title" class="header">
        <span class="title">排班日历</span>

        <span class="el-icon-close" @click="handleClose"></span>
      </div>
      <div class="box">
        <div class="day">
          当前时间：{{ activateDate.year }}-{{
            this.activateDate.month < 10 ? '0' + this.activateDate.month : this.activateDate.month
          }}
          <span>（点击日历格可调整班休状态，历史日期不可调整，红色为休息日）</span>
        </div>
        <ve-calendar
          :activateDate="activateDate"
          v-model="selected"
          :right-menu="false"
          :over-hide="true"
          :min="min"
          ref="veCalendar"
          v-if="show"
        >
          <div slot="header" slot-scope="{ year, month }"></div>
          <div slot="day-number" slot-scope="{ day }">{{ day.sDay }}</div>
          <slot slot="day-lunar" slot-scope="{ day }">
            <span class="fz12">{{ getLunar(day, 5) }}</span>
          </slot>
        </ve-calendar>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="handleClose">取 消</el-button>
        <el-button type="primary" :loading="loading" @click="submit">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import veCalendar from 've-calendar'

import { putCalendarApi, calendarYearApi } from '@/api/config'

export default {
  name: 'CrmebOaEntCalendar',
  components: {
    veCalendar
  },
  data() {
    return {
      show: false,
      selected: [],
      activateDate: {
        year: 2023,
        month: 0
      },
      ids: [],
      selectList: [],
      loading: false,
      sundayOff: [],
      min: this.$moment(new Date()).add(1, 'days').format('YYYY-MM-DD')
    }
  },

  mounted() {},

  methods: {
    getLunar(day, length) {
      // 显示节气、节日、农历
      let lunar =
        day.solarTerms ||
        day.solarFestival ||
        day.lunarFestival ||
        (day.lDay == 1 ? day.lMonthChinese : day.lDayChinese) ||
        ''
      return lunar.substr(0, length)
    },

    async openBox(year, month) {
      this.activateDate.year = Number(year)
      this.activateDate.month = Number(month)
      let data = year + '-' + month
      const result = await calendarYearApi(data)
      this.selected = JSON.parse(JSON.stringify(result.data))
      this.sundayOff = this.getWeeks(this.activateDate.year, this.activateDate.month)
      this.show = true
    },
    submit() {
      // 重新组织接口需要的数据值
      let data = []
      this.selected.map((item) => {
        let val = {
          day: item,
          is_rest: '1'
        }
        data.push(val)
      })

      let newArr = this.sundayOff.filter((v) => this.selected.every((val) => val != v))

      newArr.map((n) => {
        let val = {
          day: n,
          is_rest: '0'
        }
        data.push(val)
      })
      data = data.filter(function (item, index) {
        return data.indexOf(item) === index
      })
      this.loading = true
      let date = this.activateDate.year + '-' + this.activateDate.month
      let val = {
        data: data
      }

      putCalendarApi(date, val)
        .then((res) => {
          this.loading = false
          this.handleClose()
          this.$emit('getList')
        })
        .catch((err) => {
          this.loading = false
        })
    },
    handleClose() {
      this.show = false
      this.selected = []
      this.activateDate = {
        year: 0,
        month: 0
      }
    },
    // 获取本月的休息时间
    getWeeks(year, month) {
      let isDays = new Date(year, month, 0).getDate() // 当前月份多少天

      const date = new Date()
      const currentYear = year
      let currentMonth = month
      if (currentMonth < 10) currentMonth = '0' + currentMonth
      const firstDate = new Date(`${currentYear}-${currentMonth}-01`)
      const day = parseInt(firstDate.getDay() == 0 ? 7 : firstDate.getDay())
      const days = new Date(currentYear, currentMonth, 0).getDate()

      // 计算当月周数
      let weeks
      const temp = days % 7
      if (7 - day >= temp) {
        weeks = parseInt(days / 7) + 1
      } else {
        weeks = parseInt(days / 7) + 2
      }
      const lastDay = new Date(`${currentYear}-${currentMonth}-${days}`).getDay()
      if (lastDay === 0) {
        weeks--
      }

      // 提取本月的周六和周末
      let freeDays = []
      if (day <= 7) {
        freeDays.push(7 - day)
        freeDays.push(1 + (7 - day))
      }

      for (let i = 1, j = weeks; i < j; i++) {
        const last = freeDays[freeDays.length - 1]

        if (last + 6 <= isDays) {
          freeDays.push(last + 6)
        }
        if (last + 7 <= isDays) {
          freeDays.push(last + 7)
        }
      }
      if (lastDay === 0) {
        // const last = freeDays[freeDays.length - 1]
        // freeDays.push(last + 6)
        // freeDays.push(last + 7)
      }

      if (freeDays[0] == 0) {
        freeDays.splice(0, 1)
      }

      let result = []
      freeDays.forEach((m) => {
        result.push(`${currentYear}-${currentMonth}-${m < 10 ? '0' + m : m}`)
      })

      return result
    }
  }
}
</script>

<style lang="scss" scoped>
.fz12 {
  font-size: 12px;
}
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;

  .title {
    font-size: 15px;
    font-family: PingFangSC-Medium, PingFang SC;
    font-weight: 600;
    color: #303133;
  }
  .el-icon-close {
    color: #c0c4cc;
    font-weight: 500;
    font-size: 14px;
  }
}
::v-deep.el-dialog {
  border-radius: 6px;
}
::v-deep .day-title {
  display: flex;
  flex-direction: column;
  align-items: center;
  height: 82px;
}
::v-deep .day-grid {
  cursor: pointer;
  height: 72px;
  padding-top: 19px;
  border-radius: 0;
  border: 1px solid #eeeeee;
}
::v-deep.day-grid:active {
  background-color: #fff;
}
::v-deep .title-grid {
  font-size: 15px;
  font-family: PingFang SC-Regular, PingFang SC;
  font-weight: 400;
  color: #303133;
}
::v-deep .day-number {
  height: 24px;
  line-height: 24px;
  display: flex;
  justify-content: center;
  font-size: 18px;
  font-family: PingFang SC-Medium, PingFang SC;
  font-weight: 500;
  div {
    color: #303133;
  }
}
::v-deep .day-lunar {
  font-size: 14px;
  font-weight: 400;
  display: flex;
  justify-content: center;
  font-family: PingFang SC-Regular, PingFang SC;
}
::v-deep .day-grid.selected {
  background-color: #fff;
  color: #ed4014 !important;
  .day-number div {
    color: #ed4014;
  }
}
::v-deep .week-title {
  height: 43px;
  background-color: #e7f3ff;
  line-height: 43px;
  font-family: PingFang SC-Regular, PingFang SC;
}
.box {
  ::v-deep .ve-calendar {
    padding: 0;
  }
  ::v-deep.day-grid.today {
    .day-number {
      width: 24px;
      height: 24px;
      color: #fff;
      font-size: 14px;
      background-color: #1890ff;
      border-radius: 50%;
      font-family: PingFang SC-Medium, PingFang SC;
      font-weight: 500;
    }
  }
  ::v-deep.day-grid.today.selected {
    .day-number {
      color: #fff !important;
      font-size: 14px;
      background-color: #ed4014;
      border-radius: 50%;
      font-family: PingFang SC-Medium, PingFang SC;
      font-weight: 500;
      div {
        color: #fff !important;
      }
    }
  }
  ::v-deep .day-grid.hide {
    visibility: none;
    border: 1px solid;
    ::v-deep .day-number {
      visibility: hidden;
    }
  }
  .day {
    font-size: 14px;
    font-family: PingFang SC-Semibold, PingFang SC;
    font-weight: 600;
    color: #303133;
    span {
      font-size: 14px;
      font-family: PingFang SC-Regular, PingFang SC;
      font-weight: 400;
      color: #909399;
    }
  }
}
</style>
