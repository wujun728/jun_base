<template>
  <div class="flex flex-col rounded-lg">
    <div :class="bem()" class="flex mb-1 px-2 py-1.5 items-center">
      <slot name="headerTitle" v-if="slots.headerTitle"></slot>
      <BasicTitle :helpMessage="helpMessage" v-if="!slots.headerTitle && title">
        {{ title }}
      </BasicTitle>
      <div
        class="flex items-center flex-1 cursor-pointer justify-self-stretch"
        v-if="search || toolbar"
      >
        <div :class="getInputSearchCls" v-if="search">
          <InputSearch
            :placeholder="t('common.searchText')"
            size="small"
            allowClear
            v-model:value="searchValue"
          />
        </div>
        <Dropdown @click.prevent v-if="toolbar">
          <Icon icon="ion:ellipsis-vertical" />
          <template #overlay>
            <Menu @click="handleMenuClick">
              <template v-for="item in toolbarList" :key="item.value">
                <MenuItem v-bind="{ key: item.value }">
                  {{ item.label }}
                </MenuItem>
                <MenuDivider v-if="item.divider" />
              </template>
            </Menu>
          </template>
        </Dropdown>
      </div>
    </div>
    <!-- 自定义选项 -->
    <div v-if="props.enableCustomTool" :class="bem()" class="flex flex-row gap-2 px-4 py-1.5">
      <span> 节点状态: </span>
      <span :class="checkStrictly ? 'text-red-500' : 'text-blue-500'">{{ checkStrictlyText }}</span>
    </div>
    <div
      v-if="props.enableCustomTool"
      :class="bem()"
      class="flex flex-row gap-1 px-3 py-1.5 justify-between"
    >
      <Checkbox v-model:checked="radioGroup.collapseAll" @change="handleCollapseAll"
        >展开/折叠</Checkbox
      >
      <Checkbox v-model:checked="radioGroup.checkAll" @change="handleCheckAll"
        >全选/全不选</Checkbox
      >
      <Checkbox :checked="!props.checkStrictly" @change="handleCheckStrictly"
        >父子节点关联</Checkbox
      >
    </div>
  </div>
</template>
<script lang="ts" setup>
  import { type PropType, computed, ref, watch, useSlots, reactive } from 'vue';
  import {
    Dropdown,
    Menu,
    MenuItem,
    MenuDivider,
    InputSearch,
    Checkbox,
    type MenuProps,
  } from 'ant-design-vue';
  import Icon from '@/components/Icon/Icon.vue';
  import { BasicTitle } from '@/components/Basic';
  import { useI18n } from '@/hooks/web/useI18n';
  import { useDebounceFn } from '@vueuse/core';
  import { createBEM } from '@/utils/bem';
  import { ToolbarEnum } from '../types/tree';

  const searchValue = ref('');

  const [bem] = createBEM('tree-header');

  const props = defineProps({
    helpMessage: {
      type: [String, Array] as PropType<string | string[]>,
      default: '',
    },
    title: {
      type: String,
      default: '',
    },
    toolbar: {
      type: Boolean,
      default: false,
    },
    checkable: {
      type: Boolean,
      default: false,
    },
    search: {
      type: Boolean,
      default: false,
    },
    searchText: {
      type: String,
      default: '',
    },
    checkAll: {
      type: Function,
      default: undefined,
    },
    expandAll: {
      type: Function,
      default: undefined,
    },
    checkStrictly: {
      type: Boolean,
      default: false,
    },
    /**
     * 是否启用自定义工具
     * 选择全部 / 节点关联独立 ...
     */
    enableCustomTool: {
      type: Boolean,
      default: false,
    },
  } as const);
  const emit = defineEmits(['strictly-change', 'search']);

  const slots = useSlots();
  const { t } = useI18n();

  const getInputSearchCls = computed(() => {
    const titleExists = slots.headerTitle || props.title;
    return [
      'mr-1',
      'w-full',
      {
        ['ml-5']: titleExists,
      },
    ];
  });

  const toolbarList = computed(() => {
    const { checkable } = props;
    const defaultToolbarList = [
      { label: t('component.tree.expandAll'), value: ToolbarEnum.EXPAND_ALL },
      {
        label: t('component.tree.unExpandAll'),
        value: ToolbarEnum.UN_EXPAND_ALL,
        divider: checkable,
      },
    ];

    return checkable
      ? [
          { label: t('component.tree.selectAll'), value: ToolbarEnum.SELECT_ALL },
          {
            label: t('component.tree.unSelectAll'),
            value: ToolbarEnum.UN_SELECT_ALL,
            divider: checkable,
          },
          ...defaultToolbarList,
          { label: t('component.tree.checkStrictly'), value: ToolbarEnum.CHECK_STRICTLY },
          { label: t('component.tree.checkUnStrictly'), value: ToolbarEnum.CHECK_UN_STRICTLY },
        ]
      : defaultToolbarList;
  });

  const checkStrictlyText = computed(() => {
    return props.checkStrictly ? '节点独立' : '节点关联';
  });

  const radioGroup = reactive({
    checkAll: false,
    collapseAll: false,
  });

  function handleCollapseAll() {
    props.expandAll?.(radioGroup.collapseAll);
  }

  function handleCheckAll() {
    props.checkAll?.(radioGroup.checkAll);
  }
  /**
   * 节点独立/关联
   */
  function handleCheckStrictly() {
    emit('strictly-change', !props.checkStrictly);
  }

  const handleMenuClick: MenuProps['onClick'] = ({ key }) => {
    switch (key) {
      case ToolbarEnum.SELECT_ALL:
        props.checkAll?.(true);
        break;
      case ToolbarEnum.UN_SELECT_ALL:
        props.checkAll?.(false);
        break;
      case ToolbarEnum.EXPAND_ALL:
        props.expandAll?.(true);
        break;
      case ToolbarEnum.UN_EXPAND_ALL:
        props.expandAll?.(false);
        break;
      case ToolbarEnum.CHECK_STRICTLY:
        emit('strictly-change', false);
        break;
      case ToolbarEnum.CHECK_UN_STRICTLY:
        emit('strictly-change', true);
        break;
    }
  };

  function emitChange(value?: string): void {
    emit('search', value);
  }

  const debounceEmitChange = useDebounceFn(emitChange, 200);

  watch(
    () => searchValue.value,
    (v) => {
      debounceEmitChange(v);
    },
  );

  watch(
    () => props.searchText,
    (v) => {
      if (v !== searchValue.value) {
        searchValue.value = v;
      }
    },
  );
</script>
