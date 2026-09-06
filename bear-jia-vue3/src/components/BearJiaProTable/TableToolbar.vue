<template>
  <div class="table-toolbar">
    <div class="toolbar-left">
      <slot name="left"></slot>
    </div>

    <div class="toolbar-right">
      <slot name="right"></slot>

      <!-- 刷新 -->
      <a-tooltip v-if="toolbarConfig.refresh" title="刷新">
        <a-button type="text" size="small" :loading="loading" @click="$emit('refresh')">
          <template #icon>
            <ReloadOutlined />
          </template>
        </a-button>
      </a-tooltip>

      <!-- 密度设置 -->
      <a-dropdown v-if="toolbarConfig.density" :trigger="['click']" placement="bottomRight">
        <a-tooltip title="密度">
          <a-button type="text" size="small" @click.prevent>
            <template #icon>
              <ColumnHeightOutlined />
            </template>
          </a-button>
        </a-tooltip>

        <template #overlay>
          <a-menu :selected-keys="[currentSize]" @click="handleSizeChange">
            <a-menu-item key="small">
              <span>紧凑</span>
            </a-menu-item>
            <a-menu-item key="middle">
              <span>中等</span>
            </a-menu-item>
            <a-menu-item key="large">
              <span>宽松</span>
            </a-menu-item>
          </a-menu>
        </template>
      </a-dropdown>

      <!-- 全屏 -->
      <a-tooltip v-if="toolbarConfig.fullscreen" :title="isFullscreen ? '退出全屏' : '全屏'">
        <a-button type="text" size="small" @click="toggleFullscreen">
          <template #icon>
            <FullscreenOutlined v-if="!isFullscreen" />
            <FullscreenExitOutlined v-else />
          </template>
        </a-button>
      </a-tooltip>

      <!-- 列设置 -->
      <ColumnSettings
        v-if="toolbarConfig.columnSettings"
        :columns="columns"
        :selectedColumns="selectedColumns"
        @confirm="handleColumnSettingsConfirm"
      />

      <!-- 更多设置 -->
      <a-dropdown v-if="toolbarConfig.settings" :trigger="['click']" placement="bottomRight">
        <a-tooltip title="设置">
          <a-button type="text" size="small" @click.prevent>
            <template #icon>
              <SettingOutlined />
            </template>
          </a-button>
        </a-tooltip>

        <template #overlay>
          <a-menu @click="handleSettingsClick">
            <a-menu-item key="export" :disabled="!exportConfig?.enabled">
              <ExportOutlined />
              <span>导出数据</span>
            </a-menu-item>
            <a-menu-item key="import" :disabled="!importConfig?.enabled">
              <ImportOutlined />
              <span>导入数据</span>
            </a-menu-item>
            <a-menu-divider />
            <a-menu-item key="reset">
              <ReloadOutlined />
              <span>重置表格</span>
            </a-menu-item>
          </a-menu>
        </template>
      </a-dropdown>
    </div>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue';
import {
  ColumnHeightOutlined,
  ReloadOutlined,
  FullscreenOutlined,
  FullscreenExitOutlined,
  SettingOutlined,
  ExportOutlined,
  ImportOutlined
} from '@ant-design/icons-vue';
import { useTableConfigStore } from '@/stores/tableConfig';
import ColumnSettings from './ColumnSettings.vue';

const props = defineProps({
  columns: {
    type: Array,
    default: () => []
  },
  selectedColumns: {
    type: Array,
    default: () => []
  },
  loading: {
    type: Boolean,
    default: false
  },
  // 工具栏配置对象
  toolbarConfig: {
    type: Object,
    default: () => ({
      refresh: true,      // 刷新按钮
      density: true,      // 密度设置
      fullscreen: true,   // 全屏按钮
      columnSettings: true, // 列设置
      settings: true      // 更多设置
    })
  },
  // 导出配置
  exportConfig: {
    type: Object,
    default: () => ({
      enabled: false
    })
  },
  // 导入配置
  importConfig: {
    type: Object,
    default: () => ({
      enabled: false
    })
  }
});

const emit = defineEmits([
  'update:selectedColumns',
  'columnSettingsChange',
  'refresh',
  'fullscreen',
  'export',
  'import',
  'reset'
]);

const tableConfigStore = useTableConfigStore();
const isFullscreen = ref(false);

const currentSize = computed(() => tableConfigStore.size || 'middle');

const handleSizeChange = ({ key }) => {
  tableConfigStore.setSize(key);
};

const toggleFullscreen = () => {
  isFullscreen.value = !isFullscreen.value;
  emit('fullscreen', isFullscreen.value);
};

const handleColumnSettingsConfirm = (selectedColumns) => {
  emit('columnSettingsChange', selectedColumns);
};

const handleSettingsClick = ({ key }) => {
  switch (key) {
    case 'export':
      emit('export');
      break;
    case 'import':
      emit('import');
      break;
    case 'reset':
      emit('reset');
      break;
  }
};
</script>

<style scoped>
.table-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 0;
}

.toolbar-left {
  display: flex;
  align-items: center;
  gap: 8px;
}

.toolbar-right {
  display: flex;
  align-items: center;
  gap: 4px;
}
</style>
