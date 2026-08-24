import uniqueId from '@form-create/utils/lib/unique';
import { makeRequiredRule } from '../../utils';

const label = '金额';
const name = 'moneyFrom';

export default {
  icon: 'iconfont iconjine3',
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
      { type: 'inputNumber', field: 'precision', title: '小数点位数', props: { min: 0, max: 2 } },
      makeRequiredRule(),
    ];
  },
  basic () {

  }
};
