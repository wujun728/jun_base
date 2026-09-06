<script setup>
const props = defineProps({
    img: {
        type: String,
        default: ''
    },
    title: {
        type: String,
        default: ''
    },
    subTitle: {
        type: String,
        default: ''
    },
    price: {
        type: Number,
        default: 0
    },
    type: {
        type: String,
        default: 'line' // line, rect
    }
})
</script>
<template>
    <view class="card" :class="type" @click="$emit('click')">
        <image class="img" :src="img" />
        <view class="content">
            <view class="title">{{ title }}</view>
            <view class="subTitle">{{ subTitle }}</view>
            <view class="price">￥{{ price }}</view>
        </view>
    </view>
</template>
<style lang="scss" scoped>
.card {
    padding: 0;
    border-radius: 10px;
    background-color: #ffffff;
    width: 700rpx;
    padding: 20rpx;
    margin: 10rpx;
    position: relative;

    .img {
        height: 200rpx;
        width: 200rpx;
    }
}

.line {
    display: flex;

    .content {
        height: 200rpx;
        padding-left: 20rpx;

        .title {
            width: 400rpx;
            font-size: 35rpx;
        }

        .subTitle {
            width: 400rpx;
            height: 90rpx;
            margin-top: 10rpx;
            font-size: 20rpx;
            color: rgb(87, 87, 87);
            line-height: 30rpx;
            display: -webkit-box;
            -webkit-box-orient: vertical;
            -webkit-line-clamp: 3;
            overflow: hidden;
            text-overflow: ellipsis;
        }

        .price {
            font-size: 40rpx;
            color: red;
            width: 400rpx;
        }
    }
}

.rect {
    border-radius: 10px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    width: 350rpx;
    padding: 0;
    margin: 10rpx;
    display: inline-block;

    .img {
        border-radius: 10px 10px 0 0;
        height: 350rpx;
        width: 350rpx;
    }

    .content {
        padding: 0 20rpx;
        margin: 0;
        width: 100%;
        display: flex;
        flex-direction: column;
        justify-content: space-between;

        .title {
            width: 330rpx;
            font-size: 25rpx;
        }

        .subTitle {
            width: 330rpx;
            font-size: 20rpx;
            color: rgb(87, 87, 87);
        }

        .price {
            font-size: 30rpx;
            color: red;
            width: 100%;
        }
    }
}
</style>