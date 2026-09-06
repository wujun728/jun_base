<template>
    <n-popover placement="top-end" trigger="click" :arrow-point-to-center="true">
        <template #trigger>
            <div style="position: fixed; bottom: 55px; right:15px; cursor: pointer">
                <n-card style="border-radius: 50%; width: 50px; height: 50px; position: relative">
                    <n-icon style="position: absolute; left: 50%; top: 50%; transform: translate(-50%, -50%)" :size="30"
                            :component="SparklesOutline"/>
                </n-card>
            </div>
        </template>
        <div style="width: 350px; height: 600px;">
            <span style="font-size: 20px">主题配置</span>
            <n-divider title-placement="center">系统主题</n-divider>
            <div style="display: flex; justify-content: center" class="dark-switch">
                <n-switch v-model:value="darkTheme">
                    <template #checked>
                        <n-icon size="14" color="#ffd93b" background="#212126">
                            <SunnySharp/>
                        </n-icon>
                    </template>
                    <template #unchecked>
                        <n-icon size="14" color="#ffd93b" background="#212126">
                            <Moon/>
                        </n-icon>
                    </template>
                </n-switch>
            </div>
            <n-divider title-placement="center">系统主题色</n-divider>
            <div class="theme-box">
                <div class="theme-box-item"
                     v-for="(color, index) in themeColorList" :key="index" :style="{ 'background-color': color }"
                     @click="chooseColor(color)">
                    <n-icon size="24" v-if="color === themeColor" color="white">
                        <Checkmark/>
                    </n-icon>
                </div>
                <n-color-picker v-model:value="themeColor" :show-alpha="false"/>
            </div>
        </div>
    </n-popover>
</template>

<script lang="ts">
import {Checkmark, Moon, SparklesOutline, SunnySharp} from '@vicons/ionicons5'
import {defineComponent} from "vue";
import {storeToRefs} from "pinia";
import {useSystemStore} from "@/store/modules/useSystemStore";

export default defineComponent({
    name: "Theme",
    components: {
        Moon,
        SunnySharp,
        SparklesOutline,
        Checkmark
    },
    setup() {
        const {darkTheme, themeColor, themeColorList} = storeToRefs(useSystemStore())
        const chooseColor = (color: string) => {
            useSystemStore().setThemeColor(color)
        }
        return {
            darkTheme,
            themeColor,
            themeColorList,
            chooseColor,
            Moon, SunnySharp, SparklesOutline, Checkmark
        }
    }
})
</script>

<style scoped lang="less">
.dark-switch .n-switch {
    ::v-deep(.n-switch__rail) {
        background-color: #000e1c;
    }
}

.theme-box {
    width: 100%;
    display: flex;
    justify-content: center;
    flex-wrap: wrap;

    &-item {
        width: 27px;
        height: 27px;
        margin: 4px;
        display: flex;
        cursor: pointer;
    }
}
</style>