<template>
  <myEcharts
    :isShowEmpty="isShowEmpty"
    :option="myOption"
    :field="field"
    :designer="designer"
    v-if="field.options.chartStyle != 2 || myOption.isNoData"
    v-loading="loading"
  />
  <div class="bar-progress" @click.stop="setSelected" v-loading="loading" v-else>
    <el-progress
      :text-inside="true"
      :stroke-width="36"
      :percentage="percentage"
      :format="formatText"
      style="width: 100%"
    />
  </div>
</template>

<script>
import myEcharts from '@/components/scEcharts/chart-widget.vue'
import { queryChartData } from '@/api/chart'

export default {
  name: 'progressbar-widget',
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
      donutChartOption: {
        title: {
          text: '30%',
          x: 'center',
          y: 'center',
          textStyle: {
            fontWeight: 'normal',
            color: '#29EEF3',
            fontSize: '15'
          }
        },
        legend: {
          show: false,
          right: 5,
          top: 5
        },
        grid: {
          containLabel: true
        },
        tooltip: {
          formatter: function (params) {
            return params.name + '：' + params.percent + ' %'
          }
        },
        series: [
          {
            name: 'circle',
            type: 'pie',
            radius: ['30%', '50%'],
            itemStyle: {
              label: {
                show: false
              },
              labelLine: {
                show: false
              }
            },
            data: [
              {
                value: 30,
                name: '占比',
                itemStyle: {
                  color: {
                    colorStops: [
                      {
                        offset: 0,
                        color: '#4FADFD'
                      },
                      {
                        offset: 1,
                        color: '#28E8FA'
                      }
                    ]
                  },
                  label: {
                    show: false
                  },
                  labelLine: {
                    show: false
                  }
                },
                label: {
                  show: false
                }
              },
              {
                name: '剩余',
                value: 70,
                itemStyle: {
                  color: '#909399'
                },
                label: {
                  show: false
                }
              }
            ]
          }
        ]
      },
      wavesChart: {
        title: {
          show: false,
          text: '',
          x: '48%',
          y: '50%',
          z: 10,
          textAlign: 'center',
          textStyle: {
            color: '#ffffff',
            fontSize: 15,
            fontWeight: 500
          }
        },
        grid: {
          containLabel: true
        },
        legend: {
          show: false,
          right: 5,
          top: 5
        },
        series: [
          {
            type: 'liquidFill',
            radius: '90%',
            center: ['50%', '50%'],
            itemStyle: {
              opacity: 0.95,
              shadowColor: 'rgba(0, 0, 0, 0)'
            },
            amplitude: 10,
            data: [0.1, 0.1, 0.1],
            color: [
              {
                type: 'linear',
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [
                  {
                    offset: 0,
                    color: 'rgba(24, 144, 255, 0.5)'
                  },
                  {
                    offset: 1,
                    color: '#79BEFF'
                  }
                ],
                globalCoord: false
              }
            ],
            backgroundStyle: {
              borderWidth: 1,
              color: '#fff'
            },
            label: {
              position: ['50%', '75%'],
              formatter: '10%',
              fontSize: 24,
              color: '#59AFFF'
            },
            outline: {
              borderDistance: 0,
              itemStyle: {
                borderWidth: 1,
                borderColor: '#59AFFF'
              }
            }
          }
        ]
      },
      myOption: {},
      percentage: 0,
      metePercentage: 0,
      progressText: '',
      loading: false,
      isShowEmpty: false,
      cutCompleted: 0,
      numericUnits: '',
      metricsConf: {}
    }
  },
  watch: {
    field: {
      handler(newVal) {
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
  },
  methods: {
    initOption() {
      let { options, type } = this.cutField
      if (options) {
        let { setDimensional } = options
        let { metrics } = setDimensional
        if (metrics.length < 1) {
          this.myOption.isNoData = true
          return
        }
        this.metricsConf = metrics[0]
        this.numericUnits = metrics[0].numericUnits == '无' ? '' : metrics[0].numericUnits
        this.getChartData(options, type)
        this.myOption.isNoData = false
      } else {
        this.myOption.isNoData = true
      }
    },
    async getChartData(options, type) {
      this.loading = true
      try {
        let res = await queryChartData(options, type)
        if (res && res.status === 200) {
          let { chartStyle, setDimensional, setChartConf } = options
          let { targetValue, metrics } = setDimensional
          this.myOption = chartStyle == 1 ? this.donutChartOption : this.wavesChart
          let maxNum = targetValue || 1
          let cutNum = res.data.value
          this.cutCompleted = cutNum
          let point = Math.round((cutNum / maxNum) * 100)
          this.percentage = point > 100 ? 100 : point
          this.metePercentage = point
          this.progressText = setChartConf.numShow ? metrics[0].alias : null
          // 图例是否显示
          this.myOption.legend.show = setChartConf.chartShow

          if (chartStyle == 1) {
            this.myOption.title.text = `${point}%`
            this.myOption.series[0].data[0].value = cutNum
            this.myOption.series[0].data[0].name =
              metrics[0].alias + '：' + this.getPreviewNum(cutNum) + (this.numericUnits ? this.numericUnits : '')
            this.myOption.series[0].data[1].value = maxNum - cutNum
            this.myOption.series[0].data[1].name =
              '剩余：' + this.getPreviewNum(maxNum - cutNum) + (this.numericUnits ? this.numericUnits : '')
            this.myOption.series[0].data[0].label.show = setChartConf.numShow
            this.myOption.series[0].data[1].label.show = setChartConf.numShow
          } else {
            this.myOption.series[0].data = [point / 100, point / 100, point / 100]
            this.myOption.series[0].label.formatter = point + '%'
            // this.myOption.title.text =
            //   metrics[0].alias +
            //   '（已完成：' +
            //   this.getPreviewNum(this.cutCompleted) +
            //   (this.numericUnits ? this.numericUnits : '') +
            //   '） '
            this.myOption.title.show = setChartConf.numShow
            this.myOption.series[0].label.show = setChartConf.numShow
          }
          this.isShowEmpty = false
        } else {
          this.isShowEmpty = true
        }
      } catch (error) {
        console.error('Error fetching chart data:', error)
        // this.isShowEmpty = true
      }
      this.loading = false
    },
    formatText() {
      return this.progressText
        ? this.progressText +
            '（已完成：' +
            this.getPreviewNum(this.cutCompleted) +
            (this.numericUnits ? this.numericUnits : '') +
            '） ' +
            this.metePercentage +
            '%'
        : ''
    },
    getPreviewNum(val) {
      let { thousandsSeparator, showDecimalPlaces, decimalPlaces } = this.metricsConf
      let newVal = val

      if (showDecimalPlaces) {
        newVal = Number(newVal).toFixed(decimalPlaces)
      }
      if (thousandsSeparator) {
        newVal = this.numberToCurrencyNo(newVal)
      }
      return newVal
    },
    numberToCurrencyNo(value) {
      if (!value) return 0
      // 获取整数部分
      const intPart = Math.trunc(value)
      // 整数部分处理，增加,
      const intPartFormat = intPart.toString().replace(/(\d)(?=(?:\d{3})+$)/g, '$1,')
      // 预定义小数部分
      let floatPart = ''
      // 将数值截取为小数部分和整数部分
      const valueArray = value.toString().split('.')
      if (valueArray.length === 2) {
        // 有小数部分
        floatPart = valueArray[1].toString() // 取得小数部分
        return intPartFormat + '.' + floatPart
      }
      return intPartFormat + floatPart
    },
    setSelected() {
      this.designer.setSelected(this.field)
    }
  }
}
</script>
<style lang="scss" scoped>
.bar-progress {
  display: flex;
  height: 100%;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  ::v-deep .el-progress-bar__innerText {
    color: #fff !important;
  }
}
</style>
