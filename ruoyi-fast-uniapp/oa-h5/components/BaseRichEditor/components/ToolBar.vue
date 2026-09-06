<template>
  <view class="editor-tools-wrapper" :style="editorToolsBarStyles">
    <view class="editor-tools-bar">
      <view class="editor-tools-list">
        <view class="editor-tools-item" :class="{ active: styleToolIsVisible }" @click="handleToggleFontPanel">
          <view class="iconfont icon-wenan-01" />
        </view>
        <view class="editor-tools-item reversal" :class="{ active: alignToolVisible }" @click="handleToggleAlignPanel">
          <view class="iconfont icon-youduiqi" />
        </view>
        <view class="editor-tools-item" @click="handleOpenImgPicker">
          <view class="iconfont icon-zhaopian-01" />
        </view>
        <view class="editor-tools-item" @click="handleUndoAction">
          <view class="iconfont icon-houtui-01" />
        </view>
        <view class="editor-tools-item" @click="handleRedoAction">
          <view class="iconfont icon-qianjin-01" />
        </view>
      </view>
      <view class="editor-kb-btn" @click="$emit('save')">
        <!-- 替换键盘图标为保存对号 -->
        <!-- <view class="iconfont icon-jianpan" :class="{ 'panel-is-active': toolsPanelIsVisable || editorIsFoucs }" /> -->
        <image src="@/static/image/editor-toolbar-bingo.png" class="bingo-icon" />
      </view>
    </view>
    <view class="tools-panel-wrapper" :class="toolsPanelCssNames">
      <FontStyleToolPanel v-show="styleToolIsVisible" />
      <FontAlignToolPanel v-show="alignToolVisible" />
    </view>
  </view>
</template>

<script setup lang="ts">
import FontStyleToolPanel from './FontStyleToolPanel.vue';
import FontAlignToolPanel from './FontAlignToolPanel.vue';
import { useEditorFocus, useSoftKeyboardOffsetY } from '../hooks/common';
import { runCommand, sleep } from '../utils/helper';
import { uploadImage } from '@/utils/file';
import message from '@/utils/message';
import { StyleValue } from 'vue';
import { CommandType } from '../constant';

const [softKeyboardOffsetY] = useSoftKeyboardOffsetY();

/**
 * 当编辑器处于 focus 状态时点击工具栏，应该待软键盘收起后再呼起工具栏面板
 * 但是由于在工具栏点击事件中获取 foucs 状态时，编辑器已经失焦，导致无法正确判断编辑器 foucs 状态
 * 所以引入 delayFocus 变量，让工具栏点击事件先于 delayFocus 状态改变执行
 */
const delayFocusStatus = ref(false);

const updateDelayFocusStatus = (value: boolean) => {
  setTimeout(() => {
    delayFocusStatus.value = value;
  });
}

/**
 * 当聚焦到 editor 时，需要关闭已经打开的工具栏面板
 * 同时由于软键盘会弹起，所以需要关闭工具栏面板高度变化的动画，免得工具栏和软键盘闪烁冲突
 * @param isFoucs boolean
 */
const handleEditorFoucsChange = async (isFoucs: boolean) => {
  updateDelayFocusStatus(isFoucs);
  if (isFoucs) {
    disabledAnimation.value = true;
    await nextTick();
    handleCloseAllPanel();
  } else {
    disabledAnimation.value = false;
  }
}

const [editorIsFoucs] = useEditorFocus(handleEditorFoucsChange);

const editorToolsBarStyles = computed<StyleValue>(() => {
  let style: StyleValue = {};
  if (editorIsFoucs.value) {
    style.paddingBottom = 0;
  }
  if (softKeyboardOffsetY.value > 0) {
    style.bottom = softKeyboardOffsetY.value + "px";
  }
  return style;
});

const styleToolIsVisible = ref(false);
const alignToolVisible = ref(false);
const disabledAnimation = ref(false);

const toolsPanelCssNames = computed<string[]>(() => {
  return [
    styleToolIsVisible.value && "style-tools-active",
    alignToolVisible.value && "align-tools-active",
    disabledAnimation.value && "disabled-animation"
  ];
});

const toolsPanelIsVisable = computed<boolean>(() => {
  return styleToolIsVisible.value || alignToolVisible.value;
});

const handleCloseAllPanel = () => {
  if (styleToolIsVisible.value) {
    styleToolIsVisible.value = false;
  } else if (alignToolVisible.value) {
    alignToolVisible.value = false;
  }
}


const handleToggleFontPanel = async () => {
  // #ifndef APP-ANDROID
  if (delayFocusStatus.value) await sleep(150);
  // #endif

  if (!styleToolIsVisible.value && alignToolVisible.value) {
    alignToolVisible.value = false;
  }
  styleToolIsVisible.value = !styleToolIsVisible.value;
}

const handleToggleAlignPanel = async () => {
  // #ifndef APP-ANDROID
  if (delayFocusStatus.value) await sleep(150);
  // #endif
  if (!alignToolVisible.value && styleToolIsVisible.value) {
    styleToolIsVisible.value = false;
  }
  alignToolVisible.value = !alignToolVisible.value;
}

const handleOpenImgPicker = async () => {
  try {
    const result = await uploadImage("attach/imgs", {});
    runCommand("insertImage", {
      src: result.data.src,
      width: "50%"
    });
  } catch (err) {
    console.log(err);
    message.error(err, "none");
  }
}

const handleUndoAction = () => {
  runCommand(CommandType.UNDO);
}

const handleRedoAction = () => {
  runCommand(CommandType.REDO);
}

</script>

<style scoped lang="scss">
.flex-center {
  display: flex;
  align-items: center;
  justify-content: center;
}

.editor-tools-wrapper {
  position: fixed;
  left: 0;
  bottom: 0;
  width: 100%;
  padding-bottom: var(--bottom-area-height);
  background-color: #fff;
  transition: all ease .1s;
  transform: translateZ(0);
}

.editor-tools-bar {
  display: flex;
  height: 80rpx;

  .iconfont {
    font-size: 38rpx;
    color: #303133;
  }
}

.editor-kb-btn {
  @extend .flex-center;
  width: 86rpx;
  border-left: 2rpx solid #DBDBDB;

  .iconfont {
    font-size: 40rpx;
    transition: transform ease .1s;

    &.panel-is-active {
      transform: rotateZ(180deg);
    }
  }

  .bingo-icon {
    width: 48rpx;
    height: 48rpx;
  }
}

.editor-tools-list {
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex: 1;

  padding: {
    left: 64rpx;
    right: 40rpx;
  }
}

.editor-tools-item {
  width: 48rpx;
  height: 48rpx;
  @extend .flex-center;
  border-radius: 4rpx;

  &.reversal {
    transform: rotateZ(180deg);
  }

  &.active {
    background-color: rgba($color: #1098FF, $alpha: 0.1);

    .iconfont {
      color: #1098FF;
    }
  }
}

.tools-panel-wrapper {
  transition: all ease .2s;
  height: 0;

  &.disabled-animation {
    transition: none;
  }

  $style-tools-panel-height: calc(40rpx + 44rpx + 5 * 80rpx + 4 * 20rpx);
  $align-tools-panel-height: calc(40rpx + 44rpx + 2 * 80rpx + 1 * 20rpx);

  &.style-tools-active {
    height: $style-tools-panel-height;
  }

  &.align-tools-active {
    // height: $align-tools-panel-height; // 所有面板高度保持一致
    height: $style-tools-panel-height;
  }
}
</style>
