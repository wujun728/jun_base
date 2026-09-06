interface TianaiCaptchaResponse {
  data?: {
    token?: string
  }
}

interface TianaiCaptchaInstance {
  destroyWindow: () => void
  reloadCaptcha: () => void
}

declare global {
  interface Window {
    TAC?: new (
      config: Record<string, unknown>,
      style?: Record<string, unknown>
    ) => { init: () => TianaiCaptchaInstance | void }
  }
}

export async function verifyByTianaiCaptcha(bindEl: string) {
  await loadTianaiCaptchaAssets()
  const TAC = window.TAC
  if (!TAC) throw new Error('天爱验证码 SDK 加载失败')
  const host = createCaptchaHost(bindEl)

  return new Promise<string>((resolve, reject) => {
    let settled = false
    let captchaInstance: TianaiCaptchaInstance | undefined
    const complete = (token: string, tac: TianaiCaptchaInstance) => {
      if (settled) return
      settled = true
      tac.destroyWindow()
      host.cleanup()
      resolve(token)
    }
    const fail = (error: unknown, tac?: TianaiCaptchaInstance) => {
      if (settled) return
      settled = true
      tac?.destroyWindow()
      host.cleanup()
      reject(error)
    }
    const captchaConfig = {
      requestCaptchaDataUrl: buildApiUrl('/captcha/get'),
      validCaptchaUrl: buildApiUrl('/captcha/check'),
      bindEl: host.selector,
      validSuccess: (res: TianaiCaptchaResponse, _captcha: unknown, tac: TianaiCaptchaInstance) => {
        const token = res?.data?.token
        token ? complete(token, tac) : fail(new Error('验证码校验未返回 token'), tac)
      },
      validFail: (_res: unknown, _captcha: unknown, tac: TianaiCaptchaInstance) => {
        tac.reloadCaptcha()
      },
      btnRefreshFun: (_el: HTMLElement, tac: TianaiCaptchaInstance) => {
        tac.reloadCaptcha()
      },
      btnCloseFun: (_el: HTMLElement, tac: TianaiCaptchaInstance) => {
        fail(new Error('验证码已关闭'), tac)
      }
    }
    try {
      captchaInstance =
        new TAC(captchaConfig, { logoUrl: `${import.meta.env.BASE_URL}tac/images/icon.png` }).init() ||
        undefined
      host.bindControls({
        refresh: () => captchaInstance?.reloadCaptcha(),
        close: () => fail(new Error('验证码已关闭'), captchaInstance)
      })
    } catch (error) {
      fail(error)
    }
  })
}

