<template>
  <PageWrapper dense>
    <BasicTable @register="tableRegister" @row-db-click="handleDoubleClick">
      <template v-if="!read" #headerTop>
        <Alert
          class="h-30px"
          message="提示: 双击行可展开/折叠内容"
          type="info"
          banner
          closable
          @close="read = true"
        />
      </template>
      <template #toolbar>
        <Space>
          <Tooltip title="修改后是否刷新菜单栏">
            <Switch v-model:checked="refreshMenuRef" />
          </Tooltip>
          <a-button class="<sm:hidden" @click="handleExpandOrCollapseAll('expandAll')"
            >展开</a-button
          >
          <a-button class="<sm:hidden" @click="handleExpandOrCollapseAll('collapseAll')"
            >折叠</a-button
          >
          <a-button v-auth="'system:menu:add'" type="primary" @click="handleAdd">新增</a-button>
        </Space>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <!-- 为按钮时不显示新增 -->
          <TableAction
            stopButtonPropagation
            :actions="[
              {
                label: '修改',
                type: 'primary',
                ghost: true,
                icon: IconEnum.EDIT,
                onClick: handleEdit.bind(null, record),
                auth: 'system:menu:edit',
              },
              {
                label: '新增',
                icon: IconEnum.ADD,
                type: 'primary',
                color: 'success',
                ghost: true,
                onClick: handleAdd.bind(null, record),
                auth: 'system:menu:add',
                ifShow: record.menuType !== 'F',
              },
              {
                label: '删除',
                icon: IconEnum.DELETE,
                type: 'primary',
                ghost: true,
                danger: true,
                auth: 'system:menu:delete',
                popConfirm: {
                  title: `是否删除菜单[${record.menuName}]?`,
                  placement: 'left',
                  confirm: handleDelete.bind(null, record),
                },
              },
            ]"
          />
        </template>
      </template>
    </BasicTable>
    <MenuDrawer @register="registerDrawer" @reload="reload" />
  </PageWrapper>
</template>

<script setup lang="ts">
  import { PageWrapper } from '@/components/Page';
  import { BasicTable, useTable, TableAction } from '@/components/Table';
  import { menuList, menuRemove } from '@/api/system/menu';
  import { Space, Switch, Alert, Tooltip } from 'ant-design-vue';
  import { IconEnum } from '@/enums/appEnum';
  import { tableColumns, formSchemas } from './menu.data';
  import { listToTree, removeEmptyChildren } from '@/utils/helper/treeHelper';
  import { usePermission } from '@/hooks/web/usePermission';
  import { useLocalStorage } from '@vueuse/core';
  import MenuDrawer from './MenuDrawer.vue';
  import { useDrawer } from '@/components/Drawer';

  // eslint-disable-next-line vue/no-reserved-component-names
  defineOptions({ name: 'Menu' });
  /** 修改后是否刷新菜单栏 */
  const refreshMenuRef = useLocalStorage('refreshMenu', true);
  /** 是否已读提示 */
  const read = useLocalStorage('__menu_tip_read', false);

  const [
    tableRegister,
    { getRawDataSource, expandRows, collapseRows, expandAll, collapseAll, reload },
  ] = useTable({
    title: '菜单列表',
    bordered: false,
    api: menuList,
    rowKey: 'menuId',
    isTreeTable: true,
    columns: tableColumns,
    useSearchForm: true,
    formConfig: {
      baseColProps: {
        xs: 24,
        sm: 24,
        md: 12,
        lg: 6,
      },
      name: 'menu',
      schemas: formSchemas,
    },
    pagination: false,
    afterFetch(data: Recordable[]) {
      const tree = listToTree(data, { id: 'menuId', pid: 'parentId' });
      // 去除空的子节点 这样就不会有展开
      removeEmptyChildren(tree);
      return tree;
    },
    actionColumn: {
      width: 230,
      title: '操作',
      key: 'action',
      fixed: 'right',
    },
  });

  const [registerDrawer, { openDrawer }] = useDrawer();

  function handleEdit(record: Recordable) {
    openDrawer(true, { record, update: true });
  }

  function handleAdd(record?: Recordable) {
    const parentId = record ? record.menuId : 0;
    openDrawer(true, { record: { parentId }, update: false });
  }

  // 删除后刷新菜单栏 不使用location.reload整页刷新
  const { refreshMenu } = usePermission();

  async function handleDelete(record: Recordable) {
    await menuRemove([record.menuId]);
    await reload();
    if (refreshMenuRef.value) {
      await refreshMenu();
    }
  }

  type ExpandOrCollapseType = 'expandAll' | 'collapseAll';
  function handleExpandOrCollapseAll(type: ExpandOrCollapseType) {
    // 拿到表格原始数据 响应式
    const rawDataSource = getRawDataSource();
    if (!(rawDataSource instanceof Array)) {
      return;
    }
    // 添加expand字段 用于记录是否展开
    switch (type) {
      case 'expandAll':
        rawDataSource.forEach((item) => {
          item.expand = true;
        });
        expandAll();
        break;
      case 'collapseAll':
        rawDataSource.forEach((item) => {
          item.expand = false;
        });
        collapseAll();
        break;
    }
  }

  /**
   * 行双击事件  展开/折叠
   * @param record
   */
  function handleDoubleClick(record: Recordable) {
    const { menuId } = record;
    if (record.expand === true) {
      collapseRows([menuId]);
      record.expand = false;
    } else {
      expandRows([menuId]);
      record.expand = true;
    }
  }
</script>

<style scoped></style>
