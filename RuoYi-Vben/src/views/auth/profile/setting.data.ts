import { DescItem } from '@/components/Description';
import { FormSchema, commonRules } from '@/components/Form';

// 基础设置 form
export const baseSetschemas: FormSchema[] = [
  {
    field: 'userId',
    component: 'Input',
    required: true,
    label: '用户ID',
    show: false,
  },
  {
    field: 'nickName',
    component: 'Input',
    required: true,
    label: '昵称',
    colProps: { span: 18 },
  },
  {
    field: 'email',
    component: 'Input',
    rules: commonRules('email', true),
    label: '邮箱',
    colProps: { span: 18 },
  },
  {
    field: 'phonenumber',
    component: 'Input',
    rules: commonRules('phone', true),
    label: '联系电话',
    colProps: { span: 18 },
  },
  {
    field: 'sex',
    component: 'RadioButtonGroup',
    componentProps: {
      // 这里直接用字典会在登录页请求(未授权下)报错
      // options: getDictOptions(DictEnum.SYS_USER_SEX),
      options: [
        {
          label: '男',
          value: '0',
        },
        {
          label: '女',
          value: '1',
        },
      ],
    },
    label: '性别',
    colProps: { span: 18 },
  },
];

export const descSchemas: DescItem[] = [
  {
    label: '账号',
    field: 'userName',
  },
  {
    label: '手机号码',
    field: 'phonenumber',
    render(value) {
      return value || '暂未设置手机号';
    },
  },
  {
    label: '邮箱',
    field: 'email',
    render(value) {
      return value || '暂未设置邮箱';
    },
  },
  {
    label: '部门',
    field: 'deptName',
    render(_, data) {
      if (data) {
        const postGroup = data.postGroup || '暂无岗位信息';
        let deptName = data.deptName || '暂无部门信息';
        // 为了兼容 升级后去掉
        if (data.dept && data.dept.deptName) {
          deptName = data.dept.deptName;
        }
        return deptName + ' / ' + postGroup;
      }
      return '';
    },
  },
  {
    label: '角色',
    field: 'roleGroup',
  },
  {
    label: '登录IP',
    field: 'loginIp',
  },
  {
    label: '上次登录时间',
    field: 'loginDate',
  },
];
