<template>
  <myEcharts :isShowEmpty="isShowEmpty" :option="option" :field="field" :designer="designer" v-loading="loading" />
</template>

<script>
import myEcharts from '@/components/scEcharts/chart-widget.vue'
import { queryChartData } from '@/api/chart'
import { getPreviewNum } from '@/utils/util'
import { EventBus } from '@/libs/bus'

export default {
  name: 'pieChart-widget',
  components: {
    myEcharts
  },
  props: {
    field: {
      type: Object,
      required: true
    },
    designer: {
      type: Object,
      required: false
    }
  },
  data() {
    return {
      type: '',
      drillDown: false,
      option: {
        grid: {
          left: '3%',
          bottom: '3%',
          containLabel: true
        },
        isNoData: true,
        tooltip: {
          trigger: 'item'
        },
        series: [
          {
            type: 'pie',
            radius: '50%',
            data: [],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      },
      cutField: '',
      loading: false,
      isShowEmpty: false
    }
  },
  watch: {
    field: {
      handler(newVal, oldVal) {
        this.cutField = newVal
        this.initOption()
      },
      deep: true,
      immediate: true
    }
  },
  mounted() {
    this.cutField = this.field
    this.initOption()
    EventBus.$on('pieChangeValue', (e, type) => {
      if (type) {
        this.drillDown = true
      }
      this.cutField.options.setDimensional.dimension[0].value = e
      // this.initOption()
    })
  },
  destroyed() {
    EventBus.$off('pieChangeValue')
  },
  methods: {
    initOption(isClick) {
      let { options, type } = this.cutField
      if (options) {
        let { dimension, metrics } = options.setDimensional
        if (dimension.length < 1 || metrics.length < 1) {
          this.option.isNoData = true
          return
        }
        let { showDecimalPlaces, decimalPlaces, thousandsSeparator } = metrics[0]
        let { chartStyle } = this.cutField.options
        this.option.tooltip.formatter = (e) => {
          return (
            e.name +
            '：' +
            getPreviewNum(showDecimalPlaces, decimalPlaces, thousandsSeparator, e.value) +
            ' (' +
            e.percent +
            '%)'
          )
        }
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
        // this.option.dim_value = res.data.dim_value
        this.option.series[0].radius = chartStyle == 1 ? '60%' : ['40%', '60%']
        this.getChartData(options, type, isClick)
        this.option.isNoData = false
      } else {
        this.option.isNoData = true
      }
    },
    async getChartData(options, type, isClick) {
      this.loading = true
      let res = await queryChartData(options, type)
      if ((this.drillDown && res.data.length > 0) || !this.drillDown) {
        let { setChartConf } = this.cutField.options
        // 图例是否显示
        this.option.legend = {
          show: setChartConf.chartShow,
          right: 5,
          top: 5
        }
        this.option.grid.bottom = setChartConf.chartShow ? '60px' : '10px'
        this.option.series[0].data = res.data
        this.option.series[0].label = {
          show: setChartConf.numShow,
          formatter: function (param) {
            return param.value
          },
          position: 'inside'
        }

        if (res.data.length == 0) {
          this.isShowEmpty = true
        } else {
          this.isShowEmpty = false
        }
      } else {
      }
      this.loading = false
    }
  }
}
</script>

<style lang="scss" scoped>
.bar-chart {
  width: 100%;
  height: 100%;
  .chart {
    width: 100%;
    height: 100%;
  }
}
</style>
