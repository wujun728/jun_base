
import Vue from 'vue';
import '@babel/polyfill';
import '@/styles/index.scss';
// import '@/assets/styles/ruoyi-index.scss'; // 文件不存在，已注释
// import 'babel-polyfill'
import Cookies from 'js-cookie';
import 'normalize.css/normalize.css'; // a modern alternative to CSS resets
import Element from 'element-ui';
import './theme/element-variables.scss';

import '@/theme/index.scss'; // global css
import '@/assets/styles/ruoyi.scss'; // ruoyi css
import '@/assets/fonts/font.css'; // font css
import '@/assets/iconfont/iconfont';
import '@/assets/iconfont/iconfont.css';
// import '@/assets/iconfont/iconfont copy.css';
import '@/assets/iconfont/iconfont-weapp-icon.css';
import VueAwesomeSwiper from 'vue-awesome-swiper';
import 'swiper/dist/css/swiper.css';
import 'vue-ydui/dist/ydui.base.css';
import Viewer from 'v-viewer';
import 'viewerjs/dist/viewer.css';
import { download } from '@/utils/request';
import { parseTime, resetForm, addDateRange, selectDictLabel, selectDictLabels, handleTree } from '@/utils/ruoyi';
import { getDicts } from '@/api/system/dict/data';
import { getConfigKey } from '@/api/system/config';
import Pagination from '@/components/Pagination';
import RightToolbar from '@/components/RightToolbar';
import DictData from '@/components/DictData';
import DictTag from '@/components/DictTag';
// 懒加载
import VueLazyload from 'vue-lazyload';

Vue.config.devtools = true;
import App from './App';
import store from './store';
import router from './router';
import base from '@/components/base/index'; // 公共组件
import UploadFile from '@/components/Upload/uploadFile.vue';
import Editor from '@/components/Editor';
import TimeSelect from '@/components/TimeSelect';
import dialog from '@/libs/dialog';
import scroll from '@/libs/loading';
import schema from 'async-validator';
import Debounce from './libs/debounce.js'; //防抖自定义指令
import util from '@/utils/utils';
import timeOptions from '@/libs/timeOptions';
import { loadScriptQueue } from '@/components/FormGenerator/utils/loadScript';
import './icons'; // icon
import './permission'; // permission control
import './utils/error-log'; // error integralLog
import * as filters from './filters'; // global filters
import { parseQuery } from '@/utils';
import * as constants from '@/utils/constants.js';
import SettingMer from '@/utils/settingMer';
import plugins from './plugins';
import directive from './directive'; //directive

Vue.use(Element, {
  size: Cookies.get('size') || 'small', // set element-ui default size
});

Vue.use(VueLazyload, {
  preLoad: 1.3,
  error: require('./assets/imgs/no.png'),
  loading: require('./assets/imgs/moren.jpg'),
  attempt: 1,
  listenEvents: ['scroll', 'wheel', 'mousewheel', 'resize', 'animationend', 'transitionend', 'touchmove'],
});
Vue.prototype.bus = new Vue();
Vue.use(base);
Vue.use(VueAwesomeSwiper);
Vue.use(Viewer, {
  defaultOptions: {
    zIndex: 9999,
  },
});
Vue.use(plugins);
Vue.use(directive);

Vue.component('uploadFile', UploadFile);
Vue.component('timeSelect', TimeSelect);
Vue.prototype.$dialog = dialog;
Vue.prototype.$scroll = scroll;
Vue.prototype.$util = util;
Vue.prototype.$constants = constants;
Vue.prototype.$timeOptions = timeOptions;
Vue.prototype.$validator = function (rule) {
  return new schema(rule);
};
Vue.prototype.handleTree = handleTree;
Vue.prototype.parseTime = parseTime;
Vue.prototype.resetForm = resetForm;
Vue.prototype.addDateRange = addDateRange;
Vue.prototype.selectDictLabel = selectDictLabel;
Vue.prototype.selectDictLabels = selectDictLabels;
Vue.prototype.getDicts = getDicts;
Vue.prototype.getConfigKey = getConfigKey;
Vue.prototype.download = download;

