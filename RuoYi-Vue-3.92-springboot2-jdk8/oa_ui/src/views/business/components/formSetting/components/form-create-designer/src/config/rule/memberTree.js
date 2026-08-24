import uniqueId from '@form-create/utils/lib/unique';
import { makeRequiredRule } from '../../utils';

const label = '成员';
const name = 'departmentTree';
export default {
  icon: 'iconfont iconchengyuan',
  label,
  name: 'memberTree',
  rule() {
    return {
      type: name,
      field: uniqueId(),
      title: label,
      info: '',
      props: {
        member: true,
        range: ['many', 'oneself'],
      },
    };
  },
  props() {
    return [
      {
        type: 'checkbox',
        field: 'range',
        title: '选择范围',
        options: [
          { label: '可选自己', value: 'oneself' },
          { label: '可选多人', value: 'many' },
        ],
      },
      makeRequiredRule(),
    ];
  },
  basic () {

  }
};
