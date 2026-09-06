<template>
  <div class="chute-height-page">
    <ArtSearchBar
      v-model="filters"
      :items="searchItems"
      :show-expand="false"
      @search="loadData"
      @reset="reset"
    />

    <ElCard class="height-board" shadow="never">
      <ArtTableHeader :loading="loading" @refresh="loadData">
        <template #left>
          <ElText class="board-title">溜井高度校验</ElText>
        </template>
      </ArtTableHeader>

      <ElEmpty v-if="!loading && !data.length" description="暂无溜井数据" />
      <div v-else v-loading="loading" class="chute-grid">
        <section v-for="item in data" :key="item.id" class="chute-panel">
          <div class="panel-head">
            <div>
              <h3>{{ item.lname }}</h3>
              <p>{{ item.tnameLabel || '-' }} / {{ item.company || '-' }}</p>
            </div>
            <ElTag effect="plain">{{ item.shitypeLabel || item.shitype || '-' }}</ElTag>
          </div>

          <div class="level-row">
            <div class="scale">
              <span>{{ formatNumber(item.allQuantity) }}</span>
              <span>{{ formatNumber(Number(item.allQuantity || 0) * 0.75) }}</span>
              <span>{{ formatNumber(Number(item.allQuantity || 0) * 0.5) }}</span>
              <span>{{ formatNumber(Number(item.allQuantity || 0) * 0.25) }}</span>
              <span>0</span>
            </div>
            <div class="well">
              <div class="fill" :style="{ height: `${heightPercent(item)}%` }" />
              <div class="current">{{ formatNumber(item.nowQuantity) }} 米</div>
            </div>
            <div class="stats">
              <span>矿石</span>
              <strong>{{ formatNumber(item.ton) }} 吨</strong>
              <span>半径</span>
              <strong>{{ formatNumber(item.rad) }} 米</strong>
            </div>
          </div>

          <div class="panel-actions">
            <ElButton
              type="primary"
              :disabled="!hasAuth('business:chuteHeight:edit')"
              @click="openHeightDialog(item)"
            >
              高度校验
            </ElButton>
          </div>
        </section>
      </div>
    </ElCard>

    <ElDialog v-model="dialogVisible" title="高度校验" width="420px">
      <ElForm label-width="86px">
        <ElFormItem label="溜井名称">
          <ElInput :model-value="current?.lname" disabled />
        </ElFormItem>
        <ElFormItem label="高度值">
          <ElInputNumber
            v-model="heightValue"
            :min="0"
            :max="maxHeight"
            :precision="2"
            :step="0.1"
            controls-position="right"
            class="full-input"
          />
        </ElFormItem>
      </ElForm>
      <template #footer>
        <ElButton @click="dialogVisible = false">取消</ElButton>
        <ElButton type="primary" :loading="saving" @click="submitHeight">保存</ElButton>
      </template>
    </ElDialog>
  </div>
</template>

