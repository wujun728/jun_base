<template>
  <div class="divBox">
    <el-card class="normal-page">
      <oaFromBox
        v-if="search.length > 0"
        :btnText="`导出`"
        :isAddBtn="true"
        :btnIcon="false"
        :isViewSearch="false"
        :search="search"
        :title="`汇报管理`"
        :total="totalPage"
        @addDataFn="getExportData"
        @confirmData="confirmData"
      ></oaFromBox>

      <div class="mt10 table-box">
        <el-table :data="tableData" :height="tableHeight">
          <el-table-column :label="$t('toptable.name')" prop="name" width="100" />
          <el-table-column :label="$t('toptable.department')" prop="frame_name" width="120" />
          <el-table-column :label="$t('toptable.worktoday')" min-width="250" prop="finish">
            <template slot-scope="scope">
              <div v-for="(item, index) in scope.row.finish" :key="index" class="textover3">{{ item }}</div>
            </template>
          </el-table-column>
          <el-table-column :label="$t('toptable.tomorrowplan')" min-width="250" prop="plan">
            <template slot-scope="scope">
              <div v-if="scope.row.types !== 3">
                <div v-for="(item, index) in scope.row.plan" :key="index" class="textover3">{{ item }}</div>
              </div>
              <div v-else>--</div>
            </template>
          </el-table-column>

          <el-table-column :label="$t('user.work.dailytype')" width="100">
            <template #default="{ row }">
              <span v-if="row.types === 1">周报</span>
              <span v-else-if="row.types === 2">月报</span>
              <span v-else-if="row.types === 3">汇报</span>
              <span v-else>日报</span>
            </template>
          </el-table-column>
          <el-table-column :label="$t('toptable.updatetime')" prop="updated_at" width="160" />
          <el-table-column :label="$t('user.work.creationtime')" prop="created_at" width="160" />
          <el-table-column :label="$t('public.operation')" fixed="right" width="100">
            <template slot-scope="scope">
              <el-button
                v-hasPermi="['hr:report:check']"
                size="small"
                type="text"
                @click="onCheck(scope.row.daily_id, 'check', scope.row)"
              >
                {{ $t('public.check') }}
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
            layout="total, sizes,prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </div>
    </el-card>

    <addBox ref="addBox" :daily-id="dailyIndex" :edit-data="editData" :edit-id="editId" @tableList="tableList" />
    <export-excel ref="exportExcel" :export-data="exportData" :save-name="saveName" :template="false" />
  </div>
</template>

