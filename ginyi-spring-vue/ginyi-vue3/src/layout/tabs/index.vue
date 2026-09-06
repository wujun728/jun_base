<template>
    <div style="display: flex; align-items: center;">
        <n-button strong secondary circle size="tiny" @click="onBothSideIcons('left')" style="margin-right: 8px">
            <template #icon>
                <n-icon :component="ArrowBackOutline" />
            </template>
        </n-button>
        <div class="tabsView" id="tabsView">
            <div id="tabsTransition">
                <transition-group name="tab" tag="div" class="tabs-transition">
                    <div :id="'tabView_' + item.id" :class="item.id === tabIndex ? 'tabs-active' : 'tabs'"
                         v-for="(item, index) in tabsList"
                         :key="item.id"
                         :style="{color: getTheme ||  item.id === tabIndex ? activeFontColor :  null,
                                  backgroundColor: item.id === tabIndex ? useHexToRgba(activeBackgroundColor) : null}"
                         @click="onClickTag(item)"
                         @contextmenu="handleContextMenu($event, item, index)">
                        <div class="tabs-title">
                            <n-icon v-if="item.icon">
                                <Icon :icon="item.icon"/>
                            </n-icon>
                            <span>{{ item.tabName }}</span>
                            <n-icon style="border-radius: 50%" :component="CloseOutline" v-if="index !== 0"
                                    @click.stop="() => removeTab(item.id, index)"
                                    :id="`tabs-close-${item.id}`">
                            </n-icon>
                        </div>
                        <svg :id="'tabLeftSvg_' + item.id"
                             :class="item.id === tabIndex ? 'tabs-active-before' : 'tabs-before'" width="7" height="7"
                             :style="{fill: item.id === tabIndex ? useHexToRgba(activeBackgroundColor) : 'transparent'}">
                            <path d="M 0 7 A 7 7 0 0 0 7 0 L 7 7 Z"/>
                        </svg>
                        <svg :id="'tabRightSvg_' + item.id"
                             :class="item.id === tabIndex ? 'tabs-active-after' : 'tabs-after'" width="7" height="7"
                             :style="{fill: item.id === tabIndex ? useHexToRgba(activeBackgroundColor) : 'transparent'}">
                            <path d="M 0 7 A 7 7 0 0 0 7 0 L 7 7 Z"></path>
                        </svg>
                    </div>
                </transition-group>
            </div>
        </div>
        <n-button strong secondary circle size="tiny" @click="onBothSideIcons('right')" style="margin: 0 8px">
            <template #icon>
                <n-icon :component="ArrowForwardOutline" />
            </template>
        </n-button>
        <n-button strong secondary circle size="tiny" @click="location">
            <template #icon>
                <n-icon :component="LocateOutline" />
            </template>
        </n-button>
    </div>
    <n-dropdown
        placement="bottom-start"
        trigger="manual"
        :x="xRef"
        :y="yRef"
        :options="dropOptions"
        :show="showDropdownRef"
        :on-clickoutside="() => showDropdownRef = false"
        @select="handleSelect"
    />
</template>

<script lang="ts">
import {defineComponent, nextTick, onMounted, ref, watchEffect} from "vue";
import {
    ChevronBack,
    ChevronForward,
    CloseCircleOutline,
    CloseOutline,
    CodeWorking,
    GameController,
    LocateOutline,
    PlaySkipBack,
    PlaySkipForward,
    ArrowBackOutline,
    ArrowForwardOutline,
} from '@vicons/ionicons5'
import {storeToRefs} from "pinia";
import {useSystemStore} from "@/store/modules/useSystemStore";
import {useHexToRgba} from "@/hooks/useColor";
import {useRenderIcon} from "@/plugins/naive-ui/common";
import {useCommonRouter} from "@/router";

