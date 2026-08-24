import uniqueId from '@form-create/utils/lib/unique';
import { makeRequiredRule } from '../../utils';

const label = '数字';
const name = 'inputNumber';

export default {
  icon: 'iconfont iconshuzi1',
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
    return [
      { type: 'inputNumber', field: 'min', title: '设置数字最小值' },
      { type: 'inputNumber', field: 'max', title: '设置数字最大值' },
      { type: 'inputNumber', field: 'precision', title: '小数点位数' },
      makeRequiredRule(),
    ];
  },
  basic () {

  }
};
