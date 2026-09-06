<template>
  <view class="content">
    <!-- 添加付款和续费页面 -->
    <view class="cr-position-header">
      <view class="status_bar"></view>
      <default-nav-bar :is-right="true" :default-title="data.defaultTitle"></default-nav-bar>
    </view>
    <!-- 表单内容 -->
    <view class="examine-content m10">
      <uni-forms :border="false" label-width="80px">
        <view class="list-item ">
          <!-- 选择合同 -->
          <uni-forms-item class="input-label" v-if="!data.isContract">
            <template v-slot:label>
              <view class="uni-forms-item__label">选择合同 <text class="iconfont">*</text>
              </view>
            </template>
            <picker mode="selector" :range="data.contractList" :disabled="data.isContract" range-key="title"
              @change="contractChange($event)">
              <view v-if="!formData.cid" class="picker-input picker-input-placeholder">
                请选择
                <view class="iconfont icon-fanhui"></view>
              </view>
              <view class="picker-input" v-else>
                <template v-if="formData.cid && data.contractList.length> 0">
                  {{data.contractList[getFindIndex(data.contractList, formData.cid)].title}}
                </template>
                <view class="iconfont icon-fanhui"></view>
              </view>
            </picker>

          </uni-forms-item>
          <!-- 业务类型 -->
          <!--    <uni-forms-item class="input-label">
            <template v-slot:label>
              <view class="uni-forms-item__label">业务类型 <text class="iconfont">*</text></view>
            </template>
            <picker mode="selector" :value="formData.types" :range="data.rangeType" range-key="label"
              @change="typeChange">
              <view class="picker-input">
                {{data.rangeType[data.typesIndex].label}}
                <view class="iconfont icon-fanhui"></view>
              </view>
            </picker>
          </uni-forms-item> -->
          <uni-forms-item class="input-label" v-if="formData.types==1">
            <template v-slot:label>
              <view class="uni-forms-item__label">续费类型 <text class="iconfont">*</text></view>
            </template>
            <picker mode="selector" :value="formData.cate_id" :range="data.rangeCate" range-key="title"
              @change="CateChange">
              <view v-if="formData.cate_id===''" class="picker-input picker-input-placeholder">
                请选择
                <view class="iconfont icon-fanhui"></view>
              </view>
              <view class="picker-input" v-else>
                {{data.rangeCate[data.cateIndex].title}}
                <view class="iconfont icon-fanhui"></view>
              </view>
            </picker>
          </uni-forms-item>

          <uni-forms-item class="input-label" v-if="formData.types==1">
            <template v-slot:label>
              <view class="uni-forms-item__label">续费金额(元) <text class="iconfont">*</text></view>
            </template>
            <uni-easyinput :inputBorder="false" v-model="formData.num" type="number" :clearable="false" :styles="styles"
              :placeholder-style="placeholderStyle" maxlength="11" :autoHeight="true" placeholder="请填写">
            </uni-easyinput>
          </uni-forms-item>
          <!-- 回款金额 -->
          <uni-forms-item class="input-label" v-else>
            <template v-slot:label>
              <view class="uni-forms-item__label">回款金额(元) <text class="iconfont">*</text></view>
            </template>
            <uni-easyinput :inputBorder="false" v-model="formData.num" type="number" :clearable="false" :styles="styles"
              :placeholder-style="placeholderStyle" maxlength="11" :autoHeight="true" placeholder="请填写">
            </uni-easyinput>
          </uni-forms-item>
          <uni-forms-item class="input-label" v-if="formData.types==1">
            <template v-slot:label>
              <view class="uni-forms-item__label">续费结束日期 </view>
            </template>
            <picker mode="date" :value="formData.end_date" :start="startDate" :end="endDate" @change="bindDateChange">
              <view v-if="!formData.end_date" class="picker-input picker-input-placeholder">
                请选择
                <view class="iconfont icon-fanhui"></view>
              </view>
              <view class="picker-input" v-if="formData.end_date">
                {{formData.end_date}}
                <view class="iconfont icon-fanhui"></view>
              </view>
            </picker>
          </uni-forms-item>
          <!-- 支付方式 -->
          <uni-forms-item class="input-label">
            <template v-slot:label>
              <view class="uni-forms-item__label">支付方式<text class="iconfont">*</text></view>
            </template>
            <picker mode="selector" :value="formData.type_id" :range="data.rangePayment" range-key="name"
              @change="payChange">
              <view v-if="formData.type_id===''" class="picker-input picker-input-placeholder">
                请选择
                <view class="iconfont icon-fanhui"></view>
              </view>
              <view class="picker-input" v-if="formData.type_id && data.rangePayment.length > 0">
                {{data.rangePayment[data.payIndex].name}}
                <view class="iconfont icon-fanhui"></view>
              </view>
            </picker>
          </uni-forms-item>
          <!-- 开始时间 -->
          <uni-forms-item class="input-label">
            <template v-slot:label>
              <view class="uni-forms-item__label">付款时间<text class="iconfont">*</text></view>
            </template>
            <uni-datetime-picker type="datetime" :clear-icon="false" :border="false" v-model="formData.date">
              <view v-if="!formData.date" class="picker-input picker-input-placeholder">
                请选择
                <view class="iconfont icon-fanhui"></view>
                <view class="picker-input" v-if="formData.date">
                  {{formData.date}}
                </view>
              </view>
            </uni-datetime-picker>
          </uni-forms-item>

          <!-- 付款凭证 -->
          <uni-forms-item class="is-direction-top">
            <template v-slot:label>
              <view class="uni-forms-item__label mt36 p24">
                付款凭证
              </view>
              <view class="upload">
                <view class="box" v-if="data.imgs!==''">
                  <!-- {{data.imgs.src}} -->
                  <image class="img" :src="data.imgs.src" mode="">
                  </image>
                  <view class="delete" @click="deleteImg">
                    <text class="iconfont icon-shenpizhongxin-jujue"></text>
                  </view>
                </view>

                <view v-else class="upload-box" @click="uploadAvatar">
                  <view class="iconfont icon-paizhao"></view>
                  <view class="text">
                    上传凭证
                  </view>
                </view>
              </view>
              <text class="tips">建议734*1034，大小不超过{{fileSizeOne}}M，支持jpg、jpeg、png
                等</text>
            </template>
          </uni-forms-item>
        </view>
        <!-- 备注 -->
        <view class="list-item mt20 p24">
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
    <view class="examine-button">
      <button type="primary" class="button" :loading="loading" @click="handleConfirm">提交</button>
    </view>

  </view>
