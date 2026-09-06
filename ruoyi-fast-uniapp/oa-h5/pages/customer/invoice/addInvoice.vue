<template>
  <view class="content">
    <!-- 申请发票页面 -->
    <view class="cr-position-header">
      <view class="status_bar"></view>
      <default-nav-bar :is-right="true" :default-title="data.defaultTitle"></default-nav-bar>
    </view>
    <!-- 表单内容 -->
    <view class="examine-content m10">
      <view class="tips" v-if="data.show">

        <text class="iconfont icon-yemiantishi" /> 请注意,您没有选择该发票关联的付款订单！
      </view>
      <uni-forms :border="false" label-width="80px" class="mt10">
        <view class="list-item ">
          <!-- 输入框 -->
          <uni-forms-item class="input-label">
            <template v-slot:label>
              <view class="uni-forms-item__label">客户名称 <text class="iconfont">*</text> </view>
            </template>
            <view class="picker-input">
              {{data.name||'--'}}
            </view>
          </uni-forms-item>
          <!-- 选择框 -->
          <uni-forms-item class="input-label">
            <template v-slot:label>
              <view class="uni-forms-item__label">付款金额 </view>
            </template>
            <view class="picker-input">
              {{formData.price}}
            </view>
          </uni-forms-item>
          <!-- 客户标签 -->
          <uni-forms-item class="input-label">
            <template v-slot:label>
              <view class="uni-forms-item__label">发票类目<text class="iconfont">*</text> </view>
            </template>
            <picker :range="data.categoryOptions" range-key="name" @change="categoryChange">
              <view v-if="!formData.category_id" class="picker-input picker-input-placeholder">
                请选择
                <view class="iconfont icon-fanhui"></view>
              </view>
              <view class="picker-input" v-else>
                {{data.categoryOptions[data.categoryIndex].name}}
                <view class="iconfont icon-fanhui"></view>
              </view>
            </picker>
          </uni-forms-item>
          <uni-forms-item class="input-label">
            <template v-slot:label>
              <view class="uni-forms-item__label">期望开票日期 <text class="iconfont">*</text></view>
            </template>
            <picker mode="date" :value="formData.bill_date" :start="startDate" :end="endDate" @change="bindDateChange">
              <view v-if="!formData.bill_date" class="picker-input picker-input-placeholder">
                请选择
                <view class="iconfont icon-fanhui"></view>
              </view>
              <view class="picker-input" v-else>
                {{formData.bill_date}}
                <view class="iconfont icon-fanhui"></view>
              </view>
            </picker>
          </uni-forms-item>
        </view>
        <view class="list-item mt20">
          <uni-forms-item class="input-label">
            <template v-slot:label>
              <view class="uni-forms-item__label">开票要求 </view>
            </template>
            <picker mode="selector" :range="data.rangeSource" range-key="label" @change="sourceChange">
              <view class="picker-input">
                {{data.rangeSource[data.sourceIndex].label}}
                <view class="iconfont icon-fanhui"></view>
              </view>
            </picker>
          </uni-forms-item>

          <uni-forms-item class="input-label" v-if="formData.collect_type=='mail'">
            <template v-slot:label>
              <view class="uni-forms-item__label">邮箱地址 <text class="iconfont">*</text></view>
            </template>
            <uni-easyinput :inputBorder="false" v-model="formData.collect_email" type="text" :clearable="false"
              :styles="styles" :placeholder-style="placeholderStyle" :autoHeight="true" placeholder="请填写">
            </uni-easyinput>
          </uni-forms-item>

          <template v-if="formData.collect_type=='express'">
            <uni-forms-item class="input-label">
              <template v-slot:label>
                <view class="uni-forms-item__label">联系人 <text class="iconfont">*</text></view>
              </template>
              <uni-easyinput :inputBorder="false" v-model="formData.collect_name" type="text" :clearable="false"
                :styles="styles" :placeholder-style="placeholderStyle" :autoHeight="true" placeholder="请填写">
              </uni-easyinput>
            </uni-forms-item>
            <uni-forms-item class="input-label">
              <template v-slot:label>
                <view class="uni-forms-item__label">联系电话 <text class="iconfont">*</text></view>
              </template>
              <uni-easyinput :inputBorder="false" v-model="formData.collect_tel" type="text" :clearable="false"
                :styles="styles" :placeholder-style="placeholderStyle" :autoHeight="true" placeholder="请填写">
              </uni-easyinput>
            </uni-forms-item>
            <uni-forms-item class="input-label">
              <template v-slot:label>
                <view class="uni-forms-item__label">邮寄地址 <text class="iconfont">*</text></view>
              </template>
              <uni-easyinput :inputBorder="false" v-model="formData.mail_address" type="text" :clearable="false"
                :styles="styles" :placeholder-style="placeholderStyle" :autoHeight="true" placeholder="请填写">
              </uni-easyinput>
            </uni-forms-item>
          </template>
        </view>
        <view class="list-item mt20">
          <!-- 省市区 -->
          <uni-forms-item class="input-label">
            <template v-slot:label>
              <view class="uni-forms-item__label">发票类型 <text class="iconfont">*</text></view>
            </template>
            <picker :range="data.invoiceOptions" range-key="label" @change="typesChange">
              <view class="picker-input">
                {{data.invoiceOptions[data.typesIndex].label}}
                <view class="iconfont icon-fanhui"></view>
              </view>
            </picker>
          </uni-forms-item>
          <!-- 详细地址 -->
          <uni-forms-item class="input-label">
            <template v-slot:label>
              <view class="uni-forms-item__label">开票金额 <text class="iconfont">*</text></view>
            </template>
            <uni-easyinput :inputBorder="false" v-model="formData.amount" type="number" :clearable="false"
              :styles="styles" maxlength="11" @blur="check" :placeholder-style="placeholderStyle" :autoHeight="true"
              placeholder="请填写">
            </uni-easyinput>
          </uni-forms-item>
          <!-- 联系人姓名 -->
          <uni-forms-item class="input-label">
            <template v-slot:label>
              <view class="uni-forms-item__label">发票抬头 <text class="iconfont">*</text></view>
            </template>
            <uni-easyinput :inputBorder="false" v-model="formData.title" type="text" :clearable="false" :styles="styles"
              :placeholder-style="placeholderStyle" :autoHeight="true" placeholder="请填写">
            </uni-easyinput>
          </uni-forms-item>

          <uni-forms-item class="input-label" v-if="formData.types==2 ||formData.types==3 ">
            <template v-slot:label>
              <view class="uni-forms-item__label">纳税人识别号 <text class="iconfont">*</text></view>
            </template>
            <uni-easyinput :inputBorder="false" v-model="formData.ident" type="text" :clearable="false" :styles="styles"
              :placeholder-style="placeholderStyle" :autoHeight="true" placeholder="请填写">
            </uni-easyinput>
          </uni-forms-item>

          <uni-forms-item class="input-label" v-if="formData.types==3">
            <template v-slot:label>
              <view class="uni-forms-item__label">开户行 </view>
            </template>
            <uni-easyinput :inputBorder="false" v-model="formData.bank" type="text" :clearable="false" :styles="styles"
              :placeholder-style="placeholderStyle" :autoHeight="true" placeholder="请填写">
            </uni-easyinput>
          </uni-forms-item>

          <uni-forms-item class="input-label" v-if="formData.types==3">
            <template v-slot:label>
              <view class="uni-forms-item__label">开户账号 </view>
            </template>
            <uni-easyinput :inputBorder="false" v-model="formData.account" type="text" :clearable="false"
              :styles="styles" :placeholder-style="placeholderStyle" :autoHeight="true" placeholder="请填写">
            </uni-easyinput>
          </uni-forms-item>

          <uni-forms-item class="input-label" v-if="formData.types==3">
            <template v-slot:label>
              <view class="uni-forms-item__label">开票地址</view>
            </template>
            <uni-easyinput :inputBorder="false" v-model="formData.address" type="text" :clearable="false"
              :styles="styles" :placeholder-style="placeholderStyle" :autoHeight="true" placeholder="请填写">
            </uni-easyinput>
          </uni-forms-item>

          <uni-forms-item class="input-label" v-if="formData.types == '3'">
            <template v-slot:label>
              <view class="uni-forms-item__label">电话 </view>
            </template>
            <uni-easyinput :inputBorder="false" v-model="formData.tel" type="text" :clearable="false" :styles="styles"
              :placeholder-style="placeholderStyle" :autoHeight="true" placeholder="请填写">
            </uni-easyinput>
          </uni-forms-item>
        </view>


        <!-- 备注 -->
        <view class="list-item mt20">
          <uni-forms-item class="is-direction-top">
            <template v-slot:label>
              <view class="uni-forms-item__label mt36">
                备注
              </view>
            </template>
            <uni-easyinput :inputBorder="false" v-model="formData.mark" type="textarea" :clearable="false"
              :styles="styles" :placeholder-style="placeholderStyle" :maxlength="256" :autoHeight="true"
              placeholder="填写备注信息">
            </uni-easyinput>
          </uni-forms-item>
        </view>
      </uni-forms>
    </view>
    <!-- 底部 -->
    <view class="footer">
      <view class="btn cancel" @click="onCancel">
        取消
      </view>
      <view class="btn next" :loading="loading" @click="handleConfirm">
        提交
      </view>
    </view>


  </view>
