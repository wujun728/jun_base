import { createPinia } from "pinia"
import type { App } from "vue"

export const store = createPinia()

/**
 * 初始化 Store
 */
export default function initStore(app: App<Element>): void {
  app.use(store)
}