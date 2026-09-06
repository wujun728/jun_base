<template>
    <n-scrollbar>
        <n-menu key-field="name"
                label-field="menuName"
                :collapsed-width="64"
                :accordion="true"
                :inverted="!darkTheme"
                :collapsed-icon-size="22"
                :options="menuOptions"
                :expand-icon="useRenderIcon(CaretDownOutline)"
                :expanded-keys="openKeys"
                v-model:value="activeMenuKey"
                @update:expanded-keys="handleUpdateExpandedKeys"
                @update:value="handleClickMenu"/>
    </n-scrollbar>
</template>

<script lang="ts">
import {defineComponent, ref, watch, watchEffect} from "vue";
import {useRenderIcon} from "@/plugins/naive-ui/common";
import {CaretDownOutline} from "@vicons/ionicons5";
import {useSystemStore} from "@/store/modules/useSystemStore";
import {storeToRefs} from "pinia";
import {useMenuFormat} from "@/hooks/useMenu";
import {MenuOption} from "naive-ui";
import {useCommonRouter} from "@/router";
import {useRoute} from "vue-router";
import {useFindNodeByName, useFindParentName} from "@/hooks/useTree";
import {useDeepClone} from "@/hooks/useObject";
import {ITabType} from "@/interface/modules/system";

export default defineComponent({
    name: "Menu",
    setup() {
        const currentRoute = useRoute();
        const {darkTheme, menuList, tabIndex} = storeToRefs(useSystemStore());
        const menuOptions = ref<Array<any>>([])
        const activeMenuKey = ref<string | undefined>(undefined)
        const openKeys = ref<Array<string>>([])

        const handleClickMenu = (key: string, item: MenuOption) => {
            useCommonRouter(item.name as string)

        }
        const handleUpdateExpandedKeys = (keys: string[]) => {
            openKeys.value = keys
        }

        /**
         * 监听路由的变化，实时更新多标签页的内容
         */
        watch(() => currentRoute.name, () => {
            const clickedMenu = useFindNodeByName(currentRoute.name as string, useDeepClone(menuList?.value as Array<any>))
            const bread: ITabType = {
                id: clickedMenu?.menuId,
                icon: clickedMenu?.icon,
                tabKey: clickedMenu?.name,
                tabName: clickedMenu?.menuName,
            }
            useSystemStore().addTab(bread)
        })
        watchEffect(() => {
            activeMenuKey.value = currentRoute.name as string
            menuOptions.value = useMenuFormat(useDeepClone(menuList?.value as Array<any>), true)
            openKeys.value = useFindParentName(currentRoute.name as string, menuList?.value as Array<any>) as Array<string>
        })

        return {
            darkTheme,
            menuOptions,
            useRenderIcon,
            handleClickMenu,
            activeMenuKey,
            handleUpdateExpandedKeys,
            openKeys,
            CaretDownOutline
        }
    }
})
</script>