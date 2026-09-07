import { BasicColumn } from '@/components/Table';
import { DictEnum } from '@/enums/dictEnum';
import { getDictOptions } from '@/utils/dict';
import { FormSchema } from '@/components/Form';
import { useRender } from '@/hooks/component/useRender';

import { DescItem } from '@/components/Description';

export const formSchemas: FormSchema[] = [
  {
    label: '任务名称',
    field: 'jobName',
    component: 'Input',
  },
  {
    label: '任务组名',
    field: 'jobGroup',
    component: 'Input',
  },
  {
    label: '状态',
    field: 'status',
    component: 'Select',
    componentProps: {
      options: getDictOptions(DictEnum.COMMON_STATUS),
    },
  },
];

const { renderDict } = useRender();
export const columns: BasicColumn[] = [
  {
    title: '日志编号',
    dataIndex: 'jobLogId',
  },
  {
    title: '任务名称',
    dataIndex: 'jobName',
  },
  {
    title: '任务组名',
    dataIndex: 'jobGroup',
  },
  {
    title: 'target',
    dataIndex: 'invokeTarget',
  },
  {
    title: '日志信息',
    dataIndex: 'jobMessage',
  },
  {
    title: '状态',
    dataIndex: 'status',
    customRender({ value }) {
      return renderDict(value, DictEnum.COMMON_STATUS);
    },
  },
  {
    title: '执行时间',
    dataIndex: 'createTime',
  },
];

export const descSchemas: DescItem[] = [
  {
    label: '任务编号',
    field: 'jobLogId',
  },
  {
    label: '任务名称',
    field: 'jobName',
  },
  {
    label: '任务分组',
    field: 'jobGroup',
  },
  {
    label: '调用方法',
    field: 'invokeTarget',
  },
  {
    label: '日志信息',
    field: 'jobMessage',
  },
  {
    label: '任务状态',
    field: 'status',
    render(value) {
      return renderDict(value, DictEnum.COMMON_STATUS);
    },
  },
  {
    label: '执行时间',
    field: 'createTime',
  },
];
