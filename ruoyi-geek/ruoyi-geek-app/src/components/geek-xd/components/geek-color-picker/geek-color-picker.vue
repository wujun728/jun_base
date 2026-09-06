<script setup lang="ts">
import { ref, reactive, watch, nextTick, getCurrentInstance } from 'vue';

// 类型定义
interface RGBAColor {
	r: number;
	g: number;
	b: number;
	a: number;
}

interface HSBColor {
	h: number;
	s: number;
	b: number;
}

interface SitePosition {
	top: number;
	left: number;
}

interface ElementPosition {
	top: number;
	left: number;
	width: number;
	height: number;
}

const emit = defineEmits<{
	(e: 'confirm', data: { rgba: RGBAColor; hex: string }): void;
}>();

const instance = getCurrentInstance();

const props = defineProps({
	color: {
		type: Object as () => RGBAColor,
		default() {
			return { r: 0, g: 0, b: 0, a: 0 }
		}
	},
	spareColor: {
		type: Array as () => RGBAColor[],
		default() {
			return []
		}
	}
});

// 响应式数据
const show = ref<boolean>(false);
const active = ref<boolean>(false);
const rgba = reactive<RGBAColor>({ r: 0, g: 0, b: 0, a: 1 });
const hsb = reactive<HSBColor>({ h: 0, s: 0, b: 0 });
const site = reactive<SitePosition[]>([{ top: 0, left: 0 }, { left: 0, top: 0 }, { left: 0, top: 0 }]);
const index = ref<number>(0);
const bgcolor = reactive<RGBAColor>({ r: 255, g: 0, b: 0, a: 1 });
const hex = ref<string>('#000000');
const mode = ref<boolean>(true);
const position = ref<ElementPosition[] | null>(null);
const colorList = ref<RGBAColor[]>([
	{ r: 244, g: 67, b: 54, a: 1 },
	{ r: 233, g: 30, b: 99, a: 1 },
	{ r: 156, g: 39, b: 176, a: 1 },
	{ r: 103, g: 58, b: 183, a: 1 },
	{ r: 63, g: 81, b: 181, a: 1 },
	{ r: 33, g: 150, b: 243, a: 1 },
	{ r: 3, g: 169, b: 244, a: 1 },
	{ r: 0, g: 188, b: 212, a: 1 },
	{ r: 0, g: 150, b: 136, a: 1 },
	{ r: 76, g: 175, b: 80, a: 1 },
	{ r: 139, g: 195, b: 74, a: 1 },
	{ r: 205, g: 220, b: 57, a: 1 },
	{ r: 255, g: 235, b: 59, a: 1 },
	{ r: 255, g: 193, b: 7, a: 1 },
	{ r: 255, g: 152, b: 0, a: 1 },
	{ r: 255, g: 87, b: 34, a: 1 },
	{ r: 121, g: 85, b: 72, a: 1 },
	{ r: 158, g: 158, b: 158, a: 1 },
	{ r: 0, g: 0, b: 0, a: 0.5 },
	{ r: 0, g: 0, b: 0, a: 0 },
]);

// 初始化（替代 created 钩子）
Object.assign(rgba, props.color);
if (props.spareColor.length !== 0) {
	colorList.value = props.spareColor;
}

/**
 * 初始化
 */
const init = (): void => {
	// hsb 颜色
	Object.assign(hsb, rgbToHsb(rgba));
	setValue(rgba);
};

const moveHandle = (): void => { };

const open = (): void => {
	show.value = true;
	nextTick(() => {
		init();
		setTimeout(() => {
			active.value = true;
			setTimeout(() => {
				getSelectorQuery();
			}, 350)
		}, 50)
	})
};

const close = (): void => {
	active.value = false;
	nextTick(() => {
		setTimeout(() => {
			show.value = false;
		}, 500)
	})
};

const confirm = (): void => {
	close();
	emit('confirm', {
		rgba: rgba,
		hex: hex.value
	})
};

// 选择模式
const select = (): void => {
	mode.value = !mode.value
};

// 常用颜色选择
const selectColor = (item: RGBAColor): void => {
	setColorBySelect(item)
};

const touchstart = (e: TouchEvent, idx: number): void => {
	const { pageX, pageY, clientX, clientY } = e.touches[0];
	setPosition(clientX, clientY, idx);
};

