<template>
  <PageWrapper dense>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button @click="handleClose">关闭页面</a-button>
        <a-button
          :disabled="!selected"
          type="error"
          @click="handleMultipleAuthCancel"
          v-auth="'system:role:edit'"
          >取消授权</a-button
        >
        <a-button type="primary" @click="handleAdd" v-auth="'system:role:edit'">添加</a-button>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <TableAction
            stopButtonPropagation
            :actions="[
              {
                label: '取消授权',
                icon: IconEnum.CANCEL,
                auth: 'system:role:edit',
                popConfirm: {
                  placement: 'left',
                  title: `是否取消授权用户[${record.userName} - ${record.nickName}]?`,
                  confirm: handleAuthCancel.bind(null, record),
                },
              },
            ]"
          />
        </template>
      </template>
    </BasicTable>
    <AssignRolesDrawer @register="registerDrawer" @reload="reload" />
  </PageWrapper>
</template>

<script setup lang="ts">
  import { PageWrapper } from '@/components/Page';
  import { BasicTable, useTable, TableAction } from '@/components/Table';
  import { roleAllocatedList, roleAuthCancel, roleAuthCancelAll } from '@/api/system/role';
  import { IconEnum } from '@/enums/appEnum';
  import { columns, formSchemas } from './assign.data';
  import { useRoute } from 'vue-router';
  import { ref } from 'vue';
  import { useMessage } from '@/hooks/web/useMessage';
  import { useDrawer } from '@/components/Drawer';
  import AssignRolesDrawer from './AssignRolesDrawer.vue';
  import { useTabs } from '@/hooks/web/useTabs';

  defineOptions({ name: 'AssignRoles' });

  /** 拿到路由参数 */
  const roleId = ref<string>('0');
  const route = useRoute();
  roleId.value = route.params.roleId as string;

  const [registerTable, { reload, getSelectRowKeys, selected }] = useTable({
    rowSelection: {
      type: 'checkbox',
    },
    title: '已分配的用户列表',
    showIndexColumn: false,
    clickToRowSelect: true,
    api: roleAllocatedList,
    rowKey: 'userId',
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
    // 额外的请求参数
    searchInfo: {
      roleId: roleId.value,
    },
    columns: columns,
    actionColumn: {
      title: '操作',
      key: 'action',
      fixed: 'right',
    },
  });

  const [registerDrawer, { openDrawer }] = useDrawer();
  function handleAdd() {
    // 必须传参  否则无法触发回调
    openDrawer(true, {});
  }

  const { createConfirm } = useMessage();
  /**
   * 批量取消授权
   */
  function handleMultipleAuthCancel() {
    createConfirm({
      iconType: 'warning',
      title: '取消授权',
      content: '是否取消选中的授权?',
      async onOk() {
        const userIds = getSelectRowKeys();
        await roleAuthCancelAll(roleId.value, userIds as number[]);
        await reload();
      },
    });
  }
  /**
   * 取消授权
   */
  async function handleAuthCancel(record: Recordable) {
    await roleAuthCancel({ userId: record.userId, roleId: roleId.value });
    await reload();
  }

  const { closeCurrent } = useTabs();
  async function handleClose() {
    await closeCurrent();
  }
</script>

<style scoped></style>
