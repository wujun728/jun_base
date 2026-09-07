<template>
  <PageWrapper dense>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <Space class="<sm:hidden">
          <a-button
            v-auth="'monitor:job:export'"
            @click="downloadExcel(jobLogExport, '定时任务日志', getForm().getFieldsValue())"
            >导出</a-button
          >
          <a-button
            class="<sm:hidden"
            type="primary"
            danger
            @click="multipleRemove(jobLogRemove)"
            v-auth="'monitor:job:remove'"
            :disabled="!selected"
            >删除</a-button
          >
        </Space>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <TableAction
            stopButtonPropagation
            dropDownBtnDisplay
            :actions="[
              {
                label: '查看',
                icon: IconEnum.PREVIEW,
                type: 'primary',
                ghost: true,
                auth: 'monitor:job:list',
                onClick: handlePreview.bind(null, record),
              },
            ]"
          />
        </template>
      </template>
    </BasicTable>
    <JobDrawer @register="registerPostDrawer" @reload="reload" />
  </PageWrapper>
</template>

<script setup lang="ts">
  import { PageWrapper } from '@/components/Page';
  import { Space } from 'ant-design-vue';
  import { jobInfo } from '@/api/monitor/job';
  import { jobLogList, jobLogExport, jobLogRemove } from '@/api/monitor/job/log';
  import { BasicTable, useTable, TableAction } from '@/components/Table';
  import { downloadExcel } from '@/utils/file/download';
  import { columns, formSchemas } from './job.data';
  import { IconEnum } from '@/enums/appEnum';
  import JobDrawer from './JobDrawer.vue';
  import { useDrawer } from '@/components/Drawer';
  import { useRoute } from 'vue-router';
  import { onMounted } from 'vue';
  import { pick } from 'lodash-es';

  // 缓存
  defineOptions({ name: 'JobLog' });

  const currentRoute = useRoute();
  const jobId = currentRoute.params.jobId as string;
  onMounted(async () => {
    const resp = await jobInfo(jobId);
    // 赋值
    getForm().setFieldsValue(pick(resp, ['jobName', 'jobGroup']));
  });

  const [registerTable, { reload, selected, multipleRemove, getForm }] = useTable({
    rowSelection: {
      type: 'checkbox',
    },
    title: '定时任务日志列表',
    showIndexColumn: false,
    api: jobLogList,
    rowKey: 'jobId',
    useSearchForm: true,
    formConfig: {
      schemas: formSchemas,
      name: 'jobLogId',
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
      width: 180,
      title: '操作',
      key: 'action',
      fixed: 'right',
    },
  });

  const [registerPostDrawer, { openDrawer }] = useDrawer();

  function handlePreview(record: Recordable) {
    openDrawer(true, record.jobLogId);
  }
</script>

<style lang="less" scoped>
  /** 去掉表格padding */
  :deep(.vben-basic-table-form-container) {
    padding: 5px;
  }
</style>
