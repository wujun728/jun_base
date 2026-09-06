// ✅ TableActionBar - 表格操作列组件（支持智能自适应、查看、编辑、删除、自定义操作、下拉菜单）
<template>
  <span class="table-action-bar">
    <a-space :size="4">
      <template #split>
        <a-divider type="vertical" />
      </template>
      <!-- 新方式：使用 actions 数组 -->
      <template v-if="isUsingActionsArray">
        <!-- 显示的操作按钮 -->
        <template v-for="(action, index) in visibleActions" :key="`action-${index}`">
          <a
            v-hasPermi="action.permissions"
            :class="[
              'action-btn',
              action.type === 'view' ? 'view-btn' :
              action.type === 'edit' ? 'edit-btn' :
              action.type === 'delete' ? 'delete-btn' :
              action.danger ? 'delete-btn' : 'primary-btn'
            ]"
            @click="handleActionClick(action)"
          >
            <BearJiaIcon v-if="action.icon" :icon="action.icon" />
            {{ action.label }}
          </a>
        </template>

        <!-- 更多操作下拉菜单 -->
        <a-dropdown v-if="hasDropdownActions" :trigger="['click']">
          <a class="action-btn info-btn">
            更多
            <DownOutlined />
          </a>
          <template #overlay>
            <a-menu @click="handleDropdownClick">
              <a-menu-item
                v-for="(action, index) in dropdownActions"
                :key="index"
                :disabled="action.disabled"
                :danger="action.danger"
              >
                <BearJiaIcon v-if="action.icon" :icon="action.icon" class="menu-icon" />
                {{ action.label }}
              </a-menu-item>
            </a-menu>
          </template>
        </a-dropdown>
      </template>

      <!-- 旧方式：使用 view/edit/delete（向后兼容） -->
      <template v-else>
        <!-- 查看按钮 -->
        <slot name="view">
          <a
              v-if="viewConfig.show"
              v-hasPermi="viewConfig.permissions"
              class="action-btn view-btn"
              @click="viewConfig.handler"
          >
            <BearJiaIcon :icon="viewConfig.icon"/> {{ viewConfig.text }}
          </a>
        </slot>

        <!-- 编辑按钮 -->
        <slot name="edit">
          <a
              v-if="editConfig.show"
              v-hasPermi="editConfig.permissions"
              class="action-btn edit-btn"
              @click="editConfig.handler"
          >
            <BearJiaIcon :icon="editConfig.icon"/> {{ editConfig.text }}
          </a>
        </slot>

        <!-- 删除按钮 -->
        <slot name="delete">
          <a
              v-if="deleteConfig.show"
              v-hasPermi="deleteConfig.permissions"
              class="action-btn delete-btn"
              @click="deleteConfig.handler"
          >
            <BearJiaIcon :icon="deleteConfig.icon"/> {{ deleteConfig.text }}
          </a>
        </slot>

        <!-- 自定义操作按钮插槽 -->
        <slot name="actions" :record="record">
          <!-- 默认插槽内容，用于自定义按钮 -->
        </slot>

        <!-- 默认插槽（向后兼容） -->
        <slot :record="record"></slot>

        <!-- 更多操作下拉菜单（旧方式） -->
        <a-dropdown v-if="hasMoreActions" :trigger="['click']">
          <a class="action-btn info-btn">
            更多
            <DownOutlined />
          </a>
          <template #overlay>
            <a-menu @click="handleMoreActionClick">
              <a-menu-item
                v-for="(action, index) in moreActions"
                :key="index"
                :disabled="action.disabled"
                :danger="action.danger"
              >
                <BearJiaIcon v-if="action.icon" :icon="action.icon" class="menu-icon" />
                {{ action.label }}
              </a-menu-item>
            </a-menu>
          </template>
        </a-dropdown>
      </template>
    </a-space>
  </span>
</template>

<script setup>
import {computed} from 'vue';
import {BearJiaIcon} from '@/utils/BearJiaIcon.js';
import { DownOutlined } from '@ant-design/icons-vue';

