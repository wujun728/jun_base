import { BasicColumn } from '@/components/Table';
import { useRender } from '@/hooks/component/useRender';
import { FormSchema } from '@/components/Form';
import { DictEnum } from '@/enums/dictEnum';

const { renderDict } = useRender();

export const columns: BasicColumn[] = [
  {
    title: '用户账号',
    dataIndex: 'userName',
  },
  {
    title: '用户昵称',
    dataIndex: 'nickName',
  },
  {
    title: '邮箱',
    dataIndex: 'email',
  },
  {
    title: '手机号',
    dataIndex: 'phonenumber',
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 100,
    customRender({ value }) {
      return renderDict(value, DictEnum.NORMAL_DISABLE);
    },
  },
  // {
  //   title: '创建时间',
  //   dataIndex: 'createTime',
  // },
];

export const formSchemas: FormSchema[] = [
  {
    label: '用户账号',
    field: 'userName',
    component: 'Input',
  },
  {
    label: '手机号码',
    field: 'phonenumber',
    component: 'Input',
  },
];
