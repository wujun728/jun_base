<template>
  <BaseContainer>
    <view class="nav-bar-wrapper">
      <DefaultNavBar :defaultTitle="navBarTitle" color="#fff" backgroundColor="transparent" class="nav-bar" />
    </view>

    <view class="bill-add-body">
      <view class="bill-add-card">
        <view class="bill-card-title">基本信息</view>
        <view class="bill-card-item">
          <view class="bill-card-item-label">账目类型</view>
          <view class="bill-card-item-value">
            <picker mode="selector" :range="inComeAndExpendTypeConfig" range-key="name"
              @change="handleIncomeAndExpendTypeChange">
              <view class="picker-content" :class="getCssByValue(inComeAndExpendTypeText)">
                <text class="over-text">
                  {{ inComeAndExpendTypeText }}
                </text>
                <text class="iconfont icon-jinru-copy" />
              </view>
            </picker>
          </view>
        </view>

        <view class="bill-card-item">
          <view class="bill-card-item-label">账目分类</view>
          <view class="bill-card-item-value">
            <view class="picker-content" @click="handleShowBillCatePicker" :class="getCssByValue(billCateNames)">
              {{ billCateNames }}
              <view class="iconfont icon-jinru-copy" />
            </view>
          </view>
        </view>

        <view class="bill-card-item">
          <view class="bill-card-item-label">账目金额</view>
          <view class="bill-card-item-value">
            <input placeholder="请填写" class="input-content" placeholder-class="input-placeholder" type="digit"
              v-model="billDetailData.form.num" />
          </view>
        </view>

        <view class="bill-card-item">
          <view class="bill-card-item-label">支付方式</view>
          <view class="bill-card-item-value">
            <picker mode="selector" :range="payTypeConfig" range-key="name" @change="handlePayTypeChange">
              <view class="picker-content" :class="getCssByValue(payTypeText)">
                <text class="over-text">
                  {{ payTypeText }}
                </text>
                <text class="iconfont icon-jinru-copy" />
              </view>
            </picker>
          </view>
        </view>

        <view class="bill-card-item">
          <view class="bill-card-item-label">支付时间</view>
          <view class="bill-card-item-value">
            <uni-datetime-picker type="datetime" :end="maxDateRange" :border="false"
              v-model="billDetailData.form.editTime" :clear-icon="false">
              <view class="picker-content" :class="getCssByValue(billDetailData.form.editTime)">
                {{ billDetailData.form.editTime || "请选择" }}
                <text class="iconfont icon-jinru-copy" />
              </view>
            </uni-datetime-picker>
          </view>
        </view>
      </view>

      <view class="bill-add-card remark-card">
        <view class="bill-card-title">备注信息</view>
        <textarea v-model="billDetailData.form.mark" :maxlength="-1" class="bill-card-text-area"
          placeholder="请输入备注信息"></textarea>
      </view>

      <view class="bill-add-card">
        <view class="bill-card-title">账目附件</view>
        <view class="bill-card-subtitle">
          上传图片
          <text>(支持上传1张图片，支持jpg、jpeg、png)</text>
        </view>
        <view class="bill-card-file-list">
          <image class="bill-card-file-image" :src="billDetailData.fileSrc" v-if="billDetailData.fileSrc"
            @click="handleOpenImgPicker" />
          <view class="bill-card-file-item" v-else @click="handleOpenImgPicker">
            <view class="iconfont icon-paizhao" />
            上传凭证
          </view>
        </view>
      </view>
    </view>

    <BaTreePicker ref="billCateRef" :localdata="billCateList" @select-change="handleBillCateChange" selectShow
      :clearTree="false" :multiple="false" />

    <view class="bill-submit-btn-wrapper">
      <button @click="handleSaveBillDetails" class="bill-submit-btn" :loading="saveLoading"
        :disabled="saveLoading">提交</button>
    </view>
  </BaseContainer>
</template>

