<template>
    <slot></slot>
    <div style="display: flex; justify-content: flex-end">
        <n-space>
            <n-button type="primary" attr-type="button" @click="onSubmit">{{ submitButtonText }}</n-button>
            <n-button attr-type="button" @click="onReset">{{ cancelButtonText }}</n-button>
        </n-space>
    </div>
</template>

<script lang="ts">
import {defineComponent, onMounted} from "vue";
import {definedProps} from "@/components/commonForm/props";


export default defineComponent({
    name: "CommonForm",
    emits: ["onReset", "onSubmit"],
    props: {
        ...definedProps
    },
    setup(props, context) {

        const onSubmit = () => {
            context.emit("onSubmit")
        }
        const onReset = () => {
            context.emit("onReset")
        }

        onMounted(() => {

        })
        return {
            onSubmit,
            onReset
        }
    }
})
</script>

<style scoped>

</style>