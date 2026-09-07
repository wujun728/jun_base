<template>
  <Tag v-if="color" :color="color" :class="cssClass">{{ label }}</Tag>
  <div v-if="!color" :class="cssClass">{{ label }}</div>
</template>

<script setup lang="ts">
  import { Tag } from 'ant-design-vue';
  import { computed } from 'vue';
  import { DictData } from '@/api/system/dict/dictData.model';
  import { tagTypes } from './data';

  defineOptions({ name: 'DictTag' });

  type DictTagProps = {
    dicts: DictData[]; // dict数组
    value: string | number; // value
  };
  const props = withDefaults(defineProps<DictTagProps>(), {
    dicts: undefined,
  });

  const color = computed<string>(() => {
    const current = props.dicts.filter((item) => item.dictValue == props.value)[0];
    const listClass = current?.listClass;
    // 是否为默认的颜色
    const isDefault = Reflect.has(tagTypes, listClass);
    // 判断是默认还是自定义颜色
    if (isDefault) {
      // 这里做了antd - element-plus的兼容
      return tagTypes[listClass].color;
    }
    return listClass;
  });

  const cssClass = computed<string>(() => {
    const current = props.dicts.filter((item) => item.dictValue == props.value)[0];
    return current?.cssClass ?? '';
  });

  const label = computed<string | number>(() => {
    const current = props.dicts.filter((item) => item.dictValue == props.value)[0];
    return current?.dictLabel ?? 'unknown';
  });
</script>

<style scoped></style>
