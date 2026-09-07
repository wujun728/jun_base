<template>
  <PageWrapper dense>
    <BasicTable @register="registerTable">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <TableAction
            stopButtonPropagation
            :actions="[
              {
                label: '强制退出',
                icon: 'ic:outline-logout',
                type: 'primary',
                danger: true,
                auth: 'monitor:online:forceLogout',
                ghost: true,
                popConfirm: {
                  placement: 'left',
                  title: `是否强制退出用户[${record.userName}]?`,
                  confirm: handleForceLogout.bind(null, record),
                },
              },
            ]"
          />
        </template>
      </template>
    </BasicTable>
  </PageWrapper>
</template>

<script setup lang="ts">
  import { PageWrapper } from '@/components/Page';
  import { BasicTable, useTable, TableAction } from '@/components/Table';
  import { onlineList, forceLogout } from '@/api/monitor/online';
  import { useMessage } from '@/hooks/web/useMessage';
  import { formSchemas, columns } from './online.data';

  defineOptions({ name: 'Online' });

  const [registerTable, { reload }] = useTable({
    title: '在线用户列表',
    showIndexColumn: true,
    api: onlineList,
    rowKey: 'tokenId',
    useSearchForm: true,
    formConfig: {
      schemas: formSchemas,
      baseColProps: {
        xs: 24,
        sm: 24,
        md: 24,
        lg: 6,
      },
    },
    columns: columns,
    actionColumn: {
      width: 160,
      title: '操作',
      key: 'action',
      fixed: 'right',
    },
  });

  const { createMessage } = useMessage();
  async function handleForceLogout(record: Recordable) {
    await forceLogout(record.tokenId);
    await reload();
    createMessage.success(`退出用户账号[${record.userName}]成功`);
  }
</script>

<style scoped></style>
