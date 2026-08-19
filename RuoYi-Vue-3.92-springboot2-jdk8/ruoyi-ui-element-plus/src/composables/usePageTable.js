import { reactive, ref } from "vue";

/**
 * 分页列表页通用状态管理
 *
 * 只管理请求、分页数据和查询参数，不处理勾选、弹窗、表单
 */
export function usePageTable(options) {
  const { initialParams, request, onBeforeReset } = options;

  const loading = ref(false);
  const list = ref([]);
  const total = ref(0);
  const params = reactive({ ...initialParams });

  /**
   * 拉取当前查询参数对应的分页数据
   *
   * 只负责请求和回填，不处理弹窗、路由或消息提示
   */
  async function fetchData() {
    loading.value = true;
    try {
      const data = await request(params);
      list.value = data.list ?? [];
      total.value = data.total ?? 0;
    } finally {
      loading.value = false;
    }
  }

  /**
   * 回到第一页并查询
   */
  function handleQuery() {
    params.pageNum = 1;
    fetchData();
  }

  /**
   * 恢复初始查询参数
   *
   * 保持响应式引用不变，不触发查询
   */
  function resetParams() {
    Object.assign(params, initialParams);
  }

  /**
   * 恢复初始查询参数并重新查询
   */
  function handleResetQuery() {
    onBeforeReset?.();
    resetParams();
    fetchData();
  }

  return {
    loading,
    list,
    total,
    params,
    fetchData,
    handleQuery,
    handleResetQuery,
    resetParams,
  };
}
