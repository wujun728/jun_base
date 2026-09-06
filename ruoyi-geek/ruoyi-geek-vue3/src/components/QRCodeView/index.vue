<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue';
import QRCode from 'qrcode';

const props = withDefaults(defineProps<{
    data: string,
    logo?: string,
    logoSize?: number,
    options?: {
        width?: number,
        height?: number
    }
}>(), {
    data: '',
    logo: '',
    logoSize: 20,
    options: () => ({
        width: 400,
        height: 400
    })
});

const qrDataUrl = ref(null);
watch(() => props.data, () => { generateQRCode() });
const generateQRCode = async () => {
    qrDataUrl.value = await QRCode.toDataURL(props.data, props.options);
};

const qrcodeBoxStyle = computed(() => ({
    width: props.options.width + 'px',
    height: props.options.height + 'px'
}));
const qrcodeLogoStyle = computed(() => ({
    width: props.logoSize + '%'
}));
onMounted(() => {
    generateQRCode();
});
</script>
<template>
    <div class="qrcode-component">
        <div v-if="qrDataUrl" class="qrcode-box" :style="qrcodeBoxStyle">
            <img :src="qrDataUrl" class="qrcode-code" />
            <img v-if="logo" :src="logo" class="qrcode-logo" :style="qrcodeLogoStyle" />
        </div>
    </div>
</template>
<style scoped lang="scss">
.qrcode-component {
    .qrcode-box {
        position: relative;
        display: flex;
        justify-content: center;
        align-items: center;

        .qrcode-code {
            width: 100%;
            height: 100%;
            position: absolute;
            left: 0;
            top: 0;
        }

        .qrcode-logo {
            border-radius: 5%;
            z-index: 5;
            opacity: 1;
        }
    }
}
</style>