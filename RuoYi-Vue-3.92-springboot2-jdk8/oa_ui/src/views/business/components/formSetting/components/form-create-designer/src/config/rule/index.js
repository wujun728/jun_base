import radio from './radio'
import checkbox from './checkbox'
import input from './input'
import number from './number'
import select from './select'
import _switch from './switch'
import slider from './slider'
import time from './time'
import date from './date'
import rate from './rate'
import color from './color'
import row from './row'
import col from './col'
import tabPane from './tabPane'
import divider from './divider'
import cascader from './cascader'
import upload from './upload'
import transfer from './transfer'
import tree from './tree'
import alert from './alert'
import span from './span'
import space from './space'
import tab from './tab'
import button from './button'
import editor from './editor'
import departmentTree from './departmentTree'
import memberTree from './memberTree'
import timeFrom from './timeFrom'
import leaveFrom from './leaveFrom'
import textarea from './textarea'
import datetime from './datetime'
import refillFrom from './refillFrom'
import overtimeFrom from './overtimeFrom'
import bill from './bill'
import moneyFrom from './moneyFrom'
import switchStatus from './switchStatus'
import holidaySetting from './holidaySetting'
import infoForm from './infoForm'
import uploadFrom from './uploadFrom'
import outFrom from './outFrom'
import tripFrom from './tripFrom'
import city from './city'
import contractPayment from './contractPayment'
import contractRenewal from './contractRenewal'
import contractExpenditure from './contractExpenditure'
import issueInvoice from './issueInvoice'
import voidedInvoice from './voidedInvoice'

const ruleList = {
  [radio.name]: radio,
  [checkbox.name]: checkbox,
  [input.name]: input,
  [number.name]: number,
  [select.name]: select,
  [_switch.name]: _switch,
  [slider.name]: slider,
  [time.name]: time,
  [date.name]: date,
  [rate.name]: rate,
  [color.name]: color,
  [row.name]: row,
  [col.name]: col,
  [tab.name]: tab,
  [tabPane.name]: tabPane,
  [divider.name]: divider,
  [cascader.name]: cascader,
  [upload.name]: upload,
  [transfer.name]: transfer,
  [tree.name]: tree,
  [alert.name]: alert,
  [span.name]: span,
  [space.name]: space,
  [button.name]: button,
  [editor.name]: editor,
  [departmentTree.name]: departmentTree,
  [memberTree.name]: memberTree,
  [timeFrom.name]: timeFrom,
  [datetime.name]: datetime,
  [textarea.name]: textarea,
  [bill.name]: bill,
  [city.name]: city,
  [moneyFrom.name]: moneyFrom,
  [switchStatus.name]: switchStatus,
  [holidaySetting.name]: holidaySetting,
  [infoForm.name]: infoForm,
  [uploadFrom.name]: uploadFrom,
  [leaveFrom.name]: leaveFrom,
  [refillFrom.name]: refillFrom,
  [overtimeFrom.name]: overtimeFrom,
  [outFrom.name]: outFrom,
  [tripFrom.name]: tripFrom,
  [contractPayment.name]: contractPayment,
  [contractRenewal.name]: contractRenewal,
  [contractExpenditure.name]: contractExpenditure,
  [issueInvoice.name]: issueInvoice,
  [voidedInvoice.name]: voidedInvoice
}

export default ruleList
