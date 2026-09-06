<template>
    <view class="card" :class="type" @click="$emit('click')">
        <image class="img" :src="img"/>
        <view class="right">
            <view class="title">{{ title }}</view>
            <view class="subTitle">{{ subTitle }}</view>
            <view class="price">￥ {{ price }}</view>
        </view>
    </view>
</template>


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
        default: 'line'
    }
})
</script>

<style lang="scss" scoped>
.card {
    padding: 0;
    border-radius: 10px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    height: 240rpx;
    width: 700rpx;
    padding: 20rpx;
    margin: 10rpx;
    position: relative;

    .img {
        height: 200rpx;
        width: 200rpx;
    }

    .right {
        position: absolute;
        top: 20rpx;
        left: 240rpx;
        height: 200rpx;

        .title {
            width: 400rpx;
            font-size: 35rpx;
        }

        .subTitle {
            width: 400rpx;
            margin-top: 10rpx;
            font-size: 20rpx;
            color: rgb(87, 87, 87);
        }

        .price {
            position: absolute;
            font-size: 40rpx;
            color: red;
            bottom: 10rpx;
            width: 400rpx;
        }
    }
}

.rect {
    
    border-radius: 10px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    height: 500rpx;
    width: 350rpx;
    padding: 0px;
    margin: 10rpx;
    position: relative;
    display: inline-block;

    .img {
        border-radius: 10px 10px 0 0;
        height: 350rpx;
        width: 350rpx;
    }

    .right {
        padding: 20rpx;
        position: absolute;
        height: 160rpx;
        width: 350rpx;
        top: 350rpx;
        left: 20rpx;
        .title {
            width: 330rpx;
            font-size: 25rpx;
        }

        .subTitle {
            width: 330rpx;
            margin-top: 10rpx;
            font-size: 20rpx;
            color: rgb(87, 87, 87);
        }

        .price {
            position: absolute;
            font-size: 30rpx;
            color: red;
            bottom: 10rpx;
            width: 330rpx;
        }
    }
}
</style>