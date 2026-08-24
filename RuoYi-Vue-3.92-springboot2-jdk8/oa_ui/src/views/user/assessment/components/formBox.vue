<template>
  <div>
    <el-form :inline="true" class="from-s">
      <el-row class="flex-row">
        <el-form-item :label="$t('toptable.assessmentcycle')" class="select-bar">
          <el-select
            v-model="tableFrom.period"
            :placeholder="$t('finance.pleaseselect')"
            size="small"
            clearable
            @change="selectPeriod"
          >
            <el-option v-for="(item, index) in periodOptions" :key="index" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>

        <div v-if="tableFrom.period">
          <el-form-item :label="$t('access.assessmenttime')" class="select-bar">
            <el-date-picker
              class="time"
              v-if="tableFrom.period === 1 || tableFrom.period === 2"
              ref="getDateValue"
              v-model="tableFrom.time"
              size="small"
              :picker-options="tableFrom.period == '' ? 'pickerOptions' : ''"
              clearable
              :type="dateArray[tableFrom.period - 1].type"
              :format="dateArray[tableFrom.period - 1].format"
              :placeholder="dateArray[tableFrom.period - 1].text"
              @change="getDateValue"
            />
            <el-date-picker
              class="time"
              v-else-if="tableFrom.period === 3"
              ref="getDateValue"
              v-model="tableFrom.time"
              size="small"
              clearable
              :type="dateArray[4].type"
              :format="dateArray[4].format"
              :placeholder="dateArray[4].text"
              @change="getDateValue"
            />
            <dateQuarter v-if="quarterBtn" ref="dateQuarter" :get-value="getQuarterDate" :half-year-btn="halfYearBtn" />
          </el-form-item>
        </div>

        <el-form-item v-if="!tableFrom.period" :label="$t('access.assessmenttime')" class="select-bar">
          <el-date-picker
            class="time"
            v-model="timeVal"
            type="daterange"
            :picker-options="pickerOptions"
            :placeholder="$t('toptable.selecttime')"
            format="yyyy/MM/dd"
            size="small"
            clearable
            value-format="yyyy/MM/dd"
            :range-separator="$t('toptable.to')"
            :start-placeholder="$t('toptable.startdate')"
            :end-placeholder="$t('toptable.endingdate')"
            @change="onchangeTime"
          />
        </el-form-item>

        <el-form-item label="被考核人" class="select-bar">
          <select-member
            :value="userList || []"
            :is-search="true"
            :placeholder="`请选择被考核人`"
            @getSelectList="getSelectList"
            class="mr10"
            style="width: 200px"
          ></select-member>
        </el-form-item>

        <div v-if="handle === ''">
          <el-form-item :label="$t('hr.assessmentstatus')" class="select-bar">
            <el-select
              v-model="tableFrom.cycle"
              :placeholder="$t('access.placeholder19')"
              size="small"
              clearable
              @change="changeCycle"
            >
              <el-option v-for="(item, index) in statusOptions" :key="index" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
        </div>
        <el-form-item label="管理范围" class="select-bar" v-if="handle === '' && frameUserType !== 0">
          <manage-range ref="manageRange" @change="changeMastart"></manage-range>
        </el-form-item>
        <el-form-item>
          <el-tooltip effect="dark" content="重置搜索条件" placement="top">
            <div class="reset" @click="reset"><i class="iconfont iconqingchu"></i></div>
          </el-tooltip>
        </el-form-item>
      </el-row>
    </el-form>
  </div>
</template>

