import {createApp} from 'vue'
import App from '@/App.vue'
import router from "@/router";
import naive from 'naive-ui';
import {initStore} from '@/store';
import "@/plugins/naive-ui/common"
import "@/config/console.log";
import {initDirectives} from "@/directives";
import {initIcon} from "@/plugins/naive-ui/common";
import "@/style/index.css"

const app = createApp(App)
initStore(app)
initDirectives(app)
initIcon(app)

app.use(router)
app.use(naive)
app.mount('#app')
