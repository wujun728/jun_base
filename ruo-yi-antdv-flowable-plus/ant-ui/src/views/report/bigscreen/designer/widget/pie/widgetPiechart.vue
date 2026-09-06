<template>
  <div :style="styleObj">
    <v-chart :options="options" autoresize/>
  </div>
</template>

<script>
export default {
  name: 'WidgetPiechart',
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
          left: 'center',
          textStyle: {
            color: '#fff'
          }
        },
        legend: {
          orient: 'vertical',
          left: 'left',
          textStyle: {
            color: '#fff'
          }
        },
        series: [
          {
            type: 'pie',
            radius: '50%',
            data: [],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
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
      this.setOptionsValue()
      this.setOptionsTooltip()
      this.setOptionsLegend()
      this.setOptionsColor()
      this.setOptionsData()
      this.setOptionsPiechartStyle()
    },
    // 饼图样式
    setOptionsPiechartStyle() {
      if (this.optionsSetup.piechartStyle == 'shixin') {
        this.options.series[0]['radius'] = '50%'
      } else if (this.optionsSetup.piechartStyle == 'kongxin') {
        this.options.series[0]['radius'] = ['40%', '70%']
      } else {
      }
    },
    // 标题设置
    setOptionsTitle() {
      const optionsSetup = this.optionsSetup
      const title = {}
      title.show = optionsSetup.isNoTitle
      title.text = optionsSetup.titleText
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
    // 数值设定
    setOptionsValue() {
      const optionsSetup = this.optionsSetup
      const series = this.options.series
      const numberValue = optionsSetup.numberValue ? '{c}' : ''
      const percentage = optionsSetup.percentage ? '({d})%' : ''
      const label = {
        show: optionsSetup.isShow,
        formatter: `{a|{b}：${numberValue} ${percentage}}`,
        rich: {
          a: {
            padding: [-30, 15, -20, 15],
            color: optionsSetup.subTextColor,
            fontSize: optionsSetup.fontSize,
            fontWeight: optionsSetup.fontWeight
          }
        },
        fontSize: optionsSetup.fontSize,

        fontWeight: optionsSetup.optionsSetup
      }
      for (const key in series) {
        if (series[key].type == 'pie') {
          series[key].label = label
          series[key].labelLine = {show: optionsSetup.isShow}
        }
      }
    },
    // 提示语设置 tooltip
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
    setOptionsData() {
      const optionsData = this.optionsData // 数据类型 静态 or 动态
      optionsData.dataType == 'staticData'
        ? this.staticDataFn(optionsData.staticData)
        : this.dynamicDataFn(optionsData.dynamicData, optionsData.refreshTime)
    },
    staticDataFn(val) {
      const staticData = typeof val == 'string' ? JSON.parse(val) : val
      for (const key in this.options.series) {
        if (this.options.series[key].type == 'pie') {
          this.options.series[key].data = staticData
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
      for (const key in this.options.series) {
        if (this.options.series[key].type == 'pie') {
          this.options.series[key].data = val
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
