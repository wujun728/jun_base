<template>
  <div class="dealt-bar v-height-flag">
    <div class="dealt-date">
      <el-calendar v-model="value">
        <template slot="dateCell" slot-scope="{ date, data, index }">
          <div class="dealt-date-item" @click="dateBnt(data.day)">
            <input :value="data.day" type="hidden" />
            <div class="calendar_area">
              {{ data.day.split('-').slice(2).join('-') }}
              <span v-if="getCalendarDay(data.day)" class="iconfont iconjindu-qita"></span>
            </div>
          </div>
        </template>
      </el-calendar>
      <div v-if="activeVal !== '4'" class="tips">
        <span class="iconfont icontishi2" /> 日历中的红点表示您当日未提交日报
      </div>
      <div v-else class="tips">
        <span class="iconfont icontishi2" /> 日历中的红点表示有下属未提交{{ typeText[type] }}
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: 'calendar',
  components: {},
  props: ['calendar', 'type', 'activeVal', 'tabType'],
  data() {
    return {
      value: new Date(),
      dailyDay: {
        startTime: '',
        endTime: ''
      },
      clickDate: '',
      typeText: {
        0: '日报',
        1: '周报',
        3: '汇报'
      },

      data: {
        time: this.$moment(new Date()).format('YYYY-MM-DD'),
        type: []
      }
    }
  },
  computed: {},
  watch: {
    activeVal: {
      handler(nVal) {
        if (nVal == '4') {
          this.value = new Date(new Date().getTime() - 24 * 60 * 60 * 1000)
        } else {
          this.value = new Date()
        }
      },
      immediate: true,
      deep: true
    },
    tabType: {
      handler(nVal) {
        if (nVal == 0) {
          setTimeout(() => {
            var tdHover = document.querySelectorAll('.calendar_area')
            for (var i = 0; i < tdHover.length; i++) {
              tdHover[i].style.cursor = 'default'
            }
          }, 300)
        }
      },
      immediate: true,
      deep: true
    },

    clickDate: {
      handler(nVal, oVal) {
        if (this.activeVal == '4' && this.type == 1) {
          let row = document.querySelectorAll('.el-calendar-table__row')
          this.$nextTick(() => {
            let row1 = document.querySelector('.el-calendar-table td.is-selected')
            row1.style.background = ''
          })
          if (nVal == oVal) {
            row[nVal].style.backgroundColor = '#F2F8FE'
          } else {
            this.$nextTick(() => {
              for (var i = 0; i < row.length; i++) {
                row[nVal].style.backgroundColor = '#F2F8FE'
                if (oVal) {
                  row[oVal].style.backgroundColor = 'transparent'
                }
              }
            })
          }
        }
      },
      immediate: true,
      deep: true
    }
  },
  mounted() {
    this.setCalendarButton()
    this.nextTPrevClick()
  },
  methods: {
    removeStyle() {
      let row = document.querySelectorAll('.el-calendar-table__row')
      for (var i = 0; i < row.length; i++) {
        row[i].onclick = null
        row[i].style.backgroundColor = 'transparent'
      }
    },
    // 选择周后，日历样式改变
    addIndex() {
      let list = document.querySelectorAll('.el-calendar-table__row')
      var row1 = document.querySelector('.el-calendar-table td.is-selected')
      row1.style.backgroundColor = ''
      for (var i = 0; i < list.length; i++) {
        list[i].index = i
        list[i].onclick = function () {
          this.clickDate = this.index
          for (var i = 0; i < list.length; i++) {
            list[i].style.backgroundColor = 'transparent'
          }
          list[this.index].style.backgroundColor = '#F2F8FE'
        }
      }
    },
    dateBnt(data) {
      if (this.tabType == '0') {
        var row2 = document.querySelectorAll('.el-calendar-table td')
        for (var i = 0; i < row2.length; i++) {
          row2[i].style.backgroundColor = 'transparent'
        }
      }

      this.data.time = data
      this.$emit('currentTime', data)
    },
    // 根据某天获取这一周
    getMonthWeek(theDate) {
      let currentDay = new Date(theDate)
      let theSaturday = currentDay.getDate() + (6 - currentDay.getDay())
      return Math.ceil(theSaturday / 7)
    },

    // 选择周后样式修改
    setWeek() {
      let numWeek = this.getMonthWeek(this.$moment()) - 1
      // this.value = new Date(new Date().getTime() - 24 * 60 * 60 * 1000);
      this.value = new Date()
      this.clickDate = numWeek
    },

    setCalendarButton() {
      this.$nextTick(() => {
        var prevBtn = document.querySelector('.el-button-group button:nth-of-type(1)')
        var nextBtn = document.querySelector('.el-button-group button:nth-of-type(3)')
        this.dailyDay.startTime = document.querySelector(
          '.dealt-date .el-calendar-table__row:first-child td:nth-child(1) input'
        ).value
        this.dailyDay.endTime = document.querySelector(
          '.dealt-date .el-calendar-table__row:last-child td:last-child input'
        ).value
        prevBtn.className = 'el-button--text'
        prevBtn.lastChild.setAttribute('class', 'el-icon-arrow-left')
        prevBtn.lastChild.innerHTML = ''
        nextBtn.setAttribute('class', 'el-button--text')
        nextBtn.lastChild.setAttribute('class', 'el-icon-arrow-right')
        nextBtn.lastChild.innerHTML = ''
        this.$emit('switchTime', this.dailyDay)
      })
    },

    nextTPrevClick() {
      this.$nextTick(() => {
        let prevBtn = document.querySelector(
          '.dealt-date .el-calendar__button-group .el-button-group>button:nth-child(1)'
        )
        let nextBtn = document.querySelector(
          '.dealt-date .el-calendar__button-group .el-button-group>button:nth-child(3)'
        )
        let prevFirst = document.querySelector('.dealt-date .el-calendar-table__row:first-child')
        let nextLast = document.querySelector('.dealt-date .el-calendar-table__row:last-child')
        let prevArr = prevFirst.getElementsByClassName('prev')
        let nextArr = nextLast.getElementsByClassName('next')
        for (let i = 0; i < prevArr.length; i++) {
          this.tabClick(prevArr[i])
        }
        for (let i = 0; i < nextArr.length; i++) {
          this.tabClick(nextArr[i])
        }
        this.tabClick(prevBtn)
        this.tabClick(nextBtn)
      }, 500)
    },
    getCalendarDay(day) {
      let len = this.calendar.length

      let str = false
      if (len > 0) {
        for (let i = 0; i < len; i++) {
          if (this.calendar[i].time === day) {
            if (this.calendar[i].no_submit == 1) {
              str = true
            } else {
              str = false
            }
            break
          }
        }
      }
      return str
    },
    tabClick(obj) {
      obj.addEventListener('click', (e) => {
        this.dailyDay.startTime = document.querySelector(
          '.dealt-date .el-calendar-table__row:first-child td:nth-child(1) input'
        ).value
        this.dailyDay.endTime = document.querySelector(
          '.dealt-date .el-calendar-table__row:last-child td:last-child input'
        ).value

        this.$emit('switchTime', this.dailyDay)
      })
    }
  }
}
</script>
<style lang="scss" scoped>
.boder1 {
  border-radius: 60% !important;
}
.active {
  background-color: pink !important;
}

