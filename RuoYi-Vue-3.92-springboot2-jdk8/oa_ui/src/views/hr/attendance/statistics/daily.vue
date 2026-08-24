<template>
  <div class="divBox">
    <el-card class="employees-card-bottom">
      <!-- 搜索条件 -->
      <formBox @confirmData="confirmData" :total="total"></formBox>
      <!-- 表格 -->
      <el-table class="mt10" :data="tableData" v-loading="loading" style="width: 100%" :height="tableHeight">
        <el-table-column prop="card.name" label="姓名"> </el-table-column>
        <el-table-column prop="frame.name" label="部门" min-width="140px"> </el-table-column>
        <el-table-column label="班次信息">
          <el-table-column prop="group" label="考勤组名称" width="120">
            <template slot-scope="{ row }">
              {{ row.group || '--' }}
            </template>
          </el-table-column>
          <el-table-column prop="created_at" label="日期" width="120">
            <template slot-scope="{ row }">
              {{ $moment(row.created_at).format('YYYY-MM-DD') }}
            </template>
          </el-table-column>
          <el-table-column prop="name" label="考勤班次" min-width="150px">
            <template slot-scope="{ row }">
              <div>{{ row.shift_data.name || '--' }}</div>
              <span>{{ getShift(row.shift_data) }}</span>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column label="上班1">
          <el-table-column prop="province" label="最早打卡" width="95">
            <template slot-scope="{ row }">
              {{ handle(row.one_shift_status, row.one_shift_is_after, row.one_shift_time) }}
            </template>
          </el-table-column>
          <el-table-column prop="city" label="打卡结果" width="120">
            <template slot-scope="{ row }">
              <div @click="openFn(row.one_shift_status, row.one_shift_location_status, 1, row.id)">
                <span :class="row.one_shift_status > 1 || row.one_shift_location_status > 1 ? 'red' : 'pointer'">
                  {{ getStatus(row.one_shift_status, row.one_shift_location_status, row.one_shift_normal) }}
                </span>
              </div>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column label="下班1">
          <el-table-column prop="province" label="最晚打卡" width="95">
            <template slot-scope="{ row }">
              {{ handle(row.two_shift_status, row.two_shift_is_after, row.two_shift_time) }}
            </template>
          </el-table-column>
          <el-table-column prop="city" label="打卡结果" width="120">
            <template slot-scope="{ row }">
              <div @click="openFn(row.two_shift_status, row.two_shift_location_status, 2, row.id)">
                <span :class="row.two_shift_status > 1 || row.two_shift_location_status > 0 ? 'red' : 'pointer'">
                  {{ getStatus(row.two_shift_status, row.two_shift_location_status, row.two_shift_normal) }}
                </span>
              </div>
            </template>
          </el-table-column>
        </el-table-column>

        <el-table-column label="上班2">
          <el-table-column prop="province" label="最早打卡" width="95">
            <template slot-scope="{ row }">
              {{ handle(row.three_shift_status, row.three_shift_is_after, row.three_shift_time) }}
            </template>
          </el-table-column>
          <el-table-column prop="city" label="打卡结果" width="120">
            <template slot-scope="{ row }">
              <div @click="openFn(row.three_shift_status, row.three_shift_location_status, 3, row.id)">
                <span :class="row.three_shift_status > 1 || row.three_shift_location_status > 0 ? 'red' : 'pointer'">
                  {{ getStatus(row.three_shift_status, row.three_shift_location_status, row.three_shift_normal) }}
                </span>
              </div>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column label="下班2">
          <el-table-column prop="province" label="最晚打卡" width="95">
            <template slot-scope="{ row }">
              {{ handle(row.four_shift_status, row.four_shift_is_after, row.four_shift_time) }}
            </template>
          </el-table-column>
          <el-table-column prop="city" label="打卡结果" width="120">
            <template slot-scope="{ row }">
              <div @click="openFn(row.four_shift_status, row.four_shift_location_status, 4, row.id)">
                <span :class="row.four_shift_status > 1 || row.four_shift_location_status > 0 ? 'red' : 'pointer'">
                  {{ getStatus(row.four_shift_status, row.four_shift_location_status, row.four_shift_normal) }}
                </span>
              </div>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column label="时长统计（小时）">
          <el-table-column prop="required_work_hours" label="应出勤" width="120"> </el-table-column>
          <el-table-column prop="actual_work_hours" label="实际出勤" width="120"> </el-table-column>
          <el-table-column prop="overtime_work_hours" label="加班时长" width="120"> </el-table-column>
          <el-table-column prop="leave_time" label="请假时长" width="120"> </el-table-column>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="{ row }">
            <el-button type="text" @click="openRecord(row)">处理记录</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="page-fixed">
        <el-pagination
          :page-size="where.limit"
          :current-page="where.page"
          :page-sizes="[15, 20, 30]"
          layout="total,sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="pageChange"
        />
      </div>
    </el-card>
    <!-- 记录 -->
    <record-drawer ref="recordDrawer"></record-drawer>
    <!-- 打卡结果更改弹窗 -->
    <oa-dialog ref="oaDialog" :fromData="fromData" @submit="submit">
      <el-form>
        <el-form-item :label="form.name" label-width="60px">
          <div class="flex lh-center">
            <el-select size="small" v-model="form.dis_status" placeholder="请选择" disabled>
              <el-option v-for="item in salesmanList" :key="item.id" :label="item.name" :value="item.id"> </el-option>
            </el-select>
            <span class="el-icon-d-arrow-right"></span>
            <el-select size="small" v-model="form.status" placeholder="请选择">
              <el-option v-for="item in salesmanList" :key="item.id" :label="item.name" :value="item.id"> </el-option>
            </el-select>
          </div>
        </el-form-item>
        <el-form-item label-width="60px" v-if="form.dis_locationStatus !== 0">
          <div>
            <el-select size="small" v-model="form.dis_locationStatus" placeholder="请选择" disabled>
              <el-option v-for="item in placeList" :key="item.id" :label="item.name" :value="item.id"> </el-option>
            </el-select>
            <span class="el-icon-d-arrow-right"></span>
            <el-select size="small" v-model="form.location_status" placeholder="请选择">
              <el-option v-for="item in placeList" :key="item.id" :label="item.name" :value="item.id"> </el-option>
            </el-select>
          </div>
        </el-form-item>
        <el-form-item label="备注：" label-width="60px">
          <div class="flex">
            <el-input type="textarea" placeholder="请输入内容" v-model="form.remark"> </el-input>
          </div>
        </el-form-item>
      </el-form>
    </oa-dialog>

    <!-- 导出 -->
    <export-excel :template="true" :save-name="saveName" :export-data="exportData" ref="exportExcel" />
  </div>
