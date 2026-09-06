<script lang="ts" setup>
const prop = withDefaults(defineProps<{
  contact: {
    avatar?: string;
    name?: string;
    email?: string;
    online?: boolean;
    lastMsg?: string;
  }
}>(), {
  contact: () => ({})
})
</script>
<template>
  <el-avatar :src="contact.avatar" size="small" />
  <div class="contact-info">
    <div class="contact-name">{{ contact.name }}</div>
    <div class="contact-email">{{ contact.email }}</div>
  </div>
  <div class="contact-status">
    <span v-if="contact.online" class="online-dot"></span>
    <span class="last-msg">{{ contact.lastMsg }}</span>
  </div>
</template>
<style lang="scss" scoped>
.contact-info {
  flex: 1;
  margin-left: 10px;

  .contact-name {
    font-size: 14px;
    font-weight: 500;
  }

  .contact-email {
    font-size: 12px;
    color: #aaa;
  }
}

.contact-status {
  display: flex;
  flex-direction: column;
  align-items: flex-end;

  .online-dot {
    width: 8px;
    height: 8px;
    background: #67c23a;
    border-radius: 50%;
    margin-bottom: 4px;
    display: inline-block;
  }

  .last-msg {
    font-size: 11px;
    color: #bbb;
  }
}
</style>