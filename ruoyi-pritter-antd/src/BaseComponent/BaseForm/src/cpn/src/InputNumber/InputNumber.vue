<script setup>
const props = defineProps({
  item: { type: Object, required: true },
  allDisabled: { type: Boolean },
})
const emits = defineEmits(['keyUpEnter'])
const value = defineModel('value')
const inputRef = useTemplateRef('inputRef')

const inputConfig = computed(() => props.item.config ?? {})
const keyUpEnter = ($event, item) => emits('keyUpEnter', $event, item)
const getRef = () => inputRef.value

defineExpose({ getRef })
</script>

<template>
  <a-input-number
    ref="inputRef"
    v-model:value="value"
    class="base-form-input-number"
    :disabled="allDisabled"
    v-bind="inputConfig"
    :id="item.field"
    v-on="item.eventFunction || {}"
    @press-enter="keyUpEnter($event, item)"
  >
    <template v-for="slotName in item.slotNames" #[slotName]="slotData">
      <slot :name="slotName" :slotData="slotData" />
    </template>
  </a-input-number>
</template>

<style scoped lang="scss">
.base-form-input-number {
  width: 100%;
}
</style>
