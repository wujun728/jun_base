<template>
  <div class="msg-container">
    <div class="position-title">
      <span>
        <svg-icon icon-class="msg" class="msg-flag" />日程
      </span>
      <span class="load-more" @click="loadMore">
        更多
        <i class="el-icon-arrow-right" />
      </span>
    </div>
    <div class="msg-body">
      <div class="custom-calendar">
        <!-- 日历头部 -->
        <div class="calendar-header">
          <div class="left-section">
            <span class="current-date">{{ formatDate(currentDate) }}</span>
          </div>
          <div class="right-section">
            <el-button size="mini" @click="prevWeek">❮</el-button>
            <el-button size="mini" @click="goToToday">今天</el-button>
            <el-button size="mini" @click="nextWeek">❯</el-button>
          </div>
        </div>

        <!-- 日历主体 -->
        <div class="calendar-body">
          <table>
            <thead>
              <tr>
                <th v-for="day in weekDays" :key="day">{{ day }}</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td v-for="(cell, index) in currentWeek" :key="index" :class="{ 'selected': isSelected(cell) }" @click="selectDate(cell)">
                  <span :class="{ 'today-bg': isToday(cell) }">{{ cell.getDate() }}</span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- 日程数据展示 -->
        <div class="schedule-data" ref="scheduleData" v-loading="loading">
          <div v-for="item in scheduleList" :key="item.time" @click="goDetail(item)" class="schedule-item">
            <span class="schedule-title">
              <span v-if="item.scheduleType && item.scheduleType.tagName" class="ml5">
                <el-tag
                  size="mini"
                  :effect="item.scheduleType.tagEffect || 'plain'"
                  :type="item.scheduleType.tagType || ''"
                >{{ item.scheduleType.tagName }}</el-tag>
              </span>
              <el-tooltip v-if="item.title.length >= 16" class="item" effect="dark" :content="item.title" placement="top">
                <span class="ml5">{{ item.title }}</span>
              </el-tooltip>
              <span v-else class="ml5">{{ item.title }}</span>
            </span>
            <span class="schedule-date">{{ parseTime(item.startTime, '{h}:{i}') }} - {{ parseTime(item.endTime, '{h}:{i}') }}</span>
          </div>
          <el-empty v-if="!hasData" :image-size="50"></el-empty>
        </div>
      </div>
    </div>
    <dialog-detail :open="open" :id="scheduleId" :dateDesc="dateDesc" @close="close" />
  </div>
</template>

<script>
import DialogDetail from "@/views/schedule/components/schedule/detail";
import { listDay } from "@/api/schedule/schedule";
import { parseTime } from "@/utils/ruoyi";

export default {
  name: "MessageModule",
  components: {
    DialogDetail,
  },
  data() {
    return {
      // 加载状态
      loading: false,
      // 是否有数据
      hasData: true,
      // 当前时间
      currentDate: new Date(),
      // 选中日期
      selectedDate: new Date(),
      // 当前周
      currentWeek: [],
      // 总数
      total: 0,
      // 日程数据
      scheduleList: [],
      // 日程请求参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
      },
      // 日程详情弹框
      open: false,
      // 日程id
      scheduleId: null,
      // 日期描述
      dateDesc: null,
      weekDays: ["一", "二", "三", "四", "五", "六", "日"],
      weekNames: ["周一", "周二", "周三", "周四", "周五", "周六", "周日"],
      monthNames: ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"],
    };
  },
  created() {
    this.handleCurrentWeek(new Date());
    this.getScheduleData();
  },
  methods: {
    // 获取当天日程
    getScheduleData() {
      this.loading = true;
      this.total = 0;
      this.hasData = true;
      this.scheduleList = [];
      const startTime = parseTime(this.currentDate, "{y}-{m}-{d} {h}:{i}:{s}");
      this.queryParams.startTime = startTime;
      this.queryParams.endTime = startTime;
      listDay(this.queryParams).then((res) => {
        if (res.code == 200) {
          this.scheduleList = res.rows;
          this.total = res.total;
          this.hasData = this.total > 0;
          this.loading = false;
        }
      });
    },
    /** 处理当前周 */
    handleCurrentWeek(date) {
      this.currentDate = date;
      const startOfWeek = this.getStartOfWeek(date);
      this.currentWeek = [];
      for (let i = 0; i < 7; i++) {
        const day = new Date(startOfWeek);
        day.setDate(startOfWeek.getDate() + i);
        this.currentWeek.push(day);
      }
    },
    /** 获取周的第一天 */
    getStartOfWeek(date) {
      const day = date.getDay();
      const diff = date.getDate() - (day === 0 ? 6 : day - 1);
      return new Date(date.getFullYear(), date.getMonth(), diff);
    },
    /** 前一周 */
    prevWeek() {
      const newDate = new Date(this.currentWeek[0]);
      newDate.setDate(newDate.getDate() - 7);
      this.handleCurrentWeek(newDate);
    },
    /** 下一周 */
    nextWeek() {
      const newDate = new Date(this.currentWeek[0]);
      newDate.setDate(newDate.getDate() + 7);
      this.handleCurrentWeek(newDate);
    },
    /** 今天 */
    goToToday() {
      const today = new Date();
      this.selectedDate = today;
      this.handleCurrentWeek(today);
      this.getScheduleData();
    },
    /** 是否为今天 */
    isToday(date) {
      const today = new Date();
      return date && date.getFullYear() === today.getFullYear() && date.getMonth() === today.getMonth() && date.getDate() === today.getDate();
    },
    /** 是否选中 */
    isSelected(date) {
      if (this.isToday(date)) return false;
      return (
        date &&
        date.getFullYear() === this.selectedDate.getFullYear() &&
        date.getMonth() === this.selectedDate.getMonth() &&
        date.getDate() === this.selectedDate.getDate()
      );
    },
    /** 日期选择事件 */
    selectDate(date) {
      this.currentDate = date;
      this.selectedDate = date;
      this.getScheduleData();
    },
    /** 日期格式化 */
    formatDate(date) {
      const y = date.getFullYear();
      const m = (date.getMonth() + 1).toString().padStart(1, "0");
      const d = date.getDate().toString().padStart(2, "0");
      return `${y}年${m}月${d}日`;
    },
    /** 查看更多 */
    loadMore() {
      this.$router.push("/schedule/plan");
    },
    /** 查看详情 */
    goDetail(item) {
      this.open = true;
      this.scheduleId = item.id;
      let dt = this.selectedDate;
      let m = this.monthNames[dt.getMonth()] + dt.getDate() + "日";
      let w = "（" + (this.isToday(dt) ? "今天" : this.weekNames[dt.getDay() - 1 < 0 ? 6 : dt.getDay() - 1]) + "）";
      this.dateDesc = m + w;
    },
    close() {
      this.scheduleId = null;
      this.open = false;
    },
  },
};
</script>

