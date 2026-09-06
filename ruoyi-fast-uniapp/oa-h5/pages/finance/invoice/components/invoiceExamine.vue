<template>
  <uni-popup ref="popupRef" type="right" :mask-click="false">
    <view class="slider">
      <view class="status_bar"></view>
      <default-nav-bar :is-sider="true" :default-title="data.defaultTitle"
        @goBackChange="goBackChange"></default-nav-bar>
      <view class="content m10">
        <uni-forms :border="false" label-width="80px">
          <!-- 发票开票 type = 1-->
          <template v-if="configData.type === 1">
            <view class="form-item-list" v-if="configData.type !== 0">
              <!-- 开票结果 -->
              <uni-forms-item class="input-label">
                <template v-slot:label>
                  <view class="uni-forms-item__label">开票结果<text class="iconfont">*</text></view>
                </template>
                <picker mode="selector" :value="data.payIndex" :range="data.rangePayment" range-key="name"
                  @change="payChange">
                  <view v-if="formData.status===0" class="picker-input picker-input-placeholder">
                    请选择
                    <view class="iconfont icon-fanhui"></view>
                  </view>
                  <view class="picker-input" v-else>
                    {{data.rangePayment[data.payIndex].name}}
                    <view class="iconfont icon-fanhui"></view>
                  </view>
                </picker>
              </uni-forms-item>
              <!-- 开票信息 -->
              <template v-if="formData.status === 1">
                <uni-forms-item class="input-label">
                  <template v-slot:label>
                    <view class="uni-forms-item__label">发送方式<text class="iconfont">*</text></view>
                  </template>
                  <picker mode="selector" :value="data.typeIndex" :range="data.invoiceType" range-key="name"
                    @change="typeChange">
                    <view v-if="formData.invoice_type === ''" class="picker-input picker-input-placeholder">
                      请选择
                      <view class="iconfont icon-fanhui"></view>
                    </view>
                    <view class="picker-input" v-else>
                      {{data.invoiceType[data.typeIndex].name}}
                      <view class="iconfont icon-fanhui"></view>
                    </view>
                  </picker>
                </uni-forms-item>
                <template v-if="formData.invoice_type === 'express'">
                  <uni-forms-item class="input-label">
                    <template v-slot:label>
                      <view class="uni-forms-item__label">联系人 <text class="iconfont">*</text></view>
                    </template>
                    <uni-easyinput v-model="formData.collect_name" :inputBorder="false" type="text" :clearable="false"
                      maxlength="11" :autoHeight="true" placeholder="请填写联系人">
                    </uni-easyinput>
                  </uni-forms-item>
                  <uni-forms-item class="input-label">
                    <template v-slot:label>
                      <view class="uni-forms-item__label">联系电话 <text class="iconfont">*</text></view>
                    </template>
                    <uni-easyinput v-model="formData.collect_tel" :inputBorder="false" type="text" :clearable="false"
                      maxlength="11" :autoHeight="true" placeholder="请填写联系电话">
                    </uni-easyinput>
                  </uni-forms-item>
                  <uni-forms-item class="input-label">
                    <template v-slot:label>
                      <view class="uni-forms-item__label">邮寄地址 <text class="iconfont">*</text></view>
                    </template>
                    <uni-easyinput v-model="formData.invoice_address" :inputBorder="false" type="text"
                      :clearable="false" maxlength="255" :autoHeight="true" placeholder="请填写邮寄地址">
                    </uni-easyinput>
                  </uni-forms-item>
                </template>

                <template v-else>
                  <uni-forms-item class="input-label">
                    <template v-slot:label>
                      <view class="uni-forms-item__label">邮箱地址 <text class="iconfont">*</text></view>
                    </template>
                    <uni-easyinput v-model="formData.invoice_mail" :inputBorder="false" type="text" :clearable="false"
                      maxlength="32" :autoHeight="true" placeholder="请填写邮箱地址">
                    </uni-easyinput>
                  </uni-forms-item>
                </template>

                <!-- 付款凭证 -->
                <uni-forms-item class="input-label is-direction-top">
                  <template v-slot:label>
                    <view class="uni-forms-item__label item-label-left">开票凭证</view>
                    <view class="upload">
                      <view class="box" v-if="data.imgs.src">
                        <image class="img" :src="data.imgs.src" mode=""></image>
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
                    <text class="tips">建议734*1034，大小不超过{{fileSizeOne}}M，支持jpg、jpeg、png等</text>
                  </template>
                </uni-forms-item>
              </template>
            </view>

            <view class="form-item-list">
              <uni-forms-item class="input-label is-direction-top">
                <template v-slot:label>
                  <view class="uni-forms-item__label item-label-left">{{formData.status === 1 ? '备注' : '拒绝理由'}} <text
                      v-if="formData.status === 2" class="iconfont">*</text></view>
                </template>
                <uni-easyinput :inputBorder="false" v-model="formData.remark" type="textarea" :clearable="false"
                  :maxlength="256" :autoHeight="true" :placeholder="formData.status === 1 ? '请填写备注' : '请填写拒绝理由'">
                </uni-easyinput>
              </uni-forms-item>
            </view>
          </template>
          <!-- 作废 type = 2-->
          <template v-else-if="configData.type === 2">
            <view class="form-item-list">
              <uni-forms-item class="input-label is-direction-top">
                <template v-slot:label>
                  <view class="uni-forms-item__label item-label-left">作废原因 <text class="iconfont">*</text></view>
                </template>
                <uni-easyinput :inputBorder="false" v-model="formData.remark" type="textarea" :clearable="false"
                  :maxlength="256" :autoHeight="true" placeholder="请填写作废原因">
                </uni-easyinput>
              </uni-forms-item>
            </view>
          </template>

          <!-- 作废申请 type = 3-->
          <template v-else-if="configData.type === 3">
            <!-- 作废申请 -->
            <view class="form-item-list">
              <uni-forms-item class="input-label">
                <template v-slot:label>
                  <view class="uni-forms-item__label">作废审核 <text class="iconfont">*</text></view>
                </template>
                <picker mode="selector" :value="data.cancelIndex" :range="data.cancelData" range-key="name"
                  @change="cancelChange">
                  <view v-if="formData.invalid ===0" class="picker-input picker-input-placeholder">
                    请选择
                    <view class="iconfont icon-fanhui"></view>
                  </view>
                  <view class="picker-input" v-else>
                    {{data.cancelData[data.cancelIndex].name}}
                    <view class="iconfont icon-fanhui"></view>
                  </view>
                </picker>
              </uni-forms-item>
            </view>
            <view class="form-item-list">
              <uni-forms-item class="input-label is-direction-top">
                <template v-slot:label>
                  <view class="uni-forms-item__label item-label-left">{{formData.invalid === 2 ? '备注': '原因'}} <text
                      v-if="formData.invalid === 3" class="iconfont">*</text></view>
                </template>
                <uni-easyinput :inputBorder="false" v-model="formData.remark" type="textarea" :clearable="false"
                  :maxlength="256" :autoHeight="true" :placeholder="formData.invalid === 2 ? '请填写备注': '请填写原因'">
                </uni-easyinput>
              </uni-forms-item>
            </view>
          </template>

        </uni-forms>
      </view>
      <!-- 底部 -->
      <view class="examine-button">
        <button type="primary" class="button" :loading="loading" @click="handleConfirm">确定</button>
      </view>
    </view>
  </uni-popup>
