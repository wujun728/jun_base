import { BasicColumn } from '@/components/Table';
import { FormSchema, commonRules } from '@/components/Form';
import { getDictOptions } from '@/utils/dict';
import { DictEnum } from '@/enums/dictEnum';
import { useRender } from '@/hooks/component/useRender';
import { IconEnum } from '@/enums/appEnum';

export const columns: BasicColumn[] = [
  {
    dataIndex: 'userName',
    title: '名称',
  },
  {
    dataIndex: 'nickName',
    title: '昵称',
  },
  {
    dataIndex: 'avatar',
    title: '头像',
    width: 80,
  },
  {
    dataIndex: 'deptName',
    title: '部门',
    /** 为了兼容 升级后可以去掉customRender */
    customRender({ value, record }) {
      if (record.dept && record.dept.deptName) {
        return record.dept.deptName;
      }
      return value;
    },
  },
  {
    dataIndex: 'phonenumber',
    title: '手机号',
    customRender({ value }) {
      return value || '暂无';
    },
  },
  {
    dataIndex: 'status',
    title: '状态',
  },
  // {
  //   dataIndex: 'status',
  //   title: '状态',
  //   customRender({ record }) {
  //     return renderSwitch(record, userStatusChange, (newStatusStr) => {
  //       return `确认${newStatusStr}用户[${record.userName}]吗?`;
  //     });
  //   },
  // },
  {
    dataIndex: 'createTime',
    title: '创建时间',
  },
];

export const schemas: FormSchema[] = [
  {
    field: 'userName',
    label: '用户账号',
    component: 'Input',
  },
  {
    field: 'phonenumber',
    label: '手机号码',
    component: 'Input',
    rules: commonRules('phone'),
  },
  {
    field: 'status',
    label: '用户状态',
    component: 'Select',
    componentProps: {
      options: getDictOptions(DictEnum.NORMAL_DISABLE),
    },
  },
  {
    field: 'createTime',
    label: '创建时间',
    component: 'RangePicker',
  },
];

const { renderIcon } = useRender();
export const drawerSchemas: FormSchema[] = [
  {
    label: '用户ID',
    field: 'userId',
    component: 'Input',
    show: false,
  },
  {
    label: '用户账号',
    field: 'userName',
    component: 'Input',
    required: true,
  },
  {
    label: '用户密码',
    field: 'password',
    component: 'StrengthMeter',
    componentProps: {
      placeholder: '输入密码, 长度5-20',
    },
    rules: [{ required: true, message: '输入密码, 长度5-20', min: 5, max: 20, trigger: 'blur' }],
  },
  {
    label: '用户昵称',
    field: 'nickName',
    component: 'Input',
    required: true,
  },
  {
    label: '归属部门',
    field: 'deptId',
    required: true,
    component: 'TreeSelect',
    helpMessage: ['注意绑定的角色要带相应的部门权限, 否则无法正常登录'],
  },
  {
    label: '手机号',
    field: 'phonenumber',
    component: 'Input',
    rules: commonRules('phone'),
    renderComponentContent() {
      return {
        addonBefore: () => renderIcon(IconEnum.PHONE),
      };
    },
  },
  {
    label: '邮箱',
    field: 'email',
    component: 'Input',
    rules: commonRules('email'),
    /**
     * 问题原因：
       Vue3 使用h函数 推荐使用函数式插槽，以便获得更佳的性能。
       https://blog.csdn.net/weixin_44575130/article/details/124427109
     * Non-function value encountered for slot "addonBefore". Prefer function slots for better performance.
     * @returns
     */
    renderComponentContent() {
      return {
        addonBefore: () => renderIcon(IconEnum.EMAIL),
      };
    },
  },
  {
    label: '用户性别',
    field: 'sex',
    defaultValue: '0',
    component: 'RadioButtonGroup',
    componentProps: {
      options: getDictOptions(DictEnum.SYS_USER_SEX),
    },
    colProps: { span: 12 },
  },
  {
    label: '状态',
    field: 'status',
    defaultValue: '0',
    component: 'RadioButtonGroup',
    componentProps: {
      options: getDictOptions(DictEnum.NORMAL_DISABLE),
    },
    colProps: { span: 12 },
  },
  {
    label: '岗位',
    field: 'postIds',
    slot: 'postIds',
  },
  {
    label: '角色',
    field: 'roleIds',
    slot: 'roleIds',
  },
  {
    label: '备注',
    field: 'remark',
    component: 'InputTextArea',
    componentProps: {
      rows: 3,
    },
  },
];

export const resetPasswordSchemas: FormSchema[] = [
  {
    field: 'userId',
    component: 'Input',
    label: '用户ID',
    defaultValue: -1,
    required: true,
    show: false,
  },
  {
    field: 'password',
    component: 'StrengthMeter',
    label: '新的密码',
    componentProps: {
      placeholder: '请输入新的密码, 密码长度为5 - 20',
    },
    rules: [{ required: true, min: 5, max: 20, message: '密码长度为5 - 20' }],
  },
];
