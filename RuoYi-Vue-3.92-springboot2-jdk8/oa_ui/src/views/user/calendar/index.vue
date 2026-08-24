<!-- 办公-我的日程页面 -->
<template>
  <div class="divBox">
    <el-card body-style="padding:0 0 0 0; " class="normal-page">
      <div class="header">
        <span class="title">我的日程</span>
        <el-button type="primary" size="small" icon="el-icon-plus" @click="addSchedule">
          {{ $t('calendar.newschedule') }}
        </el-button>
      </div>
      <el-row :gutter="14">
        <el-col :span="6">
          <calendar-bar ref="calendarBar" @handleDate="handleDate" />
        </el-col>
        <el-col :span="18" class="line" v-loading="loading" element-loading-text="数据正在加载中">
          <div class="plan-tabs-content"></div>
          <!-- 我的日历 -->
          <div class="needToBeDealt">
            <div class="add-btn">
              <div class="content-right"></div>
              <div class="content-button">
                <el-radio-group v-model="time" size="small" @change="selectChange">
                  <el-radio-button v-for="(itemn, indexn) in fromList" :key="indexn" :label="itemn.val">
                    {{ itemn.text }}
                  </el-radio-button>
                </el-radio-group>
              </div>
            </div>
            <full-calendar ref="calendar" id="calendar" style="height: calc(100vh - 150px)" :options="calendarOptions">
              <template v-slot:slotLabelContent="arg">
                <div class="calendar-timeline">
                  <span v-if="$moment(arg.date).format('HH:mm') !== '00:00'">{{
                    $moment(arg.date).format('HH:mm')
                  }}</span>
                </div></template
              >
              <template v-slot:dayHeaderContent="arg">
                <div class="header-box">
                  <div class="day-header" :class="period == 1 ? 'ml30' : ''">
                    <span class="week">{{ getWeek(arg.date) }}</span>
                    <span class="date" v-if="period !== 3">{{ $moment(arg.date).format('D') }}</span>
                  </div>
                </div>
              </template>

              <!-- 自定义内容 -->
              <template v-slot:eventContent="arg">
                <div
                  class="item"
                  :style="{
                    background: period == 3 ? getColorFn(arg.event.textColor, '0.1') : arg.event.color
                  }"
                >
                  <div class="flex over-title" @click="handleEventClick(arg)">
                    <img v-if="period !== 2" :src="arg.event.extendedProps.avatar" class="img" alt="" />
                    <div class="over-title" :style="{ color: period == 3 ? arg.event.textColor : arg.event.textColor }">
                      {{ arg.event.title }}
                    </div>
                  </div>
                  <template v-if="arg.event.extendedProps.show > -1">
                    <span
                      v-if="arg.event.extendedProps.finish == 2"
                      class="el-icon-error"
                      :style="{ color: arg.event.textColor }"
                    ></span>

                    <span
                      v-else-if="arg.event.extendedProps.finish == 3"
                      @click="putStatus('取消完成', 1, arg.event)"
                      class="el-icon-success"
                      :style="{ color: arg.event.textColor }"
                    ></span>
                    <span
                      v-else
                      class="yuan"
                      :style="{ 'border-color': arg.event.textColor }"
                      @click="putStatus('完成', 3, arg.event, arg)"
                    ></span>
                  </template>
                </div>
              </template>
            </full-calendar>
          </div>
        </el-col>
      </el-row>
    </el-card>
    <!-- 通用弹窗表单  -->
    <addTodo ref="addTodo" @getList="getList" :leftTime="leftTime"></addTodo>
    <calendarDetails ref="calendarDetails" @deleteFn="getList" @editFn="editFn" :dateInfo="dateInfo"></calendarDetails>
    <contract-dialog ref="contractDialog" :config="configContract" @isOk="getList"></contract-dialog>
    <!-- 跟进弹窗 -->
    <el-dialog title="添加跟进记录" class="record" :visible.sync="dialogVisible" width="40%">
      <recordUpload :form-info="formInfo" @change="recordChange"></recordUpload>
    </el-dialog>
    <edit-examine
      ref="editExamine"
      :parameterData="parameterData"
      :ids="formInfo.data.id"
      @scheduleRecord="scheduleRecord"
    ></edit-examine>
  </div>
</template>
<script>
// 导入FullCalendar插件
import dayGridPlugin from '@fullcalendar/daygrid';
import listPlugin from '@fullcalendar/list';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';

// 导入API接口
import { clientRemindDetailApi } from '@/api/enterprise';
import { scheduleListApi, scheduleStatusApi } from '@/api/user';
import { configRuleApproveApi } from '@/api/config';

