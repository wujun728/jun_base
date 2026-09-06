<template>
  <div :style="styleObj">
    <v-chart :options="options" autoresize />
  </div>
</template>

<script>
export default {
  name: 'WidgetPieNightingaleRoseArea', //南丁格尔玫瑰图面积模式 参考：https://echarts.apache.org/examples/zh/editor.html?c=pie-roseType-simple
  components: {},
  props: {
    value: Object,
    ispreview: Boolean
  },
  data() {
    return {
      options: {
        legend: {
          top: 'bottom'
        },
        toolbox: {
          show: true,
          feature: {
            mark: { show: true }
          }
        },
        series: [
          {
            //name: "面积模式",
            type: 'pie',
            radius: ['10%', '50%'],
            center: ['50%', '50%'],
            roseType: 'area',
            itemStyle: {
              borderRadius: 8
            },
            data: []
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
      this.setOptionsValue()
      this.setOptionsTooltip()
      this.setOptionsLegend()
      this.setOptionsColor()
      this.setOptionsData()
      this.setOptionsRosetype()
    },
    // 饼图模式 面积模式"area" 半径模式"radius"
    setOptionsRosetype() {
      this.options.series[0]['roseType'] = this.optionsSetup.nightingleRosetype
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
      title.subtext = optionsCollapse.subText
      title.subtextStyle = {
        color: optionsCollapse.subTextColor,
        fontWeight: optionsCollapse.subTextFontWeight,
        fontSize: optionsCollapse.subTextFontSize
      }
      this.options.title = title
    },
    // 数值设定
    setOptionsValue() {
      const optionsCollapse = this.optionsSetup
      const series = this.options.series
      const numberValue = optionsCollapse.numberValue ? '{c}' : ''
      const percentage = optionsCollapse.percentage ? '({d})%' : ''
      const label = {
        show: optionsCollapse.isShow,
        formatter: `{a|{b}：${numberValue} ${percentage}}`,
        rich: {
          a: {
            padding: [-30, 15, -20, 15],
            color: optionsCollapse.subTextColor,
            fontSize: optionsCollapse.fontSize,
            fontWeight: optionsCollapse.fontWeight
          }
        },
        fontSize: optionsCollapse.fontSize,

        fontWeight: optionsCollapse.optionsCollapse
      }
      for (const key in series) {
        if (series[key].type == 'pie') {
          series[key].label = label
          series[key].labelLine = { show: optionsCollapse.isShow }
        }
      }
    },
    // tooltip 设置
    setOptionsTooltip() {
      const optionsCollapse = this.optionsSetup
      const tooltip = {
        trigger: 'item',
        show: true,
        textStyle: {
          color: optionsCollapse.lineColor,
          fontSize: optionsCollapse.fontSize
        }
      }
      this.options.tooltip = tooltip
    },
    // 边距设置
    setOptionsMargin() {
      const optionsCollapse = this.optionsSetup
      const grid = {
        left: optionsCollapse.marginLeft,
        right: optionsCollapse.marginRight,
        bottom: optionsCollapse.marginBottom,
        top: optionsCollapse.marginTop,
        containLabel: true
      }
      this.options.grid = grid
    },
    // 图例操作 legend
    setOptionsLegend() {
      const optionsCollapse = this.optionsSetup
      const legend = this.options.legend
      legend.show = optionsCollapse.isShowLegend
      legend.left = optionsCollapse.lateralPosition == 'left' ? 0 : 'auto'
      legend.right = optionsCollapse.lateralPosition == 'right' ? 0 : 'auto'
      legend.top = optionsCollapse.longitudinalPosition == 'top' ? 0 : 'auto'
      legend.bottom =
        optionsCollapse.longitudinalPosition == 'bottom' ? 0 : 'auto'
      legend.orient = optionsCollapse.layoutFront
      legend.textStyle = {
        color: optionsCollapse.lengedColor,
        fontSize: optionsCollapse.fontSize
      }
      legend.itemWidth = optionsCollapse.lengedWidth
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
