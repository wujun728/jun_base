import uniqueId from '@form-create/utils/lib/unique'
const label = '加班'
const name = 'overtimeFrom'
export default {
  icon: 'iconfont iconjiaban',
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
          props: { timeType: 'day', titleIpt: '加班时长' },
          title: '',
          symbol: 'leaveDuration',
          type: 'timeFrom',
          _fc_drag_tag: 'timeFrom'
        },
        {
          checkType: 0,
          display: true,
          effect: { fetch: '', required: true },
          field: uniqueId(),
          hidden: false,
          info: '',
          input: false,
          title: '加班补贴',
          type: 'select',
          _fc_drag_tag: 'select',
          options: [
            { value: '加班补贴', label: '加班补贴' },
            { value: '调休', label: '调休' }
          ]
        },
        {
          display: true,
          effect: { required: true },
          field: uniqueId(),
          hidden: false,
          info: '',
          input: false,
          props: { type: 'textarea', placeholder: '请输入加班事由' },
          title: '加班事由',
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
      //     name: '加班事由'
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
            '1. 加班时长根据考勤-班次配置，加班起算时间自动计算<br>2. 加班时长默认以0.5小时为跨度进行计算<br>3. 加班数据将自动同步至考勤报表<br>4. 加班补贴选调休，自动同步假期余额',
          title: '加班规则'
        },
        input: false,
        title: '',
        type: 'infoForm',
        _fc_drag_tag: 'infoForm'
      }
    ]
  }
}
