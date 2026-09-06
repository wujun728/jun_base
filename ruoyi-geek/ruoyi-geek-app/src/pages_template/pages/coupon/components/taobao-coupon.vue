<script setup lang="ts">
import logo from '@/static/logo.png';
</script>
<template>
    <view class="taobao">
        <view class="title">
            <view class="left">
                <image class="buddha" :src="logo" mode="aspectFill"></image>
                <view class="store">袜子精保护协会</view>
            </view>
            <view class="entrance">进店</view>
        </view>
        <view class="ticket">
            <view class="left">
                <image class="picture" :src="logo" mode="widthFix"></image>
                <view class="introduce">
                    <view class="top">
                        ￥
                        <text class="big">3</text>
                        满88减3
                    </view>
                    <view class="type">店铺优惠券</view>
                    <view class="date u-line-1">2019.11.28-2020.1.24</view>
                </view>
            </view>
            <view class="right">
                <view class="use immediate-use" :round="true">去使用</view>
            </view>

        </view>
    </view>
</template>
<style scoped lang="scss">
.taobao {
    margin-top: 40rpx;
    width: 700rpx;
    background-color: white;
    padding: 30rpx 20rpx 20rpx;
    border-radius: 20rpx;

    .title {
        display: flex;
        align-items: center;
        justify-content: space-between;
        margin-bottom: 20rpx;
        font-size: 30rpx;

        .left {
            display: flex;
            align-items: center;
        }

        .store {
            font-weight: 500;
        }

        .buddha {
            width: 70rpx;
            height: 70rpx;
            border-radius: 10rpx;
            margin-right: 10rpx;
        }

        .entrance {
            color: $u-info;
            border: solid 2rpx $u-info;
            line-height: 48rpx;
            padding: 0 30rpx;
            background: none;
            border-radius: 15px;
        }
    }

    .ticket {
        display: flex;

        .left {
            width: 70%;
            padding: 30rpx 20rpx;
            background-color: rgb(255, 245, 244);
            border-radius: 20rpx;
            border-right: dashed 2rpx rgb(224, 215, 211);
            display: flex;

            .picture {
                width: 172rpx;
                height: 172rpx;
                border-radius: 20rpx;
            }

            .introduce {
                margin-left: 10rpx;

                .top {
                    color: $u-warning;
                    font-size: 28rpx;

                    .big {
                        font-size: 60rpx;
                        font-weight: bold;
                        margin-right: 10rpx;
                    }
                }

                .type {
                    font-size: 28rpx;
                    color: $u-info-dark;
                }

                .date {
                    margin-top: 10rpx;
                    font-size: 20rpx;
                    color: $u-info-dark;
                }
            }
        }

        .right {
            width: 30%;
            padding: 40rpx 20rpx;
            background-color: rgb(255, 245, 244);
            border-radius: 20rpx;
            display: flex;
            align-items: center;

            .use {
                height: auto;
                padding: 0 20rpx;
                font-size: 24rpx;
                border-radius: 40rpx;
                color: #ffffff !important;
                background-color: $u-warning !important;
                line-height: 40rpx;
                color: rgb(117, 142, 165);
                margin-left: 20rpx;
            }
        }
    }
}
</style>