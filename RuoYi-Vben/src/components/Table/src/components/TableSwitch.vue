<template>
  <ASwitch
    v-bind="$attrs"
    :checked="modelValue"
    :checkedChildren="checkedText"
    :unCheckedChildren="unCheckedText"
    :checkedValue="checkedValue"
    :unCheckedValue="unCheckedValue"
    @change="onChange"
  />
</template>

<script lang="ts">
  import { Switch } from 'ant-design-vue';
  import { defineComponent } from 'vue';
  import { propTypes } from '@/utils/propTypes';
  import { isFunction } from 'lodash-es';

  export default defineComponent({
    name: 'TableSwitch',
    components: {
      ASwitch: Switch,
    },
    inheritAttrs: false,
    props: {
      modelValue: {
        type: [Boolean, String, Number],
        default: false,
      },
      checkedText: propTypes.string.def('启用'),
      unCheckedText: propTypes.string.def('禁用'),
      // 使用严格相等判断  类型要正确
      checkedValue: {
        type: [Boolean, String, Number],
        default: '0',
      },
      unCheckedValue: {
        type: [Boolean, String, Number],
        default: '1',
      },
      api: {
        type: Function,
        required: false,
      },
      reload: {
        type: Function,
        required: false,
      },
    },
    emits: ['update:modelValue'],
    setup(props, { emit }) {
      type CheckedType = boolean | string | number;
      async function onChange(checked: CheckedType) {
        const { checkedValue, unCheckedValue } = props;
        // 原本的状态
        const lastStatus = checked === checkedValue ? unCheckedValue : checkedValue;
        // 切换状态
        emit('update:modelValue', checked);
        const { api, reload } = props;
        try {
          isFunction(api) && (await api());
          isFunction(reload) && (await reload());
        } catch (e) {
          emit('update:modelValue', lastStatus);
        }
      }

      return {
        onChange,
      };
    },
  });
</script>

<style scoped></style>
