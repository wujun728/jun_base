<template>
  <Input v-bind="$attrs" :value="state" :maxlength="7" @change="handleValueChange">
    <template #addonBefore>
      <Tag v-if="showTag" :color="state">标签</Tag>
    </template>
    <template #addonAfter>
      <ColorPicker
        :pureColor="state"
        v-model:gradientColor="gradientColor"
        format="hex"
        @pure-color-change="handleColorChange"
      />
    </template>
  </Input>
</template>

<script lang="ts">
  import { defineComponent, ref, watch } from 'vue';
  import { Input, Tag } from 'ant-design-vue';
  import { ColorPicker } from 'vue3-colorpicker';
  import 'vue3-colorpicker/style.css';
  import { useRuleFormItem } from '@/hooks/component/useFormItem';

  export default defineComponent({
    // eslint-disable-next-line vue/no-reserved-component-names
    components: { ColorPicker, Input, Tag },
    props: {
      value: {
        type: String,
        default: '',
      },
      showTag: {
        type: Boolean,
        default: true,
      },
    },
    emits: ['update:value', 'change'],
    setup(props, { emit }) {
      const gradientColor = ref(
        'linear-gradient(0deg, rgba(0, 0, 0, 1) 0%, rgba(0, 0, 0, 1) 100%)',
      );

      function handleColorChange(current: string) {
        // console.log('handleColorChange: ' + current);
        emit('update:value', current);
        emit('change', current);
      }

      function handleValueChange(e: any) {
        // console.log('handleValueChange: ' + e.target.value);
        const value = e.target.value;
        emit('update:value', value);
        emit('change', value);
      }

      const emitData = ref<any[]>([]);
      // Embedded in the form, just use the hook binding to perform form verification
      // 必须加这个才能在FormItem中使用
      const [state] = useRuleFormItem(props, 'value', 'change', emitData);

      watch(
        () => state.value,
        (v) => {
          emit('update:value', v);
        },
      );

      return {
        state,
        gradientColor,
        handleColorChange,
        handleValueChange,
      };
    },
  });
</script>
<style scoped></style>
