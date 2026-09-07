<template>
  <BasicDrawer
    v-bind="$attrs"
    title="选择用户"
    :width="drawerWidth"
    showFooter
    @register="registerDrawerModal"
    @ok="handleSubmit"
  >
    <BasicTable @register="registerTable">
      <!-- 要有这个插槽(表格顶部title/toolbar)才能显示勾选信息😅 -->
      <template #toolbar></template>
    </BasicTable>
    <template #footer>
      <a-button @click="closeDrawer">取消</a-button>
      <a-button type="primary" @click="handleSubmit">导入</a-button>
    </template>
  </BasicDrawer>
</template>

<script setup lang="ts">
  import { BasicTable, useTable } from '@/components/Table';
  import { roleUnallocatedList, roleSelectAll } from '@/api/system/role';
  import { columns, formSchemas } from './assign.data';
  import { ref } from 'vue';
  import { BasicDrawer, useDrawerInner } from '@/components/Drawer';
  import { useRoute } from 'vue-router';
  import { useMaxWidthOrDefault } from '@/hooks/web/useSize';

  const route = useRoute();
  /** 拿到路由参数 */
  const roleId = ref<string>((route.params.roleId as string) || '0');

  const drawerWidth = useMaxWidthOrDefault(800);
  const emit = defineEmits(['register', 'reload']);

  const [registerDrawerModal, { closeDrawer }] = useDrawerInner(async () => {
    await getForm().resetFields();
  });

  const [registerTable, { getForm, getSelectRowKeys, setSelectedRows }] = useTable({
    rowSelection: {
      type: 'checkbox',
    },
    showIndexColumn: false,
    clickToRowSelect: true,
    clearSelectOnPageChange: false,
    showSelectionBar: true,
    api: roleUnallocatedList,
    rowKey: 'userId',
    useSearchForm: true,
    formConfig: {
      schemas: formSchemas,
      labelWidth: 80,
      baseColProps: {
        xs: 24,
        sm: 24,
        md: 24,
        lg: 8,
      },
    },
    // 额外的请求参数
    searchInfo: {
      roleId: roleId.value,
    },
    immediate: false,
    columns: columns,
    showTableSetting: false,
    // resizeHeightOffset: 350,
    inset: true,
  });

  async function handleSubmit() {
    const userIds = getSelectRowKeys();
    if (userIds.length !== 0) {
      await roleSelectAll(roleId.value, userIds as number[]);
    }
    setSelectedRows([]);
    emit('reload');
    closeDrawer();
  }
</script>

<style lang="less" scoped></style>
