<template>
  <div class="container">
    <full-calendar firstDay="1" lang="zh" :currentDate="currentDate" :eventList="eventList" :scheduleData="scheduleData" @eventClick="eventClick"></full-calendar>
  </div>
</template> 
  <script>
import FullCalendar from "./full-calendar";
import { listHoliday } from "@/api/system/holidaySetting";
import { listWork } from "@/api/system/holidayWorkSetting";
import { listMonth } from "@/api/schedule/schedule";

export default {
  components: {
    FullCalendar,
  },
  data() {
    return {
      // 事件数组
      eventList: [],
      // 日程数据
      scheduleData: {},
      selectedDay: "",
      holidayStatus: "",
    };
  },
  props: {
    currentDate: {},
  },
  methods: {
    /** 获取日历数据 */
    getScheduleData(queryParams) {
      this.scheduleData = {};
      listMonth(queryParams).then((response) => {
        this.scheduleData = response.data;
      });
    },
    eventClick(date, event, jsEvent, pos) {
      this.selectedDay = this.parseTime(date, "{y}-{m}-{d}");
    },
    /** 获取假日信息 */
    getHolidayInfo(queryParams) {
      this.eventList = [];
      let date = queryParams.startTime;
      let holidayParam = { startDate: date };
      listHoliday(holidayParam).then((res) => {
        if (res.data && res.data.length > 0) {
          res.data.forEach((holiday) => {
            this.eventList.push({ id: holiday.id, holidayName: holiday.holidayName, start: holiday.startDate, end: holiday.endDate, type: 1 });
          });
        }
      });
      let workParam = { workDate: date };
      listWork(workParam).then((res) => {
        if (res.data && res.data.length > 0) {
          res.data.forEach((work) => {
            this.eventList.push({ id: work.id, start: work.workDate, end: work.workDate, type: 2 });
          });
        }
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.container {
  margin: 0 auto;
  background: #fff;
  .buttons {
    margin-left: 50px;
  }
}
</style>
  