const touchmove = (e: TouchEvent, idx: number): void => {
	const { pageX, pageY, clientX, clientY } = e.touches[0];
	setPosition(clientX, clientY, idx);
};

const touchend = (e: TouchEvent, idx: number): void => {
	// 原代码为空实现
};

/**
 * 设置位置
 */
const setPosition = (x: number, y: number, idx: number): void => {
	index.value = idx;
	if (!position.value || !position.value[idx]) return;

	const {
		top,
		left,
		width,
		height
	} = position.value[idx];
	// 设置最大最小值

	site[idx].left = Math.max(0, Math.min(parseInt(String(x - left)), width));
	if (idx === 0) {
		site[idx].top = Math.max(0, Math.min(parseInt(String(y - top)), height));
		// 设置颜色
		hsb.s = parseInt(String((100 * site[idx].left) / width));
		hsb.b = parseInt(String(100 - (100 * (site[idx].top as number)) / height));
		setColor();
		setValue(rgba);
	} else {
		setControl(idx, site[idx].left);
	}
};

/**
 * 设置 rgb 颜色
 */
const setColor = (): void => {
	const rgb = HSBToRGB(hsb);
	rgba.r = rgb.r;
	rgba.g = rgb.g;
	rgba.b = rgb.b;
};

/**
 * 设置二进制颜色
 * @param {RGBAColor} rgb
 */
const setValue = (rgb: RGBAColor): void => {
	hex.value = '#' + rgbToHex(rgb);
};

const setControl = (idx: number, x: number): void => {
	if (!position.value || !position.value[idx]) return;

	const {
		width
	} = position.value[idx];

	if (idx === 1) {
		hsb.h = parseInt(String((360 * x) / width));
		const newRgb = HSBToRGB({
			h: hsb.h,
			s: 100,
			b: 100
		});
		bgcolor.r = newRgb.r;
		bgcolor.g = newRgb.g;
		bgcolor.b = newRgb.b;
		setColor();
	} else {
		rgba.a = parseFloat((x / width).toFixed(1));
	}
	setValue(rgba);
};

/**
 * rgb 转 二进制 hex
 * @param {RGBAColor} rgb
 * @returns {string} 十六进制颜色字符串（不含#）
 */
const rgbToHex = (rgb: RGBAColor): string => {
	let hex = [rgb.r.toString(16), rgb.g.toString(16), rgb.b.toString(16)];
	hex.map(function (str, i) {
		if (str.length == 1) {
			hex[i] = '0' + str;
		}
	});
	return hex.join('');
};

const setColorBySelect = (getrgb: RGBAColor): void => {
	const {
		r,
		g,
		b,
		a
	} = getrgb;

	rgba.r = r ? parseInt(String(r)) : 0;
	rgba.g = g ? parseInt(String(g)) : 0;
	rgba.b = b ? parseInt(String(b)) : 0;
	rgba.a = a ? a : 0;

	Object.assign(hsb, rgbToHsb(rgba));
	changeViewByHsb();
};

const changeViewByHsb = (): void => {
	if (!position.value || position.value.length < 3) return;

	const [a, b, c] = position.value;
	site[0].left = parseInt(String(hsb.s * a.width / 100));
	site[0].top = parseInt(String((100 - hsb.b) * a.height / 100));
	setColor();
	setValue(rgba);

	const newRgb = HSBToRGB({
		h: hsb.h,
		s: 100,
		b: 100
	});
	bgcolor.r = newRgb.r;
	bgcolor.g = newRgb.g;
	bgcolor.b = newRgb.b;

	site[1].left = hsb.h / 360 * b.width;
	site[2].left = rgba.a * c.width;
};

/**
 * hsb 转 rgb
 * @param {HSBColor} hsb 颜色模式  H(hues)表示色相，S(saturation)表示饱和度，B（brightness）表示亮度
 * @returns {RGBAColor} RGB颜色对象
 */
