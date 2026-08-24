<template>
  <div>
    <el-card class="station-header" :body-style="{ padding: '14px' }">
      <el-row>
        <el-col :span="24">
          <el-page-header content="排班页面">
            <div slot="title" @click="backFn">
              <i class="el-icon-arrow-left"></i>
              返回
            </div>
          </el-page-header>
        </el-col>
      </el-row>
    </el-card>
    <el-card class="card-box mt14">
        <!-- 搜索条件 -->
        <div class="head">
          <!-- <div>
            <el-button size="small" type="primary">下载排班模板</el-button>
            <span class="el-icon-d-arrow-right"></span>
            <el-button size="small" type="primary">导入排班表</el-button>
          </div> -->

          <el-form :inline="true" class="from-s form-box">
            <el-form-item class="select-bar el-input--small">
              <el-input
                class="mr14"
                size="small"
                placeholder="请输入员工姓名"
                v-model="where.name"
                clearable
                style="width: 260px"
                @change="getList"
              >
                <i slot="prefix" class="el-input__icon el-icon-search"></i>
              </el-input>
            </el-form-item>
            <el-form-item class="select-bar">
              <el-date-picker
                v-model="where.date"
                style="width: 150px"
                size="small"
                type="month"
                :picker-options="pickerOptions"
                placeholder="选择月"
                @change="getList"
              >
              </el-date-picker>
            </el-form-item>

            <!-- <el-form-item>
              <el-tooltip effect="dark" content="重置搜索条件" placement="top">
                <div class="reset" @click="reset"><i class="iconfont iconqingchu"></i></div>
              </el-tooltip>
            </el-form-item> -->
          </el-form>
        </div>

        <!-- 设置排班 -->
        <el-form ref="ruleForm" label-width="90px" class="mt10 form-box">
          <el-form-item class="h32">
            <span slot="label">考勤班次：</span>
            <div class="flex-box">
              <template>
                <div class="shift-tag">
                  <div v-for="(item, index) in shiftsList" :key="index">
                    <div v-if="item.id !== 0 && item.times.length > 0">
                      <span class="fang" :style="{ backgroundColor: item.color }"></span>
                      <span class="mr10">
                        {{ item.name }}
                        {{ item.times[0].first_day_after == 0 ? '当日' : '次日' }} {{ item.times[0].work_hours }} -
                        {{ item.times[0].second_day_after == 0 ? '当日' : '次日' }}{{ item.times[0].off_hours }}
                        <span v-if="item.times.length > 1"
                          >、 {{ item.times[1].first_day_after == 0 ? '当日' : '次日' }} {{ item.times[1].work_hours }} -
                          {{ item.times[1].second_day_after == 0 ? '当日' : '次日' }}{{ item.times[1].off_hours }}
                        </span>
                      </span>
                    </div>
                  </div>
                  <span class="fang" style="backgroundcolor: #cccccc"></span>
                  <span>休息</span>
                </div>
              </template>
            </div>
          </el-form-item>
          <el-form-item class="mb0">
            <span slot="label">排班周期：</span>
            <div class="flex">
              <div class="flex-box">
                <template v-for="(item, index4) in cycleList">
                  <div class="cycle" :key="index4" v-if="item.id !== 0">
                    {{ item.name }}{{ cycleList.length > 2 ? '、' : '' }}
                  </div>
                </template>

                <span class="addText" @click="addCycleFn">添加</span>
              </div>
              <span class="tips">未排班时，系统根据排休日历配置判断是否上班，若上班按照默认班次进行考勤</span>
            </div>
          </el-form-item>
        </el-form>

        <div class="table-wrapper">
          <!-- 排班表格 -->
          <el-table v-loading="loading" :data="tableData" style="width: 100%" border :cell-style="cellStyle" height="100%">
            <template v-if="where.date">
              <!-- 点击选中整行 -->
              <el-table-column prop="name" label="姓名" width="140" fixed="left" style="background-color: #fff">
                <template slot-scope="scope">
                  <el-popover placement="bottom" :ref="'row' + scope.$index" width="238px" trigger="click">
                    <div>
                      <el-tabs v-model="activeName">
                        <el-tab-pane label="按班次排班" name="1">
                          <div class="shiftBox">
                            <div
                              class="box"
                              v-for="(item, index2) in shiftsList"
                              :key="index2"
                              @click="clickName(scope, item)"
                              :style="{ backgroundColor: item.color }"
                              :class="item.id == 0 ? 'empty' : ''"
                            >
                              {{ item.name }}
                            </div>
                          </div>
                        </el-tab-pane>

                        <el-tab-pane label="按周期排班" name="2">
                          <div class="shiftBox">
                            <div
                              class="box add-line"
                              v-for="(item, cycle1) in cycleList"
                              :key="cycle1"
                              @click="clickCycle(1, scope, item)"
                              :class="item.id == 0 ? 'empty' : ''"
                            >
                              {{ item.name }}
                            </div>
                          </div>
                        </el-tab-pane>
                      </el-tabs>
                    </div>

                    <div slot="reference">
                      <el-tooltip class="item" effect="dark" content="点击选中整行" placement="top">
                        <div class="nameFn" @click="clickRowIndex(scope.row.name)">
                          {{ scope.row.name }}
                        </div>
                      </el-tooltip>
                    </div>
                  </el-popover>
                </template>
              </el-table-column>

              <el-table-column :prop="item.prop" v-for="(item, index1) in colData" :key="index1">
                <!-- 点击选中整列 -->
                <template v-slot:header>
                  <el-popover :ref="'column' + index1" placement="right" width="238px" trigger="click">
                    <el-tabs v-model="activeName1" v-if="sameDay <= index1">
                      <el-tab-pane label="按班次排班" name="1">
                        <div class="shiftBox">
                          <div
                            class="box"
                            v-for="(item, index3) in shiftsList"
                            :key="index3"
                            @click="headerClick(index1, item)"
                            :style="{ backgroundColor: item.color }"
                            :class="item.id == 0 ? 'empty' : ''"
                          >
                            {{ item.name }}
                          </div>
                        </div>
                      </el-tab-pane>

                      <el-tab-pane label="按周期排班" name="2">
                        <div class="shiftBox">
                          <div
                            class="box add-line"
                            v-for="(item, cycle1) in cycleList"
                            :key="cycle1"
                            @click="clickCycle(2, index1, item)"
                            :class="item.id == 0 ? 'empty' : ''"
                          >
                            {{ item.name }}
                          </div>
                        </div>
                      </el-tab-pane>
                    </el-tabs>
                    <div v-else>历史日期无法排班</div>

                    <div slot="reference" @click="clickColumnIndex(index1)" :class="item.is_rest == 1 ? 'addDate' : ''">
                      <el-tooltip class="item" effect="dark" content="点击选中整列" placement="top">
                        <div class="date">
                          <div class="day">{{ $moment(item.date).format('D') }}</div>
                          <div class="week">{{ getWeek(item.date) }}</div>
                        </div>
                      </el-tooltip>
                    </div>
                  </el-popover>
                </template>

                <!-- 点击选中单元格 -->
                <template slot-scope="scope">
                  <el-popover :ref="'pop' + index1" placement="bottom" width="238px" trigger="click">
                    <el-tabs v-model="activeName2" v-if="sameDay <= index1">
                      <el-tab-pane label="按班次排班" name="1">
                        <div class="shiftBox">
                          <div
                            class="box"
                            v-for="(item, index2) in shiftsList"
                            :key="index2"
                            @click="clickFn(scope, index1, item)"
                            :style="{ backgroundColor: item.color }"
                            :class="item.id == 0 ? 'empty' : ''"
                          >
                            {{ item.name }}
                          </div>
                        </div>
                      </el-tab-pane>

                      <el-tab-pane label="按周期排班" name="2">
                        <div class="shiftBox">
                          <div
                            class="box add-line"
                            v-for="(item, cycle1) in cycleList"
                            :key="cycle1"
                            @click="clickCycle(3, scope, item, index1)"
                            :class="item.id == 0 ? 'empty' : ''"
                          >
                            {{ item.name }}
                          </div>
                        </div>
                      </el-tab-pane>
                    </el-tabs>
                    <div v-else>历史日期无法排班</div>

                    <div slot="reference" class="btn" v-if="!scope.row.counts[index1]" @click="cellFn(scope, index1)"></div>

                    <div
                      slot="reference"
                      class="btn1"
                      v-if="scope.row.counts[index1]"
                      :style="{ backgroundColor: scope.row.counts[index1].color }"
                      @click="cellFn(scope, index1)"
                    >
                      {{
                        scope.row.counts[index1].name && scope.row.counts[index1].name.length > 4
                          ? scope.row.counts[index1].name.substring(0, 4)
                          : scope.row.counts[index1].name
                      }}
                    </div>
                  </el-popover>
                </template>
              </el-table-column>
            </template>
          </el-table>
        </div>
    </el-card>
    <div class="cr-bottom-button btn-shadow">
      <el-button size="small" @click="backFn">取消</el-button>
      <el-button size="small" type="primary" @click="submit">保存</el-button>
    </div>
    <!-- 添加周期 -->
    <add-cycle ref="addCycle" @cycleList="getCycleList" :group_id="id"></add-cycle>
    <!-- 班次弹窗 -->
  </div>
