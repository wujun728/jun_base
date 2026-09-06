<template>
  <div :style="styleObj">
    <v-chart :options="options" autoresize/>
  </div>
</template>

<script>
export default {
  name: 'WidgetBarStackchart',
  components: {},
  props: {
    value: Object,
    ispreview: Boolean
  },
  data() {
    return {
      options: {
        grid: {},
        legend: {
          textStyle: {
            color: '#fff'
          }
        },
        xAxis: {
          type: 'category',
          data: [],
          axisLabel: {
            show: true,
            textStyle: {
              color: '#fff'
            }
          }
        },
        yAxis: {
          type: 'value',
          data: [],
          axisLabel: {
            show: true,
            textStyle: {
              color: '#fff'
            }
          }
        },
        series: [
          {
            data: [],
            name: '',
            type: 'line',
            barGap: '0%',
            itemStyle: {
              barBorderRadius: null
            }
          }
        ]
      },
      optionsStyle: {}, // 样式
      optionsData: {}, // 数据
      optionsSetup: {},
      flagInter: null
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
      this.setOptionsTooltip()
      this.setOptionsMargin()
      this.setOptionsLegend()
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
          color: optionsSetup.xNameColor,
          fontSize: optionsSetup.xNameFontSize
        },
        nameRotate: optionsSetup.textAngleX, // 文字角度
        inverse: optionsSetup.reversalX, // 轴反转
        axisLabel: {
          show: true,
          interval: optionsSetup.textInterval, // 文字角度
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
        show: optionsSetup.isShowY, // 坐标轴是否显示
        name: optionsSetup.textNameY, // 坐标轴名称
        nameTextStyle: {
          color: optionsSetup.NameColorY,
          fontSize: optionsSetup.NameFontSizeY
        },
        inverse: optionsSetup.reversalY, // y轴反转
        axisLabel: {
          show: true,
          rotate: optionsSetup.textAngleY,// 文字角度
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
    // 获取面积
    getOptionArea() {
      const optionsSetup = this.optionsSetup
      let areaStyle = []
      if (optionsSetup.area) {
        areaStyle = {
          opacity: optionsSetup.areaThickness / 100
        }
      } else {
        areaStyle = {
          opacity: 0
        }
      }
      return areaStyle
    },
    // tooltip 提示语设置，鼠标放置显示
    setOptionsTooltip() {
      const optionsSetup = this.optionsSetup
      const tooltip = {
        trigger: 'item',
        show: true,
        textStyle: {
          color: optionsSetup.lineColor,
          fontSize: optionsSetup.tipsFontSize
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
      legend.left = optionsSetup.lateralPosition
      legend.top = optionsSetup.longitudinalPosition == 'top' ? 0 : 'auto'
      legend.bottom =
        optionsSetup.longitudinalPosition == 'bottom' ? 0 : 'auto'
      legend.orient = optionsSetup.layoutFront
      legend.textStyle = {
        color: optionsSetup.lengedColor,
        fontSize: optionsSetup.lengedFontSize
      }
      legend.itemWidth = optionsSetup.lengedWidth
    },
    // 图例颜色修改
    setOptionsColor() {
      const optionsCollapse = this.optionsSetup
      const customColor = optionsCollapse.customColor
      if (!customColor) return
      const arrColor = []
      for (let i = 0; i < customColor.length; i++) {
        arrColor.push(customColor[i].color)
      }
      this.options.color = arrColor
      this.options = Object.assign({}, this.options)
    },
    // 数据解析
    setOptionsData() {
      const optionsSetup = this.optionsSetup
      const optionsData = this.optionsData // 数据类型 静态 or 动态
      optionsData.dataType == 'staticData'
        ? this.staticDataFn(optionsData.staticData, optionsSetup)
        : this.dynamicDataFn(
        optionsData.dynamicData,
        optionsData.refreshTime,
        optionsSetup
        )
    },
    //去重
    setUnique(arr) {
      const newArr = []
      arr.forEach(item => {
        return newArr.includes(item) ? '' : newArr.push(item)
      })
      return newArr
    },
    //静态数据
    staticDataFn(val) {
      const optionsSetup = this.optionsSetup
      //颜色
      const customColor = optionsSetup.customColor
      const arrColor = []
      for (let i = 0; i < customColor.length; i++) {
        arrColor.push(customColor[i].color)
      }
      //数据
      const series = []
      let xAxisList = []
      let yAxisList = []
      for (const i in val) {
        xAxisList[i] = val[i].axis
        yAxisList[i] = val[i].name
      }
      xAxisList = this.setUnique(xAxisList) // x轴 0725 0726 0727
      yAxisList = this.setUnique(yAxisList) // y轴 A B C
      for (const i in yAxisList) {
        const data = new Array(yAxisList.length).fill(0)
        for (const j in xAxisList) {
          for (const k in val) {
            if (val[k].name == yAxisList[i]) { // a = a
              if (val[k].axis == xAxisList[j]) { // 0725
                data[j] = val[k].data
              }
            }
          }
        }
        series.push({
          name: yAxisList[i],
          type: 'line',
          data: data,
          width: optionsSetup.lineWidth,
          symbol: 'circle',
          showSymbol: optionsSetup.markPoint,
          symbolSize: optionsSetup.pointSize,
          smooth: optionsSetup.smoothCurve,
          // 线条
          lineStyle: {
            color: arrColor[i],
            width: optionsSetup.lineWidth
          },
          //点
          itemStyle: {
            color: arrColor[i],
          },
          areaStyle: this.getOptionArea(),
          //标题部分
          label: {
            show: optionsSetup.isShow,
            position: 'top',
            distance: 10,
            fontSize: optionsSetup.fontSize,
            color: optionsSetup.subTextColor,
            fontWeight: optionsSetup.fontWeight
          },
        })
      }
      this.options.series = series
      if (optionsSetup.verticalShow) {
        this.options.xAxis.data = []
        this.options.yAxis.data = xAxisList
        this.options.xAxis.type = 'value'
        this.options.yAxis.type = 'category'
      } else {
        this.options.xAxis.data = xAxisList
        this.options.yAxis.data = []
        this.options.xAxis.type = 'category'
        this.options.yAxis.type = 'value'
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
      //颜色
      const customColor = optionsSetup.customColor
      const arrColor = []
      for (let i = 0; i < customColor.length; i++) {
        arrColor.push(customColor[i].color)
      }
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
      const series = []
      for (const i in val.series) {
        if (val.series[i].type == 'line') {
          series.push({
            name: val.series[i].name,
            type: 'line',
            data: val.series[i].data,
            width: optionsSetup.lineWidth,
            symbol: 'circle',
            showSymbol: optionsSetup.markPoint,
            symbolSize: optionsSetup.pointSize,
            symbolColor: arrColor[i],
            smooth: optionsSetup.smoothCurve,
            // 线条
            lineStyle: {
              color: arrColor[i],
              width: optionsSetup.lineWidth
            },
            //点
            itemStyle: {
              color: arrColor[i],
            },
            areaStyle: this.getOptionArea(),
            // 标题部分
            label: {
              show: optionsSetup.isShow,
              position: 'top',
              distance: 10,
              fontSize: optionsSetup.fontSize,
              color: optionsSetup.subTextColor,
              fontWeight: optionsSetup.fontWeight
            },
          })
        }
      }
      this.options.series = series
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
