<template>
  <div>
    <a-modal
        v-model:open="pageData.visible"
        width="60%"
        :title="pageData.title"
        :destroy-on-close="true"
        @ok="clickModalOk"
        @cancel="handleModalCancel"
    >
      <a-form
          ref="menuAddUpdateFormRef"
          name="menuAddUpdateForm"
          :model="menuAddUpdateForm.data"
          :label-col="{ span: 8 }"
          :wrapper-col="{ span: 14 }"
      >
        <a-row :gutter="24">
          <a-col span="12">
            <a-form-item
                name="menuName"
                label="菜单名称"
                :rules="[{ required: true, message: '菜单名称不能为空！' }]"
            >
              <a-input
                  v-model:value="menuAddUpdateForm.data.menuName"
                  :maxlength="30"
                  placeholder="请填写菜单名称"
              />
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item
                name="parentId"
                label="上级菜单"
                :rules="[{ required: true, message: '上级菜单不能为空！' }]"
            >
              <a-tree-select
                  v-model:value="menuAddUpdateForm.data.parentId"
                  placeholder="请选择上级菜单"
                  :tree-data="pageData.menusDict"
                  :field-names="{ children: 'children', label: 'menuName', key: 'menuId', value: 'menuId' }"
              />
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item
                name="orderNum"
                label="显示顺序"
                :rules="[{ required: true, message: '显示顺序不能为空！' }]"
            >
              <a-input
                  v-model:value="menuAddUpdateForm.data.orderNum"
                  :maxlength="30"
                  placeholder="请填写显示顺序"
              />
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="path" label="路由地址" :rules="[{}]">
              <a-input
                  v-model:value="menuAddUpdateForm.data.path"
                  :maxlength="30"
                  placeholder="请填写路由地址"
              />
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="component" label="组件路径" :rules="[{}]">
              <a-input
                  v-model:value="menuAddUpdateForm.data.component"
                  :maxlength="30"
                  placeholder="请填写组件路径"
              />
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="query" label="路由参数" :rules="[{}]">
              <a-input
                  v-model:value="menuAddUpdateForm.data.query"
                  :maxlength="30"
                  placeholder="请填写路由参数"
              />
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item
                name="isFrame"
                label="是否为外链"
                :rules="[{ required: true, message: '是否为外链不能为空！' }]"
            >
              <a-radio-group
                  v-model:value="menuAddUpdateForm.data.isFrame"
                  :options="fatherPageData.sysYesNoDict"
                  option-type="button"
                  button-style="solid"
              />
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item
                name="menuType"
                label="菜单类型"
                :rules="[{ required: true, message: '菜单类型不能为空！' }]"
            >
              <a-select
                  v-model:value="menuAddUpdateForm.data.menuType"
                  :options="fatherPageData.menuTypeDict"
                  placeholder="请选择菜单类型"
              />
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item
                name="visible"
                label="是否显示"
                :rules="[{ required: true, message: '是否显示不能为空！' }]"
            >
              <a-radio-group
                  v-model:value="menuAddUpdateForm.data.visible"
                  :options="fatherPageData.sysYesNoDict"
                  option-type="button"
                  button-style="solid"
              />
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item
                name="status"
                label="菜单状态"
                :rules="[{ required: true, message: '菜单状态不能为空！' }]"
            >
              <a-radio-group
                  v-model:value="menuAddUpdateForm.data.status"
                  :options="fatherPageData.sysNormalDisableDict"
                  option-type="button"
                  button-style="solid"
              />
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="perms" label="权限标识" :rules="[{}]">
              <a-input
                  v-model:value="menuAddUpdateForm.data.perms"
                  :maxlength="30"
                  placeholder="请填写权限标识"
              />
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="icon" label="菜单图标" :rules="[{}]">
              <a-space size="large">
                <!-- 显示自定义图标 -->
                <svg-icon
                    v-if="currentCustomIcon"
                    :icon-class="currentCustomIcon"
                    :size="20"
                    color="#333"
                />
                <!-- 显示 Ant Design 图标 -->
                <component
                    v-else-if="currentAntIconComponent"
                    :is="currentAntIconComponent"
                    style="font-size: 20px;"
                />
                <a-button type="dashed" @click="showIconModal">
                  选择图标
                </a-button>
              </a-space>
            </a-form-item>
          </a-col>
          <a-col span="12">
            <a-form-item name="remark" label="备注" :rules="[{}]">
              <a-textarea
                  v-model:value="menuAddUpdateForm.data.remark"
                  placeholder="请填写备注"
                  :rows="3"
              />
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-modal>

    <!-- 图标选择模态框 -->
    <a-modal
        v-model:open="iconModalVisible"
        title="选择图标"
        :width="900"
        :footer="null"
        :destroy-on-close="true"
    >
      <a-input-search
          v-model:value="iconSearch"
          placeholder="搜索图标"
          style="margin-bottom: 16px;"
          allow-clear
          @search="filterIcons"
      />
      <a-tabs v-model:activeKey="currentIconTab" v-if="!iconSearch">
        <!-- 自定义图标标签页 -->
        <a-tab-pane key="custom" tab="自定义图标">
          <div class="icon-list">
            <a-tooltip
                v-for="icon in customIcons"
                :key="icon"
                :title="icon"
            >
              <svg-icon
                  :icon-class="icon"
                  class="icon-item custom-icon-item"
                  :class="{ 'icon-selected': isSelectedIcon(icon, 'custom') }"
                  :size="40"
                  @click="selectIcon(icon, 'custom')"
              />
            </a-tooltip>
          </div>
        </a-tab-pane>
        <!-- Ant Design 图标分类标签页 -->
        <a-tab-pane
            v-for="category in iconCategories"
            :key="category.key"
            :tab="category.title"
        >
          <div class="icon-list">
            <a-tooltip
                v-for="icon in category.icons"
                :key="icon"
                :title="icon"
            >
              <component
                  :is="getIconComponent(icon)"
                  v-if="getIconComponent(icon)"
                  class="icon-item"
                  :class="{ 'icon-selected': isSelectedIcon(icon, 'ant') }"
                  @click="selectIcon(icon, 'ant')"
              />
            </a-tooltip>
          </div>
        </a-tab-pane>
      </a-tabs>
      <div class="icon-list" v-else>
        <!-- 搜索结果：自定义图标 -->
        <a-tooltip
            v-for="icon in filteredCustomIcons"
            :key="icon"
            :title="icon + ' (自定义)'"
        >
          <svg-icon
              :icon-class="icon"
              class="icon-item custom-icon-item"
              :class="{ 'icon-selected': isSelectedIcon(icon, 'custom') }"
              :size="48"
              @click="selectIcon(icon, 'custom')"
          />
        </a-tooltip>
        <!-- 搜索结果：Ant Design 图标 -->
        <a-tooltip
            v-for="icon in filteredAntIcons"
            :key="icon"
            :title="icon"
        >
          <component
              :is="getIconComponent(icon)"
              v-if="getIconComponent(icon)"
              class="icon-item"
              :class="{ 'icon-selected': isSelectedIcon(icon, 'ant') }"
              @click="selectIcon(icon, 'ant')"
          />
        </a-tooltip>
      </div>
    </a-modal>
  </div>
