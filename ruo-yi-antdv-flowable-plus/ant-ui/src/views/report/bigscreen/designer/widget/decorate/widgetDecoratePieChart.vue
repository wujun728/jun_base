<template>
  <div :style="styleObj">
    <v-chart :options="options" autoresize/>
  </div>
</template>

<script>

export default {
  name: 'WidgetRotatePieChart',
  components: {},
  props: {
    value: Object,
    ispreview: Boolean
  },
  data() {
    return {
      options: {
        title: {},
        series: [
          {
            name: '最外环',
            type: 'pie',
            silent: true,
            radius: ['98%', '97%'],
            label: {
              normal: {
                show: false
              },
            },
            labelLine: {
              normal: {
                show: false
              }
            },
            itemStyle: {
              normal: {
                show: false,
              },
            },
            data: [0]
          },
          {
            name: '外四环',
            type: 'pie',
            zlevel: 2,
            silent: true,
            radius: ['90%', '91%'],
            startAngle: 50,
            hoverAnimation: false,
            label: {
              normal: {
                show: false
              },
            },
            labelLine: {
              normal: {
                show: false
              }
            },
            data: [0]
          },
          {
            name: '里四环',
            type: 'pie',
            zlevel: 3,
            silent: true,
            radius: ['88%', '87%'],
            label: {
              normal: {
                show: false
              },
            },
            labelLine: {
              normal: {
                show: false
              }
            },
            data: [0]
          },
          {
            name: '虚线环',
            type: 'pie',
            zlevel: 4,
            silent: true,
            radius: ['84%', '83%'],
            label: {
              normal: {
                show: false
              },
            },
            labelLine: {
              normal: {
                show: false
              }
            },
            data: [0]
          },
          {
            name: '三分环',
            type: 'pie',
            zlevel: 5,
            silent: true,
            radius: ['80%', '78%'],
            color: ['#fc8d89', '#46d3f3', 'rgba(203,203,203,.2)'],
            startAngle: 50,
            avoidLabelOverlap: false,
            hoverAnimation: false,
            label: {
              normal: {
                show: false
              },
            },
            data: [50, 20, 40]
          },
          {
            name: '外指标环',
            type: 'gauge',
            splitNumber: 30,
            min: 0,
            max: 100,
            radius: '73%', //图表尺寸
            center: ['50%', '50%'],
            startAngle: 90,
            endAngle: -269.9999,
            axisLabel: {
              show: false
            },
            pointer: {
              show: 0,
            },
            detail: {
              show: 0,
            },
          },
          {
            name: '里指标环',
            type: 'gauge',
            splitNumber: 30,
            min: 0,
            max: 100,
            radius: '68%',
            center: ['50%', '50%'],
            startAngle: 90,
            endAngle: -269.9999,
            axisLabel: {
              show: false
            },
            pointer: {
              show: 0,
            },
            detail: {
              show: false,
            },
          },
          {
            name: '环外环',
            type: 'pie',
            zlevel: 20,
            silent: true,
            radius: ['60%', '59%'],
            hoverAnimation: false,
            label: {
              normal: {
                show: false
              },
            },
            labelLine: {
              normal: {
                show: false
              }
            },
          },
          {
            name: '中间环形图',
            type: 'pie',
            radius: ['40%', '55%'],
            avoidLabelOverlap: false,
            hoverAnimation: false,
            itemStyle: {
              normal: {
                color: '#80ADD2',
                borderColor: '#3D4268',
              }
            },
            label: {
              normal: {
                show: false,
                position: 'center',

              },
              emphasis: {
                show: true,
                textStyle: {
                  fontSize: '30',
                  fontWeight: 'bold'
                }
              }
            },
            labelLine: {
              normal: {
                show: false
              }
            },
            data: [
              25, 25, 25, 25, 25, 25
            ]
          },
        ]
      },
      optionsStyle: {}, // 样式
      optionsCollapse: {}, // 图标属性
      optionsSetup: {}
    }
  },
  computed: {
    styleObj() {
      return {
        position: this.ispreview ? 'absolute' : 'static',
        width: this.optionsStyle.width + 'px',
        height: this.optionsStyle.height + 'px',
        left: this.optionsStyle.left + 'px',
        top: this.optionsStyle.top + 'px',
        background: this.optionsSetup.background
      }
    }
  },
  watch: {
    value: {
      handler(val) {
        this.optionsStyle = val.position
        this.optionsData = val.data
        this.optionsCollapse = val.setup
        this.optionsSetup = val.setup
        this.editorOptions()
      },
      deep: true
    }
  },
  mounted() {
    this.optionsStyle = this.value.position
    this.optionsData = this.value.data
    this.optionsCollapse = this.value.setup
    this.optionsSetup = this.value.setup
    this.editorOptions()
  },
  methods: {
    editorOptions() {
      this.setOptionsLastRing()
      this.setOptionsEightRing()
      this.setOptionsDottedRing()
      this.setOptionsThreeRing()
      this.setOptionsOutRing()
      this.setOptionsInRing()
      this.setOptionsRingOnRing()
      this.setOptionsPie()
    },
    // 颜色设置
    setColor(color) {
      const nullColor = 'rgba(0,0,0,0)'
      if (color == '') {
        color = nullColor
      }
      return color
    },
    // 最外外环1
    setOptionsLastRing() {
      const optionsSetup = this.optionsSetup
      const series = this.options.series[0]
      if (optionsSetup.isLastRingShow) {
        series.data = [0]
      } else {
        series.data = ''
      }
      const normal = {
        color: {
          colorStops: [
            {
              offset: 0,
              color: this.setColor(optionsSetup.lastRing0Color),
            },
            {
              offset: 1,
              color: this.setColor(optionsSetup.lastRing100Color),
            },
          ],
        },
      }
      series.itemStyle['normal'] = normal
    },
    setRingPie2() {
      const optionsSetup = this.optionsSetup
      const dataArr = []
      for (let i = 0; i < 8; i++) {
        if (i % 2 === 0) {
          dataArr.push({
            name: (i + 1).toString(),
            value: 25,
            itemStyle: {
              normal: {
                color: this.setColor(optionsSetup.eightColor),
                borderWidth: 0,
                borderColor: 'rgba(0,0,0,0)'
              }
            }
          })
        } else {
          dataArr.push({
            name: (i + 1).toString(),
            value: 20,
            itemStyle: {
              normal: {
                color: 'rgba(0,0,0,0)'
              }
            }
          })
        }
      }
      return dataArr
    },
    // 八分环
    setOptionsEightRing() {
      const series = this.options.series
      series[1].data = this.setRingPie2()
      series[2].data = this.setRingPie2()
    },
    setRingPie3() {
      const optionsSetup = this.optionsSetup
      const dataArr = []
      for (let i = 0; i < (optionsSetup.dottedNum * 2); i++) {
        if (i % 2 === 0) {
          dataArr.push({
            name: (i + 1).toString(),
            value: 25,
            itemStyle: {
              normal: {
                color: this.setColor(optionsSetup.dottedColor),
                borderWidth: 0,
                borderColor: 'rgba(0,0,0,0)'
              }
            }
          })
        } else {
          dataArr.push({
            name: (i + 1).toString(),
            value: 20,
            itemStyle: {
              normal: {
                color: 'rgba(0,0,0,0)',
              }
            }
          })
        }
      }
      return dataArr
    },
    // 虚线环
    setOptionsDottedRing() {
      const series = this.options.series
      series[3].data = this.setRingPie3()
    },
    // 三分环
    setOptionsThreeRing() {
      const optionsSetup = this.optionsSetup
      const series = this.options.series[4]
      series.color = [this.setColor(optionsSetup.three1Color), this.setColor(optionsSetup.three2Color), this.setColor(optionsSetup.three3Color)]
    },
    // 外指标环
    setOptionsOutRing() {
      const optionsSetup = this.optionsSetup
      const series = this.options.series
      const axisLine = {
        show: optionsSetup.isOutRingShow,
        lineStyle: {
          width: optionsSetup.outRingWidth,
          shadowBlur: 0,
          color: [
            [1, this.setColor(optionsSetup.outRingColor)]
          ]
        }
      }
      const axisTick = {
        show: optionsSetup.isOutTickShow,
        lineStyle: {
          color: this.setColor(optionsSetup.outTickColor),
          width: optionsSetup.outTickWidth
        },
        length: optionsSetup.outTickLength,
        splitNumber: optionsSetup.outTickWidth
      }
      series[5].splitNumber = optionsSetup.outSplitNum
      const splitLine = {
        show: optionsSetup.isOutSplitShow,
        length: optionsSetup.outSplitLength,
        lineStyle: {
          color: this.setColor(optionsSetup.outSplitColor),
          width: optionsSetup.outSplitWidth,
        }
      }
      series[5].axisLine = axisLine
      series[5].axisTick = axisTick
      series[5].splitLine = splitLine
    },
    // 里指标环
    setOptionsInRing() {
      const optionsSetup = this.optionsSetup
      const series = this.options.series
      const axisLine = {
        show: optionsSetup.isInRingShow,
        lineStyle: {
          width: optionsSetup.inRingWidth,
          shadowBlur: 0,
          color: [
            [1, this.setColor(optionsSetup.inRingColor)],
          ],
        },
      }
      const axisTick = {
        show: optionsSetup.isInTickShow,
        lineStyle: {
          color: this.setColor(optionsSetup.inTickColor),
          width: optionsSetup.inTickWidth,
        },
        length: optionsSetup.inTickLength,
        splitNumber: optionsSetup.inTickNum,
      }
      series[6].splitNumber = optionsSetup.inSplitNum
      const splitLine = {
        show: optionsSetup.isInSplitShow,
        length: optionsSetup.inSplitLength,
        lineStyle: {
          color: this.setColor(optionsSetup.inSplitColor),
          width: optionsSetup.inSplitWidth
        }
      }
      series[6].axisLine = axisLine
      series[6].axisTick = axisTick
      series[6].splitLine = splitLine
    },
    // 环外环设置
    setOptionsRingOnRing() {
      const optionsSetup = this.optionsSetup
      const series = this.options.series[7]
      if (optionsSetup.isRingOnRingShow) {
        series.data = [0]
      } else {
        series.data = ''
      }
      const itemStyle = {
        normal: {
          color: this.setColor(optionsSetup.ringOnRingColor),
        }
      }
      series.itemStyle = itemStyle
    },
    // 中饼图设置
    setOptionsPie() {
      const optionsSetup = this.optionsSetup
      const series = this.options.series[8]
      const width = optionsSetup.pieWidth
      const pieWidth = width + '%'
      series.radius = [pieWidth, '55%']

      const pieBlocks = optionsSetup.pieBlocks
      if (pieBlocks == 'six') {
        series.data = [25, 25, 25, 25, 25, 25]
      } else if (pieBlocks == 'five') {
        series.data = [30, 30, 30, 30, 30]
      } else {
        series.data = [40, 40, 40, 40]
      }
      const itemStyle = {
        normal: {
          color: this.setColor(optionsSetup.pieColor),
          borderColor: this.setColor(optionsSetup.pieBorderColor),
        }
      }
      series.itemStyle = itemStyle
    }
  }
}
</script>

<style scoped lang="scss">
.echarts {
  width: 100%;
  height: 100%;
  overflow: hidden;
}
</style>
