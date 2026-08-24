<!-- 
  @FileDescription: 公共ECharts图表组件
  功能：封装ECharts图表基础功能，支持动态配置、响应式调整和事件交互
-->
<template>
  <!-- 图表容器，使用动态ID和样式 -->
  <div>
    <div :id="echarts" :style="styles" />
  </div>
</template>

<script>
import echarts from 'echarts'

export default {
  name: 'Index',
  props: {
    // 图表容器样式配置
    styles: {
      type: Object,
      default: null
    },
    // 图表类型标识，用于特殊类型处理
    type: {
      type: String,
      default: ''
    },
    // ECharts配置项
    optionData: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      myChart: null,       // ECharts实例
      stopClickLogin: false // 防止重复点击标志
    }
  },
  computed: {
    // 生成随机图表容器ID，避免多实例冲突
    echarts() {
      return 'echarts' + Math.ceil(Math.random() * 100)
    }
  },
  watch: {
    // 深度监听配置项变化，自动更新图表
    optionData: {
      handler(newVal, oldVal) {
        this.handleSetVisitChart()
      },
      deep: true // 对象内部属性的监听，关键。
    }
  },
  mounted: function() {
    const vm = this
    vm.$nextTick(() => {
      vm.handleSetVisitChart()
      // 监听窗口大小变化
      window.addEventListener('resize', this.wsFunc)
    })
  },
  beforeDestroy() {
    // 组件销毁前清理资源
    window.removeEventListener('resize', this.wsFunc)
    if (!this.myChart) {
      return
    }
    // 销毁图表实例
    this.myChart.dispose()
    this.myChart = null
  },
  methods: {
    // 窗口大小变化处理函数
    wsFunc() {
      this.myChart.resize()
    },

    // 初始化并设置图表
    handleSetVisitChart() {
      // 初始化图表实例
      this.myChart = echarts.init(document.getElementById(this.echarts))
      let option = null
      option = this.optionData
      // 应用配置
      this.myChart.setOption(option, true)

      // 绑定点击事件
      this.myChart.on('click', (param) => {
        let that = this
        // 防止重复点击
        if (that.stopClickLogin) {
          return false
        }
        that.stopClickLogin = true

        setTimeout(() => {
          that.stopClickLogin = false
        }, 1000)

        // 触发自定义事件
        that.$emit('pieChange', param.data)
      })
      
      // 非fd类型图表绑定图例选择事件
      if (this.type !== 'fd') {
        // 先移除旧监听器
        this.myChart.off('legendselectchanged')
        // 绑定新监听器
        this.myChart.on('legendselectchanged', (e) => {
          let that = this
          let index = null

          // 触发图例选择动作
          that.myChart.dispatchAction({
            type: 'legendSelect',
            name: e.name
          })
          // 查找对应图例项
          index = option.series[0].data.findIndex((item) => item.name === e.name)
          e.id = option.series[0].data[index].id

          // 触发自定义事件
          that.$emit('pieChange', e)
        })
      }
    }
  }
}
</script>

<style scoped>
/* 基础样式 */
</style>
