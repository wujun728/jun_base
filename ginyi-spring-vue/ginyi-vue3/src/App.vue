<template>
    <n-config-provider :theme="getTheme"
                       :theme-overrides="getThemeOverrides"
                       :locale="zhCN"
                       :date-locale="dateZhCN">
        <n-loading-bar-provider>
            <n-dialog-provider>
                <n-notification-provider>
                    <n-message-provider>
                        <router-view/>
                    </n-message-provider>
                </n-notification-provider>
            </n-dialog-provider>
        </n-loading-bar-provider>
    </n-config-provider>
</template>

<script setup lang="ts">
import {useSystemStore} from "@/store/modules/useSystemStore";
import {useThrottle} from "@/hooks/useDebthro";
import {storeToRefs} from "pinia";
import {computed} from "vue";
import {useLighten} from "@/hooks/useColor";
import { zhCN, dateZhCN } from "naive-ui";

const {getTheme, themeColor} = storeToRefs(useSystemStore())
const getThemeOverrides = computed(() => {
    return {
        common: {
            primaryColor: themeColor?.value,
            primaryColorHover: useLighten(themeColor?.value as string, 6),
            primaryColorPressed: useLighten(themeColor?.value as string, 6),
        },
        LoadingBar: {
            colorLoading: themeColor?.value as string,
        },
    };
});

// 节流，可以适当增加时间
window.addEventListener("resize", useThrottle(() => {
    useSystemStore().setClientWidth(document.body.clientWidth)
    useSystemStore().setClientHeight(document.body.clientHeight)
}, 0))


</script>