<script>
import { enterpriseDailyApi, getEnterpriseUsersApi, enterpriseDailyExportApi } from '@/api/enterprise'
import oaFromBox from '@/components/common/oaFromBox'
export default {
  name: 'IndexVue',
  components: {
    addBox: () => import('@/views/user/daily/components/addBox'),
    exportExcel: () => import('@/components/common/exportExcel'),
    oaFromBox
  },
  data() {
    return {
      // 搜索条件列表
      fromList: [
        { text: this.$t('toptable.all'), val: '' },
        { text: this.$t('toptable.thisweek'), val: 'week' },
        { text: this.$t('toptable.thismonth'), val: 'month' },
        { text: this.$t('toptable.lastmonth'), val: 'lastmonth' } // 修正为和其他逻辑一致的格式
      ],
      // 日报类型数据
      dailyData: [
        { name: this.$t('user.work.writedaily'), id: 1 },
        { name: this.$t('user.work.writedailyweek'), id: 2 },
        { name: this.$t('user.work.writedailymonth'), id: 3 }
      ],
      // 当前日报名称
      dailyName: '',
      // 当前日报索引
      dailyIndex: 1,
      // 总页数
      totalPage: 0,
      // 时间选择器的值
      timeVal: [this.$moment().startOf('month').format('YYYY/MM/DD'), this.$moment().format('YYYY/MM/DD')], // 修正为 startOf('month')
      // 表格查询参数
      tableFrom: {
        page: 1,
        limit: 15,
        types: ['0'], // 修正为数组形式
        time: `${this.$moment().startOf('month').format('YYYY/MM/DD')}-${this.$moment().format('YYYY/MM/DD')}`, // 修正为 startOf('month')
        viewer: 'hr'
      },
      // 部门列表
      departmentList: [],
      // 搜索表单配置
      search: [
        {
          field_name: '人员姓名',
          field_name_en: 'name',
          form_value: 'input'
        },
        {
          field_name: '汇报类型',
          field_name_en: 'types',
          form_value: 'select',
          data_dict: [
            { name: this.$t('user.work.dailyday'), value: '0' },
            { name: this.$t('user.work.weekday'), value: '1' }, // 统一为字符串形式
            { name: this.$t('user.work.monthday'), value: '2' }, // 统一为字符串形式
            { name: '汇报', value: '3' } // 统一为字符串形式
          ],
          value: '0'
        },
        {
          field_name: '选择时间',
          field_name_en: 'time',
          form_value: 'date_picker',
          data_dict: [
            this.$moment().startOf('month').format('YYYY/MM/DD'), // 修正为 startOf('month')
            this.$moment().format('YYYY/MM/DD')
          ]
        }
      ],
      // 表格数据
      tableData: [],
      // 编辑项的ID
      editId: 0,
      // 编辑项的数据
      editData: {},
      // 今天0点时间戳
      nowDateStar: null,
      // 今天23:59:59时间戳
      nowDateEnd: null,
      // 用户选项列表
      usersOption: [],
      // 导出数据配置
      exportData: {
        data: [],
        cols: [{ wpx: 100 }, { wpx: 120 }, { wpx: 240 }, { wpx: 180 }, { wpx: 90 }, { wpx: 140 }, { wpx: 140 }]
      },
      // 导出文件名
      saveName: '',
      // 导出加载状态
      exportLoading: false
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
  mounted() {
    this.nowDateStar = parseInt(this.$moment(new Date()).startOf('day').format('x'))
    this.nowDateEnd = parseInt(this.$moment(new Date()).endOf('day').format('x'))
    this.dailyName = this.dailyData[this.dailyIndex - 1].name
    this.getList()
    this.getUsersOption()
  },
  methods: {
    setOptions() {
      this.fromList = [
        { text: this.$t('toptable.all'), val: '' },
        { text: this.$t('toptable.thisweek'), val: 'week' },
        { text: this.$t('toptable.thismonth'), val: 'month' },
        { text: this.$t('toptable.lastmonth'), val: 'lastmonth' }
      ]
    },
    confirmData(data) {
      if (data == 'reset') {
        this.restFn()
      } else {
        this.tableFrom = { ...this.tableFrom, ...data }
        this.tableData.page = 1
        this.getList()
      }
    },
    restFn() {
      this.tableFrom = {
        page: 1,
        limit: 15,
        types: ['0'],
        time:
          this.$moment().startOf('months').format('YYYY/MM/DD') + '-' + this.$moment(new Date()).format('YYYY/MM/DD'),
        viewer: 'hr'
      }
      this.timeVal = [
        this.$moment().startOf('months').format('YYYY/MM/DD'),
        this.$moment(new Date()).format('YYYY/MM/DD')
      ]
      this.search[2].data_dict = this.timeVal
      this.getList()
    },
    async getList(val) {
      this.tableFrom.page = val ? val : this.tableFrom.page
      const result = await enterpriseDailyApi(this.tableFrom)
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
    onCheck(id, type, data) {
      this.editId = id
      this.editData = data
      // this.$refs.addBox.getInfo(id, type)
      this.$refs.addBox.openBox(id, type, data)
    },
    handleClick() {
      this.tableFrom.page = 1
      this.tableFrom.user_id = ''
      this.getList()
    },
    selectTypes() {
      this.tableFrom.page = 1
      this.getList()
    },
    onchangeTime() {
      this.tableFrom.page = 1
      if (this.timeVal) {
        this.tableFrom.time = this.timeVal[0] ? this.timeVal.join('-') : ''
      } else {
        this.tableFrom.time = ''
      }
      this.getList()
    },
    handleSizeChange(val) {
      this.tableFrom.page = 1
      this.tableFrom.limit = val
      this.getList()
    },
    handleCurrentChange(page) {
      this.tableFrom.page = page
      this.getList()
    },

    handleDaily(command) {
      this.dailyIndex = command
      this.dailyName = this.dailyData[command - 1].name
      this.addDaily()
    },
    getExportData() {
      this.saveName = '汇报导出_' + this.$moment(new Date()).format('HH_mm_ss') + '.xlsx'
      const where = JSON.parse(JSON.stringify(this.tableFrom))
      where.limit = 0
      this.exportLoading = true
      enterpriseDailyExportApi(where)
        .then((res) => {
          const data = res.data
          const aoaData = [['姓名', '部门', '已提交数', '未提交数', '汇报类型']]
          if (data.length > 0) {
            data.forEach((value) => {
              value.plan = value.types == 3 ? '' : value.plan
              aoaData.push([
                value.name,
                value.frame_name ? value.frame_name : '-',
                value.submit,
                value.no_submit,
                value.type_name
              ])
            })
            this.exportData.data = aoaData
            this.$refs.exportExcel.exportExcel()
            this.exportLoading = false
          }
        })
        .catch((err) => {
          this.exportLoading = false
        })
    }
  }
}
</script>

<style lang="scss" scoped>
.textover3 {
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 10;
  overflow: hidden;
  line-height: 1.5;
  white-space: pre-wrap;
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
</style>
