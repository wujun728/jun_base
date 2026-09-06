<!-- 数据列表 -->
<template>
  <view class="list-table-wrapper" :style="listTableWrapperStyleVariable">
    <scroll-view class="list-header" scroll-x @scroll="handleHeadScroll" :scroll-left="headScrollLeft">
      <view class="list-row">
        <view class="list-cell" v-for="item of orderedAliasForFieldsList" :key="item">
          <view class="fix-flex-text-ellipsis">
            <view class="over-text"> {{ item }}</view>
          </view>
        </view>
      </view>
    </scroll-view>
    <view class="list-body">
      <view class="list-body-inner">
        <scroll-view scroll-x scroll-y class="list-body-scroll" @scroll="handleBodyScroll"
          :scroll-left="bodyScrollLeft">
          <view class="list-row" v-for="(itemValueList, index) of transListByFieldOrder" :key="index">
            <view class="list-cell" v-for="(val, valIndex) of itemValueList" :key="valIndex">
              <view class="fix-flex-text-ellipsis">
                <view class="over-text"> {{ val }}</view>
              </view>
            </view>
          </view>
        </scroll-view>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { useChartListData, useScrollViewScroll } from './utils';
import type { StyleValue } from 'vue';


// 包含两个滚动组件，表头和表体
type ScrollPart = "head" | "body";

const headScrollLeft = ref(0);
const bodyScrollLeft = ref(0);

const scrollPart = ref<ScrollPart>();

// 生成组件滚动回调处理程序
const createScrollAfterCallback = (key: ScrollPart) => {
  return () => {
    if (!scrollPart.value) {
      scrollPart.value = key;
      syncScrollState();
    } else if (scrollPart.value === key) {
      // 同步滚动状态
      syncScrollState();
    } else {
      // 滚动部分不是自己，却触发了滚动事件，说明滚动事件来自于另一个滚动组件 set scrollLeft 引发的滚动
      scrollPart.value = undefined;
    }
  }
}

const [headScrollDetail, handleHeadScroll] = useScrollViewScroll(createScrollAfterCallback("head"));

const [bodyScrollDetail, handleBodyScroll] = useScrollViewScroll(createScrollAfterCallback("body"));


function syncScrollState() {
  switch (scrollPart.value) {
    case "body":
      headScrollLeft.value = bodyScrollDetail.scrollLeft;
      break;
    case "head":
      bodyScrollLeft.value = headScrollDetail.scrollLeft;
      break;
  }
}

const props = defineProps<{
  config: ListWidgetDesignData
}>();

// 按顺序排列的字段标签列表
const orderedLabelForFieldsList = computed(() => {
  return props.config.options.setDimensional.showFields.map(i => i.prop);
});

const [baseListData] = useChartListData<ListDataResponse>(props.config);


const listTableWrapperStyleVariable = computed<StyleValue>(() => {
  return {
    "--field-count": orderedLabelForFieldsList.value.length
  }
});

// 按顺序排列的字段别名列表
const orderedAliasForFieldsList = computed(() => {
  return props.config.options.setDimensional.showFields.map(i => i.alias);
});

const transListByFieldOrder = computed(() => {
  if (!baseListData.value || !baseListData.value.list) return [];
  return baseListData.value.list.map(item => {
    return orderedLabelForFieldsList.value.reduce((itemFieldValueList: any[], fieldKey) => {
      itemFieldValueList.push(item[fieldKey]);
      return itemFieldValueList;
    }, []);
  });
});

</script>

<style scoped lang="scss">

scroll-view {
  ::-webkit-scrollbar {
    display: none;
  }
}

// 屏幕横向默认能放置多少个单元格
$screen-row-cell-count: 4;

.list-table-wrapper {

  --screen-field-num: min(#{$screen-row-cell-count}, var(--field-count));
  --cell-width: calc((750rpx - 20rpx * 2 - 24rpx * 2) / var(--screen-field-num));

  flex: 1;
  font-size: 24rpx;
  color: #333;
  display: flex;
  flex-flow: column nowrap;

  padding: {
    top: 40rpx;
    left: 24rpx;
    right: 24rpx;
  }

  :deep(.uni-scroll-view) {
    overscroll-behavior: none;
  }
}

.list-row {
  height: 74rpx;
  display: flex;
  flex-flow: row nowrap;
}

.list-header {
  color: $uni-text-color-grey;
  white-space: nowrap;

  .list-row {
    height: 34rpx;
  }
}

.fix-flex-text-ellipsis {
  min-width: 0;
}


.list-cell {
  flex: 0 0 var(--cell-width);
  display: flex;
  align-items: center;

  @extend .fix-flex-text-ellipsis;
}

.list-body {
  // flex: 1;
  height: 740rpx;
  position: relative;

  .list-row:not(:last-child) {
    .list-cell {
      border-bottom: 2rpx solid $uni-bg-color-hover;
    }
  }
}

.list-body-inner {
  position: absolute;
  inset: 0;
}

.list-body-scroll {
  height: 100%;
}
</style>