<style scoped lang="scss">
.msg-container {
  height: calc(38vh - 10px);
  border-radius: 4px;
  border: 1px solid #f5f5f5;
  box-sizing: border-box;
  background-color: #ffffff;
  box-shadow: 0 2px 12px 0 rgb(0 0 0 / 10%);
}
.position-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 14px;
  font-weight: bold;
  border-bottom: 1px solid #f1f1f1;
  padding: 12px;
}

.msg-flag {
  margin-right: 3px;
  width: 14px;
  height: 14px;
}

.msg-body {
  overflow: hidden;
}

.custom-calendar {
  margin: 0 auto;
  border-radius: 4px;
  background-color: #fff;

  .calendar-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px 20px;

    .left-section {
      display: flex;
      align-items: center;
    }
    .right-section {
      margin-left: auto;
      display: flex;
    }
    .current-date {
      font-size: 14px;
    }
  }

  .calendar-body {
    padding: 0 10px;
    padding-bottom: 5px;
    font-size: 14px;
    border-bottom: 1px solid #f1f1f1;

    table {
      width: 100%;
      border-collapse: collapse;

      th,
      td {
        width: 14.28%;
        height: 30px;
        text-align: center;
        vertical-align: middle;
        font-size: 12px;
        cursor: pointer;

        &.today {
          background-color: #409eff;
          color: #fff;
        }

        span.today-bg {
          display: inline-block;
          width: 24px;
          height: 24px;
          line-height: 24px;
          border-radius: 50%;
          background-color: #409eff;
          color: white;
        }

        &.selected span {
          background-color: #d3e9ff;
          color: #409eff;
          display: inline-block;
          width: 24px;
          height: 24px;
          line-height: 24px;
          border-radius: 50%;
        }
      }
      th {
        color: #909399;
      }
      td {
        color: #606266;
      }
    }
  }

  .schedule-data {
    height: calc(36vh - 156px);
    overflow-y: auto;
    overflow-x: hidden;
    font-size: 14px;

    .schedule-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 12px 12px 12px 24px;
      cursor: pointer;
      transition: background-color 0.3s;

      &:hover {
        background-color: #f5f7fa;
      }
      .schedule-title {
        position: relative;
        padding-left: 5px;
        flex: 1;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        font-size: 14px;
        color: #333;
        line-height: 1.2;
        display: inline-block;

        &::before {
          content: "";
          position: absolute;
          left: 0;
          top: 50%;
          transform: translateY(-50%);
          width: 2px;
          height: 12px;
          background-color: #409eff;
          border-radius: 2px;
        }
      }

      .schedule-date {
        margin-left: 12px;
        font-size: 12px;
        color: #999;
        white-space: nowrap;
        margin-right: 5px;
      }
    }
  }
}

.load-more {
  font-size: 12px;
  color: #999;
  cursor: pointer;
  text-align: center;
}

::v-deep .el-empty {
  padding: 0;
}

::v-deep .el-button--mini {
  padding: 5px 8px;
}

::-webkit-scrollbar {
  width: 4px;
  height: 4px;
}
::-webkit-scrollbar-track {
  background-color: #eee;
}
::-webkit-scrollbar-thumb {
  background-color: #ccc;
  border-radius: 4px;
}
::-webkit-scrollbar-thumb:hover {
  background-color: #aaa;
}
</style>

