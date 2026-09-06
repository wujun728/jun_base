<template>
  <a-tag
      v-if="tagText"
      :class="tagClass"
      :color="tagColor"
  >
    {{ tagText }}
  </a-tag>
  <span v-else>{{ props.value }}</span>
</template>

<script setup>
import {computed} from 'vue';

const props = defineProps({
  options: {
    type: Array,
    default: () => []
  },
  value: {
    type: [String, Number],
    default: ''
  },
  // 自定义颜色
  color: {
    type: String,
    default: ''
  },
  // 自定义类名
  className: {
    type: String,
    default: ''
  }
});

// 计算标签文本
const tagText = computed(() => {
  if (!props.options || !Array.isArray(props.options)) {
    return props.value;
  }

  // 兼容两种数据结构：
  // 1. BearJiaUtil 格式：{ value, label }
  // 2. 标准格式：{ dictValue, dictLabel }
  const option = props.options.find(item => {
    const dictValue = item.dictValue || item.value;
    return dictValue === String(props.value);
  });

  if (option) {
    return option.dictLabel || option.label;
  }

  return props.value;
});

// 计算标签颜色
const tagColor = computed(() => {
  if (props.color) {
    return props.color;
  }

  if (!props.options || !Array.isArray(props.options)) {
    return 'blue';
  }

  // 根据字典配置的 listClass 映射颜色
  const option = props.options.find(item => {
    const dictValue = item.dictValue || item.value;
    return dictValue === String(props.value);
  });

  if (option && option.listClass) {
    // listClass 到颜色的映射
    const colorMap = {
      'default': '',
      'primary': 'blue',
      'success': 'green',
      'info': 'cyan',
      'warning': 'orange',
      'danger': 'red',
      'error': 'red',
      // 直接颜色映射
      'processing': 'blue',
      'red': 'red',
      'orange': 'orange',
      'green': 'green',
      'cyan': 'cyan',
      'blue': 'blue',
      'purple': 'purple',
      'magenta': 'magenta',
      'volcano': 'volcano',
      'geekblue': 'geekblue',
      'lime': 'lime',
      'gold': 'gold',
    };

    return colorMap[option.listClass] || option.listClass || 'blue';
  }

  // 默认颜色映射（兼容旧版本）
  const value = String(props.value);
  switch (value) {
    case '0':
      return 'green'; // 正常/启用
    case '1':
      return 'red';   // 禁用/停用
    case '2':
      return 'orange'; // 待审核
    default:
      return 'blue';
  }
});

// 计算标签类名
const tagClass = computed(() => {
  const classes = [];

  if (props.className) {
    classes.push(props.className);
  }

  if (!props.options || !Array.isArray(props.options)) {
    return classes.join(' ');
  }

  const option = props.options.find(item => {
    const dictValue = item.dictValue || item.value;
    return dictValue === String(props.value);
  });

  if (option && option.cssClass) {
    classes.push(option.cssClass);
  }

  return classes.join(' ');
});
</script>

<style scoped>
/* 可以添加自定义样式 */
</style>