// 导入工具函数
import { toGetWeek, getColor } from '@/utils/format';

export default {
  name: 'WorkDealt',
  components: {
    calendarBar: () => import('./components/calendarBar'),
    dialogForm: () => import('@/views/user/workDealt/components/index'),
    FullCalendar: () => import('@fullcalendar/vue'),
    addTodo: () => import('./components/addTodo'),
    contractDialog: () => import('@/views/customer/contract/components/contractDialog'),
    recordUpload: () => import('@/views/customer/list/components/recordUpload'),
    calendarDetails: () => import('./components/calendarDetails'),
    editExamine: () => import('@/views/user/examine/components/editExamine')
  },
  data() {
    return {
      loading: false,
      calendarApi: null,
      title: '',
      time: '1',
      cid: null,
      dialogVisible: false,
      configContract: {},
      formInfo: {
        avatar: '',
        type: 'add',
        show: 1,
        data: {},
        follow_id: 0
      },
      fromList: [
        { text: this.$t('access.day'), val: '1' },
        { text: this.$t('user.work.week'), val: '2' },
        { text: this.$t('user.work.month'), val: '3' }
      ],

      start_time: '',
      end_time: '',
      dateInfo: {},
      type: [],
      period: 1,
      calendarOptions: {
        height: 'calc(100vh - 232px)',
        eventColor: '',
        initialDate: this.$moment().format('yyyy-MM-DD HH:mm:ss'),
        plugins: [dayGridPlugin, interactionPlugin, timeGridPlugin, listPlugin],
        handleWindowResize: true,
        displayEventTime: false,
        slotDuration: '00:60:00',
        scrollTime: '05:30:00', //自动滚到这个时间
        slotMinTime: '00:00:00', //设置显示的时间从几点开始
        slotMaxTime: '24:00:00', //设置显示的时间从几点结束
        headerToolbar: {
          // 日历头部按钮位置
          left: '',
          center: 'prev title next',
          right: ''
        },

        buttonText: {
          month: '月',
          week: '周',
          day: '天'
        },

        allDaySlot: true,
        dayMaxEventRows: 6,
        allDayText: '',
        moreLinkContent: this.moreLinkContent,
        weekends: true,
        nowIndicator: true,
        weekNumbers: false,
        slotLabelFormat: {
          hour: '2-digit',
          minute: '2-digit',
          meridiem: false,
          hour12: false // 设置时间为24小时
        },
        selectable: true,
        displayEventEnd: false, //所有视图显示结束时间
        initialView: 'timeGridDay', // 设置默认显示月，可选周、日
        dateClick: this.handleDateClick,
        customButtons: {
          next: {
            text: 'PREV',
            click: () => {
              this.next()
            }
          },
          prev: {
            text: 'PREV',
            click: () => {
              this.prev()
            }
          }
        },
        events: [],
        slotEventOverlap: false,
        eventOverlap: false,
        locale: 'zh-cn', // 设置语言
        weekNumberCalculation: 'ISO' // 周数
      },
      detailedData: {},
      parameterData: {
        contract_id: '',
        customer_id: '',
        invoice_id: '',
        bill_id: ''
      },
      leftTime: '',
      buildData: [],
      item: {},
      status: 0
    }
  },

  mounted() {
    // 从本地存储中获取用户信息
    const userInfo = JSON.parse(localStorage.getItem('userInfo'));
    if (userInfo) {
      this.userId = userInfo.userId;
      this.formInfo.avatar = userInfo.avatar;
    }

    // 获取配置审批信息
    this.getConfigApprove();

    // 等待日历组件加载完成后执行操作
    setTimeout(() => {
      if (this.$refs.calendar) {
        // 获取日历API实例
        this.calendarApi = this.$refs.calendar.getApi();
        // 修改当天系统背景颜色
        this.dayRender();
        // 获取当前日期
        this.title = this.$moment(this.calendarApi.view.currentStart).format('YYYY-MM-DD');
        // 设置开始和结束时间
        this.start_time = this.title + ' 00:00:00';
        this.end_time = this.title + ' 23:59:59';
      }
    }, 400);

    // 设置日历滚动时间
    // this.calendarOptions.scrollTime = this.$moment().subtract(1, 'hours').format('HH:mm:ss');

    // 移除标题
    this.removeTitle();
  },

  methods: {
    // 点击有事件的单元格
    handleEventClick(e) {
      this.$refs.calendarDetails.openBox(e.event.extendedProps)
    },
    recordChange() {
      this.dialogVisible = false
      this.getList()
    },
    async getConfigApprove() {
      const result = await configRuleApproveApi(0)
      this.buildData = result.data
    },
    // 修改状态
    putStatus(text, status, item) {
      const { extendedProps } = item;
      this.item = extendedProps;
      const bill_id = extendedProps.bill_id;
      this.status = status;

      switch (extendedProps.cid_value) {
        case 3:
        case 4:
          const id = bill_id.bill_id !== 0 ? bill_id.bill_id : bill_id.remind_id;
          if (status == 3) {
            clientRemindDetailApi(id).then((res) => {
              res.data.id = bill_id.remind_id;
              this.parameterData.customer_id = res.data.eid;
              this.parameterData.contract_id = res.data.cid;

              const switchType = res.data.types === 0 ? 'contract_refund_switch' : 'contract_renew_switch';
              const data = { id: this.buildData[switchType] };
              this.$refs.editExamine.openBox(data, res.data.cid, switchType, item, status);
            });
          }
          break;
        case 2:
          if (extendedProps.finish === 3) {
            return false;
          }
          this.formInfo.follow_id = bill_id ? bill_id.follow_id : 0;
          this.formInfo.data.eid = extendedProps.link_id;
          this.dialogVisible = true;
          break;
        default:
          this.scheduleRecord(extendedProps, status);
      }
    },
    
    // 提醒状态
    async scheduleRecord(data = this.item, status = this.status) {
      const { start_time: start, end_time: end, itemId: id } = data;
      const info = { status, start, end };
      await scheduleStatusApi(id, info);
      await this.getList();
    },
    // 点击空白单元格
    handleDateClick(e) {
      this.detailedData = {
        startDate: this.$moment(e.date).format('YYYY-MM-DD'),
        startTime: this.$moment(e.date).format('HH:mm:ss'),
        endDate: this.$moment(e.date).format('YYYY-MM-DD'),
        time: this.$moment(e.date).format('YYYY-MM-DD HH:mm:ss')
      }
      this.$refs.addTodo.openBox(this.detailedData)
    },

    // 修改当天系统背景颜色
    dayRender() {
      document.documentElement.style.setProperty(`--fc-today-bg-color`, '#fff')
      let dayTime = document.querySelectorAll('.fc-timegrid-slot-label-cushion')
      dayTime[0].classList.add('fcTime')
      document.querySelectorAll('.fc-button-primary').forEach((li) => li.setAttribute('title', ''))
    },

    // 更多日程
    moreLinkContent(e) {
      return '还有' + e.num + '个日程'
    },

    day() {
      this.period = 1;
      this.calendarApi?.changeView('timeGridDay');
      this.getList();
      this.removeTitle();
      this.dayRender();
    },

    month() {
      this.period = 3
      this.calendarApi.changeView('dayGridMonth')
      this.getList()
      this.removeTitle()
    },

    week() {
      this.period = 2
      this.calendarApi.changeView('timeGridWeek')
      this.getList()
      this.removeTitle()
      this.dayRender()
    },

    // 上一天
    prev() {
      // 调用日历API的prev方法切换到上一个时间段
      this.calendarApi.prev();
      // 获取最新的日程列表数据
      this.getList();

      // 根据当前视图模式更新日历栏的值
      if (this.period === 2) {
        // 周视图模式下，将日期减去7天并格式化为YYYY-MM-DD
        const date = this.$refs.calendarBar.value;
        this.$refs.calendarBar.value = this.$moment(date).subtract(7, 'd').format('YYYY-MM-DD');
      } else {
        // 非周视图模式下，将当前视图的开始日期格式化为YYYY-MM-DD
        this.$refs.calendarBar.value = this.$moment(this.calendarApi.view.currentStart).format('YYYY-MM-DD');
      }
    },
    removeTitle() {
      let row = null
      setTimeout(() => {
        row = document.querySelectorAll('.fc-daygrid-day-bottom a')
        for (var i = 0; i < row.length; i++) {
          row[i].title = ''
        }
      }, 300)
    },

    // 下一周
    next() {
      this.calendarApi.next()
      this.getList()
      if (this.period == 2) {
        let date = this.$refs.calendarBar.value
        this.$refs.calendarBar.value = this.$moment(this.$moment(date).add(7, 'd'))
      } else {
        this.$refs.calendarBar.value = this.$moment(this.calendarApi.view.currentStart).format('YYYY-MM-DD')
      }
    },
    // 获取列表数据
    getList() {
      this.loading = true
      let currentStart = this.calendarApi.view.currentStart
      let currentEnd = this.calendarApi.view.currentEnd
   
// 使用对象存储不同周期下的时间计算逻辑，避免重复代码
const timeCalculations = {
  1: () => {
    const date = this.$moment(currentStart).format('YYYY-MM-DD');
    return {
      start: `${date} 00:00:00`,
      end: `${date} 23:59:59`
    };
  },
  2: () => {
    const startDate = this.$moment(currentStart).format('YYYY-MM-DD');
    const endDate = this.$moment(currentEnd).subtract(1, 'days').format('YYYY-MM-DD');
    return {
      start: `${startDate} 00:00:00`,
      end: `${endDate} 23:59:59`
    };
  },
  default: () => {
    const startDate = this.$moment(this.calendarApi.view.activeStart).format('YYYY-MM-DD');
    const endDate = this.$moment(currentEnd).subtract(1, 'days').format('YYYY-MM-DD');
    return {
      start: `${startDate} 00:00:00`,
      end: `${endDate} 23:59:59`
    };
  }
};

// 根据当前周期选择对应的时间计算逻辑
const { start, end } = timeCalculations[this.period] || timeCalculations.default();
this.start_time = start;
this.end_time = end;
      let data = {
        start_time: this.start_time,
        end_time: this.end_time,
        period: this.period,
        cid: this.cid
      }

      scheduleListApi(data).then((res) => {
        this.loading = false
        let newArr = []
        res.data.map((item) => {
          let start_time = item.start_time
          let end_time = item.end_time
          let itemStartTime = this.$moment(item.start_time).format('YYYY-MM-DD')
          let itemEndTime = this.$moment(item.end_time).format('YYYY-MM-DD')

          if (itemEndTime != itemStartTime) {
            item.all_day = true
            item.end_time = itemEndTime + ' 24:00:00'
          } else if (itemEndTime == itemStartTime && item.all_day == 1) {
            //全天
            item.all_day = true
            item.end_time = itemEndTime + ' 24:00:00'
          } else if (item.all_day == 0) {
            item.all_day = false
          }
          if (item.user.length == 0) {
            item.user.push(item.master)
          }

          let avatar =  this.formInfo.avatar
          newArr.push({
            start: item.start_time,
            end: item.end_time,
            allDay: item.all_day,
            title: item.title,
            avatar: item.master.avatar || avatar,
            content: item.content,
            start_time,
            end_time,
            color: getColor(item.color, '0.1'),
            textColor: item.color,
            show: this.findItem(item.user, 'id', item.master.id),
            finish: item.finish,
            itemId: item.id,
            cid_value: item.cid,
            overlap: false,
            bill_id: item.relation || '',
            link_id: item.link_id
          })
        })
  
        this.calendarOptions.events = newArr
        this.$refs.calendarBar?.getList()
      })
    },

    // 方法
    findItem(arr, key, val) {
      for (var i = 0; i < arr.length; i++) {
        if (arr[i].id === val || arr[i].id == this.userId) {
          return 2
        }
      }
      return -1
    },
    getWeek(date) {
      // 参数时间戳
      return toGetWeek(date)
    },

    editFn(id, type, date) {
      let data = {
        id,
        type,
        edit: true,
        date
      }
      this.$refs.addTodo.openBox(data)
    },
    // 点击日历事件
    handleDate(data, val) {
      this.cid = data.type
      this.leftTime = data.time
      if (val) {
        this.calendarApi.gotoDate(data.time)
      }
      setTimeout(() => {
        // 获取日历实例对象
        this.calendarApi = this.$refs.calendar.getApi()
        // 切换到日视图
        this.time = 1
        this.day()
        // 获取日历列表
        this.getList()
      }, 300)
    },
    // 切换视图 1 日视图 2 周视图 3 月视图
    selectChange(e) {
      this.time = e
      if (this.time == 1) {
        this.day()
      } else if (this.time == 2) {
        this.week()
      } else {
        this.month()
      }
    },

    addSchedule() {
      this.$refs.addTodo.openBox()
    },

    // 颜色转换函数
    getColorFn(thisColor, thisOpacity) {
      getColor(thisColor, thisOpacity)
    }
  }
}
</script>

