<template>
  <a-spin :tip="tip" :spinning="state.spinning" wrapper-class-name="iframe_warp">
    <iframe id="_iframe" class="my_iframe" :src="src" :style="`height:${state.height};width:${state.width}`"></iframe>
  </a-spin>
</template>

<script setup>
import {reactive, onMounted, nextTick} from "vue"

const props = defineProps({
  src: String,
  tip: {
    type: String,
    default: ""
  }
});

const state = reactive({
  spinning: true,
  height: `${document.documentElement.clientHeight - 136}px;`,
  width: `${document.documentElement.clientWidth - 220}px;`,
});

const init = () => {
  const iframe = document.getElementById('_iframe');
  if (iframe) {
    iframe.onload = () => {
      state.spinning = false;
    };
    state.height = `${document.documentElement.clientHeight - 213}px;`;
    state.width = `${document.documentElement.clientWidth - 242}px;`;
  }
}

onMounted(() =>
    nextTick(() => init())
)
</script>

<style lang="less">
.iframe_warp {
  width: 100%;
  height: 100%;

  .my_iframe {
    width: 100%;
    height: 100%;
  }
}
</style>
