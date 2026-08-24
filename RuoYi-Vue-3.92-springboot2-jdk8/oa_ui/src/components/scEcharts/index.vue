<!-- @FileDescription: 图表设计-echarts组件 -->
<template>
  <div @click="sizechange" ref="scEcharts" :style="{ height: height, width: width }"></div>
</template>

<script>
import * as echarts from 'echarts'
import T from './echarts-theme-T.js'
import 'echarts-liquidfill'
echarts.registerTheme('T', T)
const unwarp = (obj) => obj && (obj.__v_raw || obj.valueOf() || obj)
import { EventBus } from '@/libs/bus'

export default {
  ...echarts,
  name: 'scEcharts',
  props: {
    height: { type: String, default: '100%' },
    width: { type: String, default: '100%' },
    nodata: { type: Boolean, default: false },
    option: { type: Object, default: () => {} },
    field: { type: Object, default: () => {} },
    designer: { type: Object, default: () => {} }
  },
  data() {
    return {
      isActivat: false,
      myChart: null
    }
  },
  inject: ['previewState', 'getScreenWidth'],
  watch: {
    option: {
      deep: true,
      handler(v) {
        unwarp(this.myChart)?.clear()
        unwarp(this.myChart)?.setOption(v)
      }
    },
    field: {
      deep: true,
      handler(v) {
        this.draw()
      }
    },
    injectId(val) {
      // 屏幕宽度变化停止时,重新绘制 进行防抖处理
      clearTimeout(this.resizeTimer)
      this.resizeTimer = setTimeout(() => {
        this.draw()
      }, 200)
    }
  },
  computed: {
    myOptions: function () {
      return this.option || {}
    },
    injectId() {
      return this.getScreenWidth()
    }
  },
  activated() {
    if (!this.isActivat) {
      this.$nextTick(() => {
        this.myChart.resize()
      })
    }
  },
  deactivated() {
    this.isActivat = false
  },
  mounted() {
    this.isActivat = true
    setTimeout(() => {
      this.draw()
    }, 500)
  },
  methods: {
    sizechange() {
      // 修改 echart 大小
      this.myChart.resize()
    },
    draw() {
      var myChart = echarts.init(this.$refs.scEcharts, 'T')

      myChart.setOption(this.myOptions)
      this.myChart = myChart
      setTimeout(() => {
        //由于网格布局拖拽放大缩小图表不能自适应，这里设置一个定时器使得echart加载为一个异步过程，需要点击一下才能实现自适应(还需优化)
        this.$nextTick(() => {
          this.sizechange()
        })
      }, 0)

      // 点击图表下钻
      if (['barXChart', 'barChart', 'pieChart'].includes(this.field.type) && this.previewState) {
        this.myChart.on('click', (params) => {
          if (this.field.type === 'pieChart') {
            EventBus.$emit('pieChangeValue', params.data?.dim_value || '', true)
          } else if (this.myOptions.other && !Array.isArray(this.myOptions.other) && this.myOptions.other.dim_value) {
            let name = this.field.type == 'barChart' ? 'barChangeValue' : 'barXChangeValue'
            EventBus.$emit(name, this.myOptions.other.dim_value[params.dataIndex] || '', true)
          }
        })
      }
    }
  }
}
</script>
