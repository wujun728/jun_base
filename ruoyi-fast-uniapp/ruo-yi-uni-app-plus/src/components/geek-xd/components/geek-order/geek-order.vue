<template>
    <view class="geek-card" @click="$emit('click')">
        <view class="geek-header">
            <view class="geek-shop">{{ shop }} > </view>
            <view class="geek-status">{{ status }}</view>
        </view>
        <view class="geek-content">
            <image class="geek-img" :src="img"></image>
            <view class="geek-label">{{ label }}</view>
            <view class="geek-sum">
                <view class=".geek-sum-1">￥{{ number.integerPart }}</view>
                <view class=".geek-sum-2">. {{ number.decimalPart }}</view>
                <view class=".geek-sum-3">共 {{ num }} 件</view>
            </view>
        </view>
        <view class="geek-footer">
            <view class="geek-more" @click="$emit('more')">更多</view>
            <view class="geek-buttonGroup">
                <view class="geek-btn" @click="$emit('sell')">卖了换钱</view>
                <view class="geek-btn" @click="$emit('return')">退换/售后</view>
                <view class="geek-buy" @click="$emit('again')">再次购买</view>
            </view>
        </view>
    </view>
</template>
<script setup>
import { computed } from 'vue';

const props = defineProps({
    shop: {
        type: String,
        default: ''
    },
    status: {
        type: String,
        default: ''
    },
    img: {
        type: String,
        default: ''
    },
    label: {
        type: String,
        default: ''
    },
    price: {
        type: Number,
        default: 0
    },
    num: {
        type: Number,
        default: 0
    },
    type: {
        type: String,
        default: 'line'
    }
})

const number = computed(() => {
    return formatNumber(props.price, 2)
})

function formatNumber(num, place) {

    let fixedNum = Number(num).toFixed(place); // 将数字保留两位小数
    let parts = fixedNum.split('.'); // 拆分整数部分和小数部分
    let integerPart = parts[0]; // 整数部分
    let decimalPart = parts[1]; // 小数部分

    // 使用padStart方法补0到小数部分
    decimalPart = decimalPart.padStart(place, '0');

    return {
        integerPart, decimalPart
    }
}
</script>
<style lang="scss" scoped>
.geek-card {

    position: relative;
    padding: 28rpx 18rpx 26rpx 16rpx;
    border: 1rpx solid rgb(183, 183, 183);
    border-radius: 50rpx;
    height: 400rpx;
    width: 700rpx;
    margin: 25rpx;

    .geek-header {
        height: 60rpx;
        width: 664rpx;
        margin-bottom: 6rpx;

        .geek-shop {
            font-size: 28rpx;
            font-weight: 600;
        }

        .geek-status {
            width: 100rpx;
            text-align: center;
            position: absolute;
            opacity: 0.5;
            top: 30rpx;
            right: 30rpx;
            font-size: 25rpx;
        }

    }

    .geek-content {
        position: relative;

        margin-bottom: 46rpx;
        height: 170rpx;
        width: 664rpx;

        .geek-img {
            border-radius: 30rpx;
            height: 170rpx;
            width: 170rpx;
            display: inline-block;
        }

        .geek-label {
            position: absolute;
            top: 40rpx;
            left: 178rpx;

            width: 350rpx;

            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            overflow: hidden;
            text-overflow: ellipsis;
        }

        .geek-sum {
            position: absolute;
            top: 44rpx;
            right: 0rpx;

            width: 126rpx;
            height: 84rpx;

            .geek-sum-1 {
                position: absolute;
                top: 0;
                left: 0;
                font-size: 32rpx;
            }

            .geek-sum-2 {
                position: absolute;
                top: 30rpx;
                left: 84rpx;
                font-size: 20rpx;
            }

            .geek-sum-3 {
                position: absolute;
                top: 55rpx;
                left: 46rpx;
                font-size: 20rpx;
            }
        }
    }

    .geek-footer {
        height: 60rpx;
        width: 664rpx;
        font-size: 25rpx;

        .geek-more {
            width: 100rpx;
            height: 60rpx;
            margin-right: 110rpx;

            padding: 10rpx;

            text-align: center;
            display: inline-block;
        }

        .geek-buttonGroup {
            width: 450rpx;
            height: 60rpx;
            display: inline-block;

            .geek-btn {
                border: 1rpx solid #E9E9E9;
                width: 140rpx;
                height: 60rpx;
                border-radius: 80rpx;
                opacity: 0.8;

                padding: 10rpx;

                margin-right: 10rpx;

                text-align: center;
                display: inline-block;

            }

            .geek-buy {
                width: 140rpx;
                height: 60rpx;
                border-radius: 80rpx;
                opacity: 0.8;

                padding: 10rpx;

                text-align: center;
                display: inline-block;

                color: #F25E53;
                border: 1rpx solid #F4DADA;
            }



        }
    }

}
</style>