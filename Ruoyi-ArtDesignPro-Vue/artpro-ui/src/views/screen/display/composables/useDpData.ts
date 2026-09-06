import { computed, onBeforeUnmount, reactive, ref } from 'vue'
import { dpApi } from '../services/api'
import { checkDeviceStatuses } from '../services/deviceRegistry'
import {
  mockBar,
  mockCarsLogs,
  mockDevices,
  mockLive,
  mockLogs,
  mockMaterials,
  mockMineCars,
  mockSelectOptions,
  mockWarnings,
  mockWarningSummary
} from '../services/mock'
import type {
  BarPayload,
  CameraCategory,
  DeviceStatus,
  LogsPayload,
  MaterialLevel,
  MineCarStats,
  SelectOption,
  WarningItem,
  WarningSummary
} from '../services/types'

const USE_MOCK_ON_ERROR = import.meta.env.VITE_USE_MOCK_ON_ERROR !== 'false'
const FILL_VIDEO_TEST_DATA = import.meta.env.DEV && import.meta.env.VITE_FILL_VIDEO_TEST_DATA !== 'false'

async function withFallback<T>(loader: () => Promise<T>, fallback: T): Promise<T> {
  try {
    return await loader()
  } catch (error) {
    if (!USE_MOCK_ON_ERROR) throw error
    return fallback
  }
}

function fillVideoTestData(live: CameraCategory[]) {
  if (!FILL_VIDEO_TEST_DATA) return live

  const merged = live.map(group => ({ ...group, children: [...(group.children || [])] }))
  mockLive.forEach(mockGroup => {
    let group = merged.find(item => item.label === mockGroup.label)
    if (!group) {
      group = { label: mockGroup.label, children: [] }
      merged.push(group)
    }

    const labels = new Set(group.children.map(item => item.label))
    mockGroup.children.forEach(camera => {
      if (!labels.has(camera.label)) group.children.push(camera)
    })
  })

  return merged
}

export function useDpData() {
  const loading = ref(true)
  const lastUpdated = ref('')
  const warningType = ref('全部')
  const materialType = ref<'ai' | 'ld'>('ai')
  const selectedDept = ref('0')
  const wsMessage = ref('')
  const wsTick = ref(0)
  let refreshAllPromise: Promise<void> | null = null
  let refreshAlertStatePromise: Promise<void> | null = null
  let socket: WebSocket | null = null

  const state = reactive({
    warningSummary: { ...mockWarningSummary } as WarningSummary,
    warnings: [...mockWarnings] as WarningItem[],
    devices: [...mockDevices] as DeviceStatus[],
    cars: { ...mockMineCars } as MineCarStats,
    bar: { ...mockBar } as BarPayload,
    logs: {
      ...mockLogs,
      detail: [...mockLogs.detail]
    } as LogsPayload,
    selectOptions: [...mockSelectOptions] as SelectOption[],
    materials: [...mockMaterials] as MaterialLevel[],
    live: [...mockLive] as CameraCategory[]
  })

  const selectedDeptLabel = computed(() => {
    if (selectedDept.value === '0') return '总览'
    return state.selectOptions.find(item => item.value === selectedDept.value)?.lable || selectedDept.value
  })

  const cameras = computed(() => state.live.flatMap(group => group.children || []))

  async function refreshStatic() {
    const [summary, devices, cars, bar, logs, options, live] = await Promise.all([
      withFallback(dpApi.warningSummary, mockWarningSummary),
      checkDeviceStatuses(),
      withFallback(dpApi.cars, mockMineCars),
      withFallback(dpApi.bar, mockBar),
      withFallback(dpApi.logs, mockLogs),
      withFallback(dpApi.select, mockSelectOptions),
      withFallback(dpApi.live, mockLive)
    ])

    state.warningSummary = summary
    state.devices = devices
    state.cars = cars
    state.bar = bar
    state.logs = {
      ...logs,
      detail: [...logs.detail]
    }
    state.selectOptions = options
    state.live = fillVideoTestData(live)
  }

  async function refreshWarnings() {
    state.warnings = await withFallback(() => dpApi.warningList(warningType.value), mockWarnings)
  }

  async function refreshMaterials() {
    const dept = selectedDept.value === '0' ? '' : selectedDept.value
    const apiResult = await dpApi.aiLogs(materialType.value, dept).catch(() => null)
    if (apiResult && Array.isArray(apiResult)) {
      state.materials = apiResult.map(item => ({ ...item, dept: item.dept || dept }))
    } else {
      state.materials = [...mockMaterials]
    }
  }

  async function refreshAlertState() {
    if (refreshAlertStatePromise) return refreshAlertStatePromise

    refreshAlertStatePromise = (async () => {
      const [summary, warnings, devices] = await Promise.all([
        withFallback(dpApi.warningSummary, mockWarningSummary),
        withFallback(() => dpApi.warningList(warningType.value), mockWarnings),
        checkDeviceStatuses()
      ])

      state.warningSummary = summary
      state.warnings = warnings
      state.devices = devices
    })().finally(() => {
      refreshAlertStatePromise = null
    })

    return refreshAlertStatePromise
  }

  async function refreshAll() {
    if (refreshAllPromise) return refreshAllPromise

    loading.value = true
    refreshAllPromise = Promise.all([refreshStatic(), refreshWarnings(), refreshMaterials()])
      .then(() => {
        lastUpdated.value = new Date().toLocaleTimeString('zh-CN', { hour12: false })
      })
      .finally(() => {
        loading.value = false
        refreshAllPromise = null
      })

    return refreshAllPromise
  }

  function openWarningStream() {
    if (socket) {
      try {
        socket.close()
      } catch {
        // ignore
      }
    }

    const wsBase = import.meta.env.VITE_DP_WS_BASE_URL || import.meta.env.VITE_WS_BASE_URL || `${location.protocol === 'https:' ? 'wss' : 'ws'}://${location.host}`
    const wsUrl = `${wsBase.replace(/\/$/, '')}/webSocket/dpwarn`

    try {
      socket = new WebSocket(wsUrl)
      socket.onmessage = event => {
        try {
          const data = JSON.parse(event.data)
          wsMessage.value = data?.msg || JSON.stringify(data)
          void refreshAlertState().finally(() => {
            wsTick.value += 1
          })
        } catch {
          wsMessage.value = String(event.data || `ws-${Date.now()}`)
          void refreshAlertState().finally(() => {
            wsTick.value += 1
          })
        }
      }
      socket.onerror = () => {
        wsMessage.value = 'WebSocket连接异常'
      }
    } catch {
      wsMessage.value = 'WebSocket未连接'
      socket = null
    }
  }

  function closeWarningStream() {
    if (socket) {
      try {
        socket.close()
      } catch {
        // ignore
      }
      socket = null
    }
  }

  onBeforeUnmount(() => {
    closeWarningStream()
  })

  return {
    loading,
    lastUpdated,
    warningType,
    materialType,
    selectedDept,
    selectedDeptLabel,
    wsMessage,
    wsTick,
    state,
    cameras,
    refreshAll,
    refreshAlertState,
    refreshWarnings,
    refreshMaterials,
    openWarningStream
  }
}
