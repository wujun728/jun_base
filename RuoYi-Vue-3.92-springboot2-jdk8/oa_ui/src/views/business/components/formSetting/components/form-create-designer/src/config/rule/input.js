import uniqueId from '@form-create/utils/lib/unique';
import { makeRequiredRule } from '../../utils';
const label = '单行文本';
const name = 'input';

export default {
  icon: 'iconfont icondanhangwenben1',
  label,
  name,
  rule() {
    return {
      type: name,
      field: uniqueId(),
      title: label,
      info: '',
      props: {},
    };
  },
  props() {
    return [makeRequiredRule()];
  },
  basic () {

  }
};
