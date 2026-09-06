import { createApp } from 'vue';
import { createPinia } from 'pinia';
import App from './App.vue';
import router from './router';
import Antd from 'ant-design-vue';
import directive from './directive';
import 'ant-design-vue/dist/reset.css';
import ErrorHandler from './plugins/errorHandler';
import tablePlugin from '@/plugins/table';
// 导入 dayjs 配置
import './plugins/dayjs';
// 导入全局样式 (包含所有主题样式)
import './style/global.less';
// 导入主题服务
import { themeService } from '@/utils/theme';
// 应用挂载后初始化配置
import { initAllConfigs, watchConfigChanges } from '@/utils/configInit';
// 注册全局组件
import GlobalLoading from './components/common/GlobalLoading.vue';
import DictTag from './components/DictTag/index.vue';
// 导入工具函数

// 导入字典管理函数
import { useDict } from '@/composables/useDict.js';

const app = createApp(App);
const pinia = createPinia();

const bootstrap = async () => {
  app.use(pinia).use(router).use(Antd).use(directive).use(ErrorHandler).use(tablePlugin);

  // 注册全局组件
  app.component('GlobalLoading', GlobalLoading);
  app.component('DictTag', DictTag);

  // 添加全局属性
  app.config.globalProperties.useDict = useDict;

  await themeService.init({
    followSystem: true,
  });

  initAllConfigs();
  watchConfigChanges();

  app.mount('#app');
};

bootstrap().catch((error) => {
  console.error('应用启动失败:', error);
});
