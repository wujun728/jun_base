import uniqueId from '@form-create/utils/lib/unique';
import { makeOptionsRule, makeRequiredRule } from '../../utils';

const label = '内容说明';
const name = 'infoForm';

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
        value: '',
        title: ''
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
