import { BasicColumn } from '@/components/Table';
import { useRender } from '@/hooks/component/useRender';
import { FormSchema } from '@/components/Form';
import { getDictOptions } from '@/utils/dict';
import { DictEnum } from '@/enums/dictEnum';
import { DescItem } from '@/components/Description';

const { renderDict, renderTooltip, renderBoldText, renderBrowserIcon, renderOsIcon } = useRender();
export const columns: BasicColumn[] = [
  // {
  //   title: 'ID',
  //   dataIndex: 'infoId',
  // },
  {
    title: '用户账号',
    dataIndex: 'userName',
  },
  {
    title: 'IP地址',
    dataIndex: 'ipaddr',
  },
  {
    title: 'IP地点',
    dataIndex: 'loginLocation',
    width: 200,
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
    title: '登录结果',
    dataIndex: 'status',
    customRender({ value }) {
      return renderDict(value, DictEnum.COMMON_STATUS);
    },
  },
  {
    title: '信息',
    dataIndex: 'msg',
    ellipsis: true,
    customRender({ value }) {
      return renderTooltip(value);
    },
  },
  {
    title: '日期',
    dataIndex: 'loginTime',
  },
];

export const formSchemas: FormSchema[] = [
  {
    field: 'ipaddr',
    label: 'IP地址',
    component: 'Input',
  },
  {
    field: 'userName',
    label: '用户账号',
    component: 'Input',
  },
  {
    field: 'status',
    label: '登录状态',
    component: 'Select',
    componentProps: {
      options: getDictOptions(DictEnum.COMMON_STATUS),
    },
  },
  {
    field: 'dateTime',
    label: '登录日期',
    component: 'RangePicker',
  },
];

export const modalSchemas: DescItem[] = [
  {
    field: 'status',
    label: '登录状态',
    labelMinWidth: 80,
    render(value) {
      return renderDict(value, DictEnum.COMMON_STATUS);
    },
  },
  {
    field: 'ipaddr',
    label: '账号信息',
    render(_, data) {
      const { userName, ipaddr, loginLocation } = data;
      return '账号: ' + userName + ' / ' + ipaddr + ' / ' + loginLocation;
    },
  },
  {
    field: 'loginTime',
    label: '登录时间',
  },
  {
    field: 'msg',
    label: '登录信息',
    render(_, data) {
      const { status, msg } = data;
      return renderBoldText(msg, status == '0' ? '' : 'text-red-500');
    },
  },
  {
    field: 'os',
    label: '登录设备',
    render(value) {
      return renderOsIcon(value);
    },
  },
  {
    field: 'browser',
    label: '浏览器',
    render(value) {
      return renderBrowserIcon(value);
    },
  },
];
