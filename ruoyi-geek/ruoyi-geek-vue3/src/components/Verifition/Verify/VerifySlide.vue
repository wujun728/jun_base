<template>
    <div ref="rootEl" style="position: relative;">
        <div v-if="type === '2'" class="verify-img-out"
            :style="{ height: (parseInt(setSize.imgHeight) + vSpace) + 'px' }">
            <div class="verify-img-panel" :style="{
                width: setSize.imgWidth,
                height: setSize.imgHeight,
            }">
                <img :src="'data:image/png;base64,' + backImgBase" alt="" style="width:100%;height:100%;display:block">
                <div class="verify-refresh" @click="refresh" v-show="showRefresh"><i class="iconfont icon-refresh"></i>
                </div>
                <transition name="tips">
                    <span class="verify-tips" v-if="tipWords" :class="passFlag ? 'suc-bg' : 'err-bg'">{{ tipWords
                        }}</span>
                </transition>
            </div>
        </div>
        <!-- 公共部分 -->
        <div ref="barAreaEl" class="verify-bar-area" :style="{
            width: setSize.imgWidth,
            height: barSize.height,
            'line-height': barSize.height
        }">
            <span class="verify-msg">{{ text }}</span>
            <div class="verify-left-bar"
                :style="{ width: (leftBarWidth !== undefined) ? leftBarWidth : barSize.height, height: barSize.height, 'border-color': leftBarBorderColor, transaction: transitionWidth }">
                <span class="verify-msg" v-text="finishText"></span>
                <div class="verify-move-block" @touchstart="start" @mousedown="start"
                    :style="{ width: barSize.height, height: barSize.height, 'background-color': moveBlockBackgroundColor, left: moveBlockLeft, transition: transitionLeft }">
                    <i :class="['verify-icon iconfont', iconClass]" :style="{ color: iconColor }"></i>
                    <div v-if="type === '2'" class="verify-sub-block" :style="{
                        'width': Math.floor(parseInt(setSize.imgWidth) * 47 / 310) + 'px',
                        'height': setSize.imgHeight,
                        'top': '-' + (parseInt(setSize.imgHeight) + vSpace) + 'px',
                        'background-size': setSize.imgWidth + ' ' + setSize.imgHeight,
                    }">
                        <img :src="'data:image/png;base64,' + blockBackImgBase" alt=""
                            style="width:100%;height:100%;display:block;-webkit-user-drag:none;">
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
<script setup>
/**
 * VerifySlide
 * @description 滑块
 */
import { aesEncrypt } from '@/utils/jsencrypt'
import { getCaptcha } from '@/api/captcha'
import { resetSize } from './verifyutil'
import { nextTick, onMounted, reactive, ref, watch, getCurrentInstance, useTemplateRef } from 'vue'

defineOptions({ name: 'VerifySlide' })

const emit = defineEmits(['ready', 'success', 'error'])

const props = defineProps({
    captchaType: {
        type: String
    },
    type: {
        type: String,
        default: '1'
    },
    // 弹出式 pop，固定 fixed
    mode: {
        type: String,
        default: 'fixed'
    },
    vSpace: {
        type: Number,
        default: 5
    },
    explain: {
        type: String,
        default: '向右滑动完成验证'
    },
    imgSize: {
        type: Object,
        default() {
            return {
                width: '310px',
                height: '155px'
            }
        }
    },
    blockSize: {
        type: Object,
        default() {
            return {
                width: '50px',
                height: '50px'
            }
        }
    },
    barSize: {
        type: Object,
        default() {
            return {
                width: '310px',
                height: '40px'
            }
        }
    },
    check: {
        type: Function
    }
})

const rootEl = useTemplateRef("rootEl")
const barAreaEl = useTemplateRef("barAreaEl")
const instanceProxy = getCurrentInstance()?.proxy

const secretKey = ref('')
const passFlag = ref('')
const backImgBase = ref('')
const blockBackImgBase = ref('')
const backToken = ref('')
const startMoveTime = ref('')
const endMovetime = ref('')
const tipWords = ref('')
const text = ref('')
const finishText = ref('')

const setSize = reactive({
    imgHeight: 0,
    imgWidth: 0,
    barHeight: 0,
    barWidth: 0
})

const moveBlockLeft = ref(undefined)
const leftBarWidth = ref(undefined)

const moveBlockBackgroundColor = ref(undefined)
const leftBarBorderColor = ref('#ddd')
const iconColor = ref(undefined)
const iconClass = ref('icon-right')

const status = ref(false)
const isEnd = ref(false)
const showRefresh = ref(true)

const transitionLeft = ref('')
const transitionWidth = ref('')

const startLeft = ref(0)

const getPointerX = (e) => e?.touches?.length ? e.touches[0].pageX : e.clientX

function start(e) {
    const x = getPointerX(e)
    if (!barAreaEl.value) return

    startLeft.value = Math.floor(x - barAreaEl.value.getBoundingClientRect().left)
    startMoveTime.value = +new Date()
    if (isEnd.value === false) {
        text.value = ''
        moveBlockBackgroundColor.value = '#337ab7'
        leftBarBorderColor.value = '#337AB7'
        iconColor.value = '#fff'
        e.stopPropagation?.()
        status.value = true
    }
}

