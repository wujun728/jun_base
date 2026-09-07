<template>
  <PageWrapper dense>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <Space>
          <a-button @click="handleDeleteAll" v-auth="'monitor:operlog:remove'">清空</a-button>
          <a-button
            class="<sm:hidden"
            v-auth="'monitor:operlog:export'"
            @click="downloadExcel(operLogExport, '操作日志', getForm().getFieldsValue())"
            >导出</a-button
          >
          <a-button
            class="<sm:hidden"
            type="primary"
            danger
            :disabled="!selected"
            @click="multipleRemove(operLogDelete)"
            v-auth="'monitor:operlog:remove'"
            >删除</a-button
          >
        </Space>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <TableAction
            stopButtonPropagation
            :actions="[
              {
                label: '详情',
                icon: IconEnum.DETAIL,
                type: 'primary',
                ghost: true,
                onClick: handleCheck.bind(null, record),
              },
            ]"
          />
        </template>
      </template>
    </BasicTable>
    <InfoDrawer @register="registerDrawer" />
  </PageWrapper>
</template>

<script setup lang="ts">
  import { PageWrapper } from '@/components/Page';
  import { BasicTable, useTable, TableAction } from '@/components/Table';
  import { operLogList, operLogExport, operLogDelete, operLogClean } from '@/api/monitor/operlog';
  import { useDrawer } from '@/components/Drawer';
  import { Space } from 'ant-design-vue';
  import { useMessage } from '@/hooks/web/useMessage';
  import { downloadExcel } from '@/utils/file/download';
  import { formSchemas, columns } from './operlog.data';
  import { IconEnum } from '@/enums/appEnum';
  import InfoDrawer from './InfoDrawer.vue';
  import { ref } from 'vue';

  defineOptions({ name: 'Operlog' });

  const [registerDrawer, { openDrawer }] = useDrawer();

  const [registerTable, { multipleRemove, reload, selected, getForm }] = useTable({
    rowSelection: {
      type: 'checkbox',
    },
    title: '操作日志列表',
    showIndexColumn: false,
    api: operLogList,
    rowKey: 'operId',
    useSearchForm: true,
    formConfig: {
      schemas: formSchemas,
      labelWidth: 80,
      baseColProps: {
        xs: 24,
        sm: 24,
        md: 24,
        lg: 6,
      },
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
      width: 120,
      title: '操作',
      key: 'action',
      fixed: 'right',
    },
  });

  function handleCheck(record: Recordable) {
    openDrawer(true, record);
  }

  const { createConfirm } = useMessage();

  function handleDeleteAll() {
    /** 多少秒后能进行确认 */
    let secondsToRemove = ref<number>(5);
    /** 设置定时器 在0的时候自己清除
     *  或者点击取消也会进行清除
     *  或者地址栏刷新页面  也会清除。。。
     */
    const interval = setInterval(() => {
      secondsToRemove.value--;
      if (secondsToRemove.value === 0) {
        modalInstance.update({
          okButtonProps: {
            disabled: false,
            type: 'primary',
            danger: true,
          },
        });
        clearInterval(interval);
      }
    }, 1000);
    const modalInstance = createConfirm({
      iconType: 'warning',
      title: '提示',
      content: '是否要清空全部日志内容?',
      okButtonProps: {
        disabled: true,
      },
      okText() {
        if (secondsToRemove.value === 0) {
          return '确认';
        }
        return `等待${secondsToRemove.value}秒`;
      },
      onOk: async () => {
        await operLogClean();
        await reload();
      },
      onCancel() {
        clearInterval(interval);
      },
    });
  }
</script>

<style scoped></style>
