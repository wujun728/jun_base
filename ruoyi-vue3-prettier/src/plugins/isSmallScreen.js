export default () => {
  const htmlEl = document.getElementsByTagName('html')[0]
  let htmlClass = htmlEl.getAttribute('class') ?? ''
  // 小屏幕
  if (document.documentElement.offsetWidth < 800) {
    window.isSmallScreen = true
    htmlClass += ' isSmallScreen'
  } else {
    window.isSmallScreen = false
    htmlClass = htmlClass.replace('isSmallScreen', '')
  }
  htmlEl.setAttribute('class', htmlClass.trim())
  return window.isSmallScreen
}
