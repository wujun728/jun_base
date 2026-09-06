<template>
  <div :style="styleObj">
    <v-chart :options="options" autoresize/>
  </div>
</template>

<script>
var per = 60
export default {
  name: 'WidgetPiePercentageChart',//百分比图参考：https://www.makeapie.com/editor.html?c=xFkzKG-bpl
  components: {},
  props: {
    value: Object,
    ispreview: Boolean
  },
  data() {
    return {
      angle: 0,
      options: {
        title: {
          text: '{nums|' + per + '}{percent|%}',
          x: 'center',
          y: 'center',
          textStyle: {
            rich: {
              nums: {
                fontSize: 60,
                color: '#29EEF3',
              },
              percent: {
                fontSize: 30,
                color: '#29EEF3',
              },
            },
          },
        },
        legend: {
          type: 'plain',
          orient: 'vertical',
          right: 0,
          top: '10%',
          align: 'auto',
          data: [{
            name: '1',
            icon: 'circle'
          }, {
            name: '2',
            icon: 'circle'
          }, {
            name: '3',
            icon: 'circle'
          }, {
            name: '4',
            icon: 'circle'
          }],
          textStyle: {
            color: 'white',
            fontSize: 16,
            padding: [10, 1, 10, 0],
          },
          selectedMode: false,
        },
        series: [
          {
            //name: '环1',
            type: 'custom',
            coordinateSystem: 'none',
            renderItem: (params, api) => {
              return {
                type: 'arc',
                shape: {
                  cx: api.getWidth() / 2,
                  cy: api.getHeight() / 2,
                  r: (Math.min(api.getWidth(), api.getHeight()) / 2) * 0.6,
                  startAngle: ((0 + this.angle) * Math.PI) / 180,
                  endAngle: ((90 + this.angle) * Math.PI) / 180,
                },
                style: {
                  stroke: '#0CD3DB',
                  fill: 'transparent',
                  lineWidth: 1.5,
                },
                silent: true,
              }
            },
            data: [0],
          },
          {
            //name: '环2',
            type: 'custom',
            coordinateSystem: 'none',
            renderItem: (params, api) => {
              return {
                type: 'arc',
                shape: {
                  cx: api.getWidth() / 2,
                  cy: api.getHeight() / 2,
                  r: (Math.min(api.getWidth(), api.getHeight()) / 2) * 0.6,
                  startAngle: ((180 + this.angle) * Math.PI) / 180,
                  endAngle: ((270 + this.angle) * Math.PI) / 180,
                },
                style: {
                  stroke: '#0CD3DB',
                  fill: 'transparent',
                  lineWidth: 1.5,
                },
                silent: true,
              }
            },
            data: [0],
          },
          {
            //name: '环3',
            type: 'custom',
            coordinateSystem: 'none',
            renderItem: (params, api) => {
              return {
                type: 'arc',
                shape: {
                  cx: api.getWidth() / 2,
                  cy: api.getHeight() / 2,
                  r: (Math.min(api.getWidth(), api.getHeight()) / 2) * 0.65,
                  startAngle: ((270 + -this.angle) * Math.PI) / 180,
                  endAngle: ((40 + -this.angle) * Math.PI) / 180,
                },
                style: {
                  stroke: '#0CD3DB',
                  fill: 'transparent',
                  lineWidth: 1.5,
                },
                silent: true,
              }
            },
            data: [0],
          },
          {
            //name: '环4',
            type: 'custom',
            coordinateSystem: 'none',
            renderItem: (params, api) => {
              return {
                type: 'arc',
                shape: {
                  cx: api.getWidth() / 2,
                  cy: api.getHeight() / 2,
                  r: (Math.min(api.getWidth(), api.getHeight()) / 2) * 0.65,
                  startAngle: ((90 + -this.angle) * Math.PI) / 180,
                  endAngle: ((220 + -this.angle) * Math.PI) / 180,
                },
                style: {
                  stroke: '#0CD3DB',
                  fill: 'transparent',
                  lineWidth: 1.5,
                },
                silent: true,
              }
            },
            data: [0],
          },
          {
            //name: '绿点1',
            type: 'custom',
            coordinateSystem: 'none',
            renderItem: (params, api) => {
              const x0 = api.getWidth() / 2
              const y0 = api.getHeight() / 2
              const r = (Math.min(api.getWidth(), api.getHeight()) / 2) * 0.65
              const point = this.getCirlPoint(x0, y0, r, 90 + -this.angle)
              return {
                type: 'circle',
                shape: {
                  cx: point.x,
                  cy: point.y,
                  r: 4,
                },
                style: {
                  stroke: '#0CD3DB', //粉
                  fill: '#0CD3DB',
                },
                silent: true,
              }
            },
            data: [0],
          },
          {
            //name: '绿点2',
            type: 'custom',
            coordinateSystem: 'none',
            renderItem: (params, api) => {
              const x0 = api.getWidth() / 2
              const y0 = api.getHeight() / 2
              const r = (Math.min(api.getWidth(), api.getHeight()) / 2) * 0.65
              const point = this.getCirlPoint(x0, y0, r, 270 + -this.angle)
              return {
                type: 'circle',
                shape: {
                  cx: point.x,
                  cy: point.y,
                  r: 4,
                },
                style: {
                  stroke: '#0CD3DB', //绿
                  fill: '#0CD3DB',
                },
                silent: true,
              }
            },
            data: [0],
          },
          {
            //name: '圆环',
            type: 'pie',
            radius: ['58%', '45%'],
            silent: true,
            clockwise: true,
            startAngle: 90,
            z: 0,
            zlevel: 0,
            label: {
              normal: {
                position: 'center',
              },
            },
            data: [
              {
                value: per,
                name: '',
                itemStyle: {
                  normal: {
                    color: {
                      // 完成的圆环的颜色
                      colorStops: [
                        {
                          offset: 0,
                          color: '#4FADFD', // 0% 处的颜色
                        },
                        {
                          offset: 1,
                          color: '#28E8FA', // 100% 处的颜色
                        },
                      ],
                    },
                  },
                },
              },
              {
                value: 100 - per,
                name: '',
                label: {
                  normal: {
                    show: false,
                  },
                },
                //剩余圆环颜色
                itemStyle: {
                  normal: {
                    color: '#173164',
                  },
                },
              },
            ],
          },
          {
            name: 'percent',
            type: 'gauge',
            radius: '58%',
            center: ['50%', '50%'],
            startAngle: 0,
            endAngle: 359.9,
            splitNumber: 8,
            hoverAnimation: true,
            axisTick: {
              show: false,
            },
            splitLine: {
              length: 15,
              lineStyle: {
                width: 5,
                color: '#061740',
              },
            },
            axisLabel: {
              show: false,
            },
            pointer: {
              show: false,
            },
            axisLine: {
              lineStyle: {
                opacity: 0,
              },
            },
            detail: {
              show: false,
            },
            data: [{
              value: 0,
              name: ''
            }],
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
        this.optionsStyle = val.position
        this.optionsData = val.data
        this.optionsCollapse = val.collapse
        this.optionsSetup = val.setup
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
  mounted() {
    /*        setInterval(() => {
              this.angle = this.angle + 3
              myChart.setOption(options,true)
            }, 1000);*/
  },
  methods: {
    //轴point设置
    getCirlPoint(x0, y0, r, x) {
      const x1 = x0 + r * Math.cos((x * Math.PI) / 180)
      const y1 = y0 + r * Math.sin((x * Math.PI) / 180)
      return {
        x: x1,
        y: y1,
      }
    },
    editorOptions() {
      this.setOptionsTitle()
      this.setOptionsColor()
      this.setOptionsData()
      this.setOptionLine()
      this.setOptionSurplusColor()
    },
    setOptionsTitle() {
      const optionsSetup = this.optionsSetup
      const title = this.options.title
      title.x = 'center'
      title.y = 'center'
      const rich = {
        nums: {
          fontSize: optionsSetup.textNumFontSize,
          color: optionsSetup.textNumColor,
          fontWeight: optionsSetup.textNumFontWeight
        },
        percent: {
          fontSize: optionsSetup.textPerFontSize,
          color: optionsSetup.textPerColor,
          fontWeight: optionsSetup.textPerFontWeight
        }
      }
      title.textStyle['rich'] = rich
      this.options.title = title
    },
    setOptions() {
      const optionsSetup = this.optionsSetup
      const series = this.options.series
      for (const key in series) {
        if (series[key].type == 'gauge') {
          series[key].axisLine.lineStyle.width = optionsSetup.tickMarkWeight
          series[key].axisLabel.show = optionsSetup.showScaleValue
          series[key].axisLabel.fontSize = optionsSetup.scaleFontSize
          series[key].axisTick.show = optionsSetup.showTickMarks
          series[key].detail.textStyle.fontSize = optionsSetup.targetFontSize
        }
      }
    },
    //圆环0-100%颜色
    setOptionsColor() {
      const optionsSetup = this.optionsSetup
      const itemStyle = this.options.series[6]['data'][0]['itemStyle']
      const normal = {
        color: {
          // 完成的圆环的颜色
          colorStops: [
            {
              offset: 0,
              color: optionsSetup.color0Start, // 0% 处的颜色
            },
            {
              offset: 1,
              color: optionsSetup.color100End, // 100% 处的颜色
            },
          ],
        },
      }
      itemStyle['normal'] = normal
    },
    setOptionSurplusColor() {
      const itemStyle = this.options.series[6]['data'][1]['itemStyle']
      const normal = {
        color: this.optionsSetup.colorsurplus,
      }
      itemStyle['normal'] = normal
    },
    setOptionLine() {
      const optionsSetup = this.optionsSetup
      const line = this.options.series[7]['splitLine']
      const num = this.options.series[7]
      num.splitNumber = optionsSetup.lineNumber
      line.length = optionsSetup.lineLength
      const lineStyle = {
        width: optionsSetup.lineWidth,
        color: optionsSetup.lineColor,
      }
      line['lineStyle'] = lineStyle
    },
    // 数据解析
    setOptionsData() {
      const optionsData = this.optionsData // 数据类型 静态 or 动态
      optionsData.dataType == 'staticData'
        ? this.staticDataFn(optionsData.staticData)
        : this.dynamicDataFn(
        optionsData.dynamicData,
        optionsData.refreshTime
        )
    },
    staticDataFn(val) {
      const title = this.options.title
      const num = val[0]['num']
      title.text = '{nums|' + num + '}{percent|%}'
      this.options.series[6]['data'][0]['value'] = num
      this.options.series[6]['data'][1]['value'] = 100 - num
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
        this.options.title.text = '{nums|' + res[0].value + '}{percent|%}'
        this.options.series[6]['data'][0]['value'] = res[0].value
        this.options.series[6]['data'][1]['value'] = 100 - res[0].value
      })
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
