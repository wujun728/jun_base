import { createI18n } from "vue-i18n";
import { useAppStoreHook } from "@/stores/app";
// 本地语言包
import enLocale from "./package/en";
import zhCnLocale from "./package/zh-cn";

const appStore = useAppStoreHook();

const messages = {
  "zh-cn": {
    ...zhCnLocale,
  },
  en: {
    ...enLocale,
  },
};

const i18n = createI18n({
  legacy: false,
  locale: appStore.language,
  messages,
  globalInjection: true,
});

// 全局注册 i18n
export function setupI18n(app) {
  app.use(i18n);
}

export default i18n;