function createCaptchaHost(bindEl: string) {
  const id = bindEl.replace(/^#/, '') || 'tianai-captcha-box'
  const hostId = `${id}-panel`
  document.querySelector(`#${hostId}-overlay`)?.remove()
  ensureOverlayStyle()
  const anchor = document.querySelector<HTMLElement>(bindEl)

  const overlay = document.createElement('div')
  overlay.id = `${hostId}-overlay`
  overlay.className = 'tianai-captcha-overlay'

  const panel = document.createElement('div')
  panel.className = 'tianai-captcha-panel'

  const mount = document.createElement('div')
  mount.id = hostId
  mount.className = 'tianai-captcha-mount'

  panel.appendChild(mount)
  overlay.appendChild(panel)
  if (anchor) {
    anchor.insertAdjacentElement('afterend', overlay)
    overlay.scrollIntoView({ block: 'nearest', behavior: 'smooth' })
  } else {
    document.body.appendChild(overlay)
  }

  return {
    selector: `#${hostId}`,
    bindControls: (handlers: { refresh: () => void; close: () => void }) => {
      const refreshBtn = overlay.querySelector<HTMLElement>('#tianai-captcha-slider-refresh-btn')
      const closeBtn = overlay.querySelector<HTMLElement>('#tianai-captcha-slider-close-btn')
      refreshBtn?.addEventListener('click', (event) => {
        event.preventDefault()
        event.stopPropagation()
        handlers.refresh()
      })
      closeBtn?.addEventListener('click', (event) => {
        event.preventDefault()
        event.stopPropagation()
        handlers.close()
      })
    },
    cleanup: () => overlay.remove()
  }
}

function ensureOverlayStyle() {
  if (document.querySelector('#tianai-captcha-overlay-style')) return
  const style = document.createElement('style')
  style.id = 'tianai-captcha-overlay-style'
  style.textContent = `
    .tianai-captcha-overlay {
      position: relative;
      z-index: 10;
      width: 100%;
      margin-top: 12px;
    }
    .tianai-captcha-panel {
      width: 100%;
      padding: 12px;
      border: 1px solid rgba(148, 163, 184, 0.28);
      border-radius: 10px;
      background: var(--default-box-color, #fff);
      box-shadow: 0 14px 38px rgba(15, 23, 42, 0.12);
    }
    .tianai-captcha-mount {
      display: flex;
      justify-content: center;
    }
    .tianai-captcha-mount #tianai-captcha-parent {
      position: relative;
      height: 276px !important;
      box-shadow: none;
      border: 1px solid rgba(148, 163, 184, 0.24);
      border-radius: 10px;
    }
    .tianai-captcha-mount #tianai-captcha-parent #tianai-captcha-box {
      height: 260px;
    }
    .tianai-captcha-mount #tianai-captcha-parent #tianai-captcha .slider-tip {
      padding-right: 78px;
    }
    .tianai-captcha-mount #tianai-captcha-parent > .slider-bottom {
      position: absolute;
      top: 6px;
      right: 10px;
      z-index: 1005;
      display: flex;
      align-items: center;
      flex-direction: row-reverse;
      gap: 12px;
      width: auto;
      height: 24px;
      padding: 2px 4px;
      border-radius: 999px;
      background: rgba(255, 255, 255, 0.86);
      pointer-events: none;
    }
    .tianai-captcha-mount #tianai-captcha-parent > .slider-bottom .logo {
      display: none !important;
    }
    .tianai-captcha-mount #tianai-captcha-parent > .slider-bottom .refresh-btn,
    .tianai-captcha-mount #tianai-captcha-parent > .slider-bottom .close-btn {
      position: static;
      float: none;
      width: 20px;
      height: 20px;
      background-size: auto;
      margin: 0;
      opacity: 0.68;
      pointer-events: auto;
      transition: opacity 0.2s ease;
    }
    .tianai-captcha-mount #tianai-captcha-parent > .slider-bottom .refresh-btn:hover,
    .tianai-captcha-mount #tianai-captcha-parent > .slider-bottom .close-btn:hover {
      opacity: 1;
    }
  `
  document.head.appendChild(style)
}

function buildApiUrl(path: string) {
  const base = (import.meta.env.VITE_API_URL || '').replace(/\/$/, '')
  return `${base}${path}`
}

function loadTianaiCaptchaAssets() {
  return Promise.all([
    loadStyle(`${import.meta.env.BASE_URL}tac/css/tac.css`),
    loadScript(`${import.meta.env.BASE_URL}tac/js/tac.min.js`)
  ])
}

function loadStyle(href: string) {
  if (document.querySelector(`link[href="${href}"]`)) return Promise.resolve()
  return new Promise<void>((resolve, reject) => {
    const link = document.createElement('link')
    link.rel = 'stylesheet'
    link.href = href
    link.onload = () => resolve()
    link.onerror = () => reject(new Error(`样式加载失败: ${href}`))
    document.head.appendChild(link)
  })
}

function loadScript(src: string) {
  if (window.TAC) return Promise.resolve()
  const existingScript = document.querySelector(`script[src="${src}"]`)
  if (existingScript) {
    return new Promise<void>((resolve) => existingScript.addEventListener('load', () => resolve()))
  }
  return new Promise<void>((resolve, reject) => {
    const script = document.createElement('script')
    script.src = src
    script.onload = () => resolve()
    script.onerror = () => reject(new Error(`脚本加载失败: ${src}`))
    document.body.appendChild(script)
  })
}

export {}
