<template>
  <div
    class="layout-logo"
    v-if="$store.state.themeConfig.themeConfig.layout !== 'columns' && !$store.state.themeConfig.themeConfig.isCollapse"
    @click="onThemeConfigChange"
  >
    <img v-if="maxLogo" class="layout-logo-medium-img" :src="maxLogo" />
  </div>
  <div class="layout-logo-size" v-else @click="onThemeConfigChange">
    <img v-if="minLogo" class="layout-logo-size-img" :src="minLogo" />
  </div>
</template>

<script>
export default {
  name: 'layoutLogo',
  data() {
    return {};
  },
  computed: {
    maxLogo() {
      return require('@/assets/logo/logo.png');
    },
    minLogo() {
      return require('@/assets/logo/logo.png');
    },
    getThemeConfig() {
      return this.$store.state.themeConfig.themeConfig;
    },
    setShowLogo() {
      let { isCollapse, layout } = this.$store.state.themeConfig.themeConfig;
      return !isCollapse || layout === 'classic' || document.body.clientWidth < 1000;
    },
  },
  methods: {
    onThemeConfigChange() {
      this.$router.push(`/index`);
    },
  },
};
</script>

<style scoped lang="scss">
.layout-logo {
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--prev-color-primary);
  font-size: 16px;
  cursor: pointer;
  animation: logoAnimation 0.3s ease-in-out;
  height: 64px;
  line-height: 64px;
  width: 206px;
  &:hover {
    span {
      opacity: 0.9;
    }
  }
  &-medium-img {
    height: 32px;
  }
}
.layout-logo-size {
  display: flex;
  cursor: pointer;
  margin: auto;
  height: 50px;
  &-img {
    width: 50px;
    height: auto;
    margin: auto;
    animation: logoAnimation 0.3s ease-in-out;
  }
}
</style>
