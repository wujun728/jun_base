<template>
  <router-view v-slot="{ Component, route }">
    <transition name="fade" mode="out-in" appear>
      <div>
        <!-- <transition>中不能有多个root元素，<keep-alive>、<component>都不会创建真实的DOM元素，直接包裹在<transition>中的，所以需要将组件都包裹成单root节点 -->
        <component
            :is="Component"
            v-if="route.meta.ignoreCache"
            :key="route.fullPath"
        />
        <keep-alive v-else :include="cacheList">
          <component :is="Component" :key="route.fullPath"/>
        </keep-alive>
      </div>
    </transition>
  </router-view>
</template>

<script lang="ts" setup>
import {computed} from 'vue';
import {useTabBarStore} from '@/store';

const tabBarStore = useTabBarStore();

const cacheList = computed(() => tabBarStore.getCacheList);
</script>

<style scoped lang="less"></style>
