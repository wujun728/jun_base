import uniqueId from '@form-create/utils/lib/unique';
import { makeRequiredRule } from '../../utils';

const label = '多行文本';
const name = 'input';

export default {
  icon: 'iconfont iconduohangwenben1',
  label,
  name: 'textarea',
  rule() {
    return {
      type: name,
      field: uniqueId(),
      title: label,
      info: '',
      props: {
        type: 'textarea',
      },
    };
  },
  props() {
    return [makeRequiredRule()];
  },
  basic () {

  }
};
