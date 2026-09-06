<template>
  <a-drawer
      :closable="false"
      :open="visible"
      class="theme-setting-drawer"
      placement="right"
      style="z-index: 999"
      title="布局设置"
      width="300"
      @close="$emit('update:visible', false)"
  >
    <div class="setting-drawer-content">
      <!-- 侧边栏风格设置 -->
      <div class="setting-drawer-block">
        <a-divider orientation="left">主题模式</a-divider>
        <div class="setting-drawer-block-checbox">
          <div class="setting-drawer-block-checbox-item" @click="handleThemeChange('dark')">
            <a-tooltip title="暗色主题">
              <img alt="dark" src="@/assets/images/dark.svg">
              <check-outlined v-if="localSettings.darkMode" class="setting-drawer-theme-color-icon"/>
            </a-tooltip>
          </div>
          <div class="setting-drawer-block-checbox-item" @click="handleThemeChange('light')">
            <a-tooltip title="亮色主题">
              <img alt="light" src="@/assets/images/light.svg">
              <check-outlined v-if="!localSettings.darkMode" class="setting-drawer-theme-color-icon"/>
            </a-tooltip>
          </div>
        </div>
      </div>

      <!-- 主题色设置 -->
      <div class="setting-drawer-block">
        <a-divider orientation="left">主题色</a-divider>
        <div class="setting-drawer-theme-color">
          <a-tooltip v-for="(item, index) in colorList" :key="index" :title="item.key">
            <div
                :style="{ backgroundColor: item.color }"
                class="setting-drawer-theme-color-block"
                @click="handleColorChange(item.color)"
            >
              <check-outlined v-if="localSettings.primaryColor === item.color"/>
            </div>
          </a-tooltip>
        </div>
      </div>

      <!-- 导航模式 -->
      <div class="setting-drawer-block">
        <a-divider orientation="left">导航模式</a-divider>
        <div class="setting-drawer-block-checbox">
          <div
            class="setting-drawer-block-checbox-item"
            :class="{ active: localSettings.navMode === 'side' }"
            @click="handleNavModeChange('side')">
            <a-tooltip title="侧边菜单布局">
              <img alt="side" src="@/assets/images/layout/side.svg">
              <check-outlined v-if="localSettings.navMode === 'side'" class="setting-drawer-theme-color-icon"/>
            </a-tooltip>
          </div>
          <div
            class="setting-drawer-block-checbox-item"
            :class="{ active: localSettings.navMode === 'top' }"
            @click="handleNavModeChange('top')">
            <a-tooltip title="顶部菜单布局">
              <img alt="top" src="@/assets/images/layout/top.svg">
              <check-outlined v-if="localSettings.navMode === 'top'" class="setting-drawer-theme-color-icon"/>
            </a-tooltip>
          </div>
          <div
            class="setting-drawer-block-checbox-item"
            :class="{ active: localSettings.navMode === 'mix' }"
            @click="handleNavModeChange('mix')">
            <a-tooltip title="混合布局">
              <img alt="mix" src="@/assets/images/layout/mix.svg">
              <check-outlined v-if="localSettings.navMode === 'mix'" class="setting-drawer-theme-color-icon"/>
            </a-tooltip>
          </div>
          <div
            class="setting-drawer-block-checbox-item"
            :class="{ active: localSettings.navMode === 'column' }"
            @click="handleNavModeChange('column')">
            <a-tooltip title="分栏布局">
              <img alt="column" src="@/assets/images/layout/fenlan.svg">
              <check-outlined v-if="localSettings.navMode === 'column'" class="setting-drawer-theme-color-icon"/>
            </a-tooltip>
          </div>
          <div
            class="setting-drawer-block-checbox-item"
            :class="{ active: localSettings.navMode === 'drawer' }"
            @click="handleNavModeChange('drawer')">
            <a-tooltip title="抽屉布局">
              <img alt="drawer" src="@/assets/images/layout/drawer.svg">
              <check-outlined v-if="localSettings.navMode === 'drawer'" class="setting-drawer-theme-color-icon"/>
            </a-tooltip>
          </div>
        </div>

        <!-- 布局设置 -->
        <a-divider orientation="left">布局设置</a-divider>
        <a-list :split="false">
          <a-list-item>
            <template #extra>
              <a-select
                  v-model:value="localSettings.contentWidth"
                  size="small"
                  style="width: 80px"
                  @change="handleSettingChange"
              >
                <a-select-option v-if="localSettings.layout === 'top'" value="Fixed">固定</a-select-option>
                <a-select-option value="Fluid">流式</a-select-option>
              </a-select>
            </template>
            内容区域宽度
          </a-list-item>
          <a-list-item>
            <template #extra>
              <a-switch
                  v-model:checked="localSettings.fixedHeader"
                  size="small"
                  @change="handleSettingChange"
              />
            </template>
            固定 Header
          </a-list-item>
          <a-list-item>
            <template #extra>
              <a-switch
                  v-model:checked="localSettings.fixedSidebar"
                  :disabled="localSettings.layout === 'top'"
                  size="small"
                  @change="handleSettingChange"
              />
            </template>
            <span :style="{ opacity: localSettings.layout === 'top' ? 0.5 : 1 }">固定侧边菜单</span>
          </a-list-item>
          <a-list-item>
            <template #extra>
              <a-switch
                  v-model:checked="localSettings.hideFooter"
                  size="small"
                  @change="handleSettingChange"
              />
            </template>
            隐藏 Footer
          </a-list-item>
          <a-list-item>
            <template #extra>
              <a-switch
                  v-model:checked="localSettings.multiTab"
                  size="small"
                  @change="handleSettingChange"
              />
            </template>
            多页签模式
          </a-list-item>
          <a-list-item>
            <template #extra>
              <a-switch
                  v-model:checked="localSettings.sidebarColorfulIcon"
                  size="small"
                  @change="handleSettingChange"
              />
            </template>
            侧边栏彩色图标
          </a-list-item>
        </a-list>
      </div>

      <!-- 系统配置 -->
      <div class="setting-drawer-block">
        <a-divider orientation="left">系统配置</a-divider>
        <a-list :split="false">
          <a-list-item>
            <template #extra>
              <a-input
                  v-model:value="systemConfig.title"
                  placeholder="系统标题"
                  size="small"
                  style="width: 120px"
                  @change="handleSystemConfigChange"
              />
            </template>
            系统标题
          </a-list-item>
          <a-list-item>
            <template #extra>
              <a-input
                  v-model:value="systemConfig.copyright"
                  placeholder="版权信息"
                  size="small"
                  style="width: 120px"
                  @change="handleSystemConfigChange"
              />
            </template>
            版权信息
          </a-list-item>
          <a-list-item>
            <template #extra>
              <a-input
                  v-model:value="systemConfig.version"
                  placeholder="版本号"
                  size="small"
                  style="width: 80px"
                  @change="handleSystemConfigChange"
              />
            </template>
            系统版本
          </a-list-item>
        </a-list>
      </div>

      <!-- 表格样式设置 -->
      <div class="setting-drawer-block">
        <a-divider orientation="left">表格通用样式</a-divider>
        <a-list :split="false">
          <a-list-item>
            <template #extra>
              <a-select
                  v-model:value="tableConfig.size"
                  size="small"
                  style="width: 80px"
                  @change="handleTableConfigChange"
              >
                <a-select-option value="default">默认</a-select-option>
                <a-select-option value="middle">中等</a-select-option>
                <a-select-option value="small">紧凑</a-select-option>
              </a-select>
            </template>
            表格大小
          </a-list-item>
          <a-list-item>
            <template #extra>
              <a-switch
                  v-model:checked="tableConfig.bordered"
                  size="small"
                  @change="handleTableConfigChange"
              />
            </template>
            显示边框
          </a-list-item>
          <a-list-item>
            <template #extra>
              <a-switch
                  v-model:checked="tableConfig.fixHeader"
                  size="small"
                  @change="handleTableConfigChange"
              />
            </template>
            固定表头
          </a-list-item>
          <a-list-item>
            <template #extra>
              <a-switch
                  v-model:checked="tableConfig.headerBold"
                  size="small"
                  @change="handleTableConfigChange"
              />
            </template>
            表头加粗
          </a-list-item>
          <a-list-item>
            <template #extra>
              <a-switch
                  v-model:checked="tableConfig.resizable"
                  size="small"
                  @change="handleTableConfigChange"
              />
            </template>
            允许调整列宽
          </a-list-item>
          <a-list-item>
            <template #extra>
              <a-switch
                  v-model:checked="tableConfig.rowSelection"
                  size="small"
                  @change="handleTableConfigChange"
              />
            </template>
            允许行选择
          </a-list-item>
          <a-list-item>
            <template #extra>
              <input
                  v-model="tableConfig.headerBgColor"
                  type="color"
                  style="width: 40px; height: 24px; border: 1px solid #d9d9d9; padding: 0; cursor: pointer"
                  @change="handleTableConfigChange"
              />
            </template>
            表头背景色
          </a-list-item>
          <a-list-item v-if="tableConfig.fixHeader">
            <template #extra>
              <a-input-number
                  v-model:value="tableConfig.tableHeight"
                  :min="100"
                  :max="2000"
                  :step="50"
                  size="small"
                  style="width: 80px"
                  @change="handleTableConfigChange"
              />
            </template>
            表格高度
          </a-list-item>
          <a-list-item>
            <template #extra>
              <a-select
                  v-model:value="tableConfig.pageSize"
                  size="small"
                  style="width: 80px"
                  @change="handleTableConfigChange"
              >
                <a-select-option v-for="size in tableConfig.pageSizeOptions" :key="size" :value="size">{{
                    size
                  }}条/页
                </a-select-option>
              </a-select>
            </template>
            分页大小
          </a-list-item>
          <a-list-item>
            <template #extra>
              <a-switch
                  v-model:checked="tableConfig.showTotal"
                  size="small"
                  @change="handleTableConfigChange"
              />
            </template>
            显示数据总数
          </a-list-item>
          <a-list-item>
            <template #extra>
              <a-switch
                  v-model:checked="tableConfig.showSizeChanger"
                  size="small"
                  @change="handleTableConfigChange"
              />
            </template>
            显示分页选择器
          </a-list-item>
          <a-list-item>
            <template #extra>
              <a-switch
                  v-model:checked="tableConfig.showQuickJumper"
                  size="small"
                  @change="handleTableConfigChange"
              />
            </template>
            显示快速跳转
          </a-list-item>
          <a-list-item>
            <template #extra>
              <a-switch
                  v-model:checked="tableConfig.stripe"
                  size="small"
                  @change="handleTableConfigChange"
              />
            </template>
            斑马条纹
          </a-list-item>
          <a-list-item>
            <template #extra>
              <a-switch
                  v-model:checked="tableConfig.showHeader"
                  size="small"
                  @change="handleTableConfigChange"
              />
            </template>
            显示表头
          </a-list-item>
        </a-list>

        <!-- 添加保存按钮 -->
        <div class="setting-drawer-block-buttons">
          <a-button type="primary" @click="saveTableConfig">
            保存表格配置
          </a-button>
          <a-button @click="resetTableConfig">
            重置
          </a-button>
        </div>
      </div>

      <!-- 配置管理 -->
      <div class="setting-drawer-block">
        <a-divider orientation="left">配置管理</a-divider>
        <div class="setting-drawer-block-buttons">
          <a-button style="width: 100%" type="primary" @click="saveAllSettings">
            <save-outlined/>
            保存所有配置
          </a-button>
          <a-button style="width: 100%" @click="exportSettings">
            <download-outlined/>
            导出配置
          </a-button>
          <a-upload
              :before-upload="importSettings"
              :show-upload-list="false"
              accept=".json"
              style="width: 100%"
          >
            <a-button style="width: 100%">
              <upload-outlined/>
              导入配置
            </a-button>
          </a-upload>
          <a-button danger style="width: 100%" @click="resetAllSettings">
            <redo-outlined/>
            重置所有配置
          </a-button>
        </div>
      </div>
    </div>
  </a-drawer>
