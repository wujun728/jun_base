<template>
    <n-modal
        v-model:show="showModal"
        :preset="preset"
        :title="title"
        positiveText="提交"
        negativeText="取消"
        @positive-click="onSubmit"
        @negative-click="showModal = false">
        <div style="padding-top: 15px">
            <slot></slot>
        </div>
    </n-modal>
</template>

<script lang="ts">
import {computed, defineComponent} from "vue";
import {definedProps} from "@/components/commonModal/props";

export default defineComponent({
    name: "CommonModal",
    props: {
        ...definedProps
    },
    emits: ["onSubmit", "update:show"],
    setup(props, context) {

        const onSubmit = () => {
            context.emit("onSubmit")
        }

        const showModal = computed({
            get() {
                return props.show
            },
            set(value) {
                context.emit("update:show", value)
            }
        })
        return {
            onSubmit,
            showModal
        }
    }
})
</script>

<style scoped>

</style>