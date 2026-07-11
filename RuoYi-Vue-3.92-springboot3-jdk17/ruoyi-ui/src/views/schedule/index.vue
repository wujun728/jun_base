
<template>
  <div class="schedule-container">
    <div class="schedule-bar">
      <Header ref="header" @title-change="titleChange" @filter-change="filterChange" @add-schedule="addSchedule" />
    </div>
    <div class="schedule-body">
      <!-- 左侧日历、任务 -->
      <div class="left-flex">
        <Calendar ref="calendar" @date-change="dateChange" />
        <ScheduleType ref="myType" @type-change="typeChange" />
      </div>
      <!-- 右侧日历日程 -->
      <div class="right-flex">
        <CustomCalendar ref="customCalendar" :currentDate="currentDate" />
      </div>
    </div>
  </div>
</template>
  
<script>
import Calendar from "./components/calendar";
import CustomCalendar from "./components/custom-calendar/index.vue";
import Header from "./components/header/index.vue";
import ScheduleType from "./components/type/index.vue";

export default {
  components: { Calendar, CustomCalendar, Header, ScheduleType },
  data() {
    return {
      // 日程请求参数
      queryParams: {},
      // 当前日期
      currentDate: new Date(),
    };
  },
  methods: {
    /** 日历变更 */
    dateChange(date) {
      this.queryParams.startTime = date;
      this.currentDate = new Date(date);
      this.resetParam();
      this.updateScheduleData();
      this.updateHolidayData();
    },
    /** 新增日程 */
    addSchedule() {
      this.updateScheduleData();
      this.updateCalendarScheduleData();
    },
    /** 我的分类变更 */
    typeChange(type) {
      this.queryParams.typeId = type;
      this.updateScheduleData();
      this.$refs.header.resetType();
    },
    /** 标题变更 */
    titleChange(title) {
      this.queryParams.title = title;
      this.updateScheduleData();
    },
    /** 条件过滤 */
    filterChange(level, partType) {
      this.queryParams.level = level;
      this.queryParams.partType = partType;
      this.updateScheduleData();
    },
    /** 重置请求参数，只保留日期 */
    resetParam() {
      this.queryParams.typeId = null;
      this.queryParams.partType = null;
      this.queryParams.level = null;
      this.$refs.myType.resetType();
      this.$refs.header.resetFilter();
    },
    /** 更新日程数据 */
    updateScheduleData() {
      this.$refs.customCalendar.getScheduleData(this.queryParams);
    },
    /** 更新节假日数据 */
    updateHolidayData() {
      this.$refs.customCalendar.getHolidayInfo(this.queryParams);
    },
    /** 更新日历日程数据 */
    updateCalendarScheduleData() {
      this.$refs.calendar.getScheduleCalendar();
    },
  },
};
</script>
  
<style scoped lang="scss">
.schedule-container {
  overflow: hidden;
}
.schedule-body {
  padding: 10px 10px 10px 15px;
  display: flex;
}
.schedule-bar {
  height: 50px;
  background-color: #fff;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  transition: all 0.3s ease;
}

.left-flex {
  width: 20%;
  border: 1px solid #ebeef5;
  box-shadow: 0 4px 12px 0 rgb(0 0 0 / 10%);
  padding: 10px;
  margin-right: 10px;
  max-height: calc(88vh - 66px);
  overflow-y: auto;
}
.right-flex {
  width: 80%;
  border: 1px solid #ebeef5;
  box-shadow: 0 4px 12px 0 rgb(0 0 0 / 10%);
  padding: 10px;
  max-height: calc(88vh - 66px);
  overflow-y: auto;
}
</style>