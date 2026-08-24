import uniqueId from '@form-create/utils/lib/unique'
const label = '作废发票'
const name = 'voidedInvoice'
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
          title: '发票抬头',
          symbol: 'invoiceList',
          type: 'select',
          _fc_drag_tag: 'select',
          options: []
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
      //   field: 'abolition',
      //   title: '',
      //   display: true,
      //   hidden: false,
      //   props: {
      //     activeText: '显示',
      //     inactiveText: '隐藏',
      //     value: true,
      //     name: '作废原因'
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
          value: '1.作废发票是否需要审批流，在客户规格设置中配置<br>2.作废发票关联的付款单，支持重新开具发票',
          title: '作废发票规则'
        },
        input: false,
        title: '',
        type: 'infoForm',
        _fc_drag_tag: 'infoForm'
      }
    ]
  }
}
