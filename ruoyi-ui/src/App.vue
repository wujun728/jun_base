<template>
  <div id="app" :class="currentTheme">
    <router-view />
    <theme-picker />
  </div>
</template>

<script>
import ThemePicker from "@/components/ThemePicker";

export default {
  name: "App",
  components: { ThemePicker },
  computed: {
    currentTheme() {
      return this.$store.state.settings.saAdminTheme || 'theme-0'
    }
  },
  mounted() {
    // 初始化 Sa-Admin 主题
    const savedTheme = localStorage.getItem('saAdminTheme') || 'theme-0'
    this.$store.dispatch('settings/changeSaAdminTheme', savedTheme)
  },
  metaInfo() {
    return {
      title: this.$store.state.settings.dynamicTitle && this.$store.state.settings.title,
      titleTemplate: title => {
        return title ? `${title} - ${process.env.VUE_APP_TITLE}` : process.env.VUE_APP_TITLE
      }
    }
  }
};
</script>
<style scoped>
#app .theme-picker {
  display: none;
}
</style>
