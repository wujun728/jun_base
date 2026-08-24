import uniqueId from '@form-create/utils/lib/unique'
const label = '请假'
const name = 'leaveFrom'
export default {
  icon: 'iconfont iconqingjia2',
  label,
  name,
  loadChildren: false,
  rule() {
    return {
      type: name,
      field: uniqueId(),
      children: [
        {
          checkType: 0,
          display: true,
          effect: { fetch: '', required: true },
          field: uniqueId(),
          hidden: false,
          info: '',
          input: false,
          title: '假期类型',
          symbol: 'holidayType',
          type: 'select',
          _fc_drag_tag: 'select',
          options: []
        },
        {
          display: true,
          effect: { required: 'timeFrom' },
          field: uniqueId(),
          hidden: false,
          info: '',
          input: false,
          props: { timeType: 'day', titleIpt: '请假时长' },
          title: '',
          symbol: 'leaveDuration',
          type: 'timeFrom',
          _fc_drag_tag: 'timeFrom'
        },
        {
          type: 'input',
          effect: { required: true },
          field: uniqueId(),
          display: true,
          hidden: false,
          info: '',
          props: { type: 'textarea', placeholder: '请输入请假事由' },
          title: '请假事由',
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
      //     name: '请假事由'
      //   },
      //   _fc_drag_tag: 'switchStatus'
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
      //   _fc_drag_tag: 'switchStatus'
      // }
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
          value: '根据假期类型设置的规则进行计算/手动填写',
          title: '假期类型'
        },
        input: false,
        title: '',
        type: 'infoForm',
        _fc_drag_tag: 'infoForm'
      },
      {
        checkType: 0,
        display: true,
        field: uniqueId(),
        hidden: false,
        info: '',
        input: false,
        title: '',
        type: 'holidaySetting',
        _fc_drag_tag: 'holidaySetting'
      }
    ]
  }
}
