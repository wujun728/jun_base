import uniqueId from '@form-create/utils/lib/unique';
import { makeRequiredRule } from '../../utils';

const label = '日期时间';
const name = 'datePicker';

export default {
  icon: 'iconfont iconriqishijian1',
  label,
  name: 'datetimerange',
  rule() {
    return {
      type: name,
      field: uniqueId(),
      title: label,
      info: '',
      props: {
        type: 'datetime',
      },
    };
  },
  props() {
    return [makeRequiredRule()];
  },
  basic () {

  }
};
