import uniqueId from '@form-create/utils/lib/unique'
import { makeRequiredRule } from '../../utils'

const label = '补卡'
const name = 'refillFrom'
export default {
  icon: 'iconfont iconbuka',
  label,
  name,
  rule() {
    return {
      type: name,
      field: uniqueId(),
      loadChildren: false,
      children: [
        {
          checkType: 0,
          display: true,
          effect: { fetch: '', required: true },
          field: uniqueId(),
          hidden: false,
          info: '',
          input: false,
          title: '异常日期',
          type: 'select',
          _fc_drag_tag: 'select',
          symbol: 'attendanceExceptionDate',
          options: []
        },
        {
          checkType: 0,
          display: true,
          effect: { fetch: '', required: true },
          field: uniqueId(),
          hidden: false,
          info: '',
          input: false,
          title: '异常记录',
          type: 'select',
          _fc_drag_tag: 'select',
          symbol: 'attendanceExceptionRecord',
          options: []
        },
        // {
        //   checkType: 0,
        //   display: true,
        //   effect: {fetch: "", required: true},
        //   field: uniqueId(),
        //   hidden: false,
        //   info: "",
        //   props: {type: "datetime"},
        //   title: "补卡时间",
        //   type: "datePicker",
        //   _fc_drag_tag: "datetimerange",

        //   },
        {
          type: 'input',
          effect: { required: true },
          field: uniqueId(),
          display: true,
          hidden: false,
          info: '',
          props: { type: 'textarea', placeholder: '请输入补卡事由' },
          title: '补卡事由',
          _fc_drag_tag: 'textarea'
        }
      ]
      // props: {
      //   timeType: 'day',
      //   titleIpt: '补卡',
      // },
      // title: '',
      // input: false,
      // info: '',
      // effect: { required: 'refillFrom' },
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
      //     name: '补卡事由'
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
            '1.是否需要补卡，根据考勤组规则自动判断<br>2. 补卡次数与时间限制，可在考勤组-打卡规则中修改<br>3. 补卡数据将自动同步至考勤报表',
          title: '补卡规则'
        },
        input: false,
        title: '',
        type: 'infoForm',
        _fc_drag_tag: 'infoForm'
      }
    ]
  }
}
