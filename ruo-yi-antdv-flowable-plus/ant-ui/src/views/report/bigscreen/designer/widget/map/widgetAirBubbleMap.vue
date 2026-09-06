<template>
  <div :style="styleObj">
    <v-chart :options="options" autoresize/>
  </div>
</template>
<script>
import echarts from 'echarts'
import '../../../../../../utils/echarts-map/js/china.js'
//https://www.makeapie.com/editor.html?c=x2yaz6dfRw
//https://www.makeapie.com/editor.html?c=xMpGBbTEKU

var geoCoordMap = {
  '台湾': [121.5135, 25.0308],
  '黑龙江': [127.9688, 45.368],
  '内蒙古': [110.3467, 41.4899],
  '吉林': [125.8154, 44.2584],
  '北京': [116.4551, 40.2539],
  '辽宁': [123.1238, 42.1216],
  '河北': [114.4995, 38.1006],
  '天津': [117.4219, 39.4189],
  '山西': [112.3352, 37.9413],
  '陕西': [109.1162, 34.2004],
  '甘肃': [103.5901, 36.3043],
  '宁夏': [106.3586, 38.1775],
  '青海': [101.4038, 36.8207],
  '新疆': [87.9236, 43.5883],
  '西藏': [91.11, 29.97],
  '四川': [103.9526, 30.7617],
  '重庆': [108.384366, 30.439702],
  '山东': [117.1582, 36.8701],
  '河南': [113.4668, 34.6234],
  '江苏': [118.8062, 31.9208],
  '安徽': [117.29, 32.0581],
  '湖北': [114.3896, 30.6628],
  '浙江': [119.5313, 29.8773],
  '福建': [119.4543, 25.9222],
  '江西': [116.0046, 28.6633],
  '湖南': [113.0823, 28.2568],
  '贵州': [106.6992, 26.7682],
  '云南': [102.9199, 25.4663],
  '广东': [113.12244, 23.009505],
  '广西': [108.479, 23.1152],
  '海南': [110.3893, 19.8516],
  '上海': [121.4648, 31.2891],

}
var data = [
  {
    name: '南海诸岛',
    value: 1
  },
  {
    name: '北京',
    value: 524
  },
  {
    name: '天津',
    value: 14
  },
  {
    name: '上海',
    value: 150
  },
  {
    name: '重庆',
    value: 75
  },
  {
    name: '河北',
    value: 13
  },
  {
    name: '河南',
    value: 83
  },
  {
    name: '云南',
    value: 11
  },
  {
    name: '辽宁',
    value: 19
  },
  {
    name: '黑龙江',
    value: 15
  },
  {
    name: '湖南',
    value: 69
  },
  {
    name: '安徽',
    value: 260
  },
  {
    name: '山东',
    value: 39
  },
  {
    name: '新疆',
    value: 4
  },
  {
    name: '江苏',
    value: 31
  },
  {
    name: '浙江',
    value: 104
  },
  {
    name: '江西',
    value: 36
  },
  {
    name: '湖北',
    value: 1052
  },
  {
    name: '广西',
    value: 33
  },
  {
    name: '甘肃',
    value: 347
  },
  {
    name: '山西',
    value: 8
  },
  {
    name: '内蒙古',
    value: 157
  },
  {
    name: '陕西',
    value: 22
  },
  {
    name: '吉林',
    value: 4
  },
  {
    name: '福建',
    value: 36
  },
  {
    name: '贵州',
    value: 39
  },
  {
    name: '广东',
    value: 996
  },
  {
    name: '青海',
    value: 27
  },
  {
    name: '西藏',
    value: 31
  },
  {
    name: '四川',
    value: 46
  },
  {
    name: '宁夏',
    value: 16
  },
  {
    name: '海南',
    value: 22
  },
  {
    name: '台湾',
    value: 6
  },
  {
    name: '香港',
    value: 2
  },
  {
    name: '澳门',
    value: 9
  },
]
var convertData = function (data) {
  var res = []
  for (var i = 0; i < data.length; i++) {
    var geoCoord = geoCoordMap[data[i].name]
    if (geoCoord) {
      res.push({
        name: data[i].name,
        value: geoCoord.concat(data[i].value)
      })
    }
  }
  return res
}
var max = 6000,
  min = 10
var maxSize4Pin = 100,
  minSize4Pin = 20

