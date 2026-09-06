<template>
  <div>
    <SectionTitle :title="$t('setting.box.title')" class="mt-10" />
    <div class="flex-cb mt-5 box-border rounded-lg bg-g-200 p-1">
      <div
        v-for="option in boxStyleOptions"
        :key="option.value"
        class="c-p h-8.5 w-[calc(50%-3px)] rounded-md text-center text-sm leading-8.5 transition-all duration-200 select-none"
        :class="
          isActive(option.type)
            ? 'bg-[var(--default-box-color)] text-g-800 dark:bg-g-300 dark:!text-white'
            : 'hover:bg-black/[0.04] hover:text-g-800 dark:hover:bg-black/20'
        "
        @click="boxStyleHandlers.setBoxMode(option.type)">
        {{ option.label }}
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { storeToRefs } from 'pinia'

import { useSettingStore } from '@/store/modules/setting'

import { useSettingsConfig } from '../composables/useSettingsConfig'
import { useSettingsHandlers } from '../composables/useSettingsHandlers'
import SectionTitle from './SectionTitle.vue'

const settingStore = useSettingStore()
const { boxBorderMode } = storeToRefs(settingStore)
const { boxStyleOptions } = useSettingsConfig()
const { boxStyleHandlers } = useSettingsHandlers()

// 判断当前选项是否激活
const isActive = (type: 'border-mode' | 'shadow-mode') => {
  return type === 'border-mode' ? boxBorderMode.value : !boxBorderMode.value
}
</script>