<script setup lang="ts">
  import { financeBillDetailsApi, financeBillDetailsAddApi, financeBillDetailsEditApi } from "@/api/finance";

  import BaTreePicker from "@/components/baTreePicker/index.vue";
  import BaseContainer from "@/components/BaseContainer/index.vue";
  import DefaultNavBar from "@/components/defaultNavBar/index.vue";

  import { BillInComeAndExpendTypes, useBillFilterIncomeAndExpend } from "./hooks/useBillFilterIncomeAndExpend";
  import { DEFAULT_PAY_TYPE_ALL, useFilterPayType } from "./hooks/useIndexBillListFilterPayType";
  import { useBillCate } from "./hooks/useBillCate";

  import { uploadImage } from "@/utils/file";
  import message from "@/utils/message";
  import { BILL_DETAIL_FLUSH_EVENT } from "@/utils/constants";

  const DEFAULT_SELECT_TEXT = "请选择";
  const maxDateRange = Date.now();
  const saveLoading = ref(false);
  const billCateRef = ref();
  const billId = ref<string>();
  const billDetailData = reactive({
    form: {
      editTime: "", // 变动时间
      mark: "", // 备注
      num: "", // 变动金额
      fileId: null // 附件ID
    },
    fileSrc: ""
  });

  const navBarTitle = computed(() => billId.value ? "修改账目信息" : "添加账目信息");


  const [
    incomeAndExpendType,
    inComeAndExpendTypeText,
    inComeAndExpendTypeConfig,
    handleIncomeAndExpendTypeChange
  ] = useBillFilterIncomeAndExpend(false, DEFAULT_SELECT_TEXT);

  const [
    billCateList,
    billCateIds,
    billCateNames,
    handleBillCateChange
  ] = useBillCate(false, DEFAULT_SELECT_TEXT, incomeAndExpendType);

  const [
    payType,
    payTypeText,
    payTypeConfig,
    handlePayTypeChange,
    handleUpdatePayType
  ] = useFilterPayType(false, DEFAULT_SELECT_TEXT);

  const getCssByValue = (value : string) => {
    return [
      value && value !== DEFAULT_SELECT_TEXT && "has-value"
    ];
  }

  const handleShowBillCatePicker = () => {
    billCateRef.value.show();
  }

  const handleOpenImgPicker = async () => {
    try {
      const result = await uploadImage("attach/imgs", {}, 1);
      const { id, src } = result.data;
      billDetailData.fileSrc = src;
      billDetailData.form.fileId = id;
    } catch (err) {
      message.error(err, "none");
    }
  }

  const handleSaveBillDetails = async () => {
    if (saveLoading.value) return;

    const { editTime, mark, num, fileId } = billDetailData.form;

    if (!incomeAndExpendType.value) {
      return message.error("请选择账目类型!", "none");
    }
    if (!billCateIds.value.length) {
      return message.error("请选择账目分类!", "none");
    }
    if (!num) {
      return message.error("请输入账目金额!", "none");
    }
    if (Number(num) < 0.01) {
      return message.error("请输入合法的账目金额!", "none");
    }
    if (payType.value === DEFAULT_PAY_TYPE_ALL) {
      return message.error("请选择支付方式!", "none");
    }
    if (!editTime) {
      return message.error("请选择支付时间!", "none");
    }

    saveLoading.value = true;
    uni.showLoading({ mask: true });

    const file_id : string[] = [];
    if (fileId) {
      file_id.push(fileId);
    }

    const data = {
      cate_id: Number(billCateIds.value[0]),
      types: Number(incomeAndExpendType.value),
      edit_time: editTime,
      mark,
      num: Number(num),
      type_id: Number(payType.value),
      file_id
    };

    try {
      if (billId.value) {
        await financeBillDetailsEditApi(billId.value, data);
        setTimeout(() => {
          uni.navigateBack();
        }, 800);
      } else {
        const result = await financeBillDetailsAddApi(data);
        setTimeout(() => {
          uni.redirectTo({
            url: `/pages/finance/bill/details?id=${result.data.id}`
          });
        }, 800);
      }

      uni.hideLoading();
      message.success("保存成功", "none");
      uni.$emit(BILL_DETAIL_FLUSH_EVENT);
    } catch (err) {
      uni.hideLoading();
      message.error(err.message, "none");
    }
    saveLoading.value = false;
  }


  onLoad(async (option : PageLoadOptions) => {

    if (option.id) {
      billId.value = option.id;
      // 获取账目详情

      const { data } = await financeBillDetailsApi(billId.value);
      const {
        cate,
        cate_id,
        types,
        edit_time,
        mark,
        num,
        type_id,
        files
      } = data as BillDetails;

      billCateIds.value = [cate_id + ""];
      billCateNames.value = cate?.name || "";

      incomeAndExpendType.value = (types + "") as unknown as BillInComeAndExpendTypes;
      const typesConfig = inComeAndExpendTypeConfig.find(i => i.value == incomeAndExpendType.value);
      inComeAndExpendTypeText.value = typesConfig.name;

      billDetailData.form.editTime = edit_time;
      billDetailData.form.mark = mark;
      billDetailData.form.num = num;
      handleUpdatePayType(type_id);

      if (files.length) {
        billDetailData.form.fileId = files[0].id;
        billDetailData.fileSrc = files[0].src;
      }
    }
  });
