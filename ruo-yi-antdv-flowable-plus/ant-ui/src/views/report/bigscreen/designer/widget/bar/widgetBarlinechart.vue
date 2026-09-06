<template>
  <div :style="styleObj">
    <v-chart :options="options" autoresize/>
  </div>
</template>

<script>
export default {
  name: 'WidgetBarlinechart',
  components: {},
  props: {
    value: Object,
    ispreview: Boolean
  },
  data() {
    return {
      options: {
        color: [],
        grid: {},
        title: {
          text: '',
          textStyle: {
            color: '#fff'
          }
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c}%'
        },
        legend: {
          textStyle: {
            color: '#fff'
          },
        },
        xAxis: [
          {
            type: 'category',
            data: [],
            axisLabel: {
              show: false,
              textStyle: {
                color: '#fff'
              }
            }
          }
        ],
        yAxis: [
          {
            type: 'value',
            name: '',
            min: 0,
            max: 250,
            interval: 50,
            axisLabel: {
              show: true,
              textStyle: {
                color: '#fff'
              }
            }
          },
          {
            type: 'value',
            name: '',
            min: 0,
            max: 25,
            interval: 5,
            axisLabel: {
              show: true,
              textStyle: {
                color: '#fff'
              }
            }
          }
        ],
        series: [
          {
            name: '',
            type: 'bar',
            data: [],
            itemStyle: {
              barBorderRadius: null
            }
          },
          {
            name: '',
            type: 'line',
            yAxisIndex: 1,
            data: [],
            itemStyle: {}
          }
        ]
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
  methods: {
    // 修改图标options属性
    editorOptions() {
      this.setOptionsTitle()
      this.setOptionsX()
      this.setOptionsY()
      this.setOptionsTop()
      this.setOptionsBar()
      this.setOptionsTooltip()
      this.setOptionsData()
      this.setOptionsMargin()
      //this.setOptionsLegend();
      this.setOptionsColor()
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
      const yAxis = [
        {
          type: 'value',
          splitNumber: optionsSetup.splitNumberLeft,// 均分
          show: optionsSetup.isShowYLeft, // 坐标轴是否显示
          name: optionsSetup.textNameYLeft, // 坐标轴名称
          nameTextStyle: { // 别名
            color: optionsSetup.nameColorYLeft,
            fontSize: optionsSetup.namefontSizeYLeft
          },
          inverse: optionsSetup.reversalY, // 轴反转
          axisLabel: {
            show: true,
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
            show: false,
          }
        },
        {
          type: 'value',
          splitNumber: optionsSetup.splitNumberRight,// 均分
          show: optionsSetup.isShowYRight, // 坐标轴是否显示
          name: optionsSetup.textNameYRight, // 坐标轴名称
          nameTextStyle: { // 别名
            color: optionsSetup.nameColorYRight,
            fontSize: optionsSetup.namefontSizeYRight
          },
          inverse: optionsSetup.reversalY, // 轴反转
          axisLabel: {
            show: true,
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
            show: false,
          }
        }
      ]
      this.options.yAxis = yAxis
    },
    // 折线设置 数值设置
    setOptionsTop() {
      const optionsSetup = this.optionsSetup
      const series = this.options.series
      for (const key in series) {
        if (series[key].type == 'line') {
          series[key].showSymbol = optionsSetup.markPoint
          series[key].symbolSize = optionsSetup.pointSize
          series[key].smooth = optionsSetup.smoothCurve
          if (optionsSetup.area) {
            series[key].areaStyle = {
              opacity: optionsSetup.areaThickness / 100
            }
          } else {
            series[key].areaStyle = {
              opacity: 0
            }
          }
          series[key].lineStyle = {
            width: optionsSetup.lineWidth
          }
          series[key].itemStyle.borderRadius = optionsSetup.radius
          series[key].label = {
            show: optionsSetup.isShowLine,
            position: 'top',
            distance: optionsSetup.distanceLine,
            fontSize: optionsSetup.fontSizeLine,
            color: optionsSetup.subTextColorLine,
            fontWeight: optionsSetup.fontWeightLine
          }
        }
      }
      this.options.series = series
    },
    // 柱体设置 数值设置
    setOptionsBar() {
      const optionsSetup = this.optionsSetup
      const series = this.options.series
      for (const key in series) {
        if (series[key].type == 'bar') {
          series[key].label = {
            show: optionsSetup.isShowBar,
            position: 'top',
            distance: optionsSetup.distanceBar,
            fontSize: optionsSetup.fontSizeBar,
            color: optionsSetup.subTextColorBar,
            fontWeight: optionsSetup.fontWeightBar
          }
          series[key].barWidth = optionsSetup.maxWidth
          series[key].barMinHeight = optionsSetup.minHeight
          series[key].itemStyle.barBorderRadius = optionsSetup.radius
        }
      }
      this.options.series = series
    },
    // tooltip 设置
    setOptionsTooltip() {
      const optionsSetup = this.optionsSetup
      const tooltip = {
        trigger: 'item',
        show: true,
        textStyle: {
          color: optionsSetup.lineColor,
          fontSize: optionsSetup.tipFontSize
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
    // 图例颜色修改
    setOptionsColor() {
      const optionsSetup = this.optionsSetup
      const customColor = optionsSetup.customColor
      if (!customColor) return
      const arrColor = []
      for (let i = 0; i < customColor.length; i++) {
        arrColor.push(customColor[i].color)
      }
      this.options.color = arrColor
      this.options = Object.assign({}, this.options)
    },
    // 数据处理
    setOptionsData() {
      const optionsData = this.optionsData // 数据类型 静态 or 动态
      optionsData.dataType == 'staticData'
        ? this.staticDataFn(optionsData.staticData)
        : this.dynamicDataFn(optionsData.dynamicData, optionsData.refreshTime)
    },
    staticDataFn(val) {
      const series = this.options.series
      const axis = []
      const bar = []
      const line = []
      for (const i in val) {
        axis[i] = val[i].axis
        bar[i] = val[i].bar
        line[i] = val[i].line
      }
      // x轴
      this.options.xAxis.data = axis
      // series
      for (const i in series) {
        if (series[i].type == 'bar') {
          series[i].data = bar
        } else {
          series[i].data = line
        }
      }
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
      this.options.xAxis.data = val.xAxis
      // series
      const series = this.options.series
      for (const i in series) {
        for (const j in val.series) {
          if (series[i].type == val.series[j].type) {
            series[i].data = val.series[j].data
          }
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