<style lang="scss" scoped>
::v-deep .fc-header-toolbar {
  position: absolute;
  top: 0;
  left: 0;
}

.header {
  padding: 16px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #dcdfe6;
  .title {
    font-weight: 500;
    font-size: 18px;
    color: #303133;
  }
}
.day-header {
  display: flex;
  flex-direction: column;
  justify-content: center;
  height: 52px;
  font-family: PingFang SC-Regular, PingFang SC;
  font-weight: 400;

  line-height: 20px;

  .week {
    font-size: 13px;
    color: #909399;
  }
  .date {
    font-size: 18px;
    color: #606266;
    font-weight: 800;
  }
}

::v-deep .fc .fc-button .fc-icon {
  color: #c0c4cc;
}
::v-deep .fc .fc-timegrid-axis-frame-liquid {
  border-bottom: none !important;
}
::v-deep .fc .fc-popover {
  z-index: 22;
}
.ml30 {
  margin-left: -30px;
}

::v-deep .fc .fc-timegrid-slot {
  height: 3.5em !important;
}
::v-deep .fc .fc-timegrid-divider {
  border: none;
  padding: 0;
}
// 日历边框线删除

::v-deep .fc .fc-cell-shaded {
  background: #fff;
}
::v-deep .fc-timegrid-slot-label-cushion {
  color: #909399;
  font-size: 12px;
}

