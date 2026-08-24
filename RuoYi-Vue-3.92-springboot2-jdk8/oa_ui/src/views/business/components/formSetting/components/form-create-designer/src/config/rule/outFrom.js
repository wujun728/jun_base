import uniqueId from '@form-create/utils/lib/unique'
import { makeRequiredRule } from '../../utils'

const label = '外出'
const name = 'outFrom'
export default {
  icon: 'iconfont iconwaichu',
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
          info: '',
          input: false,
          props: { timeType: 'day', titleIpt: '外出时长' },
          title: '',
          symbol: 'leaveDuration',
          type: 'timeFrom',
          timeType: 'day',
          _fc_drag_tag: 'timeFrom'
        },

        {
          type: 'input',
          effect: { required: true },
          field: uniqueId(),
          display: true,
          hidden: false,
          info: '',
          props: { type: 'textarea', placeholder: '请输入外出事由' },
          title: '外出事由',
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
      //     name: '外出事由'
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
          value: '1.外出数据将自动同步至考勤报表',
          title: '外出规则'
        },
        input: false,
        title: '',
        type: 'infoForm',
        _fc_drag_tag: 'infoForm'
      }
    ]
  }
}
