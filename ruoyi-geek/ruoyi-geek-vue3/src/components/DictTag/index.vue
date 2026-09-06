<template>
  <div>
    <template v-for="(item, index) in options">
      <template v-if="isValueMatch(item.value)">
        <span v-if="item.elTagType == 'default' || item.elTagType == ''" :key="item.value" :index="index"
          :class="item.elTagClass">{{ item.label }}</span>
        <el-tag v-else :disable-transitions="true" :key="item.value + ''" :index="index" :type="item.elTagType"
          :class="item.elTagClass">{{ item.label }}</el-tag>
      </template>
    </template>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';

const props = withDefaults(defineProps<{
  separator: string;
  showValue: boolean
  options?: Array<{ label: string; value: string | number; elTagType?: string; elTagClass?: string }>;
  value?: string | number | Array<string | number> | null;
}>(), {
  separator: ',',
  showValue: true,
})

const values = computed(() => {
  if (props.value === null || typeof props.value === 'undefined' || props.value === '') return []
  if (typeof props.value === 'number' || typeof props.value === 'boolean') return [props.value]
  return Array.isArray(props.value) ? props.value.map(item => '' + item) : String(props.value).split(props.separator)
})

function isValueMatch(itemValue: string | number) {
  return values.value.some(val => val == itemValue);
}

</script>

<style scoped>
.el-tag+.el-tag {
  margin-left: 10px;
}
</style>