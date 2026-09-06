<template>
  <view class="editor-tools-panel">
    <view class="font-tool-panel-row align-tools" @click="handleSetAlignType">
      <view class="iconfont" :class="item.classes" :data-align="item.type" v-for="item of alignTypeList"
        :key="item.type" />
    </view>

    <view class="font-tool-panel-row list-tools" @click="handleSetListType">
      <view class="iconfont" :class="item.classes" :data-list="item.type" v-for="item of listTypeList"
        :key="item.type" />
    </view>
  </view>
</template>

<script setup lang="ts">
import { CommandType } from "../constant";
import { useAlignTool } from "../hooks/useAlignTool";

enum ALIGN_TYPE {
  LEFT = "left",
  CENTER = "center",
  RIGHT = "right",
  JUSTIFY = "justify"
};

enum LIST_TYPE {
  BULLET = "bullet",
  ORDERED = "ordered",
  CHECK = "check"
};

const [currentAlignType, handleSetAlignType] = useAlignTool<ALIGN_TYPE>("align", CommandType.ALIGN);
const [currentListType, handleSetListType] = useAlignTool<LIST_TYPE>("list", CommandType.LIST);


// 当前值与选中值相等时返回 active css名称
const generateActiveCSSByEqualValueFunc = <T>(baseValue: T) => {
  return (diffValue: T) => {
    return baseValue === diffValue ? "active" : null;
  }
}

const alignTypeList = computed(() => {

  const func = generateActiveCSSByEqualValueFunc<ALIGN_TYPE>(currentAlignType.value);

  return [
    {
      type: ALIGN_TYPE.LEFT,
      classes: [
        "icon-youduiqi",
        func(ALIGN_TYPE.LEFT),
        "rotate180"
      ]
    },
    {
      type: ALIGN_TYPE.CENTER,
      classes: [
        "icon-juzhong",
        func(ALIGN_TYPE.CENTER)
      ]
    },
    {
      type: ALIGN_TYPE.RIGHT,
      classes: [
        "icon-youduiqi",
        func(ALIGN_TYPE.RIGHT)
      ]
    },
    {
      type: ALIGN_TYPE.JUSTIFY,
      classes: [
        "icon-liangbianduiqi",
        func(ALIGN_TYPE.JUSTIFY)
      ]
    }
  ];
});

const listTypeList = computed(() => {
  const func = generateActiveCSSByEqualValueFunc<LIST_TYPE>(currentListType.value);
  return [
    {
      type: LIST_TYPE.BULLET,
      classes: [
        "icon-wuxuliebiao",
        func(LIST_TYPE.BULLET)
      ]
    },
    {
      type: LIST_TYPE.ORDERED,
      classes: [
        "icon-youxuliebiao",
        func(LIST_TYPE.ORDERED)
      ]
    },
    {
      type: LIST_TYPE.CHECK,
      classes: [
        "icon-renwuliebiao",
        func(LIST_TYPE.CHECK)
      ]
    }
  ];
});

</script>

<style scoped lang="scss">
@import "../styles/toolbar.scss";

.font-tool-panel-row {
  view {
    width: 60rpx;
    height: 60rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #606266;
    font-size: 54rpx;

    &.rotate180 {
      transform: rotateZ(180deg);
    }

    &.active {
      background-color: rgba(16, 152, 255, 0.1);
      border-radius: 4rpx 4rpx 4rpx 4rpx;
    }
  }
}

.align-tools {
  padding: {
    left: 58rpx;
    right: 56rpx;
  }
}

.list-tools {
  padding: {
    left: 128rpx;
    right: 126rpx;
  }
}
</style>
