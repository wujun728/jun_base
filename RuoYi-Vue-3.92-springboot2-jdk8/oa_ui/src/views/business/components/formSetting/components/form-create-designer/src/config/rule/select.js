import uniqueId from '@form-create/utils/lib/unique';
import { makeOptionsRule } from '../../utils/index';
import { makeRequiredRule } from '../../utils';

const label = '选择器';
const name = 'select';

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
      props: {},
      options: [
        { value: '选项1', label: '选项1' },
        { value: '选项2', label: '选项2' },
      ],
      checkType: 0,
    };
  },
  props() {
    return [makeOptionsRule('options'), makeRequiredRule()];
  },
  basic () {

  }
};
