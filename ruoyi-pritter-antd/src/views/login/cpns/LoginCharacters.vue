<script setup>
import LoginEyeBall from './LoginEyeBall.vue'
import LoginPupil from './LoginPupil.vue'
import {
  characterTransformVars,
  eyesPositionVars,
  LOGIN_INK,
  mouthPositionVars,
  purpleCharacterVars,
  readFaceSkew,
  subscribeMousePosition,
} from '../utils/loginUtils.js'
import {
  computed,
  onBeforeUnmount,
  onMounted,
  ref,
  shallowRef,
  watch,
} from 'vue'

const props = defineProps({
  passwordLen: { type: Number, required: true },
  showPassword: { type: Boolean, required: true },
  isTyping: { type: Boolean, required: true },
  failureShakeNonce: { type: Number, default: 0 },
})

const mouse = shallowRef({
  x: typeof window !== 'undefined' ? window.innerWidth / 2 : 0,
  y: typeof window !== 'undefined' ? window.innerHeight / 2 : 0,
})

let unsubMouse = () => {}

onMounted(() => {
  unsubMouse = subscribeMousePosition((p) => {
    mouse.value = p
  })
})

onBeforeUnmount(() => {
  unsubMouse()
})

function useRandomBlink() {
  const blinking = ref(false)
  let outerTimer = 0
  let innerTimer = 0
  let cancelled = false

  onMounted(() => {
    const arm = () => {
      outerTimer = window.setTimeout(() => {
        if (cancelled) return
        blinking.value = true
        innerTimer = window.setTimeout(() => {
          if (!cancelled) blinking.value = false
          arm()
        }, 150)
      }, Math.random() * 4000 + 3000)
    }
    arm()
  })

  onBeforeUnmount(() => {
    cancelled = true
    clearTimeout(outerTimer)
    clearTimeout(innerTimer)
  })

  return blinking
}

const isPurpleBlinking = useRandomBlink()
const isBlackBlinking = useRandomBlink()

const shakePlay = ref(false)

watch(
  () => props.failureShakeNonce,
  (n, _, onCleanup) => {
    if (n <= 0) return
    shakePlay.value = false
    requestAnimationFrame(() => {
      shakePlay.value = true
    })
    const end = window.setTimeout(() => {
      shakePlay.value = false
    }, 480)
    onCleanup(() => clearTimeout(end))
  },
)

const isLookingAtEachOther = ref(false)

watch(
  () => props.isTyping,
  (typing, _, onCleanup) => {
    if (!typing) {
      isLookingAtEachOther.value = false
      return
    }
    isLookingAtEachOther.value = true
    const timer = window.setTimeout(() => {
      isLookingAtEachOther.value = false
    }, 800)
    onCleanup(() => clearTimeout(timer))
  },
)

const isPurplePeeking = ref(false)

watch(
  () => [props.passwordLen, props.showPassword],
  (_, __, onCleanup) => {
    const pwdOk = props.passwordLen > 0 && props.showPassword
    if (!pwdOk) {
      isPurplePeeking.value = false
      return
    }
    let cancelled = false
    let outerTimer = 0
    let innerTimer = 0
    const loop = () => {
      outerTimer = window.setTimeout(() => {
        if (cancelled) return
        isPurplePeeking.value = true
        innerTimer = window.setTimeout(() => {
          if (!cancelled) isPurplePeeking.value = false
          loop()
        }, 800)
      }, Math.random() * 3000 + 2000)
    }
    loop()
    onCleanup(() => {
      cancelled = true
      clearTimeout(outerTimer)
      clearTimeout(innerTimer)
    })
  },
  { immediate: true },
)

const purpleRef = ref(null)
const blackRef = ref(null)
const yellowRef = ref(null)
const orangeRef = ref(null)

const purpleEyeShared = {
  size: 18,
  pupilSize: 7,
  maxDistance: 5,
  eyeColor: 'white',
  pupilColor: LOGIN_INK,
}

const blackEyeShared = {
  size: 16,
  pupilSize: 6,
  maxDistance: 4,
  eyeColor: 'white',
  pupilColor: LOGIN_INK,
}

const twinPupilShared = {
  size: 12,
  maxDistance: 5,
  pupilColor: LOGIN_INK,
}

