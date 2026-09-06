/**
 * 性能优化工具
 */

// 防抖函数
export function debounce(func, wait, immediate = false) {
  let timeout;
  return function executedFunction(...args) {
    const later = () => {
      timeout = null;
      if (!immediate) func.apply(this, args);
    };
    const callNow = immediate && !timeout;
    clearTimeout(timeout);
    timeout = setTimeout(later, wait);
    if (callNow) func.apply(this, args);
  };
}

// 节流函数
export function throttle(func, limit) {
  let inThrottle;
  return function (...args) {
    if (!inThrottle) {
      func.apply(this, args);
      inThrottle = true;
      setTimeout(() => (inThrottle = false), limit);
    }
  };
}

// 图片懒加载指令
export const lazyLoad = {
  mounted(el, binding) {
    const observer = new IntersectionObserver((entries) => {
      entries.forEach((entry) => {
        if (entry.isIntersecting) {
          const img = entry.target;
          img.src = binding.value;
          img.classList.remove('lazy');
          observer.unobserve(img);
        }
      });
    });

    el.classList.add('lazy');
    observer.observe(el);
  },
};

// 虚拟滚动组件
export function useVirtualScroll(items, itemHeight, containerHeight) {
  const visibleCount = Math.ceil(containerHeight / itemHeight);
  const startIndex = ref(0);
  const endIndex = computed(() => Math.min(startIndex.value + visibleCount, items.length));

  const visibleItems = computed(() => items.slice(startIndex.value, endIndex.value));

  const onScroll = throttle((e) => {
    const scrollTop = e.target.scrollTop;
    startIndex.value = Math.floor(scrollTop / itemHeight);
  }, 16);

  return {
    visibleItems,
    onScroll,
    totalHeight: items.length * itemHeight,
    offsetY: startIndex.value * itemHeight,
  };
}

// 内存泄漏检测
export function detectMemoryLeaks() {
  if (import.meta.env.DEV) {
    let initialMemory = performance.memory?.usedJSHeapSize || 0;

    setInterval(() => {
      const currentMemory = performance.memory?.usedJSHeapSize || 0;
      const memoryIncrease = currentMemory - initialMemory;

      if (memoryIncrease > 50 * 1024 * 1024) {
        // 50MB
        console.warn('⚠️ 检测到可能的内存泄漏:', {
          initial: `${(initialMemory / 1024 / 1024).toFixed(2)}MB`,
          current: `${(currentMemory / 1024 / 1024).toFixed(2)}MB`,
          increase: `${(memoryIncrease / 1024 / 1024).toFixed(2)}MB`,
        });
      }
    }, 30000); // 每30秒检查一次
  }
}

// 组件缓存管理
export class ComponentCache {
  constructor(maxSize = 10) {
    this.cache = new Map();
    this.maxSize = maxSize;
  }

  get(key) {
    if (this.cache.has(key)) {
      // 移到最后（LRU）
      const value = this.cache.get(key);
      this.cache.delete(key);
      this.cache.set(key, value);
      return value;
    }
    return null;
  }

  set(key, value) {
    if (this.cache.has(key)) {
      this.cache.delete(key);
    } else if (this.cache.size >= this.maxSize) {
      // 删除最旧的项
      const firstKey = this.cache.keys().next().value;
      this.cache.delete(firstKey);
    }
    this.cache.set(key, value);
  }

  clear() {
    this.cache.clear();
  }
}

// 资源预加载
export function preloadResources(resources) {
  resources.forEach((resource) => {
    if (resource.type === 'image') {
      const img = new Image();
      img.src = resource.url;
    } else if (resource.type === 'script') {
      const link = document.createElement('link');
      link.rel = 'preload';
      link.as = 'script';
      link.href = resource.url;
      document.head.appendChild(link);
    } else if (resource.type === 'style') {
      const link = document.createElement('link');
      link.rel = 'preload';
      link.as = 'style';
      link.href = resource.url;
      document.head.appendChild(link);
    }
  });
}

// 代码分割辅助函数
export function createAsyncComponent(loader, options = {}) {
  return defineAsyncComponent({
    loader,
    loadingComponent: options.loading,
    errorComponent: options.error,
    delay: options.delay || 200,
    timeout: options.timeout || 3000,
    suspensible: false,
    onError(error, retry, fail, attempts) {
      if (attempts <= 3) {
        retry();
      } else {
        fail();
      }
    },
  });
}

export default {
  debounce,
  throttle,
  lazyLoad,
  useVirtualScroll,
  detectMemoryLeaks,
  ComponentCache,
  preloadResources,
  createAsyncComponent,
};
