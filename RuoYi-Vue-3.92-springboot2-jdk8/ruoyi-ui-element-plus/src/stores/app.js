import zhCn from "element-plus/es/locale/lang/zh-cn";
import en from "element-plus/es/locale/lang/en";
import { store } from "@/stores";
import { DeviceEnum, SidebarStatus } from "@/enums";
import { STORAGE_KEYS } from "@/constants";
import { defaults } from "@/settings";

export const useAppStore = defineStore("app", () => {
  /** 当前设备类型 */
  const device = useStorage(STORAGE_KEYS.DEVICE, DeviceEnum.DESKTOP);

  /** 组件默认尺寸 */
  const size = useStorage(STORAGE_KEYS.SIZE, defaults.size);

  /** 当前语言 */
  const language = useStorage(STORAGE_KEYS.LANGUAGE, defaults.language);

  /** 侧边栏持久化状态 */
  const sidebarStatus = useStorage(STORAGE_KEYS.SIDEBAR_STATUS, SidebarStatus.CLOSED);

  /** 侧边栏显示状态 */
  const sidebar = reactive({
    opened: sidebarStatus.value === SidebarStatus.OPENED,
    withoutAnimation: false,
  });

  /** 双列布局第二列（次级菜单）展开状态 */
  const secondarySidebar = reactive({
    opened: true,
  });

  /** 当前激活的顶部菜单路径 */
  const activeTopMenuPath = useStorage(STORAGE_KEYS.ACTIVE_TOP_MENU_PATH, "");

  /** 内容区是否全屏 */
  const contentFullscreen = ref(false);

  /** Element Plus 当前语言包 */
  const locale = computed(() => (language?.value === "en" ? en : zhCn));

  function toggleSidebar() {
    sidebar.opened = !sidebar.opened;
    sidebarStatus.value = sidebar.opened ? SidebarStatus.OPENED : SidebarStatus.CLOSED;
  }

  function closeSidebar() {
    sidebar.opened = false;
    sidebarStatus.value = SidebarStatus.CLOSED;
  }

  function openSidebar() {
    sidebar.opened = true;
    sidebarStatus.value = SidebarStatus.OPENED;
  }

  /** 切换第二列（次级菜单）展开状态 */
  function toggleSecondarySidebar() {
    secondarySidebar.opened = !secondarySidebar.opened;
  }

  function toggleDevice(val) {
    device.value = val;
  }

  function changeSize(val) {
    size.value = val;
  }

  function changeLanguage(val) {
    language.value = val;
  }

  function setActiveTopMenuPath(path) {
    activeTopMenuPath.value = path;
  }

  function toggleContentFullscreen() {
    contentFullscreen.value = !contentFullscreen.value;
  }

  return {
    device,
    sidebar,
    secondarySidebar,
    language,
    locale,
    size,
    contentFullscreen,
    toggleDevice,
    changeSize,
    changeLanguage,
    toggleSidebar,
    closeSidebar,
    openSidebar,
    toggleSecondarySidebar,
    setActiveTopMenuPath,
    toggleContentFullscreen,
    activeTopMenuPath,
  };
});

export function useAppStoreHook() {
  return useAppStore(store);
}
