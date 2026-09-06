import modules from "./modules/index";
import getters from "./getters";
import { createStore } from "vuex";

export interface AppState {
  token: string | null;
  forumToken: string | null;
  isLogin: boolean;
  userInfo: Record<string, any>;
  currentIndex: number;
  enterprise: Record<string, any>;
  uid: number;
  loginBackUrl: string;
  homeActive: boolean;
  phoneStatus: boolean;
  depSelectPeople: Array<any>;
  isProcess: boolean;
  depSelectIds: Array<number>;
  depCheckIds: Array<number>;
  isOrgShow: boolean;
  frameTree: Record<string, any>;
  selectFrameTree: Record<string, any>;
  enterpriseAuth: Record<string, any>;
  treeSelectBeforePage: string;
  customerFormType: Record<string, any>;
  codeSuccessKey: string;
  menus: Array<Record<string, any>>;
  oneOnOneData: Record<string, any>;
  isNoticeJumpPage: boolean;
  depSelectIndex: number;
  depUserIndex: number;
  selectCustomUsers: Array<Record<string, any>>;
  backgroundColor: string;
};

export interface State {
  app: AppState;
}

const store = createStore<State>({
  modules,
  getters,
});

export default store;
