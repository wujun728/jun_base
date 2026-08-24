import uniqueId from '@form-create/utils/lib/unique';
import { makeRequiredRule } from '../../utils';

const label = '附件';
const name = 'uploadFrom';
export default {
  icon: 'iconfont iconfujian',
  label,
  name,
  rule() {
    return {
      type: name,
      field: uniqueId(),
      props: {},
      title: '附件',
      info: '',
    };
  },
  props() {
    return [makeRequiredRule()];
  },
  basic () {

  }
};