</template>

<script setup>
import {nextTick, onMounted, ref, watch, computed} from 'vue';
import {CheckOutlined, DownloadOutlined, RedoOutlined, SaveOutlined, UploadOutlined} from '@ant-design/icons-vue';
import {useTableConfigStore} from '@/stores/tableConfig';
import {useAppStore} from '@/stores/app';
import {message, Modal} from 'ant-design-vue';
import { useThemeComposition } from '@/utils/theme';
import { SYSTEM_INFO } from '@/config/system.config';

// 初始化 stores
let tableConfigStore = null;
let appStore = null;

// 安全初始化 stores
try {
  tableConfigStore = useTableConfigStore();
  appStore = useAppStore();
} catch (error) {
  console.error('Store initialization error:', error);
  message.error('系统初始化失败，请刷新页面重试');
}

// 从 localStorage 获取已保存的配置，如果没有则使用默认值
const savedConfig = (() => {
  try {
    return JSON.parse(localStorage.getItem('table-config') || '{}');
  } catch (error) {
    console.warn('Failed to parse saved config:', error);
    return {};
  }
})();

// 系统配置
const systemConfig = ref({
  title: appStore?.systemConfig?.title || SYSTEM_INFO.name,
  copyright: appStore?.systemConfig?.copyright || SYSTEM_INFO.copyright,
  version: appStore?.systemConfig?.version || SYSTEM_INFO.version,
  description: appStore?.systemConfig?.description || SYSTEM_INFO.description,
  keywords: appStore?.systemConfig?.keywords || SYSTEM_INFO.keywords
});

