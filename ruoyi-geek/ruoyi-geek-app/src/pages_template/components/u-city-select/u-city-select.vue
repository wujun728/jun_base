<template>
	<u-popup :show="modelValue" mode="bottom" :popup="false" :mask="true" :closeable="true"
		:safe-area-inset-bottom="true" close-icon-color="#ffffff" :z-index="uZIndex" :maskCloseAble="maskCloseAble"
		@close="close">
		<u-tabs v-if="modelValue" :list="genTabsList" :scrollable="true" :current="tabsIndex" @change="tabsChange"
			ref="tabs" />
		<view class="area-box">
			<view class="u-flex" :class="{ 'change': isChange }">
				<view class="area-item">
					<view class="u-padding-10 u-bg-gray" style="height: 100%;">
						<scroll-view :scroll-y="true" style="height: 100%">
							<u-cell-group>
								<u-cell v-for="(item, index) in provinces" :title="item.label" :arrow="false"
									:index="index" :key="index" @click="provinceChange(index)">
									<template v-slot:right-icon>
										<u-icon v-if="isChooseP && province === index" size="17"
											name="checkbox-mark"></u-icon>
									</template>
								</u-cell>
							</u-cell-group>
						</scroll-view>
					</view>
				</view>
				<view class="area-item">
					<view class="u-padding-10 u-bg-gray" style="height: 100%;">
						<scroll-view :scroll-y="true" style="height: 100%">
							<u-cell-group v-if="isChooseP">
								<u-cell v-for="(item, index) in citys" :title="item.label" :arrow="false" :index="index"
									:key="index" @click="cityChange(index)">
									<template v-slot:right-icon>
										<u-icon v-if="isChooseC && city === index" size="17"
											name="checkbox-mark"></u-icon>
									</template>
								</u-cell>
							</u-cell-group>
						</scroll-view>
					</view>
				</view>
				<view class="area-item">
					<view class="u-padding-10 u-bg-gray" style="height: 100%;">
						<scroll-view :scroll-y="true" style="height: 100%">
							<u-cell-group v-if="isChooseC">
								<u-cell v-for="(item, index) in areas" :title="item.label" :arrow="false" :index="index"
									:key="index" @click="areaChange(index)">
									<template v-slot:right-icon>
										<u-icon v-if="isChooseA && area === index" size="17"
											name="checkbox-mark"></u-icon>
									</template>
								</u-cell>
							</u-cell-group>
						</scroll-view>
					</view>
				</view>
			</view>
		</view>
	</u-popup>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, PropType } from 'vue';
import provincesSource from "./province.json";
import citysSource from "./city.json";
import areasSource from "./area.json";

// Props 定义
const props = defineProps({
	// 通过双向绑定控制组件的弹出与收起
	modelValue: {
		type: Boolean,
		default: false
	},
	// 默认显示的地区，可传类似["河北省", "秦皇岛市", "北戴河区"]
	defaultRegion: {
		type: Array as PropType<string[]>,
		default: () => []
	},
	// 默认显示地区的编码，defaultRegion和areaCode同时存在，areaCode优先，可传类似["13", "1303", "130304"]
	areaCode: {
		type: Array as PropType<string[]>,
		default: () => []
	},
	// 是否允许通过点击遮罩关闭Picker
	maskCloseAble: {
		type: Boolean,
		default: true
	},
	// 弹出的z-index值
	zIndex: {
		type: [String, Number],
		default: 0
	}
});

// 事件定义
type Region = { label: string; value: string; }
interface CitySelectResult {
	province: Region;
	city: Region;
	area: Region;
}
const emit = defineEmits<{
	(e: 'update:modelValue', value: boolean): void;
	(e: 'close'): void;
	(e: 'city-change', result: CitySelectResult): void;
}>();

const isChooseP = ref(false); // 是否已经选择了省
const province = ref(0); // 省级下标
const provinces = ref(provincesSource);
const isChooseC = ref(false); // 是否已经选择了市
const city = ref(0); // 市级下标
const citys = ref(citysSource[0]);
const isChooseA = ref(false); // 是否已经选择了区
const area = ref(0); // 区级下标
const areas = ref(areasSource[0][0]);
const tabsIndex = ref(0);

// 计算属性
const isChange = computed(() => {
	return tabsIndex.value > 1;
});

type TabItem = { name: string; }
const genTabsList = computed((): TabItem[] => {
	let tabsList: TabItem[] = [{
		name: "请选择"
	}];

	if (isChooseP.value) {
		tabsList[0].name = provinces.value[province.value].label;
		tabsList[1] = {
			name: "请选择"
		};
	}

	if (isChooseC.value) {
		tabsList[1].name = citys.value[city.value].label;
		tabsList[2] = {
			name: "请选择"
		};
	}

	if (isChooseA.value) {
		tabsList[2].name = areas.value[area.value].label;
	}

	return tabsList;
});

const uZIndex = computed(() => {
	// 如果用户有传递z-index值，优先使用
	return props.zIndex ? props.zIndex : 1075; // 假设$u.zIndex.popup为1075
});

// 方法
const setProvince = (label = "", value = "") => {
	provinces.value.map((v, k) => {
		if (value ? v.value == value : v.label == label) {
			provinceChange(k);
		}
	});
};

const setCity = (label = "", value = "") => {
	citys.value.map((v, k) => {
		if (value ? v.value == value : v.label == label) {
			cityChange(k);
		}
	});
};

const setArea = (label = "", value = "") => {
	areas.value.map((v, k) => {
		if (value ? v.value == value : v.label == label) {
			isChooseA.value = true;
			area.value = k;
		}
	});
};

const close = () => {
	emit('update:modelValue', false);
	emit('close');
};

const tabsChange = (value: { index: number }) => {
	tabsIndex.value = value.index;
};

const provinceChange = (index: number) => {
	isChooseP.value = true;
	isChooseC.value = false;
	isChooseA.value = false;
	province.value = index;
	citys.value = citysSource[index];
	tabsIndex.value = 1;
};

const cityChange = (index: number) => {
	isChooseC.value = true;
	isChooseA.value = false;
	city.value = index;
	areas.value = areasSource[province.value][index];
	tabsIndex.value = 2;
};

const areaChange = (index: number) => {
	isChooseA.value = true;
	area.value = index;
	const result: CitySelectResult = {
		province: provinces.value[province.value],
		city: citys.value[city.value],
		area: areas.value[area.value]
	};
	emit('city-change', result);
	close();
};

// 生命周期钩子
onMounted(() => {
	if (props.areaCode.length == 3) {
		setProvince("", props.areaCode[0]);
		setCity("", props.areaCode[1]);
		setArea("", props.areaCode[2]);
	} else if (props.defaultRegion.length == 3) {
		setProvince(props.defaultRegion[0], "");
		setCity(props.defaultRegion[1], "");
		setArea(props.defaultRegion[2], "");
	}
});
</script>

<style lang="scss">
.area-box {
	width: 100%;
	overflow: hidden;
	height: 800rpx;

	>view {
		width: 150%;
		transition: transform 0.3s ease-in-out 0s;
		transform: translateX(0);

		&.change {
			transform: translateX(-33.3333333%);
		}
	}

	.area-item {
		width: 33.3333333%;
		height: 800rpx;
	}
}
</style>