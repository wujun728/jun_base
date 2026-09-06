<template>
    <view :style="labelStyle" class="title">{{ label }}</view>
    <view :style="numberStyle" class="number">{{ formatNumber(number,props.place) }}</view>
</template>
  
<script setup>
import { computed } from 'vue';

const props = defineProps({
    label: {
        type: String,
        default: "订单数量"
    },
    width: {
        type: Number,
        default: 300
    },
    labelColor: {
        type: String,
        default: '#white'
    },
    labelSize: {
        type: Number,
        default: 16
    },
    number: {
        type: Number,
        default: 80
    },
    numberColor: {
        type: String,
        default: 'red'
    },
    numberSize: {
        type: Number,
        default: 20
    },
    place: {
        type: Number,
        default: 2
    }
})
const labelStyle = computed(() => {
    return {
        width: `${props.width}rpx`,
        color: props.labelColor,
        fontSize: `${props.labelSize}px`
    }
})

const numberStyle = computed(() => {
    return {
        width: `${props.width}rpx`,
        color: props.numberColor,
        fontSize: `${props.numberSize}px`
    }
})

function formatNumber(num,place) {
    
    let fixedNum = Number(num).toFixed(place); // 将数字保留两位小数
    let parts = fixedNum.split('.'); // 拆分整数部分和小数部分
    let integerPart = parts[0]; // 整数部分
    let decimalPart = parts[1]; // 小数部分

    // 使用padStart方法补0到小数部分
    decimalPart = decimalPart.padStart(place, '0');

    return integerPart + '.' + decimalPart;
}


</script>
  
<style lang="scss" scoped>
.title {
    text-align: center;
}

.number {
    text-align: center;
}
</style>
  