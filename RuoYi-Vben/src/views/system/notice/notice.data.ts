import { BasicColumn } from '@/components/Table';
import { useRender } from '@/hooks/component/useRender';
import { FormSchema } from '@/components/Form';
import { getDictOptions } from '@/utils/dict';
import { DictEnum } from '@/enums/dictEnum';

const { renderDict } = useRender();
export const columns: BasicColumn[] = [
  {
    title: '公告标题',
    dataIndex: 'noticeTitle',
  },
  {
    title: '公告类型',
    dataIndex: 'noticeType',
    width: 120,
    customRender: ({ value }) => {
      return renderDict(value, DictEnum.NOTICE_TYPE);
    },
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 120,
    customRender: ({ value }) => {
      return renderDict(value, DictEnum.NOTICE_STATUS);
    },
  },
  {
    title: '创建人',
    dataIndex: 'createByName',
    width: 150,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
  },
];

export const formSchemas: FormSchema[] = [
  {
    field: 'noticeTitle',
    label: '公告标题',
    component: 'Input',
  },
  {
    field: 'createBy',
    label: '创建人',
    component: 'Input',
  },
  {
    field: 'noticeType',
    label: '公告类型',
    component: 'Select',
    componentProps: {
      options: getDictOptions(DictEnum.NOTICE_TYPE),
    },
  },
];

export const modalSchemas: FormSchema[] = [
  {
    label: '公告ID',
    field: 'noticeId',
    component: 'Input',
    show: false,
  },
  {
    label: '公告标题',
    field: 'noticeTitle',
    component: 'Input',
    required: true,
    colProps: { span: 24 },
  },
  {
    label: '公告状态',
    field: 'status',
    component: 'Select',
    defaultValue: '0',
    required: true,
    componentProps: {
      allowClear: false,
      options: getDictOptions(DictEnum.NOTICE_STATUS),
    },
    colProps: { span: 11 },
  },
  {
    label: '公告类型',
    field: 'noticeType',
    defaultValue: '1',
    component: 'Select',
    required: true,
    componentProps: {
      allowClear: false,
      options: getDictOptions(DictEnum.NOTICE_TYPE),
    },
    colProps: { span: 11, offset: 2 },
  },
  {
    field: 'noticeContent',
    label: '公告内容',
    component: 'RichTextarea',
    required: true,
    defaultValue: '',
    colProps: { span: 24 },
  },
];