</template>
<script setup>
  import { ref, reactive, toRefs, computed } from 'vue'
  import message from '@/utils/message'
  import defaultNavBar from '@/components/defaultNavBar/index'
  import baTreePicker from '@/components/baTreePicker/index.vue'
  import {
    billSaveApi,
    contractSelectApi,
    payTypeApi,
    clientSourceApi,
    billDetailApi,
    billPutApi,
    contractPriceApi,
    clientRemindDetailApi
  } from '@/api/customer'
  import moment from 'moment'

  const placeholderStyle = ref('color: #C0C4CC;font-size: 30rpx')
  const styles = reactive({
    color: '#303133',
    disableColor: '#ffffff'
  })
  const data = reactive({
    defaultTitle: '',
    contractIndex: '',
    typesIndex: 0,
    payIndex: '',
    cateIndex: '',
    imgs: '',
    type: 3, // 3：付款 5：续费
    rangePayment: [],
    contractList: [],
    rangeCate: [],
    treeData: [],
    rangeType: [
      { value: 0, label: '回款' },
      { value: 1, label: '续费' },
    ],
    cid: 0, // 合同id
    id: '', // 付款id
    tab: 1,
    isContract: false,
    rId: 0,
    isSchedule: false
  })
  // 定义表单
  const formData = reactive({
    eid: '',
    cid: '',
    types: 0,
    cate_id: '',
    date: '',
    num: '',
    attach_ids: [],
    type_id: '',
    mark: '',
    end_date: '',
    remind_id: ''
  })
  import { onLoad } from '@dcloudio/uni-app'
  onLoad(async e => {
    getPayType()
    if (e.eid) {
      formData.eid = e.eid
    }
    if (e.paymentType) {
      data.type = e.paymentType
      data.defaultTitle = e.paymentType == 3 ? '添加回款' : '添加续费'
      formData.types = e.paymentType == 3 ? 0 : 1
    }
    formData.date = moment(new Date()).format('YYYY-MM-DD HH:mm:ss')
    await getContractSelect(formData.eid)
    if (e.cid) {
      formData.cid = Number(e.cid)
      data.isContract = true
    }
    if (e.price) {
      formData.num = e.price
    }

    if (e.type && e.type == 1) {
      data.isSchedule = true
    }

    await getSourceData()
    // 编辑付款
    if (e.id) {
      data.id = e.id
      data.defaultTitle = '编辑付款'
      setTimeout(() => {
        getBillDetail(e.id)
      }, 300)
    }
    // 根据提醒创建付款记录
    if (e.rid) {
      data.rId = Number(e.rid)
      clientRemind(data.rId)
    }
  })

  // 获取合同列表
  const getContractSelect = id => {
    let types = uni.getStorageSync('types')
    contractSelectApi(id).then(res => {
      data.contractList = res.data
      if (data.contractList.length <= 0) {
        message.error('暂无合同，请添加合同')
      } else {
        if (types == 1) {
          formData.cid = res.data[0].id
          getPrice(formData.cid)
        }
      }
    }).catch(error => {
      message.error(error.message)
    })
  }
  // 获取支付方式接口
  const getPayType = () => {
    payTypeApi().then(res => {
      data.rangePayment = res.data.list
    }).catch(error => {
      message.error(error.message)
    })
  }

  // 获取付款提醒详情
  const clientRemind = (id) => {
    clientRemindDetailApi(id).then(res => {
      const config = res.data
      formData.types = config.types
      formData.num = config.num
      formData.remind_id = config.id

      if (config.cate_id > 0) {
        formData.cate_id = config.cate_id
        data.cateIndex = data.rangeCate.findIndex(item => item.id === config.cate_id)
      }

      data.tab = 2
      data.typesIndex = data.rangeType.findIndex(item => item.value === formData.types)
    }).catch(error => {
      message.error(error.message)
    })
  }

  // 编辑付款记录
  const getBillDetail = id => {
    billDetailApi(id).then(res => {
      formData.num = res.data.num
      formData.date = res.data.date
      formData.cid = res.data.cid
      formData.type_id = res.data.type_id
      formData.types = res.data.types
      if (res.data.renew) {
        formData.cate_id = res.data.renew.id
        data.cateIndex = data.rangeCate.findIndex(item => item.id === formData.cate_id)
      }
      if (res.data.end_date == '0000-00-00') {
        formData.end_date = ''
      } else {
        formData.end_date = res.data.end_date
      }

      data.payIndex = data.rangePayment.findIndex(item => item.id === formData.type_id)
      data.contractIndex = data.contractList.findIndex(item => item.id === formData.cid)
      data.typesIndex = data.rangeType.findIndex(item => item.value === formData.types)
      if (res.data.attachs.length > 0) {
        formData.attach_ids = res.data.attachs.id
        data.imgs = res.data.attachs[0]
      } else {
        data.imgs = ''
      }
    })
  }

  const deleteImg = () => {
    data.imgs = ''
    formData.attach_ids = []
  }

  // 获取续费类型接口
  const getSourceData = () => {
    let dataInfo = { keys: ['renew'] }
    clientSourceApi(dataInfo).then(res => {
      data.rangeCate = res.data.renew
    }).catch(error => {
      message.error(error.message)
    })
  }
  // 选后回调
  const selectChange = e => {
    const res = {
      title: data.default.title,
      content: data.default.content,
      pid: e[0]
    }
    setMemorialEdit(data.default.id, res)
  }
  import { delayedReLaunch, getFindIndex, debounce, fileSizeOne } from '@/utils/helper'
  const loading = ref(false)
  // 提交表单
  const handleConfirm = debounce(() => {
    if (!formData.cid) {
      message.error('合同不能为空')
      return false
    }
    if (formData.types === 1) {
      if (!formData.cate_id) {
        message.error('续费类型不能为空')
        return false
      }
      if (!formData.num) {
        message.error('续费金额不能为空')
        return false
      }
    } else {
      if (!formData.num) {
        message.error('回款金额不能为空')
        return false
      }
    }
    if (!formData.type_id) {
      message.error('支付方式不能为空')
      return false
    }
    if (!formData.date) {
      message.error('付款时间不能为空')
      return false
    }
    formData.attach_ids = data.imgs.attach_id

    if (data.id) {
      let id = data.id
      loading.value = true
      billPutApi(id, formData).then(res => {
        message.success(res.message)
        loading.value = true
        let types = uni.getStorageSync('types')
        if (types == 1) {
          let type = 3
          delayedReLaunch(`/pages/customer/list/details?id=${formData.eid}&type=${type}`)
        } else {
          delayedReLaunch(`/pages/customer/contract/details?id=${formData.cid}&tab=${data.tab}`)
        }
      }).catch(err => {
        message.error(err.message)
      })

    } else {
      loading.value = true
      billSaveApi(formData).then(res => {
        loading.value = true
        message.success(res.message)
        let typeInfo = uni.getStorageSync('types')
        if (typeInfo == 2) {
          // 合同列表添加
          const query = data.isSchedule ? '/pages/users/schedule/index' :
            `/pages/customer/contract/details?id=${formData.cid}&tab=${data.tab}`
          delayedReLaunch(query)
        } else {
          // 客户列表添加
          let type = 3
          delayedReLaunch(`/pages/customer/list/details?id=${formData.eid}&type=${type}`)
        }
      }).catch(error => {
        loading.value = false
        message.error(error.message)
      })
    }
  }, 500)

  const contractChange = e => {
    let len = e.detail.value
    data.contractIndex = len
    formData.cid = data.contractList[len].id
    if (data.rId === 0) {
      getPrice(formData.cid)
    }
  }
  const getPrice = id => {
    contractPriceApi(id).then(res => {
      formData.num = res.data.price
    })
  }
  const typeChange = e => {
    let len = e.detail.value
    data.typesIndex = len
    formData.types = data.rangeType[len].value
  }

  const CateChange = e => {
    let len = e.detail.value
    data.cateIndex = len
    formData.cate_id = data.rangeCate[len].id
  }

  const payChange = e => {
    const len = e.detail.value
    data.payIndex = len
    formData.type_id = data.rangePayment[len].id
  }

  const startDate = computed(() => {
    return getDate('start')
  })
  const endDate = computed(() => {
    return getDate('end')
  })
  const bindDateChange = e => {
    formData.end_date = e.detail.value
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
  // 上传付款凭证
  import { uploadImage } from '@/utils/file'
  const uploadAvatar = () => {
    uploadImage('attach/imgs', { relation_type: 'bill' }, fileSizeOne).then(res => {
      data.imgs = res.data
    }).catch(error => {
      message.error(error)
    })
  }
</script>

<style lang="scss">
  .content {
    width: 100%;
    position: relative;

    .cr-position-header {
      background-color: #fff;
    }

    .tips {
      margin-left: 20rpx;
      font-size: 20rpx;
      font-family: PingFang SC-常规体, PingFang SC;
      font-weight: 400;
      color: #999999;
      margin-bottom: 20rpx;
    }

    ::v-deep .uni-input-wrapper {
      text-align: right;
    }

    ::v-deep .uni-date-x {
      padding: 0;
      color: #303133;
      font-size: 30rpx;
    }

    ::v-deep .uni-date__x-input {
      padding: 0;
    }

    .examine-content {
      padding-top: calc($uni-default-bar-height + var(--status-bar-height));
      padding-bottom: 126rpx;
    }

    .uni-forms-item__label {
      height: auto;
      padding: 0;
      font-size: 30rpx;
      color: $uni-text-color;
      line-height: 1;
      font-family: PingFang SC-常规体, PingFang SC;

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
      background-color: $uni-default-bg;
      border: 1px solid #F0F1F5;

      .button {

        width: 100%;
        height: 86rpx;
        line-height: 86rpx;
        font-size: $uni-font-size-default;
        border-radius: 12rpx;
      }
    }

    .p24 {
      padding: 0 24rpx;
    }

    .upload {

      width: 100%;
      min-height: 236rpx;
      display: flex;
      align-items: center;
      padding: 28rpx 28rpx 0 28rpx;


      .box {
        position: relative;

        .img {
          display: block;
          width: 140rpx;
          height: 140rpx;
          margin-right: 20rpx;
        }

        .delete {
          position: absolute;
          top: 0;
          right: 20rpx;
          width: 32rpx;
          height: 32rpx;
          background: rgba(0, 0, 0, 0.6);
          border-radius: 0 8rpx 0 16rpx;
          display: flex;
          align-items: center;
          justify-content: center;

          .icon-paizhao {
            font-size: 35rpx;
            color: #BFBFBF;
          }
        }
      }


      .upload-box {
        width: 140rpx;
        height: 140rpx;
        border-radius: 8rpx 8rpx 8rpx 8rpx;
        border: 2rpx solid #DDDDDD;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;

        .icon-paizhao {
          font-size: 40rpx;
          color: #BFBFBF;
        }

        .text {
          margin-top: 20rpx;
          font-size: 24rpx;
          font-family: PingFang SC-常规体, PingFang SC;
          font-weight: 400;
          color: #999999;
        }
      }
    }

    .list-item {
      background-color: #fff;
      border-radius: 12rpx;
      width: 100%;
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

      &:last-of-type {
        border-bottom: none;
      }
    }

    .input-label {
      padding: 18rpx 24rpx 18rpx 0;
      margin-left: 24rpx;
      align-items: center;

      ::v-deep .uni-easyinput__content-input {
        text-align: right;
        padding-right: 0 !important;
      }

      ::v-deep .uni-icons {
        display: none;
      }

      ::v-deep .uni-forms-item__label {
        max-width: 198rpx;
        display: flex;
        line-height: 1.2;

        .iconfont {
          width: 16rpx;
        }
      }
    }
  }

  ::v-deep .uni-input-input {
    font-size: 30rpx;
  }
</style>