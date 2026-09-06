<script setup lang="ts">
import JsBarcode from 'jsbarcode'
import { onMounted, ref, watch } from 'vue';

const props = withDefaults(defineProps < {
    data?: string,
    options?: JsBarcode.Options
} > (), {
    data: '',
    options: () => ({})
});
watch(() => props.data, () => { generateJsBarcode() });
const generateJsBarcode = async () => {
    JsBarcode(barcodeRef.value, props.data, props.options)
};
const barcodeRef = ref(null)
onMounted(() => {
    generateJsBarcode()
})
</script>
<template>
    <div>
        <img ref="barcodeRef"></img>
    </div>
</template>