</template>

<script setup>
import { reactive, ref, computed } from 'vue';
import BearJiaUtil from '@/utils/BearJiaUtil.js';
import { listMenu as getMenuTreeselect, getMenu, addMenu, updateMenu } from '@/api/system/menu';
import { handleTree } from '@/utils/bearjia.js';
import * as allIcons from '@ant-design/icons-vue'; // 导入所有 Ant Design 图标
import iconCategoriesData from '@/components/IconSelector/icons.js'; // 导入图标分类数据
import SvgIcon from '@/components/SvgIcon/index.vue'; // 导入 SvgIcon 组件

// 父页面公用数据
const fatherPageData = defineProps({
  sysYesNoDict: Array,
  sysShowHideDict: Array,
  menuTypeDict: Array,
  sysNormalDisableDict: Array,
});

const emit = defineEmits(['refreshFatherPageTable']);

// 当前页面使用的数据
const pageData = reactive({
  title: '新增页面',
  visible: false,
  operateType: '',
  menusDict: [],
});

// 新增修改表单
const menuAddUpdateFormRef = ref();
const menuAddUpdateForm = reactive({
  data: { isFrame: '1', visible: '0', status: '0' },
});

// 重置表单
const resetMenuAddUpdateForm = () => {
  BearJiaUtil.resetFormFieldsToEmpty(menuAddUpdateForm.data);
};

// 图标选择相关
const iconModalVisible = ref(false);
const iconSearch = ref('');
const currentIconTab = ref('custom'); // 默认显示自定义图标

const ANT_ICON_THEME_SUFFIX_REG = /(Outlined|Filled|TwoTone)$/;
const customIconAliasMap = {
  'tree-table': 'treeTable',
  'redis-list': 'redis',
  code: 'codeNew',
  logininfor: 'loginLog',
};