<script setup lang="ts">
  import { fetchDictData } from '@/api/common'
  import { fetchDeptTree } from '@/api/system/user'
  import { fetchSqChuteHeightList, updateSqChuteHeight, type SqChute } from '@/api/business/sqChute'
  import { useAuth } from '@/hooks/core/useAuth'

  defineOptions({ name: 'BusinessSqChuteHeight' })

  const { hasAuth } = useAuth()
  const loading = ref(false)
  const saving = ref(false)
  const data = ref<SqChute[]>([])
  const deptOptions = ref<any[]>([])
  const chuteOptions = ref<Array<{ label: string; value: string }>>([])
  const shitypeOptions = ref<Array<{ label: string; value: string }>>([])
  const filters = reactive<Record<string, any>>({})
  const dialogVisible = ref(false)
  const current = ref<SqChute>()
  const heightValue = ref(0)
  const maxHeight = computed(() => Number(current.value?.allQuantity || 0))

  const searchItems = computed(() => [
    {
      key: 'tname',
      label: '所属单位',
      type: 'treeselect',
      props: {
        data: deptOptions.value,
        nodeKey: 'id',
        valueKey: 'id',
        props: { value: 'id', label: 'label', children: 'children' },
        checkStrictly: true,
        clearable: true
      }
    },
    {
      key: 'lname',
      label: '溜井名称',
      type: 'select',
      props: { options: chuteOptions.value, filterable: true, clearable: true }
    },
    { key: 'company', label: '所属中段', type: 'input', props: { clearable: true } }
  ])

  onMounted(async () => {
    const [deptTree, shitypes] = await Promise.all([fetchDeptTree(), fetchDictData('shitype')])
    deptOptions.value = deptTree
    shitypeOptions.value = shitypes.map((item) => ({
      label: item.dictLabel,
      value: item.dictValue
    }))
    await loadData()
  })

  async function loadData() {
    loading.value = true
    try {
      data.value = (await fetchSqChuteHeightList({ ...filters })).map((item) => ({
        ...item,
        id: String(item.id),
        shitypeLabel: item.shitypeLabel || resolveShitype(item.shitype)
      }))
      chuteOptions.value = data.value.map((item) => ({
        label: item.lname || String(item.id),
        value: item.lname || ''
      }))
    } finally {
      loading.value = false
    }
  }

  function reset() {
    Object.keys(filters).forEach((key) => delete filters[key])
    loadData()
  }

  function openHeightDialog(row: SqChute) {
    current.value = row
    heightValue.value = Number(row.nowQuantity || 0)
    dialogVisible.value = true
  }

  async function submitHeight() {
    if (!current.value) return
    saving.value = true
    try {
      await updateSqChuteHeight({
        id: current.value.id,
        nowQuantity: String(Math.min(heightValue.value, maxHeight.value)),
        shitype: current.value.shitype,
        rad: current.value.rad
      })
      dialogVisible.value = false
      await loadData()
    } finally {
      saving.value = false
    }
  }

  function resolveShitype(value?: string) {
    return shitypeOptions.value.find((item) => item.value === value)?.label || value || ''
  }

  function heightPercent(item: SqChute) {
    const total = Number(item.allQuantity || 0)
    if (!total) return 0
    return Math.max(0, Math.min(100, (Number(item.nowQuantity || 0) / total) * 100))
  }

  function formatNumber(value?: string | number) {
    const numberValue = Number(value || 0)
    return Number.isFinite(numberValue) ? numberValue.toFixed(2) : '0.00'
  }
</script>

<style scoped>
  .chute-height-page {
    display: flex;
    min-height: 0;
    flex-direction: column;
    gap: 16px;
  }

  .height-board {
    min-height: 0;
    flex: 1;
  }

  .board-title {
    font-size: 16px;
    font-weight: 600;
  }

  .chute-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
    gap: 16px;
  }

  .chute-panel {
    display: flex;
    min-height: 360px;
    flex-direction: column;
    justify-content: space-between;
    border: 1px solid var(--el-border-color-light);
    border-radius: 8px;
    padding: 18px;
    background: var(--el-bg-color);
  }

  .panel-head {
    display: flex;
    align-items: flex-start;
    justify-content: space-between;
    gap: 12px;
  }

  .panel-head h3 {
    margin: 0;
    color: var(--el-text-color-primary);
    font-size: 18px;
    font-weight: 600;
  }

  .panel-head p {
    margin: 6px 0 0;
    color: var(--el-text-color-secondary);
    font-size: 13px;
  }

  .level-row {
    display: grid;
    grid-template-columns: 54px 96px 1fr;
    align-items: center;
    justify-content: center;
    gap: 16px;
    padding: 18px 0;
  }

  .scale {
    display: flex;
    height: 220px;
    flex-direction: column;
    justify-content: space-between;
    color: var(--el-text-color-secondary);
    font-size: 12px;
    text-align: right;
  }

  .well {
    position: relative;
    overflow: hidden;
    width: 86px;
    height: 220px;
    border: 1px solid var(--el-border-color);
    border-radius: 43px 43px 18px 18px;
    background: color-mix(in srgb, var(--el-color-info-light-9) 70%, transparent);
  }

  .fill {
    position: absolute;
    right: 0;
    bottom: 0;
    left: 0;
    background: linear-gradient(180deg, #66d9a3 0%, #2f9e75 100%);
    transition: height 0.2s ease;
  }

  .current {
    position: absolute;
    right: 0;
    bottom: 18px;
    left: 0;
    color: #fff;
    font-size: 13px;
    font-weight: 600;
    text-align: center;
    text-shadow: 0 1px 2px rgb(0 0 0 / 35%);
  }

  .stats {
    display: grid;
    grid-template-columns: auto 1fr;
    gap: 10px 12px;
    color: var(--el-text-color-secondary);
    font-size: 13px;
  }

  .stats strong {
    color: var(--el-text-color-primary);
    font-weight: 600;
  }

  .panel-actions {
    display: flex;
    justify-content: flex-end;
  }

  .full-input {
    width: 100%;
  }
</style>
