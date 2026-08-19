import { SidebarColor, ThemeMode, TagsViewStyle } from "@/enums/settings";
import {
  applyTheme,
  generateThemeColors,
  resolveThemeMode,
  toggleDarkMode,
  toggleSidebarColor,
  watchSystemTheme,
} from "@/utils/theme";
import { STORAGE_KEYS } from "@/constants";
import { defaults, themePalettePresets } from "@/settings";

const CUSTOM_THEME_PALETTE = "custom";
const HEX_COLOR_RE = /^#[0-9a-f]{6}$/i;

export const useSettingsStore = defineStore("setting", () => {
  // 界面显示
  const settingsVisible = ref(false);
  const showTagsView = useStorage(STORAGE_KEYS.SHOW_TAGS_VIEW, defaults.showTagsView);
  const tagsViewStyle = useStorage(STORAGE_KEYS.TAGS_VIEW_STYLE, defaults.tagsViewStyle);
  const showAppLogo = useStorage(STORAGE_KEYS.SHOW_APP_LOGO, defaults.showAppLogo);
  const showWatermark = useStorage(STORAGE_KEYS.SHOW_WATERMARK, defaults.showWatermark);
  const pageSwitchingAnimation = useStorage(
    STORAGE_KEYS.PAGE_SWITCHING_ANIMATION,
    defaults.pageSwitchingAnimation
  );

  // 布局
  const layout = useStorage(STORAGE_KEYS.LAYOUT, defaults.layout);
  const sidebarColorScheme = useStorage(
    STORAGE_KEYS.SIDEBAR_COLOR_SCHEME,
    defaults.sidebarColorScheme
  );

  // 主题
  const theme = useStorage(STORAGE_KEYS.THEME, defaults.theme);
  const themePalette = useStorage(STORAGE_KEYS.THEME_PALETTE, defaults.themePalette);
  const themeColors = useStorage(STORAGE_KEYS.THEME_COLORS, {
    ...defaults.themeColors,
  });

  // 兼容旧版单色存储 → 新版调色板
  const LEGACY_KEY = STORAGE_KEYS.THEME_COLOR;
  if (localStorage.getItem(LEGACY_KEY)) {
    const oldColor = localStorage.getItem(LEGACY_KEY);
    try {
      const parsed = JSON.parse(oldColor);
      if (typeof parsed === "string" && /^#[0-9a-f]{6}$/i.test(parsed)) {
        themeColors.value = { ...themeColors.value, primary: parsed };
      }
    } catch {
      /* 解析失败忽略 */
    }
    localStorage.removeItem(LEGACY_KEY);
  }

  const activeThemePalette = computed(
    () => themePalettePresets.find((item) => item.id === themePalette.value) || null
  );

  const resolvedTheme = ref(resolveThemeMode(theme.value));

  // 特殊模式
  const grayMode = useStorage(STORAGE_KEYS.GRAY_MODE, false);
  const colorWeak = useStorage(STORAGE_KEYS.COLOR_WEAK, false);

  // 主题变化监听
  let stopWatchingSystemTheme;

  watch(
    theme,
    (value) => {
      stopWatchingSystemTheme?.();
      resolvedTheme.value = resolveThemeMode(value);

      if (value === ThemeMode.AUTO) {
        stopWatchingSystemTheme = watchSystemTheme((systemTheme) => {
          resolvedTheme.value = systemTheme;
        });
      } else {
        stopWatchingSystemTheme = undefined;
      }
    },
    { immediate: true }
  );

  watch(
    [resolvedTheme, themeColors],
    ([t, colors]) => {
      toggleDarkMode(t === ThemeMode.DARK);
      applyTheme(generateThemeColors(colors, t));
    },
    { immediate: true, deep: true }
  );

  watch(sidebarColorScheme, (v) => toggleSidebarColor(v === SidebarColor.CLASSIC_BLUE), {
    immediate: true,
  });

  // 灰色模式监听
  watch(
    grayMode,
    (v) => {
      document.documentElement.style.filter = v ? "grayscale(100%)" : "";
    },
    { immediate: true }
  );

  // 色弱模式监听
  watch(
    colorWeak,
    (v) => {
      document.documentElement.classList.toggle("color-weak", v);
    },
    { immediate: true }
  );

  function applyThemePalette(id) {
    const preset = themePalettePresets.find((item) => item.id === id);
    if (!preset) return;

    themePalette.value = preset.id;
    themeColors.value = { ...preset.colors };
  }

  function updateThemeColor(name, color) {
    if (!HEX_COLOR_RE.test(color)) return;

    themePalette.value = CUSTOM_THEME_PALETTE;
    themeColors.value = {
      ...themeColors.value,
      [name]: color,
    };
  }

  function resetSettings() {
    showTagsView.value = defaults.showTagsView;
    tagsViewStyle.value = defaults.tagsViewStyle;
    showAppLogo.value = defaults.showAppLogo;
    showWatermark.value = defaults.showWatermark;
    pageSwitchingAnimation.value = defaults.pageSwitchingAnimation;
    grayMode.value = false;
    colorWeak.value = false;
    sidebarColorScheme.value = defaults.sidebarColorScheme;
    layout.value = defaults.layout;
    themePalette.value = defaults.themePalette;
    themeColors.value = { ...defaults.themeColors };
    theme.value = defaults.theme;
  }

  return {
    settingsVisible,
    showTagsView,
    tagsViewStyle,
    showAppLogo,
    showWatermark,
    pageSwitchingAnimation,
    grayMode,
    colorWeak,
    sidebarColorScheme,
    layout,
    themePalette,
    themeColors,
    theme,
    resolvedTheme,
    activeThemePalette,
    applyThemePalette,
    updateThemeColor,
    resetSettings,
  };
});
