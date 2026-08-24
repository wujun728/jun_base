<script>
// 自定义表单-字段类型选项
const typeOptions = [
  {
    label: '文本框',
    value: 'text',
    type: 'input'
  },
  {
    label: '数字',
    value: 'number',
    type: 'input'
  },
  {
    label: '文本域',
    value: 'textarea',
    type: 'input'
  },
  // {
  //   label: '级联选择',
  //   value: 'cascader',
  //   type: 'select'
  // },
  {
    label: '下拉单选',
    value: 'single',
    type: 'select'
  },
  {
    label: '下拉复选',
    value: 'multiple',
    type: 'select'
  },
  {
    label: '单选按钮',
    value: 'radio',
    type: 'radio'
  },
  {
    label: '复选按钮组',
    value: 'checked',
    type: 'checked'
  },
  {
    label: '日期控件',
    value: 'date',
    type: 'date'
  },
  {
    label: '图片选择控件',
    value: 'images',
    type: 'images'
  },
  {
    label: '附件控件',
    value: 'file',
    type: 'file'
  },
  {
    label: '富文本控件',
    value: 'oaWangeditor',
    type: 'oaWangeditor'
  }
]
const maxDisabledList = [
  'customer_way',
  'area_cascade',
  'customer_followed',
  'contract_cate',
  'contract_customer_name',
  '8ae68ada',
  'contract_status',
  'signing_status',
  'contract_followed',
  'e06d7153',
  'contract_category',
  'contract_customer',
  '8ae68ada',
  'contract_followed'
]
let typeDisabled = ['text', 'number', 'textarea', 'date', 'images', 'file', 'oaWangeditor']
const contractList = ['customer_status', 'customer_followed', 'contract_status', 'contract_followed', 'signing_status']

export default {
  typeOptions,
  contractList,
  typeDisabled,
  maxDisabledList
}
</script>
