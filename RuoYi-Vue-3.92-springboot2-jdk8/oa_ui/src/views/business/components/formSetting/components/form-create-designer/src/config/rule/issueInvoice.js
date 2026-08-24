import uniqueId from '@form-create/utils/lib/unique'
const label = '开具发票'
const name = 'issueInvoice'
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
          title: '客户名称',
          symbol: 'customerList',
          type: 'select',
          _fc_drag_tag: 'select',
          options: []
        },
        {
          checkType: 0,
          display: true,
          field: uniqueId(),
          hidden: true,
          info: '',
          input: false,
          title: '关联付款单',
          symbol: 'billId',
          type: 'select',
          _fc_drag_tag: 'select',
          options: []
        },
        {
          checkType: 0,
          display: true,
          field: uniqueId(),
          hidden: false,
          info: '',
          input: false,
          title: '关联付款金额',
          symbol: 'billAmount',
          props: { disabled: true,readonly:true,placeholder:'请输入关联付款单' },
          type: 'input',
          _fc_drag_tag: 'input',
          options: []
        },
        {
          checkType: 0,
          display: true,
          effect: { fetch: '', required: true },
          field: uniqueId(),
          hidden: false,
          info: '',
          props: { placeholder: '请选择期望开票日期' },
          title: '期望开票日期',
          symbol: 'desireDate',
          type: 'datePicker',
          _fc_drag_tag: 'datePicker'
        },
        {
          effect: { fetch: '', required: true },
          field: uniqueId(),
          props: { type: 'radio' },
          title: '开票要求',
          symbol: 'invoicingMethod',
          value: 'mail',
          type: 'radio',
          _fc_drag_tag: 'radio',
          options: [
            { value: 'mail', label: '电子' },
            { value: 'express', label: '纸质' }
          ]
        },
        {
          display: true,
          effect: { required: false },
          field: uniqueId(),
          hidden: false,
          info: '',
          input: false,
          props: { type: 'input', placeholder: '请输入邮箱地址'  },
          title: '邮箱地址',
          symbol: 'invoicingEmail',
          type: 'input',
          _fc_drag_tag: 'input'
        },
        {
          display: true,
          effect: { required: false },
          field: uniqueId(),
          hidden: true,
          info: '',
          input: false,
          props: { type: 'input', placeholder: '请输入联系人' },
          title: '联系人',
          symbol: 'liaisonMan',
          type: 'input',
          _fc_drag_tag: 'input'
        },
        {
          display: true,
          effect: { required: false },
          field: uniqueId(),
          hidden: true,
          info: '',
          input: false,
          props: { type: 'input', placeholder: '请输入联系电话' },
          title: '联系电话',
          symbol: 'telephone',
          type: 'input',
          _fc_drag_tag: 'input'
        },
        {
          display: true,
          effect: { required: false },
          field: uniqueId(),
          hidden: true,
          info: '',
          input: false,
          props: { type: 'input', placeholder: '请输入邮寄详细地址' },
          title: '邮寄地址',
          symbol: 'mailingAddress',
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
          title: '发票类型',
          symbol: 'invoiceType',
          type: 'select',
          _fc_drag_tag: 'select',
          options: []
        },
        {
          effect: { fetch: '', required: true },
          field: uniqueId(),
          props: { type: 'moneyFrom', placeholder: '请输入开票金额' },
          title: '开票金额（元）',
          symbol: 'invoiceAmount',
          type: 'moneyFrom',
          _fc_drag_tag: 'moneyFrom'
        },
        {
          display: true,
          effect: { required: true },
          field: uniqueId(),
          hidden: false,
          info: '',
          input: false,
          props: { type: 'input', placeholder: '请输入抬头信息' },
          title: '发票抬头',
          symbol: 'invoiceHeader',
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
          props: { type: 'input', placeholder: '请输入纳税人识别号' },
          title: '纳税人识别号',
          symbol: 'dutyParagraph',
          type: 'input',
          _fc_drag_tag: 'input'
        },
        {
          type: 'input',
          field: uniqueId(),
          display: true,
          hidden: false,
          info: '',
          props: { type: 'textarea', placeholder: '请填写备注信息' },
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
      //   field: 'invoice',
      //   title: '',
      //   display: true,
      //   hidden: false,
      //   props: {
      //     activeText: '显示',
      //     inactiveText: '隐藏',
      //     value: true,
      //     name: '发票类目'
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
          value: '1.开具发票是否需要审批流，在客户规格设置中配置<br>2.支持财务进行发票开具/拒绝开票',
          title: '开具发票规则'
        },
        input: false,
        title: '',
        type: 'infoForm',
        _fc_drag_tag: 'infoForm'
      }
    ]
  }
}
