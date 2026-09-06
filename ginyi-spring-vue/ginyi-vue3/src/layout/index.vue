<template>
    <n-layout has-sider class="layout">
        <n-layout-sider
            class="layout-side"
            bordered
            show-trigger="bar"
            collapse-mode="width"
            :collapsed-width="64"
            :collapsed="collapsed"
            :width="240"
            :inverted="true"
            :native-scrollbar="false"
            :on-update:collapsed="(val) => setCollapsed(val)">
            <Logo :style="{height: logoHeight + 'px'}"/>
            <Menu/>
        </n-layout-sider>
        <n-layout content-style="display: flex;flex-direction: column;">
            <n-layout-header :style="{height: (layoutHeaderHeight + tabsHeight) + 'px', padding: '0px 10px 10px 10px'}">
                <div>
                    <Headers :style="{height: layoutHeaderHeight - 10 + 'px'}"/>
                    <TabsView/>
                </div>
            </n-layout-header>
            <n-layout-content :content-style="{padding: '15px 15px 15px 20px', backgroundColor: darkTheme ? null : '#f5f6fa'}">
                <router-view v-slot="{ Component }">
                    <transition name="fade-main" mode="out-in">
                        <keep-alive>
                            <component :is="Component"/>
                        </keep-alive>
                    </transition>
                </router-view>
            </n-layout-content>
            <n-layout-footer
                :style="{height: layoutFooterHeight + 'px', display: 'flex', alignItems: 'center', justifyContent: 'center'}">
                {{ `Copyright © 2023-Now ${author}. All rights reserved.` }}
            </n-layout-footer>
        </n-layout>
        <Theme/>
    </n-layout>
</template>

<script lang="ts">
import {defineComponent} from 'vue'
import {CaretDownOutline, SparklesOutline} from '@vicons/ionicons5'
import Logo from "@/layout/logo/index.vue";
import {useSystemStore} from "@/store/modules/useSystemStore";
import Headers from "@/layout/header/index.vue";
import {storeToRefs} from "pinia";
import {setting} from "@/config/setting";
import TabsView from "@/layout/tabs/index.vue";
import Theme from "@/layout/theme/index.vue";
import Menu from "@/layout/menu/index.vue";
import {useRouterStore} from "@/store/modules/useRouterStore";

export default defineComponent({
    name: "Layout",
    components: {
        Logo,
        Headers,
        TabsView,
        Theme,
        Menu
    },
    setup() {
        const {
            clientHeight,
            layoutHeaderHeight,
            layoutFooterHeight,
            collapsed,
            logoHeight,
            darkTheme,
            tabsHeight
        } = storeToRefs(useSystemStore());
        const {routesKeepAliveList} = storeToRefs(useRouterStore())
        const {title, author} = setting;
        const {setCollapsed} = useSystemStore();


        return {
            title,
            author,
            clientHeight,
            layoutHeaderHeight,
            layoutFooterHeight,
            collapsed,
            logoHeight,
            setCollapsed,
            darkTheme,
            tabsHeight,
            Logo,
            Headers,
            CaretDownOutline,
            SparklesOutline,
            routesKeepAliveList
        }
    }
})
</script>

<style lang="less" scoped>
.layout {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    &-side {
        box-shadow: 2px 0 8px 0 rgb(29 35 41 / 5%);
        transition: all 0.3s ease-in-out;
    }
}
</style>