</template>

<script setup lang="ts">
  import defaultNavBar from '@/components/defaultNavBar/index.vue'
  import { uploadImage } from '@/utils/file'
  import { fileSizeOne } from '@/utils/helper'
  import message from '@/utils/message'
  import type { Res, Detail, PropType } from '@/utils/typeHelper'
  import { financeInvoiceStatusApi, financeInvoiceInvalidApi } from '@/api/finance'

  interface C {
    row : Detail
    type : number
  }
  const props = defineProps<{
    configData : C,
  }>()
  const { configData } = toRefs(props)

  interface Imgs extends PropType {
    readonly id : number
    src : string
  }
  const data = reactive({
    defaultTitle: '发票审核',
    rangePayment: [
      { id: 1, name: '已开票' },
      // { id: 2, name: '拒绝' },
    ],
    invoiceType: [
      { id: 'mail', name: '邮箱' },
      { id: 'express', name: '快递' },
    ],
    cancelData: [
      { id: 2, name: '已通过' },
      { id: 3, name: '未通过' },
    ],
    payIndex: 0,
    typeIndex: 0,
    cancelIndex: 0,
    imgs: <Imgs>{},
  })
  //结束{

  const formData = reactive({
    collect_name: '',
    collect_tel: '',
    invoice_address: '',
    invoice_mail: '',
    status: 1,
    invoice_type: 'mail',
    file: [],
    remark: '',
    invalid: 2
  })

  let emit = defineEmits(['change'])
  const popupRef = ref(null)
  const popupOpen = () => {
    popupRef.value.open()
  }

  const goBackChange = () : void => {
    cancel()
  }

  // 关闭验证码
  const cancel = () => {
    popupRef.value.close()
    formData.remark = ''
    emit('change', false)
  }

  const payChange = (e : any) : void => {
    const len = e.detail.value
    data.payIndex = len
    formData.status = data.rangePayment[len].id
  }

  const typeChange = (e : any) : void => {
    const len = e.detail.value
    data.typeIndex = len
    formData.invoice_type = data.invoiceType[len].id
  }

  const cancelChange = (e : any) : void => {
    const len = e.detail.value
    data.cancelIndex = len
    formData.invalid = data.cancelData[len].id
  }

  // 上传付款凭证
  const uploadAvatar = () => {
    uploadImage('client/file/upload', { relation_type: 'bill' }, fileSizeOne).then(res => {
      data.imgs = res.data
    }).catch(error => {
      message.error(error)
    })
  }

  const deleteImg = () : void => {
    data.imgs.src = ''
    formData.file = []
  }

  const loading : Ref<boolean> = ref(false)
  const handleConfirm = () : boolean => {
    if (configData.value.type === 1) {
      if (formData.status === 2 && !formData.remark) {
        message.error('请填写拒绝原因')
        return false
      }

      if (formData.status === 1 && formData.invoice_type === 'express' && !formData.collect_name) {
        message.error('请填写联系人')
        return false
      }
      if (formData.status === 1 && formData.invoice_type === 'express' && !formData.collect_tel) {
        message.error('请填写联系电话')
        return false
      }
      if (formData.status === 1 && formData.invoice_type === 'express' && !formData.invoice_address) {
        message.error('请填写邮寄地址')
        return false
      }
      if (formData.status === 1 && formData.invoice_type === 'mail' && !formData.invoice_mail) {
        message.error('请填写邮箱地址')
        return false
      }
      if (data.imgs.thumb_dir) {
        formData.file.push(data.imgs.id)
      }

      getBilllStatus(configData.value.row.id, formData)
    } else if (configData.value.type === 2) {
      if (!formData.remark) {
        message.error('请填写作废原因')
        return false
      }
      getBilllInvalid(configData.value.row.id, {
        invalid: 2,
        remark: formData.remark
      })
    } else if (configData.value.type === 3) {
      if (formData.invalid === 3 && !formData.remark) {
        message.error('请填写拒绝原因')
        return false
      }
      getBilllInvalid(configData.value.row.id, {
        invalid: formData.invalid,
        remark: formData.remark
      })
    }
  }

  // 财务发票审核
  const getBilllStatus = (id : number, datas : object) : void => {
    loading.value = true
    financeInvoiceStatusApi(id, datas).then((res : Res) : void => {
      message.error(res.message)
      loading.value = false
      cancel()
    }).catch((error : Res) : void => {
      message.error(error.message)
      loading.value = false
    })
  }

  // 发票作废
  const getBilllInvalid = (id : number, datas : object) : void => {
    loading.value = true
    financeInvoiceInvalidApi(id, datas).then((res : Res) : void => {
      message.error(res.message)
      loading.value = false
      cancel()
    }).catch((error : Res) : void => {
      message.error(error.message)
      loading.value = false
    })
  }

  watch(configData, (newvalue) => {
    if (newvalue.type === 1) {
      data.defaultTitle = '发票审核'
      formData.invoice_type = newvalue.row.collect_type
      if (formData.invoice_type === 'express') {
        data.typeIndex = 1
      } else {
        data.typeIndex = 0
      }
      // data.typeIndex = getFindIndex(data.invoiceType, formData.invoice_type)
      formData.invoice_mail = newvalue.row.collect_email
      formData.invoice_address = newvalue.row.mail_address
      formData.collect_tel = newvalue.row.collect_tel
      formData.collect_name = newvalue.row.collect_name
    } else if (newvalue.type === 2) {
      data.defaultTitle = '发票作废'
    } else if (newvalue.type === 3) {
      data.defaultTitle = '作废审核'
    }
  })

  defineExpose({ popupOpen })
