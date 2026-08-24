<template>
  <myEcharts :isShowEmpty="isShowEmpty" :option="option" :field="field" :designer="designer" v-loading="loading" />
</template>

<script>
import myEcharts from '@/components/scEcharts/chart-widget.vue'
import { queryChartData } from '@/api/chart'

export default {
  name: 'lineChart-widget',
  components: {
    myEcharts
  },
  props: {
    field: Object,
    designer: Object
  },
  data() {
    return {
      loading: false,
      cutField: '',
      isShowEmpty: false,
      option: {
        isNoData: true,
        tooltip: {
          trigger: 'axis'
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '0%',
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
            type: 'line'
          }
        ]
      }
    }
  },
  watch: {
    field: {
      deep: true,
      handler(newVal) {
        this.cutField = newVal
        this.initOption()
      }
    }
  },
  mounted() {
    this.cutField = this.field
    this.initOption()
  },
  methods: {
    initOption() {
      const { options, type } = this.cutField
      if (options) {
        const { dimension, metrics } = options.setDimensional
        if (dimension.length < 1 || metrics.length < 1) {
          this.option.isNoData = true
          return
        }
        this.getChartData(options, type)
        this.option.isNoData = false
      } else {
        this.option.isNoData = true
      }
    },
    async getChartData(options, type) {
      this.loading = true
      await queryChartData(options, type)
        .then((res) => {
          if (!res) {
            // 返回false 无数据
            setTimeout(() => {
              this.loading = false
            }, 1000)
            return
          }
          const { chartStyle, setChartConf, axisCoordinates } = this.cutField.options

          // 如果设置了Y轴最大值
          if (axisCoordinates.max > 0) {
            this.option.yAxis.min = axisCoordinates.min
            this.option.yAxis.max = axisCoordinates.max
          } else {
            this.option.yAxis = {
              type: 'value'
            }
          }

          // 图例是否显示
          this.option.legend = {
            show: setChartConf.chartShow,
            right: 5,
            top: 5
          }
          this.option.grid.bottom = setChartConf.chartShow ? '50px' : '10px'

          if (res && res.status === 200) {
            this.option.xAxis.data = [...res.data.xAxis]
            this.option.series = res.data.series.map((el) => {
              el.type = 'line'
              el.smooth = chartStyle == 2
              // 数值是否展示
              el.label = {
                show: setChartConf.numShow,
                position: 'top'
              }
              return el
            })
            this.isShowEmpty = false
          } else {
            this.isShowEmpty = true
          }
          this.loading = false
        })
        .catch((err) => {
          console.log(err, 'err')
        })
    }
  }
}
</script>
