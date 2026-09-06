export const LOGIN_PAGE_APP_TITLE =
  import.meta.env.VITE_APP_TITLE ?? 'App'

/** 插画角色描边 / 瞳孔色 */
export const LOGIN_INK = '#2D2D2D'

type RouteQuery = Record<string, unknown> & { redirect?: unknown }

export function resolveLoginNavigateTarget(query: RouteQuery) {
  const redirectParam =
    typeof query.redirect === 'string' ? query.redirect : undefined
  const path =
    redirectParam &&
    redirectParam.startsWith('/') &&
    !redirectParam.startsWith('//')
      ? redirectParam.split('?')[0]
      : '/'

  const restQuery = { ...query }
  delete restQuery.redirect
  return { path, query: restQuery as Record<string, string | string[] | undefined> }
}

export function pupilOffsetFromMouse(
  mouseX: number,
  mouseY: number,
  originEl: HTMLElement | null | undefined,
  maxDistance: number,
  forced?: { x: number; y: number },
) {
  if (!originEl) return { x: 0, y: 0 }
  if (forced) return forced
  const r = originEl.getBoundingClientRect()
  const cx = r.left + r.width / 2
  const cy = r.top + r.height / 2
  const dx = mouseX - cx
  const dy = mouseY - cy
  const dist = Math.min(Math.hypot(dx, dy), maxDistance)
  const ang = Math.atan2(dy, dx)
  return { x: Math.cos(ang) * dist, y: Math.sin(ang) * dist }
}

export function readFaceSkew(
  el: HTMLElement | null | undefined,
  mouse: { x: number; y: number },
) {
  if (!el) return { faceX: 0, faceY: 0, bodySkew: 0 }
  const rect = el.getBoundingClientRect()
  const centerX = rect.left + rect.width / 2
  const centerY = rect.top + rect.height / 3
  const deltaX = mouse.x - centerX
  const deltaY = mouse.y - centerY
  return {
    faceX: Math.max(-15, Math.min(15, deltaX / 20)),
    faceY: Math.max(-10, Math.min(10, deltaY / 30)),
    bodySkew: Math.max(-6, Math.min(6, -deltaX / 120)),
  }
}

export function subscribeMousePosition(
  onMove: (p: { x: number; y: number }) => void,
) {
  let raf = 0
  const handler = (e: MouseEvent) => {
    const nx = e.clientX
    const ny = e.clientY
    cancelAnimationFrame(raf)
    raf = requestAnimationFrame(() => onMove({ x: nx, y: ny }))
  }
  window.addEventListener('mousemove', handler, { passive: true })
  return () => {
    cancelAnimationFrame(raf)
    window.removeEventListener('mousemove', handler)
  }
}

export function pupilAnchorVars(sizePx: number) {
  return { '--login-pupil-size': `${sizePx}px` }
}

export function pupilDotVars({
  sizePx,
  fill,
  x,
  y,
}: {
  sizePx: number
  fill: string
  x: number
  y: number
}) {
  return {
    '--login-pupil-size': `${sizePx}px`,
    '--login-pupil-fill': fill,
    '--login-pupil-x': `${x}px`,
    '--login-pupil-y': `${y}px`,
  }
}

export function eyeBallShellVars({
  sizePx,
  heightPx,
  fill,
}: {
  sizePx: number
  heightPx: number
  fill: string
}) {
  return {
    '--login-eye-size': `${sizePx}px`,
    '--login-eye-height': `${heightPx}px`,
    '--login-eye-fill': fill,
  }
}

export function eyeBallPupilVars({
  pupilSizePx,
  fill,
  x,
  y,
}: {
  pupilSizePx: number
  fill: string
  x: number
  y: number
}) {
  return {
    '--login-eye-pupil-size': `${pupilSizePx}px`,
    '--login-eye-pupil-fill': fill,
    '--login-eye-pupil-x': `${x}px`,
    '--login-eye-pupil-y': `${y}px`,
  }
}

export function purpleCharacterVars(heightPx: number, transform: string) {
  return {
    '--login-char-h': `${heightPx}px`,
    '--login-char-tf': transform,
  }
}

export function characterTransformVars(transform: string) {
  return { '--login-char-tf': transform }
}

export function eyesPositionVars(leftPx: number, topPx: number) {
  return {
    '--login-eye-left': `${leftPx}px`,
    '--login-eye-top': `${topPx}px`,
  }
}

export function mouthPositionVars(leftPx: number, topPx: number) {
  return {
    '--login-mouth-left': `${leftPx}px`,
    '--login-mouth-top': `${topPx}px`,
  }
}
