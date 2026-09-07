<template>
  <PageWrapper dense>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <Space>
          <a-button @click="handleRefresh" v-auth="'system:config:query'">刷新缓存</a-button>
          <a-button
            class="<sm:hidden"
            v-auth="'system:config:export'"
            @click="downloadExcel(configExport, '参数数据', getForm().getFieldsValue())"
            >导出</a-button
          >
          <a-button
            class="<sm:hidden"
            type="primary"
            danger
            @click="multipleRemove(configRemove)"
            :disabled="!selected"
            v-auth="'system:config:remove'"
            >删除</a-button
          >
          <a-button type="primary" @click="handleAdd" v-auth="'system:config:add'">新增</a-button>
        </Space>
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
                auth: 'system:config:edit',
                onClick: handleEdit.bind(null, record),
              },
              {
                label: '删除',
                icon: IconEnum.DELETE,
                type: 'primary',
                danger: true,
                ghost: true,
                auth: 'system:config:remove',
                popConfirm: {
                  placement: 'left',
                  title: `是否删除[${record.configName}]?`,
                  confirm: handleDelete.bind(null, record),
                },
              },
            ]"
          />
        </template>
      </template>
    </BasicTable>
    <ConfigModal @register="registerModal" @reload="reload" />
  </PageWrapper>
</template>

<script setup lang="ts">
  import { PageWrapper } from '@/components/Page';
  import { BasicTable, useTable, TableAction } from '@/components/Table';
  import { Space } from 'ant-design-vue';
  import { configList, configExport, configRefreshCache, configRemove } from '@/api/system/config';
  import ConfigModal from './ConfigModal.vue';
  import { useModal } from '@/components/Modal';
  import { downloadExcel } from '@/utils/file/download';
  import { formSchemas, columns } from './config.data';
  import { IconEnum } from '@/enums/appEnum';

  defineOptions({ name: 'Config' });

  const [registerTable, { reload, multipleRemove, selected, getForm }] = useTable({
    rowSelection: {
      type: 'checkbox',
    },
    title: '参数列表',
    showIndexColumn: false,
    api: configList,
    rowKey: 'configId',
    useSearchForm: true,
    formConfig: {
      schemas: formSchemas,
      labelWidth: 80,
      name: 'config',
      baseColProps: {
        xs: 24,
        sm: 24,
        md: 24,
        lg: 6,
      },
      // 日期选择格式化
      fieldMapToTime: [
        [
          'createTime',
          ['params[beginTime]', 'params[endTime]'],
          ['YYYY-MM-DD 00:00:00', 'YYYY-MM-DD 23:59:59'],
        ],
      ],
    },
    columns: columns,
    actionColumn: {
      width: 200,
      title: '操作',
      key: 'action',
      fixed: 'right',
    },
  });

  const [registerModal, { openModal }] = useModal();

  async function handleRefresh() {
    await configRefreshCache();
    await reload();
  }

  function handleEdit(record: Recordable) {
    openModal(true, { record, update: true });
  }

  function handleAdd() {
    openModal(true, { update: false });
  }

  async function handleDelete(record: Recordable) {
    await configRemove([record.configId]);
    await reload();
  }
</script>

<style scoped></style>
