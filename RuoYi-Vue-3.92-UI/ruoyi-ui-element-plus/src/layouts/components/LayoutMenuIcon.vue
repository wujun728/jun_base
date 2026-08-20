<template>
  <el-icon v-if="isElementIcon" class="layout-menu-icon layout-menu-icon--element menu-icon">
    <component :is="elementIconName" />
  </el-icon>
  <svg v-else-if="icon" class="layout-menu-icon layout-menu-icon--svg menu-icon svg-icon" aria-hidden="true">
    <use :xlink:href="`#icon-${icon}`" />
  </svg>
</template>

<script setup>
const props = defineProps({
  icon: {
    type: String,
    default: "",
  },
});

const isElementIcon = computed(() => (props.icon || "").startsWith("el-icon"));
const elementIconName = computed(() => (props.icon || "").replace("el-icon-", ""));
</script>

<style scoped>
.svg-icon {
  width: 1em;
  height: 1em;
  fill: currentColor;
  vertical-align: -2px;
}
</style>