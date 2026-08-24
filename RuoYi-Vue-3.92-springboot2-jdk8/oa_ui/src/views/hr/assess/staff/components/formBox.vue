<template>
  <div>
    <div class="flex-between">
      <div class="title-16">考核列表</div>
      <div class="lh-center">
        <el-dropdown>
          <span class="iconfont icongengduo2 fz32 pointer ml10"></span>
          <el-dropdown-menu style="text-align: left">
            <el-dropdown-item @click.native="handleExport()"> 导出 </el-dropdown-item>
            <el-dropdown-item @click.native="dropdownSearch()"> 查看异常 </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>
    <div class="search mt20 mb10">
      <div class="flex">
        <span class="total-16 mr10 mt10">共{{ total }}条</span>
        <el-form :inline="true">
          <div class="flex-row flex-col">
            <div class="flex">
              <template v-if="!showForm">
                <el-form-item class="select-bar">
                  <select-member
                    :value="userList || []"
                    :placeholder="$t('access.placeholder18')"
                    @getSelectList="getSelectList($event, 2)"
                    :is-search="true"
                    style="width: 200px"
                  ></select-member>
                </el-form-item>
                <el-form-item class="select-bar">
                  <el-select
                    v-model="tableFrom.period"
                    placeholder="请选择考核周期"
                    clearable
                    size="small"
                    style="width: 160px"
                    @change="changePeriod"
                  >
                    <el-option
                      v-for="(item, index) in periodOptions"
                      :key="index"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
                </el-form-item>

                <template v-if="tableFrom.period">
                  <el-form-item class="select-bar">
                    <el-date-picker
                      v-if="tableFrom.period === 1 || tableFrom.period === 2"
                      ref="getDateValue"
                      v-model="tableFrom.time"
                      :format="dateArray[tableFrom.period - 1].format"
                      :placeholder="dateArray[tableFrom.period - 1].text"
                      :type="dateArray[tableFrom.period - 1].type"
                      class="time"
                      clearable
                      size="small"
                      style="width: 250px"
                      @change="getDateValue"
                    />
                    <el-date-picker
                      v-else-if="tableFrom.period === 3"
                      ref="getDateValue"
                      v-model="tableFrom.time"
                      :format="dateArray[4].format"
                      :placeholder="dateArray[4].text"
                      :type="dateArray[4].type"
                      class="time"
                      clearable
                      size="small"
                      @change="getDateValue"
                    />
                    <dateQuarter
                      v-if="quarterBtn"
                      ref="dateQuarter"
                      :get-value="getQuarterDate"
                      :half-year-btn="halfYearBtn"
                    />
                  </el-form-item>
                </template>

                <el-form-item v-if="tableFrom.period === ''" class="select-bar">
                  <el-date-picker
                    v-model="timeVal"
                    :end-placeholder="$t('toptable.endingdate')"
                    :picker-options="pickerOptions"
                    :placeholder="$t('toptable.selecttime')"
                    :range-separator="$t('toptable.to')"
                    :start-placeholder="$t('toptable.startdate')"
                    class="time"
                    clearable
                    format="yyyy/MM/dd"
                    size="small"
                    type="daterange"
                    value-format="yyyy/MM/dd"
                    @change="onchangeTime"
                  />
                </el-form-item>
              </template>

              <el-form-item>
                <el-tooltip content="重置搜索条件" effect="dark" placement="top">
                  <div class="reset" @click="reset"><i class="iconfont iconqingchu"></i></div>
                </el-tooltip>
              </el-form-item>
            </div>
          </div>
        </el-form>
      </div>
      <div class="flex">
        <el-popover
          v-model="$store.state.business.conditionDialog"
          placement="bottom-start"
          trigger="manual"
          width="750"
          popper-class="popover"
        >
          <!-- 高级筛选 -->
          <div class="condition-box">
            <div class="flex-between">
              <div class="title">筛选条件</div>
              <div class="el-icon-close pointer" @click="$store.state.business.conditionDialog = false"></div>
            </div>
            <condition-dialog
              v-if="$store.state.business.conditionDialog"
              :eventStr="`event`"
              :formArray="viewSearch"
              :max="9"
              :noRule="false"
              @saveCondition="saveCondition"
            ></condition-dialog>
          </div>
          <div slot="reference" class="pointer text-16 el-dropdown-link mr10" @click="onShow">
            筛选 <span class="iconfont icona-bianzu8"></span>
            <span v-if="additional_search.length > 0" class="yuan">{{
              additional_search ? additional_search.length : 0
            }}</span>
          </div>
        </el-popover>
        <el-popover placement="bottom" ref="popover" width="140" trigger="click" popper-class="time-popover">
          <div class="field-box">
            <div
              class="field-text"
              v-for="(item, index) in timeSearch"
              :key="index"
              :class="activeIndex == item.value ? 'field-bga' : ''"
              @click="handleClick(item, index)"
            >
              <span v-if="activeIndex == item.value" class="el-icon-check"></span>
              <span class="over-text">{{ item.name }}</span>
            </div>
          </div>
          <div class="field-box">
            <div
              class="field-text"
              v-for="(item, index) in sortList"
              :key="index"
              :class="sortIndex == item.value ? 'field-bga' : ''"
              @click="sortFn(item, index)"
            >
              <span v-if="sortIndex == item.value" class="el-icon-check"></span> {{ item.name }}
            </div>
          </div>

          <div class="iconfont iconpaixu4 text-16 paixuBox pointer" slot="reference"></div>
        </el-popover>
      </div>
    </div>
  </div>