</template>
<script setup>
  import defaultNavBar from '@/components/defaultNavBar/index'
  import { ref, reactive, toRefs, computed } from 'vue'
  const loading = ref(false)
  import {
    clientInvoiceSaveApi,
    invoiceCategorysApi,
    clientInvoiceDetailsApi,
    clientInvoicePutApi
  } from '@/api/customer'
  import message from '@/utils/message'
  import moment from 'moment'
  // 定义data数据
  const placeholderStyle = ref('color: #C0C4CC;font-size: 30rpx')
  const styles = reactive({
    color: '#303133',
    disableColor: '#ffffff'
  })

  const rangeGender = reactive([
    { label: '男', value: 1 },
    { label: '女', value: 2 },
    { label: '未知', value: 3 },
  ])

  // 定义表单
  const data = reactive({
    defaultTitle: '申请发票',
    name: '',
    fid: '',
    typesIndex: 0,
    categoryIndex: '',
    sourceIndex: 0,
    show: false,
    categoryOptions: [],
    invoiceOptions: [
      { label: '个人普通发票', value: 1 },
      { label: '企业普通发票', value: 2 },
      { label: '企业专用发票', value: 3 },
    ],
    rangeSource: [
      { value: 'mail', label: '电子' },
      { value: 'express', label: '纸质' }
    ]
  })
  const formData = reactive({
    eid: 0,
    cid: 0,
    price: '',
    amount: undefined,
    bill_date: '',
    types: 1,
    title: '',
    ident: '',
    collect_type: 'mail',
    collect_name: '',
    collect_tel: '',
    mail_address: '',
    bank: '',
    address: '',
    account: '',
    bill_id: [],
    tel: '',
    mark: '',
    mail_address: '',
    collect_email: '',
    category_id: '',
  })
  import { onLoad } from '@dcloudio/uni-app'
  onLoad(e => {
    if (e.cid) {
      formData.cid = e.cid
    }
    formData.eid = e.eid
    data.name = e.name
    getSourceData()
    if (e.num) {
      formData.price = e.num
      formData.amount = e.num
    } else {
      formData.price = 0
    }

    if (e.id !== null && e.id) {
      if (e.id == '') {
        data.show = true
      } else {
        formData.bill_id = e.id.split(',')
      }
    }
    formData.bill_date = moment(new Date()).format('YYYY-MM-DD')
    if (e.fid) {
      setTimeout(() => {
        data.fid = e.fid
        getDetails(e.fid)
      }, 300)
    }
    let titleList = JSON.parse(uni.getStorageSync('titleList'))
    if (titleList.length !== 0) {
      titleList.map(item => {
        if (item.eid == formData.eid) {
          formData.title = item.title
        } else {
          formData.title = e.name
        }
      })
    } else {
      formData.title = e.name
    }
  })

  // 点击返回
  import { clickNavigateTo, delayedReLaunch } from '@/utils/helper'
  // 获取发票类目
  const getSourceData = () => {
    invoiceCategorysApi().then(res => {
      data.categoryOptions = res.data.list
    })
  }

  // 获取发票详情
  const getDetails = id => {
    clientInvoiceDetailsApi(id).then(res => {
      formData.amount = res.data.amount
      formData.address = res.data.address
      formData.price = res.data.price
      formData.title = res.data.title
      formData.bill_date = res.data.bill_date
      formData.category_id = res.data.category.id
      formData.types = res.data.types
      formData.ident = res.data.ident
      formData.bank = res.data.bank
      formData.account = res.data.account
      formData.mark = res.data.mark
      formData.tel = res.data.tel
      formData.collect_email = res.data.collect_email
      formData.collect_tel = res.data.collect_tel
      formData.collect_name = res.data.collect_name
      formData.collect_type = res.data.collect_type
      formData.mail_address = res.data.mail_address
      data.sourceIndex = data.rangeSource.findIndex(item => item.value == formData.collect_type)
      data.categoryIndex = data.categoryOptions.findIndex(item => item.id === formData.category_id)
      data.typesIndex = data.invoiceOptions.findIndex(item => item.value == formData.types)
    })
  }
  // 取消
  const onCancel = (() => {
    uni.navigateBack({})
  })

  import { identReg, mailboxReg, phonelineReg } from '@/utils/helper'
  // 提交表单
  const handleConfirm = () => {

    if (!formData.category_id) {
      message.error('请选择发票类目')
      return false
    }
    if (!formData.bill_date) {
      message.error('请选择期望开票日期')
      return false
    }
    if (!formData.amount) {
      message.error('开票金额不能为空')
      return false
    }

    if (!formData.title) {
      message.error('发票抬头不能为空')
      return false
    }
    if (formData.types != 1) {

      if (!formData.ident) {
        message.error('纳税人识别号不能为空')
        return false
      }
    }
    if (formData.types >= 2 && !identReg.test(formData.ident)) {
      message.error('请输入正确的纳税人识别号')
      return false
    }

    // if (formData.types == 3) {
    // if (!formData.bank) {
    //   message.error('开户行不能为空')
    //   return false
    // }
    // if (!formData.account) {
    //   message.error('开户账号不能为空')
    //   return false
    // }
    // if (!formData.address) {
    //   message.error('开票地址不能为空')
    //   return false
    // }
    // if (!formData.tel) {
    //   message.error('电话不能为空')
    //   return false
    // }

    // }
    if (formData.collect_type == 'mail') {
      if (!formData.collect_email) {
        message.error('邮箱地址不能为空')
        return false
      }


    } else {
      if (!formData.collect_name) {
        message.error('联系人不能为空')
        return false
      }
      if (!formData.collect_tel) {
        message.error('联系电话不能为空')
        return false
      }

      if (!formData.mail_address) {
        message.error('邮寄地址不能为空')
        return false
      }
    }

    let types = uni.getStorageSync('types')
    if (!loading.value) {
      if (data.fid) {
        let id = data.fid
        loading.value = true
        clientInvoicePutApi(id, formData).then(res => {
          message.success(res.message)
          if (types == 1) {
            delayedReLaunch(`/pages/customer/list/invoice?eid=${formData.eid}&name=${data.name}`)
          } else if (types == 2) {
            delayedReLaunch(`/pages/customer/contract/details?id=${formData.cid}&tab=3`)
          } else {
            delayedReLaunch('/pages/customer/invoice/index')
          }
          loading.value = true
        }).catch(err => {
          loading.value = false
          message.error(err.message)
        })
      } else {
        loading.value = true
        clientInvoiceSaveApi(formData).then(res => {
          message.success(res.message)
          let objTitle = { eid: formData.eid, title: formData.title }
          let titleList = JSON.parse(uni.getStorageSync('titleList'))
          if (titleList.length !== 0) {
            titleList.map(item => {
              if (item.eid == objTitle.eid) {
                item.title = objTitle.title
              } else {
                titleList.push(objTitle)
              }
            })
          } else {
            titleList.push(objTitle)
          }

          uni.setStorageSync('titleList', JSON.stringify(titleList))
          loading.value = true

          if (types == 1) {
            delayedReLaunch(`/pages/customer/list/invoice?eid=${formData.eid}&name=${data.name}`)
          } else {
            delayedReLaunch(`/pages/customer/contract/details?id=${formData.cid}&tab=3`)
          }
        }).catch(err => {
          loading.value = false
          message.error(err.message)
        })
      }
    }
  }

  // 开票要求
  const sourceChange = e => {
    data.sourceIndex = e.detail.value
    formData.collect_type = data.rangeSource[data.sourceIndex].value
  }

  // 开票日期
  const bindDateChange = e => {
    formData.bill_date = e.detail.value
  }
  // 发票类型
  const typesChange = e => {
    data.typesIndex = e.detail.value
    formData.types = data.invoiceOptions[data.typesIndex].value
  }
  // 发票类目
  const categoryChange = e => {
    data.categoryIndex = e.detail.value
    formData.category_id = data.categoryOptions[data.categoryIndex].id
  }
  const startDate = computed(() => {
    return getDate('start')
  })

  const endDate = computed(() => {
    return getDate('end')
  })
  const check = e => {
    e.detail.value = (e.detail.value.match(/^\d*(\.?\d{0,2})/g)[0]) || null
    setTimeout(() => {
      formData.amount = e.detail.value
    }, 200)

  }
  // 日期选择器
  const getDate = type => {
    const date = new Date()
    let year = date.getFullYear()
    let month = date.getMonth() + 1
    let day = date.getDate()
    if (type === 'start') {
      year = year - 60
    } else if (type === 'end') {
      year = year + 2
    }
    month = month > 9 ? month : '0' + month
    day = day > 9 ? day : '0' + day
    return `${year}-${month}-${day}`
  }
