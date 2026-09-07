<template>
  <div :class="prefixCls">
    <template v-for="color in colorList || []" :key="color">
      <span
        @click="handleClick(color)"
        :class="[
          `${prefixCls}__item`,
          {
            [`${prefixCls}__item--active`]: def === color,
          },
        ]"
        :style="{ background: color }"
      >
        <CheckOutlined />
      </span>
    </template>
  </div>
  <div class="flex items-center justify-between font-size-14px">
    <span>自定义颜色: </span>
    <ColorPicker
      :pureColor="def"
      v-model:gradientColor="gradientColor"
      format="hex"
      @pure-color-change="handleClick"
    />
  </div>
</template>
<script lang="ts" setup>
  import { ref, type PropType } from 'vue';
  import { CheckOutlined } from '@ant-design/icons-vue';

  import { useDesign } from '@/hooks/web/useDesign';

  import { baseHandler } from '../handler';
  import { HandlerEnum } from '../enum';
  import { ColorPicker } from 'vue3-colorpicker';
  import 'vue3-colorpicker/style.css';

  defineOptions({ name: 'ThemeColorPicker' });

  const props = defineProps({
    colorList: {
      type: Array as PropType<string[]>,
      default: () => [],
    },
    event: {
      type: Number as PropType<HandlerEnum>,
    },
    def: {
      type: String,
    },
  });

  const { prefixCls } = useDesign('setting-theme-picker');

  function handleClick(color: string) {
    props.event && baseHandler(props.event, color);
  }

  const gradientColor = ref('linear-gradient(0deg, rgba(0, 0, 0, 1) 0%, rgba(0, 0, 0, 1) 100%)');
</script>
<style lang="less">
  @prefix-cls: ~'@{namespace}-setting-theme-picker';

  .@{prefix-cls} {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-around;
    margin: 16px 0;

    &__item {
      display: flex; // 勾选图标居中
      align-items: center;
      justify-content: center;
      width: 20px;
      height: 20px;
      border: 1px solid #ddd;
      border-radius: 2px;
      cursor: pointer;

      svg {
        display: none;
      }

      &--active {
        border: 1px solid lighten(#ccc, 10%);

        svg {
          display: inline-block;
          margin: 0 0 3px 3px;
          fill: @white !important;
          font-size: 12px;
        }
      }
    }
  }
  // 颜色选择器边框
  .vc-color-wrap {
    border: 1px solid #ccc;
    border-radius: 4px;
  }
</style>
