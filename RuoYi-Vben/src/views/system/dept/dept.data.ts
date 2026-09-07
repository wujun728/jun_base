import { BasicColumn } from '@/components/Table';
import { DictEnum } from '@/enums/dictEnum';
import { getDictOptions } from '@/utils/dict';
import { FormSchema, commonRules } from '@/components/Form';
import { useRender } from '@/hooks/component/useRender';

const { renderDict } = useRender();
export const columns: BasicColumn[] = [
  {
    dataIndex: 'deptName',
    title: '部门名称',
  },
  {
    dataIndex: 'orderNum',
    title: '排序',
    width: 180,
  },
  {
    dataIndex: 'status',
    width: 180,
    title: '状态',
    customRender({ value }) {
      return renderDict(value, DictEnum.NORMAL_DISABLE);
    },
  },
  {
    dataIndex: 'createTime',
    title: '创建时间',
  },
];

export const formSchemas: FormSchema[] = [
  {
    field: 'deptName',
    label: '部门名称',
    component: 'Input',
  },
  {
    field: 'status',
    label: '部门状态',
    component: 'Select',
    componentProps: {
      options: getDictOptions(DictEnum.NORMAL_DISABLE),
    },
  },
];

export const modalSchemas: FormSchema[] = [
  {
    field: 'deptId',
    label: '部门ID',
    component: 'Input',
    show: false,
  },
  {
    field: 'parentId',
    label: '上级部门',
    required: true,
    defaultValue: 100,
    component: 'TreeSelect',
    show({ model }) {
      // 0为根节点
      return model.parentId !== 0;
    },
  },
  {
    field: 'deptName',
    label: '部门名称',
    component: 'Input',
    required: true,
  },
  {
    field: 'orderNum',
    label: '显示排序',
    component: 'InputNumber',
    required: true,
  },
  {
    field: 'leader',
    label: '负责人',
    component: 'Input',
  },
  {
    field: 'phone',
    label: '联系电话',
    component: 'Input',
    rules: commonRules('phone'),
  },
  {
    field: 'email',
    label: '邮箱地址',
    component: 'Input',
    rules: commonRules('email'),
  },
  {
    field: 'status',
    label: '部门状态',
    component: 'RadioButtonGroup',
    defaultValue: '0',
    componentProps: {
      options: getDictOptions(DictEnum.NORMAL_DISABLE),
    },
  },
];
