import uniqueId from '@form-create/utils/lib/unique';
import { makeRequiredRule } from '../../utils';

const label = '部门';
const name = 'departmentTree';
export default {
  icon: 'iconfont iconbumen',
  label,
  name,
  rule() {
    return {
      type: name,
      field: uniqueId(),
      title: label,
      info: '',
      props: {
        departType: 'many',
      },
    };
  },
  props() {
    return [
      {
        type: 'radio',
        field: 'departType',
        title: '选择类型',
        options: [
          { label: '可选一个部门', value: 'oneself' },
          { label: '可选多个部门', value: 'many' },
        ],
      },
      makeRequiredRule(),
    ];
  },
  basic () {

  }
};
