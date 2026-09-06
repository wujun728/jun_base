<template>
  <div :style="styleObj">
    <v-chart :options="options" autoresize/>
  </div>
</template>

<script>
export default {
  name: 'WidgetFunnel',
  components: {},
  props: {
    value: Object,
    ispreview: Boolean
  },
  data() {
    return {
      options: {
        color: [],
        title: {
          text: '',
          textStyle: {
            color: '#fff'
          }
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c}'
        },
        legend: {
          x: 'center',
          y: '92%',
          textStyle: {
            color: '#fff'
          }
        },
        series: [
          {
            name: '',
            type: 'funnel',
            left: 'center',
            width: '80%',
            //maxSize: '80%',
            sort: 'descending',
            label: {
              normal: {
                show: true,
                position: 'inside',
                formatter: '{c}',
                textStyle: {
                  color: '#fff',
                  fontSize: 14,
                }
              },
              emphasis: {
                position: 'inside',
                formatter: '{b}: {c}'
              }
            },
            itemStyle: {
              normal: {
                opacity: 0.8,
                borderColor: 'rgba(12, 13, 43, .9)',
                borderWidth: 1,
                shadowBlur: 4,
                shadowOffsetX: 0,
                shadowOffsetY: 0,
                shadowColor: 'rgba(0, 0, 0, .6)'
              }
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
      this.setEnding()
      this.setOptionsText()
      this.setOptionsTitle()
      this.setOptionsTooltip()
      this.setOptionsLegend()
      this.setOptionsColor()
      this.setOptionsData()
    },
    // 翻转
    setEnding() {
      const optionsSetup = this.optionsSetup
      const series = this.options.series
      if (optionsSetup.ending) {
        series[0].sort = 'ascending'
      } else {
        series[0].sort = 'descending'
      }
    },
    // 数值设置
    setOptionsText() {
      const optionsSetup = this.optionsSetup
      const normal = {
        show: optionsSetup.isShow,
        position: 'inside',
        formatter: '{c}',
        textStyle: {
          color: optionsSetup.color,
          fontSize: optionsSetup.fontSize,
          fontWeight: optionsSetup.fontWeight,
        }
      }
      this.options.series[0].label['normal'] = normal
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
    // 提示语设置 tooltip
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
    // 图例操作 legend
    setOptionsLegend() {
      const optionsSetup = this.optionsSetup
      const legend = this.options.legend
      legend.show = optionsSetup.isShowLegend
      legend.left = optionsSetup.lateralPosition
      legend.right = optionsSetup.lateralPosition
      legend.top = optionsSetup.longitudinalPosition
      legend.bottom =
        optionsSetup.longitudinalPosition
      legend.orient = optionsSetup.layoutFront
      legend.textStyle = {
        color: optionsSetup.lengedColor,
        fontSize: optionsSetup.lengedFontSize
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
        if (this.options.series[key].type == 'funnel') {
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
        if (this.options.series[key].type == 'funnel') {
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
