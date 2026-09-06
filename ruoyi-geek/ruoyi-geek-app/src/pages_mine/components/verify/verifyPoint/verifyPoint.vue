<template>
    <view style="position: relative">
        <view class="verify-image-out" v-show="showImage">
            <view class="verify-img-panel" :style="{
                'width': imgSize.width,
                'height': imgSize.height,
                'margin-bottom': vSpace + 'px'
            }">
                <view class="verify-refresh" style="z-index:3" @click="refresh" v-show="showRefresh">
                    <text class="iconfont icon-refresh"></text>
                </view>
                <image :src="pointBackImgBase ? ('data:image/png;base64,' + pointBackImgBase) : defaultImg" id="image"
                    ref="canvas" style="width:100%;height:100%;display:block"
                    @click=" bindingClick ? canvasClick($event) : undefined"></image>
                <view v-for="(tempPoint, index) in tempPoints" :key="index" class="point-area" :style="{
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
                </view>
            </view>
        </view>
        <!-- 'height': this.barSize.height, -->
        <view class="verify-bar-area" :style="{
            'width': imgSize.width,
            'color': barAreaColor,
            'border-color': barAreaBorderColor,
            'line-height': '40px'
        }">
            <text class="verify-msg">{{ text }}</text>
        </view>
    </view>
</template>
<script setup>
/**
 * VerifyPoints
 * @description 点选
 */
import { getCurrentInstance, nextTick, onMounted, reactive, ref, watch } from 'vue'
import { aesEncrypt } from '../utils/ase.js'
import { myRequest } from '../utils/request'

const props = defineProps({
    mode: {
        type: String,
        default: 'fixed',
    },
    captchaType: {
        type: String,
    },
    // 兼容原有监听的类型字段
    type: {
        type: String,
    },
    vSpace: {
        type: Number,
        default: 5,
    },
    imgSize: {
        type: Object,
        default: () => ({
            width: '310px',
            height: '155px',
        }),
    },
    barSize: {
        type: Object,
        default: () => ({
            width: '310px',
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

const secretKey = ref('')
const checkNum = ref(3)
const fontPos = ref([])
const checkPosArr = ref([])
const num = ref(1)
const pointBackImgBase = ref('')
const poinTextList = ref([])
const backToken = ref('')
const imgRand = ref(0)
const setSize = reactive({
    imgHeight: 0,
    imgWidth: 0,
    barHeight: 0,
    barWidth: 0,
})
const showImage = ref(true)
const tempPoints = ref([])
const text = ref('')
const barAreaColor = ref('#fff')
const barAreaBorderColor = ref('#fff')
const showRefresh = ref(true)
const bindingClick = ref(true)
const imgLeft = ref('')
const imgTop = ref('')

const canvas = ref(null)
const { proxy } = getCurrentInstance()
const parent = proxy?.$parent

const emit = defineEmits(['ready', 'success', 'error'])

function init() {
    fontPos.value.splice(0, fontPos.value.length)
    checkPosArr.value.splice(0, checkPosArr.value.length)
    num.value = 1
    nextTick(() => {
        refresh()
        parent?.$emit('ready', proxy)
        emit('ready', proxy)
    })
}

function canvasClick(e) {
    const query = uni.createSelectorQuery().in(proxy)
    query.select('#image').boundingClientRect((data) => {
        imgLeft.value = Math.ceil(data.left)
        imgTop.value = Math.ceil(data.top)
        checkPosArr.value.push(getMousePos(canvas.value, e))
        if (num.value === checkNum.value) {
            num.value = createPoint(getMousePos(canvas.value, e))
            checkPosArr.value = pointTransfrom(checkPosArr.value, props.imgSize)
            setTimeout(() => {
                const captchaVerification = secretKey.value
                    ? aesEncrypt(backToken.value + '---' + JSON.stringify(checkPosArr.value), secretKey.value)
                    : backToken.value + '---' + JSON.stringify(checkPosArr.value)
                const data = {
                    captchaType: props.captchaType,
                    pointJson: secretKey.value
                        ? aesEncrypt(JSON.stringify(checkPosArr.value), secretKey.value)
                        : JSON.stringify(checkPosArr.value),
                    token: backToken.value,
                }
                props.check(data).then((res) => {
                    barAreaColor.value = '#4cae4c'
                    barAreaBorderColor.value = '#5cb85c'
                    text.value = '验证成功'
                    bindingClick.value = false
                    setTimeout(() => {
                        if (props.mode === 'pop' && parent) {
                            parent.clickShow = false
                        }
                        refresh()
                    }, 1500)
                    parent?.$emit('success', { captchaVerification })
                    emit('success', { captchaVerification })
                }).catch(e => {
                    parent?.$emit('error', proxy)
                    emit('error', proxy)
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
            num.value = createPoint(getMousePos(canvas.value, e))
        }
    }).exec()
}

function getMousePos(obj, e) {
    return {
        x: Math.ceil(e.detail.x) - Number(imgLeft.value || 0),
        y: Math.ceil(e.detail.y) - Number(imgTop.value || 0),
    }
}

function createPoint(pos) {
    tempPoints.value.push(Object.assign({}, pos))
    return ++num.value
}

function refresh() {
    tempPoints.value.splice(0, tempPoints.value.length)
    barAreaColor.value = '#000'
    barAreaBorderColor.value = '#ddd'
    bindingClick.value = true

    fontPos.value.splice(0, fontPos.value.length)
    checkPosArr.value.splice(0, checkPosArr.value.length)
    num.value = 1

    getPictrue()
    showRefresh.value = true
}

function getPictrue() {
    const data = {
        captchaType: props.captchaType,
        clientUid: uni.getStorageSync('point'),
        ts: Date.now(),
    }
    myRequest({
        url: '/captcha/get',
        data,
        method: 'POST',
    }).then((res) => {
        pointBackImgBase.value = res.data.originalImageBase64
        backToken.value = res.data.token
        secretKey.value = res.data.secretKey
        poinTextList.value = res.data.wordList
        text.value = '请依次点击【' + poinTextList.value.join(',') + '】'
    }).catch((error) => {
        pointBackImgBase.value = null
    })
}

const pointTransfrom = (pointArr, imgSize) => pointArr.map((p) => ({
    x: Math.round(310 * p.x / parseInt(imgSize.width)),
    y: Math.round(155 * p.y / parseInt(imgSize.height))
}))

watch(
    () => props.type,
    () => {
        init()
    },
    { immediate: true }
)

onMounted(() => {
    console.log(props.defaultImg)
})
</script>
<style scoped lang="scss">
@import '../static/index.scss'
</style>
