<template>
  <view>
    <view class="center">
      <view class="sign-in center" :class="typeClassName" @click="signIn">
        <view class="text mb10">{{ typeName }}</view>
        <view class="time">{{ hour }}:{{ minute }}:{{ second }}</view>
      </view>
    </view>
    <view class="suc-range" v-if="isEffectiveRange && !isWhite && isGroup && type != 0">您已进入考勤范围</view>
    <view class="suc-range" v-if="!isEffectiveRange && !isWhite && isGroup && type != 0">您当前不在考勤范围</view>
    <view class="suc-range" v-if="type == 0 && isExternal">未到打卡时间，稍候再试</view>
    <view class="address" v-if="address && !isWhite">
      <text class="iconfont icon-kaoqin-dingwei"></text>
      <text class="line2">{{ address }}</text>
      <text class="upload" @click="getLocation">刷新</text>
    </view>
  </view>
</template>

<script setup>
  import {
    ref,
    reactive
  } from "vue";
  // 实时时间
  let timer = ref(null);
  const date = ref(new Date());
  const emit = defineEmits(["signIn", "getLocation", "updateType"]);

  const props = defineProps({
    // 当前打卡状态
    type: {
      type: Number,
      default: 0,
    },
    // 是否是白名单人员
    isWhite: {
      type: Boolean,
      default: false,
    },
    // 当前地址信息
    address: {
      type: String,
      default: "",
    },
    onWork: {
      type: String,
      default: "",
    },
    // 更新时间
    clockUpdateTime: {
      type: Number,
      default: 0,
    },
    // 当前打卡进度
    recordLength: {
      type: Number,
      default: 0,
    },
    // 打卡记录status
    classesStatus: {
      type: Number,
      default: 0,
    },
    // 是否在考勤范围内
    isEffectiveRange: {
      type: Boolean,
      default: false,
    },
    // 是否开启外勤打卡
    isExternal: {
      type: Number,
      default: 0,
    },
    // 是否可以更新打卡
    updateStatus: {
      type: Number,
      default: 0,
    },
    // 是否有考勤组
    isGroup: {
      type: Number,
      default: 0,
    },
  });
  const {
    type,
    isWhite,
    isGroup,
    onWork,
    address,
    clockUpdateTime,
    recordLength,
    classesStatus,
    isEffectiveRange,
    isExternal,
    updateStatus,
  } = toRefs(props);
  const isUpdate = ref(false);
  const typeClassName = computed(() => {
    if (type.value == 0) {
      if (updateStatus.value) {
        isUpdate.value = true;
        return "normal";
      }
      return "err";
    }
    if (type.value == 1) {
      if (isExternal.value) {
        return "upload";
      }
      return "normal";
    }
    if (type.value == 2 || type.value == 3 || type.value == 5) {
      if (!isExternal.value && !isEffectiveRange.value && isGroup.value) {
        return "err";
      }
      return "suc";
    }
    if (type.value == 4 || type.value == 6) {
      if (updateStatus.value) {
        return "normal";
      }
      if (!isExternal.value && isGroup.value) {
        return "err";
      }
      return "suc";
    }
  });
  const typeName = computed(() => {
    // type 当前打开类型  0：不能打卡；1：可以打卡；2：迟到；3：严重迟到；4：早退；5：晚到缺卡；6：早退缺卡
    // recordLength 当前打卡阶段  0 第一阶段  1 第二阶段  2第三阶段  3第四阶段
    console.log('打卡类型：', type.value);
    console.log('更新打卡：', updateStatus.value);
    console.log('是否在打卡范围内：', isEffectiveRange.value);
    console.log('是否开启外勤：', isExternal.value);
    console.log('是否白名单：', isWhite.value);
    console.log('是否有考勤组：', isGroup.value);
    console.log('上班卡、下班呢：', recordLength.value);
    if (type.value == 0) {
      if (updateStatus.value) {
        return "更新打卡";
      }
      return "无法打卡";
    }

    if (type.value == 1) {
      if (updateStatus.value) {
        return "更新打卡";
      }
      if (recordLength.value == 1 || recordLength.value == 3) {
        if (isEffectiveRange.value) {
          return "上班打卡";
        } else if (isExternal.value) {
          return "外勤打卡";
        }
        return "上班打卡"
      } else if (recordLength.value == 2 || recordLength.value == 4) {
        if (isEffectiveRange.value) {
          return "下班打卡";
        } else if (isExternal.value) {
          return "外勤打卡";
        }
        return "下班打卡"
      }
    }
    if (type.value == 2 || type.value == 3 || type.value == 5) {
      if (isExternal.value && !isEffectiveRange.value) {
        return "迟到外勤";
      }
      if (!isExternal.value && !isEffectiveRange.value && !isWhite.value && isGroup.value) {
        return "无法打卡";
      }
      return "迟到打卡";
    }
    if (type.value == 4 || type.value == 6) {
      if (updateStatus.value) {
        return "更新打卡";
      }
      if (!isEffectiveRange.value && isGroup.value) {
        return "早退外勤";
      }
      if (!isExternal.value && isGroup.value) {
        return "无法打卡";
      }
      return "早退打卡";
    }
  });

  onMounted(() => {
    showTime();
  });
  onDeactivated(() => {
    clearInterval(timer.value);
  });
  const MINS = ref(0);
  const hour = computed(() => date.value.getHours());
  const minute = computed(() => {
    let sec = date.value.getMinutes();
    //   每分钟更新定位
    if (MINS.value !== sec) {
      emit("getLocation"); // 更新地址信息
      MINS.value = sec;
    } else {
      MINS.value = sec;
    }
    return sec < 10 ? `0${sec}` : `${sec}`;
  });
  const second = computed(() => {
    const nowTime = ref(Date.parse(new Date()) / 1000);
    if (clockUpdateTime.value <= nowTime.value) {
      emit("updateType");
    } else {}
    let sec = date.value.getSeconds();
    return sec < 10 ? `0${sec}` : `${sec}`;
  });
  let prev = 0;

  function updateTime() {
    const now = performance.now();
    const delay = now - prev;

    const ticks = Math.ceil(delay / 1000); // 需要更新的次数
    for (let i = 0; i < ticks; i++) {
      date.value = new Date();
    }

    prev = now;
    timer.value = setTimeout(updateTime, 1000 - (delay % 1000));
  }

  function showTime() {
    date.value = new Date();
    timer.value = setTimeout(showTime, 1000);
  }

  function signIn() {
    if (type.value == 0 && !isWhite.value) {
      return false;
    }
    // 不在考勤范围并且不是外勤打卡&&不是白名单&&没有考勤组就禁止打卡
    // if (!isEffectiveRange.value && isExternal.value == 0 && !isWhite.value && !isGroup.value) return
    if (updateStatus.value) {
      emit(
        "signIn",
        recordLength.value > 2 ? 1 : 0,
        onWork.value,
        updateStatus.value
      );
    } else {
      emit("signIn");
    }
  }

  function getLocation() {
    emit("getLocation");
  }