</script>

<style scoped lang="scss">
  $submit-btn-wrapper-height: calc(126rpx + var(--bottom-area-height));

  .nav-bar-wrapper {
    padding-top: var(--status-bar-height);
    background-image: linear-gradient(90deg, #459FFF 0%, #388AEF 100%, #3384E7 100%);
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    z-index: 1;
  }

  .bill-add-body {
    padding: 20rpx;
    font-size: 30rpx;
    margin-top: calc(#{$uni-default-bar-height} + var(--status-bar-height));
    margin-bottom: $submit-btn-wrapper-height;
  }

  .bill-add-card {
    padding: 30rpx 24rpx 8rpx;
    background-color: #fff;
    border-radius: 12rpx;

    &+& {
      margin-top: 20rpx;
    }
  }

  .bill-card-title {
    font-size: 32rpx;
    line-height: 44rpx;
    font-weight: 500;
    margin-bottom: 4rpx;
  }

  .bill-card-item {
    height: 90rpx;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .bill-card-item-label {
    display: flex;
    align-items: center;
    margin-right: 20rpx;

    &::after {
      content: "*";
      height: 20rpx;
      color: #FF2529;
      margin-left: 8rpx;
    }
  }

  .bill-card-item-value {
    flex: 1;
    min-width: 0;
    display: flex;
    justify-content: flex-end;
    color: $uni-text-color-five;

    picker {
      overflow: hidden;
    }

    .picker-content {
      display: flex;
      align-items: center;
      justify-content: flex-end;

      .iconfont {
        font-size: 24rpx;
        margin-left: 12rpx;
        margin-top: 2px;
      }
    }

    :deep(.input-placeholder) {
      font-size: 30rpx;
      color: #C0C4CC;
    }

    .has-value,
    .input-content {
      color: $uni-text-color;
    }

    .input-content {
      flex: 1;
      text-align: right;
    }
  }

  .remark-card {
    display: flex;
    flex-flow: column nowrap;
    height: 354rpx;
  }

  .bill-card-text-area {
    width: 100%;
    margin: 16rpx 0 22rpx;
    flex: 1;
    font-size: 28rpx;
  }

  .bill-card-subtitle {
    margin-top: 26rpx;
    margin-bottom: 30rpx;

    text {
      font-size: 20rpx;
      color: #999999;
    }
  }

  .bill-card-file-list {
    margin-bottom: 22rpx;
  }

  .bill-card-file-item {
    width: 140rpx;
    height: 140rpx;
    border-radius: 8rpx;
    border: 2rpx solid #DDDDDD;
    font-size: 24rpx;
    color: #999999;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-flow: column nowrap;

    .iconfont {
      font-size: 40rpx;
      margin-bottom: 18rpx;
    }
  }

  .bill-card-file-image {
    width: 140rpx;
    height: 140rpx;
    border-radius: 8rpx;
  }

  .bill-submit-btn-wrapper {
    position: fixed;
    left: 0;
    bottom: 0;
    right: 0;
    padding: 20rpx;
    height: $submit-btn-wrapper-height;
    background-color: #fff;
    box-shadow: inset 0rpx 2rpx 0rpx 0rpx rgba(0, 0, 0, 0.06);
  }

  .bill-submit-btn {
    width: 100%;
    height: 86rpx;
    background: $uni-color-primary;
    border-radius: 12rpx;
    color: #fff;
    font-size: 30rpx;
    display: flex;
    align-items: center;
    justify-content: center;
  }
</style>