::v-deep .fc .fc-daygrid-day-top {
  padding-top: 6px;
  display: block;
}

::v-deep .fc .fc-timegrid-slot-label {
  border-bottom: 1px solid #fff;
  vertical-align: top;
  position: relative;
}
::v-deep .fc .fc-scrollgrid-shrink {
  width: 60px !important;
}
::v-deep .fc-timegrid-slot-label-cushion {
  position: absolute;
  left: 0;
  top: -8px;
}

::v-deep .fcTime {
  position: absolute;
  left: 0;
  top: -16px !important;
}

.line {
  display: inline-block;
  border-left: 1px solid #f0f2f5;
}
.item {
  width: 100%;
  display: flex;
  font-size: 14px;
  font-family: PingFang SC-Regular, PingFang SC;
  font-weight: 400;
  justify-content: space-between;
  align-items: center;
  padding-right: 4px;
  border-radius: 13px 13px 13px 13px;

  .img {
    flex-shrink: 0; // flex布局下图片挤压变形
    display: block;
    width: 18px;
    height: 18px;
    border-radius: 50%;
    margin-right: 5px;
    object-fit: cover;
  }
  .yuan {
    width: 12px;
    height: 12px;
    border-radius: 50px;
    border: 1px solid #fff;
  }
}
.over-title {
  width: 98% !important;
  overflow: hidden !important;
  white-space: nowrap;
  text-overflow: ellipsis !important;
}

