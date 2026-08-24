import uniqueId from '@form-create/utils/lib/unique';
import { makeRequiredRule } from '../../utils';

const label = '省市区';
const name = 'city';
export default {
  icon: 'iconfont iconshengshiqu',
  label,
  name,
  rule() {
    return {
      type: name,
      field: uniqueId(),
      props: {

        titleIpt: '省市区',
      },
      title: '',
      input: false,
      info: '',
      effect: { required: 'city' },
    };
  },
  props() {
    return [

      {
        type: 'input',
        field: 'titleIpt',
        title: '标识名',
        value: '省市区',
      },
      makeRequiredRule(),
    ];
  },
  basic () {

  }
};
