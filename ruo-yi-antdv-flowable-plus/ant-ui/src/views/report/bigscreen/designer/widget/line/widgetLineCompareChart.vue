<template>
  <div :style="styleObj">
    <v-chart :options="options" autoresize/>
  </div>
</template>

<script>
export default {
  name: 'WidgetBarCompareChart',
  //参考 https://www.makeapie.com/editor.html?c=xOjLyozu2W
  components: {},
  props: {
    value: Object,
    ispreview: Boolean
  },
  data() {
    return {
      options: {
        axisPointer: {
          link: {
            xAxisIndex: 'all',
          },
        },
        title: {
          x: 'center',
          textStyle: {
            color: '#ffffff',
          },
        },
        tooltip: {
          show: true,
          trigger: 'axis',
          axisPointer: {
            type: 'line',
            lineStyle: {
              color: '#ffffff',
              type: 'dashed',
            },
          },
          /*axisPointer: {
              type: 'cross',
              lineStyle: {
                  color: '#ffffff',
                  type: 'dashed',
              },
              crossStyle: {
                  color: '#ffffff',
              }
          },*/
        },
        //边距
        grid: [
          {// 上
            left: 30,
            right: 20,
            top: '60px',
            containLabel: true,
            bottom: '50%',
          },
          { // 下
            left: 30,
            containLabel: true,
            right: 20,
            top: '51%',
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
          {//
            gridIndex: 0,
            show: true,
            type: 'category',
            boundaryGap: true, // 居中
            axisLine: { //x轴线
              show: true,
              lineStyle: {
                color: '#ffffff',
              }
            },
            axisTick: { // 刻度
              show: true,
            },
            axisLabel: { // X轴数据
              show: true,
              textStyle: {
                interval: 0,
                color: '#ffffff',
                fontSize: 14
              }
            },
            data: [],
          },
          {//
            gridIndex: 1,
            show: true,
            type: 'category',
            position: 'top',
            boundaryGap: true, // 居中
            axisLine: { // x轴线
              show: true,
              lineStyle: {
                color: '#ffffff',
              }
            },
            axisTick: { // 刻度
              show: true,
            },
            axisLabel: {
              show: false,
              interval: 0,
            },
            data: [],
          },
        ],
        yAxis: [
          {
            gridIndex: 0,
            show: true,
            scale: true, // 是否不从0开始,false从0开始
            axisLabel: {
              show: true,
              textStyle: {
                color: '#ffffff',
                fontSize: 14,
              },
            },
            axisLine: { // 轴线
              show: true,
              lineStyle: {
                color: '#ffffff',
              },
            },
            splitLine: {
              show: false,
              lineStyle: {
                color: '#ffffff',
              },
            },
            axisPointer: {
              snap: true
            },
          },
          {
            gridIndex: 1,
            scale: false, // 是否从0开始
            inverse: true, // 翻转
            axisLabel: {
              show: true,
              textStyle: {
                color: '#ffffff',
                fontSize: 14,
              },
            },
            axisLine: { // 轴线
              show: true,
              lineStyle: {
                color: '#ffffff',
              },
            },
            splitLine: {
              show: false,
              lineStyle: {
                color: '#ffffff',
              },
            },
            axisPointer: {
              snap: true
            },
          },
        ],
        series: [
          {
            name: '',
            type: 'line',
            xAxisIndex: 0,
            yAxisIndex: 0,
            showSymbol: true,// 标记点
            symbol: 'circle',
            symbolSize: 5,
            smooth: true, // 曲线，折线
            itemStyle: {
              color: '#36c5e7',
            },
            lineStyle: {
              color: '#36c5e7',
              width: 2,
            },
            label: { // 数值
              position: 'top',
              distance: 10,
              show: true,
              color: '#36c5e7',
              fontSize: 14,
            },
            data: [],
          },
          {
            name: '',
            type: 'line',
            xAxisIndex: 1,
            yAxisIndex: 1,
            showSymbol: true,// 标记点
            symbol: 'circle',
            symbolSize: 5,
            smooth: true, // 曲线，折线
            itemStyle: {
              color: '#e68b55',
            },
            lineStyle: {
              color: '#e68b55',
              width: 2,
            },
            label: {// 数值
              position: 'bottom',
              distance: 10,
              show: true,
              color: '#e68b55',
              fontSize: 16,
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
      this.setOptionsX()
      this.setOptionsYTop()
      this.setOptionsYBottom()
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
    // X轴设置
    setOptionsX() {
      const optionsSetup = this.optionsSetup
      const xAxis0 = {
        gridIndex: 0,
        show: optionsSetup.isShowX,
        name: optionsSetup.xName, // 坐标轴名称
        nameTextStyle: {
          color: optionsSetup.nameColorX,
          fontSize: optionsSetup.nameFontSizeX
        },
        type: 'category',
        boundaryGap: optionsSetup.boundaryX, // 值居中
        axisLine: { //x轴线
          show: optionsSetup.lineX,
          lineStyle: {
            color: optionsSetup.lineColorX,
          }
        },
        axisTick: { // 刻度
          show: optionsSetup.tickLineX,
        },
        axisLabel: { // X轴数据
          show: true,
          interval: optionsSetup.splitNumberX,
          textStyle: {
            color: optionsSetup.colorX,
            fontSize: optionsSetup.fontSizeX
          }
        },
      }
      const xAxis1 = {
        gridIndex: 1,
        show: optionsSetup.isShowX,
        type: 'category',
        position: 'top',
        boundaryGap: optionsSetup.boundaryX, // 值居中
        axisLine: { // x轴线
          show: optionsSetup.lineX,
          lineStyle: {
            color: optionsSetup.lineColorX,
          }
        },
        axisTick: { // 刻度
          show: optionsSetup.tickLineX,
        },
        axisLabel: {
          show: false,
          interval: optionsSetup.splitNumberX,
        },
      }
      this.options.xAxis[0] = xAxis0
      this.options.xAxis[1] = xAxis1
    },
    // 上y轴设置
    setOptionsYTop() {
      const optionsSetup = this.optionsSetup
      const yAxis = {
        gridIndex: 0,
        splitNumber: optionsSetup.splitNumberYTop,
        show: optionsSetup.isShowYTop,
        scale: optionsSetup.scaleYTop, // 缩放
        name: optionsSetup.textNameYTop, // 坐标轴名称
        nameTextStyle: {
          color: optionsSetup.nameColorYTop,
          fontSize: optionsSetup.namefontSizeYTop
        },
        axisLabel: {
          show: true,
          textStyle: {
            color: optionsSetup.colorYTop,
            fontSize: optionsSetup.fontSizeYTop,
          },
        },
        axisTick: { // 刻度
          show: optionsSetup.tickLineYTop,
        },
        axisLine: { // 轴线
          show: optionsSetup.lineYTop,
          lineStyle: {
            color: optionsSetup.lineColorYTop,
          },
        },
        splitLine: {
          show: optionsSetup.splitLineYTop,
          lineStyle: {
            width: optionsSetup.splitLinefontSizeYTop,
            color: optionsSetup.splitLineColorYTop,
          },
        },
        axisPointer: {
          snap: true
        },
      }
      this.options.yAxis[0] = yAxis
    },
    // 下Y轴设置
    setOptionsYBottom() {
      const optionsSetup = this.optionsSetup
      const yAxis = {
        gridIndex: 1,
        splitNumber: optionsSetup.splitNumberYBottom,
        show: optionsSetup.isShowYBottom,
        scale: optionsSetup.scaleYBottom, // 缩放
        name: optionsSetup.textNameYBottom, // 坐标轴名称
        nameTextStyle: {
          color: optionsSetup.nameColorYBottom,
          fontSize: optionsSetup.namefontSizeYBottom
        },
        inverse: true, // 翻转
        axisLabel: {
          show: true,
          textStyle: {
            color: optionsSetup.colorYBottom,
            fontSize: optionsSetup.fontSizeYBottom,
          },
        },
        axisTick: { // 刻度
          show: optionsSetup.tickLineYBottom,
        },
        axisLine: { // 轴线
          show: optionsSetup.lineYBottom,
          lineStyle: {
            color: optionsSetup.lineColorYBottom,
          },
        },
        splitLine: {
          show: optionsSetup.splitLineYBottom,
          lineStyle: {
            width: optionsSetup.splitLinefontSizeYBottom,
            color: optionsSetup.splitLineColorYBottom,
          },
        },
        axisPointer: {
          snap: true
        },
      }
      this.options.yAxis[1] = yAxis
    },
    // 数值设定 折线设置
    setOptionsTop() {
      const optionsSetup = this.optionsSetup
      const series = this.options.series
      // 折线
      for (const key in series) {
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
      }
      // 数值
      if (series[0].type == 'line') {
        series[0].label = {
          position: 'top',
          distance: 10,
          show: optionsSetup.isShow,
          color: optionsSetup.subTextColor,
          fontSize: optionsSetup.fontSize,
          fontWeight: optionsSetup.fontWeight
        }
        series[1].label = {
          position: 'bottom',
          distance: 10,
          show: optionsSetup.isShow,
          color: optionsSetup.subTextColor,
          fontSize: optionsSetup.fontSize,
          fontWeight: optionsSetup.fontWeight
        }
      }
    },
    // tooltip 提示语设置
    setOptionsTooltip() {
      const optionsSetup = this.optionsSetup
      let tooltip = {}
      if (optionsSetup.tipType == 'line') {
        tooltip = {
          show: optionsSetup.tipShow,
          trigger: 'axis',
          axisPointer: {
            type: optionsSetup.tipType,
            lineStyle: {
              color: optionsSetup.tipColor,
              type: 'dashed',
            },
          },
        }
      } else {
        tooltip = {
          show: optionsSetup.tipShow,
          trigger: 'axis',
          axisPointer: {
            type: optionsSetup.tipType,
            lineStyle: {
              color: optionsSetup.tipColor,
              type: 'dashed',
            },
            crossStyle: {
              color: optionsSetup.tipColor,
            }
          },
        }
      }
      this.options.tooltip = tooltip
    },
    // 边距设置
    setOptionsGrid() {
      const optionsSetup = this.optionsSetup
      const grid = [
        {// 上
          left: optionsSetup.marginLeft,
          right: optionsSetup.marginRight,
          top: optionsSetup.marginTop,
          containLabel: true,
          bottom: '50%',
        },
        { // 下
          left: optionsSetup.marginLeft,
          right: optionsSetup.marginRight,
          containLabel: true,
          top: '51%',
          bottom: optionsSetup.marginBottom,
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
    // 颜色修改、宽度修改
    setOptionsColor() {
      const optionsSetup = this.optionsSetup
      const customColor = optionsSetup.customColor
      const series = this.options.series
      const arrColor = []
      for (let i = 0; i < customColor.length; i++) {
        arrColor.push(customColor[i].color)
      }
      if (!customColor) return
      for (const key in series) {
        const itemStyle = {
          color: arrColor[key],
        }
        const lineStyle = {
          color: arrColor[key],
          width: optionsSetup.lineWidth,
        }
        this.options.series[key].itemStyle = itemStyle
        this.options.series[key].lineStyle = lineStyle
      }
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
      this.options.xAxis[0]['data'] = xAxisList
      this.options.xAxis[1]['data'] = xAxisList
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
      this.options.xAxis[0]['data'] = val.xAxis
      this.options.xAxis[1]['data'] = val.xAxis
      if (val.series[0].type == 'line') {
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
