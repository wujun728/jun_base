<template>
  <div class="text" :style="styleColor">{{ timestr }}</div>
</template>
<script>
export default {
  name: 'WidgetTime',
  components: {},
  props: {
    value: Object,
    ispreview: Boolean
  },
  data() {
    return {
      timestr: '',
      options: {}
    }
  },
  computed: {
    transStyle() {
      console.log(this.objToOne(this.options))
      return this.objToOne(this.options)
    },
    styleColor() {
      return {
        position: this.ispreview ? 'absolute' : 'static',
        color: this.transStyle.color || '#fff',
        text: this.transStyle.text || '文本框',
        'font-size': this.transStyle.fontSize + 'px' || '30px',
        'letter-spacing': this.transStyle.letterSpacing + 'em',
        background: this.transStyle.background,
        'font-weight': this.transStyle.fontWeight,
        'text-align': this.transStyle.textAlign,
        display:
          this.transStyle.hideLayer == undefined
            ? 'block'
            : this.transStyle.hideLayer
            ? 'none'
            : 'block',
        width: this.transStyle.width + 'px',
        height: this.transStyle.height + 'px',
        left: this.transStyle.left + 'px',
        top: this.transStyle.top + 'px',
        right: this.transStyle.right + 'px'
      }
    }
  },
  watch: {
    value: {
      handler(val) {
        this.options = val
      },
      deep: true
    }
  },
  mounted() {
    this.options = this.value
    this.getTime()
  },
  methods: {
    formatFunction(date, fmt) {
      if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(
          RegExp.$1,
          (date.getFullYear() + '').substr(4 - RegExp.$1.length)
        )
      }
      const o = {
        'M+': date.getMonth() + 1, // 月份
        'd+': date.getDate(), // 日
        'h+': date.getHours(), // 小时
        'm+': date.getMinutes(), // 分
        's+': date.getSeconds(), // 秒
        'q+': Math.floor((date.getMonth() + 3) / 3), // 季度
        S: date.getMilliseconds() // 毫秒
      }
      for (var k in o) {
        if (new RegExp('(' + k + ')').test(fmt)) {
          fmt = fmt.replace(
            RegExp.$1,
            RegExp.$1.length == 1
              ? o[k]
              : ('00' + o[k]).substr(('' + o[k]).length)
          )
        }
      }
      return fmt
    },
    check(val) {
      if (val < 10) {
        return '0' + val
      } else {
        return val
      }
    },
    formatWeek(date, fmt) {
      const year = date.getFullYear()
      const month = date.getMonth() + 1
      const day = date.getDate()
      const hours = date.getHours()
      const minutes = date.getMinutes()
      const seconds = date.getSeconds()
      let dayCycle = date.getDay()
      const dayCycleArray = ['日', '一', '二', '三', '四', '五', '六']
      for (let i = 0; i < 7; i++) {
        if (dayCycle == i) {
          dayCycle = ' 星期' + dayCycleArray[i]
        }
      }
      if (fmt == 'year-week') {
        return year + '-' + month + '-' + day + dayCycle
      } else if (fmt == 'year-h-m-week') {
        return (
          year +
          '-' +
          month +
          '-' +
          day +
          ' ' +
          hours +
          ':' +
          minutes +
          dayCycle
        )
      } else if (fmt == 'year-h-m-s-week') {
        return (
          year +
          '-' +
          month +
          '-' +
          day +
          ' ' +
          hours +
          ':' +
          minutes +
          ':' +
          seconds +
          dayCycle
        )
      } else if (fmt == 'week') {
        return dayCycle
      }
    },
    displayTime() {
      this.timestr =
        this.transStyle.timeFormat.indexOf('week') > -1
          ? this.formatWeek(new Date(), this.transStyle.timeFormat)
          : this.formatFunction(new Date(), this.transStyle.timeFormat)
    },
    getTime() {
      setInterval(() => {
        this.displayTime()
      }, 1000)
    }
  }
}
</script>
<style lang="scss" scoped>
.text {
  width: 100%;
  height: 100%;
  overflow: hidden;
}
</style>
