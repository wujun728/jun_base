import uniqueId from '@form-create/utils/lib/unique';
import { makeOptionsRule } from '../../utils/index';
import { makeRequiredRule } from '../../utils';

const label = '多选';
const name = 'checkbox';

export default {
  icon: 'iconfont iconduoxuan1',
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
    };
  },
  props() {
    return [makeOptionsRule('options'), makeRequiredRule()];
  },
  basic () {

  }
};