// 动态扫描 src/assets/icons 文件夹获取自定义 SVG 图标列表
const customIcons = (() => {
  const iconModules = import.meta.glob('@/assets/icons/*.svg');
  return Object.keys(iconModules).map((path) => {
    const fileName = path.split('/').pop().replace('.svg', '');
    return fileName;
  }).sort((a, b) => a.localeCompare(b));
})();
const customIconSet = new Set(customIcons);

const toPascalCase = (value) =>
  value
    .split('-')
    .filter(Boolean)
    .map((word) => word.charAt(0).toUpperCase() + word.slice(1))
    .join('');

const toCamelCase = (value) =>
  value
    .split('-')
    .filter(Boolean)
    .map((word, index) => (index === 0 ? word : word.charAt(0).toUpperCase() + word.slice(1)))
    .join('');

const normalizeSearchKeyword = (value) => String(value || '').toLowerCase().replace(/[-_\s]/g, '');

const antIconToKebab = (iconName) =>
  iconName
    .replace(/([a-z0-9])([A-Z])/g, '$1-$2')
    .replace(/-outlined$/i, '')
    .replace(/-filled$/i, '')
    .replace(/-two-tone$/i, '')
    .toLowerCase();

const resolveCustomIconName = (iconName) => {
  if (!iconName || iconName === '#') return null;
  const raw = String(iconName).trim();
  if (!raw) return null;

  const candidates = [raw];
  const alias = customIconAliasMap[raw];
  if (alias) {
    candidates.push(alias);
  }
  if (raw.includes('-')) {
    const camel = toCamelCase(raw);
    candidates.push(camel);
    const camelAlias = customIconAliasMap[camel];
    if (camelAlias) {
      candidates.push(camelAlias);
    }
  }

  for (const candidate of candidates) {
    if (customIconSet.has(candidate)) {
      return candidate;
    }
  }
  return null;
};

const resolveAntIconKey = (iconName) => {
  if (!iconName || iconName === '#') return null;
  const raw = String(iconName).trim();
  if (!raw) return null;

  const candidates = [];
  const addCandidate = (name) => {
    if (name && !candidates.includes(name)) {
      candidates.push(name);
    }
  };

  addCandidate(raw);
  if (raw.includes('-')) {
    const pascal = toPascalCase(raw);
    addCandidate(pascal);
    if (!ANT_ICON_THEME_SUFFIX_REG.test(pascal)) {
      addCandidate(`${pascal}Outlined`);
    }
  } else {
    const pascal = raw.charAt(0).toUpperCase() + raw.slice(1);
    addCandidate(pascal);
    if (!ANT_ICON_THEME_SUFFIX_REG.test(raw)) {
      addCandidate(`${pascal}Outlined`);
    }

    if (/outlined$/i.test(raw) || /filled$/i.test(raw) || /twotone$/i.test(raw)) {
      addCandidate(
        pascal
          .replace(/outlined$/i, 'Outlined')
          .replace(/filled$/i, 'Filled')
          .replace(/twotone$/i, 'TwoTone')
      );
    }
  }

  for (const candidate of candidates) {
    if (allIcons[candidate]) {
      return candidate;
    }
  }
  return null;
};

// 根据图标名称获取对应的 Ant Design 图标组件
const getIconComponent = (iconName) => {
  const iconKey = resolveAntIconKey(iconName);
  return iconKey ? allIcons[iconKey] : null;
};

// 直接使用 icons.js 中的分类数据（保持 kebab-case 格式）
const iconCategories = iconCategoriesData;

// 只获取真正的图标组件（排除非组件导出）
const iconList = Object.keys(allIcons)
  .filter((key) => ANT_ICON_THEME_SUFFIX_REG.test(key))
  .sort((a, b) => a.localeCompare(b));
const filteredAntIcons = ref(iconList);
const filteredCustomIcons = ref(customIcons);

const currentCustomIcon = computed(() => resolveCustomIconName(menuAddUpdateForm.data.icon));
const currentAntIconComponent = computed(() => {
  if (currentCustomIcon.value) {
    return null;
  }
  return getIconComponent(menuAddUpdateForm.data.icon);
});

const showIconModal = () => {
  iconModalVisible.value = true;
  iconSearch.value = '';
  filteredAntIcons.value = iconList; // 重置为完整列表
  filteredCustomIcons.value = customIcons;
};

