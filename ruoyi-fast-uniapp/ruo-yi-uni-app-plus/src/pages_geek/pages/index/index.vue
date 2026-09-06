<template>
  <uni-section class="mb-10" title="数值板" sub-title="statistic" type="line"></uni-section>
  <u-row gutter="0">
    <u-col span="6">
      <geek-statistic label="订单数量(个)" labelColor="#1f1f1f" :number="0" numberColor="red" />
    </u-col>
    <u-col span="6">
      <geek-statistic label="交易金额(元)" labelColor="#1f1f1f" :number="0" numberColor="red" />
    </u-col>
  </u-row>

  <uni-section class="mb-10" title="菜单" sub-title="menu" type="line"></uni-section>
  <u-row>
    <u-col :span="2.1" :offset="0.3" v-for="menu, index in menus" :key="index">
      <geek-menu v-bind=menu :size="60" @click="modal.msg(menu.label)" type="circle" />
    </u-col>
  </u-row>
  <u-row>
    <u-col :span="2.1" :offset="0.3" v-for="menu, index in menus" :key="index">
      <geek-menu :icon="menu.icon" :label="menu.label" :size="60" @click="modal.msg(menu.label)" type="rect" />
    </u-col>
  </u-row>

  <uni-section class="mb-10" title="商品列表" sub-title="commodity" type="line"></uni-section>
  <geek-commodity v-for="item, index in commodityList" :key="index" v-bind="item" type="line"
    @click="modal.msg(item.title)" />
  <geek-commodity v-for="item, index in commodityList" :key="index" v-bind="item" type="rect"
    @click="modal.msg(item.title)" />


  <uni-section class="mb-10" title="订单列表" sub-title="order" type="line"></uni-section>
  <geek-order v-for="item, index in orderList" :key="index" v-bind="item" @more="modal.msg('更多')"
    @again="modal.msg('再次购买')" @return="modal.msg('退换')" @sell="modal.msg('卖了换钱')"></geek-order>

  <uni-section class="mb-10" title="颜色选择器" sub-title="order" type="line"></uni-section>
  <geek-color-picker ref="gk"></geek-color-picker>
  <button @click="open()">打开颜色选择器</button>


  <uni-section class="mb-10" title="二维码" sub-title="order" type="line"></uni-section>
  <geek-qrcode cid="qrcode2" ref="qrcode2" val="二维码" :loadMake="true" />
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue';
import modal from '@/plugins/modal'
import { Commodity, CommodityOrder, Menu } from "@/components/geek-xd/types"

const gk = ref(null)
function open() {
  //@ts-ignore
  gk.value.open()
}

const menus:Array<Menu> = reactive([
  { icon: "/static/images/icon/rocket.png", label: '抢单' },
  { icon: "/static/images/icon/phone.png", label: '回访' },
  { icon: "/static/images/icon/message.png", label: '消息' },
  { icon: "/static/images/icon/dialogue.png", label: '公告' },
  { icon: "/static/images/icon/knowledge.png", label: '知识库' }
]);

const commodityList: Array<Commodity> = reactive([
  {
    img: '/static/images/banner/banner01.jpg',
    title: '商品1',
    subTitle: '商品1简介',
    price: 100,
  },
  {
    img: '/static/images/banner/banner02.jpg',
    title: '商品2',
    subTitle: '商品2简介',
    price: 300,
  },
  {
    img: '/static/images/banner/banner03.jpg',
    title: '商品3',
    subTitle: '商品3简介',
    price: 200,
  }
])

const orderList: Array<CommodityOrder> = [
  {
    shop: 'geek自营旗舰店',
    status: '完成',
    img: '/static/images/banner/banner01.jpg',
    title: '商品1',
    subTitle: '商品1简介',
    price: 100.32,
    num: 10
  },
  {
    shop: 'geek自营旗舰店',
    status: '已取消',
    img: '/static/images/banner/banner03.jpg',
    title: '商商商商商商商商商商商商商商商商商商商商商商商商商商商商商商商商商商品3',
    subTitle: '商品3简介',
    price: 2000.67,
    num: 10
  },
  {
    shop: 'geek自营旗舰店',
    status: '已取消',
    img: '/static/images/banner/banner03.jpg',
    title: '商商商商商商商商商商商商商商商商商商商商商商商商商商商商商商商商商商品3',
    subTitle: '商品3简介',
    price: 10.67,
    num: 10
  }
]


</script>

<style lang="scss" scoped></style>