// 修改选中日期样式
// ::v-deep .el-calendar-table td.is-selected {

// }

.dealt-date {
  ::v-deep .el-calendar__header {
    padding: 20px;
  }
  .calendar_area {
    width: 100%;
    text-align: center;
    display: flex;
    justify-content: center;
    line-height: 40px;
    align-items: center;
    position: relative;
    .iconjindu-qita {
      position: absolute;
      top: -7px;
      right: 4px;

      -webkit-transform: scale(0.3);
      border-radius: 100%;
      color: #ed4014;
    }
  }

  ::v-deep .el-calendar-table td {
    border: none;
  }

  ::v-deep .el-calendar-table td :hover {
    background: transparent;
  }

  ::v-deep .el-calendar-table thead th {
    text-align: center;
  }

  ::v-deep.el-calendar-table td .dealt-content {
    i {
      position: absolute;
      right: 0;
      top: 0;
      font-size: 14px;
      font-weight: bold;
    }
  }
  .dealt-date-item {
    width: 100%;
    height: 100%;
  }

  position: relative;
  ::v-deep .el-calendar-table .el-calendar-day {
    height: 40px;
    padding: 0;
  }
  ::v-deep .el-calendar__body {
    // padding-bottom: 20px;
    padding: 0px;
  }
  ::v-deep .el-calendar-table__row td {
    text-align: center;
  }
  ::v-deep .el-calendar__header {
    justify-content: center;
    border: none;
    padding-bottom: 0px;
  }
  ::v-deep .el-calendar__button-group {
    .el-button-group {
      button {
        color: #000000;
      }
      button:nth-of-type(1) {
        position: absolute;
        left: 14px;
        top: 18px;
      }
      button:nth-of-type(2) {
        display: none;
      }
      button:nth-of-type(3) {
        position: absolute;
        right: 14px;
        top: 18px;
      }
    }
  }
}

.tips {
  display: flex;
  align-items: center;
  font-size: 13px;
  margin: 15px 0 20px 15px;
  font-family: PingFang SC-常规体, PingFang SC;
  font-weight: normal;
  color: #606266;
  .icontishi2 {
    color: #1890ff;
    margin-right: 4px;
  }
}
</style>
