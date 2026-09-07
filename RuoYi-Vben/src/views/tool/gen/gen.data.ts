import { FormSchema } from '@/components/Form';
import { BasicColumn } from '@/components/Table';

export const columns: BasicColumn[] = [
  {
    title: '表名称',
    dataIndex: 'tableName',
  },
  {
    title: '表描述',
    dataIndex: 'tableComment',
  },
  {
    title: '实体类',
    dataIndex: 'className',
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

export const formSchemas: FormSchema[] = [
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
  {
    label: '创建时间',
    field: 'createTime',
    component: 'RangePicker',
  },
];