<script>
import Common from '@/components/user/accessCommon'
import { frameUserApi } from '@/api/setting'
export default {
  name: 'FormBox',
  components: {
    dateQuarter: () => import('@/components/form-common/select-dateQuarter'),
    manageRange: () => import('@/components/form-common/select-manageRange'),
    selectMember: () => import('@/components/form-common/select-member')
  },
  props: {
    frameUserType: {
      Frame: Number,
      default: 0
    },
    handle: {
      Frame: Number,
      default: 1
    }
  },
  data() {
    return {
      fromList: [
        { text: this.$t('toptable.all'), val: '' },
        { text: this.$t('user.work.backlog'), val: 0 },
        { text: this.$t('user.work.done'), val: 1 }
      ],
      pickerOptions: this.$pickerOptionsTimeEle,
      timeVal: [],
      userList: [],
      tableFrom: {
        matter: '',
        period: '',
        scope_frame: 'all',
        test_uid: '',
        cycle: '',
        time: '',
        data: '',
        times: ''
      },

      periodOptions: Common.periodOption,
      type: 0,
      statusOptions: Common.statusOptions,
      title: '',
      frameUserArray: [],
      frameUserOption: [],
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
    },
    handle() {
      this.reset()
    }
  },
 
  methods: {
    setOptions() {
      this.fromList = [
        { text: this.$t('toptable.all'), val: '' },
        { text: this.$t('user.work.backlog'), val: 0 },
        { text: this.$t('user.work.done'), val: 1 }
      ]
    },

    // 选择管理范围完成回调
    changeMastart(data) {
      this.tableFrom.scope_frame = data
      this.$emit('confirmData', this.tableFrom)
    },
    // 选择成员完成回调
    getSelectList(data) {
      this.userList = data
      var testUid = []
      this.userList.forEach((item) => {
        testUid.push(item.value)
      })
      this.tableFrom.test_uid = testUid
      this.$emit('confirmData', this.tableFrom)
    },

    selectPeriod(e) {
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
      this.$emit('confirmData', this.tableFrom)
    },
    changeCycle() {
      this.$emit('confirmData', this.tableFrom)
    },
    // 重置
    reset() {
      this.timeVal = []
      this.tableFrom = {
        matter: this.handle,
        period: '',
        scope_frame: 'all',
        test_uid: '',
        cycle: '',
        time: '',
        data: '',
        times: ''
      }

      this.userList = []
      setTimeout(() => {
        if (this.$refs.manageRange) {
          this.$refs.manageRange.frame_id = 'all'
        }
      }, 300)
      this.$emit('confirmData', this.tableFrom)
    },
    // 选择时间区域
    onchangeTime(e) {
      if (e == null) {
        this.tableFrom.time = ''
        this.tableFrom.times = ''
        this.confirmData()
      } else {
        if (this.tableFrom.period === 4 || this.tableFrom.period === 5) {
          this.$refs.dateQuarter.showValue = ''
        }
        this.tableFrom.time = this.timeVal[0] + '-' + this.timeVal[1]
        this.tableFrom.times = this.timeVal[0] + '-' + this.timeVal[1]
        this.tableFrom.date = ''
        this.confirmData()
      }
    },
    // 确认
    handleConfirm() {
      this.confirmData()
    },
    getFrameUser() {
      const data = {
        type: this.frameUserType
      }
      this.frameUserOption = []
      frameUserApi(data).then((res) => {
        res.data == undefined ? (this.frameUserArray = []) : (this.frameUserArray = res.data)
        if (this.frameUserArray.length > 0) {
          if (this.frameUserType === 1) {
            this.frameUserArray.forEach((value) => {
              this.frameUserOption.push({ value: value.user_ent.id, label: value.name })
            })
          } else {
            this.frameUserArray.forEach((value) => {
              this.frameUserOption.push({ value: value.id, label: value.card.name })
            })
          }
        }
      })
    },
    confirmData() {
      this.$emit('confirmData', this.tableFrom)
    },

    getPeriodText(id) {
      const txt = Common.getPeriodText(id)
      return txt
    },
    getStatusText(id) {
      const txt = Common.getStatusText(id)
      return txt
    },
    getStatusTag(status) {
      return Common.getStatusTag(status)
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
      this.confirmData()
    },
    getQuarterDate(data) {
      this.tableFrom.date = this.getQuarterTime(this.tableFrom.period, data)
      this.timeVal = []
      this.tableFrom.times = ''
      this.tableFrom.time = ''
      this.confirmData()
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
::v-deep .el-select__tags {
  cursor: pointer;
  display: flex;
  flex-wrap: nowrap;
  overflow: hidden;
  overflow-x: auto;
}
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
  .tag {
    background-color: F0F2F5;
    border: none;
  }
}
::v-deep.el-select__input {
  cursor: pointer;
}
::v-deep .el-select__tags::-webkit-scrollbar {
  display: none;
}
::v-deep .from-s .flex-row .el-form-item {
  margin-right: 10px;
  margin-left: 0;
}
</style>
