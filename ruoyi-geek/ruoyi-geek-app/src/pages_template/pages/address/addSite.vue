<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import tab from '@/plugins/tab';
import citySelect from '@/pages_template/components/u-city-select/u-city-select.vue';
import { useAddressEditPage } from './index';

// 使用编辑页面Hook
const {
  isEdit,
  form,
  defaultAddress,
  selectedTag,
  addressTags,
  initEditPage,
  saveAddress,
  deleteAddress
} = useAddressEditPage();

// 地区选择器显示状态
const showRegionPicker = ref(false);

// 表单错误状态 - 移到Vue组件中
const formErrors = reactive({
  name: false,
  phone: false,
  region: false,
  address: false
});

// 重置表单错误
function resetFormErrors() {
  formErrors.name = false;
  formErrors.phone = false;
  formErrors.region = false;
  formErrors.address = false;
}

// 初始化页面数据
onMounted(() => {
  const pages = getCurrentPages();
  const currentPage: any = pages[pages.length - 1];
  const options = currentPage.$page?.options;

  // 调用hook的初始化方法
  initEditPage(options?.id);
  // 重置表单错误状态
  resetFormErrors();
});

// 设置默认地址
function handleSetDefault(e: any) {
  defaultAddress.value = e.detail.value;
}

// 显示地区选择器
function handleShowRegionPicker() {
  showRegionPicker.value = true;
}

// 确认选择地区
function handleCityChange(e: any) {
  form.region = e.province.label + e.city.label + e.area.label;
  formErrors.region = false;
}

// 选择标签
function handleSelectTag(tag: string) {
  selectedTag.value = tag;
}

// 表单验证
function validateForm(): boolean {
  // 验证姓名
  formErrors.name = !form.name.trim();

  // 验证手机号
  const phoneReg = /^1[3-9]\d{9}$/;
  formErrors.phone = !phoneReg.test(form.phone);

  // 验证地区
  formErrors.region = !form.region;

  // 验证详细地址
  formErrors.address = !form.address.trim();

  // 如果有任何错误，返回false
  return !(formErrors.name || formErrors.phone || formErrors.region || formErrors.address);
}

// 保存地址
function handleSaveAddress() {
  // 使用本地验证方法
  if (!validateForm()) {
    uni.showToast({
      title: '请填写完整信息',
      icon: 'none'
    });
    return;
  }

  try {
    const success = saveAddress();

    if (success) {
      uni.showToast({
        title: isEdit.value ? '修改成功' : '添加成功',
        icon: 'success'
      });

      // 延迟返回，让用户看到提示
      setTimeout(() => {
        tab.navigateBack();
      }, 1000);
    } else {
      uni.showToast({
        title: '保存失败',
        icon: 'none'
      });
    }
  } catch (e) {
    console.error('保存地址失败', e);
    uni.showToast({
      title: '保存失败',
      icon: 'none'
    });
  }
}

// 删除地址
function handleDeleteAddress() {
  if (!isEdit.value) return;

  uni.showModal({
    title: '提示',
    content: '确定要删除此地址吗？',
    success: (res) => {
      if (res.confirm) {
        try {
          const success = deleteAddress();

          if (success) {
            uni.showToast({
              title: '删除成功',
              icon: 'success'
            });

            setTimeout(() => {
              tab.navigateBack();
            }, 1000);
          } else {
            uni.showToast({
              title: '删除失败',
              icon: 'none'
            });
          }
        } catch (e) {
          console.error('删除地址失败', e);
          uni.showToast({
            title: '删除失败',
            icon: 'none'
          });
        }
      }
    }
  });
}
</script>

<template>
  <view class="wrap">
    <view class="container">
      <view class="top">
        <view class="item">
          <view class="left">
            <text class="required">*</text>收货人
          </view>
          <input type="text" v-model="form.name" placeholder-class="line" placeholder="请填写收货人姓名"
            :class="{ 'error-input': formErrors.name }" />
          <u-icon name="account" size="36rpx" color="#999"></u-icon>
        </view>
        <view class="error-msg" v-if="formErrors.name">请输入收货人姓名</view>

        <view class="item">
          <view class="left">
            <text class="required">*</text>手机号码
          </view>
          <input type="number" v-model="form.phone" placeholder-class="line" placeholder="请填写收货人手机号" maxlength="11"
            :class="{ 'error-input': formErrors.phone }" />
          <u-icon name="phone" size="36rpx" color="#999"></u-icon>
        </view>
        <view class="error-msg" v-if="formErrors.phone">请输入正确的手机号码</view>

        <view class="item" @tap="handleShowRegionPicker">
          <view class="left">
            <text class="required">*</text>所在地区
          </view>
          <input disabled v-model="form.region" type="text" placeholder-class="line" placeholder="省市区县、乡镇等"
            :class="{ 'error-input': formErrors.region }" />
          <u-icon name="arrow-right" size="36rpx" color="#999"></u-icon>
        </view>
        <view class="error-msg" v-if="formErrors.region">请选择所在地区</view>

        <view class="item address">
          <view class="left">
            <text class="required">*</text>详细地址
          </view>
          <textarea v-model="form.address" type="text" placeholder-class="line" placeholder="街道、楼牌等"
            :class="{ 'error-textarea': formErrors.address }" />
        </view>
        <view class="error-msg" v-if="formErrors.address">请输入详细地址</view>
      </view>

      <view class="bottom">
        <view class="tag">
          <view class="left">标签</view>
          <view class="right">
            <text v-for="tag in addressTags" :key="tag" class="tags" :class="{ 'active': selectedTag === tag }"
              @tap="handleSelectTag(tag)">
              {{ tag }}
            </text>
            <view class="tags plus"><u-icon size="22" name="plus" color="#999"></u-icon></view>
          </view>
        </view>
        <view class="default">
          <view class="left">
            <view class="set">设置默认地址</view>
            <view class="tips">提醒：每次下单会默认推荐该地址</view>
          </view>
          <view class="right">
            <switch color="#fa3534" :checked="defaultAddress" @change="handleSetDefault" />
          </view>
        </view>
      </view>

      <view class="button-group">
        <view class="save-btn" @tap="handleSaveAddress">
          {{ isEdit ? '保存修改' : '保存地址' }}
        </view>
        <view v-if="isEdit" class="delete-btn" @tap="handleDeleteAddress">
          删除地址
        </view>
      </view>
    </view>

    <city-select v-model="showRegionPicker" @city-change="handleCityChange"></city-select>
  </view>
