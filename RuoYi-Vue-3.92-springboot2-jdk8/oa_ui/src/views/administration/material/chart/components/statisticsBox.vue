<template>
  <div>
    <el-form :inline="true" class="from-s">
      <div class="flex-row">
        <el-form-item class="select-bar">
          <el-date-picker
            v-model="timeVal"
            :clearable="false"
            :end-placeholder="$t('toptable.endingdate')"
            :picker-options="pickerOptions"
            :placeholder="$t('toptable.selecttime')"
            :range-separator="$t('toptable.to')"
            :start-placeholder="$t('toptable.startdate')"
            class="time"
            format="yyyy/MM/dd"
            size="small"
            type="daterange"
            value-format="yyyy/MM/dd"
            @change="onchangeTime"
          />
        </el-form-item>
        <el-form-item>
          <el-tooltip content="重置搜索条件" effect="dark" placement="top">
            <div class="reset" @click="reset"><i class="iconfont iconqingchu"></i></div>
          </el-tooltip>
        </el-form-item>
      </div>
    </el-form>
  </div>
</template>

<script>
export default {
  name: 'FormBox',
  data() {
    return {
      timeVal: [
        this.$moment().subtract(30, 'days').format('YYYY/MM/DD'),
        this.$moment(new Date()).format('YYYY/MM/DD')
      ],
      pickerOptions: this.$pickerOptionsTimeEle,
      tableFrom: {
        time:
          this.$moment().subtract(30, 'days').format('YYYY/MM/DD') +
          '-' +
          this.$moment(new Date()).format('YYYY/MM/DD'),
        type: ''
      }
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
    selectType() {
      this.confirmData()
    },
    reset() {
      this.timeVal = [
        this.$moment().subtract(30, 'days').format('YYYY/MM/DD'),
        this.$moment(new Date()).format('YYYY/MM/DD')
      ]
      ;(this.tableFrom.time =
        this.$moment().subtract(30, 'days').format('YYYY/MM/DD') + '-' + this.$moment(new Date()).format('YYYY/MM/DD')),
        this.confirmData()
    },
    // 选择时间区域
    onchangeTime(e) {
      if (e == null) {
        this.tableFrom.time = ''
        this.confirmData()
      } else {
        this.timeVal = e
        this.tableFrom.time = this.timeVal[0] + '-' + this.timeVal[1]
        this.confirmData()
      }
    },
    confirmData() {
      this.$emit('confirmData', this.tableFrom)
    }
  }
}
</script>

<style lang="scss" scoped>
.el-form-item {
  margin-bottom: 0;
}
::v-deep.el-form-item--medium .el-form-item__content {
  line-height: 30px;
}
</style>
