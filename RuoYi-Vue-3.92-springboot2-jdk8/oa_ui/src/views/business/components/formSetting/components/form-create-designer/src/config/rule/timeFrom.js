import uniqueId from '@form-create/utils/lib/unique'
import { makeRequiredRule } from '../../utils'

const label = '时长'
const name = 'timeFrom'
export default {
  icon: 'iconfont iconshichang1',
  label,
  name,
  rule() {
    return {
      type: name,
      field: uniqueId(),
      props: {
        timeType: 'day',
        titleIpt: '时长'
      },
      // title: '时长',
      input: false,
      info: '',
      symbol: 'leaveDuration',
      effect: { required: 'timeFrom' }
    }
  },
  props() {
    return [
      {
        type: 'select',
        field: 'timeType',
        symbol: 'leaveDuration',
        title: '时间刻度',
        value: 'day',
        options: [
          { label: '按天', value: 'day' },
          { label: '按小时', value: 'time' }
        ]
      },
      {
        type: 'input',
        field: 'titleIpt',
        title: '标识名',
        value: '时长'
      },
      makeRequiredRule()
    ]
  },
  basic() {}
}
