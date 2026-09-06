<script setup>
import { computed, ref } from 'vue'
import {
  eyeBallPupilVars,
  eyeBallShellVars,
  pupilOffsetFromMouse,
} from '../utils/loginUtils.ts'

const props = defineProps({
  mouseX: { type: Number, required: true },
  mouseY: { type: Number, required: true },
  size: { type: Number, default: 48 },
  pupilSize: { type: Number, default: 16 },
  maxDistance: { type: Number, default: 10 },
  eyeColor: { type: String, default: 'white' },
  pupilColor: { type: String, default: 'black' },
  isBlinking: { type: Boolean, default: false },
  forcedLook: { type: Object, default: undefined },
})

const eyeRef = ref(null)

const pupilStyle = computed(() => {
  const { x, y } = pupilOffsetFromMouse(
    props.mouseX,
    props.mouseY,
    eyeRef.value,
    props.maxDistance,
    props.forcedLook,
  )
  return eyeBallPupilVars({
    pupilSizePx: props.pupilSize,
    fill: props.pupilColor,
    x,
    y,
  })
})

const shellStyle = computed(() =>
  eyeBallShellVars({
    sizePx: props.size,
    heightPx: props.isBlinking ? 2 : props.size,
    fill: props.eyeColor,
  }),
)
</script>

<template>
  <div ref="eyeRef" class="login-eye-ball-shell" :style="shellStyle">
    <div v-if="!isBlinking" class="login-eye-ball-pupil" :style="pupilStyle" />
  </div>
</template>
