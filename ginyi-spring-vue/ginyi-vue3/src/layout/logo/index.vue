<template>
    <div class="logo-box" @click="goHome">
        <n-avatar v-if="collapsed" round size="medium" :src="logo" style="margin-right: 10px"/>
        <div v-else style="display: flex; align-items: center; margin-left: 15px;">
            <n-avatar round size="medium" :src="logo" style="margin-right: 10px"/>
            <h3 style="white-space: nowrap">{{ `${title} ${name}` }}</h3>
        </div>
    </div>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import {useSystemStore} from "@/store/modules/useSystemStore";
import {storeToRefs} from "pinia";
import {setting} from "@/config/setting";
import {useCommonRouter} from "@/router";

export default defineComponent({
    name: "Logo",
    setup() {
        const {collapsed} = storeToRefs(useSystemStore());
        const {logo, title, name} = setting;

        const goHome = () => {
            useCommonRouter("home")
        }
        return {
            goHome,
            logo, title, name, collapsed
        }
    }
})
</script>

<style lang="less" scoped>
.logo-box {
    display: flex;
    align-items: center;
    margin-left: 15px;
}
.logo-box:hover {
    cursor: pointer;
}
</style>