</script>

<style lang="scss" scoped>
  .center {
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    margin-bottom: 20rpx;

    .sign-in.suc {
      background: linear-gradient(139deg, #ffc43c 0%, #ff9615 100%);
      box-shadow: 0px 5px 16px 0px rgba(255, 153, 0, 0.5);
    }

    .sign-in.err {
      background: linear-gradient(139deg, #acb7bf 0%, #8c9dac 99%);
      box-shadow: 0rpx 10rpx 32rpx 0rpx rgba(122, 140, 162, 0.5);
    }

    .sign-in.upload {
      background: linear-gradient(139deg, #29e084 0%, #19be6b 100%);
      box-shadow: 0rpx 10rpx 32rpx 0rpx rgba(25, 190, 107, 0.502);
    }

    .sign-in.normal {
      background: linear-gradient(139deg, #47b5ff 0%, #0f86f5 100%);
      box-shadow: 0rpx 10rpx 32rpx 0rpx rgba(48, 139, 248, 0.5);
    }

    .sign-in {
      width: 272rpx;
      height: 272rpx;

      border-radius: 50%;
      flex-direction: column;

      .text {
        font-size: 40rpx;
        font-weight: 500;
        color: #ffffff;
        line-height: 40rpx;
      }

      .time {
        font-size: 32rpx;
        font-weight: 400;
        color: #ffffff;
        line-height: 32rpx;
        opacity: 0.7;
      }
    }
  }

  .address {
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 28rpx;
    font-weight: 400;
    color: #909399;
    line-height: 28rpx;
    max-width: 500rpx;
    margin: 20rpx auto 0 auto;

    .icon-kaoqin-dingwei {
      font-size: 28rpx;
      margin-right: 12rpx;
    }

    .upload {
      color: #308bf8;
      white-space: nowrap;
      margin-left: 12rpx;
    }
  }

  .suc-address {
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 24rpx;
    font-weight: 400;
    color: #909399;
    line-height: 24rpx;
    margin: 20rpx auto 0 auto;
    max-width: 70%;
    white-space: nowrap;

    .icon-kaoqin-dingwei {
      font-size: 24rpx;
      margin-right: 12rpx;
    }

    .upload {
      color: #308bf8;
      white-space: nowrap;
      margin-left: 12rpx;
    }
  }

  .suc-range {
    text-align: center;
    font-size: 28rpx;
    font-weight: 400;
    color: #606266;
    line-height: 28rpx;
    margin-top: 60rpx;
  }
</style>