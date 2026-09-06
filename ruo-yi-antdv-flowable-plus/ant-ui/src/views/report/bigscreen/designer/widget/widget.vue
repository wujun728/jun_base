<!--
 * @Author: lide1202@hotmail.com
 * @Date: 2021-3-13 11:04:24
 * @Last Modified by:   lide1202@hotmail.com
 * @Last Modified time: 2021-3-13 11:04:24
 !-->
<template>
<!--  avue-draggable-->
  <avue-draggable
    :step="step"
    :width="widgetsWidth"
    :height="widgetsHeight"
    :left="widgetsLeft"
    :top="widgetsTop"
    ref="draggable"
    :index="index"
    :z-index="-1"
    @focus="handleFocus"
    @blur="handleBlur"
  >
    <component :is="type" :value="value"/>
  </avue-draggable>
</template>

<script>
import widgetHref from './widgetHref.vue'
import widgetText from './widgetText.vue'
import WidgetMarquee from './widgetMarquee.vue'
import widgetTime from './widgetTime.vue'
import widgetImage from './widgetImage.vue'
import widgetSlider from './widgetSlider.vue'
import widgetVideo from './widgetVideo.vue'
import WidgetIframe from './widgetIframe.vue'
import widgetBarchart from './bar/widgetBarchart.vue'
import widgetGradientColorBarchart from './bar/widgetGradientColorBarchart.vue'
import widgetLinechart from './line/widgetLinechart.vue'
import widgetBarlinechart from './bar/widgetBarlinechart'
import WidgetPiechart from './pie/widgetPiechart.vue'
import WidgetFunnel from './widgetFunnel.vue'
import WidgetGauge from './percent/widgetGauge.vue'
import WidgetPieNightingaleRoseArea from './pie/widgetPieNightingaleRose'
import widgetTable from './widgetTable.vue'
import widgetMap from './widgetMap.vue'
import widgetPiePercentageChart from './percent/widgetPiePercentageChart'
import widgetAirBubbleMap from './map/widgetAirBubbleMap'
import widgetBarStackChart from './bar/widgetBarStackChart'
import widgetLineStackChart from './line/widgetLineStackChart'
import widgetBarCompareChart from './bar/widgetBarCompareChart'
import widgetLineCompareChart from './line/widgetLineCompareChart'
import widgetDecoratePieChart from './decorate/widgetDecoratePieChart'

export default {
  name: 'Widget',
  components: {
    widgetHref,
    widgetText,
    WidgetMarquee,
    widgetTime,
    widgetImage,
    widgetSlider,
    widgetVideo,
    WidgetIframe,
    widgetBarchart,
    widgetGradientColorBarchart,
    widgetLinechart,
    widgetBarlinechart,
    WidgetPiechart,
    WidgetFunnel,
    WidgetGauge,
    WidgetPieNightingaleRoseArea,
    widgetTable,
    widgetMap,
    widgetPiePercentageChart,
    widgetAirBubbleMap,
    widgetBarStackChart,
    widgetLineStackChart,
    widgetBarCompareChart,
    widgetLineCompareChart,
    widgetDecoratePieChart
  },
  model: {
    prop: 'value',
    event: 'input'
  },
  props: {
    /*
    widget-text widget-marquee widget-href widget-time widget-image widget-slider widget-video widget-table widget-iframe widget-universal
    widget-linechart widget-barlinechart widget-piechart widget-hollow-piechart widget-funnel widget-gauge widget-china-map
    */
    index: Number, // 当前组件，在工作区变量widgetInWorkbench中的索引
    type: String,
    bigscreen: Object,
    value: {
      type: [Object],
      default: () => {
      }
    },
    step: Number
  },
  data() {
    return {
      data: {
        setup: {},
        data: {},
        position: {}
      }
    }
  },
  computed: {
    widgetsWidth() {
      return this.value.position.width
    },
    widgetsHeight() {
      return this.value.position.height
    },
    widgetsLeft() {
      return this.value.position.left
    },
    widgetsTop() {
      return this.value.position.top
    },
    widgetsZIndex() {
      return this.value.position.zIndex || 1
    }
  },
  mounted() {
  },
  methods: {
    handleFocus({index, left, top, width, height}) {
    },
    handleBlur({index, left, top, width, height}) {
      this.$emit('onActivated', {index, left, top, width, height})
      this.$refs.draggable.setActive(true)
    }
  }
}
</script>

<style scoped lang="scss">
.vue-draggalbe {
  position: absolute;
}

.widget-active {
  cursor: move;
  border: 1px dashed #09f;
  background-color: rgba(115, 170, 229, 0.5);
}

.avue-draggable {
  padding: 0 !important;
}
</style>