</template>

<script>
import { toGetWeek } from '@/utils/format'
import {
  attendanceShiftListApi,
  rosterCycleListApi,
  getAttendanceArrangeApi,
  saveAttendanceArrangeApi
} from '@/api/config'

export default {
  name: 'CrmebOaEntAddSchedul',
  components: { addCycle: () => import('./components/addCycle') },
  props: {
    id: {
      type: Number,
      default: 0 //考勤组id
    },
    newData: {
      type: Object,
      default: {} //考勤组数据
    }
  },

  data() {
    return {
      where: {
        name: '',
        date: ''
      },
      sameDay: 0, // 当天
      activeName: '1',
      activeName1: '1',
      activeName2: '1',
      loading: false,
      cycleList: [],
      shiftsList: [{ name: '清空', color: 'rgba(229, 241, 255, 1)', id: 0 }],
      tableData: [],
      colData: [],
      // 这是shiftsList 和 cycleList 的数据
      shiftsMappingList: [],
      rowIndex: null,
      columnIndex: -2,
      cellRow: null,
      cellIndex: null,
      activeRow: false,
      activeColumn: false,
      activeCell: false,
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() < Date.now()
        }
      }
    }
  },

  mounted() {
    this.where.date = this.$moment(this.newData.date).format('yyyy-MM')
    this.getday()
    this.getDataOptionalList().then(() => {
      this.getList()
    })
  },

  methods: {
    getday() {
      let date = new Date()

      if (this.$moment(this.where.date).format('YYYY-MM') == this.$moment().format('YYYY-MM')) {
        this.sameDay = date.getDate()
      } else {
        this.sameDay = 0
      }
    },
    async submit() {
      let object = {
        date: this.where.date,
        data: this.tableData.map((item) => {
          return {
            uid: item.id,
            shifts: item.counts.map((v) => {
              // 1. v 是0
              // 2. v 是一个渲染对象
              // 如果v是一个渲染对象, 则取v.id
              if (typeof v === 'object') {
                return v.id
              }
              if (!v) {
                return 0
              }
              // 如果v是0, 则取v
              return v
            })
          }
        })
      }

      await saveAttendanceArrangeApi(this.id, object)
    },

    // 点击选中单元格
    clickFn(row, index, data) {
      this.$set(this.tableData[row.$index].counts, [index], data.id == 0 ? 0 : data)
      // 手动关闭
      let ref = 'pop' + index
      this.$refs[ref][row.$index].doClose()
      this.activeCell = false
    },

    // 点击选中整行
    clickName(row, data) {
      this.colData.map((item, index) => {
        if (index < this.sameDay) return;
        if (data.id == 0) {
          this.$set(this.tableData[row.$index].counts, index, 0)
        } else {
          this.$set(this.tableData[row.$index].counts, index, data)
        }
      })

      let ref = 'row' + row.$index
      this.$refs[ref].doClose()
      this.rowIndex = null
      this.activeRow = false
    },

    // 点击选中整列
    headerClick(val, data) {
      this.columnIndex = val
      this.tableData.forEach((item, index) => {
        this.$set(item.counts, val, data.id === 0 ? 0 : data)
      })

      this.columnIndex = -2
      let ref = 'column' + val
      this.$refs[ref][0].doClose()
      this.activeColumn = false
    },

    // 选中行渲染周期
    clickCycle(type, row, data, index) {
      // 选中整行
      if (type == 1) {
        if (data.id == 0) {
          if (this.sameDay !== 0) {
            const counts = this.tableData[row.$index].counts;
            counts.splice(this.sameDay - 1, counts.length - this.sameDay + 1, ...new Array(counts.length - this.sameDay + 1).fill(0))
          } else {
            this.tableData[row.$index].counts.fill(0);
          }
        } else {
          let n = this.colData.length
          let mul = n / data.shifts.length
          let rem = n % data.shifts.length
          mul = parseInt(mul)

          this.tableData[row.$index].counts = []
          for (var i = 0, len = mul; i < len; i++) {
            this.tableData[row.$index].counts.push(...data.shifts)
          }

          let newData = data.shifts.slice(0, rem)
          newData.map((item) => {
            this.tableData[row.$index].counts.push(item)
          })
          if (this.sameDay !== 0) {
            this.tableData[row.$index].counts.forEach((item, i) => {
              if (i < this.sameDay) {
                this.tableData[row.$index].counts[i] = 0
              }
            })
          }

          this.rowIndex = null
          let ref = 'row' + row.$index
          this.$refs[ref].doClose()
        }
      }
      // 选中整列
      if (type == 2) {
        this.tableData.forEach((item, index) => {
          if (data.id == 0) {
            this.$set(item.counts, row, 0)
          } else {
            this.$set(item.counts, row, data.shifts[0])
          }
        })
        this.columnIndex = -2
        let ref = 'column' + row
        this.$refs[ref][0].doClose()
      }
      if (type == 3) {
        if (data.id == 0) {
          this.$set(this.tableData[row.$index].counts, index, {})
        } else {
          this.$set(this.tableData[row.$index].counts, index, data.shifts[0])
        }
        let ref = 'pop' + index
        this.$refs[ref][row.$index].doClose()
      }
    },

    clickRowIndex(name) {
      this.activeRow = true
      this.rowIndex = name
      this.columnIndex = -2
      this.cellRow = null
      this.cellIndex = null
    },
    clickColumnIndex(index) {
      this.activeColumn = true
      this.columnIndex = index
      this.rowIndex = null
      this.cellRow = null
      this.cellIndex = null
    },
    cellFn(row, index) {
      this.activeCell = true
      this.cellRow = row.$index
      this.cellIndex = index
      this.columnIndex = -2
      this.rowIndex = null
    },

    // 单元格样式
    cellStyle({ row, rowIndex, columnIndex }) {
      if (this.activeColumn) {
        if (this.columnIndex + 1 == columnIndex) {
          return { background: 'rgba(24, 144, 255, 0.2)' }
        }
      }

      if (this.activeRow) {
        if (row.name == this.rowIndex) {
          return { background: 'rgba(24, 144, 255, 0.2)' }
        }
      }

      if (this.activeCell) {
        if (this.cellIndex + 1 == columnIndex && this.cellRow == rowIndex) {
          return { background: 'rgba(24, 144, 255, 0.2)' }
        }
      }
    },

    // 获取班次 / 周期列表
    getDataOptionalList() {
      this.loading = true
      return new Promise((resolve, reject) => {
        let data = {
          page: 1,
          group_id: this.id
        }
        const p1 = attendanceShiftListApi(data)
        const p2 = rosterCycleListApi(this.id)
        Promise.all([p1, p2]).then((res) => {
          this.shiftsList = res[0].data.list
          this.shiftsList.unshift({ name: '清空', color: 'rgba(229, 241, 255, 1)', id: 0 })
          this.cycleList = res[1].data
          this.cycleList.unshift({ name: '清空', id: 0 })
          // 集合映射列表
          this.shiftsMappingList = [...this.shiftsList]
          this.cycleList.forEach((item) => {
            if (item.shifts) {
              item.shifts.forEach((data) => {
                if (this.shiftsMappingList.findIndex((i) => i.id === data.id) === -1) {
                  this.shiftsMappingList.push(data)
                }
              })
            }
          })
          // 后续的方法可以then了
          resolve()
        })
      })
    },

    // 周期列表
    async getCycleList() {
      const result = await rosterCycleListApi(this.id)
      this.cycleList = [{ name: '清空', id: 0 }]
      result.data.map((item) => {
        this.cycleList.push(item)
      })
    },

    // 打开排班周期
    addCycleFn() {
      this.$refs.addCycle.openBox(this.id, 'add')
    },

    async getList() {
      this.getday()
      this.tableData = []
      this.colData = []
      if (!this.where.date) return;
      this.loading = true
      this.where.date = this.$moment(this.where.date).format('yyyy-MM')
      const result = await getAttendanceArrangeApi(this.id, this.where)
      this.tableData = result.data.members.map((per) => {
        return {
          id: per.id,
          name: per.name,
          counts: this.generateSchedulingCycleData(per.id, result.data.arrange, result.data.calendar.length)
        }
      })

      // 生成表格头信息
      this.colData = result.data.calendar.map((item, index) => {
        return {
          date: item.date,
          is_rest: item.is_rest
        }
      })
      this.loading = false
    },

    /**
     *
     * 生成排版数据映射数据
     * @param {} id 排版人员id
     * @param {*} arr 排版数据
     * @param {*} length 排版天数
     */
    generateSchedulingCycleData(id, arr, length) {
      // 如果接口返回的排班数据为空, 则返回一个对应排版天数的空数组
      if (!arr || arr.length === 0 || !arr.find((item) => item.uid === id)) return new Array(length).fill(0)
      // 如果接口返回的排班数据不为空, 判断接口返回的这个人的数据是否齐全
      let findResObj = arr.find((item) => item.uid === id)
      if (findResObj && findResObj.shifts) {
        if (findResObj.shifts.length === length) {
          // 如果接口返回的这个人的数据齐全, 则返回对应映射数据返回页面的渲染元素
          return findResObj.shifts.map((id) => {
            return this.shiftsMappingList.find((per) => {
              if (id === 0) return 0
              return per.id === id
            })
          })
        }

        // 如果接口返回的这个人的数据不齐全, 则返回对应的渲染对象, 剩下数据补齐
        let tempArr = []
        for (let i = 0; i < length; i++) {
          if (findResObj.shifts[i]) {
            tempArr.push(
              this.shiftsMappingList.find((per) => {
                if (findResObj.shifts[i] === 0) return 0
                return per.id === findResObj.shifts[i]
              })
            )
          } else {
            tempArr.push(0)
          }
        }
        return tempArr
      }
    },

    backFn() {
      this.$emit('backFn')
    },

    restFn() {
      this.where.name = ''
      this.where.date = this.$moment(this.newData.date).format('yyyy-MM')
    },

    getWeek(date) {
      // 参数时间戳
      return toGetWeek(date)
    }
  }
}
</script>

