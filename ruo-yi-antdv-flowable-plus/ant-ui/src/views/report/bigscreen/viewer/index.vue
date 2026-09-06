<!--
 * @Author: lide1202@hotmail.com
 * @Date: 2021-3-13 11:04:24
 * @Last Modified by:   lide1202@hotmail.com
 * @Last Modified time: 2021-3-13 11:04:24
 !-->
<template>
  <div class="layout">
    <div :style="bigScreenStyle">
      <widget
        v-for="(widget, index) in widgets"
        :key="index"
        v-model="widget.value"
        :type="widget.type"
      />
    </div>
  </div>
</template>

<script>
import widget from '../designer/widget/temp'
import { detailDashboard } from '@/api/report/bigscreen'
export default {
  name: 'Login',
  components: {
    widget
  },
  data() {
    return {
      bigScreenStyle: {},
      widgets: []
    }
  },
  mounted() {
    this.getData()
  },
  methods: {
    async getData() {
      const reportCode = this.$route.query.reportCode
      const { code, data } = await detailDashboard(reportCode)
      if (code != 200) return
      const equipment = document.body.clientWidth
      const ratioEquipment = equipment / data.dashboard.width
      this.bigScreenStyle = {
        width: data.dashboard.width + 'px',
        height: data.dashboard.height + 'px',
        'background-color': data.dashboard.backgroundColor,
        'background-image': 'url(' + data.dashboard.backgroundImage + ')',
        'background-position': '0% 0%',
        'background-size': '100% 100%',
        'background-repeat': 'initial',
        'background-attachment': 'initial',
        'background-origin': 'initial',
        'background-clip': 'initial',
        transform: `scale(${ratioEquipment}, ${ratioEquipment})`,
        'transform-origin': '0 0'
      }
      this.widgets = data.dashboard.widgets
    }
  }
}
</script>

<style scoped lang="scss">
.layout {
  width: 100%;
  height: 100%;
  text-align: center;
}
.bottom-text {
  width: 100%;
  color: #a0a0a0;
  position: fixed;
  bottom: 16px;
  z-index: 9999;
}
</style>