export default defineComponent({
    name: "TabsView",
    components: {
        CloseOutline, GameController, CodeWorking,
        ChevronBack, ChevronForward, LocateOutline,
        PlaySkipForward, PlaySkipBack, CloseCircleOutline,
    },
    setup() {
        const {getTheme, themeColor, clientWidth, tabIndex, tabsList} = storeToRefs(useSystemStore());
        // 选中的tag颜色
        const activeBackgroundColor = ref<string | undefined | null>(useHexToRgba(themeColor?.value as string));
        // 选中的文字颜色
        const activeFontColor = themeColor

        // 右击下拉菜单
        const dropOptions = ref<Array<{ label: string, key: string, icon?: Function }>>([
            {
                label: '关闭其他',
                key: 'other',
                icon: useRenderIcon(CodeWorking)
            },
            {
                label: '关闭全部',
                key: 'all',
                icon: useRenderIcon(CloseCircleOutline)
            },
            {
                label: '关闭左侧',
                key: 'left',
                icon: useRenderIcon(PlaySkipBack)
            },
            {
                label: '关闭右侧',
                key: 'right',
                icon: useRenderIcon(PlaySkipForward)
            }
        ])
        const whichIndex = ref<number | undefined>(undefined)
        const showDropdownRef = ref<boolean>(false)
        const xRef = ref<number>(0)
        const yRef = ref<number>(0)


        watchEffect(() => {
            activeBackgroundColor.value = themeColor?.value
        })

        // 初始化选中的颜色
        const initActiveBackgroundColor = () => {
            const tabsActive = document.getElementsByClassName("tabs-active")[0];
            const tabsBeforeActive = document.getElementsByClassName("tabs-active-before")[0];
            const tabsAfterActive = document.getElementsByClassName("tabs-active-after")[0];

            if (tabsActive instanceof HTMLElement) {
                tabsActive.style.backgroundColor = useHexToRgba(activeBackgroundColor.value as string);
            }
            if (tabsBeforeActive instanceof SVGElement) {
                tabsBeforeActive.style.fill = useHexToRgba(activeBackgroundColor.value as string);
            }
            if (tabsAfterActive instanceof SVGElement) {
                tabsAfterActive.style.fill = useHexToRgba(activeBackgroundColor.value as string);
            }
        }
        // 初始化鼠标事件
        const initMouseEvent = (index?: number) => {
            tabsList?.value?.forEach((item, key) => {
                const tagsView = document.getElementById(`tabView_${item.id}`);
                if (tagsView) {
                    const childNodes = tagsView.childNodes;
                    // 给 tagsView 添加鼠标【到达】事件监听
                    tagsView.onmouseover = () => {
                        tagsView.style.backgroundColor = useHexToRgba(activeBackgroundColor.value as string);
                        tagsView.style.color = activeFontColor?.value as string
                        childNodes.forEach(child => {
                            if (child.nodeName.toUpperCase() === "SVG" && child instanceof SVGElement) {
                                child.style.fill = useHexToRgba(activeBackgroundColor.value as string);
                            }
                        })
                    }
                    // 给 tagsView 添加鼠标【离开】事件监听
                    tagsView.onmouseout = () => {
                        if (tagsView.id.split("_")[1] !== tabIndex?.value?.toString()) {
                            tagsView.style.backgroundColor = "";
                            tagsView.style.color = getTheme.value !== undefined ? activeFontColor?.value as string : "";
                        }
                        childNodes.forEach(child => {
                            if (child.nodeName === "svg" && child instanceof SVGElement) {
                                if (tagsView.id.split("_")[1] !== tabIndex?.value?.toString()) {
                                    child.style.fill = "transparent";
                                }
                            }
                        })
                    }

                    // 关闭icon的鼠标监听事件
                    const closeIcon = document.getElementById(`tabs-close-${item.id}`);
                    if (closeIcon) {
                        closeIcon.onmouseover = () => {
                            closeIcon.style.backgroundColor = useHexToRgba(activeBackgroundColor.value as string, 0.3);
                            closeIcon.style.opacity = "0.5"
                        }
                        closeIcon.onmouseout = () => {
                            closeIcon.style.backgroundColor = ""
                            closeIcon.style.opacity = "1"
                        }
                    }

                }
            })

            const leftIcon = document.getElementsByClassName("both-icon")[0];
            const rightIcon = document.getElementsByClassName("both-icon")[1];
            const locationIcon = document.getElementsByClassName("both-icon")[2];
            [leftIcon, rightIcon, locationIcon].forEach(icon => {
                if (icon instanceof HTMLElement) {
                    icon.onmouseover = () => {
                        icon.style.color = activeBackgroundColor.value as string;
                    }
                    icon.onmouseout = () => {
                        icon.style.color = ""
                    }
                }
            })
        }

        const onClickTab = (item: any) => {
            useSystemStore().setTagIndex(item.id);
            initMouseEvent(tabIndex?.value);
            useCommonRouter(item.tabKey)
        }

        const removeTab = (tabId: number, index: number) => {
            useSystemStore().removeTab(tabId, index)
        }
        // tabsView两边的icon
        const onBothSideIcons = (type: string) => {
            const tabsView = document.getElementById("tabsView");
            if (tabsView) {
                const offset = tabsView.scrollLeft
                if (type === "right") {
                    tabsView.scrollTo({left: offset + 500, behavior: 'smooth'});
                }
                if (type === "left") {
                    tabsView.scrollTo({left: offset - 500, behavior: 'smooth'});
                }
            }
        }
        // 定位当前
        const location = () => {
            const tabsList = document.getElementsByClassName("tabs-transition")[0];
            if (tabsList && tabsList instanceof HTMLElement) {
                (tabsList.childNodes || [])?.forEach(tab => {
                    if (tab instanceof HTMLElement && (tab.id === `tabView_${tabIndex?.value}`)) {
                        tab.scrollIntoView && tab.scrollIntoView({
                            inline: "center",
                            behavior: "smooth",
                        });
                    }
                })
            }
        }

        // 右击
        const handleContextMenu = (e: MouseEvent, item: any, index: number) => {
            e.preventDefault()
            showDropdownRef.value = false
            nextTick().then(() => {
                showDropdownRef.value = true
                xRef.value = e.clientX
                yRef.value = e.clientY
                whichIndex.value = index
            })
        }

        const handleSelect = (key: string) => {
            showDropdownRef.value = false
            switch (key) {
                case "right":
                    useSystemStore().removeRightTabs(whichIndex.value as number);
                    break;
                case "left":
                    useSystemStore().removeLeftTabs(whichIndex.value as number);
                    break;
                case "other":
                    useSystemStore().removeOtherTabs(whichIndex.value as number);
                    break;
                case "all":
                    useSystemStore().removeAllTabs();
                    break;
            }
        }

        onMounted(() => {
            initActiveBackgroundColor();
            initMouseEvent(tabIndex?.value);
        })

        return {
            getTheme,
            activeBackgroundColor,
            activeFontColor,
            CloseOutline, CloseCircleOutline,
            ChevronBack, ChevronForward, LocateOutline,
            PlaySkipForward, PlaySkipBack, CodeWorking, ArrowBackOutline, ArrowForwardOutline,
            onClickTag: onClickTab,
            useHexToRgba,
            removeTab,
            tabIndex,
            tabsList,
            onBothSideIcons,
            location,
            dropOptions,
            showDropdownRef,
            xRef,
            yRef,
            handleContextMenu,
            handleSelect
        }
    }
})
</script>

