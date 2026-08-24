<template>
  <div>
    <el-card :body-style="{ padding: '14px' }" class="station-header">
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
        <el-form :inline="true" class="from-s">
          <el-form-item class="select-bar el-input--small">
            <el-input
              v-model="where.name"
              class="mr14"
              clearable
              placeholder="请输入员工姓名"
              size="small"
              style="width: 260px"
              @change="getList"
            >
              <i slot="prefix" class="el-input__icon el-icon-search"></i>
            </el-input>
          </el-form-item>
          <el-form-item class="select-bar">
            <el-date-picker
              v-model="where.date"
              placeholder="选择月"
              size="small"
              style="width: 150px"
              type="month"
              @change="getList"
            >
            </el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-tooltip content="重置搜索条件" effect="dark" placement="top">
              <div class="reset" @click="restFn"><i class="iconfont iconqingchu"></i></div>
            </el-tooltip>
          </el-form-item>
        </el-form>

        <span class="tips mt10">未排班时，系统根据排休日历配置判断是否上班，若上班按照默认班次进行考勤</span>
      </div>

      <!-- 排班表格 -->
      <el-table v-loading="loading" :data="tableData" border style="width: 100%">
        <!-- 点击选中整行 -->
        <el-table-column fixed="left" label="姓名" prop="name" style="background-color: #fff" width="140">
          <template slot-scope="scope">
            <div class="nameFn">
              {{ scope.row.name }}
            </div>
          </template>
        </el-table-column>

        <el-table-column v-for="(item, index1) in colData" :key="index1" :prop="item.prop">
          <!-- 点击选中整列 -->
          <template v-slot:header>
            <div :class="item.is_rest == 1 ? 'addDate' : ''">
              <div class="date">
                <div class="day">{{ $moment(item.date).format('D') }}</div>
                <div class="week">{{ getWeek(item.date) }}</div>
              </div>
            </div>
          </template>

          <!-- 点击选中单元格 -->
          <template slot-scope="scope">
            <div v-if="!scope.row.counts[index1]" class="btn"></div>
            <div
              v-if="scope.row.counts[index1]"
              :style="{ backgroundColor: scope.row.counts[index1].color }"
              class="btn1"
            >
              {{
                scope.row.counts[index1].name && scope.row.counts[index1].name.length > 4
                  ? scope.row.counts[index1].name.substring(0, 4)
                  : scope.row.counts[index1].name
              }}
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { attendanceShiftListApi, rosterCycleListApi, getAttendanceArrangeApi } from '@/api/config'
import { toGetWeek } from '@/utils/format'
export default {
  name: 'CrmebOaEntAddSchedul',

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
      activeCell: false
    }
  },

  mounted() {
    this.where.date = this.$moment(this.newData.date).format('yyyy-MM')
    this.getDataOptionalList().then(() => {
      this.getList()
    })
  },

  methods: {
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

    async getList() {
      this.tableData = []
      this.colData = []
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

.btn {
  cursor: pointer;
  border-radius: 6px;
  width: 65px;
  height: 34px;
  text-align: center;
}
.btn1 {
  cursor: pointer;
  width: 60px;
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
.flex {
  display: flex;
  justify-content: space-between;
}
.addText {
  font-family: PingFang SC-Regular, PingFang SC;
  font-weight: 400;
  color: #1890ff;
  cursor: pointer;
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
  height: calc(100vh - 150px);
  overflow-y: scroll;
  font-size: 13px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #303133;
  .main {
    width: 800px;
    margin: 0 auto;
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
</style>
