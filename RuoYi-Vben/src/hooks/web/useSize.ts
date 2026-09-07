import { useWindowSize } from '@vueuse/core';
import { computed } from 'vue';

/**
 * 默认宽度小于屏幕宽度时 返回屏幕宽度
 * @param defaultWidth 默认宽度
 * @returns
 */
export function useMaxWidthOrDefault(defaultWidth: number) {
  const { width: windowWidth } = useWindowSize();
  return computed(() => {
    return windowWidth.value < defaultWidth ? windowWidth.value : defaultWidth;
  });
}
