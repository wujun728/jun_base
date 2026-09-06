<template>
  <div :style="styleObj">
    <v-chart :options="options" autoresize/>
  </div>
</template>

<script>
import echarts from 'echarts'

export default {
  name: 'WidgetGauge',
  components: {},
  props: {
    value: Object,
    ispreview: Boolean
  },
  data() {
    return {
      options: {
        series: [
          {
            type: 'gauge',
            z: 100,
            axisLine: {
              lineStyle: {
                width: 10,
                color: [
                  [
                    0.3,
                    new echarts.graphic.LinearGradient(0, 1, 1, 0, [
                      {
                        offset: 0,
                        color: 'rgba(0, 237, 3,0.1)',
                      },
                      {
                        offset: 0.5,
                        color: 'rgba(0, 237, 3,0.6)',
                      },
                      {
                        offset: 1,
                        color: 'rgba(0, 237, 3,1)',
                      },
                    ]),
                  ],
                  [
                    0.7,
                    new echarts.graphic.LinearGradient(0, 1, 1, 0, [
                      {
                        offset: 0,
                        color: 'rgba(255, 184, 0,0.1)',
                      },
                      {
                        offset: 0.5,
                        color: 'rgba(255, 184, 0,0.6)',
                      },
                      {
                        offset: 1,
                        color: 'rgba(255, 184, 0,1)',
                      },
                    ]),
                  ],
                  [
                    1,
                    new echarts.graphic.LinearGradient(0, 1, 1, 0, [
                      {
                        offset: 0,
                        color: 'rgba(175, 36, 74,0.1)',
                      },
                      {
                        offset: 0.5,
                        color: 'rgba(255, 36, 74,0.6)',
                      },
                      {
                        offset: 1,
                        color: 'rgba(255, 36, 74,1)',
                      },
                    ]),
                  ],
                ],
              },
            },
            pointer: {
              itemStyle: {
                color: 'auto',
              },
            },
            axisTick: {
              show: true,
              distance: 0,
              length: 10,
              lineStyle: {
                color: 'auto',
                width: 2,
              },
            },
            splitLine: {
              show: true,
              distance: 0,
              length: 14,
              lineStyle: {
                color: 'auto',
                width: 4,
              },
            },
            axisLabel: {
              show: true,
              color: 'white',
              distance: 2,
              fontSize: 10,
            },
            detail: {
              valueAnimation: true,
              formatter: '{value} %',
              color: 'white',
              fontSize: 18,
            },
            data: [
              {
                value: 70,
              },
            ],
          },
        ],
      },
      optionsStyle: {}, // 样式
      optionsData: {}, // 数据
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
        this.optionsStyle = val.position // 位置
        this.optionsData = val.data // 数据
        this.optionsCollapse = val.collapse // 折叠数据
        this.optionsSetup = val.setup // 样式
        this.editorOptions()
      },
      deep: true
    }
  },
  created() {
    this.optionsStyle = this.value.position
    this.optionsData = this.value.data
    this.optionsCollapse = this.value.collapse
    this.optionsSetup = this.value.setup
    this.editorOptions()
  },
  methods: {
    editorOptions() {
      this.setOptions()
      this.setOptionsData()
    },
    setOptions() {
      const optionsSetup = this.optionsSetup
      const series = this.options.series
      if (series[0].type == 'gauge') {
        const axisLine = {
          show: optionsSetup.ringShow,
          lineStyle: {
            width: optionsSetup.pieWeight,
            color: [
              [
                0.3,
                new echarts.graphic.LinearGradient(0, 1, 1, 0, [
                  {
                    offset: 0,
                    color: optionsSetup.color30p0,
                  },
                  {
                    offset: 0.5,
                    color: optionsSetup.color30p5,
                  },
                  {
                    offset: 1,
                    color: optionsSetup.color30p10,
                  },
                ]),
              ],
              [
                0.7,
                new echarts.graphic.LinearGradient(0, 1, 1, 0, [
                  {
                    offset: 0,
                    color: optionsSetup.color70p0,
                  },
                  {
                    offset: 0.5,
                    color: optionsSetup.color70p5,
                  },
                  {
                    offset: 1,
                    color: optionsSetup.color70p10,
                  },
                ]),
              ],
              [
                1,
                new echarts.graphic.LinearGradient(0, 1, 1, 0, [
                  {
                    offset: 0,
                    color: optionsSetup.color100p0,
                  },
                  {
                    offset: 0.5,
                    color: optionsSetup.color100p5,
                  },
                  {
                    offset: 1,
                    color: optionsSetup.color100p10,
                  },
                ]),
              ],
            ],
          },
        }
        const axisTick = {
          show: optionsSetup.tickShow,
          distance: optionsSetup.tickDistance,
          length: optionsSetup.tickLength,
          lineStyle: {
            color: 'auto',
            width: optionsSetup.tickWidth,
          },
        }
        const splitLine = {
          show: optionsSetup.splitShow,
          distance: optionsSetup.splitDistance,
          length: optionsSetup.splitLength,
          lineStyle: {
            color: 'auto',
            width: optionsSetup.splitWidth,
          },
        }
        series[0].axisLine = axisLine
        series[0].axisTick = axisTick
        series[0].splitLine = splitLine
      }
    },
    setOptionsData() {
      const optionsData = this.optionsData // 数据类型 静态 or 动态
      optionsData.dataType == 'staticData'
        ? this.staticDataFn(optionsData.staticData)
        : this.dynamicDataFn(optionsData.dynamicData, optionsData.refreshTime)
    },
    staticDataFn(val) {
      const optionsSetup = this.optionsSetup
      const series = this.options.series
      const num = val[0]['num']
      const data = [
        {
          value: num
        }
      ]
      const detail = {
        valueAnimation: true,
        formatter: '{value} %',
        color: optionsSetup.labelColor,
        fontSize: optionsSetup.labelFontSize,
        fontWeight: optionsSetup.labelFontWeight,
      }
      series[0].data = data
      series[0].detail = detail
    },
    dynamicDataFn(val, refreshTime) {
      if (!val) return
      if (this.ispreview) {
        this.getEchartData(val)
        this.flagInter = setInterval(() => {
          this.getEchartData(val)
        }, refreshTime)
      } else {
        this.getEchartData(val)
      }
    },
    getEchartData(val) {
      const data = this.queryEchartsData(val)
      data.then(res => {
        this.renderingFn(res)
      })
    },
    renderingFn(val) {
      const optionsSetup = this.optionsSetup
      const series = this.options.series
      const data = [
        {
          value: val[0].value
        }
      ]
      const detail = {
        valueAnimation: true,
        formatter: '{value} %',
        color: optionsSetup.labelColor,
        fontSize: optionsSetup.labelFontSize,
        fontWeight: optionsSetup.labelFontWeight,
      }
      series[0].data = data
      series[0].detail = detail
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
