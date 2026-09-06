<script setup lang="ts" generic="T extends { id : any}">
const model = defineModel<T>()
const prop = withDefaults(defineProps<{
  list: Array<T>
}>(), {
  list: () => ([])
})
function selectContact(contact: T) {
  model.value = contact;
}
</script>
<template>
  <div class="contact-list">
    <div v-for="item in list" :key="item.id" :class="['contact-item', { active: item.id === model?.id }]"
      @click="selectContact(item)">
      <slot :item="item" />
    </div>
  </div>
</template>
<style lang="scss" scoped>
.contact-list {
  overflow-y: auto;
  flex: 1;
  padding: 12px 5px;


  .contact-item {
    display: flex;
    align-items: center;
    padding: 10px 16px;
    cursor: pointer;
    transition: background 0.2s;
    user-select: none;

    &.active {
      background: #f0f4fa;

      &:hover {
        background: #f0f4fa;
      }
    }

    &:hover {
      background: #f0f4fa8f;
    }
  }
}
</style>