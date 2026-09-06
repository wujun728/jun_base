import {App} from 'vue';
import permission from './permission';
import highlight from './highlight';

export default {
  install(Vue: App) {
    Vue.directive('permission', permission);
    Vue.directive('highlight', highlight);
  },
};