</script>

<style lang="scss" scoped>
  ::v-deep .uni-popup {
    z-index: 999;
  }

  .icon-shenpizhongxin-jujue {
    font-size: 14px;
    color: #fff;

  }

  ::v-deep .uni-popup__wrapper.left {
    padding-top: 0 !important;
  }

  .slider {
    height: 100vh;
    width: 100vw;
    background-color: $uni-default-bg;

    .content {
      background-color: $uni-default-bg;

      .form-item-list {
        background-color: #fff;
        border-radius: 12rpx;
        margin-bottom: 20rpx;

        &:last-of-type {
          margin-bottom: 0;
        }


        .input-label {
          padding: 18rpx 24rpx 18rpx 0;
          margin-left: 24rpx;
          align-items: center;
          margin-bottom: 0;
          border-bottom: 1px solid #EBEEF5;

          &:last-of-type {
            border-bottom: none;
          }

          ::v-deep .uni-easyinput__content-input {
            text-align: right;
            padding-right: 0 !important;
          }

          ::v-deep .uni-easyinput__placeholder-class {
            color: $uni-text-color-five;
            font-size: $uni-font-size-default;
          }

          ::v-deep .uni-icons {
            display: none;
          }

          ::v-deep .uni-forms-item__content {
            width: 100%;
          }

          ::v-deep .uni-input-input {
            font-size: $uni-font-size-default;
            color: $uni-text-color;
            text-align: right;
          }

          ::v-deep .uni-forms-item__label {
            max-width: 198rpx;
            display: flex;
            line-height: 1.2;

            .iconfont {
              width: 16rpx;
            }
          }

          .uni-date-x,
          .uni-date__x-input {
            padding: 0;
          }

          .uni-forms-item__label {
            height: auto;
            padding: 0;
            font-size: $uni-font-size-default;
            color: $uni-text-color;
            line-height: 1;
            font-family: PingFang SC-常规体, PingFang SC;

            .iconfont {
              color: #FF2529;
            }
          }

          // 选择器样式
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

            &.picker-input-placeholder {
              color: #C0C4CC;
            }
          }

          //上传样式
          &.is-direction-top {
            align-items: flex-start;
          }

          .item-label-left {
            padding-top: 20rpx;
          }

          .upload {
            width: 100%;
            min-height: 236rpx;
            display: flex;
            align-items: center;
            padding: 0 28rpx;

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

          .tips {
            margin-left: 20rpx;
            font-size: 20rpx;
            font-family: PingFang SC-常规体, PingFang SC;
            font-weight: 400;
            color: #999999;
            margin-bottom: 20rpx;
          }
        }
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
  }
</style>