</template>

<style lang="scss" scoped>
:v-deep(.line) {
  color: $u-light-color;
  font-size: 28rpx;
}

.wrap {
  background-color: #f5f5f5;
  min-height: 100vh;
  padding: 20rpx;
  box-sizing: border-box;

  .container {
    border-radius: 16rpx;
    overflow: hidden;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
  }

  .top {
    background-color: #ffffff;
    padding: 30rpx;

    .item {
      display: flex;
      font-size: 32rpx;
      line-height: 100rpx;
      align-items: center;
      border-bottom: solid 1rpx #eeeeee;
      position: relative;

      .left {
        width: 180rpx;
        font-weight: 500;
        color: #333;

        .required {
          color: #fa3534;
          margin-right: 4rpx;
        }
      }

      input {
        text-align: left;
        flex: 1;
        height: 100rpx;
        font-size: 30rpx;

        &.error-input {
          border-bottom: 1px solid #fa3534;
        }
      }

      u-icon {
        margin-left: 10rpx;
      }
    }

    .error-msg {
      color: #fa3534;
      font-size: 24rpx;
      padding-left: 180rpx;
      margin-top: -10rpx;
      margin-bottom: 10rpx;
    }

    .address {
      padding: 20rpx 0;
      align-items: flex-start;

      .left {
        padding-top: 20rpx;
      }

      textarea {
        flex: 1;
        height: 180rpx;
        background-color: #f9f9f9;
        line-height: 60rpx;
        margin: 20rpx 0;
        padding: 20rpx;
        border-radius: 12rpx;
        font-size: 30rpx;

        &.error-textarea {
          border: 1px solid #fa3534;
        }
      }
    }
  }

  .bottom {
    margin-top: 20rpx;
    padding: 30rpx;
    background-color: #ffffff;
    font-size: 28rpx;
    border-radius: 16rpx;

    .tag {
      display: flex;
      align-items: center;

      .left {
        width: 160rpx;
        font-weight: 500;
        color: #333;
      }

      .right {
        display: flex;
        flex-wrap: wrap;
        flex: 1;

        .tags {
          width: 150rpx;
          padding: 20rpx 10rpx;
          border: solid 2rpx #eeeeee;
          text-align: center;
          border-radius: 100rpx;
          margin: 0 20rpx 20rpx 0;
          display: flex;
          font-size: 28rpx;
          align-items: center;
          justify-content: center;
          color: #333;
          line-height: 1;
          transition: all 0.3s;

          &.active {
            background-color: #ffebec;
            color: #fa3534;
            border-color: #fa3534;
          }
        }

        .plus {
          background-color: #f5f5f5;
        }
      }
    }

    .default {
      margin-top: 30rpx;
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding-bottom: 20rpx;

      .left {
        .set {
          font-weight: 500;
          color: #333;
          font-size: 30rpx;
        }

        .tips {
          font-size: 24rpx;
          color: #999;
          margin-top: 10rpx;
        }
      }
    }
  }

  .button-group {
    display: flex;
    flex-direction: column;
    margin-top: 60rpx;

    .save-btn {
      background: linear-gradient(90deg, #ff4034, #fa3534);
      color: #fff;
      height: 90rpx;
      line-height: 90rpx;
      text-align: center;
      font-size: 32rpx;
      border-radius: 45rpx;
      font-weight: bold;
      box-shadow: 0 10rpx 20rpx rgba(250, 53, 52, 0.2);
      letter-spacing: 2rpx;
    }

    .delete-btn {
      margin-top: 30rpx;
      background: #ffffff;
      color: #fa3534;
      border: 1px solid #fa3534;
      height: 90rpx;
      line-height: 90rpx;
      text-align: center;
      font-size: 32rpx;
      border-radius: 45rpx;
      letter-spacing: 2rpx;
    }
  }
}
</style>
