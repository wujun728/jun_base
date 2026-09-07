<template>
  <PageWrapper dense>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <Space>
          <a-button
            @click="downloadExcel(loginInfoExport, '登录日志', getForm().getFieldsValue())"
            v-auth="'monitor:logininfor:export'"
            >导出</a-button
          >
          <a-button @click="handleClean" v-auth="'monitor:logininfor:remove'">清空</a-button>
          <a-button
            type="primary"
            danger
            @click="multipleRemove(loginInfoRemove)"
            :disabled="!selected"
            v-auth="'monitor:logininfor:remove'"
            >删除</a-button
          >
          <a-button
            type="primary"
            color="success"
            :disabled="!canUnlock"
            @click="handleUnlock"
            v-auth="'monitor:logininfor:unlock'"
            >解锁</a-button
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
                auth: 'monitor:logininfor:query',
                onClick: handleOpenModal.bind(null, record),
              },
              {
                label: '删除',
                icon: IconEnum.DELETE,
                type: 'primary',
                danger: true,
                ghost: true,
                auth: 'monitor:logininfor:remove',
                popConfirm: {
                  placement: 'left',
                  title: `是否删除[${record.infoId}]-${record.userName}的登录日志?`,
                  confirm: handleDelete.bind(null, record),
                },
              },
            ]"
          />
        </template>
      </template>
    </BasicTable>
    <LoginInfoModal @register="registerModal" />
  </PageWrapper>
</template>

<script setup lang="ts">
  import { PageWrapper } from '@/components/Page';
  import { BasicTable, useTable, TableAction } from '@/components/Table';
  import { Space } from 'ant-design-vue';
  import {
    loginInfoList,
    loginInfoExport,
    loginInfoRemove,
    userUnlock,
    loginInfoClean,
  } from '@/api/monitor/logininfo';
  import { useMessage } from '@/hooks/web/useMessage';
  import { formSchemas, columns } from './login.data';
  import { downloadExcel } from '@/utils/file/download';
  import { IconEnum } from '@/enums/appEnum';
  import { useModal } from '@/components/Modal';
  import LoginInfoModal from './LoginInfoModal.vue';
  import { computed, ref } from 'vue';

  defineOptions({ name: 'Logininfor' });

  /**
   * 是否符合解锁条件
   * 只选中1个
   * 登录状态为失败
   */
  const canUnlock = computed(() => {
    const rows = getSelectRows();
    return rows.length === 1 && rows[0].status === '1';
  });

  const [
    registerTable,
    { reload, getSelectRows, clearSelectedRowKeys, multipleRemove, selected, getForm },
  ] = useTable({
    rowSelection: {
      type: 'checkbox',
    },
    title: '登录日志列表',
    showIndexColumn: false,
    api: loginInfoList,
    rowKey: 'infoId',
    clickToRowSelect: true,
    useSearchForm: true,
    formConfig: {
      schemas: formSchemas,
      // 日期选择格式化
      fieldMapToTime: [
        [
          'dateTime',
          ['params[beginTime]', 'params[endTime]'],
          ['YYYY-MM-DD 00:00:00', 'YYYY-MM-DD 23:59:59'],
        ],
      ],
      baseColProps: {
        xs: 24,
        sm: 24,
        md: 24,
        lg: 6,
      },
    },
    columns: columns,
    actionColumn: {
      title: '操作',
      key: 'action',
      fixed: 'right',
    },
  });

  const [registerModal, { openModal }] = useModal();
  function handleOpenModal(record: Recordable) {
    openModal(true, record);
  }

  function handleUnlock() {
    const rows = getSelectRows();
    if (rows.length !== 1) {
      return;
    }
    const record = rows[0];
    createConfirm({
      iconType: 'warning',
      title: '提示',
      content: `是否解锁账号[${record.userName}]?`,
      onOk: async () => {
        await userUnlock(record.userName);
        await reload();
      },
    });
  }

  async function handleDelete(record: Recordable) {
    await loginInfoRemove([record.infoId]);
    await reload();
  }

  const { createConfirm } = useMessage();
  function handleClean() {
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
      content: '是否清空登录日志?',
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
        await loginInfoClean();
        clearSelectedRowKeys();
        await reload();
      },
      onCancel() {
        clearInterval(interval);
      },
    });
  }
</script>

<style scoped></style>
