<template>
  <view class="work-container">
    <!-- 轮播图 -->
    <uni-swiper-dot class="uni-swiper-dot-box" :info="data" :current="current" field="content">
      <swiper class="swiper-box">
        <swiper-item v-for="(item, index) in data" :key="index">
          <view class="swiper-item" @click="clickBannerItem(item)">
            <image :src="item.image" mode="aspectFill" :draggable="false" />
          </view>
        </swiper-item>
      </swiper>
    </uni-swiper-dot>
    <!-- 宫格组件 -->
    <uni-section title="应用中心" type="line"></uni-section>
    <view class="grid-body">
      <uni-list :border="true">
        <uni-list-chat v-for="item in EquipList" :title="item.eqName" :avatar="item.eqMapUrl" :note="item.eqMemo" link  @click="handleEquip(item)"></uni-list-chat>

      </uni-list>
    </view>
	</view>
</template>

<script  setup>

import {ref} from "vue";
import modal from "@/plugins/modal";
import {equipList} from "@/api/xcj/equip";


// 租户列表
const EquipList = ref({});
const current=ref(0);

const data= ref([{
    image: '/static/images/banner/banner01.jpg'
  },
  {
    image: '/static/images/banner/banner02.jpg'
  },
  {
    image: '/static/images/banner/banner03.jpg'
  }
]);

function changeGrid(e) {
  modal.showToast({
    title: '模块建设中',
    mask: false,
    icon:'loading',
    duration: 1000
  });
}
function  getEquipment(){
   equipList().then(res => {
     EquipList.value = res.data;
     // console.log(EquipList.value)
   })
}
function handleEquip(item) {
  // console.log(item);
  uni.navigateTo({
    url: '/pages/common/detail?id=' + item.id,
  });
}
getEquipment();
</script>

<style>
page {
  display: flex;
  flex-direction: column;
  box-sizing: border-box;
  background-color: #fff;
  min-height: 100%;
  height: auto;
}

view {
  font-size: 14px;
  line-height: inherit;
}

.text {
  text-align: center;
  font-size: 26rpx;
  margin-top: 10rpx;
}

.grid-item-box {
  flex: 1;
  /* #ifndef APP-NVUE */
  display: flex;
  /* #endif */
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 15px 0;
}

.uni-margin-wrap {
  width: 690rpx;
  width: 100%;
;
}
.swiper {
  height: 300rpx;
}

.swiper-box {
  height: 150px;
}

.swiper-item {
  /* #ifndef APP-NVUE */
  display: flex;
  /* #endif */
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: #fff;
  height: 300rpx;
  line-height: 300rpx;
}

@media screen and (min-width: 500px) {
  .uni-swiper-dot-box {
    width: 400px;
    /* #ifndef APP-NVUE */
    margin: 0 auto;
    /* #endif */
    margin-top: 8px;
  }

  .image {
    width: 100%;
  }
}
</style>
