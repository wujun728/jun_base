import { BasicColumn } from '@/components/Table';
import { useRender } from '@/hooks/component/useRender';
import { FormSchema } from '@/components/Form';
import { getDictOptions } from '@/utils/dict';
import { DictEnum } from '@/enums/dictEnum';

export const { renderDict } = useRender();
export const columns: BasicColumn[] = [
  // {
  //   title: '主键ID',
  //   dataIndex: 'configId',
  // },
  {
    title: '参数名称',
    dataIndex: 'configName',
  },
  {
    title: '参数KEY',
    dataIndex: 'configKey',
  },
  {
    title: '参数Value',
    dataIndex: 'configValue',
  },
  {
    title: '系统内置',
    dataIndex: 'configType',
    width: 120,
    customRender({ value }) {
      return renderDict(value, DictEnum.YES_NO);
    },
  },
  {
    title: '备注',
    dataIndex: 'remark',
    ellipsis: true,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
  },
];

export const formSchemas: FormSchema[] = [
  {
    label: '参数名称',
    field: 'configName',
    component: 'Input',
  },
  {
    label: '参数键名',
    field: 'configKey',
    component: 'Input',
  },
  {
    label: '系统内置',
    field: 'configType',
    component: 'Select',
    componentProps: {
      options: getDictOptions(DictEnum.YES_NO),
    },
  },
  {
    label: '创建时间',
    field: 'createTime',
    component: 'RangePicker',
  },
];

export const modalSchemas: FormSchema[] = [
  {
    field: 'configId',
    label: '主键ID',
    component: 'Input',
    show: false,
  },
  {
    field: 'configName',
    label: '参数名称',
    required: true,
    component: 'Input',
  },
  {
    field: 'configKey',
    label: '参数键名',
    component: 'Input',
    required: true,
  },
  {
    field: 'configValue',
    label: '参数键值',
    component: 'Input',
    required: true,
  },
  {
    field: 'configType',
    label: '系统内置',
    component: 'RadioButtonGroup',
    defaultValue: 'Y',
    componentProps: {
      options: getDictOptions(DictEnum.YES_NO),
    },
  },
  {
    field: 'remark',
    label: '备注',
    component: 'InputTextArea',
  },
];
