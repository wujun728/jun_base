import uniqueId from '@form-create/utils/lib/unique'
import { makeRequiredRule } from '../../utils'

const label = '出差'
const name = 'tripFrom'
export default {
  icon: 'iconfont iconchucha',
  label,
  name,
  loadChildren: false,
  rule() {
    return {
      type: name,
      field: uniqueId(),

      children: [
        {
          display: true,
          effect: { required: 'timeFrom' },
          field: uniqueId(),
          hidden: false,
          info: '请输入出差时长',
          input: false,
          props: { timeType: 'day', titleIpt: '出差时长' },
          title: '',
          timeType: 'day',
          symbol: 'leaveDuration',
          type: 'timeFrom',
          _fc_drag_tag: 'timeFrom'
        },
        {
          display: true,
          effect: { required: true },
          field: uniqueId(),
          hidden: false,
          info: '',
          input: false,
          props: { type: 'input', placeholder: '请输入出发城市' },
          title: '出发城市',
          type: 'input',
          _fc_drag_tag: 'input'
        },
        {
          display: true,
          effect: { required: true },
          field: uniqueId(),
          hidden: false,
          info: '',
          input: false,
          props: { type: 'input', placeholder: '请输入目的城市' },
          title: '目的城市',
          type: 'input',
          _fc_drag_tag: 'input'
        },
        {
          checkType: 0,
          display: true,
          effect: { fetch: '', required: true },
          field: uniqueId(),
          hidden: false,
          info: '',
          input: false,
          title: '交通工具',
          type: 'select',
          _fc_drag_tag: 'select',
          options: [
            { value: '飞机', label: '飞机' },
            { value: '火车', label: '火车' },
            { value: '高铁/动车', label: '高铁/动车' },
            { value: '汽车', label: '汽车' },
            { value: '船', label: '船' },
            { value: '其他', label: '其他' }
          ]
        },
        {
          checkType: 0,
          display: true,
          effect: { fetch: '', required: true },
          field: uniqueId(),
          hidden: false,
          info: '',
          input: false,
          title: '单程往返',
          type: 'select',
          _fc_drag_tag: 'select',
          options: [
            { value: '单程', label: '单程' },
            { value: '往返', label: '往返' }
          ]
        },
        {
          display: true,
          effect: { required: true },
          field: uniqueId(),
          hidden: false,
          info: '',
          input: false,
          props: { type: 'textarea', titleIpt: '出差事由', placeholder: '请输入出差事由' },
          title: '出差事由',
          type: 'input',
          _fc_drag_tag: 'textarea'
        }
      ]
    }
  },
  props() {
    return [
      // {
      //   type: 'switchStatus',
      //   field: 'remark',
      //   title: '',
      //   display: true,
      //   hidden: false,
      //   props: {
      //     activeText: '显示',
      //     inactiveText: '隐藏',
      //     value: true,
      //     name: '出差事由'
      //   },
      //   _fc_drag_tag: 'switchStatus',
      // },
      // {
      //   type: 'switchStatus',
      //   field: 'isMustHave',
      //   display: true,
      //   hidden: false,
      //   props: {
      //     activeText: '必填',
      //     inactiveText: '选填',
      //     value: false,
      //     name: '是否必填'
      //   },
      //   title: '',
      //   _fc_drag_tag: 'switchStatus',
      // },
    ]
  },
  basic() {
    return [
      {
        checkType: 0,
        display: true,
        field: uniqueId(),
        hidden: false,
        info: '',
        props: {
          value:
            '1. 时长根据自然日计算，提交人可修改<br>2. 交通工具：飞机、火车、高铁/动车、汽车、船、其他<br>3. 单程往返：单程或往返<br>4. 出差总时长：所有行程的总时长，自动计算，支持修改；审批通过之后会同步至考勤',
          title: '出差规则'
        },
        input: false,
        title: '',
        type: 'infoForm',
        _fc_drag_tag: 'infoForm'
      }
    ]
  }
}
