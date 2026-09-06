import { fetchChuteOptions } from '@/api/device'
import { fetchDeptList } from '@/api/system/dept'
import { useDict } from '@/hooks/core/useDict'
import { buildTree } from '@/utils/system/tree'

export function useDeviceOptions() {
  const { dict } = useDict('sys_normal_disable')
  const chuteOptions = ref<Record<string, any>[]>([])
  const deptOptions = ref<Record<string, any>[]>([])

  const refreshChutes = async () => {
    chuteOptions.value = (await fetchChuteOptions()).map((item) => ({
      ...item,
      label: item.chuteName,
      value: item.chuteId
    }))
  }

  const refreshDepts = async () => {
    deptOptions.value = buildTree(await fetchDeptList(), 'deptId', 'parentId')
  }

  onMounted(() => {
    void refreshChutes()
    void refreshDepts()
  })

  return {
    dict,
    chuteOptions,
    deptOptions,
    refreshChutes
  }
}
