import auth from './auth';
import cache from './cache';
import modal from './modal';
import download from './download';
import tab from './tab';

export default {
  install(Vue) {
    Vue.prototype.$auth = auth;
    Vue.prototype.$cache = cache;
    Vue.prototype.$modal = modal;
    Vue.prototype.$download = download;
    Vue.prototype.$tab = tab;
  },
};
