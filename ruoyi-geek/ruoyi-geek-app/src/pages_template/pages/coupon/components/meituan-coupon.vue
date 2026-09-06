<template>
    <view class="meituan">
        <view class="content">
            <view class="left">
                <view class="sum">
                    ￥<text class="num">8</text>
                </view>
                <view class="type">抵用券</view>
            </view>
            <view class="centre">
                <view class="title">【洗牙】8元无门槛红包</view>
                <view class="valid-date">今日到期</view>
            </view>
            <view class="right">
                <view size="mini" class="immediate-use" :round="true">立即使用</view>
            </view>
        </view>
        <view class="tips">
            <view class="circle-left"></view>
            <view class="circle-right"></view>
            <view class="explain u-line-1">满8.1元可用、限最新版本客户端使用</view>
            <view class="rule">
                <text>使用规则</text>
                <u-icon name="arrow-right" color="" :size="20" @click=""></u-icon>
            </view>
        </view>
    </view>
</template>
<style scoped lang="scss">
.meituan {
    margin: 30rpx auto;
    background-color: #ffffff;
    width: 700rpx;
    // border: 10rpx;
    color: $u-warning;
    font-size: 28rpx;

    .content {
        display: flex;
        align-items: center;
        padding: 80rpx 20rpx;
        border: 10rpx;
        background-color: #fff5f4;

        .left {
            .sum {
                font-size: 32rpx;

                .num {
                    font-size: 60rpx;
                    font-weight: bold;
                }
            }
        }

        .centre {
            margin-left: 40rpx;

            .title {
                font-size: 32rpx;
                font-weight: bold;
                color: $u-main-color;
                margin-bottom: 20rpx;
            }
        }

        .right {
            margin-left: 30rpx;

            .immediate-use {
                padding: 0 20rpx;
                height: 50rpx;
                border-radius: 25rpx;
                line-height: 50rpx;
                background-color: $u-warning !important;
                color: #ffffff !important;
                font-size: 24rpx;
                border: none;
                word-break: keep-all;
            }
        }
    }

    .tips {
        padding: 0 20rpx;
        border: 10rpx;
        background-color: $u-info-light;
        position: relative;
        color: $u-tips-color;
        display: flex;
        justify-content: space-between;
        line-height: 60rpx;
        font-size: 24rpx;

        .circle-left,
        .circle-right {
            position: absolute;
            height: 36rpx;
            width: 18rpx;
            background-color: #f2f2f2;
        }

        .circle-right {
            border-radius: 40rpx 0 0 40rpx;
            right: 0;
            top: -18rpx;
        }

        .circle-left {
            border-radius: 0 40rpx 40rpx 0;
            left: 0;
            top: -18rpx;
        }

        .rule {
            font-size: 24rpx;
            display: flex;
            align-items: center;

            text {
                margin-right: 10rpx;
                flex: 1;
            }
        }
    }
}
</style>