// @ts-nocheck
import {isBrowser} from '../isBrowser'

// Keep forward compatible
// should be removed in next major version
export const supportsPassive = true;

export function raf(fn: FrameRequestCallback): number | NodeJS.Timeout {
  return isBrowser ? requestAnimationFrame(fn) : setTimeout(fn, 1000 / 30)
}

export function cancelRaf(id: number) {
  if (isBrowser) {
    cancelAnimationFrame(id);
  } else {
	clearTimeout(id);  
  }
}

// double raf for animation
export function doubleRaf(fn: FrameRequestCallback): void {
  raf(() => raf(fn));
}
