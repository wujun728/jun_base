import { createApp } from "vue";
import App from "./App.vue";

import "element-plus/theme-chalk/dark/css-vars.css";
import "@/styles/index.scss";
import "uno.css";
import "animate.css";
import "virtual:svg-icons-register";

import { setupDirective } from "@/directives";
import { setupRouter } from "@/router";
import { setupStore } from "@/stores";
import { setupI18n } from "@/lang";
import * as ElementPlusIcons from "@element-plus/icons-vue";
import { setupPermissionGuard } from "@/router/guards/permission";

// 若依兼容：全局插件
import modal from "@/plugins/modal";
import cache from "@/plugins/cache";
import auth from "@/plugins/auth";
import tab from "@/plugins/tab";
import pluginDownload from "@/plugins/download";
// 若依兼容：全局方法
import { useDict } from "@/utils/dict";
import { download } from "@/utils/request";
import {
  parseTime,
  resetForm,
  handleTree,
  addDateRange,
  selectDictLabel,
  selectDictLabels,
} from "@/utils/ruoyi";
import { getConfigKey } from "@/api/system/config";
// 若依兼容：全局组件
import DictTag from "@/components/DictTag";
import Pagination from "@/components/Pagination";
import RightToolbar from "@/components/RightToolbar";
import SvgIcon from "@/components/SvgIcon";

const app = createApp(App);

// 全局插件/方法挂载（若依页面通过 proxy.$xxx / proxy.xxx 调用）
app.config.globalProperties.$modal = modal;
app.config.globalProperties.$cache = cache;
app.config.globalProperties.$auth = auth;
app.config.globalProperties.$tab = tab;
app.config.globalProperties.$download = pluginDownload;
app.config.globalProperties.$prompt = modal.prompt;
app.config.globalProperties.useDict = useDict;
app.config.globalProperties.download = download;
app.config.globalProperties.parseTime = parseTime;
app.config.globalProperties.resetForm = resetForm;
app.config.globalProperties.handleTree = handleTree;
app.config.globalProperties.addDateRange = addDateRange;
app.config.globalProperties.getConfigKey = getConfigKey;
app.config.globalProperties.selectDictLabel = selectDictLabel;
app.config.globalProperties.selectDictLabels = selectDictLabels;

// 全局组件挂载
app.component("DictTag", DictTag);
app.component("Pagination", Pagination);
app.component("RightToolbar", RightToolbar);
app.component("svg-icon", SvgIcon);

setupDirective(app);
setupI18n(app);
setupRouter(app);
setupStore(app);

Object.entries(ElementPlusIcons).forEach(([name, comp]) => app.component(name, comp));

setupPermissionGuard();

app.mount("#app");