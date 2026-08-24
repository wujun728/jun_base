<template>
  <!-- 日历配置页面 -->
  <div class="divBox">
    <el-card class="normal-page">
      <div class="flex">
        <div>
          <span class="el-icon-arrow-left" @click="yearFn(1)" /><span class="year">{{ year }}年</span
          ><span class="el-icon-arrow-right" @click="yearFn(2)" />
        </div>
        <div class="right">日历配置中黑色为上班日，红色为休息日</div>
      </div>

      <div :class="screenWidth < 1871 ? 'col-3' : 'col-4'" v-for="m in 12" :key="m">
        <ve-calendar
          :ref="`vc${m}`"
          mode="mini"
          :value="selected"
          :right-menu="false"
          :activate-date="{ year: year, month: m }"
          :offDays="offDays"
          :enabledList="enabledList"
          :disabledList="disabledList"
          :selectMode="selectMode"
          :cancelClick="cancelClick"
          :mostChoice="mostChoice"
          :over-hide="true"
          :lunar="lunar"
          :min="min"
        >
          <div
            slot="header"
            slot-scope="{ year, month }"
            class="header"
            :class="year > todayDate || m >= todayTime ? 'activeHeader' : ''"
          >
            <span> {{ month }}月</span>

            <img
              v-if="year > todayDate || m >= todayTime"
              @click="openBox(year, month)"
              src="../../../../assets/images/day.png"
              alt=""
              class="img"
            />
          </div>
          <div slot="day-number" slot-scope="{ day }">{{ day.sDay }}</div>
        </ve-calendar>
      </div>
    </el-card>
    <!-- 日历弹窗 -->
    <calendar ref="calendar" @getList="getList" :selectList="selected"></calendar>
  </div>
</template>

<script>
import calendar from './components/calendar'
import veCalendar from 've-calendar'
import { calendarYearApi } from '@/api/config'
export default {
  name: 'CrmebOaEntCalendarsetUp',
  components: {
    veCalendar,
    calendar
  },
  data() {
    return {
      year: 2022,
      selectDateList: [],
      mode: 'mini', // 显示模型
      offDays: [], // 工作休息日
      enabledList: [], // 只准选择名单中的日期
      disabledList: [], // 禁止选中的日期
      selected: [''], // 选中日期列表

      selectMode: 'list', // range 只需要点击2次，会选中区间全部值，首尾模式下，输出默认变为2个[start,end]
      // list 用鼠标控制，点击选中的或者拖动选中的生效
      cancelClick: true, // 是否允许点击取消，拖动依然可以取消
      mostChoice: 0, // 最多选择日期数量,0无限
      lunar: true,
      min: '2100-07-08',
      todayTime: '',
      screenWidth: 0,
      todayDate: null,
      timer: null
    }
  },
  created() {
    this.windowWidth(document.documentElement.clientWidth)
  },

  mounted() {
    var myDate = new Date()
    this.year = myDate.getFullYear()
    this.todayTime = this.$moment().subtract('day').format('M') // 当天日期
    this.todayDate = this.$moment().format('YYYY') // 当天日期
    this.getList()
    window.onresize = () => {
      return (() => {
        this.screenWidth = `${document.documentElement.clientWidth}`
      })()
    }
  },
  watch: {
    screenWidth(val) {
      if (!this.timer) {
        this.screenWidth = val
        this.timer = true
        let _this = this
        setTimeout(function () {
          _this.timer = false
        }, 500)
      }
      // 这里可以添加修改时的方法
      this.windowWidth(val)
    }
  },

  methods: {
    async getList() {
      const result = await calendarYearApi(this.year)
      this.selected = JSON.parse(JSON.stringify(result.data))
    },
    windowWidth(value) {
      this.screenWidth = value
    },

    openBox(year, month) {
      this.$refs.calendar.openBox(year, month, this.selected)
    },
    yearFn(data) {
      if (data == 1) {
        this.year = this.year - 1
      } else {
        this.year = this.year + 1
      }
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
.header {
  width: 100%;
  height: 30px;
  background: #f5f5f5;
  font-size: 14px;
  font-family: PingFang SC-Medium, PingFang SC;
  font-weight: 500;
  color: #909399;
  text-align: left;
  border-radius: 4px 4px 4px 4px;
}
.img {
  display: block;
  width: 16px;
  height: 16px;
  cursor: pointer;
}
.activeHeader {
  background: #e7f3ff;
  color: #1890ff;
}
.flex {
  display: flex;
  justify-content: space-between;
  .right {
    font-size: 14px;
    font-family: PingFang SC-Regular, PingFang SC;
    font-weight: 400;
    color: #606060;
  }
  margin-bottom: 36px;
}

.year {
  margin: 0 48px;
  font-size: 16px;
  font-family: PingFang SC-Medium, PingFang SC;
  font-weight: 500;
  color: #303133;
}
.el-icon-arrow-left {
  color: #c0c4cc;
  cursor: pointer;
}
.el-icon-arrow-right {
  color: #c0c4cc;
  cursor: pointer;
}

.col-3 {
  width: calc(100% / 3);
  display: inline-block;
}
.col-4 {
  width: calc(100% / 4);
  display: inline-block;
}

::v-deep.ve-calendar {
  border: none;
  box-shadow: none;
  border-right: 43px;
  .header {
    padding: 0 10px;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}
::v-deep .ve-calendar.mini {
  padding: 0;
  margin-right: 20px;
  margin-bottom: 20px;
  .week-title {
    font-size: 14px;
    font-family: PingFang SC-Medium, PingFang SC;
    font-weight: 500;
    color: #909399;
  }
  .day-number {
    line-height: 29px;
    font-size: 14px;
    font-family: PingFang SC-Medium, PingFang SC;
    font-weight: 600;
    div {
      color: #303133;
    }
  }
  .day-content {
    font-size: 12px;
    font-family: PingFang SC-Regular, PingFang SC;
    font-weight: 400;
    color: #606266;
    margin-top: 4px;
    margin-bottom: 10px;
  }
}
::v-deep.day-grid.mini.today {
  border: none;

  .day-number {
    color: #fff;
    font-size: 14px;
    background-color: #1890ff;
    border-radius: 50%;
    font-family: PingFang SC-Medium, PingFang SC;
    font-weight: 500;
  }
}

::v-deep .day-grid.selected {
  background: #fff;

  .day-number div {
    color: #ed4014;
  }

  .day-content {
    color: #ed4014;
  }
}
::v-deep.day-grid.mini.today.selected {
  border: none;

  .day-number {
    color: #fff;
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
</style>
