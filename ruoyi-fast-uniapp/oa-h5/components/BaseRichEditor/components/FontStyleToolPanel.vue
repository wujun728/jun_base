<template>
  <view class="editor-tools-panel">
    <view class="font-tool-panel-row style-tool" @click="handleSetFontStyle">
      <view v-for="item of fontStyleConfig" :key="item.style" :data-style="item.style" class="iconfont"
        :class="item.classes"></view>
    </view>
    <view class="font-tool-panel-row title-tool" @click="handleSetTextLevel">
      <view :data-level="item.level" v-for="item of fontTextLevelConfig" :class="item.classes">{{ item.text }}</view>
    </view>
    <view class="font-tool-panel-row size-tool" @click="handleSetFontSize">
      <view class="tool-name">字号</view>
      <view class="tool-inner-wrap">
        <view v-for="item of fontSizeList" :data-size="item.size" :key="item.size"
          :style="{ fontSize: `${item.realSize}px` }" :class="{ active: currentFontSize === item.size }">{{ item.size
          }}</view>
      </view>
    </view>
    <view class="font-tool-panel-row color-tool" @click="handleSetColor">
      <view class="tool-name">文字颜色</view>
      <view class="tool-inner-wrap">
        <view class="color-item" :class="{ active: currentTextColor === color }" v-for="color of colorList"
          :data-color="color" :key="color" :style="{ backgroundColor: color }">
        </view>
      </view>
    </view>
    <view class="font-tool-panel-row color-tool" @click="handleSetHighlightColor">
      <view class="tool-name">高亮颜色</view>
      <view class="tool-inner-wrap">
        <view class="color-item hightlight-color" :class="{ active: currentHightlightColor === hlColor }"
          v-for="hlColor of hightlightColorList" :data-hl-color="hlColor" :key="hlColor"
          :style="{ backgroundColor: hlColor }">
        </view>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { colorList, hightlightColorList } from "../config";
import { useFontStyleTool } from "../hooks/useFontStyleTool";
import { useFontTextLevelTool } from "../hooks/useFontTextLevelTool";
import { useFontSizeTool } from "../hooks/useFontSizeTool";
import { useFontColorTool } from "../hooks/useFontColorTool";
import { CommandType } from "../constant";

const [fontStyleConfig, handleSetFontStyle] = useFontStyleTool();

const [
  fontTextLevelConfig,
  handleSetTextLevel
] = useFontTextLevelTool();

const [
  currentFontSize,
  fontSizeList,
  handleSetFontSize
] = useFontSizeTool();

const [currentTextColor, handleSetColor] = useFontColorTool("color", CommandType.FONT_COLOR);

const [currentHightlightColor, handleSetHighlightColor] = useFontColorTool("hlColor", CommandType.HIGHLIGHT_COLOR);


</script>

<style scoped lang="scss">
@import "../styles/toolbar.scss";

.active-bg {
  border-radius: 4rpx;
  background: rgba(16, 152, 255, 0.1);
}

.size-tool,
.color-tool {
  font-size: 36rpx;
  color: #606366;

  padding: {
    left: 30rpx;
    right: 36rpx;
  }
}


.style-tool,
.title-tool {
  padding: {
    left: 58rpx;
    right: 56rpx;
  }

  view {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 60rpx;
    height: 60rpx;

    font-size: 54rpx;
    color: #606266;

    border-radius: 4rpx;

    &.active {
      @extend .active-bg;
    }
  }
}


.title-tool {
  color: #303133;
  font-weight: 500;

  view[data-level=HEADER1] {
    font-size: 40rpx;
  }

  view[data-level=HEADER2] {
    font-size: 36rpx;
  }

  view[data-level=HEADER3] {
    color: #7A7D82;
    font-size: 30rpx;
  }

  view[data-level=PARAGRAPH] {
    color: #7A7D82;
    font-size: 28rpx;
  }
}

.tool-inner-wrap {
  display: flex;
  flex: 1;
  justify-content: space-between;
  align-items: center;
}

.tool-name {
  font-size: 22rpx;
  color: #606266;
  min-width: 88rpx;
  text-align: center;
  margin-right: 30rpx;
}

.size-tool {
  .tool-inner-wrap {
    view {
      width: 60rpx;
      height: 60rpx;
      display: flex;
      align-items: center;
      justify-content: center;

      &.active {
        @extend .active-bg;
      }
    }
  }
}

@function calcHasBorderSize($width, $borderSize: 4rpx) {
  @return $width +$borderSize * 2;
}

.color-item {
  $size: calcHasBorderSize(36rpx);
  width: $size;
  height: $size;
  border: 4rpx solid transparent;
  border-radius: 50%;
  background-clip: padding-box;

  &.hightlight-color {
    $size: calcHasBorderSize(38rpx);
    width: $size;
    height: $size;
  }

  &.active {
    border-color: #fff;
    box-shadow: 0 0 4px 2px rgba(0, 0, 0, .1);
  }
}
</style>
