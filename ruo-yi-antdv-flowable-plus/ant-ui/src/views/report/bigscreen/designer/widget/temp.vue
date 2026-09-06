<!--
 * @Author: lide1202@hotmail.com
 * @Date: 2021-3-13 11:04:24
 * @Last Modified by:   lide1202@hotmail.com
 * @Last Modified time: 2021-3-13 11:04:24
 !-->
<template>
  <div>
    <component :is="type" :value="value" :ispreview="true"/>
  </div>
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
import widgetLinechart from './line/widgetLinechart.vue'
import widgetBarlinechart from './bar/widgetBarlinechart'
import widgetGradientColorBarchart from './bar/widgetGradientColorBarchart.vue'
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
  name: 'WidgetTemp',
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
    type: String,
    value: {
      type: [Object],
      default: () => {
      }
    }
  },
  data() {
    return {}
  },
  mounted() {
  },
  methods: {}
}
</script>

<style scoped lang="scss"></style>
