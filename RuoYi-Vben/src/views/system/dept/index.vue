<template>
  <PageWrapper dense>
    <BasicTable
      @register="registerTable"
      @fetch-success="onFetchSuccess"
      @row-db-click="handleDoubleClick"
    >
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
        <a-button class="<sm:hidden" @click="handleExpandOrCollapseAll('expandAll')">展开</a-button>
        <a-button class="<sm:hidden" @click="handleExpandOrCollapseAll('collapseAll')"
          >折叠</a-button
        >
        <a-button type="primary" @click="handleAdd(100)" v-auth="'system:dept:add'">
          新增
        </a-button>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <TableAction
            stopButtonPropagation
            :actions="[
              {
                label: '修改',
                icon: IconEnum.EDIT,
                type: 'primary',
                ghost: true,
                auth: 'system:dept:edit',
                onClick: handleEdit.bind(null, record),
              },
              {
                label: '新增',
                icon: IconEnum.ADD,
                type: 'primary',
                color: 'success',
                ghost: true,
                auth: 'system:dept:add',
                onClick: handleAdd.bind(null, record.deptId),
              },
              {
                label: '删除',
                icon: IconEnum.DELETE,
                type: 'primary',
                danger: true,
                ghost: true,
                auth: 'system:dept:remove',
                ifShow: () => record.parentId !== 0,
                popConfirm: {
                  title: `是否删除部门[${record.deptName}]`,
                  placement: 'left',
                  confirm: handleRemove.bind(null, record.deptId),
                },
              },
            ]"
          />
        </template>
      </template>
    </BasicTable>
    <DeptModal @register="registerModal" @reload="reload" />
  </PageWrapper>
</template>

<script setup lang="ts">
  import { PageWrapper } from '@/components/Page';
  import { BasicTable, useTable, TableAction } from '@/components/Table';
  import { deptList, deptRemove } from '@/api/system/dept';
  import { Dept } from '@/api/system/dept/model';
  import { Alert } from 'ant-design-vue';
  import DeptModal from './DeptModal.vue';
  import { useModal } from '@/components/Modal';
  import { nextTick } from 'vue';
  import { formSchemas, columns } from './dept.data';
  import { listToTree, removeEmptyChildren } from '@/utils/helper/treeHelper';
  import { IconEnum } from '@/enums/appEnum';
  import { useLocalStorage } from '@vueuse/core';

  defineOptions({ name: 'Dept' });

  /** 是否已读提示 */
  const read = useLocalStorage('__dept_tip_read', false);

  const [
    registerTable,
    { getRawDataSource, expandRows, collapseRows, expandAll, collapseAll, reload },
  ] = useTable({
    title: '部门列表',
    isTreeTable: true,
    showTableSetting: true,
    api: deptList,
    pagination: false,
    rowKey: 'deptId',
    afterFetch(data: Dept[]) {
      const deptTree = listToTree(data, { id: 'deptId', pid: 'parentId' });
      removeEmptyChildren(deptTree);
      return deptTree;
    },
    useSearchForm: true,
    formConfig: {
      schemas: formSchemas,
      name: 'dept',
      baseColProps: {
        xs: 24,
        sm: 24,
        md: 24,
        lg: 6,
      },
    },
    columns: columns,
    actionColumn: {
      title: '操作',
      key: 'action',
      fixed: 'right',
    },
  });

  function onFetchSuccess() {
    // 需要添加上expand标记
    const rawDataSource = getRawDataSource();
    if (!(rawDataSource instanceof Array)) {
      return;
    }
    rawDataSource.forEach((item) => {
      item.expand = true;
    });
    // 默认展开全部
    nextTick(expandAll);
  }

  const [registerModal, { openModal }] = useModal();

  function handleEdit(record: Recordable) {
    const { deptId } = record;
    openModal(true, { deptId, update: true });
  }

  function handleAdd(deptId: string | number) {
    openModal(true, { deptId, update: false });
  }

  async function handleRemove(deptId: string | number) {
    await deptRemove(deptId);
    await reload();
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
    const { deptId } = record;
    if (record.expand === true) {
      collapseRows([deptId]);
      record.expand = false;
    } else {
      expandRows([deptId]);
      record.expand = true;
    }
  }
</script>

<style scoped></style>
