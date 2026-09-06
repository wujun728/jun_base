<template>
	<view class="index-content">
    <Navbar :hideBtn="true" title="首页" bgColor="#fff" :h5Show="false" :fixed="false"></Navbar>
		<view class="index-block">
      <view class="index-block-title">运行统计</view>
      <u-row gutter="16">
        <u-col span="4">
          <view class="item-tj item-tj-frist">
            <view style="padding: 16rpx; height: 100%; position: relative;">
              <view style="display: flex; color: white;">
                <u-icon name="photo" color="#fff"></u-icon>
                <text>在线人数</text>
              </view>
              <view style="position: absolute; bottom: 48rpx; right: 16rpx;">
                <u-count-to :startVal="0" :endVal="149" :duration="1500" color="#fff" separator=","></u-count-to>
              </view>
            </view>
          </view>
        </u-col>
        <u-col span="4">
          <view class="item-tj item-tj-second">
            <view style="padding: 16rpx; height: 100%; position: relative;">
              <view style="display: flex; color: white;">
                <u-icon name="photo" color="#fff"></u-icon>
                <text>今日新增</text>
              </view>
              <view style="position: absolute; bottom: 48rpx; right: 16rpx;">
                <u-count-to :startVal="1" :endVal="2349" :duration="1500" color="#fff" separator=","></u-count-to>
              </view>
            </view>
          </view>
        </u-col>
        <u-col span="4">
          <view class="item-tj item-tj-thrid">
            <view style="padding: 16rpx; height: 100%; position: relative;">
              <view style="display: flex; color: white;">
                <u-icon name="photo" color="#fff"></u-icon>
                <text>总人数</text>
              </view>
              <view style="position: absolute; bottom: 48rpx; right: 16rpx;">
                <u-count-to :startVal="1" :endVal="1249350" :duration="1500" color="#fff" separator=","></u-count-to>
              </view>
            </view>
          </view>
        </u-col>
      </u-row>
    </view>
    <view class="index-block">
      <view class="index-block-title">近7天访问量统计</view>
      <view>
        <qiun-data-charts type="line" canvasId="finance_a" :canvas2d="isCanvas2d" :resshow="delayload"
						:opts="{xAxis:{itemCount:12,disableGrid:true},yAxis:{disableGrid:true,data:[{disabled:true}]}}"
						:chartData="historyData" />
      </view>
      <view class="index-block-title">访问量统计</view>
      <view>
        <qiun-data-charts type="bar" canvasId="finance_b" :canvas2d="isCanvas2d" :resshow="delayload"
						:opts="{xAxis:{disabled: true,disableGrid:true},extra:{bar:{barBorderCircle:true,width:20}},legend:{show:false}}"
						:chartData="historyData" />
      </view>
    </view>
    <view class="index-block">
      <view class="index-block-title">数据总览</view>
      <view class="detail_list">
        <view v-for="(item,index) in detail_list" :key="index" class="detail_item">
          <view>
            <view class="font-middle">{{item.date}}</view>
            <view class="font-small">{{item.time}}</view>
          </view>
          <view class="icon"><li :class="['iconfont',item.type == 'income'?'icon-income':'icon-expend']"></li></view>
          <view class="right_content">
            <view class="money">{{item.type == 'income'?'+':'-'}}{{item.money}}</view>
            <view class="text-gray font-middle">{{item.desc}}</view>
          </view>
        </view>
      </view>
    </view>
	</view>
</template>

<script>
import Navbar from '@/components/navbar/Navbar'

let _now = new Date();
let now_time = {};
now_time.year = _now.getFullYear()
now_time.month = _now.getMonth() + 1
now_time.day = _now.getDay()

export default {
  components: {
    Navbar,
  },
  data () {
    return {
      isCanvas2d: true,
      delayload: false,
      historyData: {
        "categories": [
          "1月",
          "2月",
          "2月",
          "4月",
          "5月"
        ],
        "series": [
          {
            "name": "收入情况",
            "data": [1601,1840.5,1900,1760,1500.85],
            "type": "line",
            "style": "curve",
            "color": "#4ECDB6",
            "unit":""
          }
        ],
        "yAxis":[
          {"calibration":true,"position":"left","title":"单位/元","titleFontSize":12,"unit":"","tofix":0,"min":0,"disableGrid":true}
        ]
      },
      detail_list:[
        {date:now_time.month + "-01",time:"11:01","type":"extend",money:"10.00",desc:"银行卡转出"},
        {date:now_time.month + "-01",time:"13:45","type":"income",money:"18.00",desc:"银行卡收入"},
        {date:now_time.month + "-02",time:"06:21","type":"extend",money:"123.45",desc:"信用卡转出"},
        {date:now_time.month + "-03",time:"07:38","type":"income",money:"23.00",desc:"银行卡收入"},
        {date:now_time.month + "-08",time:"16:28","type":"extend",money:"23.56",desc:"信用卡转出"},
        {date:now_time.month + "-09",time:"15:25","type":"income",money:"850.12",desc:"银行卡收入"},
        {date:now_time.month + "-09",time:"18:52","type":"income",money:"1.88",desc:"银行卡收入"},
        {date:now_time.month + "-11",time:"21:12","type":"extend",money:"220.21",desc:"银行卡转出"},
        {date:now_time.month + "-12",time:"13:08","type":"income",money:"32.28",desc:"银行卡收入"},
        {date:now_time.month + "-12",time:"12:41","type":"extend",money:"122.12",desc:"信用卡转出"},
        {date:now_time.month + "-13",time:"17:21","type":"income",money:"10.00",desc:"银行卡收入"},
      ]
    }
  }
}
</script>

<style lang="scss">
.index-content {
  background-color: #f3f4f6;
  min-height: 100vh;
}
.index-block {
  padding: 40rpx;
  background-color: #fff;
}
.index-block-title {
  font-size: 40rpx;
  font-weight: bold;
  padding: 0 0 40rpx 0;
}
.item-tj {
  width: 100%;
  height: 160rpx;
  border-radius: 16rpx;
  
  &-frist {
    background-color: rgba($color: #2979ff, $alpha: 0.8);
    // background-image: url('/static/img/bg/qb.png');
  }

  &-second {
    background-color: rgba($color: #303133, $alpha: 0.8);
    // background-image: url('/static/img/bg/qb.png');
  }

  &-thrid {
    background-color: rgba($color: #19be6b, $alpha: 0.8);
    // background-image: url('/static/img/bg/qb.png');
  }
}
.detail_list{
  height: 700rpx;
  overflow: auto;
  color: #9E9E9E;
  .detail_item{
    display: flex;
    margin: 20rpx 0;
    align-items: center;
    .icon{
      width: 30%;
      text-align: center;
      li{
        font-size: 80rpx;
      }
    }
    .right_content{
      width: 50%;
      text-align: center;
    }
    .icon-income{
      color:#4AABF9;
    }
    .icon-expend{
      color: #E45521;
    }
    .money{
      color: #000;
    }
  }
}
</style>