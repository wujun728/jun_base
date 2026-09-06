<script setup>
import { VueDraggable } from 'vue-draggable-plus'

defineProps({
  listKey: {
    type: Number,
    default: 0,
  },
})

const open = defineModel('open', { type: Boolean })
const list = defineModel('list', { type: Array })
const checked = defineModel('checked', { type: Array })

const emit = defineEmits(['change-column', 'drag-update', 'reset-sort'])
</script>

<template>
  <a-dropdown
    v-model:open="open"
    :trigger="['click']"
    placement="bottomRight"
    overlay-class-name="column-display"
  >
    <div class="table-search-button-item right-border">
      <SvgIcon size="14" icon-class="ant-icon-AppstoreOutlined" />
    </div>

    <template #overlay>
      <div class="column-display-panel">
        <a-checkbox-group v-model:value="checked">
          <div class="pt6 pb6">
            <VueDraggable
              :key="listKey"
              v-model="list"
              :animation="150"
              @update="emit('drag-update')"
            >
              <div
                v-for="item in list"
                :key="item.prop || item.label"
                class="column-display-item"
              >
                <a-checkbox
                  v-if="item.prop"
                  :value="item.prop"
                  @change="
                    (e) => emit('change-column', e.target.checked, item.prop)
                  "
                >
                  {{ item.label }}
                </a-checkbox>
              </div>
            </VueDraggable>
          </div>
        </a-checkbox-group>

        <a-divider style="margin: 0" />

        <div class="drop-btns">
          <a-button size="small" @click="emit('reset-sort')">
            恢复默认排序
          </a-button>
        </div>
      </div>
    </template>
  </a-dropdown>
</template>

<style lang="scss">
.column-display {
  .column-display-panel {
    min-width: 180px;
    max-height: 380px;
    overflow-y: auto;
    background: var(--ba-bg-color-overlay);
    border-radius: 6px;
    box-shadow: 0 6px 16px rgba(0, 0, 0, 0.08);
    border: 1px solid var(--ba-border-color);
  }

  .column-display-item {
    padding: 0;

    .ant-checkbox-wrapper {
      width: 100%;
      padding: 6px 16px;
      height: 32px;
    }
  }

  .drop-btns {
    padding: 6px 16px;
  }
}
</style>
