<template>
    <view class="confirm-dialog" v-if="visible">
        <view class="dialog-mask"></view>
        <view class="dialog-content">
            <view class="dialog-header">提示</view>
            <view class="dialog-title">{{ title }}</view>
            <view class="dialog-buttons">
                <button class="button cancel-btn" @click="handleCancel" hover-class="button-hover">{{ cancelText
                    }}</button>
                <button class="button confirm-btn" @click="handleConfirm" hover-class="button-hover"
                    :style="{ background: color }">{{ confirmText }}</button>
            </view>
        </view>
    </view>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue'

const props = defineProps({
    visible: {
        type: Boolean,
        default: false
    },
    title: {
        type: String,
        default: '提示'
    },
    cancelText: {
        type: String,
        default: '取消'
    },
    confirmText: {
        type: String,
        default: '确认'
    },
    color: {
        type: String,
        default: '#1E88E5'
    }
})
const emit = defineEmits(['update:visible', 'confirm', 'cancel']);
const handleConfirm = () => {
    emit('update:visible', false);
    emit('confirm')
}

const handleCancel = () => {
    emit('cancel')
    emit('update:visible', false);
}
</script>

<style lang="scss" scoped>
.confirm-dialog {
    .dialog-mask {
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background: rgba(0, 0, 0, 0.4);
        z-index: 999;
    }

    .dialog-content {
        position: fixed;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        width: 280px;
        background: #FFFFFF;
        border-radius: 12px;
        z-index: 1000;

        .dialog-header {
            padding: 10px;
            text-align: center;
            font-size: 16px;
            color: #333;
        }

        .dialog-title {
            padding: 10px 20px 20px 20px;
            text-align: center;
            font-size: 16px;
            color: #333;
            line-height: 1.4;
        }

        .dialog-buttons {
            display: flex;
            justify-content: space-between;
            padding: 20rpx;
            gap: 20rpx;
            margin-bottom: 20rpx;

            .button {
                flex: 1;
                height: 80rpx;
                line-height: 80rpx;
                text-align: center;
                font-size: 32rpx;
                border-radius: 8rpx;
                border: none;

                &.button-hover {
                    opacity: 0.8;
                }
            }

            .cancel-btn {
                background: #F5F5F5;
                color: #333333;
            }

            .confirm-btn {
                color: #FFFFFF;
            }
        }
    }
}
</style>