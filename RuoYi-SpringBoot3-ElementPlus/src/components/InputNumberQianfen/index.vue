<template>
    <el-input :formatter="qianFormatter" :parser="qianParser" @change="handleChange" v-model="content" :placeholder="placeholder" />
</template>
<script setup>
import commafy from 'xe-utils/commafy';
import toNumber from 'xe-utils/toNumber';
import endsWith from 'xe-utils/endsWith';

function qianFormatter(v) {
    let digits = 0;
    if (v && v.indexOf('.') > -1) {
        const decimalsL = v.split('.')[1].length;
        if (decimalsL > 0) digits = decimalsL > props.digits ? props.digits : decimalsL;
    }
    return endsWith(v, '.') ? v : /^[+-]?(0|([1-9]\d*))(\.\d+)?$/g.test(v) ? commafy(toNumber(v), { digits }) : null;
}

function qianParser(v) {
    return endsWith(v, '.') ? v : toNumber(v);
}

const props = defineProps({
    modelValue: [String, Number],
    placeholder: {
        type: String
    },
    digits: {
        type: Number,
        default: 2
    }
});

const { proxy } = getCurrentInstance();
const emit = defineEmits();

function handleChange() {
    let emitValue = content.value;
    if (endsWith(emitValue, '.')) {
        emitValue = toNumber(emitValue.substr(0, emitValue.length - 1));
        content.value = emitValue;
    }
    emit('update:modelValue', emitValue);
    emit('change');
}

const content = ref('');
watch(
    () => props.modelValue,
    (v) => {
        if (v !== content.value) {
            content.value = v;
        }
    },
    { immediate: true }
);
</script>
