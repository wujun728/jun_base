import radio from './rule/radio'
import checkbox from './rule/checkbox'
import input from './rule/input'
import number from './rule/number'
import select from './rule/select'
import _switch from './rule/switch'
import slider from './rule/slider'
import time from './rule/time'
import date from './rule/date'
import rate from './rule/rate'
import color from './rule/color'
import row from './rule/row'
import divider from './rule/divider'
import cascader from './rule/cascader'
import upload from './rule/upload'
import transfer from './rule/transfer'
import tree from './rule/tree'
import alert from './rule/alert'
import span from './rule/span'
import space from './rule/space'
import button from './rule/button'
import editor from './rule/editor'
import departmentTree from './rule/departmentTree'
import memberTree from './rule/memberTree'
import timeFrom from './rule/timeFrom'
import tab from './rule/tab'
import textarea from './rule/textarea'
import datetime from './rule/datetime'
import bill from './rule/bill'
import moneyFrom from './rule/moneyFrom'
import switchStatus from './rule/switchStatus'
import holidaySetting from './rule/holidaySetting'
import infoForm from './rule/infoForm'
import uploadFrom from './rule/uploadFrom'
import leaveFrom from './rule/leaveFrom'
import refillFrom from './rule/refillFrom'
import overtimeFrom from './rule/overtimeFrom'
import outFrom from './rule/outFrom'
import tripFrom from './rule/tripFrom'
import city from './rule/city'
import contractPayment from './rule/contractPayment'
import contracTrenewal from './rule/contractRenewal'
import contractExpenditure from './rule/contractExpenditure'
import issueInvoice from './rule/issueInvoice'
import voidedInvoice from './rule/voidedInvoice'

export default function createMenu() {
  return [
    {
      name: 'main',
      title: '字段库',
      list: [
        input,
        textarea,
        number,
        moneyFrom,
        date,
        datetime,
        timeFrom,
        radio,
        checkbox,
        select,
        memberTree,
        departmentTree,
        uploadFrom,
        bill,
        span,
        city
      ],
      group: [leaveFrom, overtimeFrom, outFrom, refillFrom, tripFrom],
      contract: [contractPayment, contracTrenewal, contractExpenditure, issueInvoice, voidedInvoice]
    }
  ]
}