const tableConfig = ref({
  size: savedConfig.size || tableConfigStore?.size || 'middle',
  bordered: savedConfig.bordered ?? tableConfigStore?.bordered ?? true,
  showHeader: savedConfig.showHeader ?? tableConfigStore?.showHeader ?? true,
  headerBold: savedConfig.headerBold ?? tableConfigStore?.headerBold ?? true,
  stripe: savedConfig.stripe ?? tableConfigStore?.stripe ?? false,
  headerBgColor: savedConfig.headerBgColor || tableConfigStore?.headerBgColor || '#f5f7fa',
  loading: savedConfig.loading ?? tableConfigStore?.loading ?? false,
  fixHeader: savedConfig.fixHeader ?? tableConfigStore?.fixHeader ?? false,
  tableHeight: savedConfig.tableHeight ?? tableConfigStore?.tableHeight ?? 400,
  resizable: savedConfig.resizable ?? tableConfigStore?.resizable ?? false,
  rowSelection: savedConfig.rowSelection ?? tableConfigStore?.rowSelection ?? true,
  pageSize: savedConfig.pageSize || tableConfigStore?.pageSize || 10,
  pageSizeOptions: savedConfig.pageSizeOptions || tableConfigStore?.pageSizeOptions || ['10', '20', '50', '100'],
  showTotal: savedConfig.showTotal ?? tableConfigStore?.showTotal ?? true,
  showSizeChanger: savedConfig.showSizeChanger ?? tableConfigStore?.showSizeChanger ?? true,
  showQuickJumper: savedConfig.showQuickJumper ?? tableConfigStore?.showQuickJumper ?? true
});

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  settings: {
    type: Object,
    required: true
  }
});

