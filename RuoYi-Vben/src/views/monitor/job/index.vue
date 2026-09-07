<template>
  <PageWrapper dense>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <Space class="<sm:hidden">
          <a-button
            v-auth="'monitor:job:export'"
            @click="downloadExcel(jobExport, '定时任务数据', getForm().getFieldsValue())"
            >导出</a-button
          >
          <a-button
            class="<sm:hidden"
            type="primary"
            danger
            @click="multipleRemove(jobDelete)"
            v-auth="'system:post:remove'"
            :disabled="!selected"
            >删除</a-button
          >
          <a-button type="primary" @click="handleAdd" v-auth="'monitor:job:add'">新增</a-button>
        </Space>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'status'">
          <TableSwitch
            v-model="record.status"
            :disabled="!hasPermission('monitor:job:changeStatus')"
            :api="() => changeStatus(record)"
            :reload="reload"
          />
        </template>
        <template v-if="column.key === 'action'">
          <TableAction
            stopButtonPropagation
            dropDownBtnDisplay
            :actions="[
              {
                label: '修改',
                icon: IconEnum.EDIT,
                type: 'primary',
                ghost: true,
                auth: 'monitor:job:edit',
                onClick: handleEdit.bind(null, record),
              },
              {
                label: '删除',
                icon: IconEnum.DELETE,
                type: 'primary',
                danger: true,
                ghost: true,
                auth: 'monitor:job:remove',
                popConfirm: {
                  placement: 'left',
                  title: `是否删除用户[${record.userName}]-${record.nickName}?`,
                  confirm: handleDelete.bind(null, record),
                },
              },
            ]"
            :dropDownActions="[
              {
                label: '执行一次',
                auth: 'monitor:job:edit',
                onClick: handleRunOnce.bind(null, record),
              },
              {
                label: '任务详细',
                auth: 'monitor:job:list',
                onClick: handleViewInfo.bind(null, record),
              },
              {
                label: '调度日志',
                auth: 'monitor:job:list',
                onClick: toLogPage.bind(null, record),
              },
            ]"
          />
        </template>
      </template>
    </BasicTable>
    <JobDrawer @register="registerDrawer" @reload="reload" />
    <JobDetailDrawer @register="registerDetailDrawer" />
  </PageWrapper>
</template>

<script setup lang="ts">
  import { PageWrapper } from '@/components/Page';
  import { Space } from 'ant-design-vue';
  import { jobList, jobExport, jobDelete, changeStatus, jobRunOnce } from '@/api/monitor/job';
  import { BasicTable, useTable, TableAction, TableSwitch } from '@/components/Table';
  import { downloadExcel } from '@/utils/file/download';
  import { columns, formSchemas } from './job.data';
  import { IconEnum } from '@/enums/appEnum';
  import JobDrawer from './JobDrawer.vue';
  import JobDetailDrawer from './JobDetailDrawer.vue';
  import { useDrawer } from '@/components/Drawer';
  import { usePermission } from '@/hooks/web/usePermission';
  import { useGo } from '@/hooks/web/usePage';
  import { pick } from 'lodash-es';

  // 缓存
  defineOptions({ name: 'Post' });

  const { hasPermission } = usePermission();

  const [registerTable, { reload, selected, multipleRemove, getForm }] = useTable({
    rowSelection: {
      type: 'checkbox',
    },
    title: '定时任务列表',
    showIndexColumn: false,
    api: jobList,
    rowKey: 'jobId',
    useSearchForm: true,
    formConfig: {
      schemas: formSchemas,
      name: 'job',
      baseColProps: {
        xs: 24,
        sm: 24,
        md: 24,
        lg: 6,
      },
      labelWidth: 100,
      // 日期选择格式化
      fieldMapToTime: [
        [
          'createTime',
          ['params[beginTime]', 'params[endTime]'],
          ['YYYY-MM-DD 00:00:00', 'YYYY-MM-DD 23:59:59'],
        ],
      ],
    },
    columns,
    actionColumn: {
      width: 220,
      title: '操作',
      key: 'action',
      fixed: 'right',
    },
  });

  const [registerDrawer, { openDrawer }] = useDrawer();

  function handleEdit(record: Recordable) {
    openDrawer(true, { update: true, record });
  }

  function handleAdd() {
    openDrawer(true, { update: false });
  }

  async function handleDelete(record: Recordable) {
    await jobDelete([record.jobId]);
    await reload();
  }

  const go = useGo();
  function toLogPage(record: Recordable) {
    go(`/job-log/${record.jobId}`);
  }

  async function handleRunOnce(record: Recordable) {
    await jobRunOnce(pick(record, ['jobGroup', 'jobId']));
  }

  const [registerDetailDrawer, { openDrawer: openDetailDrawer }] = useDrawer();
  function handleViewInfo(record: Recordable) {
    openDetailDrawer(true, record.jobId);
  }
</script>

<style lang="less" scoped>
  /** 去掉表格padding */
  :deep(.vben-basic-table-form-container) {
    padding: 5px;
  }
</style>
