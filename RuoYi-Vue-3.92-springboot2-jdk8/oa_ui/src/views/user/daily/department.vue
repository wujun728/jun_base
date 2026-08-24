<template>
  <!-- 部门汇报页面 -->
  <div class="divBox">
    <el-card class="employees-card-bottom" body-style="padding: 20px 20px 0 20px;">
      <el-form :inline="true" class="from-s">
        <div class="flex-row flex-col">
          <div>
            <el-tabs v-model="activeVal" class="tabs" @tab-click="activeClick">
              <el-tab-pane label="部门汇报" name="3" />
              <el-tab-pane label="部门统计" name="4" />
              <el-tab-pane label="抄送我的" name="5" />
            </el-tabs>
          </div>
        </div>
      </el-form>
      <div class="splitLine"></div>
      <div v-if="activeVal == '3'" class="card-box mt10">
        <oaFromBox
          :isAddBtn="false"
          :isViewSearch="false"
          :search="search"
          :total="totalPage"
          @confirmData="confirmData"
        >
        </oaFromBox>
        <!-- 我的汇报/汇报记录列表 -->
        <div v-loading="loading" class="mt-10">
          <el-table ref="elTable" :data="tableData" :height="tableHeight">
            <el-table-column :label="$t('toptable.name')" prop="name" width="90" />
            <el-table-column :label="$t('toptable.department')" min-width="100" prop="frame_name" />
            <el-table-column :label="$t('toptable.worktoday')" min-width="250" prop="finish">
              <template slot-scope="scope">
                <div v-for="(item, index) in scope.row.finish" :key="index" class="textover3">{{ item }}</div>
              </template>
            </el-table-column>
            <el-table-column :label="$t('toptable.tomorrowplan')" min-width="250" prop="plan">
              <template slot-scope="scope">
                <div v-if="scope.row.types != 3">
                  <div v-for="(item, index) in scope.row.plan" :key="index" class="textover3">{{ item }}</div>
                </div>
                <span v-else>--</span>
              </template>
            </el-table-column>

            <el-table-column :label="$t('user.work.dailytype')" width="120">
              <template slot-scope="scope">
                {{ getDailyTypes(scope.row.types) }}
              </template>
            </el-table-column>

            <el-table-column :label="$t('user.work.creationtime')" prop="created_at" width="160" />

            <el-table-column
              :label="$t('public.operation')"
              :show-overflow-tooltip="true"
              :width="tableFrom.type == 0 ? 160 : 60"
              fixed="right"
            >
              <template slot-scope="scope">
                <el-button
                  v-hasPermi="['user:daily:department:check']"
                  type="text"
                  @click="oncheck(scope.row.daily_id, 'check', scope.row)"
                >
                  {{ $t('public.check') }}
                </el-button>

                <el-button
                  v-if="tableFrom.type == 0 && new Date(scope.row.end_time).getTime() > new Date().getTime()"
                  :disabled="scope.row.reply !== ''"
                  type="text"
                  @click="onEditt(scope.row.daily_id, 'edit', scope.row)"
                >
                  {{ $t('public.edit') }}
                </el-button>
                <el-button
                  v-if="tableFrom.type == 0 && new Date(scope.row.end_time).getTime() > new Date().getTime()"
                  :disabled="scope.row.reply !== ''"
                  type="text"
                  @click="onDelete(scope.row.daily_id)"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="page-fixed">
            <el-pagination
              :current-page="tableFrom.page"
              :page-size="tableFrom.limit"
              :page-sizes="[15, 20, 30]"
              :total="totalPage"
              layout="total,sizes, prev, pager, next, jumper"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
            />
          </div>
        </div>
      </div>

      <!-- 汇报统计页面 -->
      <el-row v-if="activeVal == '4'" class="flexBox">
        <el-col :span="14" class="left">
          <div class="title">
            <oaFromBox
              :isAddBtn="false"
              :isTotal="false"
              :isViewSearch="false"
              :search="search1"
              :sortSearch="false"
              @confirmData="confirmData"
            >
            </oaFromBox>
            <div class="title-text">
              <span class="tips1">{{ thePreviousDay }} 填写情况</span>
              <span class="tips2">每日 00:00 ～ 23:59 统计</span>
            </div>
          </div>
          <div class="classification">
            <div class="content">
              <span class="num">{{ total || 0 }}</span>
              <span class="text">提交结果</span>
            </div>
            <div class="content">
              <span class="num color1">{{ submit || 0 }}</span>
              <span class="text color1">已提交</span>
            </div>
            <div class="content">
              <span class="num color2">{{ no_submit || 0 }}</span>
              <span class="text color2">未提交</span>
            </div>
          </div>
          <el-tabs v-model="submitType" class="mt14 tabs" @tab-click="submitTypeFn">
            <el-tab-pane label="已提交" name="1" />
            <el-tab-pane label="未提交" name="2" />
          </el-tabs>
          <div class="inTotal">共 {{ totalSubmit }} 条</div>

          <el-table ref="elTable" :data="table" :height="tableHeight">
            <el-table-column label="姓名" prop="name" />
            <el-table-column label="部门" prop="frame_name" />
            <el-table-column label="汇报类型" prop="types">
              <template slot-scope="scope">
                {{ getDailyTypes(scope.row.types) }}
              </template></el-table-column
            >
            <el-table-column v-if="submitType == '1'" label="提交时间" prop="created_at" />
            <el-table-column v-if="submitType == '1'" label="操作" prop="" width="80">
              <template slot-scope="scope">
                <el-button type="text" @click="oncheck(scope.row.daily_id, 'check', scope.row)">
                  {{ $t('public.check') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination
            :current-page="submitFrom.page"
            :page-size="submitFrom.limit"
            :page-sizes="[15, 20, 30]"
            :total="totalSubmit"
            layout="total,sizes, prev, pager, next, jumper"
            @size-change="handleLimit"
            @current-change="handleChange"
          />
        </el-col>
        <el-col :span="10" class="right">
          <!-- 右边日历 -->
          <calendar
            v-if="tableFrom.types !== 2"
            ref="week"
            :activeVal="activeVal"
            :calendar="calendar"
            :type="tableFrom.types"
            @currentTime="currentTimeFn"
            @switchTime="switchTime"
          >
          </calendar>
          <!-- 汇报类型为月 -->
          <div v-else>
            <div class="month">
              <i class="el-icon-arrow-left" @click="leftFn"></i> {{ year }}年
              <i class="el-icon-arrow-right" @click="rightFn"></i>
            </div>
            <div class="monthBox">
              <div v-for="(item, index) in monthList" :key="index" @click="clickMonth(item, index)">
                <span :class="active == index ? 'active-add' : ''" class="box"
                  >{{ item.name }} <span v-if="getCalendarDay(item.time)" class="iconfont iconjindu-qita"></span
                ></span>
              </div>
            </div>
            <div class="tips"><span class="iconfont icontishi2" /> 日历中的红点表示有下属未提交月报</div>
          </div>
        </el-col>
      </el-row>
      <div v-if="activeVal == '5'" class="mt10">
        <oaFromBox
          :isAddBtn="false"
          :isViewSearch="false"
          :search="search3"
          :total="totalPage"
          @confirmData="confirmData"
        >
        </oaFromBox>
        <!-- 抄送我的列表 -->
        <div v-loading="loading" class="mt-10">
          <el-table ref="elTable" :data="tableData" :height="tableHeight">
            <el-table-column :label="$t('toptable.name')" prop="name" width="90" />
            <el-table-column :label="$t('toptable.department')" min-width="100" prop="frame_name" />
            <el-table-column :label="$t('toptable.worktoday')" min-width="250" prop="finish">
              <template slot-scope="scope">
                <div v-for="(item, index) in scope.row.finish" :key="index" class="textover3">{{ item }}</div>
              </template>
            </el-table-column>
            <el-table-column :label="$t('toptable.tomorrowplan')" min-width="250" prop="plan">
              <template slot-scope="scope">
                <div v-if="scope.row.types != 3">
                  <div v-for="(item, index) in scope.row.plan" :key="index" class="textover3">{{ item }}</div>
                </div>
                <span v-else>--</span>
              </template>
            </el-table-column>

            <el-table-column :label="$t('user.work.dailytype')" width="120">
              <template slot-scope="scope">
                {{ getDailyTypes(scope.row.types) }}
              </template>
            </el-table-column>

            <el-table-column :label="$t('user.work.creationtime')" prop="created_at" width="160" />
            <el-table-column
              :label="$t('public.operation')"
              :show-overflow-tooltip="true"
              :width="tableFrom.type == 0 ? 160 : 60"
              fixed="right"
            >
              <template slot-scope="scope">
                <el-button
                  v-hasPermi="['user:daily:department:check']"
                  type="text"
                  @click="oncheck(scope.row.daily_id, 'check', scope.row)"
                >
                  {{ $t('public.check') }}
                </el-button>

                <el-button
                  v-if="tableFrom.type == 0 && new Date(scope.row.end_time).getTime() > new Date().getTime()"
                  :disabled="scope.row.reply !== ''"
                  type="text"
                  @click="onEditt(scope.row.daily_id, false, scope.row)"
                >
                  {{ $t('public.edit') }}
                </el-button>
                <el-button
                  v-if="tableFrom.type == 0 && new Date(scope.row.end_time).getTime() > new Date().getTime()"
                  :disabled="scope.row.reply !== ''"
                  type="text"
                  @click="onDelete(scope.row.daily_id)"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <div class="page-fixed">
          <el-pagination
            :current-page="tableFrom.page"
            :page-size="tableFrom.limit"
            :page-sizes="[15, 20, 30]"
            :total="totalPage"
            layout="total,sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </div>
    </el-card>

    <addBox ref="addBox" :daily-id="dailyIndex" :edit-data="editData" :edit-id="editId" @tableList="tableList" />
  </div>
</template>

<script>
import { getEnterpriseDaily, getEnterpriseUsersApi, dailyReportListApi } from '@/api/enterprise'
import { deleteDailyApi, statisticsApi, submitStatisticsApi, submitListApi, noSubmitListApi } from '@/api/user'
import { reportTreeApi } from '@/api/public'
export default {
  name: 'IndexVue',
  components: {
    addBox: () => import('./components/addBox'),
    manageRange: () => import('@/components/form-common/select-manageRange'),
    calendar: () => import('./components/calendar'),
    oaFromBox: () => import('@/components/common/oaFromBox')
  },
  data() {
    return {
      currentTime: '',
      activeVal: '3',
      submitType: '1',
      value: new Date(),
      active: false,
      userList: [],
      submitFrom: {
        page: 1,
        limit: 15
      },
      totalSubmit: 0,
      fromList: [
        { text: this.$t('toptable.all'), val: '' },
        { text: this.$t('toptable.thisweek'), val: 'week' },
        { text: this.$t('toptable.thismonth'), val: 'month' },
        { text: this.$t('toptable.lastmonth'), val: 'last month' }
      ],
      dailyData: [
        { name: this.$t('user.work.writedaily'), id: 1 },
        { name: this.$t('user.work.writedailyweek'), id: 2 },
        { name: this.$t('user.work.writedailymonth'), id: 3 },
        { name: '填写汇报', id: 4 }
      ],
      options: [
        { name: this.$t('user.work.dailyday'), id: 0 },
        { name: this.$t('user.work.weekday'), id: 1 },
        { name: this.$t('user.work.monthday'), id: 2 },
        { name: '汇报', id: 3 }
      ],
      search: [
        {
          field_name: '人员',
          field_name_en: 'user_id',
          form_value: 'user_id',
          data_dict: []
        },
        {
          field_name: '汇报类型',
          field_name_en: 'types',
          form_value: 'select',
          data_dict: [
            { name: this.$t('user.work.dailyday'), id: 0 },
            { name: this.$t('user.work.weekday'), id: 1 },
            { name: this.$t('user.work.monthday'), id: 2 },
            { name: '汇报', id: 3 }
          ],
          value: 0
        },
        {
          field_name: '管理范围',
          field_name_en: 'scope_frame',
          form_value: 'manage',
          data_dict: []
        },

        {
          field_name: '开始时间',
          field_name_end: '结束时间',
          field_name_en: 'time',
          form_value: 'date_picker',
          data_dict: [
            this.$moment().startOf('month').format('YYYY/MM/DD'),
            this.$moment().endOf('month').format('YYYY/MM/DD')
          ]
        }
      ],
      search1: [
        {
          field_name: '汇报类型',
          field_name_en: 'types',
          form_value: 'select',
          data_dict: [
            { name: this.$t('user.work.dailyday'), id: 0 },
            { name: this.$t('user.work.weekday'), id: 1 },
            { name: this.$t('user.work.monthday'), id: 2 },
            { name: '汇报', id: 3 }
          ],
          value: 0
        },
        {
          field_name: '管理范围',
          field_name_en: 'scope_frame',
          form_value: 'manage',
          data_dict: []
        }
      ],
      search3: [
        {
          field_name: '人员',
          field_name_en: 'user_id',
          form_value: 'user_id',
          data_dict: []
        },
        {
          field_name: '汇报类型',
          field_name_en: 'types',
          form_value: 'select',
          data_dict: [
            { name: this.$t('user.work.dailyday'), id: 0 },
            { name: this.$t('user.work.weekday'), id: 1 },
            { name: this.$t('user.work.monthday'), id: 2 },
            { name: '汇报', id: 3 }
          ],
          value: 0
        },
        {
          field_name: '开始时间',
          field_name_end: '结束时间',
          field_name_en: 'time',
          form_value: 'date_picker',
          data_dict: [
            this.$moment().startOf('month').format('YYYY/MM/DD'),
            this.$moment().endOf('month').format('YYYY/MM/DD')
          ]
        }
      ],
      reportData: [],
      monthList: [
        {
          name: '一月',
          time: '01'
        },
        {
          name: '二月',
          time: '02'
        },
        {
          name: '三月',
          time: '03'
        },
        {
          name: '四月',
          time: '04'
        },
        {
          name: '五月',
          time: '05'
        },
        {
          name: '六月',
          time: '06'
        },
        {
          name: '七月',
          time: '07'
        },
        {
          name: '八月',
          time: '08'
        },
        {
          name: '九月',
          time: '09'
        },
        {
          name: '十月',
          time: '10'
        },
        {
          name: '十一月',
          time: '11'
        },
        {
          name: '十二月',
          time: '12'
        }
      ],
      weekInfo: '',
      dailyName: '',
      dailyIndex: 1,
      totalPage: 0,
      timeVal: [
        this.$moment().startOf('month').format('YYYY/MM/DD'),
        this.$moment().endOf('month').format('YYYY/MM/DD')
      ],
      year: '',
      tableFrom: {
        page: 1,
        limit: 15,
        type: 1,
        types: 0,
        time:
          this.$moment().startOf('month').format('YYYY/MM/DD') +
          '-' +
          this.$moment().endOf('month').format('YYYY/MM/DD'),
        user_id: '',
        scope_frame: 'all',
        sort_value: '',
        sort_field: ''
      },
      total: '',
      submit: '',
      no_submit: '',
      pickerOptions: this.$pickerOptionsTimeEle,
      tableData: [],
      loading: false,
      departmentalData: [],
      editId: 0,
      editData: {},
      nowDateStar: null, // 今天时间0点时间戳
      nowDateEnd: null, // 今天时间23:59:59 时间戳
      leftTime:
        this.$moment().subtract(1, 'day').format('YYYY/MM/DD') +
        '-' +
        this.$moment().subtract(1, 'day').format('YYYY/MM/DD'),
      calendarTime: '',
      usersOption: [],
      calendar: [],
      thePreviousDay: '',
      table: [],
      currentWeek: null,
      windowHeight: 0
    }
  },
  computed: {
    lang: function () {
      return this.$store.getters.lang
    }
  },
  watch: {
    lang() {
      this.setOptions()
    }
  },
  created() {
    this.windowHeight = window.innerHeight - 330
  },
  activated() {
    this.$refs.elTable.doLayout()
  },
  mounted() {
    this.getDate()
    this.getReportTreeApi()
    this.nowDateStar = parseInt(this.$moment(new Date()).startOf('day').format('x'))
    this.nowDateEnd = parseInt(this.$moment(new Date()).endOf('day').format('x'))
    this.dailyName = this.dailyData[this.dailyIndex - 1].name
    this.getList()
    this.getUsersOption()
  },
  methods: {
    confirmData(data) {
      this.tableFrom.page = 1
      this.submitFrom.page = 1
      // 判断data是个字符串并等于reset
      if (data == 'reset') {
        this.submitType = '1'
        this.tableFrom = {
          page: 1,
          limit: 15,
          type: 1,
          types: 0,
          time:
            this.$moment().startOf('month').format('YYYY/MM/DD') +
            '-' +
            this.$moment().endOf('month').format('YYYY/MM/DD'),
          user_id: '',
          scope_frame: 'all',
          sort_value: '',
          sort_field: ''
        }
        this.timeVal = [
          this.$moment().startOf('month').format('YYYY/MM/DD'),
          this.$moment().endOf('month').format('YYYY/MM/DD')
        ]
        this.submitFrom = {
          page: 1,
          limit: 15
        }
        this.search[3].data_dict = this.timeVal
        this.search3[2].data_dict = this.timeVal
      } else {
        this.tableFrom = { ...this.tableFrom, ...data }
      }

      this.activeClick()
    },
    // 获取日期
    getDate() {
      this.currentTime = this.$moment().subtract('day').format('YYYY-MM-DD') // 当天日期
      this.thePreviousDay = this.$moment().subtract(1, 'day').format('YYYY-MM-DD') // 前一天日期
      this.thePreviousDay = this.$moment(this.thePreviousDay).format('YYYY年MM月DD日') // 格式化日期
    },
    usersOptionFn() {
      if (this.activeVal == 5) {
        this.dailyReportList()
      } else {
        this.getList()
      }
    },

    cardTag(index) {
      this.userList.splice(index, 1)
      this.tableFrom.user_id = ''
      this.usersOptionFn()
    },
    getPeriodText(id) {
      const txt = Common.getPeriodText(id)
      return txt
    },
    getStatusText(id) {
      const txt = Common.getStatusText(id)
      return txt
    },

    // 点击日历日期的回调
    currentTimeFn(data) {
      this.thePreviousDay = this.$moment(data).format('YYYY年MM月DD日')
      if (this.activeVal == '4' && (this.tableFrom.types == 0 || this.tableFrom.types == 3)) {
        this.leftTime = `${this.$moment(data).format('YYYY/MM/DD')}-${this.$moment(data).format('YYYY/MM/DD')}`
        this.getstatisticsApi()
        if (this.submitType == '1') {
          this.submitList()
        } else {
          this.noSubmitList()
        }
      } else if (this.activeVal == '4' && this.tableFrom.types == 1) {
        this.getLastWeek(data)
      }
    },

    // 点击日历周日报
    getLastWeek(i) {
      const curWeek = this.$moment(i, 'YYYY-MM-DD').format('W')
      let weekOfday = this.$moment(i, 'YYYY-MM-DD').format('E')
      let last_monday = this.$moment(i)
        .subtract(weekOfday - 1, 'days')
        .format('YYYY/MM/DD') //周一日期

      let last_sunday = this.$moment(i)
        .add(7 - weekOfday, 'days')
        .format('YYYY/MM/DD') //周日日期
      this.thePreviousDay =
        this.$moment(last_monday, 'YYYY/MM/DD').format('YYYY年MM月DD日') +
        '~' +
        this.$moment(last_sunday, 'YYYY/MM/DD').format('YYYY年MM月DD日')
      this.leftTime = last_monday + '-' + last_sunday

      // 处理在相同周内只调用一次接口逻辑
      if (this.currentWeek && this.currentWeek === curWeek) return false
      this.getstatisticsApi()

      this.currentWeek = curWeek
      if (this.submitType == '1') {
        this.submitList()
      } else {
        this.noSubmitList()
      }
    },

    // 获取统计数据提交/未提交
    async getstatisticsApi() {
      let data = {
        time: this.leftTime,
        types: this.tableFrom.types,
        scope_frame: this.tableFrom.scope_frame
      }
      const result = await statisticsApi(data)
      this.total = result.data.total
      this.submit = result.data.submit
      this.no_submit = result.data.no_submit
    },

    // 点击日历月报
    clickMonth(data, index) {
      this.active = index
      this.thePreviousDay = this.year + '年' + data.time + '月'
      const f = this.$moment(this.thePreviousDay, 'YYYY-MM').startOf('month').format('YYYY/MM/DD')
      const e = this.$moment(this.thePreviousDay, 'YYYY-MM').endOf('month').format('YYYY/MM/DD')
      this.leftTime = `${f}-${e}`
      this.getstatisticsApi()
      if (this.submitType == '1') {
        this.submitList()
      } else {
        this.noSubmitList()
      }
    },

    // 获取部门下拉数据
    async getReportTreeApi() {
      const result = await reportTreeApi()
      this.departmentalData = result.data
    },

    // 汇报类型切换
    onChangeTypes() {
      let weekOfday = ''
      let last_monday = ''
      let last_sunday = ''

      if (this.activeVal == '4' && this.tableFrom.types == 2) {
        // 选择月报
        let date = new Date()
        this.year = date.getFullYear()
        this.thePreviousDay = this.$moment().startOf('month').format('YYYY年MM月')
        this.calendarTime = this.year
        this.active = date.getMonth()
        this.leftTime =
          this.$moment().startOf('month').format('YYYY/MM/DD') +
          '-' +
          this.$moment().endOf('month').format('YYYY/MM/DD')
      } else if (this.activeVal == '4' && (this.tableFrom.types == 0 || this.tableFrom.types == 3)) {
        // 选择日报
        this.thePreviousDay = this.$moment().subtract(1, 'day').format('YYYY年MM月DD日')
        this.leftTime =
          this.$moment().subtract(1, 'day').format('YYYY/MM/DD') +
          '-' +
          this.$moment().subtract(1, 'day').format('YYYY/MM/DD')
        setTimeout(() => {
          this.$refs.week.removeStyle()
          this.$refs.week.value = new Date(new Date().getTime() - 24 * 60 * 60 * 1000)
        }, 300)
      } else if (this.activeVal == '4' && this.tableFrom.types == 1) {
        // 选择周报
        this.removeStyle()
        weekOfday = this.$moment().format('E')
        last_monday = this.$moment()
          .subtract(weekOfday - 1, 'days')
          .format('YYYY/MM/DD')

        last_sunday = this.$moment()
          .add(7 - weekOfday, 'days')
          .format('YYYY/MM/DD')
        this.thePreviousDay =
          this.$moment(last_monday, 'YYYY/MM/DD').format('YYYY年MM月DD日') +
          '~' +
          this.$moment(last_sunday, 'YYYY/MM/DD').format('YYYY年MM月DD日')
        this.leftTime = last_monday + '-' + last_sunday
        setTimeout(() => {
          this.$refs.week.addIndex()
        }, 200)
      }
      this.selectTypes()
    },
    leftFn() {
      this.year = this.year - 1
      this.calendarTime = this.year
      this.submitStatisticsApi()
    },
    rightFn() {
      this.year = this.year + 1
      this.calendarTime = this.year
      this.submitStatisticsApi()
    },
    getCalendarDay(day) {
      let len = this.calendar.length
      let str = false
      if (len > 0) {
        for (let i = 0; i < len; i++) {
          if (this.calendar[i].time.substring(5) === day) {
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

    // 获取未提交的日历
    async submitStatisticsApi() {
      let data = {
        time: this.calendarTime,
        types: this.tableFrom.types,
        scope_frame: this.tableFrom.scope_frame,
        type: this.tableFrom.type == 0 ? '1' : '0'
      }
      const result = await submitStatisticsApi(data)
      this.calendar = result.data
    },

    // 切换日历月份触发的回调
    switchTime(data) {
      this.calendarTime =
        this.$moment(data.startTime).format('YYYY/MM/DD') + '-' + this.$moment(data.endTime).format('YYYY/MM/DD')
      this.submitStatisticsApi()
    },

    setOptions() {
      this.fromList = [
        { text: this.$t('toptable.all'), val: '' },
        { text: this.$t('toptable.thisweek'), val: 'week' },
        { text: this.$t('toptable.thismonth'), val: 'month' },
        { text: this.$t('toptable.lastmonth'), val: 'lastmonth' }
      ]
    },

    async getList() {
      this.loading = true
      const result = await getEnterpriseDaily(this.tableFrom)
      const data = result.data
      if (data.list.length) {
        data.list.forEach((el) => {
          el.isEdit = true
        })
      }
      this.totalPage = data.count
      this.loading = false
      this.tableData = data.list
    },

    async dailyReportList() {
      const result = await dailyReportListApi(this.tableFrom)
      const data = result.data
      if (data.list.length) {
        data.list.forEach((el) => {
          el.isEdit = true
        })
      }
      this.totalPage = data.count
      this.tableData = data.list
    },
    async getUsersOption() {
      const result = await getEnterpriseUsersApi()
      this.usersOption = result.data ? result.data : []
    },
    tableList() {
      this.getList()
    },
    oncheck(id, type, data) {
      this.editId = id
      this.editData = data
      this.$refs.addBox.openBox(id, type, data)
    },
    onEditt(id, type, data) {
      this.editId = id
      this.$refs.addBox.openBox(id, type, data)
    },

    // 删除当日当周当月日报
    onDelete(id) {
      this.$modalSure('确认删除当前汇报').then(() => {
        deleteDailyApi(id).then((res) => {
          this.getList()
        })
      })
    },
    // 添加日报
    addDaily(val) {
      this.editId = 0
      setTimeout(() => {
        this.$refs.addBox.getCompletedFn()
      }, 200)
      this.$refs.addBox.drawer = true
      this.$refs.addBox.editType = false
    },
    handleClick() {
      this.tableFrom.page = 1
      this.tableFrom.user_id = ''
      if (this.tableFrom.type == '0') {
        this.activeVal = '3'
      }
      this.getList()
    },

    // 获取提交的汇报表格数据
    async submitList() {
      let data = {
        time: this.leftTime,
        types: this.tableFrom.types,
        scope_frame: this.tableFrom.scope_frame,
        page: this.submitFrom.page,
        limit: this.submitFrom.limit
      }
      const result = await submitListApi(data)
      this.totalSubmit = result.data.count
      this.table = result.data.list
    },
    // 获取未提交的汇报表格数据
    async noSubmitList() {
      let data = {
        time: this.leftTime,
        types: this.tableFrom.types,
        scope_frame: this.tableFrom.scope_frame,
        page: this.submitFrom.page,
        limit: this.submitFrom.limit
      }
      const result = await noSubmitListApi(data)
      this.totalSubmit = result.data.count
      this.table = result.data.list
    },
    // 二级tabs页切换
    activeClick() {
      if (this.activeVal == '4') {
        this.thePreviousDay = this.$moment().subtract(1, 'day').format('YYYY年MM月DD日')
        this.leftTime =
          this.$moment().subtract(1, 'day').format('YYYY/MM/DD') +
          '-' +
          this.$moment().subtract(1, 'day').format('YYYY/MM/DD')
        this.submitTypeFn()
        // this.submitList()
        // this.getstatisticsApi()
        // this.submitStatisticsApi()
      }
      if (this.activeVal == '5') {
        this.tableFrom.page = 1
        this.dailyReportList()
      } else if (this.activeVal == '3') {
        this.submitType = '1'
        this.tableFrom.page = 1
        this.getList()
      }
    },

    submitTypeFn() {
      if (this.submitType == '1') {
        this.reset()
        this.submitList()
      } else {
        this.reset()
        this.noSubmitList()
      }
    },

    // 搜索
    selectTypes(e) {
      this.tableFrom.page = 1
      this.tableFrom.scope_frame = e
      if (this.activeVal == '4') {
        this.getstatisticsApi()
        if (this.submitType == '1') {
          this.submitList()
        } else {
          this.noSubmitList()
        }
      }
      if (this.activeVal == '5') {
        this.dailyReportList()
        this.tableFrom.page = 1
      }
      if (this.activeVal == '3') {
        this.submitStatisticsApi()
        this.getList()
      }
    },

    // 清空
    reset() {
      this.userList = []
      this.timeVal = [
        this.$moment().startOf('month').format('YYYY/MM/DD'),
        this.$moment().endOf('month').format('YYYY/MM/DD')
      ]
      this.calendarTime =
        this.$moment().subtract(1, 'day').format('YYYY/MM/DD') +
        '-' +
        this.$moment().subtract(1, 'day').format('YYYY/MM/DD')

      if (this.$refs.week) {
        this.$refs.week.removeStyle()
        this.$refs.week.value = new Date(new Date().getTime() - 24 * 60 * 60 * 1000)
      }

      this.leftTime =
        this.$moment().subtract(1, 'day').format('YYYY/MM/DD') +
        '-' +
        this.$moment().subtract(1, 'day').format('YYYY/MM/DD')
      this.thePreviousDay = this.$moment().subtract(1, 'day').format('YYYY年MM月DD日')
      // this.tableFrom.types = 0
      this.tableFrom.time =
        this.$moment().startOf('month').format('YYYY/MM/DD') + '-' + this.$moment().endOf('month').format('YYYY/MM/DD')

      this.tableFrom.user_id = ''
      this.tableFrom.scope_frame = 'all'
      this.tableFrom.page = 1

      if (this.activeVal == '4') {
        this.submitFrom.page = 1
        this.submitStatisticsApi()
        this.getstatisticsApi()
        if (this.submitType == '1') {
          this.submitList()
        } else {
          this.noSubmitList()
        }
      } else if (this.activeVal == '5') {
        this.tableFrom.page = 1
        this.dailyReportList()
      } else {
        this.tableFrom.page = 1
        this.getList()
      }
    },

    removeStyle() {
      if (this.tableFrom.types == '1') {
        setTimeout(() => {
          this.$refs.week.setWeek()
        }, 300)
      }
    },

    onchangeTime() {
      this.tableFrom.page = 1
      if (this.timeVal) {
        this.tableFrom.time = this.timeVal[0] ? this.timeVal.join('-') : ''
      } else {
        this.tableFrom.time = ''
      }
      if (this.activeVal == '5') {
        this.dailyReportList()
      } else {
        this.getList()
      }
    },

    getDailyTypes(type) {
      var str = ''
      this.options.map((value) => {
        if (value.id == type) {
          str = value.name
        }
      })
      return str
    },

    handleSizeChange(val) {
      this.tableFrom.page = 1
      this.tableFrom.limit = val
      if (this.activeVal == '5') {
        this.dailyReportList()
      } else {
        this.getList()
      }
    },
    handleCurrentChange(page) {
      this.tableFrom.page = page
      if (this.activeVal == '5') {
        this.dailyReportList()
      } else {
        this.getList()
      }
    },
    // 统计日报的分页
    handleLimit(val) {
      this.submitFrom.page = 1
      this.submitFrom.limit = val
      if (this.submitType == '1') {
        this.submitList()
      } else {
        this.noSubmitList()
      }
    },
    // 统计日报的分页
    handleChange(page) {
      this.submitFrom.page = page
      if (this.submitType == '1') {
        this.submitList()
      } else {
        this.noSubmitList()
      }
    },

    handleDaily(command) {
      this.dailyIndex = command
      this.dailyName = this.dailyData[command - 1].name
      this.addDaily('fillInTheReport')
    }
  }
}
</script>

<style lang="scss" scoped>
.plan-footer-one {
  height: 26px;
  line-height: 28px;
}
.flex-box {
  span {
    margin-right: 6px;
  }
  span:last-of-type {
    margin-right: 0;
  }
}

.formFlex {
  float: right;
  clear: both;
}
::v-deep .el-table th {
  background-color: #f7fbff;
}

::v-deep .el-form-item {
  margin-bottom: 0px;
}

.popoverWidth {
  width: 316px;
  height: 350px;
}
.tips {
  display: flex;
  align-items: center;
  font-size: 13px;
  margin-left: 15px;
  font-family: PingFang SC-常规体, PingFang SC;
  font-weight: normal;
  color: #606266;
  .icontishi2 {
    color: #1890ff;
    margin-right: 4px;
  }
}

.search {
  width: 100%;
  height: 50px;
  background: #f7fbff;
  padding: 0 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  .el-btn {
    width: 54px;
    height: 32px;
    font-size: 13px;
  }
}
.color1 {
  color: #1890ff !important;
}
.color2 {
  color: #ed4014 !important;
}
.splitLine {
}
.btn {
  margin-left: 24px;
  font-size: 13px;
  .iconrili3 {
    font-size: 13px;
    margin-right: 4px;
  }
}
.textover3 {
  white-space: pre-wrap;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 10;
  overflow: hidden;
  line-height: 1.5;
}

::v-deep .el-dropdown-menu__item {
  text-align: left;
  i {
    color: #00c050;
  }
}
::v-deep .el-dropdown__caret-button {
  height: 32px;
}

::v-deep .el-tabs__header {
  margin-bottom: 0;
}
::v-deep .el-tabs__nav-wrap::after {
  display: none;
}

.active-add {
  background-color: #f2f8fe;
}

.flexBox {
  display: flex;
  height: 100%;
  .right {
    max-width: 400px;
    padding: 20px 0px 20px 0;
    .month {
      margin-top: 25px;
      display: flex;
      justify-content: space-between;
      font-size: 14px;
      font-family: PingFang SC-中黑体, PingFang SC;
      font-weight: 600;
      color: #303133;
      .el-icon-arrow-left {
        cursor: pointer;
      }
      .el-icon-arrow-right {
        cursor: pointer;
      }
    }
    .monthBox {
      margin: 0 auto;
      margin-top: 50px;
      display: flex;
      flex-wrap: wrap;
      .box {
        cursor: pointer;
        width: 50px;
        height: 35px;
        line-height: 35px;
        display: inline-block;
      }
      .box:hover {
        width: 50px;
        height: 35px;
        line-height: 35px;
        display: inline-block;
        background-color: #f2f8fe;
      }

      div {
        width: 24%;
        font-size: 14px;
        font-family: PingFang SC-中黑体, PingFang SC;

        color: #303133;
        text-align: center;
        margin-bottom: 50px;
        span {
          position: relative;
          .iconjindu-qita {
            position: absolute;
            top: -5px;
            right: -5px;

            -webkit-transform: scale(0.3);
            border-radius: 100%;
            color: #ed4014;
          }
        }
      }
      div:not(:nth-child(4n)) {
        margin-right: calc(4% / 3);
      }
    }

    ::v-deep .el-calendar__header {
      border: none;
      margin-bottom: 30px;
    }
  }
  .card {
    min-height: calc(100vh - 80px);
  }
  .left {
    height: calc(100vh - 150px);
    flex: 1;
    margin: 0px;
    padding-top: 20px;
    padding-right: 20px;
    border-right: 1px solid #eff2f4;

    .inTotal {
      margin-bottom: 10px;
    }
    // height: 594px;
    .title {
      display: flex;
      justify-content: space-between;
      align-items: end;
      margin-top: -11px;
      .tips1 {
        font-size: 14px;
        font-family: PingFang SC-中黑体, PingFang SC;
        font-weight: 600;
        color: #303133;
      }
      .tips2 {
        font-size: 12px;
        font-family: PingFang SC-常规体, PingFang SC;
        font-weight: normal;
        color: #909399;
        margin-left: 10px;
      }
    }
    .classification {
      padding: 0 80px;
      display: flex;
      align-items: center;
      justify-content: space-around;
      margin: 10px 0 30px 0;
      height: 80px;
      background: #f7fbff;
      border-radius: 4px 4px 4px 4px;
      .content {
        width: 140px;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        .num {
          font-size: 20px;
          font-family: PingFang SC-中黑体, PingFang SC;
          font-weight: 600;
          color: #303133;
        }
        .text {
          margin-top: 5px;
          font-size: 13px;
          font-family: PingFang SC-常规体, PingFang SC;
          font-weight: normal;
          color: #606266;
        }
      }
    }
    .line {
      height: 1px;
      background: #f0f2f5;
      border-radius: 4px 4px 4px 4px;
    }
  }
}
.divBox .el-pagination {
  margin-top: 30px;
  padding-bottom: 10px;
}
</style>
