import uniqueId from '@form-create/utils/lib/unique';
import { makeRequiredRule } from '../../utils';

const label = '时间选择器';
const name = 'timePicker';

export default {
  icon: 'iconfont iconchaosong',
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
};
