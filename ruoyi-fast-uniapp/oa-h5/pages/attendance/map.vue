<template>
	<view class="">
		<map style="width: 100%; height: 100vh" :latitude="latitude" :longitude="longitude" :markers="covers"
			:circles="circles" :enable-zoom="false" :enable-scroll="false">
		</map>
	</view>
</template>

<script setup>
	import {
		ref,
		reactive
	} from "vue";
	import {
		onLoad
	} from "@dcloudio/uni-app";
	const id = ref(0);
	const title = ref("map");
	const mapShow = ref(false);
	const latitude = ref(0);
	const longitude = ref(0);
	const covers = reactive([{
		latitude: 0, //纬度
		longitude: 0, //经度
		iconPath: "/static/image/attendance/range.png",
	}, ]);
	const circles = reactive([{
		latitude: 0, //纬度
		longitude: 0, //经度
		fillColor: "#9db0a1A",
		radius: 1000,
		strokeWidth: 2,
		color: "#00aaff",
	}, ]);
	onLoad((options) => {
		latitude.value = options.lat;
		longitude.value = options.lng;
		covers[0].latitude = options.lat;
		covers[0].longitude = options.lng;
		circles[0].latitude = options.lat;
		circles[0].longitude = options.lng;
		circles[0].radius = Number(options.radius);

		mapShow.value = true;
	});

	onMounted(() => {});
</script>

<style lang="scss" scoped></style>