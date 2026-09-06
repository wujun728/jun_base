<template>
    <div ref="rootEl" style="position: relative">
        <div class="verify-img-out">
            <div class="verify-img-panel" :style="{
                'width': setSize.imgWidth,
                'height': setSize.imgHeight,
                'background-size': setSize.imgWidth + ' ' + setSize.imgHeight,
                'margin-bottom': vSpace + 'px'
            }">
                <div class="verify-refresh" style="z-index:3" @click="refresh" v-show="showRefresh">
                    <i class="iconfont icon-refresh"></i>
                </div>
                <img :src="'data:image/png;base64,' + pointBackImgBase" ref="canvas" alt=""
                    style="width:100%;height:100%;display:block" @click="bindingClick && canvasClick($event)">

                <div v-for="(tempPoint, index) in tempPoints" :key="index" class="point-area" :style="{
                    'background-color': '#1abd6c',
                    color: '#fff',
                    'z-index': 9999,
                    width: '20px',
                    height: '20px',
                    'text-align': 'center',
                    'line-height': '20px',
                    'border-radius': '50%',
                    position: 'absolute',
                    top: parseInt(tempPoint.y - 10) + 'px',
                    left: parseInt(tempPoint.x - 10) + 'px'
                }">
                    {{ index + 1 }}
                </div>
            </div>
        </div>
        <!-- 'height': this.barSize.height, -->
        <div class="verify-bar-area" :style="{
            'width': setSize.imgWidth,
            'color': barAreaColor,
            'border-color': barAreaBorderColor,
            'line-height': barSize.height
        }">
            <span class="verify-msg">{{ text }}</span>
        </div>
    </div>
</template>
<script setup>
/**
 * VerifyPoints
 * @description 点选
 */
import { aesEncrypt } from '@/utils/jsencrypt'
import { getCaptcha } from '@/api/captcha'
import { resetSize } from './verifyutil'
import { nextTick, onMounted, reactive, ref, getCurrentInstance, useTemplateRef } from 'vue'

defineOptions({ name: 'VerifyPoints' })

const emit = defineEmits(['ready', 'success', 'error'])

const props = defineProps({
    // 弹出式 pop，固定 fixed
    mode: {
        type: String,
        default: 'fixed'
    },
    captchaType: {
        type: String
    },
    // 间隔
    vSpace: {
        type: Number,
        default: 5
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
const canvas = useTemplateRef("canvas")
const instanceProxy = getCurrentInstance()?.proxy

const secretKey = ref('')
const checkNum = ref(3)
const fontPos = reactive([])
const checkPosArr = reactive([])
const num = ref(1)
const pointBackImgBase = ref('')
const poinTextList = ref([])
const backToken = ref('')

const setSize = reactive({
    imgHeight: 0,
    imgWidth: 0,
    barHeight: 0,
    barWidth: 0
})

const tempPoints = reactive([])
const text = ref('')
const barAreaColor = ref(undefined)
const barAreaBorderColor = ref(undefined)
const showRefresh = ref(true)
const bindingClick = ref(true)

const getMousePos = (obj, e) => ({ x: e.offsetX, y: e.offsetY })

const pointTransfrom = (pointArr, imgSize) => pointArr.map((p) => ({
    x: Math.round((310 * p.x) / parseInt(imgSize.imgWidth)),
    y: Math.round((155 * p.y) / parseInt(imgSize.imgHeight))
}))

function getPictrue() {
    const data = { captchaType: props.captchaType }
    getCaptcha(data).then((res) => {
        pointBackImgBase.value = res.data.originalImageBase64
        backToken.value = res.data.token
        secretKey.value = res.data.secretKey
        poinTextList.value = res.data.wordList
        text.value = '请依次点击【' + poinTextList.value.join(',') + '】'
    }).catch((e) => {
        text.value = '网络异常，请稍后重试'
    })
}

function init() {
    fontPos.splice(0, fontPos.length)
    checkPosArr.splice(0, checkPosArr.length)
    num.value = 1
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

function refresh() {
    tempPoints.splice(0, tempPoints.length)
    barAreaColor.value = '#000'
    barAreaBorderColor.value = '#ddd'
    bindingClick.value = true
    fontPos.splice(0, fontPos.length)
    checkPosArr.splice(0, checkPosArr.length)
    num.value = 1
    getPictrue()
    text.value = '验证失败'
    showRefresh.value = true
}

function createPoint(pos) {
    tempPoints.push(Object.assign({}, pos))
    return num.value + 1
}

function canvasClick(e) {
    checkPosArr.push(getMousePos(canvas, e))
    if (num.value == checkNum.value) {
        num.value = createPoint(getMousePos(canvas, e))

        let arr = pointTransfrom(checkPosArr, setSize)
        checkPosArr.length = 0
        checkPosArr.push(...arr)

        setTimeout(() => {
            const captchaVerification = secretKey.value
                ? aesEncrypt(backToken.value + '---' + JSON.stringify(checkPosArr), secretKey.value)
                : backToken.value + '---' + JSON.stringify(checkPosArr)

            let data = {
                captchaType: props.captchaType,
                pointJson: secretKey.value
                    ? aesEncrypt(JSON.stringify(checkPosArr), secretKey.value)
                    : JSON.stringify(checkPosArr),
                token: backToken.value
            }

            props.check(data).then((res) => {
                barAreaColor.value = '#4cae4c'
                barAreaBorderColor.value = '#5cb85c'
                text.value = '验证成功'
                bindingClick.value = false
                if (props.mode == 'pop') {
                    setTimeout(() => {
                        refresh()
                    }, 1500)
                }
                emit('success', { captchaVerification })
            }).catch(e => {
                emit('error', instanceProxy)
                barAreaColor.value = '#d9534f'
                barAreaBorderColor.value = '#d9534f'
                text.value = '验证失败'
                setTimeout(() => {
                    refresh()
                }, 700)
            })
        }, 400)
    }

    if (num.value < checkNum.value) {
        num.value = createPoint(getMousePos(canvas, e))
    }
}

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