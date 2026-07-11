<template>
  <div class="full-calendar-body">
    <div class="weeks">
      <strong class="week" v-for="(week, index) in weekNames" :key="index">{{ week }}</strong>
    </div>
    <div class="dates" ref="dates">
      <div class="dates-events">
        <div class="events-week" v-for="(week, index) in currentDates" :key="index">
          <div
            class="events-day"
            v-for="(day, index) in week"
            :key="index"
            track-by="$index"
            :class="{
              today: day.isToday,
              'not-cur-month': !day.isCurMonth,
            }"
          >
            <div class="day-info">
              <p class="holiday-item" v-for="(event, index) in day.eventList" :key="index">
                <span
                  class="holiday-tag"
                  :class="[
                  {
                    'is-start': isStart(event.start, day.date),
                    'is-end': isEnd(event.end, day.date),
                    'is-opacity': !event.isShow,
                    'is-work':event.type == 2
                  },
                ]"
                >
                  {{ isBegin(event, day.date, day.weekDay) }}
                  <span
                    v-if="event.holidayName"
                    class="holiday-name"
                  >{{event.holidayName?event.holidayName:''}}</span>
                </span>
              </p>
              <p class="day-number" :class="{today: day.isToday}">{{ day.monthDay }}</p>
            </div>

            <div v-if="hasSchedule(day.dateStr)" class="day-task">
              <div
                class="day-task-item"
                v-for="(item, index) in scheduleData[day.dateStr].slice(0, 3)"
                :key="item.id"
                @click.stop="goDetail(day, item)"
              >
                <span v-if="item.partType == '1'">
                  <el-tag size="mini" effect="dark">参与</el-tag>
                </span>
                <span v-else>
                  <el-tag
                    v-if="item.scheduleType && item.scheduleType.tagName"
                    size="mini"
                    :effect="item.scheduleType.tagEffect || 'plain'"
                    :type="item.scheduleType.tagType || ''"
                  >{{ item.scheduleType.tagName }}</el-tag>
                </span>
                <span class="ml5" :style="scheduleStyle(item.scheduleType)">{{ parseTime(item.startTime, '{h}:{i}') }}</span>
                <span class="ml5" :style="scheduleStyle(item.scheduleType)">{{ item.title }}</span>
              </div>
              <div v-if="scheduleData[day.dateStr].length > 3" class="more-tip" @click.stop="clickMore(day, scheduleData[day.dateStr], $event)">
                还有 {{ scheduleData[day.dateStr].length - 3 }} 条日程
                <svg-icon icon-class="down-more" />
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="more-events" v-show="showMore" :style="{ left: morePos.left + 'px', top: morePos.top + 'px' }">
        <div class="more-header">
          <span class="title">{{ moreTitle(selectDay.date) }}</span>
          <span class="close" @click.stop="showMore = false">关闭</span>
        </div>
        <div class="more-body">
          <ul class="body-list">
            <li v-for="(item, index) in moreSchedulList" :key="index" :id="index" class="body-item" @click="goDetail(selectDay, item)">
              <span :style="scheduleStyle(item.scheduleType)">{{ parseTime(item.startTime, '{h}:{i}') }}</span>
              <span class="ml5" :style="scheduleStyle(item.scheduleType)">{{ item.title}}</span>
            </li>
          </ul>
        </div>
      </div>
      <slot name="body-card"></slot>
    </div>

    <DialogDetail :open="open" :id="scheduleId" :dateDesc="dateDesc" @close="close" />
  </div>
</template>

<script type="text/babel">
import dateFunc from "./date-func";
import { parseTime } from "@/utils/ruoyi";
import DialogDetail from "../schedule/detail";

