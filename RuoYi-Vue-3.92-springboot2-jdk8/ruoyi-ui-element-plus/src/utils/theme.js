import { ThemeMode } from "@/enums";
import { themeColorNames } from "@/settings";

const SYSTEM_DARK_MEDIA = "(prefers-color-scheme: dark)";

function hexToRgb(hex) {
  const bigint = parseInt(hex.slice(1), 16);
  return [(bigint >> 16) & 255, (bigint >> 8) & 255, bigint & 255];
}

function rgbToHex(r, g, b) {
  return `#${((1 << 24) + (r << 16) + (g << 8) + b).toString(16).slice(1)}`;
}

export function getDarkColor(color, level) {
  const rgb = hexToRgb(color);
  for (let i = 0; i < 3; i++) rgb[i] = Math.round(20.5 * level + rgb[i] * (1 - level));
  return rgbToHex(rgb[0], rgb[1], rgb[2]);
}

export const getLightColor = (color, level) => {
  const rgb = hexToRgb(color);
  for (let i = 0; i < 3; i++) rgb[i] = Math.round(255 * level + rgb[i] * (1 - level));
  return rgbToHex(rgb[0], rgb[1], rgb[2]);
};

/**
 * Element Plus 运行时需要 base、light-1..9 和 dark-2。
 * 这里从完整颜色方案一次性生成，避免主色和功能色来自不同体系。
 */
export function generateThemeColors(palette, theme) {
  const resolvedTheme = resolveThemeMode(theme);
  const colors = {};

  themeColorNames.forEach((name) => {
    const base = palette[name];
    colors[name] = base;

    for (let i = 1; i <= 9; i++) {
      colors[`${name}-light-${i}`] =
        resolvedTheme === ThemeMode.LIGHT
          ? `${getLightColor(base, i / 10)}`
          : `${getDarkColor(base, i / 10)}`;
    }
    colors[`${name}-dark-2`] =
      resolvedTheme === ThemeMode.LIGHT
        ? `${getLightColor(base, 0.2)}`
        : `${getDarkColor(base, 0.3)}`;
  });

  return colors;
}

export function getSystemTheme() {
  return window.matchMedia(SYSTEM_DARK_MEDIA).matches ? ThemeMode.DARK : ThemeMode.LIGHT;
}

export function resolveThemeMode(theme) {
  return theme === ThemeMode.AUTO ? getSystemTheme() : theme;
}

export function watchSystemTheme(callback) {
  const mediaQuery = window.matchMedia(SYSTEM_DARK_MEDIA);
  const handler = () => callback(mediaQuery.matches ? ThemeMode.DARK : ThemeMode.LIGHT);

  mediaQuery.addEventListener("change", handler);

  return () => {
    mediaQuery.removeEventListener("change", handler);
  };
}

export function applyTheme(colors) {
  const el = document.documentElement;

  Object.entries(colors).forEach(([key, value]) => {
    el.style.setProperty(`--el-color-${key}`, value);
  });

  requestAnimationFrame(() => {
    el.style.setProperty("--theme-update-trigger", Date.now().toString());
  });
}

export function toggleDarkMode(isDark) {
  if (isDark) {
    document.documentElement.classList.add(ThemeMode.DARK);
  } else {
    document.documentElement.classList.remove(ThemeMode.DARK);
  }
}

export function toggleSidebarColor(isBlueSidebar) {
  if (isBlueSidebar) {
    document.documentElement.classList.add("sidebar-color-blue");
  } else {
    document.documentElement.classList.remove("sidebar-color-blue");
  }
}
