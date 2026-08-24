<template>
  <myEcharts :isShowEmpty="isShowEmpty" :option="option" :field="field" :designer="designer" v-loading="loading" />
</template>
<script>
import myEcharts from '@/components/scEcharts/chart-widget.vue'
import { queryChartData } from '@/api/chart'
import { EventBus } from '@/libs/bus'

export default {
  name: 'barChart-widget',
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
      drillDown: false,
      loading: false,
      isShowEmpty: false,
      option: {
        isNoData: true,
        tooltip: {
          trigger: 'axis'
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: []
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            data: [],
            type: 'bar'
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
    EventBus.$on('barChangeValue', (e, type) => {
      if (type) {
        this.drillDown = true
      }
      this.cutField.options.setDimensional.dimension[0].value = e
      this.initOption(1)
    })
  },
  destroyed() {
    EventBus.$off('barChangeValue')
  },
  methods: {
    async getChartData(options, type, isClick) {
      this.loading = true
      let res = await queryChartData(options, type)
      if ((this.drillDown && res.data.xAxis.length > 0) || !this.drillDown) {
        this.option.xAxis.data = [...res.data.xAxis]
        let { chartStyle, setChartConf, axisCoordinates } = this.cutField.options

        if (chartStyle != 3 && axisCoordinates.max > 0) {
          this.option.yAxis.min = axisCoordinates.min
          this.option.yAxis.max = axisCoordinates.max
        } else {
          this.option.yAxis = {
            type: 'value'
          }
        }

        this.option.legend = {
          show: setChartConf.chartShow,
          right: 5,
          top: 5
        }
        this.option.grid.bottom = setChartConf.chartShow ? '10px' : '10px'
        this.option.other = res.data.other
        if (options.setDimensional.dimension[0].value) {
          // 返回按钮
          this.option.graphic = [
            {
              type: 'text',
              left: '3%',
              top: '3%',
              style: {
                text: '返回',
                fontSize: 12,
                fill: '#ccc'
              },
              onclick: () => {
                this.cutField.options.setDimensional.dimension[0].value = ''
              }
            }
          ]
        } else {
          this.option.graphic = []
        }
        this.option.series = res.data.series.map((el) => {
          el.type = 'bar'
          el.stack = chartStyle == 1 ? null : chartStyle == 2 ? '普通堆叠' : '百分比堆叠'
          el.label = {
            show: setChartConf.numShow,
            color: '#FFF',
            formatter: function (param) {
              return param.value + (chartStyle == 3 ? '%' : '')
            }
          }
          return el
        })
        if (res.data.xAxis.length == 0) {
          this.isShowEmpty = true
        } else {
          this.isShowEmpty = false
        }
      } else {
        if (!isClick) this.isShowEmpty = true
      }
      this.loading = false
    },
    initOption(isClick) {
      let { options, type } = this.cutField
      if (options) {
        let { dimension, metrics } = options.setDimensional
        if (dimension.length < 1 || metrics.length < 1) {
          this.option.isNoData = true
          return
        }
        let { chartStyle } = this.cutField.options
        if (metrics.length < 2 && chartStyle != 1) {
          this.cutField.options.chartStyle = 1
        }
        this.getChartData(options, type, isClick)
        this.option.isNoData = false
      } else {
        this.option.isNoData = true
      }
    }
  }
}
</script>
