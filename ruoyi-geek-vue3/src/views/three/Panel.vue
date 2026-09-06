<script lang="ts" setup>
import { ref } from 'vue';
const props = defineProps({
    title: {
        type: String,
        default: '标题'
    }
});
const isMinimized = ref(false);
const isClosed = ref(false);
const toggleMinimize = () => isMinimized.value = !isMinimized.value
const toggleClose = () => isClosed.value = !isClosed.value
</script>
<template>
    <div class="panel" :class="{ minimized: isMinimized }" v-if="!isClosed">
        <div class="header">
            <div class="title">{{ title }}</div>
            <div class="buttons">
                <div class="button reduce" @click="toggleMinimize">-</div>
                <div class="button close" @click="toggleClose">x</div>
            </div>
        </div>
        <div class="body" v-if="!isMinimized">
            <slot></slot>
        </div>
    </div>
</template>
<style scoped lang="scss">
$radius: 5px;

.panel {
    position: absolute;
    background-color: rgba(0, 0, 0, 0.5);
    border-radius: $radius;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
    z-index: 999;
    color: white;

    &:hover {
        background-color: rgba(0, 0, 0, 0.8);
    }

    .header {
        height: 20px;
        border-radius: $radius $radius 0 0;
        background-color: rgba(0, 0, 0, 0.9);
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 0 10px;

        .title {
            font-size: 12px;
        }

        .buttons {
            display: flex;
            justify-content: space-between;
            width: 30px;

            .button {
                font-size: 10px;
                font-weight: bolder;
                width: 12px;
                height: 12px;
                border-radius: 50%;
                color: #fff;
                text-align: center;
                line-height: 12px;
                // 设置文字不可选中
                -webkit-user-select: none;
                -moz-user-select: none;
                -ms-user-select: none;
                user-select: none;

                &.reduce {
                    background-color: rgba(0, 0, 255, 0.8);
                }

                &.close {
                    background-color: rgba(255, 0, 0, 0.8);
                }

                &:hover {
                    box-shadow: 0 0 5px rgba(255, 255, 255, 0.5);
                    cursor: pointer;
                }

                &:active {
                    box-shadow: 0 0 5px rgba(255, 255, 255, 0.5);
                    background-color: rgba(255, 255, 255, 0.5);
                    cursor: pointer;
                    transition: all 0.2s;
                }
            }
        }
    }

    .body {
        padding: 10px;
    }
}
</style>