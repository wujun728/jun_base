<template>
	<view :class="autoSize ? 'avatar' : ''">
		<image v-if="autoSize" :style="{borderRadius: radius + 'rpx'}" class="avatar-image" @error="avatarError"
			@click="clickAvatar" :src="src ? src : '/static/image/default-avatar.png'" mode="aspectFill"></image>
		<image v-else :style="{borderRadius: radius + 'rpx',width: width+'rpx',height: height+ 'rpx'}"
			class="avatar-image" @error="avatarError" @click="clickAvatar"
			:src="src ? src : '/static/image/default-avatar.png'" mode="aspectFill"></image>
	</view>
</template>

<script setup lang="ts">
	import { toRefs } from 'vue'
	import defaultAvatar from '/static/image/default-avatar.png'
	const props = withDefaults(
		defineProps<{
			src ?: string,
			radius ?: number
			autoSize ?: boolean
			width ?: number
			height ?: number
		}>(), {
		src: '/static/image/default-avatar.png',
		radius: 12,
		autoSize: true,
		width: 90,
		height: 90
	})
	const { src, radius, autoSize, width, height } = toRefs(props)

	const emit = defineEmits(['change'])

	const clickAvatar = () : void => {
		emit('change')
	}

	const avatarError = () : void => {
		src.value = defaultAvatar
	}
</script>

<style scoped lang="scss">
	.avatar {
		width: 100%;
		height: 100%;

		.avatar-image {
			width: 100%;
			height: 100%;
		}
	}
</style>