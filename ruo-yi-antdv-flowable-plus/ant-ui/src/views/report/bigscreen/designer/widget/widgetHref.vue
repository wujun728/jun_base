<!--
 * @Author: lide1202@hotmail.com
 * @Date: 2021-3-13 11:04:24
 * @Last Modified by:   lide1202@hotmail.com
 * @Last Modified time: 2021-3-13 11:04:24
 !-->
<template>
  <a
    :href="styleColor.linkAdress"
    :style="styleColor"
    :target="styleColor.jumpMode"
  >{{ styleColor.text }}</a
  >
</template>

<script>
export default {
  name: 'WidgetHref',
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
        color: this.transStyle.color || '#fff',
        'font-weight': this.transStyle.fontWeight || '600',
        text: this.transStyle.text || '超链接',
        'font-size': this.transStyle.fontSize + 'px' || '12px',
        'letter-spacing': this.transStyle.letterSpacing + 'em',
        background: this.transStyle.background,
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
        right: this.transStyle.right + 'px',
        linkAdress: this.transStyle.linkAdress,
        jumpMode: this.transStyle.jumpMode == 'other' ? '_blank' : '_self'
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
  },
  methods: {}
}
</script>

<style scoped lang="scss">
a {
  width: 100%;
  height: 100%;
  overflow: hidden;
}
</style>
