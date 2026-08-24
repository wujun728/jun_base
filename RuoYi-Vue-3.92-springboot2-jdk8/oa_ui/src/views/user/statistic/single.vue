<template>
  <div class="divBox">
    <!-- 个人统计 -->
    <el-card>
      <oaFromBox
        :isAddBtn="false"
        :isTotal="false"
        :isViewSearch="false"
        :search="search"
        :sortSearch="false"
        @confirmData="confirmData"
      >
      </oaFromBox>
      <!-- 考勤统计 -->
      <div class="flex-box mt14">
        <div class="left">
          <div class="title">
            平均工时(小时) <span class="num">{{ work_hours || 0 }}</span>
          </div>

          <echartBox :option-data="optionData" :styles="styles" />
        </div>
        <div class="right">
          <div class="top">
            <div class="title">异常考勤汇总</div>
            <div class="right-box">
              <div v-for="(item, index) in attendanceList" :key="index" class="attendance">
                <img :src="item.img" alt="" />
                <div class="attendance-days">
                  <div class="day">{{ item.num || 0 }}</div>
                  <div class="tips">{{ item.title }}</div>
                </div>
              </div>
            </div>
          </div>
          <div class="lower">
            <div class="title">打卡异常汇总</div>
            <div class="right-box">
              <div v-for="(item, index) in clockInList" :key="index" class="attendance">
                <img :src="item.img" alt="" />
                <div class="attendance-days">
                  <div class="day">{{ item.num || 0 }}</div>
                  <div class="tips">{{ item.title }}</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-card>
    <!-- 考勤表格 -->
    <el-card class="mt14">
      <div class="mb10 flex">
        <span class="title-k">个人打卡统计</span>
        <el-select v-model="where.status" clearable placeholder="请选择打卡结果" @change="getDataList">
          <el-option v-for="item in options" :key="item.id" :label="item.name" :value="item.id"> </el-option>
        </el-select>
      </div>
      <!-- 表格 -->
      <el-table :data="tableData" style="width: 100%">
        <el-table-column label="姓名" prop="card.name"> </el-table-column>
        <el-table-column label="考勤班次" min-width="150px" prop="name">
          <template slot-scope="{ row }">
            <div>{{ row.shift_data.name }}</div>
            <span>{{ getShift(row.shift_data) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="日期" prop="created_at" width="160">
          <template slot-scope="{ row }">
            {{ row.created_at }}
          </template>
        </el-table-column>

        <el-table-column label="上班1">
          <el-table-column label="最早打卡" min-width="150" prop="province">
            <template slot-scope="{ row }">
              <div>{{ row.one_shift_is_after ? '次日' : '当日' }}</div>
              {{ row.one_shift_time }}
            </template>
          </el-table-column>
          <el-table-column label="打卡结果" prop="city" width="120">
            <template slot-scope="{ row }">
              <div @click="openFn(row.one_shift_status, row.one_shift_location_status, 1, row.id)">
                <span :class="row.one_shift_status > 1 || row.one_shift_location_status > 1 ? 'red' : ''">
                  {{ getStatus(row.one_shift_status, row.one_shift_location_status) }}
                  <span v-if="1 < row.one_shift_status < 5 && row.one_shift_normal !== 0"
                    >-{{ row.one_shift_normal }}分钟</span
                  >
                </span>
              </div>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column label="下班1">
          <el-table-column label="最晚打卡" prop="province" width="120">
            <template slot-scope="{ row }">
              <div>{{ row.one_shift_is_after == 0 ? '当日' : '次日' }}</div>
              {{ row.two_shift_time }}
            </template>
          </el-table-column>
          <el-table-column label="打卡结果" prop="city" width="120">
            <template slot-scope="{ row }">
              <span :class="row.two_shift_status > 1 || row.two_shift_location_status > 0 ? 'red' : ''">
                {{ getStatus(row.two_shift_status, row.two_shift_location_status) }}
                <span v-if="1 < row.two_shift_status < 5 && row.two_shift_normal !== 0"
                  >-{{ row.two_shift_normal }}分钟</span
                >
              </span>
            </template>
          </el-table-column>
        </el-table-column>

        <el-table-column label="上班2">
          <el-table-column label="最早打卡" prop="province" width="120">
            <template slot-scope="{ row }">
              <div v-if="row.three_shift_time">{{ row.three_shift_is_after == 0 ? '当日' : '次日' }}</div>
              {{ row.three_shift_time || '--' }}
            </template>
          </el-table-column>
          <el-table-column label="打卡结果" prop="city" width="120">
            <template slot-scope="{ row }">
              <span
                v-if="row.three_shift_status"
                :class="row.three_shift_status > 1 || row.three_shift_location_status > 0 ? 'red' : ''"
              >
                {{ getStatus(row.three_shift_status, row.three_shift_location_status) }}
                <span v-if="1 < row.one_shift_status < 5 && row.three_shift_normal !== 0"
                  >-{{ row.three_shift_normal }}分钟</span
                >
              </span>
              <span v-else>--</span>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column label="下班2">
          <el-table-column label="最晚打卡" prop="province" width="120">
            <template slot-scope="{ row }">
              <div v-if="row.four_shift_time">{{ row.four_shift_is_after == 0 ? '当日' : '次日' }}</div>
              {{ row.four_shift_time || '--' }}
            </template>
          </el-table-column>
          <el-table-column label="打卡结果" prop="city" width="120">
            <template slot-scope="{ row }">
              <span
                v-if="row.four_shift_status"
                :class="row.four_shift_status > 1 || row.four_shift_location_status > 0 ? 'red' : ''"
              >
                {{ getStatus(row.four_shift_status, row.four_shift_location_status) }}
                <span v-if="1 < row.one_shift_status < 5 && row.four_shift_normal !== 0"
                  >-{{ row.four_shift_normal }}分钟</span
                >
              </span>
              <span v-else>--</span>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column label="时长统计（小时）">
          <el-table-column label="应出勤" prop="required_work_hours" width="120"> </el-table-column>
          <el-table-column label="实际出勤" prop="actual_work_hours" width="120"> </el-table-column>
          <el-table-column label="加班时长" prop="overtime_work_hours" width="120"> </el-table-column>
          <el-table-column label="请假时长" prop="leave_time" width="120"> </el-table-column>
        </el-table-column>
      </el-table>

      <el-pagination
        :current-page="where.page"
        :page-size="where.limit"
        :page-sizes="[10, 15, 20]"
        :total="totalPage"
        layout="total, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>
  </div>
</template>
<script>
import { attendanceStatistics, individualStatistics } from '@/api/user'
export default {
  name: '',
  components: {
    echartBox: () => import('@/components/common/echarts'),
    oaFromBox: () => import('@/components/common/oaFromBox')
  },
  data() {
    return {
      work_hours: 0, // 平均工时
      required_days: 0, // 应出勤天数
      absenteeism: 0, // 未出勤
      normal_days: 0, // 实际出勤
      userList: [],
      search: [
        {
          field_name: '开始时间',
          field_name_end: '结束时间',
          field_name_en: 'time',
          form_value: 'date_picker'
        },
        {
          field_name: '请选择人员',
          field_name_en: 'user_id',
          form_value: 'user_id',
          data_dict: []
        }
      ],
      totalPage: 0,
      options: [
        {
          id: 1,
          name: '正常'
        },
        {
          id: 2,
          name: '迟到'
        },
        {
          id: 3,
          name: '严重迟到'
        },
        {
          id: 4,
          name: '早退'
        },
        {
          id: 5,
          name: '缺卡'
        },
        {
          id: 6,
          name: '地点异常'
        }
      ],
      clockInList: [
        {
          num: 0,
          img: require('../../../assets/images/clock1.png'),
          title: '迟到(次)'
        },
        {
          num: 0,
          img: require('../../../assets/images/clock2.png'),
          title: '早退(次)'
        },
        {
          num: 0,
          img: require('../../../assets/images/clock3.png'),
          title: '缺卡(次)'
        },
        {
          num: 0,
          img: require('../../../assets/images/clock5.png'),
          title: '地点异常(次)'
        },
        {
          num: 0,
          img: require('../../../assets/images/clock4.png'),
          title: '旷工(天)'
        }
      ],
      attendanceList: [
        {
          num: 0,
          img: require('../../../assets/images/single1.png'),
          title: '请假(小时)'
        },
        {
          num: 0,
          img: require('../../../assets/images/single2.png'),
          title: '出差(天)'
        },
        {
          num: 0,
          img: require('../../../assets/images/single3.png'),
          title: '外出(小时)'
        },
        {
          num: 0,
          img: require('../../../assets/images/single4.png'),
          title: '加班(小时)'
        },
        {
          num: 0,
          img: require('../../../assets/images/single5.png'),
          title: '补卡(次)'
        }
      ],

      where: {
        time:
          this.$moment().startOf('months').format('YYYY/MM/DD') + '-' + this.$moment(new Date()).format('YYYY/MM/DD'),
        user_id: '',
        status: '',
        page: 1,
        limit: 15
      },
      defaultUse: {},
      tableData: [],
      optionData: {},
      styles: {
        height: '256px',
        width: '256px',
        margin: 'auto'
      }
    }
  },

  mounted() {
    const userInfo = JSON.parse(localStorage.getItem('userInfo'))
    this.defaultUse = {
      label: userInfo.name,
      value: userInfo.id,
      name: userInfo.name,
      id: userInfo.id
    }

    this.userList.push(this.defaultUse)
    this.where.user_id = userInfo.value
    this.getOptionData()
    this.getList()
    this.getDataList()
  },
  methods: {
    confirmData(data) {
      if (data == 'reset') {
        this.where.user_id = []
        this.where.status = ''
        this.where.time =
          this.$moment().startOf('months').format('YYYY/MM/DD') + '-' + this.$moment(new Date()).format('YYYY/MM/DD')
      } else {
        for (let i in data) {
          this.where[i] = data[i]
        }
      }

      this.getDataList()
      this.getList()
    },

    getSelectList(data) {
      this.userList = data
      this.where.user_id = data[0].value
      this.timeChange()
    },
    timeChange() {
      this.getDataList()
      this.getList()
    },

    async getDataList() {
      let data = {
        time: this.where.time,
        user_id: this.where.user_id,
        status: this.where.status,
        page: this.where.page,
        limit: this.where.limit
      }
      const result = await individualStatistics(data)
      this.tableData = result.data.list
      this.totalPage = result.data.count
    },
    handleSizeChange(val) {
      this.where.page = 1
      this.where.limit = val
      this.getDataList()
    },
    handleCurrentChange(page) {
      this.where.page = page
      this.getDataList()
    },

    getList() {
      let data = {
        time: this.where.time,
        user_id: this.where.user_id
      }
      attendanceStatistics(data).then((res) => {
        this.work_hours = res.data.work_hours
        this.absenteeism = res.data.absenteeism
        this.required_days = res.data.required_days
        this.normal_days = res.data.normal_days
        this.attendanceList[0].num = res.data.leave_hours
        this.attendanceList[1].num = res.data.trip_hours
        this.attendanceList[2].num = res.data.out_hours
        this.attendanceList[3].num = res.data.overtime_hours
        this.attendanceList[4].num = res.data.sign
        this.clockInList[0].num = res.data.late
        this.clockInList[1].num = res.data.early_leave
        this.clockInList[2].num = res.data.lack_card
        this.clockInList[3].num = res.data.location_abnormal
        this.clockInList[4].num = res.data.absenteeism
        this.getOptionData()
      })
    },
    // 处理表格班次数据
    getShift(data) {
      let text2 = ''
      let text1 = ''
      if (data.rules && data.rules.length !== 0) {
        text1 = `${data.rules[0].first_day_after == 0 ? '当日' : '次日'}${data.rules[0].work_hours} - ${
          data.rules[0].second_day_after == 0 ? '当日' : '次日'
        }${data.rules[0].off_hours}`

        if (data.rules[1]) {
          text2 = `${data.rules[1].first_day_after == 0 ? '当日' : '次日'}${data.rules[1].work_hours} - ${
            data.rules[1].second_day_after == 0 ? '当日' : '次日'
          }${data.rules[1].off_hours}`
        }
      }

      return text1 + text2
    },

    // 处理表格打卡结果数据
    getStatus(status, tip) {
      let str = ''
      let tips = ''
      if (status == 0 || status == 1) {
        str = '正常'
      } else if (status == 2) {
        str = '迟到'
      } else if (status == 3) {
        str = '严重迟到'
      } else if (status == 4) {
        str = '早退'
      } else {
        str = '缺卡'
      }
      if (tip == 0) {
      } else if (tip == 1) {
        tips = '(外勤卡)'
      } else if (tip == 2) {
        tips = '(地点异常)'
      }
      return str + tips
    },

    getOptionData() {
      let absenteeism = this.absenteeism ? this.absenteeism : 0 // 未出勤
      let normal_days = this.normal_days ? this.normal_days : 0 // 实际出勤

      let days = this.required_days ? this.required_days : 0
      let dataArr = [
        { value: normal_days, name: '实际出勤(天)' },
        { value: absenteeism, name: '未出勤(天)' }
      ]
      this.optionData = {
        color: ['#1890FF', '#FF9900'],
        tooltip: {
          trigger: 'item'
        },

        legend: {
          bottom: '1%',
          left: 'center',
          icon: 'circle',
          itemWidth: 10,
          textStyle: {
            fontSize: 13,
            color: '#606266',
            lineHeight: 20 // 解决提示语字显示不全
          },
          formatter: function (name) {
            let count
            for (let i = 0; i < dataArr.length; i++) {
              if (dataArr[i].name === name) {
                count = dataArr[i].value
              }
            }
            let arr = [name + '  ' + count]
            return arr.join('\n')
          }
        },

        series: [
          {
            type: 'pie',
            radius: ['45%', '70%'],
            avoidLabelOverlap: false,
            label: {
              show: true,
              position: 'center',
              formatter: [days, '{name|应出勤天数}'].join('\n'),
              color: '#333',
              textStyle: {
                fontSize: 18,
                fill: '#333',
                lineHeight: 26,
                rich: {
                  name: {
                    fontSize: 14,
                    fill: '#333',
                    color: '#3D3D3D'
                  }
                }
              }
            },

            labelLine: {
              show: false
            },
            data: dataArr
          }
        ]
      }
    },

    reset() {
      this.where.time = [
        this.$moment().startOf('months').format('YYYY/MM/DD'),
        this.$moment(new Date()).format('YYYY/MM/DD')
      ]
      this.userList = []

      this.where.user_id = ''
      this.getDataList()
      this.getList()
    }
  }
}
</script>
<style lang="scss" scoped>
.plan-footer-one {
  height: 26px;
  line-height: 28px;
}
.flex {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.flex-box {
  height: 290px;
  display: flex;
  .left {
    width: 302px;
    .title {
      text-align: center;
      font-size: 13px;
      font-family: PingFang SC-Medium, PingFang SC;
      font-weight: 500;
      color: #606266;
      .num {
        display: inline-block;
        font-size: 24px;
        font-weight: 600;
        color: #1890ff;
        margin-left: 10px;
      }
      .box {
        width: 156px;
        height: 156px;
      }
    }
  }
  .right {
    flex: 1;
    display: flex;
    flex-direction: column;
    .title {
      line-height: 30px;

      font-size: 13px;
      font-family: PingFang SC-Medium, PingFang SC;
      font-weight: 500;
      color: #606266;
    }
    .right-box {
      margin-top: 13px;
      width: 100%;
      height: 92px;
      background-color: #f7fbff;
      padding: 24px 0px;
      padding-right: 0;
      display: flex;

      // justify-content: space-between;
      .attendance {
        margin-left: 30px;
        width: 20%;
        display: flex;
        .attendance-days {
          margin-left: 21px;
          display: flex;
          flex-direction: column;
          .day {
            font-size: 24px;
            font-family: PingFang SC-Semibold, PingFang SC;
            font-weight: 600;
            color: #303133;
          }
          .tips {
            font-size: 13px;
            font-family: PingFang SC-Regular, PingFang SC;
            font-weight: 400;
            color: #606266;
            margin-top: 2px;
          }
        }
      }
    }
    .top {
      width: 100%;
      height: 145px;
    }
    .lower {
      flex: 1;
      width: 100%;
    }
  }
}
::v-deep .el-table thead.is-group th {
  background-color: rgba(247, 251, 255, 1);
  border-color: #fff;
}
::v-deep .el-table {
  border: none;
}

::v-deep .el-table td {
  border: none;
}
.red {
  color: red;
}
.title-k {
  font-size: 16px;
  font-weight: 500;
}
</style>
