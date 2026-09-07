<template>
  <BasicModal
    v-bind="$attrs"
    title="导入表"
    :width="900"
    @register="registerInnerModal"
    @ok="handleSubmit"
    @cancel="clearFormFields"
  >
    <BasicTable @register="registerTable" />
  </BasicModal>
</template>

<script setup lang="ts">
  import { BasicTable, useTable, BasicColumn } from '@/components/Table';
  import { BasicModal, useModalInner } from '@/components/Modal';
  import { readyToGenList, importTable } from '@/api/tool/gen';
  import { FormSchema } from '@/components/Form';

  defineOptions({ name: 'ImportTableModal' });
  const emit = defineEmits(['register', 'reload']);

  const [registerInnerModal, { modalLoading, closeModal }] = useModalInner(async () => {
    // 有缓存 每次都得刷新
    await reload();
  });

  const columns: BasicColumn[] = [
    {
      title: '表名称',
      dataIndex: 'tableName',
      align: 'left',
    },
    {
      title: '表描述',
      dataIndex: 'tableComment',
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
    },
    {
      title: '更新时间',
      dataIndex: 'updateTime',
    },
  ];

  const formSchemas: FormSchema[] = [
    {
      label: '表名称',
      field: 'tableName',
      component: 'Input',
    },
    {
      label: '表描述',
      field: 'tableComment',
      component: 'Input',
    },
  ];

  const [registerTable, { reload, getForm, getSelectRowKeys, clearSelectedRowKeys }] = useTable({
    rowSelection: {
      type: 'checkbox',
    },
    clickToRowSelect: true,
    canResize: false,
    showIndexColumn: false,
    showTableSetting: false,
    api: readyToGenList,
    rowKey: 'tableName',
    useSearchForm: true,
    immediate: false,
    size: 'small',
    formConfig: {
      labelWidth: 60,
      schemas: formSchemas,
      baseColProps: { span: 6 },
    },
    columns: columns,
    inset: true,
  });

  async function handleSubmit() {
    try {
      const tables = getSelectRowKeys();
      if (tables.length === 0) {
        closeModal();
        return;
      }
      const { validate } = getForm();
      modalLoading(true);
      const data = await validate();
      const dataSource = data.dataName;
      await importTable(tables.join(','), dataSource);
      emit('reload');
      closeModal();
      await clearFormFields();
    } catch (e) {
      console.warn(e);
    } finally {
      modalLoading(false);
    }
  }

  async function clearFormFields() {
    const { resetFields } = getForm();
    clearSelectedRowKeys();
    await resetFields();
  }
</script>

<style lang="less" scoped></style>
