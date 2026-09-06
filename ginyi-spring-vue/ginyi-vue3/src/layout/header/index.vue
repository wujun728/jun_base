<template>
    <div style="display: flex; align-items: center;height: 100%; justify-content: space-between">
        <Breadcrumb/>
        <Personnel />
    </div>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import Breadcrumb from "@/layout/header/breadcrumb.vue";
import Personnel from "@/layout/header/personnel.vue";

export default defineComponent({
    name: "Headers",
    components: {
        Breadcrumb,
        Personnel
    },
    setup() {

        return {
            Breadcrumb, Personnel
        }
    }
})
</script>