const emit = defineEmits(['update:visible', 'update:settings']);

const localSettings = ref({...props.settings});

// 组件初始化时同步配置到store
onMounted(async () => {
  try {
    await nextTick();

    // 确保 store 已初始化
    if (!tableConfigStore || !appStore) {
      console.error('Stores not initialized');
      return;
    }

    // 确保初始配置同步到store
    tableConfigStore.updateConfig(tableConfig.value);
    tableConfigStore.updatePagination({
      pageSize: tableConfig.value.pageSize,
      showSizeChanger: tableConfig.value.showSizeChanger,
      showQuickJumper: tableConfig.value.showQuickJumper,
      showTotal: tableConfig.value.showTotal
    });
  } catch (error) {
    console.error('Error in onMounted:', error);
    message.error('配置初始化失败');
  }
});

// 使用watch来同步更新本地存储和store
watch(tableConfig, (newConfig) => {
  try {
    localStorage.setItem('table-config', JSON.stringify(newConfig));

    // 更新store中的配置
    if (tableConfigStore) {
      tableConfigStore.updateConfig(newConfig);

      // 同时更新分页配置
      tableConfigStore.updatePagination({
        pageSize: newConfig.pageSize,
        showSizeChanger: newConfig.showSizeChanger,
        showQuickJumper: newConfig.showQuickJumper,
        showTotal: newConfig.showTotal
      });
    }
  } catch (error) {
    console.error('Error updating table config:', error);
  }
}, {deep: true});

