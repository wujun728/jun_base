<template>
  <div>
    <el-form :inline="true" class="from-s">
      <el-row>
        <el-form-item :label="$t('finance.entrytime')" class="select-bar">
          <el-date-picker
            v-model="timeVal"
            type="daterange"
            class="time"
            size="small"
            :picker-options="pickerOptions"
            :placeholder="$t('toptable.selecttime')"
            format="yyyy/MM/dd"
            value-format="yyyy/MM/dd"
            :range-separator="$t('toptable.to')"
            :start-placeholder="$t('toptable.startdate')"
            :end-placeholder="$t('toptable.endingdate')"
            :clearable="false"
            @change="onchangeTime"
          />
        </el-form-item>

        <el-form-item label="管理范围" class="select-bar">
          <manage-range ref="manageRange" @change="changeMastart"></manage-range>
        </el-form-item>

        <!-- 合同分类 -->

        <el-form-item label="合同分类" class="select-bar category_id">
          <el-cascader
            size="small"
            placeholder="请选择合同分类"
            v-model="tableFrom.category_id"
            :options="options"
            :props="{ checkStrictly: false, label: 'name', value: 'value', multiple: true }"
            clearable
            collapse-tags
            @change="handleRange"
          >
          </el-cascader>
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
// 管理范围组件
import manageRange from '@/components/form-common/select-manageRange'
import Commnt from '@/components/user/accessCommon.vue'

export default {
  name: 'FormBox',
  components: {
    manageRange
  },
  props: {
    options: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      pickerOptions: this.$pickerOptionsTimeEle,
      times: '',
      tableFrom: {
        id: '',
        time: '',
        scope_frame: '',
        category_id: ''
      },
      periodOptions: Commnt.periodOption,
      type: 0,
      statusOptions: Commnt.statusOptions,
      openStatus: false,
      activeDepartment: {},
      userList: [],
      checkList: [],
      title: '',
      showPerson: true,
      timeVal: [
        this.$moment().startOf('month').format('YYYY/MM/DD'),
        this.$moment().endOf('month').format('YYYY/MM/DD')
      ]
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
    this.getTime()
    this.$emit('confirmData', this.tableFrom)
  },
  methods: {
    handleRange(e) {
      this.handleConfirm()
    },
    getTime() {
      this.times = this.timeVal[0] + '-' + this.timeVal[1]
      this.tableFrom.time = this.times
      return this.times
    },
    getCategory() {
      return this.tableFrom.category_id
    },
    setOptions() {},
    // 时间段选择
    selectChange() {
      this.timeVal = ''
    },
    // 重置
    reset() {
      this.timeVal = [
        this.$moment().startOf('month').format('YYYY/MM/DD'),
        this.$moment().endOf('month').format('YYYY/MM/DD')
      ]
      this.times = this.timeVal[0] + '-' + this.timeVal[1]
      this.tableFrom.time = this.times
      this.departmentList = []
      this.tableFrom.frame = ''
      this.tableFrom.category_id = []
      this.$refs.manageRange.reset()
      this.$emit('confirmData', this.tableFrom)
    },
    // 选择时间区域
    onchangeTime(e) {
      if (e == null) {
        this.tableFrom.time = ''
        this.tableFrom.times = ''
      } else {
        this.tableFrom.time = this.timeVal[0] + '-' + this.timeVal[1]
        this.tableFrom.times = this.timeVal[0] + '-' + this.timeVal[1]
        this.tableFrom.date = ''
      }
      this.handleConfirm()
    },
    // 确认
    handleConfirm() {
      this.confirmData()
    },
    confirmData() {
      this.$emit('confirmData', { ...this.tableFrom, id: this.tableFrom.scope_frame })
    },
    // 选择部门关闭
    departmentClose() {
      this.openStatus = false
    },
    // 选择成员完成回调
    changeMastart(data) {
      this.tableFrom.scope_frame = data
      this.handleConfirm()
    }
  }
}
</script>

<style lang="scss" scoped>
.flex-box {
  span {
    margin-right: 6px;
  }
  span:last-of-type {
    margin-right: 0;
  }
}
.category_id .el-cascader {
  min-width: 310px !important;
  ::v-deep .el-input__suffix {
    z-index: 20;
  }
}

.select-bar {
  height: 33px !important;
}
::v-deep .el-cascader__tags {
  position: absolute;
  right: 0;
}
</style>