export default {
  components: {
    DialogDetail,
  },
  data() {
    return {
      moreSchedulList: [],
      weekMask: [1, 2, 3, 4, 5, 6, 7],
      showMore: false,
      morePos: {
        top: 0,
        left: 0,
      },
      selectDay: {},
      selectedDay: "",
      open: false,
      scheduleId: null,
      dateDesc: null,
    };
  },
  props: {
    currentDate: {},
    eventList: {
      type: Array,
    },
    weekNames: {
      type: Array,
      default: [],
    },
    monthNames: {},
    firstDay: {},
    scheduleData: {},
  },
  created() {},
  watch: {
    currentDate: function (newVal, oldVal) {
      if (newVal != oldVal) {
        this.getCalendar(newVal);
      }
    },
  },
  computed: {
    currentDates() {
      return this.getCalendar(this.currentDate);
    },
  },
  methods: {
    scheduleStyle(scheduleType) {
      if (!scheduleType) return;
      let color = scheduleType.color == "#303133" ? "#909399" : scheduleType.color;
      let style = { color: color };
      return style;
    },
    hasSchedule(day) {
      return this.scheduleData.hasOwnProperty(day);
    },
    isBegin(event, date, index) {
      let start = new Date(event.start);
      let end = new Date(event.end);
      if (date >= start && date <= end) {
        if (event.type == 1) {
          return "休";
        } else if (event.type == 2) {
          return "班";
        }
      }
      return "　";
    },
    /** 获取当前日历 */
    getCalendar(currentDate) {
      let now = new Date();
      let current = new Date(currentDate);
      let startDate = dateFunc.getStartDate(current);
      let curWeekDay = startDate.getDay();
      let diff = parseInt(this.firstDay) - curWeekDay;
      diff = diff > 0 ? diff - 7 : diff;
      startDate.setDate(startDate.getDate() + diff);
      let calendar = [];
      for (let perWeek = 0; perWeek < 6; perWeek++) {
        let week = [];
        for (let perDay = 0; perDay < 7; perDay++) {
          week.push({
            monthDay: startDate.getDate(),
            isToday: now.toDateString() == startDate.toDateString(),
            isCurMonth: startDate.getMonth() == current.getMonth(),
            weekDay: perDay,
            date: new Date(startDate),
            eventList: this.slotEvents(startDate),
            dateStr: parseTime(startDate, "{y}-{m}-{d}"),
          });
          startDate.setDate(startDate.getDate() + 1);
        }
        calendar.push(week);
      }
      return calendar;
    },
    slotEvents(date) {
      let thisDayEvents = this.eventList.filter((day) => {
        let dt = new Date(day.start);
        let st = new Date(dt.getFullYear(), dt.getMonth(), dt.getDate());
        let ed = day.end ? new Date(day.end) : st;
        return date >= st && date <= ed;
      });
      thisDayEvents.sort((a, b) => {
        if (!a.cellIndex) return 1;
        if (!b.cellIndex) return -1;
        return a.cellIndex - b.cellIndex;
      });
      for (let i = 0; i < thisDayEvents.length; i++) {
        thisDayEvents[i].cellIndex = thisDayEvents[i].cellIndex || i + 1;
        thisDayEvents[i].isShow = true;
        if (thisDayEvents[i].cellIndex == i + 1 || i > 2) continue;
        thisDayEvents.splice(i, 0, {
          title: "holder",
          cellIndex: i + 1,
          start: dateFunc.format(date, "yyyy-MM-dd"),
          end: dateFunc.format(date, "yyyy-MM-dd"),
          isShow: false,
        });
      }
      return thisDayEvents;
    },
    isStart(eventDate, date) {
      let st = new Date(eventDate);
      return st.toDateString() == date.toDateString();
    },
    isEnd(eventDate, date) {
      let ed = new Date(eventDate);
      return ed.toDateString() == date.toDateString();
    },
    /** 查看更多 */
    clickMore(day, data, jsEvent) {
      this.selectDay = day;
      this.showMore = true;
      this.morePos = this.computePos(jsEvent.target);
      let dayIndex = new Date(day.date).getDay();
      this.morePos.top += 10;
      this.morePos.left -= dayIndex == 1 ? 0 : 60;
      this.moreSchedulList = data;
    },
    /** 标题 */
    moreTitle(date) {
      let dt = new Date(date);
      return this.monthNames[dt.getMonth()] + dt.getDate() + "日，" + this.weekNames[dt.getDay() - 1 < 0 ? 6 : dt.getDay() - 1];
    },
    /** 计算位置 */
    computePos(target) {
      let eventRect = target.getBoundingClientRect();
      let pageRect = this.$refs.dates.getBoundingClientRect();
      return {
        left: eventRect.left - pageRect.left,
        top: eventRect.top + eventRect.height - pageRect.top,
      };
    },
    /** 查看详情 */
    goDetail(day, val) {
      this.open = true;
      this.scheduleId = val.id;
      let dt = new Date(day.date);
      let m = this.monthNames[dt.getMonth()] + dt.getDate() + "日";
      let w = "（" + (day.isToday ? "今天" : this.weekNames[dt.getDay() - 1 < 0 ? 6 : dt.getDay() - 1]) + "）";
      this.dateDesc = m + w;
    },
    close() {
      this.scheduleId = null;
      this.open = false;
    },
  },
};
</script>
<style lang="scss">
.full-calendar-body {
  .weeks {
    height: 44px;
    line-height: 44px;
    display: flex;
    border-top: 1px solid #dfe6ec;
    border-bottom: 1px solid #dfe6ec;
    border-left: 1px solid #dfe6ec;
    background-color: #edf6ff;
    position: relative;

    .week {
      flex: 1;
      text-align: center;
      border-right: 1px solid #dfe6ec;
      font-size: 14px;
    }
  }

  .dates {
    position: relative;
    font-size: 14px;

    .dates-events {
      position: relative;
      width: 100%;
      border-left: 1px solid #dfe6ec;
      z-index: 1;

      .events-week {
        display: flex;

        .events-day {
          flex: 1;
          min-height: 140px;
          overflow: hidden;
          position: relative;
          border-right: 1px solid #dfe6ec;
          border-bottom: 1px solid #dfe6ec;
          background-color: #fff;

          .day-info {
            position: relative;
            display: flex;
            align-items: center;
            justify-content: flex-start;
            // border-bottom: 1px solid #dfe6ec;
            box-sizing: border-box;
            box-shadow: 0 1px 5px 0 rgb(0 0 0 / 10%);
            padding: 5px;
            height: 30px;

            .holiday-item {
              font-size: 12px;
              color: white;
              line-height: 20px;
            }

            .holiday-name {
              font-weight: normal;
              font-size: 12px;
            }

            .holiday-tag {
              background-color: #e02d2d;
              padding: 2px;
              font-weight: bold;
            }

            .is-work {
              background-color: #ff8400;
              color: #fff;
            }

            .day-number {
              // position: absolute;
              // left: 50%;
              // transform: translateX(-50%);
              padding-left: 5px;
              font-size: 14px;
              font-weight: bold;
              color: #303133;

              &.today {
                color: #409eff;
              }
            }

            .more-link {
              position: absolute;
              right: 0;
              cursor: pointer;
              color: #909399;
              font-size: 12px;
            }
          }

          &.today {
            background-color: #edf6ff;
          }

          &.not-cur-month {
            .day-number {
              color: #c0c4cc;
            }
          }

          .day-task {
            font-size: 12px;
            max-height: 106px;
            overflow: hidden;
            // padding-top: 5px;

            .day-task-item {
              padding: 5px 0 5px 2px;
              line-height: 18px;
              overflow: hidden;
              white-space: nowrap;
              text-overflow: ellipsis;
              // border-bottom: 1px solid #eeeeee;
              cursor: pointer;
              &:hover {
                background-color: #eaf7ff;
              }
            }

            .more-tip {
              font-size: 12px;
              color: #999;
              padding-left: 8px;
              cursor: pointer;
              text-align: center;
              display: block;
              transition: color 0.3s ease;
            }
            .more-tip:hover {
              color: #409eff;
            }
          }
        }
      }
    }

    .more-events {
      position: absolute;
      width: 200px;
      z-index: 1000;
      border: 1px solid #f1f1f1;
      box-shadow: 5px 5px 12px 5px rgb(0 0 0 / 10%);
      border-radius: 2px;

      .more-header {
        background-color: #353636;
        padding: 5px;
        display: flex;
        align-items: center;
        font-size: 14px;
        color: #fff;
        border: 1px solid #f1f1f1;
        box-shadow: 0px 2px 5px rgb(0 0 0 / 10%);
        margin-bottom: 5px;

        .title {
          flex: 1;
        }

        .close {
          margin-right: 5px;
          cursor: pointer;
          font-size: 12px;
        }
      }

      .more-body {
        max-height: 150px;
        min-height: 60px;
        overflow-y: auto;
        margin-bottom: 5px;
        background-color: #f5fbff;

        .body-list {
          background-color: #fff;

          .body-item {
            cursor: pointer;
            font-size: 12px;
            margin-bottom: 5px;
            padding: 5px;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
            border-bottom: 1px solid #ebebeb;
            box-sizing: border-box;
            box-shadow: 0 1px 5px 0 rgb(0 0 0 / 5%);

            &:hover {
              background-color: #eaf7ff;
            }
          }
        }
      }
    }
  }
}
</style>

