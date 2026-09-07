// import { Rule, RuleObject } from 'ant-design-vue/lib/form/interface';
import { Rule } from './types/form';

const PHONE_PATTERN = /^1[3,4,5,6,7,8,9][0-9]{9}$/;

type ruleNames = 'phone' | 'email';

export function commonRules(
  name: ruleNames,
  required = false,
  trigger: 'blur' | 'change' | ['change', 'blur'] = 'change',
) {
  const phone: Rule[] = [
    { required, message: '请输入正确的手机号码', pattern: PHONE_PATTERN, trigger },
  ];
  const email: Rule[] = [{ required, message: '请输入正确的邮箱地址', type: 'email', trigger }];
  switch (name) {
    case 'phone':
      return phone;
    case 'email':
      return email;
  }
}
