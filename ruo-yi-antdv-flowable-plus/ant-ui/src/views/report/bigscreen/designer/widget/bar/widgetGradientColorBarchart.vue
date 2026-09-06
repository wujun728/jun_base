<template>
  <div :style="styleObj">
    <v-chart :options="options" autoresize/>
  </div>
</template>

<script>
import echarts from 'echarts'

export default {
  name: 'WidgetGradientColorBarchart', //渐变色，参考https://www.makeapie.com/editor.html?c=x0oZWoncE
  components: {},
  props: {
    value: Object,
    ispreview: Boolean
  },
  data() {
    return {
      options: {
        title: {
          text: '',
          x: 'center',
          y: '4%',
          textStyle: {
            color: '#fff',
            fontSize: '22'
          },
          subtextStyle: {
            color: '#90979c',
            fontSize: '16'
          }
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        grid: {
          top: '15%',
          right: '3%',
          left: '5%',
          bottom: '12%'
        },
        legend: {
          textStyle: {
            color: '#fff'
          }
        },
        xAxis: {
          type: 'category',
          data: [],
          axisLine: {
            lineStyle: {
              color: 'rgba(255,255,255,0.12)'
            }
          },
          axisLabel: {
            margin: 10,
            color: '#e2e9ff',
            textStyle: {
              fontSize: 14
            }
          }
        },
        yAxis: {
          name: '',
          axisLabel: {
            formatter: '{value}',
            color: '#e2e9ff'
          },
          axisLine: {
            show: false,
            lineStyle: {
              color: 'rgba(255,255,255,0)'
            }
          },
          splitLine: {
            lineStyle: {
              color: 'rgba(255,255,255,0.12)'
            }
          }
        },
        series: [
          {
            type: 'bar',
            data: [5000, 2600, 1300, 1300, 1250, 1500],
            barWidth: '20px',
            itemStyle: {
              normal: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1,
                  [
                    {
                      offset: 0,
                      color: 'rgba(0,244,255,1)' // 0% 处的颜色
                    },
                    {
                      offset: 1,
                      color: 'rgba(0,77,167,1)' // 100% 处的颜色
                    }
                  ],
                  false
                ),
                shadowColor: 'rgba(0,160,221,1)',
                //模糊细数
                shadowBlur: 4
              }
            },
            label: {
              normal: {
                show: true,
                position: ['-10', '-30'],
                distance: 1,
                formatter: '{a|{c}}',
                rich: {
                  a: {
                    fontSize: 15,
                    color: '#fff',
                    align: 'center'
                  }
                }
              }
            }
          }
        ]
      },
      optionsStyle: {}, // 样式
      optionsData: {}, // 数据
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
    // 修改图标options属性
    editorOptions() {
      this.setOptionsTitle()
      this.setOptionsX()
      this.setOptionsY()
      this.setOptionsTop()
      this.setOptionsMargin()
      this.setOptionsColor()
      this.setOptionsData()
    },
    // 标题修改
    setOptionsTitle() {
      const optionsSetup = this.optionsSetup
      const title = {}
      title.text = optionsSetup.titleText
      title.show = optionsSetup.isNoTitle
      title.left = optionsSetup.textAlign
      title.textStyle = {
        color: optionsSetup.textColor,
        fontSize: optionsSetup.textFontSize,
        fontWeight: optionsSetup.textFontWeight
      }
      title.subtext = optionsSetup.subText
      title.subtextStyle = {
        color: optionsSetup.subTextColor,
        fontWeight: optionsSetup.subTextFontWeight,
        fontSize: optionsSetup.subTextFontSize
      }

      this.options.title = title
    },
    // X轴设置
    setOptionsX() {
      const optionsSetup = this.optionsSetup
      const xAxis = {
        type: 'category',
        show: optionsSetup.hideX, // 坐标轴是否显示
        name: optionsSetup.xName, // 坐标轴名称
        nameTextStyle: {
          color: optionsSetup.nameColorX,
          fontSize: optionsSetup.nameFontSizeX
        },
        nameRotate: optionsSetup.textAngle, // 文字角度
        inverse: optionsSetup.reversalX, // 轴反转
        axisLabel: {
          show: true,
          interval: optionsSetup.textInterval, // 文字间隔
          rotate: optionsSetup.textAngle, // 文字角度
          textStyle: {
            color: optionsSetup.Xcolor, // x轴 坐标文字颜色
            fontSize: optionsSetup.fontSizeX
          }
        },
        axisLine: {
          show: true,
          lineStyle: {
            color: optionsSetup.lineColorX
          }
        },
        splitLine: {
          show: optionsSetup.isShowSplitLineX,
          lineStyle: {
            color: optionsSetup.splitLineColorX
          }
        }
      }
      this.options.xAxis = xAxis
    },
    // Y轴设置
    setOptionsY() {
      const optionsSetup = this.optionsSetup
      const yAxis = {
        type: 'value',
        scale: optionsSetup.scale,
        splitNumber: optionsSetup.splitNumber,// 均分
        show: optionsSetup.isShowY, // 坐标轴是否显示
        name: optionsSetup.textNameY, // 坐标轴名称
        nameTextStyle: { // 别名
          color: optionsSetup.nameColorY,
          fontSize: optionsSetup.namefontSizeY
        },
        inverse: optionsSetup.reversalY, // 轴反转
        axisLabel: {
          show: true,
          rotate: optionsSetup.ytextAngle, // 文字角度
          textStyle: {
            color: optionsSetup.colorY, // y轴 坐标文字颜色
            fontSize: optionsSetup.fontSizeY
          }
        },
        axisLine: {
          show: true,
          lineStyle: {
            color: optionsSetup.lineColorY
          }
        },
        splitLine: {
          show: optionsSetup.isShowSplitLineY,
          lineStyle: {
            color: optionsSetup.splitLineColorY
          }
        }
      }
      this.options.yAxis = yAxis
    },
    // 数值设定 or 柱体设置
    setOptionsTop() {
      const optionsSetup = this.optionsSetup
      const series = this.options.series
      if (series[0].type == 'bar') {
        if (optionsSetup.verticalShow) {
          series[0].label = {
            show: optionsSetup.isShow,
            position: 'right',
            distance: optionsSetup.distance,
            fontSize: optionsSetup.fontSize,
            color: optionsSetup.subTextColor,
            fontWeight: optionsSetup.fontWeight
          }
        } else {
          series[0].label = {
            show: optionsSetup.isShow,
            position: 'top',
            distance: optionsSetup.distance,
            fontSize: optionsSetup.fontSize,
            color: optionsSetup.subTextColor,
            fontWeight: optionsSetup.fontWeight
          }
        }
        series[0].barWidth = optionsSetup.maxWidth
      }
    },
    // tooltip 提示语设置
    setOptionsTooltip() {
      const optionsSetup = this.optionsSetup
      const tooltip = {
        trigger: 'item',
        show: true,
        textStyle: {
          color: optionsSetup.lineColor,
          fontSize: optionsSetup.fontSize
        }
      }
      this.options.tooltip = tooltip
    },
    // 边距设置
    setOptionsMargin() {
      const optionsSetup = this.optionsSetup
      const grid = {
        left: optionsSetup.marginLeft,
        right: optionsSetup.marginRight,
        bottom: optionsSetup.marginBottom,
        top: optionsSetup.marginTop,
        containLabel: true
      }
      this.options.grid = grid
    },
    // 图例操作 legend
    setOptionsLegend() {
      const optionsSetup = this.optionsSetup
      const legend = this.options.legend
      legend.show = optionsSetup.isShowLegend
      legend.left = optionsSetup.lateralPosition == 'left' ? 0 : 'auto'
      legend.right = optionsSetup.lateralPosition == 'right' ? 0 : 'auto'
      legend.top = optionsSetup.longitudinalPosition == 'top' ? 0 : 'auto'
      legend.bottom =
        optionsSetup.longitudinalPosition == 'bottom' ? 0 : 'auto'
      legend.orient = optionsSetup.layoutFront
      legend.textStyle = {
        color: optionsSetup.lengedColor,
        fontSize: optionsSetup.fontSize
      }
      legend.itemWidth = optionsSetup.lengedWidth
    },
    // 渐变色
    setOptionsColor() {
      const optionsSetup = this.optionsSetup
      const itemStyle = this.options.series[0]['itemStyle']
      let normal = {}
      if (optionsSetup.verticalShow) {
        normal = {
          color: new echarts.graphic.LinearGradient(1, 0, 0, 0,
            [
              {
                offset: 0,
                color: optionsSetup.bar0color // 0% 处的颜色
              },
              {
                offset: 1,
                color: optionsSetup.bar100color // 100% 处的颜色
              }
            ],
          ),
          barBorderRadius: optionsSetup.radius, //圆角
          shadowColor: optionsSetup.shadowColor, // 阴影颜色
          shadowBlur: optionsSetup.shadowBlur //模糊系数
        }
      } else {
        normal = {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1,
            [
              {
                offset: 0,
                color: optionsSetup.bar0color // 0% 处的颜色
              },
              {
                offset: 1,
                color: optionsSetup.bar100color // 100% 处的颜色
              }
            ],
          ),
          barBorderRadius: optionsSetup.radius, //圆角
          shadowColor: optionsSetup.shadowColor, // 阴影颜色
          shadowBlur: optionsSetup.shadowBlur //模糊系数
        }
      }
      itemStyle['normal'] = normal
    },
    // 数据解析
    setOptionsData() {
      const optionsSetup = this.optionsSetup
      const optionsData = this.optionsData // 数据类型 静态 or 动态
      optionsData.dataType == 'staticData'
        ? this.staticDataFn(optionsData.staticData)
        : this.dynamicDataFn(
          optionsData.dynamicData,
          optionsData.refreshTime,
          optionsSetup
        )
    },
    // 静态数据
    staticDataFn(val) {
      const optionsSetup = this.optionsSetup
      const series = this.options.series
      const axis = []
      const data = []
      for (const i in val) {
        axis[i] = val[i].axis
        data[i] = val[i].data
      }
      // x轴
      if (optionsSetup.verticalShow) {
        this.options.xAxis.data = []
        this.options.yAxis.data = axis
        this.options.xAxis.type = 'value'
        this.options.yAxis.type = 'category'
      } else {
        this.options.xAxis.data = axis
        this.options.yAxis.data = []
        this.options.xAxis.type = 'category'
        this.options.yAxis.type = 'value'
      }
      if (series[0].type == 'bar') {
        series[0].data = data
      }
    },
    // 动态数据
    dynamicDataFn(val, refreshTime, optionsSetup) {
      if (!val) return
      if (this.ispreview) {
        this.getEchartData(val, optionsSetup)
        this.flagInter = setInterval(() => {
          this.getEchartData(val, optionsSetup)
        }, refreshTime)
      } else {
        this.getEchartData(val, optionsSetup)
      }
    },
    getEchartData(val, optionsSetup) {
      const data = this.queryEchartsData(val)
      data.then(res => {
        this.renderingFn(optionsSetup, res)
      })
    },
    renderingFn(optionsSetup, val) {
      // x轴
      if (optionsSetup.verticalShow) {
        this.options.xAxis.data = []
        this.options.yAxis.data = val.xAxis
        this.options.xAxis.type = 'value'
        this.options.yAxis.type = 'category'
      } else {
        this.options.xAxis.data = val.xAxis
        this.options.yAxis.data = []
        this.options.xAxis.type = 'category'
        this.options.yAxis.type = 'value'
      }

      // series
      const series = this.options.series
      for (const i in series) {
        if (series[i].type == 'bar') {
          series[i].data = val.series[i].data
        }
      }
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
