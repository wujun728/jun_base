<template>
  <div>
    <SectionTitle :title="$t('setting.color.title')" class="mt-10" />
    <div class="-mr-4">
      <div class="flex flex-wrap">
        <div
          v-for="color in configOptions.mainColors"
          :key="color"
          class="mr-4 mb-2.5 flex size-[23px] cursor-pointer items-center justify-center rounded-full transition-all duration-200 hover:opacity-85"
          :style="{ background: `${color} !important` }"
          @click="colorHandlers.selectColor(color)">
          <ArtSvgIcon
            icon="ri:check-fill"
            class="text-base !text-white"
            v-show="color === systemThemeColor" />
        </div>
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
const { systemThemeColor } = storeToRefs(settingStore)
const { configOptions } = useSettingsConfig()
const { colorHandlers } = useSettingsHandlers()
</script>
