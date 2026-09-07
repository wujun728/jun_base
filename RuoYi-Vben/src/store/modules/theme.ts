import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useThemeStore = defineStore(
  'app-theme',
  () => {
    const primaryColor = ref('#1677ff');

    function setPrimaryColor(color: string) {
      if (color === primaryColor.value) {
        return;
      }
      primaryColor.value = color;
    }

    return {
      primaryColor,
      setPrimaryColor,
    };
  },
  { persist: true },
);
