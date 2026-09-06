<template>
    <view style="position: relative; touch-action: none; touch-action: pan-y">
        <view v-if="type === '2'" class="verify-img-out"
            :style="{ height: (parseInt(imgSize.height) + vSpace) + 'px' }">
            <view class="verify-img-panel" :style="{
                width: imgSize.width,
                height: imgSize.height,
            }">

                <image :src="backImgBase ? ('data:image/png;base64,' + backImgBase) : defaultImg" alt=""
                    style="width:100%;height:100%;display:block"></image>
                <view class="verify-refresh" @click="refresh" v-show="showRefresh">
                    <text class="iconfont icon-refresh"></text>
                </view>
                <transition name="tips">
                    <text class="verify-tips" v-if="tipWords" :class="passFalg ? 'suc-bg' : 'err-bg'">
                        {{ tipWords }}
                    </text>
                </transition>
            </view>
        </view>

        <!-- 公共部分 -->
        <view class="verify-bar-area" :style="{
            width: imgSize.width,
            height: '40px',
            'line-height': '40px'
        }">
            <text class="verify-msg" v-text="text"></text>
            <view class="verify-left-bar"
                :style="{ width: leftBarWidth ? leftBarWidth : '40px', height: '40px', 'border-color': leftBarBorderColor, transaction: transitionWidth }">
                <text class="verify-msg" v-text="finishText"></text>
                <view class="verify-move-block" @touchstart="start" @touchend="end" @touchmove="move"
                    :style="{ width: '40px', height: '40px', 'background-color': moveBlockBackgroundColor, left: moveBlockLeft, transition: transitionLeft }">
                    <text :class="['verify-icon iconfont', iconClass]" :style="{ color: iconColor }"></text>
                    <view v-if="type === '2'" class="verify-sub-block" :style="{
                        'width': Math.floor(parseInt(imgSize.width) * 47 / 310) + 'px',
                        'height': imgSize.height,
                        'top': '-' + (parseInt(imgSize.height) + vSpace) + 'px',
                    }">
                        <image :src="'data:image/png;base64,' + blockBackImgBase" alt=""
                            style="width:100%;height:100%;display:block"></image>
                    </view>
                </view>
            </view>
        </view>
    </view>
</template>
<script setup>
/**
 * VerifySlide
 * @description 滑块
 */
import { getCurrentInstance, nextTick, reactive, ref, toRefs, watch } from 'vue'
import { aesEncrypt } from '../utils/ase.js'
import { myRequest } from '../utils/request'

const props = defineProps({
    captchaType: {
        type: String,
    },
    type: {
        type: String,
        default: '1',
    },
    mode: {
        type: String,
        default: 'fixed',
    },
    vSpace: {
        type: Number,
        default: 5,
    },
    explain: {
        type: String,
        default: '向右滑动完成验证',
    },
    imgSize: {
        type: Object,
        default: () => ({
            width: '310px',
            height: '155px',
        }),
    },
    blockSize: {
        type: Object,
        default: () => ({
            width: '50px',
            height: '50px',
        }),
    },
    barSize: {
        type: Object,
        default: () => ({
            width: '100%',
            height: '40px',
        }),
    },
    defaultImg: {
        type: String,
        default: '',
    },
    check: {
        type: Function
    }
})

const emit = defineEmits(['ready', 'success', 'error'])

const {
    captchaType,
    type,
    mode,
    vSpace,
    explain,
    imgSize,
    blockSize,
    barSize,
    defaultImg,
} = toRefs(props)

const secretKey = ref('')
const passFalg = ref(false)
const backImgBase = ref('')
const blockBackImgBase = ref('')
const backToken = ref('')
const startMoveTime = ref('')
const endMovetime = ref('')
const tipsBackColor = ref('')
const tipWords = ref('')
const text = ref('')
const finishText = ref('')
const setSize = reactive({
    imgHeight: 0,
    imgWidth: 0,
    barHeight: 0,
    barWidth: 0,
})
const top = ref(0)
const left = ref(0)
const moveBlockLeft = ref()
const leftBarWidth = ref()
const moveBlockBackgroundColor = ref()
const leftBarBorderColor = ref('#ddd')
const iconColor = ref()
const iconClass = ref('icon-right')
const status = ref(false)
const isEnd = ref(false)
const showRefresh = ref(true)
const transitionLeft = ref('')
const transitionWidth = ref('')

const { proxy } = getCurrentInstance()
const parent = proxy?.$parent

function init() {
    text.value = explain.value
    getPictrue()
    nextTick(() => {
        parent?.$emit('ready', proxy)
        emit('ready', proxy)
    })
}