Vue.component('Pagination', Pagination);
Vue.component('RightToolbar', RightToolbar);
Vue.component('DictTag', DictTag);
Vue.component('Editor', Editor);

DictData.install();

let cookieName = 'VCONSOLE';
let query = parseQuery();
let urlSpread = query['spread'];
let vconsole = query[cookieName.toLowerCase()];
let md5Scrm = 'b14d1e9baeced9bb7525ab19ee35f2d2'; //SCRM MD5 加密开启vconsole模式
let md5UnScrm = '3dca2162c4e101b7656793a1af20295c'; //UN_SCRM MD5 加密关闭vconsole模式

if (vconsole !== undefined) {
  if (vconsole === md5UnScrm && Cookies.has(cookieName)) Cookies.remove(cookieName);
} else vconsole = Cookies.get(cookieName);

if (vconsole !== undefined && vconsole === md5Scrm) {
  Cookies.set(cookieName, md5Scrm, 3600);
  const module = () => import('vconsole');
  module().then((Module) => {
    new Module.default();
  });
}
// 自定义实现String 类型的replaceAll方法
String.prototype.replaceAll = function (s1, s2) {
  return this.replace(new RegExp(s1, 'gm'), s2);
};
// Vue.prototype.$modalCoupon = modalCoupon
/**
 * If you don't want to use mock-server
 * you want to use MockJs for mock api
 * you can execute: mockXHR()
 *
 * Currently MockJs will be used in the production environment,
 * please remove it before going online ! ! !
 */
// if (process.env.NODE_ENV === 'production') {
//   const { mockXHR } = require('../mock')
//   mockXHR()
// }

// register global utility filters
Object.keys(filters).forEach((key) => {
  Vue.filter(key, filters[key]);
});

Vue.config.productionTip = false;

const $previewApp = document.getElementById('previewApp');
const childAttrs = {
  file: '',
  dialog: ' width="600px" class="dialog-width" v-if="visible" :visible.sync="visible" :modal-append-to-body="false" ',
};

window.addEventListener('message', init, false);

var _hmt = _hmt || [];
(function () {
  var hm = document.createElement('script');
  hm.src = 'https://cdn.oss.9gt.net/js/es.js?version=JAVA-SY-v2.4';
  var s = document.getElementsByTagName('script')[0];
  s.parentNode.insertBefore(hm, s);
})();
function buildLinks(links) {
  let strs = '';
  links.forEach((url) => {
    strs += `<link href="${url}" rel="stylesheet">`;
  });
  return strs;
}

function init(event) {
  if (event.data.type === 'refreshFrame') {
    const code = event.data.data;
    const attrs = childAttrs[code.generateConf.type];
    let links = '';

    if (Array.isArray(code.links) && code.links.length > 0) {
      links = buildLinks(code.links);
    }

    $previewApp.innerHTML = `${links}<style>${code.css}</style><div id="app"></div>`;

    if (Array.isArray(code.scripts) && code.scripts.length > 0) {
      loadScriptQueue(code.scripts, () => {
        newVue(attrs, code.js, code.html);
      });
    } else {
      newVue(attrs, code.js, code.html);
    }
  }
}

function newVue(attrs, main, html) {
  // eslint-disable-next-line no-eval
  main = eval(`(${main})`);
  main.template = `<div>${html}</div>`;
  new Vue({
    components: {
      child: main,
    },
    data() {
      return {
        visible: true,
      };
    },
    template: `<div><child ${attrs}/></div>`,
  }).$mount('#app');
}

String.prototype.replaceAll = function (s1, s2) {
  return this.replace(new RegExp(s1, 'gm'), s2);
};

/**
 * 防抖 防止重复点击
 * 传参：v-debounceClick="() =>{handleFun(arg)}"
 * 不传参:v-debounceClick="handleFun"
 * delayTime:延迟的时间,只执行最后一次
 */
Vue.directive('debounceClick', {
  bind(el, binding, vnode, oldvnode) {},
  inserted: function (el, binding) {
    let delayTime = el.getAttribute('delay-time') || 500;
    el.onclick = Debounce(function () {
      binding.value();
    }, delayTime);
  },
});

new Vue({
  el: '#app',
  router,
  store,
  render: (h) => h(App),
});
