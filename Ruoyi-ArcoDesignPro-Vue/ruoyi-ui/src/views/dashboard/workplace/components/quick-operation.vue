<template>
  <a-card
      class="general-card"
      :title="'快捷操作'"
      :header-style="{ paddingBottom: '0' }"
      :body-style="{ padding: '24px 20px 0 20px' }"
  >
    <template #extra>
      <a-link>{{ '管理' }}</a-link>
    </template>
    <a-row :gutter="8">
      <a-col v-for="link in links" :key="link.text" :span="8" class="wrapper">
        <div class="icon">
          <component :is="link.icon"/>
        </div>
        <a-typography-paragraph class="text">
          {{ link.text }}
        </a-typography-paragraph>
      </a-col>
    </a-row>
    <a-divider class="split-line" style="margin: 0"/>
  </a-card>
</template>

<script lang="ts" setup>
const links = [
  {text: '内容管理', icon: 'icon-file'},
  {text: '内容分析', icon: 'icon-storage'},
  {text: '高级管理', icon: 'icon-settings'},
  {text: '线上推广', icon: 'icon-mobile'},
  {text: '高级管理', icon: 'icon-fire'},
];
</script>

<style scoped lang="less"></style>
