<template>
  <div class="imagebox" :style="styleColor">
    <img
      :class="transStyle.startRotate ? 'startImg' : ''"
      :style="imgStyle"
      :src="imgStyle.imageAdress"
      alt=""
    />
  </div>
</template>
<script>
export default {
  name: 'WidgetImage',
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
        background: this.transStyle.background,
        'text-align': this.transStyle.textAlign,
        width: this.transStyle.width + 'px',
        height: this.transStyle.height + 'px',
        left: this.transStyle.left + 'px',
        top: this.transStyle.top + 'px',
        right: this.transStyle.right + 'px'
      }
    },
    imgStyle() {
      return {
        imageAdress: this.transStyle.imageAdress,
        'border-radius': this.transStyle.borderRadius + 'px',
        opacity: this.transStyle.transparency / 100
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
  created() {
    this.options = this.value
  },
  mounted() {},
  methods: {}
}
</script>

<style scoped lang="scss">
.imagebox {
  width: 100%;
  height: 100%;
  overflow: hidden;
}
.imagebox img {
  width: 100%;
  height: 100%;
}
.startImg {
  animation: turn 1s linear infinite;
}
@keyframes turn {
  0% {
    -webkit-transform: rotate(0deg);
  }
  25% {
    -webkit-transform: rotate(90deg);
  }
  50% {
    -webkit-transform: rotate(180deg);
  }
  75% {
    -webkit-transform: rotate(270deg);
  }
  100% {
    -webkit-transform: rotate(360deg);
  }
}
</style>
