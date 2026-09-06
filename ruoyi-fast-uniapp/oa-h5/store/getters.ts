import { GetterTree } from "vuex";
import type { State } from "./index";

const getters: GetterTree<State, any> = {
  token: (state: State) => state.app.token,
  isLogin: (state: State) => !!state.app.token,
  backgroundColor: (state: State) => state.app.backgroundColor,
};

export default getters;
