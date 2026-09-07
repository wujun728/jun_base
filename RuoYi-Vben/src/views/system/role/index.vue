<template>
  <PageWrapper dense>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <Space>
          <a-button
            v-auth="'system:role:export'"
            @click="downloadExcel(roleExport, '角色信息', getForm().getFieldsValue())"
            >导出</a-button
          >
          <a-button
            type="primary"
            danger
            @click="multipleRemove(roleRemove)"
            :disabled="!selected"
            v-auth="'system:role:remove'"
            >删除</a-button
          >
          <a-button type="primary" @click="handleAdd" v-auth="'system:role:add'">新增</a-button>
        </Space>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'status'">
          <TableSwitch
            v-model="record.status"
            :disabled="
              record.roleId === 1 ||
              record.roleKey === 'admin' ||
              !hasPermission('system:role:edit')
            "
            :api="() => roleChangeStatus(record)"
            :reload="reload"
          />
        </template>
        <template v-if="column.key === 'action'">
          <!-- 超级管理员和小管理员不可操作 -->
          <TableAction
            stopButtonPropagation
            dropDownBtnDisplay
            :actions="[
              {
                label: '修改',
                icon: IconEnum.EDIT,
                type: 'primary',
                ghost: true,
                auth: 'system:role:edit',
                onClick: handleEdit.bind(null, record),
                ifShow: !record.superAdmin && (record.roleKey !== 'admin' || isSuperAdmin),
              },
              {
                label: '删除',
                icon: IconEnum.DELETE,
                type: 'primary',
                danger: true,
                ghost: true,
                auth: 'system:role:remove',
                ifShow: !record.superAdmin && record.roleKey !== 'admin',
                popConfirm: {
                  placement: 'left',
                  title: `是否删除角色[${record.roleName}]?`,
                  confirm: handleDelete.bind(null, record),
                },
              },
            ]"
            :dropDownActions="[
              {
                label: '数据权限',
                icon: 'mingcute:department-line',
                auth: 'system:role:edit',
                ifShow: !record.superAdmin && record.roleKey !== 'admin',
                onClick: handleOpenAuth.bind(null, record),
              },
              {
                label: '分配用户',
                icon: 'solar:user-outline',
                auth: 'system:role:edit',
                ifShow: !record.superAdmin && record.roleKey !== 'admin',
                onClick: handleAssignRole.bind(null, record.roleId),
              },
            ]"
          />
        </template>
      </template>
    </BasicTable>
    <RoleDrawer @register="registerDrawer" @reload="reload" />
    <RoleAuthModal @register="registerAuthModal" @reload="reload" />
  </PageWrapper>
</template>

<script setup lang="ts">
  import { PageWrapper } from '@/components/Page';
  import { BasicTable, useTable, TableAction, TableSwitch } from '@/components/Table';
  import { Space } from 'ant-design-vue';
  import { roleList, roleExport, roleRemove, roleChangeStatus } from '@/api/system/role';
  import { useModal } from '@/components/Modal';
  import RoleAuthModal from './RoleAuthModal.vue';
  import { downloadExcel } from '@/utils/file/download';
  import { formSchemas, columns } from './role.data';
  import { IconEnum } from '@/enums/appEnum';
  import { useGo } from '@/hooks/web/usePage';
  import RoleDrawer from './RoleDrawer.vue';
  import { useDrawer } from '@/components/Drawer';
  import { useUserStore } from '@/store/modules/user';
  import { computed } from 'vue';
  import { RoleEnum } from '@/enums/roleEnum';
  import { usePermission } from '@/hooks/web/usePermission';

  // 缓存
  defineOptions({ name: 'Role' });
  const { hasPermission } = usePermission();

  const [registerTable, { reload, multipleRemove, selected, getForm }] = useTable({
    rowSelection: {
      type: 'checkbox',
      getCheckboxProps: (record: Recordable) => ({
        disabled: record.superAdmin || record.roleKey === 'admin',
      }),
    },
    title: '角色信息',
    showIndexColumn: false,
    api: roleList,
    rowKey: 'roleId',
    useSearchForm: true,
    formConfig: {
      schemas: formSchemas,
      name: 'role',
      baseColProps: {
        xs: 24,
        sm: 24,
        md: 24,
        lg: 6,
      },
      fieldMapToTime: [
        ['createTime', ['params[beginTime]', 'params[endTime]'], ['YYYY-MM-DD', 'YYYY-MM-DD']],
      ],
    },
    columns: columns,
    actionColumn: {
      title: '操作',
      key: 'action',
      fixed: 'right',
    },
  });

  const userStore = useUserStore();
  const isSuperAdmin = computed(() => {
    return userStore.userInfo?.roles.includes(RoleEnum.SUPER_ADMIN);
  });

  const [registerDrawer, { openDrawer }] = useDrawer();
  const [registerAuthModal, { openModal: openAuthModal }] = useModal();

  function handleOpenAuth(record: Recordable) {
    openAuthModal(true, { record });
  }

  function handleEdit(record: Recordable) {
    openDrawer(true, { record, update: true });
  }

  function handleAdd() {
    openDrawer(true, { update: false });
  }

  const go = useGo();
  function handleAssignRole(roleId: string) {
    go(`/system/assign-roles/${roleId}`);
  }

  async function handleDelete(record: Recordable) {
    await roleRemove([record.roleId]);
    await reload();
  }
</script>

<style scoped></style>
