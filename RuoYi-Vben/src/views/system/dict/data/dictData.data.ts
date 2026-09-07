import { BasicColumn } from '@/components/Table';
import { useRender } from '@/hooks/component/useRender';
import { FormSchema } from '@/components/Form';
import { dictOptionSelectList } from '@/api/system/dict/dictType';

const { renderDictTag } = useRender();
export const columns: BasicColumn[] = [
  // {
  //   title: '字典CODE',
  //   dataIndex: 'dictCode',
  // },
  {
    title: '字典标签',
    dataIndex: 'dictLabel',
  },
  {
    title: '标签预览',
    dataIndex: 'cssClass',
    customRender({ record }) {
      const { dictValue } = record;
      return renderDictTag(dictValue, [record as any]);
    },
  },
  {
    title: '字典键值',
    dataIndex: 'dictValue',
  },
  {
    title: '字典排序',
    dataIndex: 'dictSort',
  },
  {
    title: '备注',
    dataIndex: 'remark',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
  },
];

export const formSchemas: FormSchema[] = [
  {
    label: '字典类型',
    field: 'dictType',
    component: 'ApiSelect',
    componentProps: {
      api: dictOptionSelectList,
      labelField: 'dictName',
      valueField: 'dictType',
    },
  },
  {
    label: '字典标签',
    field: 'dictLabel',
    component: 'Input',
  },
];

// modal
export const modalSchemas: FormSchema[] = [
  {
    label: '字典CODE',
    field: 'dictCode',
    component: 'Input',
    show: false,
  },
  {
    label: '字典类型',
    field: 'dictType',
    component: 'Input',
    componentProps: {
      disabled: true,
    },
  },
  {
    label: '标签样式',
    field: 'listClass',
    slot: 'listClass',
  },
  {
    label: '数据标签',
    field: 'dictLabel',
    component: 'Input',
    required: true,
  },
  {
    label: '数据键值',
    field: 'dictValue',
    component: 'Input',
    required: true,
  },
  {
    label: 'css类名',
    field: 'cssClass',
    helpMessage: [
      '标签的css样式, 可添加已经编译的css类名',
      '可添加前置/后置小点效果 前置:dot-before-color 后置: dot-after-color',
      '可使用unocss类名, 需要在src/views/system/dict/data/unocss.vue先注册',
    ],
    component: 'InputTextArea',
    componentProps: {
      placeholder: '可使用unocss类名 如bg-blue w-full h-full等',
    },
  },
  {
    label: '显示排序',
    field: 'dictSort',
    component: 'InputNumber',
    required: true,
  },
  {
    label: '备注',
    field: 'remark',
    component: 'InputTextArea',
  },
];
