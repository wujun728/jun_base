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
                <view class="geek-price">
                    <view class="geek-sum-1">￥{{ number.integerPart }}</view>
                    <view class="geek-sum-2">. {{ number.decimalPart }}</view>
                </view>
                <view class="geek-sum-3">共 {{ num }} 件</view>
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
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    position: relative;
    padding: 20rpx;
    background-color: white;
    border-radius: 20rpx;
    height: 360rpx;
    width: 700rpx;
    margin: 25rpx;

    .geek-header {
        display: flex;
        justify-content: space-between;
        height: 60rpx;
        width: 100%;
        margin-bottom: 6rpx;

        .geek-shop {
            font-size: 28rpx;
            font-weight: 600;
        }

        .geek-status {
            width: 100rpx;
            text-align: center;
            opacity: 0.5;
            font-size: 25rpx;
            text-align: end;
        }

    }

    .geek-content {
        position: relative;
        height: auto;
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
            right: 20rpx;

            width: 150rpx;
            height: 84rpx;

            .geek-price {
                display: flex;
                justify-content: flex-end;
                font-weight: bold;

                .geek-sum-1 {
                    font-size: 32rpx;
                }

                .geek-sum-2 {
                    padding-top: 20rpx;
                    font-size: 20rpx;
                }
            }


            .geek-sum-3 {
                font-size: 20rpx;
                text-align: end;
            }
        }
    }

    .geek-footer {
        display: flex;
        justify-content: space-between;
        height: 60rpx;
        width: 100%;
        font-size: 25rpx;

        .geek-more {
            height: 60rpx;
            margin-right: 110rpx;
            text-align: center;
            line-height: 60rpx;
            display: inline-block;
        }

        .geek-buttonGroup {
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