// 使用主题组合式函数
const {
  presetColors,
  setThemeMode,
  setPrimaryColor,
  setColorWeak,
  resetTheme: resetThemeConfig
} = useThemeComposition();

// 使用主题服务提供的预设颜色
const colorList = computed(() =>
  presetColors.value.map(item => ({
    color: item.color,
    key: item.name
  }))
);

// 监听外部设置变化
watch(() => props.settings, (newSettings) => {
  localSettings.value = {...newSettings};
}, {deep: true});

// 监听系统配置变化
watch(() => appStore?.systemConfig, (newConfig) => {
  if (newConfig) {
    systemConfig.value = {...newConfig};
  }
}, {deep: true});

// 处理主题切换（使用新的主题服务）
const handleThemeChange = (theme) => {
  try {
    // 使用主题服务设置主题模式
    setThemeMode(theme);

    localSettings.value = {
      ...localSettings.value,
      darkMode: theme === 'dark',
      primaryColor: localSettings.value.primaryColor
    };
    handleSettingChange();
  } catch (error) {
    console.error('Error changing theme:', error);
    message.error('主题切换失败');
  }
};

// 处理主题色切换（使用新的主题服务）
const handleColorChange = (color) => {
  try {
    // 使用主题服务设置主题色
    setPrimaryColor(color);

    localSettings.value = {
      ...localSettings.value,
      primaryColor: color
    };
    handleSettingChange();
  } catch (error) {
    console.error('Error changing color:', error);
    message.error('主题色切换失败');
  }
};

// 处理导航模式切换
const handleNavModeChange = (navMode) => {
  try {
    console.log('切换导航模式:', navMode);
    localSettings.value.navMode = navMode;

    // 切换为顶部菜单时，自动取消固定侧边栏
    if (navMode === 'top') {
      localSettings.value.fixedSidebar = false;
    }

    handleSettingChange();
    message.success(`已切换到${navMode === 'side' ? '侧边栏' : navMode === 'top' ? '顶部菜单' : '混合'}模式`);
  } catch (error) {
    console.error('Error changing nav mode:', error);
    message.error('导航模式切换失败');
  }
};

// 处理布局切换
const handleLayoutChange = (layout) => {
  try {
    localSettings.value.layout = layout;
    // 切换为顶部菜单时，自动取消固定侧边栏
    if (layout === 'top') {
      localSettings.value.fixedSidebar = false;
    }
    handleSettingChange();
  } catch (error) {
    console.error('Error changing layout:', error);
    message.error('布局切换失败');
  }
};

// 处理设置变更
const handleSettingChange = () => {
  try {
    emit('update:settings', {...localSettings.value});
  } catch (error) {
    console.error('Error updating settings:', error);
    message.error('设置更新失败');
  }
};

// 处理表格配置变更
const handleTableConfigChange = () => {
  try {
    // 仅在内存中更新配置
    tableConfig.value = {...tableConfig.value};
  } catch (error) {
    console.error('Error updating table config:', error);
    message.error('表格配置更新失败');
  }
};

// 保存表格配置
const saveTableConfig = () => {
  try {
    if (!tableConfigStore) {
      message.error('表格配置存储未初始化');
      return;
    }

    // 更新 store
    tableConfigStore.updateConfig(tableConfig.value);
    // 保存到 localStorage
    localStorage.setItem('table-config', JSON.stringify(tableConfig.value));
    message.success('表格配置已保存');
  } catch (error) {
    console.error('Error saving table config:', error);
    message.error('表格配置保存失败');
  }
};

