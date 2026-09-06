export default () => {
  // 小屏幕
  if (document.documentElement.offsetWidth < 800) {
    window.isSmallScreen = true
  } else {
    window.isSmallScreen = false
  }
  return window.isSmallScreen
}
