<template>
  <ConfigProvider :locale="getAntdLocale" :theme="themeConfig">
    <AppProvider>
      <RouterView />
    </AppProvider>
  </ConfigProvider>
</template>

<script lang="ts" setup>
  import { AppProvider } from '@/components/Application';
  import { useTitle } from '@/hooks/web/useTitle';
  import { useLocale } from '@/locales/useLocale';
  import { ConfigProvider, theme } from 'ant-design-vue';

  import { useDarkModeTheme } from '@/hooks/setting/useDarkModeTheme';
  import 'dayjs/locale/zh-cn';
  import { computed, toValue } from 'vue';
  import { useThemeStore } from './store/modules/theme';
  import { storeToRefs } from 'pinia';

  // support Multi-language
  const { getAntdLocale } = useLocale();

  const { isDark, darkTheme } = useDarkModeTheme();
  const themeStore = useThemeStore();
  const { primaryColor } = storeToRefs(themeStore);

  const themeConfig = computed(() =>
    Object.assign(
      {
        token: {
          colorPrimary: toValue(primaryColor),
          colorSuccess: '#55D187',
          colorWarning: '#EFBD47',
          colorError: '#ED6F6F',
          colorInfo: toValue(primaryColor),
        },
      },
      isDark.value ? darkTheme : {},
    ),
  );
  // Listening to page changes and dynamically changing site titles
  useTitle();

  const { token } = theme.useToken();
</script>

<style lang="less">
  @import '@/design/index.less';

  @colorPrimary: v-bind('token.colorPrimary');

  .ant-btn-default:hover {
    border-color: @colorPrimary;
    background-color: #fff;
    color: @colorPrimary;
  }

  html[data-theme='dark'] {
    .ant-btn {
      &:not(
          .ant-btn-link,
          .is-disabled,
          .ant-btn-primary,
          .ant-btn-success,
          .ant-btn-warning,
          .ant-btn-error,
          .ant-btn-dangerous
        ) {
        &:hover {
          border-color: @colorPrimary;
          color: @colorPrimary;
        }
      }
    }
  }
</style>
