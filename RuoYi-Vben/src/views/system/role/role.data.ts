import { BasicColumn } from '@/components/Table';
import { DictEnum } from '@/enums/dictEnum';
import { getDictOptions } from '@/utils/dict';
import { FormSchema } from '@/components/Form';
import { useRender } from '@/hooks/component/useRender';

const { renderTag } = useRender();
export const columns: BasicColumn[] = [
  {
    title: '角色名称',
    dataIndex: 'roleName',
  },
  {
    title: '权限字符',
    dataIndex: 'roleKey',
    customRender({ value }) {
      return renderTag(value, 'processing');
    },
  },
  {
    title: '数据权限',
    dataIndex: 'dataScope',
    customRender({ value }) {
      const found = authScopeOptions.find((item) => item.value === value);
      if (found) {
        return renderTag(found.label, found.color);
      }
      return renderTag(value);
    },
  },
  {
    title: '排序',
    dataIndex: 'roleSort',
  },
  {
    title: '状态',
    dataIndex: 'status',
    // customRender({ record }) {
    //   return renderSwitch(record, roleChangeStatus, (newStatusStr) => {
    //     return `确认要${newStatusStr}角色[${record.roleName}]吗?`;
    //   });
    // },
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
  },
];

export const formSchemas: FormSchema[] = [
  {
    label: '角色名称',
    field: 'roleName',
    component: 'Input',
  },
  {
    label: '权限字符',
    field: 'roleKey',
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
  {
    label: '创建时间',
    field: 'createTime',
    component: 'RangePicker',
  },
];

export const modalSchemas: FormSchema[] = [
  {
    label: '角色ID',
    field: 'roleId',
    component: 'Input',
    show: false,
  },
  {
    label: '角色名称',
    field: 'roleName',
    component: 'Input',
    required: true,
  },
  {
    label: '权限标识',
    field: 'roleKey',
    helpMessage: ['如: test simpleUser等'],
    component: 'Input',
    required: true,
  },
  {
    label: '角色排序',
    field: 'roleSort',
    component: 'InputNumber',
    required: true,
    colProps: {
      lg: 11,
      md: 24,
    },
  },
  {
    label: '角色状态',
    field: 'status',
    helpMessage: '修改后, 拥有该角色的用户将自动下线.',
    defaultValue: '0',
    component: 'Select',
    required: true,
    componentProps: {
      allowClear: false,
      options: getDictOptions(DictEnum.NORMAL_DISABLE),
    },
    colProps: {
      lg: {
        span: 11,
        offset: 2,
      },
      md: 24,
    },
  },
  {
    label: '菜单权限',
    field: 'menuIds',
    slot: 'menuTree',
    defaultValue: [],
    // component: 'Input',
  },
  {
    label: '备注',
    field: 'remark',
    defaultValue: '',
    component: 'InputTextArea',
  },
];

/**
 * authScopeOptions user也会用到
 */
export const authScopeOptions = [
  { label: '全部数据权限', value: '1', color: 'green' },
  { label: '自定数据权限', value: '2', color: 'default' },
  { label: '本部门数据权限', value: '3', color: 'orange' },
  { label: '本部门及以下数据权限', value: '4', color: 'cyan' },
  { label: '仅本人数据权限', value: '5', color: 'error' },
];

export const authSchemas: FormSchema[] = [
  {
    field: 'roleId',
    label: '角色ID',
    component: 'Input',
    show: false,
  },
  {
    label: '角色名称',
    field: 'roleName',
    component: 'Input',
    componentProps: {
      disabled: true,
    },
  },
  {
    label: '权限标识',
    field: 'roleKey',
    component: 'Input',
    componentProps: {
      disabled: true,
    },
  },
  {
    label: '权限范围',
    field: 'dataScope',
    helpMessage: '更改后需要用户重新登录才能生效',
    component: 'Select',
    componentProps: {
      allowClear: false,
      options: authScopeOptions,
      getPopupContainer: (triggerNode) => triggerNode.parentNode,
    },
  },
  {
    label: '部门权限',
    field: 'deptIds',
    slot: 'deptTree',
    helpMessage: '更改后立即生效',
    defaultValue: [],
    // component: 'Input',
    show({ values }) {
      // 为自定义的时候才显示
      return values.dataScope === '2';
    },
  },
];

// 租户管理 不可分配  只有superadmin有权限操作 分配了也没用
export const excludeIds = [6, 121, 122, 1606, 1607, 1608, 1609, 1610, 1611, 1612, 1613, 1614, 1615];