export default {
  name: 'WidgetAirBubbleMap',
  props: {
    value: Object,
    ispreview: Boolean
  },
  data() {
    return {
      options: {
        //backgroundColor: '#0F1C3C',
        tooltip: {
          show: true,
          formatter: function (params) {
            if (params.value.length > 1) {
              return '&nbsp;&nbsp;' + params.name + '&nbsp;&nbsp;&nbsp;' + params.value[2] + '&nbsp;&nbsp;'
            } else {
              return '&nbsp;&nbsp;' + params.name + '&nbsp;&nbsp;&nbsp;' + params.value + '&nbsp;&nbsp;'
            }
          },
        },
        geo: {
          map: 'china',
          show: true,
          roam: false,
          label: {
            emphasis: {
              show: false
            }
          },
          layoutSize: '80%',
          itemStyle: {
            normal: {
              borderColor: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                offset: 0,
                color: '#00F6FF'
              }, {
                offset: 1,
                color: '#53D9FF'
              }], false),
              borderWidth: 3,
              shadowColor: 'rgba(10,76,139,1)',
              shadowOffsetY: 0,
              shadowBlur: 60
            }
          }
        },
        series: [
          {
            type: 'map',
            map: 'china',
            aspectScale: 0.75,
            label: {
              normal: {//调整数值
                position: 'right',
                show: true,
                color: '#53D9FF',
                fontSize: 20
              },
              emphasis: {
                show: true,
              },
            },
            itemStyle: {
              normal: {
                //地图块颜色
                areaColor: {
                  x: 0,
                  y: 0,
                  x2: 0,
                  y2: 1,
                  colorStops: [{
                    offset: 0,
                    color: '#073684' // 0% 处的颜色
                  }, {
                    offset: 1,
                    color: '#061E3D' // 100% 处的颜色
                  }],
                },
                borderColor: '#215495',
                borderWidth: 1,
              },
              //鼠标放置颜色加深
              emphasis: {
                areaColor: {
                  x: 0,
                  y: 0,
                  x2: 0,
                  y2: 1,
                  colorStops: [{
                    offset: 0,
                    color: '#073684' // 0% 处的颜色
                  }, {
                    offset: 1,
                    color: '#2B91B7' // 100% 处的颜色
                  }],
                },
              }
            },
            data: data,
          },
          {
            type: 'effectScatter',
            coordinateSystem: 'geo',
            rippleEffect: {
              brushType: 'stroke'
            },
            showEffectOn: 'render',
            itemStyle: {
              normal: {
                //气泡颜色
                color: {
                  type: 'radial',
                  x: 0.5,
                  y: 0.5,
                  r: 0.5,
                  colorStops: [{
                    offset: 0,
                    color: 'rgba(5,80,151,0.2)'
                  }, {
                    offset: 0.8,
                    color: 'rgba(5,80,151,0.8)'
                  }, {
                    offset: 1,
                    color: 'rgba(0,108,255,0.7)'
                  }],
                  global: false
                },
              }
            },
            label: {
              normal: {
                show: true,
                color: '#fff',
                fontWeight: 'bold',
                position: 'inside',
                formatter: function (para) {
                  return '{cnNum|' + para.data.value[2] + '}'
                },
                rich: {
                  cnNum: {
                    fontSize: 13,
                    color: '#D4EEFF',
                  }
                }
              },
            },
            symbol: 'circle',
            symbolSize: function (val) {
              if (val[2] == 0) {
                return 0
              }
              ;
              return ((maxSize4Pin - minSize4Pin) / (max - min)) * val[2] + (maxSize4Pin - ((maxSize4Pin - minSize4Pin) / (max - min)) * max) * 1.2
            },
            data: convertData(data),
            zlevel: 1,
          }]
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
      this.setOptionTextValue()
      //this.setOptionDataValue();
      this.setOptionsData()
      this.setOptionAirSize()
      this.setOptionMapBlocak()
    },
    // 标题设置
    setOptionsTitle() {
      const optionsCollapse = this.optionsSetup
      const title = {}
      title.show = optionsCollapse.isNoTitle
      title.text = optionsCollapse.titleText
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
    setOptionTextValue() {
      const optionsSetup = this.optionsSetup
      const label = this.options.series[0]['label']
      const normal = {
        position: 'right',
        show: true,
        color: optionsSetup.fontTextColor,
        fontSize: optionsSetup.fontTextSize,
        fontWeight: optionsSetup.fontTextWeight,
      }
      label['normal'] = normal
    },
    setOptionMapBlocak() {
      const optionsSetup = this.optionsSetup
      const itemStyle = this.options.series[0]['itemStyle']
      const normal = {
          //地图块颜色
          areaColor: {
            x: 0,
            y: 0,
            x2: 0,
            y2: 1,
            colorStops: [{
              offset: 0,
              color: optionsSetup.font0PreColor // 0% 处的颜色
            }, {
              offset: 1,
              color: optionsSetup.font100PreColor // 100% 处的颜色
            }],
          },
          borderColor: '#215495',
          borderWidth: 1,
        }
      //鼠标放置颜色加深
      const emphasis = {
        areaColor: {
          x: 0,
          y: 0,
          x2: 0,
          y2: 1,
          colorStops: [{
            offset: 0,
            color: '#073684' // 0% 处的颜色
          }, {
            offset: 1,
            color: optionsSetup.fontHighlightColor // 100% 处的颜色
          }],
        },
      }
      itemStyle['normal'] = normal
      itemStyle['emphasis'] = emphasis
    },
    setOptionAirSize() {
      maxSize4Pin = this.optionsSetup.fontmaxSize4Pin
      minSize4Pin = this.optionsSetup.fontminSize4Pin
    },
    //数据解析
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
      this.options.series[0]['data'] = val
      const optionsSetup = this.optionsSetup
      const label = this.options.series[1]['label']
      const normal = {
        show: true,
        color: '#fff',
        fontWeight: 'bold',
        position: 'inside',
        formatter: function (para) {
          return '{cnNum|' + para.data.value[2] + '}'
        },
        rich: {
          cnNum: {
            fontSize: optionsSetup.fontDataSize,
            color: optionsSetup.fontDataColor,
            fontWeight: optionsSetup.fontDataWeight,
          }
        }
      }
      const data = convertData(val)
      this.options.series[1]['data']=data
      label['normal'] = normal
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
      this.options.series[0]['data'] = val
      const optionsSetup = this.optionsSetup
      const label = this.options.series[1]['label']
      const normal = {
        show: true,
        color: '#fff',
        fontWeight: 'bold',
        position: 'inside',
        formatter: function (para) {
          return '{cnNum|' + para.data.value[2] + '}'
        },
        rich: {
          cnNum: {
            fontSize: optionsSetup.fontDataSize,
            color: optionsSetup.fontDataColor,
            fontWeight: optionsSetup.fontDataWeight,
          }
        }
      }
      const data = convertData(val)
      this.options.series[1]['data']=data
      label['normal'] = normal
    }
  }
}
</script>
<style lang="scss" scoped>
.echartMap {
  width: 100%;
  height: 100%;
}

.echarts {
  width: 100%;
  height: 100%;
  overflow: hidden;
}
</style>
