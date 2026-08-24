import uniqueId from '@form-create/utils/lib/unique';
import { makeRequiredRule } from '../../utils';

const label = '日期';
const name = 'datePicker';

export default {
  icon: 'iconfont iconriqi1',
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
