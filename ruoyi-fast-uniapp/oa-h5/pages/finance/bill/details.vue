<template>
  <BaseContainer>
    <view class="nav-bar-wrapper">
      <DefaultNavBar color="#fff" backgroundColor="transparent" class="nav-bar" is-right :right-data="navRightIconList"
        @handleNarItem="handleNavBarRightIconClick" />
      <DropDown ref="navDropDownRef" :list-data="navDropDownData" @btn-click="handleDropDownClick" minWidth="172rpx" />
    </view>
    <view class="bill-details-body" v-if="cardCombineConfig.length">
      <view class="bill-details-card">
        <view class="bill-details-price" :class="isIncomeTypeBill ? 'income' : 'expend'">
          {{ billDetails.num }}
          <view class="bill-price-unit">元</view>
        </view>
        <view class="bill-details-cate text-center">
          {{ billDetails.cate ? billDetails.cate.name : '--' }}
        </view>
        <view class="bill-details-item" v-for="item of cardCombineConfig.slice(0, 3)" :key="item.label">
          <view class="bill-details-item-label">{{ item.label }}</view>
          <view class="bill-details-item-value">{{ item.value }}</view>
        </view>
      </view>


      <view class="bill-details-card">
        <view class="bill-details-item" v-for="item of cardCombineConfig.slice(3)" :key="item.label">
          <view class="bill-details-item-label">{{ item.label }}</view>
          <view class="bill-details-item-value">{{ item.value || "--" }}</view>
        </view>
        <view class="bill-details-item">
          <view class="bill-details-item-label">创建时间</view>
          <view class="bill-details-item-value">{{ billDetails.created_at }}</view>
        </view>
        <view class="bill-details-item scaleman-item">
          <view class="bill-details-item-label">业务员</view>
          <view class="bill-details-item-value">
            <image :src="scalesManInfo.avatar" v-if="scalesManInfo.avatar" class="bill-details-scaleman-avatar" />
            <text>
              {{ scalesManInfo.username }}
            </text>
          </view>
        </view>
        <view class="bill-details-item attachment-item">
          <view class="bill-details-item-label">付款凭证</view>
          <view class="bill-details-item-value">
            <template v-if="billFileList.length">
              <image :src="item.src" :key="item.id" @click="handlePreviewAttachment(index)"
                class="bill-details-attachment-img" v-for="(item, index) of billFileList" />
            </template>
            <text v-else>--</text>
          </view>
        </view>
      </view>
    </view>
  </BaseContainer>
</template>

<script setup lang="ts">
import { financeBillDetailsApi, financeBillDetailsDelApi } from "@/api/finance";
import BaseContainer from "@/components/BaseContainer/index.vue";
import DefaultNavBar from "@/components/defaultNavBar/index.vue";
import DropDown from "@/pages/forum/components/dropDown.vue";
import { BILL_DETAIL_FLUSH_EVENT } from "@/utils/constants";
import { lookPreview, showModal } from "@/utils/helper";

enum DropDownKeys {
  EDIT = 1,
  DEL = 2
}

interface DropDownItem {
  id: DropDownKeys,
  name: string,
  icon: string,
  handler: () => void
}

type BillDetailsProperty = keyof BillDetails;

const billId = ref<string>(null);
const billDetails = ref<BillDetails>();
const scalesManInfo = reactive({
  username: "--",
  avatar: ""
});
const billFileList = ref<BillFileInfo[]>([]);


const navRightIconList = [
  {
    type: 1,
    icon: "icon-gengduo1"
  }
];

const navDropDownRef = ref();

// 是否收入类型账目
const isIncomeTypeBill = computed(() => billDetails.value.types !== 0);

const detailsCardConfigTemplate = [
  {
    label: "收支时间",
    key: "edit_time",
  },
  {
    label: "收支方式",
    key: "pay_type",
  },
  {
    label: "备注信息",
    get: (detail: BillDetails) => {
      if (detail.mark) return detail.mark;
      return detail?.client_bill?.mark || "--";
    }
  },
  {
    label: "数据来源",
    get: (detail: BillDetails) => {
      return detail.link_id > 0 ? "合同账目" : "手动添加";
    }
  },
  {
    label: "客户名称",
    get: (detail: BillDetails) => {
      return detail.client ? detail.client.customer_name : "--";
    }
  },
  {
    label: "合同名称",
    get: (detail: BillDetails) => {
      return detail.contract ? detail.contract.title : "--";
    }
  },
  {
    label: "付款单号",
    get: (detail: BillDetails) => {
      return detail.client_bill ? detail.client_bill.bill_no : "--";
    }
  }
];