</template>

<script>
import { dailyStatisticsApi, putStatisticsApi } from '@/api/config'
export default {
  name: 'CrmebOaEntDaily',
  components: {
    formBox: () => import('./components/formBox'),
    oaDialog: () => import('@/components/form-common/dialog-form'),
    recordDrawer: () => import('./components/recordDrawer'),
    exportExcel: () => import('@/components/common/exportExcel')
  },

  data() {
    return {
      saveName: '上下班打卡_日报_统计日期范围.xlsx',
      loading: false,
      exportData: {
        data: [],
        cols: [{ wpx: 70 }, { wpx: 70 }, { wpx: 120 }, { wpx: 140 }, { wpx: 200 }, { wpx: 120 }, { wpx: 120 }]
      },
      total: 0,
      where: {
        page: 1,
        limit: 15,
        time: '',
        scope: '',
        status: '',
        frame_id: '',
        user_id: [],
        group_id: ''
      },
      rowId: '',
      form: {
        name: '上班1',
        dis_status: '',
        dis_locationStatus: '',
        status: '',
        number: '',
        remark: '',
        location_status: ''
      },
      placeList: [
        {
          id: 1,
          name: '外勤卡'
        },
        {
          id: 2,
          name: '地点异常'
        }
      ],
      salesmanList: [
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
          id: 7,
          name: '缺卡'
        }
      ],
      val: '',
      fromData: {
        with: '700px',
        title: '打卡结果更改',
        btnText: '确定',
        labelWidth: '90px',
        type: 'slot'
      },
      tableData: []
    }
  },

  mounted() {
    this.getList()
  },

  methods: {
    openRecord(data) {
      this.$refs.recordDrawer.openBox(data)
    },
    openFn(status, location_status, type, id) {
      this.rowId = id
      this.form.remark = ''
      if (type == 1) {
        this.form.name = '上班1：'
        this.form.number = 0
      } else if (type == 2) {
        this.form.name = '下班1：'
        this.form.number = 1
      } else if (type == 3) {
        this.form.name = '上班2：'
        this.form.number = 2
      } else {
        this.form.name = '下班2：'
        this.form.number = 3
      }
      this.form.dis_status = status
      this.form.dis_locationStatus = location_status

      this.$refs.oaDialog.openBox()
    },
    async submit() {
      let data = {
        number: this.form.number,
        remark: this.form.remark,
        status: this.form.status,
        location_status: this.form.location_status || 0
      }
      await putStatisticsApi(this.rowId, data)
      await this.$refs.oaDialog.handleClose()
      await this.getList()
    },
    async getList() {
      this.loading = true
      const result = await dailyStatisticsApi(this.where)
      this.loading = false
      this.total = result.data.count
      this.tableData = result.data.list
    },
    confirmData(data, type) {
      this.where.page = 1
      this.where.time = data.time
      this.where.scope = data.scope
      this.where.status = data.status
      this.where.frame_id = data.frame_id
      this.where.user_id = data.user_id
      this.where.group_id = data.group_id
      if (type == '导出') {
        this.exportFn()
      } else {
        this.getList()
      }
    },

    // 处理数据
    handle(status, day, time) {
      let str = ''
      if (status > 0) {
        str = day == 0 ? '当日' : '次日'
        if (time) {
          str = str + time
        }
      } else {
        str = '--'
      }

      return str
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
    async exportFn() {
      let aoaData = [
        ['概况统计与打卡明细'],
        [`统计时间：${this.where.time} 制表时间：${this.$moment(new Date()).format('YYYY/MM/DD')}`],
        [
          '姓名',
          '部门',
          '班次信息',
          '',
          '',
          '上班1',
          '',
          '下班1',
          '',
          '上班2',
          '',
          '下班2',
          '',
          '时长统计',
          '',
          '',
          ''
        ],
        [
          '',
          '',
          '考勤组名称',
          '日期',
          '考勤班次',
          '最早打卡',
          '打卡结果',
          '最晚打卡',
          '打卡结果',
          '最早打卡',
          '打卡结果',
          '最晚打卡',
          '打卡结果',
          '应出勤',
          '实际出勤',
          '加班时长',
          '请假时长'
        ]
      ]
      let obj = { ...this.where }
      obj.page = 0
      obj.limit = 0
      const result = await dailyStatisticsApi(obj)
      result.data.list.map((item) => {
        aoaData.push([
          item.card ? item.card.name : '--',
          item.frame ? item.frame.name : '--',
          item.group || '--',
          this.$moment(item.created_at).format('YYYY-MM-DD'),
          item.shift_data.name + this.getShift(item.shift_data),

          this.handle(item.one_shift_status, item.one_shift_is_after, item.one_shift_time),
          this.getStatus(item.one_shift_status, item.one_shift_location_status, item.one_shift_normal),

          this.handle(item.two_shift_status, item.two_shift_is_after, item.two_shift_time),
          this.getStatus(item.two_shift_status, item.two_shift_location_status, item.two_shift_normal),

          this.handle(item.three_shift_status, item.three_shift_is_after, item.three_shift_time),
          this.getStatus(item.three_shift_status, item.three_shift_location_status, item.three_shift_normal),

          this.handle(item.four_shift_status, item.four_shift_is_after, item.four_shift_time),
          this.getStatus(item.four_shift_status, item.four_shift_location_status, item.four_shift_normal),

          item.required_work_hours + '小时',
          item.actual_work_hours + '小时',
          item.overtime_work_hours + '小时',
          item.leave_time + '小时'
        ])
      })

      this.exportData.data = aoaData
      this.$refs.exportExcel.exportExcel()
    },

    // 处理表格打卡结果数据
    getStatus(status, tip, time) {
      let str = ''
      let tips = ''
      if (status == 1) {
        str = '正常'
      } else if (status == 0) {
        str = '--'
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

      if (time !== 0) {
        str = str + tips + '-' + time + '分钟'
      } else {
        str = str + tips
      }
      return str
    },
    pageChange(page) {
      this.where.page = page
      this.getList()
    },
    handleSizeChange(val) {
      this.where.limit = val
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
::v-deep .el-table thead.is-group th {
  background-color: rgba(247, 251, 255, 1);
  border-color: #fff;
}
.el-icon-d-arrow-right {
  margin: 0 12px;
}

::v-deep .el-table td {
  border: none;
}
::v-deep .el-table {
  border: none;
}
.red {
  cursor: pointer;
  color: red;
}
</style>
