import uniqueId from '@form-create/utils/lib/unique';
import { makeOptionsRule, makeRequiredRule } from '../../utils';

const label = '客户管理说明';
const name = 'holidaySetting';

export default {
  icon: 'iconfont iconxuanzeqi',
  label,
  name,
  rule() {
    return {
      type: name,
      field: uniqueId(),
      title: label,
      info: '',
      effect: {
        fetch: '',
      },
      checkType: 0,
    };
  },
  props() {
    // makeOptionsRule 需要接受分组和选项
    return [makeOptionsRule('options', { group: true }), makeRequiredRule()];
  },
  basic () {

  }
};
