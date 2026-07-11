<template>
  <div id="app" :class="{ 'rounded-corner': roundedCorner }" :style="roundedStyle">
    <router-view />
    <theme-picker />
  </div>
</template>

<script>
import ThemePicker from "@/components/ThemePicker"

export default {
  name: "App",
  components: { ThemePicker },
  computed: {
    roundedCorner() {
      return this.$store.state.settings.roundedCorner
    },
    roundedCornerValue() {
      return this.$store.state.settings.roundedCornerValue || 6
    },
    roundedStyle() {
      if (!this.roundedCorner) return {}
      const v = this.roundedCornerValue + 'px'
      return {
        '--rounded-radius-base': v
      }
    }
  }
}
</script>
<style scoped>
#app .theme-picker {
  display: none;
}
</style>
