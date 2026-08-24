import uniqueId from '@form-create/utils/lib/unique';
import { makeOptionsRule, makeRequiredRule } from '../../utils';

const label = '状态开关';
const name = 'switchStatus';

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
      props: {
        activeText: '',
        inactiveText: '',
        value: false,
        name: ''
      },
      checkType: 0,
    };
  },
  props() {
    return [makeOptionsRule('props'), makeRequiredRule()];
  },
  basic () {

  }
};
