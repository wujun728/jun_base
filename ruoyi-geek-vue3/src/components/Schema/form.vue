<script lang="ts" setup>
import { getSchemas } from "@/annotation/Schema";
import { computed, ref } from "vue";
const props = defineProps<{
  target: new (...args: any[]) => any;
  schema?: string
}>();
const form = defineModel<any>()
const schemas = computed(() => {
  return getSchemas(props.target).filter(item => item.components[props.schema ?? 'form']);
});
const formSchemas = computed(() => {
  return schemas.value.filter(item => item.components[props.schema ?? 'form']);
});
</script>
<template>
  <el-form :model="form" label-width="80px">
    <el-form-item v-for="item in formSchemas" :label="item.name" :prop="item.attr" :key="item.attr">
      <component :is="item.components['form']" v-model="form[item.attr]" />
    </el-form-item>
    <slot />
  </el-form>
</template>