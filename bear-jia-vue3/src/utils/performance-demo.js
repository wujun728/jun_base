/**
 * 性能优化示例 - 展示自动导入的使用
 * 这个文件展示了如何使用自动导入的API，无需手动导入
 */

// 使用自动导入的 Vue API（无需 import）
export function usePerformanceDemo() {
  // 响应式数据
  const loading = ref(false);
  const data = reactive({
    list: [],
    total: 0,
  });

  // 计算属性
  const isEmpty = computed(() => data.list.length === 0);

  // 本地存储
  const theme = useLocalStorage('theme', 'light');

  // 异步操作示例
  const fetchData = async () => {
    loading.value = true;
    try {
      // 使用自动导入的 axios
      const response = await axios.get('/api/data');
      data.list = response.data.list;
      data.total = response.data.total;

      // 使用自动导入的 message
      message.success('数据加载成功');
    } catch (error) {
      // 使用自动导入的 notification
      notification.error({
        message: '加载失败',
        description: error.message,
      });
    } finally {
      loading.value = false;
    }
  };

  // 生命周期
  onMounted(() => {
    fetchData();
  });

  return {
    loading,
    data,
    isEmpty,
    theme,
    fetchData,
  };
}

// 工具函数示例
export function formatDate(date) {
  // 使用自动导入的 dayjs
  return dayjs(date).format('YYYY-MM-DD HH:mm:ss');
}

// 路由导航示例
export function navigateToPage(path) {
  const router = useRouter();
  router.push(path);
}
