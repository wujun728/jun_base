<script lang="ts" setup>
import { getSchemas } from "@/annotation/Schema";
import { computed, ref } from "vue";
const props = defineProps<{
  target: new (...args: any[]) => any;
  selection?: boolean;
}>();
const schemas = computed(() => {
  return getSchemas(props.target).filter(item => item.components['table']);
});
const tableSchemas = computed(() => {
  return schemas.value.filter(item => item.components['table']);
});
</script>
<template>
  <el-table ref="invoiceRef" label-width="80px">
    <el-table-column type="selection" width="55" align="center" v-if="selection" />
    <el-table-column v-for="item in tableSchemas" :label="item.name" :prop="item.attr" :key="item.attr" align="center">
      <template #default="scope">
        <component :is="item.components['table']" :value="scope.row[item.attr]">
          {{ scope.row[item.attr] }}
        </component>
      </template>
    </el-table-column>
    <slot />
  </el-table>
</template>