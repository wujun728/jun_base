<script setup>
import hasPermi from '@/utils/hasPermi'
import ColumnDisplayPanel from './ColumnDisplayPanel.vue'

const props = defineProps({
  headerButtons: {
    type: Array,
    default: () => [],
  },
  permission: {
    type: Object,
    default: () => ({}),
  },
  tableSelected: {
    type: Array,
    default: () => [],
  },
  showPageSearch: Boolean,
  columnDisplayKey: {
    type: Number,
    default: 0,
  },
  hasLeftSlot: Boolean,
  hasRightSlot: Boolean,
})

const columnDisplayOpen = defineModel('columnDisplayOpen', { type: Boolean })
const columnDisplayList = defineModel('columnDisplayList', { type: Array })
const columnChecked = defineModel('columnChecked', { type: Array })

const emit = defineEmits([
  'refresh',
  'add',
  'edit',
  'delete',
  'toggle-search',
  'change-column',
  'drag-update',
  'reset-sort',
])

const hasSelection = computed(() => props.tableSelected.length > 0)
const canEditSelected = computed(() => props.tableSelected.length === 1)

const showLeft = computed(
  () => props.hasLeftSlot || props.headerButtons.length > 0
)

const showRight = computed(
  () => props.hasRightSlot || props.headerButtons.length > 0
)

const showToolbar = computed(
  () =>
    props.headerButtons.includes('columnDisplay') ||
    props.headerButtons.includes('comSearch')
)

const includes = (name) => props.headerButtons.includes(name)
</script>

<template>
  <div class="header-scroll">
    <div class="table-header">
      <div v-if="showLeft">
        <div class="flex">
          <a-button v-if="includes('refresh')" @click="emit('refresh')">
            <template #icon>
              <SvgIcon icon-class="sync-alt" :size="12" />
            </template>
          </a-button>

          <a-button
            v-if="includes('add') && hasPermi(permission.add)"
            type="primary"
            class="order5"
            @click="emit('add')"
          >
            <template #icon>
              <SvgIcon :size="14" icon-class="plus" />
            </template>
            <span class="ml6">添加</span>
          </a-button>

          <a-button
            v-if="includes('edit') && hasPermi(permission.edit)"
            type="primary"
            class="order10"
            :disabled="!canEditSelected"
            @click="emit('edit')"
          >
            <template #icon>
              <SvgIcon :size="14" icon-class="pencil" />
            </template>
            <span class="ml6">编辑</span>
          </a-button>

          <a-popconfirm
            v-if="includes('delete') && hasPermi(permission.del)"
            :title="`确定删除选中的${tableSelected.length}条记录？`"
            ok-text="确认"
            class="order15"
            cancel-text="取消"
            :disabled="!hasSelection"
            @confirm="emit('delete', tableSelected)"
          >
            <a-button danger type="primary" :disabled="!hasSelection">
              <template #icon>
                <SvgIcon :size="14" icon-class="trash" />
              </template>
              <span class="ml6">删除</span>
            </a-button>
          </a-popconfirm>

          <slot name="handleLeft" />
        </div>
      </div>

      <div v-if="showRight">
        <div v-if="showToolbar" class="table-search-button-group">
          <ColumnDisplayPanel
            v-if="includes('columnDisplay')"
            v-model:open="columnDisplayOpen"
            v-model:list="columnDisplayList"
            v-model:checked="columnChecked"
            :list-key="columnDisplayKey"
            @change-column="
              (checked, prop) => emit('change-column', checked, prop, true)
            "
            @drag-update="emit('drag-update')"
            @reset-sort="emit('reset-sort')"
          />

          <a-tooltip
            v-if="includes('comSearch')"
            :title="showPageSearch ? '关闭搜索' : '展开搜索'"
            placement="top"
          >
            <div
              class="table-search-button-item"
              @click="emit('toggle-search')"
            >
              <SvgIcon size="14" icon-class="search" />
            </div>
          </a-tooltip>
        </div>

        <slot name="handleRight" />
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
.header-scroll {
  width: 100%;
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: none;
  font-size: 14px;
  padding: 13px 0;
  overflow-x: auto;
}

.table-search-button-group {
  display: flex;
  margin-left: 12px;
  border: 1px solid var(--ba-border-color);
  border-radius: 6px;
  overflow: hidden;

  button:focus,
  button:active {
    color: #000;
    background-color: var(--ba-bg-color);
  }

  button:hover {
    color: #000;
    background-color: rgba(0, 0, 0, 0.04);
  }

  :deep(.table-search-button-item) {
    padding: 0;
    width: 32px;
    height: 30px;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 0;
    cursor: pointer;
    &:hover {
      background-color: var(--ba-bg-color);
    }
  }

  .ant-btn + .ant-btn {
    margin: 0;
  }

  :deep(.ant-btn:focus-visible) {
    outline: none;
    outline-offset: 0;
  }
}

.flex {
  display: flex;
  align-items: center;
  gap: 10px;
}
</style>
