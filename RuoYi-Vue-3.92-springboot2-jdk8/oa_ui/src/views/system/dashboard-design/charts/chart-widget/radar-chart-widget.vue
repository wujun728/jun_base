<template>
  <myEcharts :isShowEmpty="isShowEmpty" :option="option" :field="field" :designer="designer" v-loading="loading" />
</template>

<script>
import myEcharts from '@/components/scEcharts/chart-widget.vue'
import { queryChartData } from '@/api/chart'
import { getPreviewNum } from '@/utils/util'

export default {
  name: 'radarChart-widget',
  components: {
    myEcharts
  },
  props: {
    field: Object,
    designer: Object
  },
  data() {
    return {
      cutField: '',
      loading: false,
      isShowEmpty: false,
      option: {
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        tooltip: {
          trigger: 'item'
        },
        radar: {
          indicator: []
        },
        series: []
      },
      units: {}
    }
  },
  watch: {
    field: {
      handler() {
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
        let { dimension, metrics } = options.setDimensional
        if (dimension.length < 1 || metrics.length < 1) {
          this.option.isNoData = true
          return
        }
        metrics.forEach((el) => {
          this.$set(this.units, el.field_name, {
            showDecimalPlaces: el.showDecimalPlaces,
            decimalPlaces: el.decimalPlaces,
            thousandsSeparator: el.thousandsSeparator,
            numericUnits: el.numericUnits == '无' ? '' : el.numericUnits
          })
        })
        await this.getChartData(options, type)
        this.option.isNoData = false
      } else {
        this.option.isNoData = true
      }
    },
    async getChartData(options, type) {
      this.loading = true
      let res = await queryChartData(options, type)
      if (res && res.status === 200) {
        let { setChartConf } = this.cutField.options
        this.option.legend = {
          show: setChartConf.chartShow,
          right: 5,
          top: 5
        }
        this.option.grid.bottom = setChartConf.chartShow ? '60px' : '10px'
        this.option.series[0] = {
          type: 'radar',
          data: res.data.series.map((el) => {
            return {
              value: el.data,
              name: el.name
            }
          })
        }
        let xAxis = res.data.xAxis
        let { dimension, metrics } = options.setDimensional
        if (metrics.length > 1 && dimension.length == 1) {
          this.option.tooltip.formatter = (e) => {
            let { showDecimalPlaces, decimalPlaces, thousandsSeparator, numericUnits } = this.units[e.name]
            let formatterStr = []
            e.data.value.forEach((el, inx) => {
              formatterStr.push(
                xAxis[inx] +
                  '：' +
                  getPreviewNum(showDecimalPlaces, decimalPlaces, thousandsSeparator, el) +
                  numericUnits
              )
            })
            formatterStr = formatterStr.join('<br />')
            return e.name + '<br />' + formatterStr
          }
        } else {
          this.option.tooltip.formatter = ''
        }

        this.option.radar.indicator = res.data.xAxis.map((el) => {
          return {
            name: el || '--'
          }
        })
        this.isShowEmpty = false
      } else {
        this.isShowEmpty = true
      }
      this.loading = false
    }
  }
}
</script>
