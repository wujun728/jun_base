import { BasicColumn } from '@/components/Table';
import { useRender } from '@/hooks/component/useRender';
import { FormSchema } from '@/components/Form';

const { renderDateTime, renderBrowserIcon, renderOsIcon } = useRender();
export const columns: BasicColumn[] = [
  // {
  //   title: '会话ID',
  //   dataIndex: 'tokenId',
  // },
  {
    title: '登录账号',
    dataIndex: 'userName',
  },
  {
    title: '部门名称',
    dataIndex: 'deptName',
  },
  {
    title: 'IP地址',
    dataIndex: 'ipaddr',
  },
  {
    title: '登录地址',
    dataIndex: 'loginLocation',
  },
  {
    title: '浏览器',
    dataIndex: 'browser',
    customRender({ value }) {
      return renderBrowserIcon(value, true);
    },
  },
  {
    title: '系统',
    dataIndex: 'os',
    customRender({ value }) {
      // Windows 10 or Windows Server 2016 太长了 分割一下 详情依旧能看到详细的
      // 为什么不直接保存userAgent让前端解析 很奇怪
      if (value) {
        const split = value.split(' or ');
        if (split.length === 2) {
          value = split[0];
        }
      }
      return renderOsIcon(value, true);
    },
  },
  {
    title: '登录时间',
    dataIndex: 'loginTime',
    customRender({ value }) {
      return renderDateTime(value);
    },
  },
];

export const formSchemas: FormSchema[] = [
  {
    label: 'IP地址',
    field: 'ipaddr',
    component: 'Input',
  },
  {
    label: '用户账号',
    field: 'userName',
    component: 'Input',
  },
];
