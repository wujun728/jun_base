<!-- @FileDescription: 选择季度与半年考核组件（绩效模块常用） -->
<template>
  <div>
    <mark
      v-show="showSeason"
      style="position: fixed; top: 0; bottom: 0; left: 0; right: 0; background: rgba(0, 0, 0, 0); z-index: 999"
      @click.stop="showSeason = false"
    />
    <el-input
      v-model="showValue"
      :placeholder="halfYearBtn ? '请选择半年' : '请选择季度'"
      style="width: 100%"
      size="small"
      @focus="showSeason = true"
    >
      <i slot="prefix" class="el-input__icon el-icon-date" />
    </el-input>
    <el-card
      v-show="showSeason"
      class="box-card"
      style="width: 322px; padding: 0 3px 20px; margin-top: 10px; position: fixed; z-index: 9999"
    >
      <div slot="header" class="clearfix" style="text-align: center; padding: 0">
        <button
          type="button"
          aria-label="前一年"
          class="el-picker-panel__icon-btn el-date-picker__prev-btn el-icon-d-arrow-left"
          @click="prev"
        />
        <span role="button" class="el-date-picker__header-label">{{ year }}年</span>
        <button
          type="button"
          aria-label="后一年"
          class="el-picker-panel__icon-btn el-date-picker__next-btn el-icon-d-arrow-right"
          @click="next"
        />
      </div>
      <div v-if="!halfYearBtn" class="text item text-center">
        <el-button
          v-for="(item, index) in quarterArray"
          :key="index"
          type="text"
          size="medium"
          class="item-button"
          @click="selectSeason(index)"
        >
          {{ item.name }}</el-button
        >
      </div>
      <div v-if="halfYearBtn" class="text item text-center">
        <el-button
          v-for="(item, index) in halfYearArray"
          :key="index"
          type="text"
          size="medium"
          class="item-button"
          @click="selectSeason(index)"
        >
          {{ item.name }}</el-button
        >
      </div>
    </el-card>
  </div>
</template>
<script>
/**
 * @file:  View 组件 季节选择控件
 * @author: v_zhuchun
 * @date: 2019-05-23
 * @description: UI组件  可选择季节
 * @api: valueArr : 季度value defalut['01-03', '04-06', '07-09', '10-12'] 默认值待设置
 */
export default {
  name: 'DateQuarter',
  props: {
    valueArr: {
      default: () => {
        return ['01', '02', '03', '04']
      },
      type: Array
    },
    getValue: {
      default: () => {},
      type: Function
    },
    defaultValue: {
      default: '',
      type: String
    },
    halfYearBtn: {
      default: false,
      type: Boolean
    }
  },
  data() {
    return {
      showSeason: false,
      season: '',
      year: new Date().getFullYear(),
      showValue: '',
      showText: '请选择季度',
      halfYearText: ['上', '下'],
      hailYearIndex: null,
      month: new Date().getMonth() + 1,
      quarterArray: [
        { name: '第一季度', type: 1, year: new Date().getFullYear() },
        { name: '第二季度', type: 4, year: new Date().getFullYear() },
        { name: '第三季度', type: 7, year: new Date().getFullYear() },
        { name: '第四季度', type: 10, year: new Date().getFullYear() }
      ],
      halfYearArray: [
        { name: '上半年', type: 1, year: new Date().getFullYear() },
        { name: '下半年', type: 7, year: new Date().getFullYear() }
      ]
    }
  },
  watch: {
    defaultValue: function (value, oldValue) {
      var arr = value.split('-')
      this.year = arr[0].slice(0, 4)
      var str = arr[0].slice(4, 6) + '-' + arr[1].slice(4, 6)
      var arrAll = this.valueArr
      this.showValue = this.halfYearBtn
        ? `${this.year}年${this.halfYearText[this.hailYearIndex]}半年`
        : `${this.year}年${arrAll.indexOf(str) + 1}季度`
    }
  },
  created() {
    if (this.defaultValue) {
      var value = this.defaultValue
      var arr = value.split('-')
      this.year = arr[0].slice(0, 4)
      var str = arr[0].slice(4, 6) + '-' + arr[1].slice(4, 6)
      var arrAll = this.valueArr
      this.showValue = this.halfYearBtn
        ? `${this.year}年${this.halfYearText[this.hailYearIndex]}半年`
        : `${this.year}年${arrAll.indexOf(str) + 1}季度`
    }
  },
  methods: {
    one() {
      this.showSeason = false
    },
    prev() {
      this.year = this.year * 1 - 1
    },
    next() {
      this.year = this.year * 1 + 1
    },
    getDisabled(m, y) {
      if (y > this.year) {
        return true
      } else if (y === this.year) {
        if (this.halfYearBtn) {
          if (m <= this.month && m <= 6) {
            return true
          } else {
            return false
          }
        } else {
          if (m <= this.month && m <= 3) {
            return true
          } else if (m <= this.month && m <= 6) {
            return false
          } else if (m <= this.month && m <= 9) {
            return false
          } else {
            return false
          }
        }
      } else {
        return false
      }
    },
    selectSeason(i) {
      var that = this
      that.hailYearIndex = i
      that.season = i + 1
      var arr = that.valueArr[i].split('-')
      that.getValue(that.year + '-' + arr[0])
      that.showSeason = false
      this.showValue = this.halfYearBtn
        ? `${this.year}年${this.halfYearText[i]}半年`
        : `${this.year}年${this.season}季度`
    }
  }
}
</script>
<style lang="scss">
.item-button {
  width: 40%;
  color: #606266;
  text-align: center;
  margin: 0 !important;
}
.item {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
}
.item-button.active {
  background-color: #f5f7fa;
  cursor: not-allowed;
  color: #c0c4cc;
}
</style>
