import { BasicColumn } from '@/components/Table';
import { DictEnum } from '@/enums/dictEnum';
import { getDictOptions } from '@/utils/dict';
import { FormSchema } from '@/components/Form';
import { DescItem } from '@/components/Description';
import { useRender } from '@/hooks/component/useRender';

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
      options: getDictOptions(DictEnum.JOB_STATUS),
    },
  },
];

export const columns: BasicColumn[] = [
  {
    title: '任务编号',
    dataIndex: 'jobId',
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
    title: 'cron表达式',
    dataIndex: 'cronExpression',
  },
  {
    title: '状态',
    dataIndex: 'status',
  },
];

export const modalSchemas: FormSchema[] = [
  {
    label: '任务编号',
    field: 'jobId',
    component: 'Input',
    show: false,
  },
  {
    label: '任务名称',
    field: 'jobName',
    component: 'Input',
    required: true,
  },
  {
    label: '任务分组',
    field: 'jobGroup',
    component: 'Select',
    required: true,
    componentProps: {
      options: getDictOptions(DictEnum.JOB_GROUP),
    },
  },
  {
    label: '调用方法',
    field: 'invokeTarget',
    component: 'Input',
    required: true,
  },
  {
    label: 'cron表达式',
    field: 'cronExpression',
    component: 'Input',
    required: true,
  },
  {
    label: '任务状态',
    field: 'status',
    defaultValue: '0',
    component: 'RadioButtonGroup',
    required: true,
    componentProps: {
      options: getDictOptions(DictEnum.JOB_STATUS),
    },
  },
  {
    label: '执行策略',
    field: 'misfirePolicy',
    component: 'RadioButtonGroup',
    defaultValue: '1',
    componentProps: {
      options: [
        { label: '立即执行', value: '1' },
        { label: '执行一次', value: '2' },
        { label: '放弃执行', value: '3' },
      ],
    },
  },
  {
    label: '是否并发',
    field: 'concurrent',
    component: 'RadioButtonGroup',
    defaultValue: '0',
    componentProps: {
      options: [
        { label: '允许', value: '0' },
        { label: '禁止', value: '1' },
      ],
    },
  },
];

const { renderDict } = useRender();
export const descSchemas: DescItem[] = [
  {
    label: '任务编号',
    field: 'jobId',
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
    label: 'cron表达式',
    field: 'cronExpression',
  },
  {
    label: '执行时间',
    field: 'createTime',
  },
  {
    label: '下次执行时间',
    field: 'nextValidTime',
  },
  {
    label: '任务状态',
    field: 'status',
    render(value) {
      return renderDict(value, DictEnum.COMMON_STATUS);
    },
  },
  {
    label: '是否并发',
    field: 'concurrent',
    render(value) {
      const options = [
        { label: '允许', value: '0' },
        { label: '禁止', value: '1' },
      ];
      return options.find((item) => item.value === value)?.label;
    },
  },
  {
    label: '执行策略',
    field: 'misfirePolicy',
    render(value) {
      const options = [
        { label: '立即执行', value: '1' },
        { label: '执行一次', value: '2' },
        { label: '放弃执行', value: '3' },
      ];
      return options.find((item) => item.value === value)?.label;
    },
  },
];