const props = defineProps({
  record: Object,
  // 新方式：统一的操作数组配置（推荐使用）
  // 每项格式：{ label, icon, onClick, disabled, danger, permissions, type }
  actions: {
    type: Array,
    default: () => []
  },
  // 是否启用智能自适应模式
  // true: 根据操作数量自动调整显示策略
  // - 2个或更少：全部显示
  // - 3个或更多：显示1个 + "更多"下拉
  autoAdaptive: {
    type: Boolean,
    default: false
  },
  // 新的配置方式: 查看按钮配置（向后兼容）
  view: {
    type: [Object, Boolean],
    default: true
  },
  // 新的配置方式: 编辑按钮配置（向后兼容）
  edit: {
    type: [Object, Boolean],
    default: true
  },
  // 新的配置方式: 删除按钮配置（向后兼容）
  delete: {
    type: [Object, Boolean],
    default: false
  },
  // 更多操作配置（数组）（向后兼容）
  moreActions: {
    type: Array,
    default: () => []
  },
  // 向后兼容的旧参数
  hasView: {type: Boolean, default: undefined},
  hasEdit: {type: Boolean, default: undefined},
  hasDelete: {type: Boolean, default: undefined},
  icons: {type: Object, default: undefined},
  texts: {type: Object, default: undefined},
  permissions: {type: Object, default: undefined}
});

const emit = defineEmits(['view', 'edit', 'delete', 'moreAction', 'action']);

// 计算各按钮配置(需要提前声明,因为后面会用到)
const viewConfig = computed(() => processButtonConfig(
  'view',
  props.view,
  props.hasView,
  props.icons?.view,
  props.texts?.view
));

const editConfig = computed(() => processButtonConfig(
  'edit',
  props.edit,
  props.hasEdit,
  props.icons?.edit,
  props.texts?.edit
));

const deleteConfig = computed(() => processButtonConfig(
  'delete',
  props.delete,
  props.hasDelete,
  props.icons?.delete,
  props.texts?.delete
));

// 使用新的 actions 数组还是旧的配置方式
const isUsingActionsArray = computed(() => {
  return props.actions && props.actions.length > 0;
});

// 用于新方式的 actions 数组
const convertedActions = computed(() => {
  return props.actions || [];
});

// 计算实际要显示的操作数量
const computedVisibleCount = computed(() => {
  if (!isUsingActionsArray.value || !props.autoAdaptive) {
    return convertedActions.value.length; // 非自适应模式，全部显示
  }

  const totalCount = convertedActions.value.length;

  // 智能自适应策略：
  // 2个或更少：全部显示
  // 3个或更多：只显示1个 + "更多"
  if (totalCount <= 2) {
    return totalCount;
  } else {
    return 1;
  }
});

// 显示的操作
const visibleActions = computed(() => {
  if (!isUsingActionsArray.value) return [];
  return convertedActions.value.slice(0, computedVisibleCount.value);
});

// 隐藏在"更多"菜单中的操作
const dropdownActions = computed(() => {
  if (!isUsingActionsArray.value) return [];
  return convertedActions.value.slice(computedVisibleCount.value);
});

// 是否有更多操作（新方式）
const hasDropdownActions = computed(() => {
  return dropdownActions.value.length > 0;
});

// 是否有更多操作（旧方式 - 向后兼容）
const hasMoreActions = computed(() => {
  return props.moreActions && props.moreActions.length > 0;
});

// 处理操作点击（新方式）
const handleActionClick = (action) => {
  if (action.disabled) return;

  if (action.onClick && typeof action.onClick === 'function') {
    action.onClick(props.record);
  } else {
    emit('action', { action, record: props.record });
  }
};

// 处理下拉菜单点击（新方式）
const handleDropdownClick = ({ key }) => {
  const actionIndex = parseInt(key);
  const action = dropdownActions.value[actionIndex];
  if (action) {
    handleActionClick(action);
  }
};

// 处理更多操作点击（旧方式 - 向后兼容）
const handleMoreActionClick = ({ key }) => {
  const actionIndex = parseInt(key);
  const action = props.moreActions[actionIndex];
  if (action && !action.disabled) {
    if (action.onClick && typeof action.onClick === 'function') {
      action.onClick(props.record);
    } else {
      emit('moreAction', { action, record: props.record });
    }
  }
};

// 默认配置
const defaultConfig = {
  view: {
    show: true,
    text: '查看',
    icon: 'eye-outlined',
    permissions: null
  },
  edit: {
    show: true,
    text: '修改',
    icon: 'edit-outlined',
    permissions: null
  },
  delete: {
    show: false,
    text: '删除',
    icon: 'delete-outlined',
    permissions: null
  }
};