<style lang="scss" scoped>
.nameFn {
  cursor: pointer;
  width: 100%;
  height: 100%;
  padding: 4px 5px;
}
.form-box {
  ::v-deep .el-form-item {
    margin-bottom: 10px;
  }
}

.btn {
  cursor: pointer;
  border-radius: 6px;
  // width: 65px;
  width: 100%;
  height: 34px;
  text-align: center;
}
.btn1 {
  cursor: pointer;
  // width: 60px;
  width: 100%;
  padding: 4px 5px;
  border-radius: 6px;
  color: #ffffff;
  white-space: nowrap;
  overflow: hidden;
  text-align: center;
}
::v-deep .el-table--medium td {
  padding: 6px 0;
}

.shiftBox {
  width: 228px;
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
  .box {
    width: 100px;
    height: 30px;
    border-radius: 6px;
    text-align: center;
    line-height: 30px;
    font-size: 13px;
    font-family: PingFang SC-Regular, PingFang SC;
    font-weight: 400;
    color: #fff;
    margin-right: 14px;
    background-color: #1890ff;
    margin-top: 10px;

    cursor: pointer;
  }
}
.add-line {
  color: #303133 !important;
  border: 1px solid #dddddd;
  text-align: center;
  background-color: #fff !important;
}

.empty {
  color: #1890ff !important;
  border: 1px solid #1890ff;
  background-color: rgba(229, 241, 255, 1) !important;
}
.date {
  text-align: center;
  .day {
    font-size: 18px;
    font-family: PingFang SC-Medium, PingFang SC;
    font-weight: 500;
    color: #303133;
  }
  .week {
    font-size: 13px;
    font-family: PingFang SC-Regular, PingFang SC;
    font-weight: 400;
    color: #606266;
  }
}
.tips {
  font-size: 14px;
  font-family: PingFang SC-Regular, PingFang SC;
  font-weight: 400;
  color: #606266;
}
.mb0 {
  margin-bottom: 0px !important;
}
.flex {
  display: flex;
  justify-content: space-between;
}
.addText {
  font-family: PingFang SC-Regular, PingFang SC;
  font-weight: 400;
  color: #1890ff;
  cursor: pointer;
  margin-left: 8px;
}
.cycle {
  font-size: 13px;
  font-family: PingFang SC-Regular, PingFang SC;
  font-weight: 400;
  color: rgba(0, 0, 0, 0.8);
  margin-bottom: 10px;
}

