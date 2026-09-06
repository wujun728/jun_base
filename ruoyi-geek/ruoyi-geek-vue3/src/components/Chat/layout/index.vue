<template>
  <div class="window">
    <div class="window-header">
      <slot name="window-header" />
    </div>
    <div class="window-main">
      <div class="window-sidebar">
        <slot name="window-sidebar" />
      </div>
      <div class="app-sidebar">
        <slot name="app-sidebar" />
      </div>
      <div class="app-main">
        <slot />
      </div>
    </div>
  </div>
</template>
<style lang="scss" scoped>
$window-height: 80vh;
$window-width: 80vw;
$window-header-height: 45px;
$window-sidebar-width: 60px;
$app-sidebar-width: 300px;

.window {
  height: $window-height;
  width: $window-width;
  background: #f5f6fa;
  border-radius: 10px;
  border: 1px solid #e6e6e6;
  overflow: hidden;

  .window-header {
    background-color: #409eff;
    height: $window-header-height;
  }

  .window-main {
    display: flex;

    .window-sidebar {
      width: $window-sidebar-width;
      display: flex;
      flex-direction: column;
      align-items: center;
      padding: 10px 0;
      gap: 10px;
    }
  }
}

.app-sidebar {
  width: $app-sidebar-width;
  height: calc(#{$window-height} - #{$window-header-height});
  background: #fff;
  border-right: 1px solid #e6e6e6;
  display: flex;
  flex-direction: column;
}

.app-main {
  flex: 1;
  height: calc(#{$window-height} - #{$window-header-height});
  background: #f9fafb;
  display: flex;
  flex-direction: column;
}
</style>