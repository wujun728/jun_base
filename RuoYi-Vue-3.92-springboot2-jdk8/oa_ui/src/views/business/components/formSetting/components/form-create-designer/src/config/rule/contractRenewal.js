import uniqueId from '@form-create/utils/lib/unique'
const label = '合同续费'
const name = 'contractRenewal'
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
          title: '合同名称',
          symbol: 'contractList',
          props: { disabled: false, readonly: true, placeholder:'请选择合同名称'},
          type: 'select',
          _fc_drag_tag: 'select',
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
          title: '财务收入科目',
          symbol: 'incomeCategories',
          type: 'cascader',
          _fc_drag_tag: 'cascader',
          props: {
            filterable: true,
            expandTrigger: 'hover',
            options: []
          }
        },
        {
          checkType: 0,
          display: true,
          effect: { fetch: '', required: true },
          field: uniqueId(),
          hidden: false,
          info: '',
          input: false,
          title: '续费类型',
          symbol: 'renewalType',
          type: 'select',
          _fc_drag_tag: 'select',
          options: []
        },
        {
          effect: { fetch: '', required: true },
          field: uniqueId(),
          props: { type: 'moneyFrom' },
          title: '续费金额（元）',
          symbol: 'renewalAmount',
          type: 'moneyFrom',
          _fc_drag_tag: 'moneyFrom'
        },
        {
          checkType: 0,
          display: true,
          effect: { fetch: '', required: true },
          field: uniqueId(),
          hidden: false,
          info: '',
          props: { type: 'datetime', placeholder: '请选择日期' },
          title: '续费结束日期',
          symbol: 'renewalEndTime',
          type: 'datePicker',
          _fc_drag_tag: 'datetimerange'
        },
        {
          checkType: 0,
          display: true,
          effect: { fetch: '', required: true },
          field: uniqueId(),
          hidden: false,
          info: '',
          input: false,
          title: '支付方式',
          type: 'select',
          symbol: 'payType',
          _fc_drag_tag: 'select',
          options: []
        },
        {
          checkType: 0,
          display: true,
          effect: { fetch: '', required: true },
          field: uniqueId(),
          hidden: false,
          info: '',
          props: { placeholder: '请选择日期' },
          title: '付款时间',
          symbol: 'payTime',
          type: 'datePicker',
          _fc_drag_tag: 'datePicker'
        },

        {
          effect: { fetch: '', required: false },
          field: uniqueId(),
          props: { type: 'uploadFrom' },
          title: '付款凭证',
          symbol: 'paymentVoucher',
          type: 'uploadFrom',
          _fc_drag_tag: 'uploadFrom'
        },
        {
          type: 'input',
          field: uniqueId(),
          display: true,
          hidden: false,
          info: '',
          props: { type: 'textarea', placeholder: '请输入' },
          title: '备注',
          symbol: 'remark',
          _fc_drag_tag: 'textarea'
        }
      ]
    }
  },
  props() {
    return [
      // {
      //   type: 'switchStatus',
      //   field: 'paymentVoucher',
      //   title: '',
      //   display: true,
      //   hidden: false,
      //   props: {
      //     activeText: '显示',
      //     inactiveText: '隐藏',
      //     value: true,
      //     name: '付款凭证'
      //   },
      //   _fc_drag_tag: 'switchStatus'
      // },
      // {
      //   type: 'switchStatus',
      //   field: 'mustHave',
      //   display: true,
      //   hidden: false,
      //   props: {
      //     activeText: '必填',
      //     inactiveText: '选填',
      //     value: true,
      //     name: '是否必填'
      //   },
      //   title: '',
      //   _fc_drag_tag: 'switchStatus'
      // },
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
      //     name: '备注'
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
          value:
            '1. 回款是否需要审批流，在客户规格设置中配置<br>2.支持财务在付款记录中进行回款修改、管理<br>3. 合同回款根据财务收入科目，自动同步财务账目收入记录',
          title: '回款规则'
        },
        input: false,
        title: '',
        type: 'infoForm',
        _fc_drag_tag: 'infoForm'
      }
    ]
  }
}