const HSBToRGB = (hsb: HSBColor): RGBAColor => {
	let rgb: { r: number; g: number; b: number } = { r: 0, g: 0, b: 0 };
	let h = Math.round(hsb.h);
	let s = Math.round((hsb.s * 255) / 100);
	let v = Math.round((hsb.b * 255) / 100);
	if (s == 0) {
		rgb.r = rgb.g = rgb.b = v;
	} else {
		let t1 = v;
		let t2 = ((255 - s) * v) / 255;
		let t3 = ((t1 - t2) * (h % 60)) / 60;
		if (h == 360) h = 0;
		if (h < 60) {
			rgb.r = t1;
			rgb.b = t2;
			rgb.g = t2 + t3;
		} else if (h < 120) {
			rgb.g = t1;
			rgb.b = t2;
			rgb.r = t1 - t3;
		} else if (h < 180) {
			rgb.g = t1;
			rgb.r = t2;
			rgb.b = t2 + t3;
		} else if (h < 240) {
			rgb.b = t1;
			rgb.r = t2;
			rgb.g = t1 - t3;
		} else if (h < 300) {
			rgb.b = t1;
			rgb.g = t2;
			rgb.r = t2 + t3;
		} else if (h < 360) {
			rgb.r = t1;
			rgb.g = t2;
			rgb.b = t1 - t3;
		} else {
			rgb.r = 0;
			rgb.g = 0;
			rgb.b = 0;
		}
	}
	return {
		r: Math.round(rgb.r),
		g: Math.round(rgb.g),
		b: Math.round(rgb.b),
		a: 1
	};
};

/**
 * rgb转hsb
 * @param {RGBAColor} rgb RGB颜色对象
 * @returns {HSBColor} HSB颜色对象
 */
const rgbToHsb = (rgb: RGBAColor): HSBColor => {
	let hsb = {
		h: 0,
		s: 0,
		b: 0
	};
	let min = Math.min(rgb.r, rgb.g, rgb.b);
	let max = Math.max(rgb.r, rgb.g, rgb.b);
	let delta = max - min;
	hsb.b = max;
	hsb.s = max != 0 ? 255 * delta / max : 0;
	if (hsb.s != 0) {
		if (rgb.r == max) hsb.h = (rgb.g - rgb.b) / delta;
		else if (rgb.g == max) hsb.h = 2 + (rgb.b - rgb.r) / delta;
		else hsb.h = 4 + (rgb.r - rgb.g) / delta;
	} else hsb.h = -1;
	hsb.h *= 60;
	if (hsb.h < 0) hsb.h = 0;
	hsb.s *= 100 / 255;
	hsb.b *= 100 / 255;
	return hsb;
};

const getSelectorQuery = (): void => {
	const views = uni.createSelectorQuery().in(instance!.proxy);
	views.selectAll('.boxs')
		.boundingClientRect(data => {
			if (Array.isArray(data) ? data.length === 0 : !data) {
				setTimeout(() => getSelectorQuery(), 20)
				return
			}
			position.value = data as unknown as ElementPosition[];
			setColorBySelect(rgba);
		})
		.exec();
};

// 监听 props
watch(() => props.spareColor, (newVal) => {
	colorList.value = newVal;
});

