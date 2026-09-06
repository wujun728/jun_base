<template>
    <n-breadcrumb>
        <transition-group name="bread">
            <n-breadcrumb-item v-for="(item, index) in breadMenuList" :key="index">
                <n-icon style="margin-right: 5px">
                    <Icon :icon="item.icon"/>
                </n-icon>
                <n-dropdown v-if="index !== breadMenuList.length - 1"
                            :options="useMenuFormat(useDeepClone(breadMenuList[index].children), true)"
                            placement="bottom-start"
                            key-field="menuId"
                            label-field="menuName"
                            @select="handleSelect"
                            trigger="click">
                    {{ item.menuName }}
                </n-dropdown>
                <span v-else>{{ item.menuName }}</span>
            </n-breadcrumb-item>
        </transition-group>
    </n-breadcrumb>
</template>

<script lang="ts">
import {defineComponent, watch} from "vue";
import {ApertureOutline, Bicycle, BulbOutline} from "@vicons/ionicons5";
import {useRoute} from "vue-router";
import {useFindParentNodes} from "@/hooks/useTree";
import {useDeepClone} from "@/hooks/useObject";
import {storeToRefs} from "pinia";
import {useSystemStore} from "@/store/modules/useSystemStore";
import {DropdownOption} from "naive-ui";
import {useCommonRouter} from "@/router";
import {useMenuFormat} from "@/hooks/useMenu";


export default defineComponent({
    name: "Breadcrumb ",
    setup() {
        const currentRoute = useRoute();
        const {menuList, breadMenuList} = storeToRefs(useSystemStore());

        const handleSelect = (key: string, option: DropdownOption) => {
            useCommonRouter(option.name as string)
        }

        /**
         * 监听路由变化，实时更新面包屑导航的内容
         */
        watch(() => currentRoute.name, () => {
            const list = useFindParentNodes("name", currentRoute.name as string, "children",
                useMenuFormat(useDeepClone(menuList?.value as Array<any>), false)
            ) as Array<any>
            useSystemStore().setBreadMenuList(list)
        })

        return {
            breadMenuList,
            handleSelect,
            useDeepClone,
            useMenuFormat,
            ApertureOutline, Bicycle, BulbOutline
        }
    }
})
</script>

<style lang="less" scoped>
.bread-enter-active,
.bread-leave-active {
    transition: all 0.5s ease;
}

.bread-enter-from,
.bread-leave-active {
    opacity: 0;
    transform: translateX(20px);
}

.bread-leave-active {
    position: absolute;
    z-index: -1;
}
</style>