const filterIcons = () => {
  const searchValue = normalizeSearchKeyword(iconSearch.value);
  if (!searchValue) {
    filteredAntIcons.value = iconList;
    filteredCustomIcons.value = customIcons;
    return;
  }

  // 过滤 Ant Design 图标
  filteredAntIcons.value = iconList.filter((icon) =>
    normalizeSearchKeyword(icon).includes(searchValue) ||
    normalizeSearchKeyword(antIconToKebab(icon)).includes(searchValue)
  );
  // 过滤自定义图标
  filteredCustomIcons.value = customIcons.filter((icon) =>
    normalizeSearchKeyword(icon).includes(searchValue)
  );
};

const selectIcon = (icon, source = 'custom') => {
  if (source === 'ant') {
    menuAddUpdateForm.data.icon = resolveAntIconKey(icon) || icon;
  } else {
    menuAddUpdateForm.data.icon = resolveCustomIconName(icon) || icon;
  }
  iconModalVisible.value = false;
  iconSearch.value = ''; // 清空搜索
};

// 判断是否为自定义图标
const isCustomIcon = (iconName) => {
  return !!resolveCustomIconName(iconName);
};

// 判断图标是否已选中（处理 custom / ant 两套命名）
const isSelectedIcon = (iconName, source) => {
  const currentIcon = menuAddUpdateForm.data.icon;
  if (source === 'custom') {
    const currentCustom = resolveCustomIconName(currentIcon);
    const targetCustom = resolveCustomIconName(iconName);
    return !!currentCustom && currentCustom === targetCustom;
  }

  if (isCustomIcon(currentIcon)) {
    return false;
  }

  const currentAnt = resolveAntIconKey(currentIcon);
  const targetAnt = resolveAntIconKey(iconName);
  return !!currentAnt && currentAnt === targetAnt;
};

// 打开新增窗口
const openAddModal = () => {
  menuAddUpdateForm.data.isFrame = '1';
  menuAddUpdateForm.data.visible = '0';
  menuAddUpdateForm.data.status = '0';
  pageData.operateType = 'add';
  pageData.title = '新增菜单';
  getMenuTreeselect().then((response) => {
    pageData.menusDict = handleTree(response.data, 'menuId');
    pageData.menusDict.push({ menuId: 0, menuName: '无' });
    pageData.visible = true;
  });
};

// 打开修改窗口
const openUpdateModal = (record) => {
  pageData.operateType = 'update';
  pageData.title = '修改菜单';
  getMenu(record.menuId).then((response) => {
    menuAddUpdateForm.data = response.data;
    getMenuTreeselect().then((response) => {
      pageData.menusDict = handleTree(response.data, 'menuId');
      pageData.menusDict.push({ menuId: 0, menuName: '无' });
      pageData.visible = true;
    });
  });
};

// 点击窗口确认
const clickModalOk = () => {
  menuAddUpdateFormRef.value
      .validate()
      .then(() => {
        if (pageData.operateType === 'add') {
          addMenu(menuAddUpdateForm.data).then(() => {
            pageData.visible = false;
            resetMenuAddUpdateForm();
            emit('refreshFatherPageTable');
            BearJiaUtil.messageSuccess('新增操作成功。');
          });
        } else if (pageData.operateType === 'update') {
          updateMenu(menuAddUpdateForm.data).then(() => {
            pageData.visible = false;
            resetMenuAddUpdateForm();
            emit('refreshFatherPageTable');
            BearJiaUtil.messageSuccess('修改操作成功。');
          });
        }
      })
      .catch((info) => {
        console.log('Validate Failed:', info);
      });
};

// 点击窗口取消
const handleModalCancel = () => {
  resetMenuAddUpdateForm();
  pageData.visible = false;
};

// 暴露方法
defineExpose({
  openAddModal,
  openUpdateModal,
});
</script>

<style lang="less" scoped>
.icon-list {
  max-height: 400px;
  overflow-y: auto;
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  padding: 8px;
}

.icon-item {
  font-size: 24px;
  cursor: pointer;
  padding: 8px;
  transition: all 0.3s;
  border-radius: 4px;

  &:hover {
    color: #1890ff;
    background: #f0f0f0;
  }

  &.icon-selected {
    color: #fff;
    background: #1890ff;
  }
}

.custom-icon-item {
  width: 40px!important;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #333;

  &.icon-selected {
    color: #fff;
  }

  &:hover:not(.icon-selected) {
    color: #1890ff;
  }
}

:deep(.ant-tabs-content) {
  height: auto;
}
</style>
