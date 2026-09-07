import { BasicColumn } from '@/components/Table';
import { DictEnum } from '@/enums/dictEnum';
import { getDictOptions } from '@/utils/dict';
import { FormSchema } from '@/components/Form';
import { useRender } from '@/hooks/component/useRender';

export const formSchemas: FormSchema[] = [
  {
    label: '岗位编码',
    field: 'postCode',
    component: 'Input',
  },
  {
    label: '岗位名称',
    field: 'postName',
    component: 'Input',
  },
  {
    label: '状态',
    field: 'status',
    component: 'Select',
    componentProps: {
      options: getDictOptions(DictEnum.NORMAL_DISABLE),
    },
  },
];

const { renderDict } = useRender();
export const columns: BasicColumn[] = [
  // {
  //   title: '岗位编号',
  //   dataIndex: 'postId',
  // },
  {
    title: '岗位编码',
    dataIndex: 'postCode',
  },
  {
    title: '类别编码',
    dataIndex: 'postCategory',
  },
  {
    title: '岗位名称',
    dataIndex: 'postName',
  },
  {
    title: '排序',
    dataIndex: 'postSort',
  },
  {
    title: '状态',
    dataIndex: 'status',
    customRender: ({ value }) => renderDict(value, DictEnum.NORMAL_DISABLE),
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
  },
];

export const modalSchemas: FormSchema[] = [
  {
    label: '岗位编号',
    field: 'postId',
    component: 'Input',
    show: false,
  },
  {
    label: '岗位名称',
    field: 'postName',
    component: 'Input',
    required: true,
  },
  {
    label: '岗位编码',
    field: 'postCode',
    component: 'Input',
    required: true,
  },
  {
    label: '岗位排序',
    field: 'postSort',
    component: 'InputNumber',
    required: true,
  },
  {
    label: '岗位状态',
    field: 'status',
    defaultValue: '0',
    component: 'RadioButtonGroup',
    required: true,
    componentProps: {
      options: getDictOptions(DictEnum.NORMAL_DISABLE),
    },
  },
  {
    label: '备注',
    field: 'remark',
    component: 'InputTextArea',
  },
];
