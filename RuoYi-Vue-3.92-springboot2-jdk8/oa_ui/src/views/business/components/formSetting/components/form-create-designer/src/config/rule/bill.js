import uniqueId from '@form-create/utils/lib/unique';

const label = '明细';
const name = 'approvalBill';

export default {
  icon: 'iconfont iconmingxi1',
  label,
  name,
  drag: true,
  inside: false,
  rule() {
    return {
      type: name,
      field: uniqueId(),
      title: label,
      info: '',
      props: {
        member: false,
      },
      children: [],
    };
  },
  props() {
    return [];
  },
  basic () {

  }
};
