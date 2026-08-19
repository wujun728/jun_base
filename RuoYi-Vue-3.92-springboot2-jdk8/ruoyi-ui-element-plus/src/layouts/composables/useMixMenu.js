import { useLayout } from "./useLayout";
import { useAppStore, usePermissionStore } from "@/stores";
import { isExternal } from "@/utils/index";

export function useMixMenu() {
  const route = useRoute();
  const router = useRouter();

  const appStore = useAppStore();
  const permissionStore = usePermissionStore();

  const { activeTopMenuPath, sideMenuRoutes } = useLayout();

  const topMenuItems = computed(() => {
    const routes = permissionStore.routes.filter((item) => !item.meta?.hidden);

    return routes.map((route) => {
      if (route.meta?.alwaysShow || !route.children?.length) return route;

      const visibleChildren = route.children.filter((child) => !child.meta?.hidden);

      if (visibleChildren.length === 1) {
        const child = visibleChildren[0];
        return {
          ...route,
          meta: {
            ...route.meta,
            title: child.meta?.title || route.meta?.title,
            icon: child.meta?.icon || route.meta?.icon,
          },
        };
      }
      return route;
    });
  });

  const activeSideMenuPath = computed(() => {
    const { meta, path } = route;
    return typeof meta?.activeMenu === "string" ? meta.activeMenu : path;
  });

  function resolvePath(routePath) {
    if (isExternal(routePath)) return routePath;
    if (routePath.startsWith("/")) return activeTopMenuPath.value + routePath;
    return `${activeTopMenuPath.value}/${routePath}`;
  }

  function extractTopMenuPath(path) {
    return path.split("/").filter(Boolean).length > 1 ? path.match(/^\/[^/]+/)?.[0] || "/" : "/";
  }

  function handleTopMenuSelect(menuPath) {
    if (menuPath === activeTopMenuPath.value) return;

    appStore.setActiveTopMenuPath(menuPath);
    permissionStore.setMixLayoutSideMenus(menuPath);
    navigateToFirstMenu(permissionStore.mixLayoutSideMenus);
  }

  function navigateToFirstMenu(menus) {
    if (!menus.length) return;

    const [first] = menus;
    if (first.children?.length) {
      navigateToFirstMenu(first.children);
    } else if (first.name) {
      router.push({
        name: first.name,
        query: typeof first.meta?.params === "object" ? first.meta.params : undefined,
      });
    }
  }

  watch(
    () => route.path,
    (newPath) => {
      const topMenuPath = extractTopMenuPath(newPath);
      const isTopMenuChanged = topMenuPath !== activeTopMenuPath.value;

      if (isTopMenuChanged) {
        appStore.setActiveTopMenuPath(topMenuPath);
      }

      if (isTopMenuChanged || permissionStore.mixLayoutSideMenus.length === 0) {
        permissionStore.setMixLayoutSideMenus(topMenuPath);
      }
    },
    { immediate: true }
  );

  return {
    topMenuItems,
    activeSideMenuPath,
    activeTopMenuPath,
    sideMenuRoutes,
    resolvePath,
    handleTopMenuSelect,
  };
}
