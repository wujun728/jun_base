import uniqueId from '@form-create/utils/lib/unique';
import { makeRequiredRule } from '../../utils';

const label = '附件';
const name = 'upload';

export default {
  icon: 'iconfont iconfujian1',
  label,
  name,
  rule() {
    return {
      type: name,
      field: uniqueId(),
      title: label,
      info: '',
      props: {
        action: '',
        onSuccess(res, file) {
          file.url = res.data.url;
        },
      },
    };
  },
  props() {
    return [makeRequiredRule()];
  },
  basic () {

  }
};