<style lang="less">
.tabsView {
    width: 100%;
    display: flex;
    align-items: center;
    padding: 0 5px 0 5px;
    overflow: hidden;

    .tabs-transition {
        display: flex;

        .tabs {
            height: 30px;
            position: relative;
            border-radius: 8px 8px 0 0;
            display: flex;
            align-items: center;
            justify-content: space-between;

            &-title {
                width: 100%;
                height: 15px;
                display: flex;
                align-items: center;
                padding: 0 6px 0 10px;

                span {
                    margin: 0 10px 0 5px;
                    user-select: none;
                    white-space: nowrap;
                }
            }

            &-before {
                position: absolute;
                left: -7px;
                bottom: 0;
            }

            &-after {
                position: absolute;
                right: -7px;
                bottom: 0;
                transform: rotate(90deg);
            }
        }

        .tabs-active {
            height: 30px;
            position: relative;
            border-radius: 8px 8px 0 0;
            display: flex;
            align-items: center;
            justify-content: space-between;

            &-before {
                position: absolute;
                left: -7px;
                bottom: 0;
            }

            &-after {
                position: absolute;
                right: -7px;
                bottom: 0;
                transform: rotate(90deg);
            }
        }

        .tabs-active:hover {
            cursor: pointer;
        }

        .tabs:hover {
            cursor: pointer;
        }

        /* 多标签组件过渡动画 */
        .tab-move,
        .tab-enter-active,
        .tab-leave-active {
            transition: all 0.3s ease;
        }

        .tab-enter-from,
        .tab-leave-to {
            opacity: 0;
            transform: translateX(-30px);
        }

        .tab-leave-active {
            position: absolute;
        }
    }
}


</style>
