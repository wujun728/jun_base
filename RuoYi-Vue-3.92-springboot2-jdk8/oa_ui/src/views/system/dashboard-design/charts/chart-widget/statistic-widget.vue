<template>
  <div class="pivot-table-widget" @click.stop="setSelected" v-loading="loading">
    <!-- {{ cutField.options.setDimensional.metrics[0].decimalPlaces }} -->
    <div
      class="statistic-box"
      :style="{ color: cutField.options && cutField.options.setChartStyle.useTextColor }"
      v-if="!isNoData"
    >
      <div class="statistic-metrics yichu">
        <span
          class="sub-text"
          :style="{
            'font-size': cutField.options && cutField.options.setChartStyle.currencySymbolSize + 'px'
          }"
          >{{ cutField.options && cutField.options.setChartStyle.currencySymbol }}</span
        >
        <span class="">{{ metricsNum }}</span>
        <span class="sub" v-if="numericUnits">{{ numericUnits }}</span>
      </div>
    </div>
    <div class="no-data" v-else>
      请通过右侧
      <span class="lh">维度指标设置</span> 维度、指标栏来添加数据
    </div>
  </div>
</template>
<script>
import { queryChartData } from '@/api/chart'

export default {
  name: 'statistic-widget',
  props: {
    field: Object,
    designer: Object
  },
  data() {
    return {
      cutField: {},
      isNoData: true,
      loading: false,
      numericUnits: '',
      metricsNum: ''
    }
  },
  watch: {
    field: {
      handler(newVal) {
        this.cutField = this.field
        this.initOption()
      },
      deep: true
    }
  },
  mounted() {
    this.cutField = this.field
    this.initOption()
  },
  methods: {
    async initOption() {
      let { options, type } = this.cutField
      if (options) {
        let { metrics } = options.setDimensional
        if (metrics.length < 1) {
          this.isNoData = true
          return
        }
        this.isNoData = false
        this.numericUnits = metrics[0].numericUnits == '无' ? '' : metrics[0].numericUnits
        await this.getChartData(options, type)
        this.getPreviewNum(metrics[0])
      } else {
        this.isNoData = true
      }
    },
    async getChartData(options, type) {
      this.loading = true
      let res = await queryChartData(options, type)
      if (res && res.status === 200) {
        this.metricsNum = res.data.value || 0
      }
      this.loading = false
    },
    getPreviewNum(conf) {
      let { thousandsSeparator, showDecimalPlaces, decimalPlaces } = conf
      if (showDecimalPlaces) {
        this.metricsNum = Number(this.metricsNum).toFixed(decimalPlaces)
      }
      if (thousandsSeparator) {
        this.metricsNum = this.numberToCurrencyNo(this.metricsNum)
      }
    },
    numberToCurrencyNo(value) {
      if (!value) return 0
      const intPart = Math.trunc(value)
      const intPartFormat = intPart.toString().replace(/(\d)(?=(?:\d{3})+$)/g, '$1,')
      let floatPart = ''
      const valueArray = value.toString().split('.')
      if (valueArray.length === 2) {
        floatPart = valueArray[1].toString()
        return intPartFormat + '.' + floatPart
      }
      return intPartFormat + floatPart
    },
    setSelected() {
      this.designer.setSelected(this.field)
      // localStorage.setItem("widget__list__selected", JSON.stringify(this.field));
    }
  }
}
</script>
<style lang="scss" scoped>
.pivot-table-widget {
  width: 100%;
  height: 100%;
  .statistic-box {
    // width: 10;
    height: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    container-type: inline-size;
    .statistic-dimension {
      font-size: 5cqw;
    }
    .statistic-metrics {
      font-size: 15cqw;
      font-weight: 700;
      padding: 0 20px;
      .sub-text {
        margin-right: 5px;
      }
      .sub {
        font-size: 14px;
      }
    }
  }
}
.no-data {
  font-size: 14px;
  .lh {
    color: var(--el-color-primary);
  }
}
</style>