// 处理权限
const getPermissions = (buttonType) => {
  // 优先使用 permissions 对象
  if (props.permissions && props.permissions[buttonType]) {
    return props.permissions[buttonType];
  }

  return null;
};

// 处理按钮配置
const processButtonConfig = (buttonType, propValue, legacyShow, legacyIcon, legacyText) => {
  const defaults = defaultConfig[buttonType];
  const legacyPermission = getPermissions(buttonType);

  // 优先处理向后兼容旧参数（hasView/hasEdit/hasDelete 优先级最高）
  if (legacyShow !== undefined) {
    return {
      ...defaults,
      show: legacyShow,
      icon: legacyIcon || defaults.icon,
      text: legacyText || defaults.text,
      permissions: legacyPermission,
      handler: (e) => emit(buttonType, props.record)
    };
  }

  // 新的对象配置方式
  if (typeof propValue === 'object' && propValue !== null) {
    return {
      show: propValue.show !== undefined ? propValue.show : true,
      text: propValue.text || defaults.text,
      icon: propValue.icon || defaults.icon,
      permissions: propValue.permissions || null,
      handler: (e) => {
        if (propValue.onClick) {
          propValue.onClick(props.record);
        } else {
          emit(buttonType, props.record);
        }
      }
    };
  }

  // 布尔值配置方式
  if (typeof propValue === 'boolean') {
    return {
      ...defaults,
      show: propValue,
      icon: legacyIcon || defaults.icon,
      text: legacyText || defaults.text,
      permissions: legacyPermission,
      handler: (e) => emit(buttonType, props.record)
    };
  }

  // 默认配置
  return {
    ...defaults,
    permissions: legacyPermission,
    handler: (e) => emit(buttonType, props.record)
  };
};

</script>

<style lang="less">
/* TableActionBar 组件样式 */
.table-action-bar {
  .action-buttons {
    display: inline-flex;
    align-items: center;
    gap: 4px;
  }
}

/* 全局 action-btn 样式，供插槽使用 */
.action-btn {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  padding: 4px 8px;
  font-size: 13px;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.2s ease;
  border: none !important;
  box-shadow: none !important;

  &.view-btn {
    background-color: #e6f4ff;
    color: #1677ff;
  }

  &.edit-btn {
    background-color: #fff7e6;
    color: #fa8c16;
  }

  &.delete-btn {
    background-color: #fff1f0;
    color: #ff4d4f;
  }

  &.custom-btn {
    background-color: #f6ffed;
    color: #52c41a;
  }

  &.warning-btn {
    background-color: #fffbe6;
    color: #faad14;
  }

  &.info-btn {
    background-color: #e6fffb;
    color: #13c2c2;
  }

  &.primary-btn {
    background-color: #e6f4ff;
    color: #1677ff;
  }
}

// 暗黑主题支持 - 纯文字链接样式，无背景色
:global(.dark-theme) {
  // 去掉操作按钮之间的垂直分隔线
  .table-action-bar {
    .ant-divider-vertical {
      display: none !important;
    }

    .ant-space-item-split {
      display: none !important;
    }
  }

  .action-btn {
    // 统一去掉背景色，只保留文字颜色
    background-color: transparent !important;
    background: none !important;
    padding: 4px 8px !important;
    border-radius: 0 !important;
    transition: color 0.2s ease !important;
    margin: 0 2px !important;
    border: none !important;
    box-shadow: none !important;

    &:hover {
      background-color: transparent !important;
      text-decoration: underline !important;
    }

    &.view-btn {
      color: var(--primary-color);

      &:hover {
        color: var(--primary-color-hover);
      }
    }

    &.edit-btn {
      color: #faad14;

      &:hover {
        color: #ffc53d;
      }
    }

    &.delete-btn {
      color: #ff4d4f;

      &:hover {
        color: #ff7875;
      }
    }

    &.custom-btn {
      color: #52c41a;

      &:hover {
        color: #73d13d;
      }
    }

    &.warning-btn {
      color: #faad14;

      &:hover {
        color: #ffc53d;
      }
    }

    &.info-btn {
      color: #13c2c2;

      &:hover {
        color: #36cfc9;
      }
    }

    &.primary-btn {
      color: var(--primary-color);

      &:hover {
        color: var(--primary-color-hover);
      }
    }
  }
}
</style>
