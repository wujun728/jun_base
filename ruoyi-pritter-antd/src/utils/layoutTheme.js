const CSS_VAR_MAP = {
  menuBackground: '--layout-menu-bg',
  menuColor: '--layout-menu-color',
  menuHoverBackground: '--layout-menu-hover-bg',
  menuActiveBackground: '--layout-menu-active-bg',
  menuActiveColor: '--layout-menu-active-color',
}

export function syncLayoutCssVars(configStore) {
  if (typeof document === 'undefined') {
    return
  }

  const root = document.documentElement
  const { layout, getColorVal } = configStore

  Object.entries(CSS_VAR_MAP).forEach(([key, cssVar]) => {
    root.style.setProperty(cssVar, getColorVal(key))
  })

  root.style.setProperty(
    '--layout-menu-border',
    layout.isDark ? 'rgba(255, 255, 255, 0.08)' : 'rgba(0, 0, 0, 0.06)'
  )
}
