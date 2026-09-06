<template>
  <view class="drop-down" @click="closeDropdown"
    :style="dropDownWrapperStyles">
    <view class="modal-ang"></view>
    <view class="drop-down-list">
      <view class="drop-down-list-item" v-for="(item, index) in listData" :key="'meus' + index"
        @click="dropDownItem(item)">
        <text class="iconfont" :class="item.icon"></text>
        <text> {{ item.table_name ? '新建' : '' }}{{ item.name || item.table_name }} </text>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
  import type { StyleValue } from 'vue';

  const moreButton : Ref<boolean> = ref(false)
  // 关闭弹出
  const closeDropdown = () => {
    moreButton.value = false
  }
  // 打开弹出
  const openDropdown = () => {
    moreButton.value = true
  }

  const emit = defineEmits(['btnClick'])
  const dropDownItem = (item: PropType<any>) : void => {
    emit('btnClick', item)
  }

  defineExpose({ openDropdown, closeDropdown })
  const props = withDefaults(
    defineProps<{
      listData : any[]
      fixRight? : string,
      minWidth?: string
    }>(),
    {
      listData: <any>[],
      fixRight: <any>'36rpx',
      minWidth: () => ""
    }
  )
  const { listData, fixRight, minWidth } = toRefs(props)

  const dropDownWrapperStyles = computed<StyleValue>(() => {
    const styles: StyleValue =  {
      display: moreButton.value ? 'block' : 'none',
      '--right': fixRight.value
    };

    if (minWidth.value) {
      styles['--min-width'] = minWidth.value;
    }

    return styles;
  });
</script>

<style scoped lang="scss">
  .drop-down {
    width: 100vw;
    height: 100vh;
    position: absolute;
    left: 0;
    top: 0;
    z-index: 111;
    display: none;

    .drop-down-list {
      font-size: 28rpx;
      font-weight: 400;
      color: $nui-text-color-two;
      line-height: 28rpx;
      position: fixed;
      top: calc(44px + var(--status-bar-height));
      right: 20rpx;
      box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.16);
      border-radius: 12rpx;
      padding: 32rpx 30rpx;
      min-width: var(--min-width, 240rpx);
      background-color: #fff;
      border: 1px solid $uni-line-style-color-three;

      .drop-down-list-item {
        margin-bottom: 44rpx;

        &:last-of-type {
          margin-bottom: 0;
        }

        .iconfont {
          font-size: 32rpx;
          color: $nui-text-color-four;
          margin-right: 14rpx;
        }
      }
    }

    .modal-ang {
      width: 0px;
      height: 0px;
      position: fixed;
      top: calc(44px + var(--status-bar-height) - 6px);
      right: var(--right);
      border-color: transparent;
      border-style: solid;
      border-width: 6px;
      filter: drop-shadow(0 2px 12px rgba(0, 0, 0, 0.16));
      border-top-width: 0;
      border-bottom-color: $uni-line-style-color-three;
      z-index: 2;

      &::after {
        content: '';
        position: absolute;
        display: block;
        width: 0;
        height: 0;
        border-color: transparent;
        border-style: solid;
        border-width: 6px;
        top: 1px;
        margin-left: -6px;
        border-top-width: 0;
        border-bottom-color: #fff;
      }
    }
  }
</style>