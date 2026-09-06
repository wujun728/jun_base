<template>
  <a-dropdown
    :trigger="['click']"
    placement="bottomRight"
    :open="dropdownVisible"
    @openChange="handleOpenChange"
  >
    <a-tooltip title="列设置">
      <a-button type="text" size="small" @click.prevent="handleButtonClick">
        <template #icon>
          <BearJiaIcon icon="UnorderedListOutlined" />
        </template>
      </a-button>
    </a-tooltip>

    <template #overlay>
      <div class="column-settings-dropdown" @click.stop>
        <div class="settings-header">
          <span>列设置</span>
          <a-button type="link" size="small" @click="resetAll">重置</a-button>
        </div>

        <div class="settings-content">
          <a-checkbox-group v-model:value="localSelectedColumns" class="column-list">
            <a-checkbox
              v-for="column in columns"
              :key="column.dataIndex || column.key"
              :value="column.dataIndex || column.key"
              class="column-item"
            >
              <span class="column-title">{{ column.title }}</span>
            </a-checkbox>
          </a-checkbox-group>
        </div>

        <div class="settings-footer">
          <a-button size="small" @click="handleCancel">取消</a-button>
          <a-button type="primary" size="small" @click="handleConfirm">确定</a-button>
        </div>
      </div>
    </template>
  </a-dropdown>
</template>

<script setup>
import { ref, watch } from 'vue';
import { BearJiaIcon } from '../../utils/BearJiaIcon';

const props = defineProps({
  columns: {
    type: Array,
    required: true
  },
  selectedColumns: {
    type: Array,
    required: true
  }
});

const emit = defineEmits(['update:selectedColumns', 'confirm']);

const localSelectedColumns = ref([...props.selectedColumns]);
const dropdownVisible = ref(false);

// 监听外部变化
watch(() => props.selectedColumns, (newVal) => {
  localSelectedColumns.value = [...newVal];
}, { deep: true });

const handleButtonClick = () => {
  dropdownVisible.value = !dropdownVisible.value;
};

const handleOpenChange = (visible) => {
  dropdownVisible.value = visible;
};

const resetAll = () => {
  localSelectedColumns.value = props.columns.map(col => col.dataIndex || col.key);
};

const handleCancel = () => {
  localSelectedColumns.value = [...props.selectedColumns];
  dropdownVisible.value = false;
};

const handleConfirm = () => {
  emit('update:selectedColumns', localSelectedColumns.value);
  emit('confirm', localSelectedColumns.value);
  dropdownVisible.value = false;
};
</script>

<style scoped>
.column-settings-dropdown {
  width: 200px;
  padding: 8px;
  background: white;
  border-radius: 6px;
  box-shadow: 0 6px 16px 0 rgba(0, 0, 0, 0.08);
}

.settings-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 8px;
  border-bottom: 1px solid #f0f0f0;
  margin-bottom: 8px;
}

.settings-content {
  max-height: 300px;
  overflow-y: auto;
}

.column-list {
  width: 100%;
  display: flex;
  flex-direction: column;
}

.column-item {
  display: flex !important;
  align-items: center !important;
  width: 100%;
  padding: 4px 0;
  margin-bottom: 0;
}

.column-item .ant-checkbox {
  margin-right: 8px;
}

.column-item .ant-checkbox-wrapper {
  width: 100%;
  display: flex !important;
  align-items: center !important;
  margin: 0 !important;
}

.column-title {
  font-size: 12px;
}

.settings-footer {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  padding-top: 8px;
  border-top: 1px solid #f0f0f0;
  margin-top: 8px;
}
</style>
