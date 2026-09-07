<template>
  <PageWrapper title="代码生成" dense>
    <template #tags>
      <Tag color="orange">beta版本</Tag>
    </template>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button id="how-to-edit" class="font-bold" type="link" @click="openDrawer(true)"
          >👉如何修改后端?
        </a-button>
        <a-button
          v-auth="'tool:gen:remove'"
          type="primary"
          danger
          @click="multipleRemove(genRemove)"
          :disabled="!selected"
          >删除</a-button
        >
        <a-button
          v-auth="'tool:gen:code'"
          type="primary"
          color="success"
          :disabled="!selected"
          @click="handleBatchGen"
          >生成</a-button
        >
        <a-button v-auth="'tool:gen:import'" type="primary" @click="handleImport">导入</a-button>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <TableAction
            stopButtonPropagation
            :actions="[
              {
                label: '预览',
                icon: IconEnum.PREVIEW,
                auth: 'tool:gen:preview',
                onClick: handlePreview.bind(null, record),
              },
              {
                label: '编辑',
                icon: IconEnum.EDIT,
                auth: 'tool:gen:edit',
                onClick: handleEdit.bind(null, record),
              },
              {
                label: '同步',
                icon: IconEnum.SYNC,
                auth: 'tool:gen:edit',
                popConfirm: {
                  title: `确认要强制同步[${record.tableName}]表结构吗？`,
                  placement: 'left',
                  confirm: handleSync.bind(null, record),
                },
              },
              {
                label: '生成代码',
                icon: IconEnum.DOWNLOAD,
                auth: 'tool:gen:code',
                onClick: handleGenerate.bind(null, record),
              },
              {
                label: '删除',
                icon: IconEnum.DELETE,
                danger: true,
                auth: 'tool:gen:remove',
                popConfirm: {
                  placement: 'left',
                  title: `是否删除表[${record.tableName}]?`,
                  confirm: handleDelete.bind(null, record),
                },
              },
            ]"
          />
        </template>
      </template>
    </BasicTable>
    <ImportTableModal @register="registerImportModal" @reload="reload" />
    <PreviewModal @register="registerPreviewModal" />
    <DocsDrawer @register="registerDrawer" />
  </PageWrapper>
</template>

<script setup lang="ts">
  import { PageWrapper } from '@/components/Page';
  import { BasicTable, useTable, TableAction } from '@/components/Table';
  import { Tag } from 'ant-design-vue';
  import { useLoading } from '@/components/Loading';
  import { downloadByData } from '@/utils/file/download';
  import { useModal } from '@/components/Modal';
  import { onMounted } from 'vue';
  import { generatedList, genRemove, syncDb, batchGenCode } from '@/api/tool/gen';
  import ImportTableModal from './ImportTableModal.vue';
  import PreviewModal from './PreviewModal.vue';
  import { useGo } from '@/hooks/web/usePage';
  import { IconEnum } from '@/enums/appEnum';
  import DocsDrawer from './docs/DocsDrawer.vue';
  import { columns, formSchemas } from './gen.data';
  import { useDrawer } from '@/components/Drawer';
  import { driver } from 'driver.js';
  import 'driver.js/dist/driver.css';
  import { useLocalStorage } from '@vueuse/core';

  const [registerDrawer, { openDrawer }] = useDrawer();

  defineOptions({ name: 'Gen' });

  const readTip = useLocalStorage('__read_how_to_gen', false);

  onMounted(async () => {
    /** 引导 */
    if (readTip.value) {
      return;
    }
    setTimeout(() => {
      const defaultProps = {
        nextBtnText: '下一步',
        prevBtnText: '上一步',
      };
      const showDriver = driver({
        showProgress: true,
        allowClose: false,
        steps: [
          {
            element: '#how-to-edit',
            popover: { title: '提示', description: '(1)如何生成写在这了!', ...defaultProps },
          },
          {
            element: '#how-to-edit',
            popover: { title: '提示', description: '(2)如何生成写在这了!', ...defaultProps },
          },
          {
            element: '#how-to-edit',
            popover: {
              title: '提示',
              description: '(3)如何生成写在这了!',
              ...defaultProps,
              nextBtnText: '知道了',
            },
          },
        ],
      });
      showDriver.drive();
      readTip.value = true;
    }, 1000);
  });

  const [
    registerTable,
    { reload, getSelectRowKeys, clearSelectedRowKeys, selected, multipleRemove },
  ] = useTable({
    rowSelection: {
      type: 'checkbox',
    },
    title: '已导入的表',
    clickToRowSelect: true,
    api: generatedList,
    rowKey: 'tableId',
    useSearchForm: true,
    formConfig: {
      schemas: formSchemas,
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
      width: 400,
      title: '操作',
      key: 'action',
      fixed: 'right',
    },
  });

  const [registerImportModal, { openModal: openImportModal }] = useModal();
  function handleImport() {
    // 必须传参数才能触发回调  第二个参数无意义
    openImportModal(true, {});
  }

  /**
   * 批量代码生成
   */
  const [openFullLoading, closeFullLoading] = useLoading({
    tip: '加载中...',
  });
  async function handleBatchGen() {
    try {
      openFullLoading();
      const params = getSelectRowKeys().join(',');
      const data = await batchGenCode(params);
      const timestamp = new Date().getTime();
      downloadByData(data, '批量代码生成-' + timestamp + '.zip');
    } finally {
      closeFullLoading();
    }
  }

  /**
   * 下载
   */
  async function handleGenerate(record: Recordable) {
    try {
      openFullLoading();
      const data = await batchGenCode(record.tableId);
      const timestamp = new Date().getTime();
      downloadByData(data, '代码生成-' + timestamp + '.zip');
    } finally {
      closeFullLoading();
    }
  }

  /**
   * 跳转到编辑页面
   */
  const go = useGo();
  function handleEdit(record: Recordable) {
    go(`/tool/generate/edit/${record.tableId}`);
  }

  /**
   * 同步
   */
  async function handleSync(record: Recordable) {
    await syncDb(record.tableId);
    clearSelectedRowKeys();
    await reload();
  }

  /**
   * 预览Modal
   */
  const [registerPreviewModal, { openModal: openPreviewModal }] = useModal();
  function handlePreview(record: Recordable) {
    openPreviewModal(true, record.tableId);
  }

  /**
   * 删除
   */
  async function handleDelete(record: Recordable) {
    await genRemove([record.tableId]);
    await reload();
  }
</script>

<style scoped></style>