// 重置表格配置
const resetTableConfig = () => {
  try {
    // 重置store配置
    if (tableConfigStore && typeof tableConfigStore.resetConfig === 'function') {
      tableConfigStore.resetConfig();
    }

    // 重置本地配置
    const defaultConfig = {
      size: 'middle',
      bordered: true,
      showHeader: true,
      headerBold: true,
      stripe: false,
      headerBgColor: '#f5f7fa',
      loading: false,
      fixHeader: false,
      tableHeight: 400,
      resizable: false,
      rowSelection: true,
      pageSize: 10,
      pageSizeOptions: ['10', '20', '50', '100'],
      showTotal: true,
      showSizeChanger: true,
      showQuickJumper: true
    };

    tableConfig.value = defaultConfig;

    // 同步更新分页配置到store
    if (tableConfigStore && typeof tableConfigStore.updatePagination === 'function') {
      tableConfigStore.updatePagination({
        pageSize: defaultConfig.pageSize,
        showSizeChanger: defaultConfig.showSizeChanger,
        showQuickJumper: defaultConfig.showQuickJumper,
        showTotal: defaultConfig.showTotal
      });
    }

    localStorage.setItem('table-config', JSON.stringify(defaultConfig));
    message.success('表格配置已重置');
  } catch (error) {
    console.error('Error resetting table config:', error);
    message.error('表格配置重置失败');
  }
};

// 处理系统配置变更
const handleSystemConfigChange = () => {
  try {
    if (appStore && typeof appStore.updateSystemConfig === 'function') {
      appStore.updateSystemConfig(systemConfig.value);
      message.success('系统配置已更新');
    } else {
      message.error('系统配置更新失败：Store 未初始化');
    }
  } catch (error) {
    console.error('Error updating system config:', error);
    message.error('系统配置更新失败');
  }
};

// 保存所有设置
const saveAllSettings = () => {
  try {
    // 保存布局设置
    emit('update:settings', {...localSettings.value});

    // 保存系统配置
    if (appStore && typeof appStore.updateSystemConfig === 'function') {
      appStore.updateSystemConfig(systemConfig.value);
    }

    // 保存表格配置
    if (tableConfigStore && typeof tableConfigStore.updateConfig === 'function') {
      tableConfigStore.updateConfig(tableConfig.value);
    }

    message.success('所有配置已保存');
  } catch (error) {
    console.error('Error saving all settings:', error);
    message.error('配置保存失败');
  }
};

// 导出设置
const exportSettings = () => {
  try {
    if (appStore && typeof appStore.exportSettings === 'function') {
      appStore.exportSettings();
      message.success('配置已导出');
    } else {
      // 如果 store 方法不存在，手动导出
      const config = {
        layoutSettings: localSettings.value,
        systemConfig: systemConfig.value,
        tableConfig: tableConfig.value
      };

      const dataStr = JSON.stringify(config, null, 2);
      const dataBlob = new Blob([dataStr], {type: 'application/json'});
      const url = URL.createObjectURL(dataBlob);
      const link = document.createElement('a');
      link.href = url;
      link.download = 'bearjia-admin-config.json';
      link.click();
      URL.revokeObjectURL(url);

      message.success('配置已导出');
    }
  } catch (error) {
    console.error('Error exporting settings:', error);
    message.error('配置导出失败');
  }
};

// 导入设置
const importSettings = (file) => {
  const reader = new FileReader();
  reader.onload = (e) => {
    try {
      const config = JSON.parse(e.target.result);

      Modal.confirm({
        title: '确认导入配置',
        content: '导入配置将覆盖当前所有设置，是否继续？',
        okText: '确认',
        cancelText: '取消',
        onOk() {
          // 导入布局设置
          if (config.layoutSettings) {
            localSettings.value = {...config.layoutSettings};
            emit('update:settings', {...config.layoutSettings});
          }

          // 导入系统配置
          if (config.systemConfig) {
            systemConfig.value = {...config.systemConfig};
            appStore.updateSystemConfig(config.systemConfig);
          }

          // 导入表格配置
          if (config.tableConfig) {
            tableConfig.value = {...config.tableConfig};
            tableConfigStore.updateConfig(config.tableConfig);
          }

          message.success('配置导入成功');
        }
      });
    } catch (error) {
      message.error('配置文件格式错误');
    }
  };
  reader.readAsText(file);
  return false; // 阻止默认上传行为
};

