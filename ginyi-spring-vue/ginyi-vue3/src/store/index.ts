import type {App} from 'vue';
import {createPinia} from 'pinia';
import {piniaPlugin} from "@/plugins/pinia/piniaPlugin";

const store = createPinia();
store.use(piniaPlugin())

export const initStore = (app: App<Element>) => {
    app.use(store);
}

export {store};