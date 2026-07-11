<template>
  <div>
    <el-calendar v-model="calendarDate">
      <template slot="dateCell" slot-scope="{ date, data }">
        <div class="custom-date-cell" :class="{ 'has-schedule': hasSchedule(date) }">
          <span class="cell-day">{{ date.getDate() }}</span>
          <span v-if="hasSchedule(date)" class="dot"></span>
        </div>
      </template>
    </el-calendar>
  </div>
</template>

<script>
import { parseTime } from "@/utils/ruoyi";
import { listContainDate } from "@/api/schedule/schedule";

export default {
  data() {
    return {
      // 当前时间
      calendarDate: new Date(),
      // 当前日历月
      currentMonth: null,
      // 日程数据
      scheduleList: [],
    };
  },
  watch: {
    calendarDate(newVal) {
      this.getScheduleData(newVal);
    },
  },
  mounted() {
    this.getScheduleData(this.calendarDate);
  },
  methods: {
    async getScheduleData(date) {
      if (!this.isChangeMonth(date)) {
        return;
      }
      this.currentMonth = parseTime(this.calendarDate, "{y}-{m}");
      const startTime = parseTime(this.calendarDate, "{y}-{m}-{d} {h}:{i}:{s}");
      this.$emit("date-change", startTime);
      this.getScheduleCalendar(startTime);
    },
    /** 获取包含日程的日期 */
    async getScheduleCalendar(date) {
      this.scheduleList = [];
      let time = !date ? parseTime(new Date(), "{y}-{m}-{d} {h}:{i}:{s}") : date;
      let params = { startTime: time };
      const res = await listContainDate(params);
      this.scheduleList = res.data;
    },
    /** 是否包含日程 */
    hasSchedule(date) {
      const dateStr = this.formatMonth(date);
      return this.scheduleList.includes(dateStr);
    },
    /**
     * 格式化
     */
    formatMonth(date) {
      const y = date.getFullYear();
      const m = (date.getMonth() + 1).toString().padStart(2, "0");
      const d = date.getDate().toString().padStart(2, "0");
      return `${y}-${m}-${d}`;
    },
    /** 月份是否变更，变更才重新请求数据 */
    isChangeMonth(date) {
      const dateMonth = parseTime(date, "{y}-{m}");
      return this.currentMonth != dateMonth;
    },
  },
};
</script>
  
<style scoped lang="scss">
::v-deep .el-calendar__button-group .el-button {
  font-size: 12px;
  padding: 5px 5px !important;
  margin: 0 2px;
}
::v-deep .el-calendar__header {
  padding: 10px 0;
}
::v-deep .el-calendar-day {
  height: 40px;
}
::v-deep .el-calendar__body {
  padding: 0;
}
::v-deep .el-calendar-table {
  width: 100%;
}
::v-deep .el-calendar-table thead th {
  font-size: 12px;
}
::v-deep .el-calendar__title {
  font-weight: bold;
}
::v-deep .el-calendar__body table td {
  height: 40px !important;
  vertical-align: middle;
  font-size: 12px;
}
::v-deep .el-calendar-table td .cell {
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}
::v-deep .el-calendar__button-group {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
::v-deep .el-calendar__button-group .el-button:nth-child(1) {
  & span {
    display: none;
  }
  &::before {
    content: "❮";
    font-size: 14px;
    margin-right: 5px;
  }
}
::v-deep .el-calendar__button-group .el-button:nth-child(2) {
  flex: 1;
  text-align: center;
  height: 26px;
}
::v-deep .el-calendar__button-group .el-button:nth-child(3) {
  & span {
    display: none;
  }
  &::before {
    content: "❯";
    font-size: 14px;
    margin-left: 5px;
  }
}
::v-deep .el-calendar__button-group .el-button.today-button {
  position: absolute;
  right: 12px;
  top: 5px;
  padding: 4px 10px;
  font-size: 14px;
  background-color: #409eff;
  color: #fff;
  border: none;
  border-radius: 4px;
}
.custom-date-cell {
  position: relative;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;

  &.has-schedule .dot {
    content: "";
    position: absolute;
    top: 0;
    right: -2px;
    width: 6px;
    height: 6px;
    background-color: red;
    border-radius: 50%;
  }
}
</style>