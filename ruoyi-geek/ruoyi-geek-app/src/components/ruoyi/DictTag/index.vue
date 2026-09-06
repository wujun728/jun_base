<template>
  <div>
    <template v-for="(item, index) in options">
      <template v-if="values.includes(item.value)">
        <span
          v-if="item.elTagType == 'default' || item.elTagType == ''"
          :key="item.value"
          :index="index"
          :class="item.elTagClass"
        >{{ item.label }}</span>
        <uni-tag
          v-else
          :key="item.value + ''"
          :index="index"
          :type="elTagType(item.elTagType)"
          :class="item.elTagClass"
          :text="item.label"
        />
      </template>
    </template>
  </div>
</template>

<script setup>
const props = defineProps({
  // 数据
  options: {
    type: Array,
    default: null,
  },
  // 当前的值
  value: [Number, String, Array],
})

const values = computed(() => {
  if (props.value !== null && typeof props.value !== 'undefined') {
    return Array.isArray(props.value) ? props.value : [String(props.value)];
  } else {
    return [];
  }
})

const elTagType = (tagType) =>{
  tagType === 'danger' ? 'error' : tagType
  tagType === 'info' ? 'default' : tagType
}

</script>

<style scoped>
.uni-tag + .uni-tag {
  margin-left: 10px;
}
</style>