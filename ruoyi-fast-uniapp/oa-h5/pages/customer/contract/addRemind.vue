<template>
  <view class="content">
    <!-- 添加付款提醒页面  回款提醒：formData.types==0 续费提醒：formData.types==1 -->
    <view class="cr-position-header">
      <view class="status_bar"></view>
      <default-nav-bar :is-right="true" :default-title="data.defaultTitle">
      </default-nav-bar>
    </view>
    <!-- 表单内容 -->
    <view class="examine-content m10">
      <uni-forms :border="false" label-width="80px">
        <view class="list-item ">
          <uni-forms-item class="input-label">
            <template v-slot:label>
              <view class="uni-forms-item__label">提醒类型 <text class="iconfont">*</text> </view>
            </template>
            <picker mode="selector" :value="formData.types" :range="data.rangeType" range-key="label"
              @change="typeChange($event)">
              <view v-if="formData.type===''" class="picker-input picker-input-placeholder">
                请选择
                <view class="iconfont icon-fanhui"></view>
              </view>
              <view class="picker-input" v-else>
                {{data.rangeType[formData.types].label}}
                <view class="iconfont icon-fanhui"></view>
              </view>
            </picker>
          </uni-forms-item>
        </view>
        <!-- 回款提醒 -->
        <view class="list-item mt20" v-if="formData.types==0">
          <uni-forms-item class="input-label">
            <template v-slot:label>
              <view class="uni-forms-item__label">预计回款金额 <text class="iconfont">*</text></view>
            </template>
            <uni-easyinput :inputBorder="false" v-model="formData.num" type="number" :clearable="false" :styles="styles"
              :placeholder-style="placeholderStyle" :maxlength="99999999.99" :autoHeight="true" placeholder="请填写">
            </uni-easyinput>
          </uni-forms-item>
          <uni-forms-item class="input-label">
            <template v-slot:label>
              <view class="uni-forms-item__label">回款提醒日期 <text class="iconfont">*</text></view>
            </template>
            <uni-datetime-picker type="datetime" :clear-icon="false" :border="false" v-model="formData.time"
              @change="timeChange">
              <view v-if="!formData.time" class="picker-input picker-input-placeholder">
                请选择
                <view class="iconfont icon-fanhui"></view>
              </view>
              <view class="picker-input" v-if="formData.time">
                {{formData.time}}
              </view>
            </uni-datetime-picker>
          </uni-forms-item>
        </view>
        <!-- 续费提醒 -->
        <view class="list-item mt20" v-else>
          <!--  <uni-forms-item class="input-label">
            <template v-slot:label>
              <view class="uni-forms-item__label">续费类型 <text class="iconfont">*</text> </view>
            </template>
            <picker mode="selector" :range="data.renewal" range-key="title" @change="renewalChange($event)">
              <view v-if="!formData.cate_id" class="picker-input picker-input-placeholder">
                请选择
                <view class="iconfont icon-fanhui"></view>
              </view>
              <view class="picker-input" v-if="formData.cate_id">
                {{data.renewal[getFindIndex(data.renewal, formData.cate_id)].title}}
                <view class="iconfont icon-fanhui"></view>
              </view>
            </picker>
          </uni-forms-item> -->

          <uni-forms-item class="input-label">
            <template v-slot:label>
              <view class="uni-forms-item__label">续费金额 <text class="iconfont">*</text></view>
            </template>
            <uni-easyinput :inputBorder="false" v-model="formData.num" type="number" :clearable="false" :styles="styles"
              :placeholder-style="placeholderStyle" @blur="blurNum" :autoHeight="true" placeholder="请填写">
            </uni-easyinput>
          </uni-forms-item>
          <uni-forms-item class="input-label">
            <template v-slot:label>
              <view class="uni-forms-item__label">续费提醒日期 <text class="iconfont">*</text></view>
            </template>
            <uni-datetime-picker type="datetime" :clear-icon="false" :border="false" v-model="formData.time"
              @change="timeChange">
              <view v-if="!formData.time" class="picker-input picker-input-placeholder">
                请选择
                <view class="iconfont icon-fanhui"></view>
              </view>
              <view class="picker-input" v-if="formData.time">
                {{formData.time}}
              </view>
            </uni-datetime-picker>
          </uni-forms-item>
        </view>
        <!-- 备注 -->
        <view class="list-item mt20 p24">
          <uni-forms-item class="is-direction-top">
            <template v-slot:label>
              <view class="uni-forms-item__label mt36">
                提醒内容
                <text class="iconfont">*</text>
              </view>
            </template>
            <uni-easyinput :inputBorder="false" v-model="formData.mark" type="textarea" :clearable="false"
              :styles="styles" :placeholder-style="placeholderStyle" :maxlength="256" :autoHeight="true"
              placeholder="填写提醒内容">
            </uni-easyinput>
          </uni-forms-item>
        </view>
      </uni-forms>
    </view>
    <!-- 底部 -->
    <view class="examine-button">
      <button type="primary" :loading="loading" @click="handleConfirm">提交</button>
    </view>
    <!-- 组件 -->
  </view>
</template>