</script>
<style lang="scss" scoped>
  .mt10 {
    margin-top: 20rpx;
  }

  .content {
    width: 100%;
    position: relative;

    .m10 {
      margin-top: 0;
    }

    .cr-position-header {
      background-color: #fff;
    }

    .examine-content {
      padding-top: calc($uni-default-bar-height + var(--status-bar-height));
      padding-bottom: 126rpx;
    }

    .tips {
      height: 68rpx;
      background-color: #F2F6FC;
      display: flex;
      align-items: center;
      font-size: 24rpx;
      font-family: PingFang SC-常规体, PingFang SC;
      font-weight: 400;
      color: #1890FF;
      margin-bottom: 32rpx;

      .icon-yemiantishi {
        margin-right: 10rpx;
      }
    }

    .uni-forms-item__label {
      height: auto;
      padding: 0;
      font-size: 30rpx;
      color: $uni-text-color;
      line-height: 1;

      .iconfont {
        color: #FF2529;
      }
    }

    .examine-button {
      height: 126rpx;
      line-height: 126rpx;
      width: 100%;
      padding: 0 20rpx;
      position: fixed;
      left: 0;
      bottom: 0;
      right: 0;
      display: flex;
      align-items: center;

      uni-button {
        width: 100%;
        height: 86rpx;
        line-height: 86rpx;
        font-size: $uni-font-size-default;
        border-radius: 12rpx;
      }
    }

    .list-item {
      background-color: #fff;
      border-radius: 12rpx;
      width: 100%;
      padding-left: 24rpx;
    }

    .mt36 {
      margin-top: 36rpx;

    }

    .mt20 {
      margin-top: 20rpx;
    }

    .uni-easyinput__content-textarea {
      min-height: 252rpx;
    }

    .picker-input {
      text-align: right;
      height: 35px;
      color: $uni-text-color;
      font-size: 30rpx;
      align-items: center;
      display: flex;
      justify-content: flex-end;

      .iconfont {
        padding-right: 16rpx;
        margin-top: 7rpx;
        transform: rotate(180deg);
        font-size: 24rpx;
        color: #C0C4CC;
      }
    }

    .picker-input-placeholder {
      color: #C0C4CC;
    }

    ::v-deep .uni-forms-item {
      margin-bottom: 0;
      border-bottom: 1px solid #EBEEF5;
    }

    .input-label {
      padding: 18rpx 24rpx 18rpx 0;
      align-items: center;

      ::v-deep .uni-easyinput__content-input {
        text-align: right;
        padding-right: 0 !important;
      }

      ::v-deep .uni-forms-item__label {
        max-width: 198rpx;
        display: flex;
        line-height: 1.2;
        font-family: PingFang SC-常规体, PingFang SC;

        .iconfont {
          width: 16rpx;
        }
      }
    }

    .footer {
      position: fixed;
      bottom: 0;
      width: 100%;
      height: 116rpx;
      padding: 0 30rpx;
      background-color: #fff;
      display: flex;
      justify-content: space-between;
      align-items: center;

      .btn {
        width: 345rpx;
        height: 86rpx;
        font-size: 30rpx;
        font-family: PingFang SC-常规体, PingFang SC;
        font-weight: 400;
        text-align: center;
        line-height: 86rpx;
        border-radius: 8rpx;
      }

      .cancel {
        background-color: #F0F1F5;
        color: #303133;
        margin-right: 24rpx;
      }

      .next {
        color: #fff;
        background-color: #1890FF;
      }
    }
  }

  ::v-deep .uni-input-input {
    font-size: 30rpx;
  }
</style>