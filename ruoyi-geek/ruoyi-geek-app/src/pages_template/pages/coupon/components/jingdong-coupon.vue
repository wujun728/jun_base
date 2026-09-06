<template>
    <view class="jingdong">
        <view class="left">
            <view class="sum">
                ￥
                <text class="num">100</text>
            </view>
            <view class="type">满149元可用</view>
        </view>
        <view class="right">
            <view class="top">
                <view class="title">
                    <text class="tag">限品类东券</text>
                    <text>仅可购买个人护理部分商品</text>
                </view>
                <view class="bottom">
                    <view class="date u-line-1">2020.01.01-2020.01.31</view>
                    <view class="immediate-use">立即使用</view>
                </view>
            </view>
            <view class="tips">
                <view class="explain">
                    <u-icon name="zhuanfa" class="transpond" :size="24"></u-icon>
                    <text>可赠送</text>
                </view>
            </view>
        </view>
    </view>
</template>
<style scoped lang="scss">
.jingdong {
    margin-top: 40rpx;
    width: 700rpx;
    height: auto;
    background-color: #ffffff;
    display: flex;

    .left {
        padding: 0 30rpx;
        background-color: rgb(95, 148, 224); //rgb(94, 152, 225);
        text-align: center;
        font-size: 28rpx;
        color: #ffffff;

        .sum {
            margin-top: 50rpx;
            font-weight: bold;
            font-size: 32rpx;

            .num {
                font-size: 80rpx;
            }
        }

        .type {
            margin-bottom: 50rpx;
            font-size: 24rpx;
        }
    }

    .right {
        padding: 20rpx 20rpx 0;
        font-size: 28rpx;

        .top {
            border-bottom: 2rpx dashed $u-border-color;

            .title {
                margin-right: 60rpx;
                line-height: 40rpx;

                .tag {
                    padding: 4rpx 20rpx;
                    background-color: rgb(73, 154, 201);
                    border-radius: 20rpx;
                    color: #ffffff;
                    font-weight: bold;
                    font-size: 24rpx;
                    margin-right: 10rpx;
                }
            }

            .bottom {
                display: flex;
                margin-top: 20rpx;
                align-items: center;
                justify-content: space-between;
                margin-bottom: 10rpx;

                .date {
                    font-size: 20rpx;
                    flex: 1;
                }

                .immediate-use {
                    height: auto;
                    padding: 0 20rpx;
                    font-size: 24rpx;
                    border-radius: 40rpx;
                    line-height: 40rpx;
                    color: rgb(117, 142, 165);
                    border: 2rpx solid rgb(117, 142, 165);
                }
            }
        }

        .tips {
            width: 100%;
            line-height: 50rpx;
            display: flex;
            align-items: center;
            justify-content: space-between;
            font-size: 24rpx;

            .transpond {
                margin-right: 10rpx;
            }

            .explain {
                display: flex;
                align-items: center;
            }

            .particulars {
                width: 30rpx;
                height: 30rpx;
                box-sizing: border-box;
                padding-top: 8rpx;
                border-radius: 50%;
                background-color: $u-info-disabled;
                text-align: center;
            }
        }
    }
}
</style>