<script setup>
  import defaultNavBar from '@/components/defaultNavBar/index'
  import {
    ref,
    reactive,
    toRefs
  } from 'vue'
  import message from '@/utils/message'
  import {
    onLoad
  } from '@dcloudio/uni-app'
  import {
    delayedReLaunch,
    getFindIndex,
    number,
    debounce
  } from '@/utils/helper'
  import {
    clientSourceApi,
    clientRemindSaveApi,
    clientRemindDetailApi,
    clientRemindEditApi
  } from '@/api/customer'
  const placeholderStyle = ref('color: #C0C4CC;font-size: 30rpx')
  const styles = reactive({
    color: '#303133',
    disableColor: '#ffffff'
  })

  const data = reactive({
    defaultTitle: '添加付款提醒',
    rangeType: [{
        label: '回款提醒',
        value: 0
      },
      {
        label: '续费提醒',
        value: 1
      },
    ],
    renewal: [],
    rateIndex: 2,
    rangeRate: [{
        label: '按天',
        value: 0
      },
      {
        label: '按周',
        value: 1
      },
      {
        label: '按月',
        value: 2
      },
      {
        label: '按年',
        value: 3
      },
    ],
    cid: -1,
    eid: -1,
    tab: 2,
    id: 0
  })
  // 定义表单
  const formData = reactive({
    types: 0,
    cate_id: '',
    time: '',
    mark: '',
    num: ''
  })
  onLoad(async e => {
    await getRenewList()
    data.cid = e.cid
    data.eid = e.eid
    if (e.id) {
      data.id = e.id
      data.defaultTitle = '编辑付款提醒'
      getRemindDetail(e.id)
    }
  })

  // 获取续费类型
  const getRenewList = () => {
    clientSourceApi({
      keys: ['renew']
    }).then(res => {
      data.renewal = res.data ? res.data.renew : []
    }).catch(error => {
      message.error(error.message)
    })
  }

  // 获取付款记录详情
  const getRemindDetail = id => {
    clientRemindDetailApi(id).then(res => {
      formData.types = res.data.types
      formData.cate_id = res.data.cate_id
      // formData.period = res.data.period
      // formData.rate = res.data.rate
      formData.time = res.data.time
      formData.mark = res.data.mark
      formData.num = res.data.num
    }).catch(error => {
      message.error(error.message)
    })
  }


  // 保存付款提醒
  const loading = ref(false)
  const remindSave = datas => {
    loading.value = true
    clientRemindSaveApi(datas).then(res => {
      loading.value = false
      message.success(res.message)
      delayedReLaunch(`/pages/customer/contract/details?id=${data.cid}&tab=${data.tab}`)
    }).catch(error => {
      loading.value = false
      message.error(error.message)
    })
  }
  // 编辑付款提醒
  const remindEdit = (id, datas) => {
    loading.value = true
    clientRemindEditApi(id, datas).then(res => {
      loading.value = false
      message.success(res.message)
      delayedReLaunch(`/pages/customer/contract/details?id=${data.cid}&tab=${data.tab}`)
    }).catch(error => {
      loading.value = false
      message.error(error.message)
    })
  }

  const blurNum = () => {
    formData.num = Math.abs(formData.num)
  }

  // 提交表单
  const handleConfirm = debounce(() => {
    if (!data.cid) {
      message.error('缺少必要参数不能添加')
      return false
    }

    if (formData.types == 1) {
      // if (!formData.cate_id) {
      //   message.error('续费类型不能为空')
      //   return false
      // }


      if (!formData.num) {
        message.error('续费金额不能为空')
        return false
      }
      if (!formData.time) {
        message.error('续费日期不能为空')
        return false
      }
    }

    if (!formData.mark) {
      message.error('提醒内容不能为空')
      return false
    }

    formData.cid = data.cid
    formData.eid = data.eid
    if (data.id) {
      remindEdit(data.id, formData)
    } else {
      remindSave(formData)
    }
  }, 800)
  // 提醒类型 
  const typeChange = e => {
    let len = e.detail.value
    formData.types = data.rangeType[len].value
  }

  // 续费周期
  const rateChange = e => {
    let len = e.detail.value
    data.rateIndex = len
    formData.period = data.rangeRate[len].value
  }

  // 续费类型
  const renewalChange = e => {
    let len = e.detail.value
    formData.cate_id = data.renewal[len].id
  }

  const timeChange = e => {
    formData.time = e
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

<style lang="scss">
  .content {
    width: 100%;
    position: relative;

    .cr-position-header {
      background-color: #fff;
    }

    .tips {
      font-size: 20rpx;
      font-family: PingFang SC-常规体, PingFang SC;
      font-weight: 400;
      color: #999999;
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
      font-family: PingFang SC-常规体, PingFang SC;
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

    .p24 {
      padding: 0 24rpx;
    }

    .upload {
      width: 100%;
      min-height: 276rpx;
      display: flex;
      align-items: center;
      padding: 36rpx 28rpx;

      .img {
        display: block;
        width: 140rpx;
        height: 140rpx;
        margin-right: 20rpx;
      }

      .upload-box {
        width: 140rpx;
        height: 140rpx;
        border-radius: 8rpx 8rpx 8rpx 8rpx;
        border: 2rpx solid #DDDDDD;
        display: flex;
        justify-content: center;
        align-items: center;

        .icon-xuanfuanniu-jia {
          font-size: 35rpx;
          color: #BFBFBF;
        }
      }
    }

    .list-item {
      background-color: #fff;
      border-radius: 12rpx;
      padding-left: 24rpx;
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