const scene = computed(() => {
  const m = mouse.value
  const pwdShow = props.passwordLen > 0 && props.showPassword
  const pwdHidden = props.passwordLen > 0 && !props.showPassword
  const leanPurple = props.isTyping || pwdHidden

  let purpleForced
  if (pwdShow) {
    purpleForced = isPurplePeeking.value ? { x: 4, y: 5 } : { x: -4, y: -4 }
  } else if (isLookingAtEachOther.value) {
    purpleForced = { x: 3, y: 4 }
  } else {
    purpleForced = undefined
  }

  let blackForced
  if (pwdShow) {
    blackForced = { x: -4, y: -4 }
  } else if (isLookingAtEachOther.value) {
    blackForced = { x: 0, y: -4 }
  } else {
    blackForced = undefined
  }

  const smallCreatureForced = pwdShow ? { x: -5, y: -4 } : undefined

  const pp = readFaceSkew(purpleRef.value, m)
  const bp = readFaceSkew(blackRef.value, m)
  const yp = readFaceSkew(yellowRef.value, m)
  const op = readFaceSkew(orangeRef.value, m)

  const purpleTf = pwdShow
    ? 'skewX(0deg)'
    : leanPurple
      ? `skewX(${pp.bodySkew - 12}deg) translateX(40px)`
      : `skewX(${pp.bodySkew}deg)`

  const blackTf = pwdShow
    ? 'skewX(0deg)'
    : isLookingAtEachOther.value
      ? `skewX(${bp.bodySkew * 1.5 + 10}deg) translateX(20px)`
      : leanPurple
        ? `skewX(${bp.bodySkew * 1.5}deg)`
        : `skewX(${bp.bodySkew}deg)`

  const orangeTf = pwdShow ? 'skewX(0deg)' : `skewX(${op.bodySkew}deg)`
  const yellowTf = pwdShow ? 'skewX(0deg)' : `skewX(${yp.bodySkew}deg)`

  const purpleEyeLeft = pwdShow
    ? 20
    : isLookingAtEachOther.value
      ? 55
      : 45 + pp.faceX
  const purpleEyeTop = pwdShow
    ? 35
    : isLookingAtEachOther.value
      ? 65
      : 40 + pp.faceY

  const blackEyeLeft = pwdShow
    ? 10
    : isLookingAtEachOther.value
      ? 32
      : 26 + bp.faceX
  const blackEyeTop = pwdShow
    ? 28
    : isLookingAtEachOther.value
      ? 12
      : 32 + bp.faceY

  const orangeEyeLeft = pwdShow ? 50 : 82 + op.faceX
  const orangeEyeTop = pwdShow ? 85 : 90 + op.faceY

  const yellowEyeLeft = pwdShow ? 20 : 52 + yp.faceX
  const yellowEyeTop = pwdShow ? 35 : 40 + yp.faceY

  const mouthLeft = pwdShow ? 10 : 40 + yp.faceX
  const mouthTop = pwdShow ? 88 : 88 + yp.faceY

  return {
    purpleForced,
    blackForced,
    smallCreatureForced,
    purpleShellStyle: purpleCharacterVars(leanPurple ? 440 : 400, purpleTf),
    blackShellStyle: characterTransformVars(blackTf),
    orangeShellStyle: characterTransformVars(orangeTf),
    yellowShellStyle: characterTransformVars(yellowTf),
    purpleEyesStyle: eyesPositionVars(purpleEyeLeft, purpleEyeTop),
    blackEyesStyle: eyesPositionVars(blackEyeLeft, blackEyeTop),
    orangeEyesStyle: eyesPositionVars(orangeEyeLeft, orangeEyeTop),
    yellowEyesStyle: eyesPositionVars(yellowEyeLeft, yellowEyeTop),
    mouthStyle: mouthPositionVars(mouthLeft, mouthTop),
    mouseX: m.x,
    mouseY: m.y,
  }
})
</script>

<template>
  <div class="login-characters-wrap">
    <div
      class="login-characters-stage"
      :class="{ 'login-characters-stage--shake': shakePlay }"
    >
      <div
        ref="purpleRef"
        class="login-char-body-layer login-purple-character-shell"
        :style="scene.purpleShellStyle"
      >
        <div
          class="login-char-eyes-row-loose login-char-eyes-shift"
          :style="scene.purpleEyesStyle"
        >
          <LoginEyeBall
            :mouse-x="scene.mouseX"
            :mouse-y="scene.mouseY"
            v-bind="purpleEyeShared"
            :is-blinking="isPurpleBlinking"
            :forced-look="scene.purpleForced"
          />
          <LoginEyeBall
            :mouse-x="scene.mouseX"
            :mouse-y="scene.mouseY"
            v-bind="purpleEyeShared"
            :is-blinking="isPurpleBlinking"
            :forced-look="scene.purpleForced"
          />
        </div>
      </div>

      <div
        ref="blackRef"
        class="login-char-body-layer login-black-character-shell"
        :style="scene.blackShellStyle"
      >
        <div
          class="login-char-eyes-row-black login-char-eyes-shift"
          :style="scene.blackEyesStyle"
        >
          <LoginEyeBall
            :mouse-x="scene.mouseX"
            :mouse-y="scene.mouseY"
            v-bind="blackEyeShared"
            :is-blinking="isBlackBlinking"
            :forced-look="scene.blackForced"
          />
          <LoginEyeBall
            :mouse-x="scene.mouseX"
            :mouse-y="scene.mouseY"
            v-bind="blackEyeShared"
            :is-blinking="isBlackBlinking"
            :forced-look="scene.blackForced"
          />
        </div>
      </div>

      <div
        ref="orangeRef"
        class="login-char-body-layer login-orange-character-shell"
        :style="scene.orangeShellStyle"
      >
        <div
          class="login-char-eyes-row-orange login-char-eyes-shift"
          :style="scene.orangeEyesStyle"
        >
          <LoginPupil
            :mouse-x="scene.mouseX"
            :mouse-y="scene.mouseY"
            v-bind="twinPupilShared"
            :forced-look="scene.smallCreatureForced"
          />
          <LoginPupil
            :mouse-x="scene.mouseX"
            :mouse-y="scene.mouseY"
            v-bind="twinPupilShared"
            :forced-look="scene.smallCreatureForced"
          />
        </div>
      </div>

      <div
        ref="yellowRef"
        class="login-char-body-layer login-yellow-character-shell"
        :style="scene.yellowShellStyle"
      >
        <div
          class="login-char-eyes-row-yellow login-char-eyes-shift"
          :style="scene.yellowEyesStyle"
        >
          <LoginPupil
            :mouse-x="scene.mouseX"
            :mouse-y="scene.mouseY"
            v-bind="twinPupilShared"
            :forced-look="scene.smallCreatureForced"
          />
          <LoginPupil
            :mouse-x="scene.mouseX"
            :mouse-y="scene.mouseY"
            v-bind="twinPupilShared"
            :forced-look="scene.smallCreatureForced"
          />
        </div>
        <div class="login-yellow-mouth-bar" :style="scene.mouthStyle" />
      </div>
    </div>
  </div>
</template>