.flex-box {
  display: flex;
  flex-wrap: wrap;
  div {
    display: flex;
    align-items: center;
  }

  .shift-tag {
    display: flex;
    align-items: center;
    margin-right: 20px;
    font-size: 13px;
    font-family: PingFang SC-Regular, PingFang SC;
    font-weight: 400;
    color: rgba(0, 0, 0, 0.85);
    .fang {
      display: inline-block;
      margin-right: 4px;
      width: 12px;
      height: 12px;
      background: #ffaa18;
      border-radius: 2px;
    }
  }

  div:last-of-type {
    margin-left: 0;
  }
}
.head {
  display: flex;
  justify-content: space-between;
}
.from-s {
  display: inline-block;
}
::v-deep .el-icon-back {
  display: none;
}
.card-box {
  height: calc(100vh - 208px);
  // overflow-y: scroll;
  font-size: 13px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #303133;
  .main {
    width: 800px;
    margin: 0 auto;
  }

  ::v-deep.el-card__body {
    display: flex;
    flex-flow: column nowrap;
    height: 100%;

    .table-wrapper {
      flex: 1;
      padding-bottom: 20px;
    }
  }
}
.addDate {
  .week {
    color: red !important;
  }
  .day {
    color: red !important;
  }
}
.el-icon-d-arrow-right {
  margin: 0 10px;
  background-image: linear-gradient(180deg, #dedede 0%, #999999 100%);
  color: transparent;
  -webkit-background-clip: text;
}

::v-deep.el-table__body tr.hover-row {
  background-color: transparent !important;
}
::v-deep .el-tabs__nav-wrap::after {
  height: 1px;
}
.cr-bottom-button {
  position: fixed;
  left: -20px;
  right: 0;
  bottom: 0;
  width: calc(100% + 220px);
}
</style>
