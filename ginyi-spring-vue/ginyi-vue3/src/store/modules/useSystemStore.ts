import {defineStore} from "pinia";
import {storeKeyEnums} from "@/enums/storeKeyEnums";
import {darkTheme} from "naive-ui";
import {BuiltInGlobalTheme} from "naive-ui/es/themes/interface";
import {ISystemState, ITabType} from "@/interface/modules/system";
import {useCommonRouter} from "@/router";

export const useSystemStore = defineStore(storeKeyEnums.SYSTEM, {
    state: (): ISystemState => ({
        darkTheme: false,
        themeColor: themeColorList[0],
        themeColorList: themeColorList,
        clientHeight: document.body.clientHeight,
        clientWidth: document.body.clientWidth,
        layoutHeaderHeight: 58,
        layoutFooterHeight: 40,
        collapsed: false,
        logoHeight: 60,
        tabsHeight: 20,
        tabIndex: tabsList[0].id,
        tabsList: tabsList,
        menuList: [],
        breadMenuList: []
    }),
    getters: {
        getTheme(): BuiltInGlobalTheme | undefined {
            return this.darkTheme ? darkTheme : undefined
        },
        getCollapsed(): boolean | undefined {
            return this.collapsed
        },
        getMenuList(): any {
            return this.menuList
        }
    },
    actions: {
        setThemeColor(data: string | undefined) {
            this.themeColor = data
        },
        setClientHeight(data: number | undefined) {
            this.clientHeight = data
        },
        setClientWidth(data: number | undefined) {
            this.clientWidth = data
        },
        setCollapsed(data: boolean | undefined) {
            this.collapsed = data
        },
        setTagIndex(data: number | undefined) {
            this.tabIndex = data
        },
        addTab(data: ITabType) {
            const exist = this.tabsList?.some(tab => {
                return tab.id === data.id
            })
            if (!exist) {
                this.tabsList?.push(data)
            }
            this.tabIndex = data.id

            setTimeout(() => {
                const currentTab = document.getElementById(`tabView_${this.tabIndex}`)
                if (currentTab instanceof HTMLElement) {
                    currentTab.scrollIntoView && currentTab.scrollIntoView({
                        inline: "center",
                        behavior: "smooth",
                    });
                }
            }, 50)
        },
        removeTab(tabId: number, index: number) {
            this.tabsList?.splice(index, 1)
            if (this.tabsList && tabId === this.tabIndex) {
                this.tabIndex = this.tabsList[0].id
                useCommonRouter(this.tabsList[0].tabKey)
            }
        },
        removeLeftTabs(index: number) {
            this.tabsList?.splice(1, index - 1)
            const closeSelf = this.tabsList?.some(tab => {
                return tab.id === this.tabIndex
            })
            if (!closeSelf && this.tabsList) {
                this.tabIndex = this.tabsList[0].id
                useCommonRouter(this.tabsList[0].tabKey)
            }
        },
        removeRightTabs(index: number) {
            this.tabsList?.splice(index + 1, this.tabsList?.length - 1)
            const closeSelf = this.tabsList?.some(tab => {
                return tab.id === this.tabIndex
            })
            if (!closeSelf && this.tabsList) {
                this.tabIndex = this.tabsList[0].id
                useCommonRouter(this.tabsList[0].tabKey)
            }
        },
        removeOtherTabs(index: number) {
            this.tabsList = this.tabsList?.filter((tab, key) => {
                return key === 0 || key === index
            })
            const closeSelf = this.tabsList?.some(tab => {
                return tab.id === this.tabIndex
            })
            if (!closeSelf && this.tabsList) {
                this.tabIndex = this.tabsList[0].id
                useCommonRouter(this.tabsList[0].tabKey)
            }
        },
        removeAllTabs() {
            this.tabsList = this.tabsList?.filter((tab, key) => {
                return key === 0
            })
            if (this.tabsList) {
                this.tabIndex = this.tabsList[0].id
                useCommonRouter(this.tabsList[0].tabKey)
            }
        },
        setMenuList(data: any) {
            this.menuList = data
            const hasHome = this.menuList?.some(menu => {
                return menu.name === "home"
            })
            const has404 = this.menuList?.some(menu => {
                return menu.name === "404"
            })
            if (!hasHome) {
                this.menuList?.unshift({
                    menuId: 0,
                    path: "home",
                    name: "home",
                    icon: "Home",
                    menuName: "首页",
                    component: "home/index",
                    menuType: "C",
                })
            }
            if (!has404) {
                this.menuList?.push({
                    menuId: 199999,
                    path: "404",
                    name: "404",
                    icon: "Bug",
                    show: false,
                    menuName: "404",
                    component: "404/index",
                    menuType: "C",
                })
            }
        },
        setBreadMenuList(data: Array<any>) {
            this.breadMenuList = []
            // 延时主要是为了配合动画效果
            setTimeout(() => {
                this.breadMenuList = data
            }, 0)
        },
        resetBreadMenuList() {
            this.breadMenuList = [{
                menuId: 0,
                path: "home",
                name: "home",
                icon: "Home",
                menuName: "首页",
                component: "home/index",
                menuType: "C",
            }]
        }
    }
})

const themeColorList: Array<string> = [
    "#9A53FE",
    "#e88080",
    "#0084f4",
    "#009688",
    "#536dfe",
    "#ff5c93",
    "#ee4f12",
    "#0096c7",
    "#9c27b0",
    "#ff9800",
    "#FF3D68",
    "#00C1D4",
    "#1DB85B",
    "#2AB193",
    "#5CC143",
    "#63e2b7",
    "#E61E8F",
    "#E03D3D",
    "#E63AAF",
    "#35BA7B",
]

const tabsList: Array<ITabType> = [
    {
        id: 0,
        tabKey: "home",
        tabName: "首页",
        icon: "Home"
    }
]