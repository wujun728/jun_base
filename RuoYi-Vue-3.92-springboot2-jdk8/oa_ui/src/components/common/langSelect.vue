<!-- @FileDescription: 公共-多语言切换组件 -->
<template>
  <el-dropdown trigger="click" class="international" @command="handleSetLanguage">
    <div>
      <span class="iconfont icondiqiu" />
    </div>
    <el-dropdown-menu slot="dropdown">
      <el-dropdown-item :disabled="language === 'zh-cn'" command="zh-cn"> 中文 </el-dropdown-item>
      <el-dropdown-item :disabled="language === 'en'" command="en"> English </el-dropdown-item>
    </el-dropdown-menu>
  </el-dropdown>
</template>

<script>
export default {
  computed: {
    language() {
      return this.$store.getters.lang
    }
  },
  methods: {
    handleSetLanguage(lang) {
      this.$i18n.locale = lang
      this.$store.dispatch('app/setLanguage', lang)
    }
  }
}
</script>
<style scoped>
.icondiqiu {
  font-size: 20px;
  color: #fff;
}
</style>
