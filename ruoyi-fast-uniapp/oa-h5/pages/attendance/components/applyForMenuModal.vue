<template>
  <view class="main">
    <view class="title">选择申请单</view>
    <view class="menu">
      <view class="item" v-for="item in listData" :key="item.type">
        <!-- <image @click="handleExamineItem(item)" :src="item.img" alt="" /> -->
        <view class="item-logo">
          <view @click="handleExamineItem(item)" class="iconfont" :class="getIconChange(item.icon)"
            :style="{color: item.color}"></view>
        </view>
        <view class="name">{{ item.name }}</view>
      </view>
    </view>
    <text class="iconfont icon-guanbi" @click="cancel"></text>
  </view>
</template>

<script setup>
  import {
    ref,
    reactive,
    toRefs
  } from "vue";
  let listData = ref([]);
  const props = defineProps({
    type: {
      type: Number,
      default: 0,
    },
    dateId: {
      type: Number,
      default: 0,
    },
    modal: {
      type: Boolean,
      default: false,
    },
    dateVal: {
      type: String,
      default: '',
    },
  });
  const {
    type,
    modal,
    dateVal,
    dateId
  } = toRefs(props);
  const emit = defineEmits(["cancel", "ok"]);
  const menu = reactive([{
      img: "/static/image/attendance/apply-0.png",
      path: "",
      name: "请假",
      type: 0,
    },
    {
      img: "/static/image/attendance/apply-1.png",
      path: "",
      name: "出差",
      type: 1,
    },
    {
      img: "/static/image/attendance/apply-2.png",
      path: "",
      name: "外出",
      type: 2,
    },
    {
      img: "/static/image/attendance/apply-3.png",
      path: "",
      name: "补卡",
      type: 3,
    },
    {
      img: "/static/image/attendance/apply-4.png",
      path: "",
      name: "加班",
      type: 4,
    },
  ]);
  const ok = () => {
    emit("ok");
  };
  const cancel = () => {
    emit("cancel");
  };
  onMounted(() => {
    getAttendanceApprove()
  })
  const getIconChange = (icon) => {
    if (icon.indexOf('-') > -1) {
      return icon
    } else {
      return `icon-${icon.slice(4)}`
    }
  }
  import {
    clickNavigateTo
  } from '@/utils/helper'
  import {
    attendanceApproveApi
  } from '@/api/attendance'
  const handleExamineItem = (item) => {
    clickNavigateTo(`/pages/users/examine/default?id=${item.id}&name=${item.name}&dateId=${dateId.value}`)
    emit('cancel');
  }
  const getAttendanceApprove = () => {
    attendanceApproveApi({
      date: dateVal.value
    }).then((res) => {
      listData.value = res.data
    }).catch((error) => {
      message.error(error.message)
    });
  }
</script>

<style lang="scss" scoped>
  .main {
    background-color: #fff;
    border-radius: 12rpx 12rpx 0 0;
    padding: 36rpx 52rpx 60rpx 52rpx;
    position: relative;

    .icon-guanbi {
      position: absolute;
      right: 30rpx;
      top: 36rpx;
      color: #c0c4cc;
      font-size: 36rpx;
    }

    .title {
      font-size: 30rpx;
      font-weight: 600;
      color: #303133;
      line-height: 30rpx;
      text-align: center;
      margin-bottom: 60rpx;
    }

    .menu {
      display: flex;
      justify-content: flex-start;
      flex-direction: row;
      flex-wrap: wrap;

      .item {
        //   width: 25%;
        text-align: center;
        margin-bottom: 48rpx;

        .item-logo {
          display: inline-block;
          width: 100rpx;
          height: 100rpx;
          margin-bottom: 20rpx;

          .iconfont {
            font-size: 90rpx;
            color: #fff;
          }
        }

        // image {
        //   width: 100rpx;
        //   height: 100rpx;
        //   margin-bottom: 20rpx;
        // }
        .name {
          font-size: 28rpx;
          font-weight: 400;
          color: #606266;
          line-height: 28rpx;
        }
      }
    }
  }
</style>