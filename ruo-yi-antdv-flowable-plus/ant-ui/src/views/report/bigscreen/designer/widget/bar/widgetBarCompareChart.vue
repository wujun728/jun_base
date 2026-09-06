<template>
  <div :style="styleObj">
    <v-chart :options="options" autoresize/>
  </div>
</template>

<script>
export default {
  name: 'WidgetBarCompareChart',
  //参考 https://www.makeapie.com/editor.html?c=xrJwcCF3NZ
  components: {},
  props: {
    value: Object,
    ispreview: Boolean
  },
  data() {
    return {
      options: {
        title: {
          //text: '柱状对比图',
          x: 'center',
          textStyle: {
            color: '#ffffff',
          },
        },
        //边距
        grid: [
          {//左
            show: false,//边框线
            left: '4%',
            top: 60,
            bottom: 10,
            containLabel: true,
            width: '40%'
          },
          {//中间字体位置
            show: false,
            left: '50.5%',
            top: 60,
            bottom: 25,
            width: '0%'
          },
          {//右
            show: false,
            right: '4%',
            top: 60,
            bottom: 10,
            containLabel: true,
            width: '40%'
          },
        ],
        //图例
        legend: {
          textStyle: {
            color: '#fff',
            textAlign: 'center'
          },
          //itemGap:80,
          //itemWidth: 0
        },
        xAxis: [
          {// 左
            splitNumber: 2,
            show: true,
            type: 'value',
            inverse: true,
            axisLine: {//底分割线
              show: false,
            },
            axisTick: {
              show: false,
            },
            position: 'bottom',
            axisLabel: { // x轴
              show: true,
              textStyle: {
                color: '#ffffff',
                fontSize: 12
              }
            },
            splitLine: { // 竖分割线
              show: true,
              lineStyle: {
                color: '#57617f',
                width: 1,
                type: 'solid'
              }
            }
          },
          {
            gridIndex: 1,
            show: false,
          },
          {// 右
            gridIndex: 2,
            show: true,
            type: 'value',
            axisLine: {
              show: false,
            },
            axisTick: {
              show: false,
            },
            position: 'bottom',
            axisLabel: {
              show: true,
              textStyle: {
                color: '#ffffff',
                fontSize: 12,
              },
            },
            splitLine: {
              show: true,
              lineStyle: {
                color: '#57617f',
                width: 1,
                type: 'solid',
              },
            },
          },
        ],
        yAxis: [
          {
            type: 'category',
            inverse: true,
            position: 'right',
            axisLine: {
              show: false,
            },
            axisTick: {
              show: false
            },
            axisLabel: {
              show: false,
            },
            data: [],
          },
          {//处理轴数据
            gridIndex: 1,
            type: 'category',
            inverse: true,
            position: 'left',
            axisLine: {
              show: false
            },
            axisTick: {
              show: false
            },
            axisLabel: {
              show: true,
              textStyle: {
                align: 'center',
                color: '#ffffff',
                fontSize: 14,
              }
            },
            data: [],
          },
          {
            gridIndex: 2,
            type: 'category',
            inverse: true,
            position: 'left',
            axisLine: {
              show: false
            },
            axisTick: {
              show: false
            },
            axisLabel: {
              show: false,
            },
          },
        ],
        series: [
          {
            name: '',
            type: 'bar',
            barGap: 20,
            barWidth: 15,
            label: {
              normal: {
                show: true,
                color: 'red',
                position: 'insideLeft',
                textStyle: {
                  color: '#ffffff',
                }

              },
              emphasis: {
                show: false,
              },
            },
            itemStyle: {
              normal: {
                color: '#36c5e7',
                barBorderRadius: [8, 0, 0, 8],
              },
              emphasis: {
                show: false,
              },
            },
            data: [],
          },
          {
            name: '',
            type: 'bar',
            barGap: 20,
            barWidth: 15,
            xAxisIndex: 2,
            yAxisIndex: 2,
            label: {
              normal: {
                show: true,
                color: 'red',
                position: 'insideRight',
                textStyle: {
                  color: '#ffffff',
                }
              },
            },
            itemStyle: {
              normal: {
                color: '#e68b55',
                barBorderRadius: [0, 8, 8, 0],
              },
              emphasis: {
                show: false,
              },
            },
            data: [],
          },
        ]
      },
      optionsStyle: {},
      optionsData: {},
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
      this.setOptionsXLeft()
      this.setOptionsXRight()
      this.setOptionsY()
      this.setOptionsTop()
      this.setOptionsTooltip()
      this.setOptionsGrid()
      this.setOptionsLegend()
      this.setOptionsColor()
      this.setOptionsData()
    },
    // 标题修改
    setOptionsTitle() {
      const optionsCollapse = this.optionsSetup
      const title = {}
      title.text = optionsCollapse.titleText
      title.show = optionsCollapse.isNoTitle
      title.left = optionsCollapse.textAlign
      title.textStyle = {
        color: optionsCollapse.textColor,
        fontSize: optionsCollapse.textFontSize,
        fontWeight: optionsCollapse.textFontWeight
      }
      this.options.title = title
    },
    // 左X轴设置
    setOptionsXLeft() {
      const optionsSetup = this.optionsSetup
      const xAxisLeft = {
        splitNumber: optionsSetup.splitNumberLeft,
        type: 'value',
        show : optionsSetup.hideXLeft,
        inverse: true,
        axisLine: {//X轴线
          show: optionsSetup.xLineLeft,
          lineStyle: {
            color: optionsSetup.lineColorXLeft,
          },
        },
        axisTick: {
          show: optionsSetup.tickLineLeft,
        },
        position: 'bottom',
        axisLabel: { // x轴
          show: true,
          textStyle: {
            color: optionsSetup.XcolorLeft,
            fontSize: optionsSetup.fontSizeXLeft
          }
        },
        splitLine: { // 分割线
          show: optionsSetup.SplitLineLeft,
          lineStyle: {
            color: optionsSetup.SplitLineColorLeft,
            width: optionsSetup.SplitLinefontSizeLeft,
            type: 'solid'
          }
        }
      }
      this.options.xAxis[0] = xAxisLeft
    },
    // 右X轴设置
    setOptionsXRight() {
      const optionsSetup = this.optionsSetup
      const xAxisRight = {
        gridIndex: 2,
        splitNumber: optionsSetup.splitNumberRight,
        show : optionsSetup.hideXRight,
        type: 'value',
        axisLine: {//X轴线
          show: optionsSetup.xLineRight,
          lineStyle: {
            color: optionsSetup.lineColorXRight,
          },
        },
        axisTick: {
          show: optionsSetup.tickLineRight,
        },
        position: 'bottom',
        axisLabel: { // x轴
          show: true,
          textStyle: {
            color: optionsSetup.XcolorRight,
            fontSize: optionsSetup.fontSizeXRight
          }
        },
        splitLine: { // 分割线
          show: optionsSetup.SplitLineRight,
          lineStyle: {
            color: optionsSetup.SplitLineColorRight,
            width: optionsSetup.SplitLinefontSizeRight,
            type: 'solid'
          }
        }
      }
      this.options.xAxis[2] = xAxisRight
    },
    // Y轴设置
    setOptionsY() {
      const optionsSetup = this.optionsSetup
      const axisLine = {
        show: optionsSetup.lineY,
        lineStyle: {
          color: optionsSetup.lineColorY
        }
      }
      const axisTick = {
        show: optionsSetup.tickLineY
      }
      const axisLabel = {
        show: optionsSetup.hideY,
        textStyle: {
          align: optionsSetup.textAlign,
          color: optionsSetup.colorY,
          fontSize: optionsSetup.fontSizeY,
        }
      }
      this.options.yAxis[1]['axisLine'] = axisLine
      this.options.yAxis[1]['axisTick'] = axisTick
      this.options.yAxis[1]['axisLabel'] = axisLabel
    },
    // 数值设定、柱体设置
    setOptionsTop() {
      const optionsSetup = this.optionsSetup
      const series = this.options.series
      for (const key in series) {
        if (series[key].type == 'bar') {
          series[0].label = {
            normal: {
              show: optionsSetup.isShow,
              position: 'insideLeft',
              textStyle: {
                fontSize: optionsSetup.fontSize,
                color: optionsSetup.subTextColor,
                fontWeight: optionsSetup.fontWeight
              }
            },
            emphasis: {
              show: false,
            },
          },
            series[1].label = {
              normal: {
                show: optionsSetup.isShow,
                color: 'red',
                position: 'insideRight',
                textStyle: {
                  fontSize: optionsSetup.fontSize,
                  color: optionsSetup.subTextColor,
                  fontWeight: optionsSetup.fontWeight
                }
              },
              emphasis: {
                show: false,
              },
            },
            series[key].barWidth = optionsSetup.maxWidth
        }
      }
      this.options.series = series
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
    setOptionsGrid() {
      const optionsSetup = this.optionsSetup
      const grid = [
        {//左
          show: optionsSetup.frameLineLeft,
          left: optionsSetup.marginLeftRight,
          top: optionsSetup.marginTop,
          bottom: optionsSetup.marginBottom,
          containLabel: true,
          width: '40%'
        },
        {//中间字体位置
          show: false,
          left: '51%',
          top: optionsSetup.marginTop,
          bottom: optionsSetup.marginBottom + 15,
          width: '0%'
        },
        {//右
          show: optionsSetup.frameLineRight,
          right: optionsSetup.marginLeftRight,
          top: optionsSetup.marginTop,
          bottom: optionsSetup.marginBottom,
          containLabel: true,
          width: '40%'
        },
      ]
      this.options.grid = grid
    },
    // 图例操作
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
    // 颜色修改、圆角修改
    setOptionsColor() {
      const optionsSetup = this.optionsSetup
      const customColor = optionsSetup.customColor
      if (!customColor) return
      const itemStyleLeft = {
        normal: {
          color: customColor[0].color,
          barBorderRadius: [optionsSetup.radius, 0, 0, optionsSetup.radius]
        },
        emphasis: {
          show: false,
        },
      }
      const itemStyleRight = {
        normal: {
          color: customColor[1].color,
          barBorderRadius: [0, optionsSetup.radius, optionsSetup.radius, 0]
        },
        emphasis: {
          show: false,
        },
      }
      this.options.series[0].itemStyle = itemStyleLeft
      this.options.series[1].itemStyle = itemStyleRight
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
      //数据
      let xAxisList = []
      let yAxisList = []
      const arrayList = []
      for (const i in val) {
        xAxisList[i] = val[i].axis
        yAxisList[i] = val[i].name
      }
      xAxisList = this.setUnique(xAxisList)
      yAxisList = this.setUnique(yAxisList)
      for (const i in yAxisList) {
        const data = new Array(yAxisList.length).fill(0)
        for (const j in xAxisList) {
          for (const k in val) {
            if (val[k].name == yAxisList[i]) {
              if (val[k].axis == xAxisList[j]) {
                data[j] = val[k].data
              }
            }
          }
        }
        arrayList.push({
          name: yAxisList[i],
          data: data
        })
      }
      this.options.series[0]['name'] = arrayList[0].name
      this.options.series[0]['data'] = arrayList[0].data
      this.options.series[1]['name'] = arrayList[1].name
      this.options.series[1]['data'] = arrayList[1].data
      this.options.yAxis[1]['data'] = xAxisList
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
      this.options.yAxis[1]['data'] = val.xAxis
      if (val.series[0].type == 'bar'){
        this.options.series[0]['name'] = val.series[0].name
        this.options.series[0]['data'] = val.series[0].data
        this.options.series[1]['name'] = val.series[1].name
        this.options.series[1]['data'] = val.series[1].data
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
