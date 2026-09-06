<template>
  <div class="text" :style="styleColor">
    <marquee behavior="" :direction="styleColor.direction">{{
      styleColor.text
    }}</marquee>
  </div>
</template>

<script>
export default {
  name: 'WidgetMarquee',
  components: {},
  props: {
    value: Object,
    ispreview: Boolean
  },
  data() {
    return {
      options: {}
    }
  },
  computed: {
    transStyle() {
      return this.objToOne(this.options)
    },
    styleColor() {
      return {
        position: this.ispreview ? 'absolute' : 'static',
        color: this.transStyle.color,
        'font-weight': this.transStyle.fontWeight,
        text: this.transStyle.text,
        'font-size': this.transStyle.fontSize + 'px',
        'letter-spacing': this.transStyle.letterSpacing + 'em',
        background: this.transStyle.background,
        'text-align': this.transStyle.textAlign,
        width: this.transStyle.width + 'px',
        height: this.transStyle.height + 'px',
        left: this.transStyle.left + 'px',
        top: this.transStyle.top + 'px',
        right: this.transStyle.right + 'px',
        marqueeSet: this.transStyle.marqueeSet,
        styleColor: this.transStyle.marqueeQuit
      }
    },
    isBehavior() {
      return this.styleColor.marqueeSet ? 'start()' : 'stop()'
    }
  },
  watch: {
    value: {
      handler(val) {
        this.options = val
        this.optionsData = val.data
        this.setOptionsData()
      },
      deep: true
    }
  },
  mounted() {
    this.options = this.value
    this.optionsData = this.value.data
    this.setOptionsData()
  },
  methods: {
    // 数据解析
    setOptionsData() {
      const optionsData = this.optionsData // 数据类型 静态 or 动态
      if (optionsData.dataType == 'dynamicData') {
        this.dynamicDataFn(optionsData.dynamicData, optionsData.refreshTime)
      } else {};
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
        this.styleColor.text = res[0].value
        this.$forceUpdate()
      })
    }
  }
}
</script>

<style scoped lang="scss">
.text {
  width: 100%;
  height: 100%;
  overflow: hidden;
}
</style>