// 暴露组件方法供父组件调用
defineExpose({
	open,
	close,
	confirm
});
</script>
<template>
	<view v-show="show" class="t-wrapper" @touchmove.stop.prevent="moveHandle">
		<view class="t-mask" :class="{ active: active }" @click.stop="close"></view>
		<view class="t-box" :class="{ active: active }">
			<view class="t-header">
				<view class="t-header-button" @click="close">取消</view>
				<view class="t-header-button confrim" @click="confirm">确认</view>
			</view>
			<view class="t-color__box"
				:style="{ background: 'rgb(' + bgcolor.r + ',' + bgcolor.g + ',' + bgcolor.b + ')' }">
				<view class="t-background boxs" @touchstart="touchstart($event, 0)" @touchmove="touchmove($event, 0)"
					@touchend="touchend($event, 0)">
					<view class="t-color-mask"></view>
					<view class="t-pointer" :style="{
						top: site[0].top - 8 + 'px',
						left: site[0].left - 8 + 'px'
					}">
					</view>
				</view>
			</view>
			<view class="t-control__box">
				<view class="t-control__color">
					<view class="t-control__color-content"
						:style="{ background: 'rgba(' + rgba.r + ',' + rgba.g + ',' + rgba.b + ',' + rgba.a + ')' }">
					</view>
				</view>
				<view class="t-control-box__item">
					<view class="t-controller boxs" @touchstart="touchstart($event, 1)"
						@touchmove="touchmove($event, 1)" @touchend="touchend($event, 1)">
						<view class="t-hue">
							<view class="t-circle" :style="{ left: site[1].left - 12 + 'px' }"></view>
						</view>
					</view>
					<view class="t-controller boxs" @touchstart="touchstart($event, 2)"
						@touchmove="touchmove($event, 2)" @touchend="touchend($event, 2)">
						<view class="t-transparency">
							<view class="t-circle" :style="{ left: site[2].left - 12 + 'px' }"></view>
						</view>
					</view>
				</view>
			</view>
			<view class="t-result__box">
				<view v-if="mode" class="t-result__item">
					<view class="t-result__box-input">{{ hex }}</view>
					<view class="t-result__box-text">HEX</view>
				</view>
				<template v-else>
					<view class="t-result__item">
						<view class="t-result__box-input">{{ rgba.r }}</view>
						<view class="t-result__box-text">R</view>
					</view>
					<view class="t-result__item">
						<view class="t-result__box-input">{{ rgba.g }}</view>
						<view class="t-result__box-text">G</view>
					</view>
					<view class="t-result__item">
						<view class="t-result__box-input">{{ rgba.b }}</view>
						<view class="t-result__box-text">B</view>
					</view>
					<view class="t-result__item">
						<view class="t-result__box-input">{{ rgba.a }}</view>
						<view class="t-result__box-text">A</view>
					</view>
				</template>

				<view class="t-result__item t-select" @click="select">
					<view class="t-result__box-input">
						<view>切换</view>
						<view>模式</view>
					</view>
				</view>
			</view>
			<view class="t-alternative">
				<view class="t-alternative__item" v-for="(item, index) in colorList" :key="index">
					<view class="t-alternative__item-content"
						:style="{ background: 'rgba(' + item.r + ',' + item.g + ',' + item.b + ',' + item.a + ')' }"
						@click="selectColor(item)">
					</view>
				</view>
			</view>
		</view>
	</view>
</template>
<style lang="scss" scoped>
.t-wrapper {
	position: fixed;
	top: 0;
	bottom: 0;
	left: 0;
	width: 100%;
	box-sizing: border-box;
	z-index: 9999;
}

.t-box {
	width: 100%;
	position: absolute;
	bottom: 0;
	padding: 30upx 0;
	padding-top: 0;
	background: #fff;
	transition: all 0.3s;
	transform: translateY(100%);

	&.active {
		transform: translateY(0%);
	}
}

.t-header {
	display: flex;
	justify-content: space-between;
	width: 100%;
	height: 100upx;
	border-bottom: 1px #eee solid;
	box-shadow: 1px 0 2px rgba(0, 0, 0, 0.1);
	background: #fff;
}

.t-header-button {
	display: flex;
	align-items: center;
	width: 150upx;
	height: 100upx;
	font-size: 30upx;
	color: #666;
	padding-left: 20upx;

	&:last-child {
		justify-content: flex-end;
		padding-right: 20upx;
	}

	&.confrim {
		color: #007AFF;
	}
}

.t-mask {
	position: absolute;
	top: 0;
	left: 0;
	right: 0;
	bottom: 0;
	background: rgba(0, 0, 0, 0.6);
	z-index: -1;
	transition: all 0.3s;
	opacity: 0;

	&.active {
		opacity: 1;
	}
}

// 避免使用 &__box 写法，改为完整类名
.t-color__box {
	position: relative;
	height: 400upx;
	background: rgb(255, 0, 0);
	overflow: hidden;
	box-sizing: border-box;
	margin: 0 20upx;
	margin-top: 20upx;
	box-sizing: border-box;
}