</template>

<script>
import Commnt from '@/components/user/accessCommon'
export default {
  name: 'FormBox',
  props: ['total'],
  components: {
    selectMember: () => import('@/components/form-common/select-member'),
    dateQuarter: () => import('@/components/form-common/select-dateQuarter'),
    conditionDialog: () => import('@/components/develop/conditionDialog')
  },
  data() {
    return {
      pickerOptions: this.$pickerOptionsTimeEle,
      showForm: false,
      showText: '展开',
      activeIndex: 'created_at',
      timeVal: [],
      tableFrom: {
        time: '',
        date: '',
        times: '',
        type: '',
        period: '',
        test_uid: '',
        exportType: 0
      },
      additional_search: [],
      departmentList: [],
      periodOptions: Commnt.periodOption,
      type: 0,
      statusOptions: Commnt.statusOptions,
      activeDepartment: {},
      userList: [],
      checkList: [],
      sortIndex: '',
      sortList: [
        {
          name: '升序',
          value: 'asc'
        },
        {
          name: '降序',
          value: 'desc'
        },
        {
          name: '默认排序',
          value: ''
        }
      ],
      timeSearch: [
        {
          name: '创建时间',
          value: 'created_at'
        },
        {
          name: '修改时间',
          value: 'updated_at'
        }
      ],
      viewSearch: [
        {
          field: 'frame',
          title: '部门',
          type: 'frame_id'
        },
        {
          field: 'check_uid',
          title: '考核人',
          type: 'check_uid'
        },
        {
          field: 'status',
          title: '考核状态',
          type: 'select',
          options: Commnt.statusOptions
        }
      ],
      title: '',
      dateArray: [
        { value: 1, type: 'week', text: '选择周', format: 'yyyy 第 WW 周' },
        { value: 2, type: 'month', text: '选择月份', format: 'yyyy-MM' },
        { value: 4, type: '' },
        { value: 5, type: '' },
        { value: 3, type: 'year', text: '选择年份', format: 'yyyy' }
      ],
      quarterBtn: false,
      halfYearBtn: false
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
  methods: {
    setOptions() {},
    // 时间段选择
    selectChange() {
      this.timeVal = ''
    },
    onShow() {
      this.$store.state.business.conditionDialog = true
    },
    dropdownSearch() {
      this.$emit('openAssessed')
    },
    // 高级筛选样式
    saveCondition(data) {
      this.additional_search = this.$store.state.business.fieldOptions.list

      let obj = {}
      this.additional_search.map((item) => {
        obj[item.field] = item.option
      })
      this.tableFrom.exportType = 0
      this.tableFrom = { ...this.tableFrom, ...obj }
      this.$emit('confirmData', this.tableFrom)
    },
    handleClick(item, index) {
      this.tableFrom.sort_field = item.value
      this.activeIndex = item.value
      this.tableFrom.exportType = 0
      this.$emit('confirmData', this.tableFrom)
    },
    sortFn(item, index) {
      this.tableFrom.sort_value = item.value
      this.sortIndex = item.value
      this.tableFrom.exportType = 0
      this.$emit('confirmData', this.tableFrom)
    },
    // 重置
    reset() {
      this.timeVal = []
      this.tableFrom = {
        time: '',
        data: '',
        times: '',
        type: '',
        status: '',
        period: '',
        frame: '',
        test_uid: '',
        check_uid: '',
        exportType: 0
      }

      this.departmentList = []
      this.userList = []
      this.checkList = []
      this.additional_search = []
      let data = {
        list: [],
        type: '',
        additional_search_boolean: '1'
      }
      this.$store.commit('uadatefieldOptions', data)
      this.$emit('confirmData', 'reset')
    },
    cardTag(type, index) {
      if (type === 1) {
        this.departmentList.splice(index, 1)
        this.tableFrom.frame.splice(index, 1)
      } else if (type === 2) {
        this.userList.splice(index, 1)
        this.tableFrom.test_uid.splice(index, 1)
      } else {
        this.tableFrom.check_uid.splice(index, 1)
        this.checkList.splice(index, 1)
      }
      this.handleConfirm()
    },
    openFn() {
      if (this.showForm === false) {
        this.showText = '收起'
        this.showForm = true
      } else if (this.showForm === true) {
        this.showText = '展开'
        this.showForm = false
      }
    },
    // 选择时间区域
    onchangeTime(e) {
      if (e == null) {
        this.tableFrom.time = ''
        this.tableFrom.times = ''
      } else {
        this.timeVal = e
        if (this.tableFrom.period === 4 || this.tableFrom.period === 5) {
          this.$refs.dateQuarter.showValue = ''
        }
        this.tableFrom.time = this.timeVal[0] + '-' + this.timeVal[1]
        this.tableFrom.times = this.timeVal[0] + '-' + this.timeVal[1]
        this.tableFrom.date = ''
      }
      this.handleConfirm()
    },
    // 确认
    handleConfirm() {
      this.tableFrom.exportType = 0
      this.confirmData()
    },
    handleExport() {
      this.$emit('getExportData')
    },
    confirmData() {
      this.$emit('confirmData', this.tableFrom)
    },

    gettype() {
      if (this.type == 1) {
        return this.departmentList
      } else if (this.type == 2) {
        return this.userList
      } else {
        return this.checkList
      }
    },

    // 选择成员回调
    getSelectList(data, type) {
      if (type === 2) {
        var testUid = []
        data.forEach((item) => {
          testUid.push(item.value)
        })
        this.tableFrom.test_uid = testUid
        this.userList = data
      } else if (type === 3) {
        var checkUid = []
        data.forEach((item) => {
          checkUid.push(item.value)
        })
        this.tableFrom.check_uid = checkUid
        this.checkList = data
      }
      this.handleConfirm()
    },

    getPeriodText(id) {
      const txt = Commnt.getPeriodText(id)
      return txt
    },
    getStatusText(id) {
      const txt = Commnt.getStatusText(id)
      return txt
    },
    changePeriod(e) {
      // 切换季度半年考核
      if (e == 4) {
        this.quarterBtn = true
        this.halfYearBtn = true
        this.$nextTick(() => {
          this.$refs.dateQuarter.showValue = ''
        })
      } else if (e == 5) {
        this.quarterBtn = true
        this.halfYearBtn = false
        this.$nextTick(() => {
          this.$refs.dateQuarter.showValue = ''
        })
      } else {
        this.quarterBtn = false
      }
      if (e === '') {
        this.tableFrom.date = ''
      } else {
        this.timeVal = []
        this.tableFrom.time = ''
        this.tableFrom.times = ''
      }
      this.handleConfirm()
    },
    getDateValue(e) {
      this.timeVal = []
      if (!e) {
        this.tableFrom.date = ''
      } else {
        if (this.tableFrom.period == 1) {
          this.tableFrom.date = this.$moment(this.tableFrom.time).format('YYYY-MM-DD 00:00:00')
        } else if (this.tableFrom.period == 2) {
          this.tableFrom.date = this.$moment(this.tableFrom.time).format('YYYY-MM-DD 00:00:00')
        } else if (this.tableFrom.period == 3) {
          this.tableFrom.date = this.$moment(this.tableFrom.time).format('YYYY-MM-DD 00:00:00')
        }
      }
      this.handleConfirm()
    },
    getQuarterDate(data) {
      this.tableFrom.date = this.getQuarterTime(this.tableFrom.period, data)
      this.timeVal = []
      this.tableFrom.times = ''
      this.tableFrom.time = ''
      this.handleConfirm()
    },
    getQuarterTime(type, time) {
      if (!time) return false
      var str = ''
      const timeArr = time.split('-')
      const year = timeArr[0]
      const month = timeArr[1]
      if (type === 5) {
        if (month === '01') {
          str = year + '-01-01 00:00:00'
        } else if (month === '02') {
          str = year + '-04-01 00:00:00'
        } else if (month === '03') {
          str = year + '-07-01 00:00:00'
        } else if (month === '04') {
          str = year + '-10-01 00:00:00'
        }
      } else if (type === 4) {
        if (month === '01') {
          str = year + '-01-01 00:00:00'
        } else if (month === '02') {
          str = year + '-07-01 00:00:00'
        }
      }
      return str
    }
  }
}
</script>

<style lang="scss" scoped>
.field-bga {
  color: #1890ff;
  background: rgba(24, 144, 255, 0.07);
}
.el-icon-check {
  position: absolute;
  left: 14px;
  top: 11px;
}
.reset {
  margin-top: 2px;
}
.search {
  height: 32px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.yuan {
  font-family: PingFang SC, PingFang SC;
  font-weight: 400;
  font-size: 12px;
  color: #909399;
}
.paixuBox {
  width: 25px;
  height: 32px;
  line-height: 32px;
  display: flex;
  justify-content: center;
  align-items: center;
}
.paixuBox:hover {
  background: #f7f7f7;
}

.open {
  width: 62px;
  height: 32px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  display: inline-block;
  text-align: center;
  font-size: 13px;
  color: #303133;
  line-height: 32px;
  cursor: pointer;
}
.el-dropdown-link {
  height: 32px;
  padding: 0 10px;
  line-height: 32px;
}
.el-dropdown-link:hover {
  background: #f3f3f3;
}
.iconzhankai1 {
  font-size: 13px;
  color: #909399;
}
.right-text {
  display: inline-block;
  transform: rotate(180deg);
}
.form-top-line._show {
  width: 100%;
}

::v-deep .el-form-item {
  margin-bottom: 0;
}
.icona-bianzu8 {
  color: #999999;
  font-size: 13px;
}
.iconpaixu4 {
  color: #999999;
  font-size: 13px;
  // margin-top: px;
}
.field-box {
  margin-top: 8px;
  border-bottom: 1px solid #f5f5f5;
  margin-bottom: 8px;
}
.field-text {
  cursor: pointer;
  height: 32px;
  // background-color: pink;
  width: 100%;
  font-family: PingFang SC, PingFang SC;
  font-weight: 400;
  font-size: 13px;
  color: #303133;
  line-height: 32px;
  padding-right: 15px;
  padding-left: 29px;
  position: relative;
}
.field-text:hover {
  background-color: #f2f3f5;
}
.condition-box {
  padding: 20px;
}
</style>
<style>
.popover {
  padding: 20px;
}
</style>
