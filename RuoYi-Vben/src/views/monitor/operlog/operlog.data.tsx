import { BasicColumn } from '@/components/Table';
import { useRender } from '@/hooks/component/useRender';
import { FormSchema } from '@/components/Form';
import { getDictOptions } from '@/utils/dict';
import { DictEnum } from '@/enums/dictEnum';
import { DescItem } from '@/components/Description';

const { renderJsonPreview, renderHttpMethodTag, renderBoldText, renderDict, renderTag } =
  useRender();

export const columns: BasicColumn[] = [
  // {
  //   title: '日志编号',
  //   dataIndex: 'operId',
  // },
  {
    title: '系统模块',
    dataIndex: 'title',
  },
  {
    title: '操作类型',
    // 需要从字典获取
    dataIndex: 'businessType',
    customRender({ value }) {
      return renderDict(value, DictEnum.SYS_OPER_TYPE);
    },
  },
  {
    title: '操作人员',
    dataIndex: 'operName',
  },
  {
    title: 'IP地址',
    dataIndex: 'operIp',
  },
  {
    title: 'IP信息',
    dataIndex: 'operLocation',
  },
  {
    title: '操作状态',
    dataIndex: 'status',
    customRender({ value }) {
      return renderDict(value, DictEnum.COMMON_STATUS);
    },
  },
  {
    title: '操作日期',
    dataIndex: 'operTime',
    sorter: true,
  },
  {
    title: '操作耗时',
    dataIndex: 'costTime',
    sorter: true,
    customRender({ text }) {
      return `${text} ms`;
    },
  },
];

export const formSchemas: FormSchema[] = [
  {
    field: 'title',
    label: '系统模块',
    component: 'Input',
  },
  {
    field: 'operName',
    label: '操作人员',
    component: 'Input',
  },
  {
    field: 'businessType',
    label: '操作类型',
    component: 'Select',
    componentProps: {
      options: getDictOptions(DictEnum.SYS_OPER_TYPE),
    },
  },
  {
    field: 'operIp',
    label: '操作IP',
    component: 'Input',
  },
  {
    field: 'status',
    label: '状态',
    component: 'Select',
    componentProps: {
      options: getDictOptions(DictEnum.COMMON_STATUS),
    },
  },
  {
    field: 'createTime',
    label: '操作时间',
    component: 'RangePicker',
  },
];

export const descSchema: DescItem[] = [
  {
    field: 'operId',
    label: '日志编号',
  },
  {
    field: 'status',
    label: '操作结果',
    render(value) {
      return renderDict(value, DictEnum.COMMON_STATUS);
    },
  },
  {
    field: 'title',
    label: '操作模块',
    labelMinWidth: 80,
    render(value, { businessType }) {
      const operType = renderDict(businessType, DictEnum.SYS_OPER_TYPE);
      const moduleName = renderTag(value);
      // 改为tsx书写
      return (
        <span>
          {moduleName}
          {operType}
        </span>
      );
    },
  },
  {
    field: 'operIp',
    label: '操作信息',
    render(_, data) {
      return `账号: ${data.operName} / ${data.deptName} / ${data.operIp} / ${data.operLocation}`;
    },
  },
  {
    field: 'operUrl',
    label: '请求信息',
    render(_, data) {
      const { operUrl, requestMethod } = data;
      const methodTag = renderHttpMethodTag(requestMethod);
      return (
        <span>
          {methodTag} {operUrl}
        </span>
      );
      // return h('span', {}, [methodTag, operUrl]);
    },
  },
  {
    field: 'errorMsg',
    label: '异常信息',
    show: (data) => {
      return data && data.errorMsg;
    },
    render(value) {
      return renderBoldText(value, 'text-red-600');
    },
  },
  {
    field: 'method',
    label: '方法',
  },
  {
    field: 'operParam',
    label: '请求参数',
    render(value) {
      return renderJsonPreview(value);
    },
  },
  {
    field: 'jsonResult',
    label: '响应参数',
    show(data) {
      return data && data.jsonResult;
    },
    render(value) {
      return renderJsonPreview(value);
    },
  },
  {
    field: 'costTime',
    label: '耗时',
    render(value) {
      return `${value} ms`;
    },
  },
  {
    field: 'operTime',
    label: '操作时间',
  },
];