.t-color-mask {
	position: absolute;
	top: 0;
	left: 0;
	right: 0;
	bottom: 0;
	width: 100%;
	height: 400upx;
	background: linear-gradient(to top, #000, rgba(0, 0, 0, 0));
}

.t-background {
	position: absolute;
	top: 0;
	left: 0;
	right: 0;
	bottom: 0;
	background: linear-gradient(to right, #fff, rgba(255, 255, 255, 0));
}

.t-pointer {
	position: absolute;
	bottom: -8px;
	left: -8px;
	z-index: 2;
	width: 15px;
	height: 15px;
	border: 1px #fff solid;
	border-radius: 50%;
}

.t-show-color {
	width: 100upx;
	height: 50upx;
}

// 避免使用嵌套方式，使用完整类名
.t-control__box {
	margin-top: 50upx;
	width: 100%;
	display: flex;
	padding-left: 20upx;
	box-sizing: border-box;
}

.t-control__color {
	flex-shrink: 0;
	width: 100upx;
	height: 100upx;
	border-radius: 50%;
	background-color: #fff;
	background-image: linear-gradient(45deg, #eee 25%, transparent 25%, transparent 75%, #eee 75%, #eee),
		linear-gradient(45deg, #eee 25%, transparent 25%, transparent 75%, #eee 75%, #eee);
	background-size: 36upx 36upx;
	background-position: 0 0, 18upx 18upx;
	border: 1px #eee solid;
	overflow: hidden;
}

.t-control__color-content {
	width: 100%;
	height: 100%;
}

.t-control-box__item {
	display: flex;
	flex-direction: column;
	justify-content: space-between;
	width: 100%;
	padding: 0 30upx;
}

.t-controller {
	position: relative;
	width: 100%;
	height: 16px;
	background-color: #fff;
	background-image: linear-gradient(45deg, #eee 25%, transparent 25%, transparent 75%, #eee 75%, #eee),
		linear-gradient(45deg, #eee 25%, transparent 25%, transparent 75%, #eee 75%, #eee);
	background-size: 32upx 32upx;
	background-position: 0 0, 16upx 16upx;
}

.t-hue {
	width: 100%;
	height: 100%;
	background: linear-gradient(to right, #f00 0%, #ff0 17%, #0f0 33%, #0ff 50%, #00f 67%, #f0f 83%, #f00 100%);
}

.t-transparency {
	width: 100%;
	height: 100%;
	background: linear-gradient(to right, rgba(0, 0, 0, 0) 0%, rgb(0, 0, 0));
}

.t-circle {
	position: absolute;
	top: -2px;
	width: 20px;
	height: 20px;
	box-sizing: border-box;
	border-radius: 50%;
	background: #fff;
	box-shadow: 0 0 2px 1px rgba(0, 0, 0, 0.1);
}

// 使用完整的类名而不是嵌套
.t-result__box {
	margin-top: 20upx;
	padding: 10upx;
	width: 100%;
	display: flex;
	box-sizing: border-box;
}

.t-result__item {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	padding: 10upx;
	width: 100%;
	box-sizing: border-box;
}

.t-result__box-input {
	padding: 10upx 0;
	width: 100%;
	font-size: 28upx;
	box-shadow: 0 0 1px 1px rgba(0, 0, 0, 0.1);
	color: #999;
	text-align: center;
	background: #fff;
}

.t-result__box-text {
	margin-top: 10upx;
	font-size: 28upx;
	line-height: 2;
}

.t-select {
	flex-shrink: 0;
	width: 150upx;
	padding: 0 30upx;

	.t-result__box-input {
		border-radius: 10upx;
		border: none;
		color: #999;
		box-shadow: 1px 1px 2px 1px rgba(0, 0, 0, 0.1);
		background: #fff;

		&:active {
			box-shadow: 0px 0px 1px 0px rgba(0, 0, 0, 0.1);
		}
	}
}

.t-alternative {
	display: flex;
	flex-wrap: wrap;
	width: 100%;
	padding-right: 10upx;
	box-sizing: border-box;
}

.t-alternative__item {
	margin-left: 12upx;
	margin-top: 10upx;
	width: 50upx;
	height: 50upx;
	border-radius: 10upx;
	background-color: #fff;
	background-image: linear-gradient(45deg, #eee 25%, transparent 25%, transparent 75%, #eee 75%, #eee),
		linear-gradient(45deg, #eee 25%, transparent 25%, transparent 75%, #eee 75%, #eee);
	background-size: 36upx 36upx;
	background-position: 0 0, 18upx 18upx;
	border: 1px #eee solid;
	overflow: hidden;

	&:active {
		transition: all 0.3s;
		transform: scale(1.1);
	}
}

.t-alternative__item-content {
	width: 50upx;
	height: 50upx;
	background: rgba(255, 0, 0, 0.5);
}
</style>