const cardCombineConfig = computed(() => {
  if (!billDetails.value) return [];

  return detailsCardConfigTemplate.map(({ key, label, get }) => {
    return {
      label,
      value: get ? get(billDetails.value) : billDetails.value[key as BillDetailsProperty]
    };
  });
});


const handleGoEditBillPage = () => {
  uni.navigateTo({
    url: `/pages/finance/bill/add?id=${billId.value}`
  });
}

const handleConfirmDelBill = async () => {
  try {
    await showModal("你确定要删除资金记录吗");
    // 在这里进行删除账户的相关操作

    await financeBillDetailsDelApi(billId.value);

    const pages = getCurrentPages();

    if (pages.length > 1) {
      uni.navigateBack();
      uni.$emit(BILL_DETAIL_FLUSH_EVENT);
    } else {
      uni.redirectTo({ url: "/pages/finance/bill/index" });
    }

  } catch { }
}

const navDropDownData: DropDownItem[] = [
  {
    id: DropDownKeys.DEL,
    name: "编辑",
    icon: "icon-danchuang-bianji",
    handler: handleGoEditBillPage
  },
  {
    id: DropDownKeys.EDIT,
    name: "删除",
    icon: "icon-shanchu1",
    handler: handleConfirmDelBill
  }
];

const handleNavBarRightIconClick = () => {
  navDropDownRef.value.openDropdown();
}

const handleDropDownClick = (item: DropDownItem) => {
  item?.handler();
}

const handlePreviewAttachment = (index: number) => {
  lookPreview(billFileList.value[index].src, "付款凭证", billFileList.value.map(i => i.src));
}

const handleScalesmanInfo = () => {
  let userInfo;
  if (billDetails.value.user) {
    userInfo = billDetails.value.user;
  } else if (billDetails.value.client && billDetails.value.client.card) {
    userInfo = billDetails.value.client.card;
  }
  if (!userInfo) return;
  const { name, avatar } = userInfo;
  name && (scalesManInfo.username = name || "--");
  avatar && (scalesManInfo.avatar = avatar);
}

const handleFileInfo = () => {
  if (billDetails.value.files.length) {
    billFileList.value = billDetails.value.files;
  } else if (billDetails.value.attachs.length) {
    billFileList.value = billDetails.value.attachs;
  }
};

onLoad((option: PageLoadOptions) => {
  billId.value = option.id;
});

onShow(async () => {
  const result = await financeBillDetailsApi(billId.value);
  billDetails.value = result.data;
  handleScalesmanInfo();
  handleFileInfo();
});

</script>

<style scoped lang="scss">
@import "@/static/css/din-condensed-font.scss";

.nav-bar-wrapper {
  padding-top: var(--status-bar-height);
  background-image: linear-gradient(90deg, #459FFF 0%, #388AEF 100%, #3384E7 100%);
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
}

.bill-details-body {
  padding: 20rpx;
  font-size: 28rpx;
  margin-top: calc(#{$uni-default-bar-height} + var(--status-bar-height));
}

.bill-details-card {
  background-color: #fff;
  border-radius: 12rpx;
  padding: 30rpx 24rpx;

  &+& {
    margin-top: 20rpx;
  }
}

.bill-details-price {
  font-family: DIN-Condensed-Bold;
  font-size: 64rpx;
  font-weight: bold;
  line-height: 76rpx;

  display: flex;
  align-items: baseline;
  justify-content: center;

  margin-top: 50rpx;

  &.income {
    color: #19BE6B;

    &::before {
      content: "+";
    }
  }

  &.expend {
    color: #FF9900;

    &::before {
      content: "-";
    }
  }

}

.bill-price-unit {
  margin-left: 6rpx;
  font-size: 32rpx;
}

.bill-details-cate {
  margin: {
    top: 4rpx;
    bottom: 60rpx;
  }

  color: $nui-text-color-two;
}

.bill-details-item {
  display: flex;
  align-items: baseline;

  &+& {
    margin-top: 24rpx;
  }

  &.scaleman-item {
    align-items: center;

    .bill-details-item-value {
      display: flex;
      align-items: center;
    }
  }

  &.attachment-item {
    align-items: flex-start;

    .bill-details-item-value {
      display: flex;
      flex-flow: row wrap;
      gap: 10rpx;
    }
  }
}

.bill-details-item-label {
  min-width: 112rpx;
  text-align: right;
  margin-right: 40rpx;
  color: $nui-text-color-four;
}

.bill-details-scaleman-avatar {
  width: 48rpx;
  height: 48rpx;
  margin-right: 12rpx;
  border-radius: 30rpx;
}

.bill-details-attachment-img {
  width: 92rpx;
  height: 92rpx;

  border-radius: 8rpx;
}
</style>
