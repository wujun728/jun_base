import { fetchDictData, type DictData } from '@/api/common'

const cache = new Map<string, DictData[]>()

export function useDict(...types: string[]) {
  const dict = reactive<Record<string, DictData[]>>({})
  const loading = ref(false)

  onMounted(async () => {
    loading.value = true
    try {
      await Promise.all(
        types.map(async (type) => {
          if (!cache.has(type)) cache.set(type, await fetchDictData(type))
          dict[type] = cache.get(type) || []
        })
      )
    } finally {
      loading.value = false
    }
  })

  return { dict, loading }
}

export function clearDictCache() {
  cache.clear()
}
