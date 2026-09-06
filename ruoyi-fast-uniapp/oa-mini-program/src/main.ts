import 'uno.css';
import { createSSRApp } from 'vue';
import store from '@/store';
import App from './App.vue';
import uviewPlus from 'uview-plus'

export function createApp() {
  const app = createSSRApp(App).use(store);
  app.use(uviewPlus)
  return {
    app
    // Pinia
  };
}
