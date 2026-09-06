<template>
  <div>
    <template v-for="(item, index) in options">
      <template v-if="values.includes(item.dictValue)">
        <span
            v-if="item.listClass === 'default' || item.listClass === ''"
            :key="item.dictValue"
            :index="index"
            :style="item.listClass ? 'color: ' + state.listClassOptions[item.listClass].color : ''"
        >{{ item.dictLabel }}</span
        >
        <a-tag
            v-else
            :key="item.dictValue"
            :index="index"
            :color="item.listClass === 'primary' ? 'blue' : state.listClassOptions[item.listClass].color"
            :class="item.cssClass"
        >
          {{ item.dictLabel }}
        </a-tag>
      </template>
    </template>
  </div>
</template>

<script lang="ts" setup>
import {computed, reactive} from "vue";

const props = defineProps({
  options: {
    type: Array,
    default: null,
  },
  value: {
    type: [Number, String, Array],
    default: undefined,
  },
});

const state = reactive({
  // 数据标签回显样式
  listClassOptions: {
    default: {
      value: "default",
      color: "default",
      label: "默认"
    },
    primary: {
      value: "primary",
      color: "blue",
      label: "主要"
    },
    success: {
      value: "success",
      color: "green",
      label: "成功"
    },
    info: {
      value: "info",
      color: "gray",
      label: "信息"
    },
    warning: {
      value: "warning",
      color: "orangered",
      label: "警告"
    },
    danger: {
      value: "danger",
      color: "red",
      label: "危险"
    },
  },
});

const values = computed(() => {
  const {value} = props;
  return Array.isArray(value) ? value : [String(value)];
});

</script>

<style scoped lang="less">

.el-tag + .el-tag {
  margin-left: 10px;
}

</style>
