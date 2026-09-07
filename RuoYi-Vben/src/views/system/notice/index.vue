<template>
  <PageWrapper dense>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button
          type="primary"
          danger
          @click="multipleRemove(noticeRemove)"
          :disabled="!selected"
          v-auth="'system:notice:remove'"
          >删除</a-button
        >
        <a-button type="primary" @click="handleAdd" v-auth="'system:notice:add'">新增</a-button>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <TableAction
            stopButtonPropagation
            :actions="[
              {
                label: '预览',
                icon: IconEnum.PREVIEW,
                type: 'primary',
                ghost: true,
                auth: 'system:notice:list',
                onClick: handlePreview.bind(null, record),
              },
              {
                label: '修改',
                icon: IconEnum.EDIT,
                type: 'primary',
                ghost: true,
                auth: 'system:notice:edit',
                onClick: handleEdit.bind(null, record),
              },
              {
                label: '删除',
                icon: IconEnum.DELETE,
                type: 'primary',
                danger: true,
                ghost: true,
                auth: 'system:notice:remove',
                popConfirm: {
                  placement: 'left',
                  title: `是否删除公告[${record.noticeTitle}]?`,
                  confirm: handleDelete.bind(null, record),
                },
              },
            ]"
          />
        </template>
      </template>
    </BasicTable>
    <NoticeModal @register="registerModal" @reload="reload" />
    <NoticePreviewModal @register="registerPreviewModal" />
  </PageWrapper>
</template>

<script setup lang="ts">
  import { PageWrapper } from '@/components/Page';
  import { BasicTable, useTable, TableAction } from '@/components/Table';
  import { noticeList, noticeRemove } from '@/api/system/notice';
  import { useModal } from '@/components/Modal';
  import NoticeModal from './NoticeModal.vue';
  import NoticePreviewModal from './NoticePreviewModal.vue';
  import { formSchemas, columns } from './notice.data';
  import { IconEnum } from '@/enums/appEnum';

  defineOptions({ name: 'Notice' });

  const [registerTable, { reload, multipleRemove, selected }] = useTable({
    rowSelection: {
      type: 'checkbox',
    },
    title: '通知公告列表',
    showIndexColumn: true,
    api: noticeList,
    rowKey: 'noticeId',
    useSearchForm: true,
    formConfig: {
      schemas: formSchemas,
      labelWidth: 80,
      name: 'notice',
      baseColProps: {
        xs: 24,
        sm: 24,
        md: 24,
        lg: 6,
      },
    },
    columns: columns,
    actionColumn: {
      width: 230,
      title: '操作',
      key: 'action',
      fixed: 'right',
    },
  });

  const [registerModal, { openModal }] = useModal();
  const [registerPreviewModal, { openModal: openPreviewModal }] = useModal();

  function handleEdit(record: Recordable) {
    openModal(true, { record, update: true });
  }

  function handlePreview(record: Recordable) {
    openPreviewModal(true, record);
  }

  function handleAdd() {
    openModal(true, { update: false });
  }

  async function handleDelete(record: Recordable) {
    await noticeRemove([record.noticeId]);
    await reload();
  }
</script>

<style scoped></style>