function start(e) {
    startMoveTime.value = new Date().getTime()
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
    const query = uni.createSelectorQuery().in(proxy)
    const barArea = query.select('.verify-bar-area')
    let bar_area_left
    let barArea_offsetWidth
    barArea.boundingClientRect((data) => {
        bar_area_left = Math.ceil(data.left)
        barArea_offsetWidth = Math.ceil(data.width)

        if (status.value && isEnd.value === false) {
            const x = !e.touches ? Math.ceil(e.clientX) : Math.ceil(e.touches[0].pageX)
            let move_block_left = x - bar_area_left
            if (type.value !== '1') {
                if (move_block_left >= barArea_offsetWidth - parseInt(parseInt(blockSize.value.width) / 2) - 2) {
                    move_block_left = barArea_offsetWidth - parseInt(parseInt(blockSize.value.width) / 2) - 2
                }
            }

            if (move_block_left <= 0) {
                move_block_left = parseInt(parseInt(blockSize.value.width) / 2)
            }

            moveBlockLeft.value = move_block_left - parseInt(parseInt(blockSize.value.width) / 2) + 'px'
            leftBarWidth.value = move_block_left - parseInt(parseInt(blockSize.value.width) / 2) + 'px'
        }
    }).exec()
}

function end() {
    endMovetime.value = new Date().getTime()
    if (status.value && isEnd.value === false) {
        if (type.value !== '1') {
            let moveLeftDistance = parseInt((moveBlockLeft.value || '').replace('px', ''))
            moveLeftDistance = (moveLeftDistance * 310) / parseInt(imgSize.value.width)

            const captchaVerification = secretKey.value
                ? aesEncrypt(backToken.value + '---' + JSON.stringify({ x: moveLeftDistance, y: 5.0 }), secretKey.value)
                : backToken.value + '---' + JSON.stringify({ x: moveLeftDistance, y: 5.0 })
            const data = {
                captchaType: captchaType.value,
                pointJson: secretKey.value
                    ? aesEncrypt(JSON.stringify({ x: moveLeftDistance, y: 5.0 }), secretKey.value)
                    : JSON.stringify({ x: moveLeftDistance, y: 5.0 }),
                token: backToken.value,
            }
            props.check(data).then((res) => {
                moveBlockBackgroundColor.value = '#5cb85c'
                leftBarBorderColor.value = '#5cb85c'
                iconColor.value = '#fff'
                iconClass.value = 'icon-check'
                showRefresh.value = true
                isEnd.value = true
                setTimeout(() => {
                    if (mode.value === 'pop' && parent) {
                        parent.clickShow = false
                    }
                    refresh()
                }, 1500)
                passFalg.value = true
                tipWords.value = `${((endMovetime.value - startMoveTime.value) / 1000).toFixed(2)}s验证成功`
                setTimeout(() => {
                    tipWords.value = ''
                    parent?.$emit('success', { captchaVerification })
                    emit('success', { captchaVerification })
                }, 1000)

            }).catch(e => {
                moveBlockBackgroundColor.value = '#d9534f'
                leftBarBorderColor.value = '#d9534f'
                iconColor.value = '#fff'
                iconClass.value = 'icon-close'
                passFalg.value = false
                setTimeout(() => {
                    refresh()
                }, 1000)
                parent?.$emit('error', proxy)
                emit('error', proxy)
                tipWords.value = '验证失败'
                setTimeout(() => {
                    tipWords.value = ''
                }, 1000)
            })
        }
        status.value = false
    }
}

function refresh() {
    showRefresh.value = true
    finishText.value = ''
    transitionLeft.value = 'left .3s'
    moveBlockLeft.value = 0
    leftBarWidth.value = false
    transitionWidth.value = 'width .3s'
    leftBarBorderColor.value = '#ddd'
    moveBlockBackgroundColor.value = '#fff'
    iconColor.value = '#000'
    iconClass.value = 'icon-right'
    getPictrue()
    isEnd.value = false
    setTimeout(() => {
        transitionWidth.value = ''
        transitionLeft.value = ''
        text.value = explain.value
    }, 300)
}

function getLeft(node) {
    let leftValue = 0
    let currentNode = node
    while (currentNode) {
        leftValue += currentNode.offsetLeft
        currentNode = currentNode.offsetParent
    }
    return leftValue
}

function getPictrue() {
    const data = {
        captchaType: captchaType.value,
        clientUid: uni.getStorageSync('slider'),
        ts: Date.now(),
    }
    myRequest({
        url: '/captcha/get',
        data,
        method: 'POST',
    }).then((res) => {
        backImgBase.value = res.data.originalImageBase64
        blockBackImgBase.value = res.data.jigsawImageBase64
        backToken.value = res.data.token
        secretKey.value = res.data.secretKey
    }).catch(e => {
        backImgBase.value = null
        blockBackImgBase.value = null
    })
}

watch(
    () => type.value,
    () => {
        init()
    },
    { immediate: true }
)
</script>
<style scoped lang="scss">
@import '../static/index.scss'
</style>
<style scoped>
.suc-bg {
    background-color: rgba(92, 184, 92, .5);
    filter: progid:DXImageTransform.Microsoft.gradient(startcolorstr=#7f5CB85C, endcolorstr=#7f5CB85C);
}

.err-bg {
    background-color: rgba(217, 83, 79, .5);
    filter: progid:DXImageTransform.Microsoft.gradient(startcolorstr=#7fD9534F, endcolorstr=#7fD9534F);
}
</style>
