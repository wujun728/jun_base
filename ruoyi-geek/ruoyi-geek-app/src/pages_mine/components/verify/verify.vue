<template>
    <view :class="mode == 'pop' ? 'mask' : ''" v-show="showBox">
        <view :class="mode == 'pop' ? 'verifybox' : ''" :style="{ 'max-width': parseInt(imgSize.width) + 30 + 'px' }">
            <view class="verifybox-top" v-if="mode == 'pop'">
                请完成安全验证
                <text class="verifybox-close" @click="clickShow = false">
                    <text class="iconfont icon-close"></text>
                </text>
            </view>
            <view class="verifybox-bottom" :style="{ padding: mode == 'pop' ? '15px' : '0' }">
                <!-- 验证码容器 -->
                <!-- 滑动 -->
                <view v-if="componentType == 'VerifySlide'">
                    <VerifySlide @success="success" :captchaType="captchaType" :type="verifyType" :figure="figure"
                        :check="check" :arith="arith" :mode="mode" :vSpace="vSpace" :explain="explain"
                        :imgSize="imgSize" :blockSize="blockSize" :barSize="barSize" :defaultImg="defaultImg"
                        ref="instance" />
                </view>
                <!-- 点选 -->
                <view v-if="componentType == 'VerifyPoints'">
                    <VerifyPoint :captchaType="captchaType" :type="verifyType" :figure="figure" :arith="arith"
                        :check="check" :mode="mode" :vSpace="vSpace" :explain="explain" :imgSize="imgSize"
                        :blockSize="blockSize" :barSize="barSize" :defaultImg="defaultImg" ref="instance" />
                </view>
            </view>
        </view>
    </view>
</template>
<script>
/**
 * Verify 验证码组件
 * @description 分发验证码使用
 * */
import VerifySlide from './verifySlider/verifySlider.vue'
import VerifyPoint from "./verifyPoint/verifyPoint.vue"
import defaultImg from '@/pages_mine/static/default.jpg'

export default {
    name: 'Vue2Verify',
    props: {
        captchaType: {
            type: String,
            required: true
        },
        figure: {
            type: Number
        },
        arith: {
            type: Number
        },
        mode: {
            type: String,
            default: 'pop'
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
            type: Object
        },
        check: {
            type: Function
        }
    },
    data() {
        return {
            // showBox:true,
            clickShow: false,
            // 内部类型
            verifyType: undefined,
            // 所用组件类型
            componentType: undefined,
            defaultImg: defaultImg
        }
    },
    mounted() {
        this.uuid()
    },
    methods: {
        // 生成 uuid
        uuid() {
            var s = [];
            var hexDigits = "0123456789abcdef";
            for (var i = 0; i < 36; i++) {
                s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
            }
            s[14] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
            s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
            s[8] = s[13] = s[18] = s[23] = "-";

            var slider = 'slider' + '-' + s.join("");
            var point = 'point' + '-' + s.join("");
            // 判断下是否存在 slider
            if (!uni.getStorageSync('slider')) {
                uni.setStorageSync('slider', slider)
            }
            if (!uni.getStorageSync('point')) {
                uni.setStorageSync("point", point);
            }
        },
        success(e) {
            this.$emit('success', e)
        },
        /**
         * refresh
         * @description 刷新
         * */
        refresh() {
            if (this.instance.refresh) {
                this.instance.refresh()
            }
        },
        show() {
            if (this.mode == "pop") {
                this.clickShow = true;
            }
        }
    },
    computed: {
        instance() {
            return this.$refs.instance || {}
        },
        showBox() {
            if (this.mode == 'pop') {
                return this.clickShow
            } else {
                return true;
            }
        }
    },
    watch: {
        captchaType: {
            immediate: true,
            handler(captchaType) {
                switch (captchaType.toString()) {
                    case 'blockPuzzle':
                        this.verifyType = '2'
                        this.componentType = 'VerifySlide'
                        break
                    case 'clickWord':
                        this.verifyType = ''
                        this.componentType = 'VerifyPoints'
                        break
                }
            }
        },
    },
    components: {
        VerifySlide,
        VerifyPoint
    },
}
</script>
<style scoped lang="scss">
@import './static/index.scss'
</style>