::v-deep .fc-direction-ltr .fc-daygrid-event.fc-event-start {
  border-radius: 13px !important;
}

::v-deep .el-tabs__nav-wrap::after {
  display: none;
}
.plan-tabs-content {
  margin-top: 14px;
  // margin: 14px -20px 20px -6px;
  ::v-deep .el-tabs {
    padding: 0 20px;
  }
  ::v-deep .el-tabs__item {
    line-height: 26px;
  }
}

.add-btn {
  display: flex;
  justify-content: space-between;
  margin-bottom: 15px;
}
.needToBeDealt {
  padding: 0 20px;
  position: relative;
}

::v-deep .fc .fc-button-primary {
  background-color: #fff;
  color: #606266;
  border: none;
}

::v-deep .fc .fc-toolbar-chunk {
  display: flex;
  align-items: center;
}
::v-deep .fc .fc-button-primary:hover {
  background-color: #fff;
  color: #606266;
  border: none;
}
::v-deep.fc .fc-button:focus {
  box-shadow: none !important;
}
::v-deep .fc-h-event {
  margin: 2px 0 !important;
}

::v-deep .fc .fc-toolbar-title {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
}
::v-deep .fc-scrollgrid-sync-inner {
  a {
    color: #606266 !important;
  }
}

::v-deep a {
  text-decoration: none;
  outline: none;
}

::v-deep .fc-timeGridWeek-view {
  th {
    text-align: left;
  }

  .fc-col-header-cell-cushion {
    padding: 2px 8px;
  }
}

::v-deep .fc-dayGridMonth-view {
  th {
    text-align: left;
  }
  .day-header {
    height: 40px;

    .week {
      font-size: 14px;
    }
  }
  td {
    height: 95px;
  }
  .fc-col-header-cell-cushion {
    padding: 2px 8px;
  }
}

::v-deep .fc-timegrid-axis {
  width: 100px !important;

  z-index: 99;
}
::v-deep .fc-widget-content {
  width: 150px !important;
  background-color: pink;
  z-index: 99;
}
.calendar-timeline {
  width: 50px;
  display: flex;
  justify-content: center;
}
::v-deep.fc .fc-more-popover .fc-popover-body {
  max-height: 300px;
  overflow-y: scroll;
}
.normal-page {
  height: calc(100vh - 80px);
  overflow-x: hidden;
}
</style>
