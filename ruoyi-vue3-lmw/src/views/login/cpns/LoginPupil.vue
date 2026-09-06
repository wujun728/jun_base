<script setup>
import { computed, ref } from 'vue'
import {
  pupilAnchorVars,
  pupilDotVars,
  pupilOffsetFromMouse,
} from '../utils/loginUtils.js'

const props = defineProps({
  mouseX: { type: Number, required: true },
  mouseY: { type: Number, required: true },
  size: { type: Number, default: 12 },
  maxDistance: { type: Number, default: 5 },
  pupilColor: { type: String, default: 'black' },
  forcedLook: { type: Object, default: undefined },
})

const anchorRef = ref(null)

const dotStyle = computed(() => {
  const { x, y } = pupilOffsetFromMouse(
    props.mouseX,
    props.mouseY,
    anchorRef.value,
    props.maxDistance,
    props.forcedLook
  )
  return pupilDotVars({
    sizePx: props.size,
    fill: props.pupilColor,
    x,
    y,
  })
})

const anchorStyle = computed(() => pupilAnchorVars(props.size))
</script>

<template>
  <div ref="anchorRef" class="login-pupil-anchor" :style="anchorStyle">
    <div class="login-pupil-translate" :style="dotStyle" />
  </div>
</template>
