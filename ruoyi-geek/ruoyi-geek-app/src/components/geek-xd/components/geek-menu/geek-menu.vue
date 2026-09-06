<script setup>
import { computed } from 'vue';

const props = defineProps({
    icon: {
        type: String,
        default: ''
    },
    size: {
        type: Number,
        default: 80
    },
    label: {
        type: String,
        default: "菜单"
    },
    labelColor: {
        type: String,
        default: '#515151'
    },
    type: {
        type: String,
        default: 'circle'
    }
})
const menuStyle = computed(() => {
    return {
        width: `${props.size + 40}rpx`,
        height: `${props.size + 40}rpx`
    }
})
const titleStype = computed(() => {
    return {
        width: `${props.size + 40}rpx`,
        color: props.labelColor
    }
})
</script>
<template>
    <view>
        <view class="menu" :class="type" :style="menuStyle" @click="$emit('click')">
            <image :src="icon" style="width: 100%;height: 100%"></image>
        </view>
        <view class="title" :style="titleStype">{{ label }}</view>
    </view>
</template>
<style lang="scss" scoped>
.menu {
    padding: 20rpx;
}

.circle {
    padding: 20rpx;
    border-radius: 100%;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

    &:active {
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.8);
    }
}

.rect {
    padding: 15rpx;
    margin-bottom: -15rpx;

    &:active {
        opacity: 0.5;
    }
}

.title {
    text-align: center;
}
</style>