// 重置所有设置（使用新的主题服务）
const resetAllSettings = () => {
  Modal.confirm({
    title: '确认重置',
    content: '重置将清除所有自定义配置，恢复到默认状态，是否继续？',
    okText: '确认',
    cancelText: '取消',
    onOk() {
      // 重置主题（使用主题服务）
      resetThemeConfig();

      // 重置布局设置
      appStore.resetSettings();
      localSettings.value = {...appStore.layoutSettings};

      // 重置系统配置
      const defaultSystemConfig = {
        title: SYSTEM_INFO.name,
        copyright: SYSTEM_INFO.copyright,
        version: SYSTEM_INFO.version,
        description: SYSTEM_INFO.description,
        keywords: SYSTEM_INFO.keywords
      };
      systemConfig.value = defaultSystemConfig;
      appStore.updateSystemConfig(defaultSystemConfig);

      // 重置表格配置
      resetTableConfig();

      message.success('所有配置已重置');
    }
  });
};

</script>

<style lang="less" scoped>
.setting-drawer {
  &-content {
    padding: 12px;
  }

  &-block {
    margin-bottom: 24px;

    h3 {
      margin-bottom: 12px;
      font-size: 14px;
      color: var(--text-color, rgba(0, 0, 0, 0.85));
    }

    &-checbox {
      display: flex;
      flex-wrap: wrap;
      gap: 12px;
      justify-content: space-between;
      margin: 0 -2px; /* 补偿边距 */

      &-item {
        position: relative;
        cursor: pointer;
        border-radius: 8px;
        overflow: hidden;
        border: 2px solid rgba(0, 0, 0, 0.06);
        transition: all 0.3s ease;
        width: 60px;
        height: 60px;
        flex: 0 0 calc(33.333% - 8px); /* 严格控制每行3个 */
        max-width: 60px;
        background: var(--component-background);
        display: flex;
        align-items: center;
        justify-content: center;

        &:hover {
          transform: scale(1.08);
          border-color: rgba(24, 144, 255, 0.3);
          box-shadow: 0 2px 8px rgba(24, 144, 255, 0.15);
          background: rgba(24, 144, 255, 0.02);
        }

        &.active {
          border-color: var(--primary-color);
          background: rgba(24, 144, 255, 0.05);
          box-shadow: 0 0 0 1px var(--primary-color), 0 2px 8px rgba(24, 144, 255, 0.2);
        }

        img {
          width: 42px;
          height: 42px;
          display: block;
          transition: all 0.3s ease;
        }

        .setting-drawer-theme-color-icon {
          position: absolute;
          top: 2px;
          right: 2px;
          width: 18px;
          height: 18px;
          color: var(--primary-color, #1890ff);
          font-size: 12px;
          font-weight: bold;
          background: rgba(255, 255, 255, 0.95);
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
        }
      }
    }
  }

  &-theme-color {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;

    &-block {
      width: 20px;
      height: 20px;
      border-radius: 2px;
      cursor: pointer;
      display: flex;
      align-items: center;
      justify-content: center;
      color: #fff;
    }
  }
}

:deep(.ant-list-item) {
  padding: 12px 0;
  justify-content: space-between;
  color: var(--text-color, rgba(0, 0, 0, 0.85));
  white-space: nowrap;
}

:deep(.ant-radio-group) {
  display: flex;
}

.setting-drawer-block-buttons {
  margin-top: 24px;
  display: flex;
  flex-direction: column;
  gap: 8px;
  justify-content: center;

  &:not(:last-child) {
    flex-direction: row;
  }
}

// 暗色主题下的样式已移至 global.less 统一管理，此处仅保留组件特有逻辑
</style>
