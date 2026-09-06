import user from '@/store/modules/user'
import getters from './getters'
import { createStore } from "vuex";
const store = createStore({
  modules: {
    user,
  },
  getters
});
export default store;