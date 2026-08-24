<template>
  <myEcharts
    :isShowEmpty="dataList.length < 1"
    :option="option"
    :field="field"
    :designer="designer"
    v-loading="loading"
  />
</template>

<script>
import myEcharts from '@/components/scEcharts/chart-widget.vue'
import { queryChartData } from '@/api/chart'
import { getPreviewNum } from '@/utils/util'

export default {
  name: 'funnelChart-widget',
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
      dataList: [],
      units: {},
      option: {
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        isNoData: true,
        tooltip: {
          trigger: 'item'
        },
        series: [
          {
            type: 'funnel',
            left: '10%',
            top: 60,
            bottom: 60,
            width: '80%',
            minSize: '0%',
            maxSize: '100%',
            sort: 'descending',
            gap: 2,
            label: {
              show: true
            },
            labelLine: {
              length: 10,
              lineStyle: {
                width: 1,
                type: 'solid'
              }
            },
            itemStyle: {
              borderColor: '#fff',
              borderWidth: 1
            },
            emphasis: {
              label: {
                fontSize: 20
              }
            },
            data: []
          }
        ]
      }
    }
  },
  watch: {
    field: {
      handler() {
        this.cutField = this.field
        this.initOption()
      },
      deep: true,
      immediate: true
    }
  },
  mounted() {
    this.cutField = this.field
    this.initOption()
  },
  methods: {
    initOption() {
      let { options, type } = this.cutField
      if (options) {
        let { metrics } = options.setDimensional
        if (metrics.length < 1) {
          this.option.isNoData = true
          return
        }

        metrics.forEach((el) => {
          this.units[el.field_name] = {
            showDecimalPlaces: el.showDecimalPlaces,
            decimalPlaces: el.decimalPlaces,
            thousandsSeparator: el.thousandsSeparator,
            numericUnits: el.numericUnits == '无' ? '' : el.numericUnits
          }
        })
        this.getChartData(options, type)
        this.option.isNoData = false
      } else {
        this.option.isNoData = true
      }
    },
    async getChartData(options, type) {
      this.loading = true
      this.dataList = []
      let res = await queryChartData(options, type)
      if (res && res.status === 200) {
        let { setChartConf } = this.cutField.options
        this.option.legend = {
          show: setChartConf.chartShow,
          right: 5,
          top: 5
        }
        this.option.grid.bottom = setChartConf.chartShow ? '60px' : '10px'
        this.option.series[0].data = res.data
        this.dataList = res.data || []
        if (this.dataList.length > 0) {
          this.option.tooltip.formatter = (e) => {
            let other = e.data.other
            let formatterStr = []
            for (const key in other) {
              if (Object.hasOwnProperty.call(other, key)) {
                const element = other[key]
                let { showDecimalPlaces, decimalPlaces, thousandsSeparator, numericUnits } = this.units[key]
                formatterStr.push(
                  key +
                    '：' +
                    getPreviewNum(showDecimalPlaces, decimalPlaces, thousandsSeparator, element) +
                    numericUnits
                )
              }
            }
            formatterStr = formatterStr.join('<br />')
            return e.name + '<br />' + formatterStr
          }
        }

        this.option.series[0].label = {
          show: setChartConf.numShow,
          formatter: function (param) {
            return param.value
          },
          position: 'inside'
        }
        this.loading = false
      } else {
        this.loading = false
      }
    }
  }
}
</script>
