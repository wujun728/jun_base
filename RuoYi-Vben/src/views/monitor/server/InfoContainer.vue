<template>
  <CollapseContainer :loading="loading" :canExpan="canExpan">
    <template #title>
      <span class="title"> <Icon :icon="icon" />&nbsp;{{ title }}</span>
    </template>
    <Description @register="register" :data="data" />
  </CollapseContainer>
</template>

<script setup lang="ts">
  import { CollapseContainer } from '@/components/Container/index';
  import Icon from '@/components/Icon/Icon.vue';
  import { Description } from '@/components/Description/index';

  defineOptions({ name: 'InfoContainer' });

  type PropsType = {
    title: string;
    icon: string;
    loading: boolean;
    register?: any;
    data?: any;
    canExpan?: boolean;
  };

  withDefaults(defineProps<PropsType>(), {
    title: '',
    icon: '',
    loading: false,
    register: () => {},
    data: () => {},
    canExpan: true,
  });
</script>

<style lang="less" scoped>
  .title {
    align-items: center;
  }
</style>
