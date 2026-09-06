/**
 * 虚拟滚动 Composable
 * 用于优化大数据量列表渲染性能
 *
 * @author BearJia
 * @date 2025-11-03
 */

import { ref, computed, watch, onUnmounted } from 'vue';

/**
 * 虚拟滚动配置选项
 * @typedef {Object} VirtualScrollOptions
 * @property {Array} dataSource - 完整数据源
 * @property {number} itemHeight - 每行高度（像素）
 * @property {number} buffer - 缓冲区行数（上下各渲染额外的行数）
 * @property {number} containerHeight - 容器高度（像素）
 * @property {boolean} enabled - 是否启用虚拟滚动
 * @property {number} threshold - 启用虚拟滚动的最小数据量阈值
 */

/**
 * 使用虚拟滚动
 * @param {VirtualScrollOptions} options - 配置选项
 * @returns {Object} 虚拟滚动状态和方法
 */
export function useVirtualScroll(options = {}) {
  const {
    dataSource = ref([]),
    itemHeight = 54, // Ant Design Table 默认行高（default size）
    buffer = 5, // 上下各渲染5行缓冲
    containerHeight = 600,
    enabled = true,
    threshold = 100, // 数据量超过100条时启用虚拟滚动
  } = options;

  // 滚动状态
  const scrollTop = ref(0);
  const containerRef = ref(null);

  // 是否真正启用虚拟滚动（基于数据量和enabled标志）
  const isVirtualScrollEnabled = computed(() => {
    return enabled && dataSource.value && dataSource.value.length > threshold;
  });

  // 计算可见区域可容纳的行数
  const visibleCount = computed(() => {
    return Math.ceil(containerHeight / itemHeight);
  });

  // 计算开始索引（减去缓冲区）
  const startIndex = computed(() => {
    if (!isVirtualScrollEnabled.value) return 0;

    const index = Math.floor(scrollTop.value / itemHeight) - buffer;
    return Math.max(0, index);
  });

  // 计算结束索引（加上缓冲区）
  const endIndex = computed(() => {
    if (!isVirtualScrollEnabled.value) {
      return dataSource.value?.length || 0;
    }

    const index = startIndex.value + visibleCount.value + buffer * 2;
    return Math.min(dataSource.value?.length || 0, index);
  });

  // 可见数据
  const visibleData = computed(() => {
    if (!isVirtualScrollEnabled.value) {
      return dataSource.value || [];
    }

    return (dataSource.value || []).slice(startIndex.value, endIndex.value);
  });

  // 计算总高度
  const totalHeight = computed(() => {
    return (dataSource.value?.length || 0) * itemHeight;
  });

  // 计算偏移量
  const offsetY = computed(() => {
    return startIndex.value * itemHeight;
  });

  // 滚动事件处理
  const handleScroll = (event) => {
    if (!isVirtualScrollEnabled.value) return;

    const target = event.target;
    if (target) {
      scrollTop.value = target.scrollTop;
    }
  };

  // 滚动到指定索引
  const scrollToIndex = (index) => {
    if (!containerRef.value) return;

    const scrollElement = containerRef.value.querySelector('.ant-table-body');
    if (scrollElement) {
      const targetScrollTop = index * itemHeight;
      scrollElement.scrollTop = targetScrollTop;
      scrollTop.value = targetScrollTop;
    }
  };

  // 滚动到顶部
  const scrollToTop = () => {
    scrollToIndex(0);
  };

  // 滚动到底部
  const scrollToBottom = () => {
    if (dataSource.value) {
      scrollToIndex(dataSource.value.length - 1);
    }
  };

  // 监听容器引用变化，绑定滚动事件
  watch(containerRef, (newContainer) => {
    if (newContainer && isVirtualScrollEnabled.value) {
      const scrollElement = newContainer.querySelector('.ant-table-body');
      if (scrollElement) {
        scrollElement.addEventListener('scroll', handleScroll, { passive: true });

        // 清理函数
        onUnmounted(() => {
          scrollElement.removeEventListener('scroll', handleScroll);
        });
      }
    }
  });

  // 性能监控
  const performanceStats = computed(() => {
    const total = dataSource.value?.length || 0;
    const rendered = visibleData.value.length;
    const savedNodes = total - rendered;
    const reductionPercent = total > 0 ? ((savedNodes / total) * 100).toFixed(1) : 0;

    return {
      total,
      rendered,
      savedNodes,
      reductionPercent: `${reductionPercent}%`,
      enabled: isVirtualScrollEnabled.value,
    };
  });

  return {
    // 状态
    containerRef,
    scrollTop,
    isVirtualScrollEnabled,

    // 计算属性
    visibleData,
    visibleCount,
    startIndex,
    endIndex,
    totalHeight,
    offsetY,
    performanceStats,

    // 方法
    handleScroll,
    scrollToIndex,
    scrollToTop,
    scrollToBottom,
  };
}

/**
 * 根据表格size计算行高
 * @param {string} size - 表格尺寸 ('small' | 'middle' | 'large')
 * @returns {number} 行高（像素）
 */
export function getItemHeightBySize(size = 'middle') {
  const heightMap = {
    small: 40, // Ant Design small size
    middle: 54, // Ant Design default size
    large: 64, // Ant Design large size
  };

  return heightMap[size] || heightMap.middle;
}

/**
 * 虚拟滚动表格样式辅助函数
 * 生成虚拟滚动所需的样式对象
 * @param {Object} virtualScrollState - useVirtualScroll返回的状态
 * @returns {Object} 样式对象
 */
export function getVirtualScrollStyle(virtualScrollState) {
  const { totalHeight, offsetY, isVirtualScrollEnabled } = virtualScrollState;

  if (!isVirtualScrollEnabled) {
    return {};
  }

  return {
    containerStyle: {
      position: 'relative',
      overflow: 'auto',
    },
    wrapperStyle: {
      height: `${totalHeight}px`,
      position: 'relative',
    },
    contentStyle: {
      transform: `translateY(${offsetY}px)`,
      willChange: 'transform',
    },
  };
}