function move(e) {
    if (!status.value || isEnd.value) return
    if (!barAreaEl.value) return

    const x = getPointerX(e)
    const barAreaLeft = barAreaEl.value.getBoundingClientRect().left
    let moveBlockLeftRaw = x - barAreaLeft

    const halfBlockWidth = parseInt(parseInt(props.blockSize.width) / 2)

    if (moveBlockLeftRaw >= barAreaEl.value.offsetWidth - halfBlockWidth - 2) {
        moveBlockLeftRaw = barAreaEl.value.offsetWidth - halfBlockWidth - 2
    }
    if (moveBlockLeftRaw <= 0) {
        moveBlockLeftRaw = halfBlockWidth
    }

    moveBlockLeft.value = moveBlockLeftRaw - startLeft.value + 'px'
    leftBarWidth.value = moveBlockLeftRaw - startLeft.value + 'px'
}

function end() {
    endMovetime.value = +new Date()
    if (!status.value || isEnd.value) return

    let moveLeftDistance = parseInt(String(moveBlockLeft.value || '').replace('px', ''))
    moveLeftDistance = (moveLeftDistance * 310) / parseInt(setSize.imgWidth)

    const pointPayload = { x: moveLeftDistance, y: 5.0 }
    const pointJson = secretKey.value
        ? aesEncrypt(JSON.stringify(pointPayload), secretKey.value)
        : JSON.stringify(pointPayload)

    const data = {
        captchaType: props.captchaType,
        pointJson,
        token: backToken.value
    }

    props.check(data).then((res) => {
        moveBlockBackgroundColor.value = '#5cb85c'
        leftBarBorderColor.value = '#5cb85c'
        iconColor.value = '#fff'
        iconClass.value = 'icon-check'
        showRefresh.value = false
        isEnd.value = true

        passFlag.value = true
        tipWords.value = `${((endMovetime.value - startMoveTime.value) / 1000).toFixed(2)}s验证成功`

        const captchaVerification = secretKey.value
            ? aesEncrypt(backToken.value + '---' + JSON.stringify(pointPayload), secretKey.value)
            : backToken.value + '---' + JSON.stringify(pointPayload)

        if (props.mode === 'pop') {
            setTimeout(() => {
                refresh()
            }, 1500)
        }

        setTimeout(() => {
            tipWords.value = ''
            emit('success', { captchaVerification })
        }, 1000)
    }).catch(e => {
        moveBlockBackgroundColor.value = '#d9534f'
        leftBarBorderColor.value = '#d9534f'
        iconColor.value = '#fff'
        iconClass.value = 'icon-close'
        passFlag.value = false

        emit('error', instanceProxy)

        tipWords.value = '验证失败'
        setTimeout(() => {
            refresh()
        }, 1000)
        setTimeout(() => {
            tipWords.value = ''
        }, 1000)
    })

    status.value = false
}

import { useEventListener } from '@vueuse/core'
useEventListener(window, 'touchmove', (e) => move(e))
useEventListener(window, 'mousemove', (e) => move(e))
useEventListener(window, 'touchend', () => end())
useEventListener(window, 'mouseup', () => end())

function refresh() {
    showRefresh.value = true
    finishText.value = ''

    transitionLeft.value = 'left .3s'
    moveBlockLeft.value = 0

    leftBarWidth.value = undefined
    transitionWidth.value = 'width .3s'

    leftBarBorderColor.value = '#ddd'
    moveBlockBackgroundColor.value = '#fff'
    iconColor.value = '#000'
    iconClass.value = 'icon-right'
    isEnd.value = false

    getPictrue()
    setTimeout(() => {
        transitionWidth.value = ''
        transitionLeft.value = ''
        text.value = props.explain
    }, 300)
}

function getPictrue() {
    const data = {
        captchaType: props.captchaType
    }
    getCaptcha(data).then((res) => {
        backImgBase.value = res.data.originalImageBase64
        blockBackImgBase.value = res.data.jigsawImageBase64
        backToken.value = res.data.token
        secretKey.value = res.data.secretKey
    }).catch((e) => {
        tipWords.value = '网络异常，请稍后重试'
    })
}

function init() {
    text.value = props.explain
    getPictrue()
    nextTick(() => {
        if (!rootEl.value) return
        const { imgHeight, imgWidth, barHeight, barWidth } = resetSize({
            $el: rootEl.value,
            imgSize: props.imgSize,
            barSize: props.barSize
        })
        setSize.imgHeight = imgHeight
        setSize.imgWidth = imgWidth
        setSize.barHeight = barHeight
        setSize.barWidth = barWidth
        emit('ready', instanceProxy)
    })
}

watch(
    () => props.type,
    () => { init() }
)

onMounted(() => {
    init()
    if (rootEl.value) {
        rootEl.value.onselectstart = () => false
    }
})

defineExpose({
    refresh
})
</script>
<style scoped lang="scss">
.verify-tips {
    position: absolute;
    left: 0px;
    bottom: 0px;
    width: 100%;
    height: 30px;
    line-height: 30px;
    color